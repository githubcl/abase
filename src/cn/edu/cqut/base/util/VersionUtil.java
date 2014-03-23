package cn.edu.cqut.base.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * 检测版本更新工具类<br>
 * 权限:<br>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission
 * android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 * 
 */
public class VersionUtil
{
	private static final int UP_INFO = 0;
	private static final int DOWN_APK = 1;
	private static final int INSTALL_APK = 2;
	private static final int DOWN_ERROR = 3;

	private static Activity mContext = null;
	private static int apkSize = 0;
	private static String apkDesc;
	private static String apkUrl;
	private static ProgressDialog progressDialog;
	private static String apkFilePath;

	public static void checkVersion(Activity context, String url)
	{
		mContext = context;
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage("正在更新apk...,请稍后");
		getServerVersioninfo(url);
	}

	private static Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case UP_INFO:
				showUpdataInfo();
				break;
			case DOWN_APK:
				if (!progressDialog.isShowing())
				{
					progressDialog.show();
				}
				break;
			case INSTALL_APK:
				if (progressDialog.isShowing())
				{
					progressDialog.dismiss();
				}
				installApk();
				break;
			case DOWN_ERROR:
				if (progressDialog.isShowing())
				{
					progressDialog.dismiss();
				}
				Toast.makeText(mContext, "从服务器下载apk出现错误", Toast.LENGTH_LONG)
						.show();
				break;
			default:
				break;
			}
		}
	};

	// 获取服务器版本信息
	private static void getServerVersioninfo(final String url)
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					URL u = new URL(url);
					HttpURLConnection connection = (HttpURLConnection) u
							.openConnection();
					// 设置超时为5秒
					connection.setConnectTimeout(5000);
					Properties properties = new Properties();
					properties.load(connection.getInputStream());
					;
					int version = Integer.parseInt(new String(properties
							.getProperty("apkVersion").getBytes("ISO-8859-1"),
							"UTF-8"));
					apkUrl = new String(properties.getProperty("apkUrl")
							.getBytes("ISO-8859-1"), "UTF-8");
					apkDesc = new String(properties.getProperty("apkDesc")
							.getBytes("ISO-8859-1"), "UTF-8");
					apkSize = Integer.parseInt(new String(properties
							.getProperty("apkSize").getBytes("ISO-8859-1"),
							"UTF-8"));
					LogUtil.debug("apk版本号: " + version);
					LogUtil.debug("apk地址: " + apkUrl);
					LogUtil.debug("apk描述: " + apkDesc);
					LogUtil.debug("apk大小: " + apkSize);
					// 判断服务器apk版本号是否大于当前版本号
					int versionCode = mContext.getPackageManager()
							.getPackageInfo(mContext.getPackageName(), 0).versionCode;
					// 需要升级
					if (version > versionCode)
					{
						// 提示用户需要升级
						handler.sendEmptyMessage(UP_INFO);
					}

				} catch (Exception e)
				{
					LogUtil.error("从服务器获取版本信息文件失败!");
					e.printStackTrace();
				}
			};
		}.start();
	}

	private static void showUpdataInfo()
	{
		AlertDialog.Builder builer = new Builder(mContext);
		builer.setTitle("版本升级");
		builer.setIcon(android.R.drawable.ic_dialog_info);
		builer.setMessage(apkDesc);
		builer.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						downApk();
						dialog.dismiss();
					}
				});
		builer.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	public static void downApk()
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					// 发送开始下载apk通知
					handler.sendEmptyMessage(DOWN_APK);
					URL url = new URL(apkUrl);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(5000);
					InputStream is = conn.getInputStream();
					apkFilePath = Environment.getExternalStorageDirectory()
							+ "/" + mContext.getPackageName() + ".apk";
					File file = new File(apkFilePath);
					FileOutputStream fos = new FileOutputStream(file);
					BufferedInputStream bis = new BufferedInputStream(is);
					byte[] buffer = new byte[2048];
					int len;
					int total = 0;
					progressDialog.setMax(apkSize);
					while ((len = bis.read(buffer)) != -1)
					{
						fos.write(buffer, 0, len);
						total += len;
						// 获取当前下载量
						if (total <= apkSize)
						{
							progressDialog.setProgress(total);
						}
					}
					fos.close();
					bis.close();
					is.close();
					// 发送下载完成apk通知
					handler.sendEmptyMessage(INSTALL_APK);
				} catch (Exception e)
				{
					// 发送下载apk错误通知
					handler.sendEmptyMessage(DOWN_ERROR);
					LogUtil.error("下载apk出现错误!");
					e.printStackTrace();
				}
			};
		}.start();

	}

	// 安装apk
	private static void installApk()
	{
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(new File(apkFilePath)),
				"application/vnd.android.package-archive");
		mContext.startActivityForResult(intent, 12);
	}
}
