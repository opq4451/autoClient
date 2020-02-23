package com.uf.automatic.util.dali;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uf.automatic.controller.Controller;
import com.uf.automatic.util.Utils;
import com.uf.automatic.util.mountain.MoutainHttpClient;

public class NewDaliHttpClient {
    private static String id;
    private static String password;
    private String cookie = null;

    private static NewDaliHttpClient instance;
    private static String checkStartUrl = "https://drive.google.com/uc?export=download&id=17PsTMGjyNnGga5wWjZ4CQS6dOm4OdcxF";
    // private static String forceUrl = "http://220.132.126.216:9999/getForce";
    private static String startFlag = "";

    public static String checkStart() {
        String flag = "N";
        try {
            flag = Utils.httpClientGet(checkStartUrl);
        } catch (Exception e) {

        }
        return flag;
    }

    public NewDaliHttpClient(String id, String password) {
        // TODO Auto-generated constructor stub
        setId(id);
        setPassword(password);
        daliUrl_index++;
        String d = daliUrl[daliUrl_index % 5] + "/login/check";

        setInitCookie(d);//塞 cookie
    }

    public static NewDaliHttpClient getInstance(String id, String password) {
        /*
         * startFlag = checkStart(); if (!startFlag.equals("Y")) { return null; }
         */
        // if(instance == null) {
        instance = new NewDaliHttpClient(id, password);
        // }
//        try {
//            enter();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        return instance;

    }

    static String daliUrl[] = { "https://1904.1988330.com", "https://1920.1988330.com", "https://1501.1988330.com",
                                "https://1284.1988330.com", "https://1094.1988330.com" };

    // sd8885 //Aa258369
    static int daliUrl_index = 0;

    public static void main(String[] args) {
        try {
            NewDaliHttpClient d_h = NewDaliHttpClient.getInstance("ssd3317", "asd123123");
            
            JsonObject r =  d_h.getTodayWin(d_h.getCookie());
            JsonObject betRate = r.get("betRate").getAsJsonObject();

//      
            
            JsonArray a = new JsonArray();
            String []code= {"2","3","4"};
            String sn = "7" ;
            String amount = "1";
            for (String str : code) {
                int computeInt = Integer.parseInt(sn)*10 +  Integer.parseInt(str) + 161;
                String codeBetRate =  betRate.get(Integer.toString(computeInt)).getAsString();
                
                JsonObject d = new JsonObject();
                d.addProperty("detailID", computeInt);
                d.addProperty("betRate", codeBetRate);
                d.addProperty("betMoney", amount);
                a.add(d);
            }
            
            JsonObject result = NewDaliHttpClient.httpPostBet(d_h.getCookie(), a);
            System.out.println(result);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 單球
    // public String query() {
    // String query = uraal[urli % 5] +
    // "/Handler/QueryHandler.ashx?action=get_ad";
    // try {
    // return instance.httpClientUseCookie(query);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return "";
    // }

    // 兩面ＺＲＵＦ
    // public String getoddsInfoForDouble() {
    // String query = uraal[urli % 5]
    // +
    // "/L_PK10/Handler/Handler.ashx?action=get_oddsinfo&playid=2%2C3%2C4%2C6%2C7%2C8%2C10%2C11%2C12%2C14%2C15%2C16%2C18%2C19%2C20%2C22%2C23%2C25%2C26%2C28%2C29%2C31%2C32%2C34%2C35%2C37%2C38&playpage=pk10_lmp";
    // try {
    // return instance.httpClientUseCookie(query);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return "";
    // }

    // public String getoddsInfo() {
    // String query = uraal[urli % 5]
    // +
    // "/L_PK10/Handler/Handler.ashx?action=get_oddsinfo&playid=1%2C5%2C9%2C13%2C17%2C21%2C24%2C27%2C30%2C33&playpage=pk10_d1_10";
    // try {
    // return instance.httpClientUseCookie(query);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return "";
    // }
    
    public static String getLottery() throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost Post = new HttpPost(daliUrl[daliUrl_index % 5] + "/member/Submit/trend_12");

        Post.setHeader("Cookie", daliCookie);

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("No", "400"));

        Post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        CloseableHttpResponse response = httpclient.execute(Post);
        try {

            String content = EntityUtils.toString(response.getEntity());

            return content;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
        
        return "";    
    }
    
    public static String hitEvent() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost Post = new HttpPost(daliUrl[daliUrl_index % 5] + "/member/Submit/bcyx");

        Post.setHeader("Cookie", daliCookie);

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("No", "400"));

        Post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        CloseableHttpResponse response = httpclient.execute(Post);
        try {

            String content = EntityUtils.toString(response.getEntity());

            return "";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }

