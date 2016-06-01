package com.jit.cloud_print_cc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.LeftRightHand.LeftRightGestureDetector.PhoneInHand;
import com.jit.cloud_print_cc.KEY.FileOperationWay;
import com.jit.cloud_print_cc.KEY.OrderGeneratedMode;
import com.jit.cloud_print_cc.KEY.OrderListStartMode;
import com.jit.cloud_print_cc.KEY.WizardViewMode;
import com.jit.cloud_print_cc.R;
import com.jit.cloud_print_cc.CloudPrintAddressBase.PrintFrom;
import com.jit.cloud_print_cc.CloudPrintAddressBase.PrintType;
import com.jit.cloud_print_cc.View_FileTransfer.DevicesMode;
import com.jit.cloud_print_cc.View_PrintSetPrintPoint.SharedPrinterAdapter;
import com.jit.update.UpdateAndroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressWarnings("unused")
public class View_CloudPrintTemplate  implements Interfase_CloudPrintView,OnGlobalLayoutListener,ServiceConnection
{
	protected final static int PROGRESS_MAX=100;
	public final static int ResultCode_OrderIsSuccess=11;
	public final static int ResultCode_DestoryWizzard=11;
	public  boolean SmallMemUsage=true; 
	protected  boolean     mWindowLoadSuccess=false;
	private	View       mView;
	WizardViewMode mMode;
	public Button mButtomPrev=null;
	public Button mButtomBack=null;
	public Button mButtomNext=null;
	private   Context      mContext;
	protected PhoneInHand mPhoneHand;
	protected  int mLayoutIdx;
	int mRootWidth;
	int mRootHeight;
	int mRootWidthOld;
	int mRootHeightOld;
	protected volatile boolean mThreadRun=true;
	public   NetPhonePc mNetPhonePc;
	/*--------------------------------------------*/
	public   Service_Net mMsgService;
	private  ServiceConnection mConnection =this;
	private  Handler mHandler = new Handler();
	 //private static Bitmap mBGLeft;
	 //private static Bitmap mBGRight;
	// private   LocationClient mLocClient;
	 private   BaiduLocation  mBaiduLocation;
	 protected BDLocation mLocation=null;
	 /**
	  * 当前可用打印机
	  */
	 protected  CloudPrintAddress   mPrintPointTemp;
	 protected Button   mButtonCalclePrint;
	public View_CloudPrintTemplate (Context context)
	  {
			mRootWidth=0;
			mRootHeight=0;
			mRootWidthOld=0;
			mRootHeightOld=0;
			mLayoutIdx=0;
		    this.mContext=context;
		  
		    this.InitView();
		    this.SetPhoneInHand(null);
		    this.initBackgroundbyHand();
		    this.StratNetSetvices();
		    this.InitCheakPrinterPhonePc();
	  }
	public View_CloudPrintTemplate (Context context,int mLayoutId)
	  {
			mRootWidth=0;
			mRootHeight=0;
			mRootWidthOld=0;
			mRootHeightOld=0;
			this.mLayoutIdx=mLayoutId;
		    this.mContext=context;
		  
		    this.InitView();
		    this.SetPhoneInHand(null);
		    this.initBackgroundbyHand();
		    this.StratNetSetvices();
		    this.InitCheakPrinterPhonePc();
	  }
	public View_CloudPrintTemplate (Context context,int mLayoutId,PhoneInHand   PhoneInHand)
	  {
			mRootWidth=0;
			mRootHeight=0;
			mRootWidthOld=0;
			mRootHeightOld=0;
			this.mLayoutIdx=mLayoutId;
		    this.mContext=context;
		   
		    this.InitView();
		    this.SetPhoneInHand(PhoneInHand);
		    this.initBackgroundbyHand();
		    this.StratNetSetvices();
		    this.InitCheakPrinterPhonePc();
	  }
	
	public void InitLocationClient()
	{			
			this.mBaiduLocation=new BaiduLocation(getContext(),new MyLocationListenner());
	}
	public void RequestLocationClient()
	{
		if(this.mBaiduLocation!=null)
		   this.mBaiduLocation.RequestLocationClient();
	}
	
