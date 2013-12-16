package tp.stackoverflow.database_entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by korolkov on 11/23/13.
 */
@DatabaseTable(tableName = "answers")
public class Answer implements DbEntity {

    public static String _ID           = "_id";
    public static String QUESTION_ID   = "question_id";
    public static String BODY          = "body";
    public static String USER_ID       = "user_id";
    public static String REQUEST_ID    = "request_id";
    public static String DATE          = "creation_date";
    public static String IS_ACCEPTED   = "is_accepted";
    public static String SCORE         = "score";

    @DatabaseField(generatedId = true )
    protected  int   _id;
    public     int   getId() {
        return _id;
    }
    public     void  setId(int id) {
        this._id = id;
    }

    @DatabaseField(dataType= DataType.INTEGER)
    protected  int   question_id;
    public     int   getQuestionId() {
        return question_id;
    }
    public     void  setQuestionId(int id) {
        this.question_id = id;
    }

    @DatabaseField(dataType= DataType.INTEGER)
    protected  int   request_id;
    public     int   getRequestId() {
        return request_id;
    }
    public     void  setRequestId(int id) {
        this.request_id = id;
    }

    @DatabaseField(dataType= DataType.INTEGER)
    protected  int   user_id;
    public     int   getUserId() {
        return user_id;
    }
    public     void  setUserId(int user_id) {
        this.user_id = user_id;
    }

    @DatabaseField(dataType= DataType.STRING)
    protected  String  body;
    public     String  getBody() {
        return body;
    }
    public     void    setBody(String body) {
        this.body = body;
    }


    @DatabaseField(dataType= DataType.STRING)
    protected  String  creation_date;
    public     String  getDate() {
        return creation_date;
    }
    public     void    setDate(String date) {
        this.creation_date = date;
    }

    @DatabaseField(dataType= DataType.STRING)
    protected  String  score;
    public     String  getScore() { return score; }
    public     void    setScore(String score) { this.score = score; }

    @Override
    public     int      getEntityId() {
        return  question_id;
    }
}
