package kr.co.shinae.KnouNotice.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import kr.co.shinae.KnouNotice.DepartmentInfo;
import kr.co.shinae.KnouNotice.HelpF;
import kr.co.shinae.KnouNotice.R;
import kr.co.shinae.KnouNotice.provider.second.KnouNoticeDb.KNOU_NOTICE;
import kr.co.shinae.KnouNotice.provider.second.data.DatabaseHelper;
import kr.co.shinae.KnouNotice.second.KnouNoticeAbs;
import kr.co.shinae.KnouNotice.second.KnouNoticeFileInfo;
import kr.co.shinae.KnouNotice.second.KnouNoticeInfo;
import kr.co.shinae.KnouNotice.second.LocationInfo;

public class NoticeRead extends Activity {
    @Bind(R.id.right_text)
    TextView tvRightText;
    @Bind(R.id.tvSubject)
    TextView mTvSubject;
    @Bind(R.id.wvContent)
    WebView mWvContent;
    @Bind(R.id.tvDepartment)
    TextView mTvDepartment;
    @Bind(R.id.tvDate)
    TextView mTvDate;
    @Bind(R.id.tvHit)
    TextView mTvHit;
    @Bind(R.id.tgbtnCollection)
    ToggleButton mTgbtnCollection;
    @Bind(R.id.btnFileView)
    Button mBtnFileView;
    @Bind(R.id.btnMemo)
    Button mBtnMemo;
    @Bind(R.id.btnClose)
    Button mBtnClose;

    private static final int DIALOG1_KEY = 400;
    private static final int DIALOG2_KEY = 450;
    private static final int DIALOG3_KEY = 500;
    private static final int DIALOG4_KEY = 550;
    private static final int DIALOG5_KEY = 600;

    String mGubun;
    String mBrdno;

    CustomerArrayAdapter mCustomerArrayAdapter;
    private DatabaseHelper databaseHelper = null;
    ListView mLvFile;
    int mIread;

    SharedPreferences mPref;
    KnouNoticeAbs mKnouNoticeAbs;
    KnouNoticeInfo mKnouNoticeInfo;
    public static final String anncNo = "anncNo";
    public static final String NUM = "num";

