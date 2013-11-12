package com.andbase.demo.adapter;


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


import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ab.bitmap.AbImageCache;
import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;
import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
import com.andbase.R;

// TODO: Auto-generated Javadoc
/**
 * ������ ����URL��ͼƬ.
 */
public class ImageShowAdapter extends BaseAdapter {
	
	/** The m context. */
	private Context mContext;
	
	/** The m image paths. */
	private List<String> mImagePaths = null;
	
	/** The m width. */
	private int mWidth;
	
	/** The m height. */
	private int mHeight;
	
	//ͼƬ������
    private AbImageDownloader mAbImageDownloader = null;
	
	/**
	 * Instantiates a new ab image show adapter.
	 * @param context the context
	 * @param imagePaths the image paths
	 * @param width the width
	 * @param height the height
	 */
	public ImageShowAdapter(Context context,List<String> imagePaths,int width,int height) {
		mContext = context;
		this.mImagePaths = imagePaths;
		this.mWidth = width;
		this.mHeight = height;
		//ͼƬ������
        mAbImageDownloader = new AbImageDownloader(mContext);
        mAbImageDownloader.setWidth(this.mWidth);
        mAbImageDownloader.setHeight(this.mHeight);
        mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
        mAbImageDownloader.setErrorImage(R.drawable.image_error);
        mAbImageDownloader.setNoImage(R.drawable.image_no);
	}
	
	/**
	 * ��������ȡ����.
	 *
	 * @return the count
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return mImagePaths.size();
	}

	/**
	 * ��������ȡ����λ�õ�·��.
	 *
	 * @param position the position
	 * @return the item
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return mImagePaths.get(position);
	}

	/**
	 * ��������ȡλ��.
	 *
	 * @param position the position
	 * @return the item id
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ��������ʾView.
	 *
	 * @param position the position
	 * @param convertView the convert view
	 * @param parent the parent
	 * @return the view
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			LinearLayout mLinearLayout = new LinearLayout(mContext);
			RelativeLayout mRelativeLayout = new RelativeLayout(mContext);
			ImageView mImageView1 = new ImageView(mContext);
			mImageView1.setScaleType(ScaleType.FIT_CENTER);
			ImageView mImageView2 = new ImageView(mContext);
			mImageView2.setScaleType(ScaleType.FIT_CENTER);
			holder.mImageView1 = mImageView1;
			holder.mImageView2 = mImageView2;
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
			lp1.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
			RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(mWidth,mHeight);
	        lp2.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
	        lp2.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
			mRelativeLayout.addView(mImageView1,lp2);
			mRelativeLayout.addView(mImageView2,lp2);
			mLinearLayout.addView(mRelativeLayout,lp1);
			
			convertView = mLinearLayout;
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.mImageView1.setImageBitmap(null);
		holder.mImageView2.setBackgroundDrawable(null);
		
		String imagePath = mImagePaths.get(position);
		
		if(!AbStrUtil.isEmpty(imagePath)){
		  //�ӻ����л�ȡͼƬ������Ҫ����ᵼ��ҳ������
      	  Bitmap bitmap = AbImageCache.getBitmapFromMemCache(imagePath);
      	  //������û����������SD����ȡ
      	  if(bitmap == null){
      		    holder.mImageView1.setImageResource(R.drawable.image_loading);
	      		if(imagePath.indexOf("http://")!=-1){
	      		    //ͼƬ������
	                mAbImageDownloader.setType(AbConstant.ORIGINALIMG);
	                mAbImageDownloader.display(holder.mImageView1,imagePath);
					
				}else if(imagePath.indexOf("/")==-1){
					//����ͼƬ
					try {
						int res  = Integer.parseInt(imagePath);
						holder.mImageView1.setImageDrawable(mContext.getResources().getDrawable(res));
					} catch (Exception e) {
						holder.mImageView1.setImageResource(R.drawable.image_error);
					}
				}else{
					Bitmap mBitmap = AbFileUtil.getBitmapFromSD(new File(imagePath), AbConstant.SCALEIMG, mWidth, mHeight);
					if(mBitmap!=null){
						holder.mImageView1.setImageBitmap(mBitmap);
					}else{
						// ��ͼƬʱ��ʾ
						holder.mImageView1.setImageResource(R.drawable.image_no);
					}
				}
      	  }else{
      		  //ֱ����ʾ
  			  holder.mImageView1.setImageBitmap(bitmap);
      	  }
		}else{
			// ��ͼƬʱ��ʾ
			holder.mImageView1.setImageResource(R.drawable.image_no);
	    }
		holder.mImageView1.setAdjustViewBounds(true);
	    return convertView;
	}
	
	
	/**
	 * ���Ӳ��ı���ͼ.
	 * @param position the position
	 * @param imagePaths the image paths
	 */
	public void addItem(int position,String imagePaths) {
		mImagePaths.add(position,imagePaths);
		notifyDataSetChanged();
	}
	
	/**
	 * ���Ӷ������ı���ͼ.
	 * @param imagePaths the image paths
	 */
	public void addItems(List<String> imagePaths) {
		mImagePaths.addAll(imagePaths);
		notifyDataSetChanged();
	}
	
	/**
	 * ���Ӷ������ı���ͼ.
	 */
	public void clearItems() {
		mImagePaths.clear();
		notifyDataSetChanged();
	}
	
	/**
	 * ViewԪ��.
	 */
	public static class ViewHolder {
		
		/** The m image view1. */
		public ImageView mImageView1;
		
		/** The m image view2. */
		public ImageView mImageView2;
	}

}
