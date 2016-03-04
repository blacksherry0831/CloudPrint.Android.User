package com.jit.cloud_print_cc;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;

public class Acticity_FileTransfer extends Activity_CloudPrintTemplate {

	public View_FileTransfer mView;
	WifiManager.MulticastLock mLock;
	
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        WifiManager manager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        mLock= manager.createMulticastLock("test wifi");
        mLock.acquire();
        
        
        this.mViewTemplate=mView=new View_FileTransfer(this);
        
        
         setContentView(mView.GetView());  
        
       
    }  
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(mLock!=null) mLock.release();
	}
	
}
