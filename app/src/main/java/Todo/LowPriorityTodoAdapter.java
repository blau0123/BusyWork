package Todo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.notes.R;

import java.util.ArrayList;

public class LowPriorityTodoAdapter extends
        RecyclerView.Adapter<LowPriorityTodoAdapter.ViewHolder>  {
    // arraylist of notes to be shown in list
    private ArrayList<Todo> todo;
    LowPriorityTodoAdapter.ItemClicked activity;

    /*
    Interface to be implemented by activites that want to use
    this list (makes the list clickable)
     */
    public interface ItemClicked{
        void onItemClicked(int index);
    }

    /*
    Constructor to set list items and connect list to an activity
     */
    public LowPriorityTodoAdapter(Context context, ArrayList<Todo> list){
        todo = list;
        activity = (LowPriorityTodoAdapter.ItemClicked) context;
    }

    /*
   Connects the elements in each item of the recyclerview to the adapter
    */
    public class ViewHolder extends RecyclerView.ViewHolder{
        // component in list item that will change for each list item
        CheckBox cbTodo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // connect the checkbox in the todo_layout to cbTodo variable
            cbTodo = itemView.findViewById(R.id.cbTodo);

            /*
            Method for when a view in the RecyclerView is clicked
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(todo.indexOf((Todo) v.getTag()));
                }
            });
        }
    }

    /*
   Creates the ViewHolder (inflates the todo_layout)
    */
    @NonNull
    @Override
    public LowPriorityTodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_layout,
                viewGroup, false);
        return new LowPriorityTodoAdapter.ViewHolder(view);
    }

    /*
    Sets what the contents of the list item should show
     */
    @Override
    public void onBindViewHolder(@NonNull LowPriorityTodoAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(todo.get(i));
        String todoTitle = todo.get(i).getTitle();
        viewHolder.cbTodo.setText(todoTitle);
    }

    @Override
    public int getItemCount() {
        return todo.size();
    }
}
