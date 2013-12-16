package tp.stackoverflow.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import tp.stackoverflow.database_entities.Request;

/**
 * Created by korolkov on 11/25/13.
 */
public class RequestDao extends BaseDaoImpl<Request, Integer> {

    public RequestDao(ConnectionSource connectionSource,
                       Class<Request>  dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Request getReqForKey(String key) throws SQLException{
        //List<Request> requests1 = this.queryForAll();
        List<Request> requests = this.queryForEq("request_key", key);

        return (requests.size() == 0)? null:requests.get(0);
        //TODO
    }

}