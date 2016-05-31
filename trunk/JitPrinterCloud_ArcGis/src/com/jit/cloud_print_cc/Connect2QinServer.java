package com.jit.cloud_print_cc;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import com.cui.http.httpCui;



public class Connect2QinServer 
{
	/**
	 *注册用户请求字符串
	 *@param LoginName 用户名
	 *@param Password 密码
	 *@param  Telphone 电话
	 *@param Platform 平台
	 */
	public static String GetQinRegisterParam(
			String LoginName,String Password,
			String Telphone,String Platform )
	{
		//LoginName=zhngxiaochen&Password=123123&Telphone=15651687339&Platform=android
		StringBuffer sb=new StringBuffer();
		sb.append("LoginName=");sb.append(UTF8_URL(LoginName));
		sb.append("&Password=");sb.append(UTF8_URL(Password));
		sb.append("&Telphone"); sb.append(UTF8_URL(Telphone));
		sb.append("&Platform=");sb.append(UTF8_URL("android"));
		return sb.toString();
	}
	/**
	 *
	 *
	 */
	public static String GetQinPostFile2Server(
			String  UserID,
			String  FileName,
			String  FileTyple,
			String  FileSize)
	{
		//LoginName=zhangxiaochn&Password=123123&Platform=and
		StringBuffer sb=new StringBuffer();
		sb.append("UserID=");sb.append(UTF8_URL(UserID));
		sb.append("&FileName=");sb.append(UTF8_URL(FileName));
		/*---------------------------------------------------------*/
		sb.append("&FileType=");sb.append(UTF8_URL(FileTyple));
		sb.append("&Filesize=");sb.append(UTF8_URL(FileSize));
		//sb.append("&Platform=");sb.append("android"+android.os.Build.MODEL);
		/*sb.append("&Latitude=");sb.append(La);
		sb.append("&Longitude=");sb.append(lo);*/
	//	sb.append("&MobileUUID=");sb.append(LibCui.GetDeviceID(ctx));	
		return sb.toString() ;
	}
	/**
	 *
	 *
	 */
	public static String GetQinLoginString(
			String  UserID,
			String  LicenseCode)
	{
		//LoginName=zhangxiaochn&Password=123123&Platform=and
		StringBuffer sb=new StringBuffer();
		sb.append("LoginName=");sb.append(UTF8_URL(UserID));
		sb.append("&Password=");sb.append(UTF8_URL(LicenseCode));
		sb.append("&Platform=");sb.append(UTF8_URL("android"));
		//sb.append("&Platform=");sb.append("android"+android.os.Build.MODEL);
		/*sb.append("&Latitude=");sb.append(La);
		sb.append("&Longitude=");sb.append(lo);*/
	//	sb.append("&MobileUUID=");sb.append(LibCui.GetDeviceID(ctx));	
		return sb.toString() ;
	}
	/**
	 *订单生成页面
	 *
	 */
	public static String GetOrderGenerateString(
			String  UserID,
			String  FileName,
			String  FileType,
			String  FileSize,
			String  copies,
			String  paperSize,
		    String	color,
		    String  duplex,
		    String  paperStyle,
		    String  main,
		    String  peisong)
	{
		//LoginName=zhangxiaochn&Password=123123&Platform=and
		StringBuffer sb=new StringBuffer();
		sb.append("UserID=");sb.append(UTF8_URL(UserID==null?"zhangxiaochen":UserID));
		sb.append("&FileName=");sb.append(UTF8_URL(FileName==null?"wenjianming":FileName));
		sb.append("&FileType=");sb.append(UTF8_URL(FileType==null?"doc":FileType));
		sb.append("&FileSize=");sb.append(UTF8_URL(FileSize==null?"1024":FileSize));
		sb.append("&copies=");sb.append(UTF8_URL(copies==null?"2":copies));
		sb.append("&paperSize=");sb.append(UTF8_URL(paperSize==null?"A4":paperSize));/**/
		sb.append("&color=");sb.append(UTF8_URL(color==null?"彩色":color));	
		sb.append("&duplex=");sb.append(UTF8_URL(duplex==null?"双面":duplex));/**/
		sb.append("&paperStyle=");sb.append(UTF8_URL(paperStyle==null?"70g普通纸":paperSize));	
		sb.append("&mian=");sb.append(UTF8_URL(main==null?"一页一面":main));/**/
		sb.append("&peisong=");sb.append(UTF8_URL(peisong==null?"北区01栋":peisong));	
		return sb.toString() ;
	}
	/**
	 * 获取订单数
	 * @param UserID 用户名
	 * @param userKind 用户类型  ； 默认 Y
	 * @param op 权限；默认  W
	 * */
	public static String GetDiskString(String UserID, String Kind)
	{
			StringBuffer sb=new StringBuffer();		
			sb.append("UserID=");sb.append(UTF8_URL(UserID));
			sb.append("&kind=");sb.append(UTF8_URL(Kind));		
			return sb.toString();
	}	
	/**
	 * 获取订单数
	 * @param UserID 用户名
	 * @param userKind 用户类型  ； 默认 Y
	 * @param op 权限；默认  W
	 * */
	public static String GetOrdersString(String UserID, String userKind,String op)
	{
		StringBuffer sb=new StringBuffer();
		
		sb.append("UserID=");sb.append(UTF8_URL(UserID));
		sb.append("&userKind=");sb.append(UTF8_URL(userKind));
	
		if(op!=null){
		     sb.append("&op="); sb.append(UTF8_URL(op));
		}
		return sb.toString();
	}
	
