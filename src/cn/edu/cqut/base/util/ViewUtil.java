package cn.edu.cqut.base.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.widget.TextView;

/**
 * View处理工具类
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 * 
 */
public class ViewUtil
{
	/**
	 * 设置TextView文本高亮背景显示
	 * 
	 * @param view
	 *            需要设置的TextView
	 * @param color
	 *            背景颜色
	 * @param start
	 *            开始字符位置
	 * @param end
	 *            结束字符位置
	 */
	public static void setTextLight(TextView view, int color, int start, int end)
	{
		SpannableStringBuilder style = new SpannableStringBuilder(view
				.getText().toString());
		style.setSpan(new BackgroundColorSpan(color), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(style);
	}
	
	
}
