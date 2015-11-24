package kr.co.shinae.KnouNotice.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.shinae.KnouNotice.Activity.Main;
import kr.co.shinae.KnouNotice.HelpF;
import kr.co.shinae.KnouNotice.R;
import kr.co.shinae.KnouNotice.provider.second.KnouNoticeDb.KNOU_NOTICE;
import kr.co.shinae.KnouNotice.provider.second.data.DatabaseHelper;
import kr.co.shinae.KnouNotice.second.KnouNoticeAbs;
import kr.co.shinae.KnouNotice.second.KnouNoticeInfo;
import kr.co.shinae.KnouNotice.second.KnouNoticeListInfo;


public class NotiService extends Service {
    SharedPreferences mPref;
    private DatabaseHelper databaseHelper = null;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mPref = getSharedPreferences(Main.KNOU_PREF, 0);
        // UI THREAD
        HelpF.Log.d("HAN", "CREATE SERVICE");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        // UI THREAD 서비스도 다 UI 쓰레드이다. 그러므로 긴작업은 AnsycTast를 이용하여 처리한다.
        super.onStart(intent, startId);
        HelpF.Log.d("TA", "START SERVICE");

        // 인터넷이 연결 되어어있지 않으면 false여야함
        //sendNotification();
        new LoadDataTask().execute();
        // Activity의 finish와 같은 역할을 하는것 종료
        // stopSelf();
        try {
            // Thread.sleep(100000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public static final int NOTI_ID = 1;

    void sendNotification() throws Exception {
        HelpF.Log.d("TA", "sendNotification");
        String message="";
        String blngCd = "";


        // "DP"
        blngCd = mPref.getString(Main.DEPARTMENT, "34");
        int tmpDP = setList("1", "DP", blngCd);
        if (tmpDP > 0) {
            message = "학과:" + tmpDP + " ";
        }
        // "LU"
        blngCd = mPref.getString(Main.LOCATION, "010");
        int tmpLU = setList("1", "LU", blngCd);
        if (tmpLU > 0) {
            message += "지역대학:" + tmpLU + " ";
        }

        // "KA"
        blngCd = "";
        int tmpKA = setList("1", "VT", blngCd);
        if (tmpKA > 0) {
            message += "학교:" + tmpKA;
        }
        HelpF.Log.d("TA", "tmpKA="+tmpKA);
        HelpF.Log.d("TA", "tmpLU="+tmpLU);
        HelpF.Log.d("TA", "tmpDP="+tmpDP);
        if (tmpKA > 0 || tmpLU > 0 || tmpDP > 0) {
            Editor editor = mPref.edit();
            editor.putString(Main.CHANGE, "ONLINE");
            editor.commit();
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // 첫번째 아이콘, 두번째 TEXT 메시지, 시간(이 알림대상이 알림을 준시간 - 트위터가 업데이트 된 시간 이벤트
            // 시간)
            // 두번째 인자인 TICKER TEXT 는 처음 알림이 발생했을때 잠깐 뜨는것이다.
            Notification noti = new Notification(R.drawable.knou_icon,
                    "새 공지사항이 있습니다.", System.currentTimeMillis()); // 알림정보 객체
            Intent intent = new Intent(this, Main.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    intent, 0);
            // noti.vibrate= new long[] {100,10,100,10};
            noti.setLatestEventInfo(this, "새 공지사항이 있습니다.", message, contentIntent);
            // NOTI_ID 는 현재 어플리케이션에서 유일한 키를 주면 된다.
            nm.notify(NOTI_ID, noti);
            // nm.cancel(NOTI_ID); 이동되는 액티비티에서 이것을 실행시켜서 nm을 지움
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private int setList(String page, String blngDc, String blngCd)  throws SQLException {
        KnouNoticeListInfo knouNoticeListInfo = null;
        KnouNoticeAbs knouNoticeAbs = new KnouNoticeAbs(this);
        try {
            knouNoticeListInfo = knouNoticeAbs.getList(page, blngDc, blngCd);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int newCount = 0;
        Dao<KnouNoticeInfo, Long> knouNoticeInfoDao = this.getHelper().getKnouNoticeInfoDao();
        for (int i = 0; i < knouNoticeListInfo.al.size(); i++) {
            KnouNoticeInfo knouNoticeInfo = knouNoticeListInfo.al.get(i);
            QueryBuilder<KnouNoticeInfo, Long> qb = knouNoticeInfoDao.queryBuilder();
            Where where = qb.where();
            where.eq(KNOU_NOTICE.blngDc, blngDc);
            where.and();
            where.eq(KNOU_NOTICE.blngCd, blngCd);
            where.and();
            where.eq(KNOU_NOTICE.anncNo, knouNoticeInfo.anncNo.trim());
            List<KnouNoticeInfo> knouNoticeInfoList = qb.query();
            System.out.println("qb:" + qb.prepareStatementString());

            if(knouNoticeInfoList.size()>0){
                KnouNoticeInfo tt = knouNoticeInfoList.get(0);
                knouNoticeInfo._ID=tt._ID;
                knouNoticeInfo.memo=tt.memo;
                knouNoticeInfo.iread=tt.iread;
                knouNoticeInfoDao.update(knouNoticeInfo);
            } else {
                // insert
                knouNoticeInfo.iread = "0";
                knouNoticeInfo.memo = "";
                knouNoticeInfoDao.create(knouNoticeInfo);
                newCount++;
            }
        }
        HelpF.Log.d("TA", "newCount="+newCount);
        return newCount;
    }

    private class LoadDataTask extends AsyncTask<Integer, Integer, Long> {
        public LoadDataTask() {
        }

        protected Long doInBackground(Integer... urls) {
            try {
                sendNotification();
            } catch (Exception e){
                e.printStackTrace();
            }
            return 0L;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
        }

        protected void onPostExecute(Long result) {

        }
    }


}