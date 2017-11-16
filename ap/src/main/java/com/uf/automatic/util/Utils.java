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

 
public class Utils {
	public static void main(String[] args ){
		try {
			String login = "http://mem1.fpaesf109.pasckr.com:88/Handler/LoginHandler.ashx?action=user_login"+
		"&loginName=sd8885&loginPwd=Aa258369";
			httpClientGet(login);
			
			System.out.println("*************");
			String query = "http://mem1.fpaesf109.pasckr.com:88/Handler/QueryHandler.ashx?action=get_ad";
			
			httpClientGet(query);
			String a = new SimpleDateFormat("MMddhhmmsssSSS").format(new Date());

			String bet ="http://mem1.fpaesf109.pasckr.com:88/L_PK10/Handler/Handler.ashx" +
			"?action=put_money&" +
			"phaseid=163763&" +
			"oddsid=1&" +
			"uPI_P=9.81&" +
			"uPI_M=10&" +
			"i_index=0&" +
			//"JeuValidate="+a+"&" +
			"playpage=pk10_d1_10" ;
			 
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
			;
			String v = httpClientGet(bet);
			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(v).getAsJsonObject();
			JsonObject data = o.getAsJsonObject("data");
			String t = data.get("JeuValidate").getAsString();
			String aa ="http://mem1.fpaesf109.pasckr.com:88/L_PK10/Handler/Handler.ashx" +
					"?action=put_money&" +
					"phaseid=163764&" +
					"oddsid=2&" +
					"uPI_P=9.81&" +
					"uPI_M=10&" +
					"i_index=0&" +
					"JeuValidate="+t+"&" +
					"playpage=pk10_d1_10" ;
			httpClientGet(aa);
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
	static String cookieString="";
	public static String httpClientGet(String uri) throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

	   

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpClientContext context = HttpClientContext.create();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
		
		HttpPost httpget = new HttpPost(uri);
		httpget.setConfig(requestConfig);

		//if(!cookieString.equals("")){
		//	System.out.println(cookieString);
			//httpget.setHeader("Cookie", "ASP.NET_SessionId=52y2kopnkuezlf0hysptxnzl; yunsuo_session_verify=3d30cc29cee1cf6afd5b79f57679a6c4;");
			httpget.setHeader("Cookie",cookieString);

	//	}
		
		String result = null;

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
		return result;
	}
	
	public static void writeHistory() {
		try {
			String url = "http://api.1680210.com/pks/getPksHistoryList.do?lotCode=10001";
			String ret = Utils.httpClientGet(url);
			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(ret).getAsJsonObject();
			JsonArray data = o.getAsJsonObject("result").getAsJsonArray("data");
			for (JsonElement pa : data) {
			    JsonObject paymentObj = pa.getAsJsonObject();
			    String     preDrawCode     = paymentObj.get("preDrawCode").getAsString(); //開獎
			    String     preDrawIssue = paymentObj.get("preDrawIssue").getAsString(); //期數
			    WritePropertiesFile("history",preDrawIssue,preDrawCode);

			}
 		}catch(Exception e) {
			
			
		}
		
	}
	
	public static void WritePropertiesFile(String fileName,String key, String data) {
        FileOutputStream fileOut = null;
        FileInputStream fileIn = null;
        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/"+fileName+".properties";
            File file = new File(hisFile);
            if(!file.exists()) {
            		file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn , "UTF-8"));
            String result = java.net.URLDecoder.decode(data, "UTF-8");
            configProperty.setProperty(key, result);
            fileOut = new FileOutputStream(file);
            configProperty.store(new OutputStreamWriter(fileOut, "UTF-8"), "sample properties");  
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            try {
            	    fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }
    }
}
