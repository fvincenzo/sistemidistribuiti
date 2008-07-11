package android.client;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import com.google.android.maps.Point;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper {

	static String LOGGER = "android.client.DBHelper";

	class Location {
		public long _id;
		public String address;
		public int latitude;
		public int longitude;
		public String preferred;
		public boolean isDefault;
	}

	private static final String DATABASE_CREATE =
		"create table locations (_id integer primary key autoincrement, "
		+ "latitude integer, longitude integer, preferred text not null, address text, defaultFlag integer);";

	private static final String DATABASE_NAME = "MyContacts.db";

	private static final String DATABASE_TABLE = "locations";

	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase db;

	public DBHelper(Context ctx) {
		try {
			db = ctx.openDatabase(DATABASE_NAME, null);
		} catch (FileNotFoundException e) {
			try {
				db =
					ctx.createDatabase(DATABASE_NAME, DATABASE_VERSION, 0,
							null);
				db.execSQL(DATABASE_CREATE);


			} catch (FileNotFoundException e1) {
				Log.e(LOGGER, e1.toString());
				db = null;
			}
		}
	}

	public void close() {
		db.close();
	}

	public void deleteRow(Location l) {
		db.delete(DATABASE_TABLE, "latitude=" + l.latitude+" AND longitude="+l.longitude+" AND preferred ='"+l.preferred+"'", null);
	}

	public boolean addLocation(Point p, String type) {

		String selection[] = new String[] {
				"_id"
		};
		Cursor c = db.query(DATABASE_TABLE, selection, 
				"latitude="+p.getLatitudeE6() +" AND longitude="+p.getLongitudeE6(), null, null, null, null);
		if (c.count() == 0) {

			ContentValues initialValues = new ContentValues();

			initialValues.put("latitude", p.getLatitudeE6());
			initialValues.put("longitude", p.getLongitudeE6());
			initialValues.put("preferred", type);
			initialValues.put("defaultFlag", 0);
			if (db.insert(DATABASE_TABLE, null, initialValues) != -1){
				return true;
			}
		}
		return false;

	}


	public Location fetchRow(long rowId) {
		Location row = new Location();

		Cursor c = db.query(true, DATABASE_TABLE, new String[] {
				"_id",  "latitude", "longitude","preferred","address", "defaultFlag"}, "_id=" + rowId, null, null, null, null);
		if (c.count() > 0) {
			c.first();
			row._id = c.getLong(0);
			row.latitude = c.getInt(1);
			row.longitude = c.getInt(2);
			row.preferred = c.getString(3);
			row.address = c.getString(4);
			if (c.getInt(5) == 1){
				row.isDefault = true;
			}
			else row.isDefault = false;
			return row;
		} 
		return null;

	}
	public List<Location> fetchAllRows() {
		List<Location> list = new LinkedList<Location>();
		Location row;

		Cursor c = db.query(true, DATABASE_TABLE, new String[] {
				"_id",  "latitude", "longitude","preferred","address", "defaultFlag"},null, null, null, null, null);
		while (c.next()) {
			
			row = new Location();
			row._id = c.getLong(0);
			row.latitude = c.getInt(1);
			row.longitude = c.getInt(2);
			row.preferred = c.getString(3);
			row.address = c.getString(4);
			if (c.getInt(5) == 1){
				row.isDefault = true;
			}
			else row.isDefault = false;
			list.add(row);
		} 
		return list;

	}


}
