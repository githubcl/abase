package cn.edu.cqut.base.exception;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.client.ClientProtocolException;
import org.ksoap2.SoapFault;
import org.xmlpull.v1.XmlPullParserException;

import cn.edu.cqut.base.util.LogUtil;

import android.net.ParseException;
import android.text.TextUtils;

/**
 * 全局异常类
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 */
public class BaseException extends RuntimeException
{
	private String msg;

	public BaseException(String message)
	{
		super(message);
		msg = message;
	}

	public BaseException(Exception e)
	{
		super();
		try
		{
			if (e instanceof ConnectException)
			{
				msg = "无法连接网络，请检查网络配置";
			} else if (e instanceof UnknownHostException)
			{
				msg = "不能解析的服务地址";
			} else if (e instanceof SocketException)
			{
				msg = "网络有错误，请重试";
			} else if (e instanceof SocketTimeoutException)
			{
				msg = "连接超时，请重试";
			} else if (e instanceof ClientProtocolException)
			{
				msg = "ClientProtocolException异常";
			} else if (e instanceof SoapFault)
			{
				msg = "服务器发生异常!";
			} else if (e instanceof IOException)
			{
				msg = "连接超时，请重试!";
			} else if (e instanceof ParseException)
			{
				msg = "ParseException异常";
			} else if (e instanceof XmlPullParserException)
			{
				msg = "XmlPullParserException异常";
			} else
			{
				if (e == null || TextUtils.isEmpty(e.getMessage()))
				{
					msg = "抱歉，程序出错了，请联系我们";
				}
				msg = " " + e.getMessage();
			}
		} catch (Exception e1)
		{
		}
		e.printStackTrace();
		LogUtil.error(msg);
	}

	@Override
	public String getMessage()
	{
		return msg;
	}
}
