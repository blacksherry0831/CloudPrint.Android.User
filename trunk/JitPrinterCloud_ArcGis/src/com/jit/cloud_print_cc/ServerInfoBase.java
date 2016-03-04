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
	 
}
