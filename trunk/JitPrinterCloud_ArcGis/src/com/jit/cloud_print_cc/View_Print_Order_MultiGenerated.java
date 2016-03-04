package com.jit.cloud_print_cc;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayDemoActivity;
import com.alipay.sdk.pay.demo.PayResult;
import com.jit.cloud_print_cc.CloudPrintAddress.Toast_msg;

import com.jit.cloud_print_cc.FileSendOnNet.NowSendPercent;

import com.jit.cloud_print_cc.FilesWithParams.IsDouble;
import com.jit.cloud_print_cc.FilesWithParams.PrintColor;
import com.jit.cloud_print_cc.KEY.OrderGeneratedMode;
import com.jit.cloud_print_cc.KEY.OrderListStartMode;
import com.jit.cloud_print_cc.KEY.WizardViewMode;
import com.jit.cloud_print_cc.ServerInfoSendStatus.SendFilePackage;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferDone;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferStart;
//import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

public class View_Print_Order_MultiGenerated extends View_CloudPrintTemplate
{
public static boolean isDebug = true;
private final static int mLayoutId=R.layout.activity_multi_order_form_main;
private static final int mLayoutFilesPrintParam=R.layout.printer_mulit_order_form_list_view;
public interface PrintDocSuccess
{
	void printDocSuccess();
}
	
private final static int mButtonAddressSelect=R.id.buttton_order_form_title_addr_pick;
//商户PID
public static final String PARTNER = "2088121908846331";
// 商户收款账号
public static final String SELLER = "15651687339@163.com";
// 商户私钥，pkcs8格式
public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOV+TvGvfKsHRDQKXZnUkbS4H2j/LAVIUw6iqmsF0e8lBajjsywDqgCPMUkdCDPWazumAzbI4sinXo2Iucx/Z62Rf+ch81iCM3Q7zKqaa2gVPma/R9QdFlz78F+n7EGtnPQXTaCfZRcpn5KoA5VY9R9TxD9+UVadU7cJwOky0Bo1AgMBAAECgYEAoWUlC9/Io1cm5hpsHWVbW2cp3+OlB8oHb4GCrGWZcL/urQoF1ex1wJLzrGGYhSxEmdx04jCBHXBnlM4VLPZk6F0IyC7nCV1xeyEU7DL3w2/2FjErnGszRAx1hjuCloBTCjL/oW7u46J4ZNO6S2uMEKIWYsMBlT4frx6m29Mg57kCQQD7Ji+L9oNBc54VdHPfX5hO1fIOaMYs3L/1r84Uac1NxO3uZzZHINs8FfAHItBZEgapoOcICeejj+R8iBObHJnbAkEA6e0LiT06ihWU1q8N84MJ/0xfRwdZFZAqWdBx2BFj4k6VlX/pvHnxBoVcwQgN/A61RjLTjMABhfOa7bOBTMIBLwJBANxkYx8Y4ZADTLuZKMHhmr+74aGhch8WTOHmOBsTyZUwdndaXXhHrfvpaGxqsZkoR25+A5+7SWnwMNrTcxkQHTcCQQCAB6IhueY5PziYC3VqStUE6qrW+DmUqLPVNlWouVPev7309e5anq8BL6qlZ6AnzXD/e7/3L/tlcf/gizeAaEo3AkAjfCqdTYjSpa2zpW+h96Td2OQib/0Y0pybXu61ntZpEcrtTI2fEx2mFVtSLrdZCJcYg2VUkFiZ44YG42vURlPM";
// 支付宝公钥
public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
private static final int SDK_PAY_FLAG = 1;
private static final int SDK_CHECK_FLAG = 2;

public TextView mTextViewTitle;//订单号
//public TextView mTextViewOrderId;
public Button   mButtonAddrSelected;//配送地点选择
public Button   mButtonSubmit;

/*--------------------------------------*/
private ProgressBar mProgressBar_total;
private ProgressBar mProgressBar_part;
private TextView  mTextView_SendSpeed;
/*--------------------------------------*/
final   public  FileOperaData mFileOperaData;
/*-打印设置-*/
private  UserOrderInfoGenerated mOrderTemp;
private  OrderGeneratedMode  mViewMode;
private  NotifyThisView  NOTIFY_THIS_VIEW=new NotifyThisView();
public   ListView   mListView;

final FilesAndPrinterOrder mFileShow=new FilesAndPrinterOrder();
//public ArrayList<FilesWithParams> mFiles=new ArrayList<FilesWithParams >();//文件名
public View_Print_Order_MultiGenerated (Context context)
  {
	  super(context,mLayoutId);
	  mFileOperaData=this.GetFileOpMode();
	  mOrderTemp=new UserOrderInfoGenerated(context,mFileOperaData.GetFileArray());
	  mOrderTemp.SetNowSendPercent(NOTIFY_THIS_VIEW);
	  this.mFileShow.SetData(mOrderTemp.GetOrderData());
	  this.initChildView();
	
	  
  }
	
public void initChildView()
{
	this.getPrinter();
	this.initControls();
	this.StartViewBySetting();
    this.InitWizardViewMode();
}
/**
 * 
 * 
 **/
@Override
	protected void InitNormalWizardViewMode()
	{
		super.InitNormalWizardViewMode();
		this.StartViewBySetting();
	}
/**
 * 
 * 
 **/
@Override
	protected void InitWizardWizardViewMode()
	{
		super.InitWizardWizardViewMode();
		this.StartViewBySetting();
	}
/**
 * 
 * 
 */
/* public String GetCloudFilenameById(String id)
 {
	 String filename=id;
    try {
		filename= mMsgService.mUICloudList.GetFileName(id);
	} catch (Exception e) {
		// TODO: handle exception
	}	 
	return filename;
 
 }*/
/**
 * 本地打印
 * 
 **/
	public void SetLocalOrder()
	{
		/*-------------参数检查-----------------------------------*/
		if(mTextViewTitle!=null&& mButtonSubmit!=null&&mViewMode!=null&&mOrderTemp!=null){
			
		}else{
			return;
		}
		/*------------------------------------------------*/
		if(this.GetWizardViewMode()==WizardViewMode.ModeWizard){
			mTextViewTitle.setText("第三步：打印设置");
			this.mButtomNext.setText("提交");
			this.mButtomNext.setOnClickListener(new StartNextView());
			this.findViewById(R.id.relativeLayout_mode_normal).setVisibility(View.GONE);
		}else{
			/*非向导模式*/
			mTextViewTitle.setText("打印设置");
			this.mButtonSubmit.setVisibility(View.VISIBLE);
			mButtonSubmit.setOnClickListener(new StartNextView());
		}
	
       if(mButtonAddrSelected!=null)
        mButtonAddrSelected.setVisibility(View.GONE);
        
        LinearLayout printingSetting=(LinearLayout) this.findViewById(R.id.linear_layout_print_setting_address);
        if(printingSetting!=null){
        	printingSetting.setVisibility(View.GONE);
        }
        
	}
	/*
	public void GetPrinterParam()
	{
		
		int copies=1;
		boolean IsColor=false;
		boolean Isduplex=false;
		String  page="全部";

		String copies_str=mEditTextPrintCopys.getText().toString();		
		copies=Integer.parseInt(copies_str);
		
		int Checked=mColorRadio.getCheckedRadioButtonId();
		if(Checked==R.id.radioButton_color){
			IsColor=true;
		}if(Checked==R.id.radioButton_gray){
			IsColor=false;
		}else{
			
		}
		
		int pagetypreDuplex=mColorPagetype.getCheckedRadioButtonId();
		if(pagetypreDuplex==R.id.radioButton_page_one){
			Isduplex=false;
		}else if(pagetypreDuplex==R.id.radioButton_page_two){
			Isduplex=true;
		}else{
			
		}
		
		mOrderTemp.SetPrinterColor(IsColor);
		mOrderTemp.SetPrinterCopies(copies);
		mOrderTemp.SetPrinterRange(page);
		mOrderTemp.SetPrinterDuplexPaper(Isduplex);
		
	} 
	*/
	public void SetDefaultOrder()
	{
		if(this.GetWizardViewMode()==WizardViewMode.ModeWizard){
			mTextViewTitle.setText("订单默认设置");
		}else{			
			mTextViewTitle.setText("订单默认设置");
		}
		
		
		mButtonSubmit.setText("保存设置");
		mButtonSubmit.setOnClickListener(new StartNextView());
	}
	public void SetNetOrder()
	{
		/*-------------参数检查-----------------------------------*/
		if(mTextViewTitle!=null&& mButtonSubmit!=null){
			
		}else{
			return;
		}
		/*------------------------------------------------*/
		if(this.GetWizardViewMode()==WizardViewMode.ModeWizard){
			mTextViewTitle.setText("第三步：订单生成");
			this.mButtomNext.setText("提交");
			this.mButtomNext.setOnClickListener(new StartNextView());
			this.findViewById(R.id.relativeLayout_mode_normal).setVisibility(View.GONE);
		}else{			
			mTextViewTitle.setText("订单生成");
			this.mButtonSubmit.setVisibility(View.VISIBLE);
			this.mButtonSubmit.setOnClickListener(new StartNextView());
		}
        
	}
	/*
	public void StartNormalView(){
		
		
	}
	public void StartWizzardView(){
		this.StartViewBySetting();
	}*/
	public void StartViewBySetting()
	{
		this.mViewMode=this.GetOrderGeneratedMode();
		if(this.mViewMode==null){
			mTextViewTitle.setText("OrderGeneratedMode Error");
		}
		if(this.mViewMode==OrderGeneratedMode.SetNetOrderList){
			this.SetNetOrder();
		}else if(this.mViewMode==OrderGeneratedMode.SetDefaultOrderList){
			this.SetDefaultOrder();
		}else if(this.mViewMode==OrderGeneratedMode.SetLocalOrderList){
			this.SetLocalOrder();
		}else{
			mTextViewTitle.setText("OrderGeneratedMode Error");
		}
	}
	@Override
	public CloudPrintAddress getPrinter()
	{
		  this.mPrintPointTemp=(CloudPrintAddress) (getIntent().getSerializableExtra(GetPrinter2UseKey()));
		  if(this.mPrintPointTemp!=null){
			  this.mPrintPointTemp=this.mPrintPointTemp.Clone();
			  this.mPrintPointTemp.SetToastMsg(this.NOTIFY_THIS_VIEW);
		  }
		  
		  return   this.mPrintPointTemp;
	}
	public void initControls()
	{
		 mButtonAddrSelected=(Button) findViewById(mButtonAddressSelect);
		if( mButtonAddrSelected!=null){
			 mButtonAddrSelected.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					StartActivityView(Activity_PrintAddressSelect.class);
				}
			});
		}
		this.mTextViewTitle=(TextView) this.findViewById(R.id.textview_order_form_title);
	//	mTextViewOrderId=(TextView) this.findViewById(R.id.textview_order_form_order_id);
		/*mEditTextPrintCopys=(EditText) this.findViewById(R.id.editText_order_form_copies);
		if(mEditTextPrintCopys!=null){
			mEditTextPrintCopys.setText("1");
		}*/
		//mButtonAddrSelected=(Button) this.findViewById(R.id.buttton_order_form_title_addr_pick);
		mButtonSubmit=(Button) this.findViewById(R.id.button_order_submit);
		
		/*mSpinnerPageType=(Spinner)this.findViewById(R.id.spinner1);
		if(mSpinnerPageType!=null){

	        //数据
			ArrayList<String> data_list = new ArrayList<String>();
	        data_list.add("A4");
	        data_list.add("A3");
	     
	        ArrayAdapter<String>  arr_adapter;
	        //适配器
	        arr_adapter= new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, data_list);
	        //设置样式
	        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        //加载适配器
	        mSpinnerPageType.setAdapter(arr_adapter);
		}*/
		/*--------------------------------------------------------------------*/
		mProgressBar_total=(ProgressBar) this.findViewById(R.id.progressBar_total);
		mProgressBar_part=(ProgressBar) this.findViewById(R.id.progressBar_part);
		if(mProgressBar_total!=null&&mProgressBar_part!=null){
			mProgressBar_total.setVisibility(View.GONE);
			mProgressBar_part.setVisibility(View.GONE);
			mProgressBar_total.setMax(PROGRESS_MAX);
			mProgressBar_part.setMax(PROGRESS_MAX);	
		}
	
		
		this.mTextView_SendSpeed=(TextView) this.findViewById(R.id.textView_SendSpeed);
		if(this.mTextView_SendSpeed!=null){
			this.mTextView_SendSpeed.setVisibility(View.GONE);
		}
		mListView=(ListView) this.findViewById(R.id.listView_print_setting_files);
		if(mListView!=null){
			 mListView.setAdapter(mFileShow);
		}
		/*mColorRadio=(RadioGroup) this.findViewById(R.id.radioGroup_color_gray_selected);
		mColorPagetype=(RadioGroup) this.findViewById(R.id.radioGroup_page_type);*/
		/*-------------------------------------------------------------------------------------*/
		this.mButtonCalclePrint=(Button) this.findViewById(R.id.button_cancle_print);
		if(this.mButtonCalclePrint!=null){
			
			this.mButtonCalclePrint.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish(ResultCode_DestoryWizzard);
				}
				
			});
		}
	}
	/**
	 * 
	 * 
	 */	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		super.onServiceConnected(name, service);
		//返回一个MsgService对象
		//mMsgService.mUICloudList.DownloadFile4Server(fileid);
		
		     mFileShow.notifyDataSetChanged();
		 
			 postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					 mFileShow.notifyDataSetChanged();
				}
			}, 1000);
		 
	}
	/**
	 * 
	 * 
	 */
	public  class PrintDocThread implements Runnable
	{
		 NotifyThisView notify;
		 CloudPrintAddress  cloudPrintAddress;
		public  PrintDocThread(NotifyThisView n, CloudPrintAddress cpa){
			this.notify=n;
			if(cpa!=null){
				this.cloudPrintAddress=cpa.Clone();
				this.cloudPrintAddress.SetToastMsg(NOTIFY_THIS_VIEW);
			}
		}
		@Override
		public void run() 
		{		
			  Looper.prepare();
			  if(notify==null&&cloudPrintAddress!=null) return;
			  
			   notify.Start(null);
/*---------------------------------------------------------------------------------------------*/
				// TODO Auto-generated method stub
				if( (cloudPrintAddress!=null)&& cloudPrintAddress.PrintDocs(mOrderTemp)){
					/*暂时注销下面代码*/
					//	notify.printDocSuccess();					
					if(cloudPrintAddress.GetTotalMoney()!=null){
						String Mone;
						Pay pt=new Pay(View_Print_Order_MultiGenerated.this);
						pt.payit("亲打印费用Android",
								cloudPrintAddress.GetTotalBodyOrders(),
								cloudPrintAddress.GetTotalMoney(),
								cloudPrintAddress.GetTotalOrderId());
					}
					
				}	
/*------------------------------------------------------------------------------------------------*/
				notify.Done();
				Looper.loop();
				
			}

		}
	private class NotifyThisView implements NowSendPercent,TransferStart,TransferDone,PrintDocSuccess,Toast_msg
	{
		 long  TIMEOUT=20;
		 long  mProgerssLast=System.currentTimeMillis();
		@Override
		public void NowSendPerrSent(final double Percent, final double Speed) {
			// TODO Auto-generated method stub
			postDelay(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub							 
					int LastProgress=	mProgressBar_part.getProgress();
					int CurrentProgress=(int) (Percent*PROGRESS_MAX);
					if((CurrentProgress!=LastProgress)
						&&(System.currentTimeMillis()-mProgerssLast>TIMEOUT)){
						mProgressBar_part.setProgress(CurrentProgress);
						mProgerssLast=System.currentTimeMillis();
						mTextView_SendSpeed.setText(GetSpeedValue(Speed));
					}
					
				}});
		}

		@Override
		public void Start(Context ct) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
						postDelay(new Runnable(){

							@Override
							public void run() {
								
								/*进度条*/
								mProgressBar_total.setProgress(0);
								mProgressBar_part.setProgress(0);
								mProgressBar_total.setVisibility(View.VISIBLE);					
								mProgressBar_part.setVisibility(View.VISIBLE);					
								mTextView_SendSpeed.setVisibility(View.VISIBLE);
								if(GetWizardViewMode()==WizardViewMode.ModeWizard){									
									mButtomNext.setAlpha((float) 0.5);
									mButtomNext.setEnabled(false);									
								}else{
									findViewById(R.id.relativeLayout_mode_normal).setVisibility(View.GONE);
									//mButtonSubmit.setVisibility(View.GONE);
									//mButtonSubmit.setEnabled(false);
									
								}								
							}});
			

		}

		@Override
		public void Done() {
			// TODO Auto-generated method stub
			postDelay(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					/*进度条*/
					mProgressBar_total.setVisibility(View.GONE);					
					mProgressBar_part.setVisibility(View.GONE);	
					mTextView_SendSpeed.setVisibility(View.GONE);
					/*提交按钮*/
					if(GetWizardViewMode()==WizardViewMode.ModeWizard){
						mButtomNext.setAlpha(1);
						mButtomNext.setVisibility(View.VISIBLE);
						mButtomNext.setEnabled(true);
					}else{
						//mButtonSubmit.setAlpha(1);
						//mButtonSubmit.setVisibility(View.VISIBLE);
						//mButtonSubmit.setEnabled(true);
						findViewById(R.id.relativeLayout_mode_normal).setVisibility(View.VISIBLE);
					}

				}});
		
	   }
		@Override
		public void Done(Context ct, SendFilePackage sfp) {
			
		}

		@Override
		public void printDocSuccess() {
			// TODO Auto-generated method stub
			postDelay(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast_make_show("订单已生成");				
					SetOrderListStartdMode(OrderListStartMode.CloseParents);
					StartActivityView(Activity_OrderListPrint.class);
				}
			
			});
			
		}

		@Override
		public void show(CharSequence text, int duration) {
			// TODO Auto-generated method stub
			Toast_make_show(text.toString(),duration,Gravity.CENTER);	
		}
	}
	public  class StartNextView implements View.OnClickListener
	{

		 StartNextView()
		{
		
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//GetPrinterParam();
			if(mViewMode==null){
				Toast_make_show("mViewMode 无模式");
			}
			/*网单*/
			if(mViewMode==OrderGeneratedMode.SetNetOrderList){
				
				if(mPrintPointTemp!=null){					
					new Thread(new PrintDocThread(NOTIFY_THIS_VIEW,mPrintPointTemp)).start();					
				}else{
					Toast.makeText(v.getContext(),"没有打印机",Toast.LENGTH_LONG).show();
				}			
				boolean  dug=false;
				if(dug){
						Toast.makeText(v.getContext(),"订单已生成",Toast.LENGTH_LONG).show();
				        SetOrderListStartdMode(OrderListStartMode.CloseParents);
				        StartActivityView(Activity_OrderListPrint.class);
				}
			
				
			}else if(mViewMode==OrderGeneratedMode.SetDefaultOrderList){
				
				Toast.makeText(v.getContext(),"成功保存默认设置",Toast.LENGTH_LONG).show();
				
			}else if(mViewMode==OrderGeneratedMode.SetLocalOrderList){
				//
				//执行打印
				// TODO Auto-generated method stub
				if(mPrintPointTemp!=null){					
					new Thread(new PrintDocThread(NOTIFY_THIS_VIEW,mPrintPointTemp)).start();
					
				}else{
					Toast.makeText(v.getContext(),"没有打印机",Toast.LENGTH_LONG).show();
				}
			}else{
				Toast_make_show("mViewMode 未定义");
			}
		}
		
	}
	public  class FilesAndPrinterOrder extends  BaseAdapter
	{		
	    public Handler mHandler=new Handler();
	    public  ArrayList<FilesWithParams> mData=new ArrayList<FilesWithParams>();
	    
		public void SetData(final ArrayList<FilesWithParams> Selected) {				
			
			
			mHandler.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(Selected!=null){
						mData=Selected;
					}				
					
				   notifyDataSetChanged();
				}
				
			});			
			
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
			
		}
	
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mData.get(position);
			
		}
	
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
		    FilesWithParams fwp_t=mData.get(position);
	       if(convertView==null){
				convertView=LibCui.getViewFromeXml(mLayoutFilesPrintParam,parent.getContext());
			}
	       ImageView iv=(ImageView) convertView.findViewById(R.id.thumbnail);
	       TextView  tv=(TextView) convertView.findViewById(R.id.filename);
	       TextView  tv_file_name=(TextView) convertView.findViewById(R.id.textView_file_full_name);
	       Button   button=(Button) convertView.findViewById(R.id.button_alias);
	      
	       if(fwp_t.isFile()){
	    	     FileOperations.SetFileItemBackground(fwp_t.GetF_File(),iv);
	       }else{
	    	    FileOperations.SetFileItemBackground(fwp_t.getNetFileByID(), iv);
	       }
	     
	       
	       if(tv_file_name!=null){	    	   
	    	   if(fwp_t.isFile()){	    		   
	    		     File f=fwp_t.GetF_File();
	    		     tv_file_name.setText("文件名："+f.getAbsolutePath().replace(f.getName(),"")+"\n"+f.getName());
	    	   }else{	    		   
	    		   if(mViewMode==OrderGeneratedMode.SetNetOrderList){	    			 
	    			   tv_file_name.setText("文件名："+fwp_t.GetFileShortName());  /*网单模式*/
	    		   }else{
	    			   tv_file_name.setText("文件名："+fwp_t.GetFileShortName());  /*网单模式*/
	    		   }	    		 
	    	   }	    	 
	       }	    	   
	       if(tv!=null){
	    	   tv.setText(fwp_t.getParamString());
	       }
	       if(button!=null){ 	   
	    	   /*-Net Order Mode-*/
	    	   if(mViewMode==OrderGeneratedMode.SetNetOrderList){
	    		    button.setOnClickListener(new ShowParamSettingNet(fwp_t,GetView()));
	    	   }else{
	    		    button.setOnClickListener(new ShowParamSetting(fwp_t,GetView()));
	    	   }
	    	  
	       }
			return convertView;
		}
