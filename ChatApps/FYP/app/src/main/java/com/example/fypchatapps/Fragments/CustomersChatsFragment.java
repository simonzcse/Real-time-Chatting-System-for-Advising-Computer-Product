package com.example.fypchatapps.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fypchatapps.Adapter.Customer2StaffAdapter;
import com.example.fypchatapps.Adapter.UserAdapter;
import com.example.fypchatapps.Model.Chat;
import com.example.fypchatapps.Model.Chatlist;
import com.example.fypchatapps.Model.Customer;
import com.example.fypchatapps.Model.Staff;
import com.example.fypchatapps.Notifications.Token;
import com.example.fypchatapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


public class CustomersChatsFragment extends Fragment {
    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<Staff> mStaff;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private List<Chatlist> userList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_customers_chats, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("chatlist").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chatlist chatlist = dataSnapshot.getValue(Chatlist.class);
                    userList.add(chatlist);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        updateToken(FirebaseInstanceId.getInstance().getToken());//v18
        return view;
    }
    //v18
    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }


    private void chatList(){
        mStaff = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("customer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mStaff.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    Customer2StaffAdapter customer2StaffAdapter = new Customer2StaffAdapter(customer);
                    Staff staff = (Staff)customer2StaffAdapter;
                    for (Chatlist chatlist:userList){
                        assert staff != null;
                        if (staff.getStaff_id().equals(chatlist.getId())){
                            mStaff.add(staff);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), mStaff, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}