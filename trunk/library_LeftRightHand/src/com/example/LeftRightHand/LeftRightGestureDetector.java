package com.example.LeftRightHand;

import java.lang.Thread.State;
import java.util.Vector;





import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
/**
 * 
 * @author Administrator
 *
 */
class PhonePPI
{
  public static String getAndroidPhoneModel()
  {
          String androidDisplay = null;
          androidDisplay = android.os.Build.MODEL;
          return androidDisplay;
  }
	public static double GetPhonePPI()
	{
		double defaultppi=-1;
		String  phoneType=getAndroidPhoneModel();
		if("HUAWEI C8815".equals(phoneType)){
			defaultppi=220;
		}else if("HUAWEI P6-C00".equals(phoneType)){
			defaultppi=312;
		}else if("".equals(phoneType)){
			defaultppi=-1;
		}else{
			defaultppi=-1;
		}
		
		return defaultppi;
	}
}
/**
 * 
 * @author Administrator
 *
 */
class LineCC{
	public double a;
	public double b;
	public double c;
	private double span;
	public double spanCm;
	public double angleDegree;
	public int    Errot;
	private Display mdisplay;
	LineCC(){
	a=0;
	b=0;
	c=0;
	Errot=0;
	}
	LineCC(Display display){
		a=0;
		b=0;
		c=0;
		Errot=0;
		mdisplay=display;
		}
    void ParseSpanAngle(PointF p0,PointF p1){
    	this.Parse(p0, p1);
    	this.span=Point2PointDst(p0, p1);
    	this.spanCm=Point2PointDstCm(p0,p1,mdisplay);
    	if(Errot>=0){
    		this.CalculateAngle();
    		
    	}
    }
	void Parse(PointF p0,PointF p1)
	{
		Errot=0;
		PointF pmax=new PointF();
		PointF pmin=new PointF();
		if(p0.x<p1.x){
		  pmin=p0;
		  pmax=p1;
		}else{
		  pmin=p1;
		  pmax=p0;
		}
		
		if(p0.x==p1.x&&p0.y!=p1.y){
			b=0;
			a=1;
			c=-1*p0.x;
			return;
		}
		if(p0.x!=p1.x&&p0.y==p1.y){
			a=0;
			b=1;
			c=-1*p0.y;
			return;
		}
		if(p0.x!=p1.x&&p0.y!=p1.y){
			a=pmax.y-pmin.y;
			b=pmin.x-pmax.x;
			c=pmax.x*pmin.y-pmin.x*pmax.y;
			return;
		}
		if(p0.x==p1.x&&p0.y==p1.y){
			Errot=-1;
			return;
		}

	}
	public double getSpan(){
		return this.span;
	}
	public static double Point2PointDstCm(PointF p0,PointF p1,Display display)
	{
		double dst=0;	
		DisplayMetrics dm= new DisplayMetrics();
	    display.getMetrics(dm);
		double ppiX;
		double ppiY;
		ppiX=ppiY=PhonePPI.GetPhonePPI();
	    if(ppiX<0){
	    	ppiX=dm.xdpi;
	    	ppiY=dm.ydpi;
	    }
		dst=Math.sqrt(Math.pow((p0.x-p1.x)/ppiX, 2)+Math.pow((p0.y-p1.y)/ppiY, 2));
		dst=dst*2.54;
		return dst;	
	}
	private static PointF GetRealDeviceSizeInPixels(Display display)
	    {
	    	PointF point=new PointF();
	    	int mWidthPixels;
	    	int mHeightPixels;
	    	
	        DisplayMetrics displayMetrics = new DisplayMetrics();
	        display.getMetrics(displayMetrics);
	        // since SDK_INT = 1;
	        mWidthPixels = displayMetrics.widthPixels;
	        mHeightPixels = displayMetrics.heightPixels;

	        // includes window decorations (statusbar bar/menu bar)
	        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
	        {
	            try
	            {
	                mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
	                mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
	            }
	            catch (Exception ignored)
	            {
	            }
	        }

	        // includes window decorations (statusbar bar/menu bar)
	        if (Build.VERSION.SDK_INT >= 17)
	        {
	            try
	            {
	                Point realSize = new Point();
	                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
	                mWidthPixels = realSize.x;
	                mHeightPixels = realSize.y;
	            }
	            catch (Exception ignored)
	            {
	            }
	        }
	        point.x=mWidthPixels;
	        point.y=mHeightPixels;
	        return point;
	    }
	public static double Point2PointDst(PointF p0,PointF p1)
	{
		double dst=0;		
		dst=Math.sqrt(Math.pow(p0.x-p1.x, 2)+Math.pow(p0.y-p1.y, 2));
		return dst;		
	} 
	public static boolean PointLinerelation(PointF point_t,LineCC line_t)
	{		
		double   relative=line_t.a*point_t.x+line_t.b*point_t.y+line_t.c;
		if(relative>0){
			return true;
		}else{
			return false;
		}
	}
	void CalculateAngle()
	{
		if(b!=0){
			double k=-1*a/b;
			double angleRadian=Math.atan(k);
			this.angleDegree=angleRadian*180/Math.PI;
		}else{
			this.angleDegree=90;
		}
		
		/*
	   double angleRadian=Math.atan2(a,-1*b);
	   this.angleDegree=angleRadian*180/Math.PI;
	   */
	}
	double getXvalue(double y)
	{
		double x=0;
		x=-1*(b*y+c)/a;
		return x;
	}
    double getYvalue(double x)
	{
		double y=0;
		y=-1*(a*x+c)/b;
		return y;
	}
}
/**
 * 
 * @author Administrator
 *
 */