	private void InitView()
	{
		 this.InitMainView();
		 this.SetBackground();
		 this.mView.getViewTreeObserver().addOnGlobalLayoutListener(this);
		 
		 
	}
	@Override
	public void onGlobalLayout()
	{     
	      mRootWidth=mView.getMeasuredWidth();
	      mRootHeight=mView.getMeasuredHeight(); 
	      //Log.i("CDH", "Global W:"+mRootWidthOld+"  H:"+mRootHeightOld);	
			//first in this Place	
			if((mRootWidthOld!=mRootWidth)||(mRootHeightOld!=mRootHeight)){
				 
				// Layout Change
				 // this.mLeftRightDetector.mPhoneHand=PhoneInHand.PhoneLeftHand;
				  this.initBackgroundbyHand(mPhoneHand);
				 // this.HandChange(this.mLeftRightDetector.mPhoneHand);
				  
			}	
			mRootWidthOld=mRootWidth;
			mRootHeightOld=mRootHeight;
		 
		}
	
	protected void InitMainView()
    {

		if(this.mLayoutIdx!=0){
			this.mView=LibCui.getViewFromeXml(mLayoutIdx,mContext);
			this.mButtomPrev=(Button) this.findViewById(R.id.id_button_bottom_prev);
			if(mButtomPrev!=null){
				mButtomPrev.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						 finish();
					}
				});
			}
			this.mButtomBack=(Button) this.findViewById(R.id.id_button_bottom_esc);
			if(mButtomBack!=null){
				mButtomBack.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//SetOrderListStartdMode(OrderListStartMode.CloseParents);
						finish(ResultCode_DestoryWizzard);
					}
				});
			}
			this.mButtomNext=(Button) this.findViewById(R.id.id_button_bottom_next);
			
			Button button_t=(Button) this.findViewById(R.id.button_esc);
			if(button_t!=null){
				button_t.setOnClickListener( new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					    finish();
					}
				});
			}
			
		}else{
			this.mView=new View(this.mContext);
		}
	
	  
	}
	private void initBackgroundbyHand()
	{
		
		
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		int memClass=activityManager.getMemoryClass();		
		
		if(memClass>200){
			//手机非常豪华
			SmallMemUsage=false;
		}		
		this.initBackgroundbyHand(this.mPhoneHand);
		
	}
	private void InitCheakPrinterPhonePc()
	{
		new Thread(new CheckPrinterListIsUpdata()).start();
		new Thread(new CheckPcPhoneListIsUpdata()).start();
	}
	private void initBackgroundbyHand(PhoneInHand  hand)
	{		
		 //int BGColor=0xff66ccff;
		 int BGColor=this.mContext.getResources().getColor(R.color.qin_body_white_smoke);
		 if(mRootWidth+mRootHeight==0)		 return;
		 try{
			  if(hand==PhoneInHand.PhoneLeftHand){
				  if(SmallMemUsage){
					  mView.setBackgroundColor(BGColor);
				  }else{
					  LibCui.SetBackground2View(mView, R.drawable.bg_left_minor, mContext);	
				  }				 
				}else if(hand==PhoneInHand.PhoneRightHand){
					 if(SmallMemUsage){
						 mView.setBackgroundColor(BGColor);
					  }else{
						  LibCui.SetBackground2View(mView, R.drawable.bg_right_minor, mContext);
					  }					
				}else{
					 mView.setBackgroundColor(BGColor);
				}
		  }catch(Exception nfe){
			  Log.v("1",nfe.getMessage());
			
		  }
		
	}
	public CloudPrintAddress getPrinter()
	{
		  this.mPrintPointTemp=(CloudPrintAddress) (getIntent().getSerializableExtra(GetPrinter2UseKey()));		  
		  return   this.mPrintPointTemp;
	}
	private void SetBackground()
	{
		 this.mView.setBackgroundColor(Color.RED);
	}
	private void SetPhoneInHand(PhoneInHand  hand)
	{
		if(hand!=null){
			 this.mPhoneHand=hand;
			 return;
		}	
		////////////////////////////
		
		/////////////////////////
		SharedPreferences preferences=this.mContext.getSharedPreferences(this.GetSharedFile(), Context.MODE_PRIVATE);
		
		if(preferences.contains(this.GetHandKey())){
			String handstr=preferences.getString(this.GetHandKey(),"fuck");
				if(handstr!=null){
						if(handstr.equals(PhoneInHand.PhoneRightHand.toString())){
							 this.mPhoneHand=PhoneInHand.PhoneRightHand;
							 return;
						}else if(handstr.equals(PhoneInHand.PhoneLeftHand.toString())){
							 this.mPhoneHand=PhoneInHand.PhoneLeftHand;
							 return;
						}else{
						
						}
				}
		}
		
		
		
	}

	public  String GetHandKey(){
		final String key="lrType";
		return key;
	}
	public  String GetSharedFile(){
		final String key="SharedData";
		return key;
	}
	
	public String  GetPrinter2UseKey()
	{
		return "PrinterKey";
	}
	final protected View SetView(View v)
	{
	   
		return mView=v;
	}
	public View GetView()
	{
	   
		return mView;
	}
	public void showPopupWindow( View parent)
	{
		  LibCui.showPopupWindow(this.mContext,parent,this.mView);
	}

	public void StartActivityView( Class<?> cls){
		 
		   Bundle bundle=new Bundle();
	       this.StartActivityView(cls, bundle);
	       
	}
	public final void StartActivityView( Class<?> cls,Bundle bundle){
		this.StartActivityView(cls, bundle,new Intent());
		
	}
	public final void StartActivityView( Class<?> cls,Bundle bundle,Intent intent ){
	       /*Bundle 传左右手*/
		   //if(mPhoneHand!=null)      bundle.putString(GetHandKey(),mPhoneHand.toString());
		   /* SharedPreferences 传左右手*/
		   SharedPreferences shared_sp=this.mContext.getSharedPreferences(this.GetSharedFile(), Context.MODE_PRIVATE);
		   if(shared_sp.contains(this.GetHandKey())){
			   /*LeftRight已存在----更新*/
			   Editor editor =shared_sp.edit();		   
		       editor.putString(GetHandKey(), (mPhoneHand==null)? PhoneInHand.PhoneLeftHand.toString():mPhoneHand.toString());
		       editor.commit();
		   }else{
			   //写入
			   Editor editor =shared_sp.edit();		   
		       editor.putString(GetHandKey(), (mPhoneHand==null)? PhoneInHand.PhoneLeftHand.toString():mPhoneHand.toString());
		       editor.commit();
		   }
		   
		   if(intent==null){
			   intent=new Intent();
		   }
		   if(bundle==null){
			   bundle=new Bundle();	  
		   }
		   /*--启动 Activity*/
	       intent.setClass(this.mContext,cls);
	       intent.putExtras(bundle);
	    
	       ((Activity)(this.mContext)).startActivityForResult(intent, 0);
	     //  ((Activity)(this.mContext)).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}
	
	/*public enum FileOperationWay{
		OpenWithFileTransfer,
		OPen2SetDefaultPrint,
		OPen2SetPrint,
		NULL
	}*/
	public static class FileOperaData
	{
		public FileOperationWay mMode;
		public final Set<String>     mFiles=new HashSet<String>();
		public FileOperaData()
		{
			
		}
		public FileOperaData(FileOperaData data)
		{
			this.mMode=data.mMode;
			this.SetFiles(data.mFiles);
		}
		public void SetFiles(Set<String> files)
		{
			if(files!=null){
				this.mFiles.clear();
				this.mFiles.addAll(files);
			}
		
			
		}
		public ArrayList<String> GetFileArray()
		{
			ArrayList<String> array=new ArrayList<String>();
		
			for(String elem : mFiles){  
			   if(elem!=null){
				   array.add(elem);
			   }
			}
			
			return array;
		}
	}
	public void SetFileOpMode(ArrayList<String> file,FileOperationWay Mode)
	{
		SaveParam.SetFileOpMode(mContext,file, Mode);
	}
	
	public FileOperaData GetFileOpMode()
	{
		
		return SaveParam.GetFileOpMode(mContext);
	}

/**
 * 
 * 
 **/
	protected  void SetWizardViewModeByParent()
	{
		 if(mMode==null){
			     this.SetWizardViewMode(WizardViewMode.ModeNormal);
		  }else{
			  if(mMode==WizardViewMode.ModeNormal){
				  this.SetWizardViewMode(WizardViewMode.ModeNormal);
			  }else if(mMode==WizardViewMode.ModeWizard){
				  this.SetWizardViewMode(WizardViewMode.ModeWizard);
			  }else{
				  this.SetWizardViewMode(WizardViewMode.ModeNormal);
			  }
		  }
	}
/**
 * 
 * 
 **/
	protected void InitNormalWizardViewMode()
	{
		View v_w=this.findViewById(R.id.layout_mode_wizard);
		View v_n=this.findViewById(R.id.layout_mode_normal);
		if(v_w!=null)	v_w.setVisibility(View.GONE);
		if(v_n!=null)   v_n.setVisibility(View.VISIBLE);
	}
/**
 * 
 * 
 **/
	protected void InitWizardWizardViewMode()
	{
		View v_w=this.findViewById(R.id.layout_mode_wizard);
		View v_n=this.findViewById(R.id.layout_mode_normal);
		if(v_n!=null) v_n.setVisibility(View.GONE);
		if(v_w!=null) v_w.setVisibility(View.VISIBLE);
	}
/**
 * 
 * 
 **/
	public final WizardViewMode GetWizardViewMode()
	{
		return SaveParam.GetWizardViewMode(mContext);
	}
/**
 * 
 * 
 **/
	public final boolean SetWizardViewMode(WizardViewMode vm)
	{
		return SaveParam.SetWizardViewMode(mContext,vm);
	}
/**
 * 
 * 
 **/
	public final void InitWizardViewMode()
	{
		mMode=this.GetWizardViewMode();
		if(mMode==null){
			    this.InitNormalWizardViewMode();
		}else{
			if(mMode==WizardViewMode.ModeNormal){
				this.InitNormalWizardViewMode();
			}else if(mMode==WizardViewMode.ModeWizard){
				this.InitWizardWizardViewMode();
			}else{
				this.InitNormalWizardViewMode();
			}
		}
	}

/**
 * 
 * 
 **/
	public void SetOrderGeneratedMode(OrderGeneratedMode mode)
	{
		SaveParam.SetOrderGeneratedMode(mContext, mode);
	}
	public OrderGeneratedMode GetOrderGeneratedMode()
	{
		return SaveParam.GetOrderGeneratedMode(mContext);
	}
	
	public void SetOrderListStartdMode(OrderListStartMode mode)
	{
		SaveParam.SetOrderListStartdMode(mContext, mode);
	}
	public OrderListStartMode GetOrderListStartdMode()
	{
		
		return SaveParam.GetOrderListStartdMode(mContext);
	}
	public void DestoryChild()
	{
		
	}
	private void DestoryParent()
	{
		LibCui.recycleViewImages(this.mView);		
	}
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.Destorry();		
		this.DestoryChild();
		this.DestoryParent();
		
	}
	private void Destorry()
	{
		this.mThreadRun=false;
		mNetPhonePc=null;
		
		mMsgService=null;
		if(this.mConnection!=null){
		   this.mContext.unbindService(mConnection);
		   this.mConnection=null;
		}
		this.mContext=null;
		this.mHandler=null;
		
		if(this.mBaiduLocation!=null){
			this.mBaiduLocation.Destory();
		}
		
	}
	public void onResume()
	{
	
	}
	public void onPause()
	{
	
	}
	private void StratNetSetvices()
	{
		  Intent bindIntent = new Intent(this.mContext, Service_Net.class);  
		     
		  this.mContext.bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE); 
	}
