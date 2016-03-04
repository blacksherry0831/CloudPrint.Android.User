package com.jit.cloud_print_cc;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

public class FileOperations_Select {
	public final static  int GridLayoutID=R.layout.efp__grid_item;
	public final static  int ListViewLayoutID=R.layout.efp__list_item;
	protected boolean mIsThreadRun=true;
	protected boolean mIsMultiChoice = false;
	protected boolean mOptOnlyOneItem = false;	
	protected GridView mGridView;
	protected ListView mListView;
	protected AbsListView mAbsListView;
	protected Context mContext;
	protected ArrayList<String> mSelected = new ArrayList<String>();
	protected ArrayList<String> mHideFile = new ArrayList<String>();
	public FileOperations_Select()
	{
		
	}
	protected void refreshCurrentViewData()
	{
		
	}
	public boolean ProcessBackKey(KeyEvent event)
	{
		return true;
	}
	public ArrayList<String> GetSelectedFile()
	{
		return mSelected;
	}
	/**
	 * 
	 * 
	 * 
	 * @param Selected 选中的文件
	 */
	public int GetSelectedCount()
	{
		 return this.mSelected.size();
	}
	/**
	 * 
	 */
	public void DeleteItem(ArrayList<String> file)
	{
		 mIsMultiChoice=false;
		  for(String filefullpath :file)
		  {
		     File f=new File(filefullpath);	
		     if(f.exists()){
		    	 f.delete();
		     }
			  
		  }
		  file.clear();
		  this.NotifyListChanged();
	}
/**
 *
 *
 */
	public void DeleteSelectedItem()
	{
		 mIsMultiChoice=false;
		  for(String filefullpath :mSelected)
		  {
		     File f=new File(filefullpath);	
		     if(f.exists()){
		    	 f.delete();
		     }
			  
		  }
		  mSelected.clear();
		  this.NotifyListChanged();
	}
	/**
	 * 
	 * 
	 * 
	 * @param Selected 选中的文件
	 */
	public void AddSelectedItem(ArrayList<String> Selected)
	{
		mIsMultiChoice=true;
		if(Selected!=null){
			for(String file :Selected){
				this.mSelected.add(file);
			}
		}
		this.refreshCurrentViewData();
		this.NotifyListChanged();
		
	}
	/**
	 * 
	 * 
	 */
	public void NotifyListChanged()
	{
		this.refreshCurrentViewData();
		if(mAbsListView!=null){
			((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();
		}
	}
	/**
	 * 
	 * 
	 * 
	 * @param Selected 选中的文件
	 */
	public void SetHideItem(ArrayList<String> Selected)
	{
		if(Selected!=null){
			for(String file :Selected){
				this.mHideFile.add(file);
			}
		}
	}
	/**
	 * 
	 * 
	 * 
	 * @param Selected 选中的文件
	 */
	public void ClearHideItem()
	{
		
			this.mHideFile.clear();
		
	}

	public void Destory()
	{
		mIsThreadRun=false;
		mGridView=null;
		mListView=null;
		if(mAbsListView!=null){
			mAbsListView.setOnItemClickListener(null);
			mAbsListView=null;
		}
		
		mContext=null;
	}
	public void addDataAll(ArrayList<String> src)
	{
		this.mSelected.clear();
		this.mHideFile.clear();
	}
	
	public void addDataAllNet(ArrayList<UserInfoNetFile> src)
	{
		this.mSelected.clear();
		this.mHideFile.clear();
	}
	
	protected void disableMultiChoice() 
	{
		/*showSecondHeader(false);*/
		mIsMultiChoice = false;
		mSelected.clear();
		/*if (mOptChoiceType == ExFilePicker.CHOICE_TYPE_FILES && !mOptOnlyOneItem) {
			readDirectory(mCurrentDirectory);
		}
		((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();*/
	}
}
