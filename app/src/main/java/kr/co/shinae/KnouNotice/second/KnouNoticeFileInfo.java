package kr.co.shinae.KnouNotice.second;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class KnouNoticeFileInfo {
	public KnouNoticeFileInfo() {

	}
	@DatabaseField(generatedId = true)
	public long _ID;
	@DatabaseField(foreign = true, foreignAutoRefresh = true,columnName = "_KNOU_NOTICE_ID")
	public KnouNoticeInfo _KNOU_NOTICE_ID;
	@DatabaseField(columnName = "href")
	public String href;
	@DatabaseField
	public String fileName;
}
