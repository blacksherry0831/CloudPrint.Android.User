package com.jit.cloud_print_cc;

import java.io.Serializable;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.xmlpull.v1.XmlSerializer;

import com.jit.cloud_print_cc.CloudPrintAddressBase.PrintType;



import android.content.Context;
import android.util.Xml;

class PCPhonePrinterListItem implements Serializable
{
    /**
	 * 
	 */
	private static boolean USE_POOL=true;
	private static boolean USE_HEART_POLL=false;
	private static final long serialVersionUID = -7977863124283509744L;
	//public boolean mPCListUpdata=false;
    //public boolean mPhoneListUpdata = false; 
	boolean mThreadRun=true;
    private String mPCListId=null;
    private String mPhoneListID=null; 
    private String mPrintListId=UUID.randomUUID().toString();
	private final HashMap<String, ServerInfo> mPCList = new HashMap<String, ServerInfo>();
	private final HashMap<String, ServerInfo> mPhoneList = new HashMap<String, ServerInfo>();
	private final HashMap<String, CloudPrintAddress> mPrintList = new HashMap<String,  CloudPrintAddress>();
	@SuppressWarnings("unused")
	private Context mContext=null;
	//private PrintListItem mPrinterList=new PrintListItem();
	
	PCPhonePrinterListItem(Context Context)
	{
		this.mContext=Context;
		/*---------------------*/
		if(USE_POOL){
			Thread t=new Thread(new PollPcPhonePrinterList());
			t.start();
		}		
		/*
		this.AddOnePrinter(new CloudPrintAddress(
				"172.17.2.1", 
				PrintFrom.Self,
				ServerInfo.GetSelf(),
				PrintType.NetPrint));*/
		
		//this.addItem(new ServerInfo(Context).GetDefaultLocalPC());
	}
	/**
	 * 
	 */
	 public void Destory()
	 {
		 this.mThreadRun=false;
	 }
/**
 * 
 * @return
 */
	   public String GetPcListId(){
		   if(mPCListId!=null){
			   return mPCListId;
		   }else{
			   return "PC null";
		   }
		  
	   }
/**
* 
* @return
*/
	   public String getPhoneListId(){
		   if(mPhoneListID!=null){
			   return mPhoneListID;
		   }else{
			   return "Phone null";
		   }
		  
	   }
/**
* 
* @return
*/	
   public String GetPrinterListId()
   {
   	if(mPrintListId!=null){
		   return mPrintListId;
	   }else{
		   return "Printer null";
	   }
   }
/**
* 
* @return
*/
	   public String getPCPhoneListId()
	   {
		   
		   return this.GetPcListId()+this.getPhoneListId();
		  
	   }
/**
* 
* @return
*/
       public void addItem( ServerInfo s)
       { 
    	   if(s==null){
    		   return;
    	   }
    	   
    	   if(s.mPort<=1024) 	  
    		   return;
    	  
           if(s.mHostType==1){
        	    this.AddPrinters(s);
               if (this.mPCList.containsKey(s.GetKey()))
               {
            	   ServerInfo si_t=this.mPCList.get(s.GetKey());
            	   si_t.mTimeAlive=System.currentTimeMillis();
            	   si_t.SetBaseInfo(s);
            	   
            	   
                  
               }else {
            	   s.mTimeAlive=System.currentTimeMillis();
                   this.mPCList.put(s.GetKey(), s);
                   this.mPCListId = UUID.randomUUID().toString();
               }
           
               
           }else if(s.mHostType==2){
               if (this.mPhoneList.containsKey(s.GetKey()))
               {
            	   this.mPhoneList.get(s.GetKey()).mTimeAlive=System.currentTimeMillis();
            	   if(this.mPhoneList.get(s.GetKey()).mHostPhoneNameAlias.equals(s.mHostPhoneNameAlias)){
            		   
            	   }else{
                	   this.mPhoneList.get(s.GetKey()).mHostPhoneNameAlias=s.mHostPhoneNameAlias;
                	   this.mPhoneListID =UUID.randomUUID().toString(); 
            	   }

                   return;
               }
               else
               {
            	   s.mTimeAlive=System.currentTimeMillis();
                   this.mPhoneList.put(s.GetKey(), s);
                   this.mPhoneListID =UUID.randomUUID().toString();
               }
           }else{
           
           }
       
       }
       public void RemoveItem( ServerInfo s)
       {

           if (s.mHostType == 1){
               this.mPCList.remove(s.GetKey());
               this.mPCListId = UUID.randomUUID().toString();
           }
           else if (s.mHostType == 2){
               this.mPhoneList.remove(s.GetKey());
               this.mPhoneListID = UUID.randomUUID().toString();
           }else{

          }
       }
       public ArrayList<ServerInfo> GetPCServerInfo()
       {   	   
    	   //Iterator<String> it = mPCList.keySet().iterator();
    	   ArrayList<ServerInfo>  listValue=new ArrayList<ServerInfo>();        
            
           Collection<ServerInfo> collection = mPCList.values();
           Iterator<ServerInfo> iterator = collection.iterator();
           while (iterator.hasNext()) {
        	   ServerInfo value = iterator.next();
        	   listValue.add(value);
           }
    	   
           return listValue;
       }
       public ArrayList<ServerInfo> GetPhoneServerInfo()
       {
    	   
    	   ArrayList<ServerInfo>  listValue=new ArrayList<ServerInfo>();        
           
           Collection<ServerInfo> collection = this.mPhoneList.values();
           Iterator<ServerInfo> iterator = collection.iterator();
           while (iterator.hasNext()) {
        	   ServerInfo value = iterator.next();
        	   listValue.add(value);
           }
    	   
           return listValue;
       }
       
      
      
        
        public void AddPrinters(ServerInfo si)
    	{
    		ArrayList<CloudPrintAddress> printer=si.PrinterList();
    		if(printer!=null){
    			for(CloudPrintAddress cpa:printer){
    				 this.AddOnePrinter(cpa);
    			}
    		}
    	}
    	public void RemovePrinters(ServerInfo si)
    	{
    		
    	}
    	public void	  AddOnePrinter(CloudPrintAddress pi)
    	{
    		if(LibCui.getLocalIpAddressList().contains(pi.mAddress)){
    			//本机
    			return;
    		}
    			if (this.mPrintList.containsKey(pi.GetKey()))
                {
             	  
                    return;
                }
                else {
             	 
                    this.mPrintList.put(pi.GetKey(), pi);
                    this.mPrintListId = UUID.randomUUID().toString();
                }
    		
    	}
    	public void  RemoveOnePrinter(CloudPrintAddress pi)
    	{
    		
    	}

