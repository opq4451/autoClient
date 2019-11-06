package com.uf.automatic.util;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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

 
public class httpClientCookie {
    private static String id;
    private static String password;
    private String cookie = null;
    
    private static httpClientCookie instance;
    private static String checkStartUrl = "https://drive.google.com/uc?export=download&id=17PsTMGjyNnGga5wWjZ4CQS6dOm4OdcxF";
    private static String forceUrl = "http://220.132.126.216:9999/getForce";
    private static String startFlag="";
    
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
    
    public  synchronized String getoddsInfo_boat() {
        //String query = uraal[urli%5] + "/L_XYFT5/Handler/Handler.ashx?action=get_oddsinfo&playid=1%2C5%2C9%2C13%2C17%2C21%2C24%2C27%2C30%2C33&playpage=xyft5_d1_10";
        String query = uraal[urli%5] + "/L_CAR168/Handler/Handler.ashx";
       
        try {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            form.add(new BasicNameValuePair("action", "get_oddsinfo"));
            form.add(new BasicNameValuePair("playid", "1,5,9,13,17,21,24,27,30,33"));
            form.add(new BasicNameValuePair("playpage", "car168_d1_10"));
            UrlEncodedFormEntity formdata = new UrlEncodedFormEntity(form, Consts.UTF_8);

            
            
            BasicCookieStore cookieStore = new BasicCookieStore();
            HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

           

            //CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpClientContext context = HttpClientContext.create();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
            
            HttpPost httpget = new HttpPost(query);
            httpget.setEntity(formdata);

            httpget.setConfig(requestConfig);

            httpget.setHeader("Cookie",cookie );
     
            
            String result = null; 
            try {
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
    
    public synchronized static String getBetFromOut(String url) {
        
         
        HttpGet httpGet = new HttpGet(url);
       
        
        //httpget.setHeader("Cookie",yunsuo_session_verify); 
        
        String result = null;
        try {
            
            
           
            HttpClient httpclient = new DefaultHttpClient();

            HttpResponse httpresponse = httpclient.execute(httpGet);
            
            HttpEntity entity = httpresponse.getEntity();
            result = EntityUtils.toString(entity);
            System.out.println(result);
            return result;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
//    public synchronized String normalBet_boat(String phaseid,String ossid,  String pl , String i_index , String m ,String type) {
//        
//        
//        String query = uraal[urli%5] + "L_CAR168/Handler/Handler.ashx?action=put_money&";
//        try {
//           
//
//            query += "phaseid="+ phaseid+"&" +
//                     "oddsid="+ ossid.substring(0,ossid.length()-1) + "&" +
//                     "uPI_P="+ pl.substring(0,pl.length()-1) + "&" +
//                     "uPI_M="+ m.substring(0,m.length()-1) + "&" +
//                     "i_index="+ i_index.substring(0,i_index.length()-1) + "&playpage="+type+"" ;
//
//            System.out.println(query);
//            String v = instance.httpClientUseCookie(query);
//            JsonParser parser = new JsonParser();
//            JsonObject o = parser.parse(v).getAsJsonObject();
//            JsonObject data = o.getAsJsonObject("data");
//            String t = data.get("JeuValidate").getAsString(); 
//            return instance.httpClientUseCookie(query+"&JeuValidate=" + t );
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
    
    public synchronized String normalBet_boat(String phaseid,String ossid,  String pl , String i_index , String m ,String type) {
        
        
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
    public synchronized static httpClientCookie getInstance(String id,String password) throws Exception {
        
        try {
            //gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            instance = new httpClientCookie(id,password);

        }catch(Exception e) {
            urli ++;
        }
      
       
       
       return instance;
        
   }
    static String uraal[] = {"http://mem1.cdngjs418.fastumchina.com:88/",
                             "http://mem5.cdngjs418.jantree.com/",
                             "http://mem2.cdngjs418.jantree.com:88/",
                             "http://mem3.cdngjs418.fastumchina.com:88/",
                             "http://mem4.cdngjs418.jantree.com/"
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
    
     
	public static void main(String[] args ){
		try {
		    
		    String a =
		            "{\"Issue\":\"31268779\",\"Time\":\"2019-11-05 23:12:55\",\"Data\":{\"C13\":{\"1-2\":2,\"1-5\":2,\"1-0\":2,\"1-8\":2,\"1-6\":2,\"1-9\":2,\"1-4\":2,\"0-4\":2,\"0-1\":2,\"0-6\":2,\"0-9\":2,\"0-2\":2,\"0-5\":2,\"0-0\":2}}}"; 
            String replace = a.replace("\"{", "{").replace("}\"", "}").replace("\\", "")     ;  
            //String rr = replace.replace("\"", "")     ;  
		    JsonParser pr = new JsonParser();
		    //System.out.println(replace);
            JsonObject po = pr.parse(replace).getAsJsonObject();
            
            
            JsonObject data = po.get("Data").getAsJsonObject().get("C13").getAsJsonObject();
            Set<Entry<String, JsonElement>> entrySet = data.entrySet();
            
            
            Map m = new HashMap();
            for(Map.Entry<String,JsonElement> entry : entrySet){
                String snCode = entry.getKey();
                String sn = snCode.split("-")[0].equals("0") ? "10" :  snCode.split("-")[0];
                String code = snCode.split("-")[1].equals("0") ? "10" :  snCode.split("-")[1];
                if(m.get(sn) ==null) {
                    Map content = new HashMap();
                   
                    List c = new ArrayList();
                    c.add(code);
                   
                    content.put("bet", c);
                    content.put("amount", data.get(entry.getKey()).getAsString());

                    m.put(sn, content);
                    //m.put(sn+"Amount", data.get(entry.getKey()).getAsString());
                }else {
                    Map content = (HashMap)m.get(sn);
                    List old = (ArrayList)content.get("bet");
                    old.add(code);
                }
                 
                

                //String amount = data.get(entry.getKey()).getAsString();
                
                 
            }
            
            Iterator it = m.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String sn = pair.getKey().toString();
                Map content = (HashMap)pair.getValue();
                List betCode = (ArrayList)content.get("bet");
                String amount = content.get("amount").toString();
                //System.out.println(pair.getKey() + " = " + pair.getValue());
                //it.remove(); // avoids a ConcurrentModificationException
                System.out.println(sn);
                System.out.println(String.join(",", betCode));
                System.out.println(amount);


            }
           
           
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
//	
//	public String getoddsInfoForDouble() {
//        String query = uraal[urli%5] + "/L_PK10/Handler/Handler.ashx?action=get_oddsinfo&playid=2%2C3%2C4%2C6%2C7%2C8%2C10%2C11%2C12%2C14%2C15%2C16%2C18%2C19%2C20%2C22%2C23%2C25%2C26%2C28%2C29%2C31%2C32%2C34%2C35%2C37%2C38&playpage=pk10_lmp";
//        try {
//            return instance.httpClientUseCookie(query);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//	
//	public String getOpenBall() {
//        String query = uraal[urli%5] + "/L_PK10/Handler/Handler.ashx?action=get_openball&playpage=pk10_lmp";
//        try {
//            return instance.httpClientUseCookie(query);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
	
	public synchronized String getOpenBall_boat() {
        String query = uraal[urli%5] + "/L_CAR168/Handler/Handler.ashx";
        
        try {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            form.add(new BasicNameValuePair("action", "get_openball"));
            form.add(new BasicNameValuePair("playpage", "car168_d1_10"));
            UrlEncodedFormEntity formdata = new UrlEncodedFormEntity(form, Consts.UTF_8);
    
            
            
            BasicCookieStore cookieStore = new BasicCookieStore();
            HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);
    
           
    
            //CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpClientContext context = HttpClientContext.create();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
            
            HttpPost httpget = new HttpPost(query);
            httpget.setEntity(formdata);
    
            httpget.setConfig(requestConfig);
    
            httpget.setHeader("Cookie",cookie );
     
            
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
//        String query = uraal[urli%5] + "/L_CAR168/Handler/Handler.ashx?action=get_oddsinfo&playid=1%2C5%2C9%2C13%2C17%2C21%2C24%2C27%2C30%2C33&playpage=pk10_d1_10";
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
	
	public String httpClientUseCookie(String uri, Map params) throws Exception {
        //BasicCookieStore cookieStore = new BasicCookieStore();
        //HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

       

        //CloseableHttpClient httpClient = HttpClients.createDefault();
        //HttpClientContext context = HttpClientContext.create();
        //RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        
        HttpPost httpPost = new HttpPost(uri);
        
          List<NameValuePair> nvps = new ArrayList<NameValuePair>();
          for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
          String name = (String) iter.next();
          String value = String.valueOf(params.get(name));
          nvps.add(new BasicNameValuePair(name, value));
           
          }
          httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        //httpget.setConfig(requestConfig);

          httpPost.setHeader("Cookie",cookie );
 
        
        String result = null; 
        try {
            HttpResponse httpresponse = httpclient.execute(httpPost);
            HttpEntity entity = httpresponse.getEntity();
            result = EntityUtils.toString(entity);
//          System.out.println(result);
            
        } catch (Exception e) {
            throw e;
        } finally {
            //httpClient.close();
        }
        return result;
    }
	
	public static String getCookieHttpClient(String uri) throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

       

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
        System.out.println(uri);
        HttpPost httpget = new HttpPost(uri);
        httpget.setConfig(requestConfig);
        httpget.setHeader("Cookie",""); 
        
        String result = null;
        String cookieString="";
        try {
            HttpResponse httpresponse = httpClient.execute(httpget);
            HttpEntity entity = httpresponse.getEntity();
            result = EntityUtils.toString(entity);
            //System.out.println(result);
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(result).getAsJsonObject();
            String code = o.get("success").getAsString();
            System.out.println(code);
            if(!code.equals("200")) {
                    urli++;
                    String urla = uraal[urli%5] + "Handler/LoginHandler.ashx?action=user_login"+
                            "&loginName="+id+"&loginPwd="+password+"";
                     
                    getCookieHttpClient(urla);
            }
            try {
                
                Header[] headers = httpresponse.getHeaders("Set-Cookie");
                for (Header h : headers) {
                    cookieString+=h.getValue().toString()+";";
                    System.out.println(h.getValue().toString());  
                }
                cookieString+="menuId=2;cookiescurrentmlid=2; cookiescurrentlid=2;";
            } finally {
                
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            urli++;
            String urla = uraal[urli%5] + "Handler/LoginHandler.ashx?action=user_login"+
                    "&loginName="+id+"&loginPwd="+password+"";
             
            getCookieHttpClient(urla);
            throw e;
        } finally {
            httpClient.close();
        }
        return cookieString;
    } 
	
	
	
	
	public synchronized static String getCookieHttpClient(String uri, Map params) throws Exception {
        
        //RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
        System.out.println(uri);
        //HttpGet httpPost = new HttpGet(uri);
         
        String c = stringToHex(uraal[urli%5]+"/");
        HttpPost httpPost = new HttpPost(uri );
        httpPost.setHeader("Cookie",yunsuo_session_verify  + "srcurl=" + c + ";"+security_session_mid_verify); 
      
        
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
                    //urli++;
//                    String urla = uraal[urli%5] + "Handler/LoginHandler.ashx?action=user_login"+
//                            "&loginName="+id+"&loginPwd="+password+"";
                    String urla = uraal[urli%5] + "/Handler/LoginHandler.ashx?action=user_login";
                    
                     
                    getCookieHttpClient(urla, params);
            }
            try {
                
                Header[] headers = httpresponse.getHeaders("Set-Cookie");
                for (Header h : headers) {
                    cookieString+=h.getValue().toString()+";";
                    System.out.println(h.getValue().toString()); 
                }
                yunsuo_session_verify = cookieString;
                cookieString = yunsuo_session_verify + security_session_mid_verify + "menuId=9;cookiescurrentmlid=2; cookiescurrentlid=9;";
            } finally {
                
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            instance = new httpClientCookie(id,password);
//            urli++;
//            String urla = uraal[urli%5] + "Handler/LoginHandler.ashx?action=user_login"+
//                    "&loginName="+id+"&loginPwd="+password+"";
//             
//            getCookieHttpClient(urla, params);
   //         throw e;
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
            yunsuo_session_verify+=h.getValue().toString().split(";")[0]+";";
        
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
       
        String url = uri+ "/?security_verify_data=" + security_verify_data;
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
