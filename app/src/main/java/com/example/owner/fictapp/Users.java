package com.example.owner.fictapp;

/**
 * Created by Owner on 22/05/2016.
 */
public class Users
{
    static int year;
    static String name,email,password,course;



    public void setname(String nameIn)
    {
        this.name=nameIn;
    }

    public static String getName()
    {
        return name;
    }

    public void setemail(String emailIn)
    {
        this.email=emailIn;
    }

    public static String getEmail()
    {
        return email;
    }

    public void setPassword(String passwordIn)
    {
        this.password=passwordIn;
    }

    public static String getPassword()
    {
        return password;
    }

    public void setCourse(String courseIn)
    {
        this.course=courseIn;
    }

    public static String getCourse()
    {
        return course;
    }

    public void setYear(int yearIn)
    {
        this.year=yearIn;
    }

    public static int getYear()
    {
        return year;
    }
}
