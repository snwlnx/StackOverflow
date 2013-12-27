package tp.stackoverflow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;

import tp.stackoverflow.dao.AnswerDao;
import tp.stackoverflow.dao.QuestionDao;
import tp.stackoverflow.dao.RequestDao;
import tp.stackoverflow.dao.UserDao;
import tp.stackoverflow.database_entities.Answer;
import tp.stackoverflow.database_entities.DbEntity;
import tp.stackoverflow.database_entities.Question;
import tp.stackoverflow.database_entities.Request;
import tp.stackoverflow.database_entities.User;

/**
 * Created by korolkov on 11/25/13.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application
    private static final String DATABASE_NAME    = "tp.stackOverflow.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int    DATABASE_VERSION = 14;

    // the DAO object we use to access the Book table
    private Dao requestDao = null;

    private HashMap<Class<? extends DbEntity>,Dao> entityDaoMap = new HashMap<Class<? extends DbEntity>, Dao>();


    private void initDao() {
        try {
            entityDaoMap.put(Question.class,new QuestionDao(getConnectionSource(), Question.class));
            entityDaoMap.put(Request.class, new RequestDao(getConnectionSource(),Request.class));
            entityDaoMap.put(Answer.class, new AnswerDao(getConnectionSource(),Answer.class));
            entityDaoMap.put(User.class, new UserDao(getConnectionSource(),User.class));
            //TODO do all dao
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Dao getEntitiesDao(Class<? extends DbEntity> entityClass){
        return entityDaoMap.get(entityClass);
    }


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        initDao();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DataBaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Question.class);
            TableUtils.createTable(connectionSource, Request.class);
            TableUtils.createTable(connectionSource, Answer.class);
            TableUtils.createTable(connectionSource, User.class);
        } catch (SQLException e) {
            Log.e(DataBaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DataBaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Question.class, true);
            TableUtils.dropTable(connectionSource, Request.class, true);
            TableUtils.dropTable(connectionSource, Answer.class, true);
            TableUtils.dropTable(connectionSource, User.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DataBaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }



}
