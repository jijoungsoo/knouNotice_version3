package kr.co.shinae.KnouNotice.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import kr.co.shinae.KnouNotice.DepartmentInfo;
import kr.co.shinae.KnouNotice.HelpF;
import kr.co.shinae.KnouNotice.R;
import kr.co.shinae.KnouNotice.provider.second.KnouNoticeDb.KNOU_NOTICE;
import kr.co.shinae.KnouNotice.provider.second.data.DatabaseHelper;
import kr.co.shinae.KnouNotice.second.KnouNoticeAbs;
import kr.co.shinae.KnouNotice.second.KnouNoticeInfo;
import kr.co.shinae.KnouNotice.second.KnouNoticeListInfo;
import kr.co.shinae.KnouNotice.second.LocationInfo;

public class NoticeList extends Activity {
    private final String LOG_TAG = getClass().getSimpleName();
    private static final int DIALOG2_KEY = 450;

    @Bind(R.id.lvKnouNotice)
    ListView mLvKnouNotice;
    @Bind(R.id.tvEmpty)
    TextView mTvEmpty;
    @Bind(R.id.linerLayoutKnouNoticePage)
    LinearLayout mLinerLayoutKnouNoticePage;
    @Bind(R.id.right_text)
    TextView tvRightText;

    CustomerArrayAdapter mCustomerArrayAdapter;
    KnouNoticeListInfo mKnouNoticeListInfo = new KnouNoticeListInfo();
    KnouNoticeAbs mKnouNoticeAbs;
    SharedPreferences mPref;

    KnouNoticeInfo mSelectedKnouNoticeInfo = null;
    public static int mSelectediread = 0;
    private DatabaseHelper databaseHelper = null;


    String mIntCurPage = "1";
    String mChangeState;
    String mGubun;
    int mNum = 0;
    int mSelectedFirstVisiblePosition = 0;

