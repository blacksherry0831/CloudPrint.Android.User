package com.jit.cloud_print_cc;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class Activity_FileSelect extends Activity_CloudPrintTemplate {
	
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        View_FileSelect mView;
        this.mViewTemplate=mView=new View_FileSelect(this);
       
   
         setContentView(mView.GetView());  
        
       
    }  
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
			
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//不响应按键抬起时的动作
		//TODO 代码　
			try{
				if(false== ((View_FileSelect) mViewTemplate).mFileSelect.ProcessBackKey(event)){
					this.finish();
					return super.dispatchKeyEvent(event);//向下传递，结束窗口
				}else{
					return true;
				}	
			}catch(Exception e){
			
			}
			return true;
			
		//注意这儿返回值为true时该事件将不会继续往下传递，false时反之。根据程序的需要调整
		}
		return super.dispatchKeyEvent(event);
}
	
	 @Override  
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	  {
		          // TODO Auto-generated method stub
		            if(resultCode ==View_CloudPrintTemplate.ResultCode_OrderIsSuccess) {
		            	this.setResult(resultCode);
		               finish();
		           }
	  }
	 
	 
}
