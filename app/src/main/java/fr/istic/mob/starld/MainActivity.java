package fr.istic.mob.starld;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView chooseHour;
    TextView chooseDate;
    Calendar calendar;
    Button validate;
    Spinner spinnerBus;
    Spinner spinnerSens;
    ProgressBar progressBar;
    private DataSource dataSource;
    String url;

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

        dataSource = new DataSource(this);
        dataSource.open();
        //dataSource.initializeDatabase();

       /* Constraints constraints = new Constraints.Builder().build();
        PeriodicWorkRequest downloadRequest =
                new PeriodicWorkRequest.Builder(DownloadWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();


        WorkManager.getInstance(getApplicationContext())
                .enqueue(downloadRequest);

        if(getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString("url");
            System.out.println("URL in Main Activity : " + url);
            if (url != null && url != "") {
                startDownload();
            }
        }*/

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

        //Initializes spinners
        final ArrayList<String> buses = dataSource.getBusesName ();
        final ArrayAdapter<String> listBusAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,buses);
        spinnerBus.setAdapter(listBusAdapter);

        spinnerBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> sens = dataSource.getSensForBus (buses.get(i));
                ArrayAdapter<String> listSensAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item, sens);
                spinnerSens.setAdapter(listSensAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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
}
