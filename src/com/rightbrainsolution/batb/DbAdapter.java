package com.rightbrainsolution.batb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DbAdapter {

	
	private static  String DATABASE_NAME = "batb_db.db";
	
	private static final int DATABASE_VERSION = 1;
	
	public static String[] DATABASE_CREATE = {

		"CREATE TABLE " + ApplicationStorage.TABLE_USERS + "("
			+ ApplicationStorage.USERS_USER_ID + " VARCHAR(200)"
			+ "," + ApplicationStorage.USERS_USER_TOKEN + " VARCHAR(200)"
			+ " )"
		,
		
		"CREATE TABLE " + ApplicationStorage.TABLE_OUTLETS + "("
			+ ApplicationStorage.OUTLETS_OUTLET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
			+ "," + ApplicationStorage.OUTLETS_OUTLET_NAME + " VARCHAR(200)"
			+ " )"
		,
		
		"CREATE TABLE " + ApplicationStorage.TABLE_BRANDS + "("
			+ ApplicationStorage.BRANDS_BRAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
			+ "," + ApplicationStorage.BRANDS_BRAND_NAME + " VARCHAR(200)"
			+ "," + ApplicationStorage.BRANDS_BRAND_PRICE + " DOUBLE"
			+ " )"
		,
		
		"CREATE TABLE " + ApplicationStorage.TABLE_SALES + "("
			+ ApplicationStorage.SALES_SALE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
			+ "," + ApplicationStorage.SALES_SALE_OUTLET + " VARCHAR(200)"
			+ "," + ApplicationStorage.SALES_SALE_TOTAL_PRICE + " DOUBLE"
			+ "," + ApplicationStorage.SALES_SALE_DATE + " DATE"
			+ "," + ApplicationStorage.SALES_SALE_SINK_FLAG + " INTEGER"
			+ " )"
		,
				
		"CREATE TABLE " + ApplicationStorage.TABLE_SALES_DETAILS + "("
			+ ApplicationStorage.SALES_DETAILS_SALE_ID + " INTEGER"
			+ "," + ApplicationStorage.SALES_DETAILS_BRAND_ID + " INTEGER"
			+ "," + ApplicationStorage.SALES_DETAILS_BRAND_AMOUNT + " INTEGER"
			+ "," + ApplicationStorage.SALES_DETAILS_BRAND_TOTAL_PRICE + " DOUBLE"
			+ " )"
		
	};

	private final Context context;
	// Variable to hold the database instance
	private SQLiteDatabase db;
	// Database open/upgrade helper
	private myDbHelper dbHelper;

	public DbAdapter(Context _context) {
		
		context = _context;
		
		//DATABASE_NAME = databaseName;
		
		
		dbHelper = new myDbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public DbAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}
	
	public long insert( String table, String nullColumnHack, ContentValues values) {
		  // TODO fill in ContentValues to represent the new row
		  return db.insert( table, nullColumnHack, values);
		} 
	public int delete(String table, String whereClause, String[] whereArgs) {
		return db.delete( table,whereClause,whereArgs);
		}
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs)
	{
		return db.update( table, values, whereClause,  whereArgs);
	}
	
	public Cursor rawQuery(String sql, String[] selectionArgs)
	{
		return db.rawQuery( sql,selectionArgs);
	}
	
	public void  execSQL  (String sql)
	{
		db.execSQL(sql);
	}
	
	private static class myDbHelper extends SQLiteOpenHelper {
		public myDbHelper(Context context, String name, CursorFactory factory,
				int version) {

			super(context, name, factory, version);
		}

		// Called when no database exists in
		// disk and the helper class needs
		// to create a new one.
		@Override
		public void onCreate(SQLiteDatabase _db) {
			for (int i = 0; i < DATABASE_CREATE.length; i++) 
			{
				_db.execSQL(DATABASE_CREATE[i]);
			}
			
			//Insert initial data here
			
		}

		// Called when there is a database version mismatch meaning that
		// the version of the database on disk needs to be upgraded to
		// the current version.
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			// Log the version upgrade.
			// Log.w(“TaskDBAdapter”, “Upgrading from version “ +
			// _oldVersion + “ to “ +
			// _newVersion +
			// “, which will destroy all old data”);
			// Upgrade the existing database to conform to the new version.
			// Multiple previous versions can be handled by comparing
			// _oldVersion and _newVersion values.
			// The simplest case is to drop the old table and create a
			// new one.
			for (int i = 0; i < DATABASE_CREATE.length; i++) {
				_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE[i]);
			}
			// Create a new one.
			onCreate(_db);
		}
	}

}
