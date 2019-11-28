package fr.istic.mob.starld;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "databaseStarLD.db";
    private static final int DATABASE_VERSION  = 1;

    private static final String CREATE_TABLE_ROUTE =
            "CREATE TABLE IF NOT EXISTS " + StarContract.BusRoutes.CONTENT_PATH+
                    "("+StarContract.BusRoutes.BusRouteColumns._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    StarContract.BusRoutes.BusRouteColumns.SHORT_NAME + "TEXT, "+
                    StarContract.BusRoutes.BusRouteColumns.LONG_NAME + " TEXT, "+
                    StarContract.BusRoutes.BusRouteColumns.DESCRIPTION + " TEXT, "+
                    StarContract.BusRoutes.BusRouteColumns.TYPE + " TEXT, "+
                    StarContract.BusRoutes.BusRouteColumns.COLOR + " TEXT, "+
                    StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR + " TEXT) ";

    private static final String CREATE_TABLE_TRIP =
            "CREATE TABLE IF NOT EXISTS "+ StarContract.Trips.CONTENT_PATH +
                    "("+StarContract.Trips.TripColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        StarContract.Trips.TripColumns.ROUTE_ID+ " TEXT,"+
                        StarContract.Trips.TripColumns.SERVICE_ID+ " TEXT,"+
                        StarContract.Trips.TripColumns.HEADSIGN+ " TEXT,"+
                        StarContract.Trips.TripColumns.DIRECTION_ID+ " TEXT,"+
                        StarContract.Trips.TripColumns.BLOCK_ID+ " TEXT,"+
                        StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE+ " TEXT)";

    private static final String CREATE_TABLE_STOP =
            "CREATE TABLE IF NOT EXISTS "+ StarContract.Stops.CONTENT_PATH+
                    "("+StarContract.Stops.StopColumns._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        StarContract.Stops.StopColumns.NAME + " TEXT, "+
                        StarContract.Stops.StopColumns.DESCRIPTION + " TEXT, "+
                        StarContract.Stops.StopColumns.LATITUDE + " TEXT, "+
                        StarContract.Stops.StopColumns.LONGITUDE +  " TEXT, "+
                        StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING + " TEXT)";

    private static final String CREATE_TABLE_STOP_TIME =
            "CREATE TABLE IF NOT EXISTS "+ StarContract.StopTimes.CONTENT_PATH+
                    "("+StarContract.StopTimes.StopTimeColumns._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        StarContract.StopTimes.StopTimeColumns.TRIP_ID + " TEXT, "+
                        StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME + " TEXT, "+
                        StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME + " TEXT, "+
                        StarContract.StopTimes.StopTimeColumns.STOP_ID+ " TEXT, "+
                        StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE+ " TEXT) ";

    private static final String CREATE_TABLE_CALENDAR =
            "CREATE TABLE IF NOT EXISTS "+StarContract.Calendar.CONTENT_PATH+
                    "("+StarContract.Calendar.CalendarColumns._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        StarContract.Calendar.CalendarColumns.MONDAY+" TEXT,"+
                        StarContract.Calendar.CalendarColumns.TUESDAY+" TEXT,"+
                    StarContract.Calendar.CalendarColumns.WEDNESDAY+" TEXT,"+
                    StarContract.Calendar.CalendarColumns.THURSDAY+" TEXT,"+
                    StarContract.Calendar.CalendarColumns.FRIDAY+" TEXT,"+
                    StarContract.Calendar.CalendarColumns.SATURDAY+" TEXT,"+
                    StarContract.Calendar.CalendarColumns.SUNDAY+" TEXT,"+
                    StarContract.Calendar.CalendarColumns.START_DATE+" TEXT,"+
                    StarContract.Calendar.CalendarColumns.END_DATE+" TEXT)";

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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ StarContract.BusRoutes.CONTENT_PATH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ StarContract.Trips.CONTENT_PATH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ StarContract.Stops.CONTENT_PATH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ StarContract.StopTimes.CONTENT_PATH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ StarContract.Calendar.CONTENT_PATH);
        onCreate(sqLiteDatabase);
    }
}
