package com.jit.cloud_print_cc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class LibCui {
    /* 以最省内存的方式读取本地资源的图片
     * @param context
   * @param resId
   * @return
   */  
 @SuppressWarnings("deprecation")
public static Bitmap readBitMap(Context context, int resId){  
     BitmapFactory.Options opt = new BitmapFactory.Options();  
     opt.inPreferredConfig = Bitmap.Config.RGB_565;   
     opt.inPurgeable = true;  
     opt.inInputShareable = true;  
     //获取资源图片  
       InputStream is = context.getResources().openRawResource(resId);  
     return BitmapFactory.decodeStream(is,null,opt);  
   }
 public static void SetViewBackGround(View v,Context context, int resId){  
	 	Bitmap bmp=readBitMap(context,resId);
	 	BitmapDrawable bmpdaw=new BitmapDrawable(context.getResources(),bmp);
	 	v.setBackground(bmpdaw);
   }
public static View getViewFromeXml(int mLayoutId,Context Context_unknow)
    {
	   Context mContext=Context_unknow;
	   View mView=null;
			try{
				  LayoutInflater mInflater = LayoutInflater.from(mContext);	  
				  mView= mInflater.inflate(mLayoutId,null,true);
			}catch(InflateException ife){
				Log.v("1",ife.getMessage());
			}
			if(mView==null){
				mView=new View(mContext);
				mView.setBackgroundColor(0xff66ccff);
			}
			return mView;
	  
	}
public static View getViewFromeXml(int mLayoutId,ViewGroup parent,Context Context_unknow)
{
   Context mContext=Context_unknow;
   View mView=null;
		try{
			  LayoutInflater mInflater = LayoutInflater.from(mContext);	  
			  mView= mInflater.inflate(mLayoutId,parent, false);
		}catch(InflateException ife){
			Log.v("1",ife.getMessage());
		}
		if(mView==null){
			mView=new View(mContext);
			mView.setBackgroundColor(0xff66ccff);
		}
		return mView;
  
}
public static  void showPopupWindow(
		 Context context,
		 View parent,
		 View vPopupWindow)
{	       
	      Rect t=new Rect();
	      parent.getGlobalVisibleRect(t);
	      vPopupWindow.setFocusable(true); // 这个很重要
	      vPopupWindow.setFocusableInTouchMode(true);		    
        final PopupWindow pw= new PopupWindow(vPopupWindow,t.width(),t.height(),true);  
        pw.setFocusable(true);
        pw.setOutsideTouchable(false);
        pw.setBackgroundDrawable(new BitmapDrawable());
         vPopupWindow.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
		            pw.dismiss();
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
		            return true;
		        }
		        return false;
			}
		});
        //显示popupWindow对话框  
	        // pw.showAtLocation(parent, gravity, x, y)
	    pw.showAtLocation(parent, Gravity.LEFT|Gravity.TOP,t.left,t.top);  
}  

public static int getStatusHeight(Activity activity){
    int statusHeight = 0;
    Rect localRect = new Rect();
    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
    statusHeight = localRect.top;
    if (0 == statusHeight){
        Class<?> localClass;
        try {
            localClass = Class.forName("com.android.internal.R$dimen");
            Object localObject = localClass.newInstance();
            int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
            statusHeight = activity.getResources().getDimensionPixelSize(i5);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    return statusHeight;
}
//宽
public static int getViewWidth(LinearLayout view){
    view.measure(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    return view.getMeasuredWidth();
}
//高
public static int getViewHeight(LinearLayout view){
    view.measure(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    return view.getMeasuredHeight();
}
/*
 * Java文件操作 获取文件扩展名
 *
 *  Created on: 2011-8-2
 *      Author: blueeagle
 */
    public static String getExtensionName(File file)
    { 
    	String filename=file.getName();
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length() - 1))) { 
                return filename.substring(dot + 1); 
            } 
        } 
        return filename; 
    } 
    /*
     * Java文件操作 获取不带扩展名的文件名
     *
     *  Created on: 2011-8-2
     *      Author: blueeagle
     */
 public static String getFileNameNoEx(File file) 
        { 
	 		String filename=file.getName();
            if ((filename != null) && (filename.length() > 0)) { 
                int dot = filename.lastIndexOf('.'); 
                if ((dot >-1) && (dot < (filename.length()))) { 
                    return filename.substring(0, dot); 
                } 
            } 
            return filename; 
        } 
