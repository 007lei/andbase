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
package com.ab.task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ab.global.AbAppData;
import com.ab.util.AbAppUtil;
// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�AbTaskPool.java 
 * �������̳߳�,������ֻ��1��
 * @author zhaoqp
 * @date��2013-5-23 ����10:10:53
 * @version v1.0
 */

public class AbTaskPool{
	
	/** The tag. */
	private static String TAG = "AbTaskPool";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	/** �������� The http pool. */
	private static AbTaskPool mAbTaskPool = null; 
	
	/** �̶�5���߳���ִ������. */
	private static int nThreads  = 5;
	
	/** The executor service. */
	private static ExecutorService executorService = null; 
	
	/** ������ɺ����Ϣ���. */
    private static Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
        	AbTaskItem item = (AbTaskItem)msg.obj; 
        	if(item.getCallback() instanceof AbTaskListCallback){
        		((AbTaskListCallback)item.callback).update((List<?>)item.getResult()); 
        	}else if(item.getCallback() instanceof AbTaskObjectCallback){
        		((AbTaskObjectCallback)item.callback).update(item.getResult()); 
        	}else{
        		item.callback.update(); 
        	}
        } 
    }; 
    
    /**
     * ��ʼ���̳߳�
     */
    static{
    	nThreads = AbAppUtil.getNumCores();
    	mAbTaskPool = new AbTaskPool(nThreads*5); 
    }
	
	/**
	 * �����̳߳�.
	 *
	 * @param nThreads ��ʼ���߳���
	 */
    protected AbTaskPool(int nThreads) {
    	executorService = Executors.newFixedThreadPool(nThreads); 
    } 
	
	/**
	 * ��������ͼƬ������.
	 *
	 * @return single instance of AbHttpPool
	 */
    public static AbTaskPool getInstance() { 
        return mAbTaskPool;
    } 
    
    /**
     * ִ������.
     * @param item the item
     */
    public void execute(final AbTaskItem item) {    
    	executorService.submit(new Runnable() { 
    		public void run() {
    			try {
    				//�����˻ص�
                    if (item.callback != null) { 
                    	item.callback.get();
                    	//����UI�̴߳��� 
                        Message msg = handler.obtainMessage(); 
                        msg.obj = item; 
                        handler.sendMessage(msg); 
                    }                              
    			} catch (Exception e) { 
    				throw new RuntimeException(e);
    			}                         
    		}                 
    	});                 
    	
    }
    
    
    /**
     * 
     * ��������ȡ�̳߳ص�ִ����
     * @return executorService
     * @throws 
     */
    public static ExecutorService getExecutorService() {
		return executorService;
	}


	/**
     * �����������ر�.
     */
    public void shutdownNow(){
    	if(!executorService.isTerminated()){
    		executorService.shutdownNow();
    		listenShutdown();
    	}
    	
    }
    
    /**
     * ������ƽ���ر�.
     */
    public void shutdown(){
    	if(!executorService.isTerminated()){
    	   executorService.shutdown();
    	   listenShutdown();
    	}
    }
    
    /**
     * �������رռ���.
     */
    public void listenShutdown(){
    	try {
			while(!executorService.awaitTermination(1, TimeUnit.MILLISECONDS)) { 
				if(D) Log.d(TAG, "�̳߳�δ�ر�");
			}  
			if(D) Log.d(TAG, "�̳߳��ѹر�");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
