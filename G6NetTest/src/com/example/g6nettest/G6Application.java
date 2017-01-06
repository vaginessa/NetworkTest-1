package com.example.g6nettest;

import android.app.Application;

public class G6Application extends Application {
	public static Application instance;
	public static Application getInApplication(){
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		instance = this;
		super.onCreate();
	}
}
