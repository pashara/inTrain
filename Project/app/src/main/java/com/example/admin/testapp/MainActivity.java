package com.example.admin.testapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MyLocationListener.SetUpLocationListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {

                    //((EditText) findViewById(R.id.stationText)).getText();
                    final JSONParser parser = new JSONParser();

                    @Override
                    public void run() {
                        try {
                            final Document doc = Jsoup.connect("http://rasp.rw.by/ru/ajax/autocomplete/search/?term=" + ((EditText) findViewById(R.id.stationText)).getText()).header("Accept", "text/javascript").get();
                            final ArrayList<Stations> stationsList = new ArrayList<Stations>();
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

                            if (!stationsList.isEmpty())
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String stationNames = "";
                                        for (int i = 0; i < stationsList.size(); i++) {
                                            stationNames = stationNames + stationsList.get(i) + "\r\n\r\n";
                                        }

                                        ((TextView) findViewById(R.id.hello_text)).setText("Все станции: \n" + stationNames);
                                    }
                                });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        LocationManager lm =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_progress) {
            Intent intent = new Intent(MainActivity.this, LocationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLocationChanged(Location location) {
        //if (location != null) {
            //((TextView) findViewById(R.id.hello_text)).setText("Широта=" + location.getLatitude());
            Log.d(TAG, "Широта=" + location.getLatitude());
            Log.d(TAG, "Долгота=" + location.getLongitude());
        //}
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
