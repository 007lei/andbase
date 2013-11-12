package com.ab.view.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.ab.view.listener.AbOnProgressListener;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�AbCircleProgressBar.java 
 * ���������ε�ProgressBar
 * @author zhaoqp
 * @date��2013-9-22 ����2:44:17
 * @version v1.0
 */
public class AbCircleProgressBar extends View {
	
	
	private int progress = 0;
	private int max = 100;
	
	//���ƹ켣
	private Paint pathPaint = null;
	
	//�������
	private Paint fillArcPaint = null;
	
	private RectF oval;
	
	//�ݶȽ���������ɫ
	private int[] arcColors = new int[] {0xFF02C016,  0xFF3DF346, 0xFF40F1D5, 0xFF02C016 };
	private int[] shadowsColors = new int[] { 0xFF111111, 0x00AAAAAA, 0x00AAAAAA };
	//��ɫ�켣
	private int pathColor = 0xFFF0EEDF;
	private int pathBorderColor = 0xFFD2D1C4;
	
	//����·�����
	private int pathWidth = 35;
	
	/** The width. */
	private int width;
	
	/** The height. */
	private int height; 
	
	//Ĭ��Բ�İ뾶
	private int radius = 120;
	
	// ָ���˹�Դ�ķ���ͻ�����ǿ������Ӹ���Ч��
	private EmbossMaskFilter emboss = null;
	// ���ù�Դ�ķ���  
	float[] direction = new float[]{1,1,1};
	//���û���������  
	float light = 0.4f;  
	// ѡ��ҪӦ�õķ���ȼ�  
	float specular = 6;  
	// �� maskӦ��һ�������ģ��  
	float blur = 3.5f;  
	
	//ָ����һ��ģ������ʽ�Ͱ뾶������ Paint �ı�Ե
	private BlurMaskFilter mBlur = null;
	
	//������
	private AbOnProgressListener mAbOnProgressListener = null;
	
	//view�ػ�ı��
	private boolean reset = false;
	

	public AbCircleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		pathPaint  = new Paint();
		// �����Ƿ񿹾��
		pathPaint.setAntiAlias(true);
		// �����������
		pathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		// �����пյ���ʽ
		pathPaint.setStyle(Paint.Style.STROKE);
		pathPaint.setDither(true);
		pathPaint.setStrokeJoin(Paint.Join.ROUND);
		
		fillArcPaint = new Paint();
		// �����Ƿ񿹾��
		fillArcPaint.setAntiAlias(true);
		// �����������
		fillArcPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		// �����пյ���ʽ
		fillArcPaint.setStyle(Paint.Style.STROKE);
		fillArcPaint.setDither(true);
		fillArcPaint.setStrokeJoin(Paint.Join.ROUND);
				
		oval = new RectF();
		emboss = new EmbossMaskFilter(direction,light,specular,blur);  
		mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(reset){
			canvas.drawColor(Color.TRANSPARENT);
			reset = false;
		}
		this.width = getMeasuredWidth();
		this.height = getMeasuredHeight();
		this.radius = getMeasuredWidth()/2 - pathWidth;
		
		// ���û�����ɫ
		pathPaint.setColor(pathColor);
		// ���û��ʿ��
		pathPaint.setStrokeWidth(pathWidth);
		
		//��Ӹ���Ч��
		pathPaint.setMaskFilter(emboss); 
		
		// �����ĵĵط������뾶Ϊr��Բ
		canvas.drawCircle(this.width/2, this.height/2, radius, pathPaint);
		
		//����
		pathPaint.setStrokeWidth(0.5f);
		pathPaint.setColor(pathBorderColor);
		canvas.drawCircle(this.width/2, this.height/2, radius+pathWidth/2+0.5f, pathPaint);
		canvas.drawCircle(this.width/2, this.height/2, radius-pathWidth/2-0.5f, pathPaint);
		
		
		/*int[] gradientColors = new int[3];  
		gradientColors[0] = Color.GREEN;  
		gradientColors[1] = Color.YELLOW;  
		gradientColors[2] = Color.RED;  
		float[] gradientPositions = new float[3];  
		gradientPositions[0] = 0.0f;  
		gradientPositions[1] = 0.5f;  
		gradientPositions[2] = 1.0f;  
		
		//����ɫ����Բ�����
		RadialGradient radialGradientShader = new RadialGradient(this.width/2,this.height/2, 
				radius, gradientColors, gradientPositions, TileMode.CLAMP); 
		
		paint1.setShader(radialGradientShader);*/
		
		//������ɫ���
		SweepGradient sweepGradient = new SweepGradient(this.width/2, this.height/2, arcColors, null);
		fillArcPaint.setShader(sweepGradient);
		// ���û���Ϊ��ɫ
		
		//ģ��Ч��
		fillArcPaint.setMaskFilter(mBlur);
		
		//�����ߵ�����,����Բ��
		fillArcPaint.setStrokeCap(Paint.Cap.ROUND);
		
		//fillArcPaint.setColor(Color.BLUE);
		
		fillArcPaint.setStrokeWidth(pathWidth);
		// �������������Ͻ����꣬���½�����
		oval.set(this.width/2 - radius, this.height/2 - radius, this.width/2 + radius, this.height/2 + radius);
		// ��Բ�����ڶ�������Ϊ����ʼ�Ƕȣ�������Ϊ��ĽǶȣ����ĸ�Ϊtrue��ʱ����ʵ�ģ�false��ʱ��Ϊ����
		canvas.drawArc(oval, -90, ((float) progress / max) * 360, false, fillArcPaint);
		
	}

	/**
	 * 
	 * ��������ȡԲ�İ뾶
	 * @return
	 * @throws 
	 */
	public int getRadius() {
		return radius;
	}
	
	
    /**
     * 
     * ����������Բ�İ뾶
     * @param radius
     * @throws 
     */
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		this.invalidate();
		if(this.mAbOnProgressListener!=null){
			if(this.max <= this.progress){
				this.mAbOnProgressListener.onComplete(progress);
			}else{
				this.mAbOnProgressListener.onProgress(progress);
			}
		}
	}
	
	@Override  
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){   
	    int height = View.MeasureSpec.getSize(heightMeasureSpec);    
	    int width = View.MeasureSpec.getSize(widthMeasureSpec);    
	    setMeasuredDimension(width,height);
	}

	public AbOnProgressListener getAbOnProgressListener() {
		return mAbOnProgressListener;
	}

	public void setAbOnProgressListener(AbOnProgressListener mAbOnProgressListener) {
		this.mAbOnProgressListener = mAbOnProgressListener;
	}  
	
	
	/**
	 * 
	 * ���������ý���
	 * @throws 
	 */
	public void reset(){
		reset  = true;
		this.progress = 0;
		this.invalidate();
	}
	
	
}
