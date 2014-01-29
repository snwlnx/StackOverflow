package tp.stackoverflow.loaders;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tp.stackoverflow.database_entities.User;
import tp.stackoverflow.dao.AnswerDao;
import tp.stackoverflow.dao.QuestionDao;
import tp.stackoverflow.database_entities.Answer;
import tp.stackoverflow.database_entities.Question;
import tp.stackoverflow.DataBaseManager;
import tp.stackoverflow.entity_view.MainQuestion;
import tp.stackoverflow.entity_view.ListViewEntity;

/**
 * Created by korolkov on 12/7/13.
 */
public class AnswerLoader extends MainLoader {


    public AnswerLoader(Context ctx, int questionId) {
        super(ctx, questionId);
        questionDao = (QuestionDao) DataBaseManager.getInstance().getHelper().getEntitiesDao(Question.class);
        answerDao   = (AnswerDao)   DataBaseManager.getInstance().getHelper().getEntitiesDao(Answer.class);
    }


    @Override
    public List<ListViewEntity> loadInBackground() {
        return getViewEntities();
    }


    private List<ListViewEntity> getViewEntities() {
        List<User>     dataUser     = new ArrayList<User>();
        List<Answer> answers        = new ArrayList<Answer>();

        try {
            answers.addAll(questionDao.queryForEq(Question.QUESTION_ID, entityId));
            answers.addAll(answerDao.queryForEq(Answer.QUESTION_ID, entityId));
            dataUser     = userDao.getUsers(getUsersId(answers));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ViewDataCollector collector = new ViewDataCollector(answers,dataUser);
        return   collector.getViewData();

    }


    private class ViewDataCollector {

        private List<Answer>   answers;
        private List<User>     users;

        ViewDataCollector(List<Answer> answers, List<User> users){
            this.answers  = answers;
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
            for (Answer answer : answers) {
                for (Answer ans : answers) {
                    if (answer.getId() != ans.getId() && ans.getUserId() == answer.getUserId()) {
                        repeat++;
                    }
                }
            }
            return repeat/2;
        }

        public  List<ListViewEntity>  getViewData(){
            List<ListViewEntity> viewEntities = new ArrayList<ListViewEntity>();
            int answerSize = answers.size(),userSize = users.size();
            if (answerSize >0 && userSize == answerSize -repeatUsers()) {
                for (Answer answer : answers) {
                    viewEntities.add(new MainQuestion(answer,findUser(answer.getUserId())));
                }
            } else if (answers.size() > 0) {
                for (Answer answer : answers) {
                    viewEntities.add(new MainQuestion(answer));
                }
            }
            return viewEntities;
        }
    }
};


