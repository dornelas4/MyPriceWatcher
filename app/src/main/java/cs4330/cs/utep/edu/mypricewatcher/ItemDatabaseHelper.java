package cs4330.cs.utep.edu.mypricewatcher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @Author: Daniel Ornelas
 * Class to Manage SQL item database
 * @see SQLiteOpenHelper
 */
public class ItemDatabaseHelper extends SQLiteOpenHelper {
    //Logcat tag
    private static final String LOG = "ItemDBHelper";
    //Database Version
    private static final int DATABASE_VERSION = 4;
    //Database Name
    private static final String DATABASE_NAME = "DBITEMS";
    //Table Names
    private static final String ITEM_TABLE = "items";

    //KEY
    private static final String KEY_ID = "_id";

    //Columns
    private static final String COL_NAME = "name";
    private static final String COL_URL = "url";
    private static final String COL_INITIAL_PRICE = "initialPrice";
    private static final String COL_CURRENT_PRICE = "currentPrice";
    private static final String COL_PRICE_CHANGE = "priceChange";


    //Trip table create statements
    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE "
            + ITEM_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + COL_NAME + " STRING, "
            + COL_URL + " STRING, "
            + COL_INITIAL_PRICE + " REAL, "
            + COL_CURRENT_PRICE + " REAL, "
            + COL_PRICE_CHANGE + " STRING "
            + ")";


    //Constructor
    public ItemDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
        Create databases
        Only runs once
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating required tables
        db.execSQL(CREATE_TABLE_ITEMS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        // create new tables
        onCreate(db);
    }



    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on downgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);

        // create new tables
        onCreate(db);
    }

    //CRUD OPERATIONS
    /*

    /*
        Creating item
     */
    public long addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, item.getName());
        values.put(COL_URL, item.getUrl().toString());
        values.put(COL_INITIAL_PRICE, item.getInitialPrice());
        values.put(COL_CURRENT_PRICE, item.getCurrentPrice());
        values.put(COL_PRICE_CHANGE, item.getChange());

        //insert
        long id =db.insert(ITEM_TABLE, null, values);
        db.close();
        return id;
    }


    //Updating item
    public void updateItem(int id, DatabaseItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, item.getName());
        values.put(COL_URL, item.getUrl().toString());
        values.put(COL_INITIAL_PRICE, item.getInitialPrice());
        values.put(COL_CURRENT_PRICE, item.getCurrentPrice());
        values.put(COL_PRICE_CHANGE, item.getChange());
        // updating row
        db.update(ITEM_TABLE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Delete item
     * @param id id of item to be deleted
     */
    public void deleteItem(int id ) {
        SQLiteDatabase db = this.getWritableDatabase();



        // now delete the tag
        db.delete(ITEM_TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }
//

    // Getting all items
    public List<DatabaseItem> getItems() {
        ArrayList<DatabaseItem> items = new ArrayList<DatabaseItem>();
        String selectQuery = "SELECT * FROM " + ITEM_TABLE;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                DatabaseItem t = new DatabaseItem();
                t.setID(c.getInt(c.getColumnIndex(KEY_ID)));
                t.setName(c.getString(c.getColumnIndex(COL_NAME)));
                t.setUrl(c.getString(c.getColumnIndex(COL_URL)));
                t.setCurrentPrice(c.getDouble(c.getColumnIndex(COL_CURRENT_PRICE)));
                t.setInitialPrice(c.getDouble(c.getColumnIndex(COL_INITIAL_PRICE)));
                t.setChange();


                items.add(t);
            } while (c.moveToNext());
        }
        return items;
    }

}
