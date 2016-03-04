package com.jit.cloud_print_cc;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import com.example.LeftRightHand.LeftRightGestureDetector.PhoneInHand;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jit.cloud_print_cc.FileSendOnNet.NowSendPercent;
import com.jit.cloud_print_cc.View_CloudPrintTemplate.FileOperaData;
import com.jit.cloud_print_cc.View_FileSelect.ShowDialog;
import com.jit.cloud_print_cc.View_FileSelect.ShowDialog.FileRcv;
import com.jit.cloud_print_cc.View_FileSelect_NetDisk.RefashNetDiskData;

import com.jit.cloud_print_cc.ServerInfoSendStatus.SendFilePackage;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferDone;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferStart;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferendEndOne;

import com.viewpagerindicator.CirclePageIndicator;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
@SuppressWarnings("unused")
 public class View_FileTransfer extends View_CloudPrintTemplate {

	public  enum DevicesMode
	{
		Dev_PC,
		Dev_Phone,
		Dev_Net
	}
	boolean UseBaseAdapter=false;  
	private final static int mLayoutId = R.layout.activity_file_transfer_view;
	private final static int ModeTextSelected=Color.BLUE;
	private final static int ModeTextUnselected=Color.GRAY;
	private  DevicesMode mDevicesMode;
	/*--------------------------------------------------------------------*/
	private TextView  mTitle;
	private ImageView mTitle2Image;
	private TextView  mTitle2TextName;
	private TextView  mTitle2TextDes;
	private TextView  mTitleDevices;	
	//private GridView  mDevicesList;	
	private ViewPager mViewPagerDevicesList;
	private PagerTabStrip mPagerTabStrip;
	private PagerTitleStrip  mPagerTitleStrip;
	private CirclePageIndicator mCirclePageIndicator;
	private TextView  mTitleFile;
	private GridView  mFileList;
	private Button    mButtonEsc;
	private Button    mButtonSend;
	private Button    mButtonNetMode;
	private Button    mButtonPhoneMode;
	private Button    mButtonPcMode;
	private ProgressBar mProgressBar_total;
	private ProgressBar mProgressBar_part;
	private TextView  mTextView_SendSpeed;
	private View mGridView;	
	public  FileOperations_Select mFileSelect;
	/*--------------------------------------------------------------------*/
	private final ArrayList <String> mDataFile=new ArrayList <String>();
	private final ArrayList <ServerInfo> mSelectedServer=new ArrayList <ServerInfo>();
	/*--------------------------------------------------------------------*/
	PhonePcListAdapter      mPhoneListAdapter; 
	PhonePcListAdapter      mPcListAdapter; 

	PhonePcListPagerAdapter mPcPhoneListPagerAdapter;
	/*--------------------------------------------------------------------*/
	private final NotifyThisView  NOTIFY_THIS_VIEW=new NotifyThisView();
	/*--------------------------------------------------------------------*/
	public View_FileTransfer(Context context) {
		super(context, mLayoutId);
		this.initFileList();
		this.initChildView();
		//new Thread(new UpdataPCPhoneList()).start();
		// TODO Auto-generated constructor stub
	}
	public void initFileList()
	{
		Bundle extras = ((Activity)getContext()).getIntent().getExtras();

	//	if (extras != null)
		{	
			 FileOperaData data_t=this.GetFileOpMode();
			 ArrayList <String> Files=data_t.GetFileArray(); 
			 //ArrayList <String> Files=extras.getStringArrayList(KEY.K_B_FilesSelected); 
			 if(Files!=null){
				 this.mDataFile.clear();
				 for(String file:Files){
					 this.mDataFile.add(file);
				 }
			 }
			

		}
	}
	public void init_indicator(CirclePageIndicator indicator)
	{
		if(indicator!=null){
			 final float density =getContext().getApplicationContext().getResources().getDisplayMetrics().density;
			    indicator.setBackgroundColor(0x00CCCCCC);
		        indicator.setRadius(5 * density);
		        indicator.setPageColor(ModeTextUnselected);
		        indicator.setFillColor(Color.BLUE);
		        indicator.setStrokeColor(Color.TRANSPARENT);
		        indicator.setStrokeWidth(1 * density);
		}
		   
	}
	public void initChildView()
	{
		this.mTitle=(TextView) findViewById(R.id.id_file_transfer_title);
		this.mTitle2Image=(ImageView) findViewById(R.id.id_FileTransfer_title2_imageView);
		this.mTitle2TextName=(TextView) findViewById(R.id.id_FileTransfer_title2_textView_name);
		this.mTitle2TextDes=(TextView) findViewById(R.id.id_FileTransfer_title2_textView_detail);
		this.mTitleDevices=(TextView) findViewById(R.id.id_title_devices_list);
		
		//this.mDevicesList=(GridView) mView.findViewById(R.id.id_file_transfer_devices_list);
		this.mViewPagerDevicesList=(ViewPager) this.findViewById(R.id.viewPager_file_transfer_devices_list);
		
		this.mTitleFile=(TextView) findViewById(R.id.id_title_File_list);
		this.mFileList=(GridView)findViewById(R.id.id_file_transfer_List);
		this.mButtonEsc=(Button) findViewById(R.id.button_esc);
		this.mButtonSend=(Button) findViewById(R.id.button_send);
		this.mButtonNetMode=(Button) findViewById(R.id.button_net_mode);
		this.mButtonPhoneMode=(Button) findViewById(R.id.button_phone_mode);
		this.mButtonPcMode=(Button) findViewById(R.id.button_pc_mode);
		
		/*this.mPagerTabStrip=(PagerTabStrip) findViewById(R.id.pagertab);
		//设置pagerTabStrip  

		this.mPagerTabStrip.setTabIndicatorColor(Color.RED);  
		this.mPagerTabStrip.setTextColor(Color.RED);  
		this.mPagerTabStrip.setClickable(false);  
		this.mPagerTabStrip.setTextSpacing(5);  
		this.mPagerTabStrip.setBackgroundColor(Color.GRAY);  
		this.mPagerTabStrip.setDrawFullUnderline(true);  */
		mCirclePageIndicator=(CirclePageIndicator) findViewById(R.id.circle_page_indicator); 
		this.init_indicator(this.mCirclePageIndicator);
		//mPagerTitleStrip=(PagerTitleStrip)findViewById(R.id.pagertitle);
		//mPagerTitleStrip.setTextColor(Color.RED); 
		//mPagerTitleStrip.setTextSpacing(40);
		//mPagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
		//this.mButtonNetMode.setAlpha((float)0.5);
		//this.mButtonPhoneMode.setAlpha((float)0.5);
		//this.mButtonPcMode.setAlpha((float)0.5);
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
 
		
		this.initGridView();
		this.initViewClick();
		this.initPcView();
	}
/**
 * 
 * 
 */
	public void initGridView()
	{
		this.mGridView=this.findViewById(View_FileSelect.mGridViewId);
		if(this.mGridView instanceof PullToRefreshGridView){
			/*回调函数*/
	    	this.initGridView((PullToRefreshGridView)mGridView);
	    	/*绑定适配器*/
	    	 mFileSelect=View_FileSelect_NetDisk.BingingGridView_NetDisk(this.mGridView);
	   }
		
	}
/**
 * 
 * 
 */
		private void initGridView(PullToRefreshGridView listview)
		{
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			//listview..getRefreshableView().setGroupIndicator(null);  
			//listview.getRefreshableView().setDivider(null);  
			listview.getRefreshableView().setSelector(android.R.color.transparent);  
			listview.setMode(Mode.BOTH);  
			listview.setOnRefreshListener(new OnRefreshListener<GridView>() {
		 
			@Override
			public void onRefresh(final PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				RequestNetDiskForder();
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
 */
		
/**
 * 
 * 初始化各种点击按钮
 */
	public void initViewClick()
	{
		if(this.mButtonEsc!=null)
		this.mButtonEsc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity)View_FileTransfer.this.getContext()).finish();
			}
		});
		if(this.mButtonSend!=null)
		this.mButtonSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*--------------------------------------------------------------------------*/			
					if(mDevicesMode==DevicesMode.Dev_PC||mDevicesMode==DevicesMode.Dev_Phone){	
						//发送文件到手机，电脑
						/*--------------------------------------------------------------------------*/
							if(View_FileTransfer.this.mSelectedServer.size()==0){
								Toast.makeText(v.getContext(),"请选择发送服务器",Toast.LENGTH_SHORT).show();
							}else{
								Thread t=new  Thread(   	new ServerInfoSendFileThread(
															getContext(),
															mSelectedServer,
															mDataFile,
															NOTIFY_THIS_VIEW,
															NOTIFY_THIS_VIEW,
															NOTIFY_THIS_VIEW,
															NOTIFY_THIS_VIEW)        );
								t.start();
							}
		                 /*--------------------------------------------------------------------------*/
					}else if(mDevicesMode==DevicesMode.Dev_Net){
						//发送文件到网盘
						UserInfoOperation_CloudDisk  Post2Server= new UserInfoOperation_CloudDisk(getContext(),mDataFile);
						Post2Server.SetNowSendPercent(NOTIFY_THIS_VIEW);
						Post2Server.setStart(NOTIFY_THIS_VIEW).setDone(NOTIFY_THIS_VIEW).setEnd(NOTIFY_THIS_VIEW).PostFile2Server2Save();
						
					}else{
						Toast_make_show("未定义模式");
					}
				
				

			}
		});
		if(this.mButtonNetMode!=null)
		this.mButtonNetMode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View_FileTransfer.this.initNetView();
			}
		});
		if(this.mButtonPhoneMode!=null)
		this.mButtonPhoneMode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View_FileTransfer.this.initPhoneView();
			}
		});
		if(this.mButtonPcMode!=null){
				this.mButtonPcMode.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						View_FileTransfer.this.initPcView();
					}
				});
		
	           this.mButtonPcMode.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					
					Thread t=new  Thread(new EmbeddedSendFileThread(
							getContext(),
							null,
							mDataFile,
							NOTIFY_THIS_VIEW,
							NOTIFY_THIS_VIEW,
							NOTIFY_THIS_VIEW,
							NOTIFY_THIS_VIEW));
                      t.start();
					return false;
				}
			  });
		
		}
		
		
	}
	public void initNetView()
	{
		this.mDevicesMode=DevicesMode.Dev_Net;
		/*------------------------------------------------*/
		this.mButtonNetMode.setTextColor(ModeTextSelected);
		this.mButtonPhoneMode.setTextColor(ModeTextUnselected);
		this.mButtonPcMode.setTextColor(ModeTextUnselected);
		/*------------------------------------------------*/
		this.mTitle.setText("传输文件----网盘");
		this.mTitle2Image.setImageDrawable(Res().getDrawable(R.drawable.net_disk_img));
		this.mTitle2TextName.setText("网盘信息");
		this.mTitle2TextDes.setText("测试网盘");
		this.mTitleDevices.setVisibility(View.GONE);
		//this.mDevicesList.setVisibility(View.GONE);
		this.mViewPagerDevicesList.setVisibility(View.GONE);
		this.mGridView.setVisibility(View.VISIBLE);
		mCirclePageIndicator.setVisibility(View.INVISIBLE);
		RequestNetDiskForder();
	}
  /**
   * 初始化手机
   */
	public void initPhoneView()
	{
		this.mDevicesMode=DevicesMode.Dev_Phone;
		/*------------------------------------------------*/
		this.mButtonNetMode.setTextColor(ModeTextUnselected);
		this.mButtonPhoneMode.setTextColor(ModeTextSelected);
		this.mButtonPcMode.setTextColor(ModeTextUnselected);
		/*------------------------------------------------*/
		this.mTitle.setText("传输文件----手机");
		this.mTitle2Image.setImageDrawable(Res().getDrawable(R.drawable.oe_wifi_name));
		this.mTitle2TextName.setText("名称");
		this.mTitle2TextDes.setText(LibCui.getWifiName(getContext()));
		this.mTitleDevices.setVisibility(View.VISIBLE);
		this.mTitleDevices.setText("选择手机");
		this.mFileList.setAdapter(new FilesListAdapterS_File(this.getContext(),this.mDataFile));
		//this.mDevicesList.setVisibility(View.VISIBLE);
	//	this.mDevicesList.setAdapter(this.mPhoneListAdapter);
		//this.mDevicesList.setOnItemClickListener(new  OnGridItemClick());
		this.mViewPagerDevicesList.setVisibility(View.VISIBLE);
		if(mViewPagerDevicesList.getAdapter()==null&&mPcPhoneListPagerAdapter!=null){ 
			mViewPagerDevicesList.setAdapter(mPcPhoneListPagerAdapter);
			this.mCirclePageIndicator.setViewPager(mViewPagerDevicesList);
		}else{
			
		}
		//this.mViewPagerDevicesList.setOnClickListener(null);
		this.mSelectedServer.clear();
		this.mGridView.setVisibility(View.INVISIBLE);
		mCirclePageIndicator.setVisibility(View.VISIBLE);
		mGridView.setVisibility(View.INVISIBLE);
		this.PhoneNowIsUpdata();
		
	}
	public void initPcView()
	{
		this.mDevicesMode=DevicesMode.Dev_PC;
		/*------------------------------------------------*/
		if(this.mButtonNetMode!=null){
			this.mButtonNetMode.setTextColor(ModeTextUnselected);
			this.mButtonPhoneMode.setTextColor(ModeTextUnselected);
			this.mButtonPcMode.setTextColor(ModeTextSelected);
		}
		
		/*------------------------------------------------*/
		if(this.mTitle!=null){
			this.mTitle.setText("传输文件----电脑");
		}
        if(this.mTitle2TextName!=null){
		    this.mTitle2TextName.setText("名称");
        }
		
		if(this.mTitle2Image!=null){
		   this.mTitle2Image.setImageDrawable(Res().getDrawable(R.drawable.oe_wifi_name));
		}
		if(this.mTitle2TextDes!=null){
				this.mTitle2TextDes.setText(LibCui.getWifiName(getContext()));
		}
		if(this.mTitleDevices!=null){
			
			this.mTitleDevices.setVisibility(View.VISIBLE);
			this.mTitleDevices.setText("选择电脑");	
		}

		
		this.mFileList.setAdapter(new FilesListAdapterS_File(getContext(),this.mDataFile));
		this.mViewPagerDevicesList.setVisibility(View.VISIBLE);
		if(mViewPagerDevicesList.getAdapter()==null&&mPcPhoneListPagerAdapter!=null){ 
			mViewPagerDevicesList.setAdapter(mPcPhoneListPagerAdapter);
			this.mCirclePageIndicator.setViewPager(mViewPagerDevicesList);
		}
		//this.mViewPagerDevicesList.setOnClickListener(null);
		//this.mDevicesList.setVisibility(View.VISIBLE);		
		//this.mDevicesList.setAdapter(this.mPcListAdapter);
		//this.mDevicesList.setOnItemClickListener(new  OnGridItemClick());
		this.mSelectedServer.clear();
		this.mGridView.setVisibility(View.INVISIBLE);
		mCirclePageIndicator.setVisibility(View.VISIBLE);
		mGridView.setVisibility(View.INVISIBLE);
		this.PcNowIsUpdata();
		
	}
	
	  public void switchViewPages(PagerAdapter mAdapter, ViewPager mViewPager) {
	        if (mViewPager.getAdapter() != null) {
	           
	           
	        }
	        mViewPager.setAdapter(mAdapter);
	        
	  }
	public void InitDevicesList()
	{
		
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		super.onServiceConnected(name, service);
		if(this.UseBaseAdapter){
			 mPhoneListAdapter=new PhonePcListAdapter(getContext(),this.mNetPhonePc.mPCPhonePrinterList.GetPhoneServerInfo()); 
			 mPcListAdapter=new PhonePcListAdapter(getContext(),this.mNetPhonePc.mPCPhonePrinterList.GetPCServerInfo()); 
		}		
		 mPcPhoneListPagerAdapter=new PhonePcListPagerAdapter(getContext(),this.mNetPhonePc.mPCPhonePrinterList.GetPCServerInfo());
		 mPcPhoneListPagerAdapter=new PhonePcListPagerAdapter(getContext(),this.mNetPhonePc.mPCPhonePrinterList.GetPCServerInfo());
		 this.initPcView();
		 postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 mMsgService.mUICloudList.setIRcv(new RefashNetDiskData(View_FileTransfer.this,mFileSelect,new RefashNetDiskData.CallBack() {
					
					 
				
					@Override
					public void RefashData(int file_num,float file_MB) {
						// TODO Auto-generated method stub
						 DecimalFormat decimalFormat;
						 if(file_MB>0){
							 decimalFormat=new DecimalFormat(".0");
						 }else{
							 decimalFormat=new DecimalFormat(".00");
						 }
						 mTitle2TextDes.setText("文件 :"+file_num+"个,"+"已用空间:"+decimalFormat.format(file_MB)+"MB"); 
					} 
				}));
			}
		 },1000);
		
		 RequestNetDiskForder();
		
		 
	}
