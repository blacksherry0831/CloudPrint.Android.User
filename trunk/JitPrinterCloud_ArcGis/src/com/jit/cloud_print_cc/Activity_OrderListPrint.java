package com.jit.cloud_print_cc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_OrderListPrint extends Activity_CloudPrintTemplate   {
	View_OrderListPrint mView;
	private Button btn_flash;
	//UserInfoOrdersManager mOrders=new UserInfoOrdersManager(null);
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        
        this.mViewTemplate=mView=new View_OrderListPrint(this);
       
   
         setContentView(mView.GetView());  
        
         //第一种方式    
         Button Btn1 = (Button)findViewById(R.id.btn_flash);//获取按钮资源    
         Btn1.setOnClickListener(new Button.OnClickListener(){//创建监听    
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//UpdataList();
				try{
					mView.mMsgService.mUIPrintList.RequestPrintList();
				}catch(Exception e){
					
				}
			
				
			}    
   
         });    
         
         //this.UpdataList();
       
    }  
	/*public void UpdataList()
	{
		new Thread(new GetOrdersThread()).start();
	}*/
	/*public class GetOrdersThread implements Runnable
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			UserInfoBase user=new UserInfoBase(Activity_OrderListPrint.this);
			String username=View_Login.GetQinUserName(Activity_OrderListPrint.this);
			String param=Connect2QinServer.GetOrdersString(username, "Y","W");
			String str_url= user._mGetOrderList+"?"+param;
			String Result=Connect2QinServer.GetParam2Server(str_url);
		//	mOrders.Parse(Result);
			mView.SetAdapterData(Result);
			//ShowMsg((Result==null)?"null":Result);
			
			
		}
		
	}*/
	
	
}

