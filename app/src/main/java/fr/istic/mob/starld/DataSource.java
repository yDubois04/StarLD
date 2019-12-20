package fr.istic.mob.starld;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import fr.istic.mob.starld.fr.istic.mob.starld.model.BusRoute;

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
        this.initializeTable("stops.txt", StarContract.Stops.CONTENT_PATH);
        this.initializeTable("trips.txt", StarContract.Trips.CONTENT_PATH);
        this.initializeTable("calendar.txt", StarContract.Calendar.CONTENT_PATH);
        this.initializeTable("stop_times.txt", StarContract.StopTimes.CONTENT_PATH);
    }

    public void updateDatabase (int i, int i1) {
        databaseHelper.onUpgrade(database,i, i1);
    }

    public void clearDatabase () {
        database.execSQL("DELETE FROM "+StarContract.BusRoutes.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.StopTimes.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.Stops.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.Trips.CONTENT_PATH);
        database.execSQL("DELETE FROM "+StarContract.Calendar.CONTENT_PATH);
    }

    public void initializeTable (String fileName, String nameTable) {
        File file = new File(dowloadFolder, fileName);

        String requete = getInsertRequete(nameTable);
        database.beginTransactionNonExclusive();
        SQLiteStatement statement = database.compileStatement(requete);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffered = new BufferedReader(reader);
            String line = buffered.readLine();

            while ((line = buffered.readLine()) != null) {
                String lineWithoutQuote = line.replace("\"", "");
                String[] columnsContent = lineWithoutQuote.split(",");

                if (nameTable.equals(StarContract.BusRoutes.CONTENT_PATH)) {
                    statement.bindString(1,columnsContent[2]);
                    statement.bindString(2,columnsContent[3]);
                    statement.bindString(3, columnsContent[4]);
                    statement.bindLong(4, Integer.parseInt(columnsContent[5]));
                    statement.bindString(5,columnsContent[7]);
                    statement.bindString(6, columnsContent[8]);
                }
                else if (nameTable.equals(StarContract.Trips.CONTENT_PATH)) {
                    statement.bindLong(1, Integer.parseInt(columnsContent[0]));
                    statement.bindLong(2,Integer.parseInt(columnsContent[1]));
                    statement.bindString(3, columnsContent[3]);
                    statement.bindLong(4, Integer.parseInt(columnsContent[5]));
                    statement.bindString(5,columnsContent[6]);
                    statement.bindLong(6,Integer.parseInt(columnsContent[8]));
                }
                else if (nameTable.equals(StarContract.Stops.CONTENT_PATH)) {
                    statement.bindString(1, columnsContent[2]);
                    statement.bindString(2,columnsContent[3]);
                    statement.bindString(3, columnsContent[4]);
                    statement.bindString(4, columnsContent[5]);
                    statement.bindLong(5, Integer.parseInt(columnsContent[11]));
                }
                else if (nameTable.equals(StarContract.Calendar.CONTENT_PATH)) {
                    statement.bindLong(1, Integer.parseInt(columnsContent[1]));
                    statement.bindLong(2, Integer.parseInt(columnsContent[2]));
                    statement.bindLong(3, Integer.parseInt(columnsContent[3]));
                    statement.bindLong(4, Integer.parseInt(columnsContent[4]));
                    statement.bindLong(5, Integer.parseInt(columnsContent[5]));
                    statement.bindLong(6,Integer.parseInt(columnsContent[6]));
                    statement.bindLong(7,Integer.parseInt(columnsContent[7]));
                    statement.bindLong(8,Integer.parseInt(columnsContent[8]));
                    statement.bindLong(9,Integer.parseInt(columnsContent[9]));
                }
                else if (nameTable.equals(StarContract.StopTimes.CONTENT_PATH)) {
                    statement.bindLong(1, Integer.parseInt(columnsContent[0]));
                    statement.bindString(1, columnsContent[1]);
                    statement.bindString(2, columnsContent[2]);
                    statement.bindLong(3, Integer.parseInt(columnsContent[3]));
                    statement.bindLong(4, Integer.parseInt(columnsContent[4]));
                }

                statement.execute();
                statement.clearBindings();
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        }catch (IOException e) {
            System.out.println("Erreur "+e);
        }
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

    public ArrayList<BusRoute> getBusesName () {
        ArrayList<BusRoute> ret = new ArrayList<>();

        String req = "SELECT * FROM "+StarContract.BusRoutes.CONTENT_PATH;
        Cursor cursor = database.rawQuery(req,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String busName = cursor.getString(0);
            BusRoute bus = new BusRoute(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                                        cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6));
            ret.add(bus);
            cursor.moveToNext();
        }
        cursor.close();
        return ret;
    }

    public Cursor getBuses () {
        String req = "SELECT * FROM "+StarContract.BusRoutes.CONTENT_PATH;
        Cursor cursor = database.rawQuery(req,null);
        cursor.moveToFirst();

        return cursor;
    }

    public ArrayList<String> getSensForBus (String busName) {
        ArrayList <String> ret = new ArrayList<>();

        String req = "SELECT "+ StarContract.BusRoutes.BusRouteColumns.LONG_NAME +
                     " FROM "+StarContract.BusRoutes.CONTENT_PATH+
                     " WHERE "+StarContract.BusRoutes.BusRouteColumns.SHORT_NAME +" = \""+busName+"\"";

        System.out.println(req);

        Cursor cursor = database.rawQuery(req,null);
        cursor.moveToFirst();
        String line = cursor.getString(0);
        String [] columns = line.split("<>");

        ret.add(columns[0]);
        ret.add(columns[columns.length-1]);

        return ret;
    }
}
