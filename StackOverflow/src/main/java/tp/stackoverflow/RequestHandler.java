package tp.stackoverflow;

import java.sql.SQLException;

import tp.stackoverflow.dao.RequestDao;
import tp.stackoverflow.database_entities.Request;
import tp.stackoverflow.constants.RequestStatus;
import tp.stackoverflow.service_requests.ServiceRequest;

/**
 * Created by korolkov on 11/29/13.
 */
public class RequestHandler {

    private Processor processor;

    private RequestDao requestDao = (RequestDao)DataBaseManager.
                                            getInstance().getHelper().getEntitiesDao(Request.class);

    RequestHandler(Processor processor){
        this.processor = processor;
    }

    public int getRequestsCount(){
        int rowCount = 0;
        try {
            rowCount = (int)requestDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowCount;
    }

    public boolean requestAccepted(ServiceRequest sRequest){
        //TODO accept
        if (getRequestsCount() == 0) {
            handleRequest(null,sRequest);
            return true;
        }
        Request requestEntity = acceptRequest(sRequest);
        handleRequest(requestEntity,sRequest);
        return requestEntity == null;
    }

    private Request acceptRequest(ServiceRequest serviceRequest) {
        Request request = null;
        try {
            request = requestDao.getReqForKey(serviceRequest.getRequestKey());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return request;
        //TODO one entry per request

/*        if (requestEntity != null && requestEntity.getStatus() == RequestStatus.COMPLETE.getStatusString()){
            reqExist = true;
        } else if (requestEntity == null){
            reqExist = false;
        }*/


    }


    private void handleRequest(Request requestEntity, ServiceRequest serviceRequest) {
        //TODO rename requestEntity -> requestEntity
        RequestStatus status;
        if (requestEntity == null) {
            serviceRequest.updateStatus(RequestStatus.PENDING);
            createNewRequest(serviceRequest);
        } else {
            //status = RequestStatus.valueOf(requestEntity.getStatus());
            serviceRequest.updateRequestId(requestEntity);
        }
        processor.sendResponseMessage(serviceRequest);
    }


    private void createNewRequest(ServiceRequest sRequest) {
        Request request = new Request(sRequest.getRequestType(),sRequest.getRequestKey(),sRequest.getStatus());
        try {
           /*requestDao.queryForAll().size();*/
            requestDao.create(request);
            //List<Request> requests =requestDao.queryForAll();
            //String str;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
