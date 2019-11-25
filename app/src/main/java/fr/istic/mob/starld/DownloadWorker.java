package fr.istic.mob.starld;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadWorker extends Worker {

    String lastjsonResult = "";

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        /*
         * Go check the url to see if an update has occured
         */
        String jsonResult = getJsonFromUrl();

        //if(!jsonResult.equals(lastjsonResult)){
            try {
                JSONObject jsonObjectResult = new JSONObject(jsonResult);

                Log.d("My App", jsonObjectResult.toString());
            }
            catch(JSONException e){
                Log.e("My App", "Could not parse malformed JSON: \"" + jsonResult + "\"");
            }
        //}

        lastjsonResult = jsonResult;

        // Indicate whether the task finished successfully with the Result
        return Result.success();
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
