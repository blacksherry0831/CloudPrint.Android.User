package com.jit.cloud_print_cc;


import android.content.Intent;
import android.os.Bundle;


public class Activity_Print_Order_MultiGenerated extends Activity_CloudPrintTemplate {
/*----------------------------------------------------------------------------------------------*/
	@Override  
    protected void onCreate(Bundle savedInstanceState) 
	{  
        super.onCreate(savedInstanceState);        
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现         
        View_Print_Order_MultiGenerated mView;
        this.mViewTemplate=mView=new View_Print_Order_MultiGenerated(this);
        setContentView(mView.GetView());  
        /* setContentView(new View(this));*/       
    }  
/*----------------------------------------------------------------------------------------------*/
	 @Override  
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	  {
	          // TODO Auto-generated method stub
	            if(resultCode ==View_CloudPrintTemplate.ResultCode_OrderIsSuccess) {
	            	this.setResult(resultCode);
	               finish();
	           }
	  }
/*----------------------------------------------------------------------------------------------*/
}
