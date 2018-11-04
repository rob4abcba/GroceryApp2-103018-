package com.example.android.groceryapp1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.groceryapp1.data.GroceryContract;
import com.example.android.groceryapp1.data.GroceryDbHelper;


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int GROCERY_LOADER = 0;
    GroceryCursorAdapter mCursorAdapter;

    public static final String LOG_TAG = CatalogActivity.class.getSimpleName ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_catalog );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView groceryListView = findViewById ( R.id.list );
        View emptyView = findViewById ( R.id.empty_view );
        groceryListView.setEmptyView ( emptyView );

        mCursorAdapter = new GroceryCursorAdapter(this, null);
        groceryListView.setAdapter ( (ListAdapter) mCursorAdapter );

        groceryListView.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                Intent intent = new Intent ( CatalogActivity.this, EditorActivity.class );
                Uri currentGroceryUri = ContentUris.withAppendedId ( GroceryContract.GroceryEntry.CONTENT_URI, id );
                intent.setData ( currentGroceryUri );
                startActivity ( intent );
           }
        } );
        getSupportLoaderManager ().initLoader ( GROCERY_LOADER, null, this );
    }
    private void insertGrocery() {
        ContentValues values = new ContentValues ();
        values.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_NAME, "Grocery" );
        values.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_PRICE, 0 );
        values.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY, 0 );
        values.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLIER_NAME, "supplier" );
        values.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLER_PHONE, 0 );

        Log.v ( LOG_TAG, "Values are able to be inserted" );
        Uri newUri = getContentResolver ().insert ( GroceryContract.GroceryEntry.CONTENT_URI, values );
    }

    private void deleteAllGrocery() {
        int rowsDeleted = getContentResolver ().delete ( GroceryContract.GroceryEntry.CONTENT_URI, null, null );
        Log.v ( "CatalogActivity", rowsDeleted + " rows deleted from grocery databse" );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertGrocery ();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllGrocery();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader <Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {GroceryContract.GroceryEntry._ID, GroceryContract.GroceryEntry.COLUMN_GROCERY_NAME, GroceryContract.GroceryEntry.COLUMN_GROCERY_PRICE, GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY, GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLIER_NAME, GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLER_PHONE};
        return new CursorLoader ( this, GroceryContract.GroceryEntry.CONTENT_URI, projection, null, null, null );}

    @Override
    public void onLoadFinished(@NonNull Loader <Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor ( data );
    }

    @Override
    public void onLoaderReset(@NonNull Loader <Cursor> loader) {
        mCursorAdapter.swapCursor ( (Cursor) null );
    }
}
