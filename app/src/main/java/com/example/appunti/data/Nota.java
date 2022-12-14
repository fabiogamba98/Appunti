package com.example.appunti.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

//@Entity
public class Nota implements Serializable {
    //@PrimaryKey(autoGenerate = true)
    private int id;

    //@ColumnInfo(name = "title_column")
    private String title;

    //@ColumnInfo(name = "note_column")
    private String text;

    //@ColumnInfo(name = "date_column")
    private String date;

    public Nota(String title, String text, int id){
        this.title = title;
        this.text = text;
        this.id = id;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        date = sdf.format(Calendar.getInstance().getTime());
    }

    public Nota(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
