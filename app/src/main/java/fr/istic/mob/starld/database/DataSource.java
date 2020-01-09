package fr.istic.mob.starld.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import fr.istic.mob.starld.StarContract;

public class DataSource {

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;
    private File dowloadFolder;

    public DataSource(Context context) {
        dowloadFolder = context.getExternalFilesDir(Context.DOWNLOAD_SERVICE);
        databaseHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = databaseHelper.getWritableDatabase();
        databaseHelper.onCreate(database);
    }

    public void close() {
        databaseHelper.close();
        database.close();
    }

    public void initializeDatabase() {
        this.clearDatabase();
        this.initializeTable("routes.txt", StarContract.BusRoutes.CONTENT_PATH);
        this.initializeTable("stops.txt", StarContract.Stops.CONTENT_PATH);
        this.initializeTable("calendar.txt", StarContract.Calendar.CONTENT_PATH);
        this.initializeTable("trips.txt", StarContract.Trips.CONTENT_PATH);
        this.initializeTable("stop_times.txt", StarContract.StopTimes.CONTENT_PATH);
    }

    public void updateDatabase(int i, int i1) {
        databaseHelper.onUpgrade(database, i, i1);
    }

    public void clearDatabase() {
        database.execSQL("DELETE FROM " + StarContract.BusRoutes.CONTENT_PATH);
        database.execSQL("DELETE FROM " + StarContract.StopTimes.CONTENT_PATH);
        database.execSQL("DELETE FROM " + StarContract.Stops.CONTENT_PATH);
        database.execSQL("DELETE FROM " + StarContract.Trips.CONTENT_PATH);
        database.execSQL("DELETE FROM " + StarContract.Calendar.CONTENT_PATH);
    }

    public void initializeTable(String fileName, String nameTable) {
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
                    statement.bindLong(1, Integer.parseInt(columnsContent[0]));
                    statement.bindString(2, columnsContent[2]);
                    statement.bindString(3, columnsContent[3]);
                    statement.bindString(4, columnsContent[4]);
                    statement.bindLong(5, Integer.parseInt(columnsContent[5]));
                    statement.bindString(6, columnsContent[7]);
                    statement.bindString(7, columnsContent[8]);
                } else if (nameTable.equals(StarContract.Trips.CONTENT_PATH)) {
                    statement.bindLong(1, Integer.parseInt(columnsContent[2]));
                    statement.bindLong(2, Integer.parseInt(columnsContent[0]));
                    statement.bindLong(3, Integer.parseInt(columnsContent[1]));
                    statement.bindString(4, columnsContent[3]);
                    statement.bindLong(5, Integer.parseInt(columnsContent[5]));
                    statement.bindString(6, columnsContent[6]);
                    statement.bindLong(7, Integer.parseInt(columnsContent[8]));
                } else if (nameTable.equals(StarContract.Stops.CONTENT_PATH)) {
                    statement.bindLong(1, Integer.parseInt(columnsContent[0]));
                    statement.bindString(2, columnsContent[2]);
                    statement.bindString(3, columnsContent[3]);
                    statement.bindString(4, columnsContent[4]);
                    statement.bindString(5, columnsContent[5]);
                    statement.bindLong(6, Integer.parseInt(columnsContent[11]));
                } else if (nameTable.equals(StarContract.Calendar.CONTENT_PATH)) {
                    statement.bindLong(1, Integer.parseInt(columnsContent[0]));
                    statement.bindLong(2, Integer.parseInt(columnsContent[1]));
                    statement.bindLong(3, Integer.parseInt(columnsContent[2]));
                    statement.bindLong(4, Integer.parseInt(columnsContent[3]));
                    statement.bindLong(5, Integer.parseInt(columnsContent[4]));
                    statement.bindLong(6, Integer.parseInt(columnsContent[5]));
                    statement.bindLong(7, Integer.parseInt(columnsContent[6]));
                    statement.bindLong(8, Integer.parseInt(columnsContent[7]));
                    statement.bindLong(9, Integer.parseInt(columnsContent[8]));
                    statement.bindLong(10, Integer.parseInt(columnsContent[9]));
                } else if (nameTable.equals(StarContract.StopTimes.CONTENT_PATH)) {
                    statement.bindLong(1, Integer.parseInt(columnsContent[0]));
                    statement.bindString(2, columnsContent[1]);
                    statement.bindString(3, columnsContent[2]);
                    statement.bindLong(4, Integer.parseInt(columnsContent[3]));
                    statement.bindLong(5, Integer.parseInt(columnsContent[4]));
                }

