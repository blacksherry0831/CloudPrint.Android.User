package com.jit.cloud_print_cc;

import android.os.Bundle;

public final class Activity_PrintAddressSelect extends Activity_CloudPrintTemplate
 {
	View_PrintAddressSelect mView;
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        
         this.mViewTemplate= mView=new View_PrintAddressSelect(this);
       
   
         setContentView(mView.GetView());  
        
       
    }
	
 }