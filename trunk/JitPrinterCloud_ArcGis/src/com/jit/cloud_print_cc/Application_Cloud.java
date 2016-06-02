package com.jit.cloud_print_cc;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import com.baidu.mapapi.SDKInitializer;
import com.jit.mail.SendMail;
import com.jit.update.UpdateAndroid;
import com.ta.utdid2.android.utils.StringUtils;

public class Application_Cloud extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		/*
		WifiManager manager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        WifiManager.MulticastLock lock= manager.createMulticastLock("test wifi");
        lock.acquire();
        */
		 	this.InitCrashHandler();
	       
		 	 if(LibCui.isWifiEnabled(this)){
		 		  this.SendDebug2Mail();
		 		  this.GetLatestVersion();
		 		  
		 	 }
		 	 
	      if(UpdateAndroid.ENABLE){
	    	  // 360发布成功，这个逻辑去掉
	    	   	new UpdateAndroid().SaveConfig(Application_Cloud.this); 
	      }
		
	       
	}
	public  void InitCrashHandler(){
		
		   CrashHandler crashHandler = CrashHandler.getInstance();  
	       // 注册crashHandler  
	       crashHandler.init(getApplicationContext());  
	       // 发送以前没发送的报告(可选)  
	       crashHandler.sendPreviousReportsToServer();  
	}
	
	public void SendDebug2Mail(){
		
		 if(LibCui.isWifiEnabled(this)){
	    	   new Thread(new Runnable() {				
					@Override
					public void run() {
						// TODO Auto-generated method stub
						new SendMail().SendDebugInfo();
					}
	    	   }).start();
	      }
	}
	
	public void GetLatestVersion()
	{
		 new Thread(new Runnable() {				
				@Override
				public void run() {
							// TODO Auto-generated method stub
							JSONObject ver_jo=new UpdateAndroid().GetLatestVersion();
							String LastestVersion,url_lastest;
							try {
								LastestVersion = ver_jo.getString("vercode");
								url_lastest = ver_jo.getString("url");
								if(!StringUtils.isEmpty(LastestVersion)){
									SaveParam.SetKeyValue(Application_Cloud.this,KEY.K_Version_Latest,LastestVersion);
									SaveParam.SetKeyValue(Application_Cloud.this,KEY.K_Version_Latest_URL,url_lastest);
								}
							} catch (Exception e) {
							
							}
					
				}
 	   }).start();
	}
	
}