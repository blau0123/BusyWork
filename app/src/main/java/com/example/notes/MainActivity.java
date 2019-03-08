package com.example.notes;

import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import Notes.NoteAdapter;
import Notes.NotesDB;
import Notes.ShowNote;
import Todo.HighPriorityTodoAdapter;
import Todo.LowPriorityTodoAdapter;
import Todo.MedPriorityTodoAdapter;
import Todo.TodoDB;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemClicked,
        HighPriorityTodoAdapter.ItemClicked, MedPriorityTodoAdapter.ItemClicked,
        LowPriorityTodoAdapter.ItemClicked {

    //fragmentmanager that works with the notes and todo fragments
    FragmentManager fragMan;
    // layout that will hold both fragments (created dynamically)
    FrameLayout frag_container;
    FragmentTransaction frag_trans;

    // code for when returning from shownote in case any note was updated
    int showNoteCode = 300;
    int addTodoCode = 500;
    int addNoteCode = 10;

    // to be able to use a drawer to travel from notes to todo
    DrawerLayout drawerLayout;
    NavigationView navView;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // adding toolbar as the actionbar for the activity, allowing tap to navdrawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // setting up fragments and fragment container
        fragMan = this.getSupportFragmentManager();
        frag_trans = fragMan.beginTransaction();
        frag_container = findViewById(R.id.frag_container);

        // setting up nav drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);

          /*
        Allows user to go to different activites based on what item is selected in navmenu
         */
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                frag_trans = fragMan.beginTransaction();

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                // code to choose where to go to based on what item was selected
                switch (menuItem.getItemId()){
                    // if notes tab is chosen, open todo
                    case R.id.nav_notes:
                        frag_trans.replace(R.id.frag_container, new NotesFrag());
                        frag_trans.addToBackStack(null);
                        frag_trans.commit();

                        // load in notes when go to notes tab
                        new MainActivity.getData().execute();
                        break;
                    // if todo frag is chosen, open notes
                    case R.id.nav_todo:
                        frag_trans.replace(R.id.frag_container, new TodoFrag());
                        frag_trans.addToBackStack(null);
                        frag_trans.commit();

                        // load in checkbox lists when go to todo list tab
                        loadCheckBoxes();
                        break;
                }
                return true;
            }
        });

        if (frag_container != null){
            if (savedInstanceState != null){
                return;
            }

            // set first frag to be seen by user as todo frag
            frag_trans.replace(R.id.frag_container, new TodoFrag()).commit();
            loadCheckBoxes();
        }
    }

    /*
    Handles whenever a menu option was selected (used for nav drawer opening)
     */
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //-------------------------- NOTES SECTION ----------------------------
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
    }

    /*
    Fetches all notes from SQLite in the background (calls loadNotes() in the background), then
    sets re
     */
    public class getData extends AsyncTask<Void, Void, Void> {
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
            //set recyclerview to notes list if the list is >0
            if (ApplicationClass.notes.size() != 0){
                // get the current fragment in the frame layout and if it's notesfrag, then
                // update the recyclerview
                Fragment currFrag = fragMan.findFragmentById(R.id.frag_container);
                if (currFrag instanceof NotesFrag){
                    ((NotesFrag) currFrag).notifyDataChanged();
                }
            }
        }
    }

    @Override
    public void onItemClicked(int index) {
        Intent i = new Intent(this, ShowNote.class);
        // have to subtract from size because reversed, so last item is first
        i.putExtra("itemID", ApplicationClass.notes.get(index).getID());
        startActivityForResult(i, showNoteCode);
    }

    /*
    Updates the ApplicationClass.notes list and the recyclerview (if there's any changes to the
    notes)
     */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        // if come back from shownote activity, reset notes
        if (requestCode == showNoteCode || requestCode == addNoteCode){
            System.out.println("in here");
            // will update recyclerview no matter what result code
            new MainActivity.getData().execute();
        }
        // if come back from add todo activity, reset todo lists
        else if (requestCode == addTodoCode){
            loadCheckBoxes();
        }
    }

    //--------------------------- TODO SECTION -----------------------------
    /*
    Fetches all todos from database and creates a checkbox in the appropriate linear layout
    based on what priority the todo is
     */
    public void loadCheckBoxes() {
        try {
            // resets each todo list in ApplicationClass so can reset properly when add new todo
            ApplicationClass.highPriorityTodo.clear();
            ApplicationClass.medPriorityTodo.clear();
            ApplicationClass.lowPriorityTodo.clear();

            TodoDB db = new TodoDB(this);
            db.open();
            // go through each priority with a for loop and load todo arraylist for that priority
            for (int i = 1; i <= 5; i++) {
                // if the priority level is 1, then add to lowpriority list
                if (i == 1 || i == 2) {
                    ApplicationClass.highPriorityTodo.addAll(db.getTodoBasedOnPriority(i));
                }
                // if the priority level is 2 or 3, add to medpriority list
                else if (i == 3 || i == 4) {
                    ApplicationClass.medPriorityTodo.addAll(db.getTodoBasedOnPriority(i));
                } else {
                    ApplicationClass.lowPriorityTodo.addAll(db.getTodoBasedOnPriority(i));
                }
            }
            db.close();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // reset todo list when finished updating AppClass list if in todofrag
        Fragment currFrag = fragMan.findFragmentById(R.id.frag_container);
        if (currFrag instanceof TodoFrag){
            ((TodoFrag) currFrag).notifyDataChanged();
        }
    }
}
