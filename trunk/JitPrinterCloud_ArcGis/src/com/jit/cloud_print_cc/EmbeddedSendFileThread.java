package com.jit.cloud_print_cc;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;

import com.jit.cloud_print_cc.FileSendOnNet.NowSendPercent;
import com.jit.cloud_print_cc.ServerInfoSendStatus.SendFilePackage;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferDone;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferStart;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferendEndOne;
/**
*struct msg
*{
*    int type;
*    int len;
*    char data[];
*}; 
* 
*/
/**
 * 
 * 
 */
public class EmbeddedSendFileThread implements Runnable
{	
	/**
	 * 
	 * 消息定义
	 * 
	 */
	public static class Msg{
		//final byte[] msg_type=new byte[4];
		//final byte[] msg_len=new byte[4];
		int _Len=0;
		public int Len(){
			return _Len;
		}
		public  byte[] GetMsgBody(int type,int len,byte[] buffer)
		{		
			_Len=len;
			byte[] buf_return=null;
			if(len<=0){
				buf_return=FileSendOnNet.concatAll(
					toLH(type),
					toLH(len));
			}else{
				byte [] buff_t=Arrays.copyOf(buffer, len);;
				buf_return= FileSendOnNet.concatAll(
						toLH(type),
						toLH(len),
						buff_t);
			}
			return buf_return;
			
		}
	}
	/**
	 * 
	 * 
	 * 
	 */
	 int SERV_PORT= 8888;//服务端口号
	 int MAXSIZE= 1000;//缓存队列可以存储的最大数据
	 int BUF_COUNT= 2;//缓存去的个数
	 int MSG_FILENAME= 1;
	 int MSG_CONTINUE= 2;
	 int MSG_ACK= 3;
	 int MSG_DONE= 4;
	 int MSG_EXCEPTION= 5;
	
	 public static final String ServerIP="192.168.1.113";
	 public static final int ServerPort=8888;
        private SendFilePackage sfPackage=new SendFilePackage();
		private int mFileIdx=0;
		private Context mContext;
		//private ArrayList<ServerInfo> mSelectedServer=new ArrayList<ServerInfo>();
		boolean mThreadRun=true;
		ArrayList<String> mDataFile=new ArrayList<String>();
		TransferStart start;
		TransferendEndOne end;
		TransferDone done;
		/**
		 * 
		 * 
		 * 
		 */
		private  NowSendPercent mNowSendPercent=null;
		/**
		 * 
		 * 
		 * 
		 */
		public EmbeddedSendFileThread(
				Context context,
				ArrayList<ServerInfo> SelectedServer,
				ArrayList<String> file,
				TransferStart start,
				TransferendEndOne   end,
				TransferDone done,
				NowSendPercent NowSendPercent)
		{
			if(context!=null){
				this.mContext=context.getApplicationContext();
			}
			  if(file!=null){
					for(String s:file){
						this.mDataFile.add(new String(s));
					}
					
					
			  }		
			this.start=start;
			this.end=end;
			this.done=done;
			this.mNowSendPercent=NowSendPercent;
		}
	
