package com.example.sameer.postsreader.database;

import android.provider.BaseColumns;

/**
 * Created by sameer on 20/03/16.
 */
public class PostsReaderContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public PostsReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class PostEntry implements BaseColumns {

        public static final String TABLE_NAME           = "posts";

        public static final String COLUMN_NAME_USER_ID  = "userid";
        public static final String COLUMN_NAME_TITLE    = "title";
        public static final String COLUMN_NAME_BODY     = "body";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_BODY + TEXT_TYPE +
                " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class UserEntry implements BaseColumns {

        public static final String TABLE_NAME                   = "users";

        public static final String COLUMN_NAME_NAME            = "name";
        public static final String COLUMN_NAME_USERNAME        = "username";
        public static final String COLUMN_NAME_EMAIL           = "email";
//        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_ADDRESS_STREET  = "address_street";
        public static final String COLUMN_NAME_ADDRESS_SUITE   = "address_suite";
        public static final String COLUMN_NAME_ADDRESS_CITY    = "address_city";
        public static final String COLUMN_NAME_ADDRESS_ZIPCODE = "address_zipcode";
//        public static final String COLUMN_NAME_ADDRESS_GEO = "geo";
        public static final String COLUMN_NAME_ADDRESS_GEO_LAT = "lat";
        public static final String COLUMN_NAME_ADDRESS_GEO_LNG = "lng";
        public static final String COLUMN_NAME_PHONE           = "phone";
        public static final String COLUMN_NAME_WEBSITE         = "website";
//        public static final String COLUMN_NAME_COMPANY = "company";
        public static final String COLUMN_NAME_COMPANY_NAME    = "company_name";
        public static final String COLUMN_NAME_COMPANY_CATCHPHRASE = "company_catchPhrase";
        public static final String COLUMN_NAME_COMPANY_BS      = "company_bs";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ADDRESS_STREET + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ADDRESS_SUITE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ADDRESS_CITY + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ADDRESS_ZIPCODE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ADDRESS_GEO_LAT + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ADDRESS_GEO_LNG + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_WEBSITE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_COMPANY_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_COMPANY_CATCHPHRASE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_COMPANY_BS + TEXT_TYPE +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class CommentEntry implements BaseColumns {

        public static final String TABLE_NAME           = "comments";

        public static final String COLUMN_NAME_POST_ID  = "postid";
        public static final String COLUMN_NAME_NAME    = "name";
        public static final String COLUMN_NAME_EMAIL   = "email";
        public static final String COLUMN_NAME_BODY     = "body";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_POST_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_BODY + TEXT_TYPE +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
