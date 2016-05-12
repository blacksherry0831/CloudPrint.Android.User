package com.jit.cloud_print_cc;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import android.provider.MediaStore.Files;
import android.view.View;

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
	public static final String STATUS_CHARGING="charging";
	public static final String STATUS_CHARGED="charged";
	public static final String ORDER_TYPE_LOCAL="local";
	/*-----------------------------------*/
  	JSONObject _Json_o;
  	public  String _Username;
  	public  String _Price2Pay;
	/*-----------------------------------*/
	public	String  color;//是否黑白
	public	String copies;//打印份数
	public	String doc;//文件名
	public	String filetype;//文件类型
	public	String ID;//任务ID
	public	String pages;//打印范围
	public	String printTime;//下单时间
	private	String status;//订单状态
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	/*----------------------------------*/
	private String orderType;
	/*----------------------------------*/
	private int _SvrPort=-1;
	private String _SvrAddr="";
	/*----------------------------------*/

	public UserInfoOrder(){
		
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
		return this.doc+"."+this.filetype;
	}
	public String GetDes()
	{
		StringBuffer sb=new StringBuffer();
		//sb.append("文件：");
		sb.append(FileName());sb.append("\n");
		sb.append("状态:"); sb.append(this.status);sb.append("\n");
		sb.append("份数:"); sb.append(this.copies);sb.append("\n");
		sb.append("时间:"); sb.append(this.printTime);sb.append("\n");
		sb.append("打印范围:");sb.append(this.pages);
		if(!StringUtils.isEmpty(this._Price2Pay)){		
			double price_t=Double.valueOf(this._Price2Pay);
			if(price_t>=0)	sb.append("价格:");sb.append(this._Price2Pay);
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
		/*--------------------------------------------------*/		
			PhonePcCommunication ppc=new PhonePcCommunication(
				  s,
				  fileName,
				  fileName.getPrinter(), 
				  InetAddress.getByName(this._SvrAddr.replace("/", "")),
				  this._SvrPort);
		/*--------------------------------------------------*/
			return ppc.SendFileId2GetPrice(orderid);
	  }
	/**
	 * 
	 */
	public class UpdateStatusThread implements Runnable
	{
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
		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
				if(!StringUtils.isEmpty(orderType)&&ORDER_TYPE_LOCAL.equals(orderType)){
					//本地订单
					try {
						 
						String result=GetPrice(ID);
						 if(!StringUtils.isEmpty(result)){
							 
							 		double money=Double.valueOf(result);
							 		if(money>0){
							 				_Json_o.put("Status",STATUS_CHARGED);
							 				_Json_o.put("price2pay",money);
							 				OrderLocal.SaveLocalOrder2Disk(_Username, ID, _Json_o.toString());	
							 		}
												 
						 }
						 
					} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
					
				}
				
				
			
			
			
		}
		
	}
	/**
	 * 
	 */
}
