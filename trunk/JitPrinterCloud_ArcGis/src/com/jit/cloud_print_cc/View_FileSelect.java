package com.jit.cloud_print_cc;

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.example.LeftRightHand.LeftRightGestureDetector.PhoneInHand;
import com.jit.cloud_print_cc.KEY.FileOperationWay;
import com.jit.cloud_print_cc.R;


@SuppressWarnings("unused")
public class View_FileSelect extends View_CloudPrintTemplate{
	
	public final static int mDialogId = R.layout.alert_dialog_file_rcv;
	//private final static int mDialogId = R.layout.alert_dialog_file_send;
	private final static int mLayoutId = R.layout.activity_file_select_manage;
	/*------------------------------------------------------*/
	private GridView mGridView;	
	private ListView mListView;
	private TextView mTextTitle;
    public  FileOperations_Select mFileSelect;
	public  Button mButtonTransfer;
	public  Button mButtonPrint;
	public  Button mButtonDeleteFile;
	/*------------------------------------------------------*/
	public final static int mGridViewId=R.id.id_file_select_gridview;
	public final static int mListViewId=R.id.id_file_select_listview;
	public final static int mFileTitleId=R.id.id_file_select_title;
	public final static int mButtonTransferId=R.id.id_button_transfer;
	public final static int mButtonPrintId=R.id.id_button_print;
    public  ShowDialog mShowDialog=null;
	private ArrayList<String> mFileWifiRcv=new ArrayList<String>();
	/*------------------------------------------------------*/
	 // StartPrintPointSelectedView  mStartPrintPointSelectedView;
	/*------------------------------------------------------*/
	public  View_FileSelect (Context context)
	  {		 
		 super(context, mLayoutId);  
		 this.InitChildView();
		 this.InitSetting();		 
	  }
	public  View_FileSelect (Context context,PhoneInHand   PhoneInHand)
	  {
		 super(context, mLayoutId,PhoneInHand); 
		 this.InitChildView();
		 this.InitSetting();
	  }
	public void InitSetting()
	{
		Bundle extras = ((Activity)getContext()).getIntent().getExtras();

		if (extras != null) {
			String title=extras.getString(KEY.F_KEY_TITLE);
			if(title!=null){
				this.SetTitle(title);							
			}else{
				this.SetTitle("文件头-未传入");
			}
			String type=extras.getString(KEY.F_KEY_TYPE);
			if(type!=null){
				
				if(type.equals(FExtFilter.FILE)){
					
					String [] fileext=extras.getStringArray("FILEETX");
					if(title!=null){						
						this.BingingGridView_FlieTypeSelect(fileext);									
					}
					
				}else if(type.equals(FExtFilter.FORDER)){
					    this.BingingGridView_ForderSelect();
				}else if(type.equals(FExtFilter.FORDER_PHONE)){
				    this.BingingGridView_ForderSelect();
			    }else if(type.equals(FExtFilter.FORDER_PHOTO)){
			        this.BingingGridView_ForderSelect(LibCui.GetMyPhotoDirectory());
		        }else if(type.equals(FExtFilter.FORDER_QQ)){
		        	this.BingingGridView_ForderSelect(LibCui.GetMyQQDirectory());
		        }else if(type.equals(FExtFilter.FORDER_NET)){
		        	this.BingingGridView_ForderSelect(FExtFilter.FORDER_NET);
				}else{
					
				}
							
			}else{
				this.SetTitle("BingingGridView 未绑定");
			}
		}
	}
	public void SetTitle(String title)
	{
		this.mTextTitle.setText(title);
	}
	@Override
	public void DestoryChild()
	{
		mGridView.setAdapter(null);
		mGridView.setOnItemClickListener(null);
		mGridView=null;	
		mListView=null;
		mTextTitle=null;
		if(mFileSelect!=null){
			this.mFileSelect.Destory();
			mFileSelect=null;
		}
	    
	    mShowDialog=null;
	    mFileWifiRcv=null;
	    mButtonTransfer=null;
		mButtonPrint=null;
		mButtonDeleteFile=null;
		mShowDialog=null;
	}
	public void BingingGridView_FlieTypeSelect(String [] ext)
	{
	   FileOperations_TypeSelect mFileOperations=new FileOperations_TypeSelect(getContext(), mGridView, mListView);
	   mFileOperations.mfileUtilsCui.SetExt(ext);
	   mFileSelect= mFileOperations;
	   this.InitButtonClick();
	}
	public void SupportDialog()
	{
		  mShowDialog=new ShowDialog();
	}
	/*
	public void AddSelectedItem(ArrayList<String> Selected)
	{
		mFileSelect.AddSelectedItem(Selected);
	}*/
	/**
	 * 
	 * 
	 * @author Administrator
	 *
	 */
	
	
	/**
	 * 
	 * 
	 */
	public void BingingGridView_ForderSelect()
	{
		
		  FileOperations_ForderSelect mFileOperations=new FileOperations_ForderSelect(getContext(), mGridView, mListView);
		  mFileOperations.SetDefaultTopDirectory();
		  mFileSelect= mFileOperations;
		  this.InitButtonClick();
	}	
	public void BingingGridView_ForderSelect(File f)
	{
		
		  FileOperations_ForderSelect mFileOperations=new FileOperations_ForderSelect(getContext(), mGridView, mListView);
		  mFileOperations.SetDefaultTopDirectory(f);
		  mFileSelect= mFileOperations;
		  this.InitButtonClick();
	}
	public void BingingGridView_ForderSelect(String f)
	{
		
		  FileOperations_ForderSelect mFileOperations=new FileOperations_ForderSelect(getContext(), mGridView, mListView);
		  mFileOperations.SetDefaultTopDirectory(null);
		  mFileSelect= mFileOperations;
		  this.InitButtonClick();
	}
	/**
	 * 
	 * 
	 * 
	 */
	public void InitButtonClick()
	{
		   if(this.mButtomNext!=null){
		    	this.mButtomNext.setOnClickListener(new StartPrintPointSelectedView(this, mFileSelect));
		   }		   
		   this.mButtonPrint.setOnClickListener(new StartPrintPointSelectedView(this, mFileSelect));	
		   this.mButtonTransfer.setOnClickListener(new StartTransferFileButton(this, mFileSelect));
		   if(this.mButtonDeleteFile!=null){
				this.mButtonDeleteFile.setOnClickListener(new StartDeleteFileButton(this,mFileSelect));
		   }
	}
	/**
	 * 
	 * 
	 * 
	 */
	public void InitChildView()
    {

			
			this.mGridView=(GridView) findViewById(View_FileSelect.mGridViewId);
			this.mListView=(ListView) findViewById(View_FileSelect.mListViewId);
			this.mTextTitle=(TextView)findViewById(View_FileSelect.mFileTitleId);			
			this.mButtonPrint=(Button) findViewById(View_FileSelect.mButtonPrintId);		   
			this.mButtonTransfer=(Button) findViewById(View_FileSelect.mButtonTransferId);		
			this.mButtonDeleteFile=(Button) findViewById(R.id.id_button_delete_file);			
			this.InitWizardViewMode();
	}
	/**
	 *启动地点选择 
	 * 
	 */
	public static class  StartPrintPointSelectedView implements View.OnClickListener
	{
       
