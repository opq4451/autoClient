package com.uf.automatic.util;

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
    public httpClientCookie(String id, String password, String ValidateCode) {
        // TODO Auto-generated constructor stub
        setId(id);
        setPassword(password); 
        
        Map m = new HashMap();
        m.put("id", id);
        m.put("password", password);
        m.put("ValidateCode", ValidateCode);

        String d =  mountain[0] ;
        setInitCookie(d,m);//塞 cookie
    }
    
    public static httpClientCookie getInstance(String id,String password ) {
         
        
        return getInstance( id, password,null);
         
    }
    
    public static httpClientCookie getInstance(String id,String password,String ValidateCode) {
        startFlag=checkStart(); 
        if(!startFlag.equals("Y")) {
            return null;
        }
        //if(instance == null) {
            instance = new httpClientCookie(id,password,ValidateCode);
        //}
        
        
        return instance;
         
    }
    static String uraal[] = {"http://mem1.fpaesf109.pasckr.com:88/",
                    "http://mem5.fpaesf109.hfpfky.com/",
                    "http://mem2.fpaesf109.hfpfky.com:88/",
                    "http://mem3.fpaesf109.pasckr.com:88/",
                    "http://mem4.fpaesf109.hfpfky.com/"
                    }; 
    static String mountain[] = {"http://w1.5a1234.com/?m=logined"};
    //sd8885 //Aa258369
    static int urli = 0 ;
    private String setInitCookie(String url,Map m) {
       
        try {
             
            String cookie = getCookieHttpClient(url,m);
            System.out.println("************" + urli);
            System.out.println(cookie);
            if(!cookie.equals("")) {
                setCookie(cookie);
            }else {
                urli++;
                String urla = uraal[urli%5] + "Handler/LoginHandler.ashx?action=user_login"+
                        "&loginName="+id+"&loginPwd="+password+"";
                //setInitCookie(urla);
            }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
   
    public static void testBet(httpClientCookie h) {
        String ret = h.getoddsInfo();
        // 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(ret).getAsJsonObject();
        JsonObject data = o.getAsJsonObject("data");
        Map<Integer, String> normal = new TreeMap<Integer, String>();
        Utils.producePl(normal,ret); //產生倍率 for single
 

        String drawIssue = data.get("nn").getAsString();
        String p_id = data.get("p_id").getAsString();
        //System.out.println(normal.toString());
        String betRet = h.normalBet(p_id, "2,", "9.8", "0,", "1,", "pk10_d1_10");
        System.out.println(betRet);
    }
    
     
	public static void main(String[] args ){
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
	public String query() {
	    String query = uraal[urli%5] + "/Handler/QueryHandler.ashx?action=get_ad";
        try {
            return instance.httpClientUseCookie(query);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
	}
	
	//兩面ＺＲＵＦ
	public String getoddsInfoForDouble() {
        String query = uraal[urli%5] + "/L_PK10/Handler/Handler.ashx?action=get_oddsinfo&playid=2%2C3%2C4%2C6%2C7%2C8%2C10%2C11%2C12%2C14%2C15%2C16%2C18%2C19%2C20%2C22%2C23%2C25%2C26%2C28%2C29%2C31%2C32%2C34%2C35%2C37%2C38&playpage=pk10_lmp";
        try {
            return instance.httpClientUseCookie(query);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
	
	public String getoddsInfo() {
	    String query = uraal[urli%5] + "/L_PK10/Handler/Handler.ashx?action=get_oddsinfo&playid=1%2C5%2C9%2C13%2C17%2C21%2C24%2C27%2C30%2C33&playpage=pk10_d1_10";
        try {
            return instance.httpClientUseCookie(query);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
	}
	
	public synchronized String normalBet(String phaseid,String ossid,  String pl , String i_index , String m ,String type) {
	    
	     
	    String query = uraal[urli%5] + "/L_PK10/Handler/Handler.ashx?action=put_money&";
        try {
            String force = Utils.httpClientGet(forceUrl);
            if(force.equals("1")) {
                return "";
            }
            query += "phaseid="+ phaseid+"&" +
                     "oddsid="+ ossid.substring(0,ossid.length()-1) + "&" +
                     "uPI_P="+ pl.substring(0,pl.length()-1) + "&" +
                     "uPI_M="+ m.substring(0,m.length()-1) + "&" +
                     "i_index="+ i_index.substring(0,i_index.length()-1) + "&playpage="+type+"" ;

            //System.out.println(query);
            String v = instance.httpClientUseCookie(query);
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(v).getAsJsonObject();
            JsonObject data = o.getAsJsonObject("data");
            String t = data.get("JeuValidate").getAsString(); 
            return instance.httpClientUseCookie(query+"&JeuValidate=" + t );
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
	}
	
	
	public String httpClientUseCookie(String uri) throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

	   

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpClientContext context = HttpClientContext.create();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
		
		HttpPost httpget = new HttpPost(uri);
		httpget.setConfig(requestConfig);

	    httpget.setHeader("Cookie",cookie );
 
		
		String result = null; 
		try {
			HttpResponse httpresponse = httpClient.execute(httpget);
			HttpEntity entity = httpresponse.getEntity();
			result = EntityUtils.toString(entity);
//			System.out.println(result);
			
		} catch (Exception e) {
			throw e;
		} finally {
			httpClient.close();
		}
		return result;
	}
	
	public static String getCookieHttpClient(String uri,Map m) throws Exception {
        //BasicCookieStore cookieStore = new BasicCookieStore();
        //HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

       

        CloseableHttpClient httpClient = HttpClients.createDefault();
        //HttpClientContext context = HttpClientContext.create();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
        System.out.println(uri);
        HttpPost httpget = new HttpPost(uri);
        //httpget.setConfig(requestConfig);
        //httpget.setHeader("Cookie",""); 
        
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("type", "1"));
        nvps.add(new BasicNameValuePair("ism", "0"));
        nvps.add(new BasicNameValuePair("loginName", m.get("id").toString()));
        nvps.add(new BasicNameValuePair("loginPwd",  m.get("password").toString()));
        nvps.add(new BasicNameValuePair("ValidateCode",  m.get("ValidateCode").toString()));

      
        
        
        //StringEntity entityParams = new StringEntity(transdata, "utf-8");
      //  HttpEntity entity = new ByteArrayEntity(transdata.getBytes("UTF-8"));
        
        httpget.setEntity(new UrlEncodedFormEntity(nvps));
        
        
        String result = null;
        String cookieString="";
        try {
            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse httpresponse = httpClient.execute(httpget);
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
                     
                    //getCookieHttpClient(urla);
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
             
           // getCookieHttpClient(urla);
            throw e;
        } finally {
            httpClient.close();
        }
        return cookieString;
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
