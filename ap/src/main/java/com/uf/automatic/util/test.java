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

 
public class test { 
    
    private static Map<String,String> history = new HashMap();
    public static void main (String args []) {
        String url = "https://api.api68.com/pks/getPksHistoryList.do?date=2018-01-14&lotCode=10001";
        String ret = null;
        try {
            ret = Utils.httpClientGet(url);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(ret).getAsJsonObject();
        JsonArray data = o.get("result").getAsJsonObject().get("data").getAsJsonArray();

       
        for(int i=data.size()-1;i>0;i--) {
            
            JsonObject paymentObj = data.get(i).getAsJsonObject();
            String preDrawCode = paymentObj.get("preDrawCode").getAsString();  
            String preDrawIssue = paymentObj.get("preDrawIssue").getAsString(); 
            history.put(preDrawIssue, preDrawCode);
           
        }
        
        
        
        int s = 0 ;
        for(int i=data.size()-1;i>0;i--) {
            s++;
            if(s>4) {
                JsonObject paymentObj = data.get(i).getAsJsonObject();
                String preDrawCode = paymentObj.get("preDrawCode").getAsString();  
                String preDrawIssue = paymentObj.get("preDrawIssue").getAsString(); 
                startPredict(preDrawIssue,preDrawCode);
            }
           
        }
        
        
        
         

        
    }
    
    
    private static int[] betcmp = {0,0,0,0,0,0,0,0,0,0};
    private static  Map<Integer,String[]> betMap = new HashMap();
    public static void startPredict(String phase,String opencode) {
        String phaseBefore4 = Integer.toString( Integer.parseInt(phase) - 4 );
        String codeBefore4 = history.get(phaseBefore4).toString();
        
        String phaseN = Integer.toString( Integer.parseInt(phase) + 1);
        String codeArray = history.get(phaseN).toString();
        String[] codeN = codeArray.split(",");
        
        String[] codeB = codeBefore4.split(",");
         
        for(int i =0 ; i < codeB.length ; i++  ) {//名次
            
            
           
            if(betcmp[i] == 0) {
                String c = codeB[i]; 
                
                String[] betCode = formu(c); //下注號碼
                betMap.put(i, betCode);
            }
            
            boolean overFlag = false;
            
            String[]b = betMap.get(i);
            for(String bc :b) {
                if(codeN[i].equals(bc)) { //過關
                    
                    String bb = b[0] + "," +
                            b[1] + "," +b[2] + "," +
                            b[3] + "," +b[4] ;
                    int sn = i +1;
                    int betc = betcmp[i] + 1;
                        System.out.println("第"+ phaseN +"期，"+"第"+ sn +"名，第" + betc +"關" + "，號碼" + bb );
                    
                    overFlag = true;
                    betcmp[i]=0;
                    
                    
                    break ; 
                }
            }
            
            
            if(!overFlag)
                betcmp[i]++;
            else {
                
            }
            
        }
        
    }
    
    public static String[] formu(String code) {
        String[] c = new String[5] ;
        if(code.equals("01")) { c[0] = "01";  c[1] = "02"; c[2] = "03"; c[3] = "09";  c[4] = "10"; }
        if(code.equals("02")) { c[0] = "01";  c[1] = "02"; c[2] = "03"; c[3] = "04";  c[4] = "10"; }
        if(code.equals("03")) { c[0] = "01";  c[1] = "02"; c[2] = "03"; c[3] = "04";  c[4] = "05"; }
        if(code.equals("04")) { c[0] = "02";  c[1] = "03"; c[2] = "04"; c[3] = "05";  c[4] = "06"; }
        if(code.equals("05")) { c[0] = "03";  c[1] = "04"; c[2] = "05"; c[3] = "06";  c[4] = "07"; }
        if(code.equals("06")) { c[0] = "04";  c[1] = "05"; c[2] = "06"; c[3] = "07";  c[4] = "08"; }
        if(code.equals("07")) { c[0] = "05";  c[1] = "06"; c[2] = "07"; c[3] = "08";  c[4] = "09"; }
        if(code.equals("08")) { c[0] = "06";  c[1] = "07"; c[2] = "08"; c[3] = "09";  c[4] = "10"; }
        if(code.equals("09")) { c[0] = "01";  c[1] = "07"; c[2] = "08"; c[3] = "09";  c[4] = "10"; }
        if(code.equals("10")) { c[0] = "01";  c[1] = "02"; c[2] = "08"; c[3] = "09";  c[4] = "10"; }

        
           return c;    
        
    }
    
    
}
