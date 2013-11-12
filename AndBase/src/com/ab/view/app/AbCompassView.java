package com.ab.view.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�AbCompassView.java 
 * ������ָ����
 * @author zhaoqp
 * @date��2013-8-23 ����2:03:29
 * @version v1.0
 */
public class AbCompassView extends View {
    private Drawable mCompassDrawable = null;
    private int w = 40;
    private int h = 40;
    private float mDirection = 0.0f;
    private float posCompassX = 20;
    private float posCompassY = 20;
	
    public AbCompassView(Context context) {
        super(context);
        
    }

	public AbCompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override 
    protected void onDraw(Canvas canvas) {
        w = canvas.getWidth();
        h = canvas.getHeight();
        posCompassX = w/2;
        posCompassY = h/2;
        drawPictures(canvas);
    }
	
	private void drawPictures(Canvas canvas) {
		if (mCompassDrawable != null) {
			// ͼƬ��Դ��view��λ�ã��˴��൱�ڳ���view
			mCompassDrawable.setBounds(0, 0, w, h);
			canvas.save();
			// ��ͼƬ���ĵ���ת
			canvas.rotate(mDirection, posCompassX, posCompassY);
			// ����ת���ͼƬ����view�ϣ���������ת�������
			mCompassDrawable.draw(canvas);
			// ����һ��
			canvas.restore();
		}

    }

	@Override  
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){   
	    int height = View.MeasureSpec.getSize(heightMeasureSpec);    
	    int width = View.MeasureSpec.getSize(widthMeasureSpec);    
	    setMeasuredDimension(width,height);
	}  


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

	public float getDirection() {
		return mDirection;
	}

	public void setDirection(float direction) {
		this.mDirection = direction;
		this.invalidate();
	}

	/**
	 * 
	 * ����������ָ����ͼƬ
	 * @param drawable �������ò�����ʾ
	 * @throws 
	 */
	public void setCompassDrawable(Drawable drawable) {
		this.mCompassDrawable = drawable;
	}
	
    
}