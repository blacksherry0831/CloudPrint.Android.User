package com.jit.cloud_print_cc;

import android.app.Application;
import com.baidu.mapapi.SDKInitializer;
import com.jit.mail.SendMail;

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
		  CrashHandler crashHandler = CrashHandler.getInstance();  
	       // 注册crashHandler  
	       crashHandler.init(getApplicationContext());  
	       // 发送以前没发送的报告(可选)  
	       crashHandler.sendPreviousReportsToServer();  
	       
	       this.SendDebug2Mail();
	      
	       
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
	
}