package tp.stackoverflow.service_requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Set;

import tp.stackoverflow.constants.RequestStatus;
import tp.stackoverflow.constants.RequestType;
import tp.stackoverflow.dao.UserDao;
import tp.stackoverflow.database_entities.User;
import tp.stackoverflow.DataBaseManager;
import tp.stackoverflow.Processor;
import tp.stackoverflow.RESTMethods;
import tp.stackoverflow.ResponseHandler;

/**
 * Created by korolkov on 12/10/13.
 */
public class GetUsersRequest extends ServiceRequest{

    private Set<Integer>   usersId;
    private String         intentFilter = User.INTENT_FILTER;
    private RequestType    requestType  = RequestType.USERS;
    private UserDao        userDao      = (UserDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(User.class);
    private RequestStatus  completeStatus = null;

    public GetUsersRequest(Set<Integer> usersId,   int requestID,
                           Processor    processor, RESTMethods restMethods) {
        super(requestID, processor,restMethods);
        this.usersId = usersId;
    }

    public void   setIntentFilter(String intentFilter) { this.intentFilter = intentFilter; }

    @Override
    public String getIntentFilter() {
        return intentFilter;
    }

    public void setCompleteStatus(RequestStatus status){ completeStatus = status; }

    public RequestStatus getCompleteStatus(){ return completeStatus; }

    @Override
    public RequestStatus getStatus(){
        return mStatus;
    }

    private void replaceCompleteStatus(){ mStatus = completeStatus;}

    @Override
    public void updateStatus(RequestStatus status){
        //TODO s
        if (status == RequestStatus.COMPLETE && completeStatus != null) {
            //super.updateStatus(status);
            replaceCompleteStatus();
        }
    }

    @Override
    public URL getUrl(RESTMethods restMethods){
        //TODO check
        String str = usersId.toString().replaceAll("\\[","").replaceAll("\\]","").replaceAll(",",";").replaceAll(" ","");

        return restMethods.getUsersUrl(str);
    }

    @Override
    public String getRequestKey() { return usersId.toString(); }


    @Override
    public void processEntityObject(ResponseHandler handler, JSONArray jArray) {
        handler.processUserObject(this,jArray);
    }

    @Override
    public RequestType getRequestType() {
        return requestType;
    }


    public String getImageUrl(JSONObject jObject) throws JSONException {
        return jObject.getString(User.PROFILE_IMAGE);
    }

    public int getUserId(JSONObject jObject) throws JSONException {
        return jObject.getInt(User.USER_ID);
    }



    @Override
    protected User convertToEntity(JSONObject jObject) {
        User user = new User();
        try {
            user.setDisplayNAme(jObject.getString(User.DISPLAY_NAME));
            user.setUserId(jObject.getInt(User.USER_ID));
            user.setUserType(jObject.getString(User.USER_TYPE));
            user.setCreationDate(jObject.getString(User.CREATION_DATE));
            user.setRequestId(getRequestId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void processObject(JSONObject jsonObject) {
        try {
            userDao.create(convertToEntity(jsonObject));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        HttpURLConnection conn = mRestMethods.executeRequest(this);
        //TODO handle response code
        mProcessor.handleResponse(this, conn);
    }
}
