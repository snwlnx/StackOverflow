package tp.stackoverflow;

import android.content.Intent;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Set;

import tp.stackoverflow.service_requests.ResponseDetails;
import tp.stackoverflow.service_requests.ServiceRequest;

/**
 * Created by korolkov on 11/20/13.
 */
public class Processor {

    public static final String RESPONSE_MESSAGE = "response_message";

    private DownloadService service;
    private RequestHandler  reqHandler  = new RequestHandler(this);
    private ResponseHandler respHandler = new ResponseHandler(this);


    Processor(DownloadService service){
        this.service = service;
    }

    public int getRequestsCount(){
        return reqHandler.getRequestsCount()+1;
    }

    public void handleResponse(ServiceRequest serviceRequest, HttpURLConnection connection){
        respHandler.handleResponse(serviceRequest,connection);
    }

    public boolean requestAccepted(ServiceRequest request){
        return reqHandler.requestAccepted(request);
    }


    private void sendMessage(ServiceRequest request,ResponseMessage msg){
        Intent intent = (new Intent()).setAction(request.getIntentFilter());
        intent.putExtra(RESPONSE_MESSAGE,msg);
        service.sendBroadcast(intent);
    }

    public void sendResponseMessage(ServiceRequest request) {
        ResponseMessage msg =  new ResponseMessage(request.getRequestId(),request.getStatus());
        sendMessage(request,msg);
    }

    //Dispatch response code
    public void sendResponseMessage(ServiceRequest request,String respCode) {
        ResponseMessage msg =  new ResponseMessage(request.getRequestId(),request.getStatus(),respCode);
        sendMessage(request,msg);
    }


    public void loadUsersData(Set<Integer> usersId, ResponseDetails details){
        service.loadUsersData(usersId,details);
    }

    public void loadUsersImages(Set<Map.Entry<Integer,String>>  usersImages, ResponseDetails details){
        service.loadUsersImages(usersImages,details);
    }

    public String getResponseContent(InputStream inputStream){
        return respHandler.getResponseContent(inputStream);

    }
}
