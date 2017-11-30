package com.example.admin.testapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class LocationActivity extends AppCompatActivity {
    private ListView listView;
    private AutoCompleteTextView fromStation;
    private EditText toStation;
    private Button serachButton;
    private String prevFrom;
    private String prevTo;
    private ArrayList<TrainItem> trains = new ArrayList<TrainItem>();
    final String[] mCats = { "Мурзик", "Рыжик", "Барсик", "Борис",
            "Мурзилка", "Мурка" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prevFrom = "";
        prevTo = "";

        listView = (ListView)findViewById(R.id.listView);
        fromStation = (AutoCompleteTextView)findViewById(R.id.stationTextFrom);
        toStation = (EditText)findViewById(R.id.stationTextTo);
        serachButton = (Button)findViewById(R.id.searchButton);
        startAnimateLoad(false);
        findViewById(R.id.loaderPanel).setVisibility(View.INVISIBLE);

        fromStation.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, mCats));

    }

    private void drawTrains(){
        TrainItemArrayAdapter adapter = new TrainItemArrayAdapter(this, trains);
        listView.setAdapter(adapter);
        startAnimateLoad(false);
    }


    public void startAnimateLoad(boolean start){
        if(start == true)
            findViewById(R.id.loaderPanel).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.loaderPanel).setVisibility(View.INVISIBLE);
    }

    public void onSearchClick(View view)
    {
        String curFrom =fromStation.getText().toString();
        String curTo = toStation.getText().toString();
        if(!prevFrom.equals(curFrom) || !prevTo.equals(curTo)){
            trains.clear();
            findViewById(R.id.loaderPanel).setVisibility(View.VISIBLE);
            getTrains(fromStation.getText().toString(),toStation.getText().toString());
        }
    }


    protected void getTrains(final String from, final String to) {

        prevFrom = from;
        prevTo = to;

        if (trains.isEmpty()) {
            startAnimateLoad(true);
            //findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                final JSONParser parser = new JSONParser();

                @Override
                public void run() {
                    try {
                        final Document doc = Jsoup.connect("http://mytestaccount.16mb.com/getTablesheet.php?from=" + from + "&to=" + to + "&date=today").header("Accept", "text/javascript").get();
                        final String a = (doc.body().text()).toString();
                        final JSONParser parser = new JSONParser();
                        JSONObject trainsObject = (JSONObject) parser.parse((doc.body().text()).toString());

                        String status = trainsObject.get("status").toString();
                        if(status.equals("true")) {


                            Iterator i = ((JSONArray) trainsObject.get("races")).iterator();

                            while (i.hasNext()) {
                                JSONObject slide = (JSONObject) i.next();
                                TrainItem trainItem = new TrainItem(slide);
                                trains.add(trainItem);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawTrains();
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            drawTrains();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("trains", trains);
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        trains = savedInstanceState.getParcelableArrayList("trains");
        super.onRestoreInstanceState(savedInstanceState);

        if(!trains.isEmpty())
            drawTrains();
    }
}
