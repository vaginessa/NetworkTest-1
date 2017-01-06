package com.example.g6nettest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class G6NetReceiver extends BroadcastReceiver {
	public static final String REQUEST_HTTP_ACTION = "G6NetTest.request.http.action";
	public final long NEXT_DELAY = 15*1000;
	@Override
	public void onReceive(Context ctx, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(Util.TAG, "G6NetReceiver action = " + intent.getAction());
		if(REQUEST_HTTP_ACTION.equals(intent.getAction())){
			setAlarm(ctx);
			debug(ctx);
			Util.regPhoneSignalStrength();
			HttpUtil.requestInfo();
		}else if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
			Util.saveToFile("本次为开机启动，启动后每15秒会向服务器发送请求，然后等待服务器回应，在12秒内有回应则输出success日志，" +
					"如果没有回应或者网络异常等则输出异常日志：\n本次启动时间：" + Util.formatTime(System.currentTimeMillis()) + "\n");
			setAlarm(ctx);
			debug(ctx);
			HttpUtil.requestInfo();
		} else if("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())){
			Util.saveToFile("网络发生变化：networkType = " + Util.GetNetworkType() + 
					"  Network enable：" + Util.isMobileEnabled() + 
					"  时间：" + Util.formatTime(System.currentTimeMillis()) + "\n"+"\n");
		}
	}
	private void debug(Context ctx) {
		// TODO Auto-generated method stub
		Util.savePreferences("current_wakeup", System.currentTimeMillis()+"");
		long time = System.currentTimeMillis() - Long.parseLong(Util.getPreferences("next_wakeup"));
		time = Math.abs(time);
		Log.i(Util.TAG, "debug time = " + time);
		if(time >= NEXT_DELAY + 1000 || time <= NEXT_DELAY -3000){
			Log.i(Util.TAG, "debug alarm weakup error-error time - NEXT_DELAY = " + (time - NEXT_DELAY));
			Util.saveToFile("debug alarm weakup error-error time - NEXT_DELAY = " + time + "\n");
			Util.addSaveToFile();
		}else{
			Log.i(Util.TAG, "debug alarm weakup success-success time - NEXT_DELAY = " + (time - NEXT_DELAY));
		}
		
	}
	private void setAlarm(Context ctx) {
		// TODO Auto-generated method stub
		AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);  
        Intent intent = new Intent(REQUEST_HTTP_ACTION);  
        long atTimeInMillis = System.currentTimeMillis() + NEXT_DELAY;  
        PendingIntent sender = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);  
        am.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);  
        Log.i(Util.TAG, "G6NetReceiver setAlarm = " + Util.formatTime(atTimeInMillis));
        Util.savePreferences("next_wakeup", atTimeInMillis+"");
	}
	
}