                statement.execute();
                statement.clearBindings();
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (IOException e) {
            System.out.println("Erreur " + e);
        }
    }


    private String getInsertRequete(String tableName) {
        String requete = "";

        if (tableName.equals(StarContract.BusRoutes.CONTENT_PATH)) {
            requete = "INSERT INTO " + StarContract.BusRoutes.CONTENT_PATH + "(" + StarContract.BusRoutes.BusRouteColumns._ID + ", " +
                    StarContract.BusRoutes.BusRouteColumns.SHORT_NAME + ", " + StarContract.BusRoutes.BusRouteColumns.LONG_NAME + ", " +
                    StarContract.BusRoutes.BusRouteColumns.DESCRIPTION + ", " + StarContract.BusRoutes.BusRouteColumns.TYPE + ", " +
                    StarContract.BusRoutes.BusRouteColumns.COLOR + ", " + StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR + ") VALUES (?,?,?,?,?,?,?)";
        } else if (tableName.equals(StarContract.Trips.CONTENT_PATH)) {
            requete = "INSERT INTO " + StarContract.Trips.CONTENT_PATH +
                    "(" + StarContract.Trips.TripColumns._ID + ", " +
                    StarContract.Trips.TripColumns.ROUTE_ID + "," +
                    StarContract.Trips.TripColumns.SERVICE_ID + "," +
                    StarContract.Trips.TripColumns.HEADSIGN + "," + StarContract.Trips.TripColumns.DIRECTION_ID + "," +
                    StarContract.Trips.TripColumns.BLOCK_ID + "," + StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE + ") VALUES (?,?,?,?,?,?,?)";
        } else if (tableName.equals(StarContract.Stops.CONTENT_PATH)) {
            requete = "INSERT INTO " + StarContract.Stops.CONTENT_PATH + "(" + StarContract.Stops.StopColumns._ID + "," +
                    StarContract.Stops.StopColumns.NAME + ", " + StarContract.Stops.StopColumns.DESCRIPTION + ", " +
                    StarContract.Stops.StopColumns.LATITUDE + ", " + StarContract.Stops.StopColumns.LONGITUDE + ", " +
                    StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING + ") VALUES (?,?,?,?,?,?)";
        } else if (tableName.equals(StarContract.Calendar.CONTENT_PATH)) {
            requete = "INSERT INTO " + StarContract.Calendar.CONTENT_PATH + "(" + StarContract.Calendar.CalendarColumns._ID + "," +
                    StarContract.Calendar.CalendarColumns.MONDAY + "," + StarContract.Calendar.CalendarColumns.TUESDAY + "," +
                    StarContract.Calendar.CalendarColumns.WEDNESDAY + "," + StarContract.Calendar.CalendarColumns.THURSDAY + "," +
                    StarContract.Calendar.CalendarColumns.FRIDAY + "," + StarContract.Calendar.CalendarColumns.SATURDAY + "," +
                    StarContract.Calendar.CalendarColumns.SUNDAY + " ," + StarContract.Calendar.CalendarColumns.START_DATE + "," +
                    StarContract.Calendar.CalendarColumns.END_DATE + ") VALUES (?,?,?,?,?,?,?,?,?,?)";
        } else if (tableName.equals(StarContract.StopTimes.CONTENT_PATH)) {
            requete = "INSERT INTO " + StarContract.StopTimes.CONTENT_PATH + "(" +
                    StarContract.StopTimes.StopTimeColumns.TRIP_ID + ", " + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + ", " +
                    StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME + ", " + StarContract.StopTimes.StopTimeColumns.STOP_ID + ", " +
                    StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE + ") VALUES (?,?,?,?,?)";
        }
        return requete;
    }

    public Cursor getBuses() {
        String req = "SELECT * FROM " + StarContract.BusRoutes.CONTENT_PATH;
        Cursor cursor = database.rawQuery(req, null);
        return cursor;
    }

    public Cursor getStops(String idBus, String sort) {

        String trip = getTrip(Integer.parseInt(idBus), Integer.parseInt(sort));

        String req = "SELECT DISTINCT " + StarContract.Stops.CONTENT_PATH + ".*"
                + " FROM " + StarContract.Stops.CONTENT_PATH
                + " INNER JOIN " + StarContract.StopTimes.CONTENT_PATH + " on " + StarContract.StopTimes.StopTimeColumns.STOP_ID + " = " + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns._ID
                + " INNER JOIN " + StarContract.Trips.CONTENT_PATH + " on " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + " = " + StarContract.StopTimes.StopTimeColumns.TRIP_ID
                + " WHERE " + StarContract.StopTimes.StopTimeColumns.TRIP_ID + " = " + trip
                + " ORDER BY " + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " ASC ";

        Cursor cursor = database.rawQuery(req, null);
        return cursor;
    }

    public String getTrip(int idBus, int sens) {
        String req = "SELECT DISTINCT " + StarContract.StopTimes.StopTimeColumns.TRIP_ID + ", COUNT (" + StarContract.StopTimes.StopTimeColumns.TRIP_ID + ") AS RES"
                + " FROM " + StarContract.Stops.CONTENT_PATH
                + " INNER JOIN " + StarContract.StopTimes.CONTENT_PATH + " on " + StarContract.StopTimes.StopTimeColumns.STOP_ID + " = " + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns._ID
                + " INNER JOIN " + StarContract.Trips.CONTENT_PATH + " on " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + " = " + StarContract.StopTimes.StopTimeColumns.TRIP_ID
                + " WHERE " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns.DIRECTION_ID + " = " + sens
                + " AND " + StarContract.Trips.TripColumns.ROUTE_ID + " = " + idBus
                + " GROUP BY " + StarContract.StopTimes.StopTimeColumns.TRIP_ID
                + " ORDER BY RES DESC "
                + " LIMIT 1 ";

        Cursor cursor = database.rawQuery(req, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public Cursor getBusesForAStop(String nameStop) {

        String req = "SELECT DISTINCT " + StarContract.BusRoutes.CONTENT_PATH + ".*, " + StarContract.Stops.StopColumns.NAME
                + " FROM " + StarContract.BusRoutes.CONTENT_PATH
                + " INNER JOIN " + StarContract.Trips.CONTENT_PATH + " ON " + StarContract.BusRoutes.CONTENT_PATH + "." + StarContract.BusRoutes.BusRouteColumns._ID + " = " + StarContract.Trips.TripColumns.ROUTE_ID
                + " INNER JOIN " + StarContract.StopTimes.CONTENT_PATH + " ON " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + " = " + StarContract.StopTimes.StopTimeColumns.TRIP_ID
                + " INNER JOIN " + StarContract.Stops.CONTENT_PATH + " ON " + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns._ID + " = " + StarContract.StopTimes.StopTimeColumns.STOP_ID
                + " WHERE " + StarContract.Stops.StopColumns.NAME + " LIKE \"%" + nameStop + "%\""
                + " ORDER BY " + StarContract.Stops.StopColumns.NAME;

        Cursor cursor = database.rawQuery(req, null);
        return cursor;
    }

    public Cursor getSchedules(String schedule, int tripId) {

        String req = "SELECT DISTINCT " + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns._ID + "," + StarContract.Stops.StopColumns.NAME + " , " + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME
                + " FROM " + StarContract.Stops.CONTENT_PATH
                + " INNER JOIN " + StarContract.StopTimes.CONTENT_PATH + " ON " + StarContract.Stops.CONTENT_PATH + "." + StarContract.Stops.StopColumns._ID + " = " + StarContract.StopTimes.StopTimeColumns.STOP_ID
                + " WHERE " + StarContract.StopTimes.StopTimeColumns.TRIP_ID + " = " + tripId
                + " AND " + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " > \"" + schedule + "\""
                + " ORDER BY " + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " ASC ";

        Cursor cursor = database.rawQuery(req, null);
        return cursor;
    }

    public Cursor getSchedulesForAStop(int stopId, int busRouteId, int sens, String day, String hour) {

        String req = "SELECT " + StarContract.StopTimes.CONTENT_PATH + ".*"
                + " FROM " + StarContract.StopTimes.CONTENT_PATH
                + " INNER JOIN " + StarContract.Trips.CONTENT_PATH + " ON " + StarContract.Trips.CONTENT_PATH + "." + StarContract.Trips.TripColumns._ID + " = " + StarContract.StopTimes.StopTimeColumns.TRIP_ID
                + " INNER JOIN " + StarContract.Calendar.CONTENT_PATH + " ON " + StarContract.Trips.TripColumns.SERVICE_ID + " = " + StarContract.Calendar.CONTENT_PATH + "." + StarContract.Calendar.CalendarColumns._ID
                + " WHERE " + StarContract.StopTimes.StopTimeColumns.STOP_ID + " = " + stopId
                + " AND " + StarContract.Trips.TripColumns.ROUTE_ID + " = " + busRouteId
                + " AND " + StarContract.Trips.TripColumns.DIRECTION_ID + " = " + sens
                + " AND " + day + "=" + 1
                + " AND " + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " > \"" + hour + "\""
                + " ORDER BY " + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME;

        Cursor cursor = database.rawQuery(req, null);
        return cursor;
    }
}