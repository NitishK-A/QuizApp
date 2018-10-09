package com.example.nitishatal.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by User on 2/28/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "myDatabase";    // Database Name

    private static final String TABLE_NAME = "people_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";
    private static final String COL3 = "answer";
    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " ("+COL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL2+" VARCHAR(255) ,"+ COL3+" VARCHAR(225));";


    public DbHelper(Context context) {
        super(context, TABLE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // String createTable = "CREATE TABLE " + TABLE_NAME + " ("+COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
         //       COL2 +" VARCHAR(255), "+COL3+" VARCHAR(225);)";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addData(String item1,String item2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, item1);
        cv.put(COL3,item2);



        Log.d(TAG, "addData: Adding " + item1 + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, cv);

        //if date as inserted incorrectly it will return -1
        return result;
    }

    public long addAnswer(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL3, item);
        Log.d(TAG, "addAnswer: Adding " + item + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME,null, cv);
        return result;

        //if date as inserted incorrectly it will return -1
        /*if (result == -1) {
            return false;
        } else {
            return true;
        }*/
    }

    /**
     * Returns all the data from database
     * @return
     */
    public String getData(){

        SQLiteDatabase db = this.getWritableDatabase();
        //String query = "SELECT * FROM " + TABLE_NAME;
        //Cursor data = db.rawQuery(query, null);
        //String a=data.getString()
        //return data;
        String[] columns={COL1,COL2,COL3};
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        StringBuffer buffer=new StringBuffer();
        for (int i=0;i<30;i++){
            cursor.moveToPosition(i);
            int cid =cursor.getInt(cursor.getColumnIndex(COL1));
            String name =cursor.getString(cursor.getColumnIndex(COL2));
            String  password =cursor.getString(cursor.getColumnIndex(COL3));
            buffer.append(cid+ "  , " + name + "  , " + password +" \n");
        }
        return buffer.toString();
    }


    public int UpdateAns(String id , String newans)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL3,newans);
        //String[] whereArgs= {oldans};
        String[] whereId={id};
        int count =db.update(TABLE_NAME,contentValues, COL1+" = ?",whereId );
        return count;
    }
    public void Update(int id , String newans, String oldans)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 +
                " = '" + newans + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL3 + " = '" + oldans + "'";
        db.execSQL(query);

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
    public void exportDB() throws IOException {

        SQLiteDatabase sqldb = this.getReadableDatabase(); //My Database class
        Cursor c = null;

        c = sqldb.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        int rowcount = 0;
        int colcount = 0;
        File sdCardDir = Environment.getExternalStorageDirectory();
        if (!sdCardDir.exists())
        {
            sdCardDir.mkdirs();
        }
        String filename = "MyBackUp.csv";

        // the name of the file to export with
        File saveFile = new File(sdCardDir, filename);
        FileWriter fw = new FileWriter(saveFile);


        BufferedWriter bw = new BufferedWriter(fw);
        rowcount = c.getCount();
        colcount = c.getColumnCount();
        if (rowcount > 0) {
            c.moveToFirst();

            for (int i = 0; i < colcount; i++) {
                if (i != colcount - 1) {

                    bw.write(c.getColumnName(i) + ",");

                } else {

                    bw.write(c.getColumnName(i));

                }
            }
            bw.newLine();

            for (int i = 0; i < rowcount; i++) {
                c.moveToPosition(i);

                for (int j = 0; j < colcount; j++) {
                    if (j != colcount - 1)
                        bw.write(c.getString(j) + ",");
                    else
                        bw.write(c.getString(j));
                }
                bw.newLine();
            }
            bw.flush();

        }

    }

    public  void export(FileOutputStream out){
        SQLiteDatabase db = this.getReadableDatabase(); //My Database class
        String Filename="data.csv";
        Cursor cursor=db.query(TABLE_NAME,null,null,null,null,null,null);
        for(int i=0;i<30;i++){
            cursor.moveToPosition(i);
            String question=cursor.getString(cursor.getColumnIndex(COL2));
            String torf=cursor.getString(cursor.getColumnIndex(COL3));
            String data=question+" ,"+torf+"\n";
            try {
                out.write(data.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
    public void databasexport(FileOutputStream out) throws IOException {

        SQLiteDatabase db = this.getWritableDatabase();
        String Filename="data.csv";
        String[] columns={COL1,COL2,COL3};
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        StringBuffer buffer=new StringBuffer();
        while (cursor.moveToNext()){
            int cid =cursor.getInt(cursor.getColumnIndex(COL1));
            String name =cursor.getString(cursor.getColumnIndex(COL2));
            String  password =cursor.getString(cursor.getColumnIndex(COL3));
            buffer.append(cid+ " ,  " + name + "  , " + password +" \n");
            String fin=buffer.toString();
            out.write(fin.getBytes());
        }

    }









}

