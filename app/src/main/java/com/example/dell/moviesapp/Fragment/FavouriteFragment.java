package com.example.dell.moviesapp.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.dell.moviesapp.DetailActivity;
import com.example.dell.moviesapp.DetailFragment;
import com.example.dell.moviesapp.ImageAdapter;
import com.example.dell.moviesapp.R;
import com.example.dell.moviesapp.data.MovieContract;

/**
 * Created by DELL on 22-Feb-17.
 */

public class FavouriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int MOVIE_LOADER = 0;

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
            MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITES
    };

    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_MOVIE_POSTER = 2;
    static final int COL_MOVIE_NAME = 3;
    static final int COL_MOVIE_OVERVIEW = 4;
    static final int COL_MOVIE_RELEASE_DATE = 5;
    static final int COL_MOVIE_POPULARITY = 6;
    static final int COL_MOVIE_VOTE = 7;
    static final int COL_MOVIE_TRAILER = 8;
    static final int COL_MOVIE_REVIEW = 9;
    static final int COL_MOVIE_BACKDROP = 10;
    static final int COL_MOVIE_CERTIFICATE = 11;
    static final int COL_GENRES = 12;
    static final int COL_TABS = 13;
    static final int COL_MOVIE_FAVORITES = 14;

    private GridView gridView;
    private ImageAdapter imageAdapter;
    private TextView emptyMovie;
    private Uri movieUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.grid_view, container, false);

        emptyMovie = (TextView) view.findViewById(R.id.empty);

        gridView = (GridView) view.findViewById(R.id.grid_view);
        imageAdapter = new ImageAdapter(getActivity(), null, 0);

        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if (cursor != null) {
                    movieUri = MovieContract.MovieEntry.buildMovieUri(cursor.getLong(COL_MOVIE_ID));
                }

                Intent intent = new Intent(getContext(), DetailActivity.class)
                        .putExtra(DetailFragment.DETAIL_URI, movieUri);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY + " DESC";
        Uri movieUri = MovieContract.MovieEntry.buildMovieFavoirteUri();

        return new CursorLoader(getActivity(),
                movieUri,
                MOVIE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        imageAdapter.swapCursor(data);

        if (imageAdapter.getCount() == 0) {
            emptyMovie.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        imageAdapter.swapCursor(null);
    }
}
