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
 * ����������һ��.
 *
 * @author zhaoqp
 * @date��2013-1-28 ����3:57:44
 * @version v1.0
 */
public class AbTableRow {
	
	/** �е�������. */
	public AbTableCell[] cells;
	
	/** �и�. */
	public int height;
	
	/** ���е�Ԫ��ı���. */
	public int backgroundResource;
	
	/** �����С. */
	public int textSize;
	
	/** ������ɫ. */
	public int textColor;

	/**
	 * ����һ�й���.
	 *
	 * @param cells   �е�������
	 * @param height  �и�
	 * @param textSize �����С
	 * @param textColor ������ɫ
	 * @param backgroundResource ���е�Ԫ��ı���
	 */
	public AbTableRow(AbTableCell[] cells,int height,int textSize,int textColor,int backgroundResource) {
	  this.cells = cells;
	  this.height = height;
	  this.textSize = textSize;
	  this.textColor = textColor;
	  this.backgroundResource = backgroundResource;
	}

	/**
	 * ���еĵ�Ԫ����.
	 *
	 * @return the cell size
	 */
	public int getCellSize() {
	    return cells.length;
	}

	/**
	 * ������������ȥ�е�ֵ.
	 *
	 * @param index ��0��ʼ
	 * @return ��Ԫ�����
	 */
	public AbTableCell getCellValue(int index) {
	   if (index >= cells.length)
	      return null;
	   return cells[index];
	}
}
