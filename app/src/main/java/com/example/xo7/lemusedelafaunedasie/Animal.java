package com.example.xo7.lemusedelafaunedasie;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import java.util.ArrayList;

/**
 * Created by xo7 on 11/01/16.
 */
public class Animal {

    public Context mContext;
    private int id;
    private String name;
    private String description;
    private String image;
    private String category;
    private String latitude;
    private String longitude;
    private String properties;

    public String getProperties() {
        return properties;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    private ArrayList<Drawable> imageList = new ArrayList<Drawable>();

    public Animal(Context mContext, int id, String name, String description, String image, String category,  String latitude, String longitude, String properties) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mContext = mContext;
        this.category = category;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.properties = properties;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        Drawable drawableImage = ResourcesCompat.getDrawable(mContext.getResources(), resID, null);
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
