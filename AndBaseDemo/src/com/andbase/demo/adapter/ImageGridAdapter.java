package com.andbase.demo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;
import com.andbase.R;
import com.andbase.model.User;
/**
 * Copyright (c) 2011 All rights reserved
 * ���ƣ�OverlayGridAdapter
 * ��������Adapter���ͷ�Bitmap
 * @author zhaoqp
 * @date 2011-12-10
 * @version
 */
public class ImageGridAdapter extends BaseAdapter{
  
	private Context mContext;
	//xmlתView����
    private LayoutInflater mInflater;
    //���еĲ���
    private int mResource;
    //�б�չ�ֵ�����
    private List<User> mData;
    //Map�е�key
    private String[] mFrom;
    //view��id
    private int[] mTo;
    
    //ͼƬ������
    private AbImageDownloader mAbImageDownloader = null;
    
   /**
    * ���췽��
    * @param context
    * @param data �б�չ�ֵ�����
    * @param resource ���еĲ���
    * @param from Map�е�key
    * @param to view��id
    */
    public ImageGridAdapter(Context context, List<User> data,
            int resource, String[] from, int[] to){
    	this.mContext = context;
    	this.mData = data;
    	this.mResource = resource;
    	this.mFrom = from;
    	this.mTo = to;
        //���ڽ�xmlתΪView
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //ͼƬ������
        mAbImageDownloader = new AbImageDownloader(mContext);
        mAbImageDownloader.setWidth(150);
        mAbImageDownloader.setHeight(150);
        mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
        mAbImageDownloader.setErrorImage(R.drawable.image_error);
        mAbImageDownloader.setNoImage(R.drawable.image_no);
        mAbImageDownloader.setType(AbConstant.SCALEIMG);
        //mAbImageDownloader.setAnimation(true);
    } 
    
    @Override
    public int getCount() {
        return mData.size();
    }
    
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position){
      return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
    	  final ViewHolder holder;
          if(convertView == null){
	           convertView = mInflater.inflate(mResource, parent, false);
			   holder = new ViewHolder();
			   holder.itemsIcon = ((ImageView) convertView.findViewById(mTo[0])) ;
			   convertView.setTag(holder);
          }else{
        	   holder = (ViewHolder) convertView.getTag();
          }
          
		  //��ȡ���е�����
          final User mUser = (User)mData.get(position);
          String imageUrl = mUser.getPhotoUrl();
          //���ü����е�View
          mAbImageDownloader.setLoadingView(convertView.findViewById(R.id.progressBar));
          //ͼƬ������
          mAbImageDownloader.display(holder.itemsIcon,imageUrl);
         
          return convertView;
    }
    
    /**
	 * ViewԪ��
	 */
	static class ViewHolder {
		ImageView itemsIcon;
	}
    
}