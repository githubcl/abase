package cn.edu.cqut.base.adapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.cqut.base.activity.R;
import cn.edu.cqut.base.util.LogUtil;

/**
 * 调用这个Adapter需要在每个item对应值的domain的获取值方法上打上ItemAnnoation
 * <tr>
 * 1、@ItemAnnoation(types={ItemType.Image})<br>
 * 2、@ItemAnnoation(types={ItemType.Item1})<br>
 * 3、@ItemAnnoation(types={ItemType.Item2})<br>
 * 4、@ItemAnnoation(types={ItemType.Item3})<br>
 * 5、@ItemAnnoation(types={ItemType.Item4})<br>
 * <br>
 * 指定在具体的Item的名字:<br>
 * adapter.setAdapterName(adapterName);<br>
 * 取了名字后注解的用法:<br>
 * 1、@ItemAnnoation(types={ItemType.Image}, adapters = {"123"})<br>
 * 回调接口：<br>
 * new ItemHandleCallBack()<br>
 * {<br>
 * public void handle(Object viewHolder, Object item)<br>
 * {<br>
 * ItemAdapter.ViewHolder holder = (ItemAdapter.ViewHolder) viewHolder;<br>
 * Item obj = (Item) item;<br>
 * //做自己的处理<br>
 * }<br>
 * }<br>
 * 
 * @author chenliang
 * @version v1.1
 * @date 2014-2-23
 * @param <T>
 *            操作的domain
 */
public class ItemAdapter<T> extends BaseAdapter
{
	private ItemHandleCallBack handle = null;
	private List<T> data = new ArrayList<T>();

	private LayoutInflater inflater;
	private Integer itemResource;
	private String adapterName = "";

	/**
	 * 初始化一个ItemAdapter，传入数据和自定义回调接口,item布局
	 * 
	 * @param context
	 * @param data
	 *            数据
	 * @param handle
	 *            Item处理回调接口
	 * @param itemResource
	 *            item布局
	 */
	public ItemAdapter(Context context, List<T> data,
			ItemHandleCallBack handle, Integer itemResource)
	{
		this(context, data, handle);
		this.itemResource = itemResource;
	}

	/**
	 * 初始化一个ItemAdapter，传入数据和自定义回调接口,item布局
	 * 
	 * @param context
	 * @param data
	 *            数据
	 * @param itemResource
	 *            item布局
	 */
	public ItemAdapter(Context context, List<T> data, Integer itemResource)
	{
		this(context, data);
		this.itemResource = itemResource;
	}

	/**
	 * 初始化一个ItemAdapter，传入数据和自定义回调接口
	 * 
	 * @param context
	 * @param data
	 *            数据
	 * @param handle
	 *            Item处理回调接口
	 */
	public ItemAdapter(Context context, List<T> data, ItemHandleCallBack handle)
	{
		this(context, data);
		this.handle = handle;
	}

	/**
	 * 初始化一个ItemAdapter，传入数据
	 * 
	 * @param context
	 * @param data
	 *            数据
	 */
	public ItemAdapter(Context context, List<T> data)
	{
		inflater = LayoutInflater.from(context);
		this.data = data;
	}

	/**
	 * 给ItemAdapter取一个名字
	 * 
	 * @param adapterName
	 *            adapter的名字
	 */
	public void setAdapterName(String adapterName)
	{
		this.adapterName = adapterName;
	}

	/**
	 * 数据发生改变时候调用
	 * 
	 * @param data
	 *            改变后的数据
	 */
	public void refresh()
	{
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return data.size();
	}

	@Override
	public Object getItem(int position)
	{
		return data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			// 没有设置Item布局时采用默认item布局
			if (itemResource == null)
				convertView = inflater.inflate(R.layout.base_list_item, null);
			// 设置了Item布局则采用设置的Item布局
			else
				convertView = inflater.inflate(itemResource, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.item1 = (TextView) convertView.findViewById(R.id.item1);
			holder.item2 = (TextView) convertView.findViewById(R.id.item2);
			holder.item3 = (TextView) convertView.findViewById(R.id.item3);
			holder.item4 = (TextView) convertView.findViewById(R.id.item4);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		T item = data.get(position);
		if (handle != null)
		{
			handle.handle(holder, item);
		} else
		{
			try
			{
				Class clazz = item.getClass();
				Method[] methods = clazz.getMethods();
				for (Method method : methods)
				{
					ItemAnnotation itemAnnotation = method
							.getAnnotation(ItemAnnotation.class);
					if (itemAnnotation != null)
					{
						String[] adapters = itemAnnotation.adapters();
						ItemType[] types = itemAnnotation.types();
						ItemType type = null;
						if (adapters.length != 0)
						{
							boolean flag = false;
							for (int i = 0; i < adapters.length; i++)
							{
								if (adapters[i].equals(adapterName))
								{
									flag = true;
									type = types[i];
									break;
								}
							}
							if (!flag)
							{
								continue;
							}
						} else
						{
							type = types[0];
						}

						Object value = method.invoke(item, null);
						setValue(holder, type, value);
					}
				}
			} catch (Exception e)
			{
				LogUtil.error("ItemAdapter getView 出现错误！");
				e.printStackTrace();
			}
		}
		return convertView;
	}

	private void setValue(ViewHolder holder, ItemType type, Object value)
	{
		if (type == ItemType.Image)
		{
			holder.image.setVisibility(View.VISIBLE);
			// String代表文件路径
			if (value.getClass().getSimpleName().equals("String"))
			{

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 2;
				holder.image.setImageBitmap(BitmapFactory.decodeFile(
						value.toString(), opts));
			}
			// int 代表资源id
			else
			{
				holder.image
						.setImageResource(Integer.parseInt(value.toString()));
			}
		} else if (type == ItemType.Item1)
		{
			holder.item1.setText("" + value);
		} else if (type == ItemType.Item2)
		{
			holder.item2.setText("" + value);
		} else if (type == ItemType.Item3)
		{
			holder.item3.setText("" + value);
		} else if (type == ItemType.Item4)
		{
			holder.item4.setText("" + value);
		}
	}

	public interface ItemHandleCallBack
	{
		public void handle(Object viewHolder, Object item);
	}

	public class ViewHolder
	{
		public ImageView image;
		public TextView item1;
		public TextView item2;
		public TextView item3;
		public TextView item4;
	}
}