@Override
	public void onServiceDisconnected(ComponentName name) {
		Toast.makeText(this.mContext,"onServiceDisconnected,资源不足", Toast.LENGTH_LONG).show();
	}
	
@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		//返回一个MsgService对象
		mMsgService = ((Service_Net.MsgBinder)service).getService();
		this.mNetPhonePc=this.mMsgService.mNetPhonePC;
		//this.mNetPrints=this.mMsgService.mNetPrints;
		
	}
public Intent getIntent(){
	return ((Activity)(this.mContext)).getIntent();
}
public final View findViewById(int id)
{
	View v=this.mView.findViewById(id);
	return v;
}
public final Context getApplicationContext()
{
  return this.mContext.getApplicationContext();
}
public final void finish() 
{
	
	((Activity)(mContext)).finish();
	
}
public final void finish(int resultCode) 
{
	 SetResult(resultCode);
	 finish();
	
}
public final void SetResult(int resultCode)
{
	((Activity)(this.mContext)).setResult(resultCode);
}
public void SetResultBeforeFinish()
{
	
}
/**
 * 
 * 
 * 
 */

protected void WindowsLoadFinish()
{
	 mWindowLoadSuccess=true;
}
/**
 * 
 * 
 * 
 */
protected final void  postDelayed(Runnable runnable, int delayMillis) {
	// TODO Auto-generated method stub
	if(this.IsDestory()){
		
		 return;
	}else{
		if(this.mThreadRun){
		      this.mHandler.postDelayed(runnable, delayMillis);
		}
	}
	
}
/**
 * 
 * 
 * 
 */