class HotArea implements Runnable{
	 int xScale;
	 int yScale;
	 int [][]HotAreaMapLeft;
	 int [][]HotAreaMapRight;
	 double xStep;
	 double yStep;
	 int Width;
	 int Height;
	 int WidthNew;
	 int HeightNew;
//	public Bitmap mapIdx;
//	public Bitmap mapIdxLeftColor;
//	public Bitmap mapIdxRightColor;
	 Vector<PointF> vPointRaw;
	 Vector<Integer>  ColorIdx;
	HotArea(int xs,int ys){
		xScale=xs;
		yScale=ys;		
		this.InitHot8x8LeftHandIdx();
		this.InitHot8x8RightHandIdx();
		this.FillColorIdx();
	}
	public void FillColorIdx()
	{
		ColorIdx=new Vector<Integer>();
		//ColorIdx.add(Color.BLACK);
		//ColorIdx.add(Color.BLUE);
		//ColorIdx.add(Color.CYAN);
		ColorIdx.add(0xff66ffcc);
		ColorIdx.add(0xffC7C0Fb);
		//ColorIdx.add(Color.DKGRAY);
		ColorIdx.add(Color.GRAY);
		ColorIdx.add(Color.GREEN);
		ColorIdx.add(Color.LTGRAY);
		ColorIdx.add(Color.MAGENTA);
		ColorIdx.add(Color.RED);
	//	ColorIdx.add(Color.TRANSPARENT);
		ColorIdx.add(Color.WHITE);
		ColorIdx.add(Color.YELLOW);		
	}
	public void  SetWH(int width,int height)
	{
	
		/*
		if(this.Width==width&&this.Height==height){
			return;
		}else{
			Width=width;
			Height=height;
			xStep=1.0*Width/xScale;
			yStep=1.0*Height/yScale;
			this.FillHotAreaImg();
		}*/			
		this.WidthNew=width;
		this.HeightNew=height;
		
	}
	public int CalPointHotIndx(int x,int y)
	{
		int XDim=(int)(x/xStep);
		int YDim=(int)(y/yStep);
		int XYIdx=YDim*xScale+XDim;
		return XYIdx;
	}
	private void FillHotAreaImg()
	{
		/*
		mapIdx=Bitmap.createBitmap(Width,Height,Bitmap.Config.ALPHA_8);
		for(int x=0;x<mapIdx.getWidth();x++){
			for(int y=0;y<mapIdx.getHeight();y++){
				int Idx=this.CalPointHotIndx(x, y);
				mapIdx.setPixel(x, y, Idx);
			}
		}*/
		////////////////////////////////////////////////////////////
		/*
		mapIdxLeftColor=Bitmap.createBitmap(Width, Height, Bitmap.Config.RGB_565);
		mapIdxRightColor=Bitmap.createBitmap(Width,Height,Bitmap.Config.RGB_565);
		for(int x=0;x<mapIdxLeftColor.getWidth();x++){
			for(int y=0;y<mapIdxLeftColor.getHeight();y++){
				 int Idx=this.CalPointHotIndx(x, y);
				 int colori=Idx%(ColorIdx.size());
				if(this.isPointInLeftHotArea(new PointF(x,y))){
					mapIdxLeftColor.setPixel(x, y,Color.CYAN);	
				}else{
					mapIdxLeftColor.setPixel(x, y,ColorIdx.get(colori));
				}
				if(this.isPointInRightHotArea(new PointF(x,y))){
					mapIdxRightColor.setPixel(x, y,Color.CYAN);	
				}else{
					mapIdxRightColor.setPixel(x, y,ColorIdx.get(colori));
				}
			}
		}*/
		this.FillHotAreaImg2();
	}
	private void FillHotAreaImg2()
	{
		/*
		mapIdxLeftColor=Bitmap.createBitmap(Width, Height, Bitmap.Config.RGB_565);
		mapIdxRightColor=Bitmap.createBitmap(Width,Height,Bitmap.Config.RGB_565);
		
		Canvas canvasL=new Canvas(mapIdxLeftColor);
		Canvas canvasR=new Canvas(mapIdxRightColor);
		Paint  paint=new Paint();
		int idx=0;
		for (int i = 0; i <this.xScale; i++){
   		 for (int j = 0; j <this.yScale; j++){
   			RectF rect=new RectF();   	
   			rect.top=(float)((j*this.yStep));
   			rect.left=(float)((i*this.xStep));
   			rect.right=(float)((i+1)*this.xStep);
   			rect.bottom=(float)((j+1)*this.yStep);
   		   int colori=idx%(ColorIdx.size());
   			if(HotAreaMapLeft[i][j]==1){
   				paint.setColor(Color.CYAN);
   			    canvasL.drawRect(rect, paint);
   			}else{
   				paint.setColor(ColorIdx.get(colori));
   			    canvasL.drawRect(rect, paint);
   			}
   			if(HotAreaMapRight[i][j]==1){
   				paint.setColor(Color.CYAN);
   			    canvasR.drawRect(rect, paint);
      		}else{
      			paint.setColor(ColorIdx.get(colori));
      		    canvasR.drawRect(rect, paint);
      		}   			
   			 idx++;
   		 }
   		}	*/
	}
	public void run(){
		
		int width=this.WidthNew;
		int height=this.HeightNew;
		if(this.Width==width&&this.Height==height){
			return;
		}else{
			Width=width;
			Height=height;
			xStep=1.0*Width/xScale;
			yStep=1.0*Height/yScale;
			this.FillHotAreaImg();
		}
	}
    public void InitHot8x8LeftHandIdx()
    {
    	if(xScale==8&&yScale==8){    		
    		HotAreaMapLeft=new int[8][8];
    		for (int i = 0; i < HotAreaMapLeft.length; i++){
    		 for (int j = 0; j <HotAreaMapLeft[i].length; j++){
    			 HotAreaMapLeft[i][j]=0;
    		 }
    		}
    		HotAreaMapLeft[0][1]=1;    		
    		HotAreaMapLeft[1][1]=1;
    		HotAreaMapLeft[0][2]=1;
    		HotAreaMapLeft[1][2]=1;
    		HotAreaMapLeft[2][2]=1;
    		HotAreaMapLeft[0][3]=1;
    		HotAreaMapLeft[1][3]=1;
    		HotAreaMapLeft[2][3]=1;
    		HotAreaMapLeft[3][3]=1;
    		HotAreaMapLeft[1][4]=1;
    		HotAreaMapLeft[2][4]=1;
    		HotAreaMapLeft[3][4]=1;
    		HotAreaMapLeft[4][4]=1;
    		HotAreaMapLeft[5][4]=1;
    		HotAreaMapLeft[2][5]=1;
    		HotAreaMapLeft[3][5]=1; 
    		HotAreaMapLeft[4][5]=1;
    		HotAreaMapLeft[5][5]=1;
    		HotAreaMapLeft[2][6]=1;
    		HotAreaMapLeft[3][6]=1; 
    		HotAreaMapLeft[4][6]=1;
    		HotAreaMapLeft[5][6]=1;
    		HotAreaMapLeft[2][7]=1;
    		HotAreaMapLeft[3][7]=1; 
    		HotAreaMapLeft[4][7]=1;
    		HotAreaMapLeft[5][7]=1;
    		     		
    	}
    }
    public boolean isPointInLeftHotArea(PointF point)
    {   
    	boolean result=false;
    	if((point.x<this.Width)
    			&&(point.x>=0)
    			&&(point.y>=0)
    			&&(point.y<this.Height))
    	{
    		
    		int XDim=(int)(point.x/xStep);
    		int YDim=(int)(point.y/yStep);
    		if(HotAreaMapLeft[XDim][YDim]==1){
    			result=true;
    		}else{
    			result=false;
    		}
    		
    	}
    	return result;
    	
    }
    public void InitHot8x8RightHandIdx()
    {
    	if(xScale==8&&yScale==8){
    		HotAreaMapRight=new int[8][8];
    		for (int i = 0; i < HotAreaMapRight.length; i++){
    		 for (int j = 0; j <HotAreaMapRight[i].length; j++){
    			HotAreaMapRight[i][j]=0;
    		 }
    		}
    		HotAreaMapRight[6][1]=1;    		
    		HotAreaMapRight[7][1]=1;
    		HotAreaMapRight[5][2]=1;
    		HotAreaMapRight[6][2]=1;
    		HotAreaMapRight[7][2]=1;
    		HotAreaMapRight[4][3]=1;
    		HotAreaMapRight[5][3]=1;
    		HotAreaMapRight[6][3]=1;
    		HotAreaMapRight[7][3]=1;
    		HotAreaMapRight[2][4]=1;
    		HotAreaMapRight[3][4]=1;
    		HotAreaMapRight[4][4]=1;
    		HotAreaMapRight[5][4]=1;
    		HotAreaMapRight[6][4]=1;
    		HotAreaMapRight[2][5]=1;
    		HotAreaMapRight[3][5]=1; 
    		HotAreaMapRight[4][5]=1;
    		HotAreaMapRight[5][5]=1;
    		HotAreaMapRight[2][6]=1;
    		HotAreaMapRight[3][6]=1; 
    		HotAreaMapRight[4][6]=1;
    		HotAreaMapRight[5][6]=1;
    		HotAreaMapRight[2][7]=1;
    		HotAreaMapRight[3][7]=1; 
    		HotAreaMapRight[4][7]=1;
    		HotAreaMapRight[5][7]=1;
    	}
    }
    public boolean isPointInRightHotArea(PointF point)
    {
    	boolean result=false;
    	if((point.x<this.Width)
    			&&(point.x>=0)
    			&&(point.y>=0)
    			&&(point.y<this.Height))
    	{
    		
    		int XDim=(int)(point.x/xStep);
    		int YDim=(int)(point.y/yStep);
    		if(HotAreaMapRight[XDim][YDim]==1){
    			result=true;
    		}else{
    			result=false;
    		}
    		
    	}
    	return result;
    	
    } 
 }	
