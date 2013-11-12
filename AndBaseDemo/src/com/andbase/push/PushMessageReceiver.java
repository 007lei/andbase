package com.andbase.push;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ab.db.storage.AbSqliteStorage;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInfoListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInsertListener;
import com.ab.db.storage.AbStorageQuery;
import com.ab.util.AbDateUtil;
import com.andbase.R;
import com.andbase.friend.ChatMsg;
import com.andbase.friend.ChatMsgDao;
import com.andbase.friend.PushUtil;
import com.andbase.friend.UserDao;
import com.andbase.main.MainActivity;
import com.andbase.model.User;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PushMessageReceiver extends FrontiaPushMessageReceiver {

	private final static String TAG = "PushMessageReceiver";
	//֪ͨ������
	private NotificationManager mNotificationManager;
	//֪ͨ��id
	private static int NOTIFICATIONS_ID = R.layout.main;
	//���ݿ������
	public AbSqliteStorage mAbSqliteStorage = null;
	public ChatMsgDao mChatMsgDao = null;
	public UserDao mUserDao = null;
	
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("�󶨳ɹ�\n");
		sb.append("errCode:"+errorCode);
		sb.append("appid:"+appid+"\n");
		sb.append("userId:"+userId+"\n");
		sb.append("channelId:"+channelId+"\n");
		sb.append("requestId"+requestId+"\n");
		Log.d(TAG,sb.toString());
		
		//���������������û�ʹ��
        PushUtil.saveChannelId(appid,userId,channelId,requestId);
	}

	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		StringBuffer sb = new StringBuffer();
		sb.append("���ɹ�\n");
		sb.append("errCode:"+errorCode);
		sb.append("requestId"+requestId+"\n");
		Log.d(TAG,sb.toString());
	}

	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> successTags, List<String> failTags,
			String requestId) {
		StringBuffer sb = new StringBuffer();
		sb.append("����tag�ɹ�\n");
		sb.append("errCode:"+errorCode);
		sb.append("success tags:");
		for(String tag:successTags){
			sb.append(tag+"\n");
		}
		sb.append("fail tags:");
		for(String tag:failTags){
			sb.append(tag+"\n");
		}
		sb.append("requestId"+requestId+"\n");
		Log.d(TAG,sb.toString());
	}

	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> successTags, List<String> failTags,
			String requestId) {
		StringBuffer sb = new StringBuffer();
		sb.append("ɾ��tag�ɹ�\n");
		sb.append("errCode:"+errorCode);
		sb.append("success tags:");
		for(String tag:successTags){
			sb.append(tag+"\n");
		}
		sb.append("fail tags:");
		for(String tag:failTags){
			sb.append(tag+"\n");
		}
		sb.append("requestId"+requestId+"\n");
		Log.d(TAG,sb.toString());
	}

	@Override
	public void onListTags(Context context, int errorCode,
			List<String> tags, String requestId) {
		StringBuffer sb = new StringBuffer();
		sb.append("list tag�ɹ�\n");
		sb.append("errCode:"+errorCode);
		sb.append("tags:");
		for(String tag:tags){
			sb.append(tag+"\n");
		}
		sb.append("requestId"+requestId+"\n");
		Log.d(TAG,sb.toString());
	}
	
	@Override
	public void onMessage(Context context, String message,String customContentString) {
		Log.d(TAG, "�յ�����Ϣ:" + message);
		if(mAbSqliteStorage == null){
			//��ʼ��AbSqliteStorage
		    mAbSqliteStorage = AbSqliteStorage.getInstance(context);
		    
		    //��ʼ�����ݿ����ʵ����
		    mChatMsgDao = new ChatMsgDao(context);
		    mUserDao  = new UserDao(context);
		}
		
		if(message!=null && message.indexOf("content")!=-1){
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			ChatMsg mChatMsg  = gson.fromJson(message, ChatMsg.class);
			mChatMsg.setSendState(2);
			mChatMsg.setCreateTime(AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHMS));
			
			saveData(mChatMsg);
			saveUserData(mChatMsg.getUser());
			
			//��ȡ֪ͨ������
	        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	        //����֪ͨ��ͼ������ʾ������
	        Notification notification = new Notification(R.drawable.ic_launcher,"����Ϣ",System.currentTimeMillis());
	        //���֪ͨ����ת��ָ��Activity
	        Intent mIntent  = new Intent(context, MainActivity.class);
	        User u = mChatMsg.getUser();
	        mIntent.putExtra("ID",u.getuId());
	        mIntent.putExtra("NAME", u.getName());
	        mIntent.putExtra("HEADURL",u.getPhotoUrl());
	        mIntent.putExtra("TO","chat");
	        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,mIntent, 0);
	        
	        //����֪ͨ�ı�������ʾ�����ݼ����֪ͨ�����ת
	        notification.setLatestEventInfo(context,u.getName(), mChatMsg.getContent(), contentIntent);
	        //���notification֮�󣬸�notification�Զ���ʧ
	        notification.flags = Notification.FLAG_AUTO_CANCEL;
	        //notification��notify��ʱ�򣬴���Ĭ��������Ĭ����
	        notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;
	        //��ʾ֪ͨ
	        mNotificationManager.notify(NOTIFICATIONS_ID, notification);
		}else{
			
		}
	}

	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		StringBuffer sb = new StringBuffer();
		sb.append("֪ͨ�����\n");
		sb.append("title:"+title+"\n");
		sb.append("description:"+description);
		sb.append("customContentString:"+customContentString+"\n");
		Log.d(TAG,sb.toString());
	}

	public void saveData(final ChatMsg chatMsg){
		if(mAbSqliteStorage == null){
			return;
		}
		mAbSqliteStorage.insertData(chatMsg, mChatMsgDao, new AbDataInsertListener(){

			@Override
			public void onSuccess(long id) {
				Log.d(TAG,"��Ϣ����ɹ�");
				chatMsg.set_id((int)id);
			}

			@Override
			public void onFailure(int errorCode, String errorMessage) {
			}
			
		});
	}
	
	public void saveUserData(final User user){
		if(mAbSqliteStorage == null){
			return;
		}
		//��ѯ����
		AbStorageQuery mAbStorageQuery = new AbStorageQuery();
		mAbStorageQuery.equals("u_id",user.getuId());
		
		//��sql�洢�Ĳ�ѯ
		mAbSqliteStorage.findData(mAbStorageQuery, mUserDao, new AbDataInfoListener(){

			@Override
			public void onFailure(int errorCode, String errorMessage) {
			}

			@Override
			public void onSuccess(List<?> paramList) {
				if(paramList==null || paramList.size()==0){
					mAbSqliteStorage.insertData(user, mUserDao, new AbDataInsertListener(){

						@Override
						public void onSuccess(long id) {
							Log.d(TAG,"����һ���û���Ϣ�ɹ�");
						}

						@Override
						public void onFailure(int errorCode, String errorMessage) {
							Log.d(TAG,"����һ���û���Ϣʧ��");
						}
						
					});
				}else{
					Log.d(TAG,"�û���Ϣ�Ѿ�����");
				}
			}
			
		});
		
	}
	
	/*<!-- push service client -->
    <receiver android:name="com.andbase.push.PushMessageReceiver">
        <intent-filter>
            <!-- ����push��Ϣ -->
            <action android:name="com.baidu.android.pushservice.action.MESSAGE" />
            <!-- ����bind,unbind,fetch,delete�ȷ�����Ϣ -->
            <action android:name="com.baidu.android.pushservice.action.RECEIVE" />
            <action android:name="com.baidu.android.pushservice.action.notification.CLICK" />
        </intent-filter>
    </receiver>*/

}
