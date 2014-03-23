package cn.edu.cqut.base.util;

import android.util.Log;

/**
 * 日志工具类
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 * 
 */
public class LogUtil
{
	public static final String TAG = "Base";

	public static void error(String message)
	{
		Log.e(TAG, message);
	}

	public static void debug(String message)
	{
		Log.d(TAG, message);
	}

	public static void info(String message)
	{
		Log.d(TAG, message);
	}
}