        return "";
    }

    public static String enter() throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet Post = new HttpGet(daliUrl[daliUrl_index % 5] + "/member/Main/menu_old");

        Post.setHeader("Cookie", daliCookie);

        CloseableHttpResponse response = httpclient.execute(Post);

        try {
            String content = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }

        hitEvent();
        return "";
    }

    public static JsonObject getBetMD5_PL() throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost Post = new HttpPost(daliUrl[daliUrl_index % 5] + "/member/Game_v2/gxpl");

        Post.setHeader("Cookie", daliCookie);

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("ItemNo", "407"));
        params.add(new BasicNameValuePair("MD5", "-1"));

        Post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        CloseableHttpResponse response = httpclient.execute(Post);

        try {

            String content = EntityUtils.toString(response.getEntity());

            JsonParser parser = new JsonParser();

            JsonObject o = parser.parse(content).getAsJsonObject();

            //String MD5 = o.get("MD5").getAsString();

            return o;

        } catch (Exception e) {
            instance = new NewDaliHttpClient(id,password);
            e.printStackTrace();
        } finally {
            response.close();
        }
        return null;
    }

    public static String getBetID(String betString) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost Post = new HttpPost(daliUrl[daliUrl_index % 5] + "/member/Submit/getOddsAndName");

        Post.setHeader("Cookie", daliCookie);

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("oddsName", betString));
        params.add(new BasicNameValuePair("type", "1"));

        Post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        CloseableHttpResponse response = httpclient.execute(Post);

        try {

            String content = EntityUtils.toString(response.getEntity());

            JsonParser parser = new JsonParser();

            JsonArray o = parser.parse(content).getAsJsonArray();

            JsonObject j = o.get(o.size()-1).getAsJsonObject();

            return j.get("XDnum").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
        return "";
    }
    
    
    public static JsonObject dali_bet(String betId,String md5) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost Post = new HttpPost(daliUrl[daliUrl_index % 5] + "/member/Submit/setOrder");

        Post.setHeader("Cookie", daliCookie);

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("xdnum", betId));
        params.add(new BasicNameValuePair("type", "1"));
        params.add(new BasicNameValuePair("MD5", md5));
        params.add(new BasicNameValuePair("noLst", ""));

        
        
        Post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        CloseableHttpResponse response = httpclient.execute(Post);

        try {

            String content = EntityUtils.toString(response.getEntity());

            JsonParser parser = new JsonParser();

            JsonObject o = parser.parse(content).getAsJsonObject();

            String code = o.get("FaildReason").getAsString();
            
            System.out.println(code);
            
            return o;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
        return null;
    }

    // "http://w1.5a1234.com/?m=acc&gameId=2"
    public static String httpGet(String cookie, String url) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet HttpGet = new HttpGet(url);

        HttpGet.setHeader("Cookie", cookie);

        CloseableHttpResponse response = httpclient.execute(HttpGet);

        try {

            String content = EntityUtils.toString(response.getEntity());
            return content;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
        return "";
    }

    public static String getTodayUse() throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet HttpGet = new HttpGet(daliUrl[daliUrl_index % 5] + "/member/User/userInfouu?gno=0&md5=-1");

        HttpGet.setHeader("Cookie", daliCookie);

        CloseableHttpResponse response = httpclient.execute(HttpGet);

        try {

            String content = EntityUtils.toString(response.getEntity());
            return content;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
        return "";
    }

