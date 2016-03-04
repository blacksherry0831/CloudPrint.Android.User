package com.jit.cloud_print_cc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class sendMsg2Reponse {
	InetAddress Ia;
	byte[]  msg;
	int port;
	sendMsg2Reponse(InetAddress ia,int port,byte[]  msg)
	{
		this.Ia=ia;
		this.port=port;
		this.msg=msg;
		new Thread(new Send()).start();
	}
	 class Send implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			this.SendThread();
		}
		private boolean IsReachable()
		{
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
					 
						try {
							
							socket.setBroadcast(isM);
							socket.send(packet);
						} catch (SocketException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				   			   
			
			} catch (SocketException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}finally{
					if(socket!=null)
					socket.close();
			}
		}
		
	}
	
}
