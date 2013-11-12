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

import android.graphics.Bitmap;

// TODO: Auto-generated Javadoc
/**
 * ������ͼƬ���ص�λ.
 *
 * @author zhaoqp
 * @date 2011-12-10
 * @version v1.0
 */
public class AbImageDownloadItem {
	
	/** ��Ҫ���ص�ͼƬ�Ļ�������ַ. */
	public String imageUrl;
	
	/** ��ʾ��ͼƬ�Ŀ�. */
	public int width;
	
	/** ��ʾ��ͼƬ�ĸ�. */
	public int height;
	
	/** ͼƬ�Ĵ������ͣ����л������ŵ�ָ����С���ο�AbConstant�ࣩ. */
	public int type;
	
	/** ������ɵĵ���Bitmap����. */
	public Bitmap bitmap;
	
	/** ������ɵĻص��ӿ�. */
	public AbImageDownloadCallback callback;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public AbImageDownloadCallback getCallback() {
		return callback;
	}

	public void setCallback(AbImageDownloadCallback callback) {
		this.callback = callback;
	}
	
	

}
