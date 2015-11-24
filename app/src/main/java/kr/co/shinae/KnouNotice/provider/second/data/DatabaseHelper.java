package kr.co.shinae.KnouNotice.provider.second.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import kr.co.shinae.KnouNotice.second.KnouNoticeFileInfo;
import kr.co.shinae.KnouNotice.second.KnouNoticeInfo;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "konuNotice.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the SimpleData table
	private Dao<KnouNoticeInfo, Long> knouNoticeInfoDao = null;
	private Dao<KnouNoticeFileInfo, Long> KnouNoticeFileInfoDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		System.out.println("DatabaseHelper:DatabaseHelper");
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		System.out.println("DatabaseHelper:onCreate");

		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			int result = TableUtils.createTable(connectionSource, KnouNoticeInfo.class);
			int result2 = TableUtils.createTable(connectionSource, KnouNoticeFileInfo.class);

			// here we try inserting data in the on-create as a test
			Dao<KnouNoticeInfo, Long> dao = getKnouNoticeInfoDao();
			long millis = System.currentTimeMillis();
			Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate: " + millis);
			Log.i(DatabaseHelper.class.getName(), "created new entries in result: " + result);

			System.out.println("created new entries in onCreate: " + millis);

			/*result = 2 실패 , result=1 성공*/
			System.out.println("created new entries in result: " + result);
			System.out.println("created new entries in result2: " + result2);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			System.out.println( "Can't create database"+ e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		System.out.println("DatabaseHelper:onUpgrade");
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			int result1 = TableUtils.dropTable(connectionSource, KnouNoticeInfo.class, true);
			int result2 = TableUtils.createTable(connectionSource, KnouNoticeFileInfo.class);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public Dao<KnouNoticeInfo, Long> getKnouNoticeInfoDao() throws SQLException {
		System.out.println("DatabaseHelper:getKnouNoticeInfoDao");
		if (knouNoticeInfoDao == null) {
			knouNoticeInfoDao = getDao(KnouNoticeInfo.class);
		}
		return knouNoticeInfoDao;
	}

	public Dao<KnouNoticeFileInfo, Long> getKnouNoticeFileInfoDao() throws SQLException {
		System.out.println("DatabaseHelper:getKnouNoticeFileInfoDao");
		if (KnouNoticeFileInfoDao == null) {
			KnouNoticeFileInfoDao = getDao(KnouNoticeFileInfo.class);
		}
		return KnouNoticeFileInfoDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		knouNoticeInfoDao = null;

		System.out.println("DatabaseHelper:close");
	}
}
