package kr.co.shinae.KnouNotice.second.api;

import kr.co.shinae.KnouNotice.second.KnouNoticeInfo;
import kr.co.shinae.KnouNotice.second.KnouNoticeListInfo;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface KnouNoticeHttpApiInterface {
    @GET("/portal/epo/service/retrieveIntgAnncList.data")
    Call<KnouNoticeListInfo> getNoticeList(@Query("page") String page
            , @Query("rows") String rows
            , @Query("skinCd") String skinCd
            , @Query("searchAnncBlbdNo") String searchAnncBlbdNo
            , @Query("searchAnncClsNo") String searchAnncClsNo
            , @Query("blngDc") String blngDc
            , @Query("blngCd") String blngCd
    );

/*    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("anncNo", anncNo));
    params.add(new BasicNameValuePair("skinCd", "lc_dp_list"));
    params.add(new BasicNameValuePair("blngDc", blngDc));
    params.add(new BasicNameValuePair("blngCd", "010"));
    params.add(new BasicNameValuePair("css", "table4"));
    params.add(new BasicNameValuePair("mark", "true"));
    params.add(new BasicNameValuePair("epTicket", "LOG"));
*/
    @GET("/portal/epo/service/retrieveIntgAnncDtl.do")
    Call<KnouNoticeInfo> getNoticeRead(@Query("anncNo") String anncNo
            , @Query("skinCd") String skinCd
            , @Query("blngDc") String blngDc
            , @Query("blngCd") String blngCd
            , @Query("css") String css
            , @Query("mark") String mark
            , @Query("epTicket") String epTicket
    );




}
