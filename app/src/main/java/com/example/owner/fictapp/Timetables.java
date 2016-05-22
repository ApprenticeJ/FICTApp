package com.example.owner.fictapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Timetables extends Fragment {

    final String TAG = "Timetable.java";
    private ProgressDialog progress;
    private static String url = "";

    ArrayList<HashMap<String, String>> timetables=new ArrayList<HashMap<String, String>>();
    ListView lv;
    JSONArray timetable = null;
    private int tPage;

    private static final String ARG_PARAM1 = "ARG_PARAM1";
    private static final String TAG_NAME = "UnitName";
    private static final String TAG_LOCATION = "RoomFloor";
    private static final String TAG_TIME = "tTime";
    private static final String TAG_DATE = "tDate";
    private static final String TAG_ID = "UnitID";

    // TODO: Rename and change types of parameters
    //private String mParam1;
   // private String mParam2;

    //private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static Timetables newInstance(int page) {
        Timetables fragment = new Timetables();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1,page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tPage = getArguments().getInt(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        timetables = new ArrayList<HashMap<String, String>>();
        View view = inflater.inflate(R.layout.fragment_timetables, container, false);
        url = "http://gaptwebsite.azurewebsites.net/api/TimetableApp/1/CIS/" + tPage;
        new getTimetable().execute();
        lv = (ListView) view;
        return view;

        //return inflater.inflate(R.layout.fragment_timetables, container, false);
    }


    private class getTimetable extends AsyncTask<Void,Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading Timetable...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected  Void doInBackground(Void... arg0) {
            MainActivity.ServiceHandler sh = new MainActivity.ServiceHandler();
            String jsonString = sh.makeServiceCall(url);
            if (jsonString != null)
            {
                try
                {
                    timetable = new JSONArray(jsonString);
                    for (int i = 0; i < timetable.length(); i++)
                    {

                        JSONObject c = timetable.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String time = c.getString(TAG_TIME);
                        String location = c.getString(TAG_LOCATION);
                        String date = c.getString(TAG_DATE);
                        String idname = id + ": " +name;
                        //SimpleDateFormat inputFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        //String date="";
                       /* try
                        {
                            Date d=inputFormat.parse(time);
                            SimpleDateFormat outputFormat= new SimpleDateFormat("EEEE,  dd MMMM, yyyy hh:mm a");
                            date=outputFormat.format(d);
                        }
                        catch (Exception e)
                        {
                            Log.e("Date","Date Incorrect format");
                        }*/

                        // tmp hashmap for single event
                        HashMap<String, String> event = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        // event.put(TAG_ID, id);
                        //event.put(TAG_NAME, name);
                        event.put(TAG_TIME, time);
                        event.put(TAG_LOCATION, location);
                        event.put(TAG_DATE, date);
                        event.put(TAG_NAME, idname);


                        // adding event to event list
                        timetables.add(event);
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // dismiss progress dialog
            if (progress.isShowing()) {
                progress.dismiss();
            }

            // update parsed data into ListView
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(),
                    timetables,
                    R.layout.timetable_slot,
                    new String[] {


                            TAG_TIME,
                            TAG_LOCATION,
                            TAG_NAME
                            // TAG_DATE,
                            // TAG_ID
                    },
                    new int[] {
                            R.id.time2,
                            R.id.location2,
                            R.id.name2
                    }
            );

            lv.setAdapter(adapter);

        }

    }
}





