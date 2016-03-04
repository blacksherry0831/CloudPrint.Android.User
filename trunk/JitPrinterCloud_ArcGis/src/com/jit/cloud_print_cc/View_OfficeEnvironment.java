package com.jit.cloud_print_cc;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.LeftRightHand.LeftRightGestureDetector.PhoneInHand;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jit.cloud_print_cc.R;
import com.jit.cloud_print_cc.View_FileTransfer.DevicesMode;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

 @SuppressWarnings("unused")
/*class PcInfo
{
	 public String GetDefatutPcInfo()
		{
			 String info="机器名：PC\r\n" +
			 		"主机地址：楼上\r\n" +
			 		"打印机类型：共享打印机\r\n" +
			 		"搜索时间：2015\5\14 11:01";
			 return info;
		}
}*/
public class View_OfficeEnvironment extends View_CloudPrintTemplate 
{
public final static int   mPcPhoneViewIdx=R.layout.grid_view_phone_pc_list__item;
private  final int mWifiNameIdx=R.id.id_item_text_wifi_name;
public final static int  mLayoutId = R.layout.activity_office_envriment;
/*-----------------------------------------------------------------------*/
private LinearLayout mTop;
@SuppressWarnings("unused")
private LinearLayout mWifi;
private LinearLayout mList;
public  PullToRefreshListView  mListViewPcPhonePrinter;
private LinearLayout mBottom;
private TextView   mWifiNameTextView;
public  ImageView  mTextViewWifiRefresh;
LocalWifiDevAdapter mAdapter=new LocalWifiDevAdapter();
/*-----------------------------------------------------------------------*/
public  ArrayList<Object> mData=new ArrayList<Object>();
public  ArrayList<CloudPrintAddress> mDataPrinter=new ArrayList<CloudPrintAddress>();
public  ArrayList<ServerInfo> mDataPc=new ArrayList<ServerInfo>();
public  ArrayList<ServerInfo> mDataPhone=new ArrayList<ServerInfo>();
/*-----------------------------------------------------------------------*/
public  View_OfficeEnvironment(Context context)
  {
	  super(context,mLayoutId);	 
	  this.InitChildView();
	  new Thread(new RefeshData()).start();
  }
/*
public  f_s_OfficeEnvironmentView(Context context, PhoneInHand   PhoneInHand)
{
	  super(context,mLayoutId,PhoneInHand);	
	  this.InitChildView();
}*/
public void InitChildView()
    {		
			if(this.GetView()!=null){
				mTop= (LinearLayout)    findViewById(R.id.id_office_envriment_top);
				mWifi=(LinearLayout)    findViewById(R.id.id_office_envirment_wifi);
				mList=(LinearLayout)    findViewById(R.id.id_office_envirment_list);
				mBottom=(LinearLayout)  findViewById(R.id.id_office_envirment_bottom);
				
				mWifiNameTextView=(TextView)findViewById(mWifiNameIdx);
				mListViewPcPhonePrinter= (PullToRefreshListView) this.findViewById(R.id.listview_office_envirment);
				
				mTextViewWifiRefresh=(ImageView) this.findViewById(R.id.textView_wifi_refresh);
			
			}
	          this.SetBackground();
	          /*--------------------------------------------------------------------*/
	          this.mWifiNameTextView.setText(LibCui.getWifiName(getContext()));
	          if(this.mListViewPcPhonePrinter!=null){
	        	  this.mListViewPcPhonePrinter.setAdapter(mAdapter);
	        	  this.initListView( this.mListViewPcPhonePrinter);
	          }
	          /*-------------------------------------------------------------------*/
	          if(this.mTextViewWifiRefresh!=null){
	        	  mTextViewWifiRefresh.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast_make_show_center("正在扫描网络......");
						UDP_Broad_Msg_Request();
					}
				});
	          }
	  
	}

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
		UDP_Broad_Msg_Request();
		postDelayed(new Runnable(){
			@Override
			public void run(){					
				refreshView.onRefreshComplete();
			}
		},1500);
		
	}
});  
}
public static View GetViewListItem(
		Context context,
		int ImgViewIdx,
		int ImgTextIdx,
		int ImgIdx, 
		int StrIdx,
		int layoutId)
{
	
	  LayoutInflater mInflater = LayoutInflater.from(context);	  
	  View view = mInflater.inflate(layoutId,null, false);
		ImageView iv = (ImageView) view.findViewById(R.id.id_oe_list_item_image);
		TextView tv = (TextView) view.findViewById(R.id.id_oe_list_item_text);
		if (iv != null){
			iv.setVisibility(View.VISIBLE);
			if(ImgIdx!=-1)
			iv.setImageResource(ImgIdx);
		}
		if(tv!=null){
			tv.setVisibility(View.VISIBLE);		
			if(StrIdx!=-1){
				tv.setText(context.getResources().getText(StrIdx));
			}else{
				tv.setText("");
			}
			
		}
		return view;
}
/**
* 
* 
* 
*/
public static View GetViewListItem(
		Context context,
		int ImgViewIdx,
		int ImgTextIdx,
		int ImgIdx, 
		String str,
		int layoutId)
{	 
		return GetViewListItem(
				context,
				null,
				false,
				ImgViewIdx,
				ImgTextIdx,
				ImgIdx, 
				str,
				layoutId);
}
/**
* 
* 
* 
*/
public static View GetViewListItem(
		Context context,
		ViewGroup parent,
		boolean  b,
		int ImgViewIdx,
		int ImgTextIdx,
		int ImgIdx, 
		String str,
		int layoutId)
{
	
	  LayoutInflater mInflater = LayoutInflater.from(context);	  
	  View view = mInflater.inflate(layoutId,parent,b);
		ImageView iv = (ImageView) view.findViewById(ImgViewIdx);
		TextView tv = (TextView) view.findViewById(ImgTextIdx);
		if (iv != null){
			iv.setVisibility(View.VISIBLE);
			if(ImgIdx!=-1)
			iv.setImageResource(ImgIdx);
		}
		if(tv!=null){
			tv.setVisibility(View.VISIBLE);		
			if(str!=null){
				tv.setText(str);
			}else{
				tv.setText("");
			}
			
		}
		return view;
}
/**
* 
* 
* 
*/
public View GetViewListItem(int ImgIdx,String str)
{
	View view=null;
	view=GetViewListItem(
			getContext(),
			R.id.id_oe_list_item_image,
			R.id.id_oe_list_item_text,
			ImgIdx,
			str,
			R.layout.office_envriment_wifi_list_item);
	return view;
}
public void SetListPrinterInfo()
{
	View view=null;
	for(int i=0;i<3;i++){
		CloudPrintAddress  pi=new CloudPrintAddress();
		view=GetViewListItem(R.drawable.oe_net_print,pi.GetDefatutPrinterInfo());
		View viewParentlayout=view.findViewById(R.id.id_oe_list_item_button_layout);
		viewParentlayout.setVisibility(View.VISIBLE);
		Button button=(Button) viewParentlayout.findViewById(R.id.id_oe_list_item_button);
		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(new PrintItemClick(null));
		/*-----------------------------*/
		this.mList.addView(view);
	}
}
public void SetListPcInfo()
{
	/*View view=null;
	for(int i=0;i<1;i++){
		PcInfo pi=new PcInfo();
		view=GetViewListItem(R.drawable.oe_pc_sheard,pi.GetDefatutPcInfo());
		View viewParentlayout=view.findViewById(R.id.id_oe_list_item_button_layout);
		viewParentlayout.setVisibility(View.VISIBLE);
		Button button=(Button) viewParentlayout.findViewById(R.id.id_oe_list_item_button);
		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(new PrintItemClick(null));
		this.mList.addView(view);
	}*/
}
public void SetList()
{
	this.mList.removeAllViews();
	this.SetListPrinterInfo();
	this.SetListPcInfo();
}

