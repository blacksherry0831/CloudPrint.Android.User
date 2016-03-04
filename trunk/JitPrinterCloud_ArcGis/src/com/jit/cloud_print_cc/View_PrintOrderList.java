package com.jit.cloud_print_cc;

import com.jit.cloud_print_cc.KEY.OrderListStartMode;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
/*----------------------------------------------------------------------------------------*/
public class View_PrintOrderList extends View_CloudPrintTemplate {
private final static int mLayoutId=R.layout.activity_print_order_list;
private final static int mLstItemLayoutId=R.layout.print_order_list_item;
public  LinearLayout mListView;
public OrderListStartMode mMode;
	public  View_PrintOrderList (Context context)
	  {
		  super(context,mLayoutId);
		  this.initChildView();
		  this.startViewByMode();
	  }
	/*
	public  View_PrintOrderList (Context context,PhoneInHand   PhoneInHand)
	  {
		 super(context,mLayoutId,PhoneInHand);
		 this.initChildView();
		 this.startViewByMode();
	  }*/
	public void initChildView()
	{
		this.mListView=(LinearLayout) findViewById(R.id.id_print_order_list_view);
		this.initOrder_test();
	}
	 public View GetListItem(String text_order,Boolean button_status)
	 {
		 View v=LibCui.getViewFromeXml(mLstItemLayoutId, getContext());
		 Button b=(Button) v.findViewById(R.id.id_po_list_item_button);
		 if(b!=null){
			 if(button_status!=null){
				 if(button_status){
					 b.setTextColor(Color.GREEN);
					 b.setText("已完成");
				 }else {				
					 b.setTextColor(Color.RED);
					 b.setText("未完成");					 
				 }
				 
			 }
				
		 }
		 TextView tv=(TextView) v.findViewById(R.id.id_po_list_item_text);
		 if(tv!=null){
			 if(text_order!=null)	 tv.setText(text_order);
		 }
		 return v;
	 }
	 public void initOrder_test()
	 {
		 if(this.mListView!=null){
			 this.mListView.addView(this.GetListItem(null,true));
			 this.mListView.addView(this.GetListItem(null,false));
		 }

	 }
	 public void startViewByMode()
	 {
		 this.mMode=this.GetOrderListStartdMode();
		 /*-----------------------------------------------------*/
		 if(this.mMode==null){
			 this.mMode=OrderListStartMode.SingleStart; 	
		 }
		
		 	
	 }
	 @Override
	 public void SetResultBeforeFinish()
	 {
		 /*-----------------------------------------------------*/
		 if(this.mMode==OrderListStartMode.CloseParents){
		 		this.SetResult(ResultCode_OrderIsSuccess);
		 }else if(this.mMode==OrderListStartMode.SingleStart){
		 		
		 }else{
		 		
		 }
		 /*-----------------------------------------------------*/
	 }
}
