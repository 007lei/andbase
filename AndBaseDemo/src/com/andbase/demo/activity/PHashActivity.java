package com.andbase.demo.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ab.activity.AbActivity;
import com.ab.global.AbConstant;
import com.ab.global.AbResult;
import com.ab.task.AbTaskCallback;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskPool;
import com.ab.util.AbAppUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
import com.ab.util.AbMathUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.ab.view.chart.CategorySeries;
import com.ab.view.chart.ChartFactory;
import com.ab.view.chart.PointStyle;
import com.ab.view.chart.XYMultipleSeriesDataset;
import com.ab.view.chart.XYMultipleSeriesRenderer;
import com.ab.view.chart.XYSeriesRenderer;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.ImageShowAdapter;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;

public class PHashActivity extends AbActivity {
	private static final String TAG = "PHashActivity";
	private static final boolean D = Constant.DEBUG;

	private MyApplication application;
	private GridView mGridView = null;
	private ImageShowAdapter mImagePathAdapter = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private View mAvatarView = null;
	/* ������ʶ�������๦�ܵ�activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* ������ʶ����gallery��activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* ������ʶ����ü�ͼƬ���activity */
	private static final int CAMERA_CROP_DATA = 3022;
	/* ���յ���Ƭ�洢λ�� */
	private  File PHOTO_DIR = null;
	// ��������յõ���ͼƬ
	private File mCurrentPhotoFile;
	private String mFileName;
	
	private ImageView myImage = null;
	
	private AbResult mAbResult = null;
	
	private StringBuffer filePathAll = null;
	
	private List<String> hashCodes = new ArrayList<String>();
	private List<File> files = new ArrayList<File>();
	private HashMap<Integer, Integer> hashCodesAndDis = new HashMap<Integer, Integer>();
	private com.ab.task.AbTaskPool mAbTaskPool = AbTaskPool.getInstance();
	
