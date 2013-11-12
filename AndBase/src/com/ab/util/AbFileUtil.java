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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.ab.global.AbAppData;
import com.ab.global.AbConstant;

// TODO: Auto-generated Javadoc
/**
 * �������ļ�������.
 *
 * @author zhaoqp
 * @date 2011-12-10
 * @version v1.0
 */
public class AbFileUtil {
	
	/** The tag. */
	private static String TAG = "AbFileUtil";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	/** Ĭ�������ļ���ַ. */
	private static  String downPathRootDir = File.separator + "download" + File.separator;
	
    /** Ĭ������ͼƬ�ļ���ַ. */
	private static  String downPathImageDir = downPathRootDir + "cache_images" + File.separator;
    
    /** Ĭ�������ļ���ַ. */
	private static  String downPathFileDir = downPathRootDir + "cache_files" + File.separator;
    
	/**MB  ��λB*/
	private static int MB = 1024*1024;
	
	/**�����ļ��еĴ�С100M  ��λB*/
	private static int cacheSize = 100*MB;
	
    /**ʣ��ռ����200M��ʹ�û���*/
	private static int freeSdSpaceNeededToCache = 200*MB;
	
	/**����ռ䵱ǰ�Ĵ�С����ʱ*/
	private static int dirSize = -1;
	
