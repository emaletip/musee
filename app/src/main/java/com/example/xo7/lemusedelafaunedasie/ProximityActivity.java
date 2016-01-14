package com.example.xo7.lemusedelafaunedasie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class ProximityActivity extends Activity {

    public Context mContext;
    private AnimalAdapter animalAdapter;
    private AnimalAsyncTask animalAsyncTask;
    private ListView animalListView;
    private TextView categoryText;
    private ArrayList<Animal> animalList = new ArrayList<Animal>();
    private AnimalDao datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        startService(new Intent(this, BackgroundService.class));
        mContext = this;
        animalListView = (ListView) findViewById(R.id.animal_listview);
        categoryText = (TextView) findViewById(R.id.animal_name);

        categoryText.setText(String.valueOf("A proximité").toUpperCase());

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
    @Override
    protected void onStart() {
        super.onStart();
        MyApp.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApp.getBus().unregister(this);
    }

    @Subscribe public void answerAvailable(Location location) {

        animalAsyncTask = new AnimalAsyncTask(mContext, location);
        animalAsyncTask.execute();

    }

    public void print() {
        animalAdapter.notifyDataSetChanged();
    }

    public class AnimalAsyncTask extends AsyncTask {

        private ArrayList<Animal> animalListTmp = new ArrayList<Animal>();
        public Context mContext;
        public Location loc_tmp;

        public AnimalAsyncTask (Context context, Location location){
            mContext = context;
            loc_tmp = location;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            datasource = new AnimalDao(mContext);
            datasource.open();

            for(Animal animal : datasource.getAllAnimals())  {

                Float latitude_animal = Float.parseFloat(animal.getLatitude());
                Float longitude_animal = Float.parseFloat(animal.getLongitude());

                Location location_animal = new Location("");

                location_animal.setLatitude(latitude_animal);//your coords of course
                location_animal.setLongitude(longitude_animal);

                float dist = this.loc_tmp.distanceTo(location_animal);
                if(dist <= 15000) {
                    // Do stuff, e.g:
                    animalListTmp.add(animal);
                }
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
