package com.jit.cloud_print_cc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import com.jit.cloud_print_cc.R;
import com.jit.config.GlobalConfig;
import com.jit.config.KEY.FileOperationWay;
import com.jit.config.KEY.OrderGeneratedMode;

public class View_PrintSetPrintPoint extends View_CloudPrintTemplate
{
	public enum PrintSelectedMode
	{
	  PrintNetShop,
	  PrintLocalSharedPrint
	}
    private PrintSelectedMode mPrintMode;
	/*-View启动模式-*/
	private  Set<String>      mFiles2Print=new HashSet<String>();
	private  FileOperationWay mStartMode= FileOperationWay.OPen2SetPrint;
	/*-------------View----------*/
	//public SwipeRefreshLayout mSwipeRefreshLayout;
	/*-------------View----------*/
	public MapView  mMapView = null;  
	public BaiduMap mBaiduMap=null;
	public Button   mButtonLocation;
	public Button   mButtonSetPrint;
	public Button   mButtonCanclePrint;
	public RadioGroup mRadioGroupPrintType;
	public TextView mTextViewPrintName;
	public TextView mTextVIewPrintAddr;
	public TextView mTextVIewFilesDes;
	public TextView mTextViewMainTitle;
	public FrameLayout MapParentCloudPrint=null;
	public  final  PullToRefreshListView  mSharedPrintList;
	private final  SharedPrinterAdapter   mSharedPrinterAdapter=new SharedPrinterAdapter();
	/*-----------------------*/
	
	public  ArrayList<CloudPrintAddress> mDataCloudPrintAddress=new ArrayList<CloudPrintAddress>();/**<可以打印的资源，Pc打印机,网络打印机，*/
	/*-------------资源ID----------*/
	public final static int mMapId=0;
	public final static int mMapBalloonImg=R.drawable.popup;
	public final static int mLayoutId=R.layout.activity_print_set_print_point;
	public final static  int COLOR_PRINTER_SELECTED_COLOR=0x145b7d|Color.BLACK;
	/*-------------IP 定位----------*/
	boolean isFirstLoc = true;// 是否首次定位
	//public MyLocationListenner myListener=new MyLocationListenner();
	
	public String mBaiduMapKey;
	@SuppressWarnings("unused")
	private LocationMode mCurrentMode;
	/*----------------------*/
	public View_PrintSetPrintPoint(Context context) {
		super(context, mLayoutId);
		// TODO Auto-generated constructor stub
		this.mSharedPrintList=(PullToRefreshListView) this.findViewById(R.id.ListView_shared_print);
		this.InitChildView2();
		this.InitViewByStartMode();
	
		
	}
	/*
	public f_s_PrintSetPrintPointView(Context context, PhoneInHand   PhoneInHand) {
		super(context, mLayoutId,PhoneInHand);
		// TODO Auto-generated constructor stub
		this.InitChildView2();
		this.InitViewByStartMode();
		
	}*/
	
