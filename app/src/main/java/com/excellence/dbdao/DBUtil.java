package com.excellence.dbdao;

import android.content.ContentValues;
import android.content.Context;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/7/25
 *     desc   :
 * </pre>
 */

public class DBUtil extends DBHelper
{
	public static final String TABLE_NAME = "people";
	public static final String PEOPLE_NAME = "NAME";
	public static final String PEOPLE_AGE = "AGE";

	private static DBUtil mInstance = null;

	public static DBUtil getInstance(Context context)
	{
		if (mInstance == null)
			mInstance = new DBUtil(context);
		return mInstance;
	}

	private DBUtil(Context context)
	{
		super(context);
	}

	public void dropTable()
	{
		dropTable(TABLE_NAME);
	}

	public void createTable()
	{
		if (!isTableExist(TABLE_NAME))
			createTable(TABLE_NAME, PEOPLE_ATTRS);
	}

	public static TableAttrs PEOPLE_ATTRS = new TableAttrs()
	{
		{
			addAttr(PEOPLE_NAME, "TEXT");
			addAttr(PEOPLE_AGE, "INTEGER");
		}
	};

	public synchronized void insert(People people)
	{
		ContentValues values = new ContentValues();
		values.put(PEOPLE_NAME, people.name);
		values.put(PEOPLE_AGE, people.age);
		mDatabase.insert(TABLE_NAME, null, values);
	}
}
