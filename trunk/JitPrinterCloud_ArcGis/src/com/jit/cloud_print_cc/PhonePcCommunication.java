package com.jit.cloud_print_cc;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;

import com.jit.cloud_print_cc.FileSendOnNet;
import com.jit.cloud_print_cc.FileSendOnNet.NowSendPercent;
import com.ta.utdid2.android.utils.StringUtils;


@SuppressWarnings("unused")
public class PhonePcCommunication 
{
	
	 public enum  CommunicationType
     {
         Send2Receive,
         Send2Print,
         Send2PrintNoPay,
         Send2PrintPayed,
         pay4Order,
         QueryPrintList,
         GetPriceById,
         GetPrinterListXml,
         NULL
     }
	public final static int ConnectTimeOut=0;
	public final static int ReadTimeOut=0;
	Socket mSocketClient;
	FilesWithParams mFilename;
	String mPrinterName;
	InetAddress mAddress;
	int mPort;
//	UserOrderInfoGenerated mOrder;
	NowSendPercent mNowSendPercent;
	
	public PhonePcCommunication(Socket socketClient,
								FilesWithParams filename,
								String PrinterName,
								InetAddress mAddress,
								int mPort)
	{
		this.mSocketClient=socketClient;
		this.mFilename=filename;
		this.mPrinterName=PrinterName;
		this.mAddress=mAddress;
		this.mPort=mPort;
	} 
	public void SetNowSendPercent(NowSendPercent NowSendPercent)
	{
		this.mNowSendPercent=NowSendPercent;
	}
	public  boolean SendFile2Save() throws Exception 
	{
		return SendFile2Save(mSocketClient,mFilename,mAddress,mPort);
	}
	public boolean SendFile2Print(UserOrderInfoGenerated order) throws Exception {
		// TODO Auto-generated method stub
	//	this.mOrder=order;
		return SendFile2Print(mSocketClient,mFilename,mPrinterName, mAddress,mPort);
	}
	/**
	 *提交订单 
	 */
	public boolean SendFile2PrintNoPay(UserOrderInfoGenerated order) throws Exception {
		// TODO Auto-generated method stub
	//	this.mOrder=order;
		return SendFile2PrintNoPay(mSocketClient,mFilename,mPrinterName, mAddress,mPort);
	}
  /**
   * @return 
 * @throws IOException 
   * 
   * 
   * 
   * */
  public static ArrayList<CloudPrintAddress> GetPrinterListXml(Socket client) throws IOException
  {
	  /*接受打印机列表XMl文件*/
	  client.setSoTimeout(ConnectTimeOut);
	  InputStream  in=client.getInputStream();
	  
	  ByteArrayOutputStream is = new ByteArrayOutputStream();  
	  
	  byte [] buffer =new byte[1024];
	  
	  int len=0;
	  
	  try{
		  while(true){
			  len=in.read(buffer);
			  if(len==-1){
				 break;
			  }else{
				  is.write(buffer, 0,len);
			  }
			
		  }
	  }catch(SocketTimeoutException e){
		  
	  }
	
	  
	  String xml=is.toString().replaceAll("\000", "");
	  /*解析XMl文件*/
	  return ParsePrinterListXml(xml, client.getInetAddress());
	  
  }
  /**
   * @return 
 * @throws IOException 
   * 
   * 
   * 
   * */
  public static String GetExt(Socket client) throws IOException
  {
	  /*接受打印机列表XMl文件*/
	  client.setSoTimeout(ConnectTimeOut);
	  InputStream  in=client.getInputStream();
	  
	  ByteArrayOutputStream is = new ByteArrayOutputStream();  
	  
	  byte [] buffer =new byte[1024];
	  
	  int len=0;
	  
	  try{
		  while(true){
			  len=in.read(buffer);
			  if(len==-1){
				 break;
			  }else{
				  is.write(buffer, 0,len);
			  }
			
		  }
	  }catch(SocketTimeoutException e){
		  
	  }
	
	  
	  String txt=is.toString().replaceAll("\000", "");
	  /*解析XMl文件*/
	  return txt;
	  
  }
  public static ArrayList<CloudPrintAddress> ParsePrinterListXml(String xml,InetAddress addr)
  {
	  ArrayList<CloudPrintAddress> printers=new  ArrayList<CloudPrintAddress>();
	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  DocumentBuilder builder;
	try {
		   builder = factory.newDocumentBuilder();
		   Document dom = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		   Element root =dom.getDocumentElement();
           NodeList notift_elm=root.getElementsByTagName("Printer");
           for (int i = 0; i <notift_elm.getLength(); i++) {
        	   CloudPrintAddress print=new CloudPrintAddress();
         	   Element element = (Element) notift_elm.item(i); 
         	   NodeList childNodes =  element.getChildNodes();  
         	   for(int j=0;j<childNodes.getLength();j++){  
         		  if(childNodes.item(j).getNodeType()==Node.ELEMENT_NODE){ 
         			  String value=childNodes.item(j).getFirstChild().getNodeValue();
             			  if("PrinterName".equals(childNodes.item(j).getNodeName())){
             				 print.setPrinterPoint(value);
             			  }else if("PrinterType".equals(childNodes.item(j).getNodeName())){
             				 print.setPrintType(value);
             			  }else{
             				 ;
             			  }
         			  }
         		  
         		  }
         	    print.SetInetAddress(addr);
         	    printers.add(print);
         	  }                  
		   
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
	  return printers;
	  
  }
 /**
 * @throws IOException 
 * 
 * 
 * 
 * */
  public static FileSendOnNet RcvFileRawData(Socket socket_client)
  {

	  DataInputStream input_stream=null;
	  FileOutputStream fos=null;
	  FileSendOnNet fileRcv = new FileSendOnNet();
	  long ReceivedLength=0;
	  fileRcv.SetInetAddress(socket_client.getLocalAddress(),socket_client.getInetAddress());
		try {
			
			input_stream= new DataInputStream(socket_client.getInputStream());
			//握手成功 
			if (fileRcv.RcvOperationHeader(socket_client)) {
				fos=new FileOutputStream(new File(fileRcv.mFileFullPath));
				      int RcvOneReadFile = 0;
				     
	                  byte[] recvBytes = new byte[1024];
	                  while (ReceivedLength < fileRcv.FileSize())
	                  {
	                      long UnReadLength =fileRcv.FileSize() - ReceivedLength;
	                      if (UnReadLength<= 0) break;
	                          //有数据
	                          RcvOneReadFile = input_stream.read(
	                               recvBytes,
	                               0,
	                              (int)Math.min(recvBytes.length, UnReadLength));
	                          

	                          if (RcvOneReadFile==-1)
	                          {
	                              break;
	                          }
	                          else {
	                              ReceivedLength += RcvOneReadFile;
	                              fos.write(recvBytes, 0, RcvOneReadFile);
	                          }
	                         
	                     
	                      

	                  }
	                  fos.flush();
          }
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			if(fos!=null){
				
				fos.close();
			}						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(ReceivedLength==fileRcv.FileSize()&&fileRcv.FileSize()>0){
			//完整接受到文件
		}else{			
			fileRcv.DeleteFile();
			fileRcv=null;
		}
		//文件接收完成
		//通知主界面
		return fileRcv;
		
  }
  private static void SendServerPermission(Socket client) throws IOException
  {
      client.setSoTimeout(ConnectTimeOut);
      FileSendOnNet fileRcv = new FileSendOnNet();
      fileRcv.SendServerPermission(client);


  }
  /**
 * @throws IOException 
   * 
   * 
   * 
   * */
	  private static  boolean ReadServerPermission(Socket client) throws IOException
      {
          client.setSoTimeout(ReadTimeOut);
          FileSendOnNet fileRcv = new FileSendOnNet();
          return fileRcv.ReadServerPermission(client);
         
      }
 /**
  * @throws IOException 
  * @Deprecated
  * 
  * 
  * */
  private  boolean SendOperationHeader(Socket socketClient,CommunicationType type,FilesWithParams filename) throws IOException
  {
      
	  return SendOperationHeader(socketClient,type,filename,null);
     
  }
 /**
  * @throws IOException 
  * 
  * 
  * 
  * */
  private  boolean SendOperationHeader_Ext(Socket socketClient,CommunicationType type,FilesWithParams filename,String printer,String ext) throws IOException
  {
      
	  return SendOperationHeader_param(socketClient,type,filename,printer,ext);
     
  }
  /**
 * @throws IOException 
  * 
  * 
  * 
  * */
  private  boolean SendOperationHeader(Socket socketClient,CommunicationType type,FilesWithParams filename,String printer) throws IOException
  { 
	  return  SendOperationHeader_param(socketClient,type,filename,printer,null);
  }
  /**
   * @throws IOException 
    * 
    * 
    * 
    * */
    private  boolean SendOperationHeader_param(Socket socketClient,CommunicationType type,FilesWithParams filename,String printer,String param) throws IOException
    {
        FileSendOnNet myFile=new FileSendOnNet(filename);
        myFile.SetPrinterParam(printer);
        
        if (type == CommunicationType.Send2Receive){
            myFile.SendFileRequestedCmd_SendFile(socketClient);          
        } else if (type == CommunicationType.Send2Print){
            myFile.SendFileRequestedCmd_PrintFile(socketClient);          
        }else if(type == CommunicationType. GetPrinterListXml){
      	  myFile.SendFileRequestedCmd_GetPrinters(socketClient);    	  
        }else if(type == CommunicationType.Send2PrintNoPay){
      	  myFile.SendFileRequestedCmd_SubmitPrintFileNoPay(socketClient);
        }else if(type ==CommunicationType.GetPriceById){
      	  myFile.SendFileRequestedCmd_GetPriceById(socketClient, param);
        }else{
      	  myFile.SendFileRequestedCmd_None(socketClient);
        }
        
        return true;
    }
  /**
   * 
   * 
   * 
   * */
  /**
 * @throws IOException 
  * 
  * 
  * 
  * */
  private static CommunicationType RcvOperationHeader(Socket client) throws IOException
  {
      FileSendOnNet myFile = new FileSendOnNet();
      return  myFile.RcvFileRequestedCmd(client);           
  }
  /**
 * @throws Exception 
  * 
  * 
  * 
  * */
/**
 * @throws Exception 
* 
* 
* 
* */          
  private  boolean SendFileRawData(Socket socketClient, FilesWithParams filepath,String PrinterName, InetAddress address, int port) throws Exception
  {
      FileSendOnNet file = new FileSendOnNet(filepath);
      file.SetPrinterParam(PrinterName);
      file.SetCallBackFileSendPercent(mNowSendPercent);
      file.SetServerIpPort(address, port);
      if(socketClient.isConnected()==false){
          socketClient.connect(new InetSocketAddress(address, port),ConnectTimeOut);
      }

      file.SendFileInfo(socketClient);
      file.SendFile(socketClient);
     
      return true;
  }
  
  private  boolean SendFileRawData(Socket socketClient, FilesWithParams filepath, InetAddress address, int port) throws Exception
  {
      
     
      return SendFileRawData(socketClient,filepath,null,address,port);
  }
 /**
  * 
  * 
  */
/**
* 
* 发送文件 请求保存
 * @throws Exception 
* 
* */
     boolean SendFile2Save(Socket socketClient, FilesWithParams filename, InetAddress mAddress, int mPort) throws Exception 
    {

    	return SendFile2_with_Cmd(socketClient, filename, CommunicationType.Send2Receive,null, mAddress, mPort);       
        
    }
	        
/**
* 
* 发送文件 请求打印
 * @throws Exception 
* 
* */
    public  boolean SendFile2Print(Socket socketClient, FilesWithParams filename,String PrinterName, InetAddress mAddress,int mPort) throws Exception
    {
    	
    	return SendFile2_with_Cmd(socketClient, filename, CommunicationType.Send2Print, PrinterName, mAddress, mPort);
    	
    }
    
    public  boolean SendFile2PrintNoPay(Socket socketClient, FilesWithParams filename,String PrinterName, InetAddress mAddress,int mPort) throws Exception
    {
    	
    	return SendFile2_with_Cmd(socketClient, filename, CommunicationType.Send2PrintNoPay, PrinterName, mAddress, mPort);
    	
    }
    
    public  boolean SendFile2_with_Cmd(Socket socketClient, FilesWithParams fileparams,CommunicationType type,String printerName, InetAddress mAddress,int mPort) throws Exception
    {
    	socketClient.connect(new InetSocketAddress(mAddress,mPort),ConnectTimeOut);
        
    	if (socketClient.isConnected() == false)
        {
            return true;
        }
	        /*发送发送文件头*/
	        SendOperationHeader(socketClient, type,fileparams,printerName);
	        /*读取服务器反馈*/
        boolean sendAllow = ReadServerPermission(socketClient);
        if (sendAllow == true)
        {
            /*发送文件*/
            SendFileRawData(socketClient, fileparams,printerName, mAddress, mPort);
            return true;
        }else{
            return false;
        }
    }
/**
 *  
* 
* @throws IOException
* 
* */
	public  ArrayList<CloudPrintAddress> GetPrinterList() throws Exception
	{
		Socket socketClient=this.mSocketClient;
		InetAddress mAddress=this.mAddress;
		int mPort=this.mPort;
		 ArrayList<CloudPrintAddress> printer=null;
		 
		 socketClient.connect(new InetSocketAddress(mAddress,mPort));
	     if (socketClient.isConnected() == false)
	     {
	         return null;
	     }
	     /*发送发送文件头*/
	     SendOperationHeader(socketClient, CommunicationType. GetPrinterListXml,null);
	     /*读取服务器反馈*/
	     boolean sendAllow = ReadServerPermission(socketClient);
	     if (sendAllow == true)
	     {
	         /*读取打印机列表*/
	    	 printer=GetPrinterListXml(socketClient);
	         
	     }
	     /*关闭缓冲*/
	    
	     return printer;
	}
/**
*  
* 
* @throws IOException
* 
* */
	public  String SendFileId2GetPrice(String json) throws Exception
		{		
			String	  result=SendFileId2GetResult(json);
			String    price_t=null;
			if(!StringUtils.isEmpty( result)){		    		 
	    		 JSONObject jo=new JSONObject( result);
	    		 price_t=jo.getString("price2pay");
	    		 
	    	 }			
			return price_t;		  
		}
  /**
	*  
	* 
	* @throws IOException
	* 
	* */
	public  String SendFileId2GetResult(String json) throws Exception
	{
		Socket socketClient=this.mSocketClient;
		InetAddress mAddress=this.mAddress;
		int mPort=this.mPort;
		String result_t=null; 
		 
		 socketClient.connect(new InetSocketAddress(mAddress,mPort));
	     if (socketClient.isConnected() == false)
	     {
	         return null;
	     }
	     /*发送发送文件头*/
	     SendOperationHeader_param(socketClient, CommunicationType.GetPriceById,null,null,json);
	     /*读取服务器反馈*/
	     boolean sendAllow = ReadServerPermission(socketClient);
	     if (sendAllow == true)
	     {	        
	    	 result_t=GetExt(socketClient); // Price_t=GetExt(socketClient);	
	     }
	     /*关闭缓冲*/	    
	     return result_t;
	}
/**
 * @throws IOException 
* 
* 
* 
**/      
    public static FileSendOnNet RcvFileFromClient(Socket client) throws IOException
    {
        /*接受文件头*/       
    	FileSendOnNet file=null;
    	
        CommunicationType cType=RcvOperationHeader(client);
        if (cType == CommunicationType.Send2Print)
        {
            //打印文件
            SendServerPermission(client);
            file=RcvFileRawData(client);
        }
        else if (cType == CommunicationType.Send2Receive)
        {
            //发送文件
            SendServerPermission(client);
            file=RcvFileRawData(client);
        
        }else if(cType==CommunicationType.GetPrinterListXml){
        	SendServerPermission(client);//发送许可
        	//SendPrinterListXml(client);
        }
      
        return file;
    
    }

}
