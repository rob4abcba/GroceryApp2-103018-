package com.example.android.groceryapp1;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.groceryapp1.data.GroceryContract;

import static com.example.android.groceryapp1.R.id.*;

class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_GROCERY_LOADER = 0;
    private Uri mCurrentGroceryUri;
    private EditText mGroceryNameEditText;
    private EditText mPriceEditText;
    private Button mIncreaseQtyBtn;
    private Button mDecreaseQtyBtn;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneEditText;
    private Button mSupplierPhoneBtn;

    private boolean mGroceryHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener () {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            mGroceryHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_editor );

        Button suppPhnBtn = findViewById ( R.id.supplierPhoneBtn );
        suppPhnBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mSupplierPhoneEditText.setVisibility ( View.VISIBLE );
                Log.v("EditText", mSupplierPhoneEditText.getText().toString());            }
        } );

        Intent intent = getIntent ();
        mCurrentGroceryUri = intent.getData ();

        if (mCurrentGroceryUri == null) {
            setTitle ( getString ( R.string.editor_activity_title_new_grocery ) );
            invalidateOptionsMenu ();
        } else {
            setTitle ( getString ( R.string.editor_activity_title_edit_grocery ) );
            getLoaderManager ().initLoader ( EXISTING_GROCERY_LOADER, null, null );
        }
        mGroceryNameEditText = findViewById ( groceryNameEdit );
        mPriceEditText = findViewById ( priceEditText );
        mIncreaseQtyBtn = findViewById ( increaseBtn );
        mDecreaseQtyBtn = findViewById ( decreaseBtn );
        mQuantityEditText = findViewById ( qtyEditText );
        mSupplierNameEditText = findViewById ( supplierNameEdit );
        mSupplierPhoneEditText = findViewById ( supplierPhoneEdit );
        mSupplierPhoneBtn = findViewById ( supplierPhoneBtn );

        mGroceryNameEditText.setOnTouchListener ( mTouchListener );
        mPriceEditText.setOnTouchListener ( mTouchListener );
        mIncreaseQtyBtn.setOnTouchListener ( mTouchListener );
        mDecreaseQtyBtn.setOnTouchListener ( mTouchListener );
        mQuantityEditText.setOnTouchListener ( mTouchListener );
        mSupplierNameEditText.setOnTouchListener ( mTouchListener );
        mSupplierPhoneEditText.setOnTouchListener ( mTouchListener );
        mSupplierPhoneBtn.setOnTouchListener ( mTouchListener );
    }

    private void saveGrocery() {
        String nameString = mGroceryNameEditText.getText ().toString ().trim ();
        String priceString = mPriceEditText.getText ().toString ().trim ();
        String qtyString = mQuantityEditText.getText ().toString ().trim ();
        String supplierNameString = mSupplierNameEditText.getText ().toString ().trim ();
        String supplierPhnString = mSupplierPhoneEditText.getText ().toString ().trim ();

        if (mCurrentGroceryUri == null && TextUtils.isEmpty ( nameString ) && TextUtils.isEmpty ( priceString ) && TextUtils.isEmpty ( qtyString ) && TextUtils.isEmpty ( supplierNameString ) && TextUtils.isEmpty ( supplierPhnString )) {
            return;
        }
        Toast.makeText ( this, getString ( R.string.required_fields_toast ), Toast.LENGTH_SHORT ).show ();

        ContentValues values = new ContentValues ();
        values.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_NAME, nameString );
        values.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_PRICE, priceString );
        values.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLIER_NAME, supplierNameString );
        values.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLER_PHONE, supplierPhnString );

        int quantity = 0;
        if (!TextUtils.isEmpty ( qtyString )) {
            quantity = Integer.parseInt ( qtyString );
        }
        values.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY, quantity );

        if (mCurrentGroceryUri == null) {
             Uri newUri = getContentResolver ().insert ( GroceryContract.GroceryEntry.CONTENT_URI, values );

            if (newUri == null) {
                Toast.makeText ( this, getString ( R.string.editor_delete_grocery_failed ), Toast.LENGTH_SHORT ).show ();
            } else {
                Toast.makeText ( this, getString ( R.string.editor_insert_grocery_successful ), Toast.LENGTH_SHORT ).show ();
            }
        } else {
            int rowsAffected = getContentResolver ().update ( mCurrentGroceryUri, values, null, null );
            if (rowsAffected == 0) {
                Toast.makeText ( this, getString ( R.string.editor_update_grocery_failed ), Toast.LENGTH_SHORT ).show ();
            } else {
                Toast.makeText ( this, getString ( R.string.editor_update_grocery_successful ), Toast.LENGTH_SHORT ).show ();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.menu_editor, menu );
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu ( menu );
        if (mCurrentGroceryUri == null) {
            MenuItem menuItem = menu.findItem ( R.id.action_delete );
            menuItem.setVisible ( false );
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ()) {
            case action_save:
                saveGrocery ();
                finish ();
                return true;
            case action_delete:
                showDeleteConfirmationDialog ();
                return true;
            case android.R.id.home:
                if (!mGroceryHasChanged) {
                    NavUtils.navigateUpFromSameTask ( EditorActivity.this );
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        NavUtils.navigateUpFromSameTask ( EditorActivity.this );
                    }
                };
                showUnsavedChangesDialog ( discardButtonClickListener );
                return true;
        }
        return super.onOptionsItemSelected ( item );
    }

    @Override
    public void onBackPressed() {
        if (!mGroceryHasChanged) {
            super.onBackPressed ();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish ();
            }
        };

        showUnsavedChangesDialog ( discardButtonClickListener );
    }
   @NonNull
    @Override
    public Loader <Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {GroceryContract.GroceryEntry._ID, GroceryContract.GroceryEntry.COLUMN_GROCERY_NAME, GroceryContract.GroceryEntry.COLUMN_GROCERY_PRICE, GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY, GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLIER_NAME, GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLER_PHONE};
        return new CursorLoader ( this, mCurrentGroceryUri, projection, null, null, null );}

    @Override
    public void onLoadFinished(@NonNull Loader <Cursor> loader, Cursor cursor) {
        if(cursor == null || cursor.getCount () < 1) {
            return;
        }
        if(cursor.moveToFirst ()){
            int nameColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_NAME );
            int priceColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_PRICE );
            int qtyColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY );
            int supplierNameColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLIER_NAME );
            int supplierPhoneColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLER_PHONE );

            String name = cursor.getString ( nameColumnIndex );
            String price = cursor.getString ( priceColumnIndex );
            int quantity = cursor.getInt ( qtyColumnIndex );
            String supplierName = cursor.getString ( supplierNameColumnIndex );
            int supplierPhone = cursor.getInt (supplierPhoneColumnIndex);

            mGroceryNameEditText.setText ( name );
            mPriceEditText.setText ( price );
            mQuantityEditText.setText ( Integer.toString ( quantity ) );
            mSupplierNameEditText.setText ( supplierName );
            mSupplierPhoneEditText.setText (Integer.toString ( supplierPhone ) );
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
            mGroceryNameEditText.setText ( "" );
            mPriceEditText.setText ( "" );
            mQuantityEditText.setText ( "" );
            mSupplierNameEditText.setText ( "" );
            mSupplierPhoneEditText.setText ( "" );
    }
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder ( this );
        builder.setMessage ( R.string.unsaved_changes_dialog_msg );
        builder.setPositiveButton ( R.string.discard, discardButtonClickListener );
        builder.setNegativeButton ( R.string.keep_editing, new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss ();
                }
            }
        } );
        AlertDialog alertDialog = builder.create ();
        alertDialog.show ();
    }
    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder ( this );
        builder.setMessage ( R.string.delete_dialog_msg );
        builder.setPositiveButton ( R.string.delete, new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteGrocery();
            }
        } );
        builder.setNegativeButton ( R.string.cancel, new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null){
                    dialog.dismiss ();
                }
            }
        } );
        AlertDialog alertDialog = builder.create ();
        alertDialog.show ();
    }

    private void deleteGrocery() {
        if (mCurrentGroceryUri != null){
            int rowsDeleted = getContentResolver ().delete ( mCurrentGroceryUri, null, null );
            if (rowsDeleted == 0){
                Toast.makeText ( this, R.string.editor_delete_grocery_failed, Toast.LENGTH_SHORT ).show ();
            }else {
                Toast.makeText ( this, R.string.editor_delete_grocery_successful, Toast.LENGTH_SHORT ).show ();
            }
        }
        finish ();
    }
}

