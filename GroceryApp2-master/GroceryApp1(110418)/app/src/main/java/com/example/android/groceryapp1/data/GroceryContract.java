package com.example.android.groceryapp1.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class GroceryContract {

    private GroceryContract() {
    }
    public static final String CONTENT_AUTHORITY = "com.example.android.groceryapp1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_GROCERY = "grocery";

    public static final class GroceryEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GROCERY);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GROCERY;
        public static final String CONTENT_ITEM_TYPE =ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GROCERY;
        public final static String TABLE_NAME = "grocery";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_GROCERY_NAME = "name";

        public final static String COLUMN_GROCERY_PRICE = "price";

        public final static String COLUMN_GROCERY_QUANTITY = "quantity";

        public final static String COLUMN_GROCERY_SUPPLIER_NAME = "supplier";

        public final static String COLUMN_GROCERY_SUPPLER_PHONE = "supplier_phone";
    }
}
