package com.example.dell.moviesapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by DELL on 26-Feb-17.
 */

public class ImageAdapter extends CursorAdapter {

    private static class ViewHolder {
        public final ImageView moviePoster;
        public final TextView movieTitle;

        public ViewHolder(View view){
            moviePoster = (ImageView) view.findViewById(R.id.movie_poster);
            movieTitle = (TextView) view.findViewById(R.id.movie_name);
        }
    }

    public ImageAdapter(Context context, Cursor c, int flags){
        super(context,c,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String posterPath = cursor.getString(GridFragment.COL_MOVIE_POSTER);
        String movieName = cursor.getString(GridFragment.COL_MOVIE_NAME);

        viewHolder.movieTitle.setText(movieName);

        Picasso.with(context)
                .load(posterPath)
                .into(viewHolder.moviePoster);
    }
}
