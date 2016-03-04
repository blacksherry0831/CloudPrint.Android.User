package com.jit.cloud_print_cc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.os.Environment;

public class FileUtilsCui {
	
	private  HashMap<String,FileItem>  mFiles=new HashMap<String,FileItem>(); 
	private  ArrayList<File> mRootFiles=new  ArrayList<File>();
	private  ArrayList<String> mFileExtensions=new ArrayList<String>();;

	private  File   mCurrentFile=null;
	private  boolean  mIsDone=false;
	private  boolean  IsThreadRun=true;
	FileUtilsCui(){
		this.mCurrentFile=null;
		
		
	}
	public void SetExt(String[] str)
	{
	
	  this.mFileExtensions=new ArrayList<String>();
	  this.mFileExtensions.clear();
	  for(int i=0;i<str.length;i++){
		  this.mFileExtensions.add(str[i].toLowerCase(Locale.getDefault()));
		  
	  }
	     new Thread(new ReadFile()).start();
	}
	public void Destory()
	{
	   this.IsThreadRun=false;	
	}
	public void ReStart()
	{
		 new Thread(new ReadFile()).start();
	}
	public int getCount(){
		if(this.mCurrentFile==null){
			 return	this.mRootFiles.size();
		}else if(this.mFiles.containsKey(mCurrentFile.getAbsolutePath())){
			 FileItem item=this.mFiles.get(mCurrentFile.getAbsolutePath());
			 return  item.mFileChild.size();
		}else{
			 return  0;
		}
	  
	}
	public Object getItem(int position) {
		if(this.mCurrentFile==null){
			 return	this.mRootFiles.get(position);
		}else if(this.mFiles.containsKey(mCurrentFile.getAbsolutePath())){
			FileItem item=this.mFiles.get(mCurrentFile.getAbsolutePath());
			 return  item.mFileChild.get(position);
		}else{
			 return  0;
		}
	}
	public void SetDirectory(File f){
		this.mCurrentFile=f;
	}
	public void GetDirectory(){
		
	}
	
	public boolean Return2ParentDirectory(){
		File parent=null;
		if(mCurrentFile==null){
			return true;
		}
				
		if(this.mRootFiles.contains(mCurrentFile)){
			//contins in root
			mCurrentFile=null;
			return false;
			 
		}
		
		return false;
		
	}
	public void DeleteFile( ArrayList<File> f)
	{
		
			if(this.mCurrentFile==null){
					this.mRootFiles.removeAll(f);
			}else if(this.mFiles.containsKey(mCurrentFile.getAbsolutePath())){
				  FileItem item=this.mFiles.get(mCurrentFile.getAbsolutePath());
				  item.mFileChild.removeAll(f);
			}else{
				 
			}
		
	}
	public void DeleteFile(File f)
	{
		
			if(this.mCurrentFile==null){
					this.mRootFiles.remove(f);
			}else if(this.mFiles.containsKey(mCurrentFile.getAbsolutePath())){
				  FileItem item=this.mFiles.get(mCurrentFile.getAbsolutePath());
				  item.mFileChild.remove(f);
			}else{
				 
			}
		
	}
	class FileItem{
		
		 File mDirRoot;
		 ArrayList<File> mFileChild=new ArrayList<File>();
		FileItem(){
			
		}
		
	}
	
    class ReadFile implements  Runnable{
    	  
    	   @Override
		public void run()
    	     {
    		   InitFileList();
    		  
    		   
    		   mIsDone=true;
    	     }
    	  
    	   private File getLinuxRoot()
    	   {
    		   File currentDir=Environment.getExternalStorageDirectory();
   			   File parent=currentDir.getParentFile();
   			
	   			while(parent != null){
	   				currentDir=parent;
	   				parent=currentDir.getParentFile();
	   			}
   			  return currentDir;
    	   }
    	   private void InitFileList()
    		{
    			File currentDir=Environment.getExternalStorageDirectory().getParentFile();
    			
    			this.getAllFiles(currentDir);
    		}
    		  // 遍历接收一个文件路径，然后把文件子目录中的所有文件遍历并输出来 
    	    private void getAllFiles(File root){  
    	    	
    	    	if(IsThreadRun==false) return;
    	    	
    	        File files[] = root.listFiles();  
    	        if(files != null){  
    	            for (File f : files){  
    	                if(f.isDirectory()&&(!f.getName().startsWith("."))){  
    	                    getAllFiles(f);  
    	                }else{  
    	                   /**
    	                    * File
    	                    */
    	                	this.addFile2List(f);
    	                }  
    	            }  
    	        }  
    	        
    	    }  
    	    private void addFile2List(File file){
    	    	
    	    	if(file.isFile())
    	    	{
    	    		File Parent=file.getParentFile();
    	    		
    	    		String fileExt=LibCui.getFileExtension(file.getName());
    	    		
    	    		 if(mFileExtensions.contains(fileExt)){
    	    			this.addFile2HashMap(Parent, file);
    	    		 }
    	    	}
    	    }
    	    private void addFile2HashMap(File parent,File file){
    	    	String key=parent.getAbsolutePath();
    	    	boolean isContain=mFiles.containsKey(key);
    	    	if(isContain){
    	    		// key contain--add
    	    		FileItem v=mFiles.get(key);
   	    		    v.mFileChild.add(file);
    	    	}else{
    	    		//key not find --create
    	    		FileItem v=new FileItem();
   	    		    v.mFileChild.add(file);
   	    		    mFiles.put(key, v);
   	    		    mRootFiles.add(parent);    	    		
    	    	}
    	    }
    	   
     }   
}
