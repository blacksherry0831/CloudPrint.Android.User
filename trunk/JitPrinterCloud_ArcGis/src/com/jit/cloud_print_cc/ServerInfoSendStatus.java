package com.jit.cloud_print_cc;
import java.io.File;
import java.util.ArrayList;

import android.content.Context;


import com.jit.cloud_print_cc.ServerInfo;


public class ServerInfoSendStatus 
{
	   public interface TransferStart{
			void Start(Context ct);
		}
		public interface TransferendEndOne{
			void EndOne(Context ct,ServerInfo si,double percent);
		}
		public interface TransferDone{
			void Done(Context ct,SendFilePackage sfp);
			void Done();
		}
	/**
	 * 
	 * 发送文件包裹
	 * 
	 * 
	 **/	
	  public  static class SendFilePackage 
	    {
	       public ArrayList<ServerInfo> ServerInfoSet = new ArrayList<ServerInfo>();
	       public ArrayList<String> PathSet = new ArrayList<String>();
	       public ArrayList<String> SendSuccess = new ArrayList<String>();
	       public ArrayList<String> SendEsc = new ArrayList<String>();
	       public ArrayList<String> SendAbort = new ArrayList<String>();
	       public SendFilePackage()
	       {
	    	   
	       } 
	       public String getFileName(String pathandname){  
	           
	           File f=new File(pathandname);
	            return f.getName();
	             
	       }  
	       public String GetSendInfo()
	       {
	           String a = "发送成功：";
	           for (String ss : SendSuccess) 
	           {
	        	   String s=getFileName(ss);
	               a += ("\n"+s);
	           }
	           a += ("\n"+ String.valueOf(SendSuccess.size())+"个文件"+"\n");
	           ///////////////////////////////////
	           a += ("\n" + "取消发送: ");
	           for (String ss : SendEsc)
	           {
	        	   String s=getFileName(ss);
	               a += ("\n" + s);
	           }
	           a += ("\n"+ String.valueOf(SendEsc.size())+"个文件"+"\n");
	           ////////////////////////////
	           a += ("\n" +"发送失败: ");
	           for (String ss : SendAbort)
	           {
	        	   String s=getFileName(ss);
	               a += ("\n" + s);
	           }
	           a += ("\n"+ String.valueOf(SendAbort.size())+"个文件"+"\n");
	           return a;
	       }
	    }
  /**
   * 
   * 发送文件线程
   * 
   * 
   **/
	 
	/**
	 * 
	 * 
	 * 
	 * 
	 **/
}
