package com.jit.cloud_print_cc;

import java.io.File;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;

import android.os.Build;
import android.os.Environment;


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
import com.jit.cloud_print_cc.R;




public class FileOperations_ForderSelect  extends FileOperations_Select
{
	

	private List<String> mOptFilterExclude;
	private List<String> mOptFilterListed;
	private int mOptChoiceType;
	private int mOptSortType;	
	
	private ArrayList<File> mFilesList = new ArrayList<File>();

	private HashMap<String, Integer> mListPositioins = new HashMap<String, Integer>();
	private File mCurrentDirectory;
	private File mTopCurrentDirectory;


	@SuppressWarnings("unused")
	private View mEmptyView;
	
	
	private boolean  mCurrentView=true;
	public FileOperations_ForderSelect(Context context, GridView gv,ListView lv)
	{
		super();
		this.mContext=context;
		this.mGridView=gv;
		this.mListView=lv;
		this.mTopCurrentDirectory=null;
		mEmptyView=LibCui.getViewFromeXml(R.layout.efp__empty, mContext);
		this.init(null);
		
		
	}	
	private void setAbsListViewLayout()
	{
		 FilesListAdapter adapter;
		 AbsListView AbsListView_new=null;
		 AbsListView AbsListView_old=this.mAbsListView;
		 
		if(mCurrentView){
			AbsListView_new=this.mGridView;
			adapter=new FilesListAdapter(mContext,R.layout.efp__grid_item);
		}else{
			AbsListView_new=this.mListView;
			adapter=new FilesListAdapter(mContext,R.layout.efp__list_item);
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
		//view.setBackgroundResource(state ? attrToResId(R.attr.efp__selected_item_background) : 0);
	}
	private void setAbsListView() {		
		this.setAbsListViewLayout();		
		mAbsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position < mFilesList.size()) {
					File file = mFilesList.get(position);
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
							int currentPosition = mAbsListView.getFirstVisiblePosition();
							mListPositioins.put(mCurrentDirectory.getAbsolutePath(), currentPosition);
							readDirectory(file);
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
						if (position < mFilesList.size()) {
							File file = mFilesList.get(position);
							if (mOptChoiceType != ExFilePicker.CHOICE_TYPE_FILES || file.isFile()) mSelected.add(file.getAbsolutePath());
						}

						if (mOptChoiceType == ExFilePicker.CHOICE_TYPE_FILES && !mOptOnlyOneItem) {
							ArrayList<File> tmpList = new ArrayList<File>();
							for (int i = 0; i < mFilesList.size(); i++) {
								File file = mFilesList.get(i);
								if (file.isFile()) tmpList.add(file);
							}
							mFilesList = tmpList;
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
		if (path == null) {
			path = new File("/");
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
				path = Environment.getExternalStorageDirectory();
		}
		mCurrentDirectory = path;
		mFilesList.clear();
		File[] files = path.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (mOptChoiceType == ExFilePicker.CHOICE_TYPE_DIRECTORIES && !files[i].isDirectory()) continue;
				if (files[i].isFile()) {
					String extension = getFileExtension(files[i].getName());
					if (mOptFilterListed != null && !mOptFilterListed.contains(extension)) continue;
					if (mOptFilterExclude != null && mOptFilterExclude.contains(extension)) continue;
				}
				//忽略隐藏文件
				if(!files[i].getName().startsWith(".")){
					mFilesList.add(files[i]);
				}

			}
		}

		sort();
	}
	
