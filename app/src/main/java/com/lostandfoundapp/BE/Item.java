package com.lostandfoundapp.BE;

import android.graphics.Bitmap;

public class Item
{
    int m_id;
    String m_name;
    String m_Category;
    Bitmap m_picture;

    public Item(int  id, String name, String category, Bitmap picture)
    {
        m_id = id;
        m_name = name;
        m_Category = category;
        m_picture = picture;
    }
}
