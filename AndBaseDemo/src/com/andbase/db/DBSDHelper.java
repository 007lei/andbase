package com.andbase.db;

import android.content.Context;

import com.ab.db.orm.AbSDDBHelper;
import com.andbase.demo.model.LocalUser;
import com.andbase.demo.model.Stock;
import com.andbase.friend.ChatMsg;
import com.andbase.model.User;

public class DBSDHelper extends AbSDDBHelper {
	// ���ݿ���
	private static final String DBNAME = "andbasedemo.db";
	// ���ݿ� ���·��
    private static final String DBPATH = "AndBaseDemoDB";
    
    // ��ǰ���ݿ�İ汾
	private static final int DBVERSION = 1;
	// Ҫ��ʼ���ı�
	private static final Class<?>[] clazz = { User.class,LocalUser.class,Stock.class,ChatMsg.class};

	public DBSDHelper(Context context) {
		super(context,DBPATH, DBNAME, null, DBVERSION, clazz);
	}

}



