package com.example.owner.fictapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class UserSettings extends AppCompatActivity {
    public Spinner spn1;
    public Spinner spn2;
    public AppCompatButton update;
    DatabaseHelper hp= new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        spn1=(Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"CCE", "CPS", "ICS", "CIS"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spn1.setAdapter(adapter);
        spn2=(Spinner)findViewById(R.id.spinner2);
        String[] items2 = new String[]{"1st Year", "2nd Year", "3rd Year"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        spn2.setAdapter(adapter2);
        update=(AppCompatButton)findViewById(R.id.btn_update);

        update.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        String course=spn1.getSelectedItem().toString();
                        String year=spn2.getSelectedItem().toString();
                        int yearInt=0;
                        if(year=="1st Year")
                        {
                            yearInt=1;
                        }
                        else if(year=="2nd Year")
                        {
                            yearInt=2;
                        }
                        else if(year=="3rd Year")
                        {
                            yearInt=3;
                        }

                        boolean isUpdated=hp.updateData(course,yearInt);
                        if(isUpdated)
                        {
                            Toast.makeText(getBaseContext(), "Data updated", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(UserSettings.this, UserChoice.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(), "Failed to Update", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

