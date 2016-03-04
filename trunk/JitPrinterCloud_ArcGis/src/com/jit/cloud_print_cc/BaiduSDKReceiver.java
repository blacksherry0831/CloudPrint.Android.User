package com.jit.cloud_print_cc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.baidu.mapapi.SDKInitializer;

public class BaiduSDKReceiver extends BroadcastReceiver{
	private static final String LTAG = BaiduSDKReceiver.class.getSimpleName();
	public Boolean  mKeyError;
	public Boolean  mNetError;

	BaiduSDKReceiver(){
		this.mKeyError=null;
		this.mNetError=null;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		String s = intent.getAction();
		Log.d(LTAG, "action: " + s);
		
		if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
			//text.setText("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
			this.mKeyError=true;
			
		} else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
			//text.setText("网络出错");
			this.mNetError=true;
		}
	}
	/*
	public static void registerReceiver(BaiduSDKReceiver mReceiver){
		IntentFilter iFilter = new IntentFilter();  
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);  
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);  
		
		registerReceiver(mReceiver, iFilter);
	}*/
}
