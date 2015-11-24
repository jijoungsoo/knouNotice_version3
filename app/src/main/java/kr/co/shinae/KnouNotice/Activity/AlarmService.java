package kr.co.shinae.KnouNotice.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import kr.co.shinae.KnouNotice.HelpF;
import kr.co.shinae.KnouNotice.R;
import kr.co.shinae.KnouNotice.service.NotiService;

public class AlarmService extends Activity {
    final static String KEY_HH = "KEY_HH";
    final static String KEY_MM = "KEY_MM";
    static final int TIME_DIALOG_ID = 0;
    final static String KEY_ALARM = "KEY_ALARM";
    SharedPreferences mPref;
    private PendingIntent mAlarmSender;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;

    private TextView mDateDisplay;
    boolean mbAlarm = false;

    @Bind(R.id.right_text)
    TextView tvRightText;
    @Bind(R.id.tvTimeDisplay)
    TextView mTvTimeDisplay;
    @Bind(R.id.btnAlarm)
    ToggleButton mBtnAlarm;
    @Bind(R.id.btnClose)
    Button mBtnClose;
    @Bind(R.id.btnPickTime)
    Button mBtnPickTime;

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    @OnClick(R.id.btnPickTime)
    public void btnPickTimeOnClick(View v) {
        showDialog(TIME_DIALOG_ID);
    }

    @OnClick(R.id.btnClose)
    public void btnCloseOnClick(View arg0) {
        finish();
    }


    @OnCheckedChanged(R.id.btnAlarm)
    public void btnAlarmOnCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
        // TODO Auto-generated method stub
        if (isChecked) {
            Calendar c0 = Calendar.getInstance();
            // 현재시간
            // Date dm = new Date();
            // long tm = dm.getTime();
            // Log.d("HAN","TT11:"+sdformat.format(tm));

            // 선택된시간
            Calendar c1 = Calendar.getInstance();
            // Log.d("HAN","T1:"+timePicker.getCurrentHour());
            // Log.d("HAN","T2:"+timePicker.getCurrentMinute());

            c1.set(mYear, mMonth, mDay, mHour,
                    mMinute, 0);
            // Log.d("HAN","TT2:"+sdformat.format(c1.getTimeInMillis()));

            long tmp = c1.getTimeInMillis() - c0.getTimeInMillis();

            // Log.d("HAN","TT31:"+tmp);
            // Log.d("HAN","TT3:"+sdformatmmss.format(tmp));

            // Schedule the alarm!
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.cancel(mAlarmSender);
            // 1000*60*60* 24 하루
            // am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime+tmp,
            // 1000*60*60* 24, mAlarmSender);
            //Toast.makeText(AlarmService.this,"tmp:"+ tmp , Toast.LENGTH_SHORT).show();
            if (tmp < 0) {
                tmp = tmp + 1000 * 60 * 60 * 24;
            }
            //long laterTime = 1000 * 60 * 60 * 24;
            long laterTime = 1000 * 60 * 60 * 24;  //24시간 뒤에.
            //tmp =1000*10;  언제부터

            HelpF.Log.d("SERVICE", "tmp" + tmp);
            HelpF.Log.d("SERVICE", "laterTime" + laterTime);
            am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + (tmp),laterTime, mAlarmSender);

            // Tell the user about what we did.
            // Toast.makeText(AlarmService.this,
            // "고고",Toast.LENGTH_LONG).show();

            // 저장하는 방법:START
            //Toast.makeText(AlarmService.this,"on:"+ mHour +":"+mMinute, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor edit = mPref.edit();
            edit.putInt(KEY_HH, mHour);
            edit.putInt(KEY_MM, mMinute);
            edit.putBoolean(KEY_ALARM, true);
            edit.commit();
            // 저장하는 방법:END
        } else {
            // 저장하는 방법:START
            //Toast.makeText(AlarmService.this, "off:"+mHour +":"+mMinute, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor edit = mPref.edit();
            edit.putInt(KEY_HH, mHour);
            edit.putInt(KEY_MM, mMinute);
            edit.putBoolean(KEY_ALARM, false);
            edit.commit();
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.cancel(mAlarmSender);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.alarm_service);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
        ButterKnife.bind(this);
        tvRightText.setText("새 게시물 확인 주기");

        mAlarmSender = PendingIntent.getService(AlarmService.this, 0, new Intent(AlarmService.this, NotiService.class), 0);
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mPref = getSharedPreferences("AlarmPref", 0);
        mHour = mPref.getInt(KEY_HH, mHour);
        mMinute = mPref.getInt(KEY_MM, mMinute);
        mbAlarm = mPref.getBoolean(KEY_ALARM, false);
        if (mbAlarm) {
            mBtnAlarm.setChecked(true);
        } else {
            mBtnAlarm.setChecked(false);
        }
        updateDisplay();
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            updateDisplay();
            SharedPreferences.Editor edit = mPref.edit();
            edit.putInt(KEY_HH, mHour);
            edit.putInt(KEY_MM, mMinute);
            edit.putBoolean(KEY_ALARM, false);
            edit.commit();
            mBtnAlarm.setChecked(false);
        }
    };

    private void updateDisplay() {
        mTvTimeDisplay.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(HelpF.pad(mHour)).append(":")
                .append(HelpF.pad(mMinute)));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,false);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case TIME_DIALOG_ID:
                ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
                break;
        }
    }
}