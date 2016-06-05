package com.jit.cloud_print_cc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoOrdersManager 
{
	
	public interface InfoChanged{
		void LocalOrderStatusChanged();
	}
	InfoChanged   _IC;
	private int _total=0;
	private String _json_text;
	private List<UserInfoOrder> _orders=new ArrayList<UserInfoOrder>();
	public UserInfoOrdersManager(String json_text)
	{
		this.Parse(json_text);
	}
	public UserInfoOrdersManager(Object json_text) 
	{
		// TODO Auto-generated constructor stub
	}
    private void notify_changed()
    {
	   if(_IC!=null){_IC.LocalOrderStatusChanged();}
    }
    public void SetNotiftyChanged(InfoChanged ic)
    {
      _IC=	ic;
    }
	public int GetCount()
	{
		return this._orders.size();
	}
	/**
	 * 
	 * 
	 */
	public void Clear()
	{
		this._orders.clear();
	}
	public Object GetItem(int pos)
	{
		return this._orders.get(pos);
	}
	
	/**
	 * 未完成的订单，显示在前面
	 **/
	public void Sort(){
		
	}
	/**
	 * 
	 * 
	 */
	public void add(UserInfoOrder uio)
	{
		this._orders.add(uio);
		uio.UpdateStatusLocal();
	}
	/**
	 * 
	 * 
	 */
	public void delete(UserInfoOrder uio)
	{
		_orders.remove(uio);
		notify_changed();
		
	}
	
	/**
	 * 
	 * 
	 */
	  public int Parse(String json_text)
	  {
		  if(json_text==null || json_text.equals("")) 
			{
				    return 0;
			}else{			
					try {
						
							JSONObject jo=new JSONObject(json_text);
							this.Clear();				
							return this.Parse(jo);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					return 0;
			}
	  }
	/**
	 * 
	 * 
	 */
	public int Parse(JSONObject jo)
	{
		
		try {
			this._total=jo.getInt("total");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
		try {
			JSONArray jsonStrs=jo.getJSONArray("rows");
			
			for(int i=0;i<jsonStrs.length();i++){
				
				UserInfoOrder user=new UserInfoOrder();
				user.SetNotiftyChanged(_IC);
				if(user.Parse(jsonStrs.getJSONObject(i))){
					this.add(user);
				}
			}
				
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
		
		
		
		return this._total;
		
		//UserInfoOrder user=new UserInfoOrder();
		//user.Parse_str(json_txt);
		//_orders.add(new UserInfoOrder ());
		
	}
	
	public static String GetData(JSONObject jo,String key){
		try {
			return jo.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
	     	return null;
		}
	}
   public void Refash(){
	   
	   try{
		   for(UserInfoOrder uio :this._orders){
			   uio.UpdateStatusLocal();
		   }
	   }catch(Exception e){
		   
	   }
	   
	   
   }
   
 
}
