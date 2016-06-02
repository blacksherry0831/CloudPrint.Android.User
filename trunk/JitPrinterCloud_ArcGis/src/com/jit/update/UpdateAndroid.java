package com.jit.update;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.cui.http.httpCui;
import com.jit.cloud_print_cc.Connect2QinServer;

import com.jit.cloud_print_cc.LibCui;
import com.jit.cloud_print_cc.R;
import com.jit.cloud_print_cc.SaveParam;
import com.jit.config.KEY;

import android.app.ActivityManager;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class UpdateAndroid 
{
	public static boolean ENABLE=false;
	public static final String URL_VER="http://blacksherry-software-distribution.oss-cn-shenzhen.aliyuncs.com/android/qindayin/ver.json"; 
	public static final String URL_APK_BASE="http://blacksherry-software-distribution.oss-cn-shenzhen.aliyuncs.com/android/qindayin/"; 
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context ctx) {
	    try {
	        PackageManager manager = ctx.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
	        String version = info.versionName+"_"+info.versionCode;
	        return  version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public static String getVersionCode(Context ctx) {
	    try {
	        PackageManager manager = ctx.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
	        String version = String.valueOf(info.versionCode);
	        return  version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	/**
	 * 
	 * 
	 * @return
	 */
	public JSONObject GetLatestVersion(){
		
		
		
		try {
			
			String Result=httpCui.getURLResponse_ReadOnly(URL_VER);
			JSONObject jo=new JSONObject(Result);			
			return jo;
			
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
		
		
		  String url_str=SaveParam.GetValue(ctx, KEY.K_Version_Latest_URL);
		
		  Uri uri = Uri.parse(url_str);  
		   /* String serviceString = Context.DOWNLOAD_SERVICE;  
		    DownloadManager downloadManager;  
		    downloadManager = (DownloadManager) ctx.getSystemService(serviceString);  
		      
		  
		    DownloadManager.Request request = new  DownloadManager.Request(uri);  
		    long reference = downloadManager.enqueue(request);  */
		  Intent intent= new Intent();        
          intent.setAction("android.intent.action.VIEW");         
          intent.setDataAndType(uri,"application/vnd.android.package-archive");         
          //不指定让用户选择
          //intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");   
          ctx.startActivity(intent);
	}
   
	private void GetValue(Context ctx, String kVersionLatestUrl) {
		// TODO Auto-generated method stub
		
	}
	public void SaveConfig(Context ctx)
	{
		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		int memClass=activityManager.getMemoryClass();
		
		 File path=LibCui.GetCloudPrintCfgFile();
		 
		 File file=new File(path,KEY.K_D_CONFFORDER);
		 //if(!file.exists())
		 {			
			 JSONObject jo=new JSONObject();	
			 String vercode=UpdateAndroid.getVersionCode(ctx);
			 try {
				 jo.put("mem", memClass);
				 jo.put("version", UpdateAndroid.getVersion(ctx));
				 jo.put("vercode",vercode);
				 jo.put("url", URL_APK_BASE+"CloudPrintNormalUser"+vercode+".apk");
				 LibCui.SaveString2Fille(file,jo.toString());
			} catch (Exception e) {
			}			 
			
		 }
	}
	
}
