package tp.stackoverflow.service_requests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

import tp.stackoverflow.ResponseMessage;
import tp.stackoverflow.constants.RequestStatus;
import tp.stackoverflow.constants.RequestType;
import tp.stackoverflow.database_entities.DbEntity;
import tp.stackoverflow.database_entities.Request;
import tp.stackoverflow.Processor;
import tp.stackoverflow.RESTMethods;
import tp.stackoverflow.ResponseHandler;

/**
 * Created by korolkov on 11/21/13.
 */
public  abstract class ServiceRequest implements Runnable {
    protected int            mRequestId;
    protected Processor      mProcessor;
    protected RESTMethods    mRestMethods;
    protected RequestStatus  mStatus = RequestStatus.INIT;

    ServiceRequest(int requestId, Processor processor, RESTMethods restMethods){
        mRequestId   = requestId;
        mProcessor   = processor;
        mRestMethods = restMethods;
    }

    public  int getRequestId(){
        return mRequestId;
    }

    public void updateRequestId(Request requestEntity) {
        mRequestId = requestEntity.getId();
        updateStatus(RequestStatus.valueOf(requestEntity.getStatus()));
    }

    public void updateStatus(RequestStatus status){
        mStatus = status;
    }

    public RequestStatus getStatus(){
        return mStatus;
    }



    protected abstract DbEntity convertToEntity(JSONObject jObject);

    public ResponseMessage getResponseMessage() {
        return new ResponseMessage(mRequestId,mStatus);
    }

    public abstract void        processObject(JSONObject jsonObject);

    public abstract URL         getUrl(RESTMethods restMethods);

    public abstract String      getRequestKey();

    public abstract RequestType getRequestType();

    public abstract String      getIntentFilter();

    public abstract void        processEntityObject(ResponseHandler handler, JSONArray jArray);


}
