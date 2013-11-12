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
package com.ab.activity;

import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ab.global.AbAppData;
import com.ab.global.AbConstant;
import com.ab.util.AbStrUtil;
import com.ab.view.app.AbMonitorView;
import com.ab.view.ioc.AbIocSelect;
import com.ab.view.ioc.AbIocView;
import com.ab.view.listener.AbIocEventListener;
import com.ab.view.titlebar.AbTitleBar;

// TODO: Auto-generated Javadoc
/**
 * �������̳����Activityʹ���Activityӵ��һ��������.
 * @author zhaoqp
 * @date��2013-1-15 ����3:27:05
 * @version v1.0
 */

public abstract class AbActivity extends FragmentActivity {
	
	/** The tag. */
	private String TAG = AbActivity.class.getSimpleName();
	
	/** The debug. */
	private boolean D = AbAppData.DEBUG;

	/** ���ؿ������˵��. */
	private String mProgressMessage = "���Ժ�...";
	
	/** ȫ�ֵ�LayoutInflater�����Ѿ���ɳ�ʼ��. */
	public LayoutInflater mInflater;
	
	/** ȫ�ֵļ��ؿ�����Ѿ���ɳ�ʼ��. */
	public ProgressDialog mProgressDialog;
	
	/** �ײ�������Dialog. */
	private Dialog mBottomDialog;
	
	/** ���е�����Dialog. */
	private Dialog mCenterDialog;
	
	/** ����������Dialog. */
	private Dialog mTopDialog;
	
	/** �ײ�������Dialog��View. */
	private View mBottomDialogView = null;
	
	/** ���е�����Dialog��View. */
	private View mCenterDialogView = null;
	
	/** ����������Dialog��View. */
	private View mTopDialogView = null;
	
	/** ������Dialog�����ұ߾�. */
	private int dialogPadding = 40;
	
	/** ȫ�ֵ�Application�����Ѿ���ɳ�ʼ��. */
	public Application abApplication = null;
	
	/** ȫ�ֵ�SharedPreferences�����Ѿ���ɳ�ʼ��. */
	public SharedPreferences  abSharedPreferences = null;
	
	/**
	 * LinearLayout.LayoutParams���Ѿ���ʼ��ΪFILL_PARENT, FILL_PARENT
	 */
	public LinearLayout.LayoutParams layoutParamsFF = null;
	
	/**
	 * LinearLayout.LayoutParams���Ѿ���ʼ��ΪFILL_PARENT, WRAP_CONTENT
	 */
	public LinearLayout.LayoutParams layoutParamsFW = null;
	
	/**
	 * LinearLayout.LayoutParams���Ѿ���ʼ��ΪWRAP_CONTENT, FILL_PARENT
	 */
	public LinearLayout.LayoutParams layoutParamsWF = null;
	
	/**
	 * LinearLayout.LayoutParams���Ѿ���ʼ��ΪWRAP_CONTENT, WRAP_CONTENT
	 */
	public LinearLayout.LayoutParams layoutParamsWW = null;
	
	/** �ܲ���. */
	public RelativeLayout ab_base = null;
	
	/** ����������. */
	protected AbTitleBar mAbTitlebar = null;
	
	/** ����������ID. */
	public int mAbTitlebarID = 1;
	
	/** �����ݲ���. */
	protected RelativeLayout contentLayout = null;
	
	/** ��Ļ���. */
	public int diaplayWidth  = 320;
	
	/** ��Ļ�߶�. */
	public int diaplayHeight  = 480;
	
	/** ֡����View. */
	private AbMonitorView mAbMonitorView  = null;
	
	/** ֡����Handler. */
	private Handler mMonitorHandler = new Handler();
	
	/** ֡�����߳�. */
	private Runnable mMonitorRunnable = null;
	
	/** Window ������. */
	private WindowManager mWindowManager = null;
	
