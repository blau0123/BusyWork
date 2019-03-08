package Notes;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.MainActivity;
import com.example.notes.R;

import Todo.AddTodo;

public class ShowNote extends AppCompatActivity {
    TextView tvBody, tvTitle;
    // code for editnote to know that editnote is returning back data
    final int editNoteCode = 200;
    int id;

    Toolbar toolbar;

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

        // adding toolbar as the actionbar for the activity, allowing tap to navdrawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
    Inflates the options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    /*
    Handles actions for whenever an option is chosen
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.options_delete:
                deleteNote();
                return true;
            case R.id.options_edit:
                editNote();
                return true;
            case R.id.options_convert:
                convertNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    Method to send user to the edit note screen
     */
    public void editNote(){
        Intent i = new Intent(ShowNote.this, EditNote.class);
        // gives editnote the index of item clicked (which will be the rowid in the database)
        i.putExtra("itemID", id);
        startActivityForResult(i, editNoteCode);
    }

    /*
    Delete note and return to previous activity (MainNotes)
     */
    public void deleteNote(){
        NotesDB db = new NotesDB(this);
        db.open();
        db.deleteEntry(id);
        db.close();

        // return to updated MainNotes
        Intent i = new Intent(this, MainActivity.class);
        setResult(RESULT_OK);
        this.finish();
    }

    /*
    Sends user to the add todo (creates new todo with the title of the note)
     */
    public void convertNote(){
        Intent i = new Intent(this, AddTodo.class);
        i.putExtra("todoTitle", tvTitle.getText().toString().trim());
        startActivity(i);
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
    When user presses back button, will send activity data back to mainnotes so mainnotes
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