	/*public static String ReportLocationData(
			String  UserID,
			String  LicenseCode,
			double  La,
			double  lo,
			Context ctx)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("userID=");sb.append(UserID);
		sb.append("&licenseCode=");sb.append(LicenseCode);
		sb.append("&Mobile=");sb.append(android.os.Build.MODEL);
		sb.append("&Latitude=");sb.append(La);
		sb.append("&Longitude=");sb.append(lo);
		sb.append("&MobileUUID=");sb.append(LibCui.GetDeviceID(ctx));
		
		
		return sb.toString() ;
	}*/
	/**
	 * 
	 *@deprecated 
	 * 
	 */
	public static  String GetParam2Server(String str_url)
	 {
		  return httpCui.GetParam2Server(str_url);
	 }
	/**
	 *
	 * 下载参数
	 * 
	 */
	public static String GetParamDownload(UserInfoNetFile uinf,boolean thumbnail)
	{
		        StringBuffer sb=new StringBuffer();				
				sb.append("FileID=");sb.append(uinf.get_ID());	
				if(thumbnail){
					sb.append("&Thumbnail=");sb.append(thumbnail);	
				}
				
				
				return sb.toString();
		
	}
	/**
	 *
	 * 
	 * 
	 */
	public static String GetParamDeleteFile(UserInfoNetFile uinf)
	{
		        StringBuffer sb=new StringBuffer();		
				sb.append(UTF8_URL(GetDiskString(uinf.GetUserName(),"N")));
				//sb.append("&FileID=");sb.append(uinf.get_ID());	
				sb.append("&fileID=");sb.append(UTF8_URL(uinf.get_ID()));			
				return sb.toString();
		
	}
	
	/**
	 *
	 * 
	 * 
	 */
	public static  String PostString2Server(String str_url,String RequestData)
	 {
		 
			 String str=str_url;
			
	        try {
	            URL url=new URL(str);
	            
	            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
	            connection.setDoInput(true);
	            connection.setDoOutput(true);
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
	            /*----------------------读取文件上传到服务器--------------------*/
				            BufferedOutputStream  out=new BufferedOutputStream(connection.getOutputStream());
				            byte [] bytes_t=RequestData.getBytes("UTF8");
			                out.write(bytes_t, 0, bytes_t.length);
			                out.flush();          
	            /*---------------------读取URLConnection的响应----------------------------*/
	            //读取URLConnection的响应
	            int http_response_code=connection.getResponseCode();
	                   String FileID="";
							if(http_response_code == 200){
								 byte [] bytes=new byte[1024];
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
						           			          
						           if(FileID==null||FileID.equals("")){
						        	   return null;
						           }else{
						        	   return FileID;
						           }
						          
						  }
	        } catch (Exception e) {
	            
	        }finally{
	        	
	        }
	        return null;
		}
/**
 *
 * 
 * 
 */
	public static String UTF8_URL(String a)
	{
		String es=null;
		try {
			es=URLEncoder.encode(a,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return es;
	}
/**
 *
 * 
 * 
 */
}
