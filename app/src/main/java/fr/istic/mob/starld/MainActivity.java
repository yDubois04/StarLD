package fr.istic.mob.starld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import fr.istic.mob.starld.database.DataSource;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ProgressBar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {

    Button validate;
    ProgressBar progressBar;
    String url;

    DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        validate = findViewById(R.id.buttonValidate);
        progressBar = findViewById(R.id.progressBarDownload);

        Constraints constraints = new Constraints.Builder().build();
        PeriodicWorkRequest downloadRequest =
                new PeriodicWorkRequest.Builder(DownloadWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueue(downloadRequest);

        dataSource = new DataSource(getApplicationContext());
        dataSource.open();

        if(getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString("url");
            System.out.println("URL in Main Activity : " + url);
            if (url != null && url != "") {
                startDownload();
            }
            else if(getIntent().getExtras().getBoolean("DLComplete")){
                unzip();
                dataSource.initializeDatabase();
            }
        }
    }
    public void startDownload(){
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(getApplicationContext().getString(R.string.notif_dowload_title));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setMimeType("zip");
        request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_DOWNLOADS,"Infos.zip");

        DownloadManager manager = (DownloadManager)getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public void unzip(){
        byte[] buffer = new byte[1024];

        try {
            File dest = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(dest, "Infos.zip");
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = zipInputStream.getNextEntry();


            while (entry != null) {
                String fileName = entry.getName();
                File newFile = new File(dest + File.separator + fileName);

                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int i;
                while ((i = zipInputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, i);
                }
                fos.close();
                entry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e);
        }
    }
}
