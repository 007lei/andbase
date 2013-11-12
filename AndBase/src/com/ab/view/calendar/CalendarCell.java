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
import android.graphics.Path;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout.LayoutParams;

import com.ab.view.listener.AbOnItemClickListener;

// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�CalendarCell.java 
 * �����������ؼ���Ԫ�������
 * @author zhaoqp
 * @date��2013-7-9 ����3:54:16
 * @version v1.0
 */
public class CalendarCell extends View {
	
	// �����С
	/** The text size. */
	private int textSize = 22;
	
	// ����Ԫ��
	/** The m on item click listener. */
	private AbOnItemClickListener mOnItemClickListener;
	
	/** The pt. */
	private Paint pt = new Paint();
	
	/** The rect. */
	private RectF rect = new RectF();
	
	//��ʾ������
	/** The text date value. */
	private String textDateValue = "";

	// ��ǰ����
	/** The i date year. */
	private int iDateYear = 0;
	
	/** The i date month. */
	private int iDateMonth = 0;
	
	/** The i date day. */
	private int iDateDay = 0;

	// ��������
	/** The is selected. */
	private boolean isSelected = false;
	
	/** The is active month. */
	private boolean isActiveMonth = false;
	
	/** The is today. */
	private boolean isToday = false;
	
	/** The b touched down. */
	private boolean bTouchedDown = false;
	
	/** The is holiday. */
	private boolean isHoliday = false;
	
	/** The has record. */
	private boolean hasRecord = false;
	
	//��ǰcell�����
	/** The position. */
	private int position = 0;

	/** The anim alpha duration. */
	public static int ANIM_ALPHA_DURATION = 100;
	
	/*��ѡ�е�cell��ɫ*/
	/** The select cell color. */
	private int selectCellColor = Color.rgb(150, 195, 70);
	
	/*��󱳾���ɫ*/
	/** The bg color. */
	private int bgColor = Color.rgb(163,163, 163);
	
	/*������ɫ*/
	/** The number color. */
	private int numberColor = Color.rgb(86, 86, 86);
	
	/*cell������ɫ*/
	/** The cell color. */
	private int cellColor = Color.WHITE;
	
	/*�Ǳ��µ�������ɫ*/
	/** The not active month color. */
	private int notActiveMonthColor = Color.rgb(178, 178, 178);
	
	/*����cell��ɫ*/
	/** The today color. */
	private int todayColor = Color.rgb(150, 200, 220);
	

	// ���캯��
	/**
	 * Instantiates a new calendar cell.
	 *
	 * @param context the context
	 * @param position the position
	 * @param iWidth the i width
	 * @param iHeight the i height
	 */
	public CalendarCell(Context context, int position,int iWidth, int iHeight) {
		super(context);
		setFocusable(true);
		setLayoutParams(new LayoutParams(iWidth, iHeight));
		this.position = position;
	}

	/**
	 * ��������ȡ���Cell������.
	 *
	 * @return the this cell date
	 */
	public Calendar getThisCellDate() {
		Calendar calDate = Calendar.getInstance();
		calDate.clear();
		calDate.set(Calendar.YEAR, iDateYear);
		calDate.set(Calendar.MONTH, iDateMonth);
		calDate.set(Calendar.DAY_OF_MONTH, iDateDay);
		return calDate;
	}

	/**
	 * �������������Cell������.
	 *
	 * @param iYear the i year
	 * @param iMonth the i month
	 * @param iDay the i day
	 * @param isToday the is today
	 * @param isSelected the is selected
	 * @param isHoliday the is holiday
	 * @param isActiveMonth the is active month
	 * @param hasRecord the has record
	 */
	public void setThisCellDate(int iYear, int iMonth, int iDay, Boolean isToday,Boolean isSelected,
			Boolean isHoliday, int isActiveMonth, boolean hasRecord) {
		iDateYear = iYear;
		iDateMonth = iMonth;
		iDateDay = iDay;

		this.textDateValue = Integer.toString(iDateDay);
		this.isActiveMonth = (iDateMonth == isActiveMonth);
		this.isToday = isToday;
		this.isHoliday = isHoliday;
		this.hasRecord = hasRecord;
		this.isSelected = isSelected;
	}

	/**
	 * ���������ػ��Ʒ���.
	 *
	 * @param canvas the canvas
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawColor(bgColor);
		rect.set(0, 0, this.getWidth(), this.getHeight());
		rect.inset(0.5f, 0.5f);

		final boolean bFocused = IsViewFocused();

		drawDayView(canvas, bFocused);
		drawDayNumber(canvas);
	}

	/**
	 * Checks if is view focused.
	 *
	 * @return true, if successful
	 */
	public boolean IsViewFocused() {
		return (this.isFocused() || bTouchedDown);
	}

