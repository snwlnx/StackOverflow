package tp.stackoverflow.service_requests;

import tp.stackoverflow.constants.RequestStatus;

/**
 * Created by korolkov on 12/12/13.
 */
public class ResponseDetails {

    private RequestStatus completeStatus;
    private String        intentFilter;

    public ResponseDetails(RequestStatus status, String intentFilter){
        this.completeStatus = status;
        this.intentFilter   = intentFilter;
    }

    public RequestStatus getCompleteStatus() {
        return completeStatus;
    }

    public String getIntentFilter() {
        return intentFilter;
    }
}
