package com.uf.automatic.util.mountain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

public class MoutainHttpClient {
    private static String id;
    private static String password;
    private String cookie = null;

    private static MoutainHttpClient instance;
    private static String checkStartUrl = "https://drive.google.com/uc?export=download&id=17PsTMGjyNnGga5wWjZ4CQS6dOm4OdcxF";
    private static String forceUrl = "http://220.132.126.216:9999/getForce";
    private static String startFlag = "";

    public static String checkStart() {
        String flag = "N";
        try {
            flag = Utils.httpClientGet(checkStartUrl);
        } catch (Exception e) {

        }
        return flag;
    }

    public MoutainHttpClient(String id, String password, String ValidateCode) {
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

    public static MoutainHttpClient getInstance(String id, String password) {

        return getInstance(id, password, null);

    }

    public static MoutainHttpClient getInstance(String id, String password, String ValidateCode) {
        startFlag = checkStart();
        if (!startFlag.equals("Y")) {
            return null;
        }
        //if(instance == null) {
        instance = new MoutainHttpClient(id, password, ValidateCode);
        //}

        return instance;

    }

    static String uraal[] = { "http://mem1.fpaesf109.pasckr.com:88/", "http://mem5.fpaesf109.hfpfky.com/",
                              "http://mem2.fpaesf109.hfpfky.com:88/", "http://mem3.fpaesf109.pasckr.com:88/",
                              "http://mem4.fpaesf109.hfpfky.com/" };
    static String mountain[] = { "http://w1.5a1234.com/?m=logined" };
    //sd8885 //Aa258369
    static int urli = 0;

    

//    public static void testBet(MoutainHttpClient h) {
//        String ret = h.getoddsInfo();
//        // 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数
//
//        JsonParser parser = new JsonParser();
//        JsonObject o = parser.parse(ret).getAsJsonObject();
//        JsonObject data = o.getAsJsonObject("data");
//        Map<Integer, String> normal = new TreeMap<Integer, String>();
//        Utils.producePl(normal, ret); //產生倍率 for single
//
//        String drawIssue = data.get("nn").getAsString();
//        String p_id = data.get("p_id").getAsString();
//        //System.out.println(normal.toString());
//        String betRet = h.normalBet(p_id, "2,", "9.8", "0,", "1,", "pk10_d1_10");
//        System.out.println(betRet);
//    }

    public static void main(String[] args) {
        try {

            String force = Utils.httpClientGet(forceUrl);

            System.out.println(force);
            //httpClientCookie a = httpClientCookie.getInstance("sd8885","Aa258369");
            //  httpClientCookie t = httpClientCookie.getInstance("qq7711","qaz123123");
            //   String ret = t.getoddsInfo();
            //   System.out.println(ret);
            //		    testBet(t);
            //		    String ret = a.getoddsInfo();
            //		    
            //		    JsonParser parser = new JsonParser();
            //            JsonObject o = parser.parse(ret).getAsJsonObject();
            //            JsonObject data = o.getAsJsonObject("data");
            //            JsonObject play_odds = data.getAsJsonObject("play_odds");
            //            Set<Entry<String, JsonElement>> entrySet = play_odds.entrySet();
            //            int i = 0;
            //            Map<Integer, String > m = new TreeMap<Integer, String >();   
            //            for(Map.Entry<String,JsonElement> entry : entrySet){
            //              String key = (entry.getKey());
            //              String k = key.substring(key.indexOf("_")+1,key.length());
            //              JsonElement j = entry.getValue();
            //              String pl = j.getAsJsonObject().get("pl").getAsString();
            //              
            //              m.put(i, k+"@"+pl);
            //              i++;
            //              
            //              
            //               
            //            }
            //            Set set = m.entrySet();
            //            
            //            // Get an iterator
            //            Iterator it = set.iterator();
            //         
            //            // Display elements
            //            while(it.hasNext()) {
            //              Map.Entry me = (Map.Entry)it.next();
            //              System.out.print("Key is: "+me.getKey() + " & ");
            //              System.out.println("Value is: "+me.getValue());
            //            } 

            // a.getoddsInfoForDouble();
            /*
             * action:put_money phaseid:165921 oddsid:1,2 uPI_P:9.81,9.81 uPI_M:10,10 i_index:0,1
             * JeuValidate:11170449105207 playpage:pk10_d1_10
             * 
             * 
             * action:put_money phaseid:165921 oddsid:17,146 uPI_P:9.81,9.81 uPI_M:10,10 i_index:0,1
             * JeuValidate:11170449214316 playpage:pk10_d1_10
             */

            //			String a = new SimpleDateFormat("MMddhhmmsssSSS").format(new Date());
            //
            //			String bet ="http://mem1.fpaesf109.pasckr.com:88/L_PK10/Handler/Handler.ashx" +
            //			"?action=put_money&" +
            //			"phaseid="+phaseid+"&" +
            //			"oddsid=1&" +
            //			"uPI_P=9.81&" +
            //			"uPI_M=10&" +
            //			"i_index=0&" +
            //			//"JeuValidate="+a+"&" +
            //			"playpage=pk10_d1_10" ;

            /*
             * 
             * action:put_money phaseid:163754 oddsid:2 uPI_P:9.81 uPI_M:10 i_index:0 JeuValidate:11050308464138
             * playpage:pk10_d1_10
             */
            //649096
            //phaseid 163755
            //			;
            //			String v = httpClientCookieGet(bet);
            //			JsonParser parser = new JsonParser();
            //			JsonObject o = parser.parse(v).getAsJsonObject();
            //			JsonObject data = o.getAsJsonObject("data");
            //			String t = data.get("JeuValidate").getAsString();
            //			String aa ="http://mem1.fpaesf109.pasckr.com:88/L_PK10/Handler/Handler.ashx" +
            //					"?action=put_money&" +
            //		            "phaseid="+phaseid+"&" +
            //					"oddsid=2&" +
            //					"uPI_P=9.81&" +
            //					"uPI_M=10&" +
            //					"i_index=0&" +
            //					"JeuValidate="+t+"&" +
            //					"playpage=pk10_d1_10" ;
            //			httpClientCookieGet(aa);
            //action=put_money&phaseid=163760&oddsid=1&uPI_P=9.81&uPI_M=10&i_index=0&JeuValidate=11050344302630&playpage=pk10_d1_10
            //	action=put_money&phaseid=163760&oddsid=1&uPI_P=9.81&uPI_M=10&i_index=0&JeuValidate=11050343054559&playpage=pk10_d1_10
            //
            //action:put_money
            //phaseid:163752
            //oddsid:1
            //uPI_P:9.81
            //uPI_M:10
            //i_index:0
            //JeuValidate:11050301105529
            //playpage:pk10_d1_10
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //單球
//    public String query() {
//        String query = uraal[urli % 5] + "/Handler/QueryHandler.ashx?action=get_ad";
//        try {
//            return instance.httpClientUseCookie(query);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    //兩面ＺＲＵＦ
//    public String getoddsInfoForDouble() {
//        String query = uraal[urli % 5]
//                       + "/L_PK10/Handler/Handler.ashx?action=get_oddsinfo&playid=2%2C3%2C4%2C6%2C7%2C8%2C10%2C11%2C12%2C14%2C15%2C16%2C18%2C19%2C20%2C22%2C23%2C25%2C26%2C28%2C29%2C31%2C32%2C34%2C35%2C37%2C38&playpage=pk10_lmp";
//        try {
//            return instance.httpClientUseCookie(query);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

//    public String getoddsInfo() {
//        String query = uraal[urli % 5]
//                       + "/L_PK10/Handler/Handler.ashx?action=get_oddsinfo&playid=1%2C5%2C9%2C13%2C17%2C21%2C24%2C27%2C30%2C33&playpage=pk10_d1_10";
//        try {
//            return instance.httpClientUseCookie(query);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

//    public synchronized String normalBet(String phaseid, String ossid, String pl, String i_index, String m,
//                                         String type) {
//
//        String query = uraal[urli % 5] + "/L_PK10/Handler/Handler.ashx?action=put_money&";
//        try {
//            String force = Utils.httpClientGet(forceUrl);
//            if (force.equals("1")) {
//                return "";
//            }
//            query += "phaseid=" + phaseid + "&" + "oddsid=" + ossid.substring(0, ossid.length() - 1) + "&" + "uPI_P="
//                     + pl.substring(0, pl.length() - 1) + "&" + "uPI_M=" + m.substring(0, m.length() - 1) + "&"
//                     + "i_index=" + i_index.substring(0, i_index.length() - 1) + "&playpage=" + type + "";
//
//            //System.out.println(query);
//            String v = instance.httpClientUseCookie(query);
//            JsonParser parser = new JsonParser();
//            JsonObject o = parser.parse(v).getAsJsonObject();
//            JsonObject data = o.getAsJsonObject("data");
//            String t = data.get("JeuValidate").getAsString();
//            return instance.httpClientUseCookie(query + "&JeuValidate=" + t);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    
    
    
    //"http://w1.5a1234.com/?m=acc&gameId=2"
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

    public static String httpPostGetToken(String url, String PHPSESSID_COOKIE
                                          , String ValidateCode
                                          , String loginName
                                          , String loginPwd) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Cookie", PHPSESSID_COOKIE);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("type", "1"));
        nvps.add(new BasicNameValuePair("ism", "0"));
        nvps.add(new BasicNameValuePair("loginName", loginName));
        nvps.add(new BasicNameValuePair("loginPwd", loginPwd));
        nvps.add(new BasicNameValuePair("ValidateCode", ValidateCode));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        String cookie = setCookie(response);
       
        try {
            return cookie.substring(0, cookie.indexOf(";")+1);//token=10112e4e12aa494db03ea2462302d34d;
        } catch(Exception e){
            
        }finally {
            
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
