package com.jit.cloud_print_cc;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class Activity_FileSelect_NetDisk extends Activity_CloudPrintTemplate {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View_FileSelect_NetDisk mView;
        this.mViewTemplate=mView=new View_FileSelect_NetDisk(this);
		/*setContentView(R.layout.activity_file_select__net_disk);*/
        setContentView(mView.GetView());
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity__file_select__net_disk, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) 
	{
			
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//不响应按键抬起时的动作
		//TODO 代码　
			try{
				if(false== ((View_FileSelect_NetDisk) mViewTemplate).mFileSelect.ProcessBackKey(event)){
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
            super.onActivityResult(requestCode, resultCode, data);
	  }
}
