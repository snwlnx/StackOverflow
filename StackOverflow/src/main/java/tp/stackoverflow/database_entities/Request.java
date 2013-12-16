package tp.stackoverflow.database_entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import tp.stackoverflow.constants.RequestStatus;
import tp.stackoverflow.constants.RequestType;

/**
 * Created by korolkov on 11/23/13.
 */
@DatabaseTable(tableName = "requests")
public class Request implements DbEntity {

    Request(){}

    public Request(RequestType requestType, String request_key, RequestStatus status) {
        this.request_type = requestType.name();
        this.request_key  = request_key;
        this.status       = status.name();
    }

    @DatabaseField(generatedId = true)
    private int    _id;
    public  int     getId() {
        return _id;
    }
    public  void    setId(int id) {
        this._id = id;
    }

    @DatabaseField(dataType= DataType.STRING)
    private String  request_type;
    public  String  getRequestType() {
        return request_type;
    }
    public  void    setRequestType(String request_type) {
        this.request_type = request_type;
    }

    @DatabaseField(dataType= DataType.STRING)
    private String  request_key;
    public  String  getRequestKey() { return request_key;}
    public  void    setRequestKey(String request_key) {
        this.request_key = request_key;
    }

    @DatabaseField(dataType= DataType.STRING)
    private String  status;
    public  String  getStatus() { return status;}
    public  void    setStatus(String status) {
        this.status = status;
    }

    @Override
    public  int     getEntityId() {
        return _id;
    }

}
