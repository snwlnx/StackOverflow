package tp.stackoverflow;

import tp.stackoverflow.service_requests.GetAnswersRequest;
import tp.stackoverflow.service_requests.GetQuestionsRequest;
import tp.stackoverflow.service_requests.GetUsersImages;
import tp.stackoverflow.service_requests.GetUsersRequest;
import tp.stackoverflow.service_requests.RequestDetails;
import tp.stackoverflow.service_requests.ResponseDetails;
import tp.stackoverflow.service_requests.ServiceRequest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;

import java.util.Map;
import java.util.Set;

/**
 * Created by korolkov on 11/20/13.
 */
public class DownloadService extends Service{

    private volatile      Looper          mLooper;
    private volatile      ServiceHandler  mServiceHandler;
    private final         DownloadBinder  mBinder        = new DownloadBinder();
    private               Processor       mProcessor     = new Processor(this);
    private               RESTMethods     mRestMethods   = new RESTMethods();



    private class ServiceHandler extends  Handler{
        private ServiceHandler(Looper looper) { super(looper);}
    }

    public class DownloadBinder extends Binder{

        private void processRequest(ServiceRequest request){
            if (mProcessor.requestAccepted(request)){
                mServiceHandler.post(request);
            }
        }
        //TODO api for activity

        public void getAnswers(int searchKey){
            ServiceRequest request = new GetAnswersRequest(searchKey,
                    mProcessor.getRequestsCount(),
                    mProcessor, mRestMethods);
            processRequest(request);
        }

        public void getQuestions(String searchKey){
            ServiceRequest request = new GetQuestionsRequest(searchKey,
                    mProcessor.getRequestsCount(),
                    mProcessor, mRestMethods);
            processRequest(request);
        }

        public void getQuestions(RequestDetails details){
            ServiceRequest request = new GetQuestionsRequest(details, mProcessor.getRequestsCount(),
                    mProcessor, mRestMethods);
            processRequest(request);
        }

        public void getUsers(Set<Integer> usersId){
            ServiceRequest request = new GetUsersRequest(usersId,
                    mProcessor.getRequestsCount(),
                    mProcessor, mRestMethods);
            processRequest(request);
        }

        public void getUsers(Set<Integer> usersId, ResponseDetails responseDetails){
            GetUsersRequest request = new GetUsersRequest(usersId,
                    mProcessor.getRequestsCount(),
                    mProcessor, mRestMethods);
            request.setCompleteStatus(responseDetails.getCompleteStatus());
            request.setIntentFilter(responseDetails.getIntentFilter());
            processRequest(request);
        }

       public void getUsersImages(Set<Map.Entry<Integer,String>>  usersImages, ResponseDetails responseDetails) {
           GetUsersImages request = new GetUsersImages(usersImages,
                   mProcessor.getRequestsCount(),
                   mProcessor, mRestMethods);
           request.setCompleteStatus(responseDetails.getCompleteStatus());
           request.setIntentFilter(responseDetails.getIntentFilter());
           processRequest(request);
        }
    }

    public void loadUsersData(Set<Integer> usersId, ResponseDetails details){
        mBinder.getUsers(usersId,details);
    }

    public void loadUsersImages(Set<Map.Entry<Integer,String>>  usersImages, ResponseDetails details) {
        mBinder.getUsersImages(usersImages,details);
    }


    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceThread",Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mLooper         = thread.getLooper();
        mServiceHandler = new ServiceHandler(mLooper);
    }

  /*@Override
      public void onUnbind(){
    }   */


    @Override
    public void onDestroy(){
        mLooper.quit();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
