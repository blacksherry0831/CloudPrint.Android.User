package com.jit.cloud_print_cc;

import java.io.Serializable;
import java.net.InetAddress;

public class ServerInfoBase  implements Serializable
{
	public final static int HOST_PC=1;
	public final static int HOST_PHONE=2;
	 /**
	 * 
	 */
	 private static final long serialVersionUID = -1195594850533292832L;
	 /*-----------------------打印机用----------------------*/
	 protected String       mWifiName="";
	 protected String       mMacAddress=""; 
	 protected String       mWifiMacAddress=""; 
	 /*------------------打印机用---------------------------*/
	 InetAddress mAddress;
	  int         mPort;
	  int         mUDPPort;
	 public int         mHostType;
	 public String      mHostPhoneType;
	 public String      mHostPhoneNameAlias;
	 public String      mHostPCName;
	 /*-----------------------------------------------------*/

	 public ServerInfoBase ()
	 {
		 
	 }
	 public ServerInfoBase (
			 InetAddress address,
			 int port,
			 int hosttype,
			 String phonetype,
			 String Name)
	 {
		 this.mAddress=address;
		 this.mPort=port;
		 this.mHostPhoneType=phonetype;
		 this.mHostType=hosttype;
		 if(hosttype==HOST_PC){
			this.mHostPCName=Name;
		 }else if(hosttype==HOST_PHONE){
			this.mHostPhoneNameAlias=Name;
		 }else{
			 
		 }
	 }
	 
	 public final int GetUdpPort(){
		 return this.mUDPPort;
	 }
	 
	 public final void SetUdpPort(int port){
		 this.mUDPPort=port;
	 }
	 public int getPort()
	 {
		 return this.mPort;
	 }
	 public int getHostType()
	 {
		 return this.mHostType;
	 }
	 public String getHostPCName()
	 {
		 return this.mHostPCName;
	 }
	 public String getHostPhoneType()
	 {
		 return this.mHostPhoneType;
	 }
	 public void SetBaseInfo(ServerInfoBase sib)
	 {
		 //this.SetHostName(sib.GetHostName());
		 /*-----------------------打印机用----------------------*/
		// mWifiName=si;
		// mMacAddress=""; 
		// mWifiMacAddress=""; 
		
			  this.mPort=sib.getPort();		
			  this.mHostType=sib.getHostType();		
			  this.mHostPCName=sib.getHostPCName();
			  this.mHostPhoneType=sib.getHostPhoneType();
			  this.mHostPhoneNameAlias=sib.getHostPhoneNameAlias();
			  //this.mIsMultiply=sib.getIsMultiply();
			  this.mMacAddress=sib.getMacAddress();
		 
		 /*------------------打印机用---------------------------*/
		/* mAddress;
		 mPort;
		 mUDPPort;
		 mHostType;
		 mHostPhoneType;
		 mHostPhoneNameAlias;
		 mHostPCName;*/
		 /*-----------------------------------------------------*/
	 }
	 private String getMacAddress() {
		// TODO Auto-generated method stub
		return this.mMacAddress;
	}
	
	private String getHostPhoneNameAlias() {
		// TODO Auto-generated method stub
		return this.mHostPhoneNameAlias;
	}
	public String GetHostName()
     {
		 if(this.mHostType==2){
	         return  this.mHostPhoneType;
		 }else if(this.mHostType==1){
	         return  this.mHostPCName;
		 }else{
	         return "no devices";
		 }

     }
	 public void SetHostName(String name)
     {
		 if(this.mHostType==2){
	         
		 }else if(this.mHostType==1){
			 this.mHostPCName=name;
		 }else{
	       
		 }

     }
}
