package com.jit.cloud_print_cc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


import com.jit.cloud_print_cc.FilesWithParams.IsDouble;
import com.jit.cloud_print_cc.FilesWithParams.PrintColor;



import android.content.Context;
@SuppressWarnings("unused")
public class FileSendOnNet 
{
	public interface NowSendPercent{
		void NowSendPerrSent(double Percent,double Speed);
	}
	  FilesWithParams mFileWithParam;
	  private  NowSendPercent mNowSendPercent=null;
	  private String  mFilePath;
      public  String  mFileFullPath;
      private String  mFileNameString;
      private  byte[]  mHandshake=new byte[]{0x22,0x22,0x33,0x33};
      private  byte[]  mHandshakeBuffer = new byte[4];

      private  byte[]  mFileSizeBuffer=new byte[512];
	  private long    mFileSizeLong;
	  
      private  byte[]  mFileNameBuffer =new byte[1024];
      
      private  byte[] mFileOpeaCmdBuffer = new byte[256];
      String mFileOpeaCmdBufferString;
      
      private byte[] mFileTransferCmdBuffer = new byte[256*2];
      InetAddress  mLocalAddr;
      InetAddress  mRemoteAddr;
      private byte[] mPrinterNameBuffer=new byte[1024];
      private String mPrinterName;
      /*---------------------------------*/
       	//打印参数
       int PParamcopies=1;//份数
       boolean PParamcolor=false;//彩打
       String  PParamRange="全部";//打印范围
       boolean PParam2Paper=false;
       private byte[] mPParamcopiesBuffer=new byte[256];
       private byte[] mPParamcolorBuffer=new byte[256];
       private byte[] mPParamRangeBuffer=new byte[256];
       private byte[] mPParam2PaperBuffer=new byte[256];
      /*---------------------------------*/
       private byte[] _Android_os_buffer=new byte[256];
       private byte[] _Android_phone_num=new byte[256];
       private byte[] _Android_phone_type=new byte[256];
      /*---------------------------------*/
       private byte[] _json_ext_buffer=new byte[256*20];
       private String _json_ext_string;
       public FileSendOnNet(FilesWithParams fwp) 
       {
    	   String fileName;
    	   this.mFileWithParam=fwp;
    	   if(fwp!=null){
    		   fileName=fwp.GetFileLocal();
    		  
    		   
    	   }else{
    		   fileName="No File";
    	   }
    	   
	 	  this.initBuffer();
	 	  if(fileName==null){
	 		  this.mFileFullPath="NoFilePath";
	     	  this.mFileSizeLong=1024;
	     	  this.mFileNameString="NoFile";
	 	  }else{
	 		  File f=new File(fileName);
	     	  this.mFileFullPath=fileName;
	     	  this.mFileSizeLong=FileSizeUtil.getFileOrFilesSizeLong(fileName);
	     	  this.mFileNameString=f.getName();
	     	  
	     	  
	 	  }    	
	 	 
	 	  this.ConvertData2Buffer();
       }
       /*
      public FileSendOnNet(String fileName) 
      {
    	  this.initBuffer();
    	  if(fileName==null){
    		  this.mFileFullPath="NoFilePath";
        	  this.mFileSizeLong=1024;
        	  this.mFileNameString="NoFile";
    	  }else{
    		  File f=new File(fileName);
        	  this.mFileFullPath=fileName;
        	  this.mFileSizeLong=FileSizeUtil.getFileOrFilesSizeLong(fileName);
        	  this.mFileNameString=f.getName();
    	  }    	
    	  this.ConvertData2Buffer();
      }*/
      public FileSendOnNet() 
      {
    	 this.initBuffer();
    	 this.mFilePath=LibCui.GetMyPhoneDirectory().getAbsolutePath();
      }
      public void initBuffer()
      {
    	  Arrays.fill(this.mHandshakeBuffer, (byte) 0x00);
    	  Arrays.fill(this.mFileSizeBuffer, (byte) 0x00);
    	  Arrays.fill(this.mFileNameBuffer, (byte) 0x00);
    	  Arrays.fill(this.mFileOpeaCmdBuffer, (byte) 0x00);
      }
      public String GetFolder()
      {
          return "FileRecv";
      }
      public void SetInetAddress(InetAddress local,InetAddress remote)
      {
    	  this.mLocalAddr=local;
    	  this.mRemoteAddr=remote;
      } 
      public long FileSize()
      {
    	  return this.mFileSizeLong;
      }
      public void DeleteFile(){
    	  if(mFileFullPath!=null&&"".equals(mFileFullPath))
    	  LibCui.deleteFile(mFileFullPath);
    	  
		  mFileFullPath=null;
      }
      public void SetCallBackFileSendPercent(NowSendPercent NowSendPercent)
      {
    	  this.mNowSendPercent=NowSendPercent;    	  
      }
      public void ConvertBuffer2RealData() throws UnsupportedEncodingException
      {
          /*-----文件大小----*/
          String fileSizeLongStr =ConvertByte2String(this.mFileSizeBuffer);
          
          this.mFileSizeLong = Long.parseLong(fileSizeLongStr);
          /*-----文件名----*/
          this.mFileNameString =ConvertByte2String(this.mFileNameBuffer);
          /*--------------*/
          this.mFileFullPath = this.mFilePath +File.separator+ this.mFileNameString;
          
          /*客户端请求命令*/
          this.mFileOpeaCmdBufferString =ConvertByte2String(this.mFileOpeaCmdBuffer);

      }
      private void ConvertData2Buffer()
      {
  	  
    	  LibCui.ConvertString2buffer(Long.toString(this.mFileSizeLong),this.mFileSizeBuffer);
    	  LibCui.ConvertString2buffer(this.mFileNameString,mFileNameBuffer);
    	  if(mFileWithParam!=null){    			
    			this.PParamcopies=mFileWithParam.mPrintCopies;//份数
    			this.PParamcolor=(mFileWithParam.IsColor());//彩打
    			this.PParamRange=mFileWithParam.mPrintRange;//打印范围
    			this.PParam2Paper=(mFileWithParam.IsDuplex());
    			this._json_ext_string=mFileWithParam.ToJsonStr();
    			LibCui.ConvertString2buffer(String.valueOf(this.PParamcopies),this.mPParamcopiesBuffer);
        		LibCui.ConvertString2buffer(Boolean.toString(this.PParamcolor),this.mPParamcolorBuffer);
        		LibCui.ConvertString2buffer(this.PParamRange,this.mPParamRangeBuffer);
        		LibCui.ConvertString2buffer(Boolean.toString(this.PParam2Paper),this.mPParam2PaperBuffer);
        		/**/
        		LibCui.ConvertString2buffer(mFileWithParam.mPhoneType,this._Android_phone_type);
        		LibCui.ConvertString2buffer(mFileWithParam.mPhonrNumber,this._Android_phone_num);
        		LibCui.ConvertString2buffer(mFileWithParam.mSystemType,this._Android_os_buffer);
        		/**/
        		LibCui.ConvertString2buffer(this._json_ext_string,this._json_ext_buffer);
    		}
    	  LibCui.ConvertString2buffer(mFileOpeaCmdBufferString, this.mFileOpeaCmdBuffer);
    	  
      }
      public byte[] GetFileHeader() throws UnsupportedEncodingException 
      {          
          return GetOperationHeader("发送文件头，文件大小，打印机 ");
      }
  /**
   * 此处增加数据
   * 
   **/
      public byte[] GetOperationHeader(String cmd) throws UnsupportedEncodingException
      {

    	  byte[] header = null;
          mFileOpeaCmdBufferString = cmd;
                  
          ConvertData2Buffer();
          
          header=concatAll(this.mHandshake,
        		  this.mFileSizeBuffer,
        		  this.mFileNameBuffer,
        		  this.mFileOpeaCmdBuffer,
        		  this.mPrinterNameBuffer,
        		  /*-------------------------*/
        		  mPParamcopiesBuffer,
        		  mPParamcolorBuffer,
        		  mPParamRangeBuffer,
        		  mPParam2PaperBuffer,
        		  /*--------------------------*/
        		  _Android_os_buffer,
                  _Android_phone_num,
                  _Android_phone_type,
        		  /*--------------------------*/
                  _json_ext_buffer
        		  );          
          return header;
      }
      /**
       * 此处增加数据
       * 
       **/    
      public boolean RcvOperationHeader(Socket socket_client) throws IOException
      {
    	  DataInputStream input_stream=null;
    	  input_stream= new DataInputStream(socket_client.getInputStream());
		  input_stream.readFully(mHandshakeBuffer,0,mHandshakeBuffer.length);
			//握手成功 
			if (IsHandShark()) {
				input_stream.readFully(mFileSizeBuffer, 0, mFileSizeBuffer.length);
				input_stream.readFully(mFileNameBuffer, 0, mFileNameBuffer.length);	 
				input_stream.readFully(mFileOpeaCmdBuffer, 0, mFileOpeaCmdBuffer.length);
				input_stream.readFully(mPrinterNameBuffer, 0,mPrinterNameBuffer.length);
		    	/*--------------------------------------------------------------*/		    	
				input_stream.readFully(mPParamcopiesBuffer, 0, mPParamcopiesBuffer.length);
				input_stream.readFully(mPParamcolorBuffer, 0, mPParamcolorBuffer.length);
				input_stream.readFully(mPParamRangeBuffer, 0,mPParamRangeBuffer.length);
				input_stream.readFully(mPParam2PaperBuffer, 0,mPParam2PaperBuffer.length);
		    	/*--------------------------------------------------------------*/
				input_stream.readFully(_Android_os_buffer, 0,_Android_os_buffer.length);
				input_stream.readFully(_Android_phone_num, 0,_Android_phone_num.length);
				input_stream.readFully(_Android_phone_type, 0,_Android_phone_type.length);
				/*--------------------------------------------------------------*/
				input_stream.readFully(_json_ext_buffer, 0,_json_ext_buffer.length);
				ConvertBuffer2RealData();
				return true;
			}else{
				return false;	
			}
		

    	  
      }
      /**
       *@deprecated 付费打印模式 
       */
      public byte[] GetSendPrintHeader() throws UnsupportedEncodingException
      {
    	  //尚未付费
          return GetOperationHeader("PrintFile");
      }
      public byte[] GetSendFileHeader() throws UnsupportedEncodingException
      {
          return GetOperationHeader("SendFile");
      }
      public byte[] GetXmlPrintsHeader() throws UnsupportedEncodingException
      {
          return GetOperationHeader("PrinterListXml");
      }
      public byte[] GetSubmitFileNoPay() throws UnsupportedEncodingException
      {
          return GetOperationHeader("SubmitNoPay");
      }
      public byte[] GetNoneHeader() throws UnsupportedEncodingException
      {
          return GetOperationHeader("None");
      }
      public static byte[] concatAll(byte[] first, byte[]... rest) {
    	  int totalLength = first.length;
    	  for (byte[] array : rest) {
    	    totalLength += array.length;
    	  }
    	  byte[] result = Arrays.copyOf(first, totalLength);
    	  int offset = first.length;
    	  for (byte[] array : rest) {
    	    System.arraycopy(array, 0, result, offset, array.length);
    	    offset += array.length;
    	  }
    	  return result;
    	}
      public  void SendFile(Socket socket) throws Exception
      {
    	  SendFile(socket,this.mFileFullPath);
      }
      public  void SendFile(Socket socket, String file) throws Exception
      {
    	  byte[] buf = null; 
    	  int bufferSize = 1024*40; // 20K  
          buf = new byte[bufferSize];  
          FileInputStream reader = null; 
    	  DataOutputStream out = null;  
          
          try {
        	  /*-------------------------------------*/
  			    reader = new FileInputStream(file);
	  			out = new DataOutputStream(socket.getOutputStream()); 
	      	  /*--------Send-----------------------------*/
	      	  int read = 0;
	      	  int FileSizeTotal=reader.available();
	      	  int FileSizeNowRead=0;
	  			// 将文件输入流 循环 读入 Socket的输出流中
	  			while ((read = reader.read(buf, 0, buf.length)) != -1) {
	  				long TimeSendStart=System.nanoTime();
	  				out.write(buf, 0, read);
	  				out.flush();
	  				long TimeMs=(System.nanoTime()-TimeSendStart)/1000;
	  				FileSizeNowRead+=read;
	  				double SendSpeed=(1.0*read/1024)/(1.0*TimeMs/1000);//KBpS
	  				if(mNowSendPercent!=null){
	  					mNowSendPercent.NowSendPerrSent(1.0*FileSizeNowRead/FileSizeTotal,SendSpeed);
	  				}
	  			}
	  			out.flush();
	  			
			   // socket.shutdownOutput();			
	      	  /*---Close----------------------------------*/	      	
  			
  		} catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			 throw e;
  		}catch (IOException e) {
			// TODO Auto-generated catch block
			throw e;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}finally{  		  
			 reader.close();
			// out.close();     	 
  		}   	  
      }
      public  void SendFileHeader(Socket socket) throws Exception
      {
     	   DataOutputStream out = null;  
    	  try{
    		  out = new DataOutputStream(socket.getOutputStream());  
   		      out.write(this.GetFileHeader());
   		      out.flush();
   		     
    	  }catch (Exception e) {
  			// TODO Auto-generated catch block
  			throw e;
  		  }finally{  		  
  			 out.flush();
  		    // out.close();
    	  }   	 
		   
		  
      }
      private boolean IsHandShark()
      {
          boolean bo=false;
          byte[]a=this.mHandshakeBuffer;
          byte[]b=this.mHandshake;
          for(int i=0;i<b.length;i++)
          {
             if(a[i]==b[i])
                bo=true;
             else
             {
                bo=false;
                break;
              }
          }
          return bo;
      }
  /**
  * 
  * 发送内置文件头
 * @throws IOException 
  * 
  * 
  * */
      public void SendFileInfo(Socket socket) throws IOException 
      {
    	  DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
		  out.write(this.GetFileHeader());
		  out.flush();
          
      }
  /**
  * 
  * 读取服务器权限
 * @throws IOException 
  * 
  * 
  * */
      public boolean ReadServerPermission(Socket socket) throws IOException
      {
    	  DataInputStream  in=new DataInputStream(socket.getInputStream());
         
          in.readFully(mHandshakeBuffer, 0, mHandshakeBuffer.length);
          if (IsHandShark())
          {
        	  in.readFully(mFileTransferCmdBuffer, 0, mFileTransferCmdBuffer.length);
        	  in.readFully(_json_ext_buffer,0,_json_ext_buffer.length);
          }
          /*0x22 允许权限控制*/
          if (this.mFileTransferCmdBuffer[0] == 0x22)
          {
              return true;
          }else{
              return false;
          }
        //  return true;
        
      }
      
  /**
  * 
  * 发送 请求发送命令
 * @throws IOException 
  * 
  * 
  * */
      public void SendFileRequestedCmd_SendFile(Socket socket) throws IOException
      {
    	  DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
		  out.write(this.GetSendFileHeader());
		  out.flush();
      }
  /**
   * 
   * 发送 请求发送命令
 * @throws UnsupportedEncodingException 
  * @throws IOException 
   * @deprecated
   * 
   * */
  public void SendFileRequestedCmd_PrintFile(Socket socket) throws UnsupportedEncodingException, IOException
  {
	  DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
	  out.write(this.GetSendPrintHeader());
	  out.flush();
  }
 /**
 * 
 * 发送  请求打印命令
 * @throws IOException 
 * 
 * 
 * */
      public void SendFileRequestedCmd_GetPrinters(Socket socket) throws IOException
      {
    	  DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
    	  out.write(this.GetXmlPrintsHeader());
    	  out.flush();
      }
 /**
  * 未付款，提交文件
  * 
  * 
  */
      public void SendFileRequestedCmd_SubmitPrintFileNoPay(Socket socket) throws IOException
      {
    	  DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
    	  out.write(this.GetSubmitFileNoPay());
    	  out.flush();
      }
  /**
   * 
   * 
   * 
   */
      public void SendFileRequestedCmd_None(Socket socket) throws IOException
      {
    	  DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
    	  out.write(this.GetNoneHeader());
    	  out.flush();
      }
  /**
   * 
   * 
   * 
   */
  public PhonePcCommunication.CommunicationType RcvFileRequestedCmd(Socket socket) throws IOException
  {
	  DataInputStream input_stream=new DataInputStream(socket.getInputStream());
	
      if (RcvOperationHeader(socket))
      {
    	  

          if (this.mFileOpeaCmdBufferString.equals("SendFile"))
          {
              return PhonePcCommunication.CommunicationType.Send2Receive;
          }
          else if (this.mFileOpeaCmdBufferString.equals("PrintFile"))
          {
              return PhonePcCommunication.CommunicationType.Send2Print;

          }else{
       //        Debug.Assert(false);
               return PhonePcCommunication.CommunicationType.NULL;
          }

      }
     // Debug.Assert(false);
      return PhonePcCommunication.CommunicationType.NULL;
  }
