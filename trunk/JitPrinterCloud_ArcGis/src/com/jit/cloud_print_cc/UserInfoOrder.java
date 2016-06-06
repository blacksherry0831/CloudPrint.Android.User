package com.jit.cloud_print_cc;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import android.provider.MediaStore.Files;
import android.view.View;

import com.jit.cloud_print_cc.UserInfoOrdersManager.InfoChanged;
import com.jit.cloud_print_orders.OrderLocal;
import com.ta.utdid2.android.utils.StringUtils;
/**
 *用户订单信息 
 *
 *
 *
 */
public class UserInfoOrder 
{ 
	/*-----------------------------------*/
	public static final String STATUS_CHARGING="charging";//下单完成，
	public static final String STATUS_CHARGED="charged";//计费完成
	public static final String STATUS_SUCCESS="success";//打印完成
	public static final String STATUS_PRINTED_PENDING="pending";//付款完成
	public static final String ORDER_TYPE_LOCAL="local";
	/*-----------------------------------*/
  	JSONObject _Json_o;
  	public  String _Username;
  	private  String _Price2Pay;
	/*-----------------------------------*/
	public	String  color;//是否黑白
	public	String copies;//打印份数
	public	String doc;//文件名
	public	String filetype;//文件类型
	public	String ID;//任务ID
	public	String pages;//打印范围
	public	String printTime;//下单时间
	private	String status;//订单状态
	private InfoChanged   _IC;
	//private String _NeedAsynNotify;//需要异步通知
	
	 private void notify_changed()
    {
	   if(_IC!=null){_IC.LocalOrderStatusChanged();}
    }
    public void SetNotiftyChanged(InfoChanged ic)
    {
      _IC=	ic;
    }
    
