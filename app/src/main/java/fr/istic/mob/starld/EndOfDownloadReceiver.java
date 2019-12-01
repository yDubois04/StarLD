package fr.istic.mob.starld;

import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EndOfDownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.DOWNLOAD_COMPLETE")) {
            Intent downloadIntent = new Intent(context, MainActivity.class);
            downloadIntent.putExtra("DLComplete", true);
            downloadIntent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(downloadIntent);

            stackBuilder.startActivities();
        }
    }
}

