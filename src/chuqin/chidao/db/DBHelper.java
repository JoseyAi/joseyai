package chuqin.chidao.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "chuqin_chidao"; // DB name
	public static final String TB_NAME = "chuqin_chd"; // DB name
	private Context chdcontext;
	private DBHelper chdDbHelper;
	private SQLiteDatabase db;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, 11);
		this.chdcontext = context;
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

	}

	/**
	 * 用户第一次使用软件时调用的操作，用于获取数据库创建语句（SW�??,然后创建数据�??
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//,clothes text,doctor text,laiwang text,baby text,live text,other text,remark text
		String sql = "create table if not exists chuqin_chd(id integer primary key,time text,name text,number text,xibie text,banji text,chd_time text,chd_reason text)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/* 打开数据�??,如果已经打开就使用，否则创建 */
	public DBHelper open() {
		if (null == chdDbHelper) {
			chdDbHelper = new DBHelper(chdcontext);
		}
		db = chdDbHelper.getWritableDatabase();
		return this;
	}

	/* 关闭数据�?? */
	public void close() {
		db.close();
		chdDbHelper.close();
	}

	/** 添加数据 */
	public long insert(String tableName, ContentValues values) {
		return db.insert(tableName, null, values);
	}

	public void delete(String tableName){
		db.delete(tableName, "name=?", new String[]{String.valueOf(tableName)});
		
	}
	
	/** 查询数据 */
	public Cursor findList(String tableName, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		return db.query(tableName, columns, selection, selectionArgs, groupBy,
				having, orderBy, limit);
	}

	public Cursor exeSql(String sql) {
		return db.rawQuery(sql, null);
	}
	public Cursor query(String tableName){  
        Cursor c = db.query(tableName, null, null, null, null, null, null);  
        return c;  
    }  
}