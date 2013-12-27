package tp.stackoverflow;

import android.os.Parcel;
import android.os.Parcelable;

import tp.stackoverflow.constants.RequestStatus;

/**
 * Created by korolkov on 11/27/13.
 */
public class ResponseMessage implements Parcelable {
    public int            requestId;
    public RequestStatus  status;
    int                   page       = 0;
    String                requestKey = "";
    String                errCode    = "OK";


    public ResponseMessage(int requestId, RequestStatus status, String errCode){
        this.requestId  = requestId;
        this.status     = status;
        this.errCode    = errCode;
    }

    public ResponseMessage(int requestId, RequestStatus status,String requestKey, int page){
        this(requestId,status);
        this.page          = page;
        this.requestKey    = requestKey;

    }

    public ResponseMessage(int requestId, RequestStatus status){
        this(requestId,status,null);
    }



    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(requestId);
        out.writeInt(page);
        out.writeString(status.name());
        out.writeString(requestKey);
        out.writeString(errCode);
    }

    public static final Parcelable.Creator<ResponseMessage> CREATOR
            = new Parcelable.Creator<ResponseMessage>() {

        public ResponseMessage createFromParcel(Parcel in) {
            return new ResponseMessage(in);
        }

        public ResponseMessage[] newArray(int size) {
            return new ResponseMessage[size];
        }
    };

    private ResponseMessage(Parcel in) {
        requestId  = in.readInt();
        page       = in.readInt();
        status     = RequestStatus.valueOf(in.readString());
        requestKey = in.readString();
        errCode    = in.readString();
    }
}
