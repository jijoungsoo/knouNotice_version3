package kr.co.shinae.KnouNotice.robolectric;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.GenericRawResults;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import kr.co.shinae.KnouNotice.Activity.Main;
import kr.co.shinae.KnouNotice.Activity.NoticeList;
import kr.co.shinae.KnouNotice.BuildConfig;
import kr.co.shinae.KnouNotice.provider.second.data.DatabaseHelper;
import kr.co.shinae.KnouNotice.second.KnouNoticeFileInfo;
import kr.co.shinae.KnouNotice.second.KnouNoticeInfo;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
public class OrmLiteTest {
    private DatabaseHelper databaseHelper = null;


        @Before
        public void ormLiteBefore(){
            System.out.println("ormLiteBefore");
        }

        @Test
        @Config(sdk = Build.VERSION_CODES.KITKAT)
        public void ormLiteTest1() throws Exception {
            System.out.println("ormLiteTest1");
            Dao<KnouNoticeInfo, Integer> knouNoticeInfoDao=this.getHelper().getKnouNoticeInfoDao();
            List<KnouNoticeInfo> knouNoticeInfoList=knouNoticeInfoDao.queryForAll();
            System.out.println(knouNoticeInfoList.size());
            KnouNoticeInfo test = new KnouNoticeInfo("0"
                    ,"교육"
                    ,"보통"
                    ,"튜터오프라인강의공지_대전지역대학_3학년_윤성림 튜터"
                    ,"246"
                    ,"1"
                    ,"1"
                    ,"2015-11-03 16:41:01"
                    ,"020403001"
                    ,"2015/11/02~2015/11/14"
                    ,"10"
                    ,"10"
                    ,"jhkim1101"
                    ,"29"
                    ,"jhkim1101"
                    ,"0"
                    ,"김지환"
                    ,"1"
                    ,"57804"
                    ,"61"
                    ,"X"
                    ,"컴퓨터과학과"
                    ,"2015-11-03 16:41:01"
                    );
           knouNoticeInfoDao.create(test);
           knouNoticeInfoList=knouNoticeInfoDao.queryForAll();
            org.junit.Assert.assertTrue("갯수가 1 보다 크거나 같다. ", knouNoticeInfoList.size() >= 1);
            //org.junit.Assert.assertTrue("갯수가 1 이 아니다 . ",knouNoticeInfoList.size()!=1);

            ForeignCollection<KnouNoticeFileInfo> attacheFiles =  knouNoticeInfoList.get(0).AttacheFiles;
            if(attacheFiles==null){
                attacheFiles= knouNoticeInfoDao.getEmptyForeignCollection("AttacheFiles");
            }
            KnouNoticeFileInfo  knouNoticeFileInfo= new KnouNoticeFileInfo();
            knouNoticeFileInfo._KNOU_NOTICE_ID=knouNoticeInfoList.get(0);
            knouNoticeFileInfo.href="asfdasdf";
            knouNoticeFileInfo.fileName="ddddd";

            attacheFiles.add(knouNoticeFileInfo);

            Dao<KnouNoticeFileInfo, Integer> knouNoticeFileInfoDao=this.getHelper().getKnouNoticeFileInfoDao();
            List<KnouNoticeFileInfo> tt = knouNoticeFileInfoDao.queryForAll();
            for(int i=0;i<tt.size();i++){
                System.out.println("tt.get("+i+")._ID:"+tt.get(i)._ID);
                System.out.println("tt.get("+i+").fileName:"+tt.get(i).fileName);
                System.out.println("tt.get("+i+").href:"+tt.get(i).href);
                System.out.println("tt.get("+i+")._KNOU_NOTICE_ID11:"+tt.get(i)._KNOU_NOTICE_ID._ID);

            }
            GenericRawResults<String[]> rawResults = knouNoticeFileInfoDao.queryRaw("select _ID,filename,href,_KNOU_NOTICE_ID from KnouNoticeFileInfo where _KNOU_NOTICE_ID = 1");
            // there should be 1 result

            List<String[]> results = rawResults.getResults();
            // the results array should have 1 value
            for(int i=0;i<results.size();i++){
                System.out.println("tt.get("+i+")._ID:"+results.get(i)[0]);
                System.out.println("tt.get("+i+").fileName:"+results.get(i)[1]);
                System.out.println("tt.get("+i+").href:"+results.get(i)[2]);
                System.out.println("tt.get("+i+")._KNOU_NOTICE_ID222:"+results.get(i)[3]);
            }
            // this should print the number of orders that have this account-id

        }

        @After
        public void ormLiteBeforeAfter(){
            System.out.println("ormLiteBeforeAfter");
        }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(RuntimeEnvironment.application.getApplicationContext(), DatabaseHelper.class);
        }
        return databaseHelper;
    }


}
