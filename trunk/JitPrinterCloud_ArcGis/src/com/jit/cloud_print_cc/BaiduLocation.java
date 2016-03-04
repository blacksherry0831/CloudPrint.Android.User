package com.jit.cloud_print_cc;


import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class BaiduLocation 
{
	 protected volatile boolean mThreadRun=true;
	 private   LocationClient mLocClient;
	 protected BDLocation mLocation=null;
	 Context mCtx;
	 final int SCAN_SPAN_NETWORK=3000;//5分钟
	 BaiduLocation( Context mCtx,BDLocationListener l){
		 this.mCtx=mCtx;
		 this.InitLocationClient(l);
	 }
	 public void InitLocationClient(BDLocationListener l)
		{
			    mLocClient = new LocationClient(getContext().getApplicationContext());
				mLocClient.registerLocationListener(l);
				LocationClientOption option = new LocationClientOption();
				option.setOpenGps(true);// 打开gps
				option.setCoorType("bd09ll"); // 设置坐标类型
				option.setScanSpan(SCAN_SPAN_NETWORK);
				option.setIsNeedAddress(true);
				mLocClient.setLocOption(option);
				mLocClient.start();
		}
	 public void RequestLocationClient()
		{
			int request_r=mLocClient.requestLocation();
			
			if(request_r==0){
				//success
			}else if(request_r==1){
				//service not start
				mLocClient.start();
			}else{
				//other error
			}
		}
	private Context getContext() {
		// TODO Auto-generated method stub
		return  this.mCtx;
	}
	/*
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			mLocation=location;
			WhenOnReceiveLocation(location);
		}
	}
	protected void  WhenOnReceiveLocation(BDLocation location)
	{
		this.mLocation=location;
	
	}
     */
	public void Destory(){
		if(mLocClient!=null){
			this.mLocClient.stop();
		}
	}
}

