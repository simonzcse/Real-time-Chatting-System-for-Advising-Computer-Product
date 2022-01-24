package com.example.fypchatapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fypchatapps.Adapter.Customer2StaffAdapter;
import com.example.fypchatapps.Adapter.MessageAdapter;
import com.example.fypchatapps.Adapter.UserAdapter;
import com.example.fypchatapps.Fragments.APIService;
import com.example.fypchatapps.Fragments.ProfileFragment;
import com.example.fypchatapps.Model.Chat;
import com.example.fypchatapps.Model.Customer;
import com.example.fypchatapps.Model.Staff;
import com.example.fypchatapps.Notifications.Client;
import com.example.fypchatapps.Notifications.Data;
import com.example.fypchatapps.Notifications.MyResponse;
import com.example.fypchatapps.Notifications.Sender;
import com.example.fypchatapps.Notifications.Token;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

CircleImageView profile_image;
TextView username;

FirebaseUser firebaseUser;

DatabaseReference reference, customer_reference;

ImageButton btn_send, btn_attachment, btn_call;
EditText text_send;

MessageAdapter messageAdapter;
List<Chat> mChats;

RecyclerView recyclerView;

Staff staff;
String userid;
Intent intent;
String role;

APIService apiService;

String mobile;

private static final int PERMISSIONS_REQUEST_CALL_PHONE = 999;

StorageReference storageReference ,storageReference1;
private static final int IMAGE_REQUEST = 1;
private Uri imageUri;
private StorageTask uploadTask;

boolean notify = false;

ValueEventListener seenListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        storageReference = FirebaseStorage.getInstance().getReference("chat_image");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_call = findViewById(R.id.btn_call);
        btn_attachment = findViewById(R.id.btn_attachment);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent= getIntent();

        userid = intent.getStringExtra("userid");//
        String username1 = intent.getStringExtra("username");//
        role = intent.getStringExtra("role");//
        mobile = intent.getStringExtra("mobile");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message = text_send.getText().toString();
                if (!message.equals("")){
                    sendMessage(firebaseUser.getUid(), userid, message);
                }
                else {
                    Toast.makeText(MessageActivity.this,"You can't send empty message!", Toast.LENGTH_SHORT).show();
                    Log.i("MessageActivity","Send Message Unsuccessfully");
                }
                text_send.setText("");
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(mobile);
            }
        });

        btn_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("staff").child(userid);//
        customer_reference = FirebaseDatabase.getInstance().getReference("customer").child(userid);//
        if (username1.indexOf("(Customer)")==0){
            customer_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Customer customer = snapshot.getValue(Customer.class);
                    Customer2StaffAdapter customer2StaffAdapter = new Customer2StaffAdapter(customer);
                    staff = (Staff)customer2StaffAdapter;
                    username.setText(staff.getUsername());
                    if (staff.getImageURL().equals("default")) {
                        profile_image.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(getApplicationContext()).load(staff.getImageURL()).into(profile_image);

                    }
                    readMessages(firebaseUser.getUid(), userid, staff.getImageURL());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    staff = snapshot.getValue(Staff.class);
                    username.setText(staff.getUsername());
                    if (staff.getImageURL().equals("default")) {
                        profile_image.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(getApplicationContext()).load(staff.getImageURL()).into(profile_image);

                    }
                    readMessages(firebaseUser.getUid(), userid, staff.getImageURL());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        seenMessage(userid);
    }

    private void seenMessage(String userid){
        reference = FirebaseDatabase.getInstance().getReference("chat");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(firebaseUser.getUid())&&chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("text",message);
        hashMap.put("isseen", false);
        hashMap.put("dateTime", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()));

        reference.child("chat").push().setValue(hashMap);

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chatlist").child(firebaseUser.getUid()).child(receiver);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("chatlist").child(receiver).child(firebaseUser.getUid());
        chatRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                if (!snapshot1.exists()) {
                    chatRef2.child("id").setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final String msg = message;

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("staff").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Staff staff = snapshot.getValue(Staff.class);
                    if (notify) {
                        assert staff != null;
                        sendNotification(receiver, staff.getUsername(), msg);
                    }
                    notify = false;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    private void sendNotification(String receiver, String username, String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Token token = dataSnapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.mipmap.ic_launcher, username+": "+message, "New Message", userid);
                    assert token != null;
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code()==200){
                                        if(response.body().success!=1){
                                            Toast.makeText(MessageActivity.this,"Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void readMessages(String selUid, String senderUid, String imgURL){
        mChats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChats.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        assert chat != null;
                        if ((chat.getReceiver().equals(selUid)&&chat.getSender().equals(senderUid)) ||
                                (chat.getReceiver().equals(senderUid)&&chat.getSender().equals(selUid))){
                            mChats.add(chat);
                        }
                        messageAdapter = new MessageAdapter(MessageActivity.this, mChats, imgURL);
                        recyclerView.setAdapter(messageAdapter);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();

    }

    private void status(String status){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("staff").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userid);
    }
    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }
    public void openImage(){
        Intent intent = new Intent();
        intent.setType("image/* video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(this,"Upload in progress", Toast.LENGTH_SHORT).show();
            }else {
                uploadImage();
            }
        }
    }
    private  void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri!= null){
             StorageReference fileReference = storageReference.child(firebaseUser.getUid()+"_"+System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        sendMessage(firebaseUser.getUid(), userid, mUri);
                        pd.dismiss();
                    }else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    public void makeCall(String mobile){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mobile));
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED){
            startActivity(intent);
        } else {
            requestForCallPermission();
        }
    }

    private void requestForCallPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){}
        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall(mobile);
                }else {Toast.makeText(this, "No Permission To CALL", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
