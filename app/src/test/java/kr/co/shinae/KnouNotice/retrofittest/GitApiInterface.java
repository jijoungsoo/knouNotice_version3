package kr.co.shinae.KnouNotice.retrofittest;

import java.util.List;

import kr.co.shinae.KnouNotice.second.KnouNoticeInfo;
import kr.co.shinae.KnouNotice.second.KnouNoticeListInfo;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface GitApiInterface {
    @GET("/portal/epo/service/retrieveIntgAnncList.data")
    Call<List<KnouNoticeInfo>> groupList(@Query("page") String page
            , @Query("al") String rows
            , @Query("skinCd") String skinCd
            , @Query("searchAnncBlbdNo") String searchAnncBlbdNo
            , @Query("searchAnncClsNo") String searchAnncClsNo
            , @Query("blngDc") String blngDc
            , @Query("blngCd") String blngCd
    );
    @GET("/portal/epo/service/retrieveIntgAnncList.data")
    Call<KnouNoticeListInfo> groupListAll(@Query("page") String page
            , @Query("al") String rows
            , @Query("skinCd") String skinCd
            , @Query("searchAnncBlbdNo") String searchAnncBlbdNo
            , @Query("searchAnncClsNo") String searchAnncClsNo
            , @Query("blngDc") String blngDc
            , @Query("blngCd") String blngCd
    );}
