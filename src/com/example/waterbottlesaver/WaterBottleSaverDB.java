package com.example.waterbottlesaver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.Context;
import android.content.ContentValues;


public class WaterBottleSaverDB {
	
	private final DatabaseOpenHelper mDatabaseHelper;
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "waterfill.db";
	private static String DB_PATH = "/data/data/com.example.waterbottlesaver/databases/";
	
	/**
	 * Constructor
	 */
	public WaterBottleSaverDB(Context context){
		mDatabaseHelper = new DatabaseOpenHelper(context);
	}
	
	public String testDateTimeEntry(){
		String date = "";
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] {WBF_DBContract.WaterBottleHistoryEntry.TIME};
		String selection = WBF_DBContract.WaterBottleHistoryEntry.KEY_ID1 + " =?";
		String[] singleSelection = new String[] {"1"};
		Cursor cursor = db.query(WBF_DBContract.WaterBottleHistoryEntry.TABLE1, projection, selection, singleSelection, null, null, null);
		if (cursor == null) {
			return date;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return date;
		}
		
		return cursor.getString(0); 
	}
	
	/**
	 * Adjust the total amount of plastic bottles saved
	 * @param toAdd - The number of refills
	 * @return True if added, false otherwise
	 */
	public boolean adjustTotalSaved(float toAdd){
		if(toAdd <= 0){ return false; }
		
		float sum = getTotalSaved() + (toAdd*getBottleFillSize())/20;
		
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		
		Log.d("Database", "Got writeable database");
		ContentValues fillValue = new ContentValues();

		fillValue.put(WBF_DBContract.WaterBottleFillEntry.KEY_TOTALSAVED, sum);
		String where = WBF_DBContract.WaterBottleFillEntry.KEY_ID1 + " =?";
		String[] whereArgs = new String[] {"1"};
		db.update(WBF_DBContract.WaterBottleFillEntry.TABLE1, fillValue, where, whereArgs);
		
		return true;
	}
	
	/**
	 * Gets the total amount of plastic bottles saved over time
	 * @return The total estimation of plastic bottles saved over time
	 */
	public float getTotalSaved(){

		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] {WBF_DBContract.WaterBottleFillEntry.KEY_TOTALSAVED};
		String selection = WBF_DBContract.WaterBottleFillEntry.KEY_ID1 + " =?";
		String[] singleSelection = new String[] {"1"};
		Cursor cursor = db.query(WBF_DBContract.WaterBottleFillEntry.TABLE1, projection, selection, singleSelection, null, null, null);
		if (cursor == null) {
			return -1;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return -1;
		}
		
		return cursor.getFloat(0);
	}
	
	/**
	 * Gets the size of the bottle being used
	 * @return The size of the bottle being used
	 */
	public float getBottleFillSize(){

		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] {WBF_DBContract.WaterBottleFillEntry.KEY_BOTTLESIZE};
		String selection = WBF_DBContract.WaterBottleFillEntry.KEY_ID1 + " =?";
		String[] singleSelection = new String[] {"1"};
		Cursor cursor = db.query(WBF_DBContract.WaterBottleFillEntry.TABLE1, projection, selection, singleSelection, null, null, null);
		if (cursor == null) {
			return -1;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return -1;
		}
		
		return cursor.getFloat(0);
	}
	
	/**
	 * Adjusts the size of the bottle you are using to fill
	 * @param size - The volume of the bottle
	 * @return - True if adjustment successful, false otherwise.
	 */
	public boolean adjustBottleSize(float size){	
		
		if(size <= 0){ return false; }
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		
		Log.d("Database", "Got writeable database");
		ContentValues fillValue = new ContentValues();

		fillValue.put(WBF_DBContract.WaterBottleFillEntry.KEY_BOTTLESIZE, size);
		String where = WBF_DBContract.WaterBottleFillEntry.KEY_ID1 + " =?";
		String[] whereArgs = new String[] {"1"};
		db.update(WBF_DBContract.WaterBottleFillEntry.TABLE1, fillValue, where, whereArgs);
		
		return true;
	}
	
	public boolean clearBottlesSaved(){
		
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(WBF_DBContract.WaterBottleFillEntry.KEY_TOTALSAVED, 0);
		String where = WBF_DBContract.WaterBottleFillEntry.KEY_ID1 + " =?";
		String[] whereArgs = new String[] {"1"};
		db.update(WBF_DBContract.WaterBottleFillEntry.TABLE1, values, where, whereArgs);
		
		return true;
	}
	
	/**
	 * Clears the total number of water bottles filled
	 * @return - True if clear successful, false otherwise.
	 */
	public boolean clearTotalFills(){
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(WBF_DBContract.WaterBottleFillEntry.KEY_FILLVAL, 0);
		String where = WBF_DBContract.WaterBottleFillEntry.KEY_ID1 + " =?";
		String[] whereArgs = new String[] {"1"};
		db.update(WBF_DBContract.WaterBottleFillEntry.TABLE1, values, where, whereArgs);
		
		return true;
	}
	
	/**
	 * Gets the total number of water bottles filled
	 * @return int - The total number of water bottles filled
	 */
	public int getTotalFills(){
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		String[] projection = new String[] {WBF_DBContract.WaterBottleFillEntry.KEY_FILLVAL};
		String selection = WBF_DBContract.WaterBottleFillEntry.KEY_ID1 + " =?";
		String[] singleSelection = new String[] {"1"};
		Cursor cursor = db.query(WBF_DBContract.WaterBottleFillEntry.TABLE1, projection, selection, singleSelection, null, null, null);
		if (cursor == null) {
			return -1;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return -1;
		}
		
		return cursor.getInt(0);
	}
	
	/**
	 * Add the number of water bottles filled to the total
	 * @param numFilled - The number of water bottles filled
	 * @return - True if adding was successful, false otherwise
	 */
	public boolean insertWaterFill(int numFilled){
		if(numFilled <= 0){ return false;}
		int updatedSum = 0;
		
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		
		Log.d("Database", "Got writeable database");
		ContentValues fillValue = new ContentValues();

		updatedSum = getTotalFills() + numFilled;
		
		//Now update value
		fillValue.put(WBF_DBContract.WaterBottleFillEntry.KEY_FILLVAL, updatedSum);
		String where = WBF_DBContract.WaterBottleFillEntry.KEY_ID1 + " =?";
		String[] whereArgs = new String[] {"1"};
		db.update(WBF_DBContract.WaterBottleFillEntry.TABLE1, fillValue, where, whereArgs);
		
		return true;
	}
	
	public static class DatabaseOpenHelper extends SQLiteOpenHelper{
		
		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;
		
		DatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mHelperContext = context;
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			mDatabase = db;
			//db.execSQL(TABLE_CREATE1);
			//db.execSQL(TABLE_CREATE2);
			//db.execSQL(TABLE_CREATE3);
			//db.execSQL("PRAGMA foreign_keys=ON;");// enable foreign keys
			
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
			
			Log.d("DatabaseOpenHelper", "onCreate");			
		}
		
		/**
		 * Adds the number of filled water bottles to the DB
		 * @param numFilled - 
		 * @return
		 */
