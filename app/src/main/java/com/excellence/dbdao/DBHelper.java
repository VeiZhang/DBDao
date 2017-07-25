package com.excellence.dbdao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Set;

import static com.excellence.dbdao.DBUtil.PEOPLE_ATTRS;
import static com.excellence.dbdao.DBUtil.TABLE_NAME;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/7/25
 *     desc   :
 * </pre>
 */

public class DBHelper extends SQLiteOpenHelper
{
	private static final String DB_NAME = "dao.db";
	private static final int DB_VERSION = 1;

	protected SQLiteDatabase mDatabase = null;

	public DBHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
		try
		{
			mDatabase = getWritableDatabase();
		}
		catch (Exception e)
		{
			mDatabase = getReadableDatabase();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		if (mDatabase == null)
			mDatabase = db;

		createTable(TABLE_NAME, PEOPLE_ATTRS);
	}

	protected boolean createTable(String tableName, TableAttrs attrs)
	{
		if (attrs == null)
			return false;

		Set<String> keys = attrs.keySet();
		if (keys.size() <= 0)
			return false;
		StringBuilder builder = new StringBuilder("create table if not exists ");
		builder.append(tableName);
		builder.append("(");
		for (String key : keys)
		{
			String attr = attrs.getAttr(key);
			builder.append(key);
			builder.append(" ");
			builder.append(attr);
			builder.append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");
		try
		{
			mDatabase.execSQL(builder.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected boolean isTableExist(String tableName)
	{
		boolean isTableExist = false;
		String sql = "select count(*) as c from sqlite_master where type = 'table' and name = '" + tableName + "'";
		Cursor cursor = mDatabase.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0)
		{
			if (cursor.moveToFirst() && cursor.getInt(0) > 0)
				isTableExist = true;
			cursor.close();
		}
		return isTableExist;
	}

	protected void dropTable(String tableName)
	{
		String sql = "drop table if exists " + tableName;
		mDatabase.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		dropTable(TABLE_NAME);
		createTable(TABLE_NAME, PEOPLE_ATTRS);
	}

	public SQLiteDatabase getDatabase()
	{
		return mDatabase;
	}

}
