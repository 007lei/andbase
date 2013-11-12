package com.andbase.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.task.AbAsyncTask;
import com.ab.task.AbAsyncThread;
import com.ab.task.AbTaskCallback;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskPool;
import com.ab.task.AbTaskQueue;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

public class ThreadControlActivity extends AbActivity {
	
	private MyApplication application;
	private AbTitleBar mAbTitleBar = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.thread_main);
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.thread_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        initTitleRightLayout();
        
        Button threadBtn  = (Button)this.findViewById(R.id.threadBtn);
        Button queueBtn  = (Button)this.findViewById(R.id.queueBtn);
        Button poolBtn  = (Button)this.findViewById(R.id.poolBtn);
        Button taskBtn  = (Button)this.findViewById(R.id.taskBtn);
        
        //�����߳�
        threadBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//��ʾ���ȿ�
				showProgressDialog();
				AbAsyncThread mAbTaskThread = new AbAsyncThread();
				//�����첽ִ�еĶ���
		    	final AbTaskItem item = new AbTaskItem();
				item.callback = new AbTaskCallback() {

					@Override
					public void update() {
						removeProgressDialog();
						showToast("ִ�����");
					}

					@Override
					public void get() {
			   		    try {
			   		    	showToastInThread("��ʼִ��");
			   		    	Thread.sleep(3000);
			   		    	//����дҪִ�еĴ��룬����������
			   		    } catch (Exception e) {
			   		    }
				  };
				};
				//��ʼִ��
				mAbTaskThread.execute(item);
			}
        	
        });
        
        //�̶߳���
        queueBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//��ʾ���ȿ�
				showProgressDialog();
				//��ȡ����
				AbTaskQueue mAbTaskQueue = AbTaskQueue.getInstance();
				//�����첽ִ�еĶ���
		    	AbTaskItem item1 = new AbTaskItem();
				item1.callback = new AbTaskCallback() {

					@Override
					public void update() {
						showToast("ִ�����1");
					}

					@Override
					public void get() {
			   		    try {
			   		    	showToastInThread("��ʼִ��1");
			   		    	Thread.sleep(2000);
			   		    	//����дҪִ�еĴ��룬����������
			   		    } catch (Exception e) {
			   		    }
				  };
				};
				
				AbTaskItem item2 = new AbTaskItem();
				item2.callback = new AbTaskCallback() {

					@Override
					public void update() {
						showToast("ִ�����2");
						removeProgressDialog();
					}

					@Override
					public void get() {
			   		    try {
			   		    	showToastInThread("��ʼִ��2");
			   		    	Thread.sleep(3000);
			   		    	//����дҪִ�еĴ��룬����������
			   		    } catch (Exception e) {
			   		    }
				  };
				};
				
				//��ʼִ��
				mAbTaskQueue.execute(item1);
				mAbTaskQueue.execute(item2);
				
				//ǿ��ֹͣ
				//mAbTaskQueue.quit();
				
				//ǿ��ֹͣǰ�������
				//mAbTaskQueue.execute(item2,true);
			}
        	
        });
        
        
        //�̳߳�
        poolBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//��ʾ���ȿ�
				showProgressDialog();
				AbTaskPool mAbTaskPool = AbTaskPool.getInstance();
				//�����첽ִ�еĶ���
		    	final AbTaskItem item = new AbTaskItem();
				item.callback = new AbTaskCallback() {

					@Override
					public void update() {
						removeProgressDialog();
						showToast("ִ�����");
					}

					@Override
					public void get() {
			   		    try {
			   		    	showToastInThread("��ʼִ��");
			   		    	Thread.sleep(1000);
			   		    	//����дҪִ�еĴ��룬����������
			   		    } catch (Exception e) {
			   		    }
				  };
				};
				//��ʼִ��
				mAbTaskPool.execute(item);
			}
        	
        });
        
        //�첽����
        taskBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showProgressDialog();
				AbAsyncTask task = new AbAsyncTask();
				//�����첽ִ�еĶ���
		    	final AbTaskItem item = new AbTaskItem();
				item.callback = new AbTaskCallback() {

					@Override
					public void update() {
						removeProgressDialog();
						showToast("ִ�����");
					}

					@Override
					public void get() {
			   		    try {
			   		    	showToastInThread("��ʼִ��");
			   		    	Thread.sleep(3000);
			   		    	//����дҪִ�еĴ��룬����������
			   		    } catch (Exception e) {
			   		    }
				  };
				};
		        task.execute(item);
			}
        	
        });
        
    }
    
    
    private void initTitleRightLayout(){
    	mAbTitleBar.clearRightView();
    }

	@Override
	protected void onResume() {
		super.onResume();
		initTitleRightLayout();
	}
	
	public void onPause() {
		super.onPause();
	}
   
}


