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
package com.ab.view.wheel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.global.AbConstant;
import com.ab.util.AbDateUtil;
import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class AbViewUtil.
 */
public class AbWheelUtil {
	
	/**
	 * ������Ĭ�ϵ������յ�����ѡ����.
	 *
	 * @param activity     AbActivity����
	 * @param mText the m text
	 * @param mWheelViewY  ѡ���������
	 * @param mWheelViewM  ѡ���µ�����
	 * @param mWheelViewD  ѡ���յ�����
	 * @param okBtn ȷ����ť
	 * @param cancelBtn ȡ����ť
	 * @param defaultYear  Ĭ����ʾ����
	 * @param defaultMonth the default month
	 * @param defaultDay the default day
	 * @param startYear    ��ʼ����
	 * @param endYearOffset ���������뿪ʼ�����ƫ��
	 * @param initStart  �����Ƿ��ʼ��Ĭ��ʱ��Ϊ��ǰʱ��
	 */
	public static void initWheelDatePicker(final AbActivity activity,final TextView mText,final AbWheelView mWheelViewY,final AbWheelView mWheelViewM,final AbWheelView mWheelViewD,
			 Button okBtn,Button cancelBtn,
			 int defaultYear,int defaultMonth,int defaultDay,final int startYear,int endYearOffset,boolean initStart){
		
		int endYear = startYear+endYearOffset;
		// ��Ӵ�С���·ݲ�����ת��Ϊlist,����֮����ж�
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		//ʱ��ѡ���������ʵ��
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DATE);
		
		if(initStart){
			 defaultYear = year;
			 defaultMonth = month;
			 defaultDay = day;
		}

