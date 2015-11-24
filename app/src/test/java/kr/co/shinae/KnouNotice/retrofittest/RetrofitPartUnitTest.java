package kr.co.shinae.KnouNotice.retrofittest;

import org.junit.Test;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import kr.co.shinae.KnouNotice.second.KnouNoticeAbs;
import kr.co.shinae.KnouNotice.second.KnouNoticeInfo;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RetrofitPartUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

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
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
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
for(int i=0;i<dd.size();i++){
    System.out.println("dddd = " + dd.name(i)+"=="+dd.value(i));

}
                System.out.println("dddd = " + response.request().url());
                // Do anything with response here

                return response;
            }
        });;

        System.out.println("ddd1222");
        Call<List<KnouNoticeInfo>> call = service.groupList("1", "10", "lc_dp_list", "", "", "DP", "34");

        System.out.println("ddd13");
/*
        call.enqueue(new Callback<List<KnouNotice>>() {
            @Override
            public void onResponse(retrofit.Response<List<KnouNotice>> response, Retrofit retrofit) {
                System.out.println("onResponse");
                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    System.out.println("sdfsdsdf");
                    List<KnouNotice> result = response.body();
                    System.out.println(result.size());

                } else {
                    System.out.println("ddddddddddddddddddddd");

                    //request not successful (like 400,401,403 etc)
                    //Handle errors
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("onFailure");
            }
        });
*/
        System.out.println("ddd14sdfsdf" + call.toString());

        retrofit.Response<List<KnouNoticeInfo>> response= call.execute();
        System.out.println("dfdfdf1333334" + response.isSuccess());
        System.out.println("ddd1333334"+response.code());


        System.out.println("ddd14");
    }

    public class ItemTypeAdapterFactory implements TypeAdapterFactory {

        public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

            return new TypeAdapter<T>() {

                public void write(JsonWriter out, T value) throws IOException {
                    delegate.write(out, value);
                }

                public T read(JsonReader in) throws IOException {

                    JsonElement jsonElement = elementAdapter.read(in);
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        System.out.println("json object 1 is "+ jsonObject.has("al") );
                        System.out.println("json object 2 is "+  jsonObject.get("al").isJsonObject());
                        if (jsonObject.has("al") && jsonObject.get("al").isJsonArray()) {
                            jsonElement = jsonObject.get("al");
                        }
                    }

                    return delegate.fromJsonTree(jsonElement);
                }
            }.nullSafe();
        }
    }

}