public static String getFileExtension(String fileName) {
	int index = fileName.lastIndexOf(".");
	if (index == -1) return "";
	return fileName.substring(index + 1, fileName.length()).toLowerCase(Locale.getDefault());
}
public static void SetBackground2View(View v,int bgmId,Context context)
{
	
	try {  
		 Bitmap bmp=readBitMap(context,bgmId);
		 SetBackground2View(v,bmp,context.getResources());
    } catch (OutOfMemoryError e){
    	v.setBackgroundColor(Color.BLUE);
    }catch(Exception e){  
        v.setBackgroundColor(Color.BLUE);
    }  
	
	
}
public static Bitmap SetBackground2View_ver2(View v,int bgmId,Context context)
{
	 Bitmap bmp_bg=null;
	try {  
		 Bitmap bmp=readBitMap(context,bgmId);
		 bmp_bg=SetBackground2View_ver2(v,bmp,context.getResources());
    } catch (OutOfMemoryError e){
    	v.setBackgroundColor(Color.BLUE);
    }catch(Exception e){  
        v.setBackgroundColor(Color.BLUE);
    }  
	return bmp_bg;
	
}
public Bitmap GetBackgroundBitmap()
{
	 //Bitmap bmp=readBitMap(context,bgmId);
	return null;
	
}
public static void RecycleBitMap(Bitmap bitmap)
{
	if(bitmap!=null&&!bitmap.isRecycled()){
		 bitmap.recycle() ;
		 bitmap=null;
		 System.gc();
	}
}
public static void SetBackground2View(View v,Bitmap bmp,Resources res)
{
	int vWidth=v.getWidth();
	int vHeight=v.getHeight();
	int bmpWidth=bmp.getWidth();
	int bmpHeight=bmp.getHeight();
	double bmpwhScale=1.0*bmpHeight/bmpWidth;
	Bitmap bmpNew;
	int []bmpWidth_new=new int[2];
	int []bmpHeight_new=new int[2];
	int []bmpArea_new=new int[2];
	    bmpWidth_new[0]=vWidth;
	    bmpHeight_new[0]=(int)(vWidth*bmpwhScale);
	    
	    bmpHeight_new[1]=vHeight;
		bmpWidth_new[1]=(int)(vHeight/bmpwhScale);
		for(int i=0;i<2;i++){
			bmpArea_new[i]=bmpWidth_new[i]*bmpHeight_new[i]-vWidth*vHeight;
		}
		
		int newwhIdx;
		if(bmpArea_new[1]>bmpArea_new[0]){
			newwhIdx=1;
		}else{
			newwhIdx=0;
		}
		bmpNew=LibCui.ScaleBitmap(bmp, bmpWidth_new[newwhIdx], bmpHeight_new[newwhIdx]);
		
	    Bitmap	bmpCanvas=Bitmap.createBitmap(v.getWidth(),v.getHeight(),bmp.getConfig());
		
	   
	    Canvas cv=new Canvas(bmpCanvas);	    
	    cv.drawBitmap(bmpNew,0,0,null);	
	    v.setBackground(null);
	    v.setBackground(new BitmapDrawable(res,bmpCanvas));
	    /*-------------------------------------------------*/
	    RecycleBitMap(bmpNew);
	    RecycleBitMap(bmp);
}
public static Bitmap SetBackground2View_ver2(View v,Bitmap bmp,Resources res)
{
	int vWidth=v.getWidth();
	int vHeight=v.getHeight();
	int bmpWidth=bmp.getWidth();
	int bmpHeight=bmp.getHeight();
	double bmpwhScale=1.0*bmpHeight/bmpWidth;
	Bitmap bmpNew;
	int []bmpWidth_new=new int[2];
	int []bmpHeight_new=new int[2];
	int []bmpArea_new=new int[2];
	    bmpWidth_new[0]=vWidth;
	    bmpHeight_new[0]=(int)(vWidth*bmpwhScale);
	    
	    bmpHeight_new[1]=vHeight;
		bmpWidth_new[1]=(int)(vHeight/bmpwhScale);
		for(int i=0;i<2;i++){
			bmpArea_new[i]=bmpWidth_new[i]*bmpHeight_new[i]-vWidth*vHeight;
		}
		
		int newwhIdx;
		if(bmpArea_new[1]>bmpArea_new[0]){
			newwhIdx=1;
		}else{
			newwhIdx=0;
		}
		bmpNew=LibCui.ScaleBitmap(bmp, bmpWidth_new[newwhIdx], bmpHeight_new[newwhIdx]);
		
	    Bitmap	bmpCanvas=Bitmap.createBitmap(v.getWidth(),v.getHeight(),bmp.getConfig());
		
	   
	    Canvas cv=new Canvas(bmpCanvas);	    
	    cv.drawBitmap(bmpNew,0,0,null);	
	   
	    /*-------------------------------------------------*/
	    RecycleBitMap(bmpNew);
	    RecycleBitMap(bmp);
	    return bmpCanvas;
}
public static Bitmap  ScaleBitmap(Bitmap bmp,int width_new,int height_new)
{		 
	 Matrix matrix = new Matrix();
	 matrix.postScale((float)(1.0*width_new/bmp.getWidth()),(float)(1.0*height_new/bmp.getHeight()));
	 Bitmap newbmp = Bitmap.createBitmap(bmp, 0, 0,bmp.getWidth(),bmp.getHeight(), matrix, true);
	 int w=newbmp.getWidth();
	 int h=newbmp.getHeight();
	 return newbmp;
}