/**
 * 
 * 
 * 
 * 
 **/
	public class PhonePcListAdapter  extends BaseAdapter 
	{
		private Context mContext_t;
		private final boolean mIsMultiChoice=true;
		private int mResource_t;
		private boolean mIsUpdataFile=true;
        //private final static int   mDefaultGridRes=R.layout.efp__grid_item;	
		private final static int   mDefaultGridRes=R.layout.grid_view_phone_pc_list__item;	
        private final ArrayList<ServerInfo> mData=new ArrayList<ServerInfo>();
       
		public PhonePcListAdapter(Context context, int resource) 
		{
			this.mContext_t = context;
			this.mResource_t = resource;
			this.mIsUpdataFile=true;			
		}
		public PhonePcListAdapter(Context context,ArrayList<ServerInfo> Selected) {
			this.mContext_t = context;
			this.mResource_t =mDefaultGridRes;
			this.mIsUpdataFile=true;		
			this.SetData(Selected);
		}
		public void SetData(final ArrayList<ServerInfo> Selected) {
			
			
			
			postDelay(new Runnable(){

				@Override
				public void run() {
					if(Selected!=null){
						mData.clear();
						mData.addAll(Selected);
					}
				
					// TODO Auto-generated method stub
					PhonePcListAdapter.this.notifyDataSetChanged();
				}
				
			});
		}
		@Override
		protected void finalize(){
			this.mIsUpdataFile=false;
		}
		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			if(position<mData.size()){
				return mData.get(position);
			}else{
				return null;
			}
			
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		 @Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			 ServerInfo si;
			 convertView = LayoutInflater.from(parent.getContext()).inflate(mResource_t, parent, false);
			 if(UseBaseAdapter==false) 
				 return convertView;
			 if(this.mData.size()>position){
				 
				 si=this.mData.get(position);
			 }else{
				 return convertView;
			 }
			
			ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

			CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			
			if (mSelectedServer.contains(si)) {
				checkbox.setChecked(true);
				checkbox.setVisibility(View.VISIBLE);
				setItemBackground(convertView, true);
			} else {
				checkbox.setChecked(false);
				checkbox.setVisibility(View.GONE);
				setItemBackground(convertView, false);
			}
			

		   this.SetServerImg(si,thumbnail);

			TextView filename = (TextView) convertView.findViewById(R.id.filename);
			filename.setText(si.GetDes());

			TextView filesize = (TextView) convertView.findViewById(R.id.filesize);
			if (filesize != null) {
				//if (file.isFile()) filesize.setText(getHumanFileSize(file.length()));
				//else filesize.setText("");
			}
			Button button=(Button) convertView.findViewById(R.id.button_alias);
			if(button!=null){
				button.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
					
				});
				 if(LibCui.getLocalIpAddressList().contains(si.mAddress)){
					 //本机，开放button
					 button.setVisibility(View.VISIBLE);
					 
				 }else{
					 //远程机,禁用button
					 button.setVisibility(View.GONE);
					 
				 }
			}
			
			
			return convertView;
		}
	
		
		String getHumanFileSize(long size) {
			String[] units =mContext_t.getResources().getStringArray(R.array.efp__size_units);
			for (int i = units.length - 1; i >= 0; i--) {
				if (size >= Math.pow(1024, i)) {
					return Math.round((size / Math.pow(1024, i))) + " " + units[i];
				}
			}
			return size + " " + units[0];
		}
		
		private void setItemBackground(View view, boolean state) {
			//view.setBackgroundResource(state ? attrToResId(R.attr.efp__selected_item_background) : 0);
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
	
/**
 * 
 * 
 * 
 * 
 **/
	public class PhonePcListPagerAdapter  extends PagerAdapter
	{
		private Context mContext_t;
		private final boolean mIsMultiChoice=true;
		private int mResource_t;
		private boolean mIsUpdataFile=true;
		//public  ViewGroup mViewParent=null;
        //private final static int   mDefaultGridRes=R.layout.efp__grid_item;	
		private final static int   mDefaultGridRes=R.layout.grid_view_phone_pc_list__item;	
        private final ArrayList<ServerInfo> mData=new ArrayList<ServerInfo>();
       
        //private List<View> mViewsList=new ArrayList<View>();
        public String mCurrentListId=UUID.randomUUID().toString();
        public String mcontainerListId=UUID.randomUUID().toString();
		public PhonePcListPagerAdapter(Context context,ArrayList<ServerInfo> Selected)
		{
			this.mContext_t = context;
			this.mResource_t =mDefaultGridRes;
			this.mIsUpdataFile=true;		
			this.SetData(Selected);
		}
	    public void SetData(final ArrayList<ServerInfo> Selected) {
			
	    	// TODO Auto-generated method stub
			postDelay(new Runnable(){
				@Override
				public void run() {	
					
					 if(Selected!=null){
						 mData.clear();				 
						 mData.addAll(Selected);
						 mCurrentListId=UUID.randomUUID().toString();                	
						 PhonePcListPagerAdapter.this.notifyDataSetChanged();	
				   }					
				}						
			});
			
		}
	    public String ShowPhoneNameDialog()
	    {
	    	new ShowDialog().ShowOrg();
	    	return "";
	    }
		public View InitViewByServer(ServerInfo si,ViewGroup parent)
		{
			 
              if(true){
            	  
            	View convertView = LayoutInflater.from(mContext_t).inflate(mResource_t,parent, false);
            	
  			
            	convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CheckBox checkbox = (CheckBox) v.findViewById(R.id.checkbox);
						int index=mViewPagerDevicesList.indexOfChild(v);
		  				if (!checkbox.isChecked()) {
		  					checkbox.setChecked(true);
		  					checkbox.setVisibility(View.VISIBLE);
		  					mSelectedServer.add(mData.get(index));
		  				} else {
		  					checkbox.setChecked(false);
		  					checkbox.setVisibility(View.GONE);
		  					mSelectedServer.remove(mData.get(index));
		  				}
					}
				});
            	ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

  				CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
  				
  				if (mSelectedServer.contains(si)) {
  					checkbox.setChecked(true);
  					checkbox.setVisibility(View.VISIBLE);
  				} else {
  					checkbox.setChecked(false);
  					checkbox.setVisibility(View.GONE);
  				}
  				

  			   this.SetServerImg(si,thumbnail);

  				TextView filename = (TextView) convertView.findViewById(R.id.filename);
  				filename.setText(si.GetDes());

  				TextView filesize = (TextView) convertView.findViewById(R.id.filesize);
  				if (filesize != null) {
  					//if (file.isFile()) filesize.setText(getHumanFileSize(file.length()));
  					//else filesize.setText("");
  				}
  			/*-----------------------------------------------------------------*/
  				Button button=(Button) convertView.findViewById(R.id.button_alias);
  				if(button!=null){
  					button.setOnClickListener(new View.OnClickListener(){

  						@Override
  						public void onClick(View v) {
  							// TODO Auto-generated method stub
  							//Toast.makeText(v.getContext(),"别名设置",Toast.LENGTH_SHORT).show();
  							ShowPhoneNameDialog();
  						}
  						
  					});
  					if(ServerInfo.IsSetPhoneNameAlias()){
  						button.setText("别名修改");
  					}else{
  						button.setText("别名设置");
  					}
  					 if(LibCui.getLocalIpAddressList().contains(si.mAddress)){
  						 //本机，开放button
  						 button.setVisibility(View.VISIBLE);
  						 
  					 }else{
  						 //远程机,禁用button
  						 button.setVisibility(View.GONE);
  						 
  					 }
  				}
  			/*------------------------------------------------------------------*/  			
  				return convertView;
              }else{
            	//  View convertView = LayoutInflater.from(mContext_t).inflate(R.layout.efp__list_item, null, false);
              	//return  convertView;
            	  Button b=new Button(mContext_t);
            	  b.setWidth(100);
            	  b.setHeight(50);
            	  b.setText("100*50");
            	return  b;
              }
				
		}
		public void SetServerImg(ServerInfo si,ImageView v)
		{
			if(si.mHostType==1){
				v.setImageDrawable(Res().getDrawable(R.drawable.computer_mid));
			}else if(si.mHostType==2){
				v.setImageDrawable(Res().getDrawable(R.drawable.phone_mid));
			}else{
				v.setBackgroundColor(Color.RED);
			}
	
		}
		@Override
		public int getItemPosition(Object object) {
		return POSITION_NONE;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			 return arg0 == arg1; // key  
		}
		 /** 
         * 销毁当前page的相隔2个及2个以上的item时调用 
         */  
        @Override  
        public void destroyItem(ViewGroup container, int position, Object object) {  
           // container.removeView( mViewsList.get(position)); // 将view 类型 的object熊容器中移除,根据key 
        	 container.removeView((View)object);//删除页卡
        }  
        
        /** 
         * 当前的page的前一页和后一页也会被调用，如果还没有调用或者已经调用了destroyItem 
         */  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {
        	
        	    //mViewParent=container;
        	    View v=InitViewByServer(mData.get(position),container);
        	    container.addView(v);
        	return v;  
        } 
  
        @Override  
        public CharSequence getPageTitle(int position) {  
            // TODO Auto-generated method stub  
            //return  String.valueOf(position);  
        	if(this.mData.size()>position){
        	     return  this.mData.get(position).GetHostName(); 
        	}else{
        	     return  ""; 
        	}
        
        }  
        public class ViewPageClick implements View.OnClickListener
        {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
        	
        }
    	/**
    	 * 
    	 * 
    	 * 
    	 */
    	public class ShowDialog 
    	{
    		  private final static int mDialogId=R.layout.alert_dialog_input_text;
    		  Dialog  mShowDialog=new Dialog(getContext(),R.style.Translucent_NoTitle);  //先得到构造器;
    		  View    mViewDialog=LibCui.getViewFromeXml(mDialogId, getContext());	
    	
    		  ShowDialog()
    		  {
    				this.InitDialog();
    			
    		  }
    		  public void InitDialog()
    		  {
    			  mShowDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);		
    				TextView tv_title=(TextView) mViewDialog.findViewById(R.id.textView_alert_title);
    				if(tv_title!=null){
    					tv_title.setText("请设置手机别名");
    				}
    				 /*--------------------------------------*/
    				 Button  bt=(Button) mViewDialog.findViewById(R.id.button_alert_sure);
    				 if(bt!=null){
    					//bt.setVisibility(View.GONE);
    					bt.setOnClickListener(new View.OnClickListener() {
    						
    						@Override
    						public void onClick(View v) {
    							// TODO Auto-generated method stub
    							EditText tv_title=(EditText) mViewDialog.findViewById(R.id.editText_alert_input);
    		    				if(tv_title!=null){
    		    					String phoneName=tv_title.getText().toString();
    		    					ServerInfo.SetHostPhoneNameAlias(phoneName);
    		    					Toast_make_show_center("别名修改成功");
    		    					postDelayed(new Runnable(){

										@Override
										public void run() {
											// TODO Auto-generated method stub
											PhonePcListPagerAdapter.this.notifyDataSetChanged();
										}
    		    							
    		    					},1000 );
    		    				
    		    				}   
    							 mShowDialog.cancel();	
    						}
    					});
    				 }	
    				 /*--------------------------------------*/
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
    			     /*--------------------------------------*/
    				 EditText et_title=(EditText) mViewDialog.findViewById(R.id.editText_alert_input);
    				 if(et_title!=null){
    					 if(ServerInfo.IsSetPhoneNameAlias()){
    						 et_title.setText(ServerInfo.GetHostPhoneNameAlias());
       					}
     				}   
    					
    			     /*--------------------------------------*/
    				 mShowDialog.setContentView(mViewDialog);
    		  }
    		  public void Show()
    		  {
    				int width_v_dialog= (int)(GetView().getWidth()*0.9); // 宽度
    				int height_v_dialog=(int)(GetView().getHeight()*0.618);  // 高度		 
    				////////////////////////////////////////////////////
    				Window dialogWindow =  mShowDialog.getWindow();
    				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
    				dialogWindow.setAttributes(lp);
    				lp.width =width_v_dialog; // 宽度
    				lp.height =height_v_dialog;  // 高度
    				dialogWindow.setGravity(Gravity.CENTER);
    				mShowDialog.show();
    		  }
    		  public String ShowOrg()
    		  {
    				Window dialogWindow =  mShowDialog.getWindow();
    				dialogWindow.setGravity(Gravity.CENTER);
    				mShowDialog.show();
    				String phoneName=null;
    				 				
    				return phoneName;
    		  }

    	}
