package tp.stackoverflow.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import tp.stackoverflow.dao.AnswerDao;
import tp.stackoverflow.database_entities.Answer;
import tp.stackoverflow.DataBaseManager;

/**
 * Created by korolkov on 12/3/13.
 */
public class AnswerLoaderDeprecated extends AsyncTaskLoader<List<Answer>>{

    private List<Answer>   data;
    private AnswerDao      answerDao;
    private int            questionId;

    public AnswerLoaderDeprecated(Context ctx, int questionId) {
        super(ctx);
        this.questionId = questionId;
        answerDao = (AnswerDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(Answer.class);
    }

    /****************************************************/
    /** (1) A task that performs the asynchronous load **/
    /****************************************************/

    @Override
    public List<Answer> loadInBackground() {
        List<Answer> data = null;
        try {
            data = answerDao.queryForEq(Answer.QUESTION_ID,questionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    /********************************************************/
    /** (2) Deliver the results to the registered listener **/
    /********************************************************/

    @Override
    public void deliverResult(List<Answer>   data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }
        List<Answer>   oldData = this.data;
        this.data = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

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
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
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
    public void onCanceled(List<Answer>   data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);
        releaseResources(data);
    }

    private void releaseResources(List<Answer>   data) {
    }


}


