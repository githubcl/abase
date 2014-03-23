package cn.edu.cqut.base.db;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import cn.edu.cqut.base.util.LogUtil;

/**
 * 数据库操作工具类，在使用之前必须调用DBHelper里的init方法
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 */
public class DBUtil
{
	public static final String LOGIN_USER = "loginUser";
	public static String loginUser = "";

	/**
	 * 保存一个对象
	 * 
	 * @param obj
	 *            被保存的对象
	 * @return 保存对象的id
	 */
	public static Long save(Object obj)
	{
		ContentValues values = Object2ContentValues(obj);
		// 数据库的名字要和类的名字一样,使用小写方式
		Long id = DBHelper.getSQLiteDatabase().insert(getTableName(obj), null,
				values);
		return id;
	}

	/**
	 * 删除一个对象
	 * 
	 * @param obj
	 *            被删除的对象
	 */
	public static void delete(Object obj)
	{
		try
		{
			Method method = obj.getClass().getMethod("getId", null);
			Object id = method.invoke(obj, null);
			DBHelper.getSQLiteDatabase().delete(getTableName(obj), "_id=?",
					new String[]
					{ id.toString() });
		} catch (Exception e)
		{
			LogUtil.error("数据库删除数据出现错误!");
			e.printStackTrace();
		}
	}

	/**
	 * 更新一个对象
	 * 
	 * @param obj
	 *            被更新的对象
	 */
	public static void update(Object obj)
	{
		ContentValues values = Object2ContentValues(obj);

		DBHelper.getSQLiteDatabase().update(getTableName(obj), values, "_id=?",
				new String[]
				{ values.get("_id").toString() });
	}

	/**
	 * 通过id和类型获取一个对象
	 * 
	 * @param id
	 *            获取对象的id
	 * @param clazz
	 *            获取对象的类型
	 * @return
	 */
	public static Object get(Long id, Class clazz)
	{
		String table = clazz.getSimpleName().toLowerCase();
		Cursor cursor = DBHelper.getSQLiteDatabase().query(table, null,
				"_id=?", new String[]
				{ id.toString() }, null, null, null);
		Object result = null;
		if (cursor.moveToNext())
		{
			result = getObject(cursor, clazz);
		}
		cursor.close();
		return result;
	}

	/**
	 * 获取一个指定类型的列表
	 * 
	 * @param clazz
	 *            获取列表类型
	 * @return
	 */
	public static List list(Class clazz)
	{
		List data = new ArrayList();
		String table = clazz.getSimpleName().toLowerCase();
		Cursor cursor = DBHelper.getSQLiteDatabase().query(table, null,
				LOGIN_USER + "=?", new String[]{loginUser}, null, null, null);
		while (cursor.moveToNext())
		{
			Object object = getObject(cursor, clazz);
			data.add(object);
		}
		cursor.close();
		return data;
	}

	private static String getFiledName(String methodName)
	{
		String field = methodName.substring(3);
		String first = field.substring(0, 1);
		field = first.toLowerCase() + field.substring(1);
		return field;
	}

	private static String getTableName(Object obj)
	{
		return obj.getClass().getSimpleName().toLowerCase();
	}

	/**
	 * 对象装换成ContentValues
	 */
	private static ContentValues Object2ContentValues(Object obj)
	{
		ContentValues values = new ContentValues();
		try
		{
			Method[] methods = obj.getClass().getMethods();
			for (Method method : methods)
			{
				String methodName = method.getName();
				if (methodName.startsWith("get")
						&& !methodName.equals("getClass"))
				{
					Object result = method.invoke(obj, null);
					String field = getFiledName(methodName);
					if (methodName.equals("getId"))
					{
						field = "_id";
					}
					if (result != null)
					{
						values.put(field, result.toString());
					}
				}
			}

		} catch (Exception e)
		{
			LogUtil.error("数据库Object2ContentValues错误!");
			e.printStackTrace();
		}
		values.put(LOGIN_USER, loginUser);
		return values;
	}

	private static Object getObject(Cursor cursor, Class clazz)
	{
		Object result = null;
		try
		{
			result = clazz.newInstance();
			Method[] methods = result.getClass().getMethods();
			for (Method method : methods)
			{
				String methodName = method.getName();
				if (methodName.startsWith("set"))
				{
					String field = getFiledName(methodName);
					String fieldType = clazz.getDeclaredField(field).getType()
							.getSimpleName();
					LogUtil.debug("field:" + field + " fieldType:" + fieldType);
					if (field.equals("id"))
					{
						field = "_id";
					}
					Object fieldValue = null;
					int columnIndex = cursor.getColumnIndex(field);
					if ("String".equals(fieldType))
					{
						fieldValue = cursor.getString(columnIndex);

					} else if ("Long".equals(fieldType))
					{
						fieldValue = cursor.getLong(columnIndex);
					} else if ("Double".equals(fieldType))
					{
						fieldValue = cursor.getDouble(columnIndex);
					} else if ("Float".equals(fieldType))
					{
						fieldValue = cursor.getFloat(columnIndex);
					} else if ("Integer".equals(fieldType))
					{
						fieldValue = cursor.getInt(columnIndex);
					} else if ("Short".equals(fieldType))
					{
						fieldValue = cursor.getShort(columnIndex);
					}
					method.invoke(result, fieldValue);
				}
			}
		} catch (Exception e)
		{
			LogUtil.error("数据库GET数据错误!");
			e.printStackTrace();
		}
		return result;
	}
}
