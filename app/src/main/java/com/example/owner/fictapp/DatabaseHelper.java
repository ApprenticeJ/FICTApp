package com.example.owner.fictapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Owner on 22/05/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="users.db";
    private static final String TABLE_NAME="users";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_EMAIL="email";
    private static final String COLUMN_PASSWORD="password";
    private static final String COLUMN_COURSE="course";
    private static final String COLUMN_YEAR="year";
    SQLiteDatabase db;
    private static final String TABLE_CREATE="create table users (id integer primary key not null, " +
            "name text not null, email text not null, password text not null, course text not null, year int not null);";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db=db;

    }

    public String getName()
    {
        db=this.getReadableDatabase();
        String query="select name from " + TABLE_NAME;
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            return cursor.getString(0);
        }
        else
        {
            return "no name";
        }
    }

    public String getCourse()
    {
        db=this.getReadableDatabase();
        String query="select course from " + TABLE_NAME;
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            return cursor.getString(0);
        }
        else
        {
            return "no course";
        }
    }

    public String getYear()
    {
        db=this.getReadableDatabase();
        String query="select year from " + TABLE_NAME;
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            return cursor.getString(0);
        }
        else
        {
            return "no course";
        }
    }

    public void insertUsers(Users u)
    {
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        String query="select * from users";
        Cursor cursor=db.rawQuery(query,null);
        int count=cursor.getCount();

        values.put(COLUMN_ID,count);
        values.put(COLUMN_NAME,u.getName());
        values.put(COLUMN_EMAIL,u.getEmail());
        values.put(COLUMN_PASSWORD,u.getPassword());
        values.put(COLUMN_COURSE,u.getCourse());
        values.put(COLUMN_YEAR,u.getYear());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public boolean updateData(String course,int year)
    {
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_COURSE,course);
        values.put(COLUMN_YEAR,year);
        db.update(TABLE_NAME,values, "id=0", null);
        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "Drop Table if Exists" + TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }
}