/**
 * 
 * @author Administrator
 *
 */
public class LeftRightGestureDetector implements View.OnTouchListener{
	 long mTimeDownms;
	 long mTimeUpms;
	 long mTimeMovems;
	 long mTimeDownns;
	 long mTimeUpns;
	 long mTimeMovens;
	 int Width;
	 int Height;
	//private int           mFingerNum;
	private Vector<PointF> vPointRaw;
	private Vector<PointF> vPointhandled;
	private HotArea hotArea;
	private LineCC  mLineSpan;
	private Context  mContext;
	private Display mDisplay;
	private Thread mThreadRecognizeLR;
	public enum PointsHotArea{
		PointsInLeft,PointsInRight,PointsUnknow;
	}
	PointsHotArea mPointsInArea;
	public enum PhoneInHand{
		PhoneLeftHand,PhoneRightHand;
	}
	private PhoneInHand  mPhoneHand;
	public LeftRightGestureDetector(Context context)
	{
		mContext=context;
		mDisplay=((Activity)mContext).getWindowManager().getDefaultDisplay();
		///////////////////
		vPointRaw=new Vector<PointF>();
		hotArea=new HotArea(8,8);
		this.SetPhoneHand(PhoneInHand.PhoneRightHand);
		mPointsInArea=PointsHotArea.PointsInRight;
		this.vPointhandled=new Vector<PointF>();
		//mFingerNum=0;
		mLineSpan=new LineCC(this.mDisplay);
		
	}
 
