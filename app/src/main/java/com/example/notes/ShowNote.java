package com.example.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowNote extends AppCompatActivity {
    TextView tvBody, tvTitle;

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
    }

    public void setNoteText(){
        Intent i = getIntent();
        tvTitle.setText(i.getStringExtra("title"));
        tvBody.setText(i.getStringExtra("body"));
    }
}
