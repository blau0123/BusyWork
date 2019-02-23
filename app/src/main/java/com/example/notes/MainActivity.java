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

    }

    public void goToNotes(View v){
        Intent i = new Intent(this, MainNotes.class);
        startActivity(i);
    }

    public void goToTodo(View v){
        Intent i = new Intent(this, MainTodo.class);
        startActivity(i);
    }
}
