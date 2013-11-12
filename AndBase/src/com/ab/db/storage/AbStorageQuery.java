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


import java.util.ArrayList;

import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * ���ƣ�AbStorageQuery.java 
 * ��������������ʵ��
 * @author zhaoqp
 * @date��2013-10-16 ����1:33:39
 * @version v1.0
 */
public class AbStorageQuery {
	
	/** where �Ӿ�. */
	private String whereClause = null;
	
	/** where �Ӿ�İ󶨲���. */
	private ArrayList<String> whereArgs = null;
	
	/** having �Ӿ�. */
	private String having = null;
	
	/** groupBy �Ӿ�. */
	private String groupBy = null;
	
	/** orderBy �Ӿ�. */
	private String orderBy = null;
	
	/** limit ֵ. */
	private int limit = -1;
	
	/** offset ֵ. */
	private int offset = -1;
	
	/**
	 * Instantiates a new ab storage query.
	 */
	public AbStorageQuery() {
		super();
		whereClause = "";
		whereArgs = new ArrayList<String>();
	}
	
	/**
	 * ������������.
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @return the ab storage query
	 */
	public AbStorageQuery equals(String paramString, Object paramObject){
		if(!AbStrUtil.isEmpty(whereClause)){
			whereClause += " and ";
		}
		whereClause += " "+(paramString+" = ? ");
		whereArgs.add(paramObject.toString());
		return this;
	}
	
	/**
	 * ��������������.
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @return the ab storage query
	 */
	public AbStorageQuery notEqual(String paramString, Object paramObject){
		if(!AbStrUtil.isEmpty(whereClause)){
			whereClause += " and ";
		}
		whereClause += " "+(paramString+" <> ? ");
		whereArgs.add(paramObject.toString());
		return this;
	}
	
	/**
	 * �������������.
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @return the ab storage query
	 */
	public AbStorageQuery like(String paramString, Object paramObject){
		if(!AbStrUtil.isEmpty(whereClause)){
			whereClause += " and ";
		}
		whereClause += " "+(paramString+"like ? ");
		whereArgs.add("'%"+paramObject.toString()+"%'");
		return this;
	}
	
	/**
	 * �������������.
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @return the ab storage query
	 */
	public AbStorageQuery greaterThan(String paramString, Object paramObject){
		if(!AbStrUtil.isEmpty(whereClause)){
			whereClause += " and ";
		}
		whereClause += " "+(paramString+" > ? ");
		whereArgs.add(paramObject.toString());
	    return this;
	}

	/**
	 * ������С�����.
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @return the ab storage query
	 */
	public AbStorageQuery lessThan(String paramString, Object paramObject){
		if(!AbStrUtil.isEmpty(whereClause)){
			whereClause += " and ";
		}
		whereClause += " "+(paramString+" < ? ");
		whereArgs.add(paramObject.toString());
	    return this;
	}
	
	/**
	 * ���������ڵ������.
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @return the ab storage query
	 */
	public AbStorageQuery greaterThanEqualTo(String paramString, Object paramObject){
		if(!AbStrUtil.isEmpty(whereClause)){
			whereClause += " and ";
		}
		whereClause += " "+(paramString+" >= ? ");
		whereArgs.add(paramObject.toString());
	    return this;
	}

	/**
	 * ������С�ڵ������.
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @return the ab storage query
	 */
	public AbStorageQuery lessThanEqualTo(String paramString, Object paramObject){
		if(!AbStrUtil.isEmpty(whereClause)){
			whereClause += " and ";
		}
		whereClause += " "+(paramString+" <= ? ");
		whereArgs.add(paramObject.toString());
	    return this;
	}
	
	/**
	 * �������������.
	 *
	 * @param paramString the param string
	 * @param paramArrayOfObject the param array of object
	 * @return the ab storage query
	 */
	public AbStorageQuery in(String paramString, Object[] paramArrayOfObject){
		if(!AbStrUtil.isEmpty(whereClause)){
			whereClause += " and ";
		}
		if(paramArrayOfObject!=null && paramArrayOfObject.length>0){
			whereClause += " "+(paramString+" in ( ");
			for(int i=0;i<paramArrayOfObject.length;i++){
				if(i!=0){
					whereClause += " , ";
				}
				whereClause += " ? ";
			}
			whereClause += " ) ";
			
			for(Object str:paramArrayOfObject){
				whereArgs.add((String)str);
			}
			
		}else{
			whereClause += " "+paramString;
		}
		
	    return this;
	}

	/**
	 * ���������������.
	 *
	 * @param paramString the param string
	 * @param paramArrayOfObject the param array of object
	 * @return the ab storage query
	 */
	public AbStorageQuery notIn(String paramString, Object[] paramArrayOfObject) {
		if(paramArrayOfObject!=null && paramArrayOfObject.length>0){
			whereClause += " "+(paramString+" not in ( ");
			for(int i=0;i<paramArrayOfObject.length;i++){
				if(i!=0){
					whereClause += " , ";
				}
				whereClause += " ? ";
			}
			whereClause += " ) ";
			whereArgs.add(paramArrayOfObject.toString());
		}else{
			whereClause += " "+paramString;
		}
		
	    return this;
	}
	
	/**
	 * ��������and���.
	 *
	 * @param storageQuery the storage query
	 * @return the ab storage query
	 */
	public AbStorageQuery and(AbStorageQuery storageQuery){
		whereClause += " and "+"("+storageQuery.getWhereClause()+")";
		for(String args:storageQuery.getWhereArgs()){
			this.whereArgs.add(args);
		}
		return this;
	}
	
