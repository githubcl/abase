package cn.edu.cqut.base.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 系统通知栏通知工具类
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 * 
 */
public class NotificationUtil
{
	/**
	 * 通知ID
	 */
	public static final int NOTIFICATION_ID = 0xff;

	/**
	 * 显示通知
	 * 
	 * @param context
	 * @param icon
	 *            通知图标
	 * @param contentTitle
	 *            通知标题
	 * @param activity
	 *            点击通知后跳转到哪个Activity中
	 */
	public static void sendNotification(Context context, int icon,
			CharSequence contentTitle, Class activity)
	{
		sendNotification(context, icon, contentTitle, contentTitle, null,
				activity);
	}

	/**
	 * 显示通知
	 * 
	 * @param context
	 * @param icon
	 *            通知图标
	 * @param tickerText
	 *            通知滚动时显示的文本
	 * @param contentTitle
	 *            通知标题
	 * @param contentText
	 *            通知内容
	 * @param activity
	 *            点击通知后跳转到哪个Activity中
	 */
	public static void sendNotification(Context context, int icon,
			CharSequence tickerText, CharSequence contentTitle,
			CharSequence contentText, Class activity)
	{
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);

		Intent notificationIntent = new Intent(context, activity);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		notification.flags = Notification.FLAG_AUTO_CANCEL;

		// 自定义声音 声音文件放在ram目录下，没有此目录自己创建一个
		// notification.sound=Uri.parse("android.resource://" + getPackageName()
		// + "/" +R.raw.mm);

		// 使用系统默认声音
		notification.defaults = Notification.DEFAULT_SOUND;

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}
}
