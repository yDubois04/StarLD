package fr.istic.mob.starld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import fr.istic.mob.starld.model.BusRoute;

import android.app.DatePickerDialog;
import android.app.DownloadManager;;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
    Spinner spinnerBus;
    Spinner spinnerSens;
    ProgressBar progressBar;
    String url;

    DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        chooseHour = findViewById(R.id.textViewHour);
        chooseDate = findViewById(R.id.textViewDate);
        validate = findViewById(R.id.buttonValidate);
        spinnerBus = findViewById(R.id.busSpinner);
        spinnerSens = findViewById(R.id.sensSpinner);
        progressBar = findViewById(R.id.progressBarDownload);
        calendar = GregorianCalendar.getInstance();

        Constraints constraints = new Constraints.Builder().build();
        PeriodicWorkRequest downloadRequest =
                new PeriodicWorkRequest.Builder(DownloadWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueue(downloadRequest);

        //Initializes Text View
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

        initializeSpinners();
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

    private void initializeSpinners(){
        //Initializes spinners

        if(!dataSource.equals(null)) {
            final ArrayList<BusRoute> buses = dataSource.getBusesName();
            SpinnerAdapter adapter = new SpinnerAdapter(buses, getApplicationContext());
            spinnerBus.setAdapter(adapter);

            spinnerBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ArrayList<String> sens = dataSource.getSensForBus(buses.get(i).getShortName());
                    ArrayAdapter<String> listSensAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, sens);
                    spinnerSens.setAdapter(listSensAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
        else{
            System.out.println("DataSource est null!!!");
        }
    }
}
