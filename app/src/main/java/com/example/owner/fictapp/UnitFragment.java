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


public class UnitFragment extends Fragment {

    final String TAG = "UnitFragment.java";
    private ProgressDialog progress;
    private static String url = " ";
    ArrayList<HashMap<String, String>> units = new ArrayList<HashMap<String, String>>();

    private static final String ARG_PAGE = "ARG_PAGE";
    private static final String TAG_ID = "UnitID";
    private static final String TAG_NAME = "UnitName";
    private static final String TAG_DESC = "UnitDescription";
    private static final String TAG_YEAR = "UnitYear";
    private static  String cID = " ";

    private int mPage = 0;
    JSONArray jsonArray = null;

    ListView listView;

    public static UnitFragment newInstance(int page ) {
        UnitFragment fragment = new UnitFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        units = new ArrayList<HashMap<String, String>>();
        View view = inflater.inflate(R.layout.fragment_unit, container, false);
        url = "http://gaptwebsite.azurewebsites.net/api/StudyUnits/" + mPage;
        new GetUnits().execute();
        listView = (ListView) view;
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

                        cID = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String nID = cID + ": " + name;

                        HashMap<String, String> unit = new HashMap<String, String>();

                        unit.put(TAG_ID, nID);
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
                            TAG_ID
                    },

                    new int[]{
                            R.id.unit_name
                    }
            );

            listView.setAdapter(listAdapter);

        }
    }





}

