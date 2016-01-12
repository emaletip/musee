package com.example.xo7.lemusedelafaunedasie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimalViewActivity extends AppCompatActivity {

    ImageView animalTopImage;
    TextView animalDescription, animalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_view);

        String animal_name = getIntent().getStringExtra("animal_name");
        String animal_desc = getIntent().getStringExtra("animal_desc");
        String animal_img = getIntent().getStringExtra("animal_img");
        Animal animal = new Animal(animal_name, animal_desc, animal_img, this);

        animalTopImage = (ImageView) findViewById(R.id.animal_top_image);
        animalName = (TextView) findViewById(R.id.animal_name);
        animalDescription = (TextView) findViewById(R.id.animal_desc);

        animalTopImage.setImageDrawable(animal.getImage());
        animalName.setText(animal_name);
        animalDescription.setText(animal_desc);
    }
}
