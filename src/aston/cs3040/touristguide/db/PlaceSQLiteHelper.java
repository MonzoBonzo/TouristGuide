package aston.cs3040.touristguide.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlaceSQLiteHelper extends SQLiteOpenHelper
{
	/** Name of Database */
	private static final String DBNAME = "Places";
	
	/** Version of the Database */
	private static final int DATABASE_VERSION = 1;
	
	/** Name of Table */
	private static final String TABLE_VISITED = "Visited";
	private static final String TABLE_ADDED = "Added";
	
	/** Table fields within Visited table */
	public static final String PLACE_ID = "placeID";
	public static final String PLACE_NAME = "placeName";
	public static final String PLACE_VICINITY = "placeVicinty";
	public static final String PLACE_ADDRESS = "placeAddress";
	public static final String PLACE_TYPES = "placeTypes";
	public static final String PLACE_LOCATION = "placeLocation";
	public static final String PLACE_TEL = "placeTelephone";
	public static final String PLACE_WEBSITE = "placeWebsite";
	public static final String PLACE_LAT = "placeLatitude";
	public static final String PLACE_LON = "placeLongitude";
	
	/** Table fields within Added table */
	public static final String PLACE_ADDED_ID = "placeAddedID";
	public static final String PLACE_REFERENCE_ID = "placeAddedReference";
	public static final String PLACE_ADDED_NAME = "placeAddedName";
	public static final String PLACE_ADDED_ADDRESS = "placeAddedAddress";
	
	public PlaceSQLiteHelper(Context context)
	{
		super(context, DBNAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database)
	{
		//Visited table
		String sql = 
			"CREATE TABLE " + TABLE_VISITED + " (" + 
			PLACE_ID + " INTEGER PRIMARY KEY, " + 
			PLACE_NAME + " TEXT, " +
			PLACE_VICINITY + " TEXT, " +
			PLACE_ADDRESS + " TEXT, " +
			PLACE_TYPES + " TEXT, " +
			PLACE_LOCATION + " TEXT, " +
			PLACE_TEL + " TEXT, " +
			PLACE_WEBSITE + " TEXT," +
			PLACE_LAT + " REAL, " +
			PLACE_LON + " REAL" +
			" )";
		
		Log.i("Tourist Guide", sql);
		database.execSQL(sql);
		
		//Added table
		sql = 
			"CREATE TABLE " + TABLE_ADDED + " (" + 
			PLACE_ADDED_ID + " INTEGER PRIMARY KEY, " +
			PLACE_ADDED_NAME + " TEXT, " +
			PLACE_ADDED_ADDRESS + " TEXT, " +
			PLACE_REFERENCE_ID + " TEXT " +
			" )";
		
		Log.i("Tourist Guide", sql);
		database.execSQL(sql);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITED);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDED);
		
		onCreate(db);
	}
	
	public long addVisitedPlace(ContentValues cv)
	{
		//More sophisticated find out if it exists first		
		Cursor check = checkVisitedPlaceExists(cv.getAsString(PLACE_NAME));
		
		//Check if Place already exists
		if (check.getCount() < 1)
		{
			check.close();
			SQLiteDatabase db = this.getWritableDatabase();		
			long i = db.insert(TABLE_VISITED, PLACE_NAME, cv);
			db.close();
			return i;
		}
		check.close();
		return -1; //Place already exits or error above
	}
	
	public Cursor getAllVisitedPlaces()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VISITED, null);
		return cursor;		
	}
	
	public long addAddedPlace(ContentValues cv)
	{
		//More sophisticated find out if it exists first
		
		
		SQLiteDatabase db = this.getWritableDatabase();		
		long i = db.insert(TABLE_ADDED, PLACE_ADDED_NAME, cv);
		db.close();
		return i;
	}
	
	public Cursor getAllAddedPlaces()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADDED, null);
		return cursor;		
	}
	
	private Cursor checkVisitedPlaceExists(String placeName)
	{
		if (placeName.contains("'"))
		{
			placeName = placeName.replace("'", "");
		}
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VISITED +  " WHERE " + PLACE_NAME + " = '" + placeName + "'", null);
		return cursor;		
	}
	
	private Cursor checkAddedPlaceExists(String placeName)
	{
		if (placeName.contains("'"))
		{
			placeName = placeName.replace("'", "");
		}
		
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADDED +  " WHERE " + PLACE_ADDED_NAME + " = '" + placeName + "'", null);
		return cursor;		
	}
}