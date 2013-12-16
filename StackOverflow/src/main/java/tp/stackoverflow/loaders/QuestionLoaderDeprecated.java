package tp.stackoverflow.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tp.stackoverflow.dao.QuestionDao;
import tp.stackoverflow.dao.UserDao;
import tp.stackoverflow.database_entities.Question;
import tp.stackoverflow.database_entities.User;
import tp.stackoverflow.DataBaseManager;

/**
 * Created by korolkov on 11/27/13.
 */

public class QuestionLoaderDeprecated extends AsyncTaskLoader<List<Question>> /*implements DbBroadCastReceiver.DbObserver*/ {

    // We hold a reference to the Loader’s data here.
    private List<Question> data;
    private QuestionDao    questionDao;
    private int            requestId;
    private UserDao        userDao;

    public QuestionLoaderDeprecated(Context ctx, int requestId) {
        // Loaders may be used across multiple Activitys (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().

        super(ctx);
        this.requestId = requestId;
        questionDao = (QuestionDao)DataBaseManager.getInstance().getHelper().getEntitiesDao(Question.class);
        userDao     = (UserDao)DataBaseManager.getInstance().getHelper().getEntitiesDao(User.class);
        /* mDb = (new DbHelper(ctx)).getReadableDatabase();*/
    }

    /****************************************************/
    /** (1) A task that performs the asynchronous load **/
    /****************************************************/

    @Override
    public List<Question> loadInBackground() {
        // This method is called on a background thread and should generate a
        // new set of data to be delivered back to the client.
        /*Cursor data = mDb.rawQuery(DbContract.Cars.SQL_JOIN,null);*/
        List<Question> data = null;
        try {
            data = questionDao.queryForEq(Question.REQUEST_ID,requestId);

            List<User> dataUser = userDao.getUsers(getUsersId(data));//userDao.();
            dataUser.size();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    private Set<Integer> getUsersId(List<Question> data) {
        Set<Integer> usersId = new HashSet<Integer>();
        for(Question question : data){
            usersId.add(question.getUserId());
        }
        return usersId;
    }

    /********************************************************/
    /** (2) Deliver the results to the registered listener **/
    /********************************************************/

    @Override
    public void deliverResult(List<Question>   data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<Question>   oldData = this.data;
        this.data = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    /*********************************************************/
    /** (3) Implement the Loader’s state-dependent behavior **/
    /*********************************************************/

    @Override
    protected void onStartLoading() {
        if (data != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(data);
        }

        // Begin monitoring the underlying data source.
/*        if (mObserver == null) {
            //mObserver = new SampleObserver();
            // TODO: register the observer
        }*/

        if (takeContentChanged() || data == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'data'.
        if (data != null) {
            releaseResources(data);
            data = null;
        }

        // The Loader is being reset, so we should stop monitoring for changes.
/*        if (mObserver != null) {
            // TODO: unregister the observer
            mObserver = null;
        }*/
    }

    @Override
    public void onCanceled(List<Question>   data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }

    private void releaseResources(List<Question>   data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
/*        data.close();*/
    }

    /*********************************************************************/
    /** (4) Observer which receives notifications when the data changes **/
    /*********************************************************************/

    // NOTE: Implementing an observer is outside the scope of this post (this example
    // uses a made-up "SampleObserver" to illustrate when/where the observer should
    // be initialized).

    // The observer could be anything so long as it is able to detect content changes
    // and report them to the loader with a call to onContentChanged(). For example,
    // if you were writing a Loader which loads a list of all installed applications
    // on the device, the observer could be a BroadcastReceiver that listens for the
    // ACTION_PACKAGE_ADDED intent, and calls onContentChanged() on the particular
    // Loader whenever the receiver detects that a new application has been installed.
    // Please don’t hesitate to leave a comment if you still find this confusing! :)

}

