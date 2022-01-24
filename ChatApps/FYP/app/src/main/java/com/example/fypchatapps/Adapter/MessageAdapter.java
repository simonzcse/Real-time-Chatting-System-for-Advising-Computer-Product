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

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewHolder> {
    @NonNull
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChats;
    private String imageURL;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Chat> mChats, String imageURL){
        this.mChats = mChats;
        this.mContext = mContext;
        this.imageURL=imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_itam_right, parent, false);
            return new MessageAdapter.viewHolder(view);}
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.viewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.viewHolder holder, int position) {
        Chat chat = mChats.get(position);

        holder.show_message.setText(chat.getText());

        if (imageURL.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        }else{
            Glide.with(mContext).load(imageURL).into(holder.profile_image);
        }
        if(position == mChats.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            }else{
                holder.txt_seen.setText("Send");
            }
        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;

        public TextView txt_seen;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}