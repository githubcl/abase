package cn.edu.cqut.base.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import cn.edu.cqut.base.domain.Contract;

/**
 * 联系人处理工具类
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 * 
 */
public class ContractUtil
{
	private static final String[] PHONES_PROJECTION = new String[]
	{ Phone.DISPLAY_NAME, Phone.NUMBER };
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	private static final int PHONES_NUMBER_INDEX = 1;

	/**
	 * 调用系统打电话程序
	 * 
	 * @param context
	 * @param phone
	 *            要拨打的电话号码
	 */
	public static void call(Context context, String phone)
	{
		Intent intent = new Intent(Intent.ACTION_CALL,
				Uri.parse("tel:" + phone));
		context.startActivity(intent);
	}

	/**
	 * 添加一个联系人到电话薄
	 * 
	 * @param context
	 * @param name
	 *            联系人姓名
	 * @param phone
	 *            电话
	 */
	public static void AddContract(Context context, String name, String phone)
	{
		// 创建一个空的ContentValues
		ContentValues values = new ContentValues();
		// 向rawcontent。content——uri执行一个空值插入
		// 目的是获取系统返回的rawcontactid
		ContentResolver contentResolver = context.getContentResolver();
		Uri rawcontacturi = contentResolver.insert(RawContacts.CONTENT_URI,
				values);
		long rawcontactid = ContentUris.parseId(rawcontacturi);

		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawcontactid);
		// 设置内容类型
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		// 设置联系人姓名
		values.put(StructuredName.GIVEN_NAME, name);
		// 向联系人URI添加联系人姓名
		contentResolver.insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawcontactid);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		// 设置联系人电话号码
		values.put(Phone.NUMBER, phone);
		// 设置电话类型
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);

		// 向联系人电话号码URI添加电话号码
		contentResolver.insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);
	}

	/**
	 * 查询存储在手机上的联系人
	 * 
	 * @param context
	 * @return 联系人列表
	 */
	public static List<Contract> getPhoneContacts(Context context)
	{
		List<Contract> list = new ArrayList<Contract>();
		ContentResolver resolver = context.getContentResolver();
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null)
		{
			while (phoneCursor.moveToNext())
			{
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				Contract team = new Contract(contactName, phoneNumber);
				list.add(team);
			}

			phoneCursor.close();
		}
		return list;
	}

	/**
	 * 查询存储在SIM卡上的联系人
	 * 
	 * @param context
	 * @return 联系人列表
	 */
	public static List<Contract> getSIMContacts(Context context)
	{
		List<Contract> list = new ArrayList<Contract>();
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
				null);

		if (phoneCursor != null)
		{
			while (phoneCursor.moveToNext())
			{

				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);
				Contract team = new Contract(contactName, phoneNumber);
				list.add(team);
			}
			phoneCursor.close();
		}
		return list;
	}
}
