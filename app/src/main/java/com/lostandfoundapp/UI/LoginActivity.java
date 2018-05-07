package com.lostandfoundapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lostandfoundapp.R;

/**
 * Created by Nicolai on 03-05-2018.
 */

public class LoginActivity extends AppCompatActivity {
    Button Back, Login;
    TextView FailText;
    EditText Username, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_login);
        Back = findViewById(R.id.back);
        Login = findViewById(R.id.login);
        FailText = findViewById(R.id.failText);
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);

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
    }
}
