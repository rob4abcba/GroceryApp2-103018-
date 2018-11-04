package com.example.android.groceryapp1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class GroceryProvider extends ContentProvider {
    public static final String LOG_TAG = GroceryProvider.class.getSimpleName ();
    private static final int GROCERY = 100;
    private static final int GROCERY_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher ( UriMatcher.NO_MATCH );

    static {
        sUriMatcher.addURI ( GroceryContract.CONTENT_AUTHORITY, GroceryContract.PATH_GROCERY, GROCERY );
        sUriMatcher.addURI ( GroceryContract.CONTENT_AUTHORITY, GroceryContract.PATH_GROCERY + "/#", GROCERY_ID );
    }

    private GroceryDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new GroceryDbHelper ( getContext () );
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase ();

        Cursor cursor;

        int match = sUriMatcher.match ( uri );
        switch (match) {
            case GROCERY:
                cursor = database.query ( GroceryContract.GroceryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder );
                break;

            case GROCERY_ID:
                selection = GroceryContract.GroceryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf ( ContentUris.parseId ( uri ) )};

                cursor = database.query ( GroceryContract.GroceryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder );
                break;

            default:
                throw new IllegalArgumentException ( "Cannot query unknown URI " + uri );

        }
        cursor.setNotificationUri ( getContext ().getContentResolver (), uri );
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match ( uri );
        switch (match) {
            case GROCERY:
                return insertGrocery ( uri, contentValues );
            default:
                throw new IllegalArgumentException ( "Insertion is not supported for " + uri );
        }
    }

    private Uri insertGrocery(Uri uri, ContentValues values) {
        String name = values.getAsString ( GroceryContract.GroceryEntry.COLUMN_GROCERY_NAME );
        if (name == null) {
            throw new IllegalArgumentException ( "Grocery requires a name" );
        }
        Integer quantity = values.getAsInteger ( GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY );
        SQLiteDatabase database = mDbHelper.getWritableDatabase ();

        long id = database.insert ( GroceryContract.GroceryEntry.TABLE_NAME, null, values );
        if (id == -1) {
            Log.e ( LOG_TAG, "Failed to insert row for " + uri );
            return null;
        }

        getContext ().getContentResolver ().notifyChange ( uri, null );
        return ContentUris.withAppendedId ( uri, id );
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match ( uri );
        switch (match) {
            case GROCERY:
                return update ( uri, contentValues, selection, selectionArgs );
            case GROCERY_ID:
                selection = GroceryContract.GroceryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf ( ContentUris.parseId ( uri ) )};
                return update ( uri, contentValues, selection, selectionArgs );
            default:
                throw new IllegalArgumentException ( "Update is not supported for " + uri );
        }
    }

    public int updateGrocery(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey ( GroceryContract.GroceryEntry.COLUMN_GROCERY_NAME )) {
            String name = values.getAsString ( GroceryContract.GroceryEntry.COLUMN_GROCERY_NAME );
            if (name == null) {
                throw new IllegalArgumentException ( "Grocery requires a name" );
            }
        }
        if (values.containsKey ( GroceryContract.GroceryEntry.COLUMN_GROCERY_PRICE )) {
            String price = values.getAsString ( GroceryContract.GroceryEntry.COLUMN_GROCERY_PRICE );
            if (price == null) {
                throw new IllegalArgumentException ( "Grocery price is required" );
            }
        }
        if (values.containsKey ( GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY )) {
            Integer quantity = values.getAsInteger ( GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY );
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException ( "Grocery requires a valid quantity" );
            }
        }
        if (values.containsKey ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLIER_NAME )) {
            String supplierName = values.getAsString ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLIER_NAME );
            if (supplierName == null) {
                throw new IllegalArgumentException ( "Grocery requires a supplier name" );
            }
        }
        if (values.containsKey ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLER_PHONE )) {
            String supplierPhone = values.getAsString ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLER_PHONE );
            if (supplierPhone == null) {
                throw new IllegalArgumentException ( "Grocery requires a supplier phone" );
            }
        }
        if (values.size () == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase ();
        int rowsUpdated = database.update ( GroceryContract.GroceryEntry.TABLE_NAME, values, selection, selectionArgs );
        if (rowsUpdated != 0) {
            getContext ().getContentResolver ().notifyChange ( uri, null );
        }
        return rowsUpdated;
    }

        @Override public int delete (@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs){
           SQLiteDatabase database = mDbHelper.getWritableDatabase ();
           int rowsDeleted;
           final int match = sUriMatcher.match ( uri );
           switch (match){
               case GROCERY:
                   rowsDeleted = database.delete ( GroceryContract.GroceryEntry.TABLE_NAME, selection, selectionArgs );
                   break;
               case GROCERY_ID:
                   selection = GroceryContract.GroceryEntry._ID + "=?";
                   selectionArgs = new String[]{String.valueOf ( ContentUris.parseId ( uri ) )};
                   rowsDeleted = database.delete ( GroceryContract.GroceryEntry.TABLE_NAME, selection, selectionArgs );
                   break;
               default:
                   throw new IllegalArgumentException ( "Deletion is not supported for " + uri );
           }
           if (rowsDeleted != 0){
               getContext ().getContentResolver ().notifyChange ( uri, null );
           }
           return rowsDeleted;
        }
        @Nullable @Override public String getType (@NonNull Uri uri){
            final int match = sUriMatcher.match ( uri );
            switch (match){
                case GROCERY:
                    return GroceryContract.GroceryEntry.CONTENT_LIST_TYPE;
                case GROCERY_ID:
                    return GroceryContract.GroceryEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalArgumentException ( "Uknown URI "+ uri + " with match " + match);
            }
    }
}