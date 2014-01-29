package tp.stackoverflow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by korolkov on 1/21/14.
 */
public class NetworkSwitcher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            return;
        }
        NetworkInfo networkInfo =
                (NetworkInfo)intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        if (networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.d(TAG, "Network type: " + networkInfo.getTypeName() +
                        " Network subtype: " + networkInfo.getSubtypeName());
                getOwnIpAddress();
                mClient.updateUnicastSocket(mOwnAddress, mUnicastPort);
            }
        }
        else {
            Log.e(TAG, "Network connection lost");
        }
    }
}