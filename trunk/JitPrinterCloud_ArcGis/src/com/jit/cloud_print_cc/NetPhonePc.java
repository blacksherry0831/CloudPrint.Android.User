package com.jit.cloud_print_cc;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import com.jit.cloud_print_cc.CloudPrintAddressBase.PrintFrom;
import com.jit.cloud_print_cc.CloudPrintAddressBase.PrintType;




import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.util.Xml;

@SuppressWarnings("unused")



public class NetPhonePc {
	public  interface IRcvFileNotify
	 {
	 	public void WhenRecvFile();
	 }
	public   final static int UDP_NOTIFY_INTERVAL=5000;
	//public final static  int[] mUdpSetverPort=new int[]{10001,20002,30003};
    //public final static  int[] mTcpSetverPort=new int[]{10001,20002,30003};
	public final static  int[] mUdpSetverPort=new int[]{30003};
    public final static  int[] mTcpSetverPort=new int[]{30003};
	private   IRcvFileNotify  mRcvFile=null;
	private   Handler mHandler = new Handler();
	private   boolean mThreadRun=true;
	private   NetFindfTransfer   mNetFindTransfer;
	private   NetPrinterTool     mNetPrinterTool;
	private   Context           mContext;
	/*
	 *手机 电脑 打印机 列表 
	 **/
	public  PCPhonePrinterListItem    mPCPhonePrinterList;
	/**
	 *接受文件队列 
	 * 
	 */
	private   Queue<String> mFileRcvQuere = new ConcurrentLinkedQueue<String>();
	public NetPhonePc(Context ctx){
		this.mContext=ctx;
		this.mPCPhonePrinterList=new PCPhonePrinterListItem(ctx);
		this.mNetFindTransfer=new NetFindfTransfer();		
		this.mNetPrinterTool=new  NetPrinterTool(ctx,new NetPrinterFinder());
		
	}
	public Queue<String> GetFileRcvQuere()
	{
		return  mFileRcvQuere; 
	}
 	public void Destory()
	{
		this.mThreadRun=false;
		this.mNetFindTransfer.Destory();
		this.mNetFindTransfer=null;
		this.mPCPhonePrinterList.Destory();
		this.mPCPhonePrinterList=null;
		this.mNetPrinterTool.Destory();
		this.mNetPrinterTool=null;
		this.mThreadRun=false;
	}
	public void SetIRcvFileNotify(IRcvFileNotify notify)
	{
		this.mRcvFile=notify;
	}
	public void UDP_Broadcast_MSG_Request()
	{
		
		new Thread(new UDP_Broadcast_Thread()).start();
		
	}
    public class UDP_Broadcast_Thread implements Runnable
    {

		@Override
		public void run() {
			
			if(mPCPhonePrinterList!=null){				
				mPCPhonePrinterList.RemoveUnUsePrintersFromPc();
				mPCPhonePrinterList.addItem(new ServerInfo(mContext).GetDefaultLocalPC());
			}
			// TODO Auto-generated method stub
			if(mNetFindTransfer!=null&&mThreadRun){
				mNetFindTransfer.UdpNotifyMsg_Request( LibCui.GetAllWifiIPList(mContext));
				//通知局域网主机，已接入WIFI
			}
			if(mNetPrinterTool!=null){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// mNetPrinterTool.ScanThreadStart();
			}			
			
		}
    	
    }
	public class NetFindfTransfer{
			  