	/**ͳ�Ƴ������ص�ͼƬ��������10�ż��sd��*/
	private static int downCount = 0;
	/**
	 * ���������ļ���SD����.���SD�д���ͬ���ļ�����������
	 * @param url Ҫ�����ļ��������ַ
	 * @return ���غõı����ļ���ַ
	 */
	 public static String downFileToSD(String url,String name){
		 InputStream in = null;
		 FileOutputStream fileOutputStream = null;
		 HttpURLConnection con = null;
		 String downFilePath = null;
		 File file = null;
		 try {
	    	if(!isCanUseSD()){
	    		return null;
	    	}
	    	File path = Environment.getExternalStorageDirectory();
	    	File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
			if(!fileDirectory.exists()){
				fileDirectory.mkdirs();
			}
			
			file = new File(fileDirectory,name);
			if(!file.exists()){
				file.createNewFile();
			}else{
				//�ļ��Ѿ�����
				if(file.length()!=0){
					return file.getPath();
				}
			}
			downFilePath = file.getPath();
			URL mUrl = new URL(url);
			con = (HttpURLConnection)mUrl.openConnection();
			con.connect();
			in = con.getInputStream();
			fileOutputStream = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int temp = 0;
			while((temp=in.read(b))!=-1){
				fileOutputStream.write(b, 0, temp);
			}
		}catch(Exception e){
			if(D)Log.d(TAG, ""+e.getMessage());
			return null;
		}finally{
			try {
				if(in!=null){
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(fileOutputStream!=null){
					fileOutputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(con!=null){
					con.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				//����ļ���С,����ļ�Ϊ0B˵�����粻��û�����سɹ���Ҫ�������Ŀ��ļ�ɾ��
				if(file.length() == 0){
					file.delete();
				}else{
					downCount ++;
				}
				if(downCount >= 10){
					//���ռ佫�ܾ�δʹ�õ��ļ�ɾ��
					removeCache();
					downCount = 0;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return downFilePath;
	 }
	 
	 /**
	  * ������ͨ���ļ��������ַ��SD���ж�ȡͼƬ�����SD��û�����Զ����ز�����.
	  * @param url �ļ��������ַ
	  * @param type ͼƬ�Ĵ������ͣ����л������ŵ�ָ����С���ο�AbConstant�ࣩ
	  * �������Ϊԭͼ�����߲�����Ч���õ�ԭͼ
	  * @param newWidth ��ͼƬ�Ŀ�
	  * @param newHeight ��ͼƬ�ĸ�
	  * @return Bitmap ��ͼƬ
	  */
	 public static Bitmap getBitmapFromSDCache(String url,int type,int newWidth, int newHeight){
		 Bitmap bit = null;
		 try {
			 if(AbStrUtil.isEmpty(url)){
		    	return null;
		     }
			 
			 //SD�������� ����ʣ��ռ䲻���˾Ͳ����浽SD����
			 if(!isCanUseSD() || freeSdSpaceNeededToCache < freeSpaceOnSD()){
				 bit = getBitmapFormURL(url,type,newWidth,newHeight);
				 return bit;
		     }
			 
			 if(type != AbConstant.ORIGINALIMG && ( newWidth<=0 || newHeight<=0)){
				 throw new IllegalArgumentException("���źͲü�ͼƬ�Ŀ�����ò���С��0");
			 }
			 
			 //�ļ��Ƿ����
			 File path = Environment.getExternalStorageDirectory();
			 File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
			 
			 //��ȡ��׺
			 String suffix = getSuffixFromUrl(url);
			 
			 //�����ͼƬ�ļ��������
			 String fileName = getImageFileName(url,newWidth,newHeight,type);
			 File file = new File(fileDirectory,fileName+suffix);
			 if(!file.exists()){
				 downFileToSD(url,file.getName());
				 return getBitmapFromSD(file,type,newWidth,newHeight);
			 }else{
				 //�ļ�����
				 if(type == AbConstant.CUTIMG){
			 		bit = AbImageUtil.cutImg(file,newWidth,newHeight);
			 	 }else if(type == AbConstant.SCALEIMG){
			 		bit = AbImageUtil.scaleImg(file,newWidth,newHeight);
			 	 }else{
			 		bit = AbImageUtil.originalImg(file);
			 	 }
			 	 
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bit;
		
	 }
	 
	 /**
	  * ������ͨ���ļ��������ַ��SD���ж�ȡͼƬ.
	  * @param url �ļ��������ַ
	  * @param type ͼƬ�Ĵ������ͣ����л������ŵ�ָ����С���ο�AbConstant�ࣩ
	  * �������Ϊԭͼ�����߲�����Ч���õ�ԭͼ
	  * @param newWidth ��ͼƬ�Ŀ�
	  * @param newHeight ��ͼƬ�ĸ�
	  * @return Bitmap ��ͼƬ
	  */
	 public static Bitmap getBitmapFromSD(String url,int type,int newWidth, int newHeight){
		 Bitmap bit = null;
		 try {
			 //SD���Ƿ����
			 if(!isCanUseSD()){
				 return null;
		     }
			 
			 if(type != AbConstant.ORIGINALIMG && ( newWidth<=0 || newHeight<=0)){
				 throw new IllegalArgumentException("���źͲü�ͼƬ�Ŀ�����ò���С��0");
			 }
			 
			 //�ļ��Ƿ����
			 File path = Environment.getExternalStorageDirectory();
			 File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
			 String fileName = getImageFileName(url,newWidth,newHeight,type);
			 File file = new File(fileDirectory,fileName);
			 if(!file.exists()){
				 return null;
			 }else{
				 //�ļ�����
				 if(type == AbConstant.CUTIMG){
			 		bit = AbImageUtil.cutImg(file,newWidth,newHeight);
			 	 }else if(type == AbConstant.SCALEIMG){
				 	bit = AbImageUtil.scaleImg(file,newWidth,newHeight);
			 	 }else{
			 		bit = AbImageUtil.originalImg(file);
			 	 }
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bit;
		
	 }
	 
	 /**
 	 * ������ͨ���ļ��ı��ص�ַ��SD����ȡͼƬ.
 	 *
 	 * @param file the file
 	 * @param type ͼƬ�Ĵ������ͣ����л������ŵ�ָ����С���ο�AbConstant�ࣩ
 	 * �������Ϊԭͼ�����߲�����Ч���õ�ԭͼ
 	 * @param newWidth ��ͼƬ�Ŀ�
 	 * @param newHeight ��ͼƬ�ĸ�
 	 * @return Bitmap ��ͼƬ
 	 */
	 public static Bitmap getBitmapFromSD(File file,int type,int newWidth, int newHeight){
		 Bitmap bit = null;
		 try {
			 //SD���Ƿ����
			 if(!isCanUseSD()){
		    	return null;
		     }
			 
			 if(type != AbConstant.ORIGINALIMG && ( newWidth<=0 || newHeight<=0)){
				 throw new IllegalArgumentException("���źͲü�ͼƬ�Ŀ�����ò���С��0");
			 }
			 
			 //�ļ��Ƿ����
			 if(!file.exists()){
				 return null;
			 }
			 //�ļ�����
			 if(type==AbConstant.CUTIMG){
		 		bit = AbImageUtil.cutImg(file,newWidth,newHeight);
		 	 }else if(type == AbConstant.SCALEIMG){
			 	bit = AbImageUtil.scaleImg(file,newWidth,newHeight);
		 	 }else{
		 		bit = AbImageUtil.originalImg(file);
		 	 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bit;
	 }
	 
	 /**
 	 * ������ͨ���ļ��ı��ص�ַ��SD����ȡͼƬ.
 	 *
 	 * @param file the file
 	 * @return Bitmap ͼƬ
 	 */
	 public static Bitmap getBitmapFromSD(File file){
		 Bitmap bitmap = null;
		 try {
			 //SD���Ƿ����
			 if(!isCanUseSD()){
		    	return null;
		     }
			 //�ļ��Ƿ����
			 if(!file.exists()){
				 return null;
			 }
			 //�ļ�����
			 bitmap = AbImageUtil.originalImg(file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	 }
	 
	 /**
	  * ��������ͼƬ��byte[]д�뱾���ļ�.
	  * @param imgByte ͼƬ��byte[]����
	  * @param fileName �ļ����ƣ���Ҫ������׺����.jpg
	  * @param type ͼƬ�Ĵ������ͣ����л������ŵ�ָ����С���ο�AbConstant�ࣩ
	  * @param newWidth ��ͼƬ�Ŀ�
	  * @param newHeight ��ͼƬ�ĸ�
	  * @return Bitmap ��ͼƬ
	  */
     public static Bitmap getBitmapFormByte(byte[] imgByte,String fileName,int type,int newWidth, int newHeight){
    	   FileOutputStream fos = null;
    	   DataInputStream dis = null;
    	   ByteArrayInputStream bis = null;
    	   Bitmap b = null;
    	   File file = null;
    	   try {
    		   if(imgByte!=null){
    			   File sdcardDir = Environment.getExternalStorageDirectory();
    			   String path = sdcardDir.getAbsolutePath()+downPathImageDir;
    			   file = new File(path+fileName);
    				 
    			   if(!file.getParentFile().exists()){
    			          file.getParentFile().mkdirs();
    			   }
    			   if(!file.exists()){
    			          file.createNewFile();
    			   }
    			   fos = new FileOutputStream(file);
    			   int readLength = 0;
    			   bis = new ByteArrayInputStream(imgByte);
    			   dis = new DataInputStream(bis);
    			   byte[] buffer = new byte[1024];
    			   
    			   while ((readLength = dis.read(buffer))!=-1) {
    				   fos.write(buffer, 0, readLength);
    			       try {
    						Thread.sleep(500);
    				   } catch (Exception e) {
    				   }
    			   }
    			   fos.flush();
    			   
    			   b = getBitmapFromSD(file,type,newWidth,newHeight);
    		   }
			   
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(dis!=null){
				try {
					dis.close();
				} catch (Exception e) {
				}
			}    
			if(bis!=null){
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
        return  b;
     }
	    
	/**
	 * ����������URL�ӻ�������ȡͼƬ.
	 * @param url Ҫ�����ļ��������ַ
	 * @param type ͼƬ�Ĵ������ͣ����л������ŵ�ָ����С���ο�AbConstant�ࣩ
	 * @param newWidth ��ͼƬ�Ŀ�
	 * @param newHeight ��ͼƬ�ĸ�
	 * @return Bitmap ��ͼƬ
	 */
	public static Bitmap getBitmapFormURL(String url,int type,int newWidth, int newHeight){
		Bitmap bit = null;
		try {
			bit = AbImageUtil.getBitmapFormURL(url, type, newWidth, newHeight);
	    } catch (Exception e) {
	    	 if(D)Log.d(TAG, "����ͼƬ�쳣��"+e.getMessage());
		}
	    if(D)Log.d(TAG, "���ص�Bitmap��"+bit);
		return bit;
	}
	
	/**
	 * ��������ȡsrc�е�ͼƬ��Դ.
	 *
	 * @param src ͼƬ��src·�����磨��image/arrow.png����
	 * @return Bitmap ͼƬ
	 */
	public static Bitmap getBitmapFormSrc(String src){
		Bitmap bit = null;
		try {
			bit = BitmapFactory.decodeStream(AbFileUtil.class.getResourceAsStream(src));
	    } catch (Exception e) {
	    	 if(D)Log.d(TAG, "��ȡͼƬ�쳣��"+e.getMessage());
		}
	    if(D)Log.d(TAG, "���ص�Bitmap��"+bit);
		return bit;
	}
	
	/**
	 * ��������ȡ�����ļ��Ĵ�С.
	 *
	 * @param Url ͼƬ������·��
	 * @return int �����ļ��Ĵ�С
	 */
	public static int getContentLengthFormUrl(String Url){
		int mContentLength = 0;
		try {
			 URL url = new URL(Url);
			 HttpURLConnection mHttpURLConnection = (HttpURLConnection) url.openConnection();
			 mHttpURLConnection.setConnectTimeout(5 * 1000);
			 mHttpURLConnection.setRequestMethod("GET");
			 mHttpURLConnection.setRequestProperty("Accept","image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			 mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
			 mHttpURLConnection.setRequestProperty("Referer", Url);
			 mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
			 mHttpURLConnection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			 mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			 mHttpURLConnection.connect();
			 if (mHttpURLConnection.getResponseCode() == 200){
				 // ������Ӧ��ȡ�ļ���С
				 mContentLength = mHttpURLConnection.getContentLength();
			 }
	    } catch (Exception e) {
	    	 e.printStackTrace();
	    	 if(D)Log.d(TAG, "��ȡ�����쳣��"+e.getMessage());
		}
		return mContentLength;
	}
	
	 
	/**
	 * ��ȡ�ļ�����ͨ�������ȡ.
	 * @param url �ļ���ַ
	 * @return �ļ���
	 */
	public static String getFileNameFromNetUrl(String url){
		String name = null;
		try {
			if(AbStrUtil.isEmpty(url)){
				return name;
			}
			
			URL mUrl = new URL(url);
			HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
			mHttpURLConnection.setConnectTimeout(5 * 1000);
			mHttpURLConnection.setRequestMethod("GET");
			mHttpURLConnection.setRequestProperty("Accept","image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
			mHttpURLConnection.setRequestProperty("Referer", url);
			mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
			mHttpURLConnection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			mHttpURLConnection.connect();
			if (mHttpURLConnection.getResponseCode() == 200){
				for (int i = 0;; i++) {
						String mine = mHttpURLConnection.getHeaderField(i);
						if (mine == null){
							break;
						}
						if ("content-disposition".equals(mHttpURLConnection.getHeaderFieldKey(i).toLowerCase())) {
							Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
							if (m.find())
								return m.group(1).replace("\"", "");
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
    }
	
	/**
	 * ��ȡ�ļ�����ͨ�������ȡ.
	 * @param url �ļ���ַ
	 * @return �ļ���
	 */
	public static String getFileNameFromUrl(String url){
		if(AbStrUtil.isEmpty(url)){
			return null;
		}
		String name = null;
		try {
			name = AbMd5.MD5(url)+getSuffixFromUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
    }
	
	/**
	 * ��ȡ�ļ�����ͨ�������ȡ.
	 * @param url �ļ���ַ
	 * @param suffix .tmp
	 * @return �ļ���
	 */
	public static String getFileNameFromUrl(String url,String suffix){
		if(AbStrUtil.isEmpty(url)){
			return null;
		}
		String name = null;
		try {
			name = AbMd5.MD5(url)+suffix;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
    }
	
	/**
	 * ��ȡ�ļ�����ͨ�������ȡ.
	 * @param url �ļ���ַ
	 * @param suffix .tmp
	 * @return �ļ���
	 */
	public static String getSuffixFromUrl(String url){
		if(AbStrUtil.isEmpty(url)){
			return null;
		}
		String suffix = ".tmp";
		try {
			//��ȡ��׺
			if(url.lastIndexOf(".")!=-1){
				 suffix = url.substring(url.lastIndexOf("."));
				 if(suffix.indexOf("/")!=-1){
					 suffix = ".tmp";
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return suffix;
    }
	
	/**
	 * ��������sd���е��ļ���ȡ��byte[].
	 *
	 * @param path sd�����ļ�·��
	 * @return byte[]
	 */
	public static byte[] getByteArrayFromSD(String path) {  
		byte[] bytes = null; 
		ByteArrayOutputStream out = null;
	    try {
	    	File file = new File(path);  
	    	//SD���Ƿ����
			if(!isCanUseSD()){
				 return null;
		    }
			//�ļ��Ƿ����
			if(!file.exists()){
				 return null;
			}
	    	
	    	long fileSize = file.length();  
	    	if (fileSize > Integer.MAX_VALUE) {  
	    	      return null;  
	    	}  

			FileInputStream in = new FileInputStream(path);  
		    out = new ByteArrayOutputStream(1024);  
			byte[] buffer = new byte[1024];  
			int size=0;  
			while((size=in.read(buffer))!=-1) {  
			   out.write(buffer,0,size);  
			}  
			in.close();  
            bytes = out.toByteArray();  
   
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(out!=null){
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	    return bytes;
    }  
	
	/**
	 * ��������byte����д���ļ�.
	 *
	 * @param path the path
	 * @param content the content
	 * @param create the create
	 */
	 public static void writeByteArrayToSD(String path, byte[] content,boolean create){  
	    
		 FileOutputStream fos = null;
		 try {
	    	File file = new File(path);  
	    	//SD���Ƿ����
			if(!isCanUseSD()){
				 return;
		    }
			//�ļ��Ƿ����
			if(!file.exists()){
				if(create){
					File parent = file.getParentFile();
					if(!parent.exists()){
						parent.mkdirs();
						file.createNewFile();
					}
				}else{
				    return;
				}
			}
			fos = new FileOutputStream(path);  
			fos.write(content);  
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
   }  
	 
	/**
	 * ������SD���Ƿ�����.
	 *
	 * @return true ����,false������
	 */
	public static boolean isCanUseSD() { 
	    try { 
	        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return false; 
    } 

	/**
	 * ��������õ�ǰ���صĵ�ַ.
	 * @return ���صĵ�ַ��Ĭ��SD��download��
	 */
	public static String getDownPathImageDir() {
		return downPathImageDir;
	}

	/**
	 * ����������ͼƬ�ļ������ر���·����Ĭ��SD��download/cache_images��.
	 * @param downPathImageDir ͼƬ�ļ������ر���·��
	 */
	public static void setDownPathImageDir(String downPathImageDir) {
		AbFileUtil.downPathImageDir = downPathImageDir;
	}

	
	/**
	 * Gets the down path file dir.
	 *
	 * @return the down path file dir
	 */
	public static String getDownPathFileDir() {
		return downPathFileDir;
	}

	/**
	 * �����������ļ������ر���·����Ĭ��SD��download/cache_files��.
	 * @param downPathFileDir �ļ������ر���·��
	 */
	public static void setDownPathFileDir(String downPathFileDir) {
		AbFileUtil.downPathFileDir = downPathFileDir;
	}
	
	/**
	 * ��������ȡĬ�ϵ�ͼƬ����ȫ·��.
	 *
	 * @return the default image down path dir
	 */
	public static String getDefaultImageDownPathDir(){
		String pathDir = null;
	    try {
			if(!isCanUseSD()){
				return null;
			}
			//��ʼ��ͼƬ����·��
			File fileRoot = Environment.getExternalStorageDirectory();
			File dirFile = new File(fileRoot.getAbsolutePath() + AbFileUtil.downPathImageDir);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			pathDir = dirFile.getPath();
		} catch (Exception e) {
		}
	    return pathDir;
	}
	
	/**
    * ����洢Ŀ¼�µ��ļ���С��
    * ���ļ��ܴ�С���ڹ涨��CACHE_SIZE����sdcardʣ��ռ�С��FREE_SD_SPACE_NEEDED_TO_CACHE�Ĺ涨
    * ��ôɾ��40%���û�б�ʹ�õ��ļ�
    */
    public static boolean removeCache() {
    	
       try {
		   if(!isCanUseSD()){
				return false;
		   }
		   
		   File path = Environment.getExternalStorageDirectory();
		   File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
	       File[] files = fileDirectory.listFiles();
	       if (files == null) {
	            return true;
	       }
	       if(dirSize==-1){
	    	   dirSize+=1;
	    	   for (int i = 0; i < files.length; i++) {
		            dirSize += files[i].length();
		       }
	       }
	       
	       //��ǰ��С����Ԥ������ռ�
	       if (dirSize > cacheSize) {
	           int removeFactor = (int) ((0.4 * files.length) + 1);
	           Arrays.sort(files, new FileLastModifSort());
	           for (int i = 0; i < removeFactor; i++) {
	        	   dirSize -= files[i].length();
	               files[i].delete();
	           }
	        }
	       
	   } catch (Exception e) {
		   e.printStackTrace();
		   return false;
	   }
                                                       
       return true;
    }
	
    
    /**
     * ����sdcard�ϵ�ʣ��ռ�
     */
    public static int freeSpaceOnSD() {
       StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
       double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
       return (int) sdFreeMB;
    }
	
    /**
     * �����ļ�������޸�ʱ���������
     */
    public static class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * 
     * �����������ļ��еĴ�С
     * @return
     * @throws 
     */
	public static int getCacheSize() {
		return cacheSize;
	}

	/**
	 * 
	 * ���������û����ļ��еĴ�С
	 * @param cacheSize   B
	 * @throws 
	 */
	public static void setCacheSize(int cacheSize) {
		AbFileUtil.cacheSize = cacheSize;
	}

	/**
	 * 
	 * ������ʣ��ռ���ڶ���B��ʹ�û���
	 * @return
	 * @throws 
	 */
	public static int getFreeSdSpaceNeededToCache() {
		return freeSdSpaceNeededToCache;
	}

	/**
	 * 
	 * ������ʣ��ռ���ڶ���B��ʹ�û���
	 * @param freeSdSpaceNeededToCache
	 * @throws 
	 */
	public static void setFreeSdSpaceNeededToCache(int freeSdSpaceNeededToCache) {
		AbFileUtil.freeSdSpaceNeededToCache = freeSdSpaceNeededToCache;
	}
	
	/**
     * ɾ�����л����ļ�
    */
    public static boolean removeAllFileCache() {
    	
       try {
		   if(!isCanUseSD()){
				return false;
		   }
		   
		   File path = Environment.getExternalStorageDirectory();
		   File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
	       File[] files = fileDirectory.listFiles();
	       if (files == null) {
	            return true;
	       }
           for (int i = 0; i < files.length; i++) {
               files[i].delete();
           }
	   } catch (Exception e) {
		   e.printStackTrace();
		   return false;
	   }
       return true;
    }
    
    /**
     * ����url����ͼƬ�ļ�����.
     * @param url ͼƬ��ַ.
     * @param width ͼƬ���.
     * @param height ͼƬ�߶�.
     * @param type ��������.
     */
    public static String getImageFileName(String url, int width, int height,int type) {
        return AbMd5.MD5(new StringBuilder(url.length() + 12).append("#W").append(width)
        .append("#H").append(height).append("#T").append(type).append(url).toString());
    }
    
}
