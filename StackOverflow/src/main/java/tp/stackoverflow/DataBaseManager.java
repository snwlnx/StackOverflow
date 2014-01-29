package tp.stackoverflow;

/**
 * Created by korolkov on 11/25/13.
 */
import com.j256.ormlite.android.apptools.OpenHelperManager;
import android.content.Context;

public class DataBaseManager {

    private static volatile DataBaseManager instance;
    private        volatile DataBaseHelper  helper;

    private DataBaseManager() {}

    public static DataBaseManager getInstance() {
        if (instance == null) {
            synchronized (DataBaseManager.class) {
                if (instance == null) {
                    instance = new DataBaseManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context){
        if(helper == null)
            helper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
    }

    public void release(){
        if(helper != null)
            OpenHelperManager.releaseHelper();
    }

    public DataBaseHelper getHelper() {
        return helper;
    }
}