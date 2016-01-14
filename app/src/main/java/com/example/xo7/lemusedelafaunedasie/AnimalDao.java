package com.example.xo7.lemusedelafaunedasie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AnimalDao {

    public Context mContext;
    // Champs de la base de données
    private SQLiteDatabase database;
    private DatabaseHandler dbHelper;
    private String[] allColumns = {
        DatabaseHandler.COLUMN_ID,
        DatabaseHandler.COLUMN_NAME,
        DatabaseHandler.COLUMN_DESCRIPTION,
        DatabaseHandler.COLUMN_CATEGORY,
        DatabaseHandler.COLUMN_IMAGE,
        DatabaseHandler.COLUMN_LATITUDE,
        DatabaseHandler.COLUMN_LONGITUDE,
        DatabaseHandler.COLUMN_PROPERTIES
};

    public AnimalDao(Context context) {
        dbHelper = new DatabaseHandler(context);
        mContext = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void emptyTable() {
        database.execSQL("DELETE FROM animal;" +
                "VACUUM;");
    }


    public Animal createAnimal(String name, String description, String category,  String image, String latitude, String longitude, String properties) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COLUMN_NAME, name);
        values.put(DatabaseHandler.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHandler.COLUMN_CATEGORY, category);
        values.put(DatabaseHandler.COLUMN_IMAGE, image);
        values.put(DatabaseHandler.COLUMN_LATITUDE, latitude);
        values.put(DatabaseHandler.COLUMN_LONGITUDE, longitude);
        values.put(DatabaseHandler.COLUMN_PROPERTIES, properties);

        long insertId = database.insert(DatabaseHandler.TABLE_ANIMAL, null,
                values);
        Cursor cursor = database.query(DatabaseHandler.TABLE_ANIMAL,
                allColumns, DatabaseHandler.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Animal newAnimal = cursorToAnimal(cursor);
        cursor.close();
        return newAnimal;
    }

    public void deleteAnimal(Animal animal) {
        long id = animal.getId();
        //System.out.println("Comment deleted with id: " + id);
        database.delete(DatabaseHandler.TABLE_ANIMAL, DatabaseHandler.COLUMN_ID
                + " = " + id, null);
    }

    public Animal getAnimal(int id) {
        Cursor cursor =  database.rawQuery("select * from " + DatabaseHandler.TABLE_ANIMAL + " where " + DatabaseHandler.COLUMN_ID + "='" + id + "'" , null);

        cursor.moveToFirst();
        Animal animal = cursorToAnimal(cursor);
        cursor.close();
        return animal;
    }

    public ArrayList<Animal> getAllAnimals() {
        ArrayList<Animal> animals = new ArrayList<Animal>();

        Cursor cursor = database.query(DatabaseHandler.TABLE_ANIMAL,
                allColumns, null, null, null, null, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Animal animal = cursorToAnimal(cursor);

            animals.add(animal);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return animals;
    }

    public ArrayList<Animal> getAnimalsByCategory(String cat) {
        ArrayList<Animal> animals = new ArrayList<Animal>();

        Cursor cursor = database.query(DatabaseHandler.TABLE_ANIMAL,
                allColumns, null, null, null, null, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(cursor.getString(cursor.getColumnIndex("category")).equals(cat))  {
                Animal animal = cursorToAnimal(cursor);
                animals.add(animal);
            }

            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return animals;
    }

    private Animal cursorToAnimal(Cursor cursor) {
        Animal animal = new Animal(mContext , cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("description")), cursor.getString(cursor.getColumnIndex("image")), cursor.getString(cursor.getColumnIndex("category")), cursor.getString(cursor.getColumnIndex("latitude")), cursor.getString(cursor.getColumnIndex("longitude")), cursor.getString(cursor.getColumnIndex("properties")));
        return animal;
    }
}