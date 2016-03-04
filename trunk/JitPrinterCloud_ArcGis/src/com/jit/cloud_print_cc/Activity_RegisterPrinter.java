package com.jit.cloud_print_cc;

import android.os.Bundle;

public class Activity_RegisterPrinter extends Activity_CloudPrintTemplate { 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
		
		setContentView((this.mViewTemplate=new View_RegisterPrinter(this)).GetView());
	}
}
