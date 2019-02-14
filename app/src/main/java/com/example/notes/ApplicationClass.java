package com.example.notes;

import android.app.Application;

import java.util.ArrayList;

public class ApplicationClass extends Application {
    public static ArrayList<Note> notes;

    /*
    Creates notes arraylist that will be used for holding contents of recyclerview list
     */
    @Override
    public void onCreate() {
        super.onCreate();

        notes = new ArrayList<>();
    }
}
