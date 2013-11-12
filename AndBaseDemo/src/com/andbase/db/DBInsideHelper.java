package com.andbase.db;

import android.content.Context;

import com.ab.db.orm.AbDBHelper;
import com.andbase.demo.model.LocalUser;
import com.andbase.demo.model.Stock;
import com.andbase.friend.ChatMsg;
import com.andbase.model.User;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�DBInsideHelper.java 
 * �������ֻ�data/data��������ݿ�
 * @author zhaoqp
 * @date��2013-7-31 ����3:50:18
 * @version v1.0
 */
public class DBInsideHelper extends AbDBHelper {
	// ���ݿ���
	private static final String DBNAME = "andbasedemo.db";
    
    // ��ǰ���ݿ�İ汾
	private static final int DBVERSION = 1;
	// Ҫ��ʼ���ı�
	private static final Class<?>[] clazz = { User.class,LocalUser.class,Stock.class,ChatMsg.class};

	public DBInsideHelper(Context context) {
		super(context, DBNAME, null, DBVERSION, clazz);
	}

}



