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
package com.ab.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.ab.task.AbTaskCallback;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskPool;
import com.ab.util.AbStrUtil;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�AbHttpUtil.java 
 * ������Httpִ�й����࣬�ɴ���get��post���Լ��첽�����ļ����ϴ�����
 * @author zhaoqp
 * @date��2013-10-22 ����4:15:52
 * @version v1.0
 */
public class AbHttpUtil {

	/**ʵ��������*/
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	/**��ʱʱ��*/
	private static int timeout = 5000;
	
	static{
		client.setTimeout(timeout);
	}
	
	/**
	 * ��������ȡAsyncHttpClient.
	 *
	 * @return the http client
	 */
	public static AsyncHttpClient getHttpClient() {
		return client;
	}
	

	public static int getTimeout() {
		return timeout;
	}

	/**
	 * 
	 * �������������ӳ�ʱ����������ã�Ĭ��Ϊ5s
	 * @param timeout
	 * @throws 
	 */
	public static void setTimeout(int timeout) {
		AbHttpUtil.timeout = timeout;
		client.setTimeout(AbHttpUtil.timeout);
	}


	/**
	 * 
	 * �������޲�����get����
	 * @param url
	 * @param responseHandler
	 * @throws 
	 */
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	/**
	 * 
	 * ��������������get����
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}
	
	/**
	 *  
	 * ��������������ʹ�ã��᷵��byte����(�����ļ���ͼƬ)
	 * @param url
	 * @param responseHandler
	 * @throws 
	 */
	public static void get(String url, BinaryHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}
	
	/**
	 * 
	 * �������ļ����ص�get
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public static void get(String url, RequestParams params,
			FileAsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}
	
	
	/**
	 * 
	 * �������޲�����post����
	 * @param url
	 * @param responseHandler
	 * @throws 
	 */
	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		client.post(url, responseHandler);
	}
	
	
	/**
	 * 
	 * ��������������post����
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}
	
	
	
	/**
	 * 
	 * �������ļ����ص�post
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public static void post(String url, RequestParams params,
			FileAsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}
	
	
	/**
	 * HTTPһ��/����ļ��ϴ�.
	 *
	 * @param url Ҫʹ�õ�URL
	 * @param params ������
	 * @param files Ҫ�ϴ����ļ��б�key:�ļ�����value��File����
	 */
	public static void post(final String url, final HashMap<String, String> params,
			final HashMap<String, File> files,final AsyncHttpResponseHandler responseHandler) {
		
		AbTaskItem item = new AbTaskItem();
		item.callback = new AbTaskCallback() {
			
			@Override
			public void update() {
				//���������Ϣ
				if (responseHandler != null) {
					 responseHandler.sendFinishMessage();
				}
			}
			
			@Override
			public void get() {
				//��ʶÿ���ļ��ı߽�
				String BOUNDARY = java.util.UUID.randomUUID().toString();
				String PREFIX = "--";
				String LINEND = "\r\n";
				String MULTIPART_FROM_DATA = "multipart/form-data";
				String CHARSET = "UTF-8";
				HttpURLConnection conn = null;
				DataOutputStream outStream = null;
				try {
					//���Ϳ�ʼִ��
					if (responseHandler != null) {
					   responseHandler.sendStartMessage();
					}
					URL uri = new URL(url);
					conn = (HttpURLConnection) uri.openConnection();
					//��������
					conn.setDoInput(true);
					//�������
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					// Post��ʽ
					conn.setRequestMethod("POST");
					//����request header ����
					conn.setRequestProperty("connection", "keep-alive");
					conn.setRequestProperty("Charsert", "UTF-8");
					conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
							+ ";boundary=" + BOUNDARY);
					//��װ����������
					StringBuilder sb = new StringBuilder();
					for (Map.Entry<String, String> entry : params.entrySet()) {
						sb.append(PREFIX);
						sb.append(BOUNDARY);
						sb.append(LINEND);
						sb.append("Content-Disposition: form-data; name=\""
								+ entry.getKey() + "\"" + LINEND);
						sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
						sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
						sb.append(LINEND);
						sb.append(entry.getValue());
						sb.append(LINEND);
					}
					//��ȡ���ӷ��Ͳ�������
					outStream = new DataOutputStream(conn.getOutputStream());
					outStream.write(sb.toString().getBytes());
					// �����ļ�����
					if (files != null)
						for (Map.Entry<String, File> file : files.entrySet()) {
							StringBuilder sb1 = new StringBuilder();
							sb1.append(PREFIX);
							sb1.append(BOUNDARY);
							sb1.append(LINEND);
							sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
									+ file.getKey() + "\"" + LINEND);
							sb1.append("Content-Type: application/octet-stream; charset="
									+ CHARSET + LINEND);
							sb1.append(LINEND);
							//����ͷ����������һ�����У���������\r\n����ʾ����ͷ������
							Log.d("TAG", "request start:"+sb1.toString());
							outStream.write(sb1.toString().getBytes());
							InputStream is = new FileInputStream(file.getValue());
							byte[] buffer = new byte[1024];
							int len = 0;
							while ((len = is.read(buffer)) != -1) {
								outStream.write(buffer, 0, len);
							}
							is.close();
							//һ���ļ�����һ������
							outStream.write(LINEND.getBytes());
						}
						//��������ı߽��ӡ
						byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
						Log.d("TAG","request end:"+ new String(end_data).toString());
						outStream.write(end_data);
						outStream.flush();
						outStream.close();
						// ��ȡ��Ӧ��
						int statusCode = conn.getResponseCode();
						String result = AbStrUtil.convertStreamToString(conn.getInputStream());
						//���ͽ����Ϣ
						if (responseHandler != null) {
							if(statusCode == 200){
								responseHandler.sendSuccessMessage(statusCode, null, result.getBytes());
							}else{
								responseHandler.sendFailureMessage(statusCode, null, result.getBytes(), null);
							}
						}
						
				} catch (Exception e) {
					e.printStackTrace();
					//����ʧ����Ϣ
					if (responseHandler != null) {
		                responseHandler.sendFailureMessage(0, null, null, e);
		            }
				} finally{
					if(conn!=null){
						conn.disconnect();
					}
					
				}
			}
		};

		//����������̵߳���
		AbTaskPool mAbTaskPool = AbTaskPool.getInstance();
		mAbTaskPool.execute(item);
		
	}

	
}
