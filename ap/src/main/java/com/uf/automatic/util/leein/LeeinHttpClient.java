package com.uf.automatic.util.leein;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
import java.util.Timer;
import java.util.TimerTask;
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
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uf.automatic.util.leein.*;
import com.uf.automatic.controller.Controller;
import com.uf.automatic.util.Utils;

public class LeeinHttpClient {
    private static String id;
    private static String staticpassword;
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
 
    private static void run() {
        Timer timer = new Timer();
        NewTimerTask timerTask = new NewTimerTask();
        // 程序运行后立刻执行任务，每隔100ms执行一次
        timer.schedule(timerTask, 0, 10000);
    }
    
    
    
    static String  leein_php_cookid = "2a29530a2306=b00b0a238f1bb76547c75c442ce5bc273859ad7904b7bc3e;";
    static String  futsai_php_cookid = "2a29530a2306=b00b0a238f1bb76547c75c442ce5bc273859ad7904b7bc3e;";
    
    
    
    static String futsai_url[] = { "https://1329036172-fcs.cp168.ws", "https://9402075398-fcs.cp168.ws",
                            "https://3390418965-fcs.cp168.ws", "https://0736268923-fcs.cp168.ws" };
     static int futsai_index = 0;
     static CloseableHttpClient httpclient = new DefaultHttpClient();
     static BasicCookieStore cookieStore = new BasicCookieStore();
     static String firmcName = "";
     static String boardName = "XYFT";
    public static boolean checkCookie = false;
    // static Cookie initCookie  = new Cookie(firmcName, null);
    public static void main(String[] args) {
        try {
            
//            httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
//
//            String firCookie = LeeinHttpClient.getFirstCookie(futsai_url[futsai_index % 4]);
//            String secCookie = LeeinHttpClient.getSecondCookie(futsai_url[futsai_index % 4]);
//            BasicClientCookie cookie = new BasicClientCookie("defaultLT", boardName);
//            cookieStore.addCookie(cookie);
//            LeeinHttpClient.httpPostInit(futsai_url[futsai_index % 4], "1111", "asd1212", "aSD123123");
//            LeeinHttpClient.index(futsai_url[futsai_index % 4]);
            
//            LeeinHttpClient.params(futsai_url[futsai_index % 4],"XYFT");
//            LeeinHttpClient.lasts(futsai_url[futsai_index % 4],"XYFT");
//
//            LeeinHttpClient.LoadPage(futsai_url[futsai_index % 4],"XYFT");
             initPage("asd1212", "aSD123123");
             LeeinHttpClient.getfirst12Phase();
             JsonObject ret = LeeinHttpClient.getTodayWin();
             
//             JsonParser parser = new JsonParser();
//  LeeinHttpClient.getfirst12Phase();
//            JsonArray o = parser.parse(ret).getAsJsonArray();
//        
//            JsonObject r = o.get(0).getAsJsonObject();
            System.out.println(ret);
            
            JsonObject ret1 = LeeinHttpClient.getLastPhase();
            JsonObject ret2 = LeeinHttpClient.getNextTime();
            System.out.println(ret1);
            System.out.println(ret2);
//            String betPhase = ret2.get("nextDrawNumber").getAsString();
//            System.out.println(betPhase);
//
//            JsonObject pl = LeeinHttpClient.getPl();
//            System.out.println(pl);
//
//            JsonArray a = new JsonArray();
//            String code[] = {"1","2"} ;
//            String sn = "1";
//            String amount = "1";
//            for (String str : code) {
//                String key = "B" + sn + "_" + str;
//                BigDecimal p = pl.get(key).getAsBigDecimal();
//                JsonObject d = new JsonObject();
//                d.addProperty("game", "B" + sn);
//                d.addProperty("contents", str);
//                d.addProperty("amount", amount);
//                d.addProperty("odds", p);
//                a.add(d);
//            }
//            
//            
//            //bet
//            JsonObject bet = new JsonObject();
//            bet.addProperty("lottery", boardName);
//            bet.addProperty("drawNumber", betPhase);
//            bet.add("bets", a);
//            bet.addProperty("ignore", "false");
//
//            String betS = bet.toString();
//            
//            
//            JsonObject result111 = LeeinHttpClient.httpPostBet(betS);
//            System.out.println(result111);

//            String usable_credit = r.get("balance").getAsString();
//            String unbalancedMoney = r.get("result")==null?"0":r.get("result").getAsString();
//            j.addProperty("usable_credit", Double.parseDouble(df.format(Double.valueOf(usable_credit))));
//            j.addProperty("todayWin", Double.parseDouble(df.format(Double.valueOf(unbalancedMoney))));
            
          
          
//            leein_php_cookid = LeeinHttpClient.httpPostInit("https://0164955479-sy.cp168.ws",
//                                                                   leein_php_cookid,
//                                                            "1111",
//                                                            "yy6611",
//                                                            "asd123123");
//            futsai_php_cookid = LeeinHttpClient.httpPostInit("https://1329036172-fcs.cp168.ws",
//                                                            futsai_php_cookid,
//                                                     "1111",
//                                                     "bee3311",
//                                                     "asd123123");
//            
//            run();
       
            
            
            
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public synchronized static void initPage() throws Exception {
        if(!id.isEmpty() && !staticpassword.isEmpty())
            initPage(id, staticpassword);
    }
    
    public synchronized static void initPage(String user,String password) throws Exception {
        id = user;
        staticpassword = password;
        futsai_index++;
        httpclient = new DefaultHttpClient();
        cookieStore = new BasicCookieStore();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        String firCookie = LeeinHttpClient.getFirstCookie(futsai_url[futsai_index % 4]);
        String secCookie = LeeinHttpClient.getSecondCookie(futsai_url[futsai_index % 4]);
        BasicClientCookie cookie = new BasicClientCookie("defaultLT", boardName);
        cookieStore.addCookie(cookie);
        //LeeinHttpClient.httpPostInit(futsai_url[futsai_index % 4], "1111", "asd1212", "aSD123123");
        LeeinHttpClient.httpPostInit(futsai_url[futsai_index % 4], "1111", id, staticpassword);
        LeeinHttpClient.index(futsai_url[futsai_index % 4]);
        Controller.saveLog("initPage", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " : " + futsai_url[futsai_index % 4]);
        
        checkCookie = true;
    
    }
    
    public static String testfuTodayWin() throws Exception {
        String u = "https://1329036172-fcs.cp168.ws" + "/member/accounts?_=1522119266532";
        
        return httpGet(futsai_php_cookid);
    
    
    }

    
    // 兩面ＺＲＵＦ
     public synchronized static JsonObject getTodayWin() throws Exception {
         
       
         httpclient = new DefaultHttpClient();
         httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
         Timestamp timestamp = new Timestamp(System.currentTimeMillis());

         String u = futsai_url[futsai_index % 4] + "/member/accounts?_=" + timestamp.getTime();
         
         String result =  httpGet(u);
         JsonParser parser = new JsonParser();

         JsonArray o = parser.parse(result).getAsJsonArray();
     
         JsonObject r = o.get(0).getAsJsonObject();
         //System.out.println(r);
         String usable_credit = r.get("balance").getAsString();
         String unbalancedMoney = r.get("result")==null?"0":r.get("result").getAsString();
         JsonObject n = new JsonObject();
         n.addProperty("usable_credit", usable_credit);
         n.addProperty("unbalancedMoney", unbalancedMoney);
         return n;
     
     
     }
     
     public synchronized static JsonObject getLastPhase() throws Exception {
         httpclient = new DefaultHttpClient();
         httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
         Timestamp timestamp = new Timestamp(System.currentTimeMillis());

         String u = futsai_url[futsai_index % 4] + "/member/lastResult?lottery="+boardName+"&_=" + timestamp.getTime();
         HttpGet HttpGet = new HttpGet(u);
         
         CloseableHttpResponse response = httpclient.execute(HttpGet);
         try {

             String content = EntityUtils.toString(response.getEntity());
             JsonParser parser = new JsonParser();

             JsonObject r = parser.parse(content).getAsJsonObject();
         
            // JsonObject r = o.get(0).getAsJsonObject();
             //System.out.println(r);
             String drawNumber = r.get("drawNumber").getAsString();
             String code = r.get("result").getAsString();
             JsonObject n = new JsonObject();
             n.addProperty("drawNumber", drawNumber);
             n.addProperty("code", code);
             return n;

         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             response.close();
         }
         
       
         return null;
     
     
     }
     public static boolean checkWrite12 = false;
     public synchronized static JsonObject getfirst12Phase() throws Exception {
         httpclient = new DefaultHttpClient();
         httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
         //Timestamp timestamp = new Timestamp(System.currentTimeMillis());

         String u = futsai_url[futsai_index % 4] + "/member/dresult?lottery="+boardName;
         HttpGet HttpGet = new HttpGet(u);
         
         CloseableHttpResponse response = httpclient.execute(HttpGet);
         try {

             String content = EntityUtils.toString(response.getEntity());
             
             
             Document doc = Jsoup.parse(content);
             JsonObject c = new JsonObject();
             //JSONArray list = new JSONArray();
             int check = 0;
             for (Element table : doc.select("table")) {
                 for (Element tb : table.select("tbody")) { 
                     for (Element row : tb.select("tr")) {
                         Elements tds = row.select("td");
                         String phase = tds.get(0).text();
                         Long p = Long.valueOf(phase.substring(8,11));
                         if(p>12)
                             continue;
                         String c1 = tds.get(2).text();
                         String c2 = tds.get(3).text();
                         String c3 = tds.get(4).text();
                         String c4 = tds.get(5).text();
                         String c5 = tds.get(6).text();
                         String c6 = tds.get(7).text();
                         String c7 = tds.get(8).text();
                         String c8 = tds.get(9).text();
                         String c9 = tds.get(10).text();
                         String c10 = tds.get(11).text();
                         String totalcode = c1 + "," + c2  + "," + c3 + "," + c4
                                 + "," + c5 + "," + c6 + "," + c7+ "," + c8 + "," + c9 + "," + c10;
                         Utils.WritePropertiesFile("history", phase, totalcode);
                         check ++;
                     }
                     if(check ==12)
                         checkWrite12 = true;
//                     JSONObject jsonObject = new JSONObject();
                      
//                     String Group = tds.get(1).text();
//                     String Code = tds.get(2).text();
//                     String Lesson = tds.get(3).text();
//                     String Day1 = tds.get(4).text();
//                     String Day2 = tds.get(5).text();
//                     String Day3= tds.get(6).text();        
//                     jsonObject.put("Group", Group);
//                     jsonObject.put("Code", Code);
//                     jsonObject.put("Lesson", Lesson);
//                     jsonObject.put("Day1", Day1);
//                     jsonObject.put("Day2", Day2);
//                     jsonObject.put("Day3", Day3);
//                     jsonParentObject.put(Name,jsonObject);
                  }
             }
        // return jsonParentObject.toString();
             
             
             
             return c;

         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             response.close();
         }
         
       
         return null;
     
     
     }
     
    public synchronized static JsonObject getNextTime() throws Exception {
        httpclient = new DefaultHttpClient();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
         Timestamp timestamp = new Timestamp(System.currentTimeMillis());

         String u = futsai_url[futsai_index % 4] + "/member/period?lottery="+boardName+"&games=DX1%2CDX2%2CDX3%2CDX4%2CDX5%2CDX6%2CDX7%2CDX8%2CDX9%2CDX10%2CDS1%2CDS2%2CDS3%2CDS4%2CDS5%2CDS6%2CDS7%2CDS8%2CDS9%2CDS10%2CGDX%2CGDS%2CLH1%2CLH2%2CLH3%2CLH4%2CLH5&_=" + timestamp.getTime();
         HttpGet HttpGet = new HttpGet(u);
         
         CloseableHttpResponse response = httpclient.execute(HttpGet);

         try {

             String content = EntityUtils.toString(response.getEntity());
             JsonParser parser = new JsonParser();

             JsonObject r = parser.parse(content).getAsJsonObject();
         
             //JsonObject r = o.get(0).getAsJsonObject();
             //System.out.println(r);
             String nextDrawNumber = r.get("drawNumber").getAsString();
             String nextOpenTime = r.get("drawTime").getAsString();
             String currentTime = r.get("currentTime").getAsString();
             
           
             
             //
             DecimalFormat df = new DecimalFormat("##");

             Long nextT = Long.parseLong(df.format(Long.valueOf(nextOpenTime)));

             Date date = new Date();
//             Long nowT = Long.parseLong(df.format(Long.valueOf(date.getTime())));
             Long nowT = Long.parseLong(df.format(Long.valueOf(currentTime)));
             //date.setTime(tt);
             //String formattedDate = new SimpleDateFormat("yyyyMMdd").format(date);
             Long diff = nextT - nowT;
             Long diffSeconds = diff / 1000;
             Long min = diffSeconds / 60;
             Long sec = diffSeconds % 60;
              
             JsonObject n = new JsonObject();
             n.addProperty("nextDrawNumber", nextDrawNumber);
             n.addProperty("nextOpenTime", nextOpenTime);
             n.addProperty("min", min.toString());
             n.addProperty("sec", sec.toString());
             return n;

         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             response.close();
         }

         return null;
     
     
     }
 
    // "http://w1.5a1234.com/?m=acc&gameId=2"
    public synchronized static String httpGet(String url) throws Exception {
        RequestConfig requestConfig = RequestConfig.custom()  
                .setConnectTimeout(15000).setConnectionRequestTimeout(15000)  
                .setSocketTimeout(5000).build();
      
        
        //CloseableHttpClient httpclient = HttpClients.createDefault();
//        httpclient = new DefaultHttpClient();
//        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        httpclient = new DefaultHttpClient();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet HttpGet = new HttpGet(url);
        HttpGet.setConfig(requestConfig);
       // HttpGet.setHeader("Cookie", cookie);

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

    public static JsonObject httpPostBet( String betString) throws Exception {

        httpclient = new DefaultHttpClient();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        //CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(futsai_url[futsai_index % 4] + "/member/bet");

        //httpPost.setHeader("Cookie", cookie);

        httpPost.setHeader("content-type", "application/json");
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
            e.printStackTrace();
        } finally {
            response.close();
        }
        return null;
    }
    
    public static String getFirstCookie(String url) throws Exception {
        httpclient = new DefaultHttpClient();
       // CloseableHttpClient httpclient = HttpClients.createDefault();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        url = url + "/code?_=" + timestamp.getTime() ;

        HttpGet httpPost = new HttpGet(url);
         //httpPost.setHeader("Cookie", PHPSESSID_COOKIE);

       
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
              List<Cookie> cookies = cookieStore.getCookies();
//            String c = "";
            if (cookies.isEmpty()) {
                //out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
//                    System.out.println("Cookie-" + i + "==" + cookies.get(i).toString());
                    //c = cookies.get(i).toString();
                    firmcName = cookies.get(i).getName();
                            
                }
            }
 
//            Header[] requestheaders =   response.getAllHeaders();
//            System.out.println(requestheaders);
//            
//            Header[] headers = response.getHeaders("Set-Cookie");
//            
//            String c = "";
//            for (Header h : headers) {
//               // cookieString+=h.getValue().toString()+";";
//                c = (h.getValue().toString()); 
//            }
            
//            return c ;

        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
       
        return null;
    }
    
    public static String getSecondCookie(String url) throws Exception {
       // BasicCookieStore cookieStore = new BasicCookieStore();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

//         httpclient = HttpClientBuilder.create()
//        .setRedirectStrategy(new LaxRedirectStrategy()).build();
       // Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        url = url + "/ssid1?url=/default/js/qrcode.min.js" ;
        //httpclient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true); 

        HttpGet httpPost = new HttpGet(url);
//       // httpPost.setHeader("Cookie", fC);
//        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
//        httpPost.setHeader("Sec-Fetch-Mode", "no-cors");
//        httpPost.setHeader("Sec-Fetch-Site", "same-origin");
//        httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
//        httpPost.setHeader("Accept-Language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7");

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            

        }catch(Exception e) {
            System.out.println(e.getMessage());
            Header[] requestheaders =   response.getAllHeaders();
            System.out.println(requestheaders);
            e.printStackTrace();
        } finally {
           // response.close();
        }
       
        return null;
    }
    
    
    public synchronized static String httpPostInit(String url, String ValidateCode, String loginName,
                                      String loginPwd) throws Exception {

       // CloseableHttpClient httpclient = HttpClients.createDefault();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost(url + "/login");

        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        //Content-Type: application/x-www-form-urlencoded
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("type", "1"));
        nvps.add(new BasicNameValuePair("account", loginName));
        nvps.add(new BasicNameValuePair("password", loginPwd));
        nvps.add(new BasicNameValuePair("code", ValidateCode));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {

          Header[] requestheaders =   response.getAllHeaders();
          System.out.println(requestheaders);
            
            Header headers[] = response.getHeaders("location");
            if (headers == null || headers.length == 0) {

                return null;
            }
            String location = "";

            for (Header h : headers) {
                location += h.getValue().toString();
            }

            if (!location.equals("")) {
                //String _OLID_ = location.substring(location.indexOf("=")+1,location.length()) ;
        
                //BasicClientCookie cookie = new BasicClientCookie(firmcName, _OLID_);
                //cookieStore.addCookie(cookie);
                httpGetAgreementdirect(url+"/"+location);
                return location;
            }

        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            //response.close();
        }
       
