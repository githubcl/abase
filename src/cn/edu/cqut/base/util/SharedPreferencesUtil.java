package cn.edu.cqut.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences处理工具类
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 */
public class SharedPreferencesUtil
{
	private static SharedPreferences preferences = null;

	/**
	 * 在调用SharedPreferencesUtil里的方法之前必须进行初始化
	 * 
	 * @param context
	 * @param name
	 *            SharedPreferences文件名
	 */
	public static void init(Context context, String name)
	{
		preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public static void putBoolean(String key, boolean value)
	{
		preferences.edit().putBoolean(key, value).commit();
	}

	/**
	 * 默认值false
	 * 
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key)
	{
		return preferences.getBoolean(key, false);
	}

	public static void putInt(String key, int value)
	{
		preferences.edit().putInt(key, value).commit();
	}

	/**
	 * 默认值0
	 * 
	 * @param key
	 * @return
	 */
	public static int getInt(String key)
	{
		return preferences.getInt(key, 0);
	}

	public static void putFloat(String key, Float value)
	{
		preferences.edit().putFloat(key, value).commit();
	}

	/**
	 * 默认值0f
	 * 
	 * @param key
	 * @return
	 */
	public static Float getFloat(String key)
	{
		return preferences.getFloat(key, 0f);
	}

	public static void putLong(String key, Long value)
	{
		preferences.edit().putLong(key, value).commit();
	}

	/**
	 * 默认值0L
	 * 
	 * @param key
	 * @return
	 */
	public static Long getLong(String key)
	{
		return preferences.getLong(key, 0L);
	}

	public static void putString(String key, String value)
	{
		preferences.edit().putString(key, value).commit();
	}

	/**
	 * 默认值null
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key)
	{
		return preferences.getString(key, null);
	}

	/**
	 * 保存一个对象
	 * 
	 * @param object
	 */
	public static void putObject(Object object)
	{
		Class clazz = object.getClass();
		String className = clazz.getSimpleName();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{
			String fieldName = field.getName();
			String methodName = "get" + getFirstUpString(fieldName);
			try
			{
				Method method = clazz.getMethod(methodName, null);
				Object result = method.invoke(object, null);
				if (result instanceof Integer)
				{
					SharedPreferencesUtil.putInt(getName(className, fieldName),
							Integer.parseInt(result.toString()));
				} else if (result instanceof Long)
				{
					SharedPreferencesUtil.putLong(
							getName(className, fieldName),
							Long.parseLong(result.toString()));
				} else if (result instanceof Float)
				{
					SharedPreferencesUtil.putFloat(
							getName(className, fieldName),
							Float.parseFloat(result.toString()));
				} else if (result instanceof Boolean)
				{
					SharedPreferencesUtil.putBoolean(
							getName(className, fieldName),
							Boolean.parseBoolean(result.toString()));
				} else if (result instanceof String)
				{
					SharedPreferencesUtil.putString(
							getName(className, fieldName), result.toString());
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取一个对象
	 * 
	 * @param clazz
	 *            传入对象的class
	 * @return 返回对象，如果没有，返回为null
	 */
	public static Object getObject(Class clazz)
	{
		Object object = null;
		try
		{
			object = clazz.getConstructor(null).newInstance(null);
			String className = clazz.getSimpleName();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields)
			{
				String fieldName = field.getName();
				String methodName = "set" + getFirstUpString(fieldName);

				Method method = null;
				// Object result = method.invoke(object, null);
				String result = field.getType().getSimpleName();
				Object args = null;
				if (result.equals("Integer") || result.equals("int"))
				{
					method = clazz.getMethod(methodName, Integer.class);
					args = SharedPreferencesUtil.getInt(getName(className,
							fieldName));
				} else if (result.equals("Long")|| result.equals("long"))
				{
					method = clazz.getMethod(methodName, Long.class);
					args = SharedPreferencesUtil.getFloat(getName(className,
							fieldName));
				} else if (result.equals("Float") || result.equals("float"))
				{
					method = clazz.getMethod(methodName, Float.class);
					args = SharedPreferencesUtil.getBoolean(getName(className,
							fieldName));
				} else if (result.equals("Boolean")|| result.equals("boolean"))
				{
					method = clazz.getMethod(methodName, Boolean.class);
					args = SharedPreferencesUtil.getBoolean(getName(className,
							fieldName));
				} else if (result.equals("String") )
				{
					method = clazz.getMethod(methodName, String.class);
					args = SharedPreferencesUtil.getString(getName(className,
							fieldName));
				}
				method.invoke(object, args);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return object;
	}

	/** 把第一个字符大写 */
	private static String getFirstUpString(String str)
	{
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	private static String getName(String className, String fieldName)
	{
		return className + "." + fieldName;
	}
}
