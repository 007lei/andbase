package com.andbase.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.andbase.R;
import com.andbase.demo.activity.AddPhotoActivity;
import com.andbase.demo.activity.CalendarActivity;
import com.andbase.demo.activity.CarouselActivity;
import com.andbase.demo.activity.ChartActivity;
import com.andbase.demo.activity.DBActivity;
import com.andbase.demo.activity.DemoAbActivity;
import com.andbase.demo.activity.DownListActivity;
import com.andbase.demo.activity.HttpActivity;
import com.andbase.demo.activity.ImageDownActivity;
import com.andbase.demo.activity.IocViewActivity;
import com.andbase.demo.activity.NestScrollActivity;
import com.andbase.demo.activity.PHashActivity;
import com.andbase.demo.activity.ProgressBarActivity;
import com.andbase.demo.activity.PullToRefreshActivity;
import com.andbase.demo.activity.Rotate3DActivity;
import com.andbase.demo.activity.SceneActivity;
import com.andbase.demo.activity.SlidingButtonActivity;
import com.andbase.demo.activity.SlidingMenuActivity;
import com.andbase.demo.activity.SlidingPlayViewActivity;
import com.andbase.demo.activity.SlidingTabActivity;
import com.andbase.demo.activity.TableActivity;
import com.andbase.demo.activity.ThreadControlActivity;
import com.andbase.demo.activity.WelcomeActivity;
import com.andbase.demo.activity.WheelActivity;
import com.andbase.demo.adapter.MyListViewAdapter;
import com.andbase.global.MyApplication;

public class MainContentFragment extends Fragment {
	
	private MyApplication application;
	private Activity mActivity = null;
	private ListView mListView = null;
	private MyListViewAdapter myListViewAdapter = null;
	private List<Map<String, Object>> list = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		 
		 mActivity = this.getActivity();
		 application = (MyApplication) mActivity.getApplication();
		 
