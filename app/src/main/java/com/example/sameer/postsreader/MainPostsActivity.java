package com.example.sameer.postsreader;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.sameer.postsreader.database.PostsReaderContract;
import com.example.sameer.postsreader.database.PostsReaderDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainPostsActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private PostsReaderDbHelper mDbHelper = null;

    // URL to get JSON
    private static String jsonURLposts = "http://jsonplaceholder.typicode.com/posts";
    private static String jsonURLusers = "http://jsonplaceholder.typicode.com/users";
    private static String jsonURLcomments = "http://jsonplaceholder.typicode.com/comments";

    // JSON Node names
    private static final String TAG_USER_ID = "userId";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_BODY = "body";

    private static final String TAG_NAME = "name";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_ADDRESS_STREET = "street";
    private static final String TAG_ADDRESS_SUITE = "suite";
    private static final String TAG_ADDRESS_CITY = "city";
    private static final String TAG_ADDRESS_ZIPCODE = "zipcode";
    private static final String TAG_ADDRESS_GEO = "geo";
    private static final String TAG_ADDRESS_GEO_LAT = "lat";
    private static final String TAG_ADDRESS_GEO_LNG = "lng";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_WEBSITE = "website";
    private static final String TAG_COMPANY = "company";
    private static final String TAG_COMPANY_NAME = "name";
    private static final String TAG_COMPANY_CATCHPHRASE = "catchPhrase";
    private static final String TAG_COMPANY_BS = "bs";

    private static final String TAG_POSTID = "postId";
//    private static final String TAG_NAME = "name";
//    private static final String TAG_EMAIL = "email";
//    private static final String TAG_BODY = "body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_posts);

        mDbHelper = PostsReaderDbHelper.getInstance(this);
        File dbFile = getDatabasePath(PostsReaderDbHelper.DATABASE_NAME);


