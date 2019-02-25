package Todo;

import android.content.Intent;
import android.database.SQLException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.R;

import java.util.ArrayList;

public class ShowTodo extends AppCompatActivity {
    TextView tvTodoTitle, tvTodoDescr, tvTodoPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_todo);

        initViews();
        setTodoText();

    }

    public void initViews(){
        tvTodoTitle = findViewById(R.id.tvTodoTitle);
        tvTodoDescr = findViewById(R.id.tvTodoDescr);
        tvTodoPriority = findViewById(R.id.tvTodoPriority);
    }

    /*
    Fetches todo that was clicked and displays the contents
     */
    public void setTodoText(){

    }

}
