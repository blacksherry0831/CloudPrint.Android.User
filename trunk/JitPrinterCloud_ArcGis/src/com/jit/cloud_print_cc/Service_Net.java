package com.jit.cloud_print_cc;

import java.util.List;

import com.jit.cloud_print_cc.NetPhonePc.IRcvFileNotify;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class Service_Net extends Service {
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	public  NetPhonePc mNetPhonePC=null;
	public UserInfoOperation_PrintOrderList mUIPrintList;
	public UserInfoOperation_CloudDisk      mUICloudList;
	@SuppressWarnings("deprecation")
	public Service_Net() {
		
			
			
	}
	@Override
    public void onCreate() {
        super.onCreate();
        /*-------------------------------*/
        if(this.mNetPhonePC!=null){
        	this.mNetPhonePC.Destory();
        	this.mNetPhonePC=null;
        }        
        this.mNetPhonePC=new NetPhonePc(this.getApplicationContext());
        /*-------------------------------*/
        mMediaPlayer=MediaPlayer.create(this,R.raw.file_rcv);
        if(this.mNetPhonePC!=null)
        this.mNetPhonePC.SetIRcvFileNotify(new IRcvFileNotify(){        	
			@Override
			public void WhenRecvFile() {
				// TODO Auto-generated method stub
				Service_Net.this.OpenPhoneU();	
				if (mMediaPlayer.isPlaying()) {
					
				}else{
					mMediaPlayer.start();
				}
			}        	
        });
        /*---------------------------------*/
        mUIPrintList=new UserInfoOperation_PrintOrderList(this);
        /*---------------------------------*/
        mUICloudList=new UserInfoOperation_CloudDisk(this);
        /*---------------------------------*/
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
    	if(this.mNetPhonePC!=null){
    		this.mNetPhonePC.SetIRcvFileNotify(null);
        	this.mNetPhonePC.Destory();
        	this.mNetPhonePC=null;
        }      
    	if( mMediaPlayer!=null){
    		 mMediaPlayer.release();
    		 mMediaPlayer=null;
    	}

        super.onDestroy();
    }
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		return new MsgBinder();
	}
	public void NotifyAndroid()
	{
		//消息通知栏
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		//定义通知栏展现的内容信息
	        int icon = R.drawable.file_rcv_notify;
	        CharSequence tickerText = "云打印收到文件";
	        long when = System.currentTimeMillis();
	        Notification notification = new Notification(icon, tickerText, when);
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
	        //定义下拉通知栏时要展现的内容信息
	        Context context = getApplicationContext();
	        CharSequence contentTitle = "云打印收到文件";
	        CharSequence contentText = "";
	        Intent notificationIntent = new Intent(this,Activity_FileSelect.class);
	        
	        Bundle bundle = new Bundle();
	        bundle.putString("TITLE", this.getString(R.string.dm_file_my_phone));
	        bundle.putString("TYPE","FORDER");
	        notificationIntent.putExtras(bundle);
	        
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	                notificationIntent, 0);
	        notification.setLatestEventInfo(context, contentTitle, contentText,
	                contentIntent);
	         
	        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
	        mNotificationManager.notify(1, notification);
	} 
	public void OpenPhoneU()
	{
		    Intent notificationIntent = new Intent(this.getBaseContext(),Activity_File_FileSelect_U.class);
		    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        getApplication().startActivity(notificationIntent);
	}
	/**
	 * 
	 * 
	 * @author Administrator
	 *
	 */
	String getTopActivity(Context context)
	{
	  ActivityManager manager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE) ;

	  List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(3);
	
		if(runningTaskInfos != null&&runningTaskInfos.size()!=0){
			   ComponentName cn = runningTaskInfos.get(0).topActivity;
			   return cn.getShortClassName();
	
		}else{
	
		     return null ;
		}
	}
	/**
	 * 
	 * 
	 * @author Administrator
	 *
	 */
	
	/**
	 * 
	 * 
	 * @author Administrator
	 *
	 */
	public class MsgBinder extends Binder{
		/**
		 * 获取当前Service的实例
		 * @return
		 */
		public Service_Net getService()
		{
			return Service_Net.this;
		}
		public void startDownload(){
			 
		}
	}
}
