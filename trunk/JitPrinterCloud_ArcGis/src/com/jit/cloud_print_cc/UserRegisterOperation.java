package com.jit.cloud_print_cc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class UserRegisterOperation extends UserInfoBase
{


  public String mPasswordConfirm=null;

  UserRegisterOperation (Context context)
  {
	  super(context);
  }
  public boolean CheakPassword()
  {
	  if(this.mPassword==null||this.mPassword.equals("")){
		  return false;
	  }
	  if(this.mPasswordConfirm==null||this.mPasswordConfirm.equals("")){
		  return false;
	  }
	  if(this.mPassword.equals(this.mPasswordConfirm)){
		  return true;
	  }
	  
	return false;
  }
  public String ParseReturnJsonMsg(String str)
  {
	  return "";
  }
  public String GetJsonStr() throws JSONException
  {
	  String json;	  
	  JSONObject jsonobj = new JSONObject();
	  jsonobj.put("LoginID", this.mUserName);
	  jsonobj.put("Password", this.mPassword);
	  jsonobj.put("Username", this.mTrueName);
	  jsonobj.put("Telphone", this.mPhoneNumber);	  
	  if(this.mSex==Seox.BOY){
		  jsonobj.put("Sex", "男");
	  }else if(this.mSex==Seox.GIRL){
		  jsonobj.put("Sex", "女");
	  }else{
		  jsonobj.put("Sex", "");
	  }	
	  JSONArray jsonarray=new JSONArray();
	  jsonarray.put(jsonobj);
	  json=jsonarray.toString();
	  return json;
  }
  public String Submit_v2()
  {
	  String result="";  
	  String param=Connect2QinServer.GetQinRegisterParam(mUserName,mPassword,mPhoneNumber,"Android");
	  String str_url=this._mRegisterQinUrl+"?"+param;
	  result=Connect2QinServer.GetParam2Server(str_url);
	  return result;
  }
  public boolean Submit ()
  {
	 
	  if(this.CheakPassword()){
		  /****密码正确***上传信息***/
		 try {
			String post_str=this.GetJsonStr();
		    String return_str=this.GetJson2Server(this.mRegisterUserUrl,"?reports="+post_str);
		    if(return_str.equals("")||return_str.equals("false")){
		    	return false;
		    }else{
		    	this.ParseReturnJsonMsg(return_str);
			    return true;
		    }
		    
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.mErrorStrArray.add(e.toString());
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.mErrorStrArray.add(e.toString());
			return false;
		}
		 
	  }else{
		  return false;
	  }
	  
  }
  
  public class ResisterResponse
  {
	  
  }
  
}
