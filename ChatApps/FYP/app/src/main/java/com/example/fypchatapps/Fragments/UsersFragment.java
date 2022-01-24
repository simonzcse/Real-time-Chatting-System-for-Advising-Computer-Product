package com.example.fypchatapps.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.fypchatapps.Adapter.Customer2StaffAdapter;
import com.example.fypchatapps.Adapter.UserAdapter;
import com.example.fypchatapps.Model.Staff;
import com.example.fypchatapps.Model.Customer;
import com.example.fypchatapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<Staff> mStaff;

    EditText search_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        mStaff = new ArrayList<>();
        readUser();

        search_user = view.findViewById(R.id.search_user);
        search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }
    private void searchUser(String s){
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("staff").orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mStaff.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Staff staff = snapshot.getValue(Staff.class);
                    assert staff != null;
                    assert fuser != null;
                    if(!staff.getStaff_id().equals(fuser.getUid())){//check the user is or not current user
                        mStaff.add(staff);
                    }
                }
                userAdapter = new UserAdapter(getContext(), mStaff, false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readUser() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("staff");//load Staff User
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((search_user.getText().toString().equals(""))){
                        mStaff.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Staff staff = snapshot.getValue(Staff.class);
                        assert staff != null;
                        assert firebaseUser != null;
                        if (!staff.getStaff_id().equals(firebaseUser.getUid())) {//check the user is or not current user
                            mStaff.add(staff);
                        }
                    }
                    userAdapter = new UserAdapter(getContext(), mStaff, false);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}