public void GetSDcard(Context context)
{
	//顺带描述怎么取得sdcard的空间大小，
	File sdcardDir = Environment.getExternalStorageDirectory();
	StatFs sf = new StatFs(sdcardDir.getPath()); //sdcardDir.getPath())值为/mnt/sdcard，想取外置sd卡大小的话，直接代入/mnt/sdcard2
	long blockSize = sf.getBlockSize(); //总大小
	long blockCount = sf.getBlockCount();
	long availCount = sf.getAvailableBlocks(); //有效大小
}
public static File[]  GetSDcards(){
	   File currentDir=Environment.getExternalStorageDirectory();
	   File parent=currentDir.getParentFile();
	   return  parent.listFiles();
}
public static long GetFileAvailable(File file)
{

	StatFs sf = new StatFs(file.getPath()); //sdcardDir.getPath())值为/mnt/sdcard，想取外置sd卡大小的话，直接代入/mnt/sdcard2
	long blockSize = sf.getBlockSize(); //总大小
	long blockCount = sf.getBlockCount();
	long availCount = sf.getAvailableBlocks(); //有效大小
	return availCount;
}
/**
 * 
 * 
 */
public static File GetMyQQDirectory()
{
	File f=null;
	File [] sdcards=LibCui.GetSDcards();
	  for (int i=0;i<sdcards.length;i++){  
		  if(sdcards[i].isDirectory()){
			 File[] f_list=sdcards[i].listFiles();
			 if(f_list==null) continue;
			  for(File l : f_list){
				  if(l!=null){
					  String s=l.getAbsolutePath();
								  if(s.contains("tencent")){
									  //存在名为QQ的目录
									  				for(File ll : l.listFiles()){
														  if(ll!=null){
															  String ss=ll.getAbsolutePath();
															  if(ss.contains("recv")){
																  //存在名为QQ的目录
																  
																  return ll;
															  }
														  }
													  }
									  return l;
								     }
				                 }
				  
			   }
		   }
		  }
	  
	return f;	
}
/**
 * 
 */
