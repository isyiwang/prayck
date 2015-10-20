package znc.prayer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

/**
 * Created by isaac on 10/18/15.
 */
public class BootReceiver extends BroadcastReceiver {

    public static final String HOUR_KEY = "SP_HOUR_KEY";
    public static final String MINUTE_KEY = "SP_MINUTE_KEY";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            setupNotification(context);
        }
    }

    public static void setupNotification(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, sp.getInt(HOUR_KEY, 9));
        calendar.set(Calendar.MINUTE, sp.getInt(MINUTE_KEY, 0));

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);
    }
}