    public void SetAsyncNofity()
    {
    	//this._NeedAsynNotify=String.valueOf(notify);
    	try {
			_Json_o.put("ppneedasyncnotify","true");
			this.Save2Disk();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public String getStatus() 
	{
		return status;
	}

	
	public  void setStatus(String status_t) {		
		
		if(status_t!=null&&status_t.equals(this.status)){
			//状态没有改变，
			return;
		}
		
		try {
			_Json_o.put("Status",status_t);
			Save2Disk();
			this.status = status_t;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(STATUS_PRINTED_PENDING.equalsIgnoreCase(getStatus())){		 
			//挂起，更新状态，读取服务器状态
			UpdateStatusLocal();
		}else if(STATUS_CHARGING.equals(getStatus())){
			//正在计费，拉取计费价格
			UpdateStatusLocal();
		}else if(STATUS_CHARGED.equals(getStatus())){
			//计费完成，提示支付，等待用户操作
		}else if(STATUS_SUCCESS.equals(getStatus())){
			//打印成功，没事可做
		}
		notify_changed();	
	}
	/*----------------------------------*/
	private String orderType;
	/*----------------------------------*/
	private int _SvrPort=-1;
	private String _SvrAddr="";
	/*----------------------------------*/

	public UserInfoOrder(){
		
	}
	public void SetPrice2pay(double price_t)
	{
		try {
			_Json_o.put("price2pay",price_t);
			Save2Disk();
			this._Price2Pay=GetV("price2pay");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public boolean Parse(JSONObject jo) 
	{
		this._Json_o=jo;		
		
		color=GetData(jo,"Color");
		copies=GetData(jo,"Copies");
		doc=GetData(jo,"DocumentName");
		filetype=GetData(jo,"Filetype");
		/*-------*/
		ID=GetData(jo,"Id");
		pages=GetData(jo,"Pages");
		printTime=GetData(jo,"PrintTime");
		status=GetData(jo,"Status");
		/*-------*/
		/*beizhu=GetData(jo,"Beizhu");
		Copies=GetData(jo,"Copies");
		fanwei=GetData(jo,"Fanwei");
		
		FileName=GetData(jo,"Filename");	
		BussinessID=GetData(jo,"BussinessID");
		Place=GetData(jo,"Place");
		PriceID=GetData(jo,"PriceID");
		ReceiveTime=GetData(jo,"Receivetime");
		Status=GetData(jo,"Status");
		Telphone=GetData(jo,"Telphone");
		uploadtime=GetData(jo,"Uploadtime");
		UserID=GetData(jo,"UserID");
		FileID=GetData(jo,"Fileid");*/
		orderType=GetData(jo,"ordertype");
		if(!StringUtils.isEmpty(orderType)&&"local".equals(orderType)){
			//本地订单
			color=GetData(jo,"ppcolor");
			copies=GetData(jo,"ppcopies");
			doc=GetData(jo,"DocumentName");
			filetype=GetData(jo,"Filetype");
			ID=GetData(jo,"orderid_suffix");
			pages=GetData(jo,"pprange");
			printTime=GetData(jo,"PrintTime");
			status=GetData(jo,"Status");
			/*--------------*/
			this._Price2Pay=GetV("price2pay");
			this._SvrAddr=GetData(jo,"SvrAddr");
			this._SvrPort=Integer.parseInt(GetData(jo,"SvrPort"));
			/*--------------*/
			this._Username=this.GetV("username");
			//this._NeedAsynNotify=this.GetV("ppneedasyncnotify");
		}
		

		return true;

		
	}
	public boolean Parse_str(String txt) 
	{
		if(txt==null){
			return false;
		}
		try {
			JSONObject Json_o=new JSONObject(txt);
			return Parse(Json_o);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean isNullOrEmpty(String input) {
	    return input == null || input.length() ==0;
	}
	public static String GetData(JSONObject jo,String key){
		try {
			return jo.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
	     	return null;
		}
	}
	public  String GetV(String key){
		return GetData(_Json_o,key);
	}
	public String GetKey()
	{
		return  this._Json_o.toString();
	}
	
	@Override
	public String toString(){
		return GetKey();
	}
	
	public String FileName()
	{
		
		if(!StringUtils.isEmpty(orderType)&&"local".equals(orderType)){
			//本地文件
			File f=new File(doc);
			if(f.exists()){
				return LibCui.getFileNameNoEx(f)+"."+LibCui.getExtensionName(f);
			}
			return this.doc;
		}
		
		
		return this.doc+"."+this.filetype;
	}
	public String GetDes()
	{
		StringBuffer sb=new StringBuffer();
		//sb.append("文件：");
		sb.append(FileName());sb.append("\n");
		///////////////////////////////////////////////////////
		sb.append("状态:"); 
		
		if(this.status.equals(STATUS_CHARGING)){
			sb.append("正在计算价格");
		}else if(this.status.equals(STATUS_CHARGED)){
			sb.append("计价完成，请支付");
		}else if(this.status.equals(STATUS_PRINTED_PENDING)){
			sb.append("文件进入打印队列");
		}else if(this.status.equals(STATUS_SUCCESS)){
			sb.append("打印完成");
		}else{
			sb.append("文件异常，联系客服");
		}
		
		sb.append("\n");
		///////////////////////////////////////////////////////		
		//sb.append("份数:"); sb.append(this.copies);sb.append("\n");
		sb.append("时间:"); sb.append(this.printTime);sb.append("\n");
		sb.append("打印范围:");sb.append(this.pages);sb.append("\n");
		
		sb.append("份数:"); sb.append(this.copies);sb.append("   ");
		if(!StringUtils.isEmpty(this._Price2Pay)){		
			double price_t=Double.valueOf(this._Price2Pay);
			if(price_t>=0){	sb.append("价格:");sb.append(this._Price2Pay);}
		}
		return sb.toString();
	}
	public File GetFile()
	{
		File f=new File(doc);
		if(f.exists()){
			return f;
		}else{
			return null;
		}
		
	}
	/**
	 * 没有计价，则获取计价情况
	 */
	public void UpdateStatusLocal(){
		
		if(UserInfoOrder.STATUS_CHARGING.equalsIgnoreCase(getStatus())){
			 //计算费用中
			new Thread(new UpdateStatusThread()).start();
		}else if(UserInfoOrder.STATUS_CHARGED.equalsIgnoreCase(getStatus())){
			 //计算费用完成
			
		}else if(UserInfoOrder.STATUS_PRINTED_PENDING.equalsIgnoreCase(getStatus())){		 
			new Thread(new UpdateStatusThread()).start();
		}else{
			
		}
		
	}
	/**
	 * 
	 */
	public String SendFileId2GetPrice(Socket s,FilesWithParams  fileName,String orderid) throws Exception
	  {
		/*--------------------------------------------------*/
			JSONObject jo_t=new JSONObject();
			jo_t.put("orderid_suffix", orderid);
			jo_t.put("cmd", "getprice");
		/*--------------------------------------------------*/		
			PhonePcCommunication ppc=new PhonePcCommunication(
				  s,
				  fileName,
				  fileName.getPrinter(), 
				  InetAddress.getByName(this._SvrAddr.replace("/", "")),
				  this._SvrPort);
		/*--------------------------------------------------*/
			return ppc.SendFileId2GetPrice(jo_t.toString());
	  }
	/**
	 * @throws Exception 
	 * 
	 */
	public String SendCmd2getResult(Socket s,FilesWithParams  fileName,String jsoncmd) throws Exception
	{
		/*--------------------------------------------------*/
		//JSONObject jo_t=new JSONObject();
		//jo_t.put("orderid_suffix", orderid);
	/*--------------------------------------------------*/		
		PhonePcCommunication ppc=new PhonePcCommunication(
			  s,
			  fileName,
			  fileName.getPrinter(), 
			  InetAddress.getByName(this._SvrAddr.replace("/", "")),
			  this._SvrPort);
	/*--------------------------------------------------*/
		return ppc.SendFileId2GetResult(jsoncmd);
		
	}
	/**
	 * 
	 */
	public String SendCmd(String cmd,String orderid) throws JSONException
	{
		String result="";
		JSONObject jo=new JSONObject();
		jo.put("cmd", cmd);
		jo.put("orderid_suffix", orderid);
		
		Socket s=new Socket();
		try {				
			 FilesWithParams fwp=new FilesWithParams(doc,null);
			 result=SendCmd2getResult(s, fwp,jo.toString());
			 s.shutdownInput();
	         s.shutdownOutput();				    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			 try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
		return result;
	} 
	/**
	 * 
	 * 
	 */
		public String GetPrice(String orderid)
		{
				String money="-1";
				Socket s=new Socket();
				try {				
					 FilesWithParams fwp=new FilesWithParams(doc,null);
					 money=SendFileId2GetPrice(s,fwp,orderid);
					 s.shutdownInput();
			         s.shutdownOutput();				    
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					 try {
						s.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
				}
				return money;
				
		}
	/**
	 *  
	 * 
	 */
		public boolean Send2Print(String orderid )
		{			
			String result = null;
			try {
				result = SendCmd("PrintDoc",orderid);
				if(!StringUtils.isEmpty(result)){
						JSONObject jo= new JSONObject(result);
						String r_t=jo.getString("result");
						if("success".equalsIgnoreCase(r_t)){
							return true;
						}					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			return false;
		} 
	/**
	 *  
	 * 
	 */
		 private   boolean _sync=true;//不可以是static
	public class UpdateStatusThread implements Runnable
	{		
		/**
		 *  
		 * 
		 */
			public boolean Send2Print(String orderid )
			{			
				String result = null;
				try {
					result = SendCmd("PrintDoc",orderid);
					if(!StringUtils.isEmpty(result)){
							JSONObject jo= new JSONObject(result);
							String r_t=jo.getString("result");
							if("success".equalsIgnoreCase(r_t)){
								return true;
							}					
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
				return false;
			} 
			/**
			 * 
			 */
	   public void UpdateMoneyStatus()
	   {		  
		   
		   if(!STATUS_CHARGING.equalsIgnoreCase(status)){			  
			   return ; //非计价状态
		   }
		   
		   if(StringUtils.isEmpty(orderType)){
			   return ;
		   }
		   
		   if(ORDER_TYPE_LOCAL.equals(orderType)){
					//本地订单							 
		   		String result=GetPrice(ID);
					 if(!StringUtils.isEmpty(result)){
						 
					 		double money=Double.valueOf(result);
					 		if(money>0){
					 				
					 				SetPrice2pay(money);//_Json_o.put("price2pay",money);
					 				setStatus(STATUS_CHARGED);
					 		}
											 
					 }				
	      }
	  }
		/**
		 * 
		 */
		public void CheckStatus2Print()
		{
			//如果处于挂起态
			if(STATUS_PRINTED_PENDING.equalsIgnoreCase(status)){
				//发送打印消息
				if(Send2Print(ID)){
					setStatus(UserInfoOrder.STATUS_SUCCESS);//设置打印成功
				}
				//打印消息，发送成功--改变状态
				//
			}
								
			
		}
	   
	  
		
	   @Override
		public void run() {
			// TODO Auto-generated method stub
		   synchronized(UserInfoOrder.this){
			   if(_sync==true){
					   _sync=false;
					   UpdateMoneyStatus();	
					   CheckStatus2Print();
					   _sync=true;
			   }
		   }
		
		   
			
		}
		
	}
	/**
	 * subject	String	必须String(128)
	 */
	public String GetAliPaySubject()
	{
		StringBuffer subject_sb=new StringBuffer();
		subject_sb.append(AliPay._Subject);subject_sb.append("_");
		subject_sb.append(this._Username);subject_sb.append("_");
		subject_sb.append(this.FileName());subject_sb.append("_");
		//subject_sb.append(this.);subject_sb.append("_");
		//subject_sb.append(AliPay._Subject);
		return GetSubString(subject_sb.toString(),128);
	}
	/**
	 * body	String(512)
	 */
	public String GetAlipayBody(){
		
		try {
			JSONObject jo=new JSONObject(this._Json_o.toString());
			jo.remove("phonenumber");
			jo.remove("DocumentName");
			jo.remove("orderid");
			jo.remove("orderid_suffix");
			return GetSubString(jo.toString(),512);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return GetSubString(_Json_o.toString(),512);
		
	}
	/**
	 * total_amount	Price	必须	9	订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
	 */
	public String GetAlipayPrice(){
		return GetSubString(this._Price2Pay,9);
	}
	/**
	 * out_trade_no	String	必须	64
	 */
	public String GetAlipayProductId(){	
		return GetSubString(this.ID,64);
	}
	/**
	 * 
	 */
	public String GetSubString(String value,int size){
		
			if(value!=null &&size>=0){
				return value.substring(0,Math.min(size,value.length()));	
			}else{
				return "";
			}
	   
	
		
	}



	/**
	 * 
	 */
	public void Save2Disk()
	{		
		if(!StringUtils.isEmpty(orderType)&&ORDER_TYPE_LOCAL.equals(orderType)){
			//本地订单
			OrderLocal.SaveLocalOrder2Disk(_Username, ID, _Json_o.toString());	
						
		}
	}
	/**
	 * 
	 */
	public boolean Delete4Disk()
	{		
		if(!StringUtils.isEmpty(orderType)&&ORDER_TYPE_LOCAL.equals(orderType)){
			//本地订单
			
			if(STATUS_PRINTED_PENDING.equalsIgnoreCase(status)){
				//挂起不删除，已经付钱了
				return false;
			}else{
			  return OrderLocal.DeleteLocalOrder4Disk(_Username, ID);	
				
			}
			
						
		}
		return true;
	}

}
