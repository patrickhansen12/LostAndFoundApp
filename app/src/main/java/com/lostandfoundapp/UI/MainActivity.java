package com.lostandfoundapp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lostandfoundapp.R;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    TextView textItems;
    Button PhoneButton, SMSButton, LoginButton, GetImage;
    EditText Searchbar;
    Spinner DropDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      sendMessage();
      getData();
        textItems = findViewById(R.id.textItems);
        PhoneButton = findViewById(R.id.PhoneButton);
        SMSButton = findViewById(R.id.SMSButton);
        LoginButton = findViewById(R.id.LoginButton);
        GetImage = findViewById(R.id.getImage);
        Searchbar = findViewById(R.id.Searchbar);
        DropDown = findViewById(R.id.dropdown);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                {
                    Intent x = new Intent();
                    x.setClass(MainActivity.this, AddActivity.class);
                    startActivity(x);
                }
            }
        });


        SMSButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                {
                    Intent x = new Intent();
                    x.setClass(MainActivity.this, LoginActivity.class);
                    startActivity(x);
                }
            }
        });
    }
    public void sendMessage(){


        myRef.setValue("Hello, DATABASE!");
    }

    public void getData(){
        myRef.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                textItems.setText(value);



            }


            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }
}
