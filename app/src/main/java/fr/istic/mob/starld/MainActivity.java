package fr.istic.mob.starld;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    TextView chooseHour;
    TextView chooseDate;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseHour = findViewById(R.id.textViewHour);
        chooseDate = findViewById(R.id.textViewDate);
        calendar = GregorianCalendar.getInstance();
        this.initializeTextView();

        //Service
        //intent = new Intent(getApplicationContext(), ServiceStar.class);
        //startService(intent);

        Constraints constraints = new Constraints.Builder().build();

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


}
