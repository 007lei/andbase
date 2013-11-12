package com.andbase.demo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andbase.R;
import com.andbase.demo.activity.DBInsideSampleActivity;
import com.andbase.demo.activity.DBObjectActivity;
import com.andbase.demo.activity.DBSDSampleActivity;
import com.andbase.demo.model.LocalUser;

public class UserDBListAdapter extends BaseAdapter {

	private Context mContext;
	// xmlתView����
	private LayoutInflater mInflater;
	// �б�չ�ֵ�����
	private List<LocalUser> mUserlist;

	/**
	 * ���췽��
	 * @param context
	 * @param data �б�չ�ֵ�����
	 * @param resource ���еĲ���
	 * @param from Map�е�key
	 * @param to view��id
	 */
	public UserDBListAdapter(Context context, List<LocalUser> userlist) {
		mContext = context;
		mUserlist = userlist;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mUserlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mUserlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		// ��ǰ����
		final int index = position;
		
		if (convertView == null) {
			// ʹ���Զ����list_items��ΪLayout
			convertView = mInflater.inflate(R.layout.db_list_items, parent, false);
			// ����findView�Ĵ���
			holder = new ViewHolder();
			// ��ʼ�������е�Ԫ��
			holder.itemsTitle = ((TextView) convertView.findViewById(R.id.itemsTitle));
			holder.itemsText = ((EditText) convertView.findViewById(R.id.itemsText));
			holder.modifyBtn = ((Button) convertView.findViewById(R.id.modBtn));
			holder.delBtn = ((Button) convertView.findViewById(R.id.delBtn));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// ��ȡ���е�����
		final LocalUser user = mUserlist.get(position);
		// �������ݵ�View
		holder.itemsTitle.setText(String.valueOf(user.get_id()));
		holder.itemsText.setText(user.getName());
		// �޸ĺ�ɾ����ť
		// �ͷŽ���
		holder.modifyBtn.setFocusable(false);
		holder.delBtn.setFocusable(false);
		// �޸İ�ť�¼�
		holder.modifyBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				 //�޸�����
				 user.setName(holder.itemsText.getText().toString().trim());
				 
				 if(mContext instanceof DBInsideSampleActivity){
					 ((DBInsideSampleActivity)mContext).updateData(user);
				 }else if(mContext instanceof DBSDSampleActivity){
					 ((DBSDSampleActivity)mContext).updateData(user);
					
				 }else{
					 ((DBObjectActivity)mContext).updateData(user);
				 }
				 
			}
		});
		
		// ɾ����ť�¼�
		holder.delBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mContext instanceof DBInsideSampleActivity){
					 ((DBInsideSampleActivity)mContext).delData(Integer.parseInt(holder.itemsTitle.getText().toString()));
				 }else if(mContext instanceof DBSDSampleActivity){
					 ((DBSDSampleActivity)mContext).delData(Integer.parseInt(holder.itemsTitle.getText().toString()));
				 }else{
					 ((DBObjectActivity)mContext).delData(Integer.parseInt(holder.itemsTitle.getText().toString()));
				 }
			}
		});
		return convertView;
	}

	/**
	 * ����һ�����ı���ͼ
	 * @param item
	 */
	public void addItem(int position,LocalUser user) {
		mUserlist.add(position,user);
		notifyDataSetChanged();
	}
	
	/**
	 * ����һ�����ı���ͼ
	 * @param item
	 */
	public void addItem(LocalUser user) {
		mUserlist.add(0,user);
		notifyDataSetChanged();
	}

	/**
	 * ViewԪ��
	 */
	static class ViewHolder {
		TextView itemsTitle;
		EditText itemsText;
		Button modifyBtn;
		Button delBtn;
	}

}