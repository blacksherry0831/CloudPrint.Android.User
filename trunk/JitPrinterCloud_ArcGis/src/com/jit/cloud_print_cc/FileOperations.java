package com.jit.cloud_print_cc;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;








import com.jit.cloud_print_cc.R;




import android.annotation.TargetApi;

import android.content.ContentResolver;
import android.content.Context;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.util.LruCache;
import android.util.Log;
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
class ExFilePicker {
	public final static String SET_ONLY_ONE_ITEM = "OnlyOneItem";
	public final static String SET_FILTER_EXCLUDE = "FilterExclude";
	public final static String SET_FILTER_LISTED = "FilterListed";
	public final static String SET_CHOICE_TYPE = "ViewType";
	public final static String SET_START_DIRECTORY = "StartDirectory";
	public final static String SET_SORT_TYPE = "SortType";
	public final static String DISABLE_NEW_FOLDER_BUTTON = "DisableNewFolderButton";
	public final static String DISABLE_SORT_BUTTON = "DisableSortButton";
	public final static String ENABLE_QUIT_BUTTON = "EnableCancelButton";

	public final static int CHOICE_TYPE_ALL = 0;
	public final static int CHOICE_TYPE_FILES = 1;
	public final static int CHOICE_TYPE_DIRECTORIES = 2;

	public final static int SORT_NAME_ASC = 0;
	public final static int SORT_NAME_DESC = 1;
	public final static int SORT_SIZE_ASC = 2;
	public final static int SORT_SIZE_DESC = 3;
	public final static int SORT_DATE_ASC = 4;
	public final static int SORT_DATE_DESC = 5;
}
class FileDefaultImage{
	public final static int forder_bgm=R.drawable.efp__ic_folder2;	
	public final static int file_default_bg=R.drawable.efp__ic_file;
	public final static int file_word=R.drawable.efp_ic_file_word;	
	public final static int file_execl=R.drawable.efp_ic_file_excel;
	public final static int file_pdf=R.drawable.efp_ic_file_pdf;
}
public class FileOperations
{
	final private String[] mVideoExtensions =FExtFilter.mVideoExtensions;
	final private String[] mImagesExtensions =FExtFilter.mImagesExtensions;
	final private boolean DEBUG = false;
	final private String TAG = "ExFilePicker";

	private boolean mOptOnlyOneItem = false;
	private List<String> mOptFilterExclude;
	private List<String> mOptFilterListed;
	private int mOptChoiceType;
	private int mOptSortType;
   
	private LruCache<String, Bitmap> mBitmapsCache;

	
	private ArrayList<File> mFilesListRoot = new ArrayList<File>();
	
	private ArrayList<File> mFilesList = new ArrayList<File>();
	private ArrayList<String> mSelected = new ArrayList<String>();
	private HashMap<String, Integer> mListPositioins = new HashMap<String, Integer>();
	private File mCurrentDirectory;
	private boolean mIsMultiChoice = false;

	private Context mContext;
	private View mEmptyView;
	private AbsListView mAbsListView;
	