//        dbFile.delete();//testing


        if ( ! dbFile.exists() ){
            // Calling async task to get data from json & store it into database
            new GetPosts().execute();
            new GetUsers().execute();
            new GetComments().execute();
        } else {
            setListAdapter();
        }
    }

    private void setListAdapter(){
        Cursor cursor = mDbHelper.getAllPosts();

        // The desired columns to be bound
        String[] columns = new String[] {
                PostsReaderContract.PostEntry.COLUMN_NAME_TITLE
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.textView_itemPostTitle,
        };
        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(
                this, R.layout.posts_list_item,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the ID from this row in the database.
                int postID = cursor.getInt(cursor.getColumnIndexOrThrow(PostsReaderContract.PostEntry._ID));

                // Start the activity
                Intent intent = new Intent(MainPostsActivity.this, PostDetailsActivity.class);
                intent.putExtra(PostDetailsActivity.ARG_POST_ID, postID);
                startActivity(intent);
//                        String strID = String.valueOf(postID);
//                        Toast.makeText(getApplicationContext(),
//                                strID, Toast.LENGTH_SHORT).show();

            }
        });
    }


    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetPosts extends AsyncTask<Void, Void, Boolean> {

        // posts JSONArray
        JSONArray posts = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            mProgressDialog = new ProgressDialog(MainPostsActivity.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            boolean result = false;

            // Creating service handler class instance
            httpHandler sh = new httpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(jsonURLposts, httpHandler.GET);

            if (jsonStr != null) {
                try {
                    posts = new JSONArray(jsonStr);

                    // Gets the data repository in write mode
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    // looping through All posts
                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject obj = posts.getJSONObject(i);

                        // adding posts to posts DB table
                        //
                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        values.put(PostsReaderContract.PostEntry._ID, obj.getString(TAG_ID));
                        values.put(PostsReaderContract.PostEntry.COLUMN_NAME_USER_ID, obj.getString(TAG_USER_ID));
                        values.put(PostsReaderContract.PostEntry.COLUMN_NAME_TITLE, obj.getString(TAG_TITLE));
                        values.put(PostsReaderContract.PostEntry.COLUMN_NAME_BODY, obj.getString(TAG_BODY));

                        // Insert the new row, returning the primary key value of the new row
                        long newRowId;
                        newRowId = db.insert(
                                PostsReaderContract.PostEntry.TABLE_NAME,
                                null,
                                values);
                    }
                    // close the db
                    db.close();

                    result = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("httpHandler", "Couldn't get any data from the url");
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (result) { //success
                setListAdapter();
            } else {//failed to get the data
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetUsers extends AsyncTask<Void, Void, Void> {

        // posts JSONArray
        JSONArray users = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            httpHandler sh = new httpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(jsonURLusers, httpHandler.GET);

            if (jsonStr != null) {
                try {
                    users = new JSONArray(jsonStr);

                    // Gets the data repository in write mode
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    // looping through All users
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject obj = users.getJSONObject(i);

                        // adding users to users DB table
                        //
                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        values.put(PostsReaderContract.UserEntry._ID, obj.getString(TAG_ID));
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_NAME, obj.getString(TAG_NAME));
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_USERNAME, obj.getString(TAG_USERNAME));
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_EMAIL, obj.getString(TAG_EMAIL));

                        JSONObject addressObj = obj.getJSONObject(TAG_ADDRESS);
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_ADDRESS_STREET, addressObj.getString(TAG_ADDRESS_STREET));
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_ADDRESS_SUITE, addressObj.getString(TAG_ADDRESS_SUITE));
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_ADDRESS_CITY, addressObj.getString(TAG_ADDRESS_CITY));
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_ADDRESS_ZIPCODE, addressObj.getString(TAG_ADDRESS_ZIPCODE));

                        JSONObject geoObj = addressObj.getJSONObject(TAG_ADDRESS_GEO);
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_ADDRESS_GEO_LAT, geoObj.getString(TAG_ADDRESS_GEO_LAT));
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_ADDRESS_GEO_LNG, geoObj.getString(TAG_ADDRESS_GEO_LNG));

                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_PHONE, obj.getString(TAG_PHONE));
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_WEBSITE, obj.getString(TAG_WEBSITE));

                        JSONObject companyObj = obj.getJSONObject(TAG_COMPANY);
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_COMPANY_NAME, companyObj.getString(TAG_COMPANY_NAME));
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_COMPANY_CATCHPHRASE, companyObj.getString(TAG_COMPANY_CATCHPHRASE));
                        values.put(PostsReaderContract.UserEntry.COLUMN_NAME_COMPANY_BS, companyObj.getString(TAG_COMPANY_BS));

                        // Insert the new row, returning the primary key value of the new row
                        long newRowId;
                        newRowId = db.insert(
                                PostsReaderContract.UserEntry.TABLE_NAME,
                                null,
                                values);
                    }
                    // close the db
                    db.close();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("httpHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetComments extends AsyncTask<Void, Void, Void> {

        // posts JSONArray
        JSONArray comments = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            httpHandler sh = new httpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(jsonURLcomments, httpHandler.GET);

            if (jsonStr != null) {
                try {
                    comments = new JSONArray(jsonStr);

                    // Gets the data repository in write mode
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    // looping through All comments
                    for (int i = 0; i < comments.length(); i++) {
                        JSONObject obj = comments.getJSONObject(i);

                        // adding comments to comments DB table
                        //
                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        values.put(PostsReaderContract.CommentEntry._ID, obj.getString(TAG_ID));
                        values.put(PostsReaderContract.CommentEntry.COLUMN_NAME_POST_ID, obj.getString(TAG_POSTID));
                        values.put(PostsReaderContract.CommentEntry.COLUMN_NAME_NAME, obj.getString(TAG_NAME));
                        values.put(PostsReaderContract.CommentEntry.COLUMN_NAME_EMAIL, obj.getString(TAG_EMAIL));
                        values.put(PostsReaderContract.CommentEntry.COLUMN_NAME_BODY, obj.getString(TAG_BODY));

                        // Insert the new row, returning the primary key value of the new row
                        long newRowId;
                        newRowId = db.insert(
                                PostsReaderContract.CommentEntry.TABLE_NAME,
                                null,
                                values);
                    }
                    // close the db
                    db.close();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("httpHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
