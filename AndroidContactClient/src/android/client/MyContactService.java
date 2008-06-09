package android.client;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.ipc.Message;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.widget.Toast;
import android.client.R;

public class MyContactService extends Service  {
	private NotificationManager mNM;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        MyContactService getService() {
            return MyContactService.this;
        }
    }

    @Override
    protected void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    protected void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(R.string.local_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // This is who should be launched if the user selects our notification.
        Intent contentIntent = new Intent(this, MyContatsClient.class);

        // This is who should be launched if the user selects the app icon in the notification,
        // (in this case, we launch the same activity for both)
        Intent appIntent = new Intent(this, MyContatsClient.class);

        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        mNM.notify(R.string.local_service_started,  // we use a string id because it is a unique
                                                    // number.  we use it later to cancel the
                                                    // notification
                   new Notification(
                       this,                        // our context
                       R.drawable.icon,      // the icon for the status bar
                       text,                        // the text to display in the ticker
                       System.currentTimeMillis(),  // the timestamp for the notification
                       getText(R.string.local_service_notification), // the title for the notification
                       text,                        // the details to display in the notification
                       contentIntent,               // the contentIntent (see above)
                       R.drawable.icon,  // the app icon
                       getText(R.string.app_name), // the name of the app
                       appIntent));                 // the appIntent (see above)
    }
}