public static File GetMyPhotoDirectory()
{
	File f=null;
	File [] sdcards=LibCui.GetSDcards();
	  for (int i=0;i<sdcards.length;i++){  
		  if(sdcards[i].isDirectory()){
			 File[] f_list=sdcards[i].listFiles();
			 if(f_list==null) continue;
			  for(File l : f_list){
				  if(l!=null){
					  String s=l.getAbsolutePath();
					  if(s.contains("DCIM")){
						  return l;
					  }
				  }
				  
			  }
		   }
		  }
	  
	return f;	
}
/**
 * 
 */
public static File GetMyPhoneDirectory()
{
		String FirstFolder="JitClouldPrint";
		String ALBUM_PATH;
		File [] sdcards=LibCui.GetSDcards();
		  /*-------SD卡中剩余空间最大的一个------------------------*/
		  long a_min=0;
		  int   Idx=0;
		  for (int i=0;i<sdcards.length;i++){  
		      if(sdcards[i].isDirectory()){  
		         Long a=LibCui.GetFileAvailable(sdcards[i]); 
		          if(a>a_min){
		        	  a_min=a;
		        	  Idx=i;
		          }
		      }
		  }  
		  /*-------------------------------*/
    	 ALBUM_PATH= sdcards[Idx].getAbsolutePath()+File.separator+FirstFolder+File.separator;

         File dirFirstFile=new File(ALBUM_PATH);//新建一级主目录

         if(!dirFirstFile.exists()){//判断文件夹目录是否存在

              dirFirstFile.mkdirs();//如果不存在则创建

         }
        return dirFirstFile;
     }
public static  InetAddress getWifiIpAddress(Context mContext)
{
	   InetAddress Ia=null;
	   int ip=0;
	   WifiManager wifiManager = (WifiManager) (mContext).getSystemService(Context.WIFI_SERVICE);
	   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	 
	    if(isWifiEnabled(mContext)){
		    	 ip=wifiInfo.getIpAddress();
		    	 String ip_str=long2ip(ip);  
					    	 try {
								Ia=InetAddress.getByName(ip_str);
							 } catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							 }
	     }
	   
	   return Ia;
}
public static String getWifiName(Context mContext)
{
	   String wifi_info=null;
	   WifiManager wifiManager = (WifiManager) (mContext).getSystemService(Context.WIFI_SERVICE);
	   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	 
	    if(isWifiEnabled(mContext)){
	    	wifi_info=wifiInfo.getSSID();
	    }else{
	    	wifi_info="WiFi 未连接";
	    }
	   
	   return wifi_info;
}
public static String getWifiLocalMacAddress(Context mContext) 
{     
	//本机MAC地址
	 String wifi_info=null;
	   WifiManager wifiManager = (WifiManager) (mContext).getSystemService(Context.WIFI_SERVICE);
	   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	 
	    if(isWifiEnabled(mContext)){
	    	wifi_info=wifiInfo.getMacAddress();
	    }else{
	    	wifi_info="WiFi 未连接";
	    }
	   
	   return wifi_info;  
}     
public static String getWifiMac(Context mContext)
{
		if(mContext==null){
			return null;
		}
	//路由器MAC
	  String wifi_info=null;
	   WifiManager wifiManager = (WifiManager) (mContext).getSystemService(Context.WIFI_SERVICE);
	   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	 
	    if(isWifiEnabled(mContext)){
	    	wifi_info=wifiInfo.getBSSID();
	    }else{
	    	wifi_info="WiFi 未连接";
	    }
	   
	   return wifi_info;
}
public static boolean isWifiEnabled(Context mContext)
{
    Context myContext = mContext;
    if (myContext == null) {
        throw new NullPointerException("Global context is null");
    }
    WifiManager wifiMgr = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
    if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
        ConnectivityManager connManager = (ConnectivityManager) myContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiInfo.isConnected();
    } else {
        return false;
    }
}

