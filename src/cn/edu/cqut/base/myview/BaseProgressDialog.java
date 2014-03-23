package cn.edu.cqut.base.myview;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.cqut.base.activity.R;

/**
 * 自定义进度对话框
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 * 
 */
public class BaseProgressDialog extends Dialog
{

	private ImageView loadingImg = null;
	private TextView loadingText = null;
	private Animation animation = null;

	public BaseProgressDialog(Context context)
	{
		super(context, R.style.base_progress_dialog_style);
		setContentView(R.layout.base_progress_dialog);
		getWindow().getAttributes().gravity = Gravity.CENTER;
		loadingImg = (ImageView) findViewById(R.id.loadingImg);
		animation = AnimationUtils.loadAnimation(context,
				R.anim.base_progress_dialog);
		loadingText = (TextView) findViewById(R.id.loadingMsg);
	}

	public void startAnim()
	{
		loadingImg.startAnimation(animation);
	}

	public void setMessage(String msg)
	{
		if (loadingText != null)
		{
			loadingText.setText(msg);
		}
	}
}
