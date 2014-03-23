package cn.edu.cqut.base.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.TelephonyManager;

/**
 * 通话录音
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 */
public class RecordUtil extends BroadcastReceiver
{
	private static RecordUtil recordUtil;
	private String name = null;
	private String phone = null;
	private MediaRecorder recorder;
	private boolean callOver = false;
	private static String path;

	public RecordUtil(String name, String phone)
	{
		this.name = name;
		this.phone = phone;
	}

	/**
	 * 设置录音保存的路径
	 * 
	 * @param path
	 *            录音保存路径 例子：rbzy/audio
	 */
	public static void init(String path)
	{
		RecordUtil.path = path;
	}

	/**
	 * 注册录音，录音结束后需要调用unregister
	 * 
	 * @param context
	 * @param name
	 *            联系人姓名
	 * @param phone
	 *            电话号码
	 */
	public static void register(Context context, String name, String phone)
	{
		IntentFilter intentFilter = new IntentFilter(
				"android.intent.action.PHONE_STATE");
		recordUtil = new RecordUtil(name, phone);
		context.registerReceiver(recordUtil, intentFilter);
	}

	/**
	 * 将注册录音广播取消掉
	 * 
	 * @param context
	 */
	public static void unregister(Context context)
	{
		context.unregisterReceiver(recordUtil);
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{

		if (intent.getAction().equals("android.intent.action.PHONE_STATE"))
		{
			String phoneState = intent
					.getStringExtra(TelephonyManager.EXTRA_STATE);
			// String num = intent.getStringExtra("number");
			// String name = intent.getStringExtra("name");
			// System.out.println("phoneState:" + phoneState + " name:" + name
			// + " number:" + num);

			if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE))
			{
				if (callOver)
				{
					stopRecord();
					callOver = false;
				}

			} else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
			{
				callOver = true;
				startRecord();
			} else if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING))
			{
			}
		}

	}

	/**
	 * 录音开始
	 */
	private void startRecord()
	{
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		File file = new File(Environment.getExternalStorageDirectory(), path);
		if (!file.exists())
		{
			file.mkdirs();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = format.format(new Date());
		File audioFile = new File(file, name + "_" + phone + "_" + date
				+ ".3gp");
		recorder.setOutputFile(audioFile.getAbsolutePath());

		try
		{
			recorder.prepare();
		} catch (IllegalStateException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		recorder.start();
	}

	/**
	 * 录音结束
	 */
	private void stopRecord()
	{
		recorder.stop();
		recorder.release();
		recorder = null;
	}
}
