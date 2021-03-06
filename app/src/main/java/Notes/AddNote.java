package Notes;

import android.content.Intent;
import android.database.SQLException;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.MainActivity;
import com.example.notes.R;

import java.util.Calendar;

public class AddNote extends AppCompatActivity {
    EditText etInsertTitle, etInsertNote;
    Button btnAdd;

    Toolbar toolbar;

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

        // adding toolbar as the actionbar for the activity, allowing tap to navdrawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
                setResult(RESULT_OK);
                this.finish();
            }
            catch(SQLException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