	private void InitViewByStartMode(){
		
		FileOperaData data_t=this.GetFileOpMode();
		if(data_t!=null&&data_t.mFiles!=null){
			mFiles2Print.clear();
			mFiles2Print.addAll(data_t.mFiles);
			if(mFiles2Print!=null&&mFiles2Print.size()!=0){
				//有需要打印的文件
				this.mTextVIewFilesDes.setText("待打印文件数："+mFiles2Print.size());
			}else{
				//没有文件
				
			}
		}
		
		mStartMode=data_t.mMode;

		if(mStartMode!=null){
			if(mStartMode==FileOperationWay.OPen2SetDefaultPrint){
				
				this.InitSetDefaultPrintView();
			}else if(mStartMode==FileOperationWay.OPen2SetPrint){
			
				this.InitSetCurrentPrintView();
			}else{
				//不支持的模式
				this.mTextVIewFilesDes.setVisibility(View.GONE);				
			}
		}else{
			//启动模式不存在
		}
	}
 /**
 * 设置默认打印机
 * 
 */ 
	public  class StartNextView implements View.OnClickListener
	{
		public boolean StartViewMulity=true;
		private boolean CheckVariable()
		{
			if(mPrintPointTemp!=null&&mPrintMode!=null){
				 return true;
			}else{
				 return false;
			}
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(CheckVariable()){
				/*--启动下单Activity*/
				SetDefaultCloudPrintInfo2Disk();
				if(mPrintMode==PrintSelectedMode.PrintLocalSharedPrint){
		            SetOrderGeneratedMode(OrderGeneratedMode.SetLocalOrderList);
				}else if(mPrintMode==PrintSelectedMode.PrintNetShop){
					
					if(GlobalConfig.TotalFunctions){
						SetOrderGeneratedMode(OrderGeneratedMode.SetNetOrderList);						
					}else{
						Toast_make_show_center("功能暂未开放");
						return;
					}

				}else{
					
				}
				
				if(StartViewMulity){
					StartActivityView_with_Printer(Activity_Print_Order_MultiGenerated.class);
				}
			
			
			}else{
				Toast_make_show_center("请选择打印机");
			}
			
			
		}
	}
	
