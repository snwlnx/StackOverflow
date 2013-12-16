package tp.stackoverflow.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tp.stackoverflow.DataBaseManager;
import tp.stackoverflow.dao.AnswerDao;
import tp.stackoverflow.database_entities.Answer;
import tp.stackoverflow.dao.QuestionDao;
import tp.stackoverflow.dao.UserDao;
import tp.stackoverflow.database_entities.Question;
import tp.stackoverflow.database_entities.User;
import tp.stackoverflow.entity_view.ListViewEntity;


public abstract class MainLoader extends AsyncTaskLoader<List<ListViewEntity>> {

    protected QuestionDao questionDao;
    protected UserDao     userDao;
    protected AnswerDao   answerDao;

    protected List<ListViewEntity>  data;
    //protected AnswerDao       answerDao;
    protected int            entityId;

    public MainLoader(Context ctx, int entityId) {
        super(ctx);
        this.entityId = entityId;
        questionDao = (QuestionDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(Question.class);
        answerDao   = (AnswerDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(Answer.class);
        userDao     = (UserDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(User.class);
    }

    /****************************************************/
    /** (1) A task that performs the asynchronous load **/
    /****************************************************/

    @Override
    public  abstract List<ListViewEntity> loadInBackground(); /*{
        List<DbEntity>  data = new ArrayList<DbEntity>();
        try {
            data.addAll(answerDao.queryForEq(Answer.QUESTION_ID,entityId));
            //data = (DbEntity[])(answerDao.queryForEq(Answer.QUESTION_ID,entityId).toArray());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
*/
    /********************************************************/
    /** (2) Deliver the results to the registered listener **/
    /********************************************************/

    @Override
    public void deliverResult(List<ListViewEntity>   data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }
        List<ListViewEntity>   oldData = this.data;
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
    public void onCanceled(List<ListViewEntity>  data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);
        releaseResources(data);
    }

    private void releaseResources(List<ListViewEntity>   data) {
    }



    protected Set<Integer> getUsersId(List<Answer> answers) {

        Set<Integer> usersId = new HashSet<Integer>();
        if (answers.size() != 0){
            for(Answer answer : answers){
                usersId.add(answer.getUserId());
            }
        }
        return usersId;
    }



}


