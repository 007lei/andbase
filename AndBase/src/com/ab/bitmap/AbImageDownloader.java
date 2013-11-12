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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ab.global.AbAppData;
import com.ab.global.AbConstant;
import com.ab.util.AbImageUtil;
import com.ab.util.AbMd5;
import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc
/**
 * ����������ͼƬ����ʾ�Ĺ�����.
 *
 * @author zhaoqp
 * @date 2011-12-10
 * @version v1.0
 */
public class AbImageDownloader { 
	
	/** The tag. */
	private static String TAG = "AbImageDownloader";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
    /** Context. */
    private static Context context = null;
    
    /** ��ʾ��ͼƬ�Ŀ�. */
    private int width;
	
	/** ��ʾ��ͼƬ�ĸ�. */
    private int height;
	
	/** ͼƬ�Ĵ������ͣ����л������ŵ�ָ����С���ο�AbConstant�ࣩ. */
    private int type  = AbConstant.ORIGINALIMG;
    
    /** ��ʾΪ�����е�ͼƬ. */
    private Drawable loadingImage;
    
    /** ��ʾΪ�����е�View. */
    private View loadingView;
    
    /** ��ʾ����ʧ�ܵ�ͼƬ. */
    private Drawable errorImage;
    
    /** ͼƬδ�ҵ���ͼƬ. */
    private Drawable noImage;
    
    /** �̳߳�. */
    private AbImageDownloadPool mAbImageDownloadPool = null;
    
    /** ��������. */
    private boolean animation;
    
    /**
     * ����ͼƬ������.
     */
    public AbImageDownloader(Context context) {
    	this.context = context;
    	this.mAbImageDownloadPool = AbImageDownloadPool.getInstance();
    } 
     
    /**
     * ��ʾ���ͼƬ.
     * ���붯��Ч���������һҳ��ͼƬ��ȫ����һ�£���Ϊ�����˲�ͬ��ͼƬ
     * @param imageView �Եõ�View
     * @return url ����url
     */
    public void display(final ImageView imageView,String url) { 
    	
    	if(AbStrUtil.isEmpty(url)){
    		if(noImage != null){
    			if(loadingView != null){
        			loadingView.setVisibility(View.INVISIBLE);
					imageView.setVisibility(View.VISIBLE);
        		}
    			imageView.setImageDrawable(noImage);
    		}
    		return;
    	}
    	
    	//����������
        AbImageDownloadItem item = new AbImageDownloadItem(); 
        //������ʾ�Ĵ�С
        item.width = width;
        item.height = height;
        //����Ϊ����
        item.type = type;
        item.imageUrl = url;
        String cacheKey = AbImageCache.getCacheKey(item.imageUrl, item.width, item.height, item.type);
        item.bitmap =  AbImageCache.getBitmapFromMemCache(cacheKey);
		if(D) Log.d(TAG, "�����л�ȡ��"+cacheKey+":"+item.bitmap);
		
    	if(item.bitmap == null){
    		
    		//����ʾ������
        	if(loadingView!=null){
    			loadingView.setVisibility(View.VISIBLE);
    			imageView.setVisibility(View.INVISIBLE);
    		}else if(loadingImage != null){
    			if(animation){
    				TransitionDrawable td = AbImageUtil.drawableToTransitionDrawable(loadingImage);
	        		imageView.setImageDrawable(td);
	    			td.startTransition(200);
    			}else{
    				imageView.setImageDrawable(loadingImage);
    			}
    		}
    		
    		//������ɺ���½���
            item.callback = new AbImageDownloadCallback() { 
                @Override 
                public void update(Bitmap bitmap, String imageUrl) { 
                	//δ���ü����е�ͼƬ���������������ص�View
            		if(loadingView != null){
            			loadingView.setVisibility(View.INVISIBLE);
						imageView.setVisibility(View.VISIBLE);
            		}
                	if(bitmap!=null){
                		if(animation){
	                		TransitionDrawable td = AbImageUtil.bitmapToTransitionDrawable(bitmap);
	                		imageView.setImageDrawable(td);
	            			td.startTransition(200);
                		}else{
                			imageView.setImageBitmap(bitmap);
                		}
                	}else{
                		if(errorImage != null){
                			if(animation){
	                			TransitionDrawable td = AbImageUtil.drawableToTransitionDrawable(errorImage);
	                    		imageView.setImageDrawable(td);
	                			td.startTransition(200);
                			}else{
                				imageView.setImageDrawable(errorImage);
                			}
                		}
                		
                	}
                } 
            }; 
            
            mAbImageDownloadPool.download(item);
    	}else{
    		if(loadingView != null){
    		    loadingView.setVisibility(View.INVISIBLE);
    		    imageView.setVisibility(View.VISIBLE);
    		}
    		imageView.setImageBitmap(item.bitmap);
    	}
        
    } 
    
    /**
	 * 
	 * ���������������е�ͼƬ
	 * @param resID
	 * @throws 
	 */
	public void setLoadingImage(int resID) {
		this.loadingImage = context.getResources().getDrawable(resID);
	}
	
	/**
	 * 
	 * ���������������е�View�����ȼ�����setLoadingImage
	 * @param view ����ImageView���ϱ߻����±ߵ�View
	 * @throws 
	 */
	public void setLoadingView(View view) {
		this.loadingView = view;
	}

	/**
	 * 
	 * ��������������ʧ�ܵ�ͼƬ
	 * @param resID
	 * @throws 
	 */
	public void setErrorImage(int resID) {
		this.errorImage = context.getResources().getDrawable(resID);
	}

	/**
	 * 
	 * ����������δ�ҵ���ͼƬ
	 * @param resID
	 * @throws 
	 */
	public void setNoImage(int resID) {
		this.noImage = context.getResources().getDrawable(resID);
	}

	public int getWidth() {
		return width;
	}

	/**
	 * 
	 * ����������ͼƬ�Ŀ��
	 * @param height
	 * @throws 
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * 
	 * ����������ͼƬ�ĸ߶�
	 * @param height
	 * @throws 
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	
	
	public int getType() {
		return type;
	}

	/**
	 * 
	 * ������ͼƬ�Ĵ������ͣ����л������ŵ�ָ����С���ο�AbConstant�ࣩ.
	 * @param type
	 * @throws 
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 
	 * �������Ƿ�������.
	 * @param animation
	 * @throws 
	 */
	public void setAnimation(boolean animation) {
		this.animation = animation;
	}
	
	
    
}

