package tp.stackoverflow;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import tp.stackoverflow.service_requests.ServiceRequest;

/**
 * Created by korolkov on 11/20/13.
 */
public class RESTMethods {

    //TODO rest methods
    private final String SEARCH_QUESTIONS_URL = "http://api.stackexchange.com/2.1/search?pagesize=10&" +
                                    "order=desc&sort=activity&site=stackoverflow&filter=!9f8L76x0L&intitle=";

    private String getAnswersUrlString(String requestKey){
        return "http://api.stackexchange.com/2.1/questions/"+requestKey+"/answers?" +
        "order=desc&sort=activity&site=stackoverflow&filter=!9f8L7BVrc";
    }
 ///2.1/search?page=1&pagesize=10&order=desc&sort=activity&intitle=g&site=stackoverflow

    private String getQuestionsUrlString(String requestKey,int page){

        return "http://api.stackexchange.com/2.1/search?page="+page+"&pagesize=10&" +
                "order=desc&sort=activity&site=stackoverflow&filter=!9f8L76x0L&intitle="+requestKey.replaceAll(" ","%20");
    }


    private String getUsersUrlString(String usersId){
        return "http://api.stackexchange.com/2.1/users/"+usersId+"?order=desc&sort=reputation&site=stackoverflow";
    }


    public URL getUsersUrl(String requestKey){
        return getUrl(getUsersUrlString(requestKey));
    }

    public URL getAnswersUrl(String requestKey) {
        return getUrl(getAnswersUrlString(requestKey));
    }

    public URL getQuestionsUrl(String requestKey, int page) {
        //return getUrl(SEARCH_QUESTIONS_URL+(requestKey.replaceAll(" ","%20")));
        return getUrl(getQuestionsUrlString(requestKey,page));
    }

    public URL getQuestionsUrl(String requestKey) {
        return getUrl(SEARCH_QUESTIONS_URL+(requestKey.replaceAll(" ","%20")));
    }

    private URL getUrl(String urlString){
        URL url = null;
        try {
            url =  new URL(urlString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }


    public HttpURLConnection executeRequest(ServiceRequest serviceRequest) {
        return executeRequestFromUrl(serviceRequest.getUrl(this));
    }

    public HttpURLConnection executeForwardRequest(String urlString) {
        return executeRequestFromUrl(getUrl(urlString));
    }

    private HttpURLConnection executeRequestFromUrl(URL url) {
        HttpURLConnection conn = null;
        try{
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000 /* milliseconds */);
            conn.setConnectTimeout(150000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();

        } catch(IOException e){
            e.printStackTrace();
        }
        return conn;
    }




}
