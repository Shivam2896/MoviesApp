package com.example.dell.moviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DELL on 22-Feb-17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movieData.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "
                + MovieContract.MovieEntry.TABLE_NAME + " ("
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY + " REAL NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_VOTE + " REAL NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_REVIEW + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_CERTIFICATE + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_GENRES + " TEXT,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITES + " INTEGER DEFAULT 0);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
