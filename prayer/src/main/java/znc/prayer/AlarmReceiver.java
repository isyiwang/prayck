package znc.prayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by isaac on 10/18/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int promiseIndex = sp.getInt(MainActivity.PROMISE_INDEX_KEY, 0);

        notifyPromise(context, promiseIndex);

        sp.edit().putInt(MainActivity.PROMISE_INDEX_KEY, ++promiseIndex).apply();
    }

    private void notifyPromise(Context context, int promiseIndex) {
        promiseIndex = Math.min(Promises.PROMISES.size() - 1, promiseIndex);
        Promises.Promise promise = Promises.PROMISES.get(promiseIndex);

        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.putExtra(MainActivity.PROMISE_INDEX_KEY, promiseIndex);

        Uri soundUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("29 Promises")
                .setContentText(promise.title)
                .setContentIntent(
                        PendingIntent.getActivity(context, 0, activityIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .setSound(soundUri).setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat.from(context).notify(0, notification);
    }
}
