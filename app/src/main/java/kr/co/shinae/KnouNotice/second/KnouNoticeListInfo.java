package kr.co.shinae.KnouNotice.second;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kr.co.shinae.KnouNotice.HelpF;


public class KnouNoticeListInfo {
	public String records;
	public String page;
	@SerializedName("rows")
	public ArrayList<KnouNoticeInfo> al = new ArrayList<KnouNoticeInfo>();

	@SerializedName("EPOAnncVO")
	public EPOAnncVO epo = new EPOAnncVO();
	public String total;

	public ArrayList<KnouNoticeInfo> getAl() {
		return al;
	}

	public void setAl(ArrayList<KnouNoticeInfo> al) {
		this.al = al;
	}

	public void print(){
		HelpF.Log.d("HAN","======================KnouNoticeListInfo:START=================");
		HelpF.Log.d("HAN","records:"+records);
		HelpF.Log.d("HAN","page:"+page);
		HelpF.Log.d("HAN","======================KnouNoticeListInfo:END=================");
		epo.print();
		HelpF.Log.d("HAN", "======================KnouNoticeListInfo:KnouNoticeInfo:START=================");
		for(int i=0;i< al.size();i++){
			al.get(i).print();
		}
		HelpF.Log.d("HAN", "======================KnouNoticeListInfo:KnouNoticeInfo:END=================");
	}
}