    final String mimeType = "text/html";
    final String encoding = "utf-8";
    String mAnncNo;
    String mNum = "0";
    String mChangeState;
    String mMemo = "";


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @OnClick({R.id.btnFileView, R.id.btnMemo, R.id.btnClose})
    public void noticeReadOnClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btnFileView:
                showDialog(DIALOG2_KEY);
                break;
            case R.id.btnMemo:
                showDialog(DIALOG4_KEY);
                break;
            case R.id.btnClose:
                finish();
                break;
            default:
        }
    }

    @OnCheckedChanged(R.id.tgbtnCollection)
    public void mTgbtnCollectionOnCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked == true) {
            mKnouNoticeInfo.iread="2";
        } else {
            mKnouNoticeInfo.iread="1";
        }
        NoticeList.mSelectediread = mIread;
        setChangeKnouNoticeInfo(mKnouNoticeInfo);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        Intent intent = getIntent();
        mAnncNo = intent.getStringExtra(anncNo);
        mNum = intent.getStringExtra(NUM);

        String title = "";
        mPref = getSharedPreferences(Main.KNOU_PREF, 0);
        mChangeState = mPref.getString(Main.CHANGE, "ONLINE");
        mGubun = mPref.getString(Main.GUBUN, "DP");
        mBrdno = "0";
        if (mGubun.equals("DP")) {
            mKnouNoticeAbs = new KnouNoticeAbs(this);
            String strDepartment = mPref.getString(Main.DEPARTMENT, "");
            DepartmentInfo departmentInfo = HelpF
                    .getDepartment(strDepartment);
            title = "학과공지[" + departmentInfo.subject + "]";
            mBrdno = String.valueOf(departmentInfo.no);
        } else if (mGubun.equals("LU")) {
            mKnouNoticeAbs = new KnouNoticeAbs(this);
            String strLOCATION = mPref.getString(Main.LOCATION, "");
            LocationInfo locationInfo = HelpF
                    .getLocation(strLOCATION);
            title = "지역대학[" + locationInfo.subject + "]";
            mBrdno = String.valueOf(locationInfo.no);
        } else if (mGubun.equals("VT")) {
            mKnouNoticeAbs = new KnouNoticeAbs(this);
            title = "학교공지";
        }

        if (mChangeState.equals("OFFLINE")) {
            title = "(offline)" + title;
        } else {
            title = "(online)" + title;
        }

        if (HelpF.Wifi3gConnectivity(this) == false && !(mChangeState.equals("OFFLINE"))) {
            HelpF.SSMessage(this, "네트워크 연결 상태를 확인해 주세요.");
            this.finish();
            return;
        }

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.notice_read);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
        ButterKnife.bind(this);
        tvRightText.setText(title);


        mWvContent.getSettings().setJavaScriptEnabled(true);
        mWvContent.getSettings().setSupportZoom(true);
        mWvContent.getSettings().setBuiltInZoomControls(true);
        mWvContent.getSettings().setUseWideViewPort(true);

        final Context myApp = this;
        mWvContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(myApp)
                        .setTitle("AlertDialog")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }
        });

        new DataLoadTask().execute(0);
    }

    private KnouNoticeInfo setChangeKnouNoticeInfo(KnouNoticeInfo knouNoticeInfo)  {
        try {
            Dao<KnouNoticeInfo, Long> knouNoticeInfoDao = this.getHelper().getKnouNoticeInfoDao();
            if(knouNoticeInfo==null) {
                throw new Exception("null 이 나오면 안된다구요 ");
            }
            int result =knouNoticeInfoDao.update(knouNoticeInfo);
            System.out.println("setChangeKnouNoticeInfo:"+result);
            knouNoticeInfo=knouNoticeInfoDao.queryForId(knouNoticeInfo._ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return knouNoticeInfo;
    }
    private KnouNoticeInfo setChangeKnouNoticeFileInfo(KnouNoticeInfo knouNoticeInfo)  {
        try {
            Dao<KnouNoticeInfo, Long> knouNoticeInfoDao = this.getHelper().getKnouNoticeInfoDao();
            ForeignCollection<KnouNoticeFileInfo> attacheFiles = knouNoticeInfo.AttacheFiles;
            if (attacheFiles == null) {
                attacheFiles = knouNoticeInfoDao.getEmptyForeignCollection("AttacheFiles");
            }
            attacheFiles.clear();
            for (int i = 0; i < knouNoticeInfo.AttacheFileArrayList.size(); i++) {
                KnouNoticeFileInfo knouNoticeFileInfo = knouNoticeInfo.AttacheFileArrayList.get(i);
                knouNoticeFileInfo._KNOU_NOTICE_ID = knouNoticeInfo;
                attacheFiles.add(knouNoticeFileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return knouNoticeInfo;
    }


    private List<KnouNoticeInfo> getListKnouNoticeInfo(String blngDc,String blngCd,String anncNo) {
        List<KnouNoticeInfo> knouNoticeInfoList = null;
        try {
            Dao<KnouNoticeInfo, Long> knouNoticeInfoDao = this.getHelper().getKnouNoticeInfoDao();
            QueryBuilder<KnouNoticeInfo, Long> qb = knouNoticeInfoDao.queryBuilder();
            Where where = qb.where();
            where.eq(KNOU_NOTICE.blngDc, blngDc);
            where.and();
            where.eq(KNOU_NOTICE.blngCd,blngCd );
            where.and();
            where.eq(KNOU_NOTICE.anncNo, anncNo);
            System.out.println("sql:" + qb.prepareStatementString());
            knouNoticeInfoList = qb.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return knouNoticeInfoList;
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        // TODO Auto-generated method stub
        switch (id) {
            case DIALOG1_KEY: {
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Loading..");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                return dialog;
            }
            case DIALOG2_KEY: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("첨부파일 목록");
                View v = getLayoutInflater().inflate(R.layout.dialog_file_list, null);
                mLvFile = (ListView) v.findViewById(R.id.lvFile);
                mLvFile.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        KnouNoticeFileInfo attacheFileInfo = (KnouNoticeFileInfo) parent.getItemAtPosition(position);
                        dismissDialog(DIALOG2_KEY);
                        Bundle bundle = new Bundle();
                        bundle.putString("FILESRC", attacheFileInfo.href);
                        bundle.putString("FILENAME", attacheFileInfo.fileName);
                        showDialog(DIALOG5_KEY, bundle);
                    }
                });
                mLvFile.setAdapter(mCustomerArrayAdapter);
                builder.setView(v);
                builder.setNegativeButton("닫기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismissDialog(DIALOG2_KEY);
                            }
                        });
                return builder.create();
            }

            case DIALOG3_KEY: {
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("File.. Download");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                return dialog;
            }

            case DIALOG4_KEY: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("메모");
                final EditText etMemo = new EditText(this);
                etMemo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                etMemo.setText(mKnouNoticeInfo.memo);
                etMemo.setGravity(Gravity.TOP);
                etMemo.setLines(10);
                builder.setView(etMemo);
                builder.setPositiveButton("저장",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mKnouNoticeInfo.memo=etMemo.getText().toString();
                                setChangeKnouNoticeInfo(mKnouNoticeInfo);
                                if (mKnouNoticeInfo.memo != null) {
                                    if (!mKnouNoticeInfo.memo.trim().equals("")) {
                                        mBtnMemo.setText("메모(O)");
                                    } else {
                                        mBtnMemo.setText("메모(X)");
                                    }
                                } else {
                                    mBtnMemo.setText("메모(X)");
                                }
                            }

                        });
                builder.setNegativeButton("닫기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismissDialog(DIALOG4_KEY);
                            }

                        });
                return builder.create();
            }

            case DIALOG5_KEY: {
                final String fileSrc = args.getString("FILESRC");
                final String fileName = args.getString("FILENAME");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("파일 다운로드");
                File fileHome = HelpF.getHomeFileLoc();
                final File file = new File(fileHome.getAbsolutePath(), fileName);
                boolean bExistsFile = file.isFile();
                String message = "";
                if (!bExistsFile) {
                    builder.setMessage("파일을 다운로드 하시겠습니까?");
                    message = "다운로드";
                } else {
                    builder.setMessage("파일이 존재합니다. 다운로드 하시겠습니까?");
                    message = "덮어쓰기";
                }

                builder.setPositiveButton(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (HelpF.Wifi3gConnectivity(NoticeRead.this) == false) {
                                    HelpF.SSMessage(NoticeRead.this, "네트워크 연결 상태를 확인해 주세요.");
                                    return;
                                }
                                new FileDownLoadTask(fileSrc, fileName).execute(0);
                            }
                        });

                if (bExistsFile) {
                    builder.setNeutralButton("열기",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (true) {
                                        HelpF.goIntentFileView(
                                                NoticeRead.this,
                                                file.getAbsolutePath());
                                    }
                                }
                            });
                }
                builder.setNegativeButton("닫기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismissDialog(DIALOG5_KEY);
                            }
                        });
                return builder.create();
            }
        }
        return null;
    }

    class ViewHolder {
        public TextView mTvFileName;
    }

    class CustomerArrayAdapter extends ArrayAdapter<KnouNoticeFileInfo> {

        public CustomerArrayAdapter(Context context, int textViewResourceId, List<KnouNoticeFileInfo> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.dialog_file_list_item, null);
                holder = new ViewHolder();
                holder.mTvFileName = (TextView) v.findViewById(R.id.tvFileName);
                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }
            KnouNoticeFileInfo attacheFileInfo = getItem(position);
            holder.mTvFileName.setText(attacheFileInfo.fileName);
            return v;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private void setRead() throws Exception {
        System.out.println("setRead1");
        String blngDc = mPref.getString(Main.GUBUN, "DP");
        String blngCd = mPref.getString(Main.DEPARTMENT, "34");
        if (blngDc.equals("DP")) {
            blngCd = mPref.getString(Main.DEPARTMENT, "34");
        } else if (blngDc.equals("LU")) {
            blngCd = mPref.getString(Main.LOCATION, "010");
        } else if (blngDc.equals("VT")) {
            blngCd = "";
        } else {
            blngCd = "";
        }
        System.out.println("blngCd:" + blngCd);
        System.out.println("anncNo:" + mAnncNo.trim());
        mKnouNoticeInfo = mKnouNoticeAbs.getInfo(mAnncNo, blngDc, blngCd);
        mKnouNoticeInfo.blngDc = mPref.getString(Main.GUBUN, "");
        if (mKnouNoticeInfo.blngDc.equals("DP")) {
            mKnouNoticeInfo.blngCd = mPref.getString(Main.DEPARTMENT, "");
        } else if (mKnouNoticeInfo.blngDc.equals("LU")) {
            mKnouNoticeInfo.blngCd = mPref.getString(Main.LOCATION, "");
        } else if (mKnouNoticeInfo.blngDc.equals("VT")) {
            mKnouNoticeInfo.blngCd = "";
        }

        List<KnouNoticeInfo> knouNoticeInfoList =getListKnouNoticeInfo(mKnouNoticeInfo.blngDc, mKnouNoticeInfo.blngCd, mAnncNo.trim());
        int totCount = knouNoticeInfoList.size();
        /* 저장 */
        if (knouNoticeInfoList.size() != 1) {
            throw new Exception("1만 나와야 한다구요 knouNoticeInfoList.size()  : "+knouNoticeInfoList.size() );
        }

        System.out.println("knouNoticeInfoList.size():" + knouNoticeInfoList.size());
        if (knouNoticeInfoList.size() > 0) {

            System.out.println("knouNoticeInfoList.get(0):" + knouNoticeInfoList.get(0)._ID.toString());
            KnouNoticeInfo knouNoticeInfo = knouNoticeInfoList.get(0);
            knouNoticeInfo.tit = mKnouNoticeInfo.tit;
            knouNoticeInfo.content = mKnouNoticeInfo.content;
            if(knouNoticeInfo.iread==null || knouNoticeInfo.iread.equals("0")) {
                knouNoticeInfo.iread = "1";
            }
            mIread= Integer.parseInt(knouNoticeInfo.iread);
            knouNoticeInfo=setChangeKnouNoticeInfo(knouNoticeInfo);
            knouNoticeInfo.AttacheFileArrayList=mKnouNoticeInfo.AttacheFileArrayList;
            mKnouNoticeInfo=setChangeKnouNoticeFileInfo(knouNoticeInfo);
        } else {
        }
        NoticeList.mSelectediread = mIread;
    }

    private void setReadOff() throws Exception {
        mKnouNoticeInfo = new KnouNoticeInfo();
        mKnouNoticeInfo.blngDc = mPref.getString(Main.GUBUN, "");
        if (mKnouNoticeInfo.blngDc.equals("DP")) {
            mKnouNoticeInfo.blngCd = mPref.getString(Main.DEPARTMENT, "");
        } else if (mKnouNoticeInfo.blngDc.equals("LU")) {
            mKnouNoticeInfo.blngCd = mPref.getString(Main.LOCATION, "");
        } else if (mKnouNoticeInfo.blngDc.equals("VT")) {
            mKnouNoticeInfo.blngCd = "";
        }
        List<KnouNoticeInfo> knouNoticeInfoList =getListKnouNoticeInfo(mKnouNoticeInfo.blngDc, mKnouNoticeInfo.blngCd, mAnncNo.trim());
        int totCount = knouNoticeInfoList.size();
        if (knouNoticeInfoList.size() != 1) {
            throw new Exception("1만 나와야 한다고 ");
        }

        if (knouNoticeInfoList.size() > 0) {
            mKnouNoticeInfo = knouNoticeInfoList.get(0);
        } else {
        }
    }

    private class DataLoadTask extends AsyncTask<Integer, Integer, Long> {
        public DataLoadTask() {
        }

        protected Long doInBackground(Integer... urls) {
            if (mChangeState.equals("ONLINE")) {
                try {
                    setRead();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    setReadOff();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            return 0L;
        }

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG1_KEY);
        }

        protected void onPostExecute(Long result) {
            dismissDialog(DIALOG1_KEY);

            mTvDepartment.setText(Html.fromHtml("<font color='#008652'>[부서]</font>" + mKnouNoticeInfo.regDpNm));
            mTvDate.setText(Html.fromHtml("<font color='#008652'>[작성일]</font>" + mKnouNoticeInfo.regDttm.substring(0, 10)));
            mTvHit.setText(Html.fromHtml("<font color='#008652'>[조회]</font>" + mKnouNoticeInfo.inqT));
            mTvSubject.setText(Html.fromHtml("<font color='#008" + "652'>[제목]</font>" + mKnouNoticeInfo.tit));
            if (mWvContent != null) {
                FrameLayout mContentView = (FrameLayout) getWindow().getDecorView().findViewById(android.R.id.content);
                HelpF.Log.d("HAN", "mKnouNoticeInfo.content:" + mKnouNoticeInfo.content);
                System.out.println("mKnouNoticeInfo.content:" + mKnouNoticeInfo.content);
                mWvContent.loadDataWithBaseURL(null, mKnouNoticeInfo.content.toString(), mimeType, encoding, null);
            }
            mCustomerArrayAdapter = new CustomerArrayAdapter(NoticeRead.this, 0, new ArrayList<KnouNoticeFileInfo>(mKnouNoticeInfo.AttacheFiles));
            if (0 < mKnouNoticeInfo.AttacheFiles.size()) {
                mBtnFileView.setVisibility(View.VISIBLE);
            }

            if (mIread == 1) {
                mTgbtnCollection.setChecked(false);
            } else if (mIread == 2) {
                mTgbtnCollection.setChecked(true);
            }

            if (mMemo != null) {
                if (!mMemo.trim().equals("")) {
                    mBtnMemo.setText("메모(O)");
                } else {
                    mBtnMemo.setText("메모(X)");
                }
            } else {
                mBtnMemo.setText("메모(X)");
            }
            findViewById(R.id.notice_read).setVisibility(View.VISIBLE);
        }
    }

    private class FileDownLoadTask extends AsyncTask<Integer, Integer, Long> {
        String mFileSrc;
        String mFileName;
        File mFile = null;

        public FileDownLoadTask(String fileSrc, String fileName) {
            mFileSrc = fileSrc;
            mFileName = fileName;
        }

        protected Long doInBackground(Integer... urls) {
            mFile = HelpF.fileDownload(mFileSrc, mFileName);
            return 0L;
        }

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG3_KEY);
        }

        protected void onPostExecute(Long result) {
            dismissDialog(DIALOG3_KEY);
            if (true) {
                HelpF.goIntentFileView(NoticeRead.this, mFile.getAbsolutePath());
            }
        }
    }
}
