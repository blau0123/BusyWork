package Notes;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.R;

import java.util.Calendar;

public class EditNote extends AppCompatActivity {
    EditText etEditTitle, etEditNote;
    Button btnFinishEdit;
    int id;

    Toolbar toolbar;

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

        // adding toolbar as the actionbar for the activity, allowing tap to navdrawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /*
    When the edit button is clicked, update the database entry for the note and return back to
    ShowNote
     */
    public void finishEditClicked(View v){
        // update db with new text
        try {
            NotesDB db = new NotesDB(this);
            db.open();
            db.updateEntry(id, etEditTitle.getText().toString().trim(),
                    etEditNote.getText().toString().trim(),
                    Calendar.getInstance().getTime().getTime());
            db.close();
        }
        catch (SQLException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // return back to ShowNote
        Intent finished = new Intent(this, ShowNote.class);
        setResult(RESULT_OK);
        EditNote.this.finish();
    }
}
