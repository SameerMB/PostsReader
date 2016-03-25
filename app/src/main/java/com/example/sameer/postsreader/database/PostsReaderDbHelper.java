package com.example.sameer.postsreader.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sameer on 20/03/16.
 */
public class PostsReaderDbHelper extends SQLiteOpenHelper {

    private static PostsReaderDbHelper sInstance;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PostsReader.db";

    public static synchronized PostsReaderDbHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new PostsReaderDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private PostsReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PostsReaderContract.PostEntry.SQL_CREATE_ENTRIES);
        db.execSQL(PostsReaderContract.UserEntry.SQL_CREATE_ENTRIES);
        db.execSQL(PostsReaderContract.CommentEntry.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(PostsReaderContract.PostEntry.SQL_DELETE_ENTRIES);
        db.execSQL(PostsReaderContract.UserEntry.SQL_DELETE_ENTRIES);
        db.execSQL(PostsReaderContract.CommentEntry.SQL_DELETE_ENTRIES);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Cursor getAllPosts() {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PostsReaderContract.PostEntry.TABLE_NAME,
                new String[] {PostsReaderContract.PostEntry._ID, PostsReaderContract.PostEntry.COLUMN_NAME_TITLE},
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getPostFromID(int id) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PostsReaderContract.PostEntry.TABLE_NAME,
                new String[] {PostsReaderContract.PostEntry._ID,
                        PostsReaderContract.PostEntry.COLUMN_NAME_USER_ID,
                        PostsReaderContract.PostEntry.COLUMN_NAME_TITLE,
                        PostsReaderContract.PostEntry.COLUMN_NAME_BODY},
                PostsReaderContract.PostEntry._ID + "=" +id,
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public String getUserNameFromID(int id) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PostsReaderContract.UserEntry.TABLE_NAME,
                new String[] {PostsReaderContract.UserEntry._ID,
                        PostsReaderContract.UserEntry.COLUMN_NAME_USERNAME},
                PostsReaderContract.UserEntry._ID + "=" +id,
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(PostsReaderContract.UserEntry.COLUMN_NAME_USERNAME));
        }
        return null;
    }

    public int getCommentsCountFromPostID(int id) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PostsReaderContract.CommentEntry.TABLE_NAME,
                new String[] {PostsReaderContract.CommentEntry._ID,
                        PostsReaderContract.CommentEntry.COLUMN_NAME_POST_ID},
                PostsReaderContract.CommentEntry.COLUMN_NAME_POST_ID + "=" +id,
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor.getCount();
    }

}