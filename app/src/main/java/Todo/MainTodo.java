package Todo;

import android.content.Intent;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.notes.ApplicationClass;
import com.example.notes.R;


public class MainTodo extends AppCompatActivity implements HighPriorityTodoAdapter.ItemClicked,
                                                            MedPriorityTodoAdapter.ItemClicked,
                                                            LowPriorityTodoAdapter.ItemClicked{
    LinearLayout high_layout, med_layout, low_layout;

    // to be able to use a drawer to travel from notes to todo
    DrawerLayout drawerLayout;
    NavigationView navView;

    Button btnAddTodo;
    int addTodoCode = 199;

    // variables for each recyclerview for each priority
    RecyclerView rvHighPriority, rvMedPriority, rvLowPriority;
    RecyclerView.Adapter highAdapter, medAdapter, lowAdapter;
    RecyclerView.LayoutManager highLayoutManager, medLayoutManager, lowLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_todo);

        initViews();
        initObjects();

        loadCheckBoxes();
    }

    public void initViews(){
        high_layout = findViewById(R.id.high_layout);
        med_layout = findViewById(R.id.med_layout);
        low_layout = findViewById(R.id.low_layout);
        btnAddTodo = findViewById(R.id.btnAddTodo);

        rvHighPriority = findViewById(R.id.rvHighPriority);
        rvMedPriority = findViewById(R.id.rvMedPriority);
        rvLowPriority = findViewById(R.id.rvLowPriority);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
    }

    public void initObjects(){
        rvHighPriority.setHasFixedSize(true);
        rvMedPriority.setHasFixedSize(true);
        rvLowPriority.setHasFixedSize(true);

        // creates a new layout manager for each recyclerview
        highLayoutManager = new LinearLayoutManager(this);
        medLayoutManager = new LinearLayoutManager(this);
        lowLayoutManager = new LinearLayoutManager(this);
        rvHighPriority.setLayoutManager(highLayoutManager);
        rvMedPriority.setLayoutManager(medLayoutManager);
        rvLowPriority.setLayoutManager(lowLayoutManager);

        /*
        Allows user to go to different activites based on what item is selected in navmenu
         */
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                // code to choose where to go to based on what item was selected
                return true;
            }
        });

    }

    /*
        No matter which priority recyclerview was clicked, will be taken to ShowTodo
         */
    @Override
    public void onItemClicked(int index) {

    }

    /*
    When the add todo button is clicked, bring user to the add todo activity
     */
    public void addNewTodo(View v){
        Intent i = new Intent(this, AddTodo.class);
        startActivityForResult(i, addTodoCode);
    }

    /*
    When return from add note, reloads lists of todos with newly added todo
     */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == addTodoCode) {
            if (resultCode == RESULT_OK) {
                loadCheckBoxes();
            }
        }
    }

    /*
    Fetches all todos from database and creates a checkbox in the appropriate linear layout
    based on what priority the todo is
     */
    public void loadCheckBoxes(){
        try{
            // resets each todo list in ApplicationClass so can reset properly when add new todo
            ApplicationClass.highPriorityTodo.clear();
            ApplicationClass.medPriorityTodo.clear();
            ApplicationClass.lowPriorityTodo.clear();

            TodoDB db = new TodoDB(this);
            db.open();
            // go through each priority with a for loop and load todo arraylist for that priority
            for (int i = 1; i <= 5; i++){
                // if the priority level is 1, then add to lowpriority list
                if (i == 1 || i == 2) {
                    ApplicationClass.highPriorityTodo.addAll(db.getTodoBasedOnPriority(i));
                }
                // if the priority level is 2 or 3, add to medpriority list
                else if (i == 3 || i == 4){
                    ApplicationClass.medPriorityTodo.addAll(db.getTodoBasedOnPriority(i));
                }
                else{
                    ApplicationClass.lowPriorityTodo.addAll(db.getTodoBasedOnPriority(i));
                }
            }
            db.close();
        }
        catch(SQLException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // update recyclerview by creating and setting new adapter for each
        highAdapter = new HighPriorityTodoAdapter(this, ApplicationClass.highPriorityTodo);
        medAdapter = new MedPriorityTodoAdapter(this, ApplicationClass.medPriorityTodo);
        lowAdapter = new LowPriorityTodoAdapter(this, ApplicationClass.lowPriorityTodo);

        // sets adapter to each respective recyclerview
        rvHighPriority.setAdapter(highAdapter);
        rvMedPriority.setAdapter(medAdapter);
        rvLowPriority.setAdapter(lowAdapter);
    }

}
