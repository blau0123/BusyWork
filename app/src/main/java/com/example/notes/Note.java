package com.example.notes;

import java.util.Date;

public class Note {
    private int id;
    private String title, note;
    private long date;

    public Note(){

    }

    // ctor to set all instance variables
    public Note( int noteId, String noteTitle, String noteBody, long date){
        id = noteId;
        title = noteTitle;
        note = noteBody;
        this.date = date;
    }

    //ctor to just set title and note
    public Note( String noteTitle, String noteBody, long date ){
        title = noteTitle;
        note = noteBody;
        this.date = date;
    }

    //getters
    public int getID(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getNote(){
        return note;
    }

    public long getDate(){ return date; }

    //setters
    public void setID( int noteId ){
        id = noteId;
    }

    public void setTitle( String noteTitle ){
        title = noteTitle;
    }

    public void setNote( String noteBody ){
        note = noteBody;
    }

    public void setDate( long noteDate ) { date = noteDate; }


}