//		public long addFill(int numFilled){
//			ContentValues initialValues = new ContentValues();
//			initialValues.put(WBF_DBContract.WaterBottleFillEntry.KEY_FILLVAL, value)
//			return 0;
//		}
		
		public void createDataBase() throws IOException {
			boolean dbExist = checkDataBase();
			if (dbExist) {
				Log.d("DatabaseOpenHelper", "DATBASE EXISTS");
			} else {
			
				this.getReadableDatabase();
				this.close();
				
				Log.v("database", "databae is being copied from assets to new db file");
				try {
					copyDataBase();
				} catch (IOException e) {
					throw new Error("Error copying database");
				}
			}
		}

		private boolean checkDataBase() {
			
			/*Log.d("DatabaseOpenHelper", "checkDatabase");
			File dbFile = new File(DB_PATH + DATABASE_NAME);
			return dbFile.exists();*/
			SQLiteDatabase checkDB = null;
			try {
				String myPath = DB_PATH + DATABASE_NAME;
				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READONLY);
			} catch (SQLiteException e) {
			}
			if (checkDB != null) {
				checkDB.close();
			}
			return checkDB != null ? true : false;
		}

		private void copyDataBase() throws IOException {
			// Open your local db as the input stream
			InputStream myInput = mHelperContext.getAssets().open(DATABASE_NAME);

			// Path to the just created empty db
			String outFileName = DB_PATH + DATABASE_NAME;

			// Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);

			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
			Log.d("DatabaseOpenHelper", "copydatabase");
		}

		public void openDataBase() throws SQLException {
			// Open the database
			String myPath = DB_PATH + DATABASE_NAME;
			
			try{
				mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);//SQLiteDatabase.NO_LOCALIZED_COLLATORS);//SQLiteDatabase.CREATE_IF_NECESSARY);
			}catch(SQLException e){
				Log.e("openDatabase", e.toString());
			}
		}
		
		@Override
	    public synchronized void close() 
	    {
	        if(mDatabase != null)
	            mDatabase.close();
	        super.close();
	    }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onCreate(db);
		}
	}
	
}
