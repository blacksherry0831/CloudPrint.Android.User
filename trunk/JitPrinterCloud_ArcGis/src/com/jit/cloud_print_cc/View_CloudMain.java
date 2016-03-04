package com.jit.cloud_print_cc;


import com.zxc.view.CircleMenuLayout;
import com.zxc.view.CircleMenuLayout.CircleMode;
import com.example.LeftRightHand.LeftRightGestureDetector;
import com.example.LeftRightHand.LeftRightGestureDetector.PhoneInHand;
import com.jit.cloud_print_cc.R;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;


import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class View_CloudMain extends View_CloudPrintTemplate implements LeftRightGestureDetector.OnHandChangeListener 
{
    private LeftRightGestureDetector mLeftRightDetector; 
	
	private LinearLayout     mTop;
	private LinearLayout     mCenter;
	private LinearLayout     mBottom;
	private LinearLayout     mCenterHTxt; 
	private TextView  		 mCenterHTxtTV1;
	private TextView 		 mCenterHTxtTV2;
    private FrameLayout      mCenterHCircle;
    private CircleMenuLayout mCenterHCircleMenuLayout;


	int mBgmWidth;
	int mBgmHeight;
	private double                  m_TopScale;
	private double                  m_BottomScale;
	private double                  m_CenterScale;
	private  final   double         m_CenterTextScaleL=1.0*165/720;
	private  final   double         m_CenterCircleScaleL=1-1.0*165/720;
	private double                  m_CircleTopMarginsScale;

	//private Bitmap bmpBackgroundLeft;
	//private Bitmap bmpBackgroundRight;
	/*private String[] mItemTexts = new String[] {"1", "2", "3", "4", "5","6" };
	private int[] mItemImgs = new int[] { 1,2, 3,4, 5,6};*/
	private String[] mItemTexts = new String[] {"1", "2", "3", "4", "5","6"};
	private int[] mItemImgs = new int[] { 1,2, 3,4, 5,6 };
	View_CloudMain(Context context)
	{		
			
		super(context);		
		this.InitChildLayout(context);
		//bmpBackgroundLeft=BitmapFactory.decodeResource(res, R.drawable.background_lefthand);
		//bmpBackgroundRight=BitmapFactory.decodeResource(res, R.drawable.background_righthand);
		
	}
	public void SetLeftRightHandDetector(LeftRightGestureDetector LeftRightDetector)
	{
		
		if(LeftRightDetector==null){
			if(mLeftRightDetector!=null){
				GetView().setOnTouchListener(null);
				mLeftRightDetector.setOnHandChangeListener(null);
				mLeftRightDetector=null;
			}
		}else{
			mLeftRightDetector=LeftRightDetector;
			GetView().setOnTouchListener(this.mLeftRightDetector);
			mLeftRightDetector.setOnHandChangeListener(this);
		}
		
	}
public	void InitLayoutParam(double TopScaleOnView,double BottomScaleOnView){
	if(mRootWidth+mRootHeight!=0){
		m_TopScale=TopScaleOnView*mBgmHeight/mRootHeight;
		m_BottomScale=(BottomScaleOnView*mRootHeight+mBgmHeight-mRootHeight)/mBgmHeight;	
	}else{
		m_TopScale=0.8/10;
		m_BottomScale=0.8/10;
	}
	m_CenterScale=1-m_TopScale-m_BottomScale;
	
	m_CircleTopMarginsScale=1.0*313/1278-m_TopScale;
	
}
public void InitCircleString(Context context)
   {
	   mItemTexts[0]=context.getResources().getString(R.string.my_order);
	   mItemImgs[0]=R.drawable.my_order;
	   mItemTexts[1]=context.getResources().getString(R.string.close);
	   mItemImgs[1]=R.drawable.close;
	   mItemTexts[2]=context.getResources().getString(R.string.office_environment);
	   mItemImgs[2]=R.drawable.office_environment;
	   mItemTexts[3]=context.getResources().getString(R.string.doc_manage);
	   mItemImgs[3]=R.drawable.document_manage;
	  
	   
	   mItemTexts[4]=context.getResources().getString(R.string.printer_wizard);
	   mItemImgs[4]=R.drawable.print_out;
	   
	   mItemTexts[5]=context.getResources().getString(R.string.search);
	   mItemImgs[5]=R.drawable.search;
	   /* mItemTexts[5]=context.getResources().getString(R.string.printer_wizard);
	   mItemImgs[5]=R.drawable.print_out;  */
		
	}
  
   @Override
public void InitMainView()
   {

	   InitMainLayout(getContext());
	  
	  
	}
public void InitMainLayout(Context context)
{
	
	 SetView(new  LinearLayout(context));
	((LinearLayout) GetView()).setOrientation(LinearLayout.VERTICAL);
	
	
}
public void InitMainLayoutParamLeft()
{
	
 try{
	 Bitmap bmpBackgroundLeft=LibCui.readBitMap(getContext(),R.drawable.background_lefthand);
	 
	 LibCui.SetViewBackGround(mTop, getContext(), R.drawable.background_top);
	 LibCui.SetViewBackGround(mBottom, getContext(), R.drawable.background_bottom);
	
	 
	 SetBackground2View(GetView(),bmpBackgroundLeft);		
	 //////////////////////////////////		
	 double header=Integer.parseInt(getContext().getString(R.integer.qin_header_scale));
	 double body=Integer.parseInt(getContext().getString(R.integer.qin_body_scale));
	 double footer=Integer.parseInt(getContext().getString(R.integer.qin_footer_scale));
	 double total=header+body+footer;
	 this.InitLayoutParam(header/total,footer/total);
	 /////////////////////////////////
	 //this.InitLayoutParam(0.8/10,1.0/10);
	
	 this.InitChildLayoutParams();
	 this.InitCenterLayoutParamLeft();
	 this.InitTextAreaParam();
	 this.InitCircleMenuAreaParam2();
	 this.InitCircleMenuLayoutParam();
	 this.mCenterHCircleMenuLayout.SetCricleModule(CircleMode.LeftCircle);
	 
 }catch(Exception e){
	 e.getMessage();
 }
 catch(OutOfMemoryError e){
	 e.getMessage();
 }
	
	 
}
public void InitMainLayoutParamRight()
{
	
	 try{
		 
		 Bitmap bmpBackgroundRight=LibCui.readBitMap(getContext(),R.drawable.background_righthand);
		 
		 LibCui.SetViewBackGround(mTop, getContext(), R.drawable.background_top);
		 LibCui.SetViewBackGround(mBottom, getContext(), R.drawable.background_bottom);
		 
		 SetBackground2View(GetView(),bmpBackgroundRight);	
		
		 
		 double header=Integer.parseInt(getContext().getString(R.integer.qin_header_scale));
		 double body=Integer.parseInt(getContext().getString(R.integer.qin_body_scale));
		 double footer=Integer.parseInt(getContext().getString(R.integer.qin_footer_scale));
		 double total=header+body+footer;
		 this.InitLayoutParam(header/total,footer/total);
		
		 this.InitChildLayoutParams();
		 this.InitCenterLayoutParamRight();
		 this.InitTextAreaParam();
		 this.InitCircleMenuAreaParam2();
		 this.InitCircleMenuLayoutParam();
		 this.mCenterHCircleMenuLayout.SetCricleModule(CircleMode.RightCircle); 
		 
	 }catch(Exception e){
		 e.getMessage();
	 }
	 catch(OutOfMemoryError e){
		 e.getMessage();
	 }	
	
	 
}
public void InitChildLayoutParams()
{
	
	/////////////////////////////////
	 int delta=mBgmHeight-this.mRootHeight;
	 LinearLayout.LayoutParams mTopLayoutParam=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int)(mBgmHeight*m_TopScale));
	 LinearLayout.LayoutParams mCenterLayoutParam=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int)(mBgmHeight*m_CenterScale));
	 LinearLayout.LayoutParams mBottomLayoutParam=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int)(mBgmHeight*m_BottomScale)-delta);
   // mTop.setLayoutParams(null);
	 mTop.setLayoutParams(mTopLayoutParam);
     mCenter.setLayoutParams(mCenterLayoutParam);
     mBottom.setLayoutParams(mBottomLayoutParam);
    
}
public void InitChildLayout(Context context)
{				 
		 this.InitTopLayout(context);
		 this.InitCenterLayout(context);	
		 this.InitBttomLayout(context);		
		 //mBottom.setBackgroundColor(Color.BLUE);
		 //////////////////////////////////
		 ((LinearLayout)GetView()).addView(mTop);
		 ((LinearLayout)GetView()).addView(mCenter);
		 ((LinearLayout)GetView()).addView(mBottom);
}
public void  InitCenterLayoutParamRight()
{
	  mCenter.removeAllViews();
	  mCenter.setOrientation(LinearLayout.HORIZONTAL);
	  mCenter.addView(mCenterHTxt);
	  mCenter.addView(mCenterHCircle);  
	  LinearLayout.LayoutParams mCenter01LayoutParams=new LinearLayout.LayoutParams((int)(mBgmWidth* m_CenterTextScaleL),LayoutParams.MATCH_PARENT);
      LinearLayout.LayoutParams mCenter02LayoutParams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
      mCenterHTxt.setLayoutParams(mCenter01LayoutParams); 
      mCenterHCircle.setLayoutParams(mCenter02LayoutParams); 
}
public void  InitCenterLayoutParamLeft()
{
	mCenter.removeAllViews();  
    mCenter.setOrientation(LinearLayout.HORIZONTAL);   
    mCenter.addView(mCenterHCircle);   
    mCenter.addView(mCenterHTxt);
    
    LinearLayout.LayoutParams mCenter01LayoutParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams mCenter02LayoutParams=new LinearLayout.LayoutParams((int)(mBgmWidth*m_CenterCircleScaleL),LayoutParams.MATCH_PARENT);
  
    mCenterHTxt.setLayoutParams(mCenter01LayoutParams); 
    mCenterHCircle.setLayoutParams(mCenter02LayoutParams); 
}
public void  InitCenterLayout(Context context)
{
	 mCenter=new  LinearLayout(context);
     mCenterHTxt=this.InitTextArea(context); 
     mCenterHCircle=this.InitCircleMenuLayout(context);
}
public void  InitBttomLayout(Context context)
{
	/*
	 mBottom=new  LinearLayout(context);
	 mCenter.setOrientation(LinearLayout.HORIZONTAL); 
	 View view_mywallet=GetImageText(R.drawable.mywallet,R.string.my_wallet,getContext());
	 View view_preferential=GetImageText(R.drawable.preferential,R.string.preferential,getContext());
	 View view_blank=GetImageText(-1,-1,getContext());
	 //LinearLayout.LayoutParams matchLayoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
	 //LinearLayout.LayoutParams wrapLayoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT); 
	 LinearLayout.LayoutParams LayoutParams=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
	 LayoutParams.weight=(float)0.3;
	
	 mBottom.addView(view_mywallet,LayoutParams);
	 mBottom.addView(view_blank,LayoutParams);
	 mBottom.addView(view_preferential,LayoutParams);*/
	
	 mBottom=new  LinearLayout(context);
	 mCenter.setOrientation(LinearLayout.HORIZONTAL); 
	 
	 View view_mywallet=GetImageText(R.drawable.mywallet,R.string.my_wallet,getContext());
	 
	 View view_preferential=GetImageText(R.drawable.preferential,R.string.preferential,getContext());
	 
	 View view_circle_button=View_CloudMain.GetCircleButton(R.drawable.login,R.string.login, getContext());
			
	 view_circle_button.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			View_CloudMain.this.StartActivityView(Activity_Login.class);
		}
	});
	 //LinearLayout.LayoutParams matchLayoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
	// LinearLayout.LayoutParams wrapLayoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT); 
	
	 LinearLayout.LayoutParams LayoutParams=new LinearLayout.LayoutParams(0,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
	 LayoutParams.weight=(float)0.3;
	 //LinearLayout.LayoutParams LayoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT); 
	
	 mBottom.addView(view_mywallet,LayoutParams);
	/* 
	 * 登陆按钮 
	 * mBottom.addView(view_circle_button,LayoutParams);*/
	 /*空白*/
	 mBottom.addView(new View(getContext()),LayoutParams);
	 mBottom.addView(view_preferential,LayoutParams);
	 /*-------------------------------------------------------------*/
      view_mywallet.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			StartActivityView(Activity_My.class);
		}
	});
    view_preferential.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			StartActivityView(Activity_WebView.class);
		}
	});
	 /*-------------------------------------------------------------*/
}
public void  InitTopLayout(Context context)
{
	 mTop=new  LinearLayout(context);	
	 mTop.setOrientation(LinearLayout.HORIZONTAL); 
	// View view_register=GetImageText(-1,R.string.login,getContext());
	
	 //LinearLayout.LayoutParams LayoutParams=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
	 //LayoutParams.weight=(float)0.3;
	 LinearLayout.LayoutParams LayoutParams=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
	 View v=LibCui.getViewFromeXml(R.layout.cloud_print_main_title,this.getContext());
	 mTop.addView(v,LayoutParams);
	
	 
}
public  static View GetCircleButton(int ImgIdx, int StrIdx,Context context)
{
	 int MenuItemLayoutId = R.layout.circle_button;
	  LayoutInflater mInflater = LayoutInflater.from(context);	  
	  View view = mInflater.inflate(MenuItemLayoutId,null, false);
		ImageView iv = (ImageView) view.findViewById(R.id.id_item_image);
		TextView tv = (TextView) view.findViewById(R.id.id_item_text);
		if (iv != null){
			iv.setVisibility(View.VISIBLE);
			if(ImgIdx!=-1)
			iv.setImageResource(ImgIdx);
		}
		if(tv!=null){
			tv.setVisibility(View.VISIBLE);		
			if(StrIdx!=-1){
				tv.setText(context.getResources().getText(StrIdx));
			}else{
				tv.setText("");
			}
			
		}
		return view;
}
public static View GetImageText(int ImgIdx, int StrIdx,Context context)
{
	
	
	  int MenuItemLayoutId = R.layout.image_text_item;
	  LayoutInflater mInflater = LayoutInflater.from(context);	  
	  View view = mInflater.inflate(MenuItemLayoutId,null, false);
		ImageView iv = (ImageView) view.findViewById(R.id.id_item_image);
		TextView tv = (TextView) view.findViewById(R.id.id_item_text);
		if (iv != null){
			iv.setVisibility(View.VISIBLE);
			if(ImgIdx!=-1)
			iv.setImageResource(ImgIdx);
		}
		if(tv!=null){
			tv.setVisibility(View.VISIBLE);		
			if(StrIdx!=-1){
				tv.setText(context.getResources().getText(StrIdx));
			}else{
				tv.setText("");
			}
			
		}
		return view;
}
public void InitTextAreaParam()
{
	  mCenterHTxt.setOrientation(LinearLayout.VERTICAL);
	  mCenterHTxt.setGravity(Gravity.CENTER);  
     
	  LinearLayout.LayoutParams tv1Layoutparam=new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
	  tv1Layoutparam.gravity=Gravity.CENTER;
	     
	  LinearLayout.LayoutParams tv2Layoutparam=new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
	  tv2Layoutparam.gravity=Gravity.CENTER;
	  
	  mCenterHTxtTV1.setLayoutParams(tv1Layoutparam);
	  mCenterHTxtTV2.setLayoutParams(tv2Layoutparam);
	  
}
public  LinearLayout InitTextArea(Context context)
{
     LinearLayout mCenter01=new  LinearLayout(context);	
 	 		
	 mCenterHTxtTV1=new TextView(context);
	 mCenterHTxtTV1.setGravity(Gravity.CENTER);
	// mCenterHTxtTV1.setText(getContext().getString(R.string.app_des));

     mCenterHTxtTV2=new TextView(context);
	 mCenterHTxtTV2.setGravity(Gravity.CENTER_VERTICAL);
     mCenterHTxtTV2.setText(getContext().getString(R.string.app_des));
     mCenterHTxtTV2.setTextColor(Color.WHITE);
     mCenterHTxtTV2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
   //  mCenterHTxtTV2.setText("功能介绍......");
     
     mCenter01.addView(mCenterHTxtTV1);
     mCenter01.addView(mCenterHTxtTV2);
     return  mCenter01;
}
public void InitCircleMenuAreaParam()
{
	double CircleHeight=1.0*(960-310)/1278*mBgmHeight;
	FrameLayout.LayoutParams mCircleLayoutParam=new FrameLayout.LayoutParams((int)CircleHeight,(int)CircleHeight);
	mCircleLayoutParam.setMargins(0,(int)(m_CircleTopMarginsScale*mBgmHeight),0,0);
	mCenterHCircleMenuLayout.setLayoutParams(mCircleLayoutParam);
}
public void InitCircleMenuAreaParam2()
{
	double CircleHeight=1.0*(960-310)/1278*mBgmHeight;
	FrameLayout.LayoutParams mCircleLayoutParam=new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int)CircleHeight);
	mCircleLayoutParam.setMargins(0,(int)(m_CircleTopMarginsScale*mBgmHeight),0,0);
	mCenterHCircleMenuLayout.setLayoutParams(mCircleLayoutParam);
}
public FrameLayout InitCircleMenuLayout(Context context)
{
		
	FrameLayout  Center02=new  FrameLayout(context);
	mCenterHCircleMenuLayout=this.InitCircleMenu(context);	
	Center02.addView(mCenterHCircleMenuLayout);
	return Center02;
}
public void InitCircleMenuLayoutParam()
{
	
}
public CircleMenuLayout InitCircleMenu(Context context)
{
	this.InitCircleString(context);
	CircleMenuLayout mCircleMenuLayout=new CircleMenuLayout(context);
//	mCircleMenuLayout.setBackgroundColor(Color.CYAN);
	
	RelativeLayout relativeLayout=new RelativeLayout(context);
	RelativeLayout.LayoutParams imageViewLayoutParam=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
	//relativeLayout.setBackgroundColor(Color.RED);
	relativeLayout.setId(R.id.id_circle_menu_item_center);
	ImageView imageView=new ImageView(context);
	BitmapDrawable bitmapJit=(BitmapDrawable) Res().getDrawable(R.drawable.zxc);
	imageView.setBackground(bitmapJit);
	
	ViewGroup.LayoutParams relativeLayoutParam=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
	
	relativeLayout.addView(imageView,imageViewLayoutParam);
	//relativeLayout.setBackgroundColor(Color.RED);
	mCircleMenuLayout.addView(relativeLayout,relativeLayoutParam);
	
	mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
	
	
	
	mCircleMenuLayout.setOnMenuItemClickListener(new com.zxc.view.CircleMenuLayout.OnMenuItemClickListener()
	{
		
		@Override
		public void itemClick(View view, int pos)
		{
			Toast.makeText(getContext(), mItemTexts[pos],Toast.LENGTH_SHORT).show();
			

		}
		
		@Override
		public void itemCenterClick(View view)
		{
			Toast.makeText(getContext(),
					getContext().getResources().getString(R.string.ToastClickCcenterButton),Toast.LENGTH_SHORT).show();
			
		}
	});	
	return mCircleMenuLayout;
}
public void SetCircleMenuClickListener(CircleMenuLayout.OnMenuItemClickListener listener)
{
	this.mCenterHCircleMenuLayout.setOnMenuItemClickListener(listener);
}
public int  GetClickIdx(int idx)
{
	return this.mItemImgs[idx];
}
@Override
public void onGlobalLayout()
{     
      mRootWidth=GetView().getMeasuredWidth();
      mRootHeight=GetView().getMeasuredHeight(); 
      //Log.i("CDH", "Global W:"+mRootWidthOld+"  H:"+mRootHeightOld);	
		//first in this Place	
		if((mRootWidthOld!=mRootWidth)||(mRootHeightOld!=mRootHeight)){
			 
			// Layout Change
			 // this.mLeftRightDetector.mPhoneHand=PhoneInHand.PhoneLeftHand;
			  this.mLeftRightDetector.SetWidthHeight(mRootWidth, mRootHeight);
			 // this.HandChange(this.mLeftRightDetector.mPhoneHand);
			  
		}	
		mRootWidthOld=mRootWidth;
		mRootHeightOld=mRootHeight;
	 
	}
