package kr.co.shinae.KnouNotice.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.ButterKnife;
import kr.co.shinae.KnouNotice.R;

public class Info extends Activity {
    @Bind(R.id.right_text)
    TextView tvRightText;
    @Bind(R.id.btnClose)
    Button mBtnClose;
    @Bind(R.id.tvInfo)
    TextView mTvInfo;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btnClose)
    public void btnCloseOnClick(View v) {
        // TODO Auto-generated method stub
        finish();
    }

    private String getInfoTextFromFile() throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream is = getResources().openRawResource(R.raw.info);
        java.io.InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new java.io.InputStreamReader(is, "utf8");
            br = new BufferedReader(isr);
            String tmp = null;
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp + "\n");
            }
        } catch (Exception ex) {
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
            }
            try {
                isr.close();
            } catch (IOException ex) {
            }
        }
        is.close();
        return sb.toString();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.info);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
        ButterKnife.bind(this);

        tvRightText.setText("만든이");
        try {
            String infoText = getInfoTextFromFile();
            mTvInfo.setText(infoText);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Linkify.addLinks(mTvInfo, Linkify.ALL);
    }
}