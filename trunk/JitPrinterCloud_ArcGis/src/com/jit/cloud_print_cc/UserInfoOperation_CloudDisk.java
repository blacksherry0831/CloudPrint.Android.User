package com.jit.cloud_print_cc;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jit.cloud_print_cc.ServerInfoSendStatus.SendFilePackage;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferDone;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferStart;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferendEndOne;
import com.jit.config.KEY;
import com.ta.utdid2.android.utils.StringUtils;

import android.content.Context;

public class UserInfoOperation_CloudDisk extends UserOrderInfoGenerated {
    
	TransferStart start;
	TransferendEndOne end;
	TransferDone done;
	static final ArrayList<UserInfoNetFile> uinf=new  ArrayList<UserInfoNetFile>() ;
	public static String[] SuccessStatus={"上传成功"};
	private SendFilePackage sfPackage=new SendFilePackage();
	public  UserInfoOperation_CloudDisk setStart(TransferStart start) {
		this.start = start;
		return this;
	}
	public  UserInfoOperation_CloudDisk setEnd(TransferendEndOne end) {
		this.end = end;
		return this;
	}
	public  UserInfoOperation_CloudDisk setDone(TransferDone done) {
		this.done = done;
		return this;
	}
	private boolean _Requesting=false;
	private  WhenRcvOrderList IRcv;
	String _result="";
	public interface WhenRcvOrderList{
		//public void OnRecvData(String result);
		public void OnRecvData(ArrayList<UserInfoNetFile> uinfs);
		public void OnRecvError(String error);
	}
/**
 * 
 * 
 */
	UserInfoOperation_CloudDisk(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
/**
 * 初始化，文件类型
 * 
 */
	UserInfoOperation_CloudDisk(Context context,ArrayList<String> Files) {
		super(context,Files);
		// TODO Auto-generated constructor stub
	}
/**
 * 请求订单列表
 * 
 */
	public void RequestDiskForder()
	{
		if(_Requesting==false)
		new Thread(new GetCloudDisk()).start();
	}
	/**
	 * 
	 * 
	 */
	public static UserInfoNetFile  GetNetFile(String ID)
	{
		if(uinf==null){
	      return null;
		}else{
	    	for(UserInfoNetFile t:uinf){
	    		if(t.contian_ID(ID)){
	    			return t;
	    		}
	    	}
	    }
		return null;	
	}
/**
 * 
 * 文件名
 */
	public static String  GetUUIDFileName(String ID)
	{
		if(uinf==null){
	      return null;
		}else{
	    	for(UserInfoNetFile t:uinf){
	    		if(t.contian_ID(ID)){
	    			return t.GetFileUUIDname();
	    		}
	    	}
	    }
		return null;	
	}
	/**
	 * UUID 文件名
	 * 
	 */
		public static String  GetFileName(String ID)
		{
			if(uinf==null){
		      return null;
			}else{
		    	for(UserInfoNetFile t:uinf){
		    		if(t.contian_ID(ID)){
		    			return t.GetFilename();
		    		}
		    	}
		    }
			return null;	
		}
	public static String  GetFileDownloadUrl(String ID)
	{
		if(uinf==null){
	      return null;
		}else{
	    	for(UserInfoNetFile t:uinf){
	    		if(t.contian_ID(ID)){
	    			return t.Get_Url(false);
	    	   }
	       }
	   }
		return null;	
	}
/**
 * 
 * 
 */
	public static String  GetUUIDFileNameFullPath(String ID)
	{
		String FileUUIDName=GetUUIDFileName(ID);
		if(FileUUIDName==null){
		    return null;	
		}else{
			File f=new File(LibCui.GetMyPhoneDirectory(),FileUUIDName);
			return f.getAbsolutePath();
		}
		
	}
/**
 * post file
 * setup order
 * 
 */
	public String PostFile2Server2Print(FilesWithParams fWP)
	{	
		;
		return this.PostOneFile2Print(fWP);
	}	
/**
 * 
 * 
 */
	public  String PostOneFile2Print(FilesWithParams fwp)
	{
		List listSuccess = java.util.Arrays.asList(SuccessStatus);
		/*---------------------------------------------------*/
		int i=0;
	   {
			//fwp.Post2S2SaveStatus=PostOneFile2Order(fwp.GetF_File().getAbsolutePath());
		    fwp.Post2S2SaveStatus=PostOneFile2Order(fwp);
			String SendDes ="文件："+ fwp.Post2S2SaveStatus;
			if(end!=null)   end.EndOne(mContext, null,1.0* i++/mFiles.size());
			if(listSuccess.contains(fwp.Post2S2SaveStatus)){
			
			}else{
				
			}
			return fwp.Post2S2SaveStatus;
			
		}
	
	}
/**
 * 
 * 
 */
	public static String DownloadFile4Server(String fileid)
	{
	  
		String filename_s=GetUUIDFileNameFullPath(fileid);
		
		File file_f=new File(filename_s);
		
		if(file_f.exists()){
			return file_f.getAbsolutePath();
		}else{
			//下载文件
			return GetFile4Server(GetFileDownloadUrl(fileid),filename_s);
			
		}
		
		//new Thread(new DownloadFile4CloudDisk()).start();
	}
/**
 * 
 * 
 */
	public void PostFile2Server2Save()
	{
	
		new Thread(new PostFile2CloudDisk()).start();
	}
/**
 * 
 * 
 */
	public void DeleteFile2Server2Save(ArrayList<String> files)
	{
	
		new Thread(new DeleteFile4CloudDisk(files)).start();
	}
/**
 * 
 * 
 */
	public void setIRcv(WhenRcvOrderList iRcv) {
		IRcv = iRcv;
	}
	/**
	 * 
	 * 
	 */
	public static  ArrayList<UserInfoNetFile> ParseJo2NetFile(Context ctx,JSONObject jo)
	{
		int _total;
		ArrayList<UserInfoNetFile> _netFiles=new ArrayList<UserInfoNetFile>();
		try {
			_total=jo.getInt("total");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		try {
			JSONArray jsonStrs=jo.getJSONArray("rows");
			
			for(int i=0;i<jsonStrs.length();i++){					
				UserInfoNetFile userfile=new UserInfoNetFile(ctx);
				if(userfile.Parse(jsonStrs.getJSONObject(i))){
					_netFiles.add(userfile);
				}
			}
				
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}			
		
		return _netFiles;
		
		//UserInfoOrder user=new UserInfoOrder();
		//user.Parse_str(json_txt);
		//_orders.add(new UserInfoOrder ());
		
	}
/**
 * 
 * 
 */		
		public static  boolean  IsValidJson(String  j_txt)
		{
			
			if(StringUtils.isEmpty(j_txt)){
				 return false;
			}			
			
			boolean Valid=true;
			JSONObject jo = null;
			try {
				jo=new JSONObject(j_txt);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Valid=false;
			}
			
			return  Valid;
		}
/**
 * 
 * 
 */
		public void Parse2Call()
		{
			if(IsValidJson(_result)){
				
				try {
					uinf.clear();
					uinf.addAll(ParseJo2NetFile(getContext(),new JSONObject(_result)));
					LibCui.SaveString2Fille(KEY.K_D_NETDISKFORDER, _result);
					if(uinf!=null&&IRcv!=null){						
						 IRcv.OnRecvData(uinf);					
					}		
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					if(IRcv!=null)	  IRcv.OnRecvError(e.getMessage());
				}
								
			}else{
				   if(IRcv!=null)  IRcv.OnRecvError(_result);
			}
			
			
		}
/**
 * 
 * 获取云盘列表
 */
	public class GetCloudDisk implements Runnable
	{
		int _total;
		
		/**
		 * 
		 * 
		 */
	
		@Override
		public void run()
		{
			_Requesting=true;
			// TODO Auto-generated method stub
				//UserInfoBase user=new UserInfoBase(mContext);			
				String param=Connect2QinServer.GetDiskString(GetUserName(), "N");
				String str_url= _mGetDiskListUrl+"?"+param;
				_result=Connect2QinServer.GetParam2Server(str_url);
				
				Parse2Call();
				
				
				
				
			//
			_Requesting=false;
		}
		
	}
/**
 * 
 * 
 */
	private String PostOneFile(String filename)
	{
		File f=new File(filename);
		String param=Connect2QinServer.GetQinPostFile2Server(GetUserName(),LibCui.getFileNameNoEx(f),LibCui.getExtensionName(f),String.valueOf(f.length()));
		String str_url= _mPostFileQinUrl+"?"+param;
		return this.PostFile2Server(str_url,filename);
	}
	/**
	 * http://www.qindayin.com:8080/machine/MobileMakeOrders.ashx?
	 * UserID=18115125913&
	 * FileName=%E5%86%9C%E6%9D%91%E7%94%B5%E5%95%86%E5%88%9B%E4%B8%9A%E5%88%9B%E6%96%B0%E5%A4%A7%E8%B5%9B%E9%80%9A%E7%9F%A5%EF%BC%88%E6%9C%AA%E4%BC%9A%E7%AD%BE%EF%BC%89&
	 * FileType=doc&
	 * FileSize=32256&copies=1&
	 * paperSize=A4&
	 * color=%E5%BD%A9%E8%89%B2&
	 * duplex=%E5%8F%8C%E9%9D%A2&
	 * paperStyle=70g%E6%99%AE%E9%80%9A%E7%BA%B8&
	 * mian=%E4%B8%80%E9%A1%B5%E4%B8%80%E9%9D%A2&
	 * peisong=%E5%8C%97%E5%8C%BA01%E6%A0%8B
	 * 
	 */
		private String PostOneFile2Order(FilesWithParams fwp)
		{
			String filename=fwp.GetF_File().getAbsolutePath();
			File f=new File(filename);
			//String param=Connect2QinServer.GetQinPostFile2Server(GetUserName(),LibCui.getFileNameNoEx(f),LibCui.getExtensionName(f),String.valueOf(f.length()));
			/* String param=Connect2QinServer.GetOrderGenerateString(
					 UserID,
					 FileName,
					 FileType,
					 FileSize,
					 copies,
					 paperSize,
					 color,
					 duplex,
					 paperStyle,
					 main,
					 peisong);*/
			 String param=Connect2QinServer.GetOrderGenerateString(
					 GetUserName(),
					 LibCui.getFileNameNoEx(f),
					 LibCui.getExtensionName(f),
					 String.valueOf(f.length()),
					 fwp.GetPrinterCopies(),
					 null,
					 null,
					 null,
					 null,
					 null,
					 null);
			StringBuilder  str_url=new StringBuilder();
			
			str_url.append(_mMobileMakeOrderUrl);
			str_url.append("?");
			str_url.append(param);			
			return this.PostFile2Server(str_url.toString(),filename);
		}
	/**
	 * 
	 * 
	 */
	private String PostOneFile2Print(String filename)
	{
		File f=new File(filename);
		String param=Connect2QinServer.GetQinPostFile2Server(GetUserName(),LibCui.getFileNameNoEx(f),LibCui.getExtensionName(f),String.valueOf(f.length()));
		String str_url= _mMobileMakeOrderUrl+"?"+param;
		return this.PostFile2Server(str_url,filename);
	}
/**
 * 
 * 
 */
 /**
 * 
 * 送网站上获取PCL文档
 * 
 * 
 * */	  
  public static String GetFile4Server(String url_t,String fileSavePath)
  {
	     //  String FileIdOnServer=this.mFilesIdOnServer.get(FilefullPath);

	       FileOutputStream fos=null;
	       boolean success_t=true;
			// TODO Auto-generated method stub
        try {
            URL url=new URL(url_t);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("content-type", "text/html");
            //读取URLConnection的响应
            int http_response_code=connection.getResponseCode();
                   //String FileID="";
						if(http_response_code == 200){
							
							      fos=new FileOutputStream(new File(fileSavePath));
							      InputStream is = connection.getInputStream();// 获取返回数据
								// 使用输出流来输出字符(可选)
						           long filesize_total_pcl=0;
						            byte[] buf = new byte[1024];
						            int len;
						            int persent=0;
						            while ((len = is.read(buf)) != -1) {
						            	  fos.write(buf, 0, len);
						            	  filesize_total_pcl+=len;
						            	  
						            	 /* if(mNowSendPerCent!=null){
						                		this.mNowSendPerCent.NowSendPerrSent((persent++%100),777);
						                }*/
						            }		
						            fos.flush();
						            fos.close();
						            is.close();				        	 
						         
						        	 // mErrorStrArray.add("文件下载成功");
						              return fileSavePath;
						            
						            
					  }else{
						//  this.mErrorStrArray.add("Http:响应"+ String.valueOf(http_response_code));
					  }
        } catch (Exception e) {
            e.printStackTrace();
       //     this.mErrorStrArray.add(e.toString());
            success_t=false;
            return null;
        }finally{
        	 try {
				  if(fos!=null)  fos.close();
			} catch (IOException e) {
					success_t=false;
				}
	        }
        
        if(success_t==false){
        	 LibCui.deleteFile(fileSavePath);
        	 return null;
        }else{
        	 return fileSavePath;
        }
	
  }
/**
 * 
 * 
 */
	public class PostFile2CloudDisk implements Runnable
	{
		private void PostMutiFile()
		{
			List listSuccess = java.util.Arrays.asList(SuccessStatus);
			/*---------------------------------------------------*/
			if(start!=null) start.Start(mContext);
			int i=0;
			for(FilesWithParams fwp:mFiles){
				fwp.Post2S2SaveStatus=PostOneFile(fwp.GetF_File().getAbsolutePath());
				String SendDes ="文件："+ fwp.Post2S2SaveStatus;
				if(listSuccess.contains(fwp.Post2S2SaveStatus)){
					 sfPackage.SendSuccess.add(fwp.Post2S2SaveStatus);
				}else{
					 sfPackage.SendAbort.add(SendDes);
				}
				
				if(end!=null)   end.EndOne(mContext, null,1.0* i++/mFiles.size());
			}
			if(done!=null) done.Done(mContext, sfPackage);
			
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			PostMutiFile();
		}
		
		
	}
/**
 * 
 * 
 */	
	public class DeleteFile4CloudDisk implements Runnable
	{
		ArrayList<String> _files;
	
		
		public DeleteFile4CloudDisk(ArrayList<String> files) {
			super();
			this._files=new ArrayList<String>(files);
		}

		public String Delete1File(String a)
		{
			UserInfoNetFile uinf=new UserInfoNetFile(mContext);
			uinf.set_ID(a);
			
			String param=Connect2QinServer.GetParamDeleteFile(uinf);
			String str_url= _mDeleteFileQinUrl+"?"+param;
			return Connect2QinServer.GetParam2Server(str_url);
						
		}
		@Override
		public void run() {
			int old_size=(uinf==null)?0:uinf.size();
			// TODO Auto-generated method stub
			for(String file: _files)
			{
				_result=Delete1File(file);
			}
			Parse2Call();
			int new_size=(uinf==null)?0:uinf.size();
			
			if(old_size==new_size){
				 if(IRcv!=null)  IRcv.OnRecvError("文件数目未发生变化");
			}
			
		}
		
		
	}
/**
 * 
 * 
 */	
	public class DownloadFile4CloudDisk implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
/**
 * 
 * 
 */	
}
