package tp.stackoverflow.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import tp.stackoverflow.database_entities.Question;
import tp.stackoverflow.database_entities.User;

/**
 * Created by korolkov on 11/25/13.
 */

public class QuestionDao extends BaseDaoImpl<Question, Integer> {

    public QuestionDao(ConnectionSource connectionSource,
                       Class<Question>  dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Question> getQuestions( List<Integer> pageRequestId){
        //queryBuilder().
        List<Question> questions = null;
/*        QueryBuilder<Question, Integer> queryBuilder = this.queryBuilder();
        try {
            queryBuilder.where().in(Question.REQUEST_ID,pageRequestId);
            PreparedQuery<Question> preparedQuery = queryBuilder.prepare();
            questions =  query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        Integer[] requestIds  = new Integer[pageRequestId.size()];
        requestIds = pageRequestId.toArray(requestIds);

        try {
            questions = queryForEq(Question.REQUEST_ID,requestIds[requestIds.length - 1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

}