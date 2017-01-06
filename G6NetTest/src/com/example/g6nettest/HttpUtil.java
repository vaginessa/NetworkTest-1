package com.example.g6nettest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class HttpUtil {
	
	public static final String uri = "http://121.40.98.54:8080/mis/mobile.mobileuser.getmobileuserbyusernamefromserver/global";
	
	public static void requestInfo(){
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getUserInfo();
				super.run();
			}
			
		}.start();
	}
	private static void getUserInfo() {
		//Log.d(TAG,"getUserInfo uri:"+uri);
		Util.saveToFile("		getUserInfo--start request:" + Util.formatTime(System.currentTimeMillis()) + "\n");
		HttpPost request = new HttpPost(uri);
		request.addHeader("CompanyCode","ShuGuo");
		JSONObject param = new JSONObject();
		try {
			param.put("userName", "147258369011117");
			//Log.d(TAG,"getUserInfo  param.toString():"+param.toString());
			StringEntity se = new StringEntity(param.toString());
			request.setEntity(se);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(Util.TAG, "getUserInfo--请求异常：" + e.getMessage());
			Util.saveToFile("getUserInfo--error-error--请求异常：" + e.getMessage() + "\n");
			Util.addSaveToFile();
		}
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(request);
			String retSrc = EntityUtils.toString(httpResponse.getEntity());
			if (TextUtils.isEmpty(retSrc)) {
				Log.d(Util.TAG, "getUserInfo 返回值为空");
				Util.saveToFile("getUserInfo--error-error--返回值为空" + "\n");
				Util.addSaveToFile();
				Util.saveToFile("		getUserInfo--end request:" + Util.formatTime(System.currentTimeMillis()) + "\n");
				return;
			}
			JSONObject resultJson = new JSONObject(retSrc);
			String resultStr = resultJson.getString("result");
			Log.d(Util.TAG, "getUserInfo----返回值:" + resultStr);
			long time = Math.abs(System.currentTimeMillis() - Long.parseLong(Util.getPreferences("current_wakeup")));
			if("1".equals(resultStr)){
				Log.d(Util.TAG, "getUserInfo--pre--request time = " + time);
				if(time > 12*1000){
					Log.d(Util.TAG, "getUserInfo--error-error--request time = " + time);
					Util.saveToFile("getUserInfo--error-error--request time = " + time + "\n" +
					"resultStr = " + resultStr + "\n");
					Util.addSaveToFile();
					Util.saveToFile("		getUserInfo--end request:" + Util.formatTime(System.currentTimeMillis()) + "\n");
				}else{
					Log.d(Util.TAG, "getUserInfo--success-success--request time = " + time);
					Log.d(Util.TAG, "getUserInfo--success network state = " + Util.isMobileEnabled());
					Log.d(Util.TAG, "getUserInfo--successNetworkType = " + Util.GetNetworkType());
					Util.saveToFile("success time = " + Util.formatTime(System.currentTimeMillis()) + 
							"  req-res = " + time + 
							"  NetworkType = " + Util.GetNetworkType() + 
							"  strength = " + Util.strength + "\n");
					Util.saveToFile("		getUserInfo--end request:" + Util.formatTime(System.currentTimeMillis()) + "\n");
				}
			}else{
				Log.d(Util.TAG, "getUserInfo--result error-error--request time = " + time);
				Util.saveToFile("getUserInfo--result error-error--request time = " + time + "\n" +
						"resultStr = " + resultStr + "\n");
				Util.addSaveToFile();
				Util.saveToFile("		getUserInfo--end request:" + Util.formatTime(System.currentTimeMillis()) + "\n");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			long time = Math.abs(System.currentTimeMillis() - Long.parseLong(Util.getPreferences("current_wakeup")));
			Log.d(Util.TAG, "getUserInfo--接收异常：ClientProtocolException current time = " + time);
			Util.saveToFile("getUserInfo--接收异常：ClientProtocolException current time = " + time + "\n");
			Util.addSaveToFile();
			Util.saveToFile("		getUserInfo--end request:" + Util.formatTime(System.currentTimeMillis()) + "\n");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			long time = Math.abs(System.currentTimeMillis() - Long.parseLong(Util.getPreferences("current_wakeup")));
			Log.d(Util.TAG, "getUserInfo--接收异常：IOException current time = " + time);
			Util.saveToFile("getUserInfo--接收异常：IOException current time = " + time + "\n");
			Util.addSaveToFile();
			Util.saveToFile("		getUserInfo--end request:" + Util.formatTime(System.currentTimeMillis()) + "\n");
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			long time = Math.abs(System.currentTimeMillis() - Long.parseLong(Util.getPreferences("current_wakeup")));
			Log.d(Util.TAG, "getUserInfo--接收异常：JSONException current time = " + time);
			Util.saveToFile("getUserInfo--接收异常：JSONException current time = " + time + "\n");
			Util.addSaveToFile();
			Util.saveToFile("		getUserInfo--end request:" + Util.formatTime(System.currentTimeMillis()) + "\n");
			e.printStackTrace();
		}
	}
	
	
	
}
