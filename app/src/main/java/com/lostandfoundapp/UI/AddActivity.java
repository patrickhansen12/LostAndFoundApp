package com.lostandfoundapp.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.lostandfoundapp.BLL.PictureAdapter;
import com.lostandfoundapp.R;

/**
 * Created by Nicolai on 03-05-2018.
 */

public class AddActivity extends AppCompatActivity {
    PictureAdapter pictureAdapter;

    Button PictureButton, SaveButton, BackButton;
    ImageView Picture;
    EditText NameText;
    Spinner Dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        PictureButton = findViewById(R.id.pictureButton);
        SaveButton = findViewById(R.id.saveButton);
        BackButton = findViewById(R.id.backButton);
        Picture = findViewById(R.id.picture);
        NameText = findViewById(R.id.nameText);
        Dropdown = findViewById(R.id.dropdown);

        PictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                {
                    pictureAdapter.openPictureActivity();
                }
            }
        });
    }

}

