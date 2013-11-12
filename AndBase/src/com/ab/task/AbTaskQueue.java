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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import android.os.Handler;
import android.os.Message;
import android.os.Process;

import com.ab.global.AbAppData;

// TODO: Auto-generated Javadoc
/**
 * ������ ִ�������̣߳�������ִ�У�.
 * ÿ������ֻ��1��
 * @author zhaoqp
 * @date 2011-11-10
 * @version v1.0
 */
public class AbTaskQueue extends Thread { 
	
	/** The tag. */
	private static String TAG = "AbTaskQueue";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	/** �ȴ�ִ�е�����. */
	private static List<AbTaskItem> mAbTaskItemList = null;
    
    /**�������� */
  	private static AbTaskQueue mAbTaskQueue = null; 
  	
  	/** ֹͣ�ı��. */
	private boolean mQuit = false;
	
	/** ִ����ɺ����Ϣ���. */
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
	 * ��������.
	 */
    public static AbTaskQueue getInstance() { 
        if (mAbTaskQueue == null) { 
        	mAbTaskQueue = new AbTaskQueue();
        } 
        return mAbTaskQueue;
    } 
	
	/**
	 * ����ִ���̶߳���.
	 *
	 * @param context the context
	 */
    public AbTaskQueue() {
    	mQuit = false;
    	mAbTaskItemList = new ArrayList<AbTaskItem>();
    	//�������ȼ�
    	Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
    	//���̳߳��л�ȡ
    	ExecutorService mExecutorService  = AbTaskPool.getExecutorService();
    	mExecutorService.submit(this); 
    }
    
    /**
     * ��ʼһ��ִ������.
     *
     * @param item ִ�е�λ
     */
    public void execute(AbTaskItem item) { 
         addTaskItem(item); 
    } 
    
    
    /**
     * ��ʼһ��ִ���������ԭ������.
     * @param item ִ�е�λ
     * @param clean ���֮ǰ������
     */
    public void execute(AbTaskItem item,boolean clean) { 
	    if(clean){
	    	if(mAbTaskQueue!=null){
	    		mAbTaskQueue.quit();
	    	}
	    }
    	addTaskItem(item); 
    } 
     
    /**
     * ��������ӵ�ִ���̶߳���.
     *
     * @param item ִ�е�λ
     */
    private synchronized void addTaskItem(AbTaskItem item) { 
    	if (mAbTaskQueue == null) { 
        	mAbTaskQueue = new AbTaskQueue();
        	mAbTaskItemList.add(item);
        } else{
        	mAbTaskItemList.add(item);
        }
    	//�����ִ����ͼ���߳� 
        this.notify();
        
    } 
 
    /**
     * �������߳�����.
     *
     * @see java.lang.Thread#run()
     */
    @Override 
    public void run() { 
        while(!mQuit) { 
        	try {
        	    while(mAbTaskItemList.size() > 0){
            
					AbTaskItem item  = mAbTaskItemList.remove(0);
					//�����˻ص�
				    if (item.callback != null) { 
				    	item.callback.get();
				    	//����UI�̴߳��� 
				        Message msg = handler.obtainMessage(); 
				        msg.obj = item; 
				        handler.sendMessage(msg); 
				    } 
				    
				    //ֹͣ�����
				    if(mQuit){
				    	mAbTaskItemList.clear();
				    	return;
				    }
        	    }
        	    try {
					//û��ִ����ʱ�ȴ� 
					synchronized(this) { 
					    this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					//���жϵ����˳��ͽ������������
					if (mQuit) {
						mAbTaskItemList.clear();
	                    return;
	                }
	                continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
        } 
    } 
    
    /**
     * ��������ֹ�����ͷ��߳�.
     */
    public void quit(){
		mQuit  = true;
		interrupted();
		mAbTaskQueue  = null;
    }

}

