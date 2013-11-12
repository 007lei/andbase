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
package com.ab.view.calendar;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;

import com.ab.util.AbGraphical;

// TODO: Auto-generated Javadoc
/**
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�CalendarHeader.java 
 * �����������ؼ�ͷ��������
 * @author zhaoqp
 * @date��2013-7-9 ����2:07:52
 * @version v1.0
 */
public class CalendarHeader extends View {
	
	/** The tag. */
	private String TAG = "CalendarHeader";
	
	/** The m paint. */
	private final Paint mPaint; 
	
	/** The rect. */
	private RectF rect = new RectF();
	
	//���ڼ�
	/** The week day. */
	private int weekDay = Calendar.SUNDAY;
	
	//���ڵ�����
	/** The day name. */
	private String[] dayName = new String[10];

	/** The width. */
	private int width = 320;    	
	
	/** The height. */
	private int height = 480;
	
	/** ÿ����Ԫ��Ŀ��. */
	private int cellWidth = 40;
	
	/** ������ɫ. */
	private int defaultTextColor = Color.rgb(86, 86, 86);
	
	/** �ر�������ɫ. */
	private int specialTextColor = Color.rgb(240, 140, 26);
	
	/** �����С. */
	private int defaultTextSize = 25;
	
	/** �����Ƿ�Ӵ�. */
	private boolean defaultTextBold = false;
	
	/** �Ƿ�������ͷ������. */
	private boolean hasBg = false;
	
	/**
	 * ����ͷ.
	 *
	 * @param context the context
	 */
	public CalendarHeader(Context context) {
		this(context, null);
	}
	
	/**
	 * Instantiates a new calendar header.
	 *
	 * @param context the context
	 * @param attributeset the attributeset
	 */
	public CalendarHeader(Context context, AttributeSet attributeset) {
		super(context);
		dayName[Calendar.SUNDAY] = "����";
		dayName[Calendar.MONDAY] = "��һ";
		dayName[Calendar.TUESDAY] = "�ܶ�";
		dayName[Calendar.WEDNESDAY] = "����";
		dayName[Calendar.THURSDAY] = "����";
		dayName[Calendar.FRIDAY] = "����";
		dayName[Calendar.SATURDAY] = "����";
		mPaint = new Paint(); 
        mPaint.setColor(defaultTextColor);
        mPaint.setAntiAlias(true); 
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(defaultTextSize);
        
        WindowManager wManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);    	
		Display display = wManager.getDefaultDisplay();    	
		width = display.getWidth();    	
		height = display.getHeight();
		cellWidth = (width-20)/7;
	}
	
	/**
	 * ���������ñ���.
	 *
	 * @param resid the new header background resource
	 */
 	public void setHeaderBackgroundResource(int resid){
 		setBackgroundResource(resid);
 		hasBg = true;
 	}
	
	/**
	 * ���������ִ�С.
	 *
	 * @return the text size
	 */
	public int getTextSize() {
		return defaultTextSize;
	}

	/**
	 * �������������ִ�С.
	 *
	 * @param mTextSize the new text size
	 */
	public void setTextSize(int mTextSize) {
		this.defaultTextSize = mTextSize;
		mPaint.setTextSize(defaultTextSize);
		this.invalidate();
	}  

	/**
	 * ������TODO.
	 *
	 * @param canvas the canvas
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 * @author: zhaoqp
	 * @date��2013-7-19 ����4:30:45
	 * @version v1.0
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(!hasBg){
		   canvas.drawColor(Color.WHITE);
		   //���þ��δ�С
		   rect.set(0, 0, this.getWidth(),this.getHeight());
		   rect.inset(0.5f,0.5f);
		}
		// ��������ͷ��
		drawDayHeader(canvas);
		
	}

	/**
	 * Draw day header.
	 *
	 * @param canvas the canvas
	 */
	private void drawDayHeader(Canvas canvas) {
		// д������ͷ�������û��ʲ���
		if(!hasBg){
			// �����Σ������þ��λ��ʵ���ɫ
			mPaint.setColor(Color.rgb(150, 195, 70));
			canvas.drawRect(rect, mPaint);
		}
		
		if(defaultTextBold){
			mPaint.setFakeBoldText(true);
		}
		mPaint.setColor(defaultTextColor);
		
		for (int iDay = 1; iDay < 8; iDay++) {
			if(iDay==1 || iDay==7){
				mPaint.setColor(specialTextColor);
			}
			// draw day name
			final String sDayName = getWeekDayName(iDay);
			
			TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
	        mTextPaint.setTextSize(defaultTextSize);
	        FontMetrics fm  = mTextPaint.getFontMetrics();
	        //�õ��и�
	        int textHeight = (int)Math.ceil(fm.descent - fm.ascent);
	        int textWidth = (int)AbGraphical.getStringWidth(sDayName,mTextPaint);
			
			final int iPosX = (int) rect.left +cellWidth*(iDay-1)+(cellWidth-textWidth)/2;
			final int iPosY = (int) (this.getHeight()
					- (this.getHeight() - textHeight) / 2 - mPaint
					.getFontMetrics().bottom);
			canvas.drawText(sDayName, iPosX, iPosY, mPaint);
			mPaint.setColor(defaultTextColor);
		}
		
	}
	
	/**
	 * ��������ȡ���ڵ���������.
	 *
	 * @param calendarDay the calendar day
	 * @return the week day name
	 */
	public String getWeekDayName(int calendarDay) {
		return dayName[calendarDay];
	}
	
}