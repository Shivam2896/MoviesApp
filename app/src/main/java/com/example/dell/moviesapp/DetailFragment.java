package com.example.dell.moviesapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.moviesapp.Adapter.CastAdapter;
import com.example.dell.moviesapp.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String DETAIL_URI = "Detail_Uri";

    private static final int DETAIL_LOADER = 0;

    private String mCast;

    private static final String[] DETAIL_COLUMNS = {

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
            MovieContract.MovieEntry.COLUMN_CAST,
            MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITES
    };

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_MOVIE_POSTER = 2;
    public static final int COL_MOVIE_NAME = 3;
    public static final int COL_MOVIE_OVERVIEW = 4;
    public static final int COL_MOVIE_RELEASE_DATE = 5;
    public static final int COL_MOVIE_POPULARITY = 6;
    public static final int COL_MOVIE_VOTE = 7;
    public static final int COL_MOVIE_TRAILER = 8;
    public static final int COL_MOVIE_REVIEW = 9;
    public static final int COL_MOVIE_BACKDROP = 10;
    public static final int COL_MOVIE_CERTIFICATE = 11;
    public static final int COL_GENRES = 12;
    public static final int COL_TABS = 13;
    public static final int COL_LANGUAGE = 14;
    public static final int COL_CAST = 15;
    public static final int COL_MOVIE_FAVORITES = 16;

    Uri mUri;

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    ImageView backdrop;
    ImageView poster;
    TextView title;
    TextView rating;
    TextView runtime;
    TextView release_date;
    TextView language;
    TextView genre;
    TextView overview;

    RecyclerView cast;

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
            DownloadJson();
        }

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transperent));

        backdrop = (ImageView) rootView.findViewById(R.id.backdrop);
        poster = (ImageView) rootView.findViewById(R.id.poster);
        title = (TextView) rootView.findViewById(R.id.title);
        rating = (TextView) rootView.findViewById(R.id.rating);
        runtime = (TextView) rootView.findViewById(R.id.runtime);
        release_date = (TextView) rootView.findViewById(R.id.release_date);
        language = (TextView) rootView.findViewById(R.id.language);
        genre = (TextView) rootView.findViewById(R.id.genre);
        overview = (TextView) rootView.findViewById(R.id.overview);

        cast = (RecyclerView) rootView.findViewById(R.id.cast_recycler);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Changes will apply after you reopen App", Toast.LENGTH_LONG).show();
                updateFavourite();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {
            mCast = data.getString(COL_CAST);

            collapsingToolbarLayout.setTitle(data.getString(COL_MOVIE_NAME));

            title.setText(data.getString(COL_MOVIE_NAME));

            Picasso.with(getContext())
                    .load(data.getString(COL_MOVIE_POSTER))
                    .into(poster);

            Picasso.with(getContext())
                    .load(data.getString(COL_MOVIE_BACKDROP))
                    .into(backdrop);

            rating.setText(data.getString(COL_MOVIE_VOTE));

            release_date.setText(data.getString(COL_MOVIE_RELEASE_DATE));

            language.setText(data.getString(COL_LANGUAGE));

            genre.setText(data.getString(COL_GENRES));

            overview.setText(data.getString(COL_MOVIE_OVERVIEW));

            if (data.getInt(COL_MOVIE_FAVORITES) == 1) {
                fab.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }

            if (mCast == null) {
                cast.setVisibility(View.GONE);
            } else {
                cast.setVisibility(View.VISIBLE);
                CastAdapter adapter = new CastAdapter();
                cast.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                cast.setHasFixedSize(true);
                cast.setNestedScrollingEnabled(false);
                cast.setAdapter(adapter);

            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private Long getMovieId(Uri uri) {
        return MovieContract.MovieEntry.getMovieIdFromUri(uri);
    }

    private void updateFavourite() {
        Long movieId = getMovieId(mUri);

        Cursor cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.buildMovieFavoriteWithIDUri(movieId),
                DETAIL_COLUMNS,
                null,
                null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
            int favorite = cursor.getInt(0);
            ContentValues cv = new ContentValues();

            switch (favorite) {
                case 0:
                    //if not favorite change star to black and update data
                    fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITES, 1);
                    getActivity().getContentResolver().update(
                            MovieContract.MovieEntry.buildMovieFavoriteWithIDUri(movieId),
                            cv,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                            new String[]{Long.toString(movieId)}
                    );
                    break;

                case 1:
                    fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITES, 0);
                    getActivity().getContentResolver().update(
                            MovieContract.MovieEntry.buildMovieFavoriteWithIDUri(movieId),
                            cv,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                            new String[]{Long.toString(movieId)}
                    );
                    break;
            }
        }
    }

    private void DownloadJson () {
        final RequestQueue mRequestQueue;

        final String REVIEW_TAG = "review";
        final String TRAILERS_TAG = "trailers";
        final String CAST_TAG = "cast";

        final Long movieID = getMovieId(mUri);

        String reviewUri = "http://api.themoviedb.org/3/movie/" + movieID + "/reviews?api_key=" + BuildConfig.API_KEY;
        String trailersUri = "http://api.themoviedb.org/3/movie/" + movieID + "/videos?api_key=" + BuildConfig.API_KEY;
        String castUri = "http://api.themoviedb.org/3/movie/" + movieID + "/credits?api_key=" + BuildConfig.API_KEY;

        mRequestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest castRequest = new JsonObjectRequest
                (Request.Method.GET, castUri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSON", response.toString());
                        Log.d("JSON", "Download complete!");
                        mRequestQueue.cancelAll(CAST_TAG);

                        ContentValues cv = new ContentValues();
                        cv.put(MovieContract.MovieEntry.COLUMN_CAST, response.toString());

                        if (mCast == null) {
                            try {
                                getActivity().getContentResolver().update(
                                        MovieContract.MovieEntry.buildMovieCastWithIDUri(movieID),
                                        cv,
                                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                                        new String[]{Long.toString(movieID)}
                                );
                            }catch (NullPointerException e){
                                Log.d("Movie Provider","can't get data");
                            }
                        } else {
                            Log.d("JSON", "cast not null");
                            Log.d("JSON", mCast);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        mRequestQueue.cancelAll(CAST_TAG);


                    }
                });

        castRequest.setTag(CAST_TAG);

        mRequestQueue.add(castRequest);

    }
}
