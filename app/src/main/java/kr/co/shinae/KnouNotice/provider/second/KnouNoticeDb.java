/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.co.shinae.KnouNotice.provider.second;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Convenience definitions for NotePadProvider
 */
public final class KnouNoticeDb {
	public static final String AUTHORITY = "kr.co.shinae.provider.KnouNotice";

	// This class cannot be instantiated
	private KnouNoticeDb() {
	}
	/**
	 * KNOU_NOTICE table
	 */
	public static final class KNOU_NOTICE implements BaseColumns {
		// This class cannot be instantiated
		private KNOU_NOTICE() {
		}

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/knou_notice");

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
		 */
		//public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/knou_notice";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * note.
		 */
		//public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.note";
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/knou_notice";

		public static final String TABLE_NAME = "TBL_KNOU_NOTICE";



		/**
		 * The default sort order for this table
		 */
		public static final String ORDER_ID_DESC = "_id DESC";
		public static final String blngDc="blngDc";//DP
		public static final String blngCd="blngCd";//34
		public static final String iread = "iread";
		public static final String total="total";  //속성구분
		public static final String anncClsNm="anncClsNm";	//중요도
		public static final String imtdg="imtdg"; //제목
		public static final String tit="tit";
		public static final String anncClsNo="anncClsNo";
		public static final String rnum="rnum";
		public static final String page="page";
		public static final String regDttm="regDttm"; //기간
		public static final String dpcd="dpcd";
		public static final String period="period";
		public static final String rows="al";
		public static final String lastIndex="lastIndex";
		public static final String rpnn="rpnn";//왕숙자
		public static final String ruid="ruid";
		public static final String anncBlbdNo = "anncBlbdNo"; //조회수
		public static final String uuid="uuid";
		public static final String records="records";//서울지역대학  ==작성부서
		public static final String firstIndex = "firstIndex";
		public static final String anncNo = "anncNo";
		public static final String inqT = "inqT";
		public static final String reltAnnc = "reltAnnc";
		public static final String regDpNm = "regDpNm";
		public static final String updtDttm = "updtDttm";
		public static final String content = "content";
		public static final String memo = "memo";
	}



	/**
	 * KNOU_NOTICE table
	 */
	public static final class KNOU_NOTICE_FILE implements BaseColumns {
		// This class cannot be instantiated
		private KNOU_NOTICE_FILE() {
		}

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/knou_notice_file");

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
		 */
		//public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/knou_notice_file";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * note.
		 */
		//public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.note";
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/knou_notice_file";

		public static final String TABLE_NAME = "TBL_KNOU_NOTICE_FILE";



		/**
		 * The default sort order for this table
		 */
		public static final String ORDER_ID_DESC = "_id DESC";

		/**
		 * The note itself
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String _KNOU_NOTICE_ID = "_knou_notice_id";

		public static final String href = "href";
		public static final String fileName = "fileName";
	}
}






















