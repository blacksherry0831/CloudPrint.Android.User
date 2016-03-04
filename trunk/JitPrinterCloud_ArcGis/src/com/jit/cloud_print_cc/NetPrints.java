package com.jit.cloud_print_cc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.jit.cloud_print_cc.CloudPrintAddressBase.PrintFrom;
import com.jit.cloud_print_cc.CloudPrintAddressBase.PrintType;




import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;



public class NetPrints 
{
	public   PrintListItem    mPrintListItem;
	public   NetTool          mNetTool;
	private  boolean          mThreadRun=true;
	@SuppressWarnings("unused")
	private  Context          mContext;
  
	
	public NetPrints(Context context) 
	{
		// TODO Auto-generated constructor stub
		this.mContext=context;
		this.mPrintListItem=new PrintListItem(context);
		this.mNetTool=new NetTool(context);
	
	}
	
	public void Destory()
	{
		this.mThreadRun=false;
	}
	

	public class NetTool implements Runnable  
	{

		private final int SERVERPORT = 161;
		private static final int TIMEOUT = 3000;   // 设置超时为3秒
		  private String locAddress;//存储本机ip，例：本地ip ：192.168.1.
	  
		  private int j;//存放ip最后一位地址 0-255
		   
		  private Context ctx;//上下文
		   
		  public NetTool(Context ctx){
		    this.ctx = ctx;
		    new Thread(this).start();
		  }
		 
		 

		 
		 
		  //向serversocket发送消息
		  public String sendMsg(String IP) {
		     
			  int server_port = SERVERPORT;
		        java.net.DatagramSocket s = null;
		        try {
		                s = new java.net.DatagramSocket();
		        } catch (SocketException e) {
		                e.printStackTrace();
		        }
		        InetAddress local = null;
		        try {
		                local = InetAddress.getByName(IP);
		        } catch (UnknownHostException e) {
		                e.printStackTrace();
		        }
		        byte[] defultbyte = new byte[] { (byte)0x30,(byte)0x4C,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x04,(byte)0x06,(byte)0x70
		        		,(byte)0x75,(byte)0x62,(byte)0x6C,(byte)0x69,(byte)0x63,(byte)0xA0,(byte)0x3F,(byte)0x02,(byte)0x02,(byte)0x01,(byte)0x1B
		        		,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x02,(byte)0x01,(byte)0x00,(byte)0x30,(byte)0x33,(byte)0x30,(byte)0x0F,(byte)0x06,(byte)0x0B
		        		,(byte)0x2B,(byte)0x06,(byte)0x01,(byte)0x02,(byte)0x01,(byte)0x19,(byte)0x03,(byte)0x02,(byte)0x01,(byte)0x05,(byte)0x01,(byte)0x05
		        		,(byte)0x00,(byte)0x30,(byte)0x0F,(byte)0x06,(byte)0x0B,(byte)0x2B,(byte)0x06,(byte)0x01,(byte)0x02,(byte)0x01,(byte)0x19,(byte)0x03
		        		,(byte)0x05,(byte)0x01,(byte)0x01,(byte)0x01,(byte)0x05,(byte)0x00,(byte)0x30,(byte)0x0F,(byte)0x06,(byte)0x0B,(byte)0x2B,(byte)0x06
		        		,(byte)0x01,(byte)0x02,(byte)0x01,(byte)0x19,(byte)0x03,(byte)0x05,(byte)0x01,(byte)0x02,(byte)0x01,(byte)0x05,(byte)0x00};
		       
		        java.net.DatagramPacket p = new java.net.DatagramPacket(defultbyte, defultbyte.length,
		                        local, server_port);
		        int DATA_LEN = 4096;  
		        byte[] inBuff = new byte[DATA_LEN];  
		        DatagramPacket inPacket = new DatagramPacket(inBuff , inBuff.length); 
		        try {
		        	s.setSoTimeout(TIMEOUT);//设置阻塞时间
		                s.send(p);
		                s.receive(inPacket);
		                s.close();
		                return "Yes";
		        } catch (IOException e) {
		                //e.printStackTrace();
		        	return "No";
		                
		        }
		  }
		   
		 
		 
		  /**
		   * 扫描局域网内ip，找到对应服务器
		   */
		  public String result="";
		  public String scan(){
		     
			  locAddress = getLocAddrIndex();//获取本地ip前缀
			  
			    if(locAddress.equals("")){
			      Toast.makeText(ctx, "扫描失败，请检查wifi网络", Toast.LENGTH_LONG).show();
			      
			    }
			     
			    for ( int i = 0; i < 256; i++) {//创建256个线程分别去ping
			       
			      j = i ;
			       
			      new Thread(new Runnable() {
			         
			        @Override
					public void run() {
			        	int timeOut = 5000; //定义超时，表明该时间内连不上即认定为不可达，超时值不能太小。
			        	String Ip=locAddress+ NetTool.this.j;
			            try {//ping功能
			                                    boolean status = InetAddress.getByName(Ip).isReachable(timeOut);
			                                    if(status){
			                                    	Log.d("UDP", "UsedIP = " + status+Ip);
			                                    	if(sendMsg(Ip)=="Yes")
			                                    	{
			                                    		//Log.d("UDP", "Printer = " +Ip);
			                                    		/*
			                                    		synchronized(result){
			                                    			result+=Ip+"&";
			                                    		}*/
			                                    		if(mPrintListItem!=null){
			                                    			synchronized(mPrintListItem){
				                                    			 mPrintListItem.AddOnePrinter(new  CloudPrintAddress(
				                                    					 Ip,
				                                    					 PrintFrom.Self,
				                                    					 ServerInfo.GetSelf(mContext),
				                                    					 PrintType.NetPrint));
				                                    		}
			                                    		}
			                                    		
			                                    		
			                                    	}
			                                    }
			                                    
			                            } catch (UnknownHostException e) {
			                                    // TODO Auto-generated catch block
			                                    e.printStackTrace();
			                            } catch (IOException e) {
			                                    // TODO Auto-generated catch block
			                                    e.printStackTrace();
			                                   
			                            }//
			           

			        }
			      }).start();
			       
			    }
		     return result;
		  }
		 
		   
		  //获取本地ip地址
		  public String getLocAddress(){
		     
			  WifiManager wifimanage=(WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);//获取WifiManager  
			  
			//检查wifi是否开启  
			  if(!wifimanage.isWifiEnabled()){
				  wifimanage.setWifiEnabled(true);
			  }
			
			  
			WifiInfo wifiinfo= wifimanage.getConnectionInfo();  
			  
			String ip=intToIp(wifiinfo.getIpAddress());  
			  
			  return ip;
			
		 
		  }

		  private String intToIp(int hostip)  {
			  return (hostip & 0xFF)+"."+((hostip>>8)&0xFF)+ "." + ((hostip >> 16 ) & 0xFF) +"."+((hostip >> 24 ) & 0xFF);  
		  }
		   
		  //获取IP前缀
		  public String getLocAddrIndex(){
		     
		    String str = getLocAddress();
		     
		    if(!str.equals("")){
		      return str.substring(0,str.lastIndexOf(".")+1);
		    }
		     
		    return null;
		  }
		   
		  //获取本机设备名称
		  public String getLocDeviceName() {
		     
		    return android.os.Build.MODEL;
		     
		  }





		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(mThreadRun){
				this.scan();
				try {
					Thread.sleep(40000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
			}

		}


	   }
}