public void SendServerPermission(Socket client) throws IOException
{
	// TODO Auto-generated method stub
	DataOutputStream in=new DataOutputStream(client.getOutputStream());
	in.write(this.mHandshake);
    this.mFileTransferCmdBuffer[0] = 0x22;
    in.write(this.mFileTransferCmdBuffer);
    in.write(this._json_ext_buffer);
   
}
public void SetPrinterParam(String printer)
{
	this.SetPrinterName(printer);
	
	
}

public void SetServerIpPort(InetAddress address, int port)
{
	if(mFileWithParam!=null){
		mFileWithParam.SetServerIpPort(address, port);
	}
}

private void SetPrinterName(String printer)
{
	if(printer!=null){
		this.mPrinterName=printer;
		if(this.mFileWithParam!=null){
		 this.mFileWithParam.setPrinter(printer);
		}
		
		
	}else{
		this.mPrinterName="NoPrinter";
	}
	/*
	byte[] buffer_t=LibCui.ConvertString2Byte(this.mPrinterName);
	if(buffer_t!=null){
		System.arraycopy(buffer_t, 0,this.mPrinterNameBuffer, 0, buffer_t.length);
	}*/
	LibCui.ConvertString2buffer(this.mPrinterName,this.mPrinterNameBuffer);

}
public String  ConvertByte2String(byte[] buffer)
{
	 String Str_t="";
	try {
		Str_t = new String(buffer,"UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 Str_t=Str_t.replaceAll("\000", "");
	 return Str_t;
}
/**
 * 
 *
 */
public void SetSubmitPrinter2Disk()
{
	/*String OrderId_t="none";
	String UserName_t="blacksherry";
	String Price_t="-1";
	String cmd="";
	
	
	this._json_ext_string="";*/
	
}
/**
 * 
 *
 */
}
