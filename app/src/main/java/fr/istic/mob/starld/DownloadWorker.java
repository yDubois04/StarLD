package fr.istic.mob.starld;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class DownloadWorker extends Worker {

    String finValidite = "";
    String debutValidite = "";
    String url = "";

    String lastJSONString = "";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date finValiditeDate;
    Date debutValiditeDate;

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        //Get current date
        Date date = new Date();

        String jsonResult = getJsonFromUrl();
        String lastJSONString = getStringSaved("JSONResult.txt");

        if(!jsonResult.equals(lastJSONString)){
            try {
                JSONObject jsonObjectResult = new JSONObject(jsonResult);
                JSONArray arr = jsonObjectResult.getJSONArray("records");
                JSONObject firstFields = arr.getJSONObject(0).getJSONObject("fields");
                finValidite = firstFields.getString("finvalidite");
                debutValidite = firstFields.getString("debutvalidite");

                try {
                    finValiditeDate = dateFormat.parse(finValidite);
                    debutValiditeDate = dateFormat.parse(debutValidite);
                }
                catch(ParseException e){
                    e.printStackTrace();
                }

                //If first object in records is still valid, take the url from it
                if(date.compareTo(finValiditeDate) <= 0 && date.compareTo(debutValiditeDate) >= 0){
                    url = firstFields.getString("url");
                    saveStringInMemory("JSONResult.txt", jsonResult);
                }
                else{
                    JSONObject secondFields = arr.getJSONObject(1).getJSONObject("fields");
                    finValidite = secondFields.getString("finvalidite");
                    debutValidite = secondFields.getString("debutvalidite");

                    try {
                        finValiditeDate = dateFormat.parse(finValidite);
                        debutValiditeDate = dateFormat.parse(debutValidite);
                    }
                    catch(ParseException e){
                        e.printStackTrace();
                    }

                    //if the second object in records is still valid, take the url from it
                    if(date.compareTo(finValiditeDate) <= 0 && date.compareTo(debutValiditeDate) >= 0) {
                        url = secondFields.getString("url");

                        saveStringInMemory("JSONResult.txt", jsonResult);
                    }

                    //If none are valid, we keep the old one
                }
            }
            catch(JSONException e){
                System.out.println("Could not parse malformed JSON: \"" + jsonResult + "\"");
            }
        }

        if(!url.equals("")){
            saveStringInMemory("currentURL.txt", url);
        }

        System.out.println("URL ::::: " + url);
        System.out.println("CurrentURL ::::: " + getStringSaved("currentURL.txt"));

        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }

    private void saveStringInMemory (String fileName, String JSONToSave) {
        FileOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        String nameFile = fileName;
        try {
            outputStream = getApplicationContext().openFileOutput(nameFile, MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(JSONToSave);
            outputStream.flush();
            outputStream.close();
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.println("Erreur ! : " + e);
        }
    }

    private String getStringSaved(String fileName){
        String result = "";

        try {
            FileInputStream input = getApplicationContext().openFileInput(fileName);
            ObjectInputStream inputStream = new ObjectInputStream(input);
            result = (String) inputStream.readObject();
        }
        catch (Exception e) {
            System.out.println("Erreur ! : " +e);
        }

        return result;
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
