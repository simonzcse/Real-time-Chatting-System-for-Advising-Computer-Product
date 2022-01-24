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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText  email_id, password;
    Button btn_login;

    FirebaseAuth auth;
    TextView forget_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        email_id = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        forget_password = findViewById(R.id.forget_password);

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email_id = email_id.getText().toString();
                String txt_password = password.getText().toString();

                if(TextUtils.isEmpty(txt_email_id)||TextUtils.isEmpty(txt_password)){//check empty
                    Toast.makeText(LoginActivity.this,"All field are required", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(txt_email_id, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {//send email and password to firebase auth api
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                Log.d("LoginActivity","Login Successfully");
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this,"Authentication failed!", Toast.LENGTH_SHORT).show();
                                Log.i("LoginActivity","Login Unsuccessfully");
                            }
                        }
                    });
                }
            }
        });

    }
}