package tp.stackoverflow.loaders;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tp.stackoverflow.database_entities.Answer;
import tp.stackoverflow.dao.QuestionDao;
import tp.stackoverflow.dao.UserDao;
import tp.stackoverflow.entity_view.FullQuestion;
import tp.stackoverflow.database_entities.Question;
import tp.stackoverflow.database_entities.User;
import tp.stackoverflow.DataBaseManager;
import tp.stackoverflow.entity_view.ListViewEntity;

/**
 * Created by korolkov on 12/12/13.
 */
public class QuestionLoader extends MainLoader {



    public QuestionLoader(Context ctx, int requestID) {
        super(ctx,requestID);
        questionDao = (QuestionDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(Question.class);
        userDao = (UserDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(User.class);
    }



    @Override
    public List<ListViewEntity> loadInBackground() {
        return getViewEntities();
    }


    private List<ListViewEntity> getViewEntities() {
        List<Question> dataQuestion = new ArrayList<Question>();
        List<User>     dataUser     = new ArrayList<User>();

        try {
            dataQuestion = questionDao.queryForEq(Question.REQUEST_ID,entityId);
            dataUser     = userDao.getUsers(getUsersId(new ArrayList<Answer>(dataQuestion)));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ViewDataCollector collector = new ViewDataCollector(dataQuestion,dataUser);
        return   collector.getViewData();

    }




    private class ViewDataCollector {

        private List<Question> questions;
        private List<User>     users;

        ViewDataCollector(List<Question> questions, List<User> users){
            this.questions = questions;
            this.users     = users;
        }

        private User findUser(int userId){
            User necessaryUser = null;
            if (users.size() == 0 ) return necessaryUser;
            for (User user : users){
                if (user.getUserId() == userId) {
                    necessaryUser = user;
                }
            }
            return necessaryUser;
        }

        public  List<ListViewEntity>  getViewData(){
            List<ListViewEntity> viewEntities = new ArrayList<ListViewEntity>();

            if (users.size() != 0 && questions.size() != 0) {
                for (Question question : questions) {
                    viewEntities.add(new FullQuestion(question,findUser(question.getUserId())));

                }
            } else if (questions.size() != 0) {
                for (Question question : questions) {
                    viewEntities.add(new FullQuestion(question));
                }
            }
            return viewEntities;
        }
    }
}
