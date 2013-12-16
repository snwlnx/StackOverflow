package tp.stackoverflow.database_entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by korolkov on 11/23/13.
 */
@DatabaseTable(tableName = "questions")
public class Question  extends Answer implements DbEntity{

    public static String INTENT_FILTER = "question.intent.filter";
    public static String _ID           = "_id";
    public static String QUESTION_ID   = "question_id";
    public static String TITLE         = "title";
    public static String BODY          = "body";
    public static String USER_ID       = "user_id";
    public static String REQUEST_ID    = "request_id";
    public static String TAGS          = "tags";
    public static String DATE          = "creation_date";
    public static String SCORE         = "score";

    public Question(){}

    @DatabaseField(dataType= DataType.STRING)
    private String  title;
    public  String  getTitle() { return title; }
    public  void    setTitle(String title) { this.title = title;}
}
