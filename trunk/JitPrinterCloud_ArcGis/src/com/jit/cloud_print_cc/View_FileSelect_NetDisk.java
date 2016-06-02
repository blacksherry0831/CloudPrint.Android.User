package com.jit.cloud_print_cc;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.jit.cloud_print_cc.View_FileSelect.StartPrintPointSelectedView;
import com.jit.config.KEY;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class View_FileSelect_NetDisk extends View_CloudPrintTemplate {

	private final static int mLayoutId = R.layout.activity_file_select_net_disk;
	private View mGridView;	
//	private ListView mListView;
	private TextView mTextTitle;
	public  Button mButtonTransfer;
	public  Button mButtonPrint;
	public  Button mButtonDeleteFile;
	public  FileOperations_Select mFileSelect;
	public  View_FileSelect_NetDisk(Context context) {
		  super(context,mLayoutId);
		// TODO Auto-generated constructor stub
		  this.InitChildView();
		  this.InitSetting();
		  mFileSelect=BingingGridView_NetDisk(this.mGridView);
		  this.InitButtonClick();
	}
	/**
	 * 
	 * 
	 * 
	 */
	public static FileOperations_Select BingingGridView_NetDisk(View gv)
	{
		//FileOperations_NetSelect netDisk=
	   return new FileOperations_NetSelect(gv.getContext(),gv);
	    
	}
	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
			
		   super.onServiceConnected(name, service);
		   mMsgService.mUICloudList.setIRcv(new RefashNetDiskData(this,this.mFileSelect,null));
		   RequestNetDiskForder();
		
	}
	/**
	 * 
	 * 
	 * 
	 */
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
						//this.BingingGridView_FlieTypeSelect(fileext);									
					}
					
				}else if(type.equals(FExtFilter.FORDER)){
					   // this.BingingGridView_ForderSelect();
				}else if(type.equals(FExtFilter.FORDER_PHONE)){
				   // this.BingingGridView_ForderSelect();
			    }else if(type.equals(FExtFilter.FORDER_PHOTO)){
			        //this.BingingGridView_ForderSelect(LibCui.GetMyPhotoDirectory());
		        }else if(type.equals(FExtFilter.FORDER_QQ)){
		        	//this.BingingGridView_ForderSelect(LibCui.GetMyQQDirectory());
		        }else if(type.equals(FExtFilter.FORDER_NET)){
		        	//this.BingingGridView_ForderSelect(FExtFilter.FORDER_NET);
				}else{
					
				}
							
			}else{
				this.SetTitle("BingingGridView 未绑定");
			}
		}
	}
/**
 * 
 * 
 * 
 */
	public void SetTitle(String title)
	{
		this.mTextTitle.setText(title);
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
		   this.mButtonTransfer.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
		  });
		   /*if(this.mButtonDeleteFile!=null){
				this.mButtonDeleteFile.setOnClickListener(new StartDeleteFileButton(this,mFileSelect));
		   }*/
		   
		   if(this.mButtonDeleteFile!=null){
				this.mButtonDeleteFile.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(mFileSelect.GetSelectedCount()==0){
							 Toast.makeText(getContext(),"未选中文件",Toast.LENGTH_SHORT).show();
						}else{
							//删除选中文件
							mMsgService.mUICloudList.DeleteFile2Server2Save(mFileSelect.GetSelectedFile());
							mFileSelect.DeleteSelectedItem();
						
						}
					}
				});
			}
	}
/**
 * 
 * 
 * 
 */
	public void InitChildView()
    {

			
			this.mGridView=findViewById(View_FileSelect.mGridViewId);
			if(this.mGridView instanceof PullToRefreshListView ){
			    	this.initListView((PullToRefreshListView)mGridView);
			}
			if(this.mGridView instanceof PullToRefreshGridView){
		    	    this.initGridView((PullToRefreshGridView)mGridView);
		   }
		//	this.mListView=(ListView) mView.findViewById(View_FileSelect.mListViewId);
			this.mTextTitle=(TextView)findViewById(View_FileSelect.mFileTitleId);;
			
			this.mButtonPrint=(Button)findViewById(View_FileSelect.mButtonPrintId);
		   // this.mButtonPrint.setOnClickListener(new StartPrintPointView());	
		    
		    /*if(this.mButtomNext!=null){
		    	this.mButtomNext.setOnClickListener(new StartPrintPointView());
		    }*/
		   
			this.mButtonTransfer=(Button)findViewById(View_FileSelect.mButtonTransferId);
			
			this.mButtonDeleteFile=(Button) findViewById(R.id.id_button_delete_file);
			
			this.InitWizardViewMode();
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
			try{
				RequestNetDiskForder();
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
				mMsgService.mUICloudList.RequestDiskForder();
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
 */
	public  static  class  RefashNetDiskData implements UserInfoOperation_CloudDisk.WhenRcvOrderList
	{		
		public interface CallBack{
			public void RefashData(int file_num,float file_MB);
		}
		CallBack _cb;
		View_CloudPrintTemplate _Ctx;
		FileOperations_Select   _FileSelect;
		private float _file_Use_MB;
		private int _file_num;
		//ArrayList<UserInfoNetFile> _uinf;
		public RefashNetDiskData(View_CloudPrintTemplate Ctx,FileOperations_Select FileSelect,CallBack cb)
		{
			_Ctx=Ctx;
			_FileSelect=FileSelect;
			_cb=cb;
		}
		
		public void CalculateFileSizeFileNumber(ArrayList<UserInfoNetFile> _uinf)
		{
			this._file_num=_uinf.size();
			this._file_Use_MB=0;
			for(UserInfoNetFile uinf : _uinf){
				this._file_Use_MB+=uinf.get_FileSizeFloat();
			}
			if(_cb!=null) _cb.RefashData(_file_num,_file_Use_MB);
			
		}
		@Override
		public void OnRecvData(final ArrayList<UserInfoNetFile> uinfs) {
			// TODO Auto-generated method stub
			_Ctx.postDelay(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					_FileSelect.addDataAllNet(uinfs);	
					CalculateFileSizeFileNumber(uinfs);
				}
			});
			
		
		}
		@Override
		public void OnRecvError(String error) {
			// TODO Auto-generated method stub
			_Ctx.Toast_make_show(error);
		}
		
	}
	/**
	 * 开始打印操作
	 * 
	 **/
	/*public  class StartPrintPointView implements View.OnClickListener
	{
       
		  public  FileOperations_Select mFileSelect;
		StartPrintPointView()
		{
			
		}
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			//Toast.makeText(getContext(), "print",Toast.LENGTH_SHORT).show();
			选择文件后的操作---打印文件模式--选择打印机
			if(mFileSelect.GetSelectedCount()==0){
				 Toast t=Toast.makeText(getContext(),"未选中文件",Toast.LENGTH_SHORT);
				 t.setGravity(Gravity.CENTER,0,150);
				 t.show();
			}else{
				SetFileOpMode(mFileSelect.GetSelectedFile(), FileOperationWay.OPen2SetPrint);
				StartActivityView(Activity_PrintSetPrintPoint.class,mFileSelect.GetSelectedFile());
			}
			
		}
		
	}*/
}
