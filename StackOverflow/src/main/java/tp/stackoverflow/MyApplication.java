package tp.stackoverflow;

/**
 * Created by korolkov on 11/25/13.
 */
import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataBaseManager.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DataBaseManager.getInstance().release();
    }

}