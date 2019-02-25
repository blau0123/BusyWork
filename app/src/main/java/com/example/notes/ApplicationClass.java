package com.example.notes;

import android.app.Application;

import java.util.ArrayList;

import Notes.Note;
import Todo.Todo;

public class ApplicationClass extends Application {
    public static ArrayList<Note> notes;
    public static ArrayList<Todo> highPriorityTodo;
    public static ArrayList<Todo> medPriorityTodo;
    public static ArrayList<Todo> lowPriorityTodo;


    /*
    Creates notes arraylist that will be used for holding contents of recyclerview list for
    each recyclerview for notes and each priority todo
     */
    @Override
    public void onCreate() {
        super.onCreate();

        notes = new ArrayList<>();
        highPriorityTodo = new ArrayList<>();
        medPriorityTodo = new ArrayList<>();
        lowPriorityTodo = new ArrayList<>();
    }
}
