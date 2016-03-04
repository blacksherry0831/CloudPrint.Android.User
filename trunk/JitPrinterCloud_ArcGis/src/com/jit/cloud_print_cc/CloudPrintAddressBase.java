package com.jit.cloud_print_cc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CloudPrintAddressBase implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3512719727408885739L;
	private boolean IsClone=false;
	private  boolean IsRegister=false;
	public enum PrintFrom{
		Self,
		Phone,
		Pc,
		cloud
	}
	public enum PrintType{
		NetPrint,
		PcPrint,
		CloudPrint
	}
	public enum PrintFromType{
		pc_PcPrint,
		phone_NetPrint,
		self_NetPrint,
		cloud_CloudPrint
	}
	public PrintFrom mPrintFrom;
	public PrintType mPrintType;
	/*-------------------------*/
	private ServerInfo mParent;
	public String      mIPAddrStr;
	public InetAddress mAddress;
	/*-------------------------*/
	/**
	 * 经度
	 */
	protected double latitude=0;
	/**
	 * 纬度
	 */
	protected double longitude=0;
	/**
	 * 商家名称
	 */
	private String mPrintName;
	/**
	 * 商家名称
	 */
	public String mAddrCharacter;
	/*------------------------------------------------------*/
	public String  MachineBrand="";//打印机品牌
	public String  MachineType="";//型号
	public String  MachineStyle="喷墨";//类型
	public boolean PrinterColor=false;//彩色支持
	public String  MaxPageSize="A4";//打印机支持的最大幅面
	public boolean IsDouble=false;//双面打印支持
	public String  MachineStatus="null";//打印机状态
	public String  WifiName="";//WIFI名
	public String  WifiMac="";//WIFI MAC地址
	public String  PrinterMachineMac="";//打印机归属MAC
	public String  Location="我的手机";//地理位置
	public boolean IsShared=true;//是否共享
	/*--------------------------------------------------------*/
	 public final ArrayList<String> mErrorStrArray=new ArrayList<String>();
	 /*--------------------------------------------------------*/
	 public CloudPrintAddressBase()
	 {
		 
	 }
	 protected ServerInfo GetLocalParent(){
		 if(this.mParent==null){
			 return null;
		 }else{
			 return this.mParent;
		 }
	 }
	 /*---------------------------------*/
	 public final void SetLocalParent(ServerInfo si)
	 {
		 if(si==null) return;
		 this.mParent=si;
		 WifiName=si.mWifiName;//WIFI名
		 WifiMac=si.mWifiMacAddress;//WIFI MAC地址
		 PrinterMachineMac=si.mMacAddress;//打印机归属MAC
		 
		 if(LibCui.getLocalIpAddressList().contains(mAddress)){
			 //本机
			 
			 this.mPrintFrom=PrintFrom.Self;
			 
		 }else{
			 //来自其他机器
			 if(si.mHostType==ServerInfoBase.HOST_PC){
					this.mPrintFrom=PrintFrom.Pc;
				}else if(si.mHostType==ServerInfoBase.HOST_PHONE){
					this.mPrintFrom=PrintFrom.Phone;
				}else{
				    	
				}
		 }
	 }
	 /*---------------------------------*/
		public void setPrinterPoint(String name)
		{
			this.mPrintName = name;
			if(name.contains("\\")){
				String brannd = name.substring(name.lastIndexOf("\\")+1);
				this.MachineBrand=brannd;
				this.MachineType=brannd;
			}else{
				this.MachineBrand=name;
				this.MachineType=name;
			}
		}
		public String GetPrinterPointName()
		{
			return this.mPrintName;
		}
	 /*---------------------------------*/
	 protected ServerInfo GetLocalParentClone(){
		 if(this.mParent==null){
			 return null;
		 }else{
			 return this.mParent.Clone();
		 }
	 }
	 /*---------------------------------*/
	 protected void SetCloneFlag()
	  {
		 IsClone=true;
	  }
	 /*---------------------------------*/
	 private String GetJsonStr(UserInfoBase mUserinfo) throws JSONException
	  {	  
		  JSONObject jsonobj = new JSONObject();
		 
		  jsonobj.put("LoginID",mUserinfo.GetUserName());
		  jsonobj.put("Password",mUserinfo.GetPassword());
		  /*-打印机描述-*/
		  jsonobj.put("MachineBrand", MachineBrand);
		  jsonobj.put("MachineType",MachineType);
		  jsonobj.put("MachineStyle",this.MachineStyle);
		  /*-打印机功能-*/
		  jsonobj.put("PrintColor",(PrinterColor==true)?1:0);
		  jsonobj.put("MaxPageSize",MaxPageSize);
		  jsonobj.put("IsDouble",(IsDouble==true)?1:0);		  
		  jsonobj.put("MachineStatus",MachineStatus);
		  /*-经纬度-*/
		  jsonobj.put("Latitude",(this.latitude));
		  jsonobj.put("Logitude",(this.longitude));
		  /*-地理标识别-*/
		  jsonobj.put("WifiName",WifiName);
		  jsonobj.put("WifiMac",WifiMac);		  
		  jsonobj.put("MachineMac",PrinterMachineMac);
		  jsonobj.put("Location",this.Location);
		 
		  jsonobj.put("IsShared",(IsShared==true)?1:0);
		  /*---*/
		  JSONArray jsonarray=new JSONArray();
		  jsonarray.put(jsonobj);		   
		  return jsonarray.toString();
	  }
	 /*---------------------------------*/
	 protected boolean RegisterPrinter(UserInfoBase mUserinfo)
	 {
		
			String result = null;
			try {
				 result = GetJsonStr(mUserinfo);
				 result= URLEncoder.encode( result, "UTF-8");
				 return SendData2Server(mUserinfo.mRegisterPrinterUrl+"?reports=",result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			    mErrorStrArray.add(e.toString());		
				return false;
			}
		
			
	 }
	 /*---------------------------------*/
	 public boolean SendData2Server(String url_str,String param_str)  
	  {
		  mErrorStrArray.clear();
		  String params =url_str+param_str; 
		  

		  URL url;
		  HttpURLConnection conn = null;
		 try {
			 url = new URL(params);
			 conn = (HttpURLConnection) url.openConnection();
			 conn.setConnectTimeout(3000);
			  //这是请求方式为POST
			  conn.setRequestMethod("GET");
			  //设置post请求必要的请求头
			 // conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// 请求头, 必须设置

			  int http_response_code=conn.getResponseCode();
			  
			  if(http_response_code == 200){
				   InputStream is = conn.getInputStream();// 获取返回数据
				// 使用输出流来输出字符(可选)
		            ByteArrayOutputStream out = new ByteArrayOutputStream();
		            byte[] buf = new byte[1024];
		            int len;
		            while ((len = is.read(buf)) != -1) {
		                out.write(buf, 0, len);
		            }
		            out.flush();
		            out.close();
		            String string = out.toString("UTF-8");
		            if("1".equals(string)){
		            	  mErrorStrArray.add("打印注册成功 ");
		            	  this.SetRegister(true);
		            	  return true;
		            }else{
		            	  mErrorStrArray.add("打印机注册失败："+String.valueOf(string));
		            	  return false;
		            }
		          
			  }else{
				    mErrorStrArray.add("Http Respones Status:"+String.valueOf(http_response_code));
				    return false;
			  }
		} catch (Exception e) {
			       mErrorStrArray.add(e.toString());		
		}finally{
			 
		}
		return  false;
		 
	 }
	  /*---------------------------------*/
	 /*---------------------------------*/
	  public String GetErrorDes()
	   {
		   String Des="";
		  
		  
		   
		   for(int i=0;i< mErrorStrArray.size();i++){
			   if(mErrorStrArray.size()==1){
				   Des+=mErrorStrArray.get(i);
			   }else{
				  
				   Des+=Integer.toString(i)+"."+" "+ mErrorStrArray.get(i);
			   }
			   Des+="\n";
			  
		   }
		  // Des+="\n";
		   return Des;
	   }
	  public void SetLocation(String location)
	  {
		  this.Location=location;
	  }
	  /*-----------------------------------*/
	  public final void SetRegister(boolean isR)
	  {
		  this.IsRegister=isR;
	  }
	  /*-----------------------------------*/
	  public final boolean IsRegisterd()
	  {
		  return this.IsRegister;
	  }
	  /*----------------------------------*/
}
