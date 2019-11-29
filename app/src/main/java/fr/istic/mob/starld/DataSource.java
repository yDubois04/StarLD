package fr.istic.mob.starld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Statement;
import java.util.ArrayList;

public class DataSource {

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;
    private File dowloadFolder;

    public DataSource (Context context) {
        dowloadFolder = context.getExternalFilesDir(Context.DOWNLOAD_SERVICE);
        databaseHelper = new DatabaseHelper(context);
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
        this.initializeTable("routes.txt", StarContract.BusRoutes.CONTENT_PATH);
        this.initializeTable("trips.txt", StarContract.Trips.CONTENT_PATH);
        this.initializeTable("stops.txt", StarContract.Stops.CONTENT_PATH);
        this.initializeTable("calendar.txt", StarContract.Calendar.CONTENT_PATH);
        //this.initializeTable("stop_times.txt", StarContract.StopTimes.CONTENT_PATH);
    }

    public void clearDatabase () {
        database.execSQL("DELETE FROM "+StarContract.BusRoutes.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.StopTimes.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.Stops.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.Trips.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.StopTimes.CONTENT_PATH);
    }

    public void initializeTable (String fileName, String nameTable) {
        File file = new File(dowloadFolder, fileName);
        ArrayList<ContentValues> list = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();

            while ((line = buffered.readLine()) != null) {
                String lineWithoutQuote = line.replace("\"", "");
                String[] columnsContent = lineWithoutQuote.split(",");

                if (fileName.equals("routes.txt")) {
                    ContentValues contentValues = fillContentValue (StarContract.BusRoutes.CONTENT_PATH, columnsContent);
                    list.add(contentValues);
                }
                else if (fileName.equals("trips.txt")) {
                    ContentValues contentValues = fillContentValue (StarContract.Trips.CONTENT_PATH, columnsContent);
                    list.add(contentValues);
                }
                else if (fileName.equals("stops.txt")) {
                    ContentValues contentValues = fillContentValue (StarContract.Stops.CONTENT_PATH, columnsContent);
                    list.add(contentValues);
                }
                else if (fileName.equals("calendar.txt")) {
                    ContentValues contentValues = fillContentValue (StarContract.Calendar.CONTENT_PATH, columnsContent);
                    list.add(contentValues);
                }
                else if (fileName.equals("stop_times.txt")) {
                    ContentValues contentValues = fillContentValue (StarContract.StopTimes.CONTENT_PATH, columnsContent);
                    list.add(contentValues);
                }
            }

            insertInDB (list,nameTable);
            buffered.close();
            reader.close();
        }

        catch (IOException e) {
            System.out.println("Erreur "+e);
        }
    }

    private ContentValues fillContentValue (String tableName, String [] content) {
        ContentValues contentValues = new ContentValues();

        if (tableName.equals(StarContract.BusRoutes.CONTENT_PATH)) {
            contentValues.put(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME, content[2]);
            contentValues.put(StarContract.BusRoutes.BusRouteColumns.LONG_NAME, content[3]);
            contentValues.put(StarContract.BusRoutes.BusRouteColumns.DESCRIPTION, content[4]);
            contentValues.put(StarContract.BusRoutes.BusRouteColumns.TYPE, Integer.parseInt(content[5]));
            contentValues.put(StarContract.BusRoutes.BusRouteColumns.COLOR, content[7]);
            contentValues.put(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR, content[8]);
        }
        else if (tableName.equals(StarContract.Trips.CONTENT_PATH)) {
            contentValues.put(StarContract.Trips.TripColumns.ROUTE_ID, Integer.parseInt(content[0]));
            contentValues.put(StarContract.Trips.TripColumns.SERVICE_ID, Integer.parseInt(content[1]));
            contentValues.put(StarContract.Trips.TripColumns.HEADSIGN, content[3]);
            contentValues.put(StarContract.Trips.TripColumns.DIRECTION_ID, Integer.parseInt(content[5]));
            contentValues.put(StarContract.Trips.TripColumns.BLOCK_ID, content[6]);
            contentValues.put(StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE, Integer.parseInt(content[8]));
        }
        else if (tableName.equals(StarContract.Stops.CONTENT_PATH)) {
            contentValues.put(StarContract.Stops.StopColumns.NAME, content[2]);
            contentValues.put(StarContract.Stops.StopColumns.DESCRIPTION, content[3]);
            contentValues.put(StarContract.Stops.StopColumns.LATITUDE, content[4]);
            contentValues.put(StarContract.Stops.StopColumns.LONGITUDE, content[5]);
            contentValues.put(StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING, Integer.parseInt(content[11]));
        }
        else if (tableName.equals(StarContract.Calendar.CONTENT_PATH)) {
            contentValues.put(StarContract.Calendar.CalendarColumns.MONDAY, Integer.parseInt(content[1]));
            contentValues.put(StarContract.Calendar.CalendarColumns.TUESDAY, Integer.parseInt(content[2]));
            contentValues.put(StarContract.Calendar.CalendarColumns.WEDNESDAY, Integer.parseInt(content[3]));
            contentValues.put(StarContract.Calendar.CalendarColumns.THURSDAY, Integer.parseInt(content[4]));
            contentValues.put(StarContract.Calendar.CalendarColumns.FRIDAY, Integer.parseInt(content[5]));
            contentValues.put(StarContract.Calendar.CalendarColumns.SATURDAY, Integer.parseInt(content[6]));
            contentValues.put(StarContract.Calendar.CalendarColumns.SUNDAY, Integer.parseInt(content[7]));
            contentValues.put(StarContract.Calendar.CalendarColumns.START_DATE,Integer.parseInt (content[8]));
            contentValues.put(StarContract.Calendar.CalendarColumns.END_DATE, Integer.parseInt(content[9]));
        }
        else if (tableName.equals(StarContract.StopTimes.CONTENT_PATH)) {
            contentValues.put(StarContract.StopTimes.StopTimeColumns.TRIP_ID, Integer.parseInt(content[0]));
            contentValues.put(StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME, content[1]);
            contentValues.put(StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME, content[2]);
            contentValues.put(StarContract.StopTimes.StopTimeColumns.STOP_ID, Integer.parseInt(content[3]));
            contentValues.put(StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE, Integer.parseInt(content[4]));
        }
        return contentValues;
    }