        return null;
    }

    public synchronized static void httpGetAgreementdirect(String url) throws Exception {

        //CloseableHttpClient httpclient = HttpClients.createDefault();

       HttpGet httpGet = new HttpGet(url);
 
       CloseableHttpResponse response = null;
       try {
           response = httpclient.execute(httpGet);
 
       }catch(Exception e) {

       } finally {
          // response.close();
       }
       
       httpGetAgreement(url);
    }
    
    public synchronized static void httpGetAgreement(String url) throws Exception {

        //CloseableHttpClient httpclient = HttpClients.createDefault();

       HttpGet httpGet = new HttpGet(url + "/member/agreement");
 
       CloseableHttpResponse response = null;
       try {
           response = httpclient.execute(httpGet);
 
       }catch(Exception e) {

       } finally {
          // response.close();
       }
       
     
    }
    
    public synchronized static void index(String url) throws Exception {

        //CloseableHttpClient httpclient = HttpClients.createDefault();
        httpclient = new DefaultHttpClient();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
       HttpGet httpGet = new HttpGet(url + "/member/index");
       
       CloseableHttpResponse response = null;
       try {
           response = httpclient.execute(httpGet);

       }catch(Exception e) {

       } finally {
          // response.close();
       }
       
     
    }
    
    
   
    
    public static void params(String url,String type) throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        httpclient = new DefaultHttpClient();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        //CloseableHttpClient httpclient = HttpClients.createDefault();

       HttpGet httpGet = new HttpGet(url + "/member/params?_="+timestamp.getTime());
 
       CloseableHttpResponse response = null;
       try {
           response = httpclient.execute(httpGet);
 
       }catch(Exception e) {

       } finally {
          // response.close();
       }
        
    }
    
    public static void lasts(String url,String type) throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //CloseableHttpClient httpclient = HttpClients.createDefault();
        httpclient = new DefaultHttpClient();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
       HttpGet httpGet = new HttpGet(url + "/member/lasts?lottery="+type+"&_="+timestamp.getTime());
        
       CloseableHttpResponse response = null;
       try {
           response = httpclient.execute(httpGet);
           String content = EntityUtils.toString(response.getEntity());
           System.out.println(content);
       }catch(Exception e) {

       } finally {
          // response.close();
       }
        
    }
    
    public static void LoadPage(String url,String type) throws Exception {

        //CloseableHttpClient httpclient = HttpClients.createDefault();
        httpclient = new DefaultHttpClient();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
       HttpGet httpGet = new HttpGet(url + "/member/load?lottery="+type+"&page=lm");
 
       CloseableHttpResponse response = null;
       try {
           response = httpclient.execute(httpGet);
 
       }catch(Exception e) {

       } finally {
          // response.close();
       }
        
    }
    
    public static JsonObject getPl() throws IOException {
          Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //CloseableHttpClient httpclient = HttpClients.createDefault();
        httpclient = new DefaultHttpClient();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
       HttpGet httpGet = new HttpGet(futsai_url[futsai_index % 4] + "/member/odds?lottery="+boardName+"&games=B1%2CB2%2CB3%2CB4%2CB5%2CB6%2CB7%2CB8%2CB9%2CB10&_="+timestamp.getTime());
        
       CloseableHttpResponse response = null;
       try {
           response = httpclient.execute(httpGet);
           String content = EntityUtils.toString(response.getEntity());
           JsonParser parser = new JsonParser();

           JsonObject r = parser.parse(content).getAsJsonObject();
           
           return r;
       }catch(Exception e) {

       } finally {
          // response.close();
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
        return staticpassword;
    }

    public void setPassword(String password) {
        this.staticpassword = password;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

}

class NewTimerTask extends TimerTask {
    
    @Override
    public void run() {
        String ret;
        try {
//            ret = LeeinHttpClient.testTodayWin();
//            System.out.println(ret);
//            
//            ret = LeeinHttpClient.testfuTodayWin();
//
//            System.out.println(ret);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}