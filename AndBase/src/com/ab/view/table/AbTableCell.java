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
package com.ab.view.table;

// TODO: Auto-generated Javadoc
/**
 * ����������һ����Ԫ��.
 *
 * @author zhaoqp
 * @date��2013-1-28 ����3:56:18
 * @version v1.0
 */
public class AbTableCell {
	
	/** ��������ȡֵ. */
	public Object value;
	
	/** �п�. */
	public int width;
	
	/** ��Ԫ������. */
	public int type;

	/**
	 * һ����Ԫ��.
	 *
	 * @param value ��Ԫ���ֵ
	 * @param width �п�
	 * @param type ��Ԫ������
	 */
	public AbTableCell(Object value, int width,int type) {
		this.value = value;
		this.width = width;
		this.type = type;
	}

}
