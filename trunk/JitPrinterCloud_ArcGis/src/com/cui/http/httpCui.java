package com.cui.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import com.ta.utdid2.android.utils.StringUtils;

public class httpCui {
	public static  String GetParam2Server(String str_url)
	 {
		return GetParam2Server(str_url,"GET","x-www-form-urlencode");
	 }
	
	
	public static  String GetParam2Server(String str_url,String method,String content_type)
	{
		 String RequestData="";
		 String str=str_url;
		 String HttpStatus="Default";
        try {
            URL url=new URL(str);
            
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(3*1000);
            connection.setRequestMethod(method);
            connection.setDoInput(true);
            connection.setDoOutput(true);           
            if(!StringUtils.isEmpty(content_type)){
            	connection.setRequestProperty("content-type", content_type);
            }
            if(!StringUtils.isEmpty(RequestData)){
            	 /*----------------------读取文件上传到服务器--------------------*/
			            BufferedOutputStream  out=new BufferedOutputStream(connection.getOutputStream());
			            byte [] bytes_t=RequestData.getBytes("UTF8");
		                out.write(bytes_t, 0, bytes_t.length);
		                out.flush();          
                /*---------------------读取文件上传到服务器----------------------------*/
            }
           
            //读取URLConnection的响应
            int http_response_code=connection.getResponseCode();
                   String FileID="";
						if(http_response_code == 200){
							 byte [] bytes=new byte[1024];
					            DataInputStream in=new DataInputStream(connection.getInputStream());
					            Arrays.fill(bytes, (byte) 0x00);
					            
					            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					            int len_read=0;
					            while(true){
					            	len_read=in.read(bytes,0,bytes.length);
					            	if(len_read==-1) break;
					            	outStream.write(bytes, 0, len_read);	
					            }						            
					            FileID=outStream.toString("UTF-8");
					            if(FileID==null) FileID="";
					            FileID.replace("\000", "");						            	 
					           			          
					           if(FileID==null||FileID.equals("")){
					        	   HttpStatus="";
					           }else{
					        	   HttpStatus=FileID;
					           }
					          
					  }else{
						  HttpStatus="Http Response Status:"+http_response_code;
					  }
        } catch (Exception e) {
        	if(e!=null)
            HttpStatus=e.getMessage();
        }finally{
        	
        }
        return HttpStatus;
	}
	
	
	/** 
     * 获取指定URL的响应字符串 
     * Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp;q=0.8
	*Accept-Encoding:gzip, deflate, sdch
	*Accept-Language:zh-CN,zh;q=0.8
	*Cache-Control:max-age=0
	*Connection:keep-alive
	*Host:blacksherry-software-distribution.oss-cn-shenzhen.aliyuncs.com
	*Upgrade-Insecure-Requests:1
	*User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36
     * @param urlString 
     * @return 
     */  
    public static String getURLResponse_ReadOnly(String urlString){  
        HttpURLConnection conn = null; //连接对象  
        InputStream is = null;  
        String resultData = "";  
        try {  
            URL url = new URL(urlString); //URL对象  
            conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接 
            conn.setRequestMethod("GET"); //使用get请求
            //conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            //conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            //conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            //conn.setRequestProperty("Cache-Control", "max-age=0");
            //conn.setRequestProperty("Connection", "keep-alive");
            //conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36");
            
            
            
            
            
            
            //conn.setUseCaches(true);
            conn.setDoInput(true); //允许输入流，即允许下载  
            //conn.setDoOutput(true); //允许输出流，即允许上传  
          
             int status=conn.getResponseCode();
            is = conn.getInputStream();   //获取输入流，此时才真正建立链接  
          
            InputStreamReader isr = new InputStreamReader(is);  
            BufferedReader bufferReader = new BufferedReader(isr);  
            String inputLine  = "";  
            while((inputLine = bufferReader.readLine()) != null){  
                resultData += inputLine + "\n";  
            }  
  
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }finally{  
            if(is != null){  
                try {  
                    is.close();  
                } catch (IOException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
            if(conn != null){  
                conn.disconnect();  
            }  
        }  
  
        return resultData;  
    }  
}
