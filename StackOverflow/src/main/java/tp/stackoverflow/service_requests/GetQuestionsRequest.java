package tp.stackoverflow.service_requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import tp.stackoverflow.constants.RequestStatus;
import tp.stackoverflow.constants.RequestType;
import tp.stackoverflow.dao.QuestionDao;
import tp.stackoverflow.database_entities.Question;
import tp.stackoverflow.DataBaseManager;
import tp.stackoverflow.fragments.QuestionsFragment;
import tp.stackoverflow.Processor;
import tp.stackoverflow.RESTMethods;
import tp.stackoverflow.ResponseHandler;

/**
 * Created by korolkov on 11/20/13.
 */
public class GetQuestionsRequest extends ServiceRequest {

    private String         mRequestKey;
    private final String   intentFilter = QuestionsFragment.INTENT_FILTER;
    private RequestType    requestType  = RequestType.QUESTION;
    private QuestionDao    questionDao  = (QuestionDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(Question.class);


    public GetQuestionsRequest(String requestKey, int requestID,
                               Processor processor, RESTMethods restMethods) {
        super(requestID,processor,restMethods);
        mRequestKey = requestKey;
        mStatus     = RequestStatus.PENDING;
    }

    @Override
    public URL getUrl(RESTMethods restMethods){
        return restMethods.getQuestionsUrl(mRequestKey);
    }
    @Override
    public String getRequestKey() {
        return mRequestKey;
    }
    @Override
    public String getIntentFilter() {
        return intentFilter;
    }
    @Override
    public void processEntityObject(ResponseHandler handler, JSONArray jArray) {
        handler.processQuestionObject(this,jArray);
    }
    @Override
    public RequestType getRequestType() {
        return requestType;
    }

    @Override
    protected Question convertToEntity(JSONObject jObject) {
        Question question = new Question();
        try {
            question.setQuestionId(jObject.getInt(Question.QUESTION_ID));
            question.setTitle(jObject.getString(Question.TITLE));
            question.setBody(jObject.getString(Question.BODY));
            question.setUserId(jObject.getJSONObject("owner").getInt(Question.USER_ID));
            question.setScore(jObject.getString(Question.SCORE));
            question.setDate(jObject.getString(Question.DATE));
            question.setRequestId(this.getRequestId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return question;
    }

    @Override
    public void processObject(JSONObject jsonObject) {
        try {
            questionDao.create(convertToEntity(jsonObject));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        HttpURLConnection conn = mRestMethods.executeRequest(this);
        //TODO handle response code
        mProcessor.handleResponse(this, conn);
    }
}
