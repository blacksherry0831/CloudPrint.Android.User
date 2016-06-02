package com.jit.cloud_print_cc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;



import com.jit.cloud_print_cc.R;
import com.jit.config.KEY;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class View_DocumentManagement  extends View_CloudPrintTemplate
{
	
	private GridView mGridView;	

	private final static int mLayoutId =R.layout.activity_document_management;
	
	private final int[] mFileSelectItem=new int[]{
			        R.string.dm_file_excel,
			      	R.string.dm_file_word,				
					R.string.dm_file_pdf,
					R.string.dm_file_image,
					R.string.dm_file_my_net,
					R.string.dm_file_my_phone,
					R.string.dm_file_my_photo,
					R.string.dm_file_my_qq
					};
	public  TextView   mTextView;
	public  ImageView  mImageView;

	
public View_DocumentManagement (Context context)
  {
	 super(context,mLayoutId);	
	 this.InitChildView();
  }
/*
public f_s_DocumentManagementView(Context context, PhoneInHand   PhoneInHand)
{
	  super(context,mLayoutId,PhoneInHand);	
	  this.InitChildView();
}*/
private void InitChildView()
{
	this.mTextView=(TextView) findViewById(R.id.textview_oe_title);
	this.mImageView=(ImageView) findViewById(R.id.imageView_oe_title);
	
	this.mGridView=(GridView) findViewById(R.id.id_dm_list_item_File_type_select);
	
	this.InitGridView();
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
	}
/**
 * 
 * 
 **/
@Override
	protected void InitWizardWizardViewMode()
	{
		super.InitWizardWizardViewMode();
		this.mImageView.setVisibility(View.GONE);
		this.mTextView.setText("第一步：选择要打印的文件");
	}
/**
 * 
 * 
 */
@Override
public void DestoryChild()
{
	  this.mGridView.setAdapter(null);
	  this.mGridView.setOnItemClickListener(null); 
	 // LibCui.recycleImageView(mImageView);
		mGridView=null;	
		mTextView=null;
		mImageView=null;
}
/**
 * 
 * 
 */
private void InitGridView()
{
	
	  //生成动态数组，并且转入数据
    ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
    
    for(int i=0;i<mFileSelectItem.length;i++)
    {
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	map.put("ItemImage", R.drawable.dm_file_select_background);//添加图像资源的ID
		map.put("ItemText", Res().getString(mFileSelectItem[i]));//按序号做ItemText
        	lstImageItem.add(map);
        }

 
        SimpleAdapter saImageItems=new SimpleAdapter(
        		getContext(),
        		lstImageItem,
        		R.layout.document_mananager_file_select_item,
        		new String[] {"ItemImage","ItemText"},
    		new int[] {R.id.id_dm_item_flie_imageView,R.id.id_dm_item_file_textView});

   this.mGridView.setAdapter(saImageItems);
   this.mGridView.setOnItemClickListener(new GridItemClickListener()); 
}

public void StartActivityView( Class<?> cls,String view_title,String [] file_ext,String Type){
	  
	  this.SetWizardViewModeByParent();
      Bundle bundle = new Bundle();
      bundle.putString(KEY.F_KEY_TITLE, view_title);
      bundle.putString(KEY.F_KEY_TYPE,Type);
      if(file_ext!=null)
    	  bundle.putCharSequenceArray(KEY.F_KEY_ETX,file_ext);
      this.StartActivityView(cls, bundle);
}
   //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
class  GridItemClickListener implements OnItemClickListener
    {
		@Override
		public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened 
				                          View arg1,//The view within the AdapterView that was clicked
				                          int arg2,//The position of the view in the adapter
				                          long arg3//The row id of the item that was clicked
				                          ) {
			
			HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
			
			
			String file_select=(String) item.get("ItemText");
			
			
			
			if(file_select.equals(Res().getString(R.string.dm_file_image))){
				View_DocumentManagement.this.StartActivityView(
						Activity_FileSelect.class,
						file_select,
						FExtFilter.mImagesExtensions,
						FExtFilter.FILE);
				 
			}else if(file_select.equals(Res().getString(R.string.dm_file_pdf))){
				View_DocumentManagement.this.StartActivityView(
						Activity_FileSelect.class,
						file_select,
						FExtFilter.mPdfExtensions,
						FExtFilter.FILE);				
			}
			else if(file_select.equals(Res().getString(R.string.dm_file_word))){
				View_DocumentManagement.this.StartActivityView(
						Activity_FileSelect.class,
						file_select,
						FExtFilter.mDocExtensions,
						FExtFilter.FILE);
				
			}else if(file_select.equals(Res().getString(R.string.dm_file_excel))){
				View_DocumentManagement.this.StartActivityView(
						Activity_FileSelect.class,
						file_select,
						FExtFilter.mExeclExtensions,
						FExtFilter.FILE);
				
			}else if(file_select.equals(Res().getString(R.string.dm_file_my_phone))){
				View_DocumentManagement.this.StartActivityView(
						Activity_FileSelect.class,
						file_select,
						null,
						 FExtFilter.FORDER_PHONE);
			}else if(file_select.equals(Res().getString(R.string.dm_file_my_photo))){
				View_DocumentManagement.this.StartActivityView(
						Activity_FileSelect.class,
						file_select,
						null,
						FExtFilter.FORDER_PHOTO);
			}
			else if(file_select.equals(Res().getString(R.string.dm_file_my_qq))){
				
				File qqf=LibCui.GetMyQQDirectory();
				if(qqf!=null){
					View_DocumentManagement.this.StartActivityView(
						Activity_FileSelect.class,
						file_select,
						null,
						 FExtFilter.FORDER_QQ);
				}else{
					arg1.setVisibility(View.INVISIBLE);
				}
				
			
			
			}else if(file_select.equals(Res().getString(R.string.dm_file_my_net))){
				View_DocumentManagement.this.StartActivityView(
						Activity_FileSelect_NetDisk.class,
						file_select,
						null,
						FExtFilter.FORDER_NET);
			}else{				
				//显示所选Item的ItemText
				Toast.makeText(getContext(),(String)item.get("ItemText"),Toast.LENGTH_SHORT).show();
			}
		}
    	
    }
}
