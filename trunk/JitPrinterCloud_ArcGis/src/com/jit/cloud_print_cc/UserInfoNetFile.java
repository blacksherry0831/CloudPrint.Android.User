package com.jit.cloud_print_cc;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 *
 *
 *
 *
 *
 */
public class UserInfoNetFile extends UserInfoBase
{
	

	private  JSONObject _json_o;
	private  String _Pages;
	private  String _DocName;
	private  String _FileSize;
	private  String _FileType;
	private  String _ID;
	private  String _UploadTime;
	public UserInfoNetFile(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public boolean Parse(JSONObject jo) 
	{
		this._json_o=jo;
		/*--------------------------------------------------*/
			_Pages=UserInfoOrder.GetData(jo,"Pages");
			_DocName=UserInfoOrder.GetData(jo,"documentName");		
			_FileSize=UserInfoOrder.GetData(jo,"filesize");
			_FileType=UserInfoOrder.GetData(jo, "filetype");
			_ID=UserInfoOrder.GetData(jo,"id");
			_UploadTime=UserInfoOrder.GetData(jo,"uploadTime");
		/*--------------------------------------------------*/
		
	
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
		
		if(this._ID==null||this._ID.isEmpty()){
			return false;
		}

		return true;

		
	}
	public String get_DocName() {
		return _DocName;
	}
	public void set_DocName(String _DocName) {
		this._DocName = _DocName;
	}
	public String get_FileSize() {
		return _FileSize;
	}
	public float get_FileSizeFloat() 
	{
		float value_t=0;
		String a=new String(_FileSize).toLowerCase();
		if(a.contains("mb")){
			value_t= Float.valueOf(a.replaceAll("mb",""));
		}else if(a.contains("kb")){
			value_t= Float.valueOf(a.replaceAll("kb",""))/1024;
		}else{
			a="0";
		}
	
		return value_t;
	}
	public void set_FileSize(String _FileSize) {
		this._FileSize = _FileSize;
	}
	public String get_FileType() {
		return _FileType;
	}
	public void set_FileType(String _FileType) {
		this._FileType = _FileType;
	}
	public String get_ID() {
		return _ID;
	}
	public void set_ID(String _ID) {
		this._ID = _ID;
	}
	public boolean contian_ID(String ID) {
		if(ID==null){
			return false;
		}else{
			return ID.equals(this._ID);
		}
	  
	}
	public String get_UploadTime() {
		return _UploadTime;
	}
	public void set_UploadTime(String _UploadTime) {
		this._UploadTime = _UploadTime;
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
	public String GetFilename()
	{
		return this._DocName+"."+this._FileType;
	}
	public String GetFileUUIDname()
	{
		return this._DocName+this._ID+"."+this._FileType;
	}
	public boolean isDirectory() {
		// TODO Auto-generated method stub
		return false;
	}
	public String getAbsolutePath() {
		// TODO Auto-generated method stub
		return this._ID;
	}
	public void SetDirectory(UserInfoNetFile file) {
		// TODO Auto-generated method stub
		
	}
	public boolean isFile() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public String Get_Url(boolean thumbnail)
	{
	    String	File_URL=this._mDownloadFileQinUrl+"?"+Connect2QinServer.GetParamDownload(this,thumbnail);
	    	
		return File_URL;
	}
	/*public String Get_Url_thumbnail()
	{
	    String	File_URL=this._mDownloadFileQinUrl+"?"+Connect2QinServer.GetParamDownload(this,true);
				
		return File_URL;
	}*/
}
