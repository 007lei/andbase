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
package com.ab.db.storage;

import java.util.List;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInfoListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInsertListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataOperationListener;
import com.ab.task.AbTaskCallback;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListCallback;
import com.ab.task.AbTaskQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class AbSqliteStorage.
 */
public class AbSqliteStorage {
	
	/** The m context. */
	private static Context mContext;
	
	/** The m sqlite storage. */
	private static AbSqliteStorage mSqliteStorage = null;
	
	/** The m ab task queue. */
	private static AbTaskQueue mAbTaskQueue = null;
	
	/** The error code100. */
	private int errorCode100 = 100;
	
	/** The error message100. */
	private String errorMessage100 = "��������";
	
	/** The error code101. */
	private int errorCode101 = 101;
	
	/** The error message101. */
	private String errorMessage101 = "ִ��ʱ����";
	
	/** The ret value. */
	private long retValue = -1;
	
	/**
	 * ��������ȡ�洢ʵ��.
	 *
	 * @param context the context
	 * @return single instance of AbSqliteStorage
	 */
	public static AbSqliteStorage getInstance(Context context){
		mContext = context;
	    if (null == mSqliteStorage){
	    	mSqliteStorage = new AbSqliteStorage(context);
	    }
	    //�ö��б��Ⲣ���������ݿ�����
	    mAbTaskQueue = AbTaskQueue.getInstance();
	    return mSqliteStorage;
	}
	
	/**
	 * Instantiates a new ab sqlite storage.
	 *
	 * @param context the context
	 */
	private AbSqliteStorage(Context context) {
		super();
	}
	
	/**
	 * ��������������.
	 *
	 * @param <T> the generic type
	 * @param entity  ʵ���� �����˶����ϵӳ��
	 * @param dao     ʵ��AbDBDaoImpl��Dao
	 * @param paramDataInsertListener ���ؼ�����
	 */
	public <T> void insertData(final T entity,final AbDBDaoImpl<T> dao, final AbDataInsertListener paramDataInsertListener){
		 
		 if (entity != null){
	    	
	    	AbTaskItem item = new AbTaskItem();
	    	item.callback = new AbTaskCallback() {
				
				@Override
				public void update() {
					if(retValue>=0){
						if (paramDataInsertListener != null){
				    		paramDataInsertListener.onSuccess(retValue);
					    }
					}else{
						if (paramDataInsertListener != null){
			    		    paramDataInsertListener.onFailure(errorCode101, errorMessage101);
				        }
					}
				}
				
				@Override
				public void get() {
					//ִ�в��� 
					//(1)��ȡ���ݿ� 
					dao.startWritableDatabase(false);
				  	//(2)ִ��
					retValue = dao.insert(entity);
				    //(3)�ر����ݿ�
				  	dao.closeDatabase(false);
				}
			};
			mAbTaskQueue.execute(item);
	    	
	    }else{
	    	if (paramDataInsertListener != null){
	    		paramDataInsertListener.onFailure(errorCode100, errorMessage100);
		    }
	    }
	    
	}
	
	/**
	 * ��������������.
	 *
	 * @param <T> the generic type
	 * @param entityList  ʵ���� �����˶����ϵӳ��
	 * @param dao     ʵ��AbDBDaoImpl��Dao
	 * @param paramDataInsertListener ���ؼ�����
	 */
	public <T> void insertData(final List<T> entityList,final AbDBDaoImpl<T> dao, final AbDataInsertListener paramDataInsertListener){
		 
		 if (entityList != null){
	    	
	    	AbTaskItem item = new AbTaskItem();
	    	item.callback = new AbTaskCallback() {
				
				@Override
				public void update() {
					if(retValue>=0){
						if (paramDataInsertListener != null){
				    		paramDataInsertListener.onSuccess(retValue);
					    }
					}else{
						if (paramDataInsertListener != null){
			    		    paramDataInsertListener.onFailure(errorCode101, errorMessage101);
				        }
					}
				}
				
				@Override
				public void get() {
					//ִ�в��� 
					//(1)��ȡ���ݿ� 
					dao.startWritableDatabase(false);
				  	//(2)ִ��
					retValue = dao.insertList(entityList);
				    //(3)�ر����ݿ�
				  	dao.closeDatabase(false);
			    	
				}
			};
			mAbTaskQueue.execute(item);
	    	
	    }else{
	    	if (paramDataInsertListener != null){
	    		paramDataInsertListener.onFailure(errorCode100, errorMessage100);
		    }
	    }
	    
	  }
	
	
	/**
	 * Find data.
	 *
	 * @param <T> ��������ѯ����
	 * @param storageQuery the storage query
	 * @param dao     ʵ��AbDBDaoImpl��Dao
	 * @param paramDataInsertListener ���ؼ�����
	 */
	public <T> void findData(final AbStorageQuery storageQuery,final AbDBDaoImpl<T> dao, final AbDataInfoListener paramDataInsertListener){
		     
	    	final AbTaskItem item = new AbTaskItem();
	    	item.callback = new AbTaskListCallback() {
				
				@Override
				public void update(List<?> paramList) {
					if (paramDataInsertListener != null){
			    		paramDataInsertListener.onSuccess(paramList);
				    }
				}
				
				@Override
				public void get() {
					List<?> list = null;   
					//ִ�в��� 
					//(1)��ȡ���ݿ� 
					dao.startReadableDatabase(false);
				  	//(2)ִ��
					if(storageQuery.getLimit()!=-1 && storageQuery.getOffset()!=-1){
						list = dao.queryList(null, storageQuery.getWhereClause(),storageQuery.getWhereArgs(), storageQuery.getGroupBy(), storageQuery.getHaving(), storageQuery.getOrderBy()+" limit "+storageQuery.getLimit()+ " offset " +storageQuery.getOffset(), null);
					}else{
						list = dao.queryList(null, storageQuery.getWhereClause(),storageQuery.getWhereArgs(), storageQuery.getGroupBy(), storageQuery.getHaving(), storageQuery.getOrderBy(), null);
					}
				    //(3)�ر����ݿ�
				  	dao.closeDatabase(false);
				  	
				  	//���÷��ؽ��
				  	item.setResult(list);
			    	
				}
			};
			mAbTaskQueue.execute(item);
	    
	  }
	