		/**
		 * 
		 * 
		 * 
		 */
		@Override
		public void run()
		{
	
			if(this.IsParamsValid()==false) return;
////////////////////////////////
			start.Start(mContext);			
			////////////////////////////////
			//for(int i=0;i<mSelectedServer.size();i++)
			
			{
				for(mFileIdx=0;mFileIdx<mDataFile.size();mFileIdx++){
					
				    Socket	socketClient_t=new Socket();
				    try {
						socketClient_t.connect(new InetSocketAddress(ServerIP,ServerPort),PhonePcCommunication.ConnectTimeOut);
						this.SendFile_Embeded(socketClient_t,mDataFile.get(mFileIdx));
						socketClient_t.shutdownInput();
						socketClient_t.shutdownOutput();
						Thread.sleep(300);
						/*DataInputStream Di=new DataInputStream(socketClient_t.getInputStream());
						byte[] buffer=new byte[1024];
						Di.read(buffer,0,buffer.length);*/
					     sfPackage.SendSuccess.add("发送成功");
				    }catch (Exception e) {
						 sfPackage.SendAbort.add(e.getMessage());
					}
				    try {
						socketClient_t.close();
					} catch (IOException e) {
						 sfPackage.SendAbort.add(e.getMessage());
					}
				    end.EndOne(this.mContext, null,1.0*((0+1)*(mFileIdx+1))/(1*mDataFile.size()));
				/*if(mThreadRun){
						final ServerInfo si=mSelectedServer.get(i);
						   si.SetNowSendPercent(mNowSendPercent);
						   si.SendFileOrDirectory(new FilesWithParams(mDataFile.get(mFileIdx),mContext));
						   while (!si.IsSendFileComplete());
						   String SendDes ="主机："+  si.GetHostDes() +"文件："+ mDataFile.get(mFileIdx);
						     if (si.mFileSendSuccess)
						     {
	                             sfPackage.SendSuccess.add(SendDes);
	                         }
	                         else if (si.mFileSendAbort)
						     {
	                             sfPackage.SendAbort.add(SendDes);
	                         }
	                        else if (si.mFileSendEsc)
						     {
	                             sfPackage.SendEsc.add(SendDes);
	                         }
						 
							end.EndOne(this.mContext, si,1.0*((i+1)*(mFileIdx+1))/(mSelectedServer.size()*mDataFile.size()));

					}*/
					
				}
			}
			///////////////////////////////////////
			
			done.Done(mContext,sfPackage);

		}
		/**
		 * 
		 * 
		 */	
		public boolean IsParamsValid()
		{
			if(this.mContext==null||this.mDataFile==null){
				return false;
			}else{
				return true;
			}
			
		}
/**
 * 
 * 
 */
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
/**
 * 
 * 
 * 
 */
		public  void SendFile_Embeded(Socket socket, String file) throws Exception
	      {
	    	  byte[] buf = null; 
	    	  int bufferSize = MAXSIZE; // 20K  
	          buf = new byte[bufferSize];  
	          FileInputStream reader = null; 
	    	  DataOutputStream out = null;  
	          
	          try {
	        	  /*-------------------------------------*/
	  			    reader = new FileInputStream(file);
		  			out = new DataOutputStream(socket.getOutputStream()); 
		  			socket.setSendBufferSize(1024*1024*2);
		      	  /*--------Send-----------------------------*/
		      	  int read = 0;
		      	  int FileSizeTotal=reader.available();
		      	  int FileSizeNowRead=0;
		      	    //写入文件头
		      	     {
				      	  Msg msg_t=new Msg();
				      	  String fileName_t=new String("abc.pdf"); 				         
				      	  out.write(msg_t.GetMsgBody(MSG_FILENAME,fileName_t.length(), fileName_t.getBytes("US-ASCII")), 0,8+msg_t.Len());	
				     }		      	  
		  			// 将文件输入流 循环 读入 Socket的输出流中
		  			while (read != -1) {
		  				long TimeSendStart=System.nanoTime();
		  				read = reader.read(buf, 0, buf.length);
		  				// MSG_CONTINUE
			  				if(read>=0){
				  				 Msg msg_t=new Msg();
				  				 out.write(msg_t.GetMsgBody(MSG_CONTINUE,read, buf), 0, 8+read);
				  				 out.flush();
				  				 Thread.sleep(500);
			  				 }
		  				//		  			
		  			
		  				long TimeMs=(System.nanoTime()-TimeSendStart)/1000;
		  				FileSizeNowRead+=read;
		  				double Time_t=1.0*TimeMs/1000;
		  				if(mNowSendPercent!=null&&Time_t>0){
		  					double SendSpeed=(1.0*read/1024)/(Time_t);//KBpS		  					
		  					mNowSendPercent.NowSendPerrSent(1.0*FileSizeNowRead/FileSizeTotal,SendSpeed);
		  				}
		  			}
		  			
		  			{
		  				 Msg msg_t=new Msg();
		  				 out.write(msg_t.GetMsgBody(MSG_DONE,0, buf), 0, 8+msg_t.Len());
		  				 out.flush();
	  				 }	  			
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
/**
 * 
 * 
 * 
 */
		private static byte[] intToByteArray(final int integer) {
			int byteNum = (40 -Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer))/ 8;
			byte[] byteArray = new byte[4];

			for (int n = 0; n < byteNum; n++)
			byteArray[3 - n] = (byte) (integer>>> (n * 8));

			return (byteArray);
			}
/**
  * 将int转为低字节在前，高字节在后的byte数组
  * @param n int
  * @return byte[]
  */
		public static byte[] toLH(int n) {
		  byte[] b = new byte[4];
		  b[0] = (byte) (n & 0xff);
		  b[1] = (byte) (n >> 8 & 0xff);
		  b[2] = (byte) (n >> 16 & 0xff);
		  b[3] = (byte) (n >> 24 & 0xff);
		  return b;
		} 
/**
  * 将int转为高字节在前，低字节在后的byte数组
  * @param n int
  * @return byte[]
  */
public static byte[] toHH(int n) {
  byte[] b = new byte[4];
  b[3] = (byte) (n & 0xff);
  b[2] = (byte) (n >> 8 & 0xff);
  b[1] = (byte) (n >> 16 & 0xff);
  b[0] = (byte) (n >> 24 & 0xff);
  return b;
} 
/**
 * 
 * 
 * 
 */		
}
