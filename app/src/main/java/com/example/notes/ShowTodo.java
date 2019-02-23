package com.example.notes;

import android.content.Intent;
import android.database.SQLException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowTodo extends AppCompatActivity {
    LinearLayout high_layout, med_layout, low_layout;
    Button btnAddTodo;
    int showTodoCode = 199;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_todo);

        initViews();
        // for each priority, create checkboxes and add to appropriate linearlayout
        for (int i = 1; i <= 5; i++){
            createCheckboxes(i);
        }
    }

    public void initViews(){
        high_layout = findViewById(R.id.high_layout);
        med_layout = findViewById(R.id.med_layout);
        low_layout = findViewById(R.id.low_layout);
        btnAddTodo = findViewById(R.id.btnAddTodo);
    }

    /*
    When the add todo button is clicked, bring user to the add todo activity
     */
    public void addNewTodo(View v){
        Intent i = new Intent(this, AddTodo.class);
        startActivityForResult(i, showTodoCode);
    }

    /*
    When return from add note, adds checkbox of new todo to correct linear layout
     */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        try {
            if (requestCode == showTodoCode) {
                if (resultCode == RESULT_OK) {
                    // get the priority of the recently added to do
                    int priorityOfNewTodo = data.getIntExtra("priority", 5);
                    try{
                        TodoDB db = new TodoDB(this);
                        db.open();
                        ArrayList<Todo> tempTodo = db.getTodoBasedOnPriority(priorityOfNewTodo);
                        // last elt of this array will be the most recently added todo
                        CheckBox checkbox = new CheckBox(this);
                        checkbox.setText(tempTodo.get(tempTodo.size()-1).getTitle());

                        //depending on priority, changes which linear layout to be put into
                        switch (priorityOfNewTodo) {
                            case 1:
                                high_layout.addView(checkbox);
                                return;
                            case 2:
                                high_layout.addView(checkbox);
                                return;
                            case 3:
                                med_layout.addView(checkbox);
                                return;
                            case 4:
                                med_layout.addView(checkbox);
                                return;
                            case 5:
                                low_layout.addView(checkbox);
                        }
                        db.close();

                    }
                    catch(SQLException e){
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Fetches all todos from database and creates a checkbox in the appropriate linear layout
    based on what priority the todo is
     */
    public void createCheckboxes(int priority){
        try {
            TodoDB db = new TodoDB(this);
            db.open();
            ArrayList<Todo> todoToShow = db.getTodoBasedOnPriority(priority);
            db.close();

            for (int i = 0; i < todoToShow.size(); i++) {
                System.out.println(todoToShow.get(i).getTitle());
                CheckBox checkbox = new CheckBox(this);
                checkbox.setText(todoToShow.get(i).getTitle());

                //depending on priority, changes which linear layout to be put into
                switch (priority) {
                    case 1:
                        high_layout.addView(checkbox);
                        return;
                    case 2:
                        high_layout.addView(checkbox);
                        return;
                    case 3:
                        med_layout.addView(checkbox);
                        return;
                    case 4:
                        med_layout.addView(checkbox);
                        return;
                    case 5:
                        low_layout.addView(checkbox);
                }
            }
        }
        catch(SQLException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
