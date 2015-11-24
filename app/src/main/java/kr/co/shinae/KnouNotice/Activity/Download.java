package kr.co.shinae.KnouNotice.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.shinae.KnouNotice.HelpF;
import kr.co.shinae.KnouNotice.R;

public class Download extends Activity {

    private static final int DIALOG1_KEY = 440;
    private static final int DIALOG2_KEY = 450;
    private static final int DIALOG3_KEY = 460;
    private static final int DIALOG4_KEY = 470;
    private static final String DELETE = "DELETE";
    File mSelFile;
    static int FILE_ITEM = 1;
    @Bind(R.id.lvFile)
    ListView mLvBackup = null;
    @Bind(R.id.tvEmpty)
    TextView mTvEmpty = null;
    @Bind(R.id.btnClose)
    Button mBtnClose;
    @Bind(R.id.right_text)
    TextView tvRightText;

    @OnClick(R.id.btnClose)
    public void btnCloseOnClick(View arg0) {
        // TODO Auto-generated method stub
        finish();
    }

    private void setList() {
        File sdpath = HelpF.getHomeFileLoc();
        File[] fileList = sdpath.listFiles();
        alf.clear();
        for (int i = 0; i < fileList.length; i++) {
            File item = fileList[i];
            alf.add(item);
        }

        Collections.sort(alf);
        cal.notifyDataSetChanged();

        if (alf.size() > 0) {
            mLvBackup.setVisibility(View.VISIBLE);
            mTvEmpty.setVisibility(View.GONE);
        } else {
            mLvBackup.setVisibility(View.GONE);
            mTvEmpty.setVisibility(View.VISIBLE);
        }
    }

    ArrayList<File> alf = new ArrayList<File>();
    CursorArrayAdapter cal;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.file_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
        ButterKnife.bind(this);
        tvRightText.setText("첨부파일보기");

        cal = new CursorArrayAdapter(this, 0, alf);
        mLvBackup.setAdapter(cal);
        setList();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
        mSelFile = (File) v.getTag();
        menu.setHeaderTitle("파일");
        menu.add(0, 1, 0, "열기");
        menu.add(0, 2, 0, "삭제");
        // super.onCreateContextMenu(menu, v, menuInfo);
    }


    private void delete(String absolutePath) {
        File ofile = new File(absolutePath);
        if (ofile.isFile()) {
            ofile.delete();
        } else {
            return;
        }
        ofile = null;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 1:
                HelpF.goIntentFileView(Download.this, mSelFile.getAbsolutePath());
                mSelFile = null;

                break;
            case 2:
                new File(mSelFile.getAbsolutePath()).delete();
                setList();
                mSelFile = null;
                break;
        }
        return true;
        // return super.onContextItemSelected(item);
    }

    class ViewHolder {
        TextView mTvFileName;
        TextView mTvDate;
    }

    class CursorArrayAdapter extends ArrayAdapter<File> {

        public CursorArrayAdapter(Context context, int textViewResourceId,
                                  List<File> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View v = null;
            if (convertView == null) {
                v = inflater.inflate(R.layout.file_list_item, null);
            } else {
                v = convertView;
            }

            TextView mTvFileName = (TextView) v.findViewById(R.id.tvFileName);
            // getItem(position).

            File f = getItem(position);
            v.setTag(f);
            mTvFileName.setText(f.getAbsoluteFile().getName());
            Download.this.registerForContextMenu(v);
            return v;
        }
    }
}
