package cn.edu.cqut.base.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * <p>如果文本长度大于控件长度，则采用跑马灯方式显示文本,在4.0.3中测试发现没有滚动</p>
 * <p>
 * 		需要在使用控件中添加属性<br>
 * 		android:ellipsize="marquee"<br>
        android:marqueeRepeatLimit="marquee_forever"
 * </p>
 * <p>
 * 		如果添加上面属性不能滚动，需要在根布局中添加属性:<br>android:descendantFocusability="blocksDescendants"
 * </p>
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 *
 */
public class AlwaysMarqueeTextView extends TextView
{
	public AlwaysMarqueeTextView(Context context) {
		super(context);
		}
		 
		public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		}
		 
		public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		}
		 
		@Override
		public boolean isFocused() {
		return true;
		}
}
