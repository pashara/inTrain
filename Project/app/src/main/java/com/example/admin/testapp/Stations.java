package com.example.admin.testapp;


import android.widget.EditText;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by admin on 13.09.2017.
 */

public class Stations {
    private String prefix;
    private String value;
    private GeoPosition position;
    private String label;


    private void init(String prefix,String value, String label, GeoPosition position){
        this.prefix = new String(prefix);
        this.value = new String(value);
        this.label = new String(label);
        GeoPosition lastPosition;
        if(label.contains("Беларусь")){
            if(position.getLongitude() > position.getLatitude()){
                lastPosition = new GeoPosition(position.getLongitude(),position.getLatitude());
            }else{
                lastPosition = new GeoPosition(position);
            }
        }else{
            lastPosition = new GeoPosition(position);
        }

        this.position = new GeoPosition(lastPosition);
    }

    public Stations(final JSONObject o){
        String prefix = o.get("prefix").toString();
        String value = o.get("value").toString();
        String label = o.get("label").toString();
        position = new GeoPosition(Float.valueOf(o.get("lat").toString()), Float.valueOf(o.get("lon").toString()));
        init(prefix, value, label,position);
    }
    public Stations(String prefix,String value, String label, GeoPosition position){
        init(prefix, value,label, position);
    }

    public String toString(){
        double latLine = 111.32137574886572652365646025659;
        double lonLine = 110.94818944820429897351863669647;

        double myLon = MyLocationListener.imHere().getLongitude();
        double myLat = MyLocationListener.imHere().getLatitude(); //Широта

        double stationLon = position.getLongitude();
        double stationLat = position.getLatitude();

        double rast = Math.sqrt(Math.pow(lonLine*(myLon-stationLon),2) + Math.pow(latLine*(myLat-stationLat),2));
        return new String(label+"("+ rast + " км)");
    }


    final public ArrayList<Stations> getStationsFromSite(String searchText){
        final JSONParser parser = new JSONParser();
        final ArrayList<Stations> stationsList = new ArrayList<Stations>();
        try {
            final Document doc = Jsoup.connect("http://rasp.rw.by/ru/ajax/autocomplete/search/?term=" + searchText).header("Accept", "text/javascript").get();

            Object obj = null;
            try {
                obj = parser.parse(doc.body().text());
                JSONArray jsonArray = (JSONArray) obj;

                Iterator i = jsonArray.iterator();

                while (i.hasNext()) {
                    JSONObject slide = (JSONObject) i.next();
                    if (slide.get("label").toString().contains("Беларусь"))
                        stationsList.add(new Stations(slide));

                }

                //((TextView) findViewById(R.id.hello_text)).setText("123");
            } catch (ParseException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return stationsList;
    }
}
