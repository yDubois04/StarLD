package fr.istic.mob.starld;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import fr.istic.mob.starld.database.DataSource;

public class StarProvider extends ContentProvider {

    DataSource dataSource;
    private static final int QUERY_BUS = 1;
    private static final int QUERY_BUS_BY_ID = 2;
    private static final int QUERY_TRIP = 3;
    private static final int QUERY_TRIP_BY_ID = 4;
    private static final int QUERY_STOPS = 5;
    private static final int QUERY_STOPS_BY_ID = 6;
    private static final int QUERY_STIMES = 7;
    private static final int QUERY_STIMES_BY_ID = 8;
    private static final int QUERY_CALENDAR = 9;
    private static final int QUERY_CALENDAR_BY_ID = 10;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH, QUERY_BUS);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH+"/#", QUERY_BUS_BY_ID);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Trips.CONTENT_PATH, QUERY_TRIP);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Trips.CONTENT_PATH+"/#", QUERY_TRIP_BY_ID);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Stops.CONTENT_PATH, QUERY_STOPS);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Stops.CONTENT_PATH+"/#", QUERY_STOPS_BY_ID);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.StopTimes.CONTENT_PATH, QUERY_STIMES);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.StopTimes.CONTENT_PATH+"/#", QUERY_STIMES_BY_ID);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Calendar.CONTENT_PATH, QUERY_CALENDAR);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Calendar.CONTENT_PATH+"/#", QUERY_CALENDAR_BY_ID);
    }


    @Override
    public boolean onCreate() {
        dataSource = new DataSource(getContext());
        dataSource.open();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor c = null;
        //Ajouter une condition par uri en fonction des besoins
        if (URI_MATCHER.match(uri) == QUERY_BUS) {
            c = dataSource.getBuses();
        }
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case QUERY_BUS : return StarContract.BusRoutes.CONTENT_TYPE;
            case QUERY_BUS_BY_ID : return StarContract.BusRoutes.CONTENT_ITEM_TYPE;
            case QUERY_TRIP : return StarContract.Trips.CONTENT_TYPE;
            case QUERY_TRIP_BY_ID : return StarContract.Trips.CONTENT_ITEM_TYPE;
            case QUERY_STOPS : return StarContract.Stops.CONTENT_TYPE;
            case QUERY_STOPS_BY_ID : return StarContract.Stops.CONTENT_ITEM_TYPE;
            case QUERY_STIMES : return StarContract.StopTimes.CONTENT_TYPE;
            case QUERY_STIMES_BY_ID : return StarContract.StopTimes.CONTENT_ITEM_TYPE;
            case QUERY_CALENDAR : return StarContract.Calendar.CONTENT_PATH;
            case QUERY_CALENDAR_BY_ID : return StarContract.Calendar.CONTENT_ITEM_TYPE;
            default:return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
