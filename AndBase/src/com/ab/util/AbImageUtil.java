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
package com.ab.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

import com.ab.global.AbAppData;
import com.ab.global.AbConstant;
import com.ab.util.dct.FDCT;

// TODO: Auto-generated Javadoc
/**
 * ������ͼƬ������.
 *
 * @author zhaoqp
 * @date 2011-12-10
 * @version v1.0
 */
public class AbImageUtil {
	
	/** The tag. */
	private static String TAG = "AbImageUtil";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	
	/**
	 * ֱ�ӻ�ȡ�������ϵ�ͼƬ.
	 * @param imageUrl Ҫ�����ļ��������ַ
	 * @param type ͼƬ�Ĵ������ͣ����л������ŵ�ָ����С���ο�AbConstant�ࣩ
	 * @param newWidth ��ͼƬ�Ŀ� 
	 * @param newHeight ��ͼƬ�ĸ�
	 * @return Bitmap ��ͼƬ
	 */
	public static Bitmap getBitmapFormURL(String imageUrl,int type,int newWidth,int newHeight){
		Bitmap bm = null;
		URLConnection con = null;
		InputStream is = null;
		try {
			URL url = new URL(imageUrl);
			con = url.openConnection();
			con.setDoInput(true);
			con.connect();
			is = con.getInputStream();
			//��ȡ��ԴͼƬ
			Bitmap wholeBm =  BitmapFactory.decodeStream(is,null,null); 
			if(type==AbConstant.CUTIMG){
				bm = cutImg(wholeBm,newWidth,newHeight);
		 	}else if(type==AbConstant.SCALEIMG){
		 		bm = scaleImg(wholeBm,newWidth,newHeight);
		 	}else{
		 		bm = wholeBm;
		 	}
		} catch (Exception e) {
			if(D) Log.d(TAG, ""+e.getMessage());
		}finally{
			try {
				if(is!=null){
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bm;
   }   
	
	/**
    * ��������ȡԭͼ
	* @param file File����
	* @return Bitmap ͼƬ
	*/
	public static Bitmap originalImg(File file){ 
		Bitmap resizeBmp = null;
	    try {
			resizeBmp = BitmapFactory.decodeFile(file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return resizeBmp;
	}
	
	/**
	* ����������ͼƬ.ѹ��
	* @param file File����
	* @param newWidth ��ͼƬ�Ŀ�
	* @param newHeight ��ͼƬ�ĸ�
	* @return Bitmap ��ͼƬ
	*/
	public static Bitmap scaleImg(File file,int newWidth, int newHeight){ 
		Bitmap resizeBmp = null;
		if(newWidth<=0 || newHeight<=0){
			 throw new IllegalArgumentException("����ͼƬ�Ŀ�����ò���С��0");
	 	}
	    BitmapFactory.Options opts = new BitmapFactory.Options(); 
	    //����Ϊtrue,decodeFile�Ȳ������ڴ� ֻ��ȡһЩ����߽���Ϣ��ͼƬ��С��Ϣ
	    opts.inJustDecodeBounds = true;	
	    BitmapFactory.decodeFile(file.getPath(),opts);
	    //inSampleSize=2��ʾͼƬ��߶�Ϊԭ���Ķ���֮һ����ͼƬΪԭ�����ķ�֮һ
	    //���ſ��Խ����ص��
	    // ��ȡͼƬ��ԭʼ��ȸ߶�
		int srcWidth = opts.outWidth;  
		int srcHeight = opts.outHeight;
		
		int destWidth = srcWidth;
		int destHeight = srcHeight;
		
		// ���ŵı���
		float scale = 0;
		// �������ű���
        float scaleWidth = (float)newWidth/srcWidth;
        float scaleHeight = (float)newHeight/srcHeight;
        if(scaleWidth > scaleHeight){
        	scale = scaleWidth;
        }else{
        	scale = scaleHeight;
        }
        if(scale!=0){
        	destWidth = (int)(destWidth/scale);
     		destHeight = (int)(destHeight/scale);
        }
		
        //Ĭ��ΪARGB_8888.
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		//���������ֶ���һ��ʹ�ã� 
		//������λͼ���õ����ؿռ䣬���ϵͳgc����ô������ա��������ٴα����ʣ����Bitmap�Ѿ�decode����ô�����Զ����½��� 
		opts.inPurgeable = true;
		//λͼ���Թ���һ���ο���������(inputstream�����е�)
		opts.inInputShareable = true;
        
		// ���ŵı����������Ǻ��Ѱ�׼���ı����������ŵģ�ͨ��inSampleSize���������ţ���ֵ�������ŵı�����SDK�н�����ֵ��2��ָ��ֵ
		if(scale>1){
			//��С
			opts.inSampleSize = (int)scale;
		}else{
			//�Ŵ�
			opts.inSampleSize = 1;
		}
		
	    // ���ô�С
		opts.outHeight = destHeight;
		opts.outWidth = destWidth;
	    //�����ڴ�
		opts.inJustDecodeBounds = false;	
	    //ʹͼƬ������  
		opts.inDither = false;   
		resizeBmp = BitmapFactory.decodeFile(file.getPath(),opts);
	    //��С���߷Ŵ�
	    if(resizeBmp != null && scale!=1){
	    	resizeBmp = scaleImg(resizeBmp,scale);
	    }
	    if(D) Log.d(TAG, "����ͼƬ:"+resizeBmp);
	    return resizeBmp;
    }
	
	  
	/**
	 * ����������ͼƬ,��ѹ��������.
	 *
	 * @param bitmap the bitmap
	 * @param newWidth ��ͼƬ�Ŀ�
	 * @param newHeight ��ͼƬ�ĸ�
	 * @return Bitmap ��ͼƬ
	 */
	  public static Bitmap scaleImg(Bitmap bitmap, int newWidth, int newHeight) {
	        
		   Bitmap resizeBmp = null;
		   if(bitmap == null){
	        	return null;
	        }
	        if(newWidth<=0 || newHeight<=0){
				 throw new IllegalArgumentException("����ͼƬ�Ŀ�����ò���С��0");
		 	}
		    // ���ͼƬ�Ŀ��
	        int srcWidth = bitmap.getWidth();
	        int srcHeight = bitmap.getHeight();
	        
	        if(srcWidth <= 0 || srcHeight <= 0){
		 		  return null;
		 	}
	        // ���ŵı���
	        float scale = 0;
	        // �������ű���
	        float scaleWidth = (float)newWidth/srcWidth;
	        float scaleHeight = (float)newHeight/srcHeight;
	        if(scaleWidth > scaleHeight){
	        	scale = scaleWidth;
	        }else{
	        	scale = scaleHeight;
	        }
	        //��С���߷Ŵ�
		    if(bitmap != null && scale!=1){
		    	resizeBmp = scaleImg(bitmap,scale);
		    }
	        return resizeBmp;
	  }
	  
   /**
    * ���������ݵȱ�������ͼƬ.
    *
    * @param bitmap the bitmap
    * @param scale ����
    * @return Bitmap ��ͼƬ
    */
	  public static Bitmap scaleImg(Bitmap bitmap,float scale){
			Bitmap resizeBmp = null;
			try {
				//��ȡBitmap��Դ�Ŀ�͸�
				int bmpW = bitmap.getWidth();
				int bmpH = bitmap.getHeight();
				//ע�����Matirx��android.graphics���µ��Ǹ�
				Matrix mt = new Matrix();
				//��������ϵ�����ֱ�Ϊԭ����0.8��0.8
				mt.postScale(scale, scale);
				resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpW, bmpH, mt, true);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(resizeBmp != bitmap){
					bitmap.recycle();
				}
			}
	        return resizeBmp;
	  }
	  
	  /**
	   * �������ü�ͼƬ.
	   * @param file  File����
	   * @param newWidth ��ͼƬ�Ŀ�
	   * @param newHeight ��ͼƬ�ĸ�
	   * @return Bitmap ��ͼƬ
	   */
	   public static Bitmap cutImg(File file,int newWidth, int newHeight){ 
		    Bitmap resizeBmp = null;
		    if(newWidth<=0 || newHeight<=0){
				 throw new IllegalArgumentException("�ü�ͼƬ�Ŀ�����ò���С��0");
		 	}
		    
		    BitmapFactory.Options opts = new BitmapFactory.Options(); 
		    //����Ϊtrue,decodeFile�Ȳ������ڴ� ֻ��ȡһЩ����߽���Ϣ��ͼƬ��С��Ϣ
		    opts.inJustDecodeBounds = true;	
		    BitmapFactory.decodeFile(file.getPath(),opts);
		    //inSampleSize=2��ʾͼƬ��߶�Ϊԭ���Ķ���֮һ����ͼƬΪԭ�����ķ�֮һ
		    //���ſ��Խ����ص��,�ü�ǰ��ͼƬ���ŵ�Ŀ��ͼ2����С
			int srcWidth = opts.outWidth;  // ��ȡͼƬ��ԭʼ���
			int srcHeight = opts.outHeight;// ��ȡͼƬԭʼ�߶�
			int destWidth = 0;
			int destHeight = 0;
			
			int cutSrcWidth = newWidth*2;
			int cutSrcHeight = newHeight*2;
			
			// ���ŵı���,Ϊ�˴�ͼ����С��2�����ü��Ĵ�С�ڲü�
			double ratio = 0.0;
			//����һ���������Ͳ�����
			if (srcWidth < cutSrcWidth || srcHeight < cutSrcHeight) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > cutSrcWidth) {
				ratio = (double) srcWidth / cutSrcWidth;
				destWidth = cutSrcWidth;
				destHeight = (int) (srcHeight / ratio);
			} else if (srcHeight > cutSrcHeight){
				ratio = (double) srcHeight / cutSrcHeight;
				destHeight = cutSrcHeight;
				destWidth = (int) (srcWidth / ratio);
			}
			
			//Ĭ��ΪARGB_8888.
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			//���������ֶ���һ��ʹ�ã� 
			//������λͼ���õ����ؿռ䣬���ϵͳgc����ô������ա��������ٴα����ʣ����Bitmap�Ѿ�decode����ô�����Զ����½��� 
			opts.inPurgeable = true;
			//λͼ���Թ���һ���ο���������(inputstream�����е�)
			opts.inInputShareable = true;
			// ���ŵı����������Ǻ��Ѱ�׼���ı����������ŵģ�ͨ��inSampleSize���������ţ���ֵ�������ŵı�����SDK�н�����ֵ��2��ָ��ֵ
			if(ratio>1){
				opts.inSampleSize = (int)ratio;
			}else{
				opts.inSampleSize = 1;
			}
		    // ���ô�С
			opts.outHeight = destHeight;
			opts.outWidth = destWidth;
		    //�����ڴ�
		    opts.inJustDecodeBounds = false;	
		    //ʹͼƬ������  
		    opts.inDither = false;   		
		    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(),opts);
		    if(bitmap!=null){
		    	resizeBmp = cutImg(bitmap,newWidth,newHeight);
		    }
		    return resizeBmp;
	  }
	  
	  /**
  	 * �������ü�ͼƬ.
  	 *
  	 * @param bitmap the bitmap
  	 * @param newWidth ��ͼƬ�Ŀ�
  	 * @param newHeight ��ͼƬ�ĸ�
  	 * @return Bitmap ��ͼƬ
  	 */
	  public static Bitmap cutImg(Bitmap bitmap, int newWidth, int newHeight) {
		  if(bitmap == null){
	        	return null;
	      }
		  
		  if(newWidth<=0 || newHeight<=0){
			 throw new IllegalArgumentException("�ü�ͼƬ�Ŀ�����ò���С��0");
	 	  }
	 	  
	 	  Bitmap resizeBmp = null;
	 	 
	      try {
			  int width = bitmap.getWidth();
			  int height = bitmap.getHeight();
			  
			  if(width <= 0 || height <= 0){
				  return null;
			  }
			  int offsetX = 0;
			  int offsetY = 0;
			  
			  if(width>newWidth){
				  offsetX = (width-newWidth)/2;
			  }else{
				  newWidth  = width;
			  }
			  
			  if(height>newHeight){
				  offsetY = (height-newHeight)/2;
			  }else{
				  newHeight = height;
			  }
			  
			  resizeBmp = Bitmap.createBitmap(bitmap,offsetX,offsetY,newWidth,newHeight);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }finally{
			  if(resizeBmp != bitmap){
				  bitmap.recycle();
			  }
		  }
	      return resizeBmp;
	  }
	  
	  /**
	   * DrawableתBitmap.
	   * @param drawable Ҫת����Drawable
	   * @return Bitmap
	   */
	  public static Bitmap drawableToBitmap(Drawable drawable) {
	        Bitmap bitmap = Bitmap.createBitmap(
	                drawable.getIntrinsicWidth(),
	                drawable.getIntrinsicHeight(),
	                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565
	        );
	        Canvas canvas = new Canvas(bitmap);
	        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
	        drawable.draw(canvas);
	        return bitmap;
	}
	  
	 /**
	  * Bitmap����ת��Drawable����.
	  * @param bitmap Ҫת����Bitmap����
	  * @return Drawable ת����ɵ�Drawable����
	  */
	  public static Drawable bitmapToDrawable(Bitmap bitmap) {
		  BitmapDrawable mBitmapDrawable = null;
		  try {
			if(bitmap==null){
				 return null; 
			  }
			  mBitmapDrawable = new BitmapDrawable(bitmap);   
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  return mBitmapDrawable;
	}
	  
    /**
	  * Bitmap����ת��TransitionDrawable����.
	  * @param bitmap Ҫת����Bitmap����
	  * imageView.setImageDrawable(td);
      * td.startTransition(200);
	  * @return Drawable ת����ɵ�Drawable����
	  */
	public static TransitionDrawable bitmapToTransitionDrawable(Bitmap bitmap) {
		 TransitionDrawable mBitmapDrawable = null;
		  try {
			if(bitmap==null){
				 return null; 
			  }
			  mBitmapDrawable = new TransitionDrawable(new Drawable[] { new ColorDrawable(android.R.color.transparent),new BitmapDrawable(bitmap)});
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  return mBitmapDrawable;
	}
	
	/**
	  * Drawable����ת��TransitionDrawable����.
	  * @param drawable  Ҫת����Drawable����
	  * imageView.setImageDrawable(td);
      * td.startTransition(200);
	  * @return Drawable ת����ɵ�Drawable����
	  */
	public static TransitionDrawable drawableToTransitionDrawable(Drawable drawable) {
		 TransitionDrawable mBitmapDrawable = null;
		  try {
			if(drawable==null){
				 return null; 
			  }
			  mBitmapDrawable = new TransitionDrawable(new Drawable[] { new ColorDrawable(android.R.color.transparent),drawable});
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  return mBitmapDrawable;
	}
  
   /**
    * ��Bitmapת��Ϊbyte[].
    *
    * @param bitmap the bitmap
    * @param mCompressFormat ͼƬ��ʽ Bitmap.CompressFormat.JPEG,CompressFormat.PNG
    * @param needRecycle �Ƿ���Ҫ����
    * @return byte[] ͼƬ��byte[]
    */
	public static byte[] bitmap2Bytes(Bitmap bitmap,Bitmap.CompressFormat mCompressFormat,final boolean needRecycle){  
		byte[] result = null;
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();    
			bitmap.compress(mCompressFormat, 100, output); 
			result = output.toByteArray();
			if (needRecycle) {
				bitmap.recycle();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(output!=null){
				try {
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	    return result;
	 }  
	
	 /**
	  * ��ȡBitmap��С.
	  * @param bitmap the bitmap
	  * @param mCompressFormat ͼƬ��ʽ Bitmap.CompressFormat.JPEG,CompressFormat.PNG
	  * @return ͼƬ�Ĵ�С
	  */
	public static int getByteCount(Bitmap bitmap,Bitmap.CompressFormat mCompressFormat){  
		int size = 0;
		ByteArrayOutputStream output = null;
		try {
			output = new ByteArrayOutputStream();    
			bitmap.compress(mCompressFormat, 100, output); 
			byte[] result = output.toByteArray();
			size = result.length;
			result = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(output!=null){
				try {
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	    return size;
	}  
	
	/**
	 * ��������byte[]ת��ΪBitmap.
	 * @param b ͼƬ��ʽ��byte[]����
	 * @return bitmap �õ���Bitmap
	 */
	public static  Bitmap bytes2Bimap(byte[] b){  
		Bitmap bitmap = null;
        try {
			if(b.length!=0){  
				bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return bitmap;
  }  
	
	/**
	 * ��ImageViewת��ΪBitmap.
	 * @param view Ҫת��Ϊbitmap��View
	 * @return byte[] ͼƬ��byte[]
	 */
	public static Bitmap imageView2Bitmap(ImageView view){  
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(view.getDrawingCache());
			view.setDrawingCacheEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}  
	    return bitmap;
	 }  
	
	
	/**
	 * ��Viewת��ΪDrawable.��Ҫ���ϲ㲼��ΪLinearlayout
	 * @param view Ҫת��ΪDrawable��View
	 * @return BitmapDrawable Drawable
	 */
	public static Drawable view2Drawable(View view){  
		    BitmapDrawable mBitmapDrawable = null;
			try {
				Bitmap newbmp = view2Bitmap(view);
				if(newbmp!=null){
					mBitmapDrawable = new BitmapDrawable(newbmp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mBitmapDrawable;
	 }  
	
	/**
	 * ��Viewת��ΪBitmap.��Ҫ���ϲ㲼��ΪLinearlayout
	 * @param view Ҫת��Ϊbitmap��View
	 * @return byte[] ͼƬ��byte[]
	 */
	public static Bitmap view2Bitmap(View view){  
		Bitmap bitmap = null;
		try {
			if (view != null) {
				view.setDrawingCacheEnabled(true);
				view.measure(
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
				view.buildDrawingCache();
				bitmap = view.getDrawingCache();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	 }  
	
	/**
	 * ��Viewת��Ϊbyte[].
	 *
	 * @param view Ҫת��Ϊbyte[]��View
	 * @param compressFormat the compress format
	 * @return byte[] ViewͼƬ��byte[]
	 */
	public static byte[] view2Bytes(View view,Bitmap.CompressFormat compressFormat){  
		byte[] b = null;
		try {
			Bitmap bitmap = AbImageUtil.view2Bitmap(view);
			b = AbImageUtil.bitmap2Bytes(bitmap,compressFormat,true);
		} catch (Exception e) {
			e.printStackTrace();
		}  
	    return b;
	 } 
	
	/**
	 * ��������תBitmapΪһ���ĽǶ�.
	 *
	 * @param bitmap the bitmap
	 * @param degrees the degrees
	 * @return the bitmap
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap,float degrees){  
		Bitmap mBitmap = null;
		try {
			Matrix m = new Matrix();
			m.setRotate(degrees%360);
			mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),m,false);
		} catch (Exception e) {
			e.printStackTrace();
		}  
	    return mBitmap;
   } 
	
	/**
	 * ��������תBitmapΪһ���ĽǶȲ����ܰ�������.
	 *
	 * @param bitmap the bitmap
	 * @param degrees the degrees
	 * @return the bitmap
	 */
	public static Bitmap rotateBitmapTranslate(Bitmap bitmap,float degrees){  
		Bitmap mBitmap = null;
		int width;
		int height;
		try {
			Matrix matrix = new Matrix();
	        if ((degrees / 90) % 2!= 0) {
	        	width =  bitmap.getWidth();
	        	height =  bitmap.getHeight();
            } else {
            	width = bitmap.getHeight();
            	height =  bitmap.getWidth();
            }
            int cx = width / 2;
            int cy = height/ 2;
            matrix.preTranslate(-cx, -cy);
            matrix.postRotate(degrees);
            matrix.postTranslate(cx, cy);
		} catch (Exception e) {
			e.printStackTrace();
		}  
	    return mBitmap;
   } 
	
	/**
	 * ת��ͼƬת����Բ��.
	 *
	 * @param bitmap ����Bitmap����
	 * @return the bitmap
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		if(bitmap == null){
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
	
	/**
	 * ת��ͼƬת���ɾ���Ч����ͼƬ.
	 *
	 * @param bitmap ����Bitmap����
	 * @return the bitmap
	 */
	public static Bitmap toReflectionBitmap(Bitmap bitmap) {
		if(bitmap == null){
			return null;
		}
		
	    try {
			int reflectionGap = 1;
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();

			// This will not scale but will flip on the Y axis
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);
			
			// Create a Bitmap with the flip matrix applied to it.
			// We only want the bottom half of the image
			Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0,
					height / 2, width, height / 2, matrix, false);

			// Create a new bitmap with same width but taller to fit
			// reflection
			Bitmap bitmapWithReflection = Bitmap.createBitmap(width,(height + height / 2), Config.ARGB_8888);

			// Create a new Canvas with the bitmap that's big enough for
			// the image plus gap plus reflection
			Canvas canvas = new Canvas(bitmapWithReflection);
			// Draw in the original image
			canvas.drawBitmap(bitmap, 0, 0, null);
			// Draw in the gap
			Paint deafaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + reflectionGap,deafaultPaint);
			// Draw in the reflection
			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap,null);
			// Create a shader that is a linear gradient that covers the
			// reflection
			Paint paint = new Paint();
			LinearGradient shader = new LinearGradient(0,
					bitmap.getHeight(), 0,
					bitmapWithReflection.getHeight() + reflectionGap,
					0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			// Set the paint to use this shader (linear gradient)
			paint.setShader(shader);
			// Set the Transfer mode to be porter duff and destination in
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			// Draw a rectangle using the paint with our linear gradient
			canvas.drawRect(0, height, width,bitmapWithReflection.getHeight() + reflectionGap, paint);
			
			bitmap = bitmapWithReflection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	  
   /**
	* �ͷ�Bitmap����.
	* @param bitmap Ҫ�ͷŵ�Bitmap
	*/
	public static void releaseBitmap(Bitmap bitmap){
		if(bitmap!=null){
			try {
				if(!bitmap.isRecycled()){
					if(D) Log.d(TAG, "Bitmap�ͷ�"+bitmap.toString());
					bitmap.recycle();
				}
			} catch (Exception e) {
			}
			bitmap = null;
		}
	}
	
	/**
	* �ͷ�Bitmap����.
	* @param bitmaps Ҫ�ͷŵ�Bitmap����
	*/
	public static void releaseBitmapArray(Bitmap[] bitmaps){
		if(bitmaps!=null){
			try {
				for(Bitmap bitmap:bitmaps){
					if(bitmap!=null && !bitmap.isRecycled()){
						if(D) Log.d(TAG, "Bitmap�ͷ�"+bitmap.toString());
						bitmap.recycle();
					}
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * �������򵥵�ͼ�������ֵ����������ͼ��ԭͼ�ȽϺ�
	 * @param bitmap
	 * @return
	 * @throws 
	 */
    public static String getHashCode(Bitmap bitmap){
    	//��һ������С�ߴ硣
    	//��ͼƬ��С��8x8�ĳߴ磬�ܹ�64�����ء���һ����������ȥ��ͼƬ��ϸ�ڣ�
    	//ֻ�����ṹ�������Ȼ�����Ϣ��������ͬ�ߴ硢����������ͼƬ���졣
    	
        Bitmap temp = Bitmap.createScaledBitmap(bitmap, 8, 8, false);
        
        int width = temp.getWidth();
        int height = temp.getHeight();
        Log.i("th", "��ͼƬ��С��8x8�ĳߴ�:" + width + "*" + height);
        
        //�ڶ������ڶ�������ɫ�ʡ�
        //����С���ͼƬ��תΪ64���Ҷȡ�Ҳ����˵���������ص��ܹ�ֻ��64����ɫ��
 		int[] pixels = new int[width * height];
 		for (int i = 0; i < width; i++) {
 			for (int j = 0; j < height; j++) {
 				pixels[i * height + j] = rgbToGray(temp.getPixel(i, j));
 			}
 		}
 		
 		releaseBitmap(temp);
        
        //������������ƽ��ֵ��
        //��������64�����صĻҶ�ƽ��ֵ��
        int avgPixel = AbMathUtil.average(pixels);
        
        // ���Ĳ����Ƚ����صĻҶȡ�
 		// ��ÿ�����صĻҶȣ���ƽ��ֵ���бȽϡ����ڻ����ƽ��ֵ����Ϊ1��С��ƽ��ֵ����Ϊ0��
 		int[] comps = new int[width * height];
 		for (int i = 0; i < comps.length; i++) {
 			if (pixels[i] >= avgPixel) {
 				comps[i] = 1;
 			} else {
 				comps[i] = 0;
 			}
 		}
 		 
        //���岽�������ϣֵ��
        //����һ���ıȽϽ���������һ�𣬾͹�����һ��64λ��������
        //���������ͼƬ��ָ�ơ�
        StringBuffer hashCode = new StringBuffer();
        for (int i = 0; i < comps.length; i+= 4) {
              int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1] * (int) Math.pow(2, 2)+ comps[i + 2] * (int) Math.pow(2, 1) + comps[i + 2];
              hashCode.append(AbMathUtil.binaryToHex(result));
        }
        String sourceHashCode = hashCode.toString();
        // �õ�ָ���Ժ󣬾Ϳ��ԶԱȲ�ͬ��ͼƬ������64λ���ж���λ�ǲ�һ���ġ�
        //�������ϣ����ͬ�ڼ���"��������"��Hamming distance����
        //�������ͬ������λ������5����˵������ͼƬ�����ƣ��������10����˵���������Ų�ͬ��ͼƬ��
        return sourceHashCode;
    }
    
    
    /**
	 * ������ͼ�������ֵ�������ƶ�
	 * @param bitmap
	 * @return
	 * @throws 
	 */
    public static String getDCTHashCode(Bitmap bitmap){
    	
    	//��ͼƬ��С��32x32�ĳߴ�
    	Bitmap temp = Bitmap.createScaledBitmap(bitmap, 32, 32, false);
    	
        int width = temp.getWidth();
        int height = temp.getHeight();
        Log.i("th", "��ͼƬ��С��32x32�ĳߴ�:" + width + "*" + height);
        
        //��ɫ�ʡ�
 		int[] pixels = new int[width * height];
 		for (int i = 0; i < width; i++) {
 			for (int j = 0; j < height; j++) {
 				pixels[i * height + j] = rgbToGray(temp.getPixel(i, j));
 			}
 		}
 		
 		releaseBitmap(temp);
 		
 		int[][] pxMatrix =  AbMathUtil.arrayToMatrix(pixels, width, height);
        double[][] doublePxMatrix = AbMathUtil.intToDoubleMatrix(pxMatrix);  
        
        //����DCT,�Ѿ����8*8��
        double[][] dtc = FDCT.fDctTransform(doublePxMatrix);
        
        //����ƽ��ֵ��
        double[] dctResult =  AbMathUtil.matrixToArray(dtc);
        int avgPixel = AbMathUtil.average(dctResult);
        
        //�Ƚ����صĻҶȡ�
 		// ��ÿ�����صĻҶȣ���ƽ��ֵ���бȽϡ����ڻ����ƽ��ֵ����Ϊ1��С��ƽ��ֵ����Ϊ0��
 		int[] comps = new int[8 * 8];
 		for (int i = 0; i < comps.length; i++) {
 			if (dctResult[i] >= avgPixel) {
 				comps[i] = 1;
 			} else {
 				comps[i] = 0;
 			}
 		}
 		 
        //�����ϣֵ��
        //����һ���ıȽϽ���������һ�𣬾͹�����һ��64λ��������
        //���������ͼƬ��ָ�ơ�
        StringBuffer hashCode = new StringBuffer();
        for (int i = 0; i < comps.length; i+= 4) {
              int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1] * (int) Math.pow(2, 2)+ comps[i + 2] * (int) Math.pow(2, 1) + comps[i + 2];
              hashCode.append(AbMathUtil.binaryToHex(result));
        }
        String sourceHashCode = hashCode.toString();
        // �õ�ָ���Ժ󣬾Ϳ��ԶԱȲ�ͬ��ͼƬ������64λ���ж���λ�ǲ�һ���ġ�
        //�������ϣ����ͬ�ڼ���"��������"��Hamming distance����
        //�������ͬ������λ������5����˵������ͼƬ�����ƣ��������10����˵���������Ų�ͬ��ͼƬ��
        return sourceHashCode;
    }
    
    /**
	 * ������ͼ�������ֵ��ɫ�ֲ�
	 * ����ɫ��4������0,1,2,3   ����Ϲ�64�飬����ÿ�����ص������ĸ���
	 * @param bitmap
	 * @return
	 */
    public static int[] getColorHistogram(Bitmap bitmap){
    	
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //����ɫ�ֲ�
        int [] areaColor = new int[64];
        
        //��ȡɫ�����顣
 		for (int i = 0; i < width; i++) {
 			for (int j = 0; j < height; j++) {
 				int pixels = bitmap.getPixel(i, j);
 				int alpha = (pixels >> 24) & 0xFF;
 				int red = (pixels >> 16) & 0xFF;
 				int green = (pixels >> 8) & 0xFF;
 				int blue = (pixels) & 0xFF;
 				int redArea = 0;
 				int greenArea = 0;
 				int blueArea = 0;
 				//0-63	64-127	128-191	192-255
 				if(red>=192){
 					redArea = 3;
 				}else if(red>=128 ){
 					redArea = 2;
 				}else if(red>=64 ){
 					redArea = 1;
 				}else if(red>=0 ){
 					redArea = 0;
 				}
 				
 				if(green>=192){
 					greenArea = 3;
 				}else if(green>=128 ){
 					greenArea = 2;
 				}else if(green>=64 ){
 					greenArea = 1;
 				}else if(green>=0 ){
 					greenArea = 0;
 				}
 				
 				if(blue>=192){
 					blueArea = 3;
 				}else if(blue>=128 ){
 					blueArea = 2;
 				}else if(blue>=64 ){
 					blueArea = 1;
 				}else if(blue>=0 ){
 					blueArea = 0;
 				}
 				int index = redArea*16+greenArea*4+blueArea;
 				//����
 				areaColor[index] +=1;
 			}
 		}
        return areaColor;
    }
    
    /**
	 * ����"��������"��Hamming distance����
	 * �������ͬ������λ������5����˵������ͼƬ�����ƣ��������10����˵���������Ų�ͬ��ͼƬ��
	 * @param sourceHashCode ԴhashCode
	 * @param hashCode ��֮�Ƚϵ�hashCode
	 */
	public static int hammingDistance(String sourceHashCode, String hashCode) {
		int difference = 0;
		int len = sourceHashCode.length();
		for (int i = 0; i < len; i++) {
			if (sourceHashCode.charAt(i) != hashCode.charAt(i)) {
				difference ++;
			} 
		}
		return difference;
	}
	
	/**
	 * �Ҷ�ֵ����
	 * @param pixels ����
	 * @return int �Ҷ�ֵ
	 */
	private static int rgbToGray(int pixels) {
		// int _alpha = (pixels >> 24) & 0xFF;
		int _red = (pixels >> 16) & 0xFF;
		int _green = (pixels >> 8) & 0xFF;
		int _blue = (pixels) & 0xFF;
		return (int) (0.3 * _red + 0.59 * _green + 0.11 * _blue);
	}
	
    
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
    	
    	
		//System.out.println(getHashCode(""));
	}

}