	public void  SetWidthHeight(int width,int height)
	{
		if(width+height==0){
			//View not setup
			return;
		}				
		if(this.Width==width&&this.Height==height){
			//无需改变热区图像的大小
			return;
		}else{
			//width height changed
			this.Width=width;
			this.Height=height;
			this.hotArea.SetWH(width, height);
			Thread t=new Thread(this.hotArea,"FillBitmap");
			t.start();	
			this.SetPhoneHand(this.mPhoneHand);
		}	
	}
	public void DrawPathOnCanvas(Canvas canvas)
	{
		  Path path=new Path();
		  Paint paint=new Paint();
			paint.setColor(Color.RED);
			paint.setAntiAlias(true);
			paint.setStrokeWidth(1);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeCap(Paint.Cap.ROUND);
		    Points2Path(vPointRaw,path);
		  canvas.drawPath(path,paint);
	}
	public void DrawHotAreaOnCanvas(Canvas canvas)
	{
		/*
		if(hotArea.mapIdxRightColor==null) 
			return;
		if(hotArea.mapIdxLeftColor==null)  
			return;
		
		if(this.mPointsInArea==PointsHotArea.PointsInLeft){
			canvas.drawBitmap(hotArea.mapIdxLeftColor,0,0,null);
		}else if(this.mPointsInArea==PointsHotArea.PointsInRight){
			canvas.drawBitmap(hotArea.mapIdxRightColor,0,0,null);
		}else{
			//canvas.drawBitmap(hotArea.mapIdxColor,0,0,null);
		}
		*/
	}
	public static void Points2Path(Vector<PointF> points,Path path)
	{
		path.reset();
		for(int i=0;i<points.size();i++){
			if(i==0){
				path.moveTo(points.get(i).x,points.get(i).y);		
			}else{
				path.lineTo(points.get(i).x,points.get(i).y);
			}			
			path.moveTo(points.get(i).x,points.get(i).y);			
		}
	}
	public void DrawPointOnCanvas(Canvas canvas)
	{
	     	float [] points=vtpoint2array(vPointRaw);
			Paint paint=new Paint();
			paint.setColor(Color.RED);
			paint.setAntiAlias(true);
			paint.setStrokeWidth(4);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeCap(Paint.Cap.ROUND);
			canvas.drawPoints(points,paint);
	     	
	}
	public void DrawSpanOnCanvas(Canvas canvas)
	{
	  int pointSize=vPointRaw.size();
	  if(pointSize>=3){
		  Paint paint=new Paint();
		  canvas.drawLine((float)vPointRaw.get(0).x,(float)vPointRaw.get(0).y,
				  (float)vPointRaw.get(pointSize-1).x,(float)vPointRaw.get(pointSize-1).y,
				  paint);
	  }
	  
	  if(true){
		  Paint paint=new Paint();
		  paint.setColor(Color.MAGENTA);
		  canvas.drawLine((float)this.mLineSpan.getXvalue(0),(float)0,
				  (float)(this.mLineSpan.getXvalue(this.Height-1)),(float)(this.Height-1),
				  paint);
	  }
	 
	}
	public void DrawPointHandledOnCanvas(Canvas canvas)
	{
		/*
	     	float [] points=vtpoint2array(this.vPointhandled);
			Paint paint=new Paint();
			paint.setColor(Color.BLUE);
			paint.setAntiAlias(true);
			paint.setStrokeWidth(1);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeCap(Paint.Cap.ROUND);
			canvas.drawPoints(points,paint);
			*/
		Paint paint=new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(1);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeCap(Paint.Cap.ROUND);
		
		for(int i=0;i<this.vPointhandled.size();i++){
			boolean leftr=this.hotArea.isPointInLeftHotArea(vPointhandled.get(i));
			boolean rightr=this.hotArea.isPointInRightHotArea(vPointhandled.get(i));
		   if(leftr||rightr){
			   paint.setColor(Color.YELLOW);
			   canvas.drawPoint(vPointhandled.get(i).x,vPointhandled.get(i).y, paint);
		   }else{
			   paint.setColor(Color.BLUE);
			   canvas.drawPoint(vPointhandled.get(i).x,vPointhandled.get(i).y, paint);
		   }
		  
		}
	     	
	}
	 public float[] vtpoint2array(Vector<PointF> vt)
	 {
		 float []pa=new float[vt.size()*2];
		 for(int i=0;i<vt.size();i++){
			 pa[i*2]=vt.get(i).x;
			 pa[i*2+1]=vt.get(i).y;
		 }
			 
		 return pa;
	 }
	 public void handleRowData2Multiply()
	 {
		
		 this.vPointhandled.clear();
		 ////////////////////////////////////////////
		 for(int i=0;i<this.vPointRaw.size()-1;i++){
			 LineCC line_t=new LineCC();
			 line_t.Parse(vPointRaw.get(i),vPointRaw.get(i+1));
			 double Xdelta=vPointRaw.get(i).x-vPointRaw.get(i+1).x;
			 double Ydelta=vPointRaw.get(i).y-vPointRaw.get(i+1).y;
			 if(Math.abs(Xdelta)>=Math.abs(Ydelta)){
				 int maxI=(int)Math.max(vPointRaw.get(i).x,vPointRaw.get(i+1).x);
				 int minI=(int)Math.min(vPointRaw.get(i).x,vPointRaw.get(i+1).x);
				 for(int xi=minI;xi<maxI;xi++){					 
					 double yi=line_t.getYvalue(xi);
					 this.vPointhandled.add(new PointF((float)xi,(float)yi));
				 }
			 }else{
				 int maxI=(int)Math.max(vPointRaw.get(i).y,vPointRaw.get(i+1).y);
				 int minI=(int)Math.min(vPointRaw.get(i).y,vPointRaw.get(i+1).y);
					for(int yi=minI;yi<maxI;yi++){
					     double xi=line_t.getXvalue(yi);
						 this.vPointhandled.add(new PointF((float)xi,(float)yi));
				    }
			 }
			 
		 }
	 }
	 public void DrawTextOnCanvas(Canvas canvas)
	 {
		  Paint paint_txt;
		  paint_txt = new Paint(Paint.ANTI_ALIAS_FLAG);  
		  paint_txt.setTextSize( 35);  
		  paint_txt.setColor( Color.BLACK); 
		  int hIdx=1;
		 {			
			  canvas.drawText("Point Raw Size:"+Integer.toString(vPointRaw.size()), 0, (hIdx++)*40, paint_txt);
		 }
		 {
			//  canvas.drawText("Move Time ms:"+Long.toString(mTimeMovems), 0, hIdx++*40, paint_txt);
		 }
		 {
			  canvas.drawText("Move Time2 ms:"+Long.toString(mTimeMovens/1000/1000), 0, hIdx++*40, paint_txt);
			  
			  canvas.drawText("LineSpan Pixel:"+Double.toString(this.mLineSpan.getSpan()), 0, hIdx++*40, paint_txt);
			  canvas.drawText("LineSpan Cm :"+Double.toString(this.mLineSpan.spanCm), 0, hIdx++*40, paint_txt);
			  canvas.drawText("LineAngle :"+Double.toString(this.mLineSpan.angleDegree), 0, hIdx++*40, paint_txt);
			  
			  canvas.drawText("手模式 :"+this.mPhoneHand.toString(), 0, hIdx++*40, paint_txt);
			  
			 
			  paint_txt.setColor(Color.WHITE);
			  paint_txt.setAlpha(255/3*2);
			  if(this.mPhoneHand==PhoneInHand.PhoneLeftHand){
				  //左手
				  canvas.drawCircle((float)(this.Width/3.0),(float)(this.Height/2), (float)(this.Width/2), paint_txt);
			  }else{
				  canvas.drawCircle((float)(this.Width/3*2),(float)(this.Height/2), (this.Width/2), paint_txt);
			  }
		 }
		 
	 }
	 public boolean onTouch(View v, MotionEvent event)
	 {            
       this.callOnTouch(v,event);        
		 
         return true;  
     }  
	void callOnTouch(View v, MotionEvent e){
		 int action=e.getAction();
		 int pointerCount =e.getPointerCount();
		 if(pointerCount==1){
			 switch(action){
			  case MotionEvent.ACTION_DOWN:	   
			   this.SetDownData(e);	   
			   break;
			  case MotionEvent.ACTION_MOVE:	   
			   this.SetMoveData(e); 
			   break;
			  case MotionEvent.ACTION_UP:
			   this.SetUpData(v,e);
			   break;
			  case MotionEvent.ACTION_POINTER_UP:
			  break;
			  case MotionEvent.ACTION_POINTER_DOWN:
			  break;
			  default:
			  break;
			  }
		 }else{
			 //pointer count >1 clear buffer
			 this.vPointRaw.clear();
		 }		
	}
	void SetDownData(MotionEvent e)
	{
		mTimeDownms=System.currentTimeMillis();
		mTimeDownns=System.nanoTime();
		this.vPointRaw.clear();
		//this.mFingerNum=0;
		this.push2Pointvector(e);
		this.vPointhandled.clear();
	}
	void SetMoveData(MotionEvent e)
	{
		this.push2Pointvector(e);
	}
	void SetUpData(View v, MotionEvent e)
	{
		mTimeUpms=System.currentTimeMillis();
		mTimeMovems=mTimeUpms-mTimeDownms;
		//////////////////////
		mTimeUpns=System.nanoTime();
		mTimeMovens=mTimeUpns-mTimeDownns;
		this.push2Pointvector(e);	
		/*--------------------------------------------------------*/
		/*
		if(mThreadRecognizeLR==null){
			this.mThreadRecognizeLR=new Thread(new DoLeftRightRecognize()); 
			 mThreadRecognizeLR.run();
		}else{
			Thread.State threadState=mThreadRecognizeLR.getState();
			if(threadState.TERMINATED==threadState){
				this.mThreadRecognizeLR=new Thread(new DoLeftRightRecognize()); 
				 mThreadRecognizeLR.run();
			}
		}*/
		/*--------------------------------------------------------*/
		this.mThreadRecognizeLR=new Thread(new DoLeftRightRecognize()); 
		 //mThreadRecognizeLR.run();
		  v.post(mThreadRecognizeLR);
		

	}
	 public void push2Pointvector(MotionEvent ev){
		/* 
		  int historySize = ev.getHistorySize(); 
		  for (int i = 0; i < historySize; i++) {
			  float x = ev.getHistoricalX(i);
			  float y = ev.getHistoricalY(i); 
			  this.v_point.add(new PointF(x,y));

			  } 
		   final int pointerCount = ev.getPointerCount(); 
		   for (int p = pointerCount-1; p >=0; p--) {   
			   this.v_point.add(new PointF(ev.getX(p),ev.getY(p)));
		   	} 
		   	*/
		 final int historySize = ev.getHistorySize();
	      final int pointerCount = ev.getPointerCount();
	      for (int h = 0; h < historySize; h++) {
	          for (int p = 0; p < pointerCount; p++) {
	        	  this.vPointRaw.add(new PointF(ev.getHistoricalX(p, h), ev.getHistoricalY(p, h)));
	              
	          }      
	       }
	      System.out.printf("At time %d:", ev.getEventTime());
	      for (int p = 0; p < pointerCount; p++) {
	    	  this.vPointRaw.add(new PointF(ev.getX(p),ev.getY(p)));    
		} 		   
	 }
	public void DrawSomethingOnCanvas(Canvas canvas)
	{
		  DrawHotAreaOnCanvas(canvas);
		  DrawPointHandledOnCanvas(canvas);
		  DrawSpanOnCanvas(canvas);
		  DrawPointOnCanvas(canvas);
		  DrawTextOnCanvas(canvas);
	}
	public PointsHotArea CalculatePointsInHotArea()
	{
		PointsHotArea pha=PointsHotArea.PointsUnknow;
		int pointSize=this.vPointhandled.size();
		int inLeftPointSize=0;
		int inRightPointSize=0;
		for(int i=0;i<pointSize;i++){			
			boolean result_left=this.hotArea.isPointInLeftHotArea(vPointhandled.get(i));
			if(result_left) 	inLeftPointSize++;
			
			boolean result_right=this.hotArea.isPointInRightHotArea(vPointhandled.get(i));
			if(result_right)   inRightPointSize++;
		}
		
		if(inLeftPointSize>inRightPointSize){
			double scale=1.0*inLeftPointSize/pointSize;
			if(scale>0.9){
				pha=PointsHotArea.PointsInLeft;
			}else{
				pha=PointsHotArea.PointsUnknow;
			}			
		}else if(inLeftPointSize<inRightPointSize){
			double scale=1.0*inRightPointSize/pointSize;
			if(scale>0.9){
				pha=PointsHotArea.PointsInRight;
			}else{
				pha=PointsHotArea.PointsUnknow;
			}			
		}else if(inLeftPointSize==inRightPointSize){
			pha=PointsHotArea.PointsUnknow;
		}
		return pha;
	}
	
	
	
	
	
