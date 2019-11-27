package fr.istic.mob.starld;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "databaseStarLD.db";
    private static final int DATABASE_VERSION  = 1;
    private static final String TABLE_ROUTE = "bus_route";
    private static final String TABLE_TRIP = "trip";
    private static final String TABLE_STOP = "stop";
    private static final String TABLE_STOP_TIME = "stop_time";
    private static final String TABLE_CALENDAR = "calendar";

    private static final String CREATE_TABLE_ROUTE =
            "CREATE TABLE IF NOT EXISTS bus_route" + TABLE_ROUTE+
                    "(route_id text primary key not null,agency_id text,route_short_name text," +
                    "route_long_name text,route_desc text,route_type text,route_url text,route_color text," +
                    "route_text_color text,route_sort_order text)";

    private static final String CREATE_TABLE_TRIP =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_TRIP +
                    " (route_id text primary key not null,service_id text,trip_id text," +
                    "trip_headsign text,trip_short_name text,direction_id text,block_id text," +
                    "shape_id text,wheelchair_accessible text,bikes_allowed text )";

    private static final String CREATE_TABLE_STOP =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_STOP+
                    " (stop_id text primary key not null,stop_code text,stop_name text," +
                    "stop_desc text,stop_lat text,stop_lon text,zone_id text,stop_url text," +
                    "location_type text,parent_station text,stop_timezone text,wheelchair_boarding text)";

    private static final String CREATE_TABLE_STOP_TIME =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_STOP_TIME+
                    " (trip_id text primary key not null,arrival_time text,departure_time text," +
                    "stop_id text,stop_sequence text,stop_headsign text,pickup_type text," +
                    "drop_off_type text,shape_dist_traveled text)"

            ;
    private static final String CREATE_TABLE_CALENDAR =
            "CREATE TABLE IF NOT EXISTS "+TABLE_CALENDAR+
                    " (service_id text primary key not null, text,tuesday text,wednesday text," +
                    "thursday text,friday text,saturday text,sunday text,start_date text,end_date text)";

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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_ROUTE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_TRIP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_STOP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_STOP_TIME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_CALENDAR);
        onCreate(sqLiteDatabase);
    }
}