	private void InitSetDefaultPrintView()
	{
		this.mTextVIewFilesDes.setText("设置默认打印机");
		this.mTextVIewFilesDes.setVisibility(View.VISIBLE);
		/*if(!GlobalConfig.TotalFunctions){
			
			this.mButtonSetPrint.setVisibility(View.GONE);
			this.mButtonCanclePrint.setVisibility(View.GONE);
			return;
			
		}*/
		
		/*--button事件-*/
		this.mButtonSetPrint.setText("保存设置");
		
		if(GlobalConfig.TotalFunctions){
				this.mButtonSetPrint.setOnClickListener(new StartNextView());
		}else{
			
		}
	
		
		
		
		this.mButtonCanclePrint.setText("取消设置");
		this.mButtonCanclePrint.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//finish(ResultCode_DestoryWizzard);
				SetDefaultCloudPrintInfo2Mem(null);
			}
		});
		
	}
	 /**
	 * 选择打印机
	 * 
	 */ 
	private void InitSetCurrentPrintView()
	{
		this.mTextVIewFilesDes.setVisibility(View.VISIBLE);
		/*--button事件-*/
		this.mButtonSetPrint.setOnClickListener(new StartNextView());
		this.mButtonCanclePrint.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetDefaultCloudPrintInfo2Mem(null);
			}
		});
	}
	public void InitChildView2(){
		
		this.initControls();
		
		this.initMap();
		
		this.MapParentCloudPrint.addView(this.mMapView);
	}
 /**
 * 
 * 
 */ 
	public void initControls()
	{
		this.mTextViewMainTitle=(TextView) findViewById(R.id.text_view_register_printer_main_title);
		this.MapParentCloudPrint=(FrameLayout) this.findViewById(R.id.id_search_print_map_parent);
		
		this.mSharedPrintList.setAdapter(mSharedPrinterAdapter);
		mSharedPrintList.setOnItemClickListener(new SharedPrintItemClick());
		this.mButtonLocation=(Button) this.findViewById(R.id.map_location_request);
		///////////////
		this.mButtonSetPrint=(Button) this.findViewById(R.id.button_set_print);
		
		this.mButtonCanclePrint=(Button) this.findViewById(R.id.button_cancle_print);
		this.mRadioGroupPrintType=(RadioGroup) this.findViewById(R.id.radioGroup_print_type);
		this.mTextViewPrintName=(TextView) this.findViewById(R.id.textView_print_shop_name);
		this.mTextVIewPrintAddr=(TextView) this.findViewById(R.id.textView_print_shop_addr);
		this.mTextVIewFilesDes=(TextView) this.findViewById(R.id.textView_print_shop_files_des);
		/*--button事件-*/
		this.mButtonCanclePrint.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetDefaultCloudPrintInfo2Mem(null);
			}
		});
		this.mRadioGroupPrintType.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				 SetDefaultCloudPrintInfo2Mem(null);
				 if(checkedId==R.id.radio_print_type_cloud){
					 initCloudView();
				 }else if(checkedId==R.id.radio_print_type_sheared){
					 initShearedView();
				 }else{
					 mTextVIewFilesDes.setText("mRadioGroupPrintType");
				 }
			}
			
		});
		if(this.mRadioGroupPrintType.getCheckedRadioButtonId()==R.id.radio_print_type_cloud){
			 initCloudView();
		}else if(this.mRadioGroupPrintType.getCheckedRadioButtonId()==R.id.radio_print_type_sheared){
			 initShearedView();
		}else{
			
		}
		
		if(GlobalConfig.TotalFunctions){
			
		}else{
				this.mRadioGroupPrintType.check(R.id.radio_print_type_sheared);
		}
	
		
		if(this.mButtomNext!=null) this.mButtomNext.setOnClickListener(new StartNextView());
		 this.InitViewByStartMode();
		 
		 initListView(mSharedPrintList);
	}
	
	private void initListView(final PullToRefreshListView listview)
	{
	// TODO Auto-generated method stub
		//listview..getRefreshableView().setGroupIndicator(null);  
		listview.getRefreshableView().setDivider(null);  
		listview.getRefreshableView().setSelector(android.R.color.transparent);  
		   listview.setMode(Mode.BOTH);  
		listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
	 
		@Override
		public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			UDP_Broad_Msg_Request();
			postDelayed(new Runnable(){
				@Override
				public void run(){					
					refreshView.onRefreshComplete();
				}
			},2000);
			
		}
	});  
    }

	/*public void initView(View V)
	{
		//final PullToRefreshListView mExpandList = (PullToRefreshListView)findViewById(id);  
	    //mExpandList.getRefreshableView().setGroupIndicator(null);  
	    mExpandList.getRefreshableView().setDivider(null);  
	    mExpandList.getRefreshableView().setSelector(android.R.color.transparent);  
	   
	    mExpandList.setOnRefreshListener(new OnRefreshListener<ListView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			postDelayed(new Runnable(){
				public void run(){
					mExpandList.setRefreshing(false);
				}
			},1000);
		}
	});  
	}*/
	/*public void initSwipRefreshLayout()
	{
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);  
		if(mSwipeRefreshLayout!=null){
			
			mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					postDelayed(new Runnable(){
						public void run(){
							mSwipeRefreshLayout.setRefreshing(false);
						}
					},3000);
				}
			});  
			mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,  
		                android.R.color.holo_green_light,  
		                android.R.color.holo_orange_light,  
		                android.R.color.holo_red_light);  
			
		}
	
	}*/
	/**
	 * 初始化共享打印视图
	 */
	public void initShearedView()
	{
		 View_PrintSetPrintPoint.this.MapParentCloudPrint.setVisibility(View.GONE);
		 mButtonLocation.setVisibility(View.GONE);
		 View_PrintSetPrintPoint.this.mSharedPrintList.setVisibility(View.VISIBLE);
		 mPrintMode=PrintSelectedMode.PrintLocalSharedPrint;
        if(this.mRadioGroupPrintType.getCheckedRadioButtonId()==R.id.radio_print_type_sheared){
        	//checked position 位置正确
		 }else{
			 this.mRadioGroupPrintType.check(R.id.radio_print_type_sheared);
		 }
        this.InitWizardViewMode();
	}
	/**
	 * 初始化云打印视图
	 * 
	 * 
	 */
	public void initCloudView()
	{
		 MapParentCloudPrint.setVisibility(View.VISIBLE);
		 mButtonLocation.setVisibility(View.VISIBLE);
		 mSharedPrintList.setVisibility(View.GONE);
		 mPrintMode=PrintSelectedMode.PrintNetShop;
		 //验证radio位置
		 if(this.mRadioGroupPrintType.getCheckedRadioButtonId()==R.id.radio_print_type_cloud){
			 //checked position 位置正确
		 }else{
			 this.mRadioGroupPrintType.check(R.id.radio_print_type_cloud);
		 }
		 this.InitWizardViewMode();
	}
	/**
	 * 
	 * 
	 **/
	@Override
	    protected void InitNormalWizardViewMode()
		{
			super.InitNormalWizardViewMode();
			if(mPrintMode==PrintSelectedMode.PrintLocalSharedPrint){
				this.mTextViewMainTitle.setText("本地打印机列表");
			}else if(mPrintMode==PrintSelectedMode.PrintNetShop){
				this.mTextViewMainTitle.setText("云端打印搜索地图");
			}else{
				this.mTextViewMainTitle.setText("suck");
			}
			
		}
	/**
	 * 
	 * 
	 **/
	@Override
		protected void InitWizardWizardViewMode()
		{
			super.InitWizardWizardViewMode();
			if(mPrintMode==PrintSelectedMode.PrintLocalSharedPrint){
				//this.mTextViewMainTitle.setText("第二步：选择打印设备（共享打印机）");
				this.mTextViewMainTitle.setText("第二步：选择打印设备");
			}else if(mPrintMode==PrintSelectedMode.PrintNetShop){
				//this.mTextViewMainTitle.setText("第二步：选择打印设备（云端文印店）");
				this.mTextViewMainTitle.setText("第二步：选择打印设备");
			}else{
				this.mTextViewMainTitle.setText("suck");
			}
		}
	/**
	 * 
	 * 初始化共享打印机视图
	 * 
	 */
	public void StartActivityView_with_Printer(Class<?> cls)
	{
		Bundle bundle=new Bundle();
	    Intent intent=new Intent();
	    if(mPrintPointTemp!=null){
	    	if(mLocation!=null){
	    		mPrintPointTemp.SeBDtLocation(mLocation);
	    	}	    	
	    	  intent.putExtra(GetPrinter2UseKey(),mPrintPointTemp.Clear2Serializable());
	    }	  
	    this.SetWizardViewModeByParent();
		this.StartActivityView(cls, bundle,intent);
		
	}
	
	public void initMap()
	{
	//	this.mBaiduMapKey=appcontext.getResources().getString(R.string.baidu_map_key);
		this.mMapView= new MapView(getContext(), new BaiduMapOptions());
		this.mBaiduMap = mMapView.getMap();
		
		this.InitLocation();
		/*
		Button button=new Button(mContext);
		button.setText("定位");
		button.setWidth(100);
		button.setHeight(100);
		this.mMapView.addView(button);*/
		this.initCloudPoint();
		
	}
	public void initCloudPoint()
	{
		//this.mBaiduMap.addOverlay(MarkerBaiduView.GetDefaultMarker());
		
		MarkerBaiduView.AddDefaultMarker(mBaiduMap);
		OnMarkClick OMC=new OnMarkClick();
		this.mBaiduMap.setOnMapClickListener(OMC);
		this.mBaiduMap.setOnMarkerClickListener(OMC);
		//GetPrintShopStatic
		if(GlobalConfig.TotalFunctions){
			this.SetDefaultCloudPrintInfo2Mem(MarkerBaiduView.GetPrintShopStatic().get(0));
		}
	}
	 public void InitLocation()
	    {
	    	 // 开启定位图层
	    	mCurrentMode = LocationMode.NORMAL;
	 		mBaiduMap.setMyLocationEnabled(true);
	 	
	 	// 定位初始化
	 			this.InitLocationClient();
	 			
	 			mButtonLocation.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						isFirstLoc=true;
						RequestLocationClient();
						Toast toast_t=Toast.makeText(v.getContext(),"正在定位......",Toast.LENGTH_SHORT);
						toast_t.setGravity(Gravity.CENTER,0,0);
						toast_t.getView().setBackgroundColor(0xc8ffffff);
						toast_t.show();
						;
					}
				});
	    }
	 /*
	public MapView GetBaiduMap()
	{
		return this.mMapView;
	}*/
	 @Override  
	    public void onPause()
	 {  
	        super.onPause();  
	        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
	        mMapView.onPause();  
	 }  
	 @Override  
	    public void onResume() 
	 {  
	        super.onResume();  
	        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
	        mMapView.onResume();  
	  }  
	  @Override  
	    public void onDestroy() 
	  {  
	        super.onDestroy();  
	        	//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
	        	 
	       		// 退出时销毁定位
	    		
	    		// 关闭定位图层
	    		mBaiduMap.setMyLocationEnabled(false);
	    		mBaiduMap=null;
	    		mMapView.onDestroy();
	    		mMapView = null;
	    }  
 /**
 * 
 * 
 */ 
	  public void SetDefaultCloudPrintInfo2Mem( CloudPrintAddress info)
	  {
		  if(info==null){
			//  this.finish(ResultCode_DestoryWizzard);
		  }
		  mPrintPointTemp=info;
		  
		  if(mPrintMode==PrintSelectedMode.PrintNetShop){
			  /*写入XML文件*/
			  this.mPrintPointTemp=info;
			  if(info!=null){
				     mTextViewPrintName.setText("打印店名称："+info.getName());
					 mTextVIewPrintAddr.setText("打印店地址："+info.getAddr());
			  }else{
				     mTextViewPrintName.setText("打印店名称：");
					 mTextVIewPrintAddr.setText("打印店地址：");
			  }
		  }else if(mPrintMode==PrintSelectedMode.PrintLocalSharedPrint){
			  /*写入XML文件*/
			  this.mPrintPointTemp=info;
			  if(info!=null){
				     mTextViewPrintName.setText("打印机名称："+info.getName());
					 mTextVIewPrintAddr.setText("打印机IP地址："+info.getAddr());
			  }else{
				    mTextViewPrintName.setText("打印机名称：");
					 mTextVIewPrintAddr.setText("打印机IP地址：");
			  }
			  
		  }else{
			     mTextViewPrintName.setVisibility(View.GONE);
				 mTextVIewPrintAddr.setVisibility(View.GONE);
		  }
		
		
		  
	  }
  /**
   * 
   * 
   */ 
	  public void SetDefaultCloudPrintInfo2Disk()
	  {
		  
		
		  
	  }
 /**
 * 
 * 
 */	  
	  public  class OnMarkClick implements  OnMarkerClickListener,OnMapClickListener
	  {
		  public OnMarkClick(){
			  
		  }
		  public View GetView( CloudPrintAddress info)
		  {
			  //View v=null;
			    Button button = new Button(getApplicationContext());
				button.setBackgroundResource(mMapBalloonImg);
				button.setText(info.getName());
				button.setTextColor(Color.BLACK);
			  
			  return button;
		  }
		  public InfoWindow getInfoWindow( CloudPrintAddress info)
		  {
			  View v=this.GetView(info);
			  OnInfoWindowClickListener listener = null;
				listener = new OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick() {
						//隐藏InfoWindow
						mBaiduMap.hideInfoWindow();
						try {
							this.clone();
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				
				LatLng ll = new LatLng(info.getLatitude_Double(),info.getLongitude_Double());
				return  new InfoWindow(BitmapDescriptorFactory.fromView(v), ll,0, listener);
				
		  }
		  @Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
			  CloudPrintAddress info = ( CloudPrintAddress) arg0.getExtraInfo().get("info");
				
				SetDefaultCloudPrintInfo2Mem(info);
				
				/*
				LatLng ll = arg0.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				Log.e("TAG", "--!" + p.x + " , " + p.y);
				//p.x +=50;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
				*/
				
				mBaiduMap.showInfoWindow(this.getInfoWindow(info));
				return false;
			}
		  @Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				mBaiduMap.hideInfoWindow();
			}

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}
	  }