public void SetBackground()
{
	//LibCui.SetViewBackGround(mTop, getContext(),R.drawable.oe_top_background);
	//LibCui.SetViewBackGround(mBottom, getContext(),R.drawable.oe_top_background);
}
/**
 * 
 * 
 */
public void UpdataData(Object[] o)
{
	this.mData.clear();
	this.mData.addAll(this.mDataPrinter);
	this.mData.addAll(this.mDataPhone);
	this.mData.addAll(this.mDataPc);
	this.mAdapter.SetData(mData);
}
/**
 * 
 * 
 */
@Override
protected void  PrintersNowIsUpdata(ArrayList<CloudPrintAddress> Printers)
{
	this.mDataPrinter=mNetPhonePc.mPCPhonePrinterList.GetPrinters();
	this.UpdataData(this.mDataPrinter.toArray());
	
}
/**
 * 
 * 
 */
@Override
protected void  PhoneNowIsUpdata()
{
	this.mDataPhone=mNetPhonePc.mPCPhonePrinterList.GetPhoneServerInfo();
	this.UpdataData(this.mDataPhone.toArray());
}
/**
 * 
 * 
 */
@Override
 protected void  PcNowIsUpdata()
{	
	this.mDataPc=mNetPhonePc.mPCPhonePrinterList.GetPCServerInfo();	
	this.UpdataData(this.mDataPc.toArray());
}
public void StartActivityView_with_Printer(Class<?> cls,Object o)
{
	Bundle bundle=new Bundle();
    Intent intent=new Intent();
    if(o!=null&&o instanceof CloudPrintAddress){
    	mPrintPointTemp=(CloudPrintAddress) o;
    }
    if(mPrintPointTemp!=null){
    	  intent.putExtra(GetPrinter2UseKey(),mPrintPointTemp.Clear2Serializable());
    }	  
    this.SetWizardViewModeByParent();
	this.StartActivityView(cls, bundle,intent);
	
}
@Override
protected void ActivityResult(int requestCode, int resultCode, Intent data) 
{
	   if(resultCode ==View_RegisterPrinter.REFASH_PRINTER_DATA) {
		   if(mAdapter!=null){
			   mAdapter.notifyDataSetChanged();
		   }
		   
       }
}
 public  class PrintItemClick implements View.OnClickListener
 {
	 Object o=null;
	 PrintItemClick(Object o_t)
	 {
		 this.o=o_t;
	 }
	@Override
	public void onClick(View v)
	{
		/* RegisterPrinterView rp=new RegisterPrinterView(v.getContext(),mPhoneHand);
		 rp.showPopupWindow(mView);*/
		StartActivityView_with_Printer(Activity_RegisterPrinter.class,o);
	}
	 
 }
 public class RefeshData implements Runnable
 {
 	@Override
 	public void run(){
 		while(View_OfficeEnvironment.this.mThreadRun){
 			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 			View_OfficeEnvironment.this.postDelay(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mWifiNameTextView.setText(LibCui.getWifiName(getContext()));
				}
 				
 			});
 			
 		}
 	}
 }
 public class LocalWifiDevAdapter extends  BaseAdapter
	{		
	    public Handler mHandler=new Handler();
	    public  ArrayList<Object> mDataPrinterPcPhone=new ArrayList<Object>();
		public void SetData(final ArrayList<Object> Selected) {				
			
			
			mHandler.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(Selected!=null){
						mDataPrinterPcPhone.clear();
						mDataPrinterPcPhone.addAll(Selected);
					}				
					
				   notifyDataSetChanged();
				}
				
			});			
			
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDataPrinterPcPhone.size();
		}
	
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mDataPrinterPcPhone.get(position);
		}
	
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
           Object pi_t=mDataPrinterPcPhone.get(position);
           if(convertView==null){
				convertView=View_OfficeEnvironment.GetViewListItem(
						parent.getContext(), 
						R.id.id_oe_list_item_image,
						R.id.id_oe_list_item_text,
						R.drawable.oe_net_print, 
						"suck", 
						R.layout.office_envriment_wifi_list_item);
			}
           ImageView iv = (ImageView) convertView.findViewById(R.id.id_oe_list_item_image);
           TextView tv = (TextView) convertView.findViewById(R.id.id_oe_list_item_text);
           Button   button=(Button) convertView.findViewById(R.id.id_oe_list_item_button);
           if(pi_t instanceof ServerInfo&&button!=null){
        	        ServerInfo si=(ServerInfo) pi_t;
        	        SetServerImg(si,iv);
        	        tv.setText(si.GetDes());
        	        
        	        button.setVisibility(View.GONE);
        	       
           }
            if(pi_t instanceof CloudPrintAddress&&button!=null){
            	    CloudPrintAddress pi=(CloudPrintAddress) pi_t;
            	    
            	   
            	    SetPrinterImg(pi,iv);
            	    tv.setText(pi.GetPrinterDes());
            	    if(pi.IsRegisterd()){
            	    	button.setVisibility(View.GONE);
            	    }else{
            	    	button.setVisibility(View.VISIBLE);
	            		button.setOnClickListener(new PrintItemClick(pi));
            	    	  
            	    }
           }
			
           // convertView.setBackgroundColor(0x99ccff|Color.BLACK);
			
			return convertView;
		}
		public void SetServerImg(ServerInfo si,ImageView v)
		{
			Resources res=v.getContext().getResources();
			if(si.mHostType==1){
				v.setImageDrawable(res.getDrawable(R.drawable.computer_mid));
			}else if(si.mHostType==2){
				v.setImageDrawable(res.getDrawable(R.drawable.phone_mid));
			}else{
				v.setBackgroundColor(Color.RED);
			}
	
		}
		
	
	}

 public  static void SetPrinterImg(CloudPrintAddress pi,ImageView v)
	{
		Resources res=v.getContext().getResources();
		if(pi.GetLocalParent().mHostType==1){
			v.setImageDrawable(res.getDrawable(R.drawable.oe_pc_sheard));
		}else if(pi.GetLocalParent().mHostType==2){
			v.setImageDrawable(res.getDrawable(R.drawable.oe_net_print));
		}else{
			v.setBackgroundColor(Color.RED);
		}

	}

}
