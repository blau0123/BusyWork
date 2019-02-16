package com.example.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNote extends AppCompatActivity {
    EditText etEditTitle, etEditNote;
    Button btnFinishEdit;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        initViews();
    }

    public void initViews(){
        etEditTitle = findViewById(R.id.etEditTitle);
        etEditNote = findViewById(R.id.etEditNote);
        btnFinishEdit = findViewById(R.id.btnFinishEdit);

        // set edittexts to what the note title and body currently are
        Intent i = getIntent();
        id = i.getIntExtra("itemID", 0);

        // set values of edittexts using database and id
        NotesDB db = new NotesDB(this);
        db.open();
        Note currNote = db.getNote(id);
        etEditTitle.setText(currNote.getTitle());
        etEditNote.setText(currNote.getNote());
        db.close();
    }

    /*
    When the edit button is clicked, update the database entry for the note and return back to
    ShowNote
     */
    public void finishEditClicked(View v){
        // update db with new text
        NotesDB db = new NotesDB(this);
        db.open();
        db.updateEntry(id, etEditTitle.getText().toString().trim(),
                etEditNote.getText().toString().trim());
        db.close();

        // return back to ShowNote
        Intent finished = new Intent(this, ShowNote.class);
        setResult(RESULT_OK);
        EditNote.this.finish();
    }
}