public static <T> T[] concatAll(T[] first, T[]... rest) {
	  int totalLength = first.length;
	  for (T[] array : rest) {
	    totalLength += array.length;
	  }
	  T[] result = Arrays.copyOf(first, totalLength);
	  int offset = first.length;
	  for (T[] array : rest) {
	    System.arraycopy(array, 0, result, offset, array.length);
	    offset += array.length;
	  }
	  return result;
	}
/**
 * 获得本机IP地址
 * 
 * @return
 */
public String getLocalIpAddress() {
	try {
		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				     InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
					    return inetAddress.getHostAddress().toString();
				    }
			    }
	   }
	} catch (SocketException ex) {
	  Log.e("here", ex.toString());
	}
	return null;
}
/**
 * 获得本机IP地址
 * 
 * @return
 */
public static ArrayList<InetAddress> getLocalIpAddressList()
{
	 ArrayList<InetAddress> addressList=new ArrayList<InetAddress>();
	try {
		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				     InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						addressList.add(inetAddress);
				    }
			    }
	   }
	} catch (SocketException ex) {
	  Log.e("here", ex.toString());
	}
	return addressList;
}
/**
 * 
 * @return
 */
public static ArrayList<InetAddress> getSiteLocalAddressList()
{
	 ArrayList<InetAddress> addressList=new ArrayList<InetAddress>();
	try {
		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				     InetAddress inetAddress = enumIpAddr.nextElement();
					if (!(inetAddress.isLoopbackAddress()) 
							&& (inetAddress instanceof Inet4Address)
							&&(inetAddress.isSiteLocalAddress())) {
						addressList.add(inetAddress);
				    }
			    }
	   }
	} catch (SocketException ex) {
	  Log.e("here", ex.toString());
	}
	return addressList;
}
public static String calcMaskByPrefixLength(int length) {  
    int mask = -1 << (32 - length);  
    int partsNum = 4;  
    int bitsOfPart = 8;  
    int maskParts[] = new int[partsNum];  
    int selector = 0x000000ff;  

    for (int i = 0; i < maskParts.length; i++) {  
        int pos = maskParts.length - 1 - i;  
        maskParts[pos] = (mask >> (i * bitsOfPart)) & selector;  
    }  

    String result = "";  
    result = result + maskParts[0];  
    for (int i = 1; i < maskParts.length; i++) {  
        result = result + "." + maskParts[i];  
    }  
    return result;  
}  

