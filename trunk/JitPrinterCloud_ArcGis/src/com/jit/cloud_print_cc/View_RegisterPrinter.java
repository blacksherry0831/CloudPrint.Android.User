package com.jit.cloud_print_cc;


import com.baidu.location.BDLocation;
import com.jit.cloud_print_cc.R;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class View_RegisterPrinter extends View_CloudPrintTemplate{
	public interface TransferDone{
		void Done();
	}
	public static final int REFASH_PRINTER_DATA = 0x66ccff;
	private final static int mLayoutId=R.layout.activity_register_printer;
	public TextView mTextViewPrinterDes;
	public TextView mTextViewCoord;
	public TextView mTextViewAddress;
	public Button   mButtonRegister;
	public  UserInfoBase mUserinfo;
	//public TextView mTextViewPrinterDes;
	public View_RegisterPrinter (Context context)
	  {
		  super(context,mLayoutId);
		  InitLocationClient();
		  this.getPrinter();
		  this.initLocalView();
		  mUserinfo=new UserInfoBase(context);
	  }
	/*
	public RegisterPrinterView (Context context,PhoneInHand   PhoneInHand)
	  {
		 super(context,mLayoutId,PhoneInHand);
		 InitLocationClient();
		 this.getPrinter();
	  }*/
	public void initLocalView()
	{
		/*-----------------------------------------------------------------------*/
		mTextViewPrinterDes=(TextView) this.findViewById(R.id.textView_register_des);
		mTextViewCoord=(TextView) this.findViewById(R.id.textView_register_coord);
		mTextViewAddress=(TextView) this.findViewById(R.id.textView_register_location);
		mButtonRegister=(Button) this.findViewById(R.id.button_register_button);
		/*-----------------------------------------------------------------------*/
		if(mTextViewPrinterDes!=null&&this.mPrintPointTemp!=null){
			this.mTextViewPrinterDes.setText(this.mPrintPointTemp.GetPrinterDes());
		}
		if(mButtonRegister!=null){
			mButtonRegister.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					new Thread(new RegisterPrinterThread()).start();
				}
			});
		}
	} 
	@Override
	protected void  WhenOnReceiveLocation(BDLocation location)
	{
		
		this.mPrintPointTemp.SeBDtLocation(location);
				
		if(mTextViewCoord!=null){
			this.mTextViewCoord.setText("经度："+String.valueOf(location.getLatitude())+
										"，纬度："+String.valueOf(location.getLongitude()));
		}
		
		if(mTextViewAddress!=null){
			this.mTextViewAddress.setText(location.getAddrStr());
		}
		
		
	}
public static	class LocationBD
	{
		public String Province;
		public String City;
		public String District;
		public String Street;
		LocationBD(BDLocation location){
			Province=location.getProvince();
			City=location.getCity();
			District=location.getDistrict();
			Street=location.getStreet();
			;
		}
	}

public class RegisterPrinterThread implements Runnable
{

	NotifyThisView NTV=new NotifyThisView();
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(mPrintPointTemp!=null){
			Toast_make_show("正在注册打印机...",Toast.LENGTH_SHORT,Gravity.CENTER);
			if(mPrintPointTemp.RegisterPrinter(mUserinfo)){
				//注册成功
				NTV.Done();
			}
			Toast_make_show(mPrintPointTemp.GetErrorDes(),Toast.LENGTH_SHORT,Gravity.CENTER);
		}
	}

}

public class NotifyThisView implements TransferDone
{

	@Override
	public void Done() {
		// TODO Auto-generated method stub
		postDelay(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				View_RegisterPrinter.this.finish(REFASH_PRINTER_DATA);
			}});
	}
 
}


}