		mText.setText(AbStrUtil.dateTimeFormat(defaultYear+"-"+defaultMonth+"-"+defaultDay));
		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		
		//����"��"����ʾ����
		mWheelViewY.setAdapter(new AbNumericWheelAdapter(startYear, endYear));
		mWheelViewY.setCyclic(true);// ��ѭ������
		mWheelViewY.setLabel("��");  // �������
		mWheelViewY.setCurrentItem(defaultYear - startYear);// ��ʼ��ʱ��ʾ������
		mWheelViewY.setValueTextSize(32);
		mWheelViewY.setLabelTextSize(30);
		mWheelViewY.setLabelTextColor(0x80000000);
		//mWheelViewY.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// ��
		mWheelViewM.setAdapter(new AbNumericWheelAdapter(1, 12));
		mWheelViewM.setCyclic(true);
		mWheelViewM.setLabel("��");
		mWheelViewM.setCurrentItem(defaultMonth-1);
		mWheelViewM.setValueTextSize(32);
		mWheelViewM.setLabelTextSize(30);
		mWheelViewM.setLabelTextColor(0x80000000);
		//mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// ��
		// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
		if (list_big.contains(String.valueOf(month + 1))) {
			mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
		} else {
			// ����
			if (AbDateUtil.isLeapYear(year)){
				mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
			}else{
				mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
			}
		}
		mWheelViewD.setCyclic(true);
		mWheelViewD.setLabel("��");
		mWheelViewD.setCurrentItem(defaultDay - 1);
		mWheelViewD.setValueTextSize(32);
		mWheelViewD.setLabelTextSize(30);
		mWheelViewD.setLabelTextColor(0x80000000);
		//mWheelViewD.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// ���"��"����
		AbOnWheelChangedListener wheelListener_year = new AbOnWheelChangedListener() {

			public void onChanged(AbWheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + startYear;
				int mDIndex = mWheelViewM.getCurrentItem();
				// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
				if (list_big.contains(String.valueOf(mWheelViewM.getCurrentItem() + 1))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(mWheelViewM.getCurrentItem() + 1))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
				} else {
					if (AbDateUtil.isLeapYear(year_num))
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
					else
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
				}
				mWheelViewM.setCurrentItem(mDIndex);
				
			}
		};
		// ���"��"����
		AbOnWheelChangedListener wheelListener_month = new AbOnWheelChangedListener() {

			public void onChanged(AbWheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
				if (list_big.contains(String.valueOf(month_num))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
				} else {
					int year_num = mWheelViewY.getCurrentItem() + startYear;
					if (AbDateUtil.isLeapYear(year_num))
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
					else
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
				}
				mWheelViewD.setCurrentItem(0);
			}
		};
		mWheelViewY.addChangingListener(wheelListener_year);
		mWheelViewM.addChangingListener(wheelListener_month);
		
		okBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				activity.removeDialog(AbConstant.DIALOGBOTTOM);
				int indexYear = mWheelViewY.getCurrentItem();
				String year = mWheelViewY.getAdapter().getItem(indexYear);
				
				int indexMonth = mWheelViewM.getCurrentItem();
				String month = mWheelViewM.getAdapter().getItem(indexMonth);
				
				int indexDay = mWheelViewD.getCurrentItem();
				String day = mWheelViewD.getAdapter().getItem(indexDay);
				
				mText.setText(AbStrUtil.dateTimeFormat(year+"-"+month+"-"+day));
			}
			
		});
		
		cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				activity.removeDialog(AbConstant.DIALOGBOTTOM);
			}
			
		});
		
    }
	
	/**
	 * ������Ĭ�ϵ�����ʱ�ֵ�ʱ��ѡ����.
	 *
	 * @param activity     AbActivity����
	 * @param mText the m text
	 * @param mWheelViewMD  ѡ�����յ�����
	 * @param mWheelViewHH the m wheel view hh
	 * @param mWheelViewMM  ѡ��ֵ�����
	 * @param okBtn ȷ����ť
	 * @param cancelBtn ȡ����ť
	 * @param defaultYear the default year
	 * @param defaultMonth the default month
	 * @param defaultDay the default day
	 * @param defaultHour the default hour
	 * @param defaultMinute the default minute
	 * @param initStart the init start
	 * @date��2013-7-16 ����10:19:01
	 * @version v1.0
	 */
	public static void initWheelTimePicker(final AbActivity activity,final TextView mText,final AbWheelView mWheelViewMD,final AbWheelView mWheelViewHH,final AbWheelView mWheelViewMM,
			 Button okBtn,Button cancelBtn,
			 int defaultYear,int defaultMonth,int defaultDay,int defaultHour,int defaultMinute,boolean initStart){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		if(initStart){
			defaultYear = year;
			defaultMonth = month;
			defaultDay = day;
			defaultHour = hour;
			defaultMinute = minute;
		}
		
		String val = AbStrUtil.dateTimeFormat(defaultYear+"-"+defaultMonth+"-"+defaultDay+" "+defaultHour+":"+defaultMinute+":"+second) ;
		mText.setText(val);
		// ��Ӵ�С���·ݲ�����ת��Ϊlist,����֮����ж�
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		//
		final List<String> textDMList = new ArrayList<String>();
		final List<String> textDMDateList = new ArrayList<String>();
		for(int i=1;i<13;i++){
			if(list_big.contains(String.valueOf(i))){
				for(int j=1;j<32;j++){
					textDMList.add(i+"��"+" "+j+"��");
					textDMDateList.add(defaultYear+"-"+i+"-"+j);
				}
			}else{
				if(i==2){
					if(AbDateUtil.isLeapYear(defaultYear)){
						for(int j=1;j<28;j++){
							textDMList.add(i+"��"+" "+j+"��");
							textDMDateList.add(defaultYear+"-"+i+"-"+j);
						}
					}else{
						for(int j=1;j<29;j++){
							textDMList.add(i+"��"+" "+j+"��");
							textDMDateList.add(defaultYear+"-"+i+"-"+j);
						}
					}
				}else{
					for(int j=1;j<29;j++){
						textDMList.add(i+"��"+" "+j+"��");
						textDMDateList.add(defaultYear+"-"+i+"-"+j);
					}
				}
			}
			
		}
		String currentDay = defaultMonth+"��"+" "+defaultDay+"��";
		int currentDayIndex = textDMList.indexOf(currentDay);
		
		// ����
		mWheelViewMD.setAdapter(new AbStringWheelAdapter(textDMList,AbStrUtil.strLength("12��"+" "+"12��")));
		mWheelViewMD.setCyclic(true);
		mWheelViewMD.setLabel(""); 
		mWheelViewMD.setCurrentItem(currentDayIndex);
		mWheelViewMD.setValueTextSize(32);
		mWheelViewMD.setLabelTextSize(30);
		mWheelViewMD.setLabelTextColor(0x80000000);
		//mWheelViewMD.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// ʱ
		mWheelViewHH.setAdapter(new AbNumericWheelAdapter(0, 23));
		mWheelViewHH.setCyclic(true);
		mWheelViewHH.setLabel("��");
		mWheelViewHH.setCurrentItem(defaultHour);
		mWheelViewHH.setValueTextSize(32);
		mWheelViewHH.setLabelTextSize(30);
		mWheelViewHH.setLabelTextColor(0x80000000);
		//mWheelViewH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// ��
		mWheelViewMM.setAdapter(new AbNumericWheelAdapter(0, 59));
		mWheelViewMM.setCyclic(true);
		mWheelViewMM.setLabel("��");
		mWheelViewMM.setCurrentItem(defaultMinute);
		mWheelViewMM.setValueTextSize(32);
		mWheelViewMM.setLabelTextSize(30);
		mWheelViewMM.setLabelTextColor(0x80000000);
		//mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		okBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				activity.removeDialog(AbConstant.DIALOGBOTTOM);
				int index1 = mWheelViewMD.getCurrentItem();
				int index2 = mWheelViewHH.getCurrentItem();
				int index3 = mWheelViewMM.getCurrentItem();
				
				String dmStr =  textDMDateList.get(index1);
				Calendar calendar = Calendar.getInstance();
				int second = calendar.get(Calendar.SECOND);
				String val = AbStrUtil.dateTimeFormat(dmStr+" "+index2+":"+index3+":"+second) ;
				mText.setText(val);
			}
			
		});
		
		cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				activity.removeDialog(AbConstant.DIALOGBOTTOM);
			}
			
		});
		
    }
	
	
	/**
	 * ������Ĭ�ϵ�ʱ�ֵ�ʱ��ѡ����.
	 *
	 * @param activity     AbActivity����
	 * @param mText the m text
	 * @param mWheelViewHH the m wheel view hh
	 * @param mWheelViewMM  ѡ��ֵ�����
	 * @param okBtn ȷ����ť
	 * @param cancelBtn ȡ����ť
	 * @param defaultHour the default hour
	 * @param defaultMinute the default minute
	 * @param initStart the init start
	 */
	public static void initWheelTimePicker2(final AbActivity activity,final TextView mText,final AbWheelView mWheelViewHH,final AbWheelView mWheelViewMM,
			 Button okBtn,Button cancelBtn,
			 int defaultHour,int defaultMinute,boolean initStart){
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		if(initStart){
			defaultHour = hour;
			defaultMinute = minute;
		}
		
		String val = AbStrUtil.dateTimeFormat(defaultHour+":"+defaultMinute) ;
		mText.setText(val);
		
		// ʱ
		mWheelViewHH.setAdapter(new AbNumericWheelAdapter(0, 23));
		mWheelViewHH.setCyclic(true);
		mWheelViewHH.setLabel("��");
		mWheelViewHH.setCurrentItem(defaultHour);
		mWheelViewHH.setValueTextSize(32);
		mWheelViewHH.setLabelTextSize(30);
		mWheelViewHH.setLabelTextColor(0x80000000);
		//mWheelViewH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// ��
		mWheelViewMM.setAdapter(new AbNumericWheelAdapter(0, 59));
		mWheelViewMM.setCyclic(true);
		mWheelViewMM.setLabel("��");
		mWheelViewMM.setCurrentItem(defaultMinute);
		mWheelViewMM.setValueTextSize(32);
		mWheelViewMM.setLabelTextSize(30);
		mWheelViewMM.setLabelTextColor(0x80000000);
		//mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		okBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				activity.removeDialog(AbConstant.DIALOGBOTTOM);
				int index2 = mWheelViewHH.getCurrentItem();
				int index3 = mWheelViewMM.getCurrentItem();
				String val = AbStrUtil.dateTimeFormat(index2+":"+index3) ;
				mText.setText(val);
			}
			
		});
		
		cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				activity.removeDialog(AbConstant.DIALOGBOTTOM);
			}
			
		});
		
    }
	
	
}
