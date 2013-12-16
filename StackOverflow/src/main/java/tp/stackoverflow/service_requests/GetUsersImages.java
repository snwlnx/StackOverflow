package tp.stackoverflow.service_requests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.j256.ormlite.stmt.UpdateBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import tp.stackoverflow.DataBaseManager;
import tp.stackoverflow.Processor;
import tp.stackoverflow.RESTMethods;
import tp.stackoverflow.ResponseHandler;
import tp.stackoverflow.constants.RequestStatus;
import tp.stackoverflow.constants.RequestType;
import tp.stackoverflow.dao.UserDao;
import tp.stackoverflow.database_entities.Request;
import tp.stackoverflow.database_entities.User;

/**
 * Created by korolkov on 12/11/13.
 */
public class GetUsersImages extends ServiceRequest{
    private Set<Map.Entry<Integer,String>>    imagesUrls;
    private String         intentFilter   = User.INTENT_FILTER;
    private RequestType    requestType    = RequestType.USERS;
    private UserDao        userDao        = (UserDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(User.class);
    private RequestStatus  completeStatus = null;

    public GetUsersImages( Set<Map.Entry<Integer,String>> imagesUrls,  int     requestID,
                           Processor   processor,   RESTMethods restMethods) {
        super(requestID, processor,restMethods);
        this.imagesUrls = imagesUrls;
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
        return null;
    }

    @Override
    public String getRequestKey() { return imagesUrls.toString(); }


    @Override
    public void processEntityObject(ResponseHandler handler, JSONArray jArray) {
        //handler.processUserObject(this,jArray);
    }

    @Override
    public RequestType getRequestType() {
        return requestType;
    }


    @Override
    protected User convertToEntity(JSONObject jObject) { return null; }

    @Override
    public void processObject(JSONObject jsonObject) {
    }

    public void processObject(int userId, byte[] bitmap) {
        UpdateBuilder<User, Integer> builder =  userDao.updateBuilder();
        try {
            //Bitmap bitmap1 = new Bitmap();
            builder.updateColumnValue(User.PROFILE_IMAGE,bitmap);
            builder.where().eq(User.USER_ID,userId);
            builder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        HttpURLConnection conn;
        String responseContent;
        for (Map.Entry<Integer,String> entry : imagesUrls) {
            conn = mRestMethods.executeForwardRequest(entry.getValue());
            try {
                //responseContent = mProcessor.getResponseContent(conn.getInputStream());
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                processObject(entry.getKey(), baos.toByteArray());//responseContent.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.updateStatus(RequestStatus.COMPLETE);
        mProcessor.sendResponseMessage(this);
        //TODO handle response code
        //mProcessor.handleResponse(this, conn);
    }
}
