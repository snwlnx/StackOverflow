package tp.stackoverflow.entity_view;

import java.sql.Blob;

import tp.stackoverflow.database_entities.Answer;
import tp.stackoverflow.database_entities.Question;
import tp.stackoverflow.database_entities.User;

/**
 * Created by korolkov on 12/12/13.
 */
public class FullQuestion implements ListViewEntity {

    public static String _ID           = "_id";
    public static String QUESTION_ID   = "question_id";
    public static String TITLE         = "title";
    public static String BODY          = "body";
    public static String USER_ID       = "user_id";
    public static String REQUEST_ID    = "request_id";
    public static String TAGS          = "tags";
    public static String DATE          = "creation_date";
    public static String SCORE         = "score";

    public FullQuestion(){}

    public FullQuestion(Answer answer){
        this.body          = answer.getBody();
        this.question_id   = answer.getQuestionId();
        this.creation_date = answer.getDate();
    }

    public FullQuestion(Question question){
        this((Answer)question);
        this.title = question.getTitle();
    }

    public FullQuestion(Answer answer,User user){
        this(answer);
        this.display_name  = user.getDisplayName();
        this.user_type     = user.getUserType();
        this.image         = user.getImage();
    }

    public FullQuestion(Question question,User user){
        this((Answer)question,user);
        this.title = question.getTitle();
    }

    private int    _id;
    public  int    getId() {
        return _id;
    }
    public  void   setId(int id) {
        this._id = id;
    }

    private int    question_id;
    public  int    getQuestionId() {
        return question_id;
    }
    public  void   setQuestionId(int id) {
        this.question_id = id;
    }

    private String title;
    public  String getTitle() {
        return title;
    }
    public  void   setTitle(String title) {
        this.title = title;
    }

    private String body;
    public  String getBody() {
        return body;
    }
    public  void   setBody(String body) {
        this.body = body;
    }

    private String user_type;
    public  String getUserType() {
        return user_type;
    }
    public  void   setUserType(String user_type) {
        this.user_type = user_type;
    }

    private String display_name;
    public  String getDisplayName() {
        return display_name;
    }
    public  void   setDisplayNAme(String display_name) {
        this.display_name = display_name;
    }

    private String creation_date;
    @Override
    public  String getDate() {
        return creation_date;
    }
    public  void   setCreationDate(String date) {
        this.creation_date = date;
    }

    private byte[] image;
    @Override
    public  byte[] getImage() {return image;}
    public  void   setImage(byte[]  image) {
        this.image = image;
    }

    @Override
    public int getEntityId() {
        return question_id;
    }
}
