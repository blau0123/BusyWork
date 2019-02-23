package Notes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notes.R;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    // arraylist of notes to be shown in list
    private ArrayList<Note> notes;
    ItemClicked activity;

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
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
        }
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
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(notes.get(i));
        String titleText = notes.get(i).getTitle();
        viewHolder.tvListTitle.setText(titleText);
        //only shows the first 15 characters of the note body, or whole body if body is < 15 characters
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
}
