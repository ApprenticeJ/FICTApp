package com.example.owner.fictapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UserChoice extends MainActivity
{
    DatabaseHelper hp=new DatabaseHelper(this);
    String [] menu;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choice);
        menu = getResources().getStringArray(R.array.menuItems);
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: Intent myIntent = new Intent(UserChoice.this, Timetable.class);
                            startActivity(myIntent);
                            break;
                    case 1:
                            Intent myIntent1 = new Intent(UserChoice.this, Deadline.class);
                            startActivity(myIntent1);
                            break;
                    case 2:
                            Intent myIntent2 = new Intent(UserChoice.this, CurrentUnits.class);
                            startActivity(myIntent2);
                            break;
                    case 3:
                            Intent myIntent3 = new Intent(UserChoice.this, UserSettings.class);
                            startActivity(myIntent3);
                            break;
                }
            }
        });

    }
}
