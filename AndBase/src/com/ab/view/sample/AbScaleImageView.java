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
package com.ab.view.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�AbScaleImageView.java 
 * ���������ݿ�Ȼ�߶�����ͼ��ߴ磬���δ����ȡ����ͼ��ߴ�
 * @author zhaoqp
 * @date��2013-9-3 ����4:09:16
 * @version v1.0
 */
public class AbScaleImageView extends ImageView {
    
    /** ��ǰ��bitmap. */
    private Bitmap currentBitmap;
    
    /** The image width. */
    private int imageWidth;
    
    /** The image height. */
    private int imageHeight;

    /**
     * ���췽��.
     *
     * @param context the context
     */
   public AbScaleImageView(Context context) {
        super(context);
        init();
    }

    /**
     * ���췽��.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public AbScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * ���췽��.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public AbScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * ��ʼ��ͼƬ��������.
     */
    private void init() {
        this.setScaleType(ScaleType.CENTER_INSIDE);
    }

    /**
     * ����.
     */
    public void recycle() {
        setImageBitmap(null);
        if ((this.currentBitmap == null) || (this.currentBitmap.isRecycled()))
            return;
        this.currentBitmap.recycle();
        this.currentBitmap = null;
    }

    /**
     * ����������ͼƬBitmap
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        currentBitmap = bm;
        super.setImageBitmap(currentBitmap);
    }

    /**
     * ����������ͼƬDrawable
     */
    @Override
    public void setImageDrawable(Drawable d) {
        super.setImageDrawable(d);
    }

    /**
     * ����������ͼƬ��Դ
     */
    @Override
    public void setImageResource(int id) {
        super.setImageResource(id);
    }

    
    /**
     * ������onMeasure
     * @see android.widget.ImageView#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (imageWidth == 0) {
            // ��ͼƬ��С
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        } else {
        	this.setScaleType(ScaleType.CENTER_CROP);
            setMeasuredDimension(imageWidth, imageHeight);
        }
    }
    
    /**
     * ����View�Ŀ��.
     *
     * @param w the new image width
     */
    public void setImageWidth(int w) {
        imageWidth = w;
    }

    /**
     * ����View�ĸ߶�.
     *
     * @param h the new image height
     */
    public void setImageHeight(int h) {
        imageHeight = h;
    }


}
