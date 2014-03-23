package cn.edu.cqut.base.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * 后台服务，被杀了会自动重启<br>
 * 
 * 配置文件中添加：<br>
 * <service android:name="cn.edu.cqut.base.service.BaseService"
 * android:enabled="true" android:exported="false" />
 * 
 * @author chenliang
 * @version v1.1
 * @date 2014-2-23
 * 
 */
public abstract class BaseService extends Service
{
	private Handler handler = new Handler();

	// 第一次创建的时候调用
	@Override
	public void onCreate()
	{
		setForeground(true);
		handler.post(runnable);
	}

	/**
	 * runnable用来循环做一个事
	 */
	private Runnable runnable = new Runnable()
	{
		@Override
		public void run()
		{
			// 在这里干自己想干的事
			handler.postDelayed(runnable, 1000 * doWork());
		}
	};

	/**
	 * 子类必须实现这个方法,在里面做自己后台想做的事
	 * 
	 * @return 每隔多少秒执行一次doWork
	 */
	public abstract int doWork();

	// 每次startService都会调用
	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onDestroy()
	{
		// 被销毁的时候再次开启服务
		Intent intent = new Intent();
		intent.setClass(this, BaseService.class);
		this.startService(intent);
	}
}
