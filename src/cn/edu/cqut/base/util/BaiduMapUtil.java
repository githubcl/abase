package cn.edu.cqut.base.util;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 百度地图定位工具类 <!-- 定位权限 --> <uses-permission
 * android:name="android.permission.ACCESS_COARSE_LOCATION" /> <uses-permission
 * android:name="android.permission.ACCESS_FINE_LOCATION" /> <uses-permission
 * android:name="android.permission.CHANGE_WIFI_STATE" /> <uses-permission
 * android:name="android.permission.READ_LOGS" /> 定位服务 <service
 * android:name="com.baidu.location.f" android:enabled="true"
 * android:process=":remote" />
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 */
public class BaiduMapUtil
{
	// 定位
	private static BDLocationListener myListener = null;
	private static LocationClient mLocationClient = null;
	private static CallBack mCallBack = null;
	private static String longitude = "";
	private static String latitude = "";
	private static String address = "";

	/**
	 * 开启百度地图定位功能,只定位一次
	 * 
	 * @param context
	 * @param callBack
	 *            每次定位后的回掉接口
	 */
	public static void start(Context context, CallBack callBack)
	{
		start(context, null, callBack);
	}

	/**
	 * 开启百度地图定位功能
	 * 
	 * @param context
	 * @param intervalTime
	 *            每隔多少秒定位一次
	 * @param callBack
	 *            每次定位后的回掉接口
	 */
	public static void start(Context context, Integer intervalTime,
			CallBack callBack)
	{
		mCallBack = callBack;
		mLocationClient = new LocationClient(context); // 声明LocationClient类
		myListener = new BDLocationListener()
		{

			@Override
			public void onReceiveLocation(BDLocation location)
			{
				if (location == null)
				{
					LogUtil.error("获取百度地图位置失败!返回location为空");
					return;
				}

				// 如果是模拟器 返回数据是4.9E-234
				if ("4.9E-324".equals(String.valueOf(location.getLongitude())))
				{
					if ("".equals(longitude) || "".equals(latitude))
					{
//						longitude = "29.5434";
//						latitude = "106.5984";
//						address = "重庆理工大学花溪校区";
						longitude = "106.53549";
						latitude = "29.460931";
						address = "重庆理工大学花溪校区";
						
						location.setLongitude(106.53549);
						location.setLatitude(29.460931);
						location.setAddrStr(address);
					}
				} else
				{
					longitude = String.valueOf(location.getLongitude());
					latitude = String.valueOf(location.getLatitude());
					if (location.getLocType() == BDLocation.TypeNetWorkLocation)
					{
						address = location.getAddrStr();
					}
				}

				if (mCallBack != null)
				{
					mCallBack.callBack(location);
				}
			}

			@Override
			public void onReceivePoi(BDLocation location)
			{
			}
		};
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		setLocationOptions(intervalTime);
	}

	/**
	 * 获取经度
	 * 
	 * @return 返回经度
	 */
	public static String getLongitude()
	{
		return longitude;
	}

	/**
	 * 获取纬度
	 * 
	 * @return 返回纬度
	 */
	public static String getLatitude()
	{
		return latitude;
	}

	/**
	 * 获取地址
	 * 
	 * @return 返回地址
	 */
	public static String getAddress()
	{
		return address;
	}

	// 设置参数
	private static void setLocationOptions(Integer intervalTime)
	{
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		if (intervalTime != null)
		{
			option.setScanSpan(1000 * intervalTime);// 设置发起定位请求的间隔时间
		}
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
		{
			mLocationClient.requestLocation();
		} 
	}

	/**
	 * 每次定位后的回调接口
	 * 
	 */
	public interface CallBack
	{
		void callBack(BDLocation location);
	}

}
