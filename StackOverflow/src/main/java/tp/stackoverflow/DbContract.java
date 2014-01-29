package tp.stackoverflow;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by korolkov on 10/26/13.
 */
public abstract class DbContract {

    private DbContract(){};


    private static final String AUTHORITY = "";//CustomContentProvider.AUTHORITY;

    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, Questions.TABLE_NAME, Questions.URI_NUMBER);
        sURIMatcher.addURI(AUTHORITY, Answers.TABLE_NAME,   Answers.URI_NUMBER);
        sURIMatcher.addURI(AUTHORITY, Comments.TABLE_NAME,  Comments.URI_NUMBER);
        sURIMatcher.addURI(AUTHORITY, Users.TABLE_NAME,     Users.URI_NUMBER);git 
    }


    private static final String TEXT_TYPE    = " TEXT ";
    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String COMMA_SEP    = " , ";



    public static interface Questions extends BaseColumns {

        public static final int    URI_NUMBER = 1;
        public static final String TABLE_NAME                 = "questions";
        public static final String COLUMN_NAME_QUESTION_ID    = "question_id";
        public static final String COLUMN_NAME_TITLE          = "title";
        public static final String COLUMN_NAME_BODY           = "body";
        public static final String COLUMN_NAME_USER_ID        = "user_id";
        public static final String COLUMN_NAME_SCORE          = "score";
        public static final String COLUMN_NAME_DATE           = "date";
        public static final String COLUMN_NAME_TAGS           = "tags";

        public static final String SQL_CREATE_QUESTIONS =
                "CREATE TABLE " + Questions.TABLE_NAME + " ( " +
                    _ID                       + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME_QUESTION_ID   + INTEGER_TYPE  + COMMA_SEP +
                    COLUMN_NAME_TITLE         + TEXT_TYPE     + COMMA_SEP +
                    COLUMN_NAME_BODY          + TEXT_TYPE     + COMMA_SEP +
                    COLUMN_NAME_USER_ID       + INTEGER_TYPE  + COMMA_SEP +
                    COLUMN_NAME_SCORE         + TEXT_TYPE     + COMMA_SEP +
                    COLUMN_NAME_DATE          + TEXT_TYPE     + COMMA_SEP +
                    COLUMN_NAME_TAGS          + TEXT_TYPE     + COMMA_SEP +
                " );";

        public static final String SQL_DELETE_DRIVERS =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + TABLE_NAME);

    }



    public static interface Answers extends BaseColumns {


        public static final int    URI_NUMBER = 2;


        public static final String TABLE_NAME                 = "answers";
        public static final String COLUMN_NAME_ANSWER_ID      = "answer_id";
        public static final String COLUMN_NAME_BODY           = "body";
        public static final String COLUMN_NAME_USER_ID        = "user_id";
        public static final String COLUMN_NAME_SCORE          = "score";
        public static final String COLUMN_NAME_DATE           = "date";
        public static final String COLUMN_NAME_IS_ACCEPTED    = "is_accepted";
        public static final String COLUMN_NAME_STATUS         = "status";

        public static final String SQL_CREATE_ANSWERS =
                "CREATE TABLE " + Questions.TABLE_NAME + " ( " +
                     _ID                       + " INTEGER PRIMARY KEY, " +
                     COLUMN_NAME_ANSWER_ID     + INTEGER_TYPE  + COMMA_SEP +
                     COLUMN_NAME_BODY          + TEXT_TYPE     + COMMA_SEP +
                     COLUMN_NAME_USER_ID       + INTEGER_TYPE  + COMMA_SEP +
                     COLUMN_NAME_SCORE         + TEXT_TYPE     + COMMA_SEP +
                     COLUMN_NAME_DATE          + TEXT_TYPE     + COMMA_SEP +
                     COLUMN_NAME_IS_ACCEPTED   + INTEGER_TYPE  + COMMA_SEP +
                     COLUMN_NAME_STATUS        + INTEGER_TYPE  + COMMA_SEP +
                 " );";

        public static final String SQL_DELETE_DRIVERS =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + TABLE_NAME);
    }



    public static interface Comments extends BaseColumns {


        public static final int    URI_NUMBER = 3;


        public static final String TABLE_NAME                 = "comments";
        public static final String COLUMN_NAME_COMMENTS_ID    = "comment_id";
        public static final String COLUMN_NAME_BODY           = "body";
        public static final String COLUMN_NAME_USER_ID        = "user_id";
        public static final String COLUMN_NAME_SCORE          = "score";
        public static final String COLUMN_NAME_DATE           = "date";
        public static final String COLUMN_NAME_STATUS         = "status";


        public static final String SQL_CREATE_COMMENTS =
                "CREATE TABLE " + Questions.TABLE_NAME + " ( " +
                        _ID                       + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME_COMMENTS_ID   + INTEGER_TYPE  + COMMA_SEP +
                        COLUMN_NAME_BODY          + TEXT_TYPE     + COMMA_SEP +
                        COLUMN_NAME_USER_ID       + INTEGER_TYPE  + COMMA_SEP +
                        COLUMN_NAME_SCORE         + TEXT_TYPE     + COMMA_SEP +
                        COLUMN_NAME_DATE          + TEXT_TYPE     + COMMA_SEP +
                        COLUMN_NAME_STATUS        + INTEGER_TYPE  + COMMA_SEP +
                " );";

        public static final String SQL_DELETE_DRIVERS =
                "DROP TABLE IF EXISTS " + TABLE_NAME;


        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + TABLE_NAME);

    }

    public static interface Users extends BaseColumns {

        public static final int    URI_NUMBER = 4;


        public static final String TABLE_NAME                 = "users";
        public static final String COLUMN_NAME_USER_ID        = "user_id";
        public static final String COLUMN_NAME_USER_NAME      = "name";
        public static final String COLUMN_NAME_IMAGE          = "image";
        public static final String COLUMN_NAME_LOCATION       = "location";
        public static final String COLUMN_NAME_DATE           = "date";
        public static final String COLUMN_NAME_STATUS         = "status";


        public static final String SQL_CREATE_USERS =
                "CREATE TABLE " + Questions.TABLE_NAME + " ( " +
                        _ID                       + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME_USER_ID       + INTEGER_TYPE  + COMMA_SEP +
                        COLUMN_NAME_USER_NAME     + TEXT_TYPE     + COMMA_SEP +
                        COLUMN_NAME_IMAGE         + TEXT_TYPE     + COMMA_SEP +
                        COLUMN_NAME_LOCATION      + TEXT_TYPE     + COMMA_SEP +
                        COLUMN_NAME_DATE          + TEXT_TYPE     + COMMA_SEP +
                        COLUMN_NAME_STATUS        + INTEGER_TYPE  + COMMA_SEP +
                " );";

        public static final String SQL_DELETE_DRIVERS =
                "DROP TABLE IF EXISTS " + TABLE_NAME;


        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + TABLE_NAME);

    }

}
