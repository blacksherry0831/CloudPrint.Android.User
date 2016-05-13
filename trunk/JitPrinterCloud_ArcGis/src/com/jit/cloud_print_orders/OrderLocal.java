package com.jit.cloud_print_orders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jit.cloud_print_cc.LibCui;

public class OrderLocal {
	
	 /**
     * 读取txt文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String txt2String(File file){
        String result = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            //使用readLine方法，一次读一行
            while((s = br.readLine())!=null){
                result = result + "\n" +s;
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
	/**
	 * 
	 * 
	 */
	public static void SaveLocalOrder2Disk(String username,String orderid,String json)
	{
		File path=new File(LibCui.GetCloudPrintCfgFile()+"/"+username);
	    if(!path.exists()){
	  	   path.mkdirs();
	    }
	    
	    File orderidfile=new File(path,orderid);
	    
	    LibCui.SaveString2Fille(orderidfile, json);
	   
	    
	    
	}
	/**
	 * 
	 * 
	 */
	public static String GetLocalOrder4Disk(String username)
	{
		File path=new File(LibCui.GetCloudPrintCfgFile()+"/"+username);
	    if(!path.exists()){
	  	   path.mkdirs();
	    }
	    
	    JSONObject jo_ALL=new JSONObject();
	    JSONArray ja_ALL=new JSONArray();
	    
	    for(File f : path.listFiles()){
	    	
	    	if(f.isFile()){
	    		String order=txt2String(f);
	    		try {
					JSONObject jo_t=new JSONObject(order);
					ja_ALL.put(jo_t);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		
	    	}
	    	
	    }
	    
	    try {
			jo_ALL.put("total", String.valueOf(ja_ALL.length()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			jo_ALL.put("rows",ja_ALL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    try {
			jo_ALL.put("ordertype","local");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //File orderidfile=new File(path,orderid);
	    
	    //SaveString2Fille(orderidfile, json);
	   
	    
	    return jo_ALL.toString();
	}
	/**
	 * 
	 * 
	 * 
	 */
	public static void DeleteLocalOrder4Disk(String username,String orderid)
	{
		File path=new File(LibCui.GetCloudPrintCfgFile()+"/"+username);
	    File orderidfile=new File(path,orderid);
	    if(orderidfile.exists()){
	    	orderidfile.delete();
	    }
	}
	/**
	 * 
	 * 
	 * 
	 */

}
