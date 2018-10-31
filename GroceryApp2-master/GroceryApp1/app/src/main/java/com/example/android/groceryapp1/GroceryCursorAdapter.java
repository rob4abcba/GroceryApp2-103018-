package com.example.android.groceryapp1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.groceryapp1.data.GroceryContract;

class GroceryCursorAdapter extends CursorAdapter{
    int quantity = 0;
    public GroceryCursorAdapter(Context context, Cursor cursor) {
        super(context,cursor, 0/* flags*/);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from ( context ).inflate ( R.layout.list_item, parent, false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById ( R.id.grocery_name );
        TextView priceTextView = view.findViewById ( R.id.groceryPrice );
        final TextView qtyTextView = view.findViewById ( R.id.grocery_qty );

        int nameColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_NAME );
        int priceColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_PRICE );
        int qtyColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY );

        String groceryName = cursor.getString ( nameColumnIndex );
        final String groceryPrice = cursor.getString ( priceColumnIndex );
        final String groceryQty = cursor.getString( qtyColumnIndex );


        nameTextView.setText ( groceryName );
        priceTextView.setText ( groceryPrice );
        qtyTextView.setText ( String.valueOf ( groceryQty ) );

        Button saleBtn = view.findViewById ( R.id.saleBtn );
        saleBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
               if (quantity > 0){
                    quantity = quantity - 1;
               } if (quantity == 0 ){
                    qtyTextView.setText ( String.valueOf ( groceryQty ) );
                }
            }
        } );
    }
}
