package com.jit.config;

public class KEY 
{
	/*------------Start View Selected--------------------------------*/
	public final static String F_KEY_TITLE="TITLE";
	public final static String F_KEY_TYPE="TYPE";
	public final static String F_KEY_ETX="FILEETX";
	public  final static String K_USERNAME="QinUserName";
	public  final static String K_PASSWORD="QinPassword";
	public  final static String K_SharedPreferences="UserData";
	/*---------------------------------------------------------------*/
	public final static String  K_OrderGeneratedMode="OrderGeneratedMode";
	public final static String  K_OrderListStartdMode="OrderListStartdMode";
	public final static String  K_WizardViewMode="WizardViewMode";
	public final static String  K_Files="Files";
	//public final static String  K_B_FilesSelected="FileSelected";
	public final static String  K_FileOpenMode="FileOpenMode";
	/*---------------------------------------------------------------*/
	public final static String  K_D_NETDISKFORDER="NetDiskList.txt";
	public final static String  K_D_CONFFORDER="ver.json";
	/*---------------------------------------------------------------*/
	public final static String K_Version_Latest="Version_Latest";
	public final static String K_Version_Latest_URL="Version_Latest_URL";
	/*---------------------------------------------------------------*/
	public final static String K_Location_Latitude="Location_Latitude";
	public final static String K_Location_Longitude="Location_Longitude";
	public final static String K_Location_Addr="Location_Addr";
	/*---------------------------------------------------------------*/
/**
 * 向导
 * 
 **/	
		public enum WizardViewMode
		{
			ModeNormal,
			ModeWizard
		}
/**
 * 
 * 
 **/
		public enum OrderGeneratedMode
		{
			SetDefaultOrderList,
			SetNetOrderList,
			SetLocalOrderList,
			NUll
		}
/**
 * 
 * 
 **/
		public enum OrderListStartMode
		{
			CloseParents,
			SingleStart,
		}
/**
 * 
 * 
 **/
		public enum FileOperationWay{
			OpenWithFileTransfer,
			OPen2SetDefaultPrint,
			OPen2SetPrint,
			NULL
		}
/**
 * 
 * 
 **/
}
