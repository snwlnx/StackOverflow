package tp.stackoverflow.loaders;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import tp.stackoverflow.database_entities.Answer;
import tp.stackoverflow.dao.QuestionDao;
import tp.stackoverflow.dao.UserDao;
import tp.stackoverflow.entity_view.MainQuestion;
import tp.stackoverflow.database_entities.Question;
import tp.stackoverflow.database_entities.User;
import tp.stackoverflow.DataBaseManager;
import tp.stackoverflow.entity_view.ListViewEntity;

/**
 * Created by korolkov on 12/12/13.
 */
public class QuestionLoader extends MainLoader {

    private  List<Integer> pageRequestsID;

    public QuestionLoader(Context ctx,int requestID, List<Integer> pRequestsID) {
        super(ctx,requestID);
        userDao         = (UserDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(User.class);
        questionDao     = (QuestionDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(Question.class);
        pageRequestsID  =  pRequestsID;
    }



    @Override
    public List<ListViewEntity> loadInBackground() {
        return getViewEntities();
    }

    private List<ListViewEntity> getViewEntities() {
        List<Question>  dataQuestion = questionDao.getQuestions(pageRequestsID);
        List<User>      dataUser     = userDao.getUsers(getUsersId(new ArrayList<Answer>(dataQuestion)));
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


        private int repeatUsers(){
            int repeat = 0;
            for (Answer answer : questions) {
                for (Answer ans : questions) {
                    if (answer.getEntityId() != ans.getEntityId() && ans.getUserId() == answer.getUserId()) {
                        repeat++;
                    }
                }
            }
            return repeat/2;
        }

        public  List<ListViewEntity>  getViewData(){
            List<ListViewEntity> viewEntities = new ArrayList<ListViewEntity>();

            if (questions.size() > 0 && (users.size() == (questions.size() - repeatUsers()))) {
                for (Question question : questions) {
                    viewEntities.add(new MainQuestion(question,findUser(question.getUserId())));
                }
            } else if (questions.size() > 0) {
                for (Question question : questions) {
                    viewEntities.add(new MainQuestion(question));
                }
            }
            return viewEntities;
        }
    }
}
