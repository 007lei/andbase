package com.ab.task;

import android.os.AsyncTask;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�AbAsyncTask.java 
 * �������������ݵ�����ʵ�֣���������
 * @author zhaoqp
 * @date��2013-9-2 ����12:52:13
 * @version v1.0
 */
public class AbAsyncTask extends AsyncTask<AbTaskItem, Integer, AbTaskItem> {
	
	public AbAsyncTask() {
		super();
	}

	@Override
	protected AbTaskItem doInBackground(AbTaskItem... items) {
		AbTaskItem item = items[0];
		if (item.callback != null) { 
			item.callback.get();
        } 
		return item;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(AbTaskItem item) {
		if (item.callback != null) { 
		    item.callback.update(); 
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
