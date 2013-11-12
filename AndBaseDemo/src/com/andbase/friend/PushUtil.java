package com.andbase.friend;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ab.activity.AbActivity;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaACL;
import com.baidu.frontia.FrontiaAccount;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.api.FrontiaPush;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataInsertListener;

public class PushUtil {
	
	  private static final String TAG = "PushUtil";
	  private static final boolean D = Constant.DEBUG;
	  //Ӧ�����ݴ洢
	  private static FrontiaStorage mCloudStorage = Frontia.getStorage();
	  private static MyApplication application = null;
	
	  /**
	    * 
	    * ���������ͷ���
	    * @throws 
	    */
	   public static void startPushService(final FrontiaPush mPush,final AbActivity activity){
		   application = (MyApplication)activity.abApplication;
		   if(application.mUser == null || application.mUser.getAccessToken() == null){
			   return;
		   }
	       if(!mPush.isPushWorking()){
	    	   Log.d(TAG, "���ͷ�������");
	    	   mPush.start(application.mUser.getAccessToken());
	       }
	       
	   }   
	   
	   
	   public static void saveChannelId(String appid,String userId,String channelId,String requestId){
		   if(application==null || application.mUser == null || application.mUser.getAccessToken() == null){
			   return;
		   }
		   
		   queryByUserId(appid,userId,channelId,requestId);
	   }
	   
	   /**
	    * 
	    * ����������ͨ�����ܱ������û���ѯ
	    * @param mPushAppId
	    * @param mUserId
	    * @param mPushUserId
	    * @param mPushChannelId
	    * @param handler
	    * @throws 
	    */
	   public static void saveData(final String appid,final String userId,final String channelId,final String requestId){
		   
		   FrontiaAccount user = Frontia.getCurrentAccount();
		   /**FrontiaACL acl = new FrontiaACL();
		   acl.setAccountReadable(user, true);
		   acl.setAccountWritable(user, true);
		   acl.setPublicReadable(true);*/
	        
		   //������Frontia�˻��ɶ�д
	       FrontiaACL acl = new FrontiaACL();
	       acl.setPublicReadable(true);
	       acl.setAccountWritable(user,true);
		   
		   JSONObject data = new JSONObject();
	       try {
	    	    data.put("push_user_id",userId);
	    	    data.put("push_app_id",appid);
	        	data.put("push_local_user_id",application.mUser.getuId());
	        	//�����ͷ�����������Ҫ���ý�ȥ�����ڷ���Ϣ,�Լ�������Ҫ�����˲���Ҫ
	        	data.put("push_channel_id",channelId);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	   		FrontiaData newData = new FrontiaData(data);
	   		newData.setACL(acl);
	   		Log.d(TAG, "��ʼ����channel���ݣ�"+data.toString());
	   		//�洢����
	   		mCloudStorage.insertData(newData,new DataInsertListener() {
	   		    @Override
	   		    public void onSuccess() {
	   		        Log.d(TAG, "channel�����ѱ���");
	   		        //queryAll();
	   		        
	   		    }

	   		    @Override
	   		    public void onFailure(int errCode, String errMsg) {
	   		    	Log.d(TAG, "channel���ݱ������Ϊ"+errCode+errMsg);
	   		    }
	     	});
	   		
	   }
	   
	   
	   public static void queryAll(){
           FrontiaQuery query = new FrontiaQuery();
           mCloudStorage.findData(query, new DataInfoListener() {
               @Override
               public void onSuccess(List<FrontiaData> dataList) {
               	   Log.d(TAG, "��ѯ�ƶ��������ݣ�"+dataList);
               }

               @Override
               public void onFailure(int errCode, String errMsg) {
               	    Log.d(TAG, "errCode: " + errCode+ ", errMsg: " + errMsg);
               }
           });
	   }
	   
	   public static void queryByUserId(final String appid,final String userId,final String channelId,final String requestId){
		   
		   Log.d(TAG, "��ʼ��ѯchannel����...");
		   
		   //���Ҳ�Ҫ���ҵ�key����ɾ��������лл�ˣ�ǧ��С�ģ����������Լ���key
		   /*FrontiaQuery query1 = new FrontiaQuery();
		   Log.d(TAG, "ɾ����������");
		   mCloudStorage.deleteData(query1, null);*/
		   
		   FrontiaQuery query = new FrontiaQuery();
           query = query.equals("push_user_id",userId);
           mCloudStorage.findData(query, new DataInfoListener() {
               @Override
               public void onSuccess(List<FrontiaData> dataList) {
	               	Log.d(TAG, "��ѯchannel����(push_user_id="+userId+")��"+dataList);
	               	
	               	if(dataList!=null && dataList.size()>0){
	               		Log.d(TAG, "����channel���ݣ�����Ҫ����");
		               	
	               	}else{
	               		saveData(appid,userId,channelId,requestId);
	               	}
               }

               @Override
               public void onFailure(int errCode, String errMsg) {
               	    Log.d(TAG, "��ѯchannel����errCode: " + errCode+ ", errMsg: " + errMsg);
               }
           });
	   }
	   
	   /**
	    * 
	    * ������ɾ��channel����
	    * @param mUserId
	    * @throws 
	    */
	   public static void deleteByUserId(String mUserId){
		   //��ɾ����ǰ��
		   FrontiaQuery query = new FrontiaQuery();
		   query = query.equals("push_user_id", mUserId);
		   Log.d(TAG, "ɾ��channel���ݣ�push_user_id��"+mUserId);
		   mCloudStorage.deleteData(query, null);
	   }
	   
}