public static String calcSubnetAddress(String ip, String mask) {  
    String result = "";  
    try {  
        // calc sub-net IP  
        InetAddress ipAddress = InetAddress.getByName(ip);  
        InetAddress maskAddress = InetAddress.getByName(mask);  

        byte[] ipRaw = ipAddress.getAddress();  
        byte[] maskRaw = maskAddress.getAddress();  

        int unsignedByteFilter = 0x000000ff;  
        int[] resultRaw = new int[ipRaw.length];  
        for (int i = 0; i < resultRaw.length; i++) {  
            resultRaw[i] = (ipRaw[i] & maskRaw[i] & unsignedByteFilter);  
        }  

        // make result string  
        result = result + resultRaw[0];  
        for (int i = 1; i < resultRaw.length; i++) {  
            result = result + "." + resultRaw[i];  
        }  
    } catch (UnknownHostException e) {  
        e.printStackTrace();  
    }  

    return result;  
}  
public static void getSiteLocalAddressList2()
{
    try {  
        Enumeration<NetworkInterface> eni = NetworkInterface  
                .getNetworkInterfaces();  
        while (eni.hasMoreElements()) {  

            NetworkInterface networkCard = eni.nextElement();  
            List<InterfaceAddress> ncAddrList = networkCard  
                    .getInterfaceAddresses();  
            Iterator<InterfaceAddress> ncAddrIterator = ncAddrList.iterator();  
            while (ncAddrIterator.hasNext()) {  
                InterfaceAddress networkCardAddress = ncAddrIterator.next();  
                InetAddress address = networkCardAddress.getAddress();  
                if (!address.isLoopbackAddress()) {  
                    String hostAddress = address.getHostAddress();  
                    System.out.println("address        =   " + hostAddress);  

                    if (hostAddress.indexOf(":") > 0) {  
                        // case : ipv6  
                        continue;  
                    } else {  
                        // case : ipv4  
                        String maskAddress = calcMaskByPrefixLength(networkCardAddress.getNetworkPrefixLength());  
                       
                    }  
                } else {  
                    String loopback = networkCardAddress.getAddress().getHostAddress();  
                    System.out.println("loopback addr  =   " + loopback +"\n");  
                }  
            }  
            System.out.println("----- NetworkInterface  Separator ----\n\n");  

        }  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
}
/**
 * 
 * @return
 * @throws UnknownHostException 
 * @throws SocketException 
 */
public static short getSubNetMaskAddress() throws UnknownHostException, SocketException
{
	short subnetMask=0;
	InetAddress localHost = InetAddress.getLocalHost();
	NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
	subnetMask=networkInterface.getInterfaceAddresses().get(1).getNetworkPrefixLength();
	for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {	
		address.getNetworkPrefixLength();
		}
	return subnetMask;
}
/*--------------------------------------------------*/
private static String intToIP(int ipAddress) {
    String ret = String.format("%d.%d.%d.%d", (ipAddress & 0xff),
            (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
            (ipAddress >> 24 & 0xff));

    return ret;
}
/**
 * 
 * @return
 */
public static String GetTimeStr()
{
	SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日");       
	Date curDate=new Date(System.currentTimeMillis());//获取当前时间       
	String str=formatter.format(curDate);  	
	return str;
}
/**
 *
 */
public static File GetCloudPrintCfgFile()
{
	File path=new File(Environment.getExternalStorageDirectory() + "/Documents");
	if(path.exists()){
		
	}else{
		path.mkdirs();
	}
	return path;
}
/**
*
*/
public static File GetCloudPrintPCLFile()
{
	
	File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	if(path==null){
		path=Environment.getExternalStorageDirectory();
	}
	if(path==null){
		path=Environment.getDownloadCacheDirectory();
	}
	return path;
}
/**
*
*/
public static boolean deleteFile(String sPath) {  
	boolean flag = false;  
	File file = new File(sPath);
    // 路径为文件且不为空则进行删除  
    if (file.isFile() && file.exists()) {  
        file.delete();  
        flag = true;  
    }  
    return flag;  
}  
/// <summary>
/// 获得广播地址
/// </summary>
/// <param name="ipAddress">IP地址</param>
/// <param name="subnetMask">子网掩码</param>
/// <returns>广播地址</returns>
public static String GetBroadcast(String ipAddress, String subnetMask) throws UnknownHostException
{

	
    byte[] ip = InetAddress.getByName(ipAddress).getAddress();
    byte[] sub = InetAddress.getByName(subnetMask).getAddress();

    // 广播地址=子网按位求反 再 或IP地址
    for (int i = 0; i < ip.length; i++)
    {
        ip[i] = (byte)((~sub[i]) | ip[i]);
    }
 
    return binaryArray2Ipv4Address(ip);
} 
/**将给定的字节数组转换成IPV4的十进制分段表示格式的ip地址字符串*/
public static String binaryArray2Ipv4Address(byte[]addr){
					String ip="";
					for(int i=0;i<addr.length;i++){
						if(i<3){
							 ip+=(addr[i]&0xFF)+".";
						}else{
							 ip+=(addr[i]&0xFF);
						}
					    
					}
					return ip;
	}
public static InetAddress GetBroadcast_InetAddress(String ipAddress, String subnetMask) throws UnknownHostException
{

	
    byte[] ip = InetAddress.getByName(ipAddress).getAddress();
    byte[] sub = InetAddress.getByName(subnetMask).getAddress();

    // 广播地址=子网按位求反 再 或IP地址
    for (int i = 0; i < ip.length; i++)
    {
        ip[i] = (byte)((~sub[i]) | ip[i]);
    }
    InetAddress addr=InetAddress.getByAddress(ip);
   
    return addr;
} 
/* *
 * 
 */

/* *
 * 
 */
/** 
 * 整型解析为IP地址 
 * @param num 
 * @return 
 */  
 
public static String long2ip(int ip){  
    StringBuffer sb=new StringBuffer();  
    sb.append(String.valueOf(ip&0xff));  
    sb.append('.');  
    sb.append(String.valueOf((ip>>8)&0xff));  
    sb.append('.');  
    sb.append(String.valueOf((ip>>16)&0xff));  
    sb.append('.');  
    sb.append(String.valueOf((ip>>24)&0xff));  
    return sb.toString();  
}  
//获取IP前缀
public static String getLocAddrIndex( String str ){
   
  if(!str.equals("")){
    return str.substring(0,str.lastIndexOf(".")+1);
  }
   
  return null;
}
public static ArrayList<String> GetAllDevInNet(int ip_addr,int net_mask) 
{
	
	
	 ArrayList<String>  IPDev=new ArrayList<String>();
	 //int net_start=ip_addr|net_mask;//网络号
	 //String ip_mask_str=long2ip(net_mask);
	 //int net_num=0-(int)(~(net_mask));
	 String ip_addr_str=long2ip(ip_addr);
	
	 
	 if(ip_addr_str.contains("192")&&false){
		 //使用所有地址
		 String addr=getLocAddrIndex(ip_addr_str);
		 for(int i=1;i<=254;i++){
			 String IpAddr=addr+String.valueOf(i);
			 IPDev.add(IpAddr);
		 }
	 }else{
		 //使用IP地址
		 String IpAddr = null;
		 try {
			 //GetBroadcast_InetAddress
			IpAddr = GetBroadcast(long2ip(ip_addr),long2ip(net_mask));
		 } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 if(IpAddr!=null)
		 IPDev.add(IpAddr);
	 }
	 
	
	return IPDev;
	
}

public static ArrayList<String> GetAllWifiIPList(Context mContext)
{
	int getewayIpL=0;
	int netmaskIpL=0;
	int netIPaddr=0;
    String broadcast;
	Context myContext = mContext;
    if (myContext == null) {
        throw new NullPointerException("Global context is null");
    }
    WifiManager wifiMgr = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
    if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
        ConnectivityManager connManager = (ConnectivityManager) myContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        
        if(wifiInfo.isConnected()){
        	netIPaddr=wifiMgr.getConnectionInfo().getIpAddress();
        	DhcpInfo di = wifiMgr.getDhcpInfo();  
        	getewayIpL=di.gateway;  
        	String getwayIpS=long2ip(getewayIpL);//网关地址  
        	netmaskIpL=di.netmask;  
        	String netmaskIpS=long2ip(netmaskIpL);//子网掩码地址  
        	
        }
    } 
    
    return GetAllDevInNet(netIPaddr,netmaskIpL);
}
/* *
 * 
 */
/**
 * 回收ImageView占用的图像内存;
 * @param view
 */
public static void recycleImageView(View view){
	if(view==null) return;
	if(view instanceof ImageView){
		Drawable drawable=((ImageView) view).getDrawable();
		if(drawable instanceof BitmapDrawable){
			Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
			if (bmp != null && !bmp.isRecycled()){
				((ImageView) view).setImageBitmap(null);
				bmp.recycle();
				bmp=null;
			}
		}
	}
}
/**/
public static void recycleViewImages(View view) {
    
    if (view == null) {
     return ;
    }  
    Drawable bgDrawable = view.getBackground();
		   if (bgDrawable != null && bgDrawable instanceof BitmapDrawable) {
		              BitmapDrawable bd = (BitmapDrawable) bgDrawable;
		              view.setBackground(null);
					  if (bd != null) {
					   if (bd.getBitmap() != null) {
					    bd.getBitmap().recycle();
					   }
					  }
		    }
   }
/*-------------------------------------------------------*/
public static void SaveString2Fille(String fileName,String str)
{
	if(str==null||fileName==null)
	{
		return;
	}
	 File path=LibCui.GetCloudPrintCfgFile();
	 File file=new File(path,fileName);
	try {  
	          FileOutputStream fos = new FileOutputStream(file);  
	          String info =str;  
	          fos.write(info.getBytes()); 
	          fos.flush();
	          fos.close();  
	} catch (Exception e) {  
	    e.printStackTrace();  
	}  
}
/*-------------------------------------------------------*/
public static byte[]  ConvertString2Byte(String filename)
{
	byte[] buffer=null;
	try {
		   buffer=filename.getBytes("UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return buffer;
	
}
public static void ConvertString2buffer(String filename,byte[] buffer)
{
	if(buffer!=null&&filename!=null){
		byte[] buffer_t= ConvertString2Byte(filename);
		if(buffer_t!=null){
			System.arraycopy(buffer_t, 0,buffer, 0,Math.min(buffer_t.length,buffer.length));
		}
	}
}
/*-------------------------------------------------------*/
public static String GetPhoneNumber(Context ctx)
{
	//创建电话管理
  TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
  //与手机建立连接
  //获取手机号码
  
   String phoneId = tm.getLine1Number();
   
   if(phoneId==null||"".equals(phoneId)){
	   return GetDeviceID(ctx);
   }else{
	   return phoneId;
	 
   }
}
public static String GetDeviceID(Context ctx)
{
	final TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

    final String tmDevice, tmSerial, tmPhone, androidId;
    tmDevice = "" + tm.getDeviceId();
    tmSerial = "" + tm.getSimSerialNumber();
    androidId = "" + android.provider.Settings.Secure.getString(ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
    return deviceUuid.toString();    
}
/*-------------------------------------------------------*/
public static void PlayMusic(Context ctx,int id)
{
	// SoundPool soundPool =new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

	// AudioAttributes aab= new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_SYSTEM).build();

    //soundPool.load(ctx, id, priority);

	 //int soundId =soundPool.load(ctx.getApplicationContext(),id,1);

	//soundPool.play(soundId,1, 1, 0, 0, 1);
	new Thread(new PlaySound(ctx,id)).start();
}
public static class PlaySound implements Runnable
{
	 Context ctx;
	 int id;
	 PlaySound(Context ctx,int id)
	 {
		 this.ctx=ctx;
		 this.id=id;
	 }
	@Override
	public void run() {
		
		// TODO Auto-generated method stub
		 SoundPool soundPool =new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		 final int soundId =soundPool.load(ctx.getApplicationContext(),id,1);
		 soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
			
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				// TODO Auto-generated method stub
				soundPool.play(soundId,1, 1, 0, 0, 1);
			}
		});
		 try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 soundPool.release();
		 soundPool=null;
		 
	}
	
}
/**
 * 
 */
public static boolean  IsStringFile(String path)
{
	File f = new File(path); 
	return f.isFile();
}
/**
 * File f = new File("D:\text.txt"); // 输入要删除的文件位置
if(f.exists())
f.delete(); 
 */
public static boolean  DeleteFile(String path)
{
	File f = new File(path); // 输入要删除的文件位置
	if(f.exists()){
			f.delete(); 
	}else{
		
	}
   return true;
}
/**
 * 
 * 
 */
public static File GetCloudDiskForder()
{
      File path=new File(GetCloudPrintCfgFile()+"/DiskForder");
      if(path.exists()){
    	  
      }else{
    	  path.mkdirs();
      }
      return path;
}

}