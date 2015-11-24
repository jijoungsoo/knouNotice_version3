package kr.co.shinae.KnouNotice.second;

import kr.co.shinae.KnouNotice.HelpF;

public class EPOAnncVO{
    public String blngCd;
    public String rnum;
    public String page;
    public String rows;
    public String lastIndex;
    public String blngDc;
    public String records;
    public String skinCd;
    public String firstIndex;
    public String reltAnnc;

    public void print(){
        HelpF.Log.d("HAN","======================KnouNoticeListInfo:EPOAnncVO:START=================");
        HelpF.Log.d("HAN","blngCd:"+blngCd);
        HelpF.Log.d("HAN","rnum:"+rnum);
        HelpF.Log.d("HAN","page:"+page);
        HelpF.Log.d("HAN","al:"+rows);
        HelpF.Log.d("HAN","lastIndex:"+lastIndex);
        HelpF.Log.d("HAN","blngDc:"+blngDc);
        HelpF.Log.d("HAN","records:"+records);
        HelpF.Log.d("HAN","skinCd:"+skinCd);
        HelpF.Log.d("HAN","firstIndex:"+firstIndex);
        HelpF.Log.d("HAN","reltAnnc:"+reltAnnc);
        HelpF.Log.d("HAN","======================KnouNoticeListInfo:EPOAnncVO:END==================");
    }
}