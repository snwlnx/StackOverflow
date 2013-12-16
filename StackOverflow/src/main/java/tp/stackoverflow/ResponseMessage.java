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
    String                errCode    = "OK";


    ResponseMessage(int requestId, RequestStatus status, String errCode){
        this.requestId  = requestId;
        this.status     = status;
        this.errCode    = errCode;
    }

    ResponseMessage(int requestId, RequestStatus status){
        this(requestId,status,null);
    }



    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(requestId);
        out.writeString(status.name());
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
        requestId = in.readInt();
        status    = RequestStatus.valueOf(in.readString());
        errCode   = in.readString();
    }
}
