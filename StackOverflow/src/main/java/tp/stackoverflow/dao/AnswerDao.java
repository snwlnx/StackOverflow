package tp.stackoverflow.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import tp.stackoverflow.database_entities.Answer;

/**
 * Created by korolkov on 12/2/13.
 */
public class AnswerDao extends BaseDaoImpl<Answer, Integer> {

    public AnswerDao(ConnectionSource connectionSource,
                      Class<Answer>  dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
