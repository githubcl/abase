package cn.edu.cqut.base.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.edu.cqut.base.util.LogUtil;

/**
 * 数据库操作帮助类，在使用DBUtil之前需调用init方法，关闭程序时调用closeDatabase
 * 
 * @author chenliang
 * @version v1.1
 * @date 2014-2-23
 */
public class DBHelper extends SQLiteOpenHelper {
	private static SQLiteDatabase sqLiteDatabase = null;
	private static String DB_NAME;
	private static int DB_VERSION;
	private static DBHelperCallBack callBack = null;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * 实例化数据库，在使用数据库之前必须调用这个方法
	 * 
	 * @param context
	 * @param dbName
	 *            数据库名
	 * @param version
	 *            数据库版本
	 * @param loginUser
	 *            当前登录用户的用户名
	 * @param callBack
	 *            数据库初始化和升级时的回调接口
	 */
	public static void init(Context context, String dbName, int version,
			String loginUser, DBHelperCallBack callBack) {
		DB_NAME = dbName;
		DB_VERSION = version;
		DBUtil.loginUser = loginUser;
		DBHelper.callBack = callBack;
		sqLiteDatabase = new DBHelper(context).getWritableDatabase();
	}

	/**
	 * 获取一个SQLiteDatabase
	 * 
	 * @return
	 */
	public static SQLiteDatabase getSQLiteDatabase() {
		if (sqLiteDatabase == null) {
			LogUtil.error("数据库还没有初始化!");
		}
		return sqLiteDatabase;
	}

	/**
	 * 在应用关闭的时候应该调用这个方法关闭数据库
	 */
	public static void closeDatabase() {
		if (sqLiteDatabase != null)
			sqLiteDatabase.close();
	}

	// 第一次创建数据库是调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtil.debug("数据库 onCreate");
		if (callBack != null) {
			callBack.onCreate(db);
		}
	}

	// 数据库升级的时候调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (callBack != null) {
			callBack.onUpgrade(db, oldVersion, newVersion);
		}
	}

	// 数据库初始化升级回调接口
	public interface DBHelperCallBack {
		/** 数据库第一次创建时调用 */
		public void onCreate(SQLiteDatabase db);

		/** 数据库升级时调用 */
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}
}
