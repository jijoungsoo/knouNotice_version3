package kr.co.shinae.KnouNotice.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import kr.co.shinae.KnouNotice.DepartmentInfo;
import kr.co.shinae.KnouNotice.HelpF;
import kr.co.shinae.KnouNotice.R;
import kr.co.shinae.KnouNotice.second.LocationInfo;
import kr.co.shinae.KnouNotice.service.NotiService;

public class Main extends Activity {
    @Bind(R.id.ivLogoKnou)
    ImageView mIvLogoKnou;
    @Bind(R.id.ivLogoHanwoorii)
    ImageView mIvLogoHanwoorii;
    @Bind(R.id.btnDP)
    Button mBtnDP;
    @Bind(R.id.btnLU)
    Button mBtnLU;
    @Bind(R.id.btnKA)
    Button mBtnVT;
    @Bind(R.id.btnInfo)
    Button mBtnInfo;
    @Bind(R.id.btnDownload)
    Button mBtnDownload;
    @Bind(R.id.btnAlarm)
    Button mBtnAlarm;
    @Bind(R.id.tvGonggi)
    TextView mTvGonggi;
    @Bind(R.id.spDepartment)
    Spinner mSpDepartment;
    @Bind(R.id.spLocation)
    Spinner mSpLocation;
    @Bind(R.id.tgbtnChange)
    ToggleButton mTgbtnChange;

    String mChangeState;
    SharedPreferences mPref;

    public static final String KNOU_PREF = "KnouPref";
    public static final String DEPARTMENT = "Department";
    public static final String LOCATION = "Location";
    public static final String GUBUN = "GUBUN";
    public static final String CHANGE = "CHANGE";

    public static ArrayList<DepartmentInfo> mAlDepartmentInfo = HelpF.Department();
    public static ArrayList<LocationInfo> mAlLocationInfo = HelpF.Location();

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @OnClick(R.id.ivLogoKnou)
    public void ivLogoKnouOnClick(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.knou.ac.kr"));
        startActivity(i);
    }

    @OnClick(R.id.ivLogoHanwoorii)
    public void ivLogoHanwooriiOnClick(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://wwww.hanwoorii.com/xe"));
        startActivity(i);
    }

    @OnClick(R.id.btnInfo)
    public void btnInfoOnClick(View v) {
        Intent i = new Intent(Main.this, Info.class);
        startActivity(i);
    }

    @OnClick(R.id.btnDownload)
    public void btnDownloadOnClick(View v) {
        Intent i = new Intent(Main.this, Download.class);
        startActivity(i);
    }

    @OnClick(R.id.btnDP)
    public void btnDPOnClick(View v) {
        Intent i = new Intent(Main.this, NoticeList.class);
        Editor editor = mPref.edit();
        HelpF.Log.d("HAN", "main mChangeState:" + mChangeState);
        editor.putString(CHANGE, mChangeState);
        editor.putString(GUBUN, "DP");
        editor.putString(DEPARTMENT,((DepartmentInfo) mSpDepartment.getSelectedItem()).no);
        editor.putString(LOCATION,((LocationInfo) mSpLocation.getSelectedItem()).no);
        editor.commit();
        startActivity(i);
    }

    @OnClick(R.id.btnLU)
    public void btnLUOnClick(View v) {
        Intent i = new Intent(Main.this, NoticeList.class);
        Editor editor = mPref.edit();
        editor.putString(CHANGE, mChangeState);
        editor.putString(GUBUN, "LU");
        editor.putString(DEPARTMENT,((DepartmentInfo) mSpDepartment.getSelectedItem()).no);
        editor.putString(LOCATION,((LocationInfo) mSpLocation.getSelectedItem()).no);
        editor.commit();
        startActivity(i);
    }

    @OnClick(R.id.btnKA)
    public void btnKAOnClick(View v) {
        Intent i = new Intent(Main.this, NoticeList.class);
        Editor editor = mPref.edit();
        editor.putString(CHANGE, mChangeState);
        editor.putString(GUBUN, "VT");
        editor.putString(DEPARTMENT, ((DepartmentInfo) mSpDepartment.getSelectedItem()).no);
        editor.putString(LOCATION, ((LocationInfo) mSpLocation.getSelectedItem()).no);
        editor.commit();
        startActivity(i);
    }

    @OnClick(R.id.btnAlarm)
    public void btnAlarmOnClick(View v) {
        Intent i = new Intent(Main.this, AlarmService.class);
        startActivity(i);
    }

