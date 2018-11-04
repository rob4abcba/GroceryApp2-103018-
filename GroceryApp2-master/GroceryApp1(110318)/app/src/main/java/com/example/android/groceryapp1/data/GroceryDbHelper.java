package com.example.android.groceryapp1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GroceryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = GroceryDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "grocery.db";

    private static final int DATABASE_VERSION = 1;

    public GroceryDbHelper(Context context) {
        super ( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE "
                + GroceryContract.GroceryEntry.TABLE_NAME + " ("
                + GroceryContract.GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GroceryContract.GroceryEntry.COLUMN_GROCERY_NAME + " TEXT NOT NULL, "
                + GroceryContract.GroceryEntry.COLUMN_GROCERY_PRICE + " REAL NOT NULL, "
                + GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY + " INTEGER NOT NULL, "
                + GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLIER_NAME + " TEXT NOT NULL,"
                + GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLER_PHONE + " TEXT NOT NULL );";
        db.execSQL ( SQL_CREATE_TABLE );
        Log.v ( LOG_TAG, "The table is created successfully" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
