package com.jit.cloud_print_cc;


import android.content.Intent;

import android.os.Bundle;


/*class Phoneaddr
{
	public String Province;
	public String City;
	public String District;
	public String Street;
	Phoneaddr(BDLocation location)
	{
		Province=location.getProvince();
		City=location.getCity();
		District=location.getDistrict();
		Street=location.getStreet();
	}
}*/
public class Activity_PrintSetPrintPoint extends Activity_CloudPrintTemplate {
	//public MapView mMapView = null;  
	//public BaiduMap mBaiduMap;
    //boolean isFirstLoc = true;// 是否首次定位
	
	//public MyLocationListenner myListener;
	//public LocationClient mLocClient;
	//private LocationMode mCurrentMode;
	
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        View_PrintSetPrintPoint mView=null;
        
       this.mViewTemplate=mView=new View_PrintSetPrintPoint(this);
       
   
         setContentView(mView.GetView());  
        
        //获取地图控件引用  
       //  mMapView = mView.GetBaiduMap();
         //mBaiduMap = mMapView.getMap();
        // this.InitLocation();
        /*
        setContentView(f_s_SearchPrintView.mLayoutId);  
        mMapView = (MapView) findViewById(f_s_SearchPrintView.mMapId);
        */
    } 
    /* 
    public void InitLocation()
    {
    	 // 开启定位图层
    	mCurrentMode = LocationMode.NORMAL;
 		mBaiduMap.setMyLocationEnabled(true);
 		myListener = new MyLocationListenner();
 	// 定位初始化
 			mLocClient = new LocationClient(this.getApplicationContext());
 			mLocClient.registerLocationListener(myListener);
 			LocationClientOption option = new LocationClientOption();
 			option.setOpenGps(true);// 打开gps
 			option.setCoorType("bd09ll"); // 设置坐标类型
 			option.setScanSpan(LocationClientOption.MIN_SCAN_SPAN_NETWORK);
 			option.setIsNeedAddress(true);
 			mLocClient.setLocOption(option);
 			mLocClient.start();
 			
 			this.mView.mButtonLocation.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					isFirstLoc=true;
					int request_r=mLocClient.requestLocation();
					
					if(request_r==0){
						//success
					}else if(request_r==1){
						//service not start
						mLocClient.start();
					}else{
						//other error
					}
					Toast toast_t=Toast.makeText(f_s_PrintSetPrintPointActivity.this,"正在定位......",Toast.LENGTH_SHORT);
					toast_t.setGravity(Gravity.CENTER,0,0);
					toast_t.getView().setBackgroundColor(0xc8ffffff);
					toast_t.show();
					;
				}
			});
    }
    */
    /*
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        	//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        	 
       		// 退出时销毁定位
    		mLocClient.stop();
    		// 关闭定位图层
    		mBaiduMap.setMyLocationEnabled(false);
    		mMapView.onDestroy();
    		mMapView = null;
    } */ 
    /*
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();  
        }*/  
    /*
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
        }  */
    /**
	 * 定位SDK监听函数
	 */
    /*
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
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

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	*/
	 @Override  
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	  {
		          // TODO Auto-generated method stub
		            if(resultCode ==View_CloudPrintTemplate.ResultCode_OrderIsSuccess) {
		            	this.setResult(resultCode);
		               finish();
		           }
	  }
}
