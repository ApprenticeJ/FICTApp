package com.example.owner.fictapp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public void goMap(MenuItem item)
    {
        Intent i = new Intent(MainActivity.this, Map.class);
        startActivity(i);
    }

    public void goCourses(MenuItem item)
    {
        Intent i = new Intent(MainActivity.this, Courses.class);
        startActivity(i);
    }

    public void goUser(MenuItem item)
    {
        if(hp.getName()=="no name")
        {
            Intent i = new Intent(MainActivity.this, SignIn.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(MainActivity.this, UserChoice.class);
            startActivity(i);
        }
    }


    public void goNewsFeed(MenuItem item)
    {
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void goLecturers(MenuItem item)
    {
        Intent i = new Intent(MainActivity.this, Lecturers.class);
        startActivity(i);
    }


    DatabaseHelper hp = new DatabaseHelper(this);
    final String TAG = "MainActivity.java";
    private ProgressDialog progressDialog;

    // url to get json data
    private static String url = "";

    // the following are event keys in the API JSON response
    private static final String TAG_EVENTS = "data";
    private static final String TAG_TITLE = "name";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_WHERE = "place";
    private static final String TAG_WHERE_NAME = "name";
    private static final String TAG_WHERE_LOCATION = "location";
    private static final String TAG_WHERE_LOCATION_CITY = "city";
    private static final String TAG_TIME = "start_time";
    private static final String TAG_DESCRIPTION = "description";


    // events JSONArray
    JSONArray events = null;
    ListView lv;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.url="http://jsonip.com";
        this.url = "http://gaptwebsite.azurewebsites.net/api/news";
        //this.url="https://graph.facebook.com/v2.6/1048199811920977/events/?&access_token=EAACEdEose0cBAOGylIaU5xkyntpy7ZB4OLgZCkLcgtpSXBdbPOZAMQlZBx5wikPZAS9jpr5MsIN0EYQVcckjh1dZBTiHLJDjA9939V4b3zOcABIcsZAsV2zXGzYELKi7IEuViePqdatxWb4vh8lfzUCmm06W3UVvmgwyPpznOSteAZDZD";
        lv = (ListView) findViewById(R.id.listview1);
        String s = hp.getName();
        if(s!="no name")
        {
            Toast.makeText(getBaseContext(), "Hi " + s, Toast.LENGTH_LONG).show();
        }

        // where we will store the events
        eventList = new ArrayList<HashMap<String, String>>();

        // calling async task to get json values
        new GetEvents().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(this, Map.class)));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.menu_map:
                goMap(item);
                return true;
            case R.id.user:
                goUser(item);
                return true;
            case R.id.menu_courses:
                goCourses(item);
                return true;
            case R.id.menu_newsfeed:
                goNewsFeed(item);
                return true;
            /*case R.id.menu_lecturers:
                goLecturers(item);
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Async task class to get json by making HTTP call
    private class GetEvents extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading events...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // instance of service handler class
            ServiceHandler sh = new ServiceHandler();

            // make request to url, jsonStr will store the response
            String jsonStr = sh.makeServiceCall(url);
            //class news request
            /*if (jsonStr != null) {
                try {
                        //JSONObject jsonObj = new JSONObject(jsonStr);
                        events=new JSONArray(jsonStr);

                    // get json array node

                    // looping through all events
                    for (int i = 0; i < events.length(); i++) {

                        JSONObject c = events.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String title = c.getString(TAG_TITLE);
                        String time = c.getString(TAG_TIME);
                        String location = c.getString(TAG_LOCATION);
                        String description = c.getString(TAG_DESCRIPTION);
                        SimpleDateFormat inputFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        String date="";
                        try
                        {
                            Date d=inputFormat.parse(time);
                            SimpleDateFormat outputFormat= new SimpleDateFormat("EEEE,  dd MMMM, yyyy hh:mm a");
                            date=outputFormat.format(d);
                        }
                        catch (Exception e)
                        {
                            Log.e("Date","Date Incorrect format");
                        }

                        // tmp hashmap for single event
                        HashMap<String, String> event = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        event.put(TAG_ID,id);
                        event.put(TAG_TITLE,title);
                        event.put(TAG_TIME,date);
                        event.put(TAG_LOCATION,location);
                        event.put(TAG_DESCRIPTION,description);


                        // adding event to event list
                        eventList.add(event);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }*/

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // get json array node
                    events = jsonObj.getJSONArray(TAG_EVENTS);

                    // looping through all facebook events
                    for (int i = 0; i < events.length(); i++) {

                        JSONObject c = events.getJSONObject(i);

                        String what = c.getString(TAG_TITLE);
                        String description = c.getString(TAG_DESCRIPTION);
                        String time=c.getString(TAG_TIME);




                        String category="";
                        if(c.getString(TAG_CATEGORY)!=null)
                        {
                            category = c.getString(TAG_CATEGORY);
                        }
                        String date="";
                        SimpleDateFormat inputFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date d=new Date();
                        try
                        {
                            d=inputFormat.parse(time);
                        }
                        catch (Exception e)
                        {
                            Log.e("Date","Date Incorrect format");
                        }
                        DateFormat outputFormat= new SimpleDateFormat("EEEE,  dd MMMM, yyyy hh:mm a");
                        date=outputFormat.format(d);

                        // place node is JSON Object

                        JSONObject where = c.getJSONObject(TAG_WHERE);
                        String where_name = where.getString(TAG_WHERE_NAME);
                        JSONObject where_location = where.getJSONObject(TAG_WHERE_LOCATION);
                        String where_location_city="";
                        String where_complete="";
                        if(where_location.getString(TAG_WHERE_LOCATION_CITY)!=null)
                        {
                            where_location_city = where_location.getString(TAG_WHERE_LOCATION_CITY);
                            where_complete = where_name + ", " + where_location_city;
                        }
                        if(where_location_city==null)
                        {
                            where_complete = where_name;
                        }





                        // tmp hashmap for single event
                        HashMap<String, String> event = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        event.put(TAG_TITLE, what);
                        event.put(TAG_DESCRIPTION, description);
                        event.put(TAG_CATEGORY, category);
                        event.put(TAG_WHERE, where_complete);
                        event.put(TAG_TIME, date);


                        // adding event to event list
                        eventList.add(event);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // dismiss progress dialog
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            // update parsed data into ListView
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this,
                    eventList,
                    R.layout.list_item,
                    new String[] {
                            TAG_TITLE,
                            TAG_TIME,
                            TAG_WHERE,
                            TAG_DESCRIPTION
                    },
                    new int[] {
                            R.id.name,
                            R.id.timestamp,
                            R.id.location,
                            R.id.txtStatusMsg
                    }
            );

            lv.setAdapter(adapter);

        }

    }

    public static class ServiceHandler {

        public ServiceHandler() {

        }

        /*
         * Making service call
         * @url - url to make request
         * @method - http request method
         * */
        public String makeServiceCall(String url) {
            //return this.makeServiceCall(url, method, null);
            return this.request(url);
        }

        public String request(String urlString) {

            StringBuffer chaine = new StringBuffer("");
            try{
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestProperty("User-Agent", "");
                connection.setRequestMethod("GET");
                //connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    chaine.append(line);
                }

            } catch (IOException e) {
                // writing exception to log
                e.printStackTrace();
            }

            return chaine.toString();
        }
    }

}
