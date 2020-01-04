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
    private static final int QUERY_TRIP = 2;
    private static final int QUERY_STOPS = 3;
    private static final int QUERY_STIMES = 4;
    private static final int QUERY_SEARCH = 5;
    private static final int QUERY_ROUTE_DETAILS = 6;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH, QUERY_BUS);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Trips.CONTENT_PATH, QUERY_TRIP);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Stops.CONTENT_PATH, QUERY_STOPS);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.StopTimes.CONTENT_PATH, QUERY_STIMES);
        URI_MATCHER.addURI(StarContract.AUTHORITY,"search", QUERY_SEARCH);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.RouteDetails.CONTENT_PATH, QUERY_ROUTE_DETAILS);
    }


    @Override
    public boolean onCreate() {
        dataSource = new DataSource(getContext());
        dataSource.open();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor c = null;
        if (URI_MATCHER.match(uri) == QUERY_BUS) {
            c = dataSource.getBuses();
        }
        else if (URI_MATCHER.match(uri) == QUERY_STOPS) {
            c = dataSource.getStops (selection, sortOrder);
        }
        else if (URI_MATCHER.match(uri) == QUERY_SEARCH) {
            c = dataSource.getBusesForAStop(selection);
        }
        else if (URI_MATCHER.match(uri) == QUERY_ROUTE_DETAILS) {
            c = dataSource.getSchedules(selection, Integer.valueOf(sortOrder));
        }
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case QUERY_BUS : return StarContract.BusRoutes.CONTENT_TYPE;
            case QUERY_TRIP : return StarContract.Trips.CONTENT_TYPE;
            case QUERY_STOPS : return StarContract.Stops.CONTENT_TYPE;
            case QUERY_STIMES : return StarContract.StopTimes.CONTENT_TYPE;
            case QUERY_ROUTE_DETAILS : return StarContract.RouteDetails.CONTENT_TYPE;
            case QUERY_SEARCH : return StarContract.RouteDetails.CONTENT_TYPE;
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
