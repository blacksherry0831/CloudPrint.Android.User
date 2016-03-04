package com.jit.cloud_print_cc;

import java.io.ByteArrayInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.net.Socket;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jit.cloud_print_cc.FileSendOnNet.NowSendPercent;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class ServerInfo  extends ServerInfoBase implements Serializable
{
	
    private static final boolean USE_ICMP=false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	 private  boolean IsClone=false;
	 private String   mIsMultiply;
	 private Thread     mSendThread;
	 private	NowSendPercent mNowSendPercent;
	 public volatile boolean mFileSendSuccess=false;
     public volatile boolean mFileSendAbort = false;
     public volatile boolean mFileSendEsc=false;
     private Socket mSocketClient=null;
     Context  mContext;
    // PrintListItem mPrinterList=new PrintListItem();
     public long  mTimeAlive=System.currentTimeMillis();
     
     public ServerInfo(InetAddress address, int port, int hosttype,
 			String phonetype, String Name) {
 		super(address, port, hosttype, phonetype, Name);
 		// TODO Auto-generated constructor stub
 	}
     public ServerInfo(Context  context)
     {
    	 
    	  super();
  		  this.mContext=context;
    	 
  		
  	}
     public final ArrayList<String> mErrorStrArray=new ArrayList<String>();
     public final String FileSendSuccess="文件发送成功！";
	 public boolean ParseNotifyMessageXml(byte[] msg,InetAddress addr)
      {
		 this.mAddress=addr;
		 if(mContext!=null){
	  		
	  			this.mWifiMacAddress=LibCui.getWifiMac(mContext);
	  			this.mWifiName=LibCui.getWifiName(mContext);
	  					
	  		 }
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			 //String xml_doc=new String(msg,"UTF-8");
			
			  DocumentBuilder builder = factory.newDocumentBuilder();
              Document dom = builder.parse(new ByteArrayInputStream(msg));

              Element root =dom.getDocumentElement();
              NodeList notift_elm=root.getElementsByTagName("Notify");
              for (int i = 0; i <notift_elm.getLength(); i++) {
            	  Element element = (Element) notift_elm.item(i); 
            	  NodeList childNodes =  element.getChildNodes();  
            	  for(int j=0;j<childNodes.getLength();j++){  
            		  if(childNodes.item(j).getNodeType()==Node.ELEMENT_NODE){ 
            			  String value=childNodes.item(j).getFirstChild().getNodeValue();
            			  String NodesName=childNodes.item(j).getNodeName();
                			  if("Port".equals(NodesName)){
                				  this.mPort=Integer.parseInt(value);
                			  }else if("HostType".equals(NodesName)){
                				  this.mHostType=Integer.parseInt(value);
                			  }else if("HostName".equals(NodesName)){
                				  this.mHostPCName=value;
                			  }else if("PhoneType".equals(NodesName)){
                				  this.mHostPhoneType=value;
                			  }else if("PcName".equals(NodesName)){
                				  this.mHostPCName=value;
                			  }else if("PhoneNameAlias".equals(NodesName)){
                				  this.mHostPhoneNameAlias=value;
                			  }else if("Multiply".equals(NodesName)){
                				  this.mIsMultiply=value;
                			  }else if("MachineMac".equals(NodesName)){
                				  this.mMacAddress=value;
                			  }else{
                				  
                			  }
            			  }
            		  
            		  }
            	  }                      
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		 return true;
      }
	 public String GetKey()
      {
          return mAddress.toString();
      }
	 public String GetHostDes()
     {
		 if(this.mHostType==2){
	         return  this.mHostPhoneType+"  "+this.mHostPhoneNameAlias;
		 }else if(this.mHostType==1){
	         return  this.mHostPCName;
		 }else{
	         return "no devices";
		 }

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
	 public String GetDes()
     {
	
		 String AddressString=mAddress.toString().replace("/","");
		 String Des = null;
		 if(LibCui.getLocalIpAddressList().contains(this.mAddress)){
			 //本机手机
			 if(this.mHostType==1){
				 Des="fuck";
			 }else if(this.mHostType==2){
				 Des=	 "我的手机"+
						 "\n"+"手机别名："+mHostPhoneNameAlias+
						 "\n"+"手机型号："+android.os.Build.MODEL+
						 "\n"+"IP地址："+AddressString;
						
			 }else{
				 Des="fuck";
			 }
			
			
		 }else{
			 //其他设备
			 if(this.mHostType==1){
				 Des=	  "计算机名："+this.mHostPCName+ 
						  "\n"+"IP地址："+AddressString;
			 }else if(this.mHostType==2){
				 Des=    "手机别名："+mHostPhoneNameAlias+						 
						 "\n"+"手机型号："+mHostPhoneType+
						 "\n"+"IP地址："+AddressString;
				
			 }else{
				 Des="fuck";
			 }
			
		 }
		 
		
		
         return Des;
     }
	 @Override
	public boolean equals(Object o) {
			 if (o instanceof ServerInfo) {
				 ServerInfo s = (ServerInfo) o;
				 
				 return s.GetKey().equals(this.GetKey());
			 }
			 
		    return super.equals(o);  
	 }
	 
	 public  boolean SendFileOrDirectory(FilesWithParams  fileSet)
	 {
		 if(this.IsSendFileComplete()){
			 File f=new File(fileSet.GetFileLocal());
			 if(f.isFile()){
				 this.mSendThread=new Thread(new SendFile2Server(fileSet));
				 this.mSendThread.start();
				 return true;
			 }else if(f.isDirectory()){
				 return false;
			 }else{
				 assert false : "SendFileOrDirectory(String fileSet)";
			     return false;
			 }
			
		 }else{
			 return false;
		 }
		
		
	 }
	 public boolean IsSendFileComplete()
     {
        
         if (this.mSendThread == null)
         {
             return true;
         }
         else {
             return (this.mSendThread.getState()==Thread.State.TERMINATED);
         }
      
             
     }
	 public void SetSelfIpMacInfo(Context context)
	 {
		 if(context!=null){
		  		
	  			this.mWifiMacAddress=LibCui.getWifiMac(context);
	  			this.mWifiName=LibCui.getWifiName(context);
	  			this.mMacAddress=LibCui.getWifiLocalMacAddress(context);	
	  			this.mAddress=LibCui.getWifiIpAddress(context);
	  		 }
	 }
	 public static ServerInfo GetSelf(Context context)
	 {
		 ServerInfo  si=new ServerInfo(
				 null,
				 -1,
				 ServerInfoBase.HOST_PHONE,
				 android.os.Build.MODEL,
				 GetHostPhoneNameAlias());
		
		 si.SetSelfIpMacInfo(context);		 
	
		 return si;		 
	 
	 } 
	 /**
	  * 
	  * 
	  **/
	 public ArrayList<CloudPrintAddress> PrinterList()
	 {
		 
		 
		 ArrayList<CloudPrintAddress> printer=new ArrayList<CloudPrintAddress>();
		 
		 if(LibCui.getLocalIpAddressList().contains(mAddress)){
 			//本机
				//this.mMacAddress=LibCui.getWifiLocalMacAddress(mContext);
 			return  printer;
 		}else{
 			//其他设备
 			 Socket s=new Socket();
 			 try {
 				PhonePcCommunication ppc=new PhonePcCommunication(s,null,null,this.mAddress, mPort);
 			    printer=ppc.GetPrinterList();
 			    
 				s.shutdownInput();
 				s.shutdownOutput();
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 			if(s!=null){
		 			 try {
						s.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 			}
 		}		
		 if(printer!=null){
			    for(CloudPrintAddress cpa:printer){
			    	cpa.SetLocalParent(this);
			    }
		    }
		 return printer;
	 }
	 /**
	  * 向本机发送文件
	  * @author Administrator
	  *
	  */	 
	 class SendFile2Server implements Runnable
	 {
		 String tag = "SSendFile2Server";
		 FilesWithParams  mFile;
		 public SendFile2Server(FilesWithParams  file)
		 {
			 this.mFile=file;
		 }
		 private void init()
		 {
			 mFileSendSuccess=false;
		     mFileSendAbort = false;
		     mFileSendEsc=false;
		     mErrorStrArray.clear();
		 }
		 @Override
		 public  void run()
		 {
			
			this.init();
			  //取消发送置位  终止发送
	           if (mFileSendEsc == true)
	           {
	               return;
	           }else{
	        	 
	        	   try {
	        		   if(USE_ICMP){
	        			   if(mAddress.isReachable(PhonePcCommunication.ConnectTimeOut)==false){
		        			   mErrorStrArray.add("ICMP (ICMP ECHO REQUEST) RESPONSE");
		        			   mFileSendAbort=true;
		        			   return;
		        		   }	 
	        		   }
	        		   
	        		   mSocketClient=new Socket();
	        		   
	        		   PhonePcCommunication pcphonecomm= new PhonePcCommunication(
	        				   mSocketClient,
	        				   mFile,
	        				   null,
	        				   mAddress,
	        				   mPort);	
	        		   pcphonecomm.SetNowSendPercent(mNowSendPercent);
	        		   if(pcphonecomm.SendFile2Save()){
	        			   mErrorStrArray.add(FileSendSuccess+"\n"+mFile);
	        		   }else{
	        			   mErrorStrArray.add("Read ServerPermission Fail！");
	        		   }        		  
	        		   mSocketClient.shutdownInput();
	        		   mSocketClient.shutdownOutput();	
	        		   mFileSendSuccess=true;
	       		} catch (Exception e) {
	       			Log.i(tag, "socket执行异常：" + e.toString());
	       			mErrorStrArray.add(e.toString());
	       			mFileSendAbort=true;
	       		} finally {	       		   
	       				try {
	       					if(mSocketClient!=null)
							 mSocketClient.close();							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							mFileSendAbort=true;
							mErrorStrArray.add(e.toString());
						}
	       			  mSocketClient=null;
	       			
	       		}
	           }
		 }
	 }
	 /**
	  *设置手机别名
	  */
	 public static void SetHostPhoneNameAlias(String name)
	 {		 
		 LibCui.SaveString2Fille("name.txt",name);
	 }
	 /**
	  *获取手机别名
	 * @return 
	  */
	 public static String GetHostPhoneNameAlias()
	 {
		 if(IsSetPhoneNameAlias()==false){
			 return "别名未设置";
		 }
		 File path=LibCui.GetCloudPrintCfgFile();
		 File file=new File(path,"name.txt");
		 String result=null;
		 FileInputStream is = null;
		 try {
			     is = new FileInputStream(file);  
			     byte[] b = new byte[1024];  
			     is.read(b);  
			     result = new String(b);    
			     result=result.replaceAll("\000", "");
			 } catch (Exception e) {  
			     e.printStackTrace(); 
			     result ="读取别名出错";
			 }finally{
				 try {
					 if(is!=null) is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					 result ="读取别名出错";
				}
			 }  
		 return  result;
	 }
	 /**
	  *判断手机别名是否设置
	 * @return 
	  */
	 public static boolean IsSetPhoneNameAlias()
	 {
		 File path=LibCui.GetCloudPrintCfgFile();
		 File file=new File(path,"name.txt");
		  return  file.exists();
	 }
	 
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
	  public void Toast_Make_Text_Show_ErrorMsg(Context context)
	  {
		  int Toast_LENGTH=Toast.LENGTH_LONG;
		  if(this.mErrorStrArray.size()==1){			 
			  if(this.mErrorStrArray.get(0).contains(FileSendSuccess)){
				  //发送成功
				  Toast_LENGTH=Toast.LENGTH_SHORT;
			  }
		  }
		  
		  if(this.mErrorStrArray.size()>0&&context!=null){
			  Toast t= Toast.makeText(context,GetErrorDes(),Toast_LENGTH);
		         t.setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL,0,0);
		         t.show();
		  }
		  
		
		 
	  }
	  public ServerInfo Clear2Serializable() 
		{
			// TODO Auto-generated method stub
			this.mContext=null;
			mSendThread=null;
			mSocketClient=null;
			return this;
		
			
		}
		
	  public void SetNowSendPercent(NowSendPercent NowSendPercent)
	  {
			this.mNowSendPercent=NowSendPercent;
	  }
      
	  public boolean SendFile2Print(Socket s,FilesWithParams  fileName,String PrintName,UserOrderInfoGenerated order) throws Exception
	  {
		  PhonePcCommunication ppc=new PhonePcCommunication(
				  s,
				  fileName,
				  PrintName, 
				  this.mAddress,
				  this.mPort);
		  ppc.SetNowSendPercent(this.mNowSendPercent);
		  return ppc.SendFile2Print(order);
	  }
	  public boolean IsValidPrint() 
	  {
		  final int ConnectTimeOut=1000;
		  Socket  socketClient=new Socket();
		  try {
			socketClient.connect(new InetSocketAddress(mAddress,mPort),ConnectTimeOut);
		  } catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		  }
		  
		  try {
			socketClient.close();
		  } catch (IOException e) {
			 return false;
		  }
		  return true;
	  }
	  private void SetCloneFlag()
	  {
		 IsClone=true;
	  }
	
	  public ServerInfo Clone()
	  {
		  String Name = null;
		  if(this.mHostType==HOST_PC){
			     Name=this.mHostPCName;
			 }else if(this.mHostType==HOST_PHONE){
				 Name=this.mHostPhoneNameAlias;
			 }else{
				 
			 }
		  ServerInfo SI=new ServerInfo(this.mAddress,
				  this.mPort,
				  this.mHostType,
				  this.mHostPhoneType,
				  Name);
		  SI.mWifiName=this.mWifiName;
		  SI.mWifiMacAddress=this.mWifiMacAddress;
		  SI.mMacAddress=this.mMacAddress;
		  SI.SetCloneFlag();
		  return SI;
	  }
	  public boolean IsMulitiply()
	  {
		if("true".equals(this.mIsMultiply)){
			 return true;
		}else{
			 return false;
		}
	
	  }
	  public String GetMulitiply()
	  {
		
		return this.mIsMultiply;
	
	  }
	  
	  public static ServerInfo GetMyNetCloudDisk(Context ctx)
	  {
		  ServerInfo si=new ServerInfo(ctx);
		  return si;
	  }
}