package com.jit.cloud_print_cc;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.sdk.pay.demo.PayDemoActivity;
import com.baidu.location.BDLocation;
import com.jit.cloud_print_cc.FileSendOnNet.NowSendPercent;
import android.widget.Toast;


public class CloudPrintAddress extends CloudPrintAddressBase  implements Serializable
{
	/**
	 * 
	 * 
	 */
	private double _totalMoney=0;
	private JSONArray _totalOrders=null;
	private String PayInfo;
	public interface Toast_msg 
	{
		void show(CharSequence text, int duration);
	}
	Toast_msg    mToastMsg=null;
	public String mErrorStr=null;
	private static final long serialVersionUID = -758459502806858414L;
	/**
	 * 图片ID，真实项目中可能是图片路径
	 */
	private int imgId;
	
	/**
	 * 距离
	 */
	private String distance;
	/**
	 * 赞数量
	 */
	//private int zan;
	public String mSearchTime;
	private NowSendPercent mNowSendPerCent;
	public CloudPrintAddress()
	{
		
	}
	public CloudPrintAddress (
			String AddrStr,
			PrintFrom type,
			ServerInfo parent,
			PrintType PrintType)
	{
		
		this.mPrintFrom=type;
		this.mPrintType=PrintType;
		
		this.mAddrCharacter=AddrStr;
		this.mIPAddrStr=AddrStr;
		this.setPrinterPoint(AddrStr);
		this.SetLocalParent(parent);

		try {
			this.mAddress= InetAddress.getByName(AddrStr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public CloudPrintAddress(
			double latitude, 
			double longitude,
			int imgId,
			String name,
			String distance,
			String addr,
			int zan){
		super();
		this.mPrintFrom=PrintFrom.cloud;
		this.mPrintType=PrintType.CloudPrint;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imgId = imgId;
		this.setPrinterPoint(name);
		this.distance = distance;
		//this.zan = zan;
		this.mAddrCharacter=addr;
		
	}
	public void setImgId(int imgId)
	{
		this.imgId = imgId;
	}
	public void setPrintType(String printtype)
	{
		if(printtype.equals(PrintType.NetPrint.toString())){			
			this.mPrintType = PrintType.NetPrint;
			
		}else if(printtype.equals(PrintType.CloudPrint.toString())){			
			this.mPrintType = PrintType.CloudPrint;
			
		}else if(printtype.equals(PrintType.PcPrint.toString())){
			this.mPrintType = PrintType.PcPrint;
			
		}else{
			
		}		
	}
	
	public String getDistance()
	{
		return distance;
	}

	public void setDistance(String distance)
	{
		this.distance = distance;
	}

	public int getZan()
	{
		return 0;
	}

	public void setZan(int zan)
	{
		//this.zan = zan;
	}
	public String GetKey()
	{
		String key=this.mAddress.toString()+this.GetPrinterPointName()+this.mPrintType;
		
		return key;
	}
	public String GetDefatutPrinterInfo()
		{
			 String info="机器名 ：SHARP\r\n" +
			 		"IP地址 192.168.0.20\r\n" +
			 		"打印机类型：网络打印机\r\n" +
			 		"搜索时间：2015、5、14 11:01\r\n" +
			 		"状态：状态未注册/可用";
			 return info;
		}
	public String GetPrinterDes()
	{
		if(this.mSearchTime==null){
			this.mSearchTime=LibCui.GetTimeStr();
		}
		String PrintTypeStr="";
		if(mPrintType==PrintType.NetPrint){
			PrintTypeStr="网络打印机";
		}else	if(mPrintType==PrintType.PcPrint){
			PrintTypeStr="PC机 本地打印机";
		}else if(mPrintType==PrintType.CloudPrint){
			PrintTypeStr="云端文印店";
		}else{
			PrintTypeStr="Suck_fuck";
		}
		String PrintParent="";
		
		ServerInfo siParent=this.GetLocalParent();
		if(siParent!=null){
			if(siParent.mHostType==1){
				 PrintParent+=siParent.mHostPCName;
			}else if(siParent.mHostType==2){
				PrintParent+=siParent.mHostPhoneType;
			}else{
				PrintParent="...";
			}
		}		
		
		 String info="打印机名 :"+this.GetPrinterPointName()+
				 "\r\n" +"打印机归属:"+PrintParent+
				 "\r\n" +"IP地址:"+this.mIPAddrStr+
				 "\r\n" +"打印机类型:"+PrintTypeStr+
				 "\r\n"+"搜索时间:"+this.mSearchTime+
		 		 "\r\n"+"状态：";
		 
		 return info;
	}
	/**
	 * 纬度
	 */
	    
		public double getLatitude()
		{
			
			return  Math.round(latitude*100)/100.0;
		}
		public double getLatitude_Double()
		{
			
			return  latitude;
		}
		public void setLatitude(double latitude)
		{
			//latitude=Math.round(latitude*100)/100.0;
			this.latitude = latitude;
		}

		public double getLongitude()
		{
			             
			return Math.round(longitude*100)/100.0;
		}
		public double getLongitude_Double()
		{
			             
			return longitude;
		}

		public void setLongitude(double longitude)
		{
			//longitude=Math.round(longitude*100)/100.0;
			this.longitude = longitude;
		}

		public String getName()
		{
			return this.GetPrinterPointName();
		}
		public String getAddr()
		{
			if(this.mPrintType==PrintType.CloudPrint){
				return this.mAddrCharacter;
			}else if(this.mPrintType==PrintType.NetPrint){
				return  this.mIPAddrStr;
			}else if(this.mPrintType==PrintType.PcPrint){
				return this.mIPAddrStr;
			}else{
				return this.mAddrCharacter;
			}
		
		}
      
		public int getImgId()
		{
			return imgId;
		}
	 public boolean SendFile2NetPrint(String filefullName) 
	 {   

		 if(mPrintType==PrintType.NetPrint){
			 //必须是网络打印机--发送PCL 到打印机IP地址
			 FileInputStream fin=null;
			 DataOutputStream dout=null;
			 Socket s=null;
			 byte [] buf= new byte[1024]; 
			  try{ 
				     s=new Socket(this.mAddress,9100);
			         fin = new FileInputStream(filefullName); 
			         dout =new DataOutputStream(s.getOutputStream());
			         int totalNum=fin.available();
			         int sendNum=0;
			         int read = 0;
			  			// 将文件输入流 循环 读入 Socket的输出流中
			  			while ((read = fin.read(buf, 0, buf.length)) != -1) {
			  				long TimeSendStart=System.nanoTime();
			            	sendNum+=read;
							  				dout.write(buf, 0, read);
							  				dout.flush();
							 long TimeMs=(System.nanoTime()-TimeSendStart)/1000;
			  				 double SendSpeed=(1.0*read/1024)/(1.0*TimeMs/1000);//KBpS
			                if(mNowSendPerCent!=null){
			                		this.mNowSendPerCent.NowSendPerrSent(1.0*sendNum/totalNum, SendSpeed);
			                }
			  			}
			  			dout.flush();
			            dout.close();
						fin.close();
						s.close();
						mErrorStr="成功发送文件到打印机"+"/n"+this.mIPAddrStr;
					    this.Toast_MakeText_show(mErrorStr, Toast.LENGTH_LONG);
				        return true;
			        } 
			        catch(Exception e){ 
			        	 e.printStackTrace(); 
			        	return false;
			        
			        }finally{
			        	    try {
								if(dout!=null)  dout.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        	 
							try {
								if(fin!=null) fin.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
							try {
								if(s!=null) s.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        }
		 }else{
			 return false;
		 }
		   
	      
	            
	   }
 /**
  * 显示提示信息
  * 
  * 
  * @param msg
  * @param dur
  */
    private void Toast_MakeText_show(String msg,int dur) {
		// TODO Auto-generated method stub
    	
    	if(this.mToastMsg!=null){
    		mToastMsg.show(msg,dur);
    	}
    	
	}
/**
 * 显示提示信息
 * 
 * 
 * @param msg
 * @param dur
 */
    public void SetToastMsg(Toast_msg msg) 
    {
  		// TODO Auto-generated method stub      	
	   	    	mToastMsg=msg;
	         	
  	}
 
    public void SetInetAddress(InetAddress address)
     {
    	this.mAddress=address;
    	this.mIPAddrStr=this.mAddress.toString();
	 }
    /**
     * 
     * 
     */  
    public boolean IsPCPrint()
    {
    	return (mPrintFrom==PrintFrom.Pc)&&(mPrintType==PrintType.PcPrint);
    }
    /**
     * 
     * 
     */    
	public boolean PrintDocs(UserOrderInfoGenerated order)
	{
		this._totalMoney=0;
		if(order==null){
			return  false;
		}else{
			this.mNowSendPerCent=order.mNowSendPerCent;
		}			
		/*--------------------------------------------------------------*/			
		if((mPrintFrom==PrintFrom.Self||mPrintFrom==PrintFrom.Phone)
			&&(mPrintType==PrintType.NetPrint)){
		      /*发送到Net打印机*/
			  this.SendFiles2NetPrint2Print(order,this);
		}else if((mPrintFrom==PrintFrom.Pc)
				&&(mPrintType==PrintType.PcPrint)){
			/*发送到PC打印机*/    
			SendFiles2Pc2Print(order,this.GetLocalParentClone());
		}else  if((mPrintFrom==PrintFrom.cloud)&&(mPrintType==PrintType.CloudPrint)){
			 /*发送到云端*/ /*来自云端的打印机--执行--云端打印*/
		 return	this.SendFiles2Cloud2Print(order);
			
			//return true;
		}else{
			
		}
		/*--------------------------------------------------------------*/
	 if("".equals(order.GetErrorDes())){
		 this.Toast_MakeText_show(order.GetErrorDes(),Toast.LENGTH_LONG);
	 }else if(order.GetErrorDes().contains("文件提交成功")){
		 return true;
	 }else{
		 
	 }		
		return false;
	}
	/**
	 * 发送到打印机直接打印
	 * @param order
	 */
		private void SendFiles2NetPrint2Print(UserOrderInfoGenerated order,CloudPrintAddress cpa)
		{
			for(int i=0;i<order.mFiles.size();i++){
				 /**手机下挂的网络打印机
			     *与服务器通讯，
			     *发送文件 
			     *接受文件 pcl
			     *
			     *发送文件给打印机
			     * 
			     */	
				FilesWithParams FWP=order.mFiles.get(i);
				         String FilefullName=null;			
							if(!FWP.isFile()){
								UserInfoOperation_CloudDisk.DownloadFile4Server(FWP.getFileID());
							}	
							FilefullName=FWP.GetFileLocal();
								
								String FileID=order.PostFile2Server(FWP,cpa);
								if (FileID != null && !FileID.equals("") && !FileID.equals("0")) {
								     //post success
											int TimeOutCount=0;
											
											while(order.GetFileFromCloudPrint(FilefullName)==false){
												if(TimeOutCount++<10){									
													try {
														Thread.sleep(500);
													} catch (InterruptedException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												}else{
													break;
												}
												
											}							
											
											if(order.GetFileFromCloudPrint(FilefullName)){
												/*PCL下载成功*/
												String PCLPath=order.GetPCLFileLocation(FilefullName);
												if(PCLPath!=null){
													 SendFile2NetPrint(PCLPath);
												}
													
											}else{
											   /*PCL下载失败*/
											}
									
								}else{
									 //post  errror
								}
			}
		}
		/**
		 * 发送到PC机待打印
		 * @param order
		 */
		private void SendFiles2Pc2Print(UserOrderInfoGenerated order,ServerInfo si)
		{
			
			if((mPrintFrom==PrintFrom.Pc)
					&&(mPrintType==PrintType.PcPrint)){
				order.mErrorStrArray.clear();
				
				for(int i=0;i<order.mFiles.size();i++){
					FilesWithParams  FWP=order.mFiles.get(i);
					if(!FWP.isFile()){
						UserInfoOperation_CloudDisk.DownloadFile4Server(FWP.getFileID());
					}					
					order.mErrorStrArray.add(FWP.GetFileLocal());
					Socket s=new Socket();
					try {
						/* if(mAddress.isReachable(PhonePcCommunication.ConnectTimeOut)==false){
							 order.mErrorStrArray.add("ICMP (ICMP ECHO REQUEST) RESPONSE");							
							 order.mErrorStrArray.add("文件提交失败,IP can not reachable");
							 return;
		        		   }*/
						 si=si.Clone();
						 si.SetNowSendPercent(order.mNowSendPerCent);
						if(si.SendFile2PrintNoPay(s, FWP,this.GetPrinterPointName(),order)){
							order.mErrorStrArray.add("文件提交成功");
							LibCui.SaveLocalOrder2Disk(FWP.getUserName(),FWP.GetOrderIdFile(),FWP.ToJsonStr());
						}else{
							order.mErrorStrArray.add("文件提交失败，Permission Fail");
						}
						 s.shutdownInput();
				         s.shutdownOutput();
					    
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						order.mErrorStrArray.add(e.getMessage());
					}finally{
						 try {
							s.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							order.mErrorStrArray.add(e.getMessage());
						}
					}
					order.mErrorStrArray.add("\n");
				}
			
			
			}
		}
		/**
		 * 
		 * {"FileID":"M452-1763D4DA5090D463D7292C58911B5B44","Money":"0.5"}
		 * 发送到Cloud机待打印
		 * @param cloud
		 * @return 
		 */
		private boolean SendFiles2Cloud2Print(UserOrderInfoGenerated order)
		{
			boolean  sendsuccess_t=false;
			
			if((mPrintFrom==PrintFrom.cloud)&&(mPrintType==PrintType.CloudPrint)){
					order.mErrorStrArray.clear();
					this._totalOrders=new JSONArray();
					for(int i=0;i<order.mFiles.size();i++){
							{
								FilesWithParams  FWP=order.mFiles.get(i);
								if(FWP.isFile()){
									// this is A FILE --post2server 
									UserInfoOperation_CloudDisk  ucd=new UserInfoOperation_CloudDisk(order.getContext());
									String status=ucd.PostFile2Server2Print(FWP);
									
									if(UserInfoOperation_CloudDisk.IsValidJson(status)){								
										//Toast.makeText(order.mContext, status,Toast.LENGTH_SHORT).show();
										this.PayInfo=status;											
										try{
											String money1=this.GetPrintMoney();	
											this._totalMoney+=Double.parseDouble(money1);
										}catch(Exception e){
											
										}										
										String id=this.GetProductId();										
										this._totalOrders.put(id);
										
										sendsuccess_t=true;
									}else{
										order.mErrorStrArray.add("上传文件失败");
									}
									
									//money.replaceAll(regularExpression, replacement)
								}else{
									//this is a file id
								}					
						   } 
						
						
							order.mErrorStrArray.add("\n");
					}
			
			
			}
			
			return  sendsuccess_t;
		}
/**
 * 
 * 
 **/
		public boolean Print_1_Docs()
		{
			return false;
		}
/**
 * 
 * 
 **/		
		public CloudPrintAddress Clear2Serializable() 
		{
			// TODO Auto-generated method stub
			this.mToastMsg=null;
			if(this.GetLocalParent()!=null){
				this.GetLocalParent().Clear2Serializable();
			}

			return this;
		
			
		}
/**
 * 
 * 
 */

/**
 * 克隆一份
 * 
 */
		public CloudPrintAddress  Clone()
		{
			CloudPrintAddressBase cpa=new CloudPrintAddress() ;
			/*--------------------------------*/
			cpa.mPrintFrom=this.mPrintFrom;
			cpa.mPrintType=this.mPrintType;
			cpa.mAddrCharacter=this.mAddrCharacter;
			cpa.mIPAddrStr=this.mIPAddrStr;
			
			cpa.setPrinterPoint(this.GetPrinterPointName());
			cpa.SetLocalParent(this.GetLocalParentClone());
			cpa.mAddress=this.mAddress;
			/*--------------------------------*/
			cpa.latitude=this.latitude;			
			cpa.longitude=this.longitude;
			
			/*--------------------------------*/
			cpa.SetCloneFlag();
			return (CloudPrintAddress) cpa;
			
		}
		public void SeBDtLocation(BDLocation lbd)
		{
			if(lbd!=null){
					this.setLatitude(lbd.getLatitude());
					this.setLongitude(lbd.getLongitude());
					this.SetLocation(lbd.getAddrStr());
			}
		}
/**
 * 
 * 
 **/
		public boolean ISPrinterAlive()
		{
			
			if((mPrintFrom==PrintFrom.Pc)
					&&(mPrintType==PrintType.PcPrint)){
				         ServerInfo si=this.GetLocalParent();
				         if(si!=null){
				        	   return si.IsValidPrint();
				         }else{
				        	 return false;
				         }
				      
			}else{
				
				return true;
			}
			
		
		}
/**
 * 
 * 
 **/
		public String GetTotalMoney(){
			if(this._totalMoney>1E-6){
				return String.valueOf(this._totalMoney);
			}else{
				return null;
			}
		
			
		}
/**
 * 
 * 
 **/
		public String GetTotalOrderId(){
			//return this._totalOrders.toString();
		   return	PayDemoActivity.getOutTradeNo();
			
		}
/**
 * 
 * 商品详情
 **/
		public String GetTotalBodyOrders(){
			return this._totalOrders.toString();
			
		}
/**
 * 
 * 
 **/
		public String GetPrintMoney(){
			String Money = null;
			if(this.PayInfo==null){
				return null;
			}			
			try {
				JSONObject a=new JSONObject(PayInfo);
				Money=a.getString("Money");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			return Money;
		}
/**
 * 
 * 
 **/
		public String GetProductId(){
			String id = null;
			if(this.PayInfo==null){
				return null;
			}			
			try {
				JSONObject a=new JSONObject(PayInfo);
				id=a.getString("FileID");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			return id;
		}
/**
 * 
 * 
 **/
}
