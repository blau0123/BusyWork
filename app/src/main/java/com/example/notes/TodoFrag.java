package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import Todo.AddTodo;
import Todo.HighPriorityTodoAdapter;
import Todo.LowPriorityTodoAdapter;
import Todo.MedPriorityTodoAdapter;


public class TodoFrag extends Fragment {
    View view;

    int addTodoCode = 500;

    // variables for recyclerview
    RecyclerView rvHighPriority, rvMedPriority, rvLowPriority;
    RecyclerView.Adapter highAdapter, medAdapter, lowAdapter;
    RecyclerView.LayoutManager highLayoutManager, medLayoutManager, lowLayoutManager;

    Button btnAddTodo;

    public TodoFrag() {
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
        view = inflater.inflate(R.layout.fragment_todo, container, false);

        // connect recyclerviews with their components
        rvHighPriority = view.findViewById(R.id.rvHighPriority);
        rvMedPriority = view.findViewById(R.id.rvMedPriority);
        rvLowPriority = view.findViewById(R.id.rvLowPriority);

        // creating and setting the layout manager and adapter for each recyclerview
        highLayoutManager = new LinearLayoutManager(this.getActivity());
        rvHighPriority.setLayoutManager(highLayoutManager);
        highAdapter = new HighPriorityTodoAdapter(this.getActivity(), ApplicationClass.highPriorityTodo);
        rvHighPriority.setAdapter(highAdapter);

        medLayoutManager = new LinearLayoutManager(this.getActivity());
        rvMedPriority.setLayoutManager(medLayoutManager);
        medAdapter = new MedPriorityTodoAdapter(this.getActivity(), ApplicationClass.medPriorityTodo);
        rvMedPriority.setAdapter(medAdapter);

        lowLayoutManager = new LinearLayoutManager(this.getActivity());
        rvLowPriority.setLayoutManager(lowLayoutManager);
        lowAdapter = new LowPriorityTodoAdapter(this.getActivity(), ApplicationClass.lowPriorityTodo);
        rvLowPriority.setAdapter(lowAdapter);

        // set onclick for addtodo button in todofrag
        btnAddTodo = view.findViewById(R.id.btnAddTodo);
        btnAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddTodo.class);
                getActivity().startActivityForResult(i, addTodoCode);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void notifyDataChanged(){
        // resets each recyclerview for each priority
        highAdapter = new HighPriorityTodoAdapter(this.getActivity(), ApplicationClass.highPriorityTodo);
        medAdapter = new MedPriorityTodoAdapter(this.getActivity(), ApplicationClass.medPriorityTodo);
        lowAdapter = new LowPriorityTodoAdapter(this.getActivity(), ApplicationClass.lowPriorityTodo);

        rvHighPriority.setAdapter(highAdapter);
        rvMedPriority.setAdapter(medAdapter);
        rvLowPriority.setAdapter(lowAdapter);
    }
}
