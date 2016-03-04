package com.jit.cloud_print_cc;




import com.jit.cloud_print_cc.KEY.WizardViewMode;

import android.os.Bundle;
/**
 *手机U盘。。 
 * 
 */
public class Activity_File_FileSelect_U extends Activity_FileSelect
{
	
	  @Override
	protected void onCreate(Bundle savedInstanceState)
	  {     
	        super.onCreate(savedInstanceState);
	        View_FileSelect mView=(View_FileSelect) this.mViewTemplate;
	        mView.SetWizardViewMode(WizardViewMode.ModeNormal);
	        mView.SetTitle(this.getString(R.string.dm_file_my_phone));
	        mView.BingingGridView_ForderSelect();
	        mView.SupportDialog();
	        
	  }
}
