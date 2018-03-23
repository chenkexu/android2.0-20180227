package vr.md.com.mdlibrary.utils.imagepicker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

import vr.md.com.mdlibrary.BaseActivity;
import vr.md.com.mdlibrary.utils.MySharedpreferences;
import vr.md.com.mdlibrary.utils.MyUtils;

/**
 * @author shanglibj6883 从手机提供图片，或者拍照选图的通用工具类
 *
 */
public class ImagePicker {

	public static final int PICK = 0; // 选择图片
	public static final int TAKE = 1; // 拍照获取
	private String mCameraPicNameString = null;// 拍照后，得到的图片名字

	private String imageFilePath;//图片路径


	private OnImageSelected onImageSelected;//图片加载完成监听器

	public String getCameraPicNameString() {
		return mCameraPicNameString;
	}

	public String getIamgeFilePath(BaseActivity baseActivity) {
		if (TextUtils.isEmpty(imageFilePath)) {
			imageFilePath = MySharedpreferences.getString("imageFilePath");
		}
		return imageFilePath;
	}

	public void setOnImageSelected(OnImageSelected onImageSelected) {
		this.onImageSelected = onImageSelected;
	}


	/**
	 * 将在onActivityResult中返回图片的uri
	 * 
	 * @param activity
	 * @param mode
	 * @param requestCode
	 */
	public void getImage(Activity activity, int mode, int requestCode) {
		switch (mode) {
		case PICK:
			pickPhoto(activity, requestCode);
			break;
		case TAKE:
			takePhoto(activity, requestCode);
			break;

		default:
			break;
		}
	}

	/**
	 * 从本地获取图片
	 * 
	 * @param activity
	 *            发起请求图像的activity
	 * @param requestCode
	 */
	private void pickPhoto(Activity activity, int requestCode) {

		// 进入android系统相册选择
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		activity.startActivityForResult(intent, requestCode);

		// 直接进入手机系统相册选择
		// Intent picture = new Intent(
		// Intent.ACTION_PICK,
		// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// activity.startActivityForResult(picture, requestCode);

	}

	private void takePhoto(Activity activity, int requestCode) {

		try {

			Calendar calendar = Calendar.getInstance();
			mCameraPicNameString = calendar.getTimeInMillis() + ".jpg";

			String picPathString = createDir(activity);
			if (null != picPathString) {
				// 创建文件
				File picFile = new File(picPathString, mCameraPicNameString );
				imageFilePath = picFile.getPath();
				MySharedpreferences.putString("imageFilePath", imageFilePath);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 存储卡可用 将照片存储在 sdcard
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
				activity.startActivityForResult(intent, requestCode);
			}
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, "R.string.photoPickerNotFoundText", Toast.LENGTH_SHORT);
		}

	}

	/**
	 * 创建拍照图片缓存地址
	 * 
	 * @return
	 */
	private String createDir(Activity activity) {
		String imageCacheDir = null;

		try {
			if (!MyUtils.checkSDCard()) {
				Toast.makeText(activity, "请插入SD卡", Toast.LENGTH_SHORT);
			} else {
				imageCacheDir = MyUtils.getAppPath() ;
				File file = new File(imageCacheDir);
				if (!file.exists()) {
					file.mkdirs();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return imageCacheDir;
	}

	/**
	 * 图片选择监听器
	 */
	public interface OnImageSelected {
		public abstract void onImageSelected(String filePath);
	}



	/**
	 * 创建文件路径
	 * @return
	 */
	public String creatFilePath(BaseActivity baseActivity) {
		Calendar calendar = Calendar.getInstance();
		mCameraPicNameString = calendar.getTimeInMillis() + ".jpg";
		String picPathString = createDir(baseActivity);
		if (null != picPathString) {
			// 创建文件
			File picFile = new File(picPathString, mCameraPicNameString );
			imageFilePath = picFile.getPath();
		}
		return imageFilePath;
	}
}
