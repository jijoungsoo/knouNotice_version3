package kr.co.shinae.KnouNotice.robolectric;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowPreferenceManager;
import org.robolectric.util.ActivityController;

import java.lang.reflect.Method;

import kr.co.shinae.KnouNotice.Activity.Main;
import kr.co.shinae.KnouNotice.Activity.NoticeList;
import kr.co.shinae.KnouNotice.Activity.NoticeRead;
import kr.co.shinae.KnouNotice.BuildConfig;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
public class NoticeListOrmLiteTest {

    @Before
    public void test111(){
        System.out.println("test111");
        //ShadowLog.stream = System.out;   /*안드로이드 로그를 보려고..*/

        SharedPreferences sharedPreferences = RuntimeEnvironment.application.getSharedPreferences(Main.KNOU_PREF,0);
        sharedPreferences.edit().putString(Main.GUBUN,"DP").commit();
        sharedPreferences.edit().putString(Main.DEPARTMENT,"34").commit();
    }

//    @Test
    @Config(sdk = Build.VERSION_CODES.KITKAT)
    public void onCreate_shouldInflateTheMenu() throws Exception {
        System.out.println("onCreate_shouldInflateTheMenu");
        Activity activity = Robolectric.setupActivity(NoticeList.class);
    }
   //@Test
    public void setList() throws Exception {
        System.out.println("setList");
       // NoticeList activity = Robolectric.setupActivity(NoticeList.class);

        /*
        *setupActivity와 buildActivity의 차이점.
        *setupActivity
         * ==>     return ActivityController.of(shadowsAdapter, activityClass).setup().get();
         *buildActivity
         * ==>     return ActivityController.of(shadowsAdapter, activityClass);
        *
        * */
        ActivityController controller  = Robolectric.buildActivity(NoticeList.class).create().start();
        NoticeList activity =(NoticeList)controller.get();


        Method m= activity.getClass().getDeclaredMethod("setList");
        m.setAccessible(true);
        m.invoke(activity);
   }

   @Test
    public void setRead() throws Exception {
       ActivityController controller1  = Robolectric.buildActivity(NoticeList.class).create().start();
       NoticeList activity1 =(NoticeList)controller1.get();


       Method m1= activity1.getClass().getDeclaredMethod("setList");
       m1.setAccessible(true);
       m1.invoke(activity1);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(NoticeRead.anncNo, "57788");
        intent.putExtras(bundle);

        ActivityController controller  = Robolectric.buildActivity(NoticeRead.class).withIntent(intent).create().start();
        NoticeRead activity =(NoticeRead)controller.get();


        Method m= activity.getClass().getDeclaredMethod("setRead");
        m.setAccessible(true);
        m.invoke(activity);
    }

    @After
    public void test222(){
        System.out.println("test222");
    }


}

