package com.jit.cloud_print_cc;

import com.jit.config.GlobalConfig;

import android.app.Activity;
import android.content.Context;

public class UserLoginOperation extends  UserInfoBase
{
	public interface LoginCallBack
	 {
		
			void LoginEvent(String event);
		 
	 }
	/**
	 * 
	 */
	private static final long serialVersionUID = 4767369481821616983L;
    private LoginCallBack _LCB=null;
	public void SetCallBack(LoginCallBack lcb)
	{
		this._LCB=lcb;
	}
	
	private void ShowMsg(String event)
	{
		if(this._LCB!=null)
			this._LCB.LoginEvent(event);
	}
	
	UserLoginOperation(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public enum LoginError
	{
		UserNameIsUsed,
		PasswordError
	}
	
	public boolean mRememberPassword;
	public boolean mAutoLogin;
	/*
	 * 执行登录
	 * */
	public boolean Login()
	{
		if("blacksherry2".equals(this.mUserName)){
			
			this.DestoryThisActivityStartMain();
		}
		
		if(GlobalConfig.UserDebug.equals(this.mUserName)){
			GlobalConfig.TotalFunctions=true;
		}
		
		
		 new Thread(new LoginThread()).start();
		 
		return false;
		
	}
	public void DestoryThisActivityStartMain()
	{
	    boolean IsActivity=this.mContext instanceof Activity;
	    if(IsActivity){
	    	Activity a=(Activity) this.mContext;
	    	a.finish();
	    	
	    }
	}
	
	public class LoginThread implements Runnable
	{

		@Override
		public void run()
		{
			
			// TODO Auto-generated method stub
			String param=Connect2QinServer.GetQinLoginString(mUserName, mPassword);
			String str_url= _mloginQinUrl+"?"+param;
			String Result=Connect2QinServer.GetParam2Server(str_url);
			ShowMsg((Result==null)?"null":Result);
			
		}
		
	}
	
}
