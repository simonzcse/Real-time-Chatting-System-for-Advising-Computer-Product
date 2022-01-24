package com.example.fypchatapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {
    Button login, register;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        //check if user is null, for auto login
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!= null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            Log.d("StartActivity","Auto Login Successfully");
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // For new user
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
            }
        });
    }
}