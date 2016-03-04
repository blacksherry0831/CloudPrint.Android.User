package com.jit.cloud_print_cc;


import com.jit.cloud_print_cc.R;

import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Handler;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
/*
Copyright [yyyy] [name of copyright owner]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
public class FileOperations_TypeSelect extends FileOperations_Select
{
	
	public FileUtilsCui mfileUtilsCui=new FileUtilsCui();


	private int mOptChoiceType;	
	@SuppressWarnings("unused")
	private View mEmptyView;	

//	private ArrayList<File> mFilesList = new ArrayList<File>();
	private boolean  mCurrentView=true;
	public  FileOperations_TypeSelect(Context context, GridView gv,ListView lv)
	{
		super();
		this.mContext=context;
		this.mGridView=gv;
		this.mListView=lv;
		mEmptyView=LibCui.getViewFromeXml(R.layout.efp__empty, mContext.getApplicationContext());
		this.init(null);
		
		
	}

	class FilesListAdapter extends BaseAdapter 
	{
		private Context mContext_t;
		private int mResource_t;
		private boolean mIsUpdataFile=true;
	//	private  Timer timer = new Timer();  
	//	private TimerTask task; 
		private Handler mHandler = new Handler();  
		public FilesListAdapter(Context context, int resource) {
			this.mContext_t = context;
			this.mResource_t = resource;
			this.mIsUpdataFile=true;			
		/*	this.task=new UpdataFileTimerTask();
			this.timer = new Timer();  
			this.timer.schedule(task, 300);  */
			 mHandler = new Handler();  
			 mHandler.postDelayed(new UpdataFile(),10);
			
		}
		
		@Override
		public int getCount() {
			return mfileUtilsCui.getCount();
		}

		@Override
		public Object getItem(int position) {
			return mfileUtilsCui.getItem(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		 @Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			File file =(File) mfileUtilsCui.getItem(position);
            
			//if(convertView==null)
			{
				convertView = LayoutInflater.from(mContext_t).inflate(mResource_t, parent, false);
			}
			
			ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

			CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			if (mSelected.contains(file.getAbsolutePath())) {
				checkbox.setChecked(true);
				setItemBackground(convertView, true);
			} else {
				checkbox.setChecked(false);
				setItemBackground(convertView, false);
			}
			if (mIsMultiChoice){
				checkbox.setVisibility(View.VISIBLE);
			}else{
				checkbox.setVisibility(View.INVISIBLE);
			}
			

			FileOperations.SetFileItemBackground(file,thumbnail);

			TextView filename = (TextView) convertView.findViewById(R.id.filename);
			filename.setText(file.getName());

			TextView filesize = (TextView) convertView.findViewById(R.id.filesize);
			if (filesize != null) {
				if (file.isFile()) filesize.setText(getHumanFileSize(file.length()));
				else filesize.setText("");
			}

			return convertView;
		}
	
		
		 public class CropSquareTransformation implements Transformation {
			 
			  @Override public Bitmap transform(Bitmap source) {
			 
			    int size = Math.min(source.getWidth(), source.getHeight());
			 
			    int x = (source.getWidth() - size) / 2;
			 
			    int y = (source.getHeight() - size) / 2;
			 
			    Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
			 
			    if (result != source) {
			 
			      source.recycle();
			 
			    }
			 
			    return result;
			 
			  }
			 

			  @Override public String key() { return "square()"; }
			 
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
	
	class UpdataFile implements Runnable{
		private int oldSize;
		private int newSize;
		private int count;
		private int delayTime=10;
		@Override
		public void run (){
							if(mIsUpdataFile&&mIsThreadRun){				
														if(delayTime<500){
															delayTime++;
														}
														mHandler.postDelayed(this, delayTime); 
														count++;
														if(count%3==1){
															 oldSize=getCount();
														}
														if(count%3==2){
															 newSize=getCount();
														}        
												            
											            if(oldSize!=newSize){
											            	notifyDataSetChanged();
											            }					
								}
				
			    }
		}
		
	
}
	private void setAbsListViewLayout()
	{
		 FilesListAdapter adapter;
		 AbsListView AbsListView_new=null;
		 AbsListView AbsListView_old=this.mAbsListView;
		 
		if(mCurrentView){
			AbsListView_new=this.mGridView;
			adapter=new FilesListAdapter(mContext,GridLayoutID);
		}else{
			AbsListView_new=this.mListView;
			adapter=new FilesListAdapter(mContext, ListViewLayoutID);
		}
		/**
		 *show new hide old 
		 */
		if(AbsListView_new!=null){
			AbsListView_new.setAdapter(adapter);			
			
			AbsListView_new.setVisibility(View.VISIBLE);
			this.mAbsListView=AbsListView_new;
		}
		if(AbsListView_old!=null){
			AbsListView_old.setVisibility(View.GONE);
		}
		
		mCurrentView=!mCurrentView;
	}
	private void setItemBackground(View view, boolean state) {
		
	}
	private void setAbsListView() {		
		this.setAbsListViewLayout();		
		mAbsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position <mfileUtilsCui.getCount()) {
					File file =(File) mfileUtilsCui.getItem(position);
					if (mIsMultiChoice) {
						CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
						if (checkBox.isChecked()) {
							checkBox.setChecked(false);
							setItemBackground(view, false);
							mSelected.remove(file.getAbsolutePath());
						} else {
							if (mOptOnlyOneItem) {
								mSelected.clear();
								((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();
							}
							checkBox.setChecked(true);
							setItemBackground(view, true);
							mSelected.add(file.getAbsolutePath());
						}
					} else {
						if (file.isDirectory()) {
							mIsMultiChoice=false;
							//int currentPosition = mAbsListView.getFirstVisiblePosition();
							mfileUtilsCui.SetDirectory(file);
							readDirectory(null);
							updateTitle();
							mAbsListView.setSelection(0);
						} else {
							mIsMultiChoice=true;
							mSelected.add(file.getAbsolutePath());
							
						}
						((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();
					}
				}
				
				mAbsListView.setVisibility(View.VISIBLE);
			}
		});

		if (mOptChoiceType != ExFilePicker.CHOICE_TYPE_FILES || !mOptOnlyOneItem) {
			mAbsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					if (!mIsMultiChoice) {
						mIsMultiChoice = true;
						if (position <mfileUtilsCui.getCount()) {
							File file =(File) mfileUtilsCui.getItem(position);
							if (mOptChoiceType != ExFilePicker.CHOICE_TYPE_FILES || file.isFile()) mSelected.add(file.getAbsolutePath());
						}

						if (mOptChoiceType == ExFilePicker.CHOICE_TYPE_FILES && !mOptOnlyOneItem) {
							ArrayList<File> tmpList = new ArrayList<File>();
							for (int i = 0; i <mfileUtilsCui.getCount(); i++) {
								File file =(File) mfileUtilsCui.getItem(position);
								if (file.isFile()) tmpList.add(file);
							}
							//mFilesList = tmpList;
						}

						((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();

						//showSecondHeader(true);
						return true;
					}
					return false;
				}
			});
		}
		//findViewById(curView).setVisibility(View.GONE);
		mAbsListView.setVisibility(View.VISIBLE);
	}
	private void readDirectory(File path) {
	
	}
	

	
	private void sort() {
		
		((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();
	}
	public void init(Intent intent)
	{
          
            
			setAbsListView();			
			
			readDirectory(null);
			
	}


	@Override
	public boolean ProcessBackKey (KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_UP) {
				if (mIsMultiChoice) {
					disableMultiChoice();
				} else {
					boolean Flag_return=mfileUtilsCui.Return2ParentDirectory();
					if (Flag_return == true) {
						complete(null);
						return false;
					} else {
						((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();
						readDirectory(null);
						/*
						String path = mCurrentDirectory.getAbsolutePath();
						if (mListPositioins.containsKey(path)) {
							mAbsListView.setSelection(mListPositioins.get(path));
							mListPositioins.remove(path);
						}*/

						updateTitle();
					}
				}
			} else if (event.getAction() == KeyEvent.ACTION_DOWN 
					&& (event.getFlags() & KeyEvent.FLAG_LONG_PRESS) == KeyEvent.FLAG_LONG_PRESS) {
				mSelected.clear();
				 complete(null);
			}
			return true;
		}
		return true;
	}
	@Override
	protected void disableMultiChoice() {
		showSecondHeader(false);
		mIsMultiChoice = false;
		mSelected.clear();
		if (mOptChoiceType == ExFilePicker.CHOICE_TYPE_FILES && !mOptOnlyOneItem) {
			readDirectory(null);
		}
		((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();
	}

	private void showSecondHeader(boolean show) {
		
	}
	private void updateTitle() {
	  //String FileName=mCurrentDirectory.getName();
	}
	@Override
	public void Destory()
	{
		super.Destory();
		if(mfileUtilsCui!=null){
			mfileUtilsCui.Destory();
			mfileUtilsCui=null;
		}
	
		mEmptyView=null;
		
		/*if(mFilesList!=null){
			mFilesList.clear();
			mFilesList=null;
		}*/
		
	}
	private void complete(Object object) {
		/*
		if (object == null) {
			String path = mCurrentDirectory.getAbsolutePath();
			if (!path.endsWith("/")) path += "/";
			object = new SelectFile(path, mSelected, mSelected.size());
		}*/
		//Intent intent = new Intent();
		//intent.putExtra(ExFilePickerParcelObject.class.getCanonicalName(), object);
		//setResult(RESULT_OK, intent);
		//finish();
	}
	@Override
	public void DeleteSelectedItem()
	{
		mIsMultiChoice=false;
		  for(String filefullpath :mSelected)
		  {
		     File f=new File(filefullpath);	
		     if(f.exists()){
		    	 f.delete();
		    	 mfileUtilsCui.DeleteFile(f);
		    	 LibCui.PlayMusic(this.mContext,R.raw.file_delete);
		     }
			  
		  }
		  mSelected.clear();
		  this.NotifyListChanged();
	 	
	}
	
	public class SelectFile{
		public String path="";
		public ArrayList<String> names=new ArrayList<String>();
		public int count=0;
		public SelectFile(String path, ArrayList<String> names, int count) {
			this.path=path;
			this.names=names;
			this.count=count;
		}
	}
	/*
	 * 
	 */
 
}
