package com.example.filmspecsv2.Model;

import android.content.Context;

import java.util.ArrayList;

public class MovieItem {
    public String title, poster, barcode;//add title,poster


    public MovieItem(String title, String poster, String barcode) {
        this.title = title;
        this.poster = poster;
        this.barcode = barcode;
    }

    public MovieItem() {


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster)
    {
        this.poster = poster;
    }

    public String getBarcode()
    {
        return barcode;
    }

    public void setBarcode(String barcode) {

        this.barcode = barcode;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj) {
        MovieItem movieItem = (MovieItem) obj;
        return barcode.matches(movieItem.getBarcode());
    }
}
