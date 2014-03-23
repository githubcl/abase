package cn.edu.cqut.base.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author chenliang
 * @version v1.1
 * @date 2014-2-23
 * 
 */
public abstract class BaseActivity extends Activity implements OnClickListener,
		OnItemClickListener
{
	// 按两次退出程序
	private int backCount = 0;
	// 默认为0 0代表不会退出 1代表按两次退出 2代表弹出提示框
	private int backMode = 0;
	protected static final int BACK_MODE_2COUNT = 1;
	protected static final int BACK_MODE_DIALOG = 2;

	/**
	 * 空消息
	 */
	public static final int MESSAGE_EMPTY = 0;
	/**
	 * 提交成功
	 */
	public static final int MESSAGE_SUCCESS = 1;
	/**
	 * 提交失败
	 */
	public static final int MESSAGE_ERROR = 2;

	private ProgressDialog progressDialog = null;
	public BaseThreadCallBack callBack = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 设置title内容
	 */
	public void setTitleMessage(String message)
	{
		// 引用base_title布局文件里面的TextView
		((TextView) findViewById(R.id.title).findViewById(R.id.base_title_tv))
				.setText(message);
	}

	/**
	 * 设置进度对话框消息
	 * 
	 * @param message
	 */
	public void setProgressDialogMessage(String message)
	{
		progressDialog.setMessage(message);
	}

	/**
	 * 显示进度对话框
	 */
	public void showProgressDialog()
	{
		progressDialog.show();
	}

	/**
	 * 关闭进度对话框
	 */
	public void closeProgressDialog()
	{
		if (progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
	}

	/**
	 * 显示短提示的消息
	 * 
	 * @param message
	 */
	public void showShortToast(String message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 显示长提示的消息
	 * 
	 * @param message
	 */
	public void showLongToast(String message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onClick(View v)
	{
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
	}

	protected Handler baseHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (callBack != null)
			{
				Object result = msg.obj;
				switch (msg.what)
				{
				case MESSAGE_EMPTY:
					callBack.handleEmpty();
					break;
				case MESSAGE_SUCCESS:
					if (result != null)
					{
						try
						{
							callBack.handleSuccess(result.toString());
						} catch (Exception e)
						{
							e.printStackTrace();
							showLongToast("解析数据失败!数据：" + result.toString());
						}
					}
					break;
				case MESSAGE_ERROR:
					if (result != null)
					{
						callBack.handleError(result.toString());
					}
					break;
				default:
					handleMessage(msg);
					break;
				}
			}
		}
	};

	/**
	 * 向服务器发送数据时调用的方法
	 * 
	 * @param callBack
	 *            传入数据处理的回调接口
	 */
	public void send(BaseThreadCallBack callBack)
	{
		this.callBack = callBack;
		new BaseThread().start();
	}

	/**
	 * 处理线程回调接口
	 */
	public interface BaseThreadCallBack
	{
		/** 线程开始执行时回调方法 */
		public void handleEmpty();

		/** 发送数据到服务器调用方法,返回服务器回传数据 */
		public String sendData() throws Exception;

		/** 提交数据成功时回调方法,传入服务器返回的数据 */
		public void handleSuccess(String result) throws Exception;

		/** 提交数据失败时回调方法，传入错误的消息 */
		public void handleError(String errorMessage);
	}

	/**
	 * 处理其他返回结果的调用
	 * 
	 * @param msg
	 */
	public void handleResult(Message msg)
	{
	}

	/**
	 * 公共的线程调用
	 */
	class BaseThread extends Thread
	{
		@Override
		public void run()
		{
			baseHandler.sendEmptyMessage(MESSAGE_EMPTY);
			Message msg = baseHandler.obtainMessage();
			try
			{
				// 在这里提交数据
				String result = callBack.sendData();
				msg.what = MESSAGE_SUCCESS;
				msg.obj = result;
			} catch (Exception e)
			{
				msg.what = MESSAGE_ERROR;
				msg.obj = e.getMessage();
			}
			baseHandler.sendMessage(msg);
		}
	}

	/**
	 * 设置退出模式
	 * 
	 * @param mode
	 *            BACK_MODE_2COUNT--按两次退出 BACK_MODE_DIALOG--弹出提示框退出
	 */
	public void setBackMode(int mode)
	{
		backMode = mode;
	}

	// 监听返回键
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK)
		{
			if (backMode == BACK_MODE_2COUNT)
			{
				exitByCount();
			} else if (backMode == BACK_MODE_DIALOG)
			{
				exitByDialog();
			}
			// 默认
			else if (backMode == 0)
			{
				return super.dispatchKeyEvent(event);
			}
			return false;
		}

		return super.dispatchKeyEvent(event);
	};

	// 通过连续按两次退出程序
	public void exitByCount()
	{
		backCount++;
		if (backCount >= 2)
		{
			finish();
			System.exit(0);
		} else
		{
			Toast.makeText(getApplicationContext(), "再按一次回到桌面",
					Toast.LENGTH_SHORT).show();
			new Thread()
			{
				public void run()
				{
					try
					{
						Thread.sleep(2000);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					backCount = 0;
				};
			}.start();
		}
	}

	// 通过弹出返回对话框退出程序
	public void exitByDialog()
	{
		AlertDialog.Builder builder = new Builder(BaseActivity.this);
		builder.setMessage("确定要退出程序");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("提示");
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						finish();
						System.exit(0);
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
}
