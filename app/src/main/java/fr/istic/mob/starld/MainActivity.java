package fr.istic.mob.starld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import android.app.DatePickerDialog;
import android.app.DownloadManager;;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {

    TextView chooseHour;
    TextView chooseDate;
    Calendar calendar;
    Button validate;
    int idNotif = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseHour = findViewById(R.id.textViewHour);
        chooseDate = findViewById(R.id.textViewDate);
        calendar = GregorianCalendar.getInstance();
        validate = findViewById(R.id.buttonValidate);
        this.initializeTextView();

        Constraints constraints = new Constraints.Builder().build();

        saveFile();

        PeriodicWorkRequest downloadRequest =
                new PeriodicWorkRequest.Builder(DownloadWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueue(downloadRequest);
    }
    private void initializeTextView () {
        chooseHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        chooseHour.setText(hour + " : "+minutes);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int annee, int mois, int jour) {
                        chooseDate.setText(jour + " " + mois+ " "+annee);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void createNotification () {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Notif")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("kghrtkgho")
                .setContentText("klhgrktghrt").setStyle(new NotificationCompat.BigTextStyle().bigText("ihgrtigh"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notification = NotificationManagerCompat.from(this);
        notification.notify(idNotif, builder.build());
    }

    private long saveFile () {

        long download = 0;

        String url = "http://ftp.keolis-rennes.com/opendata/tco-busmetro-horaires-gtfs-versions-td/attachments/GTFS_2019.4.0_20191223_20200105.zip";
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Téléchargement des données");
        request.setDescription("Téléchargement");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setMimeType("zip");
        request.setDestinationInExternalFilesDir(getApplicationContext(),Environment.DIRECTORY_DOWNLOADS,"Test.zip");


        DownloadManager manager = (DownloadManager)getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        download = manager.enqueue(request);

        return download;
    }

    private void dezipFile () {
        byte [] buffer = new byte [1024];

        try {
            File dest = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(dest, "Infos.zip");
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = zipInputStream.getNextEntry();


            while (entry != null) {
                String fileName = entry.getName();
                File newFile = new File(dest+File.separator+fileName);

                new File (newFile.getParent()).mkdirs();

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
        }
        catch (IOException e) {
            System.out.println("Erreur "+e);
        }
    }
}
