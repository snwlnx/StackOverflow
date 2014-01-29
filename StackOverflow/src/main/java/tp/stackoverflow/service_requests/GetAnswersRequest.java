package tp.stackoverflow.service_requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import tp.stackoverflow.ResponseMessage;
import tp.stackoverflow.constants.RequestStatus;
import tp.stackoverflow.constants.RequestType;
import tp.stackoverflow.dao.AnswerDao;
import tp.stackoverflow.database_entities.Answer;
import tp.stackoverflow.DataBaseManager;
import tp.stackoverflow.fragments.AnswersFragment;
import tp.stackoverflow.Processor;
import tp.stackoverflow.RESTMethods;
import tp.stackoverflow.ResponseHandler;

/**
 * Created by korolkov on 12/2/13.
 */
public class GetAnswersRequest extends ServiceRequest {

    private String         mRequestKey;
    private final   String intentFilter = AnswersFragment.INTENT_FILTER;
    private RequestType    requestType  = RequestType.ANSWER;
    private AnswerDao      answerDao    = (AnswerDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(Answer.class);

    public GetAnswersRequest(int       requestKey,   int         requestID,
                             Processor processor,    RESTMethods restMethods){
        super(requestID,processor,restMethods);
        mRequestKey = Integer.toString(requestKey);
        mStatus = RequestStatus.PENDING;
    }

    @Override
    public void run() {
        HttpURLConnection conn = mRestMethods.executeRequest(this);
        //TODO handle response code
        mProcessor.handleResponse(this, conn);
    }

    @Override
    public URL getUrl(RESTMethods restMethods) {
        return restMethods.getAnswersUrl(mRequestKey);
    }
    @Override
    public String      getRequestKey() {
        return mRequestKey;
    }
    @Override
    public RequestType getRequestType() {
        return requestType;
    }
    @Override
    public String      getIntentFilter() {
        return intentFilter;
    }
    @Override
    public void choiceEntityHandlerMethod(ResponseHandler handler, JSONArray jArray) {
        handler.processAnswerObject(this,jArray);
    }

    @Override
    protected Answer convertToEntity(JSONObject jObject) {
        Answer answer = new Answer();
        try {
            answer.setQuestionId(jObject.getInt(Answer.QUESTION_ID));
            answer.setBody((jObject.getString(Answer.BODY)));
            answer.setUserId(jObject.getJSONObject("owner").getInt(Answer.USER_ID));
            answer.setScore(jObject.getString(Answer.SCORE));
            answer.setDate(jObject.getString(Answer.DATE));
            answer.setRequestId(this.getRequestId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return answer;
    }


    @Override
    public void writeObjectToDB(JSONObject jsonObject) {
        try {
            answerDao.create(convertToEntity(jsonObject));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
