package com.andbase.demo.dao;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.andbase.db.DBSDHelper;
import com.andbase.demo.model.LocalUser;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�UserDao.java 
 * �������������ݿ���sd��
 * @author zhaoqp
 * @date��2013-7-31 ����4:13:02
 * @version v1.0
 */
public class UserSDDao extends AbDBDaoImpl<LocalUser> {
	public UserSDDao(Context context) {
		super(new DBSDHelper(context),LocalUser.class);
	}
}
