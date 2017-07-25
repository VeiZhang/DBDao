# SQLite性能优化

## 插入数据

### 比较
下面四种方式，比较增加1000条数据的效率
* 普通语法插入

平均耗时：6050ms

```java
private void normalInsert()
{
	for (int i = 0; i < INSERT_COUNT; i++)
	{
		People people = new People("name" + i, i);
		mDBUtil.insert(people);
	}
}
```

* 预编译插入

平均耗时：5700ms

```java
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
```

* 事务插入

平均耗时：120ms

```java
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
```

* 事务+预编译插入

平均耗时：80ms

```java
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
```

相比较下，事务+预编译的方式，对插入性能提升很大，[GreenDao][GreenDao]第三方数据依赖库就是使用事务+预编译的方式


[GreenDao]:https://github.com/greenrobot/greenDAO