package com.example.xo7.lemusedelafaunedasie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Yoshun on 12/01/2016.
 */
public class SecondActivity extends Activity {

    private ImageButton button_herbivore, button_carnivore, button_other, button_omnivore;
    private Button button_proximity, buttonMap;
    private Context context = this;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        button_herbivore = (ImageButton) findViewById(R.id.button_herbivore);
        button_carnivore = (ImageButton) findViewById(R.id.button_carnivore);
        button_other = (ImageButton) findViewById(R.id.button_other);
        button_omnivore = (ImageButton) findViewById(R.id.button_omnivore);

        button_proximity = (Button) findViewById(R.id.button_proximity);

        button_proximity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProximityActivity.class);
                startActivity(intent);
            }
        });

        button_herbivore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnimalListActivity.class);
                intent.putExtra("category", "herbivore");
                startActivity(intent);
            }
        });

        button_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnimalListActivity.class);
                intent.putExtra("category", "other");
                startActivity(intent);
            }
        });

        button_carnivore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnimalListActivity.class);
                intent.putExtra("category", "carnivore");
                startActivity(intent);
            }
        });

        button_omnivore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnimalListActivity.class);
                intent.putExtra("category", "omnivore");
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        return;
    }
}
