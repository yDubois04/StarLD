package fr.istic.mob.starld;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataSource {

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;
    private Context context;
    File dowloadFolder;

    public DataSource (Context context) {
        this.context = context;
        dowloadFolder = context.getExternalFilesDir(Context.DOWNLOAD_SERVICE);
        databaseHelper = new DatabaseHelper(context);
    }

    public void restart () {
        databaseHelper.onUpgrade(database, 1,1);
    }

    public void open () {
        database = databaseHelper.getWritableDatabase();
        databaseHelper.onCreate(database);
    }

    public void close () {
        databaseHelper.close();
        database.close();
    }

    public void initializeAllTable () {
       // this.initializeRouteTable();
        this.initializeTripTable();
        this.initializeStopTable();
        this.initializeStopTimeTable();
        this.initializeCalendarTable();
    }

    public void initializeRouteTable () {
        File file = new File(dowloadFolder, "routes.txt");

        try {
            ContentValues newValues = new ContentValues();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();

            while ((line = buffered.readLine()) != null) {
                String [] columns = line.split(",");
                newValues.put(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME, columns[2]);
                newValues.put(StarContract.BusRoutes.BusRouteColumns.LONG_NAME, columns[3]);
                newValues.put(StarContract.BusRoutes.BusRouteColumns.DESCRIPTION, columns[4]);
                newValues.put(StarContract.BusRoutes.BusRouteColumns.TYPE, columns[5]);
                newValues.put(StarContract.BusRoutes.BusRouteColumns.COLOR, columns[7]);
                newValues.put(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR, columns[8]);

                long id = database.insert(StarContract.BusRoutes.CONTENT_PATH, null, newValues);

                if (id == -1) {
                    System.out.println("Error");
                }
                else  {
                    System.out.println("Tuple is inserted");
                }
            }
            buffered.close();
            reader.close();
        }
        catch (IOException e) {
            System.out.println("Erreur "+e);
        }
    }

    public void initializeTripTable () {
        System.out.println("Début trip");
        File file = new File(dowloadFolder, "trips.txt");
        try {
            ContentValues newValues = new ContentValues();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();

            while ((line = buffered.readLine()) != null) {
                String[] columns = line.split(",");
                newValues.put(StarContract.Trips.TripColumns.ROUTE_ID, columns[0]);
                newValues.put(StarContract.Trips.TripColumns.SERVICE_ID, columns[1]);
                newValues.put(StarContract.Trips.TripColumns.HEADSIGN, columns[3]);
                newValues.put(StarContract.Trips.TripColumns.DIRECTION_ID, columns[5]);
                newValues.put(StarContract.Trips.TripColumns.BLOCK_ID, columns[6]);
                newValues.put(StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE, columns[8]);

                database.insert(StarContract.Trips.CONTENT_PATH, null, newValues);
            }
            buffered.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur " + e);
        }
        System.out.println("fin trip");
    }

    public void initializeStopTable () {
        System.out.println("Début stop");
        File file = new File(dowloadFolder, "stops.txt");
        try {
            ContentValues newValues = new ContentValues();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();

            while ((line = buffered.readLine()) != null) {
                String[] columns = line.split(",");
                newValues.put(StarContract.Stops.StopColumns.NAME, columns[3]);
                newValues.put(StarContract.Stops.StopColumns.DESCRIPTION, columns[4]);
                newValues.put(StarContract.Stops.StopColumns.LATITUDE, columns[5]);
                newValues.put(StarContract.Stops.StopColumns.LONGITUDE, columns[6]);
                newValues.put(StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING, columns[11]);

                database.insert(StarContract.Stops.CONTENT_PATH, null, newValues);
            }
            buffered.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur " + e);
        }
        System.out.println("fin stop");
    }

    public void initializeStopTimeTable () {
        System.out.println("Début stop times");
        File file = new File(dowloadFolder, "stop_times.txt");
        try {
            ContentValues newValues = new ContentValues();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();

            while ((line = buffered.readLine()) != null) {
                String[] columns = line.split(",");
                newValues.put(StarContract.StopTimes.StopTimeColumns.TRIP_ID, columns[0]);
                newValues.put(StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME, columns[1]);
                newValues.put(StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME, columns[2]);
                newValues.put(StarContract.StopTimes.StopTimeColumns.STOP_ID, columns[3]);
                newValues.put(StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE, columns[4]);

                database.insert(StarContract.StopTimes.CONTENT_PATH, null, newValues);
            }
            buffered.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur " + e);
        }
        System.out.println("fin stop times");
    }

    public void initializeCalendarTable () {
        System.out.println("Début calendar");
        File file = new File(dowloadFolder, "calendar.txt");
        try {
            ContentValues newValues = new ContentValues();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();

            while ((line = buffered.readLine()) != null) {
                String[] columns = line.split(",");
                newValues.put(StarContract.Calendar.CalendarColumns.MONDAY, columns[0]);
                newValues.put(StarContract.Calendar.CalendarColumns.TUESDAY, columns[1]);
                newValues.put(StarContract.Calendar.CalendarColumns.WEDNESDAY, columns[2]);
                newValues.put(StarContract.Calendar.CalendarColumns.THURSDAY, columns[3]);
                newValues.put(StarContract.Calendar.CalendarColumns.FRIDAY, columns[4]);
                newValues.put(StarContract.Calendar.CalendarColumns.SATURDAY, columns[5]);
                newValues.put(StarContract.Calendar.CalendarColumns.SUNDAY, columns[6]);
                newValues.put(StarContract.Calendar.CalendarColumns.START_DATE, columns[7]);
                newValues.put(StarContract.Calendar.CalendarColumns.END_DATE, columns[8]);

                database.insert(StarContract.Calendar.CONTENT_PATH, null, newValues);
            }
            buffered.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur " + e);
        }
        System.out.println("fin calendar");
    }
}