/**
 * 
 * 
 * 
 */
	public  class ShowParamSetting	implements View.OnClickListener
		{
			//FilesWithParams mFwp;
			ShowParamDialog mDialog;
			ShowParamSetting(FilesWithParams fwp_t,View  mParent)
			{
			//	this.mFwp=fwp_t;
				this.mDialog=new ShowParamDialog(fwp_t,mParent);
			}
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				this.mDialog.Show();
			}
			
		}
/**
 * 
 * 
 * 
 */
		public  class ShowParamSettingNet	implements View.OnClickListener
			{
				//FilesWithParams mFwp;
			    ShowParamDialogNet mDialog;
				ShowParamSettingNet(FilesWithParams fwp_t,View  mParent)
				{
				//	this.mFwp=fwp_t;
					this.mDialog=new ShowParamDialogNet(fwp_t,mParent);
				}
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					this.mDialog.Show();
				}
				
			}
/**
 * 
 * 
 * 
 */
	public  class ShowParamDialog 
		{
			  private final static int mDialogId=R.layout.alert_dialog_print_param_set;
			  Dialog  mShowDialog;  //先得到构造器;
			  /*----------------------------------*/
			  View    mViewDialog;	
			  View    mParent;
			  FilesWithParams mFwp;
			  RadioGroup  mColorRadio;
			  RadioGroup  mColorPagetype;
			  EditText mEditTextPrintCopys;
			  Spinner mSpinnerPageType;
			  ShowParamDialog(FilesWithParams fwp_t,View  parent)
			  {
				  this.mFwp=fwp_t;
				  this.mParent=parent;
				  this.InitDialog();
				  
			  }
			  public void InitDialog()
			  {
				  mViewDialog=LibCui.getViewFromeXml(mDialogId, mParent.getContext());	
				  mShowDialog=new Dialog(mParent.getContext(),R.style.Translucent_NoTitle); 
				  mShowDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);		
				  /*--------------------------------------------------------------------------*/
				      //打印份数
				  mEditTextPrintCopys=(EditText) this.findViewById(R.id.editText_order_form_copies);
					
				  /*--------------------------------------------------------------------------*/
					  //纸型
					mSpinnerPageType=(Spinner)this.findViewById(R.id.spinner1);
					if(mSpinnerPageType!=null){

				        //数据
						ArrayList<String> data_list = new ArrayList<String>();
						data_list.add("自动适应");
				        
						data_list.add("A4(参数无效)");
				        data_list.add("A3(参数无效)");
				     
				        ArrayAdapter<String>  arr_adapter;
				        //适配器
				        arr_adapter= new ArrayAdapter<String>(this.mViewDialog.getContext(), android.R.layout.simple_spinner_item, data_list);
				        //设置样式
				        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				        //加载适配器
				        mSpinnerPageType.setAdapter(arr_adapter);
					}
				 /*--------------------------------------------------------------------------*/	
					//色彩
					mColorRadio=(RadioGroup) this.findViewById(R.id.radioGroup_color_gray_selected);
					//打印范围
					mColorPagetype=(RadioGroup) this.findViewById(R.id.radioGroup_page_type);
					if(mColorPagetype!=null) mColorPagetype.setEnabled(false);
				 /*--------------------------------------------------------------------------*/				  
							 Button  bt=(Button) findViewById(R.id.button_alert_sure);
							 if(bt!=null){
								//bt.setVisibility(View.GONE);
								bt.setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub 
										 GetPrinterParam();
										 mShowDialog.cancel();	
									}
								});
							 }					 
							 Button  bt_cancle=(Button) mViewDialog.findViewById(R.id.button_alert_cancle);
							 if(bt_cancle!=null){
								//bt.setVisibility(View.GONE);
								 bt_cancle.setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										 mShowDialog.cancel();	
									}
								});
							 }	
					 /*--------------------------------------------------------------------------*/		
					 mShowDialog.setContentView(mViewDialog);
			  }
			  public void Show()
			  {
					int width_v_dialog= (int)(mParent.getWidth()*0.9); // 宽度
					int height_v_dialog=(int)(mParent.getHeight()*0.718);  // 高度		 
					////////////////////////////////////////////////////
					Window dialogWindow =  mShowDialog.getWindow();
					WindowManager.LayoutParams lp = dialogWindow.getAttributes();
					dialogWindow.setAttributes(lp);
					lp.width =width_v_dialog; // 宽度
					lp.height =height_v_dialog;  // 高度
					dialogWindow.setGravity(Gravity.CENTER);
					this.SetPrintParam();
					mShowDialog.show();
			  }
			  /*
			  public String ShowOrg()
			  {
					Window dialogWindow =  mShowDialog.getWindow();
					dialogWindow.setGravity(Gravity.CENTER);
					mShowDialog.show();
					String phoneName=null;
					 				
					return phoneName;
			  }*/
			  public View findViewById(int id)
			  {
				  if(mViewDialog==null){
					  return null;
				  }else{
					  return mViewDialog.findViewById(id);
				  }
			  }
			  public void GetPrinterParam()
				{
					int copies=1;
					boolean IsColor=false;
					boolean Isduplex=false;
					String  page="全部";
					/*---------------------------------------------------------*/
					String copies_str=mEditTextPrintCopys.getText().toString();		
					copies=Integer.parseInt(copies_str);
					/*---------------------------------------------------------*/
					int Checked=mColorRadio.getCheckedRadioButtonId();
					if(Checked==R.id.radioButton_color){
						IsColor=true;
					}if(Checked==R.id.radioButton_gray){
						IsColor=false;
					}else{
						
					}
					/*---------------------------------------------------------*/
					int pagetypreDuplex=mColorPagetype.getCheckedRadioButtonId();
					if(pagetypreDuplex==R.id.radioButton_page_one){
						Isduplex=false;
					}else if(pagetypreDuplex==R.id.radioButton_page_two){
						Isduplex=true;
					}else{
						
					}
					/*---------------------------------------------------------*/
					 mFwp.SetPrinterCopies(copies);
					 mFwp.SetPrinterColor(IsColor);
					 mFwp.SetPrinterDuplexPaper(Isduplex);
					 mFwp.mPrintRange=page;
					
					/*---------------------------------------------------------*/
					 notifyDataSetChanged();
				} 
			  public void SetPrintParam()
			  {
				  if(mEditTextPrintCopys!=null){
						mEditTextPrintCopys.setText(String.valueOf(mFwp.mPrintCopies));
					}
				  /*---------------------------------------------------------*/
				  if(mColorRadio!=null&&mFwp.mIsColor.equals(PrintColor.Color)){
					  mColorRadio.check(R.id.radioButton_color);
				  }else{
					  mColorRadio.check(R.id.radioButton_gray);
				  }
				/*---------------------------------------------------------*/
				  if(mColorPagetype!=null&&mFwp.mIsDuplex.equals(IsDouble.DoublePaper)){
					  mColorPagetype.check(R.id.radioButton_page_two);
				  }else{
					  mColorPagetype.check(R.id.radioButton_page_one);
				  }
				  /*---------------------------------------------------------*/
				  /*---------------------------------------------------------*/
			  }
		}

