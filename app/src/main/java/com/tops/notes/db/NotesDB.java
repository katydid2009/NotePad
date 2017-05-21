package com.tops.notes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 实现SQLiteOpenHelper接口的NotesDB类，用于创建数据库表
 * 
 * @author TOPS
 * 
 */
public class NotesDB extends SQLiteOpenHelper {

	public static final String TABLE_NAME_NOTES = "notes";
	public static final String TABLE_NAME_MEDIA = "media";
	public static final String COLUMN_NAME_ID = "_id";
	public static final String COLUMN_NAME_NOTE_NAME = "name";
	public static final String COLUMN_NAME_NOTE_CONTENT = "content";
	public static final String COLUMN_NAME_NOTE_DATE = "date";
	public static final String COLUMN_NAME_MEDIA_PATH = "path";
	public static final String COLUMN_NAME_MEDIA_OWNER_NOTE_ID = "note_id";


	/**
	 * 当第一次打开数据库，表不存在时调用，以创建表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME_NOTES + "(" + COLUMN_NAME_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME_NOTE_NAME
				+ " TEXT NOT NULL DEFAULT \"\"," + COLUMN_NAME_NOTE_CONTENT
				+ " TEXT NOT NULL DEFAULT \"\"," + COLUMN_NAME_NOTE_DATE
				+ " TEXT NOT NULL DEFAULT \"\"" + ")");
		db.execSQL("CREATE TABLE " + TABLE_NAME_MEDIA + "(" + COLUMN_NAME_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ COLUMN_NAME_MEDIA_PATH + " TEXT NOT NULL DEFAULT \"\","
				+ COLUMN_NAME_MEDIA_OWNER_NOTE_ID
				+ " INTEGER NOT NULL DEFAULT 0" + ")");

	}

	/**
	 * 当表存在且是旧版本时调用，删除旧表，创建新表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public NotesDB(Context context) {
		super(context, "notes", null, 1);
	}

	public Cursor queryAll(){
		return getWritableDatabase().query(TABLE_NAME_NOTES, null, null, null, null, null, null);
	}

	public void deleteById(long id){
		deleteById(String.valueOf(id));
	}

	public void deleteById(String id){
		String where = COLUMN_NAME_ID + "=?";
		String[] whereValues = new String[]{id};
		getWritableDatabase().delete(TABLE_NAME_NOTES, where, whereValues);
	}

	public Cursor searchByTitle(String word){
		String where = COLUMN_NAME_NOTE_NAME  + "like?";
		String[] whereValues = new String[]{"%" + word + "%"};
		return getWritableDatabase().query(TABLE_NAME_NOTES, null, where, whereValues, null, null, null);
	}

}
