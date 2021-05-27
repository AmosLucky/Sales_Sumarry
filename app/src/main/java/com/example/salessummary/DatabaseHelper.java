package com.example.salessummary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
  final public static String databaseName = "salesDb";
  final  public static String tableName = "dataTable";
    public static int databaseVersion = 1;

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create Table  IF NOT EXISTS "+ tableName + "(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, date_created TEXT,data TEXT,total TEXT )");


    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public  boolean insertData(ContentValues contentValues){
        SQLiteDatabase db = getWritableDatabase();

        long result = db.insert(tableName,null,contentValues);

        if(result < 0){


            return  false;

        }else{
            return  true;


        }
    }

    public  boolean updateDate(String id,ContentValues contentValues){
        SQLiteDatabase db = getWritableDatabase();

        String where = "id=?";
        String[] whereArgs = new String[] {String.valueOf(id)};

        long result = db.update(tableName,contentValues,where, whereArgs);

        if(result < 0){


            return  false;

        }else{
            return  true;


        }
    }


    public Cursor getData(){


        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("Select * from "+ tableName +" order by id desc ", new String[] {});


        return res;

    }


}
