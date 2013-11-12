package com.andbase.demo.dao;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.andbase.db.DBInsideHelper;
import com.andbase.demo.model.LocalUser;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�UserInsideDao.java 
 * �������������ݿ� ��data����
 * @author zhaoqp
 * @date��2013-7-31 ����4:12:36
 * @version v1.0
 */
public class UserInsideDao extends AbDBDaoImpl<LocalUser> {
	public UserInsideDao(Context context) {
		super(new DBInsideHelper(context),LocalUser.class);
	}
}