    @OnCheckedChanged(R.id.tgbtnChange)
    public void tgbtnChangeOnCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        // TODO Auto-generated method stub
        if (isChecked == true) {
            mChangeState = "ONLINE";
            Editor editor = mPref.edit();
            editor.putString(CHANGE, mChangeState);
            editor.commit();
        } else {
            mChangeState = "OFFLINE";
            Editor editor = mPref.edit();
            editor.putString(CHANGE, mChangeState);
            editor.commit();

        }
    }

    @OnItemSelected(R.id.spDepartment)
    public void spDepartmentOnItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Editor editor = mPref.edit();
        editor.putString(DEPARTMENT, ((DepartmentInfo) mSpDepartment.getSelectedItem()).no);
        editor.commit();
    }

    @OnItemSelected(R.id.spLocation)
    public void spLocationOnItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Editor editor = mPref.edit();
        editor.putString(LOCATION, ((LocationInfo) mSpLocation.getSelectedItem()).no);
        editor.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(NotiService.NOTI_ID);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        mPref = getSharedPreferences(Main.KNOU_PREF, 0);
        mChangeState = mPref.getString(CHANGE, "ONLINE");
        if ("ONLINE".equals(mChangeState)) {
            mTgbtnChange.setChecked(true);
        } else {
            mTgbtnChange.setChecked(false);
        }
        mTvGonggi.setPaintFlags(mTvGonggi.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        settingDepartment();
        settingLocation();
    }

    private void settingDepartment() {
        class ViewHolder {
            TextView mTextView;
        }

        class CustomerAdapter extends ArrayAdapter<DepartmentInfo> {

            // public ColorAdapter(Context context,ColorData[] objects) {
            public CustomerAdapter(Context context, List arrayListDepartmentInfo) {
                // TODO Auto-generated constructor stub
                super(context, 0, arrayListDepartmentInfo);
            }

            @Override
            // adapter가 해주는 핵심역할 getView
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                View v;
                ViewHolder holder;
                if (convertView == null) {
                    v = inflater.inflate(R.layout.spinner_item, null);
                    holder = new ViewHolder();
                    holder.mTextView = (TextView) v
                            .findViewById(R.id.tvSubject);
                    v.setTag(holder);
                } else {
                    v = convertView;
                    holder = (ViewHolder) v.getTag();
                }
                DepartmentInfo departmentInfo = getItem(position);
                holder.mTextView.setText(departmentInfo.subject);
                return v;
            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                View v;
                ViewHolder holder;
                if (convertView == null) {
                    v = inflater.inflate(R.layout.spinner_dropdown_item, null);
                    holder = new ViewHolder();
                    holder.mTextView = (TextView) v
                            .findViewById(R.id.tvSubject);
                    v.setTag(holder);
                } else {
                    v = convertView;
                    holder = (ViewHolder) v.getTag();
                }
                DepartmentInfo departmentInfo = getItem(position);
                // Log.d("HAN", (String) feeling);
                holder.mTextView.setText(departmentInfo.subject);
                return v;
            }
        }
        CustomerAdapter adapter = new CustomerAdapter(Main.this,mAlDepartmentInfo);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpDepartment.setAdapter(adapter);
        String strDepartment = mPref.getString(Main.DEPARTMENT, " ");
        for (int i = 0; i < mAlDepartmentInfo.size(); i++) {
            if (mAlDepartmentInfo.get(i).no.equals(strDepartment))
                mSpDepartment.setSelection(i);
        }
    }

    private void settingLocation() {
        class ViewHolder {
            TextView mTextView;
        }
        class CustomerAdapter extends ArrayAdapter<LocationInfo> {
            public CustomerAdapter(Context context, List arrayListLocationInfo) {
                super(context, 0, arrayListLocationInfo);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                View v;
                ViewHolder holder;
                if (convertView == null) {
                    v = inflater.inflate(R.layout.spinner_item, null);
                    holder = new ViewHolder();
                    holder.mTextView = (TextView) v
                            .findViewById(R.id.tvSubject);
                    v.setTag(holder);
                } else {
                    v = convertView;
                    holder = (ViewHolder) v.getTag();
                }
                LocationInfo locationInfo = getItem(position);
                // Log.d("HAN", "@:" + feeling);
                holder.mTextView.setText(locationInfo.subject);
                return v;
            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                View v;
                ViewHolder holder;
                if (convertView == null) {
                    v = inflater.inflate(R.layout.spinner_dropdown_item, null);
                    holder = new ViewHolder();
                    holder.mTextView = (TextView) v
                            .findViewById(R.id.tvSubject);
                    v.setTag(holder);
                } else {
                    v = convertView;
                    holder = (ViewHolder) v.getTag();
                }
                LocationInfo locationInfo = getItem(position);
                holder.mTextView.setText(locationInfo.subject);
                return v;
            }
        }

        CustomerAdapter adapter = new CustomerAdapter(Main.this,mAlLocationInfo);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpLocation.setAdapter(adapter);

        String strLocation = mPref.getString(Main.LOCATION, " ");
        for (int i = 0; i < mAlLocationInfo.size(); i++) {
            if (mAlLocationInfo.get(i).no.equals(strLocation))
                mSpLocation.setSelection(i);
        }

    }
}