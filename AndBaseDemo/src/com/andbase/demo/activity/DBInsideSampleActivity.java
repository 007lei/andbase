package com.andbase.demo.activity;

import java.util.List;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.UserDBListAdapter;
import com.andbase.demo.dao.UserInsideDao;
import com.andbase.demo.model.LocalUser;
import com.andbase.global.MyApplication;
/**
 * ���ƣ�DBInsideSampleActivity
 * ���������ݿ���ʾ�������ݿ�洢
 * @author zhaoqp
 * @date 2011-12-13
 * @version
 */
public class DBInsideSampleActivity extends AbActivity {
	
	private MyApplication application;
	//�б�������
	private UserDBListAdapter myListViewAdapter = null;
	//�б�����
	private List<LocalUser> userList = null;
	private ListView mListView = null;
	//�������ݿ����ʵ����
	private UserInsideDao userDao = null;
	
	//ÿһҳ��ʾ������
	public int pageSize = 10;
	//��ǰҳ��
	public int pageNum = 1;
	//������
	public int totalCount = 0;
	//��ҳ��
	private LinearLayout mListViewForPage;
	//�������͵�ǰ��ʾ�ļ�ҳ
	public TextView  total, current;
	//��һҳ����һҳ�İ�ť
	private Button preView,nextView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.db_sample);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.db_inside_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
	    application = (MyApplication)abApplication;
	    
	    //��ʼ�����ݿ����ʵ����
	    userDao = new UserInsideDao(DBInsideSampleActivity.this);
	    
	    //(1)��ȡ���ݿ� 
	  	userDao.startReadableDatabase(false);
	  	//(2)ִ�в�ѯ
	    userList = userDao.queryList(null, null, null, null, null, "create_time desc limit "+String.valueOf(pageSize)+ " offset " +0, null);
	    totalCount = userDao.queryCount(null, null);
	    //(3)�ر����ݿ�
	  	userDao.closeDatabase(false);
	  	
        //��ȡListView����
        mListView = (ListView)this.findViewById(R.id.mListView);
        //��ҳ��
        mListViewForPage = (LinearLayout) this.findViewById(R.id.mListViewForPage);
        //��һҳ����һҳ�İ�ť
        preView = (Button) this.findViewById(R.id.preView);
		nextView = (Button) this.findViewById(R.id.nextView);
		//��ҳ����ʾ���ı�
		total = (TextView)findViewById(R.id.total);
		current = (TextView)findViewById(R.id.current);
		
		//����һ��HeaderView���������ݿ�������һ������
        View headerView = mInflater.inflate(R.layout.db_list_header,null);
        //�ӵ�ListView�Ķ���
        mListView.addHeaderView(headerView);
        //ʹ���Զ����Adapter
    	myListViewAdapter = new UserDBListAdapter(this,userList);
    	mListView.setAdapter(myListViewAdapter);
    	
    	if(userList == null || userList.size()==0){
			//���������ط�ҳ��
			mListViewForPage.setVisibility(View.GONE);
		}else{
			total.setText("������:" +String.valueOf(totalCount));
			current.setText("��ǰҳ:" + String.valueOf(pageNum));
			checkView();
			mListViewForPage.setVisibility(View.VISIBLE);
		}
        
        //���Ӽ�¼�İ�ť
        final Button addBtn = (Button)headerView.findViewById(R.id.addBtn);
        //���ӵ��ֶ�����
        final EditText mEditText = (EditText)headerView.findViewById(R.id.add_name);
        addBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//��ȡ�û����������
				String name = mEditText.getText().toString();
				if(name!=null && !"".equals(name.trim())){
					//����һ�����ݵ����ݿ�
					LocalUser u = new LocalUser();
					u.setName(name);
					saveData(u);
				}else{
					showToast("����������!");
				}
			}
        });
        
        //��һҳ�¼�
        preView.setOnTouchListener(new Button.OnTouchListener(){
		      @Override
		      public boolean onTouch(View arg0, MotionEvent arg1){
		        switch (arg1.getAction()) {
		          case MotionEvent.ACTION_DOWN:
		        	  preView();
		              break;
		          case MotionEvent.ACTION_MOVE:
		              break;
		          case MotionEvent.ACTION_UP:
		              break;
		          case MotionEvent.ACTION_CANCEL:
		            break;
		          default:
		              break;
		          }
		         return true;
		      } 
		 });
		
        //��һҳ�¼�
		nextView.setOnTouchListener(new Button.OnTouchListener(){
		      @Override
		      public boolean onTouch(View arg0, MotionEvent arg1){
		        switch (arg1.getAction()) {
		          case MotionEvent.ACTION_DOWN:
		        	  nextView();
		              break;
		          case MotionEvent.ACTION_MOVE:
		              break;
		          case MotionEvent.ACTION_UP:
		              break;
		          case MotionEvent.ACTION_CANCEL:
		            break;
		          default:
		              break;
		          }
		         return true;
		      } 
		});
        
      } 
    
    
    /*
     * ��һҳ
     */
	private void preView() {
		pageNum--;
		current.setText("��ǰҳ:" + String.valueOf(pageNum));
		userList.clear();
		
		queryData();
	}
    /*
     * ��һҳ
     */
	private void nextView() {
		pageNum++;
		current.setText("��ǰҳ:" + String.valueOf(pageNum));
		userList.clear();
		
		queryData();
	}
    
    /*
     * �ı��Ƿ�ɵ��
     */
	public void checkView() {
		if (pageNum <= 1) {
			//��һҳ�ı�Ϊ���ɵ��״̬
			preView.setEnabled(false);
			preView.setBackgroundResource(R.drawable.left_press);
			//������С��ÿҳ��ʾ������
			if (totalCount <= pageSize) {
				//��һҳ�ı�Ϊ���ɵ��״̬
				nextView.setEnabled(false);
				nextView.setBackgroundResource(R.drawable.right_press);
			}else{
				nextView.setEnabled(true);
				nextView.setBackgroundResource(R.drawable.right_normal);
			}
		}//������-��ǰҳ*ÿҳ��ʾ������ <=ÿҳ��ʾ������
		else if (totalCount - (pageNum-1) * pageSize <= pageSize){
			//��һҳ�ı�Ϊ���ɵ��״̬,��һҳ��Ϊ�ɵ��
			nextView.setEnabled(false);
			nextView.setBackgroundResource(R.drawable.right_press);
			preView.setEnabled(true);
			preView.setBackgroundResource(R.drawable.left_normal);
		}else {
			//��һҳ��һҳ�ı�����Ϊ�ɵ��״̬
			preView.setEnabled(true);
			preView.setBackgroundResource(R.drawable.left_normal);
			nextView.setEnabled(true);
			nextView.setBackgroundResource(R.drawable.right_normal);
		}
	}
	
	private void checkPageBar(){
    	if(userList == null || userList.size()==0){
			//���������ط�ҳ��
			mListViewForPage.setVisibility(View.GONE);
		}else{
			queryDataCount();
		}
    }
	
	/**
	 * 
	 * ��������ѯ����
	 * @throws 
	 */
	public void queryData(){
		//(1)��ȡ���ݿ�
		userDao.startReadableDatabase(false);
		//(2)ִ�в�ѯ
		List<LocalUser> userListNew = userDao.queryList(null, null, null, null, null, "create_time desc limit "+String.valueOf(pageSize)+ " offset " +String.valueOf((pageNum-1)*pageSize), null);
		//(3)�ر����ݿ�
		userDao.closeDatabase(false);
		
		userList.clear();
		if(userListNew!=null){
			 userList.addAll(userListNew);
		}
       
		myListViewAdapter.notifyDataSetChanged();
		checkPageBar();
	}
	
	/**
	 * 
	 * ��������ѯ����
	 * @throws 
	 */
	public void queryDataCount(){
		//(1)��ȡ���ݿ�
		userDao.startReadableDatabase(false);
		//(2)ִ�в�ѯ
		totalCount = userDao.queryCount(null, null);
		//(3)�ر����ݿ�
		userDao.closeDatabase(false);
		
		total.setText("������:" +String.valueOf(totalCount));
		current.setText("��ǰҳ:" + String.valueOf(pageNum));
		checkView();
		mListViewForPage.setVisibility(View.VISIBLE);
		
	}
	
	/**
	 * 
	 * ��������������
	 * @param u
	 * @throws 
	 */
	public void saveData(LocalUser u){
		//(1)��ȡ���ݿ�
		userDao.startWritableDatabase(false);
		//(2)ִ�в�ѯ
		long id = userDao.insert(u);
		//(3)�ر����ݿ�
		userDao.closeDatabase(false);
		
		//showToast("�������ݳɹ�,ID:"+id);
		//�������ݳɹ�
		if(id!=-1){
			//��ѯ����
			queryData();
		}
	}
	
	/**
	 * ��������
	 * ������TODO
	 * @param u
	 */
	public void updateData(LocalUser u){
		//(1)��ȡ���ݿ�
		userDao.startWritableDatabase(false);
		userDao.update(u);
		userDao.closeDatabase(false);
	}
	
	/**
	 * 
	 * ����������ID��ѯ����
	 * @param id
	 * @return
	 */
	public LocalUser queryDataById(int id){
		//(1)��ȡ���ݿ�
		userDao.startReadableDatabase(false);
		LocalUser u =  (LocalUser)userDao.queryOne(id);
		userDao.closeDatabase(false);
		return u;
	}
	
	/**
	 * 
	 * ������ɾ������
	 * @param id
	 */
	public void delData(int id){
		
		//(1)��ȡ���ݿ�
		userDao.startWritableDatabase(false);
		//(2)ִ�в�ѯ
		userDao.delete(id);
		//(3)�ر����ݿ�
		userDao.closeDatabase(false);
		
		queryData();
	}
    
}