package cn.edu.cqut.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 监听网络变化广播接收器<br>
 * 1、配置文件中配置：<br>
 * < receiver android:name="cn.edu.cqut.base.receiver.NetWorkReceiver">   
      < intent-filter>   
          < action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>   
      < /intent-filter>   
  < /receiver>
 *
 *2、代码中配置：<br>
 * 1)注册广播<br>
 * NetWorkReceiver receiver = new NetWorkReceiver();
 * IntentFilter mFilter = new IntentFilter();
   mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
   registerReceiver(receiver, mFilter);<br>
   2)取消注册<br>
   unregisterReceiver(receiver);
  * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 *
 */
public class NetWorkReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE"))
		{
			//在这里判断网络是否打开等操作
		}
	}
}
