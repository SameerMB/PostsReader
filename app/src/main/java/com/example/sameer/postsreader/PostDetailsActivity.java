package com.example.sameer.postsreader;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sameer.postsreader.database.PostsReaderContract;
import com.example.sameer.postsreader.database.PostsReaderDbHelper;

public class PostDetailsActivity extends AppCompatActivity {

    public static final String ARG_POST_ID = "post_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        int postID = getIntent().getExtras().getInt(ARG_POST_ID);
        PostsReaderDbHelper dbHelper = PostsReaderDbHelper.getInstance(this);

        Cursor cursor = dbHelper.getPostFromID(postID);

        // set Title
        TextView textView = ((TextView) findViewById(R.id.textViewTitle));
        textView.setText(textView.getText()+cursor.getString(cursor.getColumnIndex(PostsReaderContract.PostEntry.COLUMN_NAME_TITLE)));

        // set Body
        textView = ((TextView) findViewById(R.id.textViewBody));
        textView.setText(textView.getText()+cursor.getString(cursor.getColumnIndex(PostsReaderContract.PostEntry.COLUMN_NAME_BODY)));

        // set Username
        int userID = cursor.getInt(cursor.getColumnIndex(PostsReaderContract.PostEntry.COLUMN_NAME_USER_ID));
        textView = ((TextView) findViewById(R.id.textViewUserName));
        textView.setText(textView.getText()+dbHelper.getUserNameFromID(userID));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: start user details activity
                Toast.makeText(getApplicationContext(), "User details...", Toast.LENGTH_SHORT).show();
            }
        });

        // set Comment count
        textView = ((TextView) findViewById(R.id.textViewCommentsCount));
        textView.setText( textView.getText()+String.valueOf(dbHelper.getCommentsCountFromPostID(postID)) );
    }
}
