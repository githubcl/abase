package cn.edu.cqut.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import cn.edu.cqut.base.exception.BaseException;

/**
 * HTTP协议操作工具类
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 * 
 */
public class HttpUtil
{
	private static String baseUrl = "";

	private static Map<String, String> urlMap = new HashMap<String, String>();

	/**
	 * 设置访问服务器公共URL
	 * 
	 * @param baseUrl
	 *            协议名 + 服务器IP + 端口号 + contextPath
	 */
	public static void setBaseURL(String baseUrl)
	{
		HttpUtil.baseUrl = baseUrl;
	}

	/**
	 * 向url容器中中增加url
	 * 
	 * @param key
	 *            url键
	 * @param url
	 *            url值
	 */
	public static void addURL(String urlKey, String urlValue)
	{
		urlMap.put(urlKey, baseUrl + urlValue);
	}

	/**
	 * @param urlKey
	 *            具体要访问的资源urlKey的键
	 * @param map
	 *            传给服务器的数据，以键值对的方式
	 * @return 服务器返回的数据,发生错误时返回null
	 * @throws Exception
	 */
	public static String post(String urlKey, Map<String, String> map)
	{
		LogUtil.debug("HttpUtil提交的数据:" + map.toString());
		String result = null;
		try
		{
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(urlMap.get(urlKey));
			HttpParams params = new BasicHttpParams();
			// 设置连接超时时间为10秒
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			// 从服务器获取响应数据等待超时时间
			HttpConnectionParams.setSoTimeout(params, 5000);
			httpPost.setParams(params);
			// 在执行之前必须设置参数
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				parameters.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}

			HttpEntity httpEntity = new UrlEncodedFormEntity(parameters,
					"UTF-8");
			httpPost.setEntity(httpEntity);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			StatusLine statusLine = httpResponse.getStatusLine();
			if (statusLine.getStatusCode() == 200)
			{
				httpEntity = httpResponse.getEntity();
				result = EntityUtils.toString(httpEntity, "UTF-8");
			}
		} catch (Exception e)
		{
			throw new BaseException(e);
		}
		LogUtil.debug("HttpUtil返回的数据:" + result);
		return result;
	}

}
