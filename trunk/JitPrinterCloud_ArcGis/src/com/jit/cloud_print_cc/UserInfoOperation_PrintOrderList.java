package com.jit.cloud_print_cc;

import android.content.Context;

public class UserInfoOperation_PrintOrderList extends UserInfoBase {

	private  WhenRcvOrderList IRcv;
	private boolean _Requesting=false;
	public interface WhenRcvOrderList{
		public void OnRecvData(String result);
	}
/**
 * 
 * 
 */
	UserInfoOperation_PrintOrderList(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public void RequestPrintList()
	{
		if(_Requesting==false)
		new Thread(new GetOrdersThread()).start();
	}
/**
 * 
 * 
 */
	public void setIRcv(WhenRcvOrderList iRcv) {
		IRcv = iRcv;
	}
/**
 * 
 * 
 */
public class GetOrdersThread implements Runnable
{

	@Override
	public void run()
	{
		_Requesting=true;
		// TODO Auto-generated method stub
			UserInfoBase user=new UserInfoBase(mContext);
			//String username=View_Login.GetQinUserName(mContext);
			String param=Connect2QinServer.GetOrdersString(user.GetUserName(), "Y","W");
			String str_url= user._mGetOrderListUrl+"?"+param;
			String Result=Connect2QinServer.GetParam2Server(str_url);
			if(IRcv!=null){
				IRcv.OnRecvData(Result);
			}
		//
		_Requesting=false;
	}
	
}
/**
 * 
 * 
 */
}
