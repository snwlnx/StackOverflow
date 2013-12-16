package tp.stackoverflow;

import com.j256.ormlite.stmt.UpdateBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import tp.stackoverflow.constants.RequestStatus;
import tp.stackoverflow.dao.RequestDao;
import tp.stackoverflow.database_entities.Answer;
import tp.stackoverflow.database_entities.Request;
import tp.stackoverflow.service_requests.GetUsersRequest;
import tp.stackoverflow.service_requests.ResponseDetails;
import tp.stackoverflow.service_requests.ServiceRequest;

/**
 * Created by korolkov on 11/29/13.
 */
public class ResponseHandler {

    private Processor  processor;
    private RequestDao requestDao = (RequestDao)DataBaseManager.
            getInstance().getHelper().getEntitiesDao(Request.class);

    ResponseHandler(Processor processor) {
        this.processor = processor;
    }

    private int getUserId(JSONObject jObject){
        Integer userId = null ;
        try {
            userId = jObject.getJSONObject("owner").getInt(Answer.USER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userId;
    }

    private void loadUsersData(Set<Integer> usersId, ServiceRequest request){
        ResponseDetails details = new ResponseDetails(RequestStatus.REFRESH,request.getIntentFilter());
        processor.loadUsersData(usersId,details);
    }


    //TODO key-value pair
    private void loadUsersImages(Set<Map.Entry<Integer,String>> imagesUrls,ServiceRequest request){
        ResponseDetails details = new ResponseDetails(RequestStatus.REFRESH,request.getIntentFilter());
        processor.loadUsersImages(imagesUrls,details);
    }

    public void processQuestionObject(ServiceRequest serviceRequest, JSONArray jArray){
        Set<Integer> usersId = new HashSet<Integer>();
        try {
             for (int i = 0, length = jArray.length(); i < length; i++) {
                usersId.add(getUserId(jArray.getJSONObject(i)));
                serviceRequest.processObject(jArray.getJSONObject(i));
             }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       loadUsersData(usersId,serviceRequest);
    }

    public void processAnswerObject(ServiceRequest serviceRequest, JSONArray jArray){
        processQuestionObject(serviceRequest,jArray);
    }

    public void processUserObject(GetUsersRequest sv, JSONArray jArray){
        Set<Map.Entry<Integer,String>> imageUrls = new HashSet<Map.Entry<Integer, String>>();
        try {

            JSONObject jObject;
            for (int i = 0, length = jArray.length(); i < length; i++) {
                jObject = jArray.getJSONObject(i);
                imageUrls.add(new AbstractMap.SimpleEntry<Integer, String>(sv.getUserId(jObject),sv.getImageUrl(jObject)));
                sv.processObject(jArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadUsersImages(imageUrls,sv);
    }


    private void processResponseString(ServiceRequest serviceRequest, String responseString){
        if (responseString  == null || responseString.equals("") ) return;
        try {
            //TODO one row
            JSONArray jArray  = new JSONObject(responseString).getJSONArray("items");
            serviceRequest.processEntityObject( this, jArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public String getResponseContent(InputStream responseStream){
        BufferedReader reader         = null;
        StringBuilder  responseString = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                responseString.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseString.toString();
    }


    private void updateRequestComplete(ServiceRequest serviceRequest){
        UpdateBuilder<Request, Integer> builder =  requestDao.updateBuilder();
        try {
            builder.updateColumnValue("status",RequestStatus.COMPLETE.name());
            builder.where().eq("_id",serviceRequest.getRequestId());
            builder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        serviceRequest.updateStatus(RequestStatus.COMPLETE);
    }

    public void handleResponse(ServiceRequest serviceRequest,  HttpURLConnection conn){
        int responseCode = 0;
        try {
            //TODO response code
             responseCode = conn.getResponseCode();
            processResponseString(serviceRequest, getResponseContent(conn.getInputStream()));
            updateRequestComplete(serviceRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
        processor.sendResponseMessage(serviceRequest);
    }
}
