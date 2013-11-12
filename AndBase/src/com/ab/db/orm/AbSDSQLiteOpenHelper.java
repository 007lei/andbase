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
package com.ab.db.orm;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�SDSQLiteOpenHelper.java 
 * ������SD���б������ݿ�
 * @author zhaoqp
 * @date��2013-7-23 ����9:47:10
 * @version v1.0
 */
public abstract class AbSDSQLiteOpenHelper extends SQLiteOpenHelper{
	
    /** ��־���. */
    private static final String TAG = "SDSQLiteOpenHelper";
    
    /** Ӧ��Context. */
    private final Context mContext;
    
    /** ���ݿ���. */
    private final String mName;
    
    /** Ҫ�ŵ�SDCard�µ��ļ���·��. */
    private final String mPath;
    
    /** ���ݿ��ѯ���α깤��. */
    private final SQLiteDatabase.CursorFactory mFactory;
    
    /** ���ݿ���°汾��. */
    private final int mNewVersion;
    
    /** ���ݿ����. */
    private SQLiteDatabase mDatabase = null;
    
    /** �Ƿ��Ѿ���ʼ����. */
    private boolean mIsInitializing = false;
    
    
    /**
     * ��ʼ��һ��AbSDSQLiteOpenHelper����.
     *
     * @param context Ӧ��Context
     * @param path Ҫ�ŵ�SDCard�µ��ļ���·��
     * @param name ���ݿ���
     * @param factory ���ݿ��ѯ���α깤��
     * @param version ���ݿ���°汾��
     */
    public AbSDSQLiteOpenHelper(Context context,String path, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		if (version < 1) throw new IllegalArgumentException("Version must be >= 1, was " + version);
		mContext = context;
	    mPath = path;
	    mName = name;
	    mFactory = factory;
	    mNewVersion = version;
	}

    /**
     * ��ȡ��дȨ�޵����ݿ����.
     *
     * @return ���ݿ����
     */
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
        	//�Ѿ���ȡ��
        	return mDatabase;  
        }
        if (mIsInitializing) {
            throw new IllegalStateException("���ݿ��ѱ�ռ��getWritableDatabase()");
        }
        boolean success = false;
        SQLiteDatabase db = null;
        try {
            mIsInitializing = true;
            if (mName == null) {
            	//����һ���ڴ�֧��SQLite���ݿ�
                db = SQLiteDatabase.create(null);
            } else {
            	//����һ���ļ�֧��SQLite���ݿ�
                String path = getDatabasePath(mPath,mName).getPath();
                db = SQLiteDatabase.openOrCreateDatabase(path,mFactory);
            }
            int version = db.getVersion();
            if (version != mNewVersion) {
                db.beginTransaction();
                try {
                    if (version == 0) {
                        onCreate(db);
                    } else {
                        onUpgrade(db, version, mNewVersion);
                    }
                    db.setVersion(mNewVersion);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
            onOpen(db);
            success = true;
            return db;
        } finally {
        	//�ͷ�ռ��
            mIsInitializing = false;
            if (success) {
                if (mDatabase != null) {
                    try { 
                    	mDatabase.close(); 
                    } catch (Exception e) {
                    }
                }
                mDatabase = db;
            } else {
                if (db != null) db.close();
            }
        }
    }

    /**
     * ��ȡ�ɶ�Ȩ�޵����ݿ����..
     *
     * @return ���ݿ����
     */
    public synchronized SQLiteDatabase getReadableDatabase() {
        if (mDatabase != null && mDatabase.isOpen()) {
        	//�Ѿ���ȡ��
            return mDatabase; 
        }
        if (mIsInitializing) {
            throw new IllegalStateException("���ݿ��ѱ�ռ��getReadableDatabase()");
        }
        
        //����д��ȡд�����ݿ�
        SQLiteDatabase db = null;
        
        try {
			 db = getWritableDatabase();
			 mDatabase = db;
		} catch (Exception e1) {
			try {
	            mIsInitializing = true;
	            String path = getDatabasePath(mPath,mName).getPath();
	            db = SQLiteDatabase.openDatabase(path, mFactory, SQLiteDatabase.OPEN_READONLY);
	            if (db.getVersion() != mNewVersion) {
	                throw new SQLiteException("���ܸ���ֻ�����ݿ�İ汾 from version " +
	                        db.getVersion() + " to " + mNewVersion + ": " + path);
	            }
	            onOpen(db);
	            mDatabase = db;
	            return mDatabase;
	        }catch (SQLiteException e) {
	        	
	        } finally {
	            mIsInitializing = false;
	            if (db != null && db != mDatabase) db.close();
	        }
		}
        
        return mDatabase;
    }
    
    /**
     * ���ݿⱻ��.
     *
     * @param db ���򿪵����ݿ�
     */
    public void onOpen(SQLiteDatabase db) {}

    /**
     * ���ݿⱻ�ر�.
     *
     */
    public synchronized void close() {
        if (mIsInitializing) throw new IllegalStateException("Closed during initialization");
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    /**
     * ���ݿⱻ�����¼�.
     *
     * @param db �����������ݿ�
     */
    public abstract void onCreate(SQLiteDatabase db);
    
    /**
     * ���ݿⱻ�ؽ�.
     *
     * @param db �����������ݿ�
     * @param oldVersion ԭ�������ݿ�汾
     * @param newVersion �µ����ݿ�汾
     */
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    

    /**
     * ��ȡ���ݿ��ļ�.
     *
     * @param dbpath ���ݿ��ļ�·��
     * @param dbName ���ݿ��ļ�����
     * @return ���ݿ��ļ�
     */
    public File getDatabasePath(String dbpath,String dbName){
    	
    	dbpath  = AbStrUtil.parseEmpty(dbpath);
    	//����Ŀ¼
        File path = new File(Environment.getExternalStorageDirectory() + "/" + dbpath);
        // �����ļ�
        File f = new File(path.getPath(),dbName);
        // Ŀ¼���ڷ���false
        if (!path.exists()) {
        	// ����Ŀ¼
            path.mkdirs();
        }
        // �ļ����ڷ���false
        if (!f.exists()) {
            try {
            	//�����ļ�
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }
}
