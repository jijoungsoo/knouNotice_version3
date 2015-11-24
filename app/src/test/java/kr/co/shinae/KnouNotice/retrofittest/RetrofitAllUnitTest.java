package kr.co.shinae.KnouNotice.retrofittest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import org.junit.Test;

import java.io.IOException;

import kr.co.shinae.KnouNotice.second.KnouNoticeAbs;
import kr.co.shinae.KnouNotice.second.KnouNoticeListInfo;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RetrofitAllUnitTest {

    @Test
    public void ddd() throws Exception {
        System.out.println("ddd");
        OkHttpClient okClient = new OkHttpClient();
        okClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        });

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        Retrofit client = new Retrofit.Builder()
                .baseUrl(KnouNoticeAbs.API_URL)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        System.out.println("ddd1111");

        GitApiInterface service = client.create(GitApiInterface.class);

        client.client().interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                System.out.println("jijs111");
                Headers dd = response.headers();
                for (int i = 0; i < dd.size(); i++) {
                    System.out.println("dddd = " + dd.name(i) + "==" + dd.value(i));

                }
                System.out.println("dddd = " + response.request().url());
                // Do anything with response here

                return response;
            }
        });;

        System.out.println("ddd1222");
        Call<KnouNoticeListInfo> call = service.groupListAll("1", "10", "lc_dp_list", "", "", "DP", "34");

        System.out.println("ddd13");

        System.out.println("ddd14sdfsdf" + call.toString());

        retrofit.Response<KnouNoticeListInfo> response= call.execute();
        System.out.println("dfdfdf1333334" + response.isSuccess());
        System.out.println("ddd1333334"+response.code());




        System.out.println("ddd14");
    }
}