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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

// TODO: Auto-generated Javadoc
/**
 * The Class AbSDDBHelper.
 */
public class AbSDDBHelper extends AbSDSQLiteOpenHelper {
	
	/** The model classes. */
	private Class<?>[] modelClasses;

	/**
	 * ��ʼ��һ��AbSDDBHelper.
	 *
	 * @param context Ӧ��context
	 * @param path Ҫ�ŵ�SDCard�µ��ļ���·��
	 * @param databaseName ���ݿ��ļ���
	 * @param factory ���ݿ��ѯ���α깤��
	 * @param databaseVersion ���ݿ���°汾��
	 * @param modelClasses Ҫ��ʼ���ı�Ķ���
	 */
	public AbSDDBHelper(Context context, String path,String databaseName,
			SQLiteDatabase.CursorFactory factory, int databaseVersion,
			Class<?>[] modelClasses) {
        super(context, path,databaseName, null, databaseVersion);
		this.modelClasses = modelClasses;

	}

    /**
     * ��������Ĵ���.
     *
     * @param db ���ݿ����
     * @see com.ab.db.orm.AbSDSQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    public void onCreate(SQLiteDatabase db) {
		AbTableHelper.createTablesByClasses(db, this.modelClasses);
	}

	/**
	 * ����������ؽ�.
	 *
	 * @param db ���ݿ����
	 * @param oldVersion �ɰ汾��
	 * @param newVersion �°汾��
	 * @see com.ab.db.orm.AbSDSQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		AbTableHelper.dropTablesByClasses(db, this.modelClasses);
		onCreate(db);
	}
}