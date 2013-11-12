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
import android.support.v4.util.LruCache;

import com.ab.util.AbImageUtil;
import com.ab.util.AbMd5;
import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc
/**
 * ������ͼƬ����.
 *
 * @author zhaoqp
 * @date��2013-5-23 ����10:10:53
 * @version v1.0
 */

public class AbImageCache {
	
	/** 4MB. */
	public static int cacheSize = 10 * 1024 * 1024; 
	
	/** Ϊ�˼ӿ��ٶȣ����ڴ��п�������,���µ�LruCache. */
	private static LruCache<String, Bitmap> bitmapCache = new LruCache<String, Bitmap>(cacheSize) {      
		protected int sizeOf(String key, Bitmap bitmap) { 
			return bitmap.getRowBytes() * bitmap.getHeight();        
	}};
	
	
	/**
	 * �������ӻ����л�ȡ���Bitmap.
	 *
	 * @param key the key
	 * @return the bitmap from mem cache
	 */
	public static Bitmap getBitmapFromMemCache(String key) {  
		  return (Bitmap)bitmapCache.get(key);  
	} 
	
	/**
	 * ����������һ��ͼƬ������.
	 *
	 * @param key  һ��Ϊһ�������ļ���url
	 * @param bitmap the bitmap
	 */
	public static void addBitmapToMemoryCache(String key,Bitmap bitmap){
		if(AbStrUtil.isEmpty(key) || bitmap == null){
			return;
		}
		if (getBitmapFromMemCache(key) == null) {  
			bitmapCache.put(key, bitmap);
		}  
	}
	
	/**
	 * ��������ջ����Bitmap.
	 */
	public static void removeAllBitmapFromCache() { 
		  bitmapCache.evictAll();  
	}
	
	/**
     * ����url���㻺��key.
     * @param url ͼƬ��ַ.
     * @param width ͼƬ���.
     * @param height ͼƬ�߶�.
     * @param type ��������.
     */
    public static String getCacheKey(String url, int width, int height,int type) {
        return AbMd5.MD5(new StringBuilder(url.length() + 12).append("#W").append(width)
        .append("#H").append(height).append("#T").append(type).append(url).toString());
    }
	
}
