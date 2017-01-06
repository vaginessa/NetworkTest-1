package com.example.g6nettest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class Util {
	public static final String TAG = "G6NetTest_TAG"; 
	public static final String BASE_INFO_DIR = Environment.getExternalStorageDirectory()+"/G6BaseInfo";
	public static final String BASE_INFO_FILE_NAME = "/G6NetTest.txt";
	private static Context mContext = G6Application.getInApplication();
	private static PhoneStateListener listener = null;
	public static int strength = 0;;
	public static String formatTime(long time){
		SimpleDateFormat ormatter = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");       
		Date curDate = new Date(time);//获取当前时间       
		String str = ormatter.format(curDate); 
		//Log.i(TAG,"formatTime str = " + str);
		return str;
	}
	
	public static void savePreferences(String key,String value){
		Log.i(TAG,"savePreferences mContext = " + mContext);
		SharedPreferences sp = mContext.getSharedPreferences("G6Help",Context.MODE_PRIVATE);
		sp.edit()
		.putString(key,value)
		.commit();
		Log.i(TAG,"savePreferences key = " + key + ",value = " + value);
	}
	public static String getPreferences(String key){
		SharedPreferences sp = mContext.getSharedPreferences("G6Help",Context.MODE_PRIVATE);
		String value = sp.getString(key,"");
		Log.i(TAG,"getPreferences key = " + key + ",value = " + value);
		if(TextUtils.isEmpty(value)){
			return "0";
		}
		return value;
	}
	
	public static void addSaveToFile(){
		String conent = "currentTimeMillis = " + formatTime(System.currentTimeMillis()) + "\n" +
				"current_wakeup = " + formatTime(Long.parseLong(Util.getPreferences("current_wakeup"))) + "\n" +
				"next_wakeup = " + formatTime(Long.parseLong(Util.getPreferences("next_wakeup"))) + "\n" +
				"NetworkType = " + GetNetworkType() + "\n" +
				"isMobileEnabled = " + isMobileEnabled() + "\n" +
				"mobile isAvailable = " + isMobileConnected() + "\n" +
				"Phone number = " + getLocalNumber() + "\n" +
				"strength = " + strength + "\n" +
				"\n" + 
				"\n";
		saveToFile(conent);
	}
	
	public static boolean isMobileEnabled() {
		ConnectivityManager cm =
		        (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		Log.i(TAG,"isMobileEnabled enable = " + isConnected);
		Log.i(TAG,"isMobileConnected isAvailable = " + isMobileConnected());
	    return isConnected;
	}
	
	public static boolean isMobileConnected() { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext 
				.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mMobileNetworkInfo = mConnectivityManager 
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
		if (mMobileNetworkInfo != null) { 
			return mMobileNetworkInfo.isAvailable(); 
		} 
		return false; 
	}
	
	/* 
     * 获取当前的手机号 
     */ 
    public static String getLocalNumber() { 
            TelephonyManager tManager = (TelephonyManager) mContext 
                            .getSystemService(Context.TELEPHONY_SERVICE); 
            String number = tManager.getLine1Number(); 
            return number; 
    }
	
	
	/** 
     * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true 
     *  
     * @param fileName 
     * @param content 
     */  
    public static void saveToFile(String conent) { 
    	File f = new File(BASE_INFO_DIR);
    	if(!f.exists()){
    		f.mkdirs();
    	}
    	f.setWritable(true);
        BufferedWriter out = null;  
        try {  
            out = new BufferedWriter(new OutputStreamWriter(  
                    new FileOutputStream(BASE_INFO_DIR + BASE_INFO_FILE_NAME, true)));  
            out.write(conent);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                out.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        fileScan(BASE_INFO_DIR + BASE_INFO_FILE_NAME);
    }  
    public static void fileScan(String fName){   
        Uri data = Uri. parse("file:///" +fName);   
        mContext.sendBroadcast( new  Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE , data));   
    }   
    
    
    public static String GetNetworkType()
    {
        String strNetworkType = "";
        
        NetworkInfo networkInfo = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                strNetworkType = "WIFI";
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String _strSubTypeName = networkInfo.getSubtypeName();
                
                Log.e(Util.TAG, "Network getSubtypeName : " + _strSubTypeName);
                
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) 
                        {
                            strNetworkType = "3G";
                        }
                        else
                        {
                            strNetworkType = _strSubTypeName;
                        }
                        
                        break;
                 }
                 
                Log.e(Util.TAG, "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }
        
        Log.e(Util.TAG, "Network Type : " + strNetworkType);
        
        return strNetworkType;
    }
    
    public static void regPhoneSignalStrength(){
    	if(listener == null){
    		listener = new PhoneStateListener(){

				@Override
				public void onSignalStrengthsChanged (
						SignalStrength signalStrength) {
					// TODO Auto-generated method stub
					strength = signalStrength.getGsmSignalStrength();
					Log.i(Util.TAG, "onSignalStrengthsChanged strength : " + strength);
					super.onSignalStrengthsChanged(signalStrength);
				}

    			
    		};  
    		TelephonyManager tel = ( TelephonyManager )mContext.getSystemService(Context.TELEPHONY_SERVICE);  
    		tel.listen(listener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);  
    	}
    }
}
