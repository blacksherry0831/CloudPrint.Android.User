package com.jit.cloud_print_cc;

import org.json.JSONException;
import org.json.JSONObject;

import com.ta.utdid2.android.utils.StringUtils;
/**
 *用户订单信息 
 *
 *
 *
 */
public class UserInfoOrder 
{
	JSONObject _Json_o;
	public	String  color;//是否黑白
	public	String copies;//打印份数
	public	String doc;//文件名
	public	String filetype;//文件类型
	public	String ID;//任务ID
	public	String pages;//打印范围
	public	String printTime;//下单时间
	public	String status;//订单状态
	
	private String orderType;

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
		
		return sb.toString();
	}
}
