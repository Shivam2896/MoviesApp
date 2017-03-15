package com.example.dell.moviesapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.dell.moviesapp.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by DELL on 15-Mar-17.
 */

public class WidgetRemoteViewsService extends RemoteViewsService {

    private static final String[] MOVIE_COLUMNS = {

            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_MOVIE_NAME,
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTE,
            MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER,
            MovieContract.MovieEntry.COLUMN_MOVIE_REVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP,
            MovieContract.MovieEntry.COLUMN_MOVIE_CERTIFICATE,
            MovieContract.MovieEntry.COLUMN_GENRES,
            MovieContract.MovieEntry.COLUMN_TABS,
            MovieContract.MovieEntry.COLUMN_LANGUAGE,
            MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITES
    };

    static final int COL_ID = 0;
    static final int COL_MOVIE_POSTER = 2;
    static final int COL_MOVIE_NAME = 3;
    static final int COL_GENRES = 12;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return  new RemoteViewsFactory() {
            private Cursor data = null;
            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();

                Uri movieUri = MovieContract.MovieEntry.buildMovieFavoirteUri();

                data = getContentResolver().query(
                        movieUri,
                        MOVIE_COLUMNS,
                        MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITES + " = ?",
                        new String[] {String.valueOf(1)},
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_collection_item);

                String poster = data.getString(COL_MOVIE_POSTER);

                try {
                    Bitmap b = Picasso.with(getApplicationContext()).load(poster).get();
                    views.setImageViewBitmap(R.id.widget_poster, b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                views.setTextViewText(R.id.widget_name, data.getString(COL_MOVIE_NAME));
                views.setTextViewText(R.id.widget_genre, data.getString(COL_GENRES));
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data != null && data.moveToPosition(position)) {
                    return data.getLong(COL_ID);
                }
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
