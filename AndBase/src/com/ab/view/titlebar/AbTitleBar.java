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
package com.ab.view.titlebar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ab.util.AbViewUtil;

// TODO: Auto-generated Javadoc
/**
 * ������������ʵ��.
 *
 * @author zhaoqp
 * @date��2013-4-24 ����3:46:47
 * @version v1.0
 */
public class AbTitleBar extends LinearLayout {
	
	/** The m context. */
	private Activity mActivity;
	
	/** ���Ⲽ��. */
	protected LinearLayout titleTextLayout = null;
	
	/** ��ʾ�������ֵ�View. */
	protected Button titleTextBtn = null;
	
	/** ����Logoͼ��View. */
	protected ImageView logoView = null;
	
	/** ����Logoͼ���ұߵķָ���View. */
	protected ImageView logoLineView = null;
	
	/** �����ı��Ķ������. */
	private LinearLayout.LayoutParams titleTextLayoutParams = null;
	
	/** �ұ߲��ֵĵĶ������. */
	private LinearLayout.LayoutParams rightViewLayoutParams = null;
	
	/** �ұߵ�View�������Զ�����ʾʲô. */
	protected LinearLayout rightLayout = null;
	
	/** ����������ID. */
	public int mAbTitlebarID = 1;
	
	/** ȫ�ֵ�LayoutInflater�����Ѿ���ɳ�ʼ��. */
	public LayoutInflater mInflater;
	
	/**
	 * LinearLayout.LayoutParams���Ѿ���ʼ��ΪFILL_PARENT, FILL_PARENT
	 */
	public LinearLayout.LayoutParams layoutParamsFF = null;
	
	/**
	 * LinearLayout.LayoutParams���Ѿ���ʼ��ΪFILL_PARENT, WRAP_CONTENT
	 */
	public LinearLayout.LayoutParams layoutParamsFW = null;
	
	/**
	 * LinearLayout.LayoutParams���Ѿ���ʼ��ΪWRAP_CONTENT, FILL_PARENT
	 */
	public LinearLayout.LayoutParams layoutParamsWF = null;
	
	/**
	 * LinearLayout.LayoutParams���Ѿ���ʼ��ΪWRAP_CONTENT, WRAP_CONTENT
	 */
	public LinearLayout.LayoutParams layoutParamsWW = null;

