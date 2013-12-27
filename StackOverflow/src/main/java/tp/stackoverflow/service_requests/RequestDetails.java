package tp.stackoverflow.service_requests;

import tp.stackoverflow.constants.RequestStatus;
import tp.stackoverflow.views.EndlessScrollListener;

/**
 * Created by korolkov on 12/18/13.
 */
public class RequestDetails {

    private String requestKey;
    private int requestId;
    private int page;

    public RequestDetails(String requestKey,int requestId, int page){
        this.requestKey = requestKey;
        this.requestId  = requestId;
        this.page       = page;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getPage() {
        return page;
    }
}
