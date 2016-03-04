package com.jit.cloud_print_cc;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.jit.cloud_print_cc.R;
import com.squareup.picasso.Picasso;

public class BitmapCacheCui {

	private LruCache<String, Bitmap> mBitmapsCache;
	
	BitmapCacheCui(){
		this.InitLruCache();
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
	 public void addBitmapToCache(String key, Bitmap bitmap) {
			if (getBitmapFromCache(key) == null) {
				mBitmapsCache.put(key, bitmap);
			}
		}

		private Bitmap getBitmapFromCache(String key) {
			return mBitmapsCache.get(key);
		}
		
		public void SetBitMapLruCache(File f) { 
		   /*
		    final Bitmap bitmap = getBitmapFromMemCache(imageKey); 
		    if (bitmap != null) { 
		        imageView.setImageBitmap(bitmap); 
		    } else { 
		        imageView.setImageResource(R.drawable.image_placeholder); 
		        BitmapWorkerTask task = new BitmapWorkerTask(imageView); 
		        task.execute(resId); 
		    }
		    */ 
		}
		class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> { 
		    // 在后台加载图片。 
		    @Override
		    protected Bitmap doInBackground(Integer... params) { 
		    	
		        final Bitmap bitmap =null;
		      //  addBitmapToMemoryCache(String.valueOf(params[0]), bitmap); 
		        return bitmap; 
		        
		    } 
		}
	public static void SetBitmap(File file,ImageView thumbnail,Context mContext){
		File imageFile=file;
		
		Picasso.with(mContext.getApplicationContext())
	    .load(imageFile)
	    .placeholder(R.drawable.efp__ic_image_default_error)	                          
	    .resize(100,100)
	    .centerCrop()
	    .into(thumbnail);
	} 
	
	public static void SetBitmap(String Url,ImageView thumbnail,Context mContext){
		
		if(Url==null||"".equals(Url))
			return;
		
		Picasso.with(mContext.getApplicationContext())
	    .load(Url)
	    .placeholder(R.drawable.efp__ic_image_default_error)	                          
	    .resize(100,100)
	    .centerCrop()
	    .into(thumbnail);
	} 
	
}