/*
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

                database.insert(StarContract.StopTimes.CONTENT_PATH, null, newValues);
            }
            buffered.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Erreur " + e);
        }
    }*/

    public void updateDatabase (int i, int i1) {
        databaseHelper.onUpgrade(database,i, i1);
    }

    private String getInsertRequete (String tableName) {
        String requete = "";

        if (tableName.equals(StarContract.BusRoutes.CONTENT_PATH)) {
            requete =  "INSERT INTO "+StarContract.BusRoutes.CONTENT_PATH+  "("+
                    StarContract.BusRoutes.BusRouteColumns.SHORT_NAME + ", "+  StarContract.BusRoutes.BusRouteColumns.LONG_NAME + ", "+
                    StarContract.BusRoutes.BusRouteColumns.DESCRIPTION + ", "+ StarContract.BusRoutes.BusRouteColumns.TYPE + ", "+
                    StarContract.BusRoutes.BusRouteColumns.COLOR + ", "+ StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR + ") VALUES (?,?,?,?,?,?)";
        }
        else if (tableName.equals(StarContract.Trips.CONTENT_PATH)) {
            requete = "INSERT INTO "+ StarContract.Trips.CONTENT_PATH +
                    "(" + StarContract.Trips.TripColumns.ROUTE_ID+ ","+ StarContract.Trips.TripColumns.SERVICE_ID+ ","+
                    StarContract.Trips.TripColumns.HEADSIGN+ ","+ StarContract.Trips.TripColumns.DIRECTION_ID+ ","+
                    StarContract.Trips.TripColumns.BLOCK_ID+ ","+ StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE+ ") VALUES (?,?,?,?,?,?)";
        }
        else if (tableName.equals(StarContract.Stops.CONTENT_PATH)) {
            requete = "INSERT INTO "+ StarContract.Stops.CONTENT_PATH + "("+
                    StarContract.Stops.StopColumns.NAME + ", "+ StarContract.Stops.StopColumns.DESCRIPTION + ", "+
                    StarContract.Stops.StopColumns.LATITUDE + ", "+ StarContract.Stops.StopColumns.LONGITUDE +  ", "+
                    StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING + ") VALUES (?,?,?,?,?)";
        }
        else if (tableName.equals(StarContract.Calendar.CONTENT_PATH)) {
            requete = "INSERT INTO "+ StarContract.Calendar.CONTENT_PATH + "("+
                    StarContract.Calendar.CalendarColumns.MONDAY+","+ StarContract.Calendar.CalendarColumns.TUESDAY+","+
                    StarContract.Calendar.CalendarColumns.WEDNESDAY+","+ StarContract.Calendar.CalendarColumns.THURSDAY+","+
                    StarContract.Calendar.CalendarColumns.FRIDAY+","+ StarContract.Calendar.CalendarColumns.SATURDAY+","+
                    StarContract.Calendar.CalendarColumns.SUNDAY+" ,"+ StarContract.Calendar.CalendarColumns.START_DATE+","+
                    StarContract.Calendar.CalendarColumns.END_DATE+ ") VALUES (?,?,?,?,?,?,?,?,?)";
        }
        else if (tableName.equals(StarContract.StopTimes.CONTENT_PATH)) {
            requete = "INSERT INTO "+ StarContract.StopTimes.CONTENT_PATH + "("+
                    StarContract.StopTimes.StopTimeColumns.TRIP_ID + ", "+ StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + ", "+
                    StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME + ", "+ StarContract.StopTimes.StopTimeColumns.STOP_ID+ ", "+
                    StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE+ ") VALUES (?,?,?,?,?)";
        }
        return requete;
    }

    public void insertInDB (ArrayList<ContentValues> listValue, String nameTable) {
        String req = getInsertRequete(nameTable);

        database.beginTransactionNonExclusive();
        SQLiteStatement statement = database.compileStatement(req);

        for (ContentValues content : listValue) {
            if (nameTable.equals(StarContract.BusRoutes.CONTENT_PATH)) {
                statement.bindString(1,(String)content.get(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME));
                statement.bindString(2,(String)content.get(StarContract.BusRoutes.BusRouteColumns.LONG_NAME));
                statement.bindString(3,(String)content.get(StarContract.BusRoutes.BusRouteColumns.DESCRIPTION));
                statement.bindLong(4,(Integer) content.get(StarContract.BusRoutes.BusRouteColumns.TYPE));
                statement.bindString(5,(String)content.get(StarContract.BusRoutes.BusRouteColumns.COLOR));
                statement.bindString(6,(String)content.get(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR));
            }
            else if (nameTable.equals(StarContract.Trips.CONTENT_PATH)) {
                statement.bindLong(1,(Integer)content.get(StarContract.Trips.TripColumns.ROUTE_ID));
                statement.bindLong(2,(Integer)content.get(StarContract.Trips.TripColumns.SERVICE_ID));
                statement.bindString(3,(String)content.get(StarContract.Trips.TripColumns.HEADSIGN));
                statement.bindLong(4,(Integer) content.get(StarContract.Trips.TripColumns.DIRECTION_ID));
                statement.bindString(5,(String)content.get(StarContract.Trips.TripColumns.BLOCK_ID));
                statement.bindLong(6,(Integer)content.get(StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE));
            }
            else if (nameTable.equals(StarContract.Stops.CONTENT_PATH)) {
                statement.bindString(1,(String) content.get(StarContract.Stops.StopColumns.NAME));
                statement.bindString(2,(String) content.get(StarContract.Stops.StopColumns.DESCRIPTION));
                statement.bindString(3,(String)content.get(StarContract.Stops.StopColumns.LATITUDE));
                statement.bindString(4,(String) content.get(StarContract.Stops.StopColumns.LONGITUDE));
                statement.bindLong(5,(Integer)content.get(StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING));
            }
            else if (nameTable.equals(StarContract.Calendar.CONTENT_PATH)) {
                statement.bindLong(1,(Integer) content.get(StarContract.Calendar.CalendarColumns.MONDAY));
                statement.bindLong(2,(Integer) content.get(StarContract.Calendar.CalendarColumns.TUESDAY));
                statement.bindLong(3,(Integer)content.get(StarContract.Calendar.CalendarColumns.WEDNESDAY));
                statement.bindLong(4,(Integer) content.get(StarContract.Calendar.CalendarColumns.THURSDAY));
                statement.bindLong(5,(Integer)content.get(StarContract.Calendar.CalendarColumns.FRIDAY));
                statement.bindLong(6,(Integer) content.get(StarContract.Calendar.CalendarColumns.SATURDAY));
                statement.bindLong(7,(Integer) content.get(StarContract.Calendar.CalendarColumns.SUNDAY));
                statement.bindLong(8,(Integer)content.get(StarContract.Calendar.CalendarColumns.START_DATE));
                statement.bindLong(9,(Integer) content.get(StarContract.Calendar.CalendarColumns.END_DATE));
            }
            else if (nameTable.equals(StarContract.StopTimes.CONTENT_PATH)) {
                statement.bindLong(1,(Integer) content.get(StarContract.StopTimes.StopTimeColumns.TRIP_ID));
                statement.bindString(2,(String) content.get(StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME));
                statement.bindString(3,(String) content.get(StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME));
                statement.bindLong(4,(Integer) content.get(StarContract.StopTimes.StopTimeColumns.STOP_ID));
                statement.bindLong(5,(Integer)content.get(StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE));
            }

            statement.execute();
            statement.clearBindings();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public ArrayList<String> getBusesName () {
        ArrayList<String> ret = new ArrayList<>();

        String req = "SELECT "+ StarContract.BusRoutes.BusRouteColumns.SHORT_NAME +" FROM "+StarContract.BusRoutes.CONTENT_PATH;
        Cursor cursor = database.rawQuery(req,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String busName = cursor.getString(0);
            ret.add(busName);
            cursor.moveToNext();
        }
        cursor.close();
        return ret;
    }

    public ArrayList<String> getBusesColor () {
        ArrayList<String> ret = new ArrayList<>();

        String req = "SELECT "+ StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR +" FROM "+StarContract.BusRoutes.CONTENT_PATH;
        Cursor cursor = database.rawQuery(req,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String busColor = cursor.getString(0);
            ret.add(busColor);
            cursor.moveToNext();
        }
        cursor.close();
        return ret;
    }

    public ArrayList<String> getSensForBus (String busName) {
        ArrayList <String> ret = new ArrayList<>();

        String req = "SELECT "+ StarContract.BusRoutes.BusRouteColumns.LONG_NAME +
                     " FROM "+StarContract.BusRoutes.CONTENT_PATH+
                     " WHERE "+StarContract.BusRoutes.BusRouteColumns.SHORT_NAME +" = \""+busName+"\"";

        Cursor cursor = database.rawQuery(req,null);
        cursor.moveToFirst();
        String line = cursor.getString(0);
        String [] columns = line.split("<>");

        ret.add(columns[0]);
        ret.add(columns[columns.length-1]);

        return ret;
    }
}
