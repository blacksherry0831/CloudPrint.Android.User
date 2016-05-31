package com.jit.update;

import org.json.JSONException;
import org.json.JSONObject;

import com.cui.http.httpCui;
import com.jit.cloud_print_cc.Connect2QinServer;
import com.jit.cloud_print_cc.R;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class UpdateAndroid 
{
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context ctx) {
	    try {
	        PackageManager manager = ctx.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
	        String version = info.versionName;
	        return  version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/**
	 * 
	 * http://blacksherry-software-distribution.oss-cn-shenzhen.aliyuncs.com/android/qindayin/ver.txt
	 * @return
	 */
	public String GetLatestVersion(){
		
		
		
		try {
			
			String Result=httpCui.getURLResponse_ReadOnly("http://blacksherry-software-distribution.oss-cn-shenzhen.aliyuncs.com/android/qindayin/ver.json");
			JSONObject jo=new JSONObject(Result);			
			return jo.getString("version");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return null;
		
	}
	
	//public String Get
	/**
	 *
	 *
	 *
	 *Android程序中我们可以通过发送隐式Intent来启动系统默认的浏览器。
	 *如果手机本身安装了多个浏览器而又没有设置默认浏览器的话，
	 *系统将让用户选择使用哪个浏览器来打开连接
	 */	
	public void DownloadApk(Context ctx){
		
		  Uri uri = Uri.parse("http://blacksherry-software-distribution.oss-cn-shenzhen.aliyuncs.com/android/qindayin/CloudPrintNormalUser.apk");  
		   /* String serviceString = Context.DOWNLOAD_SERVICE;  
		    DownloadManager downloadManager;  
		    downloadManager = (DownloadManager) ctx.getSystemService(serviceString);  
		      
		  
		    DownloadManager.Request request = new  DownloadManager.Request(uri);  
		    long reference = downloadManager.enqueue(request);  */
		  Intent intent= new Intent();        
          intent.setAction("android.intent.action.VIEW");         
          intent.setData(uri);         
          //不指定让用户选择
          intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");   
          ctx.startActivity(intent);
	}
   
	
}
