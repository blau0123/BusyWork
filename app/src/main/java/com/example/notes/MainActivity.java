package com.example.notes;

import android.app.Application;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemClicked{
    Button btnAddNote;
    //variables to work with recyclerview
    RecyclerView rv;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    // code for when returning from shownote in case any note was updated
    int showNoteCode = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initObjects();
        new getData().execute();
    }

    public void initViews(){
        btnAddNote = findViewById(R.id.btnAddNote);
        rv = findViewById(R.id.list);
    }

    public void initObjects(){
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
    }

    /*
    Method for when add note button is clicked; connected to component in xml
     */
    public void btnAddNoteClicked(View v){
        //creates intent that sends user to AddNote activity
        Intent intent = new Intent(MainActivity.this, com.example.notes.AddNote.class);
        startActivity(intent);

        //in addnote, will add note to db and now get note from db and show
        new getData().execute();
    }

    /*
    Accesses database and retrieves an ArrayList of all notes in the database table
     */
    public void loadNotes(){
        try {
            NotesDB db = new NotesDB(this);
            db.open();
            ApplicationClass.notes = db.getAllNotes();
            db.close();
        }
        catch(SQLException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //reverseNotes(notes);
    }

    /*
    Fetches all notes from SQLite in the background (calls loadNotes() in the background), then
    sets re
     */
    public class getData extends AsyncTask<Void, Void, Void>{
        /*
        Actually loads the notes from the database
         */
        @Override
        protected Void doInBackground(Void... voids) {
            loadNotes();
            return null;
        }

        /*
        Notifies recyclerView adapter of a change to update recyclerView
         */
        @Override
        protected void onPostExecute(Void aVoid){
            // reverse the order of ApplicationClass.notes
            ApplicationClass.notes = reverseNotes(ApplicationClass.notes);

            //set recyclerview to notes list if the list is >0
            if (ApplicationClass.notes.size() != 0){
                /*
                if (myAdapter == null){
                    myAdapter = new NoteAdapter(MainActivity.this, ApplicationClass.notes);
                    rv.setAdapter(myAdapter);
                }
                else {
                    System.out.println("updating data!");
                    myAdapter.notifyDataSetChanged();
                }
                */
                // how come notifydatasetchanged() isn't working?
                myAdapter = new NoteAdapter(MainActivity.this, ApplicationClass.notes);
                rv.setAdapter(myAdapter);
            }
        }
    }

    /*
    Getting list of notes from db will give them from oldest -> earliest entries, but
    want the most recent entry to be first
     */
    public ArrayList<Note> reverseNotes(ArrayList<Note> unReversed){
        // make temp arraylist to hold the correct ordering of the contents of AppClass.notes
        ArrayList<Note> tempList = new ArrayList<>();
        for (int i = unReversed.size() - 1; i >= 0; i--){
            tempList.add(unReversed.get(i));
        }
        return tempList;
    }

    /*
    Sends user to ShowNote when an item in the list was clicked
     */
    @Override
    public void onItemClicked(int index) {
        Intent i = new Intent(MainActivity.this, ShowNote.class);
        //i.putExtra("title", ApplicationClass.notes.get(index).getTitle());
        //i.putExtra("body", ApplicationClass.notes.get(index).getNote());
        // have to subtract from size because reversed, so last item is first
        i.putExtra("itemID", ApplicationClass.notes.get(index).getID());
        startActivityForResult(i, showNoteCode);
    }

    /*
    Updates the ApplicationClass.notes list and the recyclerview (if there's any changes to the
    notes)
     */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == showNoteCode){
            // will update recyclerview no matter what result code
            new getData().execute();
        }
    }
}