/**
 * 
 * 
 * 
 */
    }
	/**
	 * 
	 * 
	 * 
	 * 
	 **/
	public boolean IsParamValid()
    {
    	if(mNetPhonePc==null||mPcPhoneListPagerAdapter==null||mDevicesMode==null){
			return false;
    	}else{
			return true;
    	}

    }
/**
 * 
 * 设置phoneadapter更新数据
 * 
 * 
 **/
	@Override
	protected void  PhoneNowIsUpdata()
	{
		if(IsParamValid()==false)		return;
		if(mDevicesMode==DevicesMode.Dev_Phone){
			mPcPhoneListPagerAdapter.SetData(mNetPhonePc.mPCPhonePrinterList.GetPhoneServerInfo());
		}
	}
/**
 * 设置pcadapter 更新数据
 * 
 */
	@Override
	protected void  PcNowIsUpdata()
	{
		if(IsParamValid()==false)		return;
		if(mDevicesMode==DevicesMode.Dev_PC){
			mPcPhoneListPagerAdapter.SetData(mNetPhonePc.mPCPhonePrinterList.GetPCServerInfo());
		}
	}
/**
 * 
 * 
 * 
 * 
 **/
/*	public class UpdataPCPhoneList  implements Runnable
	{
		public String mCurrentPhoneListId=UUID.randomUUID().toString();;
		public String mCurrentPCListId=UUID.randomUUID().toString();
		public String mCurrentPCPhoneList=UUID.randomUUID().toString();
        public boolean IsParamValid()
        {
        	if(mNetPhonePc==null||mPcPhoneListPagerAdapter==null||mDevicesMode==null){
				return false;
        	}else{
				return true;
        	}

        }
		public void UpdataOldAdapter()
		{
			if(UseBaseAdapter){
					if(mNetPhonePc==null||mPcListAdapter==null||mPhoneListAdapter==null)
						return;
					if(mNetPhonePc.mPCPhonePrinterList.GetPcListId()!=mCurrentPCListId||mPcListAdapter.getCount()==0){
						//列表有更新
						mPcListAdapter.SetData(mNetPhonePc.mPCPhonePrinterList.GetPCServerInfo());							
						
						mCurrentPCListId=mNetPhonePc.mPCPhonePrinterList.GetPcListId();
					}
					if(mNetPhonePc.mPCPhonePrinterList.getPhoneListId()!=mCurrentPhoneListId||mPhoneListAdapter.getCount()==0){
						mPhoneListAdapter.SetData(mNetPhonePc.mPCPhonePrinterList.GetPhoneServerInfo());
						
						mCurrentPhoneListId=mNetPhonePc.mPCPhonePrinterList.getPhoneListId();
					}
			}
		}
		public void UpdataNewAdapter()
		{
			if(IsParamValid()==false)		return;
			
			String CurrentID_t=mNetPhonePc.mPCPhonePrinterList.getPCPhoneListId()+mDevicesMode;
			
			if(!CurrentID_t.equals(mCurrentPCPhoneList)){
				//列表有更新
				if(mDevicesMode==DevicesMode.Dev_PC){
					mPcPhoneListPagerAdapter.SetData(mNetPhonePc.mPCPhonePrinterList.GetPCServerInfo());
				}
				if(mDevicesMode==DevicesMode.Dev_Phone){
					mPcPhoneListPagerAdapter.SetData(mNetPhonePc.mPCPhonePrinterList.GetPhoneServerInfo());
				}
				
				mCurrentPCPhoneList=CurrentID_t;
			}
			
		}

		@Override
		public void run()
		{
			Looper.prepare();
			while(mThreadRun){
				try {
					postDelay(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							//更新旧的adapter
							 UpdataOldAdapter();
							//更新新的adapter
							 UpdataNewAdapter();
						}
						
					});
					
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Looper.loop();
		}
		
	}*/
