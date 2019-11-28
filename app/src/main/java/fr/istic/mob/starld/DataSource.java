package fr.istic.mob.starld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataSource {

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;
    File dowloadFolder;

    public DataSource (Context context) {
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

    public void initializeDatabase () {
        this.clearDatabase ();
        this.initializeTripTable();
        this.initializeRouteTable();
        this.initializeCalendarTable();
        this.initializeStopTable();
        this.initializeStopTimeTable();

    }

    public void clearDatabase () {
        database.execSQL("DELETE FROM "+StarContract.BusRoutes.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.StopTimes.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.Stops.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.Trips.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.StopTimes.CONTENT_PATH);
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
                String lineWithoutQuote = line.replace("\"", "");
                String[] columns = lineWithoutQuote.split(",");

                newValues.put(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME, columns[2]);
                newValues.put(StarContract.BusRoutes.BusRouteColumns.LONG_NAME, columns[3]);
                newValues.put(StarContract.BusRoutes.BusRouteColumns.DESCRIPTION, columns[4]);
                newValues.put(StarContract.BusRoutes.BusRouteColumns.TYPE, Integer.parseInt(columns[5]));
                newValues.put(StarContract.BusRoutes.BusRouteColumns.COLOR, columns[7]);
                newValues.put(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR, columns[8]);

                database.insert(StarContract.BusRoutes.CONTENT_PATH, null, newValues);
            }
            buffered.close();
            reader.close();
        }
        catch (IOException e) {
            System.out.println("Erreur "+e);
        }
    }

    public void initializeTripTable () {
        File file = new File(dowloadFolder, "trips.txt");
        try {
            ContentValues newValues = new ContentValues();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();

            while ((line = buffered.readLine()) != null) {
                String lineWithoutQuote = line.replace("\"", "");
                String[] columns = lineWithoutQuote.split(",");

                newValues.put(StarContract.Trips.TripColumns.ROUTE_ID, Integer.parseInt(columns[0]));
                newValues.put(StarContract.Trips.TripColumns.SERVICE_ID, Integer.parseInt(columns[1]));
                newValues.put(StarContract.Trips.TripColumns.HEADSIGN, columns[3]);
                newValues.put(StarContract.Trips.TripColumns.DIRECTION_ID, Integer.parseInt(columns[5]));
                newValues.put(StarContract.Trips.TripColumns.BLOCK_ID, columns[6]);
                newValues.put(StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE, Integer.parseInt(columns[8]));

                database.insert(StarContract.Trips.CONTENT_PATH, null, newValues);
            }

            buffered.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur " + e);
        }
    }

    public void initializeStopTable () {
        File file = new File(dowloadFolder, "stops.txt");
        try {
            ContentValues newValues = new ContentValues();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();

            while ((line = buffered.readLine()) != null) {
                String lineWithoutQuote = line.replace("\"", "");
                String[] columns = lineWithoutQuote.split(",");

                newValues.put(StarContract.Stops.StopColumns.NAME, columns[2]);
                newValues.put(StarContract.Stops.StopColumns.DESCRIPTION, columns[3]);
                newValues.put(StarContract.Stops.StopColumns.LATITUDE, columns[4]);
                newValues.put(StarContract.Stops.StopColumns.LONGITUDE, columns[5]);
                newValues.put(StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING, Integer.parseInt(columns[11]));

                database.insert(StarContract.Stops.CONTENT_PATH, null, newValues);
            }
            buffered.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur " + e);
        }
    }

    public void initializeStopTimeTable () {
        File file = new File(dowloadFolder, "stop_times.txt");
        try {
            ContentValues newValues = new ContentValues();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();

            while ((line = buffered.readLine()) != null) {
                String lineWithoutQuote = line.replace("\"", "");
                String[] columns = lineWithoutQuote.split(",");

                newValues.put(StarContract.StopTimes.StopTimeColumns.TRIP_ID, Integer.parseInt(columns[0]));
                newValues.put(StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME, columns[1]);
                newValues.put(StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME, columns[2]);
                newValues.put(StarContract.StopTimes.StopTimeColumns.STOP_ID, Integer.parseInt(columns[3]));
                newValues.put(StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE, Integer.parseInt(columns[4]));

                long id = database.insert(StarContract.StopTimes.CONTENT_PATH, null, newValues);

                if (id != -1) {
                    System.out.println("Tuple is inserted "+columns[0]);
                }
                else {
                    System.out.println("Tuple is not inserted");
                }
            }
            buffered.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur " + e);
        }
    }

    public void initializeCalendarTable () {
        File file = new File(dowloadFolder, "calendar.txt");
        try {
            ContentValues newValues = new ContentValues();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();

            while ((line = buffered.readLine()) != null) {
                String lineWithoutQuote = line.replace("\"", "");
                String[] columns = lineWithoutQuote.split(",");

                newValues.put(StarContract.Calendar.CalendarColumns.MONDAY, Integer.parseInt(columns[1]));
                newValues.put(StarContract.Calendar.CalendarColumns.TUESDAY, Integer.parseInt(columns[2]));
                newValues.put(StarContract.Calendar.CalendarColumns.WEDNESDAY, Integer.parseInt(columns[3]));
                newValues.put(StarContract.Calendar.CalendarColumns.THURSDAY, Integer.parseInt(columns[4]));
                newValues.put(StarContract.Calendar.CalendarColumns.FRIDAY, Integer.parseInt(columns[5]));
                newValues.put(StarContract.Calendar.CalendarColumns.SATURDAY, Integer.parseInt(columns[6]));
                newValues.put(StarContract.Calendar.CalendarColumns.SUNDAY, Integer.parseInt(columns[7]));
                newValues.put(StarContract.Calendar.CalendarColumns.START_DATE,Integer.parseInt (columns[8]));
                newValues.put(StarContract.Calendar.CalendarColumns.END_DATE, Integer.parseInt(columns[9]));

                database.insert(StarContract.Calendar.CONTENT_PATH, null, newValues);
            }
            buffered.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur " + e);
        }
    }

    public void updateDatabase (int i, int i1) {
        databaseHelper.onUpgrade(database,i, i1);
    }
}