	public static  double LinePoinstRelation(Vector<PointF> point,LineCC line)
	{  
	  final int step=1;
	  int PosRightCount=0;
	  int NegLeftCount=0;
	  for(int i=0;i<point.size();i+=step){
		  boolean  relative=LineCC.PointLinerelation(point.get(i),line);
		  if(relative==true){
			  PosRightCount++;
		  }else{
			  NegLeftCount++;
		  }
	  }
	  double Relation=1.0*PosRightCount/(PosRightCount+NegLeftCount);
	  return Relation;
    }
	/*
	 * interface
	 */
	public interface OnHandChangeListener
	{		
		void HandChange(PhoneInHand  hand);
	}

	/**
	 * 
	 */
	private OnHandChangeListener mOnHandChangeListener;

	/**
	 * 设置MenuItem的点击事件接口
	 * 
	 * @param mOnMenuItemClickListener
	 */
	public void setOnHandChangeListener(
			OnHandChangeListener mOnHandChangeListener_t)
	{
		this.mOnHandChangeListener= mOnHandChangeListener_t;
	}
	private void RecognizeRightLeftHand()
	 {
		//Step 1
		if(vPointRaw.size()<6){
			return;
		}
		//Step2
		 mLineSpan.ParseSpanAngle(vPointRaw.get(0),vPointRaw.get(vPointRaw.size()-1));
		 if(mLineSpan.spanCm<2.6||mLineSpan.spanCm>9)
			 return;
		  handleRowData2Multiply();
				 
		//Step  3
		 mPointsInArea=CalculatePointsInHotArea();
		 if(mPointsInArea==PointsHotArea.PointsUnknow){
			     return;
		 }else if(mPointsInArea==PointsHotArea.PointsInLeft){
			 if(mPhoneHand==PhoneInHand.PhoneLeftHand){
				 return;
			 }			 
		 }else if(mPointsInArea==PointsHotArea.PointsInRight){
			 if(mPhoneHand==PhoneInHand.PhoneRightHand){
				 return;
			 }
		 }		 
		//Step 4
		  if(mPointsInArea==PointsHotArea.PointsInLeft){
			  
			  if(mLineSpan.angleDegree>=30&&mLineSpan.angleDegree<=80){
					//DO under hear
				  double relation=LinePoinstRelation(vPointhandled,mLineSpan);
				  if(relation>0.9){
					  SetPhoneHand(PhoneInHand.PhoneLeftHand);
				  }				 
				}else{
					
				    return;
				}
			  
		  }else if(mPointsInArea==PointsHotArea.PointsInRight){
			if(mLineSpan.angleDegree<=-30&&mLineSpan.angleDegree>=-80){
				//DO under hear
				 double relation=LinePoinstRelation(vPointhandled,mLineSpan);
				 if(relation>0.9){
					 SetPhoneHand(PhoneInHand.PhoneRightHand);
				  }
			}else{
					return;
			}
		  }	
	}

	private class DoLeftRightRecognize implements Runnable
	{
	
		public void run()
		{
			RecognizeRightLeftHand();
		}
		
	}
	public void SetPhoneHand(PhoneInHand  hand)
	{
		 mPhoneHand=hand;
		 if(this.mOnHandChangeListener!=null){
			 this.mOnHandChangeListener.HandChange(hand);
		 }
	}
	
}
