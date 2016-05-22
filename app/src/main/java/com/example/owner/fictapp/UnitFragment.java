package com.example.owner.fictapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class UnitFragment extends Fragment {

    final String TAG = "UnitFragment.java";
    private ProgressDialog progress;
    private static String url = " ";
    ArrayList<HashMap<String, String>> units = new ArrayList<HashMap<String, String>>();

    private static final String ARG_PAGE = "ARG_PAGE";
    private static final String TAG_ID = "UnitID";
    private static final String TAG_NAME = "UnitName";
    private static final String TAG_SEM = "UnitSemester";
    private static final String TAG_ElECT = "UnitElective";
    private static  String cID = " ";
    private static String id = " ";
    private static String sem = " ";
    private static String elect = " ";

    private static  String temp = " ";

    private static ArrayList<String> first = new ArrayList<String>();
    private static ArrayList<String> second = new ArrayList<String>();
    private static ArrayList<String> third = new ArrayList<String>();

    private int mPage = 0;
    JSONArray jsonArray = null;

    ListView listView;

    public static UnitFragment newInstance(int page, String CS) {
        UnitFragment fragment = new UnitFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString("key", StudyUnits.coursecode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mPage = bundle.getInt(ARG_PAGE);
        id = bundle.getString("key");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        String id3 = getArguments().getString("key");
        units = new ArrayList<HashMap<String, String>>();
        View view = inflater.inflate(R.layout.fragment_unit, container, false);
        url = "http://gaptwebsite.azurewebsites.net/api/StudyUnits/" + mPage + "/" + id ;
        new GetUnits().execute();
        listView = (ListView) view;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), UnitDetails.class);
                Bundle b = new Bundle();
                if(mPage == 1)
                {
                    b.putString("sID", first.get(position));
                }
                else if(mPage == 2)
                {
                    b.putString("sID", second.get(position));
                }
                else
                {
                    b.putString("sID", third.get(position));
                }

                intent.putExtras(b);
                startActivity(intent);
            }
        });
        return view;
    }

    private class GetUnits extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading Units...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            MainActivity.ServiceHandler sh = new MainActivity.ServiceHandler();

            String jsonString = sh.makeServiceCall(url);
            if (jsonString != null) {
                try {
                    jsonArray = new JSONArray(jsonString);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject c = jsonArray.getJSONObject(i);

                        cID = c.getString(TAG_ID); //Get Unit id eg.CIS0000
                        if(mPage == 1)
                        {
                            first.add(cID);
                        }
                        else if(mPage == 2)
                        {
                            second.add(cID);
                        }
                        else
                        {
                            third.add(cID);
                        }
                        String name = c.getString(TAG_NAME); //Get Unit name
                        sem = "Sem: " + c.getString(TAG_SEM); //Get semester number
                        temp = c.getString(TAG_ElECT); //Checking if unit is elective
                        if(temp == "true")
                        {
                            elect = "Elective";
                        }
                        else
                        {
                            elect = " ";
                        }
                        String nID = cID + ": " + name; //Add id + name

                        HashMap<String, String> unit = new HashMap<String, String>();

                        unit.put(TAG_ID, nID); //Put in HashMap
                        unit.put(TAG_SEM, sem);
                        unit.put(TAG_ElECT, elect);
                        units.add(unit);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPreExecute();

            if (progress.isShowing()) {
                progress.dismiss();
            }

            ListAdapter listAdapter = new SimpleAdapter(
                    getActivity(),
                    units,
                    R.layout.single_unit,
                    new String[]{
                            TAG_SEM,
                            TAG_ElECT,
                            TAG_ID
                    },

                    new int[]{
                            R.id.semester_name,
                            R.id.elective,
                            R.id.unit_name
                    }
            );

            listView.setAdapter(listAdapter);

        }
    }





}