        public void CheckOffLinePoll()
        {
        	
        }
/**
 * 
 * 
 * 
 */   
                public void RemoveUnUsePrintersFromPc(){
                	String profix=PrintType.PcPrint.toString();
                	if(mPrintList==null) return;
                	synchronized(mPrintList) { 
        				if(mPrintList!=null){				 	
        					 Set<String> key_org= mPrintList.keySet();
        					 List<String> key=new ArrayList<String>();
        					 for(String k :key_org){
        						 key.add(new String(k));						 
        					 }					 
        					
        					 for(String k :key){
        						 if(k.contains(profix)){
        							
        							  CloudPrintAddress cpa=mPrintList.get(k);
        							  if(cpa!=null&&cpa.ISPrinterAlive()){
        								  
        							  }else{
        								  if(mPrintList.remove(k)!=null)
        								  this.mPrintListId=UUID.randomUUID().toString();
        							  }
        						 }
        					 }
        					 
        				 }
        				
        				
        				
        		    } 
                }    
/**
 * 
 * 
 * 
 */   
        public void RemoveAllPrintersFromPc(){
        	String profix=PrintType.PcPrint.toString();
        	synchronized(mPrintList) { 
				if(mPrintList!=null){				 	
					 Set<String> key_org= mPrintList.keySet();
					 List<String> key=new ArrayList<String>();
					 for(String k :key_org){
						 key.add(new String(k));						 
					 }					 
					 for(String k :key){
						 if(k.contains(profix)){
							 mPrintList.remove(k); 
						 }
					 }
				 }
				
				
				this.mPrintListId=UUID.randomUUID().toString();
		    } 
        }    
/**
 * 
 * 
 * 
 */      
        public void RemoveAllPrinters(){
        	synchronized(mPrintList) { 
				if(mPrintList!=null)
				 mPrintList.clear();
				this.mPrintListId=UUID.randomUUID().toString();
		    } 
        } 
       
        public void removeAllPc(){
        	synchronized(mPCList) { 
				if(mPCList!=null)
				mPCList.clear();
				this.mPCListId=UUID.randomUUID().toString();
			} 
        }
        public void RemoveAllPhone(){
        	synchronized(mPhoneList) {
				if(mPhoneList!=null)
				 mPhoneList.clear();
				this.mPhoneListID=UUID.randomUUID().toString();
		    } 
        }
        
