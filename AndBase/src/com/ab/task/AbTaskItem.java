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
package com.ab.task;

// TODO: Auto-generated Javadoc
/**
 * ����������ִ�е�λ.
 *
 * @author zhaoqp
 * @date 2011-12-10
 * @version v1.0
 */
public class AbTaskItem { 
	
	/** ��¼�ĵ�ǰ����. */
	public int position;
	 
 	/** ִ����ɵĻص��ӿ�. */
    public AbTaskCallback callback; 
    
    /** ִ����ɵĽ��. */
    private Object result;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public AbTaskCallback getCallback() {
		return callback;
	}

	public void setCallback(AbTaskCallback callback) {
		this.callback = callback;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	} 
    
} 

