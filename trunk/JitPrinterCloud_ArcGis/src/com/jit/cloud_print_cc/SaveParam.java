package com.jit.cloud_print_cc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.jit.cloud_print_cc.KEY.FileOperationWay;
import com.jit.cloud_print_cc.KEY.OrderGeneratedMode;
import com.jit.cloud_print_cc.KEY.OrderListStartMode;
import com.jit.cloud_print_cc.KEY.WizardViewMode;
import com.jit.cloud_print_cc.View_CloudPrintTemplate.FileOperaData;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveParam {
	/**
	 *获取用户名
	 */
	public  static String GetQinUserName(Context ctx)
	{
		SharedPreferences sp;
		sp=ctx.getSharedPreferences(GetSharedFileUserData(), Context.MODE_PRIVATE);
		return sp.getString(KEY.K_USERNAME, "");
	}
	/**
	 *获取用户密码
	 */
	public  static String GetQinUserPassword(Context ctx)
	{
		SharedPreferences sp;
		sp=ctx.getSharedPreferences(GetSharedFileUserData(), Context.MODE_PRIVATE);
		return sp.getString(KEY.K_PASSWORD, "");
	}
	
	public final static SharedPreferences GetFileSOperationSetting(Context ctx)
	{
		  SharedPreferences shared_sp=ctx.getSharedPreferences("", Context.MODE_PRIVATE);
		  return  shared_sp;
	}
	/**
	 *获取设置用户名
	 */
	public static void SetQinUserName(Context ctx,String username)
	{
		//ctx.getSharedPreferences(name, mode)
		SharedPreferences sp;
		sp=ctx.getSharedPreferences(GetSharedFileUserData(), Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(KEY.K_USERNAME, username);
		editor.commit();
	}
	public static void SetQinUserNamePassword(Context ctx,String username,String password)
	{
		SetQinUserName(ctx,username);
		SetQinPassword(ctx,password);
	}
	/**
	 *设置用户密码
	 */
	public static void SetQinPassword(Context ctx,String password)
	{
		SharedPreferences sp;
		sp=ctx.getSharedPreferences(GetSharedFileUserData(), Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(KEY.K_PASSWORD,password);
		editor.commit();
	}
	/**
	 * 保存用户名密码的文件系统
	 */
	public static String GetSharedFileUserData(){
		
		return KEY.K_SharedPreferences;
	}
	/**
	 * 向导模式
	 * 
	 **/
		public static final boolean SetWizardViewMode(Context ctx,WizardViewMode mode)
		{
			
			 SharedPreferences shared_sp=GetFileSOperationSetting(ctx);
			 Editor editor =shared_sp.edit();
			 editor.putString(KEY.K_WizardViewMode, mode.toString());
			 return  editor.commit();
			
		}
	/**
	 * 向导模式
	 * 
	 **/
		protected static final WizardViewMode GetWizardViewMode(Context ctx)
		{
			WizardViewMode mode = null;
			SharedPreferences shared_sp=GetFileSOperationSetting(ctx);
			if(shared_sp.contains(KEY.K_WizardViewMode)){
				 String Mode_str=shared_sp.getString(KEY.K_WizardViewMode, null);
				 if(Mode_str.equals(WizardViewMode.ModeNormal.toString())){
					 mode=WizardViewMode.ModeNormal;
				 }else if(Mode_str.equals(WizardViewMode.ModeWizard.toString())){
					 mode=WizardViewMode.ModeWizard;
				 }else{
					 mode=null;
				 }
			 }else{
				mode=null;
			 }
			
			return mode;
		}
		
/**
 * 
 * 
 * 
 */
		public static  void SetFileOpMode(Context ctx,ArrayList<String> file,FileOperationWay Mode)
		{
			 Set<String> fileset=new HashSet<String>();
			if(file!=null){
				for(int i=0;i<file.size();i++){
					 fileset.add(file.get(i));
				 }
				
			}else{
				fileset.clear();
			}		 
			 SharedPreferences shared_sp=SaveParam.GetFileSOperationSetting(ctx);
			 Editor editor =shared_sp.edit();
			 
			 editor.putStringSet(KEY.K_Files, fileset);
			 editor.putString(KEY.K_FileOpenMode, Mode.toString());
			 
			 editor.commit();
		}
/**
 * 
 * 
 * 
 */
		public static FileOperaData GetFileOpMode(Context ctx)
		{
			FileOperaData data=new FileOperaData();
			
			 SharedPreferences shared_sp=SaveParam.GetFileSOperationSetting(ctx);
			 if(shared_sp.contains(KEY.K_Files)){			 
				 data.SetFiles(shared_sp.getStringSet(KEY.K_Files,null));			
			 }else{
				 data.SetFiles(null);
			 }
			 if(shared_sp.contains(KEY.K_FileOpenMode)){
				 String Mode_str=shared_sp.getString(KEY.K_FileOpenMode, null);
				 if(Mode_str.equals(FileOperationWay.OPen2SetDefaultPrint.toString())){
					 data.mMode=FileOperationWay.OPen2SetDefaultPrint;
				 }else if(Mode_str.equals(FileOperationWay.OPen2SetPrint.toString())){
					 data.mMode=FileOperationWay.OPen2SetPrint;
				 }else if(Mode_str.equals(FileOperationWay.OPen2SetDefaultPrint.toString())){
					 data.mMode=FileOperationWay.OpenWithFileTransfer;
				 }else{
					 data.mMode=null;
				 }
			 }else{
				 data.mMode=null;
			 }
			return data;
		}
/**
 * 
 * 
 * 
 */
		public static void SetOrderGeneratedMode(Context ctx,OrderGeneratedMode mode)
		{
			SharedPreferences shared_sp=SaveParam.GetFileSOperationSetting(ctx);
			 Editor editor =shared_sp.edit();
			 if(mode!=null)
			 editor.putString(KEY.K_OrderGeneratedMode, mode.toString());
			 
			 editor.commit();
		}
		public static OrderGeneratedMode GetOrderGeneratedMode(Context ctx)
		{
			OrderGeneratedMode Mode=null;
			SharedPreferences shared_sp=SaveParam.GetFileSOperationSetting(ctx);
			if(shared_sp.contains(KEY.K_OrderGeneratedMode)){
				String mode_str=shared_sp.getString(KEY.K_OrderGeneratedMode,null);
				if(mode_str.equals(OrderGeneratedMode.SetDefaultOrderList.toString())){
					Mode=OrderGeneratedMode.SetDefaultOrderList;
				}else if(mode_str.equals(OrderGeneratedMode.SetNetOrderList.toString())){
					Mode=OrderGeneratedMode.SetNetOrderList;
				}else if(mode_str.equals(OrderGeneratedMode.SetLocalOrderList.toString())){
					Mode=OrderGeneratedMode.SetLocalOrderList;
				}else{
					
				}
			}
			return Mode;
		}
		/**
		 * 
		 * 
		 * 
		 */
		public static void SetOrderListStartdMode(Context ctx,OrderListStartMode mode)
		{
			SharedPreferences shared_sp=SaveParam.GetFileSOperationSetting(ctx);
			 Editor editor =shared_sp.edit();
			 if(mode!=null)
			 editor.putString(KEY.K_OrderListStartdMode, mode.toString());
			 
			 editor.commit();
		}
		/**
		 * 
		 * 
		 * 
		 */
		public static OrderListStartMode GetOrderListStartdMode(Context ctx)
		{
			OrderListStartMode Mode=null;
			SharedPreferences shared_sp=SaveParam.GetFileSOperationSetting(ctx);
			if(shared_sp.contains(KEY.K_OrderListStartdMode)){
				String mode_str=shared_sp.getString(KEY.K_OrderListStartdMode,null);
				if(mode_str.equals(OrderListStartMode.CloseParents.toString())){
					Mode=OrderListStartMode.CloseParents;
				}else if(mode_str.equals(OrderListStartMode.SingleStart.toString())){
					Mode=OrderListStartMode.SingleStart;
				}else{
					
				}
			}
			return Mode;
		}
		/**
		 * 
		 * 
		 * 
		 */
}
