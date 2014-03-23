package cn.edu.cqut.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 监听开机广播接收者，开机后一般的处理就是开启自己的后台服务<br/>
 * 配置文件中配置:<br/>
 * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 * <receiver
 *           android:name="cn.edu.cqut.base.receiver.BootBroadcastReceiver"
 *           android:permission="android.permission.RECEIVE_BOOT_COMPLETED" >
 *           <intent-filter>
 *               <action android:name="android.intent.action.BOOT_COMPLETED" />
 *           </intent-filter>
 *  </receiver>
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 */
public class BootBroadcastReceiver extends BroadcastReceiver {  
    @Override  
    public void onReceive(Context context, Intent intent) { 
    	
    	//开启服务
        //Intent service = new Intent(context,MyService.class);  
        //context.startService(service);  
    }  
  
}  