@Override
public void HandChange(PhoneInHand  hand)
{  
	this.mPhoneHand=hand;
	
	 if(hand==PhoneInHand.PhoneLeftHand){
		// GetView().removeAllViews();
		 //GetView().setBackgroundColor(Color.BLUE);
		// SetBackground2View(mView,bmpBackgroundLeft);
		/* try{
			 
		 }catch(Expection e){
			 
		 }*/
		 
		  InitMainLayoutParamLeft();
		// Toast.makeText(getContext(), "Left", Toast.LENGTH_SHORT);
	  }else if(hand==PhoneInHand.PhoneRightHand){
		   //  GetView().removeAllViews();
			
		//	 GetView().setBackgroundColor(Color.BLUE);
			// GetView().setBackgroundColor(Color.RED);
		 // SetBackground2View(mView,bmpBackgroundRight);		
		
		  InitMainLayoutParamRight(); 
		 // Toast.makeText(getContext(), "Right", Toast.LENGTH_SHORT);
	  }else{
		  ;
	  }
	 //			
}
public void SetBackground2View(View v,Bitmap bmp)
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
		
		BitmapDrawable background =new BitmapDrawable(Res(),bmpCanvas);	    
	    v.setBackground(background);
	   
	    this.mBgmWidth=bmpNew.getWidth();
		this.mBgmHeight=bmpNew.getHeight();
	    
	    
}


}
