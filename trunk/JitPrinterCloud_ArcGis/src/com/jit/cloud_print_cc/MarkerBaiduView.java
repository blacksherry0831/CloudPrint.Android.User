package com.jit.cloud_print_cc;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;



public class MarkerBaiduView 
{
	public ArrayList<Marker> mMarkerList=new ArrayList<Marker>();
	
	public MarkerBaiduView ()
	{
		
	}
	
	public static OverlayOptions GetDefaultMarker()
	{
		Marker ma=null;
		LatLng llA = new LatLng(31.911419,118.906299);
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		
		
		return ooA;
		
	}
	public static OverlayOptions GetDefaultMarker1()
	{
		return GetDefaultMarker(null);
	}
	public static OverlayOptions GetDefaultMarker( CloudPrintAddress info)
	{
		Marker ma=null;
		LatLng llA = new LatLng(31.911419,118.906299);
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		
		
		return ooA;
		
	}
	/**
	 * 
	 */
	public static ArrayList<CloudPrintAddress> GetPrintShopStatic()
	{
		ArrayList<CloudPrintAddress> info_array=new ArrayList<CloudPrintAddress>();
		
		{
			double LA_qin=31.911387;
			double LO_qin=118.90477;
			CloudPrintAddress info=new CloudPrintAddress(
					LA_qin,
					LO_qin,
					0,
					"亲打印-1",
					"距离1km",
					"金科南区食堂",
					 0);
			info_array.add(info);
		}
		{
			double LA_qin=31.910153;
			double LO_qin=118.904105;
			CloudPrintAddress info=new CloudPrintAddress(
					LA_qin,
					LO_qin,
					0,
					"亲打印-2",
					"距离1km",
					"金科南区食堂",
					 0);
			info_array.add(info);
		}
		return info_array;
	}
	/**
	 * 
	 */
	public static void AddDefaultMarker(BaiduMap mBaiduMap)
	{		
		AddDefaultMarker_M(mBaiduMap,GetPrintShopStatic());
	}
	public static void AddDefaultMarker_M(BaiduMap mBaiduMap,ArrayList<CloudPrintAddress> info)
	{
		for(CloudPrintAddress cpa : info){
			AddDefaultMarker_S(mBaiduMap,cpa);
		}
		
	}
	public static OverlayOptions AddDefaultMarker_S(BaiduMap mBaiduMap,CloudPrintAddress info)
	{		
		/*创建经纬度*/
		LatLng llA = new LatLng(info.getLatitude_Double(),info.getLongitude_Double());
		/*画图*/
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		/*图层创建*/
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		/*添加图层*/
		Overlay view=mBaiduMap.addOverlay(ooA);
		Bundle bundle = new Bundle();
		bundle.putSerializable("info", info);
		/*添加图层数据*/
		view.setExtraInfo(bundle);
		/**/
		//view.
		return ooA;
	}
	public void addInfosOverlay(List< CloudPrintAddress> infos,BaiduMap mBaiduMap)
	{
		BitmapDescriptor mIconMaker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions overlayOptions = null;
		Marker marker = null;
		for (CloudPrintAddress info : infos)
		{
			// 位置
			latLng = new LatLng(info.getLatitude(), info.getLongitude());
			// 图标
			overlayOptions = new MarkerOptions().position(latLng)
					.icon(mIconMaker).zIndex(5);
			marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", info);
			marker.setExtraInfo(bundle);
		}
		// 将地图移到到最后一个经纬度位置
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(u);
	}


}
