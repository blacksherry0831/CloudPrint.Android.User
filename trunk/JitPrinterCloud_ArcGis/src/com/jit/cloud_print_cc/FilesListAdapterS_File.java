package com.jit.cloud_print_cc;

import java.io.File;
import java.util.ArrayList;


import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;




	class FilesListAdapterS_File extends BaseAdapter 
	{
		private Context mContext_t;
		private int mResource_t;
		private final static int   mDefaultGridRes=R.layout.efp__grid_item;		
        final ArrayList<String> mSelectedFile=new ArrayList<String>() ;
		public FilesListAdapterS_File(Context context, int resource) {
			this.mContext_t = context;
			this.mResource_t = resource;			
		}
		public FilesListAdapterS_File(Context context,ArrayList<String> SelectedFile) {
			this.mContext_t = context;
			this.mResource_t =mDefaultGridRes;
			if(SelectedFile!=null){
				this.mSelectedFile.clear();
				for(String file:SelectedFile){
					this.mSelectedFile.add(file);
				}
				
			}
			
		}
		
		@Override
		protected void finalize(){
		}
		@Override
		public int getCount() {
			return mSelectedFile.size();
		}

		@Override
		public Object getItem(int position) {
			return mSelectedFile.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		 @Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			 String path=this.mSelectedFile.get(position);
			 File file =new File(path);

			convertView = LayoutInflater.from(mContext_t).inflate(mResource_t, parent, false);
			ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

			CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			/*
			if (mSelected.contains(file.getName())) {
				checkbox.setChecked(true);
				setItemBackground(convertView, true);
			} else {
				checkbox.setChecked(false);
				setItemBackground(convertView, false);
			}*/
			//if (mIsMultiChoice) checkbox.setVisibility(View.VISIBLE);

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


	

	private void setItemBackground(View view, boolean state) {
		//view.setBackgroundResource(state ? attrToResId(R.attr.efp__selected_item_background) : 0);
	}
	/*
	class 	UpdataFileTimerTask extends TimerTask
	{
		private int oldSize;
		private int newSize;
		private int count=0;
		@Override
		public void run(){
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
	}*/
	/*
	class UpdataFile implements Runnable{
		private int oldSize;
		private int newSize;
		private int count;
		public void run (){
			if(mIsUpdataFile){
				mHandler.postDelayed(this, 500); 
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
		}*/
		
	//}
}

