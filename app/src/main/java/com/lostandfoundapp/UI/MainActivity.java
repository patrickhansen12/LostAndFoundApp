package com.lostandfoundapp.UI;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.lostandfoundapp.BLL.ImageAdapter;
import com.lostandfoundapp.BE.Images;
import com.lostandfoundapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Images> mImages;
    public String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mImages = new ArrayList<>();

        mAdapter = new ImageAdapter(MainActivity.this, mImages);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(MainActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                mImages.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Images images = postSnapshot.getValue(Images.class);
                    images.setKey(postSnapshot.getKey());
                    mImages.add(images);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    //Creates the OptionsMenu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        switch (menuId) {
            //Opens the call service if there is more than one available makes you able to choose which one
            case R.id.phoneButton:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + 31704479));
                startActivity(Intent.createChooser(intent, ""));
                break;
                //Opens the phones message app and sets in the predefined text if the user haven't clicked on an item it will tell them that they need to pick one before they can send the school a message
            case R.id.smsButton:
                if(name != null){
                    Uri uri = Uri.parse("smsto:31704479");
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", "Hej jeg kontakter skolen angående " + name + ", da jeg mener det er min"+ " venlig hilsen  " );
                    startActivity(it);
                }else{
                    Toast.makeText(this, "Du skal klikke på det billede du mener er din ting, før at du kan sende en sms til skolen" , Toast.LENGTH_SHORT).show();
                }

                break;
                //opens the LoginActivity
            case R.id.loginButton:
                Intent x = new Intent();
                x.setClass(MainActivity.this, LoginActivity.class);
                startActivity(x);
                break;
                //Opens the Email service if the user have clicked on a item otherwise it will tell they need to click on one
            case R.id.emailButton:
                if(name != null){
                    sendEmail();
                }

                else {
                Toast.makeText(this, "Du skal klikke på det billede du mener er din ting, før at du kan sende en Email til skolen"  , Toast.LENGTH_SHORT).show();
            }

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    //gives the user the name on the item that they clicked on and sets the String name to t
    public void onItemClick(int position) {
        Images selectedItem = mImages.get(position);
        name = selectedItem.getName();
        Toast.makeText(this, "Du har klikket på: " + name, Toast.LENGTH_SHORT).show();
    }




//opens a new intent which allows the user to pick a installed message service from the phone (gmaps,messenger)
    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"HergårdskolenEsbjerg@mail.com"};
        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Glemt ting " + name);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hej jeg kontakter skolen angående " + name + ", da jeg mener det er min venlig hilsen");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();

        }
    }

}