protected Handler Handler(){
	return this.mHandler;
}
/**
 * 
 * 
 * 
 */
protected final void postDelay(Runnable runnable) {
	// TODO Auto-generated method stub
	postDelayed(runnable,0);
}
/**
 * 
 * 
 * @return
 */
protected final Context getContext() {
	// TODO Auto-generated method stub
	return this.mContext;
}
protected final  void Toast_make_show_center(final String msg)
{
	Toast_make_show(msg,Toast.LENGTH_SHORT,Gravity.CENTER);	
}
protected final  void Toast_make_show(final String msg,final int dur,final int gravity)
{
	postDelay(new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Toast t=Toast.makeText(mContext,msg,dur);
			t.setGravity(gravity,0,0);
			t.show();
		}});
	
}
public final Resources Res()
{
	return mContext.getResources();
}
protected final void  Toast_make_show(final String msg)
{
	Toast_make_show(msg,Toast.LENGTH_SHORT,0);	
	
}
@SuppressLint("NewApi") public final boolean IsDestory()
{
	Activity activity=((Activity)mContext);
	if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
		try{
			if(activity==null||activity.isDestroyed()){
				return true;
			}
		}catch(Exception e){
			
		}
		
		
	}
	//
	if(activity==null||activity.isFinishing()){
		return true;
	}
	//
	
	return false;
}
/**
 * 
 */
