package com.lostandfoundapp.BE;
import com.google.firebase.database.Exclude;

public class Images {
    private String mName;
    private String mImageUrl;
    private String mKey;

    public Images() {
        //empty constructor needed
    }

    public Images(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "Navn ikke angivet";
        }

        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
