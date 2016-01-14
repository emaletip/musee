package com.example.xo7.lemusedelafaunedasie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xo7 on 11/01/16.
 */
public class AnimalAdapter extends BaseAdapter {

    private LayoutInflater li;
    private ArrayList<Animal> animalList;
    private Context context;

    public AnimalAdapter(Context context, ArrayList<Animal> animalList) {
        this.li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.animalList = animalList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return animalList.size();
    }

    @Override
    public Animal getItem(int position) {
        return animalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            //creation
            convertView = li.inflate(R.layout.animals_view, null);
            viewHolder = new ViewHolder();

            viewHolder.animal_name = (TextView) convertView.findViewById(R.id.animal_name);
            viewHolder.animal_desc = (TextView) convertView.findViewById(R.id.animal_desc);
            viewHolder.animal_image = (ImageView) convertView.findViewById(R.id.animal_image);

            convertView.setTag(viewHolder);
        }
        else {
            //recyclage
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.animal = getItem(position);

        viewHolder.animal_name.setText(viewHolder.animal.getName());

        String desc = viewHolder.animal.getDescription();
        if(desc.length() > 100) {
            desc = String.valueOf(desc.toCharArray(), 0, 100).concat("...");
        }

        viewHolder.animal_desc.setText(desc);

        viewHolder.animal_image.setImageDrawable(viewHolder.animal.getImage());

        return convertView;
    }

    public static class ViewHolder {
        public TextView animal_name, animal_desc;
        public ImageView animal_image;
        public Animal animal;
    }
}
