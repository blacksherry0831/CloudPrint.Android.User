package com.jit.cloud_print_cc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayDemoActivity;
import com.alipay.sdk.pay.demo.PayResult;

public class AliPay {
	public static boolean isDebug = false;
	View_CloudPrintTemplate _vt;
	//商户PID
	public static final String PARTNER = "2088121908846331";
	// 商户收款账号
	public static final String SELLER = "15651687339@163.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOV+TvGvfKsHRDQKXZnUkbS4H2j/LAVIUw6iqmsF0e8lBajjsywDqgCPMUkdCDPWazumAzbI4sinXo2Iucx/Z62Rf+ch81iCM3Q7zKqaa2gVPma/R9QdFlz78F+n7EGtnPQXTaCfZRcpn5KoA5VY9R9TxD9+UVadU7cJwOky0Bo1AgMBAAECgYEAoWUlC9/Io1cm5hpsHWVbW2cp3+OlB8oHb4GCrGWZcL/urQoF1ex1wJLzrGGYhSxEmdx04jCBHXBnlM4VLPZk6F0IyC7nCV1xeyEU7DL3w2/2FjErnGszRAx1hjuCloBTCjL/oW7u46J4ZNO6S2uMEKIWYsMBlT4frx6m29Mg57kCQQD7Ji+L9oNBc54VdHPfX5hO1fIOaMYs3L/1r84Uac1NxO3uZzZHINs8FfAHItBZEgapoOcICeejj+R8iBObHJnbAkEA6e0LiT06ihWU1q8N84MJ/0xfRwdZFZAqWdBx2BFj4k6VlX/pvHnxBoVcwQgN/A61RjLTjMABhfOa7bOBTMIBLwJBANxkYx8Y4ZADTLuZKMHhmr+74aGhch8WTOHmOBsTyZUwdndaXXhHrfvpaGxqsZkoR25+A5+7SWnwMNrTcxkQHTcCQQCAB6IhueY5PziYC3VqStUE6qrW+DmUqLPVNlWouVPev7309e5anq8BL6qlZ6AnzXD/e7/3L/tlcf/gizeAaEo3AkAjfCqdTYjSpa2zpW+h96Td2OQib/0Y0pybXu61ntZpEcrtTI2fEx2mFVtSLrdZCJcYg2VUkFiZ44YG42vURlPM";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	public static final int SDK_PAY_FLAG = 1;
	public static final int SDK_CHECK_FLAG = 2;
	
	public static final String _Subject="亲打印Android";
	
	public AliPay(View_CloudPrintTemplate vt){
		this._vt=vt;
	}
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void payit(String subject, String body, String price,String ProductId) {
				payitOrder(null,subject,body,price,ProductId);
	}

	public void payitOrder(final UserInfoOrder uio, String subject, String body, String price,String ProductId) {
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
			new AlertDialog.Builder(_vt.getContext()).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							_vt.finish();
						}
					}).show();
			return;
		}
		//String orderInfo = PayDemoActivity.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
		//String orderInfo = PayDemoActivity.getOrderInfoQin("测试的商品", "该测试商品的详细描述","0.01",ProductId);
		
		if(isDebug){
			price="0.01";
		}
		
		String orderInfo = PayDemoActivity.getOrderInfoQin(subject, body, price,ProductId);
		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = PayDemoActivity.sign(orderInfo);
		try {
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + PayDemoActivity.getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				  Looper.prepare();
					// 构造PayTask 对象
					PayTask alipay = new PayTask((Activity) _vt.getContext());
					// 调用支付接口，获取支付结果
					String result = alipay.pay(payInfo, true);
					/*-----------------------------------------------------------------------*/
					PayResult payResult = new PayResult(result);
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息
					String resultStatus = payResult.getResultStatus();
					if (TextUtils.equals(resultStatus, "9000")) {
	 					//支付成功
						if(uio!=null){
							uio.setStatus(UserInfoOrder.STATUS_PRINTED_PENDING);//设置挂起
						}
	 				} else {
	 					
	 				}
					/*----------------------------------------------------------------------*/
					Message msg = new Message();
					msg.what = SDK_PAY_FLAG;
					msg.obj = result;
					_vt.Handler().sendMessage(msg);
				   Looper.loop();
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}


	public void Payit(UserInfoOrder uio)
	{
		
		this.payitOrder(uio,
				uio.GetAliPaySubject(),
				uio.GetAlipayBody(),
				uio.GetAlipayPrice(),
				uio.GetAlipayProductId());
		
	}
}
