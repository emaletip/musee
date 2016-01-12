package com.example.xo7.lemusedelafaunedasie;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by xo7 on 11/01/16.
 */
public class Animal {
    public Context mContext;
    private String name;
    private String description;
    private String image;
    private ArrayList<Drawable> imageList = new ArrayList<Drawable>();

    public Animal(String name, String description, String image, Context mContext) {
        this.name = name;
        this.description = description;
        this.mContext = mContext;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Drawable getImage() {
        int resID = mContext.getResources().getIdentifier(image , "drawable", mContext.getPackageName());
        Drawable drawableImage = mContext.getResources().getDrawable(resID);
        return drawableImage;
    }

    public String getImagePath() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void addImage(Drawable image) {
         this.imageList.add(image);
    }

    public ArrayList<Drawable> getImageList() {
        return imageList;
    }

}