	private GridView mGridView;
	private ListView mListView;
	private boolean  mCurrentView=true;
	public  FileOperations(Context context, GridView gv,ListView lv)
	{
		this.mContext=context;
		this.mGridView=gv;
		this.mListView=lv;
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
							mSelected.remove(file.getName());
						} else {
							if (mOptOnlyOneItem) {
								mSelected.clear();
								((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();
							}
							checkBox.setChecked(true);
							setItemBackground(view, true);
							mSelected.add(file.getName());
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
							mSelected.add(file.getName());
							
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
							if (mOptChoiceType != ExFilePicker.CHOICE_TYPE_FILES || file.isFile()) mSelected.add(file.getName());
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
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) path = Environment.getExternalStorageDirectory();
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
	
	private void readDirectory_with_filter(File path) {
		if (path == null) {
			path = new File("/");
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) path = Environment.getExternalStorageDirectory();
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
            this.InitLruCache();
            
			setAbsListView();			
			
			readDirectory(null);
			
	}

	 private void InitLruCache()
	    {
	    	
	    	final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
			final int cacheSize = maxMemory / 8;
	    	try{
	    		mBitmapsCache = new LruCache<String, Bitmap>(cacheSize);
			}catch(Exception ife){
				Log.v("1",ife.getMessage());
			}    	
	    	
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
			if (mSelected.contains(file.getName())) {
				checkbox.setChecked(true);
				setItemBackground(convertView, true);
			} else {
				checkbox.setChecked(false);
				setItemBackground(convertView, false);
			}
			if (mIsMultiChoice) checkbox.setVisibility(View.VISIBLE);

			this.SetFileBackground_cui01(file,thumbnail);

			TextView filename = (TextView) convertView.findViewById(R.id.filename);
			filename.setText(file.getName());

			TextView filesize = (TextView) convertView.findViewById(R.id.filesize);
			if (filesize != null) {
				if (file.isFile()) filesize.setText(getHumanFileSize(file.length()));
				else filesize.setText("");
			}

			return convertView;
		}
		 private void SetFileBackground(File file,ImageView thumbnail){
			 if (file.isDirectory()) {
					//directory
					thumbnail.setImageResource(R.drawable.efp__ic_folder);
				} else {
					//file
					if (Build.VERSION.SDK_INT >= 5 && (Arrays.asList(mVideoExtensions).contains(getFileExtension(file.getName())) 
							|| Arrays.asList(mImagesExtensions).contains(getFileExtension(file.getName())))) {
						Bitmap bitmap = getBitmapFromCache(file.getAbsolutePath());
						if (bitmap == null)
							new ThumbnailLoader(thumbnail).execute(file);
						else
							thumbnail.setImageBitmap(bitmap);
					} else{
						thumbnail.setImageResource(R.drawable.efp__ic_file);
					} 
						
				}
		 }
		 private void SetFileBackground_cui01(File file,ImageView thumbnail){
			 if (file.isDirectory()) {
					//directory
					thumbnail.setImageResource(FileDefaultImage.forder_bgm);
				} else {
					
					thumbnail.setImageResource(FileDefaultImage.file_default_bg);
					 
						
				}
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

		class ThumbnailLoader extends AsyncTask<File, Void, Bitmap> {
			private final WeakReference<ImageView> imageViewReference;

			public ThumbnailLoader(ImageView imageView) {
				imageViewReference = new WeakReference<ImageView>(imageView);
			}

			@TargetApi(Build.VERSION_CODES.ECLAIR)
			@Override
			protected Bitmap doInBackground(File... arg0) {
				Bitmap thumbnailBitmap = null;
				File file = arg0[0];
				if (DEBUG) Log.d(TAG, "Loading thumbnail");
				if (file != null) {
					if (DEBUG) Log.d(TAG, file.getAbsolutePath());
					try {
						ContentResolver crThumb = mContext.getContentResolver();
						if (Arrays.asList(mVideoExtensions).contains(getFileExtension(file.getName()))) {
							if (DEBUG) Log.d(TAG, "Video");
							Cursor cursor = crThumb.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[] { BaseColumns._ID }, MediaColumns.DATA + "='" + file.getAbsolutePath() + "'", null, null);
							if (cursor != null) {
								if (DEBUG) Log.d(TAG, "Cursor is not null");
								if (cursor.getCount() > 0) {
									if (DEBUG) Log.d(TAG, "Cursor has data");
									cursor.moveToFirst();
									thumbnailBitmap = MediaStore.Video.Thumbnails.getThumbnail(crThumb, cursor.getInt(0), MediaStore.Video.Thumbnails.MICRO_KIND, null);
								}
								cursor.close();
							}
						} else if (Arrays.asList(mImagesExtensions).contains(getFileExtension(file.getName()))) {
							if (DEBUG) Log.d(TAG, "Image");
							Cursor cursor = crThumb.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { BaseColumns._ID }, MediaColumns.DATA + "='" + file.getAbsolutePath() + "'", null, null);
							if (cursor != null) {
								if (DEBUG) Log.d(TAG, "Cursor is not null");
								if (cursor.getCount() > 0) {
									if (DEBUG) Log.d(TAG, "Cursor has data");
									cursor.moveToFirst();
									thumbnailBitmap = MediaStore.Images.Thumbnails.getThumbnail(crThumb, cursor.getInt(0), MediaStore.Images.Thumbnails.MINI_KIND, null);
								}
								cursor.close();
							}
						}
						if (DEBUG) Log.d(TAG, "Finished");
					} catch (Exception e) {
						e.printStackTrace();
					} catch (Error e) {
						e.printStackTrace();
					}
				}
				if (DEBUG) Log.d(TAG, "Thumbnail: " + (thumbnailBitmap == null ? "null" : "Ok"));
				if (thumbnailBitmap != null) addBitmapToCache(file.getAbsolutePath(), thumbnailBitmap);
				return thumbnailBitmap;
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				if (imageViewReference != null) {
					final ImageView imageView = imageViewReference.get();
					if (imageView != null) {
						if (bitmap == null) imageView.setImageResource(R.drawable.efp__ic_file);
						else imageView.setImageBitmap(bitmap);
					}
				}
			}

		

	}
	
	// Need for backward compatibility to Android 1.6
	private class OldApiHelper {
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
		private int getBtimapSize(Bitmap bitmap) {
			return bitmap.getByteCount();
		}
	}
	private void setItemBackground(View view, boolean state) {
		//view.setBackgroundResource(state ? attrToResId(R.attr.efp__selected_item_background) : 0);
	}
	private int getBitmapSize(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= 12) {
			return new OldApiHelper().getBtimapSize(bitmap);
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	private void addBitmapToCache(String key, Bitmap bitmap) {
		if (getBitmapFromCache(key) == null) {
			mBitmapsCache.put(key, bitmap);
		}
	}

	private Bitmap getBitmapFromCache(String key) {
		return mBitmapsCache.get(key);
	}
	private String getFileExtension(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index == -1) return "";
		return fileName.substring(index + 1, fileName.length()).toLowerCase(Locale.getDefault());
	}
}
	
