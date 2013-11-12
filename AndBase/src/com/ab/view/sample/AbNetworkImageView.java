
package com.ab.view.sample;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.ab.bitmap.AbImageDownloader;

/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�AbNetworkImageView.java 
 * ����������url����ͼƬ������ImageView
 * @author zhaoqp
 * @date��2013-11-12 ����10:09:55
 * @version v1.0
 */
public class AbNetworkImageView extends ImageView {
	
    /** ͼƬ��url */
    private String mUrl;

    /**ͼƬ������*/
    private AbImageDownloader mAbImageDownloader = null;


    public AbNetworkImageView(Context context) {
        this(context, null);
    }

    public AbNetworkImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 
     * ����������ͼƬ��URL
     * @param url
     * @param abImageDownloader
     * @throws 
     */
    public void setImageUrl(String url, AbImageDownloader abImageDownloader) {
        mUrl = url;
        mAbImageDownloader = abImageDownloader;
        loadImageIfNecessary(false);
    }

    /**
     * 
     * ���������δ���ؾͼ���
     * @param isInLayoutPass
     * @throws 
     */
    private void loadImageIfNecessary(final boolean isInLayoutPass) {
        int width = getWidth();
        int height = getHeight();

        boolean isFullyWrapContent = getLayoutParams() != null
                && getLayoutParams().height == LayoutParams.WRAP_CONTENT
                && getLayoutParams().width == LayoutParams.WRAP_CONTENT;
        
        if (width == 0 && height == 0 && !isFullyWrapContent) {
            return;
        }

        if (TextUtils.isEmpty(mUrl)) {
            setImageBitmap(null);
            return;
        }
        
        //ͼƬ������
        mAbImageDownloader.display(this,mUrl);
        
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        loadImageIfNecessary(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }
}