    @OnItemClick(R.id.lvKnouNotice)
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        KnouNoticeInfo knouNoticeInfo = (KnouNoticeInfo) parent.getItemAtPosition(position);
        int iRead = 0;
        try {
            iRead = Integer.parseInt(knouNoticeInfo.iread);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (iRead <= 0) {
            if (mChangeState.equals("OFFLINE")) {
                Toast.makeText(NoticeList.this, "읽어본적이 없는 게시물은 오프라인모드에서 확인하실 수 없습니다", Toast.LENGTH_SHORT).show();
                return;
            }

        }

        mSelectedFirstVisiblePosition= mLvKnouNotice.getFirstVisiblePosition();
        mSelectedKnouNoticeInfo = knouNoticeInfo;
        Intent i = new Intent(NoticeList.this, NoticeRead.class);
        i.putExtra(NoticeRead.anncNo, knouNoticeInfo.anncNo);
        i.putExtra(NoticeRead.NUM, knouNoticeInfo.ruid);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");

        String title = "";
        mPref = getSharedPreferences(Main.KNOU_PREF, 0);
        mChangeState = mPref.getString(Main.CHANGE, "ONLINE");
        mGubun = mPref.getString(Main.GUBUN, "DP");
        HelpF.Log.d("HAN", "GOGO mGubun:" + mGubun);
        if (mGubun.equals("DP")) {
            mKnouNoticeAbs = new KnouNoticeAbs(this);
            String strDepartment = mPref.getString(Main.DEPARTMENT, "");
            HelpF.Log.d("HAN", "GOGO knouNoticeInfo.memo:" + strDepartment);
            DepartmentInfo departmentInfo = HelpF.getDepartment(strDepartment);
            title = "학과공지[" + departmentInfo.subject + "]";
        } else if (mGubun.equals("LU")) {
            mKnouNoticeAbs = new KnouNoticeAbs(this);
            String strLOCATION = mPref.getString(Main.LOCATION, "");
            LocationInfo locationInfo = HelpF.getLocation(strLOCATION);
            title = "지역대학[" + locationInfo.subject + "]";
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
        setContentView(R.layout.notice_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
        ButterKnife.bind(this);
        tvRightText.setText(title);
        new LoadDataTask().execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        HelpF.Log.d("HAN", "onRestart:");
        new LoadDataTask().execute();


    }

    public List<KnouNoticeInfo> getListKnouNoticeDb() throws SQLException {
        String blngDc = mPref.getString(Main.GUBUN, "DP");
        String blngCd = "";

        if (blngDc.equals("DP")) {
            blngCd = mPref.getString(Main.DEPARTMENT, "");
        } else if (blngDc.equals("LU")) {
            blngCd = mPref.getString(Main.LOCATION, "");
        } else if (blngDc.equals("VT")) {
            blngCd = "";
        }

        Dao<KnouNoticeInfo, Long> knouNoticeInfoDao = this.getHelper().getKnouNoticeInfoDao();
        QueryBuilder<KnouNoticeInfo, Long> qb = knouNoticeInfoDao.queryBuilder();
        Where where = qb.where();
        where.eq(KNOU_NOTICE.blngDc, blngDc);
        where.and();
        where.eq(KNOU_NOTICE.blngCd, blngCd);
        List<KnouNoticeInfo> knouNoticeInfoList = qb.query();
        System.out.println("qb:"+qb.prepareStatementString());
        return knouNoticeInfoList;
    }

    class ViewHolder {
        public TextView mTvNum;
        public TextView mTvSubject;
        public TextView mTvDepartment;
        public TextView mTvDate;
        public TextView mTvHit;
        public TextView mTvbMemo;
    }

    class CustomerArrayAdapter extends ArrayAdapter<KnouNoticeInfo> {
        public CustomerArrayAdapter(Context context, int textViewResourceId, List<KnouNoticeInfo> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.notice_list_item, null);
                holder = new ViewHolder();
                holder.mTvNum = (TextView) v.findViewById(R.id.tvNum);
                holder.mTvSubject = (TextView) v.findViewById(R.id.tvSubject);
                holder.mTvDepartment = (TextView) v
                        .findViewById(R.id.tvDepartment);
                holder.mTvDate = (TextView) v.findViewById(R.id.tvDate);
                holder.mTvHit = (TextView) v.findViewById(R.id.tvHit);
                holder.mTvbMemo = (TextView) v.findViewById(R.id.tvbmemo);
                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }
            KnouNoticeInfo knouNoticeInfo = getItem(position);
            System.out.println("position:" + position);
            int iRead = 0;
            try {
                iRead = Integer.parseInt(knouNoticeInfo.iread);
            } catch (Exception e) {
                e.printStackTrace();
                knouNoticeInfo.iread="0";
                iRead =0;
            }

            if (iRead == 0) {
                v.setBackgroundColor(Color.parseColor("#ffffff"));
            } else if (iRead == 1) {
                v.setBackgroundColor(Color.parseColor("#f0f18f"));
            } else if (iRead == 2) {
                v.setBackgroundColor(Color.parseColor("#f5d7d7"));
            } else {
                v.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            if (knouNoticeInfo.memo != null) {
                if (!knouNoticeInfo.memo.trim().equals("")) {
                    holder.mTvbMemo.setVisibility(View.VISIBLE);
                } else {
                    holder.mTvbMemo.setVisibility(View.GONE);
                }
            } else {
                holder.mTvbMemo.setVisibility(View.GONE);
            }
            holder.mTvNum.setText(Html.fromHtml("<font color='#008652'>[번호]</font>" + (mNum - position)));
            holder.mTvDepartment.setText(Html.fromHtml("<font color='#008652'>[부서]</font>" + knouNoticeInfo.regDpNm));
            holder.mTvDate.setText(Html.fromHtml("<font color='#008652'>[작성일]</font>" + knouNoticeInfo.regDttm.substring(0, 10)));
            holder.mTvHit.setText(Html.fromHtml("<font color='#008652'>[조회]</font>" + knouNoticeInfo.inqT));
            holder.mTvSubject.setText(Html.fromHtml("<font color='#008652'>[제목]</font>" + knouNoticeInfo.tit));
            return v;
        }
    }

    private void setList() throws Exception {
        try {
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
            mKnouNoticeListInfo = mKnouNoticeAbs.getList(mIntCurPage, blngDc, blngCd);
        } catch (IOException e) {
            HelpF.Log.d("HAN", "IOException:" + e.getMessage());
            e.printStackTrace();
        } catch (URISyntaxException e) {
            HelpF.Log.d("HAN", "URISyntaxException:" + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            HelpF.Log.d("HAN", "JSONException:" + e.getMessage());
            e.printStackTrace();
        }
        Dao<KnouNoticeInfo, Long> knouNoticeInfoDao = this.getHelper().getKnouNoticeInfoDao();
        System.out.println("mKnouNoticeListInfo.al.size():" + mKnouNoticeListInfo.al.size());
        for (int i = 0; i < mKnouNoticeListInfo.al.size(); i++) {
            KnouNoticeInfo knouNoticeInfo = mKnouNoticeListInfo.al.get(i);
            QueryBuilder<KnouNoticeInfo, Long> qb = knouNoticeInfoDao.queryBuilder();
            Where where = qb.where();
            where.eq(KNOU_NOTICE.blngDc, mKnouNoticeListInfo.epo.blngDc);
            where.and();
            where.eq(KNOU_NOTICE.blngCd, mKnouNoticeListInfo.epo.blngCd);
            where.and();
            where.eq(KNOU_NOTICE.anncNo, knouNoticeInfo.anncNo.trim());
            List<KnouNoticeInfo> knouNoticeInfoList = qb.query();

            if (knouNoticeInfoList.size() > 1) {
                throw new Exception("1이상 나오면 안된다구요 ");
            }
            if (knouNoticeInfoList.size() > 0) {
                knouNoticeInfo.blngDc = mKnouNoticeListInfo.epo.blngDc;
                knouNoticeInfo.blngCd = mKnouNoticeListInfo.epo.blngCd;
                knouNoticeInfo._ID = knouNoticeInfoList.get(0)._ID;
                knouNoticeInfo.iread = knouNoticeInfoList.get(0).iread;
                if (knouNoticeInfo.iread == null) {
                    knouNoticeInfo.iread = "0";
                }
                knouNoticeInfoDao.update(knouNoticeInfo);
            } else {
                knouNoticeInfo.blngDc = mKnouNoticeListInfo.epo.blngDc;
                knouNoticeInfo.blngCd = mKnouNoticeListInfo.epo.blngCd;
                knouNoticeInfo.iread = "0";
                knouNoticeInfo.memo = "";
                knouNoticeInfoDao.create(knouNoticeInfo);
            }
        }
        mNum = Integer.parseInt(mKnouNoticeListInfo.epo.records) - (((int) Long.parseLong(mKnouNoticeListInfo.epo.page) - 1) * Integer.parseInt(mKnouNoticeListInfo.epo.rows));
        System.out.println("mKnouNoticeListInfo.al :"+mKnouNoticeListInfo.al .size());
/*
        QueryBuilder<KnouNoticeInfo, Long> qb = knouNoticeInfoDao.queryBuilder();
        Where where = qb.where();
        where.eq(KNOU_NOTICE.blngDc, mKnouNoticeListInfo.epo.blngDc);
        where.and();
        where.eq(KNOU_NOTICE.blngCd, mKnouNoticeListInfo.epo.blngCd);
        List<KnouNoticeInfo> knouNoticeInfoList = qb.query();
        mAl = new ArrayList<KnouNoticeInfo>(knouNoticeInfoList);

        */
    }

    private void setListOff() {
        mKnouNoticeListInfo = new KnouNoticeListInfo();
        List<KnouNoticeInfo> tmp = new ArrayList<KnouNoticeInfo>();
        try {
            tmp = getListKnouNoticeDb();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        mNum = tmp.size();
        mKnouNoticeListInfo.al = (ArrayList) tmp;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG2_KEY: {
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Loading..");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                return dialog;
            }
        }
        return null;
    }

    private class LoadDataTask extends AsyncTask<Integer, Integer, Long> {
        public LoadDataTask() {
        }
        protected Long doInBackground(Integer... urls) {
            HelpF.Log.d("HAN", "mChangeState:" + mChangeState);
            if (mChangeState.equals("ONLINE")) {
                try {
                    setList();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                setListOff();
            }
            return 0L;
        }

        @Override
        protected void onPreExecute() {
            showDialog(DIALOG2_KEY);
        }

        protected void onPostExecute(Long result) {
            mCustomerArrayAdapter = new CustomerArrayAdapter(NoticeList.this, 0, mKnouNoticeListInfo.al);
            if (mKnouNoticeListInfo.al.size() > 0) {
                mTvEmpty.setVisibility(View.GONE);
                mLvKnouNotice.setVisibility(View.VISIBLE);
            } else {
                mTvEmpty.setVisibility(View.VISIBLE);
                mLvKnouNotice.setVisibility(View.GONE);
            }
            mLvKnouNotice.setAdapter(mCustomerArrayAdapter);
            System.out.println("mSelectedFirstVisiblePosition:" + mSelectedFirstVisiblePosition);
//            mLvKnouNotice.requestFocusFromTouch();
            mLvKnouNotice.setSelectionFromTop(mSelectedFirstVisiblePosition,0);
//            mCustomerArrayAdapter.notifyDataSetChanged();
            mLinerLayoutKnouNoticePage.removeAllViews();
            if (mChangeState.equals("ONLINE")) {
                mLinerLayoutKnouNoticePage.setVisibility(View.VISIBLE);

                int total = (int) Float.parseFloat(mKnouNoticeListInfo.total);

                int page = Integer.parseInt(mKnouNoticeListInfo.page);
                if (page > 1) {
                    Button btn = new Button(NoticeList.this);
                    btn.setText("<");
                    btn.setTag(page - 1);
                    btn.setEllipsize(TruncateAt.START);
                    btn.setGravity(Gravity.CENTER);
                    btn.setLayoutParams(new ViewGroup.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                    btn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            int param = (Integer) v.getTag();
                            Toast.makeText(NoticeList.this, param + "번페이지로 이동",
                                    Toast.LENGTH_SHORT).show();
                            mIntCurPage = String.valueOf(param);
                            new LoadDataTask().execute();
                        }
                    });
                    mLinerLayoutKnouNoticePage.addView(btn);
                }

                int startNum = 1;
                for (int i = startNum; i <= total; i++) {
                    Button btn = new Button(NoticeList.this);
                    if (page == i) {
                        btn.setTextColor(Color.parseColor("#ff0000"));
                        btn.setText(Html.fromHtml("<b>" + i + "</b>"));
                    } else {
                        btn.setTextColor(Color.parseColor("#000000"));
                        btn.setText(String.valueOf(i));
                    }
                    btn.setTag(i);
                    btn.setEllipsize(TruncateAt.START);
                    btn.setGravity(Gravity.CENTER);
                    btn.setLayoutParams(new ViewGroup.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));

                    btn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int param = (Integer) v.getTag();
                            Toast.makeText(NoticeList.this, param + "번페이지로 이동", Toast.LENGTH_SHORT).show();
                            mIntCurPage = String.valueOf(param);
                            new LoadDataTask().execute();
                        }
                    });
                    mLinerLayoutKnouNoticePage.addView(btn);
                }
                if (page < total) {
                    Button btn = new Button(NoticeList.this);
                    btn.setText(">");
                    btn.setTag(page + 1);
                    btn.setEllipsize(TruncateAt.START);
                    btn.setGravity(Gravity.CENTER);
                    btn.setLayoutParams(new ViewGroup.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                    btn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            int param = (Integer) v.getTag();
                            Toast.makeText(NoticeList.this, param + "번페이지로 이동",
                                    Toast.LENGTH_SHORT).show();
                            mIntCurPage = String.valueOf(param);
                            new LoadDataTask().execute();
                        }
                    });
                    mLinerLayoutKnouNoticePage.addView(btn);
                }
            } else {
                mLinerLayoutKnouNoticePage.setVisibility(View.GONE);
            }
            dismissDialog(DIALOG2_KEY);
            findViewById(R.id.notice_list).setVisibility(View.VISIBLE);
        }
    }


}
