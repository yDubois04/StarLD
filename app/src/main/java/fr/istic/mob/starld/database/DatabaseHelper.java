package fr.istic.mob.starld.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import fr.istic.mob.starld.StarContract;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "databaseStarLD.db";
    private static final int DATABASE_VERSION  = 1;

    private static final String CREATE_TABLE_ROUTE =
            "CREATE TABLE IF NOT EXISTS " + StarContract.BusRoutes.CONTENT_PATH+
                    "("+StarContract.BusRoutes.BusRouteColumns._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    StarContract.BusRoutes.BusRouteColumns.SHORT_NAME + " TEXT, "+
                    StarContract.BusRoutes.BusRouteColumns.LONG_NAME + " TEXT, "+
                    StarContract.BusRoutes.BusRouteColumns.DESCRIPTION + " TEXT, "+
                    StarContract.BusRoutes.BusRouteColumns.TYPE + " INTEGER, "+
                    StarContract.BusRoutes.BusRouteColumns.COLOR + " TEXT, "+
                    StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR + " TEXT)";

    private static final String CREATE_TABLE_TRIP =
            "CREATE TABLE IF NOT EXISTS "+ StarContract.Trips.CONTENT_PATH +
                    "("+StarContract.Trips.TripColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        StarContract.Trips.TripColumns.ROUTE_ID+ " INTEGER,"+
                        StarContract.Trips.TripColumns.SERVICE_ID+ " INTEGER,"+
                        StarContract.Trips.TripColumns.HEADSIGN+ " TEXT,"+
                        StarContract.Trips.TripColumns.DIRECTION_ID+ " INTEGER,"+
                        StarContract.Trips.TripColumns.BLOCK_ID+ " TEXT,"+
                        StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE+ " INTEGER)";

    private static final String CREATE_TABLE_STOP =
            "CREATE TABLE IF NOT EXISTS "+ StarContract.Stops.CONTENT_PATH+
                    "("+StarContract.Stops.StopColumns._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        StarContract.Stops.StopColumns.NAME + " TEXT, "+
                        StarContract.Stops.StopColumns.DESCRIPTION + " TEXT, "+
                        StarContract.Stops.StopColumns.LATITUDE + " TEXT, "+
                        StarContract.Stops.StopColumns.LONGITUDE +  " TEXT, "+
                        StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING + " INTEGER)";

    private static final String CREATE_TABLE_STOP_TIME =
            "CREATE TABLE IF NOT EXISTS "+ StarContract.StopTimes.CONTENT_PATH+
                    "("+StarContract.StopTimes.StopTimeColumns._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        StarContract.StopTimes.StopTimeColumns.TRIP_ID + " INTEGER, "+
                        StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " TEXT, "+
                        StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME + " TEXT, "+
                        StarContract.StopTimes.StopTimeColumns.STOP_ID+ " INTEGER, "+
                        StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE+ " INTEGER) ";

    private static final String CREATE_TABLE_CALENDAR =
            "CREATE TABLE IF NOT EXISTS "+StarContract.Calendar.CONTENT_PATH+
                    "( "+StarContract.Calendar.CalendarColumns._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        StarContract.Calendar.CalendarColumns.MONDAY+" INTEGER,"+
                        StarContract.Calendar.CalendarColumns.TUESDAY+" INTEGER,"+
                        StarContract.Calendar.CalendarColumns.WEDNESDAY+" INTEGER,"+
                        StarContract.Calendar.CalendarColumns.THURSDAY+" INTEGER,"+
                        StarContract.Calendar.CalendarColumns.FRIDAY+" INTEGER,"+
                        StarContract.Calendar.CalendarColumns.SATURDAY+" INTEGER,"+
                        StarContract.Calendar.CalendarColumns.SUNDAY+" INTEGER,"+
                        StarContract.Calendar.CalendarColumns.START_DATE+" INTEGER,"+
                        StarContract.Calendar.CalendarColumns.END_DATE+" INTEGER)";

    public DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ROUTE);
        sqLiteDatabase.execSQL(CREATE_TABLE_TRIP);
        sqLiteDatabase.execSQL(CREATE_TABLE_STOP);
        sqLiteDatabase.execSQL(CREATE_TABLE_STOP_TIME);
        sqLiteDatabase.execSQL(CREATE_TABLE_CALENDAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w (DatabaseHelper.class.getName(), "Upgrading database from : version "+i + "to version "+i1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ StarContract.BusRoutes.CONTENT_PATH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ StarContract.Trips.CONTENT_PATH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ StarContract.Stops.CONTENT_PATH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ StarContract.StopTimes.CONTENT_PATH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ StarContract.Calendar.CONTENT_PATH);
        onCreate(sqLiteDatabase);
    }


}
