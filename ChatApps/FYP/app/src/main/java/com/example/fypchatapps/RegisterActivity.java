package com.example.fypchatapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username, email_id, password, first_name, last_name, role;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference, reference_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        email_id = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        role = findViewById(R.id.role);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email_id = email_id.getText().toString();
                String txt_password = password.getText().toString();
                String txt_first_name = first_name.getText().toString();
                String txt_last_name = last_name.getText().toString();
                String txt_role = role.getText().toString();

                if(TextUtils.isEmpty(txt_username)||TextUtils.isEmpty(txt_email_id)||TextUtils.isEmpty(txt_password)||TextUtils.isEmpty(txt_first_name)||TextUtils.isEmpty(txt_last_name)||TextUtils.isEmpty(txt_role)){//check empty
                    Toast.makeText(RegisterActivity.this,"All field are required", Toast.LENGTH_SHORT).show();
                }else if (txt_password.length()<6){
                    Toast.makeText(RegisterActivity.this,"Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("RegisterActivity","Start register");
                    register(txt_username, txt_email_id, txt_password, txt_first_name, txt_last_name, txt_role);
                }
            }
        });

    }

    private void register(String username, String email, String password, String first_name, String last_name, String role){

    auth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("staff").child(userid);//table name
                    reference_address = FirebaseDatabase.getInstance().getReference("staff").child(userid+"/address");//database/table/sub table

                    HashMap<String, String> hashMap= new HashMap<>();//asso array
                    hashMap.put("staff_id", userid);
                    hashMap.put("email_id", email);
                    hashMap.put("username", username);
                    hashMap.put("first_name", first_name);
                    hashMap.put("last_name", last_name);
                    hashMap.put("role", role);
                    hashMap.put("password", password);
                    hashMap.put("imageURL", "default");
                    hashMap.put("mobile", "24630066");
                    hashMap.put("status", "offline");

                    HashMap<String, String> hashMap_address= new HashMap<>();
                    hashMap_address.put("city", "NT");
                    hashMap_address.put("country", "Hong Kong");
                    hashMap_address.put("road", "18 Tsing Wun Rd, Tuen Mun");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {//add into table
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                reference_address.setValue(hashMap_address);//add into subtable
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Log.d("RegisterActivity","register Successfully");
                                finish();
                            }
                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this,"Sorry You can not register with this email or password", Toast.LENGTH_SHORT).show();
                    Log.i("RegisterActivity","register Unsuccessfully");
                }
            }
        });

    }

}