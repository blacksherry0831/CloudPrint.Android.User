package com.jit.cloud_print_cc;


import android.os.Bundle;

/**
 *用户注册 
 * 
 * 
 */
public class Activity_RegisterUser extends Activity_CloudPrintTemplate {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView((this.mViewTemplate=new View_RegisterUser(this)).GetView());
		
	}
}
