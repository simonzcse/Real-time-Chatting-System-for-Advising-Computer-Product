package com.example.fypchatapps.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fypchatapps.MainActivity;
import com.example.fypchatapps.MessageActivity;
import com.example.fypchatapps.Model.Chat;
import com.example.fypchatapps.Model.Staff;
import com.example.fypchatapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {
    @NonNull

    private Context mContext;
    private List<Staff> mStaffs;
    private boolean ischat;

    String theLastMessage;

    public UserAdapter(Context mContext, List<Staff>mStaffs , boolean ischat){
        this.mStaffs = mStaffs;
        this.mContext = mContext;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_itam, parent, false);
        return new UserAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Staff staff = mStaffs.get(position);
        holder.username.setText(staff.getUsername());
        if (staff.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(staff.getImageURL()).into(holder.profile_image);
        }

        if(ischat){
            lastMessage(staff.getStaff_id(), holder.last_msg);
        }else{
            holder.last_msg.setVisibility(View.GONE);
        }

        if(ischat){
            if (staff.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else{
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else{
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", staff.getStaff_id());//maybe change for staff and customer?
                intent.putExtra("username", staff.getUsername());
                intent.putExtra("role", staff.getRole());
                intent.putExtra("mobile", staff.getMobile());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStaffs.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        public ImageView img_on;
        public ImageView img_off;
        public TextView last_msg;

    public viewHolder(@NonNull View itemView) {
        super(itemView);

        username = itemView.findViewById(R.id.username);
        profile_image = itemView.findViewById(R.id.profile_image);

        img_on = itemView.findViewById(R.id.img_on);

        img_off = itemView.findViewById(R.id.img_off);

        last_msg = itemView.findViewById(R.id.last_msg);

    }
}
    private void lastMessage(String userid, TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid())&&chat.getSender().equals(userid)||
                            chat.getReceiver().equals(userid)&&chat.getSender().equals(firebaseUser.getUid())){
                        theLastMessage=chat.getText();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