	private void readDirectory_with_filter(File path) {
		if (path == null) {
			path = new File("/");
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
				path = Environment.getExternalStorageDirectory();
		}
		mCurrentDirectory = path;
		mFilesList.clear();
		File[] files = path.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (mOptChoiceType == ExFilePicker.CHOICE_TYPE_DIRECTORIES && !files[i].isDirectory()) continue;
				if (files[i].isFile()) {
					String extension = getFileExtension(files[i].getName());
					if (mOptFilterListed != null && !mOptFilterListed.contains(extension)) continue;
					if (mOptFilterExclude != null && mOptFilterExclude.contains(extension)) continue;
				}
				mFilesList.add(files[i]);
			}
		}

		sort();
	}
	private String getFileExtension(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index == -1) return "";
		return fileName.substring(index + 1, fileName.length()).toLowerCase(Locale.getDefault());
	}
	private void sort() {
		Collections.sort(mFilesList, new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				boolean isDirectory1 = file1.isDirectory();
				boolean isDirectory2 = file2.isDirectory();
				if (isDirectory1 && !isDirectory2) return -1;
				if (!isDirectory1 && isDirectory2) return 1;
				switch (mOptSortType) {
				case ExFilePicker.SORT_NAME_DESC:
					return file2.getName().toLowerCase(Locale.getDefault()).compareTo(file1.getName().toLowerCase(Locale.getDefault()));
				case ExFilePicker.SORT_SIZE_ASC:
					return Long.valueOf(file1.length()).compareTo(Long.valueOf(file2.length()));
				case ExFilePicker.SORT_SIZE_DESC:
					return Long.valueOf(file2.length()).compareTo(Long.valueOf(file1.length()));
				case ExFilePicker.SORT_DATE_ASC:
					return Long.valueOf(file1.lastModified()).compareTo(Long.valueOf(file2.lastModified()));
				case ExFilePicker.SORT_DATE_DESC:
					return Long.valueOf(file2.lastModified()).compareTo(Long.valueOf(file1.lastModified()));
				}
				// Default, ExFilePicker.SORT_NAME_ASC
				return file1.getName().toLowerCase(Locale.getDefault()).compareTo(file2.getName().toLowerCase(Locale.getDefault()));
			}
		});
		((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();
	}
	public void init(Intent intent)
	{
           
			setAbsListView();			
			
			readDirectory(null);
			
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
		    	 mFilesList.remove(new File(filefullpath));
		    	 LibCui.PlayMusic(this.mContext,R.raw.file_delete);
		     }
			  
		  }
		  mSelected.clear();
		  this.NotifyListChanged();
	}
 	
	class FilesListAdapter extends BaseAdapter 
	{
		private Context mContext_t;
		private int mResource_t;

		public FilesListAdapter(Context context, int resource) {
			mContext_t = context;
			mResource_t = resource;
		}

		@Override
		public int getCount() {
			return mFilesList.size();
		}

		@Override
		public Object getItem(int position) {
			return mFilesList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		 @Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			File file = mFilesList.get(position);

			convertView = LayoutInflater.from(mContext_t).inflate(mResource_t, parent, false);
			ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

			CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			if (mSelected.contains(file.getAbsolutePath())) {
				checkbox.setChecked(true);
				setItemBackground(convertView, true);
			} else {
				checkbox.setChecked(false);
				setItemBackground(convertView, false);
			}
			if (mIsMultiChoice) checkbox.setVisibility(View.VISIBLE);

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
		
		

		String getHumanFileSize(long size) {
			String[] units =mContext_t.getResources().getStringArray(R.array.efp__size_units);
			for (int i = units.length - 1; i >= 0; i--) {
				if (size >= Math.pow(1024, i)) {
					return Math.round((size / Math.pow(1024, i))) + " " + units[i];
				}
			}
			return size + " " + units[0];
		}

		
	// Need for backward compatibility to Android 1.6
	private class OldApiHelper {
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
		private int getBtimapSize(Bitmap bitmap) {
			return bitmap.getByteCount();
		}
	}
	




}
	/*
	public void refreshCurrentViewData()
	{
		if(this.mTopCurrentDirectory!=null)
		this.readDirectory(this.mTopCurrentDirectory);
	}*/
	public void  SetDefaultTopDirectory()
	{		
		this.mCurrentDirectory=this.mTopCurrentDirectory=LibCui.GetMyPhoneDirectory();
		this.readDirectory(this.mTopCurrentDirectory);
		
	}
	public void  SetDefaultTopDirectory(File f)
	{		
		this.mCurrentDirectory=this.mTopCurrentDirectory=f;
		this.readDirectory(this.mTopCurrentDirectory);
		
	}
	@Override
	public boolean ProcessBackKey (KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_UP) {
				if (mIsMultiChoice) {
					disableMultiChoice();
				} else {
					File parentFile = mCurrentDirectory.getParentFile();
					if (mCurrentDirectory.equals(mTopCurrentDirectory) ||parentFile==null) {
						complete(null);
						return false;
					} else {
						readDirectory(parentFile);

						String path = mCurrentDirectory.getAbsolutePath();
						if (mListPositioins.containsKey(path)) {
							mAbsListView.setSelection(mListPositioins.get(path));
							mListPositioins.remove(path);
						}

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
	protected void disableMultiChoice() 
	{
		showSecondHeader(false);
		mIsMultiChoice = false;
		mSelected.clear();
		if (mOptChoiceType == ExFilePicker.CHOICE_TYPE_FILES && !mOptOnlyOneItem) {
			readDirectory(mCurrentDirectory);
		}
		((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();
	}

	private void showSecondHeader(boolean show) 
	{
		
	}
	private void updateTitle() {
	   @SuppressWarnings("unused")
	   String FileName=mCurrentDirectory.getName();
	}
	
	private void complete(Object object) {
		if (object == null) {
			String path = mCurrentDirectory.getAbsolutePath();
			if (!path.endsWith("/")) path += "/";
			object = new SelectFile(path, mSelected, mSelected.size());
		}

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
}
