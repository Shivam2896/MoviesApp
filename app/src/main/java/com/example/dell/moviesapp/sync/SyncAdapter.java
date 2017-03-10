package com.example.dell.moviesapp.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.dell.moviesapp.BuildConfig;
import com.example.dell.moviesapp.R;
import com.example.dell.moviesapp.Utilities;
import com.example.dell.moviesapp.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by DELL on 25-Feb-17.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = SyncAdapter.class.getSimpleName();

    String BASE_POPULAR_URL = "http://api.themoviedb.org/3/movie/popular/";
    String BASE_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated/";

    String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w342/";

    // Interval at which to sync with the movies, in seconds.
    // 60 seconds (1 minute) * 60 mins (1 hour) * 24 hours = 1 day
    public static final int SYNC_INTERVAL = 60 * 60 * 24;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.e(LOG_TAG, "Starting sync");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        HttpURLConnection urlConnection2 = null;
        BufferedReader reader2 = null;

        String movieJsonStr ;
        String movieJsonStr2;

        try {
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(BASE_POPULAR_URL).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                    .build();

            Uri builtUri2 = Uri.parse(BASE_RATED_URL).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            URL url2 = new URL(builtUri2.toString());

            Log.e(LOG_TAG, "" + url);
            Log.e(LOG_TAG, "" + url2);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            urlConnection2 = (HttpURLConnection) url2.openConnection();
            urlConnection2.setRequestMethod("GET");
            urlConnection2.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            InputStream inputStream2 = urlConnection2.getInputStream();
            StringBuffer buffer2 = new StringBuffer();
            if (inputStream2 == null) {
                return;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            reader2 = new BufferedReader(new InputStreamReader(inputStream2));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            String line2;
            while ((line2 = reader2.readLine()) != null) {
                buffer2.append(line2 + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }
            if (buffer2.length() == 0) {
                return;
            }

            movieJsonStr = buffer.toString();
            movieJsonStr2 = buffer2.toString();

            getMovieDataFromJson(movieJsonStr, movieJsonStr2);
        }
        catch (IOException | JSONException e ) {
            Log.e(LOG_TAG, "Error ", e);
            return;
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    private void getMovieDataFromJson(String moviesJsonStr, String moviesJsonStr2) throws JSONException {

        final String RESULTS = "results";
        final String MOVIE_ID = "id";
        final String TITLE = "original_title";
        final String POPULARITY = "popularity";
        final String RATING = "vote_average";
        final String POSTER_PATH = "poster_path";
        final String RELEASE_DATE = "release_date";
        final String PLOT = "overview";
        final String BACKDROP = "backdrop_path";
        final String CERTIFICATION = "adult";
        final String LANGUAGE = "original_language";

        try {
            JSONObject movieJson = new JSONObject(moviesJsonStr);
            JSONArray resultsArray = movieJson.getJSONArray(RESULTS);

            JSONObject movieJson2 = new JSONObject(moviesJsonStr2);
            JSONArray resultsArray2 = movieJson2.getJSONArray(RESULTS);

            // Insert the new weather information into the database
            int length = resultsArray.length() + resultsArray2.length();
            Vector<ContentValues> cVVector = new Vector<ContentValues>(length);

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject curResult = resultsArray.getJSONObject(i);

                double id = curResult.getDouble(MOVIE_ID);

                String getURL = curResult.getString(POSTER_PATH);
                String posterPath = BASE_POSTER_URL + getURL;

                String name = curResult.getString(TITLE);
                String overview = curResult.getString(PLOT);
                String date = curResult.getString(RELEASE_DATE);

                double popularity = curResult.getDouble(POPULARITY);
                double vote = curResult.getDouble(RATING);

                String backdrop_path = BASE_POSTER_URL + curResult.getString(BACKDROP);
                String certi = curResult.getString(CERTIFICATION);

                String language = Utilities.getLanguage(curResult.getString(LANGUAGE));

                StringBuilder genreName = new StringBuilder("");

                JSONArray genreArray = curResult.getJSONArray("genre_ids");
                for (int j = 0; j < genreArray.length(); j++) {
                    double genreId = genreArray.getDouble(j);
                    String gname = Utilities.genreName(genreId);

                    if(j == genreArray.length() - 1) {
                        genreName.append(gname + ".");
                    }else {
                        genreName.append(gname + ", ");
                    }
                }

                //add data to in Vector
                ContentValues movieValues = new ContentValues();

                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, posterPath);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, name);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overview);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, date);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, popularity);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE, vote);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP, backdrop_path);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_CERTIFICATE, certi);
                movieValues.put(MovieContract.MovieEntry.COLUMN_GENRES, String.valueOf(genreName));
                movieValues.put(MovieContract.MovieEntry.COLUMN_TABS, 0);
                movieValues.put(MovieContract.MovieEntry.COLUMN_LANGUAGE, language);

                cVVector.add(movieValues);
            }

            for (int i = 0; i < resultsArray2.length(); i++) {

                JSONObject curResult = resultsArray2.getJSONObject(i);

                double id = curResult.getDouble(MOVIE_ID);

                String getURL = curResult.getString(POSTER_PATH);
                String posterPath = BASE_POSTER_URL + getURL;

                String name = curResult.getString(TITLE);
                String overview = curResult.getString(PLOT);
                String date = curResult.getString(RELEASE_DATE);

                double popularity = curResult.getDouble(POPULARITY);
                double vote = curResult.getDouble(RATING);

                String backdrop_path = BASE_POSTER_URL + curResult.getString(BACKDROP);
                String certi = curResult.getString(CERTIFICATION);

                String language = Utilities.getLanguage(curResult.getString(LANGUAGE));

                StringBuilder genreName = new StringBuilder("");

                JSONArray genreArray = curResult.getJSONArray("genre_ids");
                for (int j = 0; j < genreArray.length(); j++) {
                    double genreId = genreArray.getDouble(j);
                    String gname = Utilities.genreName(genreId);

                    if(j == genreArray.length() - 1) {
                        genreName.append(gname + ".");
                    }else {
                        genreName.append(gname + ", ");
                    }
                }

                //add data to in Vector
                ContentValues movieValues = new ContentValues();

                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, posterPath);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, name);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overview);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, date);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, popularity);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE, vote);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP, backdrop_path);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_CERTIFICATE, certi);
                movieValues.put(MovieContract.MovieEntry.COLUMN_GENRES, String.valueOf(genreName));
                movieValues.put(MovieContract.MovieEntry.COLUMN_TABS, 1);
                movieValues.put(MovieContract.MovieEntry.COLUMN_LANGUAGE, language);

                cVVector.add(movieValues);
            }

            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
            }
            Log.e(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static void initializeSyncAdapter (Context context) {
        getSyncAccount(context);
    }

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }
}
