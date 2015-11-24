package kr.co.shinae.KnouNotice.second;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

import kr.co.shinae.KnouNotice.HelpF;


@DatabaseTable(tableName = "TBL_KNOU_NOTICE")
public class KnouNoticeInfo  {
    public KnouNoticeInfo() {

    }

    public KnouNoticeInfo(String total
            , String anncClsNm
            , String imtdg
            , String tit
            , String anncClsNo
            , String rnum
            , String page
            , String regDttm
            , String dpcd
            , String period
            , String rows
            , String lastIndex
            , String ruid
            , String anncBlbdNo
            , String uuid
            , String records
            , String rpnn
            , String firstIndex
            , String anncNo
            , String inqT
            , String reltAnnc
            , String regDpNm
            , String updtDttm
    ) {
        this.total = total;
        this.anncClsNm = anncClsNm;
        this.imtdg = imtdg;
        this.tit = tit;
        this.anncClsNo = anncClsNo;
        this.rnum = rnum;
        this.page = page;
        this.regDttm = regDttm;
        this.dpcd = dpcd;
        this.period = period;
        this.rows = rows;
        this.lastIndex = lastIndex;
        this.ruid = ruid;
        this.anncBlbdNo = anncBlbdNo;
        this.uuid = uuid;
        this.records = records;
        this.rpnn = rpnn;
        this.firstIndex = firstIndex;
        this.anncNo = anncNo;
        this.inqT = inqT;
        this.reltAnnc = reltAnnc;
        this.regDpNm = regDpNm;
        this.updtDttm = updtDttm;
    }

    @DatabaseField(generatedId = true, columnName = "_ID")
    public Long _ID;
    @DatabaseField
    public String iread;
    @DatabaseField
    public String blngDc;//DP
    @DatabaseField
    public String blngCd;//34
    @DatabaseField
    public String total;
    @DatabaseField
    public String anncClsNm;  //속성구분
    @DatabaseField
    public String imtdg;    //중요도
    @DatabaseField
    public String tit;
    @DatabaseField
    public String anncClsNo;
    @DatabaseField
    public String rnum;
    @DatabaseField
    public String page;
    @DatabaseField
    public String regDttm;
    @DatabaseField
    public String dpcd;
    @DatabaseField
    public String period;
    @DatabaseField
    public String rows;
    @DatabaseField
    public String lastIndex;
    @DatabaseField
    public String rpnn;
    @DatabaseField
    public String ruid;
    @DatabaseField
    public String anncBlbdNo;
    @DatabaseField
    public String uuid;
    @DatabaseField
    public String records;
    @DatabaseField
    public String firstIndex;
    @DatabaseField
    public String anncNo;
    @DatabaseField
    public String inqT;  //조회수
    @DatabaseField
    public String reltAnnc;
    @DatabaseField
    public String regDpNm;//서울지역대학  ==작성부서
    @DatabaseField
    public String updtDttm;
    @DatabaseField
    public String content;
    @DatabaseField
    public String memo;

    @ForeignCollectionField
    public ForeignCollection<KnouNoticeFileInfo> AttacheFiles;


    public ArrayList<KnouNoticeFileInfo> AttacheFileArrayList = new ArrayList<KnouNoticeFileInfo>();


    public void print() {
        HelpF.Log.d("HAN", "total:" + total);
        HelpF.Log.d("HAN", "_ID:" + _ID);
        HelpF.Log.d("HAN", "anncClsNm:" + anncClsNm);
        HelpF.Log.d("HAN", "imtdg:" + imtdg);
        HelpF.Log.d("HAN", "tit:" + tit);
        HelpF.Log.d("HAN", "anncClsNo:" + anncClsNo);
        HelpF.Log.d("HAN", "rnum:" + rnum);
        HelpF.Log.d("HAN", "page" + page);
        HelpF.Log.d("HAN", "regDttm:" + regDttm);
        HelpF.Log.d("HAN", "dpcd:" + dpcd);
        HelpF.Log.d("HAN", "period:" + period);
        HelpF.Log.d("HAN", "al:" + rows);
        HelpF.Log.d("HAN", "lastIndex:" + lastIndex);
        HelpF.Log.d("HAN", "ruid:" + ruid);
        HelpF.Log.d("HAN", "anncBlbdNo:" + anncBlbdNo);
        HelpF.Log.d("HAN", "uuid:" + uuid);
        HelpF.Log.d("HAN", "records:" + records);
        HelpF.Log.d("HAN", "rpnn:" + rpnn);
        HelpF.Log.d("HAN", "firstIndex:" + firstIndex);
        HelpF.Log.d("HAN", "anncNo:" + anncNo);
        HelpF.Log.d("HAN", "inqT:" + inqT);
        HelpF.Log.d("HAN", "reltAnnc:" + reltAnnc);
        HelpF.Log.d("HAN", "regDpNm:" + regDpNm);
        HelpF.Log.d("HAN", "updtDttm:" + updtDttm);
    }
}
