package com.jit.cloud_print_cc;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.squareup.picasso.Transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 绑定GridView 到 ——adapter
 * 
 * 
 */
public class FileOperations_NetSelect extends FileOperations_Select
{
	private int mOptChoiceType;	
	PullToRefreshGridView  _gridview;
	Object mCurrentDirectory;
	ArrayList<UserInfoNetFile>     _NetDiskFiles=new ArrayList<UserInfoNetFile>();
	FilesListAdapter      _adapter;  
	public FileOperations_NetSelect(Context context,View gv )
	{
		super();
		this.mContext=context;
		if(gv  instanceof PullToRefreshGridView){
			this._adapter=new FilesListAdapter(context,GridLayoutID);
			this._gridview=(PullToRefreshGridView) gv;
			this._gridview.setAdapter(_adapter);
			this.initgridView(this._gridview, _NetDiskFiles,_adapter);
		}
		
	}
	public void initgridView(final PullToRefreshGridView prgv,final ArrayList<UserInfoNetFile> uinFiles,final FilesListAdapter adapter)
	{	
			//this.setAbsListViewLayout();		
		prgv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (position <uinFiles.size()) {
						UserInfoNetFile file = uinFiles.get(position);
						if (mIsMultiChoice) {
							CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
							if (checkBox.isChecked()) {
								checkBox.setChecked(false);
								setItemBackground(view, false);
								mSelected.remove(file.getAbsolutePath());
							} else {
								if (mOptOnlyOneItem) {
									mSelected.clear();
									adapter.notifyDataSetChanged();
								}
								checkBox.setChecked(true);
								setItemBackground(view, true);
								mSelected.add(file.getAbsolutePath());
							}
						} else {
							if (file.isDirectory()) {
								mIsMultiChoice=false;
								//int currentPosition = prgv.getFirstVisiblePosition();
								 file.SetDirectory(file);
								//readDirectory(null);
								//updateTitle();
								// prgv.setSelection(0);
							} else {
								mIsMultiChoice=true;
								mSelected.add(file.getAbsolutePath());
								
							}
							adapter.notifyDataSetChanged();
						}
					}
					
					prgv.setVisibility(View.VISIBLE);
				}
			});

			if (mOptChoiceType != ExFilePicker.CHOICE_TYPE_FILES || !mOptOnlyOneItem) {
				/*prgv.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
						if (!mIsMultiChoice) {
							mIsMultiChoice = true;
							if (position <uinFiles.size()) {
								UserInfoNetFile file =uinFiles.get(position);
								if (mOptChoiceType != ExFilePicker.CHOICE_TYPE_FILES || file.isFile()) mSelected.add(file.getAbsolutePath());
							}

							if (mOptChoiceType == ExFilePicker.CHOICE_TYPE_FILES && !mOptOnlyOneItem) {
								ArrayList<UserInfoNetFile> tmpList = new ArrayList<UserInfoNetFile>();
								for (int i = 0; i <uinFiles.size(); i++) {
									UserInfoNetFile file =uinFiles.get(position);
									if (file.isFile()) tmpList.add(file);
								}
								//mFilesList = tmpList;
							}

							adapter.notifyDataSetChanged();

							//showSecondHeader(true);
							return true;
						}
						return false;
					}
				});*/
			}
			//findViewById(curView).setVisibility(View.GONE);
			prgv.setVisibility(View.VISIBLE);
		
		
	}
	@Override
	public void addDataAll(ArrayList<String> src)
	{		
		super.addDataAll(src);
		//this._NetDiskFiles.addAll(src);
		this._adapter.notifyDataSetChanged();
		this._gridview.onRefreshComplete();
	}
	@Override
	public void addDataAllNet(ArrayList<UserInfoNetFile> src)
	{		
		super.addDataAllNet(src);
		this._NetDiskFiles.addAll(src);
		this._adapter.notifyDataSetChanged();
		this._gridview.onRefreshComplete();
	}
    private void setItemBackground(View view, boolean state) {
		
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
			// mHandler.postDelayed(new UpdataFile(),10);
			
		}
		
		@Override
		public int getCount() {
			return _NetDiskFiles.size();
		}

		@Override
		public Object getItem(int position) {
			return _NetDiskFiles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		 @Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Object file =getItem(position);
            
			//if(convertView==null)
			{
				convertView = LayoutInflater.from(mContext_t).inflate(mResource_t, parent, false);
			}
			
			ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

			CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			if(file instanceof UserInfoNetFile){
				if (mSelected.contains(((UserInfoNetFile)file).get_ID())) {
					checkbox.setChecked(true);
					setItemBackground(convertView, true);
				} else {
					checkbox.setChecked(false);
					setItemBackground(convertView, false);
				}
				
			}
			
			
			if (mIsMultiChoice){
				checkbox.setVisibility(View.VISIBLE);
			}else{
				checkbox.setVisibility(View.INVISIBLE);
			}
			
			if(file instanceof UserInfoNetFile){
				FileOperations.SetFileItemBackground((UserInfoNetFile)file, thumbnail);
			}
		

			TextView filename = (TextView) convertView.findViewById(R.id.filename);
			if(file instanceof UserInfoNetFile){
				filename.setText(((UserInfoNetFile)file).GetFilename());
			}
			

			TextView filesize = (TextView) convertView.findViewById(R.id.filesize);
			/*if (filesize != null) {
				if (file.isFile()) filesize.setText(getHumanFileSize(file.length()));
				else filesize.setText("");
			}*/

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


	

		private void setItemBackground(View view, boolean state) 
		{
			//view.setBackgroundResource(state ? attrToResId(R.attr.efp__selected_item_background) : 0);
		}
	
	}

	@Override
	public boolean ProcessBackKey (KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_UP) {
				if (mIsMultiChoice) {
					disableMultiChoice();
				} else {
					boolean Flag_return=true;
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
	private void complete(Object object) {
		// TODO Auto-generated method stub
		
	}
	private void updateTitle() {
		// TODO Auto-generated method stub
		
	}
	private void readDirectory(Object object) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void disableMultiChoice()
	{
		super.disableMultiChoice();
		if (mOptChoiceType == ExFilePicker.CHOICE_TYPE_FILES && !mOptOnlyOneItem) {	
		      readDirectory(mCurrentDirectory);
	     }
		_adapter.notifyDataSetChanged();
	}
	@Override
	public void DeleteSelectedItem()
	{
		 mIsMultiChoice=false;
		 /* for(String filefullpath :mSelected)
		  {
		     File f=new File(filefullpath);	
		     if(f.exists()){
		    	 f.delete();
		     }
			  
		  }*/
		  mSelected.clear();
		  this.NotifyListChanged();
	}
}
