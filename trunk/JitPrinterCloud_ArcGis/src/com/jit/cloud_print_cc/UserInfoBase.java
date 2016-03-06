package com.jit.cloud_print_cc;


import java.io.ByteArrayOutputStream;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import java.util.ArrayList;

import java.util.HashMap;

import com.jit.cloud_print_cc.FileSendOnNet.NowSendPercent;

import android.content.Context;



public class UserInfoBase implements Serializable
{
	 /**
	 * 
	 */
	private static final long serialVersionUID = -8497641791925231823L;
	/*-----------------------------*/
	 protected Context mContext=null;
	 protected NowSendPercent mNowSendPerCent;
	 public final ArrayList<String> mErrorStrArray=new ArrayList<String>();
	  /*-----------------------------*/
	  public enum Seox{
		  BOY,
		  GIRL
	  }
	  /*-----------------------------*/
	  protected String mUserName=null;
	  protected String mPassword=null;
	  public String mEmail=null;
	  public Seox mSex=null;
	  public String mTrueName=null;
	  public String mPhoneNumber=null;
	  /*--------PCL 文档解析---------------------*/
      public final String mUrl;
      public final String mPort;
	  public final String mRegisterUserUrl;
	  public final String mPostFileUrl;
	  public final String mDownLoadFileUrl;
	  public final String mRegisterPrinterUrl;
	  /*--------手机下单注册---------------------*/
	  public final String _mRegisterQinUrl;/**<注册用户*/
	  public final String _mloginQinUrl;/**<登录*/
	  public final String _mForgetPasswordQinUrl;/**<忘记密码*/
	  public final String _mGetOrderListUrl;/**<订单列表*/
	  public final String _mGetDiskListUrl; /**<订单列表*/
	  public final String _mPostFileQinUrl;/**<上传*/
	  public final String _mDownloadFileQinUrl;/**<下载*/
	  public final String _mDeleteFileQinUrl;/**<Delete file*/
	  public final String _mMobileMakeOrderUrl;/**<*/
	  public final String _mMobileMakeOrderUrl_v2;/**<*/
	  public final String _mMobilePostFileIDHandlerUrl_v2;/**<*/
	  /*-----------------------------*/
	  protected HashMap<String,String> mFilesIdOnServer=new HashMap<String,String>();//文件在服务器上的ID
	  protected HashMap<String,String> mPCLPathOnPhone=new HashMap<String,String>();//PCL文件在本机的路径
	  /*------------------------------*/
	  UserInfoBase(Context context)
	  {		
		  this.mContext=context;
		  this.InitUserNamePassword();
		  this.mUrl=context.getString(R.string.Server_host_url);
		  this.mPort=context.getString(R.string.Server_port);
		  String header=mUrl+":"+this.mPort;
		  /*云打印接口*/
		  this.mRegisterUserUrl=header+context.getString(R.string.Server_host_url_register_user);
		  this.mPostFileUrl=header+context.getString(R.string.Server_host_url_send_file);
		  this.mDownLoadFileUrl=header+context.getString(R.string.Server_host_url_download_file);
		  this.mRegisterPrinterUrl=header+context.getString(R.string.Server_host_url_register_printer);
		  /*亲打印接口*/
		  _mForgetPasswordQinUrl=null;
		  this._mRegisterQinUrl=header+context.getString(R.string.Server_qin_url_reg);
		  this._mloginQinUrl=header+context.getString(R.string.Server_qin_url_login);
		  this._mGetOrderListUrl=header+context.getString(R.string.Server_qin_url_GetOrderList);
		  this._mGetDiskListUrl=header+context.getString(R.string.Server_qin_url_GetDiskList);
		  this._mPostFileQinUrl=header+context.getString(R.string.Server_qin_url_SendFile2Cloud);
		  this._mDownloadFileQinUrl=header+context.getString(R.string.Server_qin_url_DownFileFromCloud);
		  this._mDeleteFileQinUrl=header+context.getString(R.string.Server_qin_url_DeleteFileFromCloud);
		  this._mMobileMakeOrderUrl=header+context.getString(R.string.Server_qin_url_MobileMakeOrders);
		  this._mMobileMakeOrderUrl_v2=header+context.getString(R.string.Server_qin_url_MobileMakeOrders_v2);/**<*/
		  this._mMobilePostFileIDHandlerUrl_v2=header+context.getString(R.string.Server_qin_url_MobilePostFileIDUrl_v2);/**<*/
	  
	  }
	  /**
	   * 
	   */
	   public void InitUserNamePassword()
	   {
		      this.mUserName=SaveParam.GetQinUserName(mContext);
		      this.mPassword=SaveParam.GetQinUserPassword(mContext);
		      if("".equals(this.mUserName)||"".equals(this.mPassword)){
		    	  this.mUserName="zhangxiaochen";
				  this.mPassword="123123";
		      }
		    
		   
	   }
	  /**
	   * 
	   *@param url_str 页面路径
	   *@param Json_str 传递的数据
	   *@parma type  Post Get 方式选择
	   * 
	   */
	  public String postJson(String url_str,String Json_str,String type)  
	  {
		 
		  String params =Json_str; 
		  byte[] data = params.getBytes();

		  URL url;
		  HttpURLConnection conn = null;
		 try {
			 url = new URL(url_str);
			 conn = (HttpURLConnection) url.openConnection();
			 conn.setConnectTimeout(3000);
			  //这是请求方式为POST
			  conn.setRequestMethod(type);
			  //设置post请求必要的请求头
			  conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// 请求头, 必须设置
			  conn.setRequestProperty("Content-Length", data.length + "");// 注意是字节长度, 不是字符长度

			  conn.setDoOutput(true);// 准备写出
			  conn.getOutputStream().write(data);// 写出数据
			  conn.getOutputStream().flush();
	         // conn.getOutputStream().close();
			  if(conn.getResponseCode() == 200){
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
		            return string;
			  }else{
				    return "false";
			  }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			 try {
				 if(conn!=null)
				conn.getOutputStream().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 try {
				 if(conn!=null)
				conn.getInputStream().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "false";
		 
	 }
 /**
 * 
 * 
 * 
 * */
	  protected Context getContext()
		 {
				// TODO Auto-generated method stub
				return this.mContext;
		 }
  /**
   * 
   * 
   * 
   * */
	  public String GetJson2Server(String url_str,String Json_str)  
	  {
		 
		  String params =url_str+Json_str; 
		  

		  URL url;
		  HttpURLConnection conn = null;
		 try {
			 url = new URL(params);
			 conn = (HttpURLConnection) url.openConnection();
			 conn.setConnectTimeout(3000);
			  //这是请求方式为POST
			  conn.setRequestMethod("GET");
			  //设置post请求必要的请求头
			  conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// 请求头, 必须设置

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
		            return string;
			  }else{
				    return "false";
			  }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mErrorStrArray.add(e.toString());
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mErrorStrArray.add(e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mErrorStrArray.add(e.toString());
		}finally{
			 
		}
		return "false";
		 
	 }
  /**
   * 
   * 送网站上获取PCL文档
   * 
   * 
   * */	  
	  public boolean GetPCLFileOnServer(String url_t,String fileSavePath,String FilefullPath)
	  {
		       String FileIdOnServer=this.mFilesIdOnServer.get(FilefullPath);

		       FileOutputStream fos=null;
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
								            	  
								            	  if(mNowSendPerCent!=null){
								                		this.mNowSendPerCent.NowSendPerrSent((persent++%100),777);
								                }
								            }		
								            fos.flush();
								            fos.close();
								            is.close();
							        	 
								            double FileSize=new File(FilefullPath).length();
								            if(filesize_total_pcl<FileSize*0.5){
								            	mErrorStrArray.add("正在装换文件到PCL");
								            //	Toast.makeText(getContext(),GetErrorDes(),Toast.LENGTH_LONG).show();
								            	return false;
								            }else{
								            	 mPCLPathOnPhone.put(FilefullPath,fileSavePath);  //PCL文件在本机的路径
								        	     mPCLPathOnPhone.put(FileIdOnServer,fileSavePath);  //PCL文件在本机的路径
								        	     mErrorStrArray.add("PCL文件下载成功");
								        	  //   Toast.makeText(getContext(),GetErrorDes(),Toast.LENGTH_LONG).show();
								        	     return true;
								            }
								            
							  }else{
								  this.mErrorStrArray.add("Http:响应"+ String.valueOf(http_response_code));
							  }
		        } catch (Exception e) {
		            e.printStackTrace();
		            this.mErrorStrArray.add(e.toString());
		            return false;
		        }finally{
		        	 try {
						  if(fos!=null)  fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		  return false;
	  }
	  /**
	   * 
	   * 
	   */
	   protected void Toast_MakeText_show(String msg,int dur) {
			// TODO Auto-generated method stub
	    	if(this.mContext!=null){
	    		//Toast.makeText(this.mContext,msg,dur).show();
	    	}
	    	
		}
	   public String GetErrorDes()
	   {
		   String Des="";
		  
		   for(int i=0;i< mErrorStrArray.size();i++){
			   if("\n".equalsIgnoreCase(mErrorStrArray.get(i))||"".equals(mErrorStrArray.get(i))){
				   Des+="\n";	   
			   }else{
				   Des+="\n"+Integer.toString(i)+"."+" "+ mErrorStrArray.get(i); 
			   }
		   }
		   Des+="\n";
		   return Des;
	   }
	   protected void NowSendPercent(double Percent,double Speed)
	   {
		 if(this.mNowSendPerCent!=null){
			 this.mNowSendPerCent.NowSendPerrSent(Percent, Speed);
		 }   
	   }
	   public void SetNowSendPercent(NowSendPercent NowSendPerCent)
	   {
		   this.mNowSendPerCent=NowSendPerCent;
	   }
	   /**
	    * 获取用户名
	    * */
	   public String GetUserName()
	   {
		   return mUserName;
	   }
	   /**
	    * 设置用户名
	    * 
	    */
	   public void SetUserName(String UserName)
	   {
		   if(UserName!=null)
		   this.mUserName=UserName;
	   }
	   /**
	    * 获取密码---
	    * */
	   public String GetPassword()
	   {
		   return mPassword;
	   }
	   /**
	    * 设置密码
	    */
	   
	   public  void SetPassword(String password)
	   {
			 if(password!=null){
				 	   this.mPassword=password;
			 }	   
	   }
	   
	   
}
