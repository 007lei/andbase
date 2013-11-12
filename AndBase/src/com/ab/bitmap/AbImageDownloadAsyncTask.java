package com.ab.bitmap;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ab.global.AbAppData;
import com.ab.util.AbFileUtil;
import com.ab.util.AbMd5;
import com.ab.util.AbStrUtil;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�AbImageDownloadAsyncTask.java 
 * ������AsyncTaskʵ�ֵ����أ���������
 * @author zhaoqp
 * @date��2013-9-2 ����12:47:51
 * @version v1.0
 */
public class AbImageDownloadAsyncTask extends AsyncTask<AbImageDownloadItem, Integer, AbImageDownloadItem> {
	
	/** The tag. */
	private static String TAG = "AbImageAsyncTask";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	/** ������ɺ����Ϣ���. */
    private static Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
        	//if(D)Log.d(TAG, "����callback handleMessage...");
            AbImageDownloadItem item = (AbImageDownloadItem)msg.obj; 
            item.callback.update(item.bitmap, item.imageUrl); 
        } 
    }; 
	
	public AbImageDownloadAsyncTask() {
		super();
	}

	/**  
     * ����ĵ�һ��������ӦAsyncTask�еĵ�һ������   
     * �����String����ֵ��ӦAsyncTask�ĵ���������  
     * �÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�  
     * ���ǿ��Ե���publishProgress��������onProgressUpdate��UI���в���  
     */  
	@Override
	protected AbImageDownloadItem doInBackground(AbImageDownloadItem... items) {
		AbImageDownloadItem item = items[0];
		//���ͼƬ·��
    	String url = item.imageUrl;
    	if(AbStrUtil.isEmpty(url)){
    		if(D)Log.d(TAG, "ͼƬURLΪ�գ������ж�");
    	}else{
    		url = url.trim();
    	}
		//���SD�������ͼƬ, �ȵ�SD�������ͼƬ
		item.bitmap =  AbImageCache.getBitmapFromMemCache(AbMd5.MD5(url+"_"+item.width+"x"+item.height+"t"+item.type));
    	if(item.bitmap == null){
    		//��ʼ����
            item.bitmap = AbFileUtil.getBitmapFromSDCache(item.imageUrl,item.type,item.width,item.height);
            //����ͼƬ·��
            AbImageCache.addBitmapToMemoryCache(AbMd5.MD5(item.imageUrl+"_"+item.width+"x"+item.height+"t"+item.type),item.bitmap);                                           
            //��Ҫִ�лص�����ʾͼƬ
            if (item.callback != null) {
                Message msg = handler.obtainMessage(); 
                msg.obj = item; 
                handler.sendMessage(msg); 
            }
		}else{
			if (item.callback != null) {
                Message msg = handler.obtainMessage(); 
                msg.obj = item; 
                handler.sendMessage(msg); 
            }
	    }
		return item;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(AbImageDownloadItem item) {
		if (item.callback != null) { 
        	item.callback.update(item.bitmap, item.imageUrl); 
        }
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

}
