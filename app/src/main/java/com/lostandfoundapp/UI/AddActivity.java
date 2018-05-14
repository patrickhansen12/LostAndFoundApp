package com.lostandfoundapp.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lostandfoundapp.BLL.PictureAdapter;
import com.lostandfoundapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Nicolai on 03-05-2018.
 */

public class AddActivity extends AppCompatActivity {
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri filePath;
    File mFile;
    private final int PICK_IMAGE_REQUEST = 71;
    PictureAdapter pictureAdapter;

    Button PictureButton, SaveButton, BackButton;
   View imageContainer;
    ImageView Picture;
    EditText NameText;
    Spinner Dropdown;
    Bitmap itemPicture;
    FirebaseStorage storage;
    StorageReference storageReference;
 private ProgressDialog mProgress;
 TextView overlayText;
 private TextView downloadedUrl;
 private final static String LOGTAG = "Camera01";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        imageContainer = findViewById(R.id.image_container);
        overlayText = (TextView) findViewById(R.id.overlayText);
        setContentView(R.layout.activity_add);
        PictureButton = findViewById(R.id.pictureButton);
        SaveButton = findViewById(R.id.saveButton);
        BackButton = findViewById(R.id.backButton);
        Picture = findViewById(R.id.picture);
        NameText = findViewById(R.id.nameText);
        Dropdown = findViewById(R.id.dropdown);
mProgress = new ProgressDialog(this);
        downloadedUrl = (TextView) findViewById(R.id.download_url);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final String[] catagories = new String[]{"Alt", "Tøj", "Sko", "Smykker", "Elektroni", "Diverse"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, catagories);
        Dropdown.setAdapter(adapter);

        PictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            openPictureActivity();
            }
        });
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showPictureTaken(mFile);
              Bitmap mFile = (Bitmap) data.getExtras().get("data");
                String test = "1";
                chooseImage();

                    filePath = data.getData();


            } else
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show();
                return;

            } else
                Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void openPictureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    private void onClickTakePics()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        mFile = getOutputMediaFile(); // create a file to save the image
        if (mFile == null)
        {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show();
            return;
        }
        // create Intent to take a picture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));



        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }


    /** Create a File for saving an image */
    private File getOutputMediaFile(){


        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "LostAndFoundPictures12");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String postfix = "jpg";
        String prefix = "IMG";

        File mediaFile = new File(mediaStorageDir.getPath() +
                File.separator + prefix +
                "_"+ timeStamp + "." + postfix);

        return mediaFile;
    }



    private void showPictureTaken(File f) {

        //Picture.setImageURI(Uri.fromFile(f));
        //Picture.setBackgroundColor(Color.RED);
        //Picture.setRotation(90);
        scaleImage();
    }


    private void scaleImage()
    {
        final Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        final float screenWidth = p.x/2;
        final float screenHeight = p.y/2; //m_takeBtn.getHeight());
        //Picture.setMaxHeight((int)screenHeight);
        //Picture.setMaxWidth((int)screenWidth);
    }


}






