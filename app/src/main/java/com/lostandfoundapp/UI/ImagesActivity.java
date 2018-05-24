package com.lostandfoundapp.UI;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lostandfoundapp.BLL.ImageAdapter;
import com.lostandfoundapp.BE.Images;
import com.lostandfoundapp.R;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private int selectedItemNumber  = -1;
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

        mAdapter = new ImageAdapter(ImagesActivity.this, mImages);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ImagesActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            //Updates the item list if a new item is added to firestorage
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
                Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    //Creates the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_images, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        switch (menuId) {

            //Deletes currently Selected Object
            case R.id.deleteBtn:
                if(selectedItemNumber == -1) {
                    Toast.makeText(ImagesActivity.this, "Du skal vælge en ting før at du kan slette den.", Toast.LENGTH_SHORT).show();
                 break;
                }else {
                    onDeleteClick(selectedItemNumber);
                    selectedItemNumber = -1;
                    break;
                }
            //Sends you to MainActivity
            case R.id.signOutBtn:
                Intent x = new Intent();
                finish();
                x.setClass(ImagesActivity.this, MainActivity.class);
                startActivity(x);
                break;
            default:
            case R.id.takePic:
                Intent t = new Intent();
                finish();
                t.setClass(ImagesActivity.this, StaffActivity.class);
                startActivity(t);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Selects an item when you click on it and notifies you
    @Override
    public void onItemClick(int position) {
        Images selectedItem = mImages.get(position);
        selectedItemNumber = position;
        System.out.println(selectedItemNumber);
        final String selectedKey = selectedItem.getKey();
        name = selectedItem.getName();
        Toast.makeText(this, "du har klikket på " + name, Toast.LENGTH_SHORT).show();
    }

    //Deletes Selected item
    public void onDeleteClick(int position) {
        Images selectedItem = mImages.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ImagesActivity.this, name + " er nu blevet slettet", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}