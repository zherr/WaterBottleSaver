package com.example.waterbottlesaver;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * This class is used to test the integrity of the database.
 * It does this by testing the creation, opening, and actual data of
 * the parent database class.
 * @author Zach
 * Code idea taken from:
 * {@link http://tinyurl.com/bqgnqhc}
 *
 */

public class DBTestAdapter{
	
	protected static final String TAG = "DataAdapter";
	
	private SQLiteDatabase database;
	private final Context mContext;
	private WaterBottleSaverDB.DatabaseOpenHelper dbOpenHelper;
	
	public DBTestAdapter(Context context){
		mContext = context;
		dbOpenHelper = new WaterBottleSaverDB.DatabaseOpenHelper(context);
	}
	
	public DBTestAdapter createDatabase() throws SQLException{
		
		Log.d("DBTestAdapter", "createDatabase");
        try 
        {
            dbOpenHelper.createDataBase();
        } 
        catch (IOException mIOException) 
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }
	
	public DBTestAdapter open() throws SQLException{
		
		dbOpenHelper = new WaterBottleSaverDB.DatabaseOpenHelper(mContext);
		try{
			dbOpenHelper.openDataBase();
			dbOpenHelper.close();
			database = dbOpenHelper.getReadableDatabase();
			Log.d("DBTestAdapter", "open");
		}catch(SQLException e){
			Log.e(TAG, "open >>"+ e.toString());
            throw e;
		}
		return this;
	}
	
	public void close() 
    {
		Log.d("DBTestAdapter", "close");
        dbOpenHelper.close();
    }
	
	public Cursor getTestData()
     {
         try
         {
             String sql ="SELECT * FROM " + WBF_DBContract.WaterBottleFillEntry.TABLE1;

             Cursor mCur = database.rawQuery(sql, null);
             if (mCur!=null)
             {
                mCur.moveToNext();
             }
             Log.d("DBTestAdapter", "getTestData");
             return mCur;
         }
         catch (SQLException mSQLException) 
         {
             Log.e(TAG, "getTestData >>"+ mSQLException.toString());
             throw mSQLException;
         }
     }
}