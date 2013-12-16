package tp.stackoverflow.database_entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Blob;

/**
 * Created by korolkov on 12/10/13.
 */
@DatabaseTable(tableName = "users")
public class User implements DbEntity {
    //TODO implements other interface

    public static final String PROFILE_IMAGE = "profile_image";
    public static final String INTENT_FILTER = "users.intent.filter";
    public static final String USER_ID       = "user_id";
    public static final String DISPLAY_NAME  = "display_name";
    public static final String USER_TYPE     = "user_type";
    public static final String IMAGE         = "image";
    public static final String CREATION_DATE = "creation_date";

    public User(){}

    @DatabaseField(generatedId = true)
    private int    _id;
    public  int    getId() {
        return _id;
    }
    public  void   setId(int id) {
        this._id = id;
    }

    @DatabaseField(dataType= DataType.STRING)
    private String user_type;
    public  String getUserType() {
        return user_type;
    }
    public  void   setUserType(String user_type) {
        this.user_type = user_type;
    }

    @DatabaseField(dataType= DataType.STRING)
    private String display_name;
    public  String getDisplayName() {
        return display_name;
    }
    public  void   setDisplayNAme(String display_name) {
        this.display_name = display_name;
    }

    @DatabaseField(dataType= DataType.INTEGER)
    private int    user_id;
    public  int    getUserId() { return user_id; }
    public  void   setUserId(int id) { this.user_id = id; }

    @DatabaseField(dataType= DataType.STRING)
    private String creation_date;
    public  String getCreationDate() {
        return creation_date;
    }
    public  void   setCreationDate(String date) {
        this.creation_date = date;
    }

    @DatabaseField(dataType= DataType.BYTE_ARRAY)
    private byte[]   profile_image;
    public  byte[]   getImage() {
        return profile_image;
    }
    public  void   setImage(byte[]  bytes) {
        this.profile_image = bytes;
    }

    @DatabaseField(dataType= DataType.INTEGER)
    private int    request_id;
    public  int    getRequestId(){ return request_id; }
    public  void   setRequestId(int id) { this.request_id = id; }

    @Override
    public  int    getEntityId() {
        return user_id;
    }

}
