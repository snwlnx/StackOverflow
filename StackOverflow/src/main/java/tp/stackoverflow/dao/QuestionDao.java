package tp.stackoverflow.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import tp.stackoverflow.database_entities.Question;

/**
 * Created by korolkov on 11/25/13.
 */

public class QuestionDao extends BaseDaoImpl<Question, Integer> {

    public QuestionDao(ConnectionSource connectionSource,
                       Class<Question>  dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}