@Override
protected void  WhenOnReceiveLocation(BDLocation location)
{
	// map view 销毁后不在处理新接收的位置
	super.WhenOnReceiveLocation(location);
	if (location == null || mMapView == null)
		return;
	MyLocationData locData = new MyLocationData.Builder()
			.accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
			.direction(location.getDirection())
			.latitude(location.getLatitude())
			.longitude(location.getLongitude())
			.build();
	mBaiduMap.setMyLocationData(locData);
	if (isFirstLoc) {
		isFirstLoc = false;
		mBaiduMap.setMyLocationData(locData);
		LatLng ll = new LatLng(location.getLatitude(),
				location.getLongitude());
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(u);
	}
	

}
/**
 * 更新打印机列表服务
 * 
 * 
 * 
 **/
@Override
protected void  PrintersNowIsUpdata(ArrayList<CloudPrintAddress> Printers)
{
	mSharedPrinterAdapter.SetData(mNetPhonePc.mPCPhonePrinterList.GetPrinters());
}
/**
 * 
 * 线程回调本页面
 * 
 **/
public class NotifyThisView
{
	
}
/**
* 
* 共享打印机启动
* 
* 
**/
 public class SharedPrintItemClick implements AdapterView.OnItemClickListener
 {

	@Override
	public void onItemClick(
			AdapterView<?> parent,
			View view,
			int position,
			long id) {
		// TODO Auto-generated method stub	
		String Profix_parent_name=parent.getClass().getName().toString();
		String Proix_PullToRefreshListView=PullToRefreshListView.class.getName();
		if(Profix_parent_name.contains(Proix_PullToRefreshListView)){
			 position-=1;
		}
		
		/*if(((Object) parent instanceof PullToRefreshListView)){
			
		}	*/	
		if(position<mDataCloudPrintAddress.size()){
				CloudPrintAddress cpa=mDataCloudPrintAddress.get(position);
				SetDefaultCloudPrintInfo2Mem(cpa);
				view.setBackgroundColor(COLOR_PRINTER_SELECTED_COLOR);
				mSharedPrinterAdapter.notifyDataSetChanged();
		}
	 
 }
}
/**
 * 
 * 共享打印机适配器
 * 
 * 
 */	 
