package com.jit.cloud_print_cc;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * 一个文件的打印参数
 * 
 */
public class FilesWithParams 
{
	public enum PrintColor
	{
		Gray,
		Color
	}
	public enum PrintPaperSize
	{
		A4,
		A3
	}
	public enum HorV 
	{
		H,
		V
	}
	public enum IsDouble 
	{
		OnePaper,
		DoublePaper
	}
	/*----------------------------------------------------------------*/
	private String userName="NoUser";/**<打印用户*/
	private String price2pay="-1";/**<价格*/
	private String orderId="订单号";/**<订单号*/
	//private String printerName="reserve";/**<打印机名*/
	/*----------------------------------------------------------------*/
	private String mFileID="No File";/**<文件ID*/
	//private String mUUIDFilefullname;
	/*-------打印级别参数-----------------------------------------------*/
	public int mPrintCopies=1;//份数
	private PrintColor     mIsColor=PrintColor.Gray;//颜色
	public PrintPaperSize mPaperSize=PrintPaperSize.A4;//纸型
	public String mPrintRange="全部";//打范围印
	private IsDouble  mIsDuplex=IsDouble.OnePaper;//双面打印
	private String Printer="No Printer";
	/*----------------------------------------------------------------*/
	public String mPhoneType=android.os.Build.MODEL;
	public String mSystemType = android.os.Build.VERSION.RELEASE;
	public String mPhonrNumber;
	/*----------------------------------------------------------------*/
	public String Post2S2SaveStatus;
	/*----------------------------------------------------------------*/
	public FilesWithParams(String f,Context ctx)
	{
		/*文件名*/
		if(f!=null)
		this.mFileID=f;
		/*手机号*/
		if(ctx!=null){
			this.mPhonrNumber=LibCui.GetPhoneNumber(ctx);
			this.userName=SaveParam.GetQinUserName(ctx);
		}
	}
	/*----------------------------------------------------------------*/
	public void SetPhoneNumber(String str)
	{
		this.mPhonrNumber=str;
	}
	public void SetPrinterColor(boolean IsColor)
	{
		this.mIsColor=(IsColor==true)?PrintColor.Color:PrintColor.Gray;
	}
	/**
	 * set print copies
	 * 
	 */
	public void SetPrinterCopies(int copies)
	{
		if(copies>0){
		     mPrintCopies=copies;//份数
		}else{
			 mPrintCopies=1;
		}
	}
	/**
	 * 
	 * 
	 */
	public String GetPrinterCopies()
	{
	/*	if(copies>0){
		     mPrintCopies=copies;//份数
		}else{
			 mPrintCopies=1;
		}*/
		
		return  String.valueOf(mPrintCopies);
	}
	/**
	 * 
	 * 
	 * 
	 */
	public boolean IsColor(){
		return mIsColor==PrintColor.Color;
	}
	/**
	 * 
	 * 
	 */
	public boolean IsDuplex(){
		return mIsDuplex==IsDouble.DoublePaper;
	}
	/**
	 * 
	 * 
	 */
	public void SetPrinterDuplexPaper(boolean duplex)
	{
		mIsDuplex=(duplex==true)?IsDouble.DoublePaper:IsDouble.OnePaper;
	}
	/**
	 * 获取文件全路径
	 * 
	 */
	public String GetFileShortName()
	{
		if(this.isFile()){
			 return this.mFileID;	
		}else{			
		  return	UserInfoOperation_CloudDisk.GetFileName(this.mFileID);
		}
	    
	}
	/**
	 * 获取文件全路径
	 * 
	 */
	public String GetFileLocal()
	{
		if(this.isFile()){
			 return this.mFileID;	
		}else{			
		  return	UserInfoOperation_CloudDisk. GetUUIDFileNameFullPath(this.mFileID);
		}
	    
	}
	/**
	 * 
	 */
	public String getFileID()
	{
	  return this.mFileID;	
	}
	public File GetF_File()
	{
	  return new File(this.mFileID);	
	}
	public String getParamString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("打印份数： ");sb.append(String.valueOf(this.mPrintCopies));sb.append("\r\n");
		sb.append("纸张类型： ");sb.append("自动适应");sb.append("\r\n");
		sb.append("打印色彩： ");sb.append((mIsColor==PrintColor.Color)?"彩色":"黑白");sb.append("\r\n");
		sb.append("打印范围： ");sb.append(this.mPrintRange);sb.append("\r\n");
		sb.append("双面打印： ");sb.append((mIsDuplex==IsDouble.DoublePaper)?"是":"否");
		return sb.toString();
	}
/**
* 
*/
	public boolean isFile()
	{
		return LibCui.IsStringFile(this.mFileID);
	}
/**
* 
*/	
   public UserInfoNetFile getNetFileByID()
    {	   
	   return  UserInfoOperation_CloudDisk.GetNetFile(this.mFileID);
	    
	}
/**
* 
*/
public String getUserName() {
	return userName;
}
/**
 *
 */
/*public String getPrinterName() {
	return printerName;
}
public void setPrinterName(String printerName) {
	this.printerName = printerName;
}*/
/**
 * 
 * 
 * 
 */
public String ToJsonStr(){
	JSONObject jo=new JSONObject();
	try {
		/*----*/
		jo.put("printid", this.orderId);
		jo.put("username", this.getUserName());
		jo.put("price2pay", this.price2pay);
		jo.put("printername", this.getPrinter());
		/*----*/
		jo.put("ppcopies", this.GetPrinterCopies());
		jo.put("ppcolor", this.IsColor());
		jo.put("pppapersize", this.mPaperSize.toString());
		jo.put("pprange", "全部");
		jo.put("ppduplex",this.IsDuplex());
		/*----*/
		jo.put("phonetype",this.mPhoneType );
		jo.put("phonenumber",this.mPhonrNumber);
		jo.put("systemtype", this.mSystemType);
		/*----*/
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	return jo.toString();
	
}
/**
 * 
 * 
 * 
 */
public String getPrinter() {
	return Printer;
}
public void setPrinter(String printer) {
	Printer = printer;
}
}
