package com.excellence.dbdao;

import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.excellence.dbdao.DBUtil.PEOPLE_AGE;
import static com.excellence.dbdao.DBUtil.PEOPLE_NAME;
import static com.excellence.dbdao.DBUtil.TABLE_NAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
	private static final int INSERT_COUNT = 1000;

	private DBUtil mDBUtil = null;
	private Executor mExecutors = null;

	private TextView mTravelTime = null;
	private Button mNormalInsert = null;
	private Button mStmtInsert = null;
	private Button mTransactionInsert = null;
	private Button mTransactionStmtInsert = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDBUtil = DBUtil.getInstance(this);
		mExecutors = Executors.newSingleThreadExecutor();

		mTravelTime = (TextView) findViewById(R.id.travel_time_text);
		mNormalInsert = (Button) findViewById(R.id.normal_insert);
		mStmtInsert = ((Button) findViewById(R.id.stmt_insert));
		mTransactionInsert = (Button) findViewById(R.id.transaction_insert);
		mTransactionStmtInsert = (Button) findViewById(R.id.transaction_stmt_insert);

		mNormalInsert.setOnClickListener(this);
		mStmtInsert.setOnClickListener(this);
		mTransactionInsert.setOnClickListener(this);
		mTransactionStmtInsert.setOnClickListener(this);
	}

	@Override
	public void onClick(final View v)
	{
		mExecutors.execute(new Runnable()
		{
			@Override
			public void run()
			{
				synchronized (this)
				{
					mDBUtil.dropTable();
					mDBUtil.createTable();
					final long startTime = System.nanoTime();
					switch (v.getId())
					{
					case R.id.normal_insert:
						normalInsert();
						break;

					case R.id.stmt_insert:
						stmtInsert();
						break;

					case R.id.transaction_insert:
						transactionInsert();
						break;

					case R.id.transaction_stmt_insert:
						transactionStmtInsert();
						break;
					}
					final long endTime = System.nanoTime();
					mTravelTime.post(new Runnable()
					{
						@Override
						public void run()
						{
							mTravelTime.setText(((Button) v).getText() + " - " + (endTime - startTime) / 1000 / 1000);
						}
					});
				}
			}
		});
	}

	private void transactionStmtInsert()
	{
		// 类似GreenDao的做法
		try
		{
			mDBUtil.getDatabase().beginTransaction();
			stmtInsert();
			mDBUtil.getDatabase().setTransactionSuccessful();
		}
		finally
		{
			mDBUtil.getDatabase().endTransaction();
		}
	}

	private void transactionInsert()
	{
		try
		{
			mDBUtil.getDatabase().beginTransaction();
			normalInsert();
			mDBUtil.getDatabase().setTransactionSuccessful();
		}
		finally
		{
			mDBUtil.getDatabase().endTransaction();
		}
	}

	private void stmtInsert()
	{
		String sql = String.format("insert into %1$s(%2$s, %3$s) values(?, ?)", TABLE_NAME, PEOPLE_NAME, PEOPLE_AGE);
		SQLiteStatement stmt = mDBUtil.getDatabase().compileStatement(sql);
		for (int i = 0; i < INSERT_COUNT; i++)
		{
			// 绑定的数据不能为null，并且是从1开始绑定
			People people = new People("name" + i, i);
			stmt.bindString(1, people.name);
			stmt.bindLong(2, people.age);
			stmt.executeInsert();
		}
	}

	private void normalInsert()
	{
		for (int i = 0; i < INSERT_COUNT; i++)
		{
			People people = new People("name" + i, i);
			mDBUtil.insert(people);
		}
	}
}
