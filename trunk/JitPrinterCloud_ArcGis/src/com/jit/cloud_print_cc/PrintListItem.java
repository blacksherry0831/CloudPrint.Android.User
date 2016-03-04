package com.jit.cloud_print_cc;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;


import com.jit.cloud_print_cc.CloudPrintAddressBase.PrintFrom;
import com.jit.cloud_print_cc.CloudPrintAddressBase.PrintType;
import com.jit.cloud_print_cc.ServerInfo;


@SuppressWarnings("unused")
public class PrintListItem  implements Serializable
{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -7908581653548731382L;
	private HashMap<String, CloudPrintAddress> mPrintSelfList = new HashMap<String,  CloudPrintAddress>();
	private String mPrintSelfListId=UUID.randomUUID().toString();

	
	public PrintListItem(Context context)
	{
		
		/*this.AddOnePrinter(new CloudPrintAddress(
				"172.17.2.1", 
				PrintFrom.Self,
				ServerInfo.GetSelf(),
				PrintType.NetPrint));*/
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
		
			if (this.mPrintSelfList.containsKey(pi.GetKey()))
            {
         	  
                return;
            }
            else {
         	 
                this.mPrintSelfList.put(pi.GetKey(), pi);
                this.mPrintSelfListId = UUID.randomUUID().toString();
            }
		
	}
	public void  RemoveOnePrinter(CloudPrintAddress pi)
	{
		
	}

    public void CheckOffLinePoll()
    {
    	
    }
	public class CheckOffLineServer implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
				
			}
			
		
		
	}
    public String GetListId()
    {
    	return mPrintSelfListId;
    }
    public ArrayList<CloudPrintAddress> GetPrintSelfServerInfo()
    {   	   
 	    ArrayList<CloudPrintAddress>  listValue=new ArrayList<CloudPrintAddress>();        
         
        Collection< CloudPrintAddress> collection =mPrintSelfList.values();
        Iterator< CloudPrintAddress> iterator = collection.iterator();
        while (iterator.hasNext()) {
        	 CloudPrintAddress value = iterator.next();
     	   listValue.add(value);
        }
 	   
        return listValue;
    }
}