//    public String getTodayWin() throws Exception {
//
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//
//        HttpGet HttpGet = new HttpGet(daliUrl[daliUrl_index % 5] + "//user/getUserInfo");
//
//        HttpGet.setHeader("Cookie", getCookie());
//
//        CloseableHttpResponse response = httpclient.execute(HttpGet);
//
//        try {
//
//            String content = EntityUtils.toString(response.getEntity());
//            
//            
//            return content.substring(content.indexOf("|")+1, content.indexOf("|",content.indexOf("</table>")+9));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            response.close();
//        }
//        return "";
//    }
    static String Refer = "/?cID=367";
    public static JsonObject getTodayWin(String cookie) throws Exception {
        HttpPost Post = new HttpPost(daliUrl[daliUrl_index % 5]  + "/sp10/renewInfo");

        RequestConfig requestConfig = RequestConfig.custom()  
                .setConnectTimeout(5000).setConnectionRequestTimeout(1000)  
                .setSocketTimeout(5000).build();
        Post.setConfig(requestConfig);
        CloseableHttpClient httpclient = HttpClients.createDefault();


        Post.setHeader("Cookie", cookie);
        Post.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
        Post.setHeader("Referer", daliUrl[daliUrl_index % 5] + Refer);

//        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
//        params.add(new BasicNameValuePair("ItemNo", "407"));
//        params.add(new BasicNameValuePair("MD5", "-1"));

//        Post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        CloseableHttpResponse response = httpclient.execute(Post);

        try {

            String content = EntityUtils.toString(response.getEntity());

            JsonParser parser = new JsonParser();

            JsonObject o = parser.parse(content).getAsJsonObject();

            //String MD5 = o.get("MD5").getAsString();

            return o;

        } catch (Exception e) {
            instance = new NewDaliHttpClient(id,password);
            e.printStackTrace();
        } finally {
            response.close();
        }
        return null;
    }

    public static String convertBetPlayId(String sn, String code) {
        String convertCode = code.length() == 1 ? "0" + code : code;
        int convertSn = Integer.parseInt(sn) - 1;
        return "1" + convertSn + convertCode;
    }

    public static JsonObject httpPostBet(String cookie,
                                     JsonArray j) throws Exception {

         
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(daliUrl[daliUrl_index % 5]  + "/sp10/bet");

        httpPost.setHeader("Cookie", cookie);
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
        httpPost.setHeader("Referer", daliUrl[daliUrl_index % 5] + Refer);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for(int i = 0 ;i< j.size();i++) {
            JsonObject object = j.get(i).getAsJsonObject();
            nvps.add(new BasicNameValuePair("betAry["+i+"][detailID]", object.get("detailID").getAsString())); //1+ sn index +  01~ 10 //code
            nvps.add(new BasicNameValuePair("betAry["+i+"][betMoney]", object.get("betMoney").getAsString())); //1+ sn index +  01~ 10 //code
            nvps.add(new BasicNameValuePair("betAry["+i+"][betRate]", object.get("betRate").getAsString())); //1+ sn index +  01~ 10 //code

            
        }
        
        nvps.add(new BasicNameValuePair("chkBetRate", "0")); //1+ sn index +  01~ 10 //code

        
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            String content = EntityUtils.toString(response.getEntity());
            JsonParser parser = new JsonParser();

            JsonObject o = parser.parse(content).getAsJsonObject();

            //String MD5 = o.get("MD5").getAsString();

            return o;
           // return content;
        } catch (Exception e) {

        } finally {

        }
        return null;
    }

    private String setInitCookie(String url) {

        try {

            String cookie = getCookieHttpClient(url);
            
            if (!cookie.equals("")) {
                setCookie(cookie);
            } else {
                daliUrl_index++;
                
                String urla = daliUrl[daliUrl_index % 5] + "/login/check";

               // String urla = daliUrl[daliUrl_index % 5] + "/member/Home/Ulogin_off_code";
                setInitCookie(urla);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String setCookie(HttpResponse httpResponse) {

        Header headers[] = httpResponse.getHeaders("Set-Cookie");
        if (headers == null || headers.length == 0) {

            return null;
        }
        String cookie = "";

        for (Header h : headers) {
            cookie += h.getValue().toString() + ";";
        }

        return cookie;
    }

    public String getCookieHttpClient(String uri) throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
  //      RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();
        System.out.println(uri);
        HttpPost httppost = new HttpPost(uri);
       // httppost.setHeader("Referer", "https://www.1988990.com/search.php");
        httppost.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
        httppost.setHeader("Referer", daliUrl[daliUrl_index % 5] + Refer);

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("account", id));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("userLang", "tw"));

        
        
        
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

  //      httppost.setConfig(requestConfig);
//        getDaliCookie();
//        System.out.println(daliCookie);
//        httppost.setHeader("Cookie", daliCookie);

        String result = null;
        String cookieString = "";
//        cookieString += daliCookie;
        try {
            HttpResponse httpresponse = httpClient.execute(httppost);
//            HttpEntity entity = httpresponse.getEntity();
//            result = EntityUtils.toString(entity);
//            System.out.println(result);
//            JsonParser parser = new JsonParser();
//            JsonObject o = parser.parse(result).getAsJsonObject();
//            String code = o.get("LoginStatus").getAsString();
//            System.out.println(code);
//            if (!code.equals("1")) {
//                daliUrl_index++;
//                String urla = daliUrl[daliUrl_index % 5] + "/member/Home/Ulogin_off_code";
//
//            
//
//                getCookieHttpClient(urla);
//            }
           
            try {
                
                Header[] headers = httpresponse.getHeaders("Set-Cookie");
                for (Header h : headers) {
                    cookieString += h.getValue().toString() + ";";
                    System.out.println(h.getValue().toString());
                }
                //cookieString+="menuId=2;cookiescurrentmlid=2; cookiescurrentlid=2;";
            } finally {

            }
            daliCookie = cookieString;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            daliUrl_index++;
            String urla = daliUrl[daliUrl_index % 5] + "/member/Home/Ulogin_off_code";

            getCookieHttpClient(urla);
            throw e;
        } finally {
            httpClient.close();
        }
        return cookieString;
    }

    private static String daliCookie = "";

    public static String getDaliCookie() throws IOException {
        try {

            CloseableHttpResponse httpresponse = null;

            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpGet HttpGet = new HttpGet(daliUrl[daliUrl_index % 5] + "/member");

            httpresponse = httpclient.execute(HttpGet);

            daliCookie = NewDaliHttpClient.setCookie(httpresponse);

            return daliCookie;
        } catch (Exception e) {
            daliUrl_index++;
        }
        return null;
    }
    
    public static int getPlIndex(String sn, String code) {
        return (Integer.parseInt(sn) - 1) * 10 + (Integer.parseInt(code) - 1);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

}