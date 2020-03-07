package com.uf.automatic.util;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.sql.Timestamp;
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

import javax.xml.bind.DatatypeConverter;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
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
import com.uf.automatic.util.leein.LeeinHttpClient;

 
public class httpClientCookie {
    private static String id;
    private static String password;
    private String cookie = null;
    
    private static httpClientCookie instance;
    private static String checkStartUrl = "https://drive.google.com/uc?export=download&id=17PsTMGjyNnGga5wWjZ4CQS6dOm4OdcxF";
    private static String forceUrl = "http://220.132.126.216:9999/getForce";
    private static String startFlag="";
    public static String checkStart() {
        String flag = "N";
        try {
            flag = Utils.httpClientGet(checkStartUrl);
        }catch(Exception e) {
            
        }
        return flag;
    }
 
    public static void mainPage()  throws Exception{
        String url = uraal[urli%5];
        firstGetCookieHttpClientGet(url);
        //Thread.sleep(1000);
        getCookieHttpClientGet(url);
        threeGetCookieHttpClientGet(url);
        
        //System.out.println(security_session_mid_verify);

    }
    public httpClientCookie(String id, String password) throws Exception {
        // TODO Auto-generated constructor stub
        setId(id);
        setPassword(password); 
        
        mainPage();
        
        //urli++;O
        //String d =  uraal[urli%5] + "/Handler/LoginHandler.ashx?action=user_login";
        
        String urla = uraal[urli%5] + "/Handler/LoginHandler.ashx?action=user_login";
        
        Map params = new HashMap<String,String>();
        
        params.put("loginName", id);
        params.put("loginPwd", password);
        params.put("ValidateCode", "");
        setInitCookie(urla, params);//塞 cookie
    }
    
    public synchronized static void Login(String id, String password, String url) throws Exception {
        // TODO Auto-generated constructor stub
//        setId(id);
//        setPassword(password); 
//        
//        mainPage();
        
        //urli++;O
        //String d =  uraal[urli%5] + "/Handler/LoginHandler.ashx?action=user_login";
        
        String urla = url + "/Handler/LoginHandler.ashx?action=user_login";
        
        Map params = new HashMap<String,String>();
        
        params.put("loginName", id);
        params.put("loginPwd", password);
        params.put("ValidateCode", "");
        getCookieHttpClient(urla, params);
    }
    