	/**
	 * �������޸�����.
	 *
	 * @param <T> the generic type
	 * @param entity  ʵ���� �����˶����ϵӳ��
	 * @param dao     ʵ��AbDBDaoImpl��Dao
	 * @param paramDataInsertListener ���ؼ�����
	 */
	public <T> void updateData(final T entity,final AbDBDaoImpl<T> dao, final AbDataOperationListener paramDataInsertListener){
		 
		 if (entity != null){
	    	
	    	AbTaskItem item = new AbTaskItem();
	    	item.callback = new AbTaskListCallback() {
				
				@Override
				public void update(List<?> paramList) {
					if(retValue>=0){
						if (paramDataInsertListener != null){
				    		paramDataInsertListener.onSuccess(retValue);
					    }
					}else{
						if (paramDataInsertListener != null){
			    		    paramDataInsertListener.onFailure(errorCode101, errorMessage101);
				        }
					}
				}
				
				@Override
				public void get() {
					//ִ�в��� 
					//(1)��ȡ���ݿ� 
					dao.startWritableDatabase(false);
				  	//(2)ִ��
					retValue = dao.update(entity);
				    //(3)�ر����ݿ�
				  	dao.closeDatabase(false);
			    	
				}
			};
			mAbTaskQueue.execute(item);
	    	
	    }else{
	    	if (paramDataInsertListener != null){
	    		paramDataInsertListener.onFailure(errorCode100, errorMessage100);
		    }
	    }
	    
	  }
	
	/**
	 * �������޸�����.
	 *
	 * @param <T> the generic type
	 * @param entityList  ʵ���� �����˶����ϵӳ��
	 * @param dao     ʵ��AbDBDaoImpl��Dao
	 * @param paramDataInsertListener ���ؼ�����
	 */
	public <T> void updateData(final List<T> entityList,final AbDBDaoImpl<T> dao, final AbDataOperationListener paramDataInsertListener){
		 
		 if (entityList != null){
	    	
	    	AbTaskItem item = new AbTaskItem();
	    	item.callback = new AbTaskCallback() {
				
				@Override
				public void update() {
					if(retValue>=0){
						if (paramDataInsertListener != null){
				    		paramDataInsertListener.onSuccess(retValue);
					    }
					}else{
						if (paramDataInsertListener != null){
			    		    paramDataInsertListener.onFailure(errorCode101, errorMessage101);
				        }
					}
				}
				
				@Override
				public void get() {
					//ִ�в��� 
					//(1)��ȡ���ݿ� 
					dao.startWritableDatabase(false);
				  	//(2)ִ��
					retValue = dao.updateList(entityList);
				    //(3)�ر����ݿ�
				  	dao.closeDatabase(false);
			    	
				}
			};
			mAbTaskQueue.execute(item);
	    	
	    }else{
	    	if (paramDataInsertListener != null){
	    		paramDataInsertListener.onFailure(errorCode100, errorMessage100);
		    }
	    }
	    
	  }
	
	/**
	 * �������޸�����.
	 *
	 * @param <T> the generic type
	 * @param storageQuery  ����ʵ��
	 * @param dao     ʵ��AbDBDaoImpl��Dao
	 * @param paramDataInsertListener ���ؼ�����
	 */
	public <T> void deleteData(final AbStorageQuery storageQuery,final AbDBDaoImpl<T> dao, final AbDataOperationListener paramDataInsertListener){
		 
	    	
	    	AbTaskItem item = new AbTaskItem();
	    	item.callback = new AbTaskCallback() {
				
				@Override
				public void update() {
					if(retValue>=0){
						if (paramDataInsertListener != null){
				    		paramDataInsertListener.onSuccess(retValue);
					    }
					}else{
						if (paramDataInsertListener != null){
			    		    paramDataInsertListener.onFailure(errorCode101, errorMessage101);
				        }
					}
				}
				
				@Override
				public void get() {
					//ִ�в��� 
					//(1)��ȡ���ݿ� 
					dao.startWritableDatabase(false);
				  	//(2)ִ��
					retValue = dao.delete(storageQuery.getWhereClause(),storageQuery.getWhereArgs());
				    //(3)�ر����ݿ�
				  	dao.closeDatabase(false);
			    	
				}
			};
			mAbTaskQueue.execute(item);
	}
	
	/**
	 * �������ͷŴ洢ʵ��.
	 */
	public void release(){
		if(mAbTaskQueue!=null){
			mAbTaskQueue.quit();
		}
	}
	
}