		 View view = inflater.inflate(R.layout.main, null);
		 //��ȡListView����
	     mListView = (ListView)view.findViewById(R.id.mListView);
	     //ListView����
	     list = new ArrayList<Map<String, Object>>();
	     Map<String, Object> map = new HashMap<String, Object>();
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "1.AbActivity�����÷�");
	    	map.put("itemsText", "AbActivityʹ��ʾ��");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "2.���ݿ�ORM");
	    	map.put("itemsText", "ע�⣬���ݿ����ӳ��");
	    	list.add(map);

	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "3.IOC ����View");
	    	map.put("itemsText", "��findViewById˵no");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "4.Http������");
	    	map.put("itemsText", "����ͨ����ѡ");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "5.�̳߳����̶߳���");
	    	map.put("itemsText", "��Ӧ��Http���������Χ�⣬������Ӧ��");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "6.ͼƬ�����봦��");
	    	map.put("itemsText", "ͼƬ����,�ü�,����");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "7.����ˢ�����ҳ��ѯ");
	    	map.put("itemsText", "֧������ˢ�£�����������һҳ");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "8.������");
	    	map.put("itemsText", "��������ı��֧���ı���ͼƬ����ѡ��");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "9.������ť");
	    	map.put("itemsText", "������ť");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "10.ͼƬ����");
	    	map.put("itemsText", "ͼƬ����,View����");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "11.������");
	    	map.put("itemsText", "���̣߳��ϵ�����");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "12.������ӭҳ��");
	    	map.put("itemsText", "��Զ��������ʾ��ͼƬ�л�");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "13.�����");
	    	map.put("itemsText", "���Ҳ����");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "14.sliding Tab");
	    	map.put("itemsText", "�ɻ�����tab��ǩ");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "15.��Iphone����ѡ��ؼ�");
	    	map.put("itemsText", "��Iphone����ѡ��ؼ�");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "16.���պ����ѡȡͼƬ");
	    	map.put("itemsText", "���պ����ѡȡͼƬ");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "17.ͼ��");
	    	map.put("itemsText", "��״ͼ����״ͼ����״ͼ���ȼ���ͼ");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "18.����ѡ����");
	    	map.put("itemsText", "����ѡ����Ŷ");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "19.ͼƬ�������");
	    	map.put("itemsText", "phash�㷨");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "20.��תľ��");
	    	map.put("itemsText", "��תľ��");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "21.ˮƽ�����ν�����");
	    	map.put("itemsText", "Ư����ˮƽ�����ν������ؼ�");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "22.3D��תЧ��");
	    	map.put("itemsText", "2013�����");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "23.���ֻ���Ƕ������");
	    	map.put("itemsText", "���ֻ���Ƕ������Ľ������");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "24.������UI");
	    	map.put("itemsText", "�����������");
	    	list.add(map);
	    	
	    	
	    	
	     //ʹ���Զ����Adapter
	     myListViewAdapter = new MyListViewAdapter(mActivity, list,R.layout.list_items,
					new String[] { "itemsIcon", "itemsTitle","itemsText" }, new int[] { R.id.itemsIcon,
							R.id.itemsTitle,R.id.itemsText });
	     mListView.setAdapter(myListViewAdapter);
	     //item������¼�
	     mListView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = null;
					switch (position) {
					case 0:
						intent = new Intent(mActivity,DemoAbActivity.class);
						startActivity(intent);
						break;
					case 1:
						intent = new Intent(mActivity,DBActivity.class);
						startActivity(intent);
						break;
					case 2:
						intent = new Intent(mActivity,IocViewActivity.class);
						startActivity(intent);
						break;
					case 3:
						intent = new Intent(mActivity,HttpActivity.class);
						startActivity(intent);
						break;
					case 4:
						intent = new Intent(mActivity,ThreadControlActivity.class);
						startActivity(intent);
						break;
					case 5:
						intent = new Intent(mActivity,ImageDownActivity.class);
						startActivity(intent);
						break;
					case 6:
						intent = new Intent(mActivity,PullToRefreshActivity.class);
						startActivity(intent);
						break;
					case 7:
						intent = new Intent(mActivity,TableActivity.class);
						startActivity(intent);
						break;
					case 8:
						intent = new Intent(mActivity,SlidingButtonActivity.class);
						startActivity(intent);
						break;
					case 9:
						intent = new Intent(mActivity,SlidingPlayViewActivity.class);
						startActivity(intent);
						break;
					case 10:
						intent = new Intent(mActivity,DownListActivity.class);
						startActivity(intent);
						break;
					case 11:
						intent = new Intent(mActivity,WelcomeActivity.class);
						startActivity(intent);
						break;
					case 12:
						intent = new Intent(mActivity,SlidingMenuActivity.class);
						startActivity(intent);
						break;
					case 13:
						intent = new Intent(mActivity,SlidingTabActivity.class);
						startActivity(intent);
						break;
					case 14:
						intent = new Intent(mActivity,WheelActivity.class);
						startActivity(intent);
						break;
					case 15:
						intent = new Intent(mActivity,AddPhotoActivity.class);
						startActivity(intent);
						break;
					case 16:
						intent = new Intent(mActivity,ChartActivity.class);
						startActivity(intent);
						break;
					case 17:
						intent = new Intent(mActivity,CalendarActivity.class);
						startActivity(intent);
						break;
					case 18:
						intent = new Intent(mActivity,PHashActivity.class);
						startActivity(intent);
						break;
					case 19:
						intent = new Intent(mActivity,CarouselActivity.class);
						startActivity(intent);
						break;
					case 20:
						intent = new Intent(mActivity,ProgressBarActivity.class);
						startActivity(intent);
						break;
					case 21:
						intent = new Intent(mActivity,Rotate3DActivity.class);
						startActivity(intent);
						break;
					case 22:
						intent = new Intent(mActivity,NestScrollActivity.class);
						startActivity(intent);
						break;
					case 23:
						intent = new Intent(mActivity,SceneActivity.class);
						startActivity(intent);
						break;
					default:
						break;
					}
				}
	     });
	    
		 return view;
	} 
    
    

}