/**
 * 
 * 
 * 
 * 
 **/
	public class OnServerItemClick implements OnItemClickListener,View.OnClickListener
	{
		boolean mIsMultiChoice=true;
		boolean mOptOnlyOneItem=false;
		
		
		OnServerItemClick()
		{
			
		}
		public void setItemBackground(View view,boolean flag)
		{
			
		}
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				
				
				GridView gridView=(GridView)parent;
				
				PhonePcListAdapter	adapter=(PhonePcListAdapter)gridView.getAdapter();
				
				ArrayList<ServerInfo> mData=adapter.mData;
				
				if(mData!=null&&position<mData.size()){
					 ServerInfo si=mData.get(position);
					 if (mIsMultiChoice) {
							CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
							if (checkBox.isChecked()) {
								checkBox.setChecked(false);
								setItemBackground(view, false);								
								mSelectedServer.remove(si); 
							} else {
								if (mOptOnlyOneItem) {								
									
									
								}
								checkBox.setChecked(true);
								setItemBackground(view, true);
								mSelectedServer.add(si);
							}
						} 
				}
				((BaseAdapter)(gridView.getAdapter())).notifyDataSetChanged(); 
			}
			@Override
			public void onClick(View v) 
			{
			
		
			}
		
	}
/**
 * 
 * 
 * 
 * 
 **/
	private class NotifyThisView implements TransferStart,TransferDone,TransferendEndOne,NowSendPercent
	{
		 boolean IsSendDone=false;
		 long  TIMEOUT=20;
		 long  mProgerssLast=System.currentTimeMillis();
		 
		@Override
		public void Start(Context ct) {
			// TODO Auto-generated method stub			
			View_FileTransfer.this.postDelay(new Runnable(){

				@Override
				public void run() {
				
					// TODO Auto-generated method stub
					mButtonSend.setEnabled(false);
					mButtonSend.setVisibility(View.INVISIBLE);
					mProgressBar_total.setProgress(0);
					mProgressBar_part.setProgress(0);
					mProgressBar_total.setVisibility(View.VISIBLE);					
					mProgressBar_part.setVisibility(View.VISIBLE);					
					mTextView_SendSpeed.setVisibility(View.VISIBLE);
				}
				
			});
		
		}

		@Override
		public void Done(Context ct,final SendFilePackage sfp) {
			// TODO Auto-generated method stub
			View_FileTransfer.this.postDelay(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// TODO Auto-generated method stub
					mButtonSend.setEnabled(true);
					mButtonSend.setVisibility(View.VISIBLE);
					mProgressBar_total.setVisibility(View.GONE);					
					mProgressBar_part.setVisibility(View.GONE);	
					mTextView_SendSpeed.setVisibility(View.GONE);
					ShowDialog_new(sfp.GetSendInfo());
					
				}});
		
		}
		
		@Override
		public void EndOne(final Context ct, final ServerInfo si, final double percent) {
			// TODO Auto-generated method stub
			View_FileTransfer.this.postDelay(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mProgressBar_total.setProgress((int) (percent*PROGRESS_MAX));
					if(si!=null)
					si.Toast_Make_Text_Show_ErrorMsg(ct);
				}
				
			});
		}

		@Override
		public void NowSendPerrSent(final double Percent,final double Speed) {
			// TODO Auto-generated method stub
						View_FileTransfer.this.postDelay(new Runnable(){

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
	    public void ShowDialog_new(String msg)
		{
			if(IsDestory()){
				return;
			}
			
			final Dialog builder=new Dialog(getContext(),R.style.Translucent_NoTitle);  //先得到构造器
			int width_v_dialog= (int)(GetView().getWidth()*0.9); // 宽度
			int height_v_dialog=(int)(GetView().getHeight()*0.618);  // 高度
			builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
			//builder.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
			View v=LibCui.getViewFromeXml(R.layout.alert_dialog_file_send, getContext());
			// v.setBackgroundColor(Color.GRAY);
			 TextView tv=(TextView) v.findViewById(R.id.textView_alert_msg);
			 if(tv!=null){
				 tv.setText(msg);
			 }
			 Button  bt=(Button) v.findViewById(R.id.button_alert_sure);
			 if(bt!=null){
				//bt.setVisibility(View.GONE);
				bt.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						builder.cancel();	
					}
				});
			 }	
			 
			 TextView  textViewMsg=tv;
			 textViewMsg.setMovementMethod(new ScrollingMovementMethod());
			 //int scrool_height= height_v_dialog-200;
			 //textViewMsg.setHeight(scrool_height);
			 ////////////////////////////////////////////////////////////////////////////
			    builder.setContentView(v);
			    Window dialogWindow =  builder.getWindow();
		        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		        dialogWindow.setAttributes(lp);
		        lp.width =width_v_dialog; // 宽度
		        lp.height =height_v_dialog;  // 高度
		        dialogWindow.setGravity(Gravity.CENTER);
			 builder.show();
			 
			 
		}
	

		@Override
		public void Done() {
			// TODO Auto-generated method stub
			
		}
		
	}    
/**
 * 
 * 
 * 
 * 
 **/
//	public void RequestNetDisk()
//	{
//		
//	}
	
}
