package com.jit.cloud_print_cc;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.UUID;

import com.jit.cloud_print_cc.FilesWithParams.IsDouble;
import com.jit.cloud_print_cc.FilesWithParams.PrintColor;



import android.content.Context;

import android.widget.Toast;

public class UserOrderInfoGenerated extends  UserInfoBase implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5655119011469025113L;
	
	public String  OrderId;
	
    public ArrayList<FilesWithParams> mFiles=new ArrayList<FilesWithParams >();//文件名

    public String FileID;
    public String TackID;
	UserOrderInfoGenerated(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.OrderId=this.getOrderId();
	}
	UserOrderInfoGenerated(Context context,ArrayList<String> Files) {
		super(context);
		// TODO Auto-generated constructor stub
		this.OrderId=this.getOrderId();
		if(Files!=null){
				for(String f:Files){			
					this.mFiles.add(new FilesWithParams(f,context));
				}
		}
		
	}
	public ArrayList<FilesWithParams> GetOrderData(){
		return this.mFiles;
	}
	public String getOrderId()
	{
		String a=UUID.randomUUID().toString();
		return a;
	}

	public boolean SetRange(int from,int to)
	{
		if(this.mFiles==null){			
			return false;
		}
		if(this.mFiles.size()==0){
			return false;
		}
		return true;
	}
	
	 public  String GetHeaderStr(FilesWithParams file,CloudPrintAddress cpa) 
	  {
		 //LibCui.getFileExtension(file);
		 String filename=LibCui.getFileNameNoEx(new File(file.GetFileLocal()));
		 String FileExt=LibCui.getExtensionName(new File(file.GetFileLocal()));
        		  
	     String full_url=null;	
	     StringBuilder sb_url=new StringBuilder();
	     
	     try {	    	 		     
		     sb_url.append("?LoginID="+this.mUserName);
		     sb_url.append("&Password="+this.mPassword);
			 sb_url.append("&FileName="+URLEncoder.encode(filename, "UTF-8")+"."+FileExt);
			 /*-----打印级参数------------------------*/
			 sb_url.append("&PrinterCopies="+String.valueOf(file.mPrintCopies));//份数
			 if(file.mIsColor==PrintColor.Color){
			     sb_url.append("&PrinterColor="+"1");//颜色
			 }else{
				 sb_url.append("&PrinterColor="+"0");//颜色
			 }
			 
			 sb_url.append("&PrintPaperSize="+file.mPaperSize.toString());//纸型
			 sb_url.append("&PrintRange="+URLEncoder.encode(file.mPrintRange, "UTF-8"));//打印范围
			 if(file.mIsDuplex==IsDouble.DoublePaper){
				 sb_url.append("&PrinterIsDuplex="+"1");//双面打印
			 }else{
				 sb_url.append("&PrinterIsDuplex="+"0");//双面打印
			 }		 
			 /*-----------------------------*/
			 sb_url.append("&WifiName="+cpa.WifiName);
			 sb_url.append("&WifiMac="+cpa.WifiMac);
			 sb_url.append("&PrinterMachineMac="+cpa.PrinterMachineMac);
			 sb_url.append("&MachineBrand="+cpa.MachineBrand);
			 sb_url.append("&MachineType="+cpa.MachineType);
			 sb_url.append("&Latitude="+String.valueOf(cpa.getLatitude()));
			 sb_url.append("&Logitude="+String.valueOf(cpa.getLongitude()));
			 /*-----------------------------*/
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	     
		 if(mFiles.contains(file)){
			 full_url=this.mPostFileUrl+sb_url;
		 }else{
			 return null;
		 }
		 
		return full_url;
		 
	  }
	 public  String PostFile2Server(String url_str,String filefullPath)
	 {
		 try {
	            URL url=new URL(url_str);
	            
	            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
	            connection.setDoInput(true);
	            connection.setDoOutput(true);
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("content-type", "text/html");
	            
	 
	            BufferedOutputStream  out=new BufferedOutputStream(connection.getOutputStream());
	            
	            /*----------------------读取文件上传到服务器--------------------*/
	            File file=new File(filefullPath);
	            FileInputStream fileInputStream=new FileInputStream(file);
	            byte[] bytes=new byte[1024];
	            int numReadByte=0;
	            int sendNum=0;
	            int totalNum=fileInputStream.available();
	            while((numReadByte=fileInputStream.read(bytes,0,1024))>0)
	            {
	            	long TimeSendStart=System.nanoTime();
			            	sendNum+=numReadByte;
			                out.write(bytes, 0, numReadByte);
			                out.flush();
			        long TimeMs=(System.nanoTime()-TimeSendStart)/1000;  
			        double SendSpeed=(1.0*numReadByte/1024)/(1.0*TimeMs/1000);//KBpS
	                if(mNowSendPerCent!=null){
	                		this.mNowSendPerCent.NowSendPerrSent(1.0*sendNum/totalNum, SendSpeed);
	                }
	            }
	            fileInputStream.close();
	            out.flush();
	            out.close();
	            /*---------------------读取文件上传到服务器----------------------------*/
	            //读取URLConnection的响应
	            int http_response_code=connection.getResponseCode();
	                   String FileID="";
							if(http_response_code == 200){
						            DataInputStream in=new DataInputStream(connection.getInputStream());
						            Arrays.fill(bytes, (byte) 0x00);
						            
						            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
						            int len_read=0;
						            while(true){
						            	len_read=in.read(bytes,0,bytes.length);
						            	if(len_read==-1) break;
						            	outStream.write(bytes, 0, len_read);
						            	
						            	
						            }
						            
						            
						            
						            FileID=outStream.toString("UTF-8");
						            if(FileID==null) FileID="";
						            FileID.replace("\000", "");						            	 
						           			          
						           if(FileID==null||FileID.equals("0")||FileID.equals("")){
						        	   this.mErrorStrArray.add("提交失败");
						        	   return null;
						           }else{
						        	   mFilesIdOnServer.put(filefullPath, FileID);
						        	   mFilesIdOnServer.put(FileID, FileID);	
						        	   this.mErrorStrArray.add("成功提交文件");
						        	   return FileID;
						           }
						          
						  }else{
							  this.mErrorStrArray.add("Http: 响应"+ String.valueOf(http_response_code)+"/n"+this.mDownLoadFileUrl);
						  }
	        } catch (Exception e) {
	            e.printStackTrace();
	            this.mErrorStrArray.add(e.toString());
	            return null;
	        }finally{
	        	
	        }
	        return null;
	 }
	 public  String PostFile2Server(FilesWithParams  fileWithParam,CloudPrintAddress cpa)
	 {
		  String filefullPath=fileWithParam.GetFileLocal();
		  this.mErrorStrArray.clear();
		  if(this.IsFileOnServer(filefullPath)){
			  //已经post,无需重复上传
			  this.mErrorStrArray.add("源文件文件已经在服务器上");
			  return this.mFilesIdOnServer.get(filefullPath);
		  }	
			// TODO Auto-generated method stub
			String str=this.GetHeaderStr(fileWithParam,cpa);
			return PostFile2Server(str,filefullPath);
	       
		}
	
	private boolean IsFileOnServer(String FilefullName)
	 {
		  String FileIdOnServer= mFilesIdOnServer.get(FilefullName);	 

		  if(FileIdOnServer != null && !FileIdOnServer.equals("") && !FileIdOnServer.equals("0")){
       	  
       	      return true;
       	      
          }else{
        	  return false;
          }
		  
		 
	 }
	 public String SendFile2CloudServer(FilesWithParams fwp,CloudPrintAddress cpa)
	 {		
		 String File=fwp.GetFileLocal();
		if(this.IsFileOnServer(File)){
			//直接提取ID 
			return mFilesIdOnServer.get(File);
			
		}else{
			return PostFile2Server(fwp,cpa);
		}
		
		
	 }
	 /**
	  * 
	  * 获取prn文档
	  * @param filefullPath
	  * @return
	  */
	 public boolean  GetFileFromCloudPrint(String FilefullName)
	 {
		
		 if(!this.IsFileOnServer(FilefullName)){
			//文件不存在与服务器
			 return false;
		 }
		 
		 ;
        
		return  GetFileOnServer(FilefullName);
	 }
 /*
  * 
  * 
  * 
  * */
	 public String GetPCLFileLocation(String key_id_or_full_Path)
	 {
	       return this.mPCLPathOnPhone.get(key_id_or_full_Path);	 
	 }
	 /**
	  * 
	  * 获取prn文档
	  * @param filefullPath
	  * @return
	  */
	public boolean GetFileOnServer(String filefullPath)
	 {
		String mFileIdOnServer=this.mFilesIdOnServer.get(filefullPath);
		
		 String param="?LoginID="+this.mUserName
	              +"&Password="+this.mPassword
			      +"&FileID="+mFileIdOnServer;
		 String FileNameNew=LibCui.getFileNameNoEx(new File(filefullPath))+".prn";
		 File file=new File(LibCui.GetCloudPrintPCLFile(),FileNameNew);
	     if(file.exists())      {   file.delete();    } 
		 /*		
		String FileFullPathNew=mContext.getExternalCacheDir().getAbsolutePath()+"/"+FileNameNew; 		
		//String FileFullPathNew=Environment.getExternalStorageDirectory()+"/"+FileNameNew;		
		File file=new File(FileFullPathNew);*/			 

		double FreeSpace=mContext.getExternalCacheDir().getFreeSpace()/1024.0/1024.0;
		double FileSize=new File(filefullPath).length()/1024.0/1024.0;
		if(FreeSpace<FileSize*2){
			this.mErrorStrArray.add("手机缓存空间不足 getExternalCacheDir");
			this.Toast_MakeText_show(this.GetErrorDes(),Toast.LENGTH_LONG);
			return false;
		}
		
		String Url=this.mDownLoadFileUrl+param;	    
		return this.GetPCLFileOnServer(Url,file.getAbsolutePath(),filefullPath);
		 
	 }
	
	public void SetAllPrinterRange(String  page)
	{
		//nothing
		for(FilesWithParams f:mFiles)
		{			
			f.mPrintRange=page;
		}
	}
	public void SetAllPrinterColor(boolean isColor) {
		// TODO Auto-generated method stub
		for(FilesWithParams f:mFiles)
		{			
			f.SetPrinterColor(isColor);
		}
	}
	public void SetAllPrinterCopies(int copies) {
		// TODO Auto-generated method stub
		for(FilesWithParams f:mFiles)
		{			
			f.SetPrinterCopies(copies);
		}
	}
	public void SetAllPrinterDuplexPaper(boolean isduplex) {
		// TODO Auto-generated method stub
		for(FilesWithParams f:mFiles)
		{			
			f.SetPrinterDuplexPaper(isduplex);
		}
	}
	
}
