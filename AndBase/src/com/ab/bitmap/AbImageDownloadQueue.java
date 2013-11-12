/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ab.bitmap;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ab.global.AbAppData;
import com.ab.util.AbFileUtil;
import com.ab.util.AbMd5;
import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc
/**
 * ������ͼƬ�����̣߳����������أ��ȼ��SD���Ƿ������ͬ�ļ��������������أ�����ٴ�SD���ж�ȡ��.
 *
 * @author zhaoqp
 * @date 2011-12-10
 * @version v1.0
 */
public class AbImageDownloadQueue extends Thread { 
	
	/** The tag. */
	private static String TAG = "AbImageDownloadQueue";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	/** ���ض���. */
	private List<AbImageDownloadItem> queue;
	
	/** ͼƬ�����̵߳�����. */
    private static AbImageDownloadQueue imageDownloadThread = null; 
    
    /** �����ͷ�. */
    private static boolean stop = false;
    
    /** ������ɺ����Ϣ���. */
    private static Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
        	//if(D)Log.d(TAG, "����callback handleMessage...");
            AbImageDownloadItem item = (AbImageDownloadItem)msg.obj; 
            item.callback.update(item.bitmap, item.imageUrl); 
        } 
    }; 
    
    
    /**
     * ����ͼƬ�����̶߳���.
     */
    private AbImageDownloadQueue() {
    	stop = false;
		queue = new ArrayList<AbImageDownloadItem>();
    } 
    
    /**
     * ��������ͼƬ�����߳�.
     *
     * @return single instance of AbImageDownloadQueue
     */
    public static AbImageDownloadQueue getInstance() { 
        if (imageDownloadThread == null) { 
            imageDownloadThread = new AbImageDownloadQueue(); 
            //��������������
            imageDownloadThread.start(); 
        } 
        return imageDownloadThread; 
    } 
     
    /**
     * ��ʼһ����������.
     *
     * @param item ͼƬ���ص�λ
     * @return Bitmap ������ɺ�õ���Bitmap
     */
    public void download(AbImageDownloadItem item) { 
    	//���ͼƬ·��
    	String url = item.imageUrl;
    	if(AbStrUtil.isEmpty(url)){
    		if(D)Log.d(TAG, "ͼƬURLΪ�գ������ж�");
    	}else{
    		url = url.trim();
    	}
		//�ӻ����л�ȡ���Bitmap.
    	String cacheKey = AbImageCache.getCacheKey(item.imageUrl, item.width, item.height, item.type);
		item.bitmap =  AbImageCache.getBitmapFromMemCache(cacheKey);
    	if(item.bitmap == null){
    			addDownloadItem(item); 
		}else{
    		if (item.callback != null) {
                Message msg = handler.obtainMessage(); 
                msg.obj = item; 
                handler.sendMessage(msg); 
            }
	    }
    } 
    
    /**
     * ��������ӵ�ͼƬ�����̶߳���.
     *
     * @param item ͼƬ���ص�λ
     */
    private synchronized void addDownloadItem(AbImageDownloadItem item) { 
        queue.add(item); 
        //�����������ͼ���߳� 
        this.notify();
    } 
    
    /**
     * ��ʼһ�������������ԭ������.
     *
     * @param item ���ص�λ
     */
    public void downloadBeforeClean(AbImageDownloadItem item) { 
    	queue.clear();
        addDownloadItem(item); 
    } 
 
    /**
     * �������߳�����.
     *
     * @see java.lang.Thread#run()
     */
    @Override 
    public void run() { 
        while(!stop) { 
        	//if(D)Log.d(TAG, "�����С��"+queue.size());
            while(queue.size() > 0) { 
                AbImageDownloadItem item = queue.remove(0); 
                //��ʼ����
                item.bitmap = AbFileUtil.getBitmapFromSDCache(item.imageUrl,item.type,item.width,item.height);
                //����ͼƬ·��
                String cacheKey = AbImageCache.getCacheKey(item.imageUrl, item.width, item.height, item.type);
                AbImageCache.addBitmapToMemoryCache(cacheKey,item.bitmap);                                           
                //��Ҫִ�лص�����ʾͼƬ
                if (item.callback != null) { 
                	//if(D)Log.d(TAG, "����callback...");
                    //����UI�̴߳��� 
                    Message msg = handler.obtainMessage(); 
                    msg.obj = item; 
                    handler.sendMessage(msg); 
                } 
                
                //ֹͣ
                if(stop){
                	queue.clear();
                	return;
                }
            } 
            try { 
            	//û��������ʱ�ȴ� 
                synchronized(this) { 
                    this.wait();
                } 
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            } 
        } 
    } 

    /**
     * 
     * ��������ֹ�����ͷ��߳�
     * @throws 
     */
    public void stopQueue(){
    	try {
			stop  = true;
			imageDownloadThread = null;
			this.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}

