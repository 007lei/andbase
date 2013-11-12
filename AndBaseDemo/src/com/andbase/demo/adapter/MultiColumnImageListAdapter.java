package com.andbase.demo.adapter;




import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;
import com.ab.view.sample.AbScaleImageView;
import com.andbase.R;
import com.andbase.demo.model.ImageInfo;
import com.andbase.global.Constant;
/**
 * Copyright (c) 2011 All rights reserved
 * ���ƣ�MyListViewAdapter
 * ��������Adapter���ͷ�Bitmap
 * @author zhaoqp
 * @date 2011-12-10
 * @version
 */
public class MultiColumnImageListAdapter extends BaseAdapter{
	
	private static String TAG = "MultiColumnImageListAdapter";
	private static final boolean D = Constant.DEBUG;
  
	private Context mContext;
    //�б�չ�ֵ�����
    private LinkedList<ImageInfo> mImageList;
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
    public MultiColumnImageListAdapter(Context context, LinkedList<ImageInfo> imageList){
    	this.mContext = context;
    	this.mImageList = imageList;
    	//ͼƬ������
        mAbImageDownloader = new AbImageDownloader(mContext);
        mAbImageDownloader.setType(AbConstant.SCALEIMG);
	    //mAbImageDownloader.setAnimation(true);
    }   
    
    @Override
    public int getCount() {
        return mImageList.size();
    }
    
    @Override
    public Object getItem(int position) {
        return mImageList.get(position);
    }

    @Override
    public long getItemId(int position){
      return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
    	  final ViewHolder holder;
          if(convertView == null){
	           //ʹ���Զ����list_items��ΪLayout
        	   LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
	           convertView = layoutInflator.inflate(R.layout.multi_list_items, null);
	           //����findView�Ĵ���
			   holder = new ViewHolder();
	           //��ʼ�������е�Ԫ��
			   holder.itemsIcon = ((AbScaleImageView) convertView.findViewById(R.id.itemsIcon)) ;
			   convertView.setTag(holder);
			   
			      
          }else{
        	   holder = (ViewHolder) convertView.getTag();
          }
          
          //��ȡ���е�����
          final ImageInfo  mImageInfo = mImageList.get(position);
          holder.itemsIcon.setImageWidth(mImageInfo.getWidth());
		  holder.itemsIcon.setImageHeight(mImageInfo.getHeight());
		  //ͼƬ���ص������
		  mAbImageDownloader.setWidth(mImageInfo.getWidth());
		  mAbImageDownloader.setHeight(mImageInfo.getHeight());
		  String url = mImageInfo.getUrl();
		  
		  //���ü����е�View
          mAbImageDownloader.setLoadingView(convertView.findViewById(R.id.progressBar));
          mAbImageDownloader.display(holder.itemsIcon,url);
          
          return convertView;
    }
    
    /**
     * 
     * ���������ײ����
     * @param datas
     * @throws 
     */
    public void addItemLast(List<ImageInfo> datas) {
    	mImageList.addAll(datas);
	}

    /**
     * 
     * ���������������
     * @param datas
     * @throws 
     */
	public void addItemTop(List<ImageInfo> datas) {
		for (ImageInfo imageInfo : datas) {
			mImageList.addFirst(imageInfo);
		}
	}
    
    /**
	 * ViewԪ��
	 */
	static class ViewHolder {
		AbScaleImageView itemsIcon;
	}
    
}