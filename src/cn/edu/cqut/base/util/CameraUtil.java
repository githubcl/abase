package cn.edu.cqut.base.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * 相机、照片相关工具类
 * 
 * @author chenliang
 * @version v1.0
 * @date 2014-2-20
 */
public class CameraUtil {
	public static final int REQUESTCODE_CAMERA = 11;

	/**
	 * 调用系统相机
	 * 
	 * @param activity
	 *            调用系统相机的Activity
	 */
	public static void startCamera(Activity activity) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		activity.startActivityForResult(intent, REQUESTCODE_CAMERA);
	}

	/**
	 * 调用系统相机，指定保存图片的目录
	 * 
	 * @param activity
	 *            调用系统相机的Activity
	 * @param saveDir
	 *            保存图片的目录
	 * @return 归还保存图片的路径
	 */
	public static String startCamera(Activity activity, String saveDir) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String filePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" +saveDir;
		LogUtil.debug("filePath:" + filePath);
		File mediaStorageDir = new File(filePath);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				LogUtil.error("保存图片创建保存文件夹失败");
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imagePath = mediaStorageDir.getPath() + File.separator + "IMG_"
				+ timeStamp + ".jpg";
		File mediaFile = new File(imagePath);
		Uri fileUri = Uri.fromFile(mediaFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		activity.startActivityForResult(intent, REQUESTCODE_CAMERA);
		return imagePath;
	}
}
