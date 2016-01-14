package com.example.xo7.lemusedelafaunedasie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class AnimalListActivity extends Activity {

    private ListView animalListView;
    private ArrayList<Animal> animalList = new ArrayList<Animal>();
    private AnimalAdapter animalAdapter;
    private AnimalAsyncTask animalAsyncTask;
    private String category;
    private TextView categoryText;
    private AnimalDao datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);

        this.category = getIntent().getStringExtra("category");

        animalListView = (ListView) findViewById(R.id.animal_listview);
        categoryText = (TextView) findViewById(R.id.animal_name);

        categoryText.setText(this.category.toUpperCase());

        switch (this.category) {
            case "carnivore":
                categoryText.setBackgroundResource(R.color.carnivore);
                break;
            case "herbivore":
                categoryText.setBackgroundResource(R.color.herbivore);
                break;
            case "other":
                categoryText.setBackgroundResource(R.color.other);
                break;
            case "omnivore":
                categoryText.setBackgroundResource(R.color.omnivore);
                break;
        }

        //animalAsyncTask = new AnimalAsyncTask(this, this.category);
        //animalAsyncTask.execute();

        datasource = new AnimalDao(this);
        datasource.open();
        animalList = datasource.getAnimalsByCategory(this.category);

        animalAdapter = new AnimalAdapter(this, animalList);
        animalListView.setAdapter(animalAdapter);
        animalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Animal animal = (Animal) animalListView.getAdapter().getItem(position);
                Intent n = new Intent(getApplicationContext(), AnimalViewActivity.class);
                n.putExtra("animal_id", animal.getId());

                startActivity(n);
            }
        });
    }

    public void print() {
        animalAdapter.notifyDataSetChanged();
    }

    public class AnimalAsyncTask extends AsyncTask {

        private ArrayList<Animal> animalListTmp = new ArrayList<Animal>();
        public Context mContext;
        public String category;

        public AnimalAsyncTask (Context context, String cat){
            mContext = context;
            category = cat;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            InputStreamReader inputStreamReader;
            try {
                inputStreamReader = new InputStreamReader(getResources().openRawResource(R.raw.animals));
                Scanner inputStream = new Scanner(inputStreamReader);

                while (inputStream.hasNext()) {
                    String data = inputStream.nextLine(); // Gets a whole line
                    String[] line = data.split(";"); // Splits the line up into a string array

                    if (line.length >= 7 && line[4].equals(this.category)) {
                        // Do stuff, e.g:
                        animalListTmp.add(new Animal(this.mContext, Integer.parseInt(line[0]), line[1], line[2], line[3], line[4], line[5], line[6], line[7]));
                    }
                }
                inputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // afficher resultats
            animalList.clear();
            animalList.addAll(animalListTmp);

            print();
        }
    }
}
