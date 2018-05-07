package com.lostandfoundapp.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    PictureAdapter pictureAdapter;

    Button PictureButton, SaveButton, BackButton;
    ImageView Picture;
    EditText NameText;
    Spinner Dropdown;
    Bitmap itemPicture;

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
                    openPictureActivity();
                }
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

