package cn.edu.cqut.base.util;

import android.app.Service;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class MediaUtil
{
	/**
	 * 播放raw文件里面的音乐
	 * 
	 * @param context
	 * @param resid 资源id
	 */
	public static void playRaw(Context context, int resid)
	{
		MediaPlayer mediaPlayer = MediaPlayer.create(context, resid);
		mediaPlayer.setLooping(false);
		mediaPlayer.start();
	}

	/**
	 * 振动milliseconds毫秒<br>
	 * 权限:<br>
	 * <uses-permission android:name="android.permission.VIBRATE" /> 
	 * @param context
	 * @param milliseconds 振动毫秒数
	 */
	public static void vibrate(Context context, long milliseconds)
	{
		Vibrator vib = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
}
