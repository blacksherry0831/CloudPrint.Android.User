package com.jit.cloud_print_cc;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.jit.cloud_print_cc.R;
import com.jit.update.UpdateAndroid;
import com.ta.utdid2.android.utils.StringUtils;

public class View_Login extends View_CloudPrintTemplate
{
	private final static int mLayoutId=R.layout.activity_login;
	private EditText userName, password;
	private CheckBox rem_pw, auto_login;
	private Button btn_login;
	private TextView mRegisterTextView;
	private TextView mUpdateTextView;
	private ImageButton btnQuit;
    private String userNameValue,passwordValue;
	private SharedPreferences sp;
	public View_Login(Context context) {
		super(context, mLayoutId);
		
		this.InitChildView();
	}
	public void InitChildView()
	{
		sp=this.getContext().getSharedPreferences(SaveParam.GetSharedFileUserData(), Context.MODE_PRIVATE);
		
		userName = (EditText) findViewById(R.id.et_zh);  
        password = (EditText) findViewById(R.id.et_mima);  
        /*-直接记住用户名密码-*/
        this.SetUserNamePasswordLocal2View();
   	    
        rem_pw = (CheckBox) findViewById(R.id.cb_mima);  
        auto_login = (CheckBox) findViewById(R.id.cb_auto);  
        btn_login = (Button) findViewById(R.id.btn_login);  
        btnQuit = (ImageButton)findViewById(R.id.img_btn); 
        this.mRegisterTextView=(TextView) this.findViewById(R.id.TextView_resister);
        
        
        
        
        this.mRegisterTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View_Login.this.StartActivityView(Activity_RegisterUser.class);
			}
		});
        
      
    	//判断记住密码多选框的状态
        if(sp.getBoolean("REMEMBER_PASS_ISCHECK", false))
          {
      	  //设置默认是记录密码状态
            rem_pw.setChecked(true);
            this.SetUserNamePasswordLocal2View();
         	  //判断自动登陆多选框状态
         	  if(sp.getBoolean("AUTO_LOGIN_ISCHECK", false))
         	  {
         		     //设置默认是自动登录状态
         		     auto_login.setChecked(true);
         		    //跳转界面
  				//Intent intent = new Intent(LoginActivity.this,LogoActivity.class);
  				//LoginActivity.this.startActivity(intent);
  				
         	  }
          }
  		
  	    // 登录监听事件  现在默认为用户名为：liu 密码：123
  		/*btn_login.setOnClickListener(new View.OnClickListener() {

  			public void onClick(View v) {
  				userNameValue = userName.getText().toString();
  			    passwordValue = password.getText().toString();
  			    
  				//if(userNameValue.equals("liu")&&passwordValue.equals("123"))
  				if(true)
  				{
  					Toast.makeText(v.getContext(),"登录成功", Toast.LENGTH_SHORT).show();
  					//登录成功和记住密码框为选中状态才保存用户信息
  					if(rem_pw.isChecked())
  					{
  					 //记住用户名、密码、
  						SetQinUserName(getContext(),userNameValue);
  						SetQinPassword(getContext(),passwordValue);
  						Editor editor = sp.edit();
  					  editor.putString(_USERNAME, userNameValue);
  					  editor.putString(_PASSWORD,passwordValue);
  					  editor.commit();
  					}
  					//跳转界面
  					//Intent intent = new Intent(LoginActivity.this,LogoActivity.class);
  					//LoginActivity.this.startActivity(intent);
  					//finish();
  					
  				}else{
  					
  					Toast.makeText(v.getContext(),"用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
  				}
  				
  			}
  		});*/

        
        
  	    //监听记住密码多选框按钮事件
  		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
  			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
  				if (rem_pw.isChecked()) {
                      
  					System.out.println("记住密码已选中");
  					sp.edit().putBoolean("REMEMBER_PASS_ISCHECK", true).commit();
  					
  				}else {
  					
  					System.out.println("记住密码没有选中");
  					sp.edit().putBoolean("REMEMBER_PASS_ISCHECK", false).commit();
  					
  				}

  			}
  		});
  		
  		//监听自动登录多选框事件
  		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
              @Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
  				if (auto_login.isChecked()) {
  					System.out.println("自动登录已选中");
  					sp.edit().putBoolean("AUTO_LOGIN_ISCHECK", true).commit();

  				} else {
  					System.out.println("自动登录没有选中");
  					sp.edit().putBoolean("AUTO_LOGIN_ISCHECK", false).commit();
  				}
  			}
  		});
  		/*
  		btnQuit.setOnClickListener(new OnClickListener() {
  			
  			@Override
  			public void onClick(View v) {
  				finish();
  			}
  		});*/
  		
  		this.mUpdateTextView=(TextView) this.findViewById(R.id.TextView_Update);
  		this.mUpdateTextView.setVisibility(View.GONE);
  		String lastest_version=SaveParam.GetValue(getContext(),KEY.K_Version_Latest);
  		if(!StringUtils.isEmpty(lastest_version)&&!lastest_version.equalsIgnoreCase(UpdateAndroid.getVersionCode(getContext()))){
  			//有新版本
  			this.mUpdateTextView.setVisibility(View.VISIBLE);
  			this.mUpdateTextView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new UpdateAndroid().DownloadApk(getContext());
				}
			});
  		}
  		

	}
	public void SetUserNamePasswordLocal2View()
	{
		    userName.setText(SaveParam.GetQinUserName(getContext()));
	   	    password.setText(SaveParam.GetQinUserPassword(getContext()));
	   	    
	   	    
	   	    if(userName.getText().toString().equals("blacksherry")){
	   	    	StringBuilder sb=new StringBuilder();
	   	    	sb.append("本地版本"+UpdateAndroid.getVersionCode(getContext()));sb.append("\n");
	   	    	sb.append("网络版本"+SaveParam.GetValue(getContext(), KEY.K_Version_Latest));
	   	    	Toast_make_show(sb.toString(),Toast.LENGTH_LONG,Gravity.CENTER);	   	    	
	   	    }
	   	    
	}

}
