package tp.stackoverflow.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import tp.stackoverflow.database_entities.User;

/**
 * Created by korolkov on 12/10/13.
 */
public class UserDao extends BaseDaoImpl<User, Integer> {

    public UserDao(ConnectionSource connectionSource,
                      Class<User>  dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<User> getUsers(Set<Integer> usersId){
        //queryBuilder().
        List<User> users = null;
        QueryBuilder<User, Integer> queryBuilder = this.queryBuilder();
        try {
            queryBuilder.where().in(User.USER_ID,usersId);
            PreparedQuery<User> preparedQuery = queryBuilder.prepare();
            users =  query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}