package com.jit.cloud_print_cc;



import com.jit.config.KEY.OrderListStartMode;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class View_My extends View_CloudPrintTemplate 
{
	private final static int mLayoutId=R.layout.activity_my;
	
	TextView tv_username;
	TextView tv_order;
	TextView tv_myWallet;
	
	public View_My(Context context) {		
		super(context, mLayoutId);
		// TODO Auto-generated constructor stub
	   this.initChildrenView();
	}
	/**
	 * 
	 * 
	 */
   public void initChildrenView()
   {
	   tv_username=(TextView) this.findViewById(R.id.text_view_user_name);
	   tv_username.setText("用户名  : "+SaveParam.GetQinUserName(getContext()));
/*----------------------------------------------------------------------------*/ 	   
	   tv_order= (TextView) this.findViewById(R.id.textView_order);
	   tv_order.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SetOrderListStartdMode(OrderListStartMode.SingleStart);
		    StartActivityView(Activity_OrderListPrint.class);
		}
	});
/*----------------------------------------------------------------------------*/
	   tv_myWallet= (TextView) this.findViewById(R.id.textView_wallet);
	   
	   tv_myWallet.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			StartActivityView(Activity_Wallet.class);
		}
	});
/*-----------------------------------------------------------------------------*/
   } 
}
