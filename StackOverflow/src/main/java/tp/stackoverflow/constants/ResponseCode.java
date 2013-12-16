package tp.stackoverflow.constants;

/**
 * Created by korolkov on 11/27/13.
 */
public enum ResponseCode {

    READY_TO_SHOW("complete"), IN_PROCESSING("pending"),ERROR("error");

    private String status;
    private String code;

    ResponseCode(String status){
        this.status = status;
    }

    public String getCodeValue(){
        return status;
    }

    public void setCode( String code){
        this.code = code;
    }

}
