package fr.istic.mob.starld;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadWorker extends Worker {

    String lastIdJson = "";
    String CHANNEL_ID = "Channel";
    int idNotif  = 5;

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        /*
         * Go check the url to see if an update has occured
         */
        String jsonResult = getJsonFromUrl();

        if(jsonResult.equals("bfj")){
            try {
                JSONObject jsonObjectResult = new JSONObject(jsonResult);
                JSONArray data = jsonObjectResult.getJSONArray("records");
                JSONObject idTable = data.getJSONObject(0);

                lastIdJson = idTable.getString("recordid");
            }
            catch(JSONException e){
                System.out.println("Erreur "+e);
            }
        }
        else {
            createNotification ();
        }
        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }

    private void createNotification () {
       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,intent,0);


        NotificationCompat.Builder notif = new NotificationCompat.Builder(getApplicationContext(), "Notification")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Nouvelles informations à télécharger")
                .setContentText("Cliquez sur la notification pour télécharger les nouvelles données")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(idNotif++,notif.build());
    }

    private String getJsonFromUrl(){
        String urlString = "https://data.explore.star.fr/api/records/1.0/search/?dataset=tco-busmetro-horaires-gtfs-versions-td";
        URL url;
        StringBuffer response = new StringBuffer();
        HttpURLConnection conn = null;

        try {
            url = new URL(urlString);

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }

            //json in string format
            String responseJSON = response.toString();

            return responseJSON;
        }
    }
}