	private List<int[]> colorHistogram = new ArrayList<int[]>();
	private LinearLayout mChartLinearLayout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.p_hash);
		application = (MyApplication) abApplication;
		
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.phash_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		
		initTitleRightLayout();
		
		mGridView = (GridView)findViewById(R.id.myGrid);
		mImagePathAdapter = new ImageShowAdapter(this, mPhotoList,116,116);
		mGridView.setAdapter(mImagePathAdapter);
	    mAvatarView = mInflater.inflate(R.layout.choose_avatar, null);
	    //Ҫ��ʾͼ�ε�View
	    mChartLinearLayout = (LinearLayout) findViewById(R.id.chart01);
	    //��ʼ��ͼƬ����·��
	    String photo_dir = AbFileUtil.getDefaultImageDownPathDir();
	    if(AbStrUtil.isEmpty(photo_dir)){
	    	showToast("�洢��������");
	    }else{
	    	PHOTO_DIR = new File(photo_dir);
	    }
		
		Button albumButton = (Button)mAvatarView.findViewById(R.id.choose_album);
		Button camButton = (Button)mAvatarView.findViewById(R.id.choose_cam);
		Button cancelButton = (Button)mAvatarView.findViewById(R.id.choose_cancel);
		
		albumButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				removeDialog(AbConstant.DIALOGBOTTOM);
				// �������ȥ��ȡ
				try {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setType("image/*");
					startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
				} catch (ActivityNotFoundException e) {
					showToast("û���ҵ���Ƭ");
				}
			}
			
		});
		
		camButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				removeDialog(AbConstant.DIALOGBOTTOM);
				doPickPhotoAction();
			}
			
		});
		
		cancelButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				removeDialog(AbConstant.DIALOGBOTTOM);
			}
			
		});
		
		Button addBtn = (Button)findViewById(R.id.addBtn);
		Button createBtn = (Button)findViewById(R.id.createBtn);
		myImage = (ImageView)findViewById(R.id.myImage);
		addBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(filePathAll==null){
					showToast("���ȴ���ͼƬ����!");
				}else{
					showDialog(1,mAvatarView);
				}
				
			}
			
		});
		
		createBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				showProgressDialog();
				final AbTaskItem item = new AbTaskItem();
				item.callback = new AbTaskCallback() {

					@Override
					public void update() {
						removeProgressDialog();
						showToast("����ͼƬ�������!");
					}

					@Override
					public void get() {
			   		    try {
			   		    	filePathAll = new StringBuffer();
			   		    	hashCodes.clear();
			   		    	hashCodesAndDis.clear();
			   		    	files.clear();
			   		    	colorHistogram.clear();
							AbAppUtil.prepareStartTime();
							//��ѯ�ֻ�������ͼƬ
							if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
							   //��sd��
							}else{
								 //��sd��
								 File pathSD = Environment.getExternalStorageDirectory();
					        	 File fileDirectory = new File(pathSD.getAbsolutePath()
					        				+ File.separator + "DCIM" + File.separator);
					        	 listFile(fileDirectory);
					    	}
							//ѭ������ͼƬ������ƶ�
							String fileStrs = filePathAll.toString();
							if(!AbStrUtil.isEmpty(fileStrs)){
								if(fileStrs.indexOf(",")!=-1){
									String [] paths = fileStrs.split(",");
									for(int i=0;i<paths.length;i++){
										String str = paths[i];
										if(!AbStrUtil.isEmpty(str)){
											files.add(new File(str));
										}
									}
								}
							}
							
							//����ͼƬ��hash
							for(int i=0;i<files.size();i++){
								File f = files.get(i);
								if(D)Log.d(TAG, "ͼƬ��·���� = " + f.getPath());
								Bitmap bitmap = AbFileUtil.getBitmapFromSD(f);
								if(bitmap==null){
									//ͼƬ������
									files.remove(i);
									i--;
									break;
								}else{
									//����hash
									String hashCode = AbImageUtil.getDCTHashCode(bitmap);
									hashCodes.add(hashCode);
									Log.d(TAG,"hashCodes add:"+i+":"+hashCode);
									//��ɫ�ֲ�
									Bitmap bitmapT = AbImageUtil.cutImg(bitmap,360,360);
									int [] colors = AbImageUtil.getColorHistogram(bitmapT);
									colorHistogram.add(colors);
									AbImageUtil.releaseBitmap(bitmap);
								}
							}
							AbAppUtil.logEndTime(D, TAG, "��������");
			   		    } catch (Exception e) {
			   		    	showToastInThread(e.getMessage());
			   		    }
				  };
				};
				mAbTaskPool.execute(item);
			}
			
		});
		
		
	}
	
	private void initTitleRightLayout() { 
		
	}
	
	/**
	 * ���������������ȡ
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		//�ж��Ƿ���SD��,�����sd������sd����˵��û��sd��ֱ��ת��ΪͼƬ
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			showToast("û�п��õĴ洢��");
		}
	}

	/**
	 * ���ջ�ȡͼƬ
	 */
	protected void doTakePhoto() {
		try {
			mFileName = System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			showToast("δ�ҵ�ϵͳ�������");
		}
	}
	
	/**
	 * ��������Ϊ������Camera��Gally����Ҫ�ж����Ǹ��Եķ������,
	 * ��������ʱ��������startActivityForResult
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		if (resultCode != RESULT_OK){
			return;
		}
		switch (requestCode) {
			case PHOTO_PICKED_WITH_DATA:
				Uri uri = mIntent.getData();
				String currentFilePath = getPath(uri);
				if(!AbStrUtil.isEmpty(currentFilePath)){
					Intent intent1 = new Intent(this, CropImageActivity.class);
					intent1.putExtra("PATH", currentFilePath);
					startActivityForResult(intent1, CAMERA_CROP_DATA);
		        }else{
		        	showToast("δ�ڴ洢�����ҵ�����ļ�");
		        }
				break;
			case CAMERA_WITH_DATA:
				if(D)Log.d(TAG, "��Ҫ���вü���ͼƬ��·���� = " + mCurrentPhotoFile.getPath());
				String currentFilePath2 = mCurrentPhotoFile.getPath();
				Intent intent2 = new Intent(this, CropImageActivity.class);
				intent2.putExtra("PATH",currentFilePath2);
				startActivityForResult(intent2,CAMERA_CROP_DATA);
				break;
			case CAMERA_CROP_DATA:
				mChartLinearLayout.removeAllViews();
				String path = mIntent.getStringExtra("PATH");
		    	if(D)Log.d(TAG, "�ü���õ���ͼƬ��·���� = " + path);
		    	
		    	Bitmap bitmap = AbFileUtil.getBitmapFromSD(new File(path));
		    	myImage.setImageBitmap(bitmap);
		    	
		    	hashCodesAndDis.clear();
		    	mPhotoList.clear();
		    	mImagePathAdapter.notifyDataSetChanged();
		    	AbViewUtil.setAbsListViewHeight(mGridView,3,25);
		    	
		    	//�ȹ�����ɫ����
		    	//��ɫ�ֲ�
				int [] sourceColors = AbImageUtil.getColorHistogram(bitmap);
		    	
		    	//��ȡ���ͼ��hashcode
		    	String sourceHashCode = AbImageUtil.getDCTHashCode(bitmap);
		    	Log.d(TAG,"this image sourceHashCode:"+sourceHashCode);
		    	//�������
		    	for(int i = 0;i<hashCodes.size();i++){ 
		    		String hashCode = hashCodes.get(i);
		    		int distanceNew = AbImageUtil.hammingDistance(sourceHashCode, hashCode);
		    		if(distanceNew<5){
		    			hashCodesAndDis.put(i,distanceNew);
		    		}
		    	}
		    	
		    	//���򰴾�������
		    	Set set = hashCodesAndDis.entrySet();
                Map.Entry[] hashCodesAndDisNew = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
                Arrays.sort(hashCodesAndDisNew, new Comparator() {
                  public int compare(Object arg0, Object arg1) {
    	             Long key1 = Long.valueOf(((Map.Entry) arg0).getValue().toString());
    	             Long key2 = Long.valueOf(((Map.Entry) arg1).getValue().toString());
    	             return key1.compareTo(key2);
    	           }
                });
		    	
                //��ʾ
                for(int i=0;i<hashCodesAndDisNew.length;i++){
            	   String pathNew = files.get((Integer)hashCodesAndDisNew[i].getKey()).getPath();
            	   Log.d(TAG,"ƥ����:"+pathNew+":"+hashCodesAndDisNew[i].getValue());
            	   mImagePathAdapter.addItem(mImagePathAdapter.getCount(),pathNew);
            	   
            	   
            	   int [] sourceColors1 = colorHistogram.get(i);
       			   for(int j = 0;j<sourceColors1.length;j++){ 
       		    		sourceColors1[j] = sourceColors[j]-sourceColors1[j];
       		       }
       			   createChart(sourceColors1,"ͼƬ"+i);
            	   
            	   AbViewUtil.setAbsListViewHeight(mGridView,3,25);
                }
                
		    
			    break;
		}
	}

	/**
	 * �����õ���urlת��ΪSD����ͼƬ·��
	 */
	public String getPath(Uri uri) {
		if(AbStrUtil.isEmpty(uri.getAuthority())){
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}
	
	public void listFile(File directory){
		if(directory.isDirectory()){
			File[] files = directory.listFiles();
			if(files!=null){
		          for(int i=0;i<files.length;i++){
		            if(files[i].isDirectory()){
		            	listFile(files[i]);
		            }else{
		            	String fName = files[i].getName();
		            	String end = fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase(); 
		            	
		            	if(end.equals("jpg")||end.equals("png")){
		            		filePathAll.append(files[i].getPath()+",");
		            	}
		            }
		          }
		      }
        }
	}
	
	public void createChart(int [] colorsArea,String title){
		
		LinearLayout chartView = (LinearLayout)mInflater.inflate(R.layout.chart_view_item, null);
		
		//˵������
		String[] titles = new String[] { "��ɫ��"};
		//����
	    List<double[]> values = new ArrayList<double[]>();
	    //ÿ�����ݵ����ɫ
	    List<int[]> colors = new ArrayList<int[]>();
	    //ÿ�����ݵ�ļ�Ҫ ˵��
	    List<String[]> explains = new ArrayList<String[]>();
	    
	    values.add(AbMathUtil.intToDoubleArray(colorsArea));
	    
	    int [] colorsPoint =  new int[colorsArea.length];
	    //������4����
	    for(int i=0;i<colorsArea.length;i++){
	    	if(i/16>3){
	    		colorsPoint[i] = Color.rgb(255, 63, 63);
	    	}else if(i/16>2){
	    		colorsPoint[i] = Color.rgb(191, 127, 127);
	    	}else if(i/16>1){
	    		colorsPoint[i] = Color.rgb(127, 191, 191);
	    	}else{
	    		colorsPoint[i] = Color.rgb(63, 255, 255);
	    	}
	    	
	    }
	    colors.add(colorsPoint);
	    explains.add(new String[colorsArea.length]);
	    
	    //�������������ɫ
	    int[] mSeriescolors = new int[] { Color.rgb(153, 204, 0)};
	    //������Ⱦ��
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    int length = mSeriescolors.length;
	    for (int i = 0; i < length; i++) {
	      //����SimpleSeriesRenderer��һ��Ⱦ��
	      XYSeriesRenderer r = new XYSeriesRenderer();
	      //SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	      //������Ⱦ����ɫ
	      r.setColor(mSeriescolors[i]);
	      r.setFillBelowLine(true);
	      r.setFillBelowLineColor(mSeriescolors[i]);
	      r.setFillPoints(true);
		  r.setPointStyle(PointStyle.CIRCLE);
		  r.setLineWidth(1);
		  r.setChartValuesTextSize(16);
	      //���뵽������
	      renderer.addSeriesRenderer(r);
	    }
	    //������������ִ�С
		renderer.setAxisTitleTextSize(16);
		//ͼ�α������ִ�С
		renderer.setChartTitleTextSize(25);
		//�����ϱ�ǩ���ִ�С
		renderer.setLabelsTextSize(15);
		//˵�����ִ�С
		renderer.setLegendTextSize(15);
		//ͼ�����
	    renderer.setChartTitle(title);
	    //X�����
	    renderer.setXTitle("����");
	    //Y�����
	    renderer.setYTitle("����");
	    //X����С�����
	    renderer.setXAxisMin(-2);
	    //X����������
	    renderer.setXAxisMax(64);
	    //Y����С�����
	    renderer.setYAxisMin(-20000);
	    //Y����������
	    renderer.setYAxisMax(20000);
	    //��������ɫ
	    renderer.setAxesColor(Color.rgb(51, 181, 229));
	    renderer.setXLabelsColor(Color.rgb(51, 181, 229));
	    renderer.setYLabelsColor(0,Color.rgb(51, 181, 229));
	    //����ͼ���ϱ�����X����Y���˵��������ɫ
	    renderer.setLabelsColor(Color.GRAY);
	    //renderer.setGridColor(Color.GRAY);
	    //��������Ӵ�
		renderer.setTextTypeface("sans_serif", Typeface.BOLD);
		//������ͼ�����Ƿ���ʾֵ��ǩ
	    renderer.getSeriesRendererAt(0).setDisplayChartValues(false);
	    //��ʾ��Ļ�ɼ�ȡ����XY�ָ���
	    renderer.setXLabels(10);
	    renderer.setYLabels(10);
	    //X�̶ȱ�ǩ���X��λ��
	    renderer.setXLabelsAlign(Align.CENTER);
	    //Y�̶ȱ�ǩ���Y��λ��
	    renderer.setYLabelsAlign(Align.LEFT);
	    renderer.setPanEnabled(false, false);
	    renderer.setZoomEnabled(false);
	    renderer.setZoomButtonsVisible(false);
	    renderer.setZoomRate(1.1f);
	    renderer.setBarSpacing(0.5f);
	    
	    //��߿���
	    renderer.setScaleLineEnabled(false);
	    //���ñ����ʾ���
	    renderer.setScaleRectHeight(10);
	    //���ñ����ʾ���
	    renderer.setScaleRectWidth(150);
	    //���ñ����ʾ�򱳾�ɫ
	    renderer.setScaleRectColor(Color.argb(150, 52, 182, 232));
	    renderer.setScaleLineColor(Color.argb(175, 150, 150, 150));
	    renderer.setScaleCircleRadius(35);
	    //��һ�����ֵĴ�С
	    renderer.setExplainTextSize1(20);
	    //�ڶ������ֵĴ�С
	    renderer.setExplainTextSize2(20);
	    
	    //�ٽ���
	    //double[] limit = new double[]{15000,12000,4000,9000};
	    //renderer.setmYLimitsLine(limit);
	    //int[] colorsLimit = new int[] { Color.rgb(100, 255,255),Color.rgb(100, 255,255),Color.rgb(0, 255, 255),Color.rgb(0, 255, 255) };
	    //renderer.setmYLimitsLineColor(colorsLimit);
	    
	    //��ʾ�����
	    renderer.setShowGrid(true);
	    //���ֵ��0�Ƿ�Ҫ��ʾ
	    renderer.setDisplayValue0(true);
	    //������Ⱦ�����������
	    XYMultipleSeriesDataset mXYMultipleSeriesDataset = new XYMultipleSeriesDataset();
	    for (int i = 0; i < length; i++) {
	      CategorySeries series = new CategorySeries(titles[i]);
	      double[] v = values.get(i);
	      int[] c = colors.get(i);
	      String[] e = explains.get(i);
	      int seriesLength = v.length;
	      for (int k = 0; k < seriesLength; k++) {
	    	  //����ÿ�������ɫ
	          series.add(v[k],c[k],e[k]);
	      }
	      mXYMultipleSeriesDataset.addSeries(series.toXYSeries());
	    }
	    //����
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.rgb(235, 235, 235));
	    renderer.setMarginsColor(Color.rgb(235, 235, 235));
	    
	    //��ͼ
	    View chart = ChartFactory.getAreaChartView(this,mXYMultipleSeriesDataset,renderer,0.2f);
	    chartView.addView(chart);
	    chartView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 250));
	    mChartLinearLayout.addView(chartView);
			    
	}
	
	
	

}
