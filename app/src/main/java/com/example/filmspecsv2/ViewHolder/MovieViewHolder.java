package com.example.filmspecsv2.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmspecsv2.R;

import java.util.ArrayList;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    public TextView t1;
    public ImageView i1;
    public View mView;


    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        t1 = (TextView)itemView.findViewById(R.id.name);
        i1 = (ImageView)itemView.findViewById(R.id.image);

        mView = itemView;

    }

}