		        UDPNotifYServer mUdpNotifYServer;
		        DatagramSocket  mSocketUDPNotifYServer;
		        ServerSocket    mSocketTcpRecvServer;
		        TcpRecvServer   mTcpRecvServer;
		        UDPNotifYServerlistener mUdpNotifYServerlistener;
		        public NetFindfTransfer()
		        {
		            this.StartTcpServer();
		            this.StartUdpServer();
		            this.StartUdpNotifySelf();
		            
		        }
			   public void Destory()
			   {
				   mThreadRun=false;
			   }
			   public void StartTcpServer() 
			   {
				    this.mTcpRecvServer=new  TcpRecvServer();
			   }
			   public void StartUdpServer()
			   {
				   this.mUdpNotifYServerlistener=new UDPNotifYServerlistener();
			   }
			   public void StartUdpNotifySelf() 
			   {
				   this.mUdpNotifYServer=new UDPNotifYServer();
			   }
			   /*
			   public void UdpNotifyMsg(InetAddress[] addrArray,boolean IsM)
		       {
				      for(InetAddress addr : addrArray){
				    	   byte[] buf = this.GetNotifyMessageXml(true);
				    	   for (int i = 0; i < mUdpSetverPort.length; i++)
				              {				                
				                  SendMsg2Notify sm2n = new SendMsg2Notify(addr.toString(),buf,addr);
				              }
				      }
		       }*/
				public  void UdpNotifyMsg_Response(InetAddress ia,int port)
			      {
					   /*---------------------------------------*/				  
					   byte data[]=GetNotifyMessageXml(false);	
					   for (int i = 0; i < mUdpSetverPort.length; i++){
						   	sendMsg2Reponse sm2n = new sendMsg2Reponse(ia,mUdpSetverPort[i],data);	
					   }
			      }
			   public void UdpNotifyMsg_Request(ArrayList<String> addrArray)
		       {
				   byte[] buf = this.GetNotifyMessageXml(true);
				      for(String addr : addrArray){
				    	  for (int i = 0; i < mUdpSetverPort.length; i++){
				    		  SendMsg2Notify sm2n = new SendMsg2Notify(addr,buf, mUdpSetverPort[i]);
				    		  try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				    	  }
							
							
				       }
				      
		       }
			public byte[] GetNotifyMessageXml(boolean IsMulity) 
				{
					ServerInfo si=ServerInfo.GetSelf(mContext);
					byte[] buf = new byte[5];
					XmlSerializer serializer = Xml.newSerializer();   
					StringWriter writer = new StringWriter();
					try{  
						serializer.setOutput(writer); 
						serializer.startDocument("UTF-8",true);
						serializer.startTag("","CloudPrint"); 
						serializer.startTag("","Notify");
						/*----------------------------------------------------------------------*/
						serializer.startTag("","Port");
						int port_t=-2;
						if(NetFindfTransfer.this.mSocketTcpRecvServer!=null){
							port_t=NetFindfTransfer.this.mSocketTcpRecvServer.getLocalPort();
						}
					    serializer.text( Integer.toString(port_t));						
						serializer.endTag("","Port");
						/*------------------------------------------------------------------------*/
						serializer.startTag("","HostType");
						serializer.text(String.valueOf(si.mHostType));
						serializer.endTag("","HostType");
						/*--------------------------------------*/
						/*--------------------------------------*/
						serializer.startTag("","HostName");
						serializer.text("not");
						serializer.endTag("","HostName");
						/*--------------------------------------*/
						/*------------------------------------------------------------------------*/
						serializer.startTag("","PhoneType");
						serializer.text(si.mHostPhoneType);
						serializer.endTag("","PhoneType");
						/*--------------------------------------*/
						/*------------------------------------------------------------------------*/
						serializer.startTag("","PcName");
						serializer.text("not");
						serializer.endTag("","PcName");
						/*--------------------------------------*/
						serializer.startTag("","Multiply");
						if(IsMulity){
							serializer.text("true");
						}else{
							serializer.text("false");
						}						
						serializer.endTag("","Multiply");
						/*------------------------------------------------------------------------*/
						serializer.startTag("","PhoneNameAlias");
						serializer.text(si.mHostPhoneNameAlias);
						serializer.endTag("","PhoneNameAlias");
                        /*------------------------------------------------------------------------*/
						serializer.startTag("","MachineMac");
						serializer.text(LibCui.getWifiLocalMacAddress(mContext));
						serializer.endTag("","MachineMac");
						/*------------------------------------------------------------------------*/
						serializer.endTag("","Notify");
						serializer.endTag("","CloudPrint");
						serializer.endDocument();  
						/*--------------------------------------*/
						String xml_doc=new String(writer.toString());
					    buf=xml_doc.getBytes("UTF-8");
					}catch(Exception e){
						return null;
					}  
					
				    return buf;
				}


            public class UDPNotifYServerlistener implements Runnable{
	   
	       Thread udpNotifyThread;
	       private void initSocket()
		      {
		    	  for(int i=0;i<mUdpSetverPort.length;i++){
		    		  try {
		    			  mSocketUDPNotifYServer = new DatagramSocket(mUdpSetverPort[i]);
							if(mSocketUDPNotifYServer!=null){
								mSocketUDPNotifYServer.setSoTimeout(2000);
								break;
							}
						  } catch (SocketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						  }
		    	  }
		      }
	       public UDPNotifYServerlistener()
		      {
		    	 this.initSocket();
		    	  if(mSocketUDPNotifYServer!=null) {
			    	  this.udpNotifyThread=new Thread(this);
			    	  this.udpNotifyThread.start();
		    	  }
		      }
	   @Override
		   public void run()
	   {
		   byte data[] = new byte[1024];
			   while(mThreadRun){
				   Arrays.fill(data, (byte)0);
				   DatagramPacket packet = new DatagramPacket(data, data.length);	
				   ServerInfo si=new  ServerInfo(mContext);
				  
				   try {
						mSocketUDPNotifYServer.receive(packet);
						InetAddress ia=packet.getAddress();
						 int port =packet.getPort();
						 si.SetUdpPort(port);
						
						 
						if(si.ParseNotifyMessageXml(packet.getData(), packet.getAddress())&&mPCPhonePrinterList!=null){
							mPCPhonePrinterList.addItem(si);
							if(si.IsMulitiply()){									
								//回传								
								UdpNotifyMsg_Response(ia,port);					
							}else if("isalive".equals(si.GetMulitiply())){
								UdpNotifyMsg_Response(ia,port);
							}
						}
						
							
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if(e!=null)		e.printStackTrace();
					}
			   }						 
			mSocketUDPNotifYServer.close();
	   }
   }
		public class UDPNotifYServer implements Runnable{
	         Thread udpNotifyThread;
	      public UDPNotifYServer()
	      {
	    	 
	    		  udpNotifyThread=new Thread(this);
	    		  udpNotifyThread.start();
	      }
	   