        public ArrayList<CloudPrintAddress> GetPrinters()
        {   	   
     	    ArrayList<CloudPrintAddress>  listValue=new ArrayList<CloudPrintAddress>();        
             
            Collection< CloudPrintAddress> collection =mPrintList.values();
            Iterator< CloudPrintAddress> iterator = collection.iterator();
            while (iterator.hasNext()) {
            	 CloudPrintAddress value = iterator.next();
         	   listValue.add(value);
            }
     	   
            return listValue;
        }
/**
 * 
 * 
 * 
 */
        public class PollPcPhonePrinterList implements Runnable
        {
        	public void CheakListByWiFiConnected()
			{
				if(LibCui.isWifiEnabled(mContext)==false){
					//wifi断开					
					RemoveAllPrinters();	
					removeAllPc();	
					RemoveAllPhone();
				}
			}
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					while(mThreadRun){
	    				if(USE_HEART_POLL){
	    					PollPcPhoneListByHeart p=new PollPcPhoneListByHeart();
	    					p.Cheak();
	    				}else{
	    					try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    				}
	    				/*-wifi断开清空表格-*/
	    				this.CheakListByWiFiConnected();
    					  
    					
    			}			
			}
/*---------------------------------------------------------------------------------------*/
				  public class PollPcPhoneListByHeart implements Runnable
			        {
								        	private void RemoveUnAliveDev(HashMap<String, ServerInfo> list)
								            {
								             	long  TimeSpan=NetPhonePc.UDP_NOTIFY_INTERVAL*2;
								             	 ArrayList<ServerInfo>  listRemove=new ArrayList<ServerInfo>();
								           	  //一级验证200
								           	   for(Iterator<String>   it   = list.keySet().iterator();   it.hasNext();){ 
								       				//中取
								       		        String   key   =   it.next(); 
								       		        ServerInfo   value   =   list.get(key);	
								       		        long timeSpan=System.currentTimeMillis()-value.mTimeAlive;
								       		        if(timeSpan>TimeSpan){
								       		        	listRemove.add(value);
								       		        }
								       			}
								           	   //二级验证
								           	   for(int i=0;i<listRemove.size();i++){
								           		 ServerInfo   value   =  listRemove.get(i);
								           		 long timeSpan=System.currentTimeMillis()-value.mTimeAlive;
								     		        if(timeSpan>TimeSpan){
								     		        	 RemoveItem(value);
								     		        }
								           		
								           		 
								           	   }
								           	   
								            }
								        	/*private void RemoveUnAliveDev2(HashMap<String, ServerInfo> list)
								            {
								            
								             	 ArrayList<ServerInfo>  listRemove=new ArrayList<ServerInfo>();
								           	  //一级验证200
								           	   for(Iterator<String>   it   = list.keySet().iterator();   it.hasNext();){ 
								       				//中取
								       		        String   key   =   (String)   it.next(); 
								       		        ServerInfo   value   =   list.get(key);	       		        
								       		        try {
														if(value.mAddress.isReachable(PhonePcCommunication.ConnectTimeOut)==false){
															listRemove.add(value);
														}
													} catch (Exception e) {
														// TODO Auto-generated catch block
														listRemove.add(value);						
													}
								       			}
								           	   //二级验证
								           	   for(int i=0;i<listRemove.size();i++){
								           		 ServerInfo   value   =  listRemove.get(i);           		 
								     		        	 RemoveItem(value);
								           	   }
								           	   
								            }*/
								        	public void pollPcPhone()
								             {
									          	 
									          	   
									          	  this.RemoveUnAliveDev(mPCList);
									          	  this.RemoveUnAliveDev(mPhoneList);
								          	      
								             }
								    		@Override
								    		public void run() {
								    			
												// TODO Auto-generated method stub
								    			while(mThreadRun){
								    				
								    				this.Cheak();
								    					  
								    					
								    			}
								    		}
								    		public void Cheak(){
								    			int count=0;
								    			pollPcPhone();
												try {
													Thread.sleep(1000);
												} catch (InterruptedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												if(count++%5==0){
													this.CheckIsAlive(mPCList);
													this.CheckIsAlive(mPhoneList);
												}
											
								    		}
								    		public void CheckIsAlive(HashMap<String, ServerInfo> list)
								    		{
								    			for(Iterator<String>   it   = list.keySet().iterator();   it.hasNext();){ 
								       				//中取
								       		        String   key   =   it.next(); 
								       		        ServerInfo   value   =   list.get(key);	
								       		        //
								       		        this.UdpNotifyMsg_heart(value.mAddress,value.GetUdpPort());
								       			}
								    		}
											public  void UdpNotifyMsg_heart(InetAddress Ia,int port)
											      {
													  				   
													   /*---------------------------------------*/				  
													   byte data[]=GetNotifyMessageXml("isalive");
													 
													   for (int i = 0; i < NetPhonePc.mUdpSetverPort.length; i++){
														   sendMsg2Reponse sm2n = new sendMsg2Reponse(Ia,NetPhonePc.mUdpSetverPort[i],data);	
													   }
																		 
											      }
											public byte[] GetNotifyMessageXml(String msg) 
											{
												
												byte[] buf = new byte[5];
												XmlSerializer serializer = Xml.newSerializer();   
												StringWriter writer = new StringWriter();
												try{  
													serializer.setOutput(writer); 
													serializer.startDocument("UTF-8",true);
													serializer.startTag("","CloudPrint"); 
													serializer.startTag("","Notify");						
													/*--------------------------------------*/
													serializer.startTag("","Multiply");
												
													serializer.text(msg);
																
													serializer.endTag("","Multiply");
													/*--------------------------------------*/
													serializer.endTag("","Notify");
													serializer.endTag("","CloudPrint");
													serializer.endDocument();  
													/*--------------------------------------*/
													String xml_doc=new String(writer.toString());
												    buf=xml_doc.getBytes("UTF-8");
												}catch(Exception e){
													return null;
												}  
												
											    return buf;
											}
								    		
								    	} 
        }
/*---------------------------------------------------------------------------------------*/     
}
/**
 * 
 * 
 * 
 */