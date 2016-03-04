package com.jit.cloud_print_cc;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class SendMsg2Notify 
{
	String ip_addr;
	byte[]  msg;
	int port;
	
	SendMsg2Notify(String ip_addr,byte[]  msg,int port)
	{
		this.port=port;
		this.msg=msg;
	    this.ip_addr=ip_addr;
		if(msg!=null&&ip_addr!=null){
			new Thread(new Send()).start();
		}
		
	}
	private void Release()
	{
	  this.ip_addr=null;
	  this.msg=null;
	}
	 class Send implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			/*if(IsReachable()){
			    this.SendThread();
			}*/
			 this.SendThread();
			Release();
		}
		private boolean IsReachable()
		{
			 InetAddress Ia=null;
				try {
					Ia = InetAddress.getByName(ip_addr);
				} catch (UnknownHostException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
		     if(Ia==null){
		    	 return false;
		     }else{
		    	 try {
					return Ia.isReachable(3000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
		     }
		   
		}
		public void SendThread()
		{
			   InetAddress Ia=null;
					try {
						Ia = InetAddress.getByName(ip_addr);
					} catch (UnknownHostException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
			   if(Ia==null) return;
			   
			   DatagramSocket socket=null;
			   
			   try {
				   boolean isM=false;
				   if(Ia!=null){
					   byte ipadd[]=Ia.getAddress();
					   if(ipadd!=null){
						   isM=(ipadd[ipadd.length-1]==-1);
					   }
				   }
				   DatagramPacket packet = new DatagramPacket(msg,msg.length);
				   packet.setAddress(Ia);
				   packet.setPort(port);
				   socket=new DatagramSocket ();
				   socket.setBroadcast(isM);
				   socket.send(packet);
						
			
			} catch (SocketException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
					if(socket!=null)
					socket.close();
			}
		}
		
	}
	
}
