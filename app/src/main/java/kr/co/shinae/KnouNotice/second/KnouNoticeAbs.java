package kr.co.shinae.KnouNotice.second;

import android.content.Context;
import android.content.SharedPreferences;

import com.j256.ormlite.dao.ForeignCollection;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.apache.http.impl.client.DefaultHttpClient;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;

import kr.co.shinae.KnouNotice.Activity.Main;
import kr.co.shinae.KnouNotice.Activity.NoticeRead;
import kr.co.shinae.KnouNotice.second.api.KnouNoticeHttpApiInterface;
import retrofit.Call;
import retrofit.Converter;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public  class KnouNoticeAbs {
    SharedPreferences mPref;
    DefaultHttpClient mHttpClient;

    public static final String API_URL = "http://ep.knou.ac.kr";

    public KnouNoticeAbs(Context context) {
        mHttpClient = new DefaultHttpClient();
        mPref = context.getSharedPreferences(Main.KNOU_PREF, 0);
    }

    public KnouNoticeListInfo getList(String page,String blngDc,String blngCd) throws JSONException, URISyntaxException, IOException {
        String rows = "10";

        OkHttpClient okClient = new OkHttpClient();
        okClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        });

        Retrofit client = new Retrofit.Builder()
                .baseUrl(KnouNoticeAbs.API_URL)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KnouNoticeHttpApiInterface service = client.create(KnouNoticeHttpApiInterface.class);
        client.client().interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                Headers dd = response.headers();
                for (int i = 0; i < dd.size(); i++) {
                    System.out.println("dddd = " + dd.name(i) + "==" + dd.value(i));

                }
                System.out.println("dddd = " + response.request().url());
                return response;
            }
        });

        Call<KnouNoticeListInfo> call = service.getNoticeList(page, rows, "lc_dp_list", "", "", blngDc, blngCd);
        retrofit.Response<KnouNoticeListInfo> response = call.execute();
        System.out.println("dfdfdf1333334" + response.isSuccess());
        System.out.println("ddd1333334" + response.code());
        return response.body();
    }

    protected String getContentHtml(String content) {
        content = content.replace(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        StringBuffer text = new StringBuffer();
        text.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> ");
        text.append("<html lang=\"ko\" xml:lang=\"ko\" xmlns=\"http://www.w3.org/1999/xhtml\">");
        text.append("<head >");
        text.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
        text.append("<script language=\"javascript\" type=\"text/javascript\" src=\"file:///android_asset/jquery-2.1.4.min.js\"></script>");
        //text.append("<script language=\"javascript\">alert(\"Bb\")</script>");
        text.append("<script type=\"text/javascript\">\n");
        //내용
        //text.append("alert(\"Bbcsscc\");\n");
        text.append("$( function() {\n");
        text.append("var strContent = $(\'textarea[name=txtaContent]\').val();\n");
        text.append("var objView = document.getElementById(\"divView\");\n");
        text.append("objView.innerHTML = strContent;\n");
        text.append("});\n");
        text.append("</script>\n");
        // text.append("<link rel=\"stylesheet\" type=\"text/css\" charset=\"UTF-8\" media=\"all\" /> ");

        // 아래코드도 안먹힘
        // text.append("<meta name='viewport' content='width=600' />");
        text.append("</head>");
        text.append("<body width='100%'>");
        text.append("<table width='100%' cellpadding='0' cellspacing='0'><tr><td width='100%'>");

        text.append(content);

        text.append("</td></tr></table>");
        text.append("</body></html>");

        content = text.toString();
        //HelpF.Log.d("HAN", "contentcontentcontentcontenttext.toString():" + content);
        return content;
    }

    protected void getFileAttache(SimpleHtmlSerializer htmlSerializer, TagNode pNode,KnouNoticeInfo knouNoticeInfo) {
        String expressionContent = "//div[@class=\"MultiFile-list\"]";
        Object[] myNodeBody = null;
        try {
            myNodeBody = pNode.evaluateXPath(expressionContent);
        } catch (XPatherException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (myNodeBody.length <= 0) {
            return;
        }
        TagNode tmpNode = (TagNode) myNodeBody[0];
        TagNode[] nl = tmpNode.getChildTags();
        //Log.d("HAN", "nl.length:" + nl.length);
        KnouNoticeFileInfo attacheFileInfo = null;
        for (int i = 0; i < nl.length; i++) { // 0번은 필요없는거 지움
            //Log.d("HAN", "nl[i].getName():" + nl[i].getName());
            //Log.d("HAN", "nl[i].getText():" + nl[i].getText());
            attacheFileInfo = new KnouNoticeFileInfo();
            if (nl[i].getName().trim().equals("a")) {
                System.out.println("a["+i+"]:"+nl[i].getName());

                String href = nl[i].getAttributeByName("href");
                System.out.println("href["+i+"]:"+href);
                attacheFileInfo.href = "http://ep.knou.ac.kr" + href;
                attacheFileInfo.fileName = nl[i].getText().toString();
                System.out.println("fileName["+i+"]:"+ attacheFileInfo.fileName);
                //knouNoticeInfo.AttacheFiles.add(attacheFileInfo);
                knouNoticeInfo.AttacheFileArrayList.add(attacheFileInfo);
            }
        }
    }

    // ep.knou.ac.kr/portal/epo/service/retrieveIntgAnncDtl.do?anncNo=34219&page=2&skinCd=lc_dp_list&blngDc=LU&blngCd=010&css=table4&mark=true&epTicket=LOG
    public KnouNoticeInfo getInfo(String anncNo,String blngDc,String blngCd) {
        OkHttpClient okClient = new OkHttpClient();
        okClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        });

        Retrofit client = new Retrofit.Builder()
                .baseUrl(KnouNoticeAbs.API_URL)
                .client(okClient)
                .addConverterFactory(new ToStringConverterFactory())
                .build();
        KnouNoticeHttpApiInterface service = client.create(KnouNoticeHttpApiInterface.class);
        client.client().interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                Headers dd = response.headers();
                for (int i = 0; i < dd.size(); i++) {
                   System.out.println("dddd = " + dd.name(i) + "==" + dd.value(i));
                }
                System.out.println("dddd = " + response.request().url());
                return response;
            }
        });

        Call<KnouNoticeInfo> call = service.getNoticeRead(anncNo, "lc_dp_list", blngDc, "010", "table4", "true", "LOG");

        retrofit.Response<KnouNoticeInfo> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        KnouNoticeInfo knouNoticeInfo =  response.body();
        knouNoticeInfo.anncNo=anncNo;


        //HelpFunction.writeDebugFile(knouNoticeInfo.content);
        return knouNoticeInfo;
    }
    public final class ToStringConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
            //noinspection EqualsBetweenInconvertibleTypes
            if (KnouNoticeInfo.class.equals(type)) {
                return new Converter<ResponseBody, KnouNoticeInfo>() {
                    @Override
                    public KnouNoticeInfo convert(ResponseBody responseBody) throws IOException {
                        HtmlCleaner htmlCleaner = new HtmlCleaner();
                        CleanerProperties props = htmlCleaner.getProperties();
                        props.setOmitComments(true); // 주석제거
                        TagNode tagNode = null;
                        tagNode = htmlCleaner.clean(responseBody.string());

                        SimpleHtmlSerializer htmlSerializer = new SimpleHtmlSerializer(props);
                        String expression = "//table/tbody";
                        Object[] myNodes = null;
                        try {
                            myNodes = tagNode.evaluateXPath(expression);
                        } catch (XPatherException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        TagNode tableNode = (TagNode) myNodes[0];
                        KnouNoticeInfo knouNoticeInfo = new KnouNoticeInfo();
                        TagNode[] tr = tableNode.getChildTags();
        /*
		 * for (int i = 0; i < tr.length; i++) { Log.d("HAN", "tr[" + i +
		 * "].getName:" + tr[i].getName()); TagNode[] td = tr[i].getChildTags();
		 * for (int j = 0; j < td.length; j++) { Log.d("HAN", "td[" + j +
		 * "].getName:" + td[j].getName()); } }
		 */
                        knouNoticeInfo.tit = tr[0].getChildTags()[1].getText().toString();// 제목
                        knouNoticeInfo.regDpNm = tr[1].getChildTags()[1].getText().toString(); // 관련부서
                        knouNoticeInfo.regDttm = tr[1].getChildTags()[3].getText().toString(); // 작성일시
                        knouNoticeInfo.regDttm = tr[1].getChildTags()[5].getText().toString(); // 공지기간
                        knouNoticeInfo.imtdg = tr[2].getChildTags()[1].getText().toString(); // 중요도
                        getFileAttache(htmlSerializer, tr[2].getChildTags()[1], knouNoticeInfo);

                        // 본문

                        //String expressionContent = "//div[@class=\"board_content\"]/div[@class=\"board_contentIn\"]/textarea[@id=\"txtaContent\"]/span";
                        String expressionContent = "//div[@class=\"board_content\"]";
                        Object[] myNodeBody = null;
                        try {
                            myNodeBody = tagNode.evaluateXPath(expressionContent);
                        } catch (XPatherException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        TagNode tmpNode = (TagNode) myNodeBody[0];
                        //tmpNode.removeAttribute("style");
                        String tmp3 = htmlSerializer.getAsString(tmpNode);
                        knouNoticeInfo.content = getContentHtml(tmp3);


                        return knouNoticeInfo;
                    }
                };
            }
            return null;
        }

        @Override
        public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
            //noinspection EqualsBetweenInconvertibleTypes
            if (String.class.equals(type)) {
                return new Converter<String, RequestBody>() {
                    @Override
                    public RequestBody convert(String value) throws IOException {
                        return RequestBody.create(MediaType.parse("text/plain"), value);
                    }
                };
            }
            return null;
        }
    }
}
