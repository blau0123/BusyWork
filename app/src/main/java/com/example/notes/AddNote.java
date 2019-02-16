package com.example.notes;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddNote extends AppCompatActivity {
    EditText etInsertTitle, etInsertNote;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        initViews();
    }

    public void initViews(){
        // connect variables to components
        etInsertTitle = findViewById(R.id.etInsertTitle);
        etInsertNote = findViewById(R.id.etInsertNote);
        btnAdd = findViewById(R.id.btnAdd);
    }

    /*
    Method for when add button is clicked; connected to button in xml file
     */
    public void btnAddClicked(View v){
        //checks if either edittext are empty - if so, show toast
        if (etInsertNote.getText().toString().isEmpty() ||
                etInsertTitle.getText().toString().isEmpty()){
            Toast.makeText(AddNote.this, "nani", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                // Adds the title and note given by the user into the database
                NotesDB db = new NotesDB(this);
                db.open();
                String title = etInsertTitle.getText().toString().trim();
                String note = etInsertNote.getText().toString().trim();
                // get current date of adding this note as milliseconds
                long currDate = Calendar.getInstance().getTime().getTime();
                db.addNote(new Note(title, note, currDate));
                db.close();
                Toast.makeText(this, "Note successfully saved!", Toast.LENGTH_SHORT).show();

                // Brings user back to MainActivity (list of notes)
                Intent i = new Intent(AddNote.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            catch(SQLException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
