package kr.co.shinae.KnouNotice;

import android.app.Application;

public class KnouNoticeApp extends Application {
	
	public static String AdamClientID="18c8Z0PT1342cba929a";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		HelpF.Log.d(this, "onCreate");
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		HelpF.Log.d(this, "onTerminate");
	}
}