public class SharedPrinterAdapter extends  BaseAdapter
		{		
	      
			public void SetData(final ArrayList<CloudPrintAddress> Selected) {				
				
				
				postDelay(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(Selected!=null){
							mDataCloudPrintAddress.clear();
							mDataCloudPrintAddress.addAll(Selected);
						}				
						
					   notifyDataSetChanged();
					   mSharedPrintList.onRefreshComplete();
					}
					
				});
						
					
					
				
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mDataCloudPrintAddress.size();
			}
		
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return mDataCloudPrintAddress.get(position);
			}
		
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
		
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				CloudPrintAddress pi=mDataCloudPrintAddress.get(position);
				if(convertView==null){
					convertView=View_OfficeEnvironment.GetViewListItem(
							parent.getContext(), 
							parent,
							false,
							R.id.id_oe_list_item_image,
							R.id.id_oe_list_item_text,
							R.drawable.oe_net_print, 
							pi.GetPrinterDes(), 
							R.layout.office_envriment_wifi_list_item);
				}else{
					((TextView)(convertView.findViewById(R.id.id_oe_list_item_text))).setText(pi.GetPrinterDes());
				}
				 ImageView iv = (ImageView) convertView.findViewById(R.id.id_oe_list_item_image);
				 Button   button=(Button) convertView.findViewById(R.id.id_oe_list_item_button);
				 View_OfficeEnvironment.SetPrinterImg(pi, iv);
				 if(button!=null){
					 button.setVisibility(View.GONE);
				 }
				if(mPrintPointTemp!=null&&mPrintPointTemp==pi){
					//当前View已被选中
					convertView.setBackgroundColor(COLOR_PRINTER_SELECTED_COLOR);
				}else{
					convertView.setBackgroundColor(Color.TRANSPARENT);
				}
				
				return convertView;
			}
			
		}
}
