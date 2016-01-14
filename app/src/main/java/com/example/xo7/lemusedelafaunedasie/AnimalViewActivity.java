package com.example.xo7.lemusedelafaunedasie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AnimalViewActivity extends Activity{

    public Context mcontext;
    Animal animal;
    ImageView animalTopImage;
    TextView animalDescription, animalName;
    TableLayout animalPropTable;
    AnimalAsyncTask animalAsyncTask;
    ImageDownloader imageDownloader;
    ImageButton downloadBtn, playBtn, shareBtn;
    private ProgressDialog simpleWaitDialog;
    public Bitmap b;
    private MediaPlayer media;
    private double timeElapsed = 0, finalTime = 0;
    private Handler durationHandler = new Handler();
    public int playing = 0;
    private AnimalDao datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_view);
        mcontext = this;
        int animal_id = getIntent().getIntExtra("animal_id", 0);

        datasource = new AnimalDao(this);
        datasource.open();
        this.animal = datasource.getAnimal(animal_id);

        shareBtn = (ImageButton) findViewById(R.id.share_button);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Pour le message
                String message = "http://mfa.fr/" + getIntent().getIntExtra("animal_id", 0);
                Intent share = new Intent(Intent.ACTION_SEND);
                // Il faudra créer un nouvel Intent, à revoir
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "Partager ces informations"));
            }
        });


        playBtn = (ImageButton) findViewById(R.id.play_button);

        media = MediaPlayer.create(this, R.raw.song);
        finalTime = media.getDuration();

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playing == 0){
                    media.start();
                    playing = 1;
                    playBtn.setImageDrawable(getResources().getDrawable(R.drawable.button_pause));

                } else {
                    media.stop();
                    try {
                        media.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    media.seekTo(0);
                    playing = 0;
                    playBtn.setImageDrawable(getResources().getDrawable(R.drawable.button_play));
                }
            }
        });

        //animalAsyncTask = new AnimalAsyncTask(this, animal_id);
        //animalAsyncTask.execute();
        print(this.animal);
    }

    public void print(Animal animal) {

        this.animal = animal;

        animalTopImage = (ImageView) findViewById(R.id.animal_top_image);
        animalName = (TextView) findViewById(R.id.animal_name);
        animalDescription = (TextView) findViewById(R.id.animal_desc);
        animalPropTable = (TableLayout) findViewById(R.id.animal_prop_list);

        downloadBtn = (ImageButton) findViewById(R.id.download_image);

        String[] animalProps = animal.getProperties().split("@");

        for (String str: animalProps) {
            String[] props = str.split(":");
            TableRow tr1 = new TableRow(this);

            tr1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView textprop1 = new TextView(this);
            TextView textprop2 = new TextView(this);
            textprop1.setText(props[0]);
            textprop2.setText(props[1]);

            LinearLayout.LayoutParams params = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            textprop1.setLayoutParams(params);
            textprop2.setLayoutParams(params);
            textprop1.setPadding(10, 5, 10, 5);
            textprop2.setPadding(10, 5, 10, 5);

            tr1.addView(textprop1);
            tr1.addView(textprop2);
            animalPropTable.addView(tr1);
        }

        switch (animal.getCategory()) {
            case "carnivore":
                animalName.setBackgroundResource(R.color.carnivore);
                animalPropTable.setBackgroundResource(R.color.carnivore_light);
                break;
            case "herbivore":
                animalName.setBackgroundResource(R.color.herbivore);
                animalPropTable.setBackgroundResource(R.color.herbivore_light);
                break;
            case "other":
                animalName.setBackgroundResource(R.color.other);
                animalPropTable.setBackgroundResource(R.color.other_light);
                break;
            case "omnivore":
                animalName.setBackgroundResource(R.color.omnivore);
                animalPropTable.setBackgroundResource(R.color.omnivore_light);
                break;
        }

        animalTopImage.setImageDrawable(this.animal.getImage());

        b = drawableToBitmap(this.animal.getImage());

        downloadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imageDownloader = new ImageDownloader(mcontext, b);
                imageDownloader.execute();
            }
        });

        animalName.setText(this.animal.getName());
        animalDescription.setText(Html.fromHtml(this.animal.getDescription()).toString());
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("eze",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Toast.makeText(this, "Image sauvegardée", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            Log.d("eze", "File not found: " + e.getMessage());
        } catch (Exception e) {
            Log.d("eze", "Error accessing file: " + e.getMessage());
        }
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/MyGallery");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public class AnimalAsyncTask extends AsyncTask {

        private Animal animalTmp;
        public Context mContext;
        public int id;

        public AnimalAsyncTask (Context context, int animal_id){
            mContext = context;
            id = animal_id;
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
                    if (line.length >= 3 && Integer.parseInt(line[0]) == this.id) {
                        // Do stuff, e.g:
                        this.animalTmp = new Animal(this.mContext, Integer.parseInt(line[0]), line[1], line[2], line[3], line[4], line[5], line[6], line[7]);
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
            print(this.animalTmp);
        }
    }

    public class ImageDownloader extends AsyncTask {

        public Context mContext;
        public Bitmap image;

        public ImageDownloader (Context c, Bitmap i){
            mContext = c;
            image = i;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            simpleWaitDialog = ProgressDialog.show(AnimalViewActivity.this,
                    "Wait", "Downloading Image");
        }

        @Override
        protected void onPostExecute(Object result) {
            storeImage(this.image);
            simpleWaitDialog.dismiss();
        }
    }

}
