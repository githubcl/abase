package cn.edu.cqut.base.util;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;

/**
 * 服务操作工具类型
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 * 
 */
public class ServiceUtil
{
	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context ctx, String className)
	{
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> servicesList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		Iterator<RunningServiceInfo> l = servicesList.iterator();
		while (l.hasNext())
		{
			RunningServiceInfo si = (RunningServiceInfo) l.next();
			if (className.equals(si.service.getClassName()))
			{
				isRunning = true;
			}
		}
		return isRunning;
	}

	/**
	 * 停止服务.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param className
	 *            the class name
	 * @return true, if successful
	 */
	public static boolean stopRunningService(Context ctx, String className)
	{
		Intent intent_service = null;
		boolean ret = false;
		try
		{
			intent_service = new Intent(ctx, Class.forName(className));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (intent_service != null)
		{
			ret = ctx.stopService(intent_service);
		}
		return ret;
	}
}
