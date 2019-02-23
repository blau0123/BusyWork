package Notes;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.MainActivity;
import com.example.notes.R;

public class ShowNote extends AppCompatActivity {
    TextView tvBody, tvTitle;
    Button btnEdit;
    Button btnDelete;
    // code for editnote to know that editnote is returning back data
    final int editNoteCode = 200;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        initViews();
        setNoteText();
    }

    public void initViews(){
        tvBody = findViewById(R.id.tvBody);
        tvTitle = findViewById(R.id.tvTitle);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
    }

    /*
    Gets the id of the wanted note then sets the TextViews by calling noteToTextView
     */
    public void setNoteText(){
        // get index of which note item was clicked
        Intent i = getIntent();
        id = i.getIntExtra("itemID", 0);

        // get curr note from database to show (rowid = list size - index)
        noteToTextView();
    }

    /*
    When user wants to edit the note, send the note id to EditNote
     */
    public void btnEditClicked(View v){
        Intent i = new Intent(ShowNote.this, EditNote.class);
        // gives editnote the index of item clicked (which will be the rowid in the database)
        i.putExtra("itemID", id);
        startActivityForResult(i, editNoteCode);
    }

    /*
    Delete note and return to previous activity (MainActivity)
     */
    public void btnDeleteClicked(View v){
        NotesDB db = new NotesDB(this);
        db.open();
        db.deleteEntry(id);
        db.close();

        // return to updated MainActivity
        Intent i = new Intent(this, MainActivity.class);
        setResult(RESULT_OK);
        this.finish();
    }

    /*
    Grabs wanted note from the database then updates textViews with the data
     */
    public void noteToTextView(){
        // get curr note from database to show (rowid = list size - index)
        NotesDB db = new NotesDB(this);
        db.open();
        Note noteToShow = db.getNote(id);
        db.close();

        tvTitle.setText(noteToShow.getTitle());
        tvBody.setText(noteToShow.getNote());
    }

    /*
    When return from EditNote, update tvTitle and tvBody by accessing the db again
     */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        try {
            if (requestCode == editNoteCode) {
                if (resultCode == RESULT_OK) {
                    //after editnote finishes, update tvtitle and tvbody
                    noteToTextView();
                }
            }
        }
        catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    When user presses back button, will send activity data back to mainactivity so mainactivity
    updates recyclerview
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this, MainActivity.class);
        setResult(RESULT_OK);
        this.finish();
    }
}
