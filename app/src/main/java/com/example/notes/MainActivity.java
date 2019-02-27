package com.example.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import Notes.MainNotes;
import Todo.MainTodo;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // have user begin at todo list
        Intent i = new Intent(this, MainTodo.class);
        startActivity(i);
    }
}
