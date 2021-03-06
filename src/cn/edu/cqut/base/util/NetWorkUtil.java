package cn.edu.cqut.base.util;

import java.lang.reflect.Method;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络操作工具
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 */
public class NetWorkUtil
{
	/**
	 * 打开数据连接
	 * 
	 * @param context
	 */
	public static void openNetWork(Context context)
	{
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<ConnectivityManager> clazz = ConnectivityManager.class;
		try
		{
			Method sMethodSetMobileDataEnabled = clazz.getMethod(
					"setMobileDataEnabled", new Class[]
					{ Boolean.TYPE });
			sMethodSetMobileDataEnabled.setAccessible(true);
			sMethodSetMobileDataEnabled.invoke(manager, new Object[]
			{ Boolean.valueOf(true) });

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 描述：判断网络是否有效.
	 * 
	 * @param context
	 *            the context
	 * @return true, if is network available
	 */
	public static boolean isNetworkAvailable(Context context)
	{
		try
		{
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null)
			{
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected())
				{
					if (info.getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		} catch (Exception e)
		{
			return false;
		}
		return false;
	}

	/**
	 * Gps是否打开 需要<uses-permission
	 * android:name="android.permission.ACCESS_FINE_LOCATION" />权限
	 * 
	 * @param context
	 *            the context
	 * @return true, if is gps enabled
	 */
	public static boolean isGpsEnabled(Context context)
	{
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * wifi是否打开.
	 * 
	 * @param context
	 *            the context
	 * @return true, if is wifi enabled
	 */
	public static boolean isWifiEnabled(Context context)
	{
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 判断当前网络是否是wifi网络.
	 * 
	 * @param context
	 *            the context
	 * @return boolean
	 */
	public static boolean isWifi(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI)
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断当前网络是否是3G网络.
	 * 
	 * @param context
	 *            the context
	 * @return boolean
	 */
	public static boolean is3G(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE)
		{
			return true;
		}
		return false;
	}
}
