package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Notes.AddNote;
import Notes.NoteAdapter;

public class NotesFrag extends Fragment{
    FloatingActionButton fab;

    //variables to work with recyclerview
    RecyclerView rv;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    View view;

    public NotesFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notes, container, false);

        // seting up recyclerview
        rv = view.findViewById(R.id.rvNoteList);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(layoutManager);
        myAdapter = new NoteAdapter(this.getActivity(), ApplicationClass.notes);
        rv.setAdapter(myAdapter);

        fab = view.findViewById(R.id.fabAddNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddNote.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void notifyDataChanged(){
        //myAdapter.notifyDataSetChanged()
        myAdapter = new NoteAdapter(this.getActivity(), ApplicationClass.notes);
        rv.setAdapter(myAdapter);
    }
}