public static String GetSpeedValue(double speed)
{
	 if(speed<1024&&speed>=1){
		 return String.format("%.2f", speed)+"KB/s";
	 }else if(speed>=1024){
		 return String.format("%.2f", speed/1024)+"MB/s";
	 }else if(speed<1){
		 return String.format("%.2f", speed*1024)+"B/s";
	 }else{
		 return "SUCK";
	 }
	
	 
}
/**
 * 
 */
public final void UDP_Broad_Msg_Request()
{
	if(mNetPhonePc!=null){
		mNetPhonePc.UDP_Broadcast_MSG_Request();
		
	}

}
/**
 * 
 * 
 */
protected void  PrintersNowIsUpdata(ArrayList<CloudPrintAddress> Printers)
{
	
}
/**
 * 
 * 
 */
protected void  PhoneNowIsUpdata()
{
	
}
/**
 * 
 * 
 */
protected void  PcNowIsUpdata()
{
	
}
/**
 * 
 */
protected void  WhenOnReceiveLocation(BDLocation location)
{
	if(location!=null&&this.mPrintPointTemp!=null){
		this.mLocation=location;
		if(this.mPrintPointTemp.mPrintFrom==PrintFrom.cloud&&this.mPrintPointTemp.mPrintType==PrintType.CloudPrint){
			//云文印店家
			
		}else{
			this.mPrintPointTemp.SeBDtLocation(location);
		}
		

	}
}
/**
 * 
 */
