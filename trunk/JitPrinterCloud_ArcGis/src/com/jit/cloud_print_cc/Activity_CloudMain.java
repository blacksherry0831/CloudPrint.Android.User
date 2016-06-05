package com.jit.cloud_print_cc;

import com.example.LeftRightHand.LeftRightGestureDetector;



import com.jit.config.KEY;
import com.jit.config.KEY.FileOperationWay;
import com.jit.config.KEY.OrderListStartMode;
import com.jit.config.KEY.WizardViewMode;

import com.jit.cloud_print_cc.R;


import com.zxc.view.CircleMenuLayout;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Rect;

import android.os.Bundle;

import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


import android.widget.LinearLayout;
import android.widget.Toast;



@SuppressWarnings("unused")
public class Activity_CloudMain  extends Activity_CloudPrintTemplate 
{

	private LeftRightGestureDetector mLeftRightDetector; 
	private View_CloudMain  mCloudPrintView;
    
	
    public Activity_CloudMain(){
    	
    	
    }
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
		
		/*---------------*/
		mLeftRightDetector=new LeftRightGestureDetector(this);
		mCloudPrintView=new View_CloudMain(this);
		mCloudPrintView.SetLeftRightHandDetector(mLeftRightDetector);
		mCloudPrintView.SetCircleMenuClickListener(new CircleItemClick());
		/*---------------*/
		setContentView((this.mViewTemplate=this.mCloudPrintView).GetView());
	}
	 
public class CircleItemClick implements CircleMenuLayout.OnMenuItemClickListener
	{
		@Override
		public void itemClick(View view, int pos)
		{
			int buttonIdx=mCloudPrintView.GetClickIdx(pos);
			if(buttonIdx==R.drawable.office_environment){
				/*办公环境*/
				 mCloudPrintView.UDP_Broad_Msg_Request();
				mCloudPrintView.StartActivityView(Activity_OfficeEnvironment.class);
				
			}else 	if(buttonIdx==R.drawable.document_manage){
				/*文件管理*/
				mCloudPrintView.SetWizardViewMode(WizardViewMode.ModeNormal);
				mCloudPrintView.StartActivityView(Activity_DocumentManagement.class);
				
			}else 	if(buttonIdx==R.drawable.close){
				/*退出*/
					finish();
			}else 	if(buttonIdx==R.drawable.search){
				/*设置默认打印机*/
				mCloudPrintView.SetWizardViewMode(WizardViewMode.ModeNormal);
				mCloudPrintView.SetFileOpMode(null, FileOperationWay.OPen2SetDefaultPrint);
				mCloudPrintView.StartActivityView(Activity_PrintSetPrintPoint.class);
			   
			}else if(buttonIdx==R.drawable.print_out){
				/*订单生成*/
				/*
				 * mCloudPrintView.SetOrderGeneratedMode(OrderGeneratedMode.SetDefaultOrderList);
				 *mCloudPrintView.StartActivityView(f_s_Print_Order_GeneratedActivity.class);
				 * */
				/*打印向导--2015年8月26日9:18:22*/
				mCloudPrintView.SetWizardViewMode(WizardViewMode.ModeWizard);
				mCloudPrintView.StartActivityView(Activity_DocumentManagement.class);
				
				
			}else if(buttonIdx==R.drawable.my_order){
				/*订单模板启动View*/
				mCloudPrintView.SetOrderListStartdMode(OrderListStartMode.SingleStart);
				mCloudPrintView.StartActivityView(Activity_OrderListPrint.class);
				 
			}else if(buttonIdx==R.drawable.another_button){
				Toast.makeText(Activity_CloudMain.this,"error by cui",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(Activity_CloudMain.this,"error by cui",Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		public void itemCenterClick(View view)
		{
			mCloudPrintView.StartActivityView(Activity_About.class);
		}
	}	
}