	public boolean ProcessKey (KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_UP) {
				if (mIsMultiChoice) {
					disableMultiChoice();
				} else {
					File parentFile = mCurrentDirectory.getParentFile();
					if (parentFile == null) {
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
	private void disableMultiChoice() {
		showSecondHeader(false);
		mIsMultiChoice = false;
		mSelected.clear();
		if (mOptChoiceType == ExFilePicker.CHOICE_TYPE_FILES && !mOptOnlyOneItem) {
			readDirectory(mCurrentDirectory);
		}
		((BaseAdapter) mAbsListView.getAdapter()).notifyDataSetChanged();
	}

	private void showSecondHeader(boolean show) {
		
	}
	private void updateTitle() {
	  String FileName=mCurrentDirectory.getName();
	}
	
	private void complete(Object object) {
		if (object == null) {
			String path = mCurrentDirectory.getAbsolutePath();
			if (!path.endsWith("/")) path += "/";
			object = new SelectFile(path, mSelected, mSelected.size());
		}
		//Intent intent = new Intent();
		//intent.putExtra(ExFilePickerParcelObject.class.getCanonicalName(), object);
		//setResult(RESULT_OK, intent);
		//finish();
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
/**
 * 
 * 
 * 
 */	
	public static void SetFileItemBackground(UserInfoNetFile file,ImageView thumbnail)
	{
		  String FILE_EXT=file.get_FileType();
		  
		 
		   
		  if(Arrays.asList(FExtFilter.mImagesExtensions).contains(FILE_EXT)){
			   //如果是图片的话 使用URL 加载
			  BitmapCacheCui.SetBitmap(file.Get_Url(true), thumbnail, thumbnail.getContext());	
		  }else{
			  FileOperations.SetFileItemBackgroundByExt(FILE_EXT,thumbnail);
		  }
		
	}
/**
 * 
 * 
 * 
 */	
	public static void SetFileItemBackgroundByExt(String fileExt,ImageView thumbnail)
	{
	   
	    
					if(Arrays.asList(FExtFilter.mDocExtensions).contains(fileExt)){
						
						thumbnail.setImageResource(FileDefaultImage.file_word);
						
					}else if(Arrays.asList(FExtFilter.mPdfExtensions).contains(fileExt)){
						
						thumbnail.setImageResource(FileDefaultImage.file_pdf);
					}else if(Arrays.asList(FExtFilter.mExeclExtensions).contains(fileExt)){
						thumbnail.setImageResource(FileDefaultImage.file_execl);
					}else{
						thumbnail.setImageResource(FileDefaultImage.file_default_bg);
					}
	}
/**
 * 
 * 
 *设置本地文件背景
 *
 * 
 * 
 */
	public static void SetFileItemBackground(File file,ImageView thumbnail){
		
		if(file==null||thumbnail==null){
			return;
		}
		
		if (file.isDirectory()) {
				//directory
				thumbnail.setImageResource(FileDefaultImage.forder_bgm);
				
		}else{
			   String fileExt=LibCui.getFileExtension(file.getName());
	           if(Arrays.asList(FExtFilter.mImagesExtensions).contains(fileExt)){
				   BitmapCacheCui.SetBitmap(file, thumbnail, thumbnail.getContext());				
		       } else {
			    /*-扩展名设置图片-*/				
				 SetFileItemBackgroundByExt(fileExt,thumbnail);					
		      }
			
		}
/**
 * 
 * 
 *
 *
 * 
 * 
 */			
		
	 }

}
