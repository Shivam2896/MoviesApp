package com.example.dell.moviesapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.moviesapp.Fragment.PopularFragment;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DELL on 26-Feb-17.
 */

public class ImageAdapter extends CursorAdapter {

    class ViewHolder {
        @Bind(R.id.movie_poster)
        public ImageView moviePoster;
        @Bind(R.id.movie_name)
        public TextView movieTitle;
        @Bind(R.id.movie_genre)
        public TextView movieGenre;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
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

        String posterPath = cursor.getString(PopularFragment.COL_MOVIE_POSTER);
        String movieName = cursor.getString(PopularFragment.COL_MOVIE_NAME);
        String movieGenre = cursor.getString(PopularFragment.COL_GENRES);

        viewHolder.movieTitle.setText(movieName);
        viewHolder.movieGenre.setText(movieGenre);

        Picasso.with(context)
                .load(posterPath)
                .into(viewHolder.moviePoster);
    }
}