public class CheckPrinterListIsUpdata  implements Runnable
{
	public String mCurrentPrintListId=UUID.randomUUID().toString();;
/**
* 
* 查询  NetServer 电脑 手机的列表
* 更新之
* 
**/
	@Override
	public void run()
	{
		while(mThreadRun){
			
			try {
				
				if(mNetPhonePc!=null&&mNetPhonePc.mPCPhonePrinterList!=null) {
					String printerListID=mNetPhonePc.mPCPhonePrinterList.GetPrinterListId();
					if(!mCurrentPrintListId.equals(printerListID)){
						//列表有更新
						postDelay(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								PrintersNowIsUpdata(null);		
							}});
									
						mCurrentPrintListId=printerListID;
					}
				}						
		
				
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
/**
 * 
 */
public class CheckPcPhoneListIsUpdata  implements Runnable
{
	public String mCurrentPhoneListId=UUID.randomUUID().toString();;
	public String mCurrentPCListId=UUID.randomUUID().toString();
	public String mCurrentPCPhoneList=UUID.randomUUID().toString();
    public boolean IsParamValid()
    {
    	if((mNetPhonePc==null)){
			return false;
    	}else{
			return true;
    	}

    }
    @Override
	public void run()
	{
		Looper.prepare();
		while(mThreadRun){
			try {
				
				 Updata();
				Thread.sleep(300);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Looper.loop();
	}
	
	public void Updata()
	{
		if(IsParamValid()==false)		return;
		
	
		if((mNetPhonePc!=null)&&
		   (!mNetPhonePc.mPCPhonePrinterList.getPCPhoneListId().equals(mCurrentPCPhoneList))){
			//列表有更新--PC--phone 有更新
			postDelay(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					PhoneNowIsUpdata();	
					PcNowIsUpdata();
				}
				
			});
			/*---------------------------------------------------*/
			mCurrentPCPhoneList=mNetPhonePc.mPCPhonePrinterList.getPCPhoneListId();
		}
		
		if((mNetPhonePc!=null)
				&&(!mNetPhonePc.mPCPhonePrinterList.GetPcListId().equals(mCurrentPCListId))){
			//列表有更新
			postDelay(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					PcNowIsUpdata();
				}
				
			});

			/*---------------------------------------------------*/
			mCurrentPCListId=mNetPhonePc.mPCPhonePrinterList.GetPcListId();
		}
		if((mNetPhonePc!=null)
				&&(!mNetPhonePc.mPCPhonePrinterList.getPhoneListId().equals(mCurrentPhoneListId))){
			postDelay(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					PhoneNowIsUpdata();	
				}
				
			});

			/*---------------------------------------------------*/
			mCurrentPhoneListId=mNetPhonePc.mPCPhonePrinterList.getPhoneListId();
		}
		
	}
}
/**
 * 
 */
public class MyLocationListenner implements BDLocationListener {

	@Override
	public void onReceiveLocation(BDLocation location) {
		mLocation=location;
		WhenOnReceiveLocation(location);
	}
}
/**
 * 
 */
protected void ActivityResult(int requestCode, int resultCode, Intent data) 
{
      // TODO Auto-generated method stub
        if(resultCode ==View_CloudPrintTemplate.ResultCode_OrderIsSuccess) {
           finish();
       }
}
/**
 * 
 */
public void RequestNetDiskForder()
{
	try{
		//请求云盘目录
		mMsgService.mUICloudList.RequestDiskForder();
	}catch(Exception e){
		
	}
}
/**
 * 
 * 
 * 
 * 
 **/	
}