/**
 * 
 * 
 * 
 */	
	public  class ShowParamDialogNet
	{
		  private final static int mDialogId=R.layout.alert_dialog_print_param_set_net;
		  Dialog  mShowDialog;  //先得到构造器;
		  /*----------------------------------*/
		  View    mViewDialog;	
		  View    mParent;
		  FilesWithParams mFwp;
		  RadioGroup  mColorRadio;
		  RadioGroup  mColorPagetype;
		  EditText mEditTextPrintCopys;
		  Spinner mSpinnerPageType;
		  ShowParamDialogNet(FilesWithParams fwp_t,View  parent)
		  {
			  this.mFwp=fwp_t;
			  this.mParent=parent;
			  this.InitDialog();
			  
		  }
		  public void InitDialog()
		  {
			  mViewDialog=LibCui.getViewFromeXml(mDialogId, mParent.getContext());	
			  mShowDialog=new Dialog(mParent.getContext(),R.style.Translucent_NoTitle); 
			  mShowDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);		
			  /*--------------------------------------------------------------------------*/
			      //打印份数
			  mEditTextPrintCopys=(EditText) this.findViewById(R.id.editText_order_form_copies);
				
			  /*--------------------------------------------------------------------------*/
				  //纸型
				mSpinnerPageType=(Spinner)this.findViewById(R.id.spinner1);
				if(mSpinnerPageType!=null){

			        //数据
					ArrayList<String> data_list = new ArrayList<String>();
					data_list.add("自动适应");
			        
					data_list.add("A4(参数无效)");
			        data_list.add("A3(参数无效)");
			     
			        ArrayAdapter<String>  arr_adapter;
			        //适配器
			        arr_adapter= new ArrayAdapter<String>(this.mViewDialog.getContext(), android.R.layout.simple_spinner_item, data_list);
			        //设置样式
			        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        //加载适配器
			        mSpinnerPageType.setAdapter(arr_adapter);
				}
			 /*--------------------------------------------------------------------------*/	
				//色彩
				mColorRadio=(RadioGroup) this.findViewById(R.id.radioGroup_color_gray_selected);
				//打印范围
				mColorPagetype=(RadioGroup) this.findViewById(R.id.radioGroup_page_type);
				if(mColorPagetype!=null) mColorPagetype.setEnabled(false);
			 /*--------------------------------------------------------------------------*/				  
						 Button  bt=(Button) findViewById(R.id.button_alert_sure);
						 if(bt!=null){
							//bt.setVisibility(View.GONE);
							bt.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub 
									 GetPrinterParam();
									 mShowDialog.cancel();	
								}
							});
						 }					 
						 Button  bt_cancle=(Button) mViewDialog.findViewById(R.id.button_alert_cancle);
						 if(bt_cancle!=null){
							//bt.setVisibility(View.GONE);
							 bt_cancle.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									 mShowDialog.cancel();	
								}
							});
						 }	
				 /*--------------------------------------------------------------------------*/		
				 mShowDialog.setContentView(mViewDialog);
		  }
		  public void Show()
		  {
				int width_v_dialog= (int)(mParent.getWidth()*0.9); // 宽度
				int height_v_dialog=(int)(mParent.getHeight()*0.718);  // 高度		 
				////////////////////////////////////////////////////
				Window dialogWindow =  mShowDialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				dialogWindow.setAttributes(lp);
				lp.width =width_v_dialog; // 宽度
				lp.height =height_v_dialog;  // 高度
				dialogWindow.setGravity(Gravity.CENTER);
				this.SetPrintParam();
				mShowDialog.show();
		  }
		  /*
		  public String ShowOrg()
		  {
				Window dialogWindow =  mShowDialog.getWindow();
				dialogWindow.setGravity(Gravity.CENTER);
				mShowDialog.show();
				String phoneName=null;
				 				
				return phoneName;
		  }*/
		  public View findViewById(int id)
		  {
			  if(mViewDialog==null){
				  return null;
			  }else{
				  return mViewDialog.findViewById(id);
			  }
		  }
		  public void GetPrinterParam()
			{
				int copies=1;
				boolean IsColor=false;
				boolean Isduplex=false;
				String  page="全部";
				/*---------------------------------------------------------*/
				String copies_str=mEditTextPrintCopys.getText().toString();		
				copies=Integer.parseInt(copies_str);
				/*---------------------------------------------------------*/
				int Checked=mColorRadio.getCheckedRadioButtonId();
				if(Checked==R.id.radioButton_color){
					IsColor=true;
				}if(Checked==R.id.radioButton_gray){
					IsColor=false;
				}else{
					
				}
				/*---------------------------------------------------------*/
				int pagetypreDuplex=mColorPagetype.getCheckedRadioButtonId();
				if(pagetypreDuplex==R.id.radioButton_page_one){
					Isduplex=false;
				}else if(pagetypreDuplex==R.id.radioButton_page_two){
					Isduplex=true;
				}else{
					
				}
				/*---------------------------------------------------------*/
				 mFwp.SetPrinterCopies(copies);
				 mFwp.SetPrinterColor(IsColor);
				 mFwp.SetPrinterDuplexPaper(Isduplex);
				 mFwp.mPrintRange=page;
				
				/*---------------------------------------------------------*/
				 notifyDataSetChanged();
			} 
		  public void SetPrintParam()
		  {
			  if(mEditTextPrintCopys!=null){
					mEditTextPrintCopys.setText(String.valueOf(mFwp.mPrintCopies));
				}
			  /*---------------------------------------------------------*/
			  if(mColorRadio!=null&&mFwp.mIsColor.equals(PrintColor.Color)){
				  mColorRadio.check(R.id.radioButton_color);
			  }else{
				  mColorRadio.check(R.id.radioButton_gray);
			  }
			/*---------------------------------------------------------*/
			  if(mColorPagetype!=null&&mFwp.mIsDuplex.equals(IsDouble.DoublePaper)){
				  mColorPagetype.check(R.id.radioButton_page_two);
			  }else{
				  mColorPagetype.check(R.id.radioButton_page_one);
			  }
			  /*---------------------------------------------------------*/
			  /*---------------------------------------------------------*/
		  }
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
			case SDK_PAY_FLAG: {
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
			case SDK_CHECK_FLAG: {
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
	
	public  static class Pay
	{
		
		
		View_CloudPrintTemplate _vt;
		
		
		public Pay(View_CloudPrintTemplate vt){
			this._vt=vt;
		}
		/**
		 * call alipay sdk pay. 调用SDK支付
		 * 
		 */
		public void payit(String subject, String body, String price,String ProductId) {
			if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
				new AlertDialog.Builder(_vt.getContext()).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialoginterface, int i) {
								//
								_vt.finish();
							}
						}).show();
				return;
			}
			//String orderInfo = PayDemoActivity.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
			//String orderInfo = PayDemoActivity.getOrderInfoQin("测试的商品", "该测试商品的详细描述","0.01",ProductId);
			
			if(isDebug){
				price="0.01";
			}
			
			String orderInfo = PayDemoActivity.getOrderInfoQin(subject, body, price,ProductId);
			/**
			 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
			 */
			String sign = PayDemoActivity.sign(orderInfo);
			try {
				/**
				 * 仅需对sign 做URL编码
				 */
				sign = URLEncoder.encode(sign, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			/**
			 * 完整的符合支付宝参数规范的订单信息
			 */
			final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + PayDemoActivity.getSignType();

			Runnable payRunnable = new Runnable() {

				@Override
				public void run() {
					  Looper.prepare();
						// 构造PayTask 对象
						PayTask alipay = new PayTask((Activity) _vt.getContext());
						// 调用支付接口，获取支付结果
						String result = alipay.pay(payInfo, true);
	
						Message msg = new Message();
						msg.what = SDK_PAY_FLAG;
						msg.obj = result;
						_vt.Handler().sendMessage(msg);
					Looper.loop();
				}
			};

			// 必须异步调用
			Thread payThread = new Thread(payRunnable);
			payThread.start();
		}
	}

}
