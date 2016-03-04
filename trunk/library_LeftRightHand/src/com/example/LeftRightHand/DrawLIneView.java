package com.example.LeftRightHand;




import com.example.LeftRightHand.LeftRightGestureDetector.PhoneInHand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;


@SuppressLint({ "ClickableViewAccessibility", "ShowToast" }) public class DrawLIneView extends View implements LeftRightGestureDetector.OnHandChangeListener  {
	 
    
     private LeftRightGestureDetector lrgd;
	 private int mWidth;
	 private int mHeight;
	 
	 public DrawLIneView(Context context) {

	super(context);  
	  this.setFocusable(true);
	  lrgd=new LeftRightGestureDetector(context);
	  lrgd.setOnHandChangeListener(this);
	 }
	 
	 @Override
	 protected void onDraw(Canvas canvas){
	  super.onDraw(canvas);
	  this.mHeight=canvas.getHeight();
	  this.mWidth=canvas.getWidth();
	  lrgd.SetWidthHeight(mWidth, mHeight);
	  lrgd.DrawSomethingOnCanvas(canvas);
	  }
	 @Override
	 public boolean onTouchEvent(MotionEvent e){
	  lrgd.callOnTouch(this,e);
	  ///////////////
	  this.invalidate();
	  return true;
	 }
	 public void HandChange(PhoneInHand  hand)
	 {
		 this.invalidate();
	 }
	
}