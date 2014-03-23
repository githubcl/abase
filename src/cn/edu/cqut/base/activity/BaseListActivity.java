package cn.edu.cqut.base.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import cn.edu.cqut.base.adapter.ItemAdapter;

/**
 * 公共的需要用到ListView的界面实现这个Activity
 * 
 * @author chenliang
 * @version v1.1
 * @date 2014-2-23
 * 
 */
public abstract class BaseListActivity extends BaseActivity
{
	ListView listView = null;
	private ItemAdapter adapter;
	private int layout = R.layout.base_list;

	public void setLayout(int layout)
	{
		this.layout = layout;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(layout);
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		init();
	}

	/**
	 * 子类需要重载这个方法，在这个方法需要调用setAdapter
	 */
	public abstract void init();

	/**
	 * 设置ListView的Adapter,只使用listView之前必须调用这个方法
	 * 
	 * @param adapter
	 */
	public void setAdapter(ItemAdapter adapter)
	{
		this.adapter = adapter;
		listView.setAdapter(adapter);
	}

	public void refresh()
	{
		adapter.refresh();
	}

	/**
	 * 显示listView的头
	 */
	public void showListHeader()
	{
		findViewById(R.id.list_header).setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏listView的头
	 */
	public void hideListHeader()
	{
		findViewById(R.id.list_header).setVisibility(View.GONE);
	}
}