	/** ֡���Բ���. */
	private WindowManager.LayoutParams mMonitorParams = null;
	
    
	/**
	 * ��ҪHandler�࣬���߳��п���
	 * what��0.��ʾ�ı���Ϣ,1.�ȴ���   ,2.�Ƴ��ȴ��� 
	 */
	private Handler baseHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case AbConstant.SHOW_TOAST:
					showToast(msg.getData().getString("Msg"));
					break;
				case AbConstant.SHOW_PROGRESS:
					showProgressDialog(mProgressMessage);
					break;
				case AbConstant.REMOVE_PROGRESS:
					removeProgressDialog();
					break;
				case AbConstant.REMOVE_DIALOGBOTTOM:
					removeDialog(AbConstant.DIALOGBOTTOM);
				case AbConstant.REMOVE_DIALOGCENTER:
					removeDialog(AbConstant.DIALOGCENTER);
				case AbConstant.REMOVE_DIALOGTOP:
					removeDialog(AbConstant.DIALOGTOP);
				default:
					break;
			}
		}
	};

	/**
	 * ����������.
	 *
	 * @param savedInstanceState the saved instance state
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mInflater = LayoutInflater.from(this);
		
		mWindowManager = getWindowManager();
		Display display = mWindowManager.getDefaultDisplay();
		diaplayWidth = display.getWidth();
		diaplayHeight = display.getHeight();
		
		layoutParamsFF = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWF = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		layoutParamsWW = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		mAbTitlebar = new AbTitleBar(this);
		
		ab_base = new RelativeLayout(this);
		ab_base.setBackgroundColor(Color.rgb(255, 255, 255));
		
		contentLayout = new RelativeLayout(this);
		contentLayout.setPadding(0, 0, 0, 0);
		
		Intent intent = this.getIntent();
		int titleTransparentFlag = intent.getIntExtra(AbConstant.TITLE_TRANSPARENT_FLAG,AbConstant.TITLE_NOTRANSPARENT);
		
		if(titleTransparentFlag == AbConstant.TITLE_TRANSPARENT){
			ab_base.addView(contentLayout,layoutParamsFW);
			RelativeLayout.LayoutParams layoutParamsFW2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW2.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
			ab_base.addView(mAbTitlebar,layoutParamsFW2);
		}else{
			ab_base.addView(mAbTitlebar,layoutParamsFW);
			RelativeLayout.LayoutParams layoutParamsFW1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW1.addRule(RelativeLayout.BELOW, mAbTitlebarID);
			ab_base.addView(contentLayout, layoutParamsFW1);
		}
		
		abApplication = getApplication();
		abSharedPreferences = getSharedPreferences(AbConstant.SHAREPATH, Context.MODE_PRIVATE);
        
        setContentView(ab_base,layoutParamsFF);
        
        //���Dialog���ǳ�����Ļ��Ҫ�������ֵ
        if(diaplayWidth < 400){
        	dialogPadding = 30;
		}else if(diaplayWidth>700){
			dialogPadding = 50;
		}
	}
	
	/**
     * ������Toast��ʾ�ı�.
     * @param text  �ı�
     */
	public void showToast(String text) {
		Toast.makeText(this,""+text, Toast.LENGTH_SHORT).show();
	}
	
	/**
     * ������Toast��ʾ�ı�.
     * @param resId  �ı�����ԴID
     */
	public void showToast(int resId) {
		Toast.makeText(this,""+this.getResources().getText(resId), Toast.LENGTH_SHORT).show();
	}
	
	
	
	/**
	 * ��������ָ����View���������.
	 * @param contentView  ָ����View
	 */
	public void setAbContentView(View contentView) {
		contentLayout.removeAllViews();
		contentLayout.addView(contentView,layoutParamsFF);
		//ioc
		initIocView();
	}
	
	/**
	 * ��������ָ����ԴID��ʾ��View���������.
	 * @param resId  ָ����View����ԴID
	 */
	public void setAbContentView(int resId) {
		contentLayout.removeAllViews();
		contentLayout.addView(mInflater.inflate(resId, null),layoutParamsFF);
		//ioc
		initIocView();
	}
	
	

	/**
	 * ���������߳�����ʾ�ı���Ϣ.
	 * @param resId Ҫ��ʾ���ַ�����ԴID����ϢwhatֵΪ0,
	 */
	public void showToastInThread(int resId) {
		Message msg = baseHandler.obtainMessage(0);
		Bundle bundle = new Bundle();
		bundle.putString("Msg", this.getResources().getString(resId));
		msg.setData(bundle);
		baseHandler.sendMessage(msg);
	}
	
	/**
	 * ���������߳�����ʾ�ı���Ϣ.
	 * @param toast ��ϢwhatֵΪ0
	 */
	public void showToastInThread(String toast) {
		Message msg = baseHandler.obtainMessage(0);
		Bundle bundle = new Bundle();
		bundle.putString("Msg", toast);
		msg.setData(bundle);
		baseHandler.sendMessage(msg);
	}
	
	/**
	 * ��������ʾ���ȿ�.
	 */
	public void showProgressDialog() {
		showProgressDialog(null);
    }
	
	/**
	 * ��������ʾ���ȿ�.
	 * @param message the message
	 */
	public void showProgressDialog(String message) {
		// ����һ����ʾ���ȵ�Dialog
		if(!AbStrUtil.isEmpty(message)){
			mProgressMessage = message;
		}
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);  
			// ���õ����ĻDialog����ʧ    
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.setMessage(mProgressMessage);
		showDialog(AbConstant.DIALOGPROGRESS);
    }
	
	
	/**
	 * �������ڵײ���ʾһ��Dialog,idΪ1,���м���ʾidΪ2.
	 * @param id Dialog������
	 * @param view ָ��һ����View
	 * @see   AbConstant.DIALOGBOTTOM
	 */
	public void showDialog(int id,View view) {
		
		if(id == AbConstant.DIALOGBOTTOM){
			mBottomDialogView = view;
			if(mBottomDialog == null){
				mBottomDialog = new Dialog(this);
				setDialogLayoutParams(mBottomDialog,dialogPadding,Gravity.BOTTOM);
			}
			mBottomDialog.setContentView(mBottomDialogView,new LayoutParams(diaplayWidth-dialogPadding,LayoutParams.WRAP_CONTENT));
			showDialog(id);
		}else if(id == AbConstant.DIALOGCENTER){
			mCenterDialogView = view;
			if(mCenterDialog == null){
				mCenterDialog = new Dialog(this);
				setDialogLayoutParams(mCenterDialog,dialogPadding,Gravity.CENTER);
			}
			mCenterDialog.setContentView(mCenterDialogView,new LayoutParams(diaplayWidth-dialogPadding,LayoutParams.WRAP_CONTENT));
			showDialog(id);
		}else if(id == AbConstant.DIALOGTOP){
			mTopDialogView = view;
			if(mTopDialog == null){
				mTopDialog = new Dialog(this);
				setDialogLayoutParams(mTopDialog,dialogPadding,Gravity.TOP);
			}
			mTopDialog.setContentView(mTopDialogView,new LayoutParams(diaplayWidth-dialogPadding,LayoutParams.WRAP_CONTENT));
			showDialog(id);
		}else{
			Log.i(TAG,"Dialog��ID�����ˣ���ο�AbConstant�ඨ��");
		}
	}
	
	/**
	 * �������Ի���dialog ��ȷ�ϣ�ȡ����.
	 * @param title �Ի����������
	 * @param msg  �Ի�����ʾ����
	 * @param mOkOnClickListener  ���ȷ�ϰ�ť���¼�����
	 */
	public void showDialog(String title,String msg,DialogInterface.OnClickListener mOkOnClickListener) {
		 AlertDialog.Builder builder = new Builder(this);
		 builder.setMessage(msg);
		 builder.setTitle(title);
		 builder.setPositiveButton("ȷ��",mOkOnClickListener);
		 builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				   dialog.dismiss();
			   }
		 });
		 builder.create().show();
	}
	
	/**
	 * �������Ի���dialog ��ȷ�ϣ�ȡ����.
	 * @param title �Ի����������
	 * @param view  �Ի�����ʾ����
	 * @param mOkOnClickListener  ���ȷ�ϰ�ť���¼�����
	 */
	public void showDialog(String title,View view,DialogInterface.OnClickListener mOkOnClickListener) {
		 AlertDialog.Builder builder = new Builder(this);
		 builder.setTitle(title);
		 builder.setView(view);
		 builder.setPositiveButton("ȷ��",mOkOnClickListener);
		 builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				   dialog.dismiss();
			   }
		 });
		 builder.create().show();
	}
	
	/**
	 * �������Ի���dialog ���ް�ť��.
	 * @param title �Ի����������
	 * @param msg  �Ի�����ʾ����
	 */
	public void showDialog(String title,String msg) {
		 AlertDialog.Builder builder = new Builder(this);
		 builder.setMessage(msg);
		 builder.setTitle(title);
		 builder.create().show();
	}
	
	/**
	 * �������Ի���dialog ���ް�ť��.
	 * @param title �Ի����������
	 * @param view  �Ի�����ʾ����
	 */
	public void showDialog(String title,View view) {
		 AlertDialog.Builder builder = new Builder(this);
		 builder.setTitle(title);
		 builder.setView(view);
		 builder.create().show();
	}
	
	/**
	 * ���������õ���Dialog������.
	 *
	 * @param dialog  ����Dialog
	 * @param dialogPadding ���Dialog���ǳ�����Ļ��Ҫ�������ֵ
	 * @param gravity the gravity
	 */
	private void setDialogLayoutParams(Dialog dialog,int dialogPadding,int gravity){
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		//�˴���������dialog��ʾ��λ��
		window.setGravity(gravity); 
		//���ÿ��
		lp.width = diaplayWidth-dialogPadding; 
		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
		//����͸��
		//lp.screenBrightness = 0.2f;
		lp.alpha = 0.8f;
		lp.dimAmount = 0f;
		window.setAttributes(lp); 
		// ��Ӷ���
		window.setWindowAnimations(android.R.style.Animation_Dialog); 
		// ���õ����ĻDialog����ʧ    
		dialog.setCanceledOnTouchOutside(false);

	}
	
	/**
	 * �������Ի����ʼ��.
	 * @param id the id
	 * @return the dialog
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		closeMonitor();
		Dialog dialog = null;
		switch (id) {
			case AbConstant.DIALOGPROGRESS:
				if (mProgressDialog == null) {
					Log.i(TAG,"Dialog�������ô���,�����showProgressDialog()!");
				}
				return mProgressDialog;
			case AbConstant.DIALOGBOTTOM:
				if (mBottomDialog == null) {
					Log.i(TAG,"Dialog�������ô���,�����showDialog(int id,View view)!");
				}
				return mBottomDialog;
			case AbConstant.DIALOGCENTER:
				if (mCenterDialog == null) {
					Log.i(TAG,"Dialog�������ô���,�����showDialog(int id,View view)!");
				}
				return mCenterDialog;
			case AbConstant.DIALOGTOP:
				if (mTopDialog == null) {
					Log.i(TAG,"Dialog�������ô���,�����showDialog(int id,View view)!");
				}
				return mTopDialog;
			default:
				break;
		}
		return dialog;
	}
	
	/**
	 * �������Ƴ����ȿ�.
	 */
	public void removeProgressDialog() {
		removeDialog(0);
    }
	
	/**
	 * �������Ƴ�Dialog.
	 * @param id the id
	 * @see android.app.Activity#removeDialog(int)
	 */
	public void removeDialogInThread(int id){
		baseHandler.sendEmptyMessage(id);
	}
	
	/**
	 * Gets the title layout.
	 * @return the title layout
	 */
	public AbTitleBar getTitleBar() {
		return mAbTitlebar;
	}
	
	/**
	 * �������򿪹ر�֡������
	 * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
	 * lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
	 *
	 */
	public void openMonitor(){
		if(!AbAppData.mMonitorOpened) {
			if(mAbMonitorView == null){
				mAbMonitorView  = new AbMonitorView(this);
				mMonitorParams = new WindowManager.LayoutParams();
		        // ����window type
				mMonitorParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		        /*
		         * �������Ϊparams.type = WindowManager.LayoutParams.TYPE_PHONE;
		         * ��ô���ȼ��ή��һЩ, ������֪ͨ�����ɼ�
		         */
				//����ͼƬ��ʽ��Ч��Ϊ����͸��
				mMonitorParams.format = PixelFormat.RGBA_8888; 
		        // ����Window flag
				mMonitorParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
		                              | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		        /*
		         * �����flags���Ե�Ч����ͬ����������
		         * ���������ɴ������������κ��¼�,ͬʱ��Ӱ�������¼���Ӧ��
		        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
		                               | LayoutParams.FLAG_NOT_FOCUSABLE
		                               | LayoutParams.FLAG_NOT_TOUCHABLE;
		         */
		        // �����������ĳ��ÿ�
				mMonitorParams.width = 60;
				mMonitorParams.height = 30;
			}
	        mWindowManager.addView(mAbMonitorView, mMonitorParams);
	        AbAppData.mMonitorOpened = true;
			mMonitorRunnable = new Runnable() {
	
				@Override
				public void run() {
					mAbMonitorView.postInvalidate();
					mMonitorHandler.postDelayed(this,0);
				}
			};
			mMonitorHandler.postDelayed(mMonitorRunnable,0);
			
			mAbMonitorView.setOnTouchListener(new OnTouchListener() {
	        	int lastX, lastY;
	        	int paramX, paramY;
	        	
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						lastX = (int) event.getRawX();
						lastY = (int) event.getRawY();
						paramX = mMonitorParams.x;
						paramY = mMonitorParams.y;
						break;
					case MotionEvent.ACTION_MOVE:
						int dx = (int) event.getRawX() - lastX;
						int dy = (int) event.getRawY() - lastY;
						if ((paramX + dx) > diaplayWidth/2 ) {
							mMonitorParams.x = diaplayWidth;
						}else {
							mMonitorParams.x = 0;
						}
	                    mMonitorParams.x = paramX + dx;
						mMonitorParams.y = paramY + dy;
						// ����������λ��
						mWindowManager.updateViewLayout(mAbMonitorView, mMonitorParams);
						break;
					}
					return true;
				}
			});
			
		}
		
	}
	
	/**
	 * �������ر�֡������.
	 */
	public void closeMonitor(){
		if(AbAppData.mMonitorOpened) {
			if(mAbMonitorView!=null){
				mWindowManager.removeView(mAbMonitorView);
			}
			AbAppData.mMonitorOpened = false;
			if(mMonitorHandler!=null  && mMonitorRunnable!=null){
			    mMonitorHandler.removeCallbacks(mMonitorRunnable);
			}
		}
		
	}
	
    /**
     * ���������õ�����Dialog�����ұ߾�.
     *
     * @param dialogPadding  ���Dialog���ǳ�����Ļ��Ҫ�������ֵ
     */
	public void setDialogPadding(int dialogPadding) {
		this.dialogPadding = dialogPadding;
	}
	
	
	/**
	 * ��������ȡ���ȿ���ʾ������.
	 *
	 * @return the progress message
	 */
	public String getProgressMessage() {
		return mProgressMessage;
	}

	/**
	 * ���������ý��ȿ���ʾ������.
	 *
	 * @param message the new progress message
	 */
	public void setProgressMessage(String message) {
		this.mProgressMessage = message;
	}

	/**
	 * ����������.
	 *
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	/**
	 * ������Activity����.
	 *
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		closeMonitor();
		super.finish();
	}

	/**
	 * Gets the bottom dialog.
	 *
	 * @return the bottom dialog
	 */
	public Dialog getBottomDialog() {
		return mBottomDialog;
	}


	/**
	 * Gets the center dialog.
	 *
	 * @return the center dialog
	 */
	public Dialog getCenterDialog() {
		return mCenterDialog;
	}


	/**
	 * Gets the top dialog.
	 *
	 * @return the top dialog
	 */
	public Dialog getTopDialog() {
		return mTopDialog;
	}
	


	/**
	 * ���������ý�����ʾ�����Ա�������
	 * @see android.app.Activity#setContentView(int)
	 */
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initIocView();
	}

	/**
	 * ���������ý�����ʾ�����Ա�������
	 * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
	 */
	@Override
	public void setContentView(View view,
			android.view.ViewGroup.LayoutParams params) {
		super.setContentView(view, params);
		initIocView();
	}

	/**
	 * ���������ý�����ʾ�����Ա�������
	 * @see android.app.Activity#setContentView(android.view.View)
	 */
	public void setContentView(View view) {
		super.setContentView(view);
		initIocView();
	}
	
	/**
	 * ��ʼ��ΪIOC���Ƶ�View.
	 */
	private void initIocView(){
		Field[] fields = getClass().getDeclaredFields();
		if(fields!=null && fields.length>0){
			for(Field field : fields){
				try {
					field.setAccessible(true);
					
					if(field.get(this)!= null )
						continue;
				
					AbIocView viewInject = field.getAnnotation(AbIocView.class);
					if(viewInject!=null){
						
						int viewId = viewInject.id();
					    field.set(this,findViewById(viewId));
					
						setListener(field,viewInject.click(),Method.Click);
						setListener(field,viewInject.longClick(),Method.LongClick);
						setListener(field,viewInject.itemClick(),Method.ItemClick);
						setListener(field,viewInject.itemLongClick(),Method.itemLongClick);
						
						AbIocSelect select = viewInject.select();
						if(!TextUtils.isEmpty(select.selected())){
							setViewSelectListener(field,select.selected(),select.noSelected());
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ����view�ļ�����.
	 *
	 * @param field the field
	 * @param select the select
	 * @param noSelect the no select
	 * @throws Exception the exception
	 */
	private void setViewSelectListener(Field field,String select,String noSelect)throws Exception{
		Object obj = field.get(this);
		if(obj instanceof View){
			((AbsListView)obj).setOnItemSelectedListener(new AbIocEventListener(this).select(select).noSelect(noSelect));
		}
	}
	
	/**
	 * ����view�ļ�����.
	 *
	 * @param field the field
	 * @param methodName the method name
	 * @param method the method
	 * @throws Exception the exception
	 */
	private void setListener(Field field,String methodName,Method method)throws Exception{
		if(methodName == null || methodName.trim().length() == 0)
			return;
		
		Object obj = field.get(this);
		
		switch (method) {
			case Click:
				if(obj instanceof View){
					((View)obj).setOnClickListener(new AbIocEventListener(this).click(methodName));
				}
				break;
			case ItemClick:
				if(obj instanceof AbsListView){
					((AbsListView)obj).setOnItemClickListener(new AbIocEventListener(this).itemClick(methodName));
				}
				break;
			case LongClick:
				if(obj instanceof View){
					((View)obj).setOnLongClickListener(new AbIocEventListener(this).longClick(methodName));
				}
				break;
			case itemLongClick:
				if(obj instanceof AbsListView){
					((AbsListView)obj).setOnItemLongClickListener(new AbIocEventListener(this).itemLongClick(methodName));
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * The Enum Method.
	 */
	public enum Method{
		
		/** The Click. */
		Click,
		/** The Long click. */
		LongClick,
		/** The Item click. */
		ItemClick,
		/** The item long click. */
		itemLongClick
	}

	
}
