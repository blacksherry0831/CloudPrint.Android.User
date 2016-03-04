package com.jit.cloud_print_cc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登录界面
 * 
 * 
 * 
 */
public class Activity_Login extends  Activity_CloudPrintTemplate
{
	View_Login mView;/**<主视图*/
	private EditText UserName;/**<用户名*/
	private EditText PWD;/**<密码*/
	private Button btn_Login;/**<登录按钮*/
	UserLoginOperation _userLoginOperation;
	 
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
         this.mViewTemplate=this.mView=new View_Login(this);
         setContentView(this.mViewTemplate.GetView());  
         this.InitParam();
        
       
    }  
	
	@Override  
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	  {
		 if(resultCode==View_CloudPrintTemplate.ResultCode_OrderIsSuccess) {
	       //下单成功    
	      }else if(resultCode==View_RegisterUser.RegisterResultSuccess){
	    	  //注册成功
	    	  mView.SetUserNamePasswordLocal2View();
	      }else{
	    	  
	      }
		 
	  } 
/**
 * 
 * 
 * 
 */
	public void StartNextView()
	{
		if(this.mViewTemplate!=null)
		this.mViewTemplate.StartActivityView(Activity_CloudMain.class);
	}
/**
 * 
 * 
 * 
 */
   public void InitParam()
   {  
	   this._userLoginOperation=new UserLoginOperation(this);
	   this._userLoginOperation.SetCallBack(new LoginCallBackFunction());
	   this.UserName=(EditText)this.findViewById(R.id.et_zh);
       this.PWD=(EditText)this.findViewById(R.id.et_mima);
       this.btn_Login=(Button)this.findViewById(R.id.btn_login);     
       btn_Login.setOnClickListener(new Button.OnClickListener(){//创建监听    
           @Override
		public void onClick(View v) {    
          	 if(UserName.getText().length()>0)
          	 {
          		if(_userLoginOperation!=null){
          			_userLoginOperation.SetUserName(UserName.getText().toString());
          			_userLoginOperation.SetPassword(PWD.getText().toString());
          			_userLoginOperation.Login();
          		}
          	 }
          	 else
          	 {
          		 Toast toast = Toast.makeText(Activity_Login.this, "请输入用户名与密码!", Toast.LENGTH_SHORT); 
          		 toast.show(); 
          		  
          		 
          	 }
              /* String strTmp = "点击Button01";    
                 UserName.setText(strTmp);*/
           }    
 
       });
    
 /**
 * 
 * 登录事件返回接口
 * 
 */
   /*class WhenLogin implements  UserLoginOperation.LoginCallBack
	{

		@Override
		public void LoginEvent(String event) {
			// TODO Auto-generated method stub
			
		}
		
	} 
   }*/

   
 
}  
   class LoginCallBackFunction implements UserLoginOperation.LoginCallBack
   {

		@Override
		public void LoginEvent(String event) {
			// TODO Auto-generated method stub
			/*LoginActivity.this.sh
			Toast.makeText(LoginActivity.this, event,Toast.LENGTH_SHORT).show();*/
			
			if(event.equalsIgnoreCase("success")){
/*				View_Login.SetQinPassword(Activity_Login.this, _userLoginOperation.mPassword);
				View_Login.SetQinUserName(Activity_Login.this,_userLoginOperation.mUserName);*/
				SaveParam.SetQinUserNamePassword(Activity_Login.this,_userLoginOperation.mUserName,_userLoginOperation.mPassword);
				Activity_Login.this.finish();
				StartNextView();
			}else{
				Activity_Login.this.Toast_make_show(event);
				Activity_Login.this.finish();
				StartNextView();
				SaveParam.SetQinUserNamePassword(Activity_Login.this,_userLoginOperation.mUserName,_userLoginOperation.mPassword);
			}
		}
	   
   }
}
		
