package com.lostandfoundapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lostandfoundapp.BE.User;
import com.lostandfoundapp.R;

/**
 * Created by Nicolai on 03-05-2018.
 */

public class LoginActivity extends AppCompatActivity {
    Button Back, Login, signUpBtn;
    TextView FailText;
    EditText Username, Password;
FirebaseDatabase database;
DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_login);
        Back = findViewById(R.id.back);
        Login = findViewById(R.id.login);
        FailText = findViewById(R.id.failText);
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        signUpBtn = findViewById(R.id.signUpBtn);
database = FirebaseDatabase.getInstance();
users = database.getReference("Users");
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
                {
                    if (Username.getText().toString().equals("1") && Password.getText().toString().equals("1"))
                    {
                        Intent x = new Intent();
                        x.setClass(LoginActivity.this, AddActivity.class);
                        startActivity(x);
                    }
                    else
                    {
                        FailText.setText("Brugernavnet eller eller kodeordet er forkert");
                    }
                }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    signUpBtn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            final User user = new User(Username.getText().toString(),
                    Password.getText().toString());
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(user.getM_username()).exists())
                        Toast.makeText(LoginActivity.this, "that Username is already taken", Toast.LENGTH_SHORT).show();
                    else {
                        users.child(user.getM_username()).setValue(user);
                        Toast.makeText(LoginActivity.this, "New user", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }});
    }}



