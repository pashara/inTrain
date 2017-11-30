package com.example.admin.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by admin on 20.09.2017.
 */

public class TrainItemArrayAdapter extends ArrayAdapter<TrainItem> {

        private final Context context;
        private final ArrayList<TrainItem> values;

        public TrainItemArrayAdapter(Context context, ArrayList<TrainItem> values) {
            super(context, R.layout.trains_list, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.trains_list, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.label);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

            textView.setText(values.get(position).getTitle());
            imageView.setImageResource(values.get(position).getType().getResourceImage());

            return rowView;
        }
}
