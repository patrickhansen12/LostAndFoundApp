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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lostandfoundapp.BLL.PictureAdapter;
import com.lostandfoundapp.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Created by Nicolai on 03-05-2018.
 */

public class AddActivity extends AppCompatActivity {
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
private FirebaseStorage storage = FirebaseStorage.getInstance();
    PictureAdapter pictureAdapter;

    Button PictureButton, SaveButton, BackButton;
   View imageContainer;
    ImageView Picture;
    EditText NameText;
    Spinner Dropdown;
    Bitmap itemPicture;
 private StorageReference mStorage;
 private ProgressDialog mProgress;
 TextView overlayText;
 private TextView downloadedUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mStorage = FirebaseStorage.getInstance().getReference();
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
        PictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
openPictureActivity();

            }
        });

    }


    public void openPictureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    setTakenPicture(data);
                    break;
            }
        }
           // mProgress.setMessage("Uploading Image ....");
            //mProgress.show();

            Picture.setDrawingCacheEnabled(true);
            Picture.buildDrawingCache();
            Bitmap bitmap = Picture.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            Picture.setDrawingCacheEnabled(false);
            byte[] data1 = baos.toByteArray();
            String path = "items/" + UUID.randomUUID() + ".png";
            StorageReference itemsRef = storage.getReference(path);

            StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("text", NameText.getText().toString()).build();
            UploadTask uploadTask = itemsRef.putBytes(data1, metadata);
            uploadTask.addOnSuccessListener(AddActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri url = taskSnapshot.getDownloadUrl();
                    downloadedUrl.setText(url.toString());
                    downloadedUrl.setVisibility(View.VISIBLE);
                }

    });
            super.onActivityResult(requestCode, resultCode, data);
}

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






