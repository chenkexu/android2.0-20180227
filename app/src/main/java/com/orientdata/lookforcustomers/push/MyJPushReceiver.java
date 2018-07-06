package com.orientdata.lookforcustomers.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orientdata.lookforcustomers.bean.MessageAndNoticeBean;
import com.orientdata.lookforcustomers.view.findcustomer.TaskDetailActivity;
import com.orientdata.lookforcustomers.view.home.imple.MsgDetailActivity;
import com.qiniu.android.common.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyJPushReceiver extends BroadcastReceiver {

	private static final String TAG = "JIGUANG";
	private String messageId ="";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			Log.d(TAG, "[MyJPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {//用户注册成功
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Log.d(TAG, "[MyJPushReceiver] 接收Registration Id : " + regId);
//				SharedPreferencesUtil.saveString(SysConstants.REG_ID,regId);

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Log.d(TAG, "[MyJPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

				// TODO: 2018/7/4 处理接收的逻辑
				Log.d(TAG, "[MyJPushReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				Log.d(TAG, "[MyJPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
				String content = bundle.getString(JPushInterface.EXTRA_ALERT);
				String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
				onClickNotification(context, notifactionId, title, content, extra);
				Log.d(TAG, "[MyJPushReceiver] 用户点击打开了通知");

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				Log.d(TAG, "[MyJPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Log.w(TAG, "[MyJPushReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				Log.d(TAG, "[MyJPushReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	private void onClickNotification(Context context, int id, String title, String content, String extra) {
		Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
		// 解析json
		Map<String, String> extraMap = new Gson().fromJson(extra, new TypeToken<Map<String, String>>() {
		}.getType());
		String typeStr = extraMap.get("type");
		int type = -1;
		try {
			type = Integer.valueOf(typeStr);
		} catch (NumberFormatException e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage());
		}
		jumpActivityByMessageType(context, extraMap, type);
	}


	private void jumpActivityByMessageType(Context context, Map<String, String> extraMap, int type) {
		Intent i = null;
		switch (type) {
			case 0: //任务
				i = new Intent(context, TaskDetailActivity.class);
				messageId = extraMap.get("messageId");
				if (messageId != null) {
					i.putExtra("taskId", Integer.parseInt(messageId));
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
				}
				break;
			case 1: //公告
				i = new Intent(context, MsgDetailActivity.class);
				MessageAndNoticeBean messageAndNoticeBean = new MessageAndNoticeBean();
				 messageId = extraMap.get("messageId");
				if (messageId != null) {
					messageAndNoticeBean.setAnnouncementId(Integer.parseInt(messageId));
					i.putExtra(Constants.MessageAndNoticeBean, messageAndNoticeBean);
					// i.putExtras(bundle);
					// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
				}
				break;
			case 2: //版本更新

				break;
			case 3: //消息
				i = new Intent(context, MsgDetailActivity.class);
				 messageId = extraMap.get("messageId");
				MessageAndNoticeBean messageAndNoticeBean2 = new MessageAndNoticeBean();
				if (messageId != null) {
					messageAndNoticeBean2.setPushMessageId(Integer.parseInt(messageId));
					i.putExtra(Constants.MessageAndNoticeBean, messageAndNoticeBean2);
					// i.putExtras(bundle);
					// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
				}
				break;
		}
	}


	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}




	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

	}
}

