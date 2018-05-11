package com.lostandfoundapp.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Nicolai on 03-05-2018.
 */

public class AddActivity extends AppCompatActivity {
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri filePath;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
//Firebase

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Picture.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void openPictureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    //public void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
          //  switch (resultCode) {
            //    case RESULT_OK:
               //     setTakenPicture(data);
                //    break;
           // }
        //}
         //   super.onActivityResult(requestCode, resultCode, data);
//}

    public void setTakenPicture(Intent data) {
        Bitmap picture = (Bitmap) data.getExtras().get("data");
        itemPicture = roundCropBitmap(picture);
        Picture.setImageBitmap(itemPicture);
    }

    public Bitmap roundCropBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    }






