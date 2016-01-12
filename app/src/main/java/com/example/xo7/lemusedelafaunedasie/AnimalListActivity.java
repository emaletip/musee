package com.example.xo7.lemusedelafaunedasie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class AnimalListActivity extends AppCompatActivity {

    private ListView animalListView;
    private ArrayList<Animal> animalList = new ArrayList<Animal>();
    private AnimalAdapter animalAdapter;
    private AnimalAsyncTask animalAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);

        animalListView = (ListView) findViewById(R.id.animal_listview);

        animalAsyncTask = new AnimalAsyncTask(this);
        animalAsyncTask.execute();

        animalAdapter = new AnimalAdapter(this, animalList);
        animalListView.setAdapter(animalAdapter);
        animalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Animal animal = (Animal) animalListView.getAdapter().getItem(position);
                Intent n = new Intent(getApplicationContext(), AnimalViewActivity.class);
                n.putExtra("animal_name", animal.getName());
                n.putExtra("animal_desc", animal.getDescription());
                n.putExtra("animal_img", animal.getImagePath());
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

        public AnimalAsyncTask (Context context){
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            InputStreamReader inputStreamReader;
            try {
                inputStreamReader = new InputStreamReader(getResources().openRawResource(R.raw.animals));
                Scanner inputStream = new Scanner(inputStreamReader);
                //inputStream.nextLine(); // Ignores the first line

                while (inputStream.hasNext()) {
                    String data = inputStream.nextLine(); // Gets a whole line
                    String[] line = data.split(","); // Splits the line up into a string array

                    if (line.length > 1) {
                        // Do stuff, e.g:
                        animalListTmp.add(new Animal(line[0], line[1], line[2], this.mContext));
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