		    @Override
			public void run()
		    {
			   boolean mIsSend=false;			   
			   try {
					Thread.sleep(1000);
				   } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				   }
			   
				   while(mThreadRun){
								   if(LibCui.isWifiEnabled(mContext)&&mIsSend==false){
									   mIsSend=true;
									   UdpNotifyMsg_Request(LibCui.GetAllWifiIPList(mContext));
								   }
								   if(LibCui.isWifiEnabled(mContext)==false){						 
									   mIsSend=false;
										   try {
											Thread.sleep(100);
										   } catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										   }
								   }			
					 
		          }						 
			
	
	 }
			
			
			
			 
}
		public class TcpRecvServer implements Runnable
		{
		 	Thread mTcpThread;
			public TcpRecvServer()
			{
				this.initSocket();
				if(mSocketTcpRecvServer!=null) {
					  mTcpThread=new Thread(this);
					  mTcpThread.start();
		    	  }
				 
			}
			private void initSocket()
			{
				for(int i=0;i<mTcpSetverPort.length;i++){
		    		  try {
		    			  //InetSocketAddress addr=new InetSocketAddress("0.0.0.0",mTcpSetverPort[i]);
		    			  mSocketTcpRecvServer= new ServerSocket(mTcpSetverPort[i]);
		    			  mSocketTcpRecvServer.setSoTimeout(3000);
							if(mSocketTcpRecvServer!=null){
								break;
							}
						  } catch (SocketException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
						  }catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
						  }
		    	  }
				
			}
		   @Override
			public void run()
		   {
			   while(mThreadRun){
				  try {
					Socket socket = mSocketTcpRecvServer.accept();
					if(socket!=null){
						Thread t=new Thread(new ProcessSocketRecv( socket));
						t.start();
					}				
				  } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				  }   
			   }
			   try {
				mSocketTcpRecvServer.close();
			   } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			   }
	       }
	
		}
		
/**
 * 
 * LibCui.GetMyPhoneDirectory();
 * 
 */	
		   public class ProcessSocketRecv implements Runnable
		   {
			   Socket socket_client=null;
			   DataInputStream input_stream=null;
			   FileOutputStream fos=null;
			   FileSendOnNet file;
			   public ProcessSocketRecv(Socket s)
			   {
				   this.socket_client=s;
			   }
			   @Override
			   public void run() {
				   try {
					   file=PhonePcCommunication.RcvFileFromClient(this.socket_client);
					   if((file!=null)&&
							(file.mFileFullPath!=null)&&
							(!"".equals(file.mFileFullPath))){
						 //文件接收完成
							//通知主界面
						   mFileRcvQuere.add(file.mFileFullPath);
						   
						   if(mThreadRun){							   
										   mHandler.post(new Runnable(){
			
												@Override
												public void run() {
													// TODO Auto-generated method stub
													mRcvFile.WhenRecvFile();
												}
												
											});
						   }

						   
					   }
				   } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				   }
				   this.Clear();
				 
					
			   }
				

			    private void Clear()
			    {
			    	try {
			    		if(input_stream!=null)
						input_stream.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	// TODO Auto-generated method stub
					try {
						if(socket_client!=null)
						this.socket_client.shutdownInput();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						if(socket_client!=null)
						this.socket_client.shutdownOutput();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						if(socket_client!=null)
						this.socket_client.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						if(this.fos!=null){
							
							this.fos.close();
						}						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			    }
			   
		   }
}
/**
 * 
 * 
 * 
 */
public class  NetPrinterFinder implements NetPrinterTool.WhenFindNetPrintInWifi
	{

				@Override
				public void AddPrinter(String AddrStr, 
						               PrintFrom type, 
						               ServerInfo parent,
						               PrintType PrintType) {
					// TODO Auto-generated method stub
			
									if(mPCPhonePrinterList!=null){
										
										CloudPrintAddress cpa=new CloudPrintAddress(AddrStr,type,parent,PrintType);
											
										mPCPhonePrinterList.AddOnePrinter(cpa);
										    
								  }
			
				
					
				}


      }
/**
 * 
 * 
 * 
 */
}
