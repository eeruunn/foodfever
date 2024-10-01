package com.bigoval.foodfever;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User on 2/28/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "logindata";
    private static final String TABLE_NAME = "tokens";
    private static final String TABLE_NAME2 = "login";
    private static final String TABLE_NAME3 = "user_data";
    private static final String USERCOL1 = "name";
    private static final String USERCOL2 = "id";
    private static final String USERCOL3 = "phone";
    private static final String COL1 = "ID";
    private static final String COL2 = "token";
    private static final String COL3 = "userid";
    private static final String LOGGEDIN = "loggedin";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 +" TEXT,"+ COL3+" TEXT)";
        String createTable2 = "CREATE TABLE " + TABLE_NAME2 + "(loggedin INTEGER DEFAULT 1)";
        String createTable3 = "CREATE TABLE " + TABLE_NAME3 + "("+USERCOL1 + " TEXT, " + USERCOL2 +" TEXT,"+ USERCOL3+" TEXT)";

        db.execSQL(createTable);
        db.execSQL(createTable2);
        db.execSQL(createTable3);
        ContentValues values = new ContentValues();
        values.put("loggedin",0);
        db.insert("login",null,values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        onCreate(db);
    }

    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean addUserData(String name,String id,String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERCOL1, name);
        contentValues.put(USERCOL2, id);
        contentValues.put(USERCOL3, phone);

        Log.d(TAG, "addData: Adding " + name+","+id+","+phone + " to " + TABLE_NAME3);

        long result = db.insert(TABLE_NAME3, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getLoginData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+ LOGGEDIN +"FROM " + TABLE_NAME2;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }


}
