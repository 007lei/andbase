package com.ab.view.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

import com.ab.view.listener.AbOnProgressListener;

@SuppressLint("DrawAllocation")
public class AbHorizontalProgressBar extends View {
	
	private int progress = 0;
	private int max = 100;
	
	//���ƹ켣
	private Paint pathPaint = null;
	
	//�������
	private Paint fillPaint = null;
	
	//·�����
	private int pathWidth = 35;
	
	/** The width. */
	private int width;
	
	/** The height. */
	private int height; 
	
	//��ɫ�켣
	private int pathColor = 0xFFF0EEDF;
	private int pathBorderColor = 0xFFD2D1C4;
	
	//�ݶȽ���������ɫ
	private int[] fillColors = new int[] {0xFF3DF346,0xFF02C016};
	
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

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public AbHorizontalProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		pathPaint  = new Paint();
		// �����Ƿ񿹾��
		pathPaint.setAntiAlias(true);
		// �����������
		pathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		// �����пյ���ʽ
		pathPaint.setStyle(Paint.Style.FILL);
		pathPaint.setDither(true);
		//pathPaint.setStrokeJoin(Paint.Join.ROUND);
		
		fillPaint = new Paint();
		// �����Ƿ񿹾��
		fillPaint.setAntiAlias(true);
		// �����������
		fillPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		// �����пյ���ʽ
		fillPaint.setStyle(Paint.Style.FILL);
		fillPaint.setDither(true);
		//fillPaint.setStrokeJoin(Paint.Join.ROUND);
		
		emboss = new EmbossMaskFilter(direction,light,specular,blur);  
		mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(reset){
			canvas.drawColor(Color.TRANSPARENT);
			reset = false;
		}
		this.width = getMeasuredWidth();
		this.height = getMeasuredHeight();
		
		// ���û�����ɫ
		pathPaint.setColor(pathColor);
		// ���û��ʿ��
		pathPaint.setStrokeWidth(pathWidth);
				
		//��Ӹ���Ч��
		pathPaint.setMaskFilter(emboss); 
		canvas.drawRect(0, 0, this.width, this.height, pathPaint);
		pathPaint.setColor(pathBorderColor);
		canvas.drawRect(0, 0, this.width, this.height, pathPaint);
		
		
		LinearGradient linearGradient = new LinearGradient(0, 0, this.width, this.height, 
				fillColors[0], fillColors[1], TileMode.CLAMP); 
		fillPaint.setShader(linearGradient);
		
		//ģ��Ч��
		fillPaint.setMaskFilter(mBlur);
		
		//�����ߵ�����,����Բ��
		fillPaint.setStrokeCap(Paint.Cap.ROUND);
		
		fillPaint.setStrokeWidth(pathWidth);
		canvas.drawRect(0, 0, ((float) progress / max) * this.width,this.height, fillPaint);
		
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