    public  static synchronized String getoddsInfo_boat() {
        //String query = uraal[urli%5] + "/L_XYFT5/Handler/Handler.ashx?action=get_oddsinfo&playid=1%2C5%2C9%2C13%2C17%2C21%2C24%2C27%2C30%2C33&playpage=xyft5_d1_10";
        String query = uraal[urli%5] + "/L_CAR168/Handler/Handler.ashx";
       
        try {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            form.add(new BasicNameValuePair("action", "get_oddsinfo"));
            form.add(new BasicNameValuePair("playid", "1,5,9,13,17,21,24,27,30,33"));
            form.add(new BasicNameValuePair("playpage", "car168_d1_10"));
            UrlEncodedFormEntity formdata = new UrlEncodedFormEntity(form, Consts.UTF_8);
            
            RequestConfig requestConfig = RequestConfig.custom()  
                    .setConnectTimeout(2000).setConnectionRequestTimeout(2000)  
                    .setSocketTimeout(5000).build();
            
            
            
//            BasicCookieStore cookieStore = new BasicCookieStore();
//            HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

           

            //CloseableHttpClient httpClient = HttpClients.createDefault();
//            HttpClientContext context = HttpClientContext.create();
//            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
            
            HttpPost httpget = new HttpPost(query);
            httpget.setEntity(formdata);

           // httpget.setConfig(requestConfig);

           // httpget.setHeader("Cookie",cookie );
     
            
            String result = null; 
            try {
                httpget.setConfig(requestConfig);
                HttpResponse httpresponse = httpclient.execute(httpget);
                HttpEntity entity = httpresponse.getEntity();
                result = EntityUtils.toString(entity);
//              System.out.println(result);
                
            } catch (Exception e) {
                throw e;
            } finally {
              //  httpClient.close();
            }
            
            
            
            return result;
            
            //return instance.httpClientUseCookie(query);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    
  
    
     
    
    public static synchronized String normalBet_boat(String phaseid,String ossid,  String pl , String i_index , String m ,String type) {
        
        
        String query = uraal[urli%5] + "/L_CAR168/Handler/Handler.ashx";
        try {
           

            Map params = new HashMap<String,String>();
            
            params.put("action", "put_money");
            params.put("phaseid", phaseid);
            params.put("oddsid", ossid.substring(0,ossid.length()-1));
            params.put("uPI_P", pl.substring(0,pl.length()-1));
            params.put("uPI_M",  m.substring(0,m.length()-1) );
            params.put("i_index", i_index.substring(0,i_index.length()-1));
            params.put("playpage",type);

            //System.out.println(query);
            String v = httpClientUseCookie(query, params);
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(v).getAsJsonObject();
            JsonObject data = o.getAsJsonObject("data");
            String t = data.get("JeuValidate").getAsString(); 
            params.put("JeuValidate",t);
            return instance.httpClientUseCookie(query, params );
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public synchronized static httpClientCookie getInstance(String id,String password) throws Exception {
        
         try {
             httpclient = new DefaultHttpClient();
             yunsuo_session_verify = "";
             security_session_mid_verify = "";
             //gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
             instance = new httpClientCookie(id,password);

         }catch(Exception e) {
             urli ++;
         }
       
        
        
        return instance;
         
    }
    static String uraal[] = {"http://mem1.cdngjs418.fastumchina.com:88",
                             "http://mem1.cdngjs418.fastumchina.com:88",
                             "http://mem2.cdngjs418.jantree.com:88",
                             "http://mem3.cdngjs418.fastumchina.com:88",
                             "http://mem4.cdngjs418.jantree.com"
                             };

    //sd8885 //Aa258369
    static int urli = 0 ;
    private String setInitCookie(String url, Map params) {
       
        try {
             
            String cookie = getCookieHttpClient(url, params);
            System.out.println("************" + urli);
            System.out.println(cookie);
            if(!cookie.equals("")) {
                setCookie(cookie);
            }else {
                urli++;
                String urla = uraal[urli%5] + "Handler/LoginHandler.ashx";
                setInitCookie(urla, params);
            }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
   
//    public static void testBet(httpClientCookie h) {
//        String ret = h.getoddsInfo();
//        // 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数
//
//        JsonParser parser = new JsonParser();
//        JsonObject o = parser.parse(ret).getAsJsonObject();
//        JsonObject data = o.getAsJsonObject("data");
//        Map<Integer, String> normal = new TreeMap<Integer, String>();
//        Utils.producePl(normal,ret); //產生倍率 for single
// 
//
//        String drawIssue = data.get("nn").getAsString();
//        String p_id = data.get("p_id").getAsString();
//        //System.out.println(normal.toString());
//        String betRet = h.normalBet(p_id, "2,", "9.8", "0,", "1,", "pk10_d1_10");
//        System.out.println(betRet);
//    }
    
   // private static String id;
    private static String staticpassword;
    static BasicCookieStore cookieStore = new BasicCookieStore();

    public static boolean checkCookie = false;
    public synchronized static void initPage() throws Exception {
        if(!id.isEmpty() && !staticpassword.isEmpty())
            initPage(id, staticpassword);
    }
    
    public synchronized static void initPage(String user,String password) throws Exception {
        //urli++;
        id = user;
        staticpassword = password;
        System.out.println("***************");
        System.out.println(uraal[urli%5]);
        httpclient = new DefaultHttpClient();
        cookieStore = new BasicCookieStore();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        String firCookie = httpClientCookie.getFirstCookie(uraal[urli%5]);
        Thread.sleep(3000);
        String secCookie = httpClientCookie.getSecondCookie(uraal[urli%5]);
        Thread.sleep(3000);

        String thirdCookie = httpClientCookie.getThirdCookie(uraal[urli%5]);
        Thread.sleep(3000);

        Login(id,staticpassword,uraal[urli%5]);
       // urli++;
//        BasicClientCookie cookie = new BasicClientCookie("defaultLT", boardName);
//        cookieStore.addCookie(cookie);
//        //LeeinHttpClient.httpPostInit(futsai_url[futsai_index % 4], "1111", "asd1212", "aSD123123");
//        LeeinHttpClient.httpPostInit(futsai_url[futsai_index % 4], "1111", id, staticpassword);
//        LeeinHttpClient.index(futsai_url[futsai_index % 4]);
//        Controller.saveLog("initPage", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " : " + futsai_url[futsai_index % 4]);
//        
        checkCookie = true;
       
    }
    
    public static String getFirstCookie(String url) throws Exception {
       // httpclient = new DefaultHttpClient();
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
       // CloseableHttpClient httpclient = HttpClients.createDefault();
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        
//        url = url + "/code?_=" + timestamp.getTime() ;

        HttpGet httpPost = new HttpGet(url);
         //httpPost.setHeader("Cookie", PHPSESSID_COOKIE);

       
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
//              List<Cookie> cookies = cookieStore.getCookies();
////            String c = "";
//            if (cookies.isEmpty()) {
//                //out.println("None");
//            } else {
//                for (int i = 0; i < cookies.size(); i++) {
////                    System.out.println("Cookie-" + i + "==" + cookies.get(i).toString());
//                    //c = cookies.get(i).toString();
//                    firmcName = cookies.get(i).getName();
//                            
//                }
//            }

        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
       
        return null;
    }
    
    public static String getThirdCookie(String url) throws Exception {
        // httpclient = new DefaultHttpClient();
         httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        // CloseableHttpClient httpclient = HttpClients.createDefault();
//         Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//         
//         url = url + "/code?_=" + timestamp.getTime() ;

         HttpGet httpPost = new HttpGet(url);
         httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
         //Accept: 

        
         CloseableHttpResponse response = httpclient.execute(httpPost);
         try { 

         }catch(Exception e) {
             e.printStackTrace();
         } finally {
             response.close();
         }
        
         return null;
     }
    
    public static String getValidCookie(String url) throws Exception {
        // httpclient = new DefaultHttpClient();
         httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        // CloseableHttpClient httpclient = HttpClients.createDefault();
//         Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//         
//         url = url + "/code?_=" + timestamp.getTime() ;

         HttpGet httpPost = new HttpGet(url+"/LoginValidate.aspx");
         //httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
         //Accept: 

        
         CloseableHttpResponse response = httpclient.execute(httpPost);
         try { 

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
         String c = stringToHex(url+"/");
         
         url = url+ "/?security_verify_data=" + security_verify_data;
         HttpGet httpget = new HttpGet(url);
         BasicClientCookie cookie = new BasicClientCookie("srcurl", c);
         cookieStore.addCookie(cookie);
        // String t = yunsuo_session_verify + " srcurl=" + c;
        // httpget.setHeader("Cookie",t ); 
//          httpclient = HttpClientBuilder.create()
//         .setRedirectStrategy(new LaxRedirectStrategy()).build();
        // Timestamp timestamp = new Timestamp(System.currentTimeMillis());
         
//         url = url + "/ssid1?url=/default/js/qrcode.min.js" ;
         //httpclient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true); 

         HttpGet httpPost = new HttpGet(url);
//        // httpPost.setHeader("Cookie", fC);
//         httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
//         httpPost.setHeader("Sec-Fetch-Mode", "no-cors");
//         httpPost.setHeader("Sec-Fetch-Site", "same-origin");
//         httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
//         httpPost.setHeader("Accept-Language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7");

         CloseableHttpResponse response = null;
         try {
             response = httpclient.execute(httpPost);
             

         }catch(Exception e) { 
         } finally {
            // response.close();
         }
        
         return null;
     }
    public static int computeIndex(String sn, String code) {
        return (Integer.parseInt(sn) - 1) * 10 + (Integer.parseInt(code) - 1);
    }
    
	public static void main(String[] args ){
		try {
		  //  mainPage();
		        initPage("sdf7711","zxc123123");
 		       // httpClientCookie a = httpClientCookie.getInstance("sdf1122","qaz123123");
//	          //  httpClientCookie t = httpClientCookie.getInstance("qq7711","qaz123123");
//	            //Thread.sleep(3000);
//	            //runnable.run();
	            String r = getoddsInfo_boat();
	            System.out.println(r);
               // String r = getOpenBall_boat();
                //System.out.println(r);
                
                //bet
                //String r = h.getoddsInfo_boat();
                // 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数

                JsonParser pr = new JsonParser();
                JsonObject po = pr.parse(r).getAsJsonObject();
                JsonObject data = po.getAsJsonObject("data");
                Map<Integer, String> normal = new TreeMap<Integer, String>();
                Utils.producePl(normal, r); // 產生倍率 for single
                String p_id = data.get("p_id").getAsString();

                // if (ret.indexOf(user) > -1) {
                String code[] = {"1"};
                String sn = "1" ;
                String amount = "2";
                String ossid = "";
                String pl = "";
                String i_index = "";
                String m = "";
                int i = 0;
                for (String str : code) {
                    // String overLog = betphase + "@" + sn + "@" + str + "@" +
                    // formu;
                    // saveOverLog(user, overLog, c);
                    //
                    int index = computeIndex(sn, str);
                    String id_pl = normal.get(index).toString(); // 15@1.963
                    ossid += id_pl.split("@")[0] + ",";
                    pl += id_pl.split("@")[1] + ",";
                    i_index += i + ",";
                    m += amount + ",";
                    i++;
                }

                String betRet = normalBet_boat(p_id, ossid, pl, i_index, m, "xyft5_d1_10");
                JsonParser parser = new JsonParser();
                JsonObject o = parser.parse(betRet).getAsJsonObject();
                String resCode = o.get("success").getAsString();
                System.out.println("resCode" + resCode);
//	            
//	            String open = a.getOpenBall_boat();
//	               System.out.println(open);
//
//                JsonParser parser = new JsonParser();
//                JsonObject o = parser.parse(open).getAsJsonObject();
//		    httpClientCookie a = httpClientCookie.getInstance("sd8885","Aa258369");
//		    httpClientCookie t = httpClientCookie.getInstance("qq7711","qaz123123");
//		    String ret = t.getoddsInfo();
//		    System.out.println(ret);
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
		 /*action:put_money
		    phaseid:165921
		    oddsid:1,2
		    uPI_P:9.81,9.81
		    uPI_M:10,10
		    i_index:0,1
		    JeuValidate:11170449105207
		    playpage:pk10_d1_10
		    
		    
		    action:put_money
            phaseid:165921
            oddsid:17,146
            uPI_P:9.81,9.81
            uPI_M:10,10
            i_index:0,1
            JeuValidate:11170449214316
            playpage:pk10_d1_10
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
			 * action:put_money
				phaseid:163754
				oddsid:2
				uPI_P:9.81
				uPI_M:10
				i_index:0
				JeuValidate:11050308464138
				playpage:pk10_d1_10
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
//	public String query() {
//	    String query = uraal[urli%5] + "/Handler/QueryHandler.ashx?action=get_ad";
//        try {
//            return instance.httpClientUseCookie(query);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//	}
	
//	public String getoddsInfoForDouble() {
//        String query = uraal[urli%5] + "/L_PK10/Handler/Handler.ashx?action=get_oddsinfo&playid=2%2C3%2C4%2C6%2C7%2C8%2C10%2C11%2C12%2C14%2C15%2C16%2C18%2C19%2C20%2C22%2C23%2C25%2C26%2C28%2C29%2C31%2C32%2C34%2C35%2C37%2C38&playpage=pk10_lmp";
//        try {
//            return instance.httpClientUseCookie(query);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
	
//	public String getOpenBall() {
//        String query = uraal[urli%5] + "/L_PK10/Handler/Handler.ashx?action=get_openball&playpage=pk10_lmp";
//        try {
//            return instance.httpClientUseCookie(query);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
	
//	public String getOpenBall_boat() {
//        String query = uraal[urli%5] + "/L_XYFT5/Handler/Handler.ashx?action=get_openball&playpage=xyft5_d1_10";
//        try {
//            return instance.httpClientUseCookie(query);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
	
	public synchronized static String getOpenBall_boat() {
    	String query = uraal[urli%5] + "/L_CAR168/Handler/Handler.ashx";
        
        try {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            form.add(new BasicNameValuePair("action", "get_openball"));
            form.add(new BasicNameValuePair("playpage", "car168_d1_10"));
            UrlEncodedFormEntity formdata = new UrlEncodedFormEntity(form, Consts.UTF_8);
    
            
            
//            BasicCookieStore cookieStore = new BasicCookieStore();
//            HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);
//    
//           
//    
//            //CloseableHttpClient httpClient = HttpClients.createDefault();
//            HttpClientContext context = HttpClientContext.create();
            RequestConfig requestConfig = RequestConfig.custom()  
                    .setConnectTimeout(2000).setConnectionRequestTimeout(2000)  
                    .setSocketTimeout(5000).build();
            
            HttpPost httpget = new HttpPost(query);
            httpget.setEntity(formdata);
    
            httpget.setConfig(requestConfig);
    
//            httpget.setHeader("Cookie",cookie );
     
            
            String result = null; 
            try {
                HttpResponse httpresponse = httpclient.execute(httpget);
                HttpEntity entity = httpresponse.getEntity();
                result = EntityUtils.toString(entity);
    //          System.out.println(result);
                
            } catch (Exception e) {
                throw e;
            } finally {
               // httpClient.close();
            }
            
            
            
            return result;
            
            //return instance.httpClientUseCookie(query);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
	
	
//	public String getoddsInfo() {
//        String query = uraal[urli%5] + "/L_PK10/Handler/Handler.ashx?action=get_oddsinfo&playid=1%2C5%2C9%2C13%2C17%2C21%2C24%2C27%2C30%2C33&playpage=pk10_d1_10";
//        try {
//            return instance.httpClientUseCookie(query);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
	
	public synchronized String normalBet(String phaseid,String ossid,  String pl , String i_index , String m ,String type) {
	    
	     
	    String query = uraal[urli%5] + "/L_PK10/Handler/Handler.ashx";
        try {
           
            Map params = new HashMap<String,String>();
            
            params.put("action", "put_money");
            params.put("phaseid", phaseid);
            params.put("oddsid", ossid.substring(0,ossid.length()-1));
            params.put("uPI_P", pl.substring(0,pl.length()-1));
            params.put("uPI_M",  m.substring(0,m.length()-1) );
            params.put("i_index", i_index.substring(0,i_index.length()-1));
            params.put("playpage",type);
            
            
//            query += "phaseid="+ phaseid+"&" +
//                     "oddsid="+ ossid.substring(0,ossid.length()-1) + "&" +
//                     "uPI_P="+ pl.substring(0,pl.length()-1) + "&" +
//                     "uPI_M="+ m.substring(0,m.length()-1) + "&" +
//                     "i_index="+ i_index.substring(0,i_index.length()-1) + "&playpage="+type+"" ;

            //System.out.println(query);
            String v = instance.httpClientUseCookie(query, params);
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(v).getAsJsonObject();
            JsonObject data = o.getAsJsonObject("data");
            String t = data.get("JeuValidate").getAsString(); 
            
            params.put("JeuValidate",t);

            return instance.httpClientUseCookie(query, params );
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
	}
	
	
	public static String httpClientUseCookie(String uri, Map params) throws Exception {
		//BasicCookieStore cookieStore = new BasicCookieStore();
		//HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

	   

		//CloseableHttpClient httpClient = HttpClients.createDefault();
		//HttpClientContext context = HttpClientContext.create();
		//RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
	    RequestConfig requestConfig = RequestConfig.custom()  
                .setConnectTimeout(2000).setConnectionRequestTimeout(2000)  
                .setSocketTimeout(5000).build();
	    
		HttpPost httpPost = new HttpPost(uri);
		
		  List<NameValuePair> nvps = new ArrayList<NameValuePair>();
          for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
          String name = (String) iter.next();
          String value = String.valueOf(params.get(name));
          nvps.add(new BasicNameValuePair(name, value));
           
          }
          httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

          httpPost.setConfig(requestConfig);

       //   httpPost.setHeader("Cookie",cookie );
 
		
		String result = null; 
		try {
			HttpResponse httpresponse = httpclient.execute(httpPost);
			HttpEntity entity = httpresponse.getEntity();
			result = EntityUtils.toString(entity);
//			System.out.println(result);
			
		} catch (Exception e) {
			throw e;
		} finally {
			//httpClient.close();
		}
		return result;
	}
	
	public synchronized static String getCookieHttpClient(String uri, Map params) throws Exception {
          
	    //RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
       // System.out.println(uri);
        //HttpGet httpPost = new HttpGet(uri);
        httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

       // String c = stringToHex(uraal[urli%5]+"/");
        HttpPost httpPost = new HttpPost(uri );
        //httpPost.setHeader("Cookie",yunsuo_session_verify  + "srcurl=" + c + ";"+security_session_mid_verify); 
      
        
        //httpget.setHeader("Cookie",yunsuo_session_verify); 
        
        String result = null;
        String cookieString="";
        try {
            
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String value = String.valueOf(params.get(name));
            nvps.add(new BasicNameValuePair(name, value));
             
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            //HttpClient httpclient = new DefaultHttpClient();

            HttpResponse httpresponse = httpclient.execute(httpPost);
            
            HttpEntity entity = httpresponse.getEntity();
            result = EntityUtils.toString(entity);
            //System.out.println(result);
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(result).getAsJsonObject();
            String code = o.get("success").getAsString();
            System.out.println(code);
            if(!code.equals("200")) { 
                
            }
           
            
        } catch (Exception e) {
            
        } finally {
            //httpClient.close();
        }
        return cookieString;
    } 
	
	
	static String yunsuo_session_verify = "";
	static String security_session_mid_verify = "";
	public synchronized static void   firstGetCookieHttpClientGet(String uri) throws Exception {
        //HttpClient httpclient = new DefaultHttpClient();
       
        
        HttpGet httpget = new HttpGet(uri +"/");
        //httpget.setHeader("Cookie","srcurl=" + c + ";"); 

        HttpResponse response = httpclient.execute(httpget);
       

        Header[] headers1 =  response.getAllHeaders();
        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header h : headers) {
            yunsuo_session_verify = h.getValue().toString().split(";")[0]+";";
        
        }
         
         
        //httpClientContext.getCookieStore().clear();
       // System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        String  result = EntityUtils.toString(entity);
       
        System.out.println(result);
        //httpclient.close();
       // return cookieString;
    } 
	
	public synchronized static void   threeGetCookieHttpClientGet(String uri)  {
        //HttpClient httpclient = new DefaultHttpClient();
       try {
           String c = stringToHex(uri+"/");
           HttpGet httpget = new HttpGet(uri +"/");
           httpget.setHeader("Cookie",yunsuo_session_verify  + "srcurl=" + c + ";"+security_session_mid_verify); 
           httpclient = new DefaultHttpClient();
           HttpResponse response = httpclient.execute(httpget);
          

           Header[] headers1 =  response.getAllHeaders();
           Header[] headers = response.getHeaders("Set-Cookie");
           for (Header h : headers) {
               yunsuo_session_verify=h.getValue().toString().split(";")[0]+";";
           
           }
            
            
           //httpClientContext.getCookieStore().clear();
          // System.out.println(response.getStatusLine());
           HttpEntity entity = response.getEntity();
           String  result = EntityUtils.toString(entity);
           
           if(result.indexOf("security_verify_data=") > -1) {
               mainPage();
           }
           System.out.println(result);
       }catch(Exception e) {
           System.out.println(e.getMessage());
       }
	   
        //httpclient.close();
       // return cookieString;
    } 
	 //static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
     static int width = 1400;
     static int height = 900;
     static String security_verify_data = stringToHex(width+","+height);
     
    static CloseableHttpClient httpclient = new DefaultHttpClient();
	public synchronized static String   getCookieHttpClientGet(String uri) throws Exception {
        String c = stringToHex(uri+"/");
       
	    String url = uri+ "/?security_verify_data=" + "313932302c31303830";
        HttpGet httpget = new HttpGet(url);
        
        String t = yunsuo_session_verify + " srcurl=" + c;
        httpget.setHeader("Cookie",t ); 

        HttpResponse response = httpclient.execute(httpget);
        
//        Header[] headers1 =  response.getAllHeaders();
        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header h : headers) {
         //  h.getValue().toString()+";";
            if(h.getValue().toString().indexOf("security_session_mid_verify") ==-1) {
                yunsuo_session_verify=h.getValue().toString().split(";")[0]+";";
                getCookieHttpClientGet(uri);
            }else {
                security_session_mid_verify = h.getValue().toString().split(";")[0]+";";
                return security_session_mid_verify;
            }
            
            
        }
        
        //httpClientContext.getCookieStore().clear();
       // System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        String  result = EntityUtils.toString(entity);
        //httpclient.close();
        return "";
    } 
	
	public static String stringToHex(String s) {
	    String val = "";
	    for(int i = 0; i < s.length(); i++)
	    {
	        Integer.toHexString((int) s.charAt(i));

	        if(val == "")
	            val = Integer.toHexString((int) s.charAt(i));
	        else val += Integer.toHexString((int) s.charAt(i));
	    }

 
	    return val;
	    
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
