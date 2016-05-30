package com.jit.mail;


import com.jit.*;
import java.io.File;
import java.io.FileFilter;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.jit.cloud_print_cc.CrashHandler;
import com.jit.cloud_print_cc.LibCui;
import com.jit.cloud_print_orders.OrderLocal;

/**
* @ClassName: Sendmail
* @Description: 发送Email
* @author: 孤傲苍狼
* @date: 2015-1-12 下午9:42:56
*
*/ 
public class SendMail {

	public void SendDebugInfo()
	{
		 File f=LibCui.GetCloudPrintCfgFile();
		 
		 if(!f.isDirectory()) return;
		 if(!f.exists()) return;
		 
		 File[] f_crash=f.listFiles();
		 
		 for(File fci :f_crash){			 
					 try{
						  if(fci.getName().endsWith(CrashHandler.CRASH_REPORTER_EXTENSION)){
							  SendTxt(OrderLocal.txt2String(fci));
							  fci.delete();
						  }
					 }catch(Exception e){
						 
					 }			 
		 }	
	}  
	
	public static void SendTxt(String txt) throws Exception
	{
		 Properties prop = new Properties();
	        prop.setProperty("mail.host", "smtp.sohu.com");
	        prop.setProperty("mail.transport.protocol", "smtp");
	        prop.setProperty("mail.smtp.auth", "true");
	        //使用JavaMail发送邮件的5个步骤
	        //1、创建session
	        Session session = Session.getInstance(prop);
	        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
	        session.setDebug(true);
	        //2、通过session得到transport对象
	        Transport ts = session.getTransport();
	        //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
	        ts.connect("smtp.sohu.com", "cui14310531@sohu.com", "Cui14310531");
	        //4、创建邮件
	        Message message = createSimpleMail(session,txt);
	        //5、发送邮件
	        ts.sendMessage(message, message.getAllRecipients());
	        ts.close();
	}
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.sohu.com");
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //2、通过session得到transport对象
        Transport ts = session.getTransport();
        //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
        ts.connect("smtp.sohu.com", "gacl", "邮箱密码");
        //4、创建邮件
        Message message = createSimpleMail(session,"test");
        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }
    
    /**
    * @Method: createSimpleMail
    * @Description: 创建一封只包含文本的邮件
    * @Anthor:孤傲苍狼
    *
    * @param session
    * @return
    * @throws Exception
    */ 
    public static MimeMessage createSimpleMail(Session session,Object msg)
            throws Exception {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress("cui14310531@sohu.com"));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("neko_win_pc_dbg@qq.com"));
        //邮件的标题
        message.setSubject("Android云打印DEBUG");
        //邮件的文本内容
        message.setContent(msg, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }
}