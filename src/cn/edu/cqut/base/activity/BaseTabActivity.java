package cn.edu.cqut.base.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import cn.edu.cqut.base.util.LogUtil;

/**
 * 公共的TabActivity,最多支持5个RadioButton显示
 * 
 * @author chenliang
 * @version v1.1
 * @date 2014-2-23
 * 
 */
public abstract class BaseTabActivity extends TabActivity implements
		OnCheckedChangeListener
{
	protected static final String TAB1 = "TAB1";
	protected static final String TAB2 = "TAB2";
	protected static final String TAB3 = "TAB3";
	protected static final String TAB4 = "TAB4";
	protected static final String TAB5 = "TAB5";

	// 按两次退出程序
	private int backCount = 0;
	// 默认为0 0代表不会退出 1代表按两次退出 2代表弹出提示框
	private int backMode = 0;
	protected static final int BACK_MODE_2COUNT = 1;
	protected static final int BACK_MODE_DIALOG = 2;

	protected TabHost tabHost = null;

	protected RadioGroup radioGroup = null;

	protected List<RadioButton> radios = new ArrayList<RadioButton>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_tab);
		initView();
	}

	protected void initView()
	{
		tabHost = getTabHost();
		radioGroup = (RadioGroup) findViewById(R.id.base_tab_radiogroup);
		radioGroup.setOnCheckedChangeListener(this);

		radios.add((RadioButton) findViewById(R.id.base_tab_radio1));
		radios.add((RadioButton) findViewById(R.id.base_tab_radio2));
		radios.add((RadioButton) findViewById(R.id.base_tab_radio3));
		radios.add((RadioButton) findViewById(R.id.base_tab_radio4));
		radios.add((RadioButton) findViewById(R.id.base_tab_radio5));

		init();
	}

	/**
	 * 初始化TabActivity的时候调用，在里面必须调用setTabs
	 */
	public abstract void init();

	/** 设置当前显示的Tab */
	public void setCurrentTabByTag(String tab)
	{
		tabHost.setCurrentTabByTag(tab);
	}

	/**
	 * 设置Tab中的Actvity,图标,文本
	 * 
	 * @param tabs
	 *            每一个面板显示的Activity
	 * @param icons
	 *            图标
	 * @param texts
	 *            文本内容
	 */
	public void setTabs(Class[] tabs, int[] icons, String[] texts)
	{
		if (tabs.length > 5 || icons.length > 5 || texts.length > 5)
		{
			LogUtil.error("初始化BaseTabActivity错误，最多只支持显示五个面板!");
			return;
		}

		for (int i = 0; i < tabs.length; i++)
		{
			TabSpec ts = tabHost.newTabSpec("TAB" + (i + 1))
					.setIndicator("TAB" + (i + 1))
					.setContent(new Intent(BaseTabActivity.this, tabs[i]));
			System.out.println("tssss:" + ts);
			tabHost.addTab(ts);
			// 设置可见
			radios.get(i).setVisibility(View.VISIBLE);
			// 设置图标
			radios.get(i).setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(icons[i]), null, null);
			// 设置文本
			radios.get(i).setText(texts[i]);
		}
		setCurrentTabByTag(TAB1);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		if (checkedId == R.id.base_tab_radio1)
		{
			tabHost.setCurrentTabByTag(TAB1);
		} else if (checkedId == R.id.base_tab_radio2)
		{
			tabHost.setCurrentTabByTag(TAB2);
		} else if (checkedId == R.id.base_tab_radio3)
		{
			tabHost.setCurrentTabByTag(TAB3);
		} else if (checkedId == R.id.base_tab_radio4)
		{
			tabHost.setCurrentTabByTag(TAB4);
		} else if (checkedId == R.id.base_tab_radio5)
		{
			tabHost.setCurrentTabByTag(TAB5);
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
		AlertDialog.Builder builder = new Builder(this);
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
