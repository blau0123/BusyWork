package Notes;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.ApplicationClass;
import com.example.notes.MainActivity;
import com.example.notes.R;

import java.util.ArrayList;

import Todo.AddTodo;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    // arraylist of notes to be shown in list
    private ArrayList<Note> notes;
    ItemClicked activity;
    // the context in which the recyclerview this adapter is used for is in
    Context ctx;
    int position;

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
    public NoteAdapter(Context context, ArrayList<Note> list){
        notes = list;
        activity = (ItemClicked) context;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        // component in list item that will change for each list item
        TextView tvListTitle, tvListNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // connect the textview variables to the textview components in the list
            tvListTitle = itemView.findViewById(R.id.tvListTitle);
            tvListNote = itemView.findViewById(R.id.tvListNote);
            /*
            Method for when a view in the RecyclerView is clicked
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(notes.indexOf((Note) v.getTag()));
                }
            });

            // registers recyclerview views for context menu when long click
            itemView.setOnCreateContextMenuListener(this);
        }

        /*
        Creates context menu (adds the menu items)
         */
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            // create a new menu item
            MenuItem delete = menu.add(Menu.NONE, R.id.context_delete, Menu.NONE, "Delete");
            MenuItem convert = menu.add(Menu.NONE, R.id.context_convert, Menu.NONE, "Convert Note to Todo");
            MenuItem edit = menu.add(Menu.NONE, R.id.context_edit, Menu.NONE, "Edit");
            // sets what to do when menu item is clicked
            delete.setOnMenuItemClickListener(onEditMenu);
            convert.setOnMenuItemClickListener(onEditMenu);
            edit.setOnMenuItemClickListener(onEditMenu);
        }

        /*
        Listener to execute commands onclick of context menu item
         */
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    // if the delete context menu item clicked, delete the item
                    case R.id.context_delete:
                        try {
                            NotesDB db = new NotesDB(ctx);
                            db.open();
                            db.deleteEntry(notes.get(getAdapterPosition()).getID());
                            // update recyclerview list of the deleted note
                            notes = db.getAllNotes();
                            ApplicationClass.notes = db.getAllNotes();
                            notifyDataSetChanged();
                            db.close();

                            Toast.makeText(ctx, "Note deleted!", Toast.LENGTH_SHORT).show();
                        }
                        catch (SQLException e){
                            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    // if the convert item clicked, bring user to add todo with the note title
                    case R.id.context_convert:
                        try{
                            Intent i = new Intent(ctx, AddTodo.class);
                            i.putExtra("todoTitle", notes.get(getAdapterPosition()).getTitle());
                            ctx.startActivity(i);
                        }
                        catch (Exception e){
                            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    // if the edit item clicked, bring user to edit note with the note contents
                    case R.id.context_edit:
                        Intent i = new Intent(ctx, EditNote.class);
                        i.putExtra("itemID", notes.get(getAdapterPosition()).getID());
                        ctx.startActivity(i);
                }
                return true;
            }
        };
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new NoteAdapter.ViewHolder(view);
    }

    /*
    Sets what the contents of the list item should show
     */
    @Override
    public void onBindViewHolder(@NonNull final NoteAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(notes.get(i));
        // sets title to note title and note body to the first 50 characters of the note.
        String titleText = notes.get(i).getTitle();
        viewHolder.tvListTitle.setText(titleText);
        //only shows the first 50 characters of the note body, or whole body if body is < 50 characters
        if (notes.get(i).getNote().length() > 50) {
            viewHolder.tvListNote.setText(notes.get(i).getNote().substring(0, 50));
        }
        else{
            viewHolder.tvListNote.setText(notes.get(i).getNote());
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int position1){
        position = position1;
    }
}
