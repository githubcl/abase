package cn.edu.cqut.base.activity;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * 带左右滑动切换视图功能的TabActivity
 * 
 * @author chenliang
 * @version v1.1
 * @date 2014-2-23
 * 
 */
public abstract class BaseSlideTabActivity extends BaseTabActivity implements
		OnGestureListener
{

	private GestureDetector gestureDetector;
	private static final int FLEEP_DISTANCE = 120;

	@Override
	protected void initView()
	{
		super.initView();
		gestureDetector = new GestureDetector(this);
		new View.OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				if (gestureDetector.onTouchEvent(event))
				{
					return true;
				}
				return false;
			}
		};
	}

	private void setCurrentTabWithAnim(int currentTab, int next, String tag)
	{
		// 这个方法是关键，用来判断动画滑动的方向
		if (currentTab > next)
		{
			tabHost.getCurrentView().startAnimation(
					AnimationUtils.loadAnimation(this, R.anim.push_right_out));
			setRadioChecked(next);
			tabHost.setCurrentTabByTag(tag);
			tabHost.getCurrentView().startAnimation(
					AnimationUtils.loadAnimation(this, R.anim.push_right_in));
		} else
		{
			tabHost.getCurrentView().startAnimation(
					AnimationUtils.loadAnimation(this, R.anim.push_left_out));
			setRadioChecked(next);
			tabHost.setCurrentTabByTag(tag);
			tabHost.getCurrentView().startAnimation(
					AnimationUtils.loadAnimation(this, R.anim.push_left_in));
		}
	}

	private void setRadioChecked(int index)
	{
		radios.get(index).setChecked(true);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		if (gestureDetector.onTouchEvent(event))
		{
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		int currentTab = tabHost.getCurrentTab();
		int next = currentTab;
		if (e1.getX() - e2.getX() <= (-FLEEP_DISTANCE))
		{// 从左向右滑动
			next = currentTab - 1;
			if (next >= 0 && next <= 3)
			{
				setCurrentTabWithAnim(currentTab, next, null);
			}
		} else if (e1.getX() - e2.getX() >= FLEEP_DISTANCE)
		{// 从右向左滑动
			next = currentTab + 1;
			if (next >= 0 && next <= 3)
			{
				setCurrentTabWithAnim(currentTab, next, null);
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		return false;
	}
}