	/**
	 * ������������������.
	 *
	 * @param canvas the canvas
	 * @param bFocused the b focused
	 */
	private void drawDayView(Canvas canvas, boolean bFocused) {

		pt.setColor(getCellColor());
		canvas.drawRect(rect, pt);

		if (hasRecord) {
			createReminder(canvas,Color.RED);
		}
		
	}

	/**
	 * ���������������е�����.
	 *
	 * @param canvas the canvas
	 */
	public void drawDayNumber(Canvas canvas) {
		// draw day number
		pt.setTypeface(null);
		pt.setAntiAlias(true);
		pt.setShader(null);
		pt.setFakeBoldText(true);
		pt.setTextSize(textSize);
		pt.setColor(numberColor);
		pt.setUnderlineText(false);
		
		if (!isActiveMonth){
			pt.setColor(notActiveMonthColor);
		}

		final int iPosX = (int) rect.left + ((int) rect.width() >> 1) - ((int) pt.measureText(textDateValue) >> 1);
		final int iPosY = (int) (this.getHeight() - (this.getHeight() - getTextHeight()) / 2 - pt.getFontMetrics().bottom);
		canvas.drawText(textDateValue, iPosX, iPosY, pt);
	}

	/**
	 * �������õ�����߶�.
	 *
	 * @return the text height
	 */
	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	/**
	 * �����������������ز�ͬ��ɫֵ.
	 *
	 * @return the cell color
	 */
	public int getCellColor() {
		if (isToday){
			return todayColor;
		}
		
		if (isSelected){
			return selectCellColor;
		}
		
		//������ĩ�����ⱳ��ɫ
		if (isHoliday){
		   return cellColor;
		}
		
		//Ĭ���ǰ�ɫ�ĵ�Ԫ��
		return cellColor;
	}

	/**
	 * �����������Ƿ�ѡ��.
	 *
	 * @param selected the new selected
	 */
	@Override
	public void setSelected(boolean selected) {
		if (this.isSelected != selected) {
			this.isSelected = selected;
			this.invalidate();
		}
	}
	
	/**
	 * �����������Ƿ�������.
	 *
	 * @param hasRecord the new checks for record
	 */
	public void setHasRecord(boolean hasRecord) {
		if (this.hasRecord != hasRecord) {
			this.hasRecord = hasRecord;
			this.invalidate();
		}
	}

	/**
	 * ���������õ���¼�.
	 *
	 * @param onItemClickListener the new on item click listener
	 */
	public void setOnItemClickListener(AbOnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}

	/**
	 * ������ִ�е���¼�.
	 */
	public void doItemClick() {
		if (mOnItemClickListener != null){
			mOnItemClickListener.onClick(position);
	    }
	}

	/**
	 * ������TODO.
	 *
	 * @param event the event
	 * @return true, if successful
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 * @author: zhaoqp
	 * @date��2013-7-19 ����4:31:18
	 * @version v1.0
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean bHandled = false;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			bHandled = true;
			bTouchedDown = true;
			invalidate();
			startAlphaAnimIn(CalendarCell.this);
		}
		if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
			doItemClick();
		}
		return bHandled;
	}

	/**
	 * ������TODO.
	 *
	 * @param keyCode the key code
	 * @param event the event
	 * @return true, if successful
	 * @see android.view.View#onKeyDown(int, android.view.KeyEvent)
	 * @author: zhaoqp
	 * @date��2013-7-19 ����4:31:18
	 * @version v1.0
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyDown(keyCode, event);
		if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
				|| (keyCode == KeyEvent.KEYCODE_ENTER)) {
			doItemClick();
		}
		return bResult;
	}

	/**
	 * ������������͸���Ƚ���.
	 *
	 * @param view the view
	 */
	public static void startAlphaAnimIn(View view) {
		AlphaAnimation anim = new AlphaAnimation(0.5F, 1);
		anim.setDuration(ANIM_ALPHA_DURATION);
		anim.startNow();
		view.startAnimation(anim);
	}

	/**
	 * �������м�¼ʱ������.
	 *
	 * @param canvas the canvas
	 * @param Color the color
	 */
	public void createReminder(Canvas canvas, int Color) {
		pt.setUnderlineText(true);
		pt.setStyle(Paint.Style.FILL_AND_STROKE);
		pt.setColor(Color);
		Path path = new Path();
		path.moveTo(rect.right - rect.width() / 4, rect.top);
		path.lineTo(rect.right, rect.top);
		path.lineTo(rect.right, rect.top + rect.width() / 4);
		path.lineTo(rect.right - rect.width() / 4, rect.top);
		path.close();
		canvas.drawPath(path, pt);
		pt.setUnderlineText(true);
	}

	/**
	 * �������Ƿ�Ϊ�����.
	 *
	 * @return true, if is active month
	 */
	public boolean isActiveMonth() {
		return isActiveMonth;
	}
	
	
	
	
	
}