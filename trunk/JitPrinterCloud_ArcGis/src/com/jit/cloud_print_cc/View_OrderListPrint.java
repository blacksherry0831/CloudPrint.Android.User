package com.jit.cloud_print_cc;

import com.alipay.sdk.pay.demo.PayResult;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jit.config.KEY.OrderListStartMode;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AbsListView;
import android.widget.Toast;
/*----------------------------------------------------------------------------------------*/
/**
 * 
 * 
 * 
 */
public  class View_OrderListPrint extends View_CloudPrintTemplate {
private final static int mLayoutId=R.layout.activity_print_order_list;
//private final static int mLstItemLayoutId=R.layout.print_order_list_item;
private  View mListView; /**<订单列表*/
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
		listview.setOnRefreshListener(new RefreshOrder());  
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
	/**
	 * 
	 * 
	 * 
	 */
	public class RefreshOrder implements  OnRefreshListener<ListView>
	{

		@Override
		public void onRefresh(final PullToRefreshBase refreshView) {
			// TODO Auto-generated method stub
			RequestPrintList();
			postDelayed(new Runnable(){
							@Override
							public void run(){					
								refreshView.onRefreshComplete();
							}
						},1500);
		}
		
		
	}
	public void RequestPrintList()
	{
		   try{
				mMsgService.mUIPrintList.RequestPrintList();
		   }catch(Exception e){
			
		   }
	}
	/**
	 * 
	 * 
	 * 
	 */
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
			
		    
		    public void UpdateOrdersStatus()
		    {
		    	mOrders.Refash();
		    	postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						notifyDataSetChanged();
					}
				},1000);
		    }
		    
		    
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
					Button bt= (Button)convertView.findViewById(R.id.id_po_list_item_button);
				if(pi instanceof UserInfoOrder){
					final UserInfoOrder uio=(UserInfoOrder) pi;
					tv.setText(uio.GetDes());
					FileOperations.SetFileItemBackground(uio.GetFile(),thumbnail);
					
					
					 
					 if(UserInfoOrder.STATUS_CHARGING.equalsIgnoreCase(uio.getStatus())){
						 //计算费用中
						 //bt.setVisibility(View.VISIBLE);
						// bt.setText("刷新计费");
						 bt.setVisibility(View.GONE);
						 bt.setOnClickListener(new View.OnClickListener() {							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub--拉取价格//RequestPrintList();
								UpdateOrdersStatus(); //mOrders.Refash();
							}
						});
						
					 }else if(UserInfoOrder.STATUS_CHARGED.equalsIgnoreCase(uio.getStatus())){
						 //计算费用完成
						 bt.setVisibility(View.VISIBLE);
						 bt.setText("支付");
						 bt.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub--付费逻辑
								AliPay pt=new AliPay(View_OrderListPrint.this);
								pt.Payit(uio);
								/*pt.payit("亲打印费用Android",
										cloudPrintAddress.GetTotalBodyOrders(),
										cloudPrintAddress.GetTotalMoney(),
										cloudPrintAddress.GetTotalOrderId());*/
							}
						});
					 }else if(UserInfoOrder.STATUS_PRINTED_PENDING.equalsIgnoreCase(uio.getStatus())){
						 bt.setVisibility(View.GONE);	
					 }else if(UserInfoOrder.STATUS_SUCCESS.equalsIgnoreCase(uio.getStatus())){
						 bt.setVisibility(View.GONE);
					 }else{
						 //
						 bt.setVisibility(View.GONE);						
					 }
					
					thumbnail.setOnLongClickListener(new View.OnLongClickListener() {
						
						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							uio.Delete4Disk();
							RequestPrintList();
							return false;
						}
						
					});
					
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
     /**
 	 * 
 	 * 
 	 * 
 	 */	
 	Handler mHandler_MSG = new Handler() {
 		@Override
 		public void handleMessage(Message msg) {
 			switch (msg.what) {
 			case AliPay.SDK_PAY_FLAG: {
 				PayResult payResult = new PayResult((String) msg.obj);
 				/**
 				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
 				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
 				 * docType=1) 建议商户依赖异步通知
 				 */
 				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

 				String resultStatus = payResult.getResultStatus();
 				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
 				if (TextUtils.equals(resultStatus, "9000")) {
 					Toast.makeText(getContext(), "支付成功", Toast.LENGTH_SHORT).show();
 					
 					postDelayed(new Runnable(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
								if(madapter!=null) madapter.notifyDataSetChanged();
						}},	1000);
 					
 					
 					
 					
 				} else {
 					// 判断resultStatus 为非"9000"则代表可能支付失败
 					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
 					if (TextUtils.equals(resultStatus, "8000")) {
 						Toast.makeText(getContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();

 					} else {
 						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
 						Toast.makeText(getContext(), "支付失败", Toast.LENGTH_SHORT).show();

 					}
 				}
 				break;
 			}
 			case AliPay.SDK_CHECK_FLAG: {
 				Toast.makeText(getContext(), "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
 				break;
 			}
 			default:
 				break;
 			}
 		};
 	};

 	/**
	 * 
	 * 
	 * 
	 */
	@Override
	protected Handler Handler(){
			
		return mHandler_MSG;
	}

	/**
	 * 
	 * 
	 * 
	 */	
}