	public AbTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		ininTitleBar(context);
	}

	public AbTitleBar(Context context) {
		super(context);
		ininTitleBar(context);
		
	}
	
	public void ininTitleBar(Context context){
		
		mActivity  = (Activity)context;
		//ˮƽ����
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setId(mAbTitlebarID);
		
		mInflater = LayoutInflater.from(context);
		
		layoutParamsFF = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWF = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		layoutParamsWW = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWW.gravity = Gravity.CENTER_VERTICAL;
		
		titleTextLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1);
		titleTextLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		rightViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,0);
		rightViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		
		
		titleTextLayout = new LinearLayout(context);
		titleTextLayout.setOrientation(LinearLayout.HORIZONTAL);
		titleTextLayout.setGravity(Gravity.CENTER_VERTICAL);
		titleTextLayout.setPadding(0, 0, 0, 0);
		
		titleTextBtn = new Button(context);
		titleTextBtn.setTextColor(Color.rgb(255, 255, 255));
		titleTextBtn.setTextSize(20);
		titleTextBtn.setPadding(5, 0, 5, 0);
		titleTextBtn.setGravity(Gravity.CENTER_VERTICAL);
		titleTextBtn.setBackgroundDrawable(null);
		titleTextLayout.addView(titleTextBtn,layoutParamsWF);
		
		logoView = new ImageView(context);
		logoView.setVisibility(View.GONE);
		logoLineView = new ImageView(context);
		logoLineView.setVisibility(View.GONE);
		
		this.addView(logoView,layoutParamsWW);
		this.addView(logoLineView,layoutParamsWW);
		this.addView(titleTextLayout,titleTextLayoutParams);
		
		
		// ���ұߵĲ���
		rightLayout = new LinearLayout(context);
		rightLayout.setOrientation(LinearLayout.HORIZONTAL);
		rightLayout.setGravity(Gravity.RIGHT);
		rightLayout.setPadding(0, 0, 0, 0);
		rightLayout.setHorizontalGravity(Gravity.RIGHT);
		rightLayout.setGravity(Gravity.CENTER_VERTICAL);
		rightLayout.setVisibility(View.GONE);
		this.addView(rightLayout,rightViewLayoutParams);
	
		logoView.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				mActivity.finish();
			}
		});
	}
	

	/**
	 * �������������ı���ͼ.
	 * @param res  ����ͼ��ԴID
	 */
	public void setTitleLayoutBackground(int res) {
		this.setBackgroundResource(res);
	}
	
	/**
	 * �������������ֵĶ���.
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTitleTextMargin(int left,int top,int right,int bottom) {
		titleTextLayoutParams.setMargins(left, top, right, bottom);
	}
	
	
	/**
	 * �������������ı���ͼ.
	 * @param color  ������ɫֵ
	 */
	public void setTitleLayoutBackgroundColor(int color) {
		this.setBackgroundColor(color);
	}

	/**
	 * ���������������ֺ�.
	 * @param titleTextSize  �����ֺ�
	 */
	public void setTitleTextSize(int titleTextSize) {
		this.titleTextBtn.setTextSize(titleTextSize);
	}
	
	/**
	 * ���������ñ������ֶ��뷽ʽ
	 * �����ұߵľ�������ж�����
	 * ��1���м俿�� Gravity.CENTER,Gravity.CENTER
	 * ��2����߾��� �ұ߾���Gravity.LEFT,Gravity.RIGHT
	 * ��3����߾��У��ұ߾���Gravity.CENTER,Gravity.RIGHT
	 * ��4����߾��ң��ұ߾���Gravity.RIGHT,Gravity.RIGHT
	 * ������addRightView(view)����������
	 * @param gravity1  ������뷽ʽ
	 * @param gravity2  �ұ߲��ֶ��뷽ʽ
	 */
	public void setTitleLayoutGravity(int gravity1,int gravity2) {
		AbViewUtil.measureView(this.rightLayout);
		AbViewUtil.measureView(this.logoView);
		int leftWidth = this.logoView.getMeasuredWidth();
		int rightWidth = this.rightLayout.getMeasuredWidth();
		//if(D)Log.d(TAG, "�������ֵĿ�ȣ�"+leftWidth+","+rightWidth);
		this.titleTextLayoutParams.rightMargin = 0;
		this.titleTextLayoutParams.leftMargin = 0;
		//ȫ���м俿
		if((gravity1 == Gravity.CENTER_HORIZONTAL || gravity1 == Gravity.CENTER) ){
            if(leftWidth==0 && rightWidth==0){
            	this.titleTextLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			}else{
				if(gravity2 == Gravity.RIGHT){
					if(rightWidth==0){
					}else{
						this.titleTextBtn.setPadding(rightWidth/3*2, 0, 0, 0);
					}
					this.titleTextBtn.setGravity(Gravity.CENTER);
					this.rightLayout.setHorizontalGravity(Gravity.RIGHT);
				}if(gravity2 == Gravity.CENTER || gravity2 == Gravity.CENTER_HORIZONTAL){
					this.titleTextLayout.setGravity(Gravity.CENTER_HORIZONTAL);
					this.rightLayout.setHorizontalGravity(Gravity.LEFT);
					this.titleTextBtn.setGravity(Gravity.CENTER);
					int offset = leftWidth-rightWidth;
					if(offset>0){
						this.titleTextLayoutParams.rightMargin = offset;
					}else{
						this.titleTextLayoutParams.leftMargin = Math.abs(offset);
					}
				}
			}
		//����
		}else if(gravity1 == Gravity.LEFT && gravity2 == Gravity.RIGHT){
			this.titleTextLayout.setGravity(Gravity.LEFT);
			this.rightLayout.setHorizontalGravity(Gravity.RIGHT);
		//ȫ���ҿ�
		}else if(gravity1 == Gravity.RIGHT && gravity2 == Gravity.RIGHT){
			this.titleTextLayout.setGravity(Gravity.RIGHT);
			this.rightLayout.setHorizontalGravity(Gravity.RIGHT);
		}else if(gravity1 == Gravity.LEFT && gravity2 == Gravity.LEFT){
			this.titleTextLayout.setGravity(Gravity.LEFT);
			this.rightLayout.setHorizontalGravity(Gravity.LEFT);
		}
		
	}
	
	/**
	 * ��������ȡ�����ı���Button.
	 * @return the title Button view
	 */
	public Button getTitleTextButton() {
		return titleTextBtn;
	}
	
	/**
	 * ��������ȡ����Logo��View.
	 * @return the logo view
	 */
	public ImageView getLogoView() {
		return logoView;
	}
	
	/**
	 * ���������ñ����������.
	 *
	 * @param bold the new title text bold
	 */
	public void setTitleTextBold(boolean bold){
		TextPaint paint = titleTextBtn.getPaint();  
		if(bold){
			//����
			paint.setFakeBoldText(true); 
		}else{
			paint.setFakeBoldText(false); 
		}
		
	}
	
	/**
	 * ���������ñ��ⱳ��.
	 *
	 * @param resId the new title text background resource
	 */
	public void setTitleTextBackgroundResource(int resId){
		titleTextBtn.setBackgroundResource(resId);
	}
	
	/**
	 * ���������ñ��ⱳ��.
	 * @param d  ����ͼ
	 */
	public void setTitleLayoutBackgroundDrawable(Drawable d) {
		this.setBackgroundDrawable(d);
	}
	
	/**
	 * ���������ñ��ⱳ��.
	 *
	 * @param drawable the new title text background drawable
	 */
	public void setTitleTextBackgroundDrawable(Drawable drawable){
		titleTextBtn.setBackgroundDrawable(drawable);
	}
	
	/**
     * ���������ñ����ı�.
     * @param text  �ı�
     */
	public void setTitleText(String text) {
		titleTextBtn.setText(text);
	}
	
	/**
     * ���������ñ����ı�.
     * @param resId  �ı�����ԴID
     */
	public void setTitleText(int resId) {
		titleTextBtn.setText(resId);
	}
	
	/**
     * ����������Logo�ı���ͼ.
     * @param drawable  Logo��ԴDrawable
     */
	public void setLogo(Drawable drawable) {
		logoView.setVisibility(View.VISIBLE);
		logoView.setBackgroundDrawable(drawable);
	}
	
	/**
     * ����������Logo�ı�����Դ.
     * @param resId  Logo��ԴID
     */
	public void setLogo(int resId) {
		logoView.setVisibility(View.VISIBLE);
		logoView.setBackgroundResource(resId);
	}
	
	/**
     * ����������Logo�ָ��ߵı�����Դ.
     * @param resId  Logo��ԴID
     */
	public void setLogoLine(int resId) {
		logoLineView.setVisibility(View.VISIBLE);
		logoLineView.setBackgroundResource(resId);
	}
	
	/**
     * ����������Logo�ָ��ߵı���ͼ.
     * @param drawable  Logo��ԴDrawable
     */
	public void setLogoLine(Drawable drawable) {
		logoLineView.setVisibility(View.VISIBLE);
		logoLineView.setBackgroundDrawable(drawable);
	}
	
	
	/**
     * ��������ָ����View��ӵ��������ұ�.
     * @param rightView  ָ����View
     */
	public void addRightView(View rightView) {
		rightLayout.setVisibility(View.VISIBLE);
		rightLayout.addView(rightView,layoutParamsWF);
	}
	
	/**
     * ��������ָ����ԴID��ʾ��View��ӵ��������ұ�.
     * @param resId  ָ����View����ԴID
     */
	public void addRightView(int resId) {
		rightLayout.setVisibility(View.VISIBLE);
		rightLayout.addView(mInflater.inflate(resId, null),layoutParamsWF);
	}
	
	/**
     * ����������������ұߵ�View.
     */
	public void clearRightView() {
		rightLayout.removeAllViews();
	}
	
	/**
	 * ����������Logo��ť�ķ����¼�.
	 * @param mOnClickListener  ָ���ķ����¼�
	 */
	public void setLogoBackOnClickListener(View.OnClickListener mOnClickListener) {
		 logoView.setOnClickListener(mOnClickListener);
	}
}
