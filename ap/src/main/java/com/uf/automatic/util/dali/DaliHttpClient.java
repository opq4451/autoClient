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

public class DaliHttpClient {
    private static String id;
    private static String password;
    private String cookie = null;

    private static DaliHttpClient instance;
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

    public DaliHttpClient(String id, String password) {
        // TODO Auto-generated constructor stub
        setId(id);
        setPassword(password);

        String d = daliUrl[daliUrl_index % 5] + "/member/Home/Ulogin_off_code";

        setInitCookie(d);//塞 cookie
    }

    public static DaliHttpClient getInstance(String id, String password) {
        /*
         * startFlag = checkStart(); if (!startFlag.equals("Y")) { return null; }
         */
        // if(instance == null) {
        instance = new DaliHttpClient(id, password);
        // }
        try {
            enter();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return instance;

    }

    static String daliUrl[] = { "http://13561.nptt6729.com", "http://13561.ajjkk6779.com", "http://13561.cppq99638.com",
                                "http://13561.ctty12369.com", "http://13561.cqnm2355.com" };

    // sd8885 //Aa258369
    static int daliUrl_index = 0;

    public static void main(String[] args) {
        try {
            DaliHttpClient d_h = DaliHttpClient.getInstance("bee6611", "qaz123123123");
            
            JsonObject a = getBetMD5_PL();

            
                String MD5 = a.get("MD5").getAsString();
                System.out.println(MD5);
           
            
            
            JsonArray o = a.getAsJsonArray("DATAODDS") ;
            
            String betString = "";//40001,9.909,1|40018,9.909,1|40036,9.909,1
            
            String[] code = {"4"};
            for (String str : code) { 
                int index = DaliHttpClient.getPlIndex("4", str) ;
                JsonObject bet = o.get(index).getAsJsonObject();
                String pl = bet.get("OddsValue1").getAsString();
                String betItemNo = bet.get("ItemNO").getAsString();
                betString += betItemNo+","+pl+","+1+"|"; 
            }
            betString = betString.substring(0,betString.length()-1);
            System.out.println(betString);
            String betid = DaliHttpClient.getBetID(betString);
            System.out.println(betid);
            
            //dali_bet(betid,MD5);
            // String force = Utils.httpClientGet(forceUrl);

            // System.out.println(force);
            // httpClientCookie a =
            // httpClientCookie.getInstance("sd8885","Aa258369");
            // httpClientCookie t =
            // httpClientCookie.getInstance("qq7711","qaz123123");
            // String ret = t.getoddsInfo();
            // System.out.println(ret);
            // testBet(t);
            // String ret = a.getoddsInfo();
            //
            // JsonParser parser = new JsonParser();
            // JsonObject o = parser.parse(ret).getAsJsonObject();
            // JsonObject data = o.getAsJsonObject("data");
            // JsonObject play_odds = data.getAsJsonObject("play_odds");
            // Set<Entry<String, JsonElement>> entrySet = play_odds.entrySet();
            // int i = 0;
            // Map<Integer, String > m = new TreeMap<Integer, String >();
            // for(Map.Entry<String,JsonElement> entry : entrySet){
            // String key = (entry.getKey());
            // String k = key.substring(key.indexOf("_")+1,key.length());
            // JsonElement j = entry.getValue();
            // String pl = j.getAsJsonObject().get("pl").getAsString();
            //
            // m.put(i, k+"@"+pl);
            // i++;
            //
            //
            //
            // }
            // Set set = m.entrySet();
            //
            // // Get an iterator
            // Iterator it = set.iterator();
            //
            // // Display elements
            // while(it.hasNext()) {
            // Map.Entry me = (Map.Entry)it.next();
            // System.out.print("Key is: "+me.getKey() + " & ");
            // System.out.println("Value is: "+me.getValue());
            // }

            // a.getoddsInfoForDouble();
            /*
             * action:put_money phaseid:165921 oddsid:1,2 uPI_P:9.81,9.81 uPI_M:10,10 i_index:0,1
             * JeuValidate:11170449105207 playpage:pk10_d1_10
             * 
             * 
             * action:put_money phaseid:165921 oddsid:17,146 uPI_P:9.81,9.81 uPI_M:10,10 i_index:0,1
             * JeuValidate:11170449214316 playpage:pk10_d1_10
             */

            // String a = new SimpleDateFormat("MMddhhmmsssSSS").format(new
            // Date());
            //
            // String bet
            // ="http://mem1.fpaesf109.pasckr.com:88/L_PK10/Handler/Handler.ashx"
            // +
            // "?action=put_money&" +
            // "phaseid="+phaseid+"&" +
            // "oddsid=1&" +
            // "uPI_P=9.81&" +
            // "uPI_M=10&" +
            // "i_index=0&" +
            // //"JeuValidate="+a+"&" +
            // "playpage=pk10_d1_10" ;

            /*
             * 
             * action:put_money phaseid:163754 oddsid:2 uPI_P:9.81 uPI_M:10 i_index:0 JeuValidate:11050308464138
             * playpage:pk10_d1_10
             */
            // 649096
            // phaseid 163755
            // ;
            // String v = httpClientCookieGet(bet);
            // JsonParser parser = new JsonParser();
            // JsonObject o = parser.parse(v).getAsJsonObject();
            // JsonObject data = o.getAsJsonObject("data");
            // String t = data.get("JeuValidate").getAsString();
            // String aa
            // ="http://mem1.fpaesf109.pasckr.com:88/L_PK10/Handler/Handler.ashx"
            // +
            // "?action=put_money&" +
            // "phaseid="+phaseid+"&" +
            // "oddsid=2&" +
            // "uPI_P=9.81&" +
            // "uPI_M=10&" +
            // "i_index=0&" +
            // "JeuValidate="+t+"&" +
            // "playpage=pk10_d1_10" ;
            // httpClientCookieGet(aa);
            // action=put_money&phaseid=163760&oddsid=1&uPI_P=9.81&uPI_M=10&i_index=0&JeuValidate=11050344302630&playpage=pk10_d1_10
            // action=put_money&phaseid=163760&oddsid=1&uPI_P=9.81&uPI_M=10&i_index=0&JeuValidate=11050343054559&playpage=pk10_d1_10
            //
            // action:put_money
            // phaseid:163752
            // oddsid:1
            // uPI_P:9.81
            // uPI_M:10
            // i_index:0
            // JeuValidate:11050301105529
            // playpage:pk10_d1_10
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
    public static String hitEvent(String code) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost Post = new HttpPost(daliUrl[daliUrl_index % 5] + "/member/Submit/bcyx");

        Post.setHeader("Cookie", daliCookie);

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("No", code));

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

        
        return "";
    }
    
    public static JsonObject getBetBoatMD5_PL() throws Exception {
        hitEvent("800");

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost Post = new HttpPost(daliUrl[daliUrl_index % 5] + "/member/Game_v2/gxpl");

        Post.setHeader("Cookie", daliCookie);

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("ItemNo", "807"));
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
            e.printStackTrace();
        } finally {
            response.close();
        }
        return null;
    }
    
    public static JsonObject getBetMD5_PL() throws Exception {

        hitEvent("400");
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

        HttpGet HttpGet = new HttpGet(daliUrl[daliUrl_index % 5] + "/member/Main/left");

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

    public static String getTodayWin() throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet HttpGet = new HttpGet(daliUrl[daliUrl_index % 5] + "/member/Game_v2/getSyjq");

        HttpGet.setHeader("Cookie", daliCookie);

        CloseableHttpResponse response = httpclient.execute(HttpGet);

        try {

            String content = EntityUtils.toString(response.getEntity());
            
            
            return content.substring(content.indexOf("|")+1, content.indexOf("|",content.indexOf("</table>")+9));

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

    public static String httpPostBet(String url, String cookie, String betPhase, String amount, String sn,
                                     String[] code) throws Exception {

        int totalAmount = Integer.parseInt(amount) * 5;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Cookie", cookie);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("gameId", "4"));
        nvps.add(new BasicNameValuePair("turnNum", betPhase));
        nvps.add(new BasicNameValuePair("totalNums", "5"));
        nvps.add(new BasicNameValuePair("totalMoney", Integer.toString(totalAmount)));
        nvps.add(new BasicNameValuePair("betBean[0][playId]", convertBetPlayId(sn, code[0]))); //1+ sn index +  01~ 10 //code
        nvps.add(new BasicNameValuePair("betBean[0][money]", amount));
        nvps.add(new BasicNameValuePair("betBean[1][playId]", convertBetPlayId(sn, code[1])));
        nvps.add(new BasicNameValuePair("betBean[1][money]", amount));
        nvps.add(new BasicNameValuePair("betBean[2][playId]", convertBetPlayId(sn, code[2])));
        nvps.add(new BasicNameValuePair("betBean[2][money]", amount));
        nvps.add(new BasicNameValuePair("betBean[3][playId]", convertBetPlayId(sn, code[3])));
        nvps.add(new BasicNameValuePair("betBean[3][money]", amount));
        nvps.add(new BasicNameValuePair("betBean[4][playId]", convertBetPlayId(sn, code[4])));
        nvps.add(new BasicNameValuePair("betBean[4][money]", amount));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            String content = EntityUtils.toString(response.getEntity());

            return content;
        } catch (Exception e) {

        } finally {

        }
        return "";
    }

    private String setInitCookie(String url) {

        try {

            String cookie = getCookieHttpClient(url);

            if (!cookie.equals("")) {
                setCookie(cookie);
            } else {
                daliUrl_index++;
                String urla = daliUrl[daliUrl_index % 5] + "/member/Home/Ulogin_off_code";
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

    public static String getCookieHttpClient(String uri) throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
        System.out.println(uri);
        HttpPost httppost = new HttpPost(uri);
        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("loginName", id));
        params.add(new BasicNameValuePair("loginPwd", password));
        params.add(new BasicNameValuePair("ValidateCode", ""));

        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        httppost.setConfig(requestConfig);
        getDaliCookie();
        System.out.println(daliCookie);
        httppost.setHeader("Cookie", daliCookie);

        String result = null;
        String cookieString = "";
        cookieString += daliCookie;
        try {
            HttpResponse httpresponse = httpClient.execute(httppost);
            HttpEntity entity = httpresponse.getEntity();
            result = EntityUtils.toString(entity);
            System.out.println(result);
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(result).getAsJsonObject();
            String code = o.get("LoginStatus").getAsString();
            System.out.println(code);
            if (!code.equals("1")) {
                daliUrl_index++;
                String urla = daliUrl[daliUrl_index % 5] + "Home/Ulogin_off_code?" + "&loginName=" + id + "&loginPwd="
                              + password + "";

                getCookieHttpClient(urla);
            }
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
            String urla = daliUrl[daliUrl_index % 5] + "Home/Ulogin_off_code?" + "&loginName=" + id + "&loginPwd="
                          + password + "";

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

            daliCookie = DaliHttpClient.setCookie(httpresponse);

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