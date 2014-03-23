package cn.edu.cqut.base.myview;

import cn.edu.cqut.base.activity.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * 自定义对话框<br>
 * 使用方法: new BaseDialog(context).show();
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 * 
 */
public class BaseDialog extends Dialog
{

	public BaseDialog(Context context)
	{
		super(context, R.style.base_dialog_style);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// setContentView(layoutResID);
	}
}