	/**
	 * ����������or���.
	 *
	 * @param storageQuery the storage query
	 * @return the ab storage query
	 */
	public AbStorageQuery or(AbStorageQuery storageQuery){
		whereClause += " or "+"("+storageQuery.getWhereClause()+")";
		for(String args:storageQuery.getWhereArgs()){
			this.whereArgs.add(args);
		}
		return this;
	}
	
	
	/**
	 * ����������һ��������sql���.
	 *
	 * @param whereClause �� user_name = ?  ���� user_name = 'xiao'
	 * @param whereArgs the where args
	 */
	public void setWhereClause(String whereClause,String[] whereArgs) {
		this.whereClause = whereClause;
		for(String args:whereArgs){
			this.whereArgs.add(args);
		}
	}
	
	/**
	 * ��������ȡ��ǰ�Ĳ�ѯsql���.
	 *
	 * @return the where clause
	 */
	public String getWhereClause() {
		return whereClause;
	}
	
    /**
     * ��������ð󶨱����Ĳ���.
     *
     * @return the where args
     */
	public String[] getWhereArgs() {
		String[] argsArray = new String[whereArgs.size()];
		for(int i=0;i<whereArgs.size();i++){
			String args = whereArgs.get(i);
			argsArray[i] = args;
		}
		return argsArray;
	}
	
	/**
	 * �������������.
	 *
	 * @param paramString the param string
	 * @param paramSortOrder the param sort order
	 * @return the ab storage query
	 */
	public AbStorageQuery addSort(String paramString, SortOrder paramSortOrder){
	    if(AbStrUtil.isEmpty(orderBy)){
	    	orderBy = " " + paramString + " " + paramSortOrder;
	    }else{
	    	orderBy += " , " + paramString + " " + paramSortOrder;
	    }
	    return this;
	}

	/**
	 * Gets the having.
	 *
	 * @return the having
	 */
	public String getHaving() {
		return having;
	}

	/**
	 * Sets the having.
	 *
	 * @param having the new having
	 */
	public void setHaving(String having) {
		this.having = having;
	}

	/**
	 * Gets the group by.
	 *
	 * @return the group by
	 */
	public String getGroupBy() {
		return groupBy;
	}

	/**
	 * Sets the group by.
	 *
	 * @param groupBy the new group by
	 */
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	/**
	 * Gets the order by.
	 *
	 * @return the order by
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * Sets the order by.
	 *
	 * @param orderBy the new order by
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * Gets the limit.
	 *
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * Sets the limit.
	 *
	 * @param limit the new limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * Gets the offset.
	 *
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Sets the offset.
	 *
	 * @param offset the new offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	
	
	/**
	 * ����������.
	 */
	public static enum SortOrder{
	    
    	/** The asc. */
    	ASC, 
    	
		/** The desc. */
		DESC;
	}
	
	
	/**
	 * Prints the log.
	 *
	 * @param mAbStorageQuery the m ab storage query
	 */
	public static void printLog(AbStorageQuery mAbStorageQuery){
		System.out.println("where "+mAbStorageQuery.getWhereClause());
		if(!AbStrUtil.isEmpty(mAbStorageQuery.getOrderBy())){
			System.out.println("order by "+mAbStorageQuery.getOrderBy());
		}
		
		System.out.print("����:[");
		for(int i=0;i<mAbStorageQuery.getWhereArgs().length;i++){
			if(i!=0){
				System.out.print(",");
			}
			System.out.print(mAbStorageQuery.getWhereArgs()[i]);
		}
		System.out.print("]");
		System.out.println(" ");
		System.out.println("��������������������������������������������������");
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		//����1
		AbStorageQuery mAbStorageQuery1 = new AbStorageQuery();
		
		//��һ������
		mAbStorageQuery1.equals("u_id","1");
		mAbStorageQuery1.equals("face_u_id","2");
		
		//�ڶ�������
		AbStorageQuery mAbStorageQuery2 = new AbStorageQuery();
		mAbStorageQuery2.equals("face_u_id","3");
		mAbStorageQuery2.equals("u_id","4");
		
		//��ϲ�ѯ
		AbStorageQuery mAbStorageQuery = mAbStorageQuery1.or(mAbStorageQuery2);
		
		printLog(mAbStorageQuery);
		
		//����2
		AbStorageQuery mAbStorageQuery3 = new AbStorageQuery();
		AbStorageQuery mAbStorageQuery4 = new AbStorageQuery();
		mAbStorageQuery3.equals("u_id","1");
		mAbStorageQuery4.equals("face_u_id","3");
		AbStorageQuery mAbStorageQuery5 = mAbStorageQuery3.and(mAbStorageQuery4);
		printLog(mAbStorageQuery5);
		
		//����3
		AbStorageQuery mAbStorageQuery6 = new AbStorageQuery();
		AbStorageQuery mAbStorageQuery7 = new AbStorageQuery();
		mAbStorageQuery6.lessThan("u_id","1");
		mAbStorageQuery7.greaterThanEqualTo("face_u_id","3");
		AbStorageQuery mAbStorageQuery8 = mAbStorageQuery6.and(mAbStorageQuery7);
		printLog(mAbStorageQuery8);
		
		//����4
		AbStorageQuery mAbStorageQuery9 = new AbStorageQuery();
		mAbStorageQuery9.in("name", new String []{"1","2","3","4"});
		mAbStorageQuery9.addSort("time", SortOrder.ASC);
		mAbStorageQuery9.addSort("state", SortOrder.DESC);
		printLog(mAbStorageQuery9);
		
	}
	
  
}
