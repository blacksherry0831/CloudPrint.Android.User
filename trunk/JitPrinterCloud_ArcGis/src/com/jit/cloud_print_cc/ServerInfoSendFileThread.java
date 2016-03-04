package com.jit.cloud_print_cc;

import java.util.ArrayList;

import android.content.Context;

import com.jit.cloud_print_cc.FileSendOnNet.NowSendPercent;
import com.jit.cloud_print_cc.ServerInfoSendStatus.SendFilePackage;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferDone;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferStart;
import com.jit.cloud_print_cc.ServerInfoSendStatus.TransferendEndOne;

public class ServerInfoSendFileThread implements Runnable {

	    private SendFilePackage sfPackage=new SendFilePackage();
	
		private int mFileIdx=0;
		private Context mContext;
		private ArrayList<ServerInfo> mSelectedServer=new ArrayList<ServerInfo>();
		boolean mThreadRun=true;
		ArrayList<String> mDataFile=new ArrayList<String>();
		TransferStart start;
		TransferendEndOne end;
		TransferDone done;
		private  NowSendPercent mNowSendPercent=null;
		public ServerInfoSendFileThread(
				Context context,
				ArrayList<ServerInfo> SelectedServer,
				ArrayList<String> file,
				TransferStart start,
				TransferendEndOne   end,
				TransferDone done,
				NowSendPercent NowSendPercent)
		{
			if(context!=null){
				this.mContext=context.getApplicationContext();
			}
			  if(file!=null&&SelectedServer!=null){
					for(String s:file){
						this.mDataFile.add(new String(s));
					}
					
					for(ServerInfo si:SelectedServer){
						this.mSelectedServer.add(si.Clone());
					}
			  }		
			this.start=start;
			this.end=end;
			this.done=done;
			this.mNowSendPercent=NowSendPercent;
		}
		@Override
		public void run()
		{
	
			if(this.IsParamsValid()==false) return;
////////////////////////////////
			start.Start(mContext);			
			////////////////////////////////
			for(int i=0;i<mSelectedServer.size();i++){
				for(mFileIdx=0;mFileIdx<mDataFile.size();mFileIdx++){
					
				if(mThreadRun){
						final ServerInfo si=mSelectedServer.get(i);
						   si.SetNowSendPercent(mNowSendPercent);
						   si.SendFileOrDirectory(new FilesWithParams(mDataFile.get(mFileIdx),mContext));
						   while (!si.IsSendFileComplete());
						   String SendDes ="主机："+  si.GetHostDes() +"文件："+ mDataFile.get(mFileIdx);
						     if (si.mFileSendSuccess) {
	                             sfPackage.SendSuccess.add(SendDes);
	                         }
	                         else if (si.mFileSendAbort){
	                             sfPackage.SendAbort.add(SendDes);
	                         }
	                         else if (si.mFileSendEsc){
	                             sfPackage.SendEsc.add(SendDes);
	                         }
						 
							end.EndOne(this.mContext, si,1.0*((i+1)*(mFileIdx+1))/(mSelectedServer.size()*mDataFile.size()));

					}
					
				}
			}
			///////////////////////////////////////
			
			done.Done(mContext,sfPackage);

		}
		public boolean IsParamsValid()
		{
			if(this.mContext==null||this.mDataFile==null||this.mSelectedServer==null){
				return false;
			}else{
				return true;
			}
			
		}

}
