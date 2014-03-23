package cn.edu.cqut.base.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

/**
 * app处理工具类
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 */
public class AppUtil
{
	/**
	 * 描述：安装APK
	 * 
	 * @param context
	 *            the context
	 * @param file
	 *            apk文件路径
	 */
	public static void installApk(Context context, File file)
	{
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 描述：卸载程序.
	 * 
	 * @param context
	 *            the context
	 * @param packageName
	 *            要卸载程序的包名
	 */
	public static void uninstallApk(Context context, String packageName)
	{
		Intent intent = new Intent(Intent.ACTION_DELETE);
		Uri packageURI = Uri.parse("package:" + packageName);
		intent.setData(packageURI);
		context.startActivity(intent);
	}

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context)
	{
		try
		{
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e)
		{
			LogUtil.error("获取版本号失败");
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取版本名
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context)
	{
		try
		{
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e)
		{
			LogUtil.error("获取版本名失败");
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取设备的IMEI/设备号
	 * @param context
	 * @return IMEI/设备号
	 */
	public static String getIMEI(Activity context)
	{
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 保存屏幕常亮
	 * 
	 * @param context
	 */
	public static void keepLight(Activity activity)
	{
		// 保持屏幕常亮
		activity.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
}
