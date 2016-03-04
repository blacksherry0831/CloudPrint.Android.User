package com.jit.cloud_print_cc;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.jit.cloud_print_cc.View_CloudPrintTemplate;
import com.jit.cloud_print_cc.UserInfoBase.Seox;


public class View_RegisterUser extends View_CloudPrintTemplate {
	private final static int mLayoutId=R.layout.activity_register;
	public  final static int RegisterResultSuccess=0xdc41a2a5;
    public EditText mEditTextUserName;
    public EditText mEditTextPassword;
    public EditText mEditTextPasswordConfirm;
    public EditText mEditTextUserTrueName;
    public EditText mEditTextUserPhoneName;
    public Button   mButtonSubimt;
    public RadioButton mRadioBoy;
    public RadioButton mRadioGirl;
    public RadioGroup mRadioGroup;
    private UserRegisterOperation mRegisterOperation; 
	public View_RegisterUser(Context context) {
		super(context, mLayoutId);
		mRegisterOperation=new UserRegisterOperation(context); 
		this.InitChildView();
	}
	public void InitChildView()
	{
		
		 mEditTextUserName=(EditText) findViewById(R.id.register_editText_user_name);
		 mEditTextPassword=(EditText) findViewById(R.id.register_editText_password);
		 mEditTextPasswordConfirm=(EditText) findViewById(R.id.register_editText_password_confirm);
		 mEditTextUserTrueName=(EditText) findViewById(R.id.register_editText_true_name);
		 mEditTextUserPhoneName=(EditText) findViewById(R.id.register_editText_phone_number);
		 mButtonSubimt=(Button) findViewById(R.id.register_button_submit);
		 mRadioGroup=(RadioGroup) findViewById(R.id.register_radioGroup_sox);
		 this.mRadioGroup.check(R.id.register_radio_girl);
		 mRegisterOperation.mSex=UserInfoBase.Seox.GIRL;		 
				 
		 this.SetClick();
	}
	public void SetClick(){
		/*更新用户名*/
		this.mEditTextUserName.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				View_RegisterUser.this.mRegisterOperation.mUserName=s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			
			}
			
			
		});
		/*更新密码*/
		this.mEditTextPassword.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				View_RegisterUser.this.mRegisterOperation.mPassword=s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		/*密码确认*/
		this.mEditTextPasswordConfirm.addTextChangedListener(new TextWatcher(){
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				View_RegisterUser.this.mRegisterOperation.mPasswordConfirm=s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
			
		});

		this.mEditTextUserTrueName.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				View_RegisterUser.this.mRegisterOperation.mTrueName=s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		/*手机密码*/
		this.mEditTextUserPhoneName.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				View_RegisterUser.this.mRegisterOperation.mPhoneNumber=s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		this.mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() { 

						@Override 
						public void onCheckedChanged(RadioGroup arg0, int arg1) { 
						// TODO Auto-generated method stub 
						//获取变更后的选中项的ID 
						int radioButtonId = arg0.getCheckedRadioButtonId(); 
							if(radioButtonId==R.id.register_radio_boy){
								View_RegisterUser.this.mRegisterOperation.mSex=Seox.BOY;
							}else if(radioButtonId==R.id.register_radio_girl){
								View_RegisterUser.this.mRegisterOperation.mSex=Seox.GIRL;
							}else{
								View_RegisterUser.this.mRegisterOperation.mSex=null;
							}
                         }});

		this.mButtonSubimt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(View_RegisterUser.this.mRegisterOperation.CheakPassword()==false){
					Toast.makeText(v.getContext(), "两次密码不同",Toast.LENGTH_SHORT).show();
					return;
				}
				
				new Thread(new SubmitThread()).start();
			}
		});
	}
	public class SubmitThread implements Runnable
	{
		String _Status;
		//public boolean mSubmitStatus=false;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			postDelay(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mButtonSubimt.setEnabled(false);
				}
				
			});
		//this.mSubmitStatus=RegisterUserView.this.mRegisterOperation.Submit();
		this._Status=View_RegisterUser.this.mRegisterOperation.Submit_v2();
		
		postDelay(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mButtonSubimt.setEnabled(true);
					Toast_make_show(_Status);
					
					if("success".equalsIgnoreCase(_Status)){
						//Toast_make_show(_Status);
						//注册成功
						//保存用户名密码
						SaveParam.SetQinUserNamePassword(getContext(),mRegisterOperation.mUserName,mRegisterOperation.mPassword);
					    finish(RegisterResultSuccess);
					   
					   
					}else{
						//注册失败
						
					}
					
					
					/*if(SubmitThread.this.mSubmitStatus){
						Toast.makeText(getContext(), "注册成功",Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getContext(), "注册失败"+RegisterUserView.this.mRegisterOperation.mErrorStrArray.toString(),Toast.LENGTH_SHORT).show();
					}*/
				}
				
			});
		}
		
	}
}
