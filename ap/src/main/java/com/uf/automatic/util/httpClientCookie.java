package com.uf.automatic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
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
    private String id;
    private String password;
    private String cookie = null;
    
    private static httpClientCookie instance;

    
    public httpClientCookie(String id, String password) {
        // TODO Auto-generated constructor stub
        setId(id);
        setPassword(password); 
        setInitCookie();//塞 cookie
    }
    public static httpClientCookie getInstance(String id,String password) {
        if(instance == null) {
            instance = new httpClientCookie(id,password);
        }
           
        
        return instance;
         
    }
    //sd8885 //Aa258369
    private String setInitCookie() {
        String login = "http://mem1.fpaesf109.pasckr.com:88/Handler/LoginHandler.ashx?action=user_login"+
                "&loginName="+id+"&loginPwd="+password+"";
        try {
            String cookie = getCookieHttpClient(login);
            setCookie(cookie);
        }catch(Exception e) {
            
        }
        return "";
    }
    
    
      
	public static void main(String[] args ){
		try {
		    //httpClientCookie a = httpClientCookie.getInstance("sd8885","Aa258369");
		    httpClientCookie a = httpClientCookie.getInstance("sd8885","qaz123123");
		    a.getoddsInfo();
		    a.getoddsInfoForDouble();
		  
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
//			 
//			/*
//			 * 
//			 * action:put_money
//				phaseid:163754
//				oddsid:2
//				uPI_P:9.81
//				uPI_M:10
//				i_index:0
//				JeuValidate:11050308464138
//				playpage:pk10_d1_10
//			 */
//			//649096
//			 //phaseid 163755
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
	    String query = "http://mem1.fpaesf109.pasckr.com:88/Handler/QueryHandler.ashx?action=get_ad";
        try {
            return instance.httpClientUseCookie(query);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
	}
	
	//兩面ＺＲＵＦ
	public String getoddsInfoForDouble() {
        String query = "http://mem1.fpaesf109.pasckr.com:88/L_PK10/Handler/Handler.ashx?action=get_oddsinfo&playid=2%2C3%2C4%2C6%2C7%2C8%2C10%2C11%2C12%2C14%2C15%2C16%2C18%2C19%2C20%2C22%2C23%2C25%2C26%2C28%2C29%2C31%2C32%2C34%2C35%2C37%2C38&playpage=pk10_lmp";
        try {
            return instance.httpClientUseCookie(query);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
	
	public String getoddsInfo() {
	    String query = "http://mem1.fpaesf109.pasckr.com:88/L_PK10/Handler/Handler.ashx?action=get_oddsinfo&playid=1%2C5%2C9%2C13%2C17%2C21%2C24%2C27%2C30%2C33&playpage=pk10_d1_10";
        try {
            return instance.httpClientUseCookie(query);
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
			System.out.println(result); 
			
		} catch (Exception e) {
			throw e;
		} finally {
			httpClient.close();
		}
		return result;
	}
	
	public static String getCookieHttpClient(String uri) throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

       

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
        
        HttpPost httpget = new HttpPost(uri);
        httpget.setConfig(requestConfig);
        httpget.setHeader("Cookie",""); 
        
        String result = null;
        String cookieString="";
        try {
            HttpResponse httpresponse = httpClient.execute(httpget);
            HttpEntity entity = httpresponse.getEntity();
            result = EntityUtils.toString(entity);
            System.out.println(result);
            
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
