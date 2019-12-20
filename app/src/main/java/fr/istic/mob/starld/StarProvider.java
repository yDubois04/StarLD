package fr.istic.mob.starld;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import java.net.URI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StarProvider extends ContentProvider {

    DataSource dataSource;
    private static final int QUERY_ALL = 1;
    private static final int QUERY_BY_ID = 2;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH, QUERY_ALL);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH+"/#", QUERY_BY_ID);
    }


    @Override
    public boolean onCreate() {
        dataSource = new DataSource(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor c = dataSource.getBuses();
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case QUERY_ALL : return StarContract.BusRoutes.CONTENT_TYPE;
            case QUERY_BY_ID : return StarContract.BusRoutes.CONTENT_ITEM_TYPE;
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