		final FileOperations_Select _FileSelect;
		View_CloudPrintTemplate _ctx;
		public StartPrintPointSelectedView(View_CloudPrintTemplate ctx,FileOperations_Select fs)
		{
		   this._FileSelect=fs;
		   this._ctx=ctx;
		}
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			//Toast.makeText(getContext(), "print",Toast.LENGTH_SHORT).show();
			/*选择文件后的操作---打印文件模式--选择打印机*/
			if(_FileSelect.GetSelectedCount()==0){
				 Toast t=Toast.makeText(_ctx.getContext(),"未选中文件",Toast.LENGTH_SHORT);
				 t.setGravity(Gravity.CENTER,0,150);
				 t.show();
			}else{
				_ctx.SetFileOpMode(_FileSelect.GetSelectedFile(), FileOperationWay.OPen2SetPrint);
				StartActivityView(_ctx,Activity_PrintSetPrintPoint.class,_FileSelect.GetSelectedFile());
			}
			
		}
		
	}
	/**
	 *delete
	 * 
	 */
	public static class  StartDeleteFileButton implements View.OnClickListener
	{
       
		final FileOperations_Select _FileSelect;
		View_CloudPrintTemplate _ctx;
		public StartDeleteFileButton(View_CloudPrintTemplate ctx,FileOperations_Select fs)
		{
		   this._FileSelect=fs;
		   this._ctx=ctx;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(_FileSelect.GetSelectedCount()==0){
				 Toast.makeText(_ctx.getContext(),"未选中文件",Toast.LENGTH_SHORT).show();
			}else{
				//删除选中文件
				_FileSelect.DeleteSelectedItem();
			}
		}
		
	}
	/**
	 *transfer 
	 * 
	 */
	public static class  StartTransferFileButton implements View.OnClickListener
	{
       
		final FileOperations_Select _FileSelect;
		View_CloudPrintTemplate _ctx;
		public StartTransferFileButton(View_CloudPrintTemplate ctx,FileOperations_Select fs)
		{
		   this._FileSelect=fs;
		   this._ctx=ctx;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//f_s_t_FileSelectView.this.StartActivityView(f_s_t_f_FileTransferView.class);
			//if(this.)
			if(_FileSelect.GetSelectedCount()==0){
				 Toast.makeText(_ctx.getContext(),"未选中文件",Toast.LENGTH_SHORT).show();
			}else{
				/*选择文件后的操作---传输模式--传输打印机*/
				_ctx.SetFileOpMode(_FileSelect.GetSelectedFile(), FileOperationWay.OpenWithFileTransfer);
				StartActivityView(_ctx,Acticity_FileTransfer.class,_FileSelect.GetSelectedFile());	
			}
		
		}
		
	}
	/**
	 * 
	 * @param cls
	 * @param Selected
	 * @return 
	 */
	
	public  static void StartActivityView(View_CloudPrintTemplate vct,Class<?> cls,ArrayList<String> Selected)
	{		
		      vct.UDP_Broad_Msg_Request();
		     /*-将文件写入bundle-*/
			  Bundle bundle=new Bundle();
		     // bundle.putStringArrayList(KEY.K_B_FilesSelected, Selected);
		      /*-选择的文件-*/		      
		      vct.StartActivityView(cls, bundle);		
	}
	
	@Override
	public void showPopupWindow( View parent)
	{
		  showPopupWindow(this.getContext(),parent,this.GetView());
	}
	private   void showPopupWindow(
			 Context context,
			 View parent,
			 View vPopupWindow)
	{	       
		      Rect t=new Rect();
		      parent.getGlobalVisibleRect(t);
		      vPopupWindow.setFocusable(true); // 这个很重要
		      vPopupWindow.setFocusableInTouchMode(true);		    
	        final PopupWindow pw= new PopupWindow(vPopupWindow,t.width(),t.height(),false);  
	        pw.setFocusable(true);
	        pw.setOutsideTouchable(false);
	       // pw.setBackgroundDrawable(new BitmapDrawable());
	        
	         vPopupWindow.setOnKeyListener(new View.OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if(false==mFileSelect.ProcessBackKey(event)){
							 pw.dismiss();
						}			           
			                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
			            return true;
			        }
			        return false;
				}
			});
	        //显示popupWindow对话框  
		        // pw.showAtLocation(parent, gravity, x, y)
		    pw.showAtLocation(parent, Gravity.LEFT|Gravity.TOP,t.left,t.top);  
	}  

	/**
	 * 
	 * 
	 * @author Administrator
	 *
	 */	
	public  class ShowDialog 
	{
		  Dialog  mShowDialog=new Dialog(getContext(),R.style.Translucent_NoTitle);  //先得到构造器;
		  View    mViewDialog=LibCui.getViewFromeXml(mDialogId, getContext());	
	     // boolean mDialogThreadRun=true;
		  ShowDialog()
		  {
				this.InitDialog();
				new Thread(new FileRcv()).start();
			//	new Thread(new ShowDialogWhenFileRcv()).start();
		  }
		  public void RcvFile()
		  {
			  //设置文件选中
			  mFileSelect.AddSelectedItem(mFileWifiRcv);
			//清空队列
			  mFileWifiRcv.clear();
			  
		  }
		  public void DeleteFile()
		  {
			  //删除文件
			  mFileSelect.DeleteItem(mFileWifiRcv);
			  //清空队列
			  mFileWifiRcv.clear();
			  
		  }
		  public void InitDialog()
		  {
			  mShowDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);		
				 TextView tv=(TextView) mViewDialog.findViewById(R.id.textView_alert_msg);
				 if(tv!=null){
					 tv.setText("MSG");
					 tv.setMovementMethod(new ScrollingMovementMethod());
				 }
				 /*--------------------------------------*/
				 Button  bt=(Button) mViewDialog.findViewById(R.id.button_alert_sure);
				 if(bt!=null){
					//bt.setVisibility(View.GONE);
					bt.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							 ShowDialog.this.RcvFile();
							 mShowDialog.cancel();	
						}
					});
				 }	
				 /*--------------------------------------*/
				 Button  bt_delete=(Button) mViewDialog.findViewById(R.id.button_alert_delete);
				 if(bt_delete!=null){
					//bt.setVisibility(View.GONE);
					 bt_delete.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							 ShowDialog.this.DeleteFile();
							 mShowDialog.cancel();	
						}
					});
				 }	
				 /*--------------------------------------*/
				 mShowDialog.setContentView(mViewDialog);
		  }
		  public void Show(){
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
		  private String ShowMsgInDialog(ArrayList<String> stringarray)
		  {
			  String msg="";
			  for(int i=0;i<stringarray.size();i++){
				  msg+=String.valueOf(i)+" : "+stringarray.get(i);
				  msg+="\n";
				  msg+="\n";
			  }
			  return msg;
		  }
		  public void ShowText(ArrayList<String> stringarray)
		  {
			  /*-------参数检查--------------------------------------------------------*/
			  if(stringarray==null||stringarray.size()==0){
				  return;
			  }
			  /*--------设置显示文本---------------------------------------------------*/
			  String txt=ShowMsgInDialog(stringarray);
			  TextView tv=(TextView) mViewDialog.findViewById(R.id.textView_alert_msg);
				 if(tv!=null){
					 if(tv.getText().toString().equals(txt)){
						 
					 }else{
						 tv.setText(txt);
					 }										
				 }
			 /*--------检查显示状态----------------------------------------------------*/
				 if(mShowDialog.isShowing()){
					 //正在显示
				 }else{
					 //还未显示。显示dialog
					 this.Show();
				 }
		    /*--------检查显示状态----------------------------------------------------*/
		  }
/**
 * 
 * 
 * 
 */
		  public class FileRcv  implements Runnable
		  {
			  
				@Override
				  public void run() {
					// TODO Auto-generated method stub
					while(mThreadRun){
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(mNetPhonePc!=null){
						   Queue<String> RcvFile=mNetPhonePc.GetFileRcvQuere();
						   String FileCurrent=RcvFile.poll();
						  
							if(FileCurrent!=null){
								mFileWifiRcv.add(FileCurrent);	
								postDelay(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										ShowText(mFileWifiRcv);
									}});
							}						 
						}
						
					}
				}
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
		
		/*初始化回调方法*/
		/*发送更新*/
		
		/*mMsgService.mUICloudList.setIRcv(new UserInfoOperation_CloudDisk.WhenRcvOrderList() {
			
			@Override
			public void OnRecvData(String result) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mMsgService.mUICloudList.RequestDiskForder();*/
		
	}
/**
 * 
 * 
 * 
 */
}
