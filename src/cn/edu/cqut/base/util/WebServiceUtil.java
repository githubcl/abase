package cn.edu.cqut.base.util;

import java.util.Map;
import java.util.Map.Entry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import cn.edu.cqut.base.exception.BaseException;

/**
 * WebServiceUtil工具类,需要库文件ksoap2-android-assembly-2.4-jar-with-dependencies.jar，
 * xfire-all-1.2.6.jar
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 */
public class WebServiceUtil
{
	private static String URL;
	private static String NAMESPACE;

	/**
	 * 对WebService进行初始化
	 * @param url WebService的地址
	 * @param namespace 命名空间
	 */
	public static void init(String url, String namespace)
	{
		URL = url;
		NAMESPACE = namespace;
	}
	
	/**
	 * 远程调用服务器端的方法
	 * 
	 * @param method
	 *            方法名
	 * @param map
	 *            传给服务器的参数，以键值对的方式
	 * @return 归还服务返回的数据
	 */
	public static String invoke(String method, Map<String, Object> map)
	{
		LogUtil.debug("WebService方法:" + method + "---提交数据:" + map.toString());
		String result = null;
		try
		{
			SoapObject soapObject = new SoapObject(NAMESPACE, method);
			for (Entry<String, Object> entry : map.entrySet())
			{
				soapObject.addProperty(entry.getKey(),
						String.valueOf(entry.getValue()));
			}
			// 设置请求超时时间为10秒

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(soapObject);
			// 设置超时时间为10秒
			HttpTransportSE httpTransportSE = new HttpTransportSE(URL, 10000);
			httpTransportSE.call(NAMESPACE + method, envelope);
			result = envelope.getResponse().toString();
		} catch (Exception e)
		{
			throw new BaseException(e);
		}
		LogUtil.debug("WebService方法:" + method + "返回数据:" + result);
		return result;
	}
}
