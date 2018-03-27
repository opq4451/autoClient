package com.uf.automatic.util.leein;

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

import org.apache.http.Header;
import org.apache.http.HttpEntity;
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
import org.apache.http.entity.StringEntity;
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
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uf.automatic.controller.Controller;
import com.uf.automatic.util.Utils;

public class LeeinHttpClient {
    private static String id;
    private static String password;
    private String cookie = null;

    private static LeeinHttpClient instance;
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

    public LeeinHttpClient(String id, String password, String ValidateCode) {
        // TODO Auto-generated constructor stub
        setId(id);
        setPassword(password);

        Map m = new HashMap();
        m.put("id", id);
        m.put("password", password);
        m.put("ValidateCode", ValidateCode);

        String d = mountain[0];
        // setInitCookie(d, m);//塞 cookie
    }

    public static LeeinHttpClient getInstance(String id, String password) {

        return getInstance(id, password, null);

    }

    public static LeeinHttpClient getInstance(String id, String password, String ValidateCode) {
        startFlag = checkStart();
        if (!startFlag.equals("Y")) {
            return null;
        }
        // if(instance == null) {
        instance = new LeeinHttpClient(id, password, ValidateCode);
        // }

        return instance;

    }

    static String mountain[] = { "http://w1.5a1234.com/?m=logined" };
    // sd8885 //Aa258369
    static int urli = 0;
 

    public static void main(String[] args) {
        try {

            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 

    // 兩面ＺＲＵＦ
     public static String getTodayWin(String url,String cookie) throws Exception {
         String u = url + "/member/accounts?_=1522119266532";
         
         return httpGet(cookie,url);
     
     
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

    public static String convertBetPlayId(String sn, String code) {
        String convertCode = code.length() == 1 ? "0" + code : code;
        int convertSn = Integer.parseInt(sn) - 1;
        return "1" + convertSn + convertCode;
    }

    public static JsonObject httpPostBet(String url, String cookie, String betString) throws Exception {

        
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Cookie", cookie);

        httpPost.setHeader("content-type", "application/x-www-form-urlencoded");
        StringEntity postingString = new StringEntity(betString);//gson.tojson() converts your pojo to json
        httpPost.setEntity(postingString);
         
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            String content = EntityUtils.toString(response.getEntity());

            JsonParser parser = new JsonParser();

            JsonObject o = parser.parse(content).getAsJsonObject();
            
            if(o.isJsonObject()) {
                return o;
            } 
        } catch (Exception e) {

        } finally {

        }
        return null;
    }
 

    public static String httpPostInit(String url, String PHPSESSID_COOKIE, String ValidateCode, String loginName,
                                      String loginPwd) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url + "/login");

        httpPost.setHeader("Cookie", PHPSESSID_COOKIE);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("type", "1"));
        nvps.add(new BasicNameValuePair("account", loginName));
        nvps.add(new BasicNameValuePair("password", loginPwd));
        nvps.add(new BasicNameValuePair("code", ValidateCode));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        Header headers[] = response.getHeaders("location");
        if (headers == null || headers.length == 0) {

            return null;
        }
        String location = "";

        for (Header h : headers) {
            location += h.getValue().toString();
        }

        if (!location.equals("")) {
              
            String agreementCookie = httpGetAgreement( url , "/" + location, "defaultLT=PK10JSC;"+PHPSESSID_COOKIE);
            System.out.println(agreementCookie);
            return agreementCookie;
        }

        return null;
    }

    public static String httpGetAgreement(String url,String location, String PHPSESSID_COOKIE) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url + location);

        String c6af995cba1f = location.substring(location.indexOf("=")+1,location.length());
        
        //httpGet.setHeader("Cookie", PHPSESSID_COOKIE + "c6af995cba1f=" + c6af995cba1f );
        
        String cookie =  PHPSESSID_COOKIE + "c6af995cba1f=" + c6af995cba1f; 
        

        return cookie;
    }
    
    public static JsonObject getPl(String url,String cookie) throws IOException {
        String u = url + "/member/odds?lottery=BJPK10&games=B1%2CB2%2CB3%2CB4%2CB5%2CB6%2CB7%2CB8%2CB9%2CB10&_=1522120853231";
        
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet HttpGet = new HttpGet(u);

        HttpGet.setHeader("Cookie", cookie);

        CloseableHttpResponse response = httpclient.execute(HttpGet);
        try {
        

            String content = EntityUtils.toString(response.getEntity());
            
            JsonParser parser = new JsonParser();

            JsonObject o = parser.parse(content).getAsJsonObject();
            
            if(o.isJsonObject()) {
                return o;
            } 

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
        
        return null;
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