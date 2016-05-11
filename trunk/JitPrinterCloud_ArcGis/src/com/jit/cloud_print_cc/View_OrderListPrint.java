package com.jit.cloud_print_cc;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jit.cloud_print_cc.KEY.OrderListStartMode;
import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AbsListView;
/*----------------------------------------------------------------------------------------*/
public  class View_OrderListPrint extends View_CloudPrintTemplate {
private final static int mLayoutId=R.layout.activity_print_order_list;
private final static int mLstItemLayoutId=R.layout.print_order_list_item;
private  View mListView; 
private  final ShowOrderAdapter madapter=new ShowOrderAdapter();;
/*this.mSharedPrintList.setAdapter(new  SharedPrinterAdapter());*/
public OrderListStartMode mMode;
	public  View_OrderListPrint (Context context)
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
		this.mListView= findViewById(R.id.id_print_order_list_view);
		if(mListView instanceof AbsListView){			
			((AbsListView) this.mListView).setAdapter(madapter);
		}else if(mListView instanceof PullToRefreshListView){
			((PullToRefreshListView) this.mListView).setAdapter(madapter);
			this.initListView( (PullToRefreshListView)mListView);
		}else{
			
		}		
		Button bt= (Button) this.findViewById(R.id.btn_back);
		bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		//this.initOrder_test();
		
	}
	/**
	 * 
	 * 
	 */
	private void initListView(final PullToRefreshListView listview)
	{
	// TODO Auto-generated method stub
		//listview..getRefreshableView().setGroupIndicator(null);  
		listview.getRefreshableView().setDivider(null);  
		listview.getRefreshableView().setSelector(android.R.color.transparent);  
		listview.setMode(Mode.BOTH);  
		listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
	 
		@Override
		public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			try{
				mMsgService.mUIPrintList.RequestPrintList();
			}catch(Exception e){
				
			}
			postDelayed(new Runnable(){
				@Override
				public void run(){					
					refreshView.onRefreshComplete();
				}
			},1500);
			
		}
	});  
	}
	/**
	 * 
	 * 
	 * 
	 */
	public void SetAdapterData(String result)
	{
		if(this.madapter!=null){
		/*	this.madapter.SetData(Serlected);*/
			this.madapter.SetData(result);
		}
	}
	
	@Override
	 public void onServiceConnected(ComponentName name, IBinder service)
	 {
		super.onServiceConnected(name, service);
		
		mMsgService.mUIPrintList.setIRcv(new UpdataData());
		mMsgService.mUIPrintList.RequestPrintList();
		
	 }
	/**
	 *
	 * 
	 */
	public void InitButton()
	{
		
	}
/*	 public View GetListItem(String text_order,Boolean button_status)
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
	 }*/
	/* public void initOrder_test()
	 {
		 if(this.mListView!=null){
			 ((ViewGroup) this.mListView).addView(this.GetListItem(null,true));
			 ((ViewGroup) this.mListView).addView(this.GetListItem(null,false));
		 }

	 }*/
	 public void startViewByMode()
	 {
		 this.mMode=this.GetOrderListStartdMode();
		 /*-----------------------------------------------------*/
		 if(this.mMode==null){
			 this.mMode=OrderListStartMode.SingleStart; 	
		 }
		
		 	
	 }
	 /**
	  *订单生成成功 
	  * 
	  */
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
	 /**
	  * 
	  * 
	  * 
	  */
	 public class ShowOrderAdapter extends  BaseAdapter
		{	
		 	private static final int _ChildViewId=R.layout.print_order_list_item;
		    public UserInfoOrdersManager mOrders=new UserInfoOrdersManager(null);
			/*public void SetData(final UserInfoOrdersManager mOrders) {				
				
				
				postDelay(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(Selected!=null){
							//mDataCloudPrintAddress.clear();
							//mDataCloudPrintAddress.addAll(Selected);
						}				
						
					   notifyDataSetChanged();
					}
					
				});
						
					
					
				
			}*/
		    public void SetData(final String json_text) 
		    {
		    	postDelay(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						  mOrders.Parse(json_text);
						  notifyDataSetChanged();
					}
		    		
		    	});
		    
		    	
		    }
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mOrders.GetCount();
			}
		
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return mOrders.GetItem(position);
			}
		
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
		
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				Object pi=this.getItem(position);
				 
				
				if(convertView==null){
					convertView=LibCui.getViewFromeXml(_ChildViewId, getContext());
				}
				
				TextView tv=(TextView) convertView.findViewById(R.id.id_po_list_item_text);
				ImageView thumbnail = (ImageView) convertView.findViewById(R.id.id_po_list_item_image);
				if(pi instanceof UserInfoOrder){
					UserInfoOrder uio=(UserInfoOrder) pi;
					tv.setText(uio.GetDes());
					FileOperations.SetFileItemBackground(uio.GetFile(),thumbnail);
				}
				
				
				
				//File file = mFilesList.get(position);
				return convertView;
			}
			
		}

     public class UpdataData implements UserInfoOperation_PrintOrderList.WhenRcvOrderList
     {

		@Override
		public void OnRecvData(String result) {
			// TODO Auto-generated method stub
			SetAdapterData(result);
		}
    	 
     }
    
}
