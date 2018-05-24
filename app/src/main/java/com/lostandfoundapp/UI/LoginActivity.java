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
    Button Back, Login, signUpBtn,deleteItemsBtn,addItemsBtn;
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
        deleteItemsBtn = findViewById(R.id.deleteItemsBtn);
        addItemsBtn = findViewById(R.id.addItemsBtn);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        //Logs in
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                new User(Username.getText().toString(), Password.getText().toString());
              signIn(Username.getText().toString(), Password.getText().toString());
            }
        });

        //Sends you to MainActivity
        Back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent x = new Intent();
                finish();
                x.setClass(LoginActivity.this, MainActivity.class);
                startActivity(x);

            }
        });

        //Sends you to ImagesActivity
        deleteItemsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent x = new Intent();
                finish();
                x.setClass(LoginActivity.this, ImagesActivity.class);
                startActivity(x);
            }
        });

        //Sends you to StaffActivity
        addItemsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent x = new Intent();
                finish();
                x.setClass(LoginActivity.this, StaffActivity.class);
                startActivity(x);

            }
        });

        //Adds new users based on information written in username and password fields
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
    }

    //Logs the user in if the correct username and password is inputted
    private void signIn(final String username,final String password) {

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(username);
                if (dataSnapshot.child(username).exists()){
                    if(!username.isEmpty()){

                        User login = dataSnapshot.child(username).getValue(User.class);
                        System.out.println(login);
        if(login.getM_password().equals(password)) {
            deleteItemsBtn.setVisibility(View.VISIBLE);
            addItemsBtn.setVisibility(View.VISIBLE);
            signUpBtn.setVisibility(View.VISIBLE);

            Toast.makeText(LoginActivity.this, "you are now logged in",Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(LoginActivity.this, "Wrong Password",Toast.LENGTH_SHORT).show();
        }
                    }
        }else{
                        Toast.makeText(LoginActivity.this, "Username is not Registered", Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}




