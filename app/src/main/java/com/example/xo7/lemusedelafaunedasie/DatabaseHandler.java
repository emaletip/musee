package com.example.xo7.lemusedelafaunedasie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String TABLE_ANIMAL = "animal";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_PROPERTIES = "properties";

    public static final String TABLE_FAVORIS = "favoris";
    public static final String COLUMN_ANIMAL_IS = "id_animal";


    private static final String DATABASE_NAME = "musee.db";
    private static final int DATABASE_VERSION = 1;

    public static final String ANIMAL_TABLE_CREATE =
            "CREATE TABLE animal (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(255), " +
                "description TEXT, " +
                "category VARCHAR(255), " +
                "image VARCHAR(255), " +
                "latitude VARCHAR(255), " +
                "longitude VARCHAR(255), " +
                "properties TEXT);";

    public static final String FAV_TABLE_CREATE =
            "CREATE TABLE favoris (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_animal INTEGER);";

    /* public static final String ANIMAL_TABLE_INSERT =
            "INSERT INTO animal " +
            "(name, description, category, image, latitude, longitude, properties) " +
            "VALUES ("+
            "\"test\", \"description\", \"herbivore\", \"animal_1_1\", \"45.1\", \"-45.1\", \"prop1:a@prop2:z\" " +
            ");";
    */

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ANIMAL_TABLE_CREATE);
        //db.execSQL(ANIMAL_TABLE_INSERT);
        db.execSQL(FAV_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
