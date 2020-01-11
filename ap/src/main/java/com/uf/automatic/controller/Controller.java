package com.uf.automatic.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
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
import com.uf.automatic.ap.OrderedProperties;
import com.uf.automatic.util.Utils;
import com.uf.automatic.util.httpClientCookie;
import com.uf.automatic.util.dali.DaliHttpClient;
import com.uf.automatic.util.dali.NewDaliHttpClient;
import com.uf.automatic.util.leein.LeeinHttpClient;
import com.uf.automatic.util.mountain.MoutainHttpClient;

@RestController
public class Controller {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    int i = 0;
    int bi = 0;
    int over_i = 0;
    httpClientCookie h = null;
    DaliHttpClient d_h = null;
    NewDaliHttpClient nd_h = null;
    String user = "";
    String pwd = "";
    String p_id = "";
    String boardType = "";

    Map<Integer, String> bs = new TreeMap<Integer, String>();

    @RequestMapping("/getUid")
    public String getUid(@RequestParam("user") String u, @RequestParam("pwd") String p,
                         @RequestParam("ValidateCode") String ValidateCode, @RequestParam("boardType") String bd) {
        // Map checkLimit = checkLimitDate(user,pwd);
        // if(!checkLimit.get("OK").toString().equals("Y")) {
        // return "null";
        // }
        //
        // String pwd_in = checkLimit.get("pwd_in").toString();
        //
        // String Step1 =
        // "http://203.160.143.110/www_new/app/login/chk_data.php?active=newlogin&"
        // + "username=" + user
        // + "" + "&passwd=" + pwd_in + "" + "&langx=zh-cn";
        try {

            user = u;
            pwd = p;
            String ret = getAuthInformation(user, pwd);
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(ret).getAsJsonObject();

            String IFOK = o.get("OK").getAsString();
            if (IFOK.equals("Y")) {
                boardType = bd;

                if (boardType.equals("0")) {
                    h = httpClientCookie.getInstance(user, pwd);
                } else if (boardType.equals("1")) {
                    token = MoutainHttpClient.httpPostGetToken(mountain_url[mountain_index % 4] + "/?m=logined",
                                                               mountain_php_cookid,
                                                               ValidateCode,
                                                               u,
                                                               p);
                    if (token.equals("v_error")) {
                        return "v_error";
                    } else if (token.equals("")) {
                        return "N";
                    }
                    mountain_token_sessid = token + mountain_php_cookid;
                } else if (boardType.equals("2")) {
                    d_h = DaliHttpClient.getInstance(user, pwd);
                } else if (boardType.equals("3")) {
//                    leein_php_cookid = LeeinHttpClient.httpPostInit(leein_url[leein_index % 4],
//                                                                    leein_php_cookid,
//                                                                    "1111",
//                                                                    u,
//                                                                    p);
//                    System.out.println(leein_php_cookid);
 
                } else if (boardType.equals("4")) {
                    LeeinHttpClient.initPage(user, pwd);
                } else if (boardType.equals("5")) {
                    nd_h = NewDaliHttpClient.getInstance(user, pwd);
                }

                clearLog(user + "bet");
                clearLog(user + "overLOGDIS");
                clearLog(user + "_over");
                return "Y";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "N";
    }

    @RequestMapping("/checkAdmin")
    public String checkAdmin(@RequestParam("user") String user, @RequestParam("pwd") String pwd) {
        FileInputStream fileIn = null;
        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/auth.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }

            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));

            if (configProperty.getProperty(user) == null) {
                return "error";

            }

            String password = configProperty.getProperty(user);

            if (password.equals(pwd)) {
                return "suc";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();

            } catch (Exception ex) {
            }
        }

        return "error";
    }

    @RequestMapping("/getTodayWin")
    public synchronized String getTodayWin(@RequestParam("user") String user, @RequestParam("pwd") String pwd,
                              @RequestParam("boardType") String boardType) throws Exception {

        try {
            JsonObject j = new JsonObject();
            JsonParser parser = new JsonParser();
            DecimalFormat df = new DecimalFormat("##.00");

            if (boardType.equals("0")) {
                if (h == null) {
                    h = httpClientCookie.getInstance(user, pwd);
                }
                String ret = h.getoddsInfo_boat();
                JsonObject o = parser.parse(ret).getAsJsonObject();

                JsonObject data = o.getAsJsonObject("data");
                String todayWin = data.get("profit").getAsString();
                String usable_credit = data.get("usable_credit").getAsString();
                String stop_time =  data.get("stop_time").getAsString();
                j.addProperty("todayWin", Double.parseDouble(df.format(Double.valueOf(todayWin))));
                j.addProperty("usable_credit", Double.parseDouble(df.format(Double.valueOf(usable_credit))));
                try {
                     
                        j.addProperty("stop_time", stop_time.split(":")[1] + ":" + stop_time.split(":")[2]);
 
                   
                }catch(Exception e) {
                    
                }
                

            } else if (boardType.equals("1")) { //華山
                String ret = MoutainHttpClient.httpGet(mountain_token_sessid,
                                                       mountain_url[mountain_index % 4] + "/?m=acc&gameId=2");
                JsonObject o = parser.parse(ret).getAsJsonObject();

                String usable_credit = o.get("balance").getAsString();
                String unbalancedMoney = o.get("totalTotalMoney").getAsString();
                j.addProperty("usable_credit", Double.parseDouble(df.format(Double.valueOf(usable_credit))));
                j.addProperty("todayWin", Double.parseDouble(df.format(Double.valueOf(unbalancedMoney))));

            } else if (boardType.equals("2")) { //大立
                String ret = DaliHttpClient.getTodayUse();
                //System.out.println(ret);
                int startIndex = ret.lastIndexOf("t_Edit_td");
                int endIndex = ret.indexOf("<", startIndex);
                String canuse = ret.substring(startIndex + 11, endIndex);
                j.addProperty("usable_credit", Double.parseDouble(df.format(Double.valueOf(canuse))));
                
                String t = DaliHttpClient.getTodayWin();
                //System.out.println(t);
                j.addProperty("todayWin", Double.parseDouble(df.format(Double.valueOf(t))));

            } else if (boardType.equals("3")) {
//                String ret = LeeinHttpClient.getTodayWin(leein_url[leein_index % 4]
//                                                         + "/member/accounts?_=1522119266532",
//                                                         leein_php_cookid);
//
//                JsonArray o = parser.parse(ret).getAsJsonArray();
//
//                JsonObject r = o.get(0).getAsJsonObject();
//
//                String usable_credit = r.get("balance").getAsString();
//                String unbalancedMoney = r.get("result")==null?"0":r.get("result").getAsString();
//                j.addProperty("usable_credit", Double.parseDouble(df.format(Double.valueOf(usable_credit))));
//                j.addProperty("todayWin", Double.parseDouble(df.format(Double.valueOf(unbalancedMoney))));

            }else if (boardType.equals("4")) {
                
            if(!LeeinHttpClient.checkCookie) {
                LeeinHttpClient.initPage(user, pwd);

                return "";
            }
            JsonObject ret = LeeinHttpClient.getTodayWin();
            String usable_credit = ret.get("usable_credit").getAsString();
            String unbalancedMoney = ret.get("unbalancedMoney").getAsString();
            j.addProperty("usable_credit", Double.parseDouble(df.format(Double.valueOf(usable_credit))));
            j.addProperty("todayWin", Double.parseDouble(df.format(Double.valueOf(unbalancedMoney))));
            

            JsonObject ret2 = LeeinHttpClient.getNextTime();
            j.addProperty("stop_time", ret2.get("min").getAsString() + ":" + ret2.get("sec").getAsString() );

            }else if (boardType.equals("5")) {
              
            
            JsonObject r =  nd_h.getTodayWin(nd_h.getCookie());
            
            String usable_credit = r.get("userInfo").getAsJsonObject().get("currentPoint").getAsString();
            String unbalancedMoney = r.get("userInfo").getAsJsonObject().get("todayResult")==null?"0":r.get("userInfo").getAsJsonObject().get("todayResult").getAsString();
            j.addProperty("usable_credit", Double.parseDouble(df.format(Double.valueOf(usable_credit))));
            j.addProperty("todayWin", Double.parseDouble(df.format(Double.valueOf(unbalancedMoney))));
            int stop_time = r.get("gameOpenStatus").getAsJsonObject() .get("betTime").getAsInt();

                try {
                    
                        j.addProperty("stop_time", stop_time/60 + ":" + stop_time%60 );
        
                   
                }catch(Exception e) {
                    
                }
            }

            FileInputStream fileIn = null;
            try {
                Properties configProperty = new Properties();
                String path = System.getProperty("user.dir");
                String hisFile = path + "/" + user + ".properties";
                File file = new File(hisFile);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fileIn = new FileInputStream(file);
                configProperty.load(fileIn);
                String type = configProperty.getProperty("type");
                String betlist = configProperty.getProperty("betlist");
                String betlist2 = configProperty.getProperty("betlist2");
                String betlist3 = configProperty.getProperty("betlist3");
                String betlist4 = configProperty.getProperty("betlist4");
                String betlist5 = configProperty.getProperty("betlist5");
                String betlist6 = configProperty.getProperty("betlist6");
                String betlist7 = configProperty.getProperty("betlist7");
                String betlist8 = configProperty.getProperty("betlist8");
                String betlist9 = configProperty.getProperty("betlist9");
                String betlist10 = configProperty.getProperty("betlist10");
                String betlist11 = configProperty.getProperty("betlist11");
                String betlist12 = configProperty.getProperty("betlist12");
                String betproject5 = configProperty.getProperty("betproject5");
                String betproject6 = configProperty.getProperty("betproject6");

                String s_h = configProperty.getProperty("s_h");
                String s_m = configProperty.getProperty("s_m");
                String e_h = configProperty.getProperty("e_h");
                String e_m = configProperty.getProperty("e_m");
                String s_m_stop = configProperty.getProperty("s_m_stop");
                String s_h_stop = configProperty.getProperty("s_h_stop");
                
                
                String stoppoint = configProperty.getProperty("stoppoint");

                String stoplose = configProperty.getProperty("stoplose");
                String stopwin = configProperty.getProperty("stopwin");
                String startstatus = configProperty.getProperty("startstatus");

                j.addProperty("type", type);
                j.addProperty("betlist", betlist);
                j.addProperty("betlist2", betlist2);
                j.addProperty("betlist3", betlist3);
                j.addProperty("betlist4", betlist4);
                j.addProperty("betlist5", betlist5);
                j.addProperty("betlist6", betlist6);
                j.addProperty("betlist7", betlist7);
                j.addProperty("betlist8", betlist8);
                j.addProperty("betlist9", betlist9);
                j.addProperty("betlist10", betlist10);
                j.addProperty("betlist11", betlist11);
                j.addProperty("betlist12", betlist12);
                j.addProperty("betproject5", betproject5);
                j.addProperty("betproject6", betproject6);

                j.addProperty("stoplose", stoplose);
                j.addProperty("stopwin", stopwin);
                j.addProperty("startstatus", startstatus);
                j.addProperty("s_h", s_h);
                j.addProperty("s_m", s_m);
                j.addProperty("e_h", e_h);
                j.addProperty("e_m", e_m);
                j.addProperty("s_m_stop", s_m_stop);
                j.addProperty("s_h_stop", s_h_stop);
                j.addProperty("autoStopCheck", configProperty.getProperty("autoStopCheck"));
                
                j.addProperty("stoppoint", stoppoint);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                try {
                    fileIn.close();
                } catch (Exception ex) {
                }
            }

            return j.toString();
        } catch (Exception e) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            saveLog(user + "error", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " : getToday 斷");
            if (boardType.equals("0")) {
                
                h = httpClientCookie.getInstance(user, pwd);
            } else if (boardType.equals("1")) {
                //mountain_index++;

                if (token.equals("")) {
                    return "N";
                }

                mountain_token_sessid = token + getPhpCookie();

            } else if (boardType.equals("2")) {
                d_h = DaliHttpClient.getInstance(user, pwd);
            } else if (boardType.equals("4")) {
                LeeinHttpClient.initPage(user, pwd);
            }else if (boardType.equals("3")) {
//                leein_index++;
//                try {
//                    leein_php_cookid = "2a29530a2306=b00b0a238f1bb76547c75c442ce5bc273859ad7904b7bc3e;";
//                    leein_php_cookid = LeeinHttpClient.httpPostInit(leein_url[leein_index % 4],
//                                                                    leein_php_cookid,
//                                                                    "1111",
//                                                                    user,
//                                                                    pwd);
//                } catch (Exception e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }

            }else if (boardType.equals("5")) {
                nd_h = NewDaliHttpClient.getInstance(user, pwd);

            }

            e.printStackTrace();
        }

        return "null";
    }

    public static void main(String[] args) {
//        Map s = combindHistoryMap();
        String HH = new SimpleDateFormat("HH").format(new Date());
        System.out.println(HH);
    }

    @RequestMapping("/getPredictLog")
    public String getPredictLog(@RequestParam("user") String user) {

        try {

            JsonObject j = new JsonObject();

            FileInputStream fileIn = null;
            try {
                Properties configProperty = new OrderedProperties();
                String path = System.getProperty("user.dir");
                String hisFile = path + "/" + user + "_log.properties";
                File file = new File(hisFile);
                if (!file.exists())
                    file.createNewFile();
                fileIn = new FileInputStream(file);
                configProperty.load(new InputStreamReader(fileIn, "UTF-8"));

                // String logHtml="";
                StringBuilder logHtml = new StringBuilder();

                int i = 0;

                Map m = new HashMap();

                Map<String, String> treemap = new TreeMap<String, String>(Collections.reverseOrder());

                for (Enumeration e = configProperty.propertyNames(); e.hasMoreElements();) {

                    String v = configProperty.getProperty(e.nextElement().toString());
                    int projectS = v.lastIndexOf("計劃") + 2;
                    int projectE = v.indexOf("，",0);
                    System.out.println(v);
                    System.out.println(projectS);
                    System.out.println(projectE);

                    String formuStr = v.substring(projectS, projectE); // (計劃)
                    System.out.println(formuStr);
                    String phase = v.substring(v.indexOf("第")+1,v.indexOf("期") ); //期別
                    String key_form = v.substring(v.lastIndexOf("式") + 1, v.lastIndexOf(")")); //公式

                    int start = v.indexOf("第", v.indexOf("期"));
                    int end = v.indexOf("名", v.indexOf("期"));
                    String sn = v.substring(start + 1, end).length() == 1 ? "0" + v.substring(start + 1, end)
                                                                          : v.substring(start + 1, end); //第幾名
                    int start_c = v.lastIndexOf("第");
                    int end_c = v.lastIndexOf("關");
                    String c = v.substring(start_c + 1, end_c).length() == 1 ? "0" + v.substring(start_c + 1, end_c)
                                                                             : v.substring(start_c + 1, end_c); //第幾關

                    int start_cc = v.lastIndexOf("碼");
                    int end_cc = v.lastIndexOf(")");
                    String cc = v.substring(start_cc + 1, end_cc).length() == 1
                                                                                ? "0"
                                                                                  + v.substring(start_cc + 1, end_cc)
                                                                                : v.substring(start_cc + 1, end_cc); //第幾關

                    //System.out.println(sn);

//                    if (v.indexOf("第0關") > -1) { //下注0的不用顯示在log
//                        continue;
//                    }
                    String index = "";
                    
                    if (key_form.equals("12")) { //計劃11
                        index = "02";
                    }
                    if (key_form.equals("10")) {//計劃10
                        index = "03";
                    }
                    if (key_form.equals("9")) {//計劃9
                        index = "04";
                    }
                    if (key_form.equals("8")) {//計劃8
                        index = "05";
                    }
                    if (key_form.equals("7")) {//計劃7
                        index = "06";
                    }
                    if (key_form.equals("6")) {//計劃6
                        index = "07";
                    }
                    ;
                    if (key_form.equals("5")) {//計劃5
                        index = "08";
                    }
                    ;
                    if (key_form.equals("4")) {//計劃4
                        index = "09";
                    }
                    ;
                    if (key_form.equals("3")) {// 計劃3
                        index = "10";
                    }
                    ;
                    if (key_form.equals("2")) {//計劃2
                        index = "11";
                    }
                    ;
                   
                    if (key_form.equals("1")) {//計劃1
                        index = "12";
                    }
                    ;
                    
//                    if (key_form.equals("11")) {
//                        index = "02";
//                    }
                    
                    
                   
                    
                    

                    String k = phase + "@" + index + "@" + sn + "@" + c + "@" + cc + "@" + formuStr;
                    System.out.println(k);
                    System.out.println(formuStr);
                    
                    
                    treemap.put(k, v);

                    //					{
                    //					    if(m.get(phase) == null) {
                    //					        i++;
                    //                            if(i % 2 == 1) {
                    //                                logHtml.insert(0,"<table  style=\"width:100%;border: 2px solid black;border-collapse: collapse;\">" );
                    //                            }else {
                    //                                logHtml.insert(0,"<table style=\"width:100%;border: 1px solid black;border-collapse: collapse;\">" );
                    //                            }  
                    //                            logHtml.insert(0,"</table>" ); 
                    //
                    //					        
                    //	                        m.put(phase, phase);
                    //
                    //	                     
                    //	                     
                    //	                     } 
                    //	                    
                    //					}
                    //					
                    //					

                }

                //				 if(i % 2 == 1) {
                //				     j.addProperty("logHtml", "<table style=\"width:100%;border: 1px solid black;border-collapse: collapse;\">" + 
                //                             logHtml.toString()  );
                //                   }else {
                //                         j.addProperty("logHtml", "<table style=\"width:100%;border: 2px solid black;border-collapse: collapse;\">" + 
                //                               logHtml.toString()  );
                //                   }  

                //process treemap
                Set set = treemap.entrySet();
                Iterator iter = set.iterator();
                // Display elements
                int d = 0;
                while (iter.hasNext()) {

                    Map.Entry me = (Map.Entry) iter.next();
                    String v = me.getValue().toString();
                    String k = me.getKey().toString();
                    String formu = k.split("@")[1];
                    String phase = k.split("@")[0];
                    if (m.get(phase) == null) {
                        d++;
                        logHtml.append("</table>");
                        if (d % 2 == 1) {
                            logHtml.append("<table  style=\"width:100%;border: 2px solid black;border-collapse: collapse;\">");
                        } else {
                            logHtml.append("<table style=\"width:100%;border: 1px solid black;border-collapse: collapse;\">");
                        }
                        m.put(phase, phase);
                    }

//                    if (formu.equals("12")) {
//                        logHtml.append("<tr><td bgcolor=\"FFFF77\"  style=\"border: 1px solid black\">"
//                                       + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
//                    }
                    if (formu.equals("12")) {//計劃一
                        logHtml.append("<tr><td bgcolor=\"5599FF\"  style=\"border: 1px solid black\">"
                                       + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
                    }
                    if (formu.equals("11")) {//計劃二
                        logHtml.append("<tr><td bgcolor=\"666666\"  style=\"border: 1px solid black\">"
                                       + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
                    }
                    if (formu.equals("10")) {//計劃三
                        logHtml.append("<tr><td bgcolor=\"FFAA33\"  style=\"border: 1px solid black\">"
                                       + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
                    }
                    if (formu.equals("09")) {// 計劃四
                        logHtml.append("<tr><td bgcolor=\"99FFFF\"  style=\"border: 1px solid black\">"
                                       + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
                    }

                    if (formu.equals("08")) {//計劃五
                        logHtml.append("<tr><td bgcolor=\"B94FFF\"  style=\"border: 1px solid black\">"
                                       + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
                    }

                    if (formu.equals("07")) {//計劃六
                        logHtml.append("<tr><td bgcolor=\"DDDDDD\"  style=\"border: 1px solid black\">"
                                      + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
                    }

                    if (formu.equals("06")) {//計劃七
                        logHtml.append("<tr><td bgcolor=\"FF8888\"  style=\"border: 1px solid black\">"
                                       + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
                    }

                    if (formu.equals("05")) {//計劃八
                        logHtml.append("<tr><td bgcolor=\"DEB887\"  style=\"border: 1px solid black\">"
                                       + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
                    }

                    if (formu.equals("04")) {//計劃九
                        logHtml.append("<tr><td bgcolor=\"66FF66\"  style=\"border: 1px solid black\">"
                                       + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
                    }

                    if (formu.equals("03")) {//計劃十
                        logHtml.append("<tr><td bgcolor=\"CCFF99\"  style=\"border: 1px solid black\">"
                                       + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
                    }

                    if (formu.equals("02")) {//計劃1 1
                        logHtml.append("<tr><td bgcolor=\"FFB3FF\"  style=\"border: 1px solid black\">"
                                       + v.substring(0, v.lastIndexOf("(")) + "</td></tr>");
                    }

                }
                logHtml.append("</table>");
                j.addProperty("logHtml", logHtml.toString().substring(8, logHtml.length()));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                try {
                    fileIn.close();
                } catch (Exception ex) {
                }
            }

            return j.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "null";
    }

    @RequestMapping("/getPhase")
    public String getPhase(@RequestParam("user") String user, @RequestParam("pwd") String pwd,
                           @RequestParam("boardType") String boardType) throws Exception {
        try {
            
            String nexphase = "";
            if(boardType.equals("0")) {
                if(h == null) {
                    h = httpClientCookie.getInstance(user, pwd);
                }
              
                String open = h.getOpenBall_boat();
                JsonParser parser = new JsonParser();
                JsonObject o = parser.parse(open).getAsJsonObject();
                JsonObject data = o.get("data").getAsJsonObject();
                String phase = data.get("draw_phase").getAsString();
                 
                JsonArray draw_result = data.getAsJsonArray("draw_result");
                String totalcode = "";
                for(int i = 0; i<draw_result.size();i++) {
                    String code = draw_result.get(i).getAsString().substring(0, 1).equals("0") ?  draw_result.get(i).getAsString().substring(1, 2) 
                                                                                               : draw_result.get(i).getAsString();
                    totalcode += code +",";
                    //Utils.WritePropertiesFile("history", phase, code);
                }
                Utils.WritePropertiesFile("history", phase, totalcode.substring(0,totalcode.length()-1));

        

               
                
            }else   if(boardType.equals("4")) {//福財神
      
//              String open = nd_h.getTodayWin(nd_h.getCookie());
//              JsonParser parser = new JsonParser();
              JsonObject o = LeeinHttpClient.getLastPhase();
              
               String phase = o.get("drawNumber").getAsString();
               String code =  o.get("code").getAsString();
//              JsonArray draw_result = data.getAsJsonArray("draw_result");
//              String totalcode = "";
//              for(int i = 1; i<11;i++) {
//                  String code = data.get("po"+i).getAsString();
//                  totalcode += code +",";
//                  //Utils.WritePropertiesFile("history", phase, code);
//              }
              Utils.WritePropertiesFile("history", phase, code);

      

              
              
          }else   if(boardType.equals("5")) {
                
//                String HH = new SimpleDateFormat("HH").format(new Date());
//                
//                writeOpenSource();
                if(nd_h == null) {
                    nd_h = NewDaliHttpClient.getInstance(user, pwd);
                }
              
//                String open = nd_h.getTodayWin(nd_h.getCookie());
//                JsonParser parser = new JsonParser();
                JsonObject o = nd_h.getTodayWin(nd_h.getCookie());
                JsonObject data = o.get("lotteryResult").getAsJsonObject().get("moreResult").getAsJsonObject().get("1").getAsJsonObject();
                String phase = data.get("pissue").getAsString();
                 
                JsonArray draw_result = data.getAsJsonArray("draw_result");
                String totalcode = "";
                for(int i = 1; i<11;i++) {
                    String code = data.get("po"+i).getAsString();
                    totalcode += code +",";
                    //Utils.WritePropertiesFile("history", phase, code);
                }
                Utils.WritePropertiesFile("history", phase, totalcode.substring(0,totalcode.length()-1));

        

                
                
            }
            nexphase = Utils.getMaxPhase();
            return Long.toString(Long.valueOf(nexphase) + 1 ) ;
//            long unixTime = System.currentTimeMillis() / 1000L;
//
//            String query = "McID=03RGK&Nose=bb4NvVOMtX&Sern=0&Time=" + unixTime;
//            String sign = Utils.MD5(query + "&key=EUAwtKL0A1").toUpperCase();
//
//            String url = "http://47.90.109.200/chatbet_v3/award_sync/get_award.php?" + query + "&Sign=" + sign;
//
//            String ret = Utils.httpClientGet(url);
//            JsonParser parser = new JsonParser();
//            JsonObject o = parser.parse(ret).getAsJsonObject();
//            String a = o.get("Award").getAsString();
//            JsonArray data = parser.parse(a).getAsJsonArray();
//            String drawIssue = data.get(0).getAsJsonObject().get("I").getAsString();
//            if (drawIssue != null && !drawIssue.equals("")) {
//                return Integer.toString(Integer.parseInt(drawIssue) + 1);
//            }

        } catch (Exception e) {
            saveLog(user + "error", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " : getPhase 斷");
            if (boardType.equals("0"))
                h = httpClientCookie.getInstance(user, pwd);
            else if (boardType.equals("4"))
                LeeinHttpClient.initPage(user, pwd);
            else if (boardType.equals("5"))
                nd_h = NewDaliHttpClient.getInstance(user, pwd);
            e.printStackTrace();

        } finally {

        }
        return "null";

    }
    
   public static void writeOpenSource() {
       try {
           String yyymmdd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
           
           String url = "http://api.api68.com/pks/getPksHistoryList.do?date="+yyymmdd+"&lotCode=10057";
           String ret = Utils.httpClientGet(url);
           JsonParser parser = new JsonParser();
           JsonObject o = parser.parse(ret).getAsJsonObject();
           
           
           JsonObject result = o.get("result").getAsJsonObject();
           JsonArray data = result.getAsJsonArray("data");
           JsonObject paymentObj = data.get(0).getAsJsonObject();
           String     code     = paymentObj.get("preDrawCode").getAsString();
           String     preDrawIssue = paymentObj.get("preDrawIssue").getAsString();
           
           String[] c = code.toString().split(",");
           String c1 = c[0].substring(0, 1).equals("0") ? c[0].substring(1, 2) : c[0] ;
           String c2 = c[1].substring(0, 1).equals("0") ? c[1].substring(1, 2) : c[1] ; 
           String c3 = c[2].substring(0, 1).equals("0") ? c[2].substring(1, 2) : c[2] ; 
           String c4 = c[3].substring(0, 1).equals("0") ? c[3].substring(1, 2) : c[3] ; 
           String c5 = c[4].substring(0, 1).equals("0") ? c[4].substring(1, 2) : c[4] ; 
           String c6 = c[5].substring(0, 1).equals("0") ? c[5].substring(1, 2) : c[5] ; 
           String c7 = c[6].substring(0, 1).equals("0") ? c[6].substring(1, 2) : c[6] ; 
           String c8 = c[7].substring(0, 1).equals("0") ? c[7].substring(1, 2) : c[7] ; 
           String c9 = c[8].substring(0, 1).equals("0") ? c[8].substring(1, 2) : c[8] ; 
           String c0 = c[9].substring(0, 1).equals("0") ? c[9].substring(1, 2) : c[9] ; 
           String cc = c1 + "," + c2 + "," + c3+ "," + c4+ "," + c5
                   + "," + c6+ "," + c7+ "," + c8+ "," + c9+ "," + c0 ;
           
           Utils.WritePropertiesFile("history", preDrawIssue, cc);
       }catch(Exception e) {
           e.printStackTrace();
       }
       
       
   } 
    
    public static Map combindHistoryMap() {
        Map m = new HashMap();
        try {
            Date dNow = new Date();   //当前时间

            Date dBefore = new Date();

            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(dNow);//把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
            dBefore = calendar.getTime();   //得到前一天的时间

            String byyymmdd = new SimpleDateFormat("yyyy-MM-dd").format(dBefore); 
            
            String yyymmdd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            
            String url = "http://api.api68.com/pks/getPksHistoryList.do?date="+yyymmdd+"&lotCode=10057";
            String ret = Utils.httpClientGet(url);
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(ret).getAsJsonObject();
            
            
            JsonObject result = o.get("result").getAsJsonObject();
            JsonArray data = result.getAsJsonArray("data");
            
           
            for (JsonElement pa : data) {
                JsonObject paymentObj = pa.getAsJsonObject();
                String     code     = paymentObj.get("preDrawCode").getAsString();
                String     preDrawIssue = paymentObj.get("preDrawIssue").getAsString();
                m.put(preDrawIssue, code);
            }
            
            
            url = "http://api.api68.com/pks/getPksHistoryList.do?date="+byyymmdd+"&lotCode=10057";
            ret = Utils.httpClientGet(url);
            parser = new JsonParser();
            o = parser.parse(ret).getAsJsonObject();
            
            
            result = o.get("result").getAsJsonObject();
            data = result.getAsJsonArray("data");
            
           
            for (JsonElement pa : data) {
                JsonObject paymentObj = pa.getAsJsonObject();
                String     code     = paymentObj.get("preDrawCode").getAsString();
                String     preDrawIssue = paymentObj.get("preDrawIssue").getAsString();
                m.put(preDrawIssue, code);
            }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
       
        
        return m;
    }

    @RequestMapping("/getCode")
    public String getCode(@RequestParam("phase") String phase) {
        FileInputStream fileIn = null;
        try {
            Properties configProperty = new Properties();
            String path = System.getProperty("user.dir");
            String hisFile = path + "/history.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(fileIn);
            if (configProperty.getProperty(phase) != null)
                return configProperty.getProperty(phase);
            else {
                Utils.WritePropertiesFile("errorPhase", phase, "");
                return "null";
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileIn.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return "null";
    }

    @RequestMapping("/saveParam")
    public String saveParam(@RequestParam("user") String user, @RequestParam("type") String type,
                            @RequestParam("betlist") String betlist, @RequestParam("betlist2") String betlist2,
                            @RequestParam("betlist3") String betlist3, @RequestParam("betlist4") String betlist4,
                            @RequestParam("betlist5") String betlist5, @RequestParam("betlist6") String betlist6,
                            @RequestParam("betlist7") String betlist7, @RequestParam("betlist8") String betlist8,
                            @RequestParam("betlist9") String betlist9, @RequestParam("betlist10") String betlist10,
                            @RequestParam("betlist11") String betlist11, @RequestParam("betlist12") String betlist12,
                            @RequestParam("betproject5") String betproject5,
                            @RequestParam("betproject6") String betproject6,
                            

                            
                            @RequestParam("stoplose") String stoplose, @RequestParam("stopwin") String stopwin,
                            @RequestParam("startstatus") String startstatus, 
                            @RequestParam("s_h") String s_h, @RequestParam("s_m") String s_m, 
                            @RequestParam("e_h") String e_h, @RequestParam("e_m") String e_m, 
                            @RequestParam("s_h_stop") String s_h_stop, @RequestParam("s_m_stop") String s_m_stop, 
                            @RequestParam("autoStopCheck") String autoStopCheck,
                            @RequestParam("stoppoint") String stoppoint

    ) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;

        try {
            Properties configProperty = new Properties();
            String path = System.getProperty("user.dir");
            String hisFile = path + "/" + user + ".properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(fileIn);

            configProperty.setProperty("type", type);
            configProperty.setProperty("betlist", betlist);
            configProperty.setProperty("betlist2", betlist2);
            configProperty.setProperty("betlist3", betlist3);
            configProperty.setProperty("betlist4", betlist4);
            configProperty.setProperty("betlist5", betlist5);
            configProperty.setProperty("betlist6", betlist6);
            configProperty.setProperty("betlist7", betlist7);
            configProperty.setProperty("betlist8", betlist8);
            configProperty.setProperty("betlist9", betlist9);
            configProperty.setProperty("betlist10", betlist10);
            configProperty.setProperty("betlist11", betlist11);
            configProperty.setProperty("betlist12", betlist12);
            configProperty.setProperty("betproject5", betproject5);
            configProperty.setProperty("betproject6", betproject6);

            configProperty.setProperty("stoplose", stoplose);
            configProperty.setProperty("stopwin", stopwin);
            configProperty.setProperty("startstatus", startstatus);
            configProperty.setProperty("s_h", s_h);
            configProperty.setProperty("s_m", s_m);
            configProperty.setProperty("e_h", e_h);
            configProperty.setProperty("e_m", e_m);
            configProperty.setProperty("s_h_stop", s_h_stop);
            configProperty.setProperty("s_m_stop", s_m_stop);
            configProperty.setProperty("autoStopCheck", autoStopCheck);
            configProperty.setProperty("stoppoint", stoppoint);
            fileOut = new FileOutputStream(file);
            configProperty.store(fileOut, "sample properties");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }

        return "null";
    }

    @RequestMapping("/saveLog")
    public String saveLog(String user, String log) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;

        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/" + user + "_log.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));
            String result = java.net.URLDecoder.decode(log, "UTF-8");
            i++;
            configProperty.setProperty(fillZero(Integer.toString(i)), result);

            fileOut = new FileOutputStream(file);
            configProperty.store(new OutputStreamWriter(fileOut, "UTF-8"), "sample properties");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }

        return "null";
    }

    public String saveOverLog(String user, String log, String c) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;

        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/" + user + "_over_log.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));
            String result = java.net.URLDecoder.decode(log, "UTF-8");

            configProperty.setProperty(log, c);

            fileOut = new FileOutputStream(file);
            configProperty.store(new OutputStreamWriter(fileOut, "UTF-8"), "sample properties");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }

        return "null";
    }

    Map overmp = new HashMap();

    @RequestMapping("/checkOver")
    public String checkOver(@RequestParam("user") String user, @RequestParam("phase") String phase,
                            @RequestParam("code") String code, @RequestParam("betproject") String betproject) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;

        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/" + user + "_over_log.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));

            String c[] = code.split(",");

            JsonObject j = new JsonObject();

            if (c.length != 10)
                return "null";

            

            for (int x = 1; x < 13; x++) { // x → 公式幾
                for (int i = 0; i < 10; i++) {

                    int sn = i + 1;

                    

                        for (int overi = 1; overi < 13; overi++) {
                            String key = phase + "@" + sn + "@" + c[i] + "@" + x + "@" + overi; 
                            
                            
                            if (configProperty.getProperty(key) != null) {
                                if (overmp.get(user + key) == null) {
                                    overmp.put(user + key, "put");
                                    over_i++;
                                    // Utils.WritePropertiesFile(user+"overLOGDIS_log",
                                    // fillZero(Integer.toString(over_i)), "第"+phase +
                                    // "期，第" + sn + "名，號碼(" + code + ") 已過關!(第"+c+"關)");
                                    String t = new SimpleDateFormat("HH:mm:ss").format(new Date());
                                    Utils.WritePropertiesFile(user + "overLOGDIS_log",
                                                              fillZero(Integer.toString(over_i)),
                                                              "第" + phase + "期，第" + sn + "名，號碼(" + c[i] + ")已過關!(第"
                                                                                                  + configProperty.getProperty(key)
                                                                                                  + "關)" + "(公式" + x
                                                                                                  + ")");

                                    j.addProperty(covertIntToLatter(overi) + x , "Y");
                                }

                            }
                        }

                   

                }

            }

            if (getCode(phase).equals("null")) { //兌不到獎
                j.addProperty("checkFlag", "N");
            } else {
                j.addProperty("checkFlag", "Y");
            }
            return j.toString();

        } catch (Exception e) {
            System.out.println(phase);
            System.out.println(code);
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
            removeOverLog(user,phase,overmp);

        }

        return "null";
    }
    
public static void removeOverLog(String user,String checkPhase,Map<String,String> overmp) {
        
        FileInputStream fileIn = null ;
        FileOutputStream fileOut = null;

        long removePhase = Long.valueOf(checkPhase)   - 1 ;
        try {
            Properties configProperty = new OrderedProperties();
            String path = System.getProperty("user.dir");
            String hisFile = path + "/" + user + "_over_log.properties";
            File file = new File(hisFile);
            if (!file.exists())
                file.createNewFile();
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));
            
           

             ArrayList<String> removeList = new ArrayList();
            for (Enumeration e = configProperty.propertyNames(); e.hasMoreElements();) {
                String key = e.nextElement().toString();
               
                removeList.add(key);
                
            } 
            
            for(String key:removeList) {
                String phase = key.split("@")[0];
                if(Long.valueOf(phase)  <= removePhase) {
                    configProperty.remove(key); 

                    //System.out.println("remove ok" + key);
                }
            }
            
            fileOut = new FileOutputStream(file);
            configProperty.store(new OutputStreamWriter(fileOut, "UTF-8"), "sample properties");
            
            
            for (Map.Entry<String, String> entry : overmp.entrySet())
            {
                String key = entry.getKey();
                String phase = key.split("@")[1];
                if(Long.valueOf(phase)  <= removePhase) {
                    overmp.remove(key);
                    //System.out.println("remove map ok" + key);

                }
                
                
            }
            System.gc() ;
        }catch(Exception e) {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }finally{

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }
        
    }

    @RequestMapping("/clearLog")
    public String clearLog(@RequestParam("user") String user) {

        try {

            String path = System.getProperty("user.dir");
            String hisFile = path + "/" + user + "_log.properties";
            File file = new File(hisFile);
            // System.out.println(hisFile);
            // System.out.println(file.exists());

            if (file.exists()) {
                file.delete();
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                                   + "clearLog delete suc");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return "null";
    }

    public int computeIndex(String sn, String code) {
        return (Integer.parseInt(sn) - 1) * 10 + (Integer.parseInt(code) - 1);
    }

    public int computeIndexForBs(String sn, String type) {
        if (sn.equals("1") && type.equals("big")) {
            return 0;
        } else if (sn.equals("1") && type.equals("small")) {
            return 1;
        } else if (sn.equals("1") && type.equals("s")) {
            return 2;
        } else if (sn.equals("1") && type.equals("d")) {
            return 3;
        }

        else if (sn.equals("2") && type.equals("big")) {
            return 6;
        } else if (sn.equals("2") && type.equals("small")) {
            return 7;
        } else if (sn.equals("2") && type.equals("s")) {
            return 8;
        } else if (sn.equals("2") && type.equals("d")) {
            return 9;
        }

        else if (sn.equals("3") && type.equals("big")) {
            return 12;
        } else if (sn.equals("3") && type.equals("small")) {
            return 13;
        } else if (sn.equals("3") && type.equals("s")) {
            return 14;
        } else if (sn.equals("3") && type.equals("d")) {
            return 15;
        }

        else if (sn.equals("4") && type.equals("big")) {
            return 18;
        } else if (sn.equals("4") && type.equals("small")) {
            return 19;
        } else if (sn.equals("4") && type.equals("s")) {
            return 20;
        } else if (sn.equals("4") && type.equals("d")) {
            return 21;
        }

        else if (sn.equals("5") && type.equals("big")) {
            return 24;
        } else if (sn.equals("5") && type.equals("small")) {
            return 25;
        } else if (sn.equals("5") && type.equals("s")) {
            return 26;
        } else if (sn.equals("5") && type.equals("d")) {
            return 27;
        }

        else if (sn.equals("6") && type.equals("big")) {
            return 30;
        } else if (sn.equals("6") && type.equals("small")) {
            return 31;
        } else if (sn.equals("6") && type.equals("s")) {
            return 32;
        } else if (sn.equals("6") && type.equals("d")) {
            return 33;
        }

        else if (sn.equals("7") && type.equals("big")) {
            return 34;
        } else if (sn.equals("7") && type.equals("small")) {
            return 35;
        } else if (sn.equals("7") && type.equals("s")) {
            return 36;
        } else if (sn.equals("7") && type.equals("d")) {
            return 37;
        }

        else if (sn.equals("8") && type.equals("big")) {
            return 38;
        } else if (sn.equals("8") && type.equals("small")) {
            return 39;
        } else if (sn.equals("8") && type.equals("s")) {
            return 40;
        } else if (sn.equals("8") && type.equals("d")) {
            return 41;
        }

        else if (sn.equals("9") && type.equals("big")) {
            return 42;
        } else if (sn.equals("9") && type.equals("small")) {
            return 43;
        } else if (sn.equals("9") && type.equals("s")) {
            return 44;
        } else if (sn.equals("9") && type.equals("d")) {
            return 45;
        }

        else if (sn.equals("10") && type.equals("big")) {
            return 46;
        } else if (sn.equals("10") && type.equals("small")) {
            return 47;
        } else if (sn.equals("10") && type.equals("s")) {
            return 48;
        } else {
            return 49;
        }

    }

    int recoup = 0;

    @RequestMapping("/specialbet")
    public String specialbet(@RequestParam("user") String user, @RequestParam("sn") String sn,
                             @RequestParam("amount") String amount, @RequestParam("betphase") String betphase,
                             @RequestParam("c") String c, @RequestParam("codeList") String codeList,
                             @RequestParam("formu") String formu, @RequestParam("boardType") String boardType,
                             @RequestParam("betsn") String betsn

    ) {

        String betsnArray[] = betsn.split(",");
        bi++;
        if (amount.equals("0") || (amount.equals("1") && boardType.equals("0"))){
            for (String str : betsnArray) {
                String overLog = betphase + "@" + str + "@" + codeList + "@" + formu + "@" + sn; 
                
                saveOverLog(user, overLog, c);
            }
            
             
//            String betlog = "第" + betphase + "期" +
//                    "計劃" +   sn 
//                    + "，第" + betsn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
//                    + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
//            saveLog(user + "bet", betlog);
    
            return "";
        }

        try {
            if (boardType.equals("0")) {
               // String r = h.getoddsInfo();
                String r = h.getoddsInfo_boat();
                // 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数

                JsonParser pr = new JsonParser();
                JsonObject po = pr.parse(r).getAsJsonObject();
                JsonObject data = po.getAsJsonObject("data");
                Map<Integer, String> normal = new TreeMap<Integer, String>();
                Utils.producePl(normal, r); // 產生倍率 for single
                p_id = data.get("p_id").getAsString();

                // if (ret.indexOf(user) > -1) {

                String ossid = "";
                String pl = "";
                String i_index = "";
                String m = "";
                int i = 0;
                for (String bsn : betsnArray) {
                    // String overLog = betphase + "@" + sn + "@" + str + "@" +
                    // formu;
                    // saveOverLog(user, overLog, c);
                    //
                    int index = computeIndex(bsn, codeList);
                    String id_pl = normal.get(index).toString(); // 15@1.963
                    ossid += id_pl.split("@")[0] + ",";
                    pl += id_pl.split("@")[1] + ",";
                    i_index += i + ",";
                    m += amount + ",";
                    i++;
                }

               // String betRet = h.normalBet(p_id, ossid, pl, i_index, m, "jscar_d1_10");
                String betRet = h.normalBet_boat(p_id, ossid, pl, i_index, m, "xyft5_d1_10");
                JsonParser parser = new JsonParser();
                JsonObject o = parser.parse(betRet).getAsJsonObject();
                String resCode = o.get("success").getAsString();

                if (resCode.equals("200")) {

                    for (String str : betsnArray) {
                        String overLog = betphase + "@" + str + "@" + codeList + "@" + formu + "@" + sn;
                        saveOverLog(user, overLog, c);
                    }

                    String betlog = "第" + betphase + "期" +
                            "計劃" +   sn 
                            + "，第" + betsn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                            + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
                    saveLog(user + "bet", betlog);

                } else {
                    recoup++;
                    if (recoup == 3) {
                        recoup = 0;
                        return "error";
                    }
                    // System.out.println(o.toString());
                    String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(失敗)" + "(公式" + formu + ")";
                    // saveLog(user + "bet", betlog);
                    saveLog(user + "error", o.toString() + " bet error:" + betlog);
                    return specialbet(user, sn, amount, betphase, c, codeList, formu, boardType, betsn);
                }

            }else if (boardType.equals("5")) {
               JsonObject r =  nd_h.getTodayWin(nd_h.getCookie());
               JsonObject betRate = r.get("betRate").getAsJsonObject();
               JsonArray a = new JsonArray();

                for (String bsn : betsnArray) {
                    int computeInt = Integer.parseInt(bsn)*10 +  Integer.parseInt(codeList) + 161;
                    String codeBetRate =  betRate.get(Integer.toString(computeInt)).getAsString();
                    JsonObject d = new JsonObject();
                    d.addProperty("detailID", computeInt);
                    d.addProperty("betRate", codeBetRate);
                    d.addProperty("betMoney", amount);
                    a.add(d);
                }

                JsonObject result = NewDaliHttpClient.httpPostBet(nd_h.getCookie(), a);

                if (result.get("pass").getAsBoolean() == true) {

                    for (String str : betsnArray) {
                        String overLog = betphase + "@" + str + "@" + codeList + "@" + formu + "@" + sn;
                        saveOverLog(user, overLog, c);
                    }

                    String betlog = "第" + betphase + "期" +
                            "計劃" +   sn 
                            + "，第" + betsn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                            + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
                    saveLog(user + "bet", betlog);

                } else {
                    
                    String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(失敗)" + "(公式" + formu + ")";
                    // saveLog(user + "bet", betlog);
                    saveLog(user + "error", result.toString() + " bet error:" + betlog);
                    return null;
                }

            } else if (boardType.equals("1")) { //華山
                JsonParser pr = new JsonParser();
                String r = MoutainHttpClient.httpPostBetBySn(mountain_url[mountain_index % 4] + "/?m=bet",
                                                             mountain_token_sessid,
                                                             betphase,
                                                             amount,
                                                             betsnArray,
                                                             codeList);
                JsonObject po = pr.parse(r).getAsJsonObject();
                String s = po.get("msg").getAsString();
                if (s.equals("投注成功")) {
                    for (String str : betsnArray) {
                        String overLog = betphase + "@" + str + "@" + codeList + "@" + formu + "@" + sn;
                        saveOverLog(user, overLog, c);
                    }

                    String betlog = "第" + betphase + "期" +
                            "計劃" +   sn 
                            + "，第" + betsn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                            + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
                    saveLog(user + "bet", betlog);
                } else {
                    recoup++;
                    if (recoup == 3) {
                        recoup = 0;
                        return "error";
                    }

                    String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(失敗)" + "(公式" + formu + ")";
                    // saveLog(user + "bet", betlog);
                    saveLog(user + "error", s.toString() + " bet error:" + betlog);

                    return specialbet(user, sn, amount, betphase, c, codeList, formu, boardType, betsn);
                }
            } else if (boardType.equals("2")) { //大立
                JsonParser pr = new JsonParser();
                JsonObject j = DaliHttpClient.getBetMD5_PL();
                String MD5 = j.get("MD5").getAsString();

                JsonArray a = j.getAsJsonArray("DATAODDS");

                String betString = "";//40001,9.909,1|40018,9.909,1|40036,9.909,1
                for (String str : betsnArray) {
                    int index = DaliHttpClient.getPlIndex(str, codeList);
                    JsonObject bet = a.get(index).getAsJsonObject();
                    String pl = bet.get("OddsValue1").getAsString();
                    String betItemNo = bet.get("ItemNO").getAsString();
                    betString += betItemNo + "," + pl + "," + amount + "|";
                }
                betString = betString.substring(0, betString.length() - 1);

                String betid = DaliHttpClient.getBetID(betString);

                JsonObject result = DaliHttpClient.dali_bet(betid, MD5);

                if ((result.get("FaildReason").getAsString()).equals("0")) {
                    for (String str : betsnArray) {
                        String overLog = betphase + "@" + str + "@" + codeList + "@" + formu + "@" + sn; 
                          
                        //String overLog = betphase + "@" + str + "@" + codeList + "@" + formu + "@" + sn;
                        saveOverLog(user, overLog, c);
                    }

                    String betlog = "第" + betphase + "期" +
                            "計劃" +   sn 
                            + "，第" + betsn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                            + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
                    saveLog(user + "bet", betlog);
                } else {
                   /* recoup++;
                    if (recoup == 3) {
                        recoup = 0;
                        return "error";
                    }*/
                    String betlog = "第" + betphase + "期" + "，第" + betsn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(失敗)" + "(公式" + formu + ")";
                    // saveLog(user + "bet", betlog);
                    saveLog(user + "error", result.toString() + " bet error:" + betlog);
                    return "error";
                }

            }else if (boardType.equals("3")|| boardType.equals("4") ) {
                String url = boardType.equals("3")?(leein_url[leein_index % 4]): (futsai_url[futsai_index % 4]) ;
                String cookie = boardType.equals("3")?(leein_php_cookid): (futsai_php_cookid) ;

                JsonObject pl = LeeinHttpClient.getPl();

                JsonArray a = new JsonArray();

                for (String str : betsnArray) {
                    String key = "B" + str + "_" + codeList;
                    BigDecimal p = pl.get(key).getAsBigDecimal();
                    JsonObject d = new JsonObject();
                    d.addProperty("game", "B" + str);
                    d.addProperty("contents", codeList);
                    d.addProperty("amount", amount);
                    d.addProperty("odds", p);
                    a.add(d);
                }

                JsonObject bet = new JsonObject();
                bet.addProperty("lottery", "XYFT");
                bet.addProperty("drawNumber", betphase);
                bet.add("bets", a);
                bet.addProperty("ignore", "false");

                String betS = bet.toString();

                JsonObject result = LeeinHttpClient.httpPostBet( betS);

                if (result.get("status").getAsString().equals("0")) {
                    for (String str : betsnArray) {
                        String overLog = betphase + "@" + str + "@" + codeList + "@" + formu + "@" + sn;
                        saveOverLog(user, overLog, c);
                    }

                    String betlog = "第" + betphase + "期" +
                            "計劃" +   sn 
                            + "，第" + betsn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                            + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
                    saveLog(user + "bet", betlog);
                } else {
                    
                    String betlog = "第" + betphase + "期" + "，第" + betsn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(失敗)" + "(公式" + formu + ")";
                    // saveLog(user + "bet", betlog);
                    saveLog(user + "error", result.toString() + " bet error:" + betlog);
                    return "error";
                }

            }
        } catch (Exception e) {
            saveLog(user + "error",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + e.getMessage() + " : specialbet 斷"
                                    + boardType);
            //h = httpClientCookie.getInstance(user, pwd);
            e.printStackTrace();
            return "error";
        }

        return "";
    }

    // sn : 1~ 0 , code : 01~10
    // sn : 1~ 0 , code : 01~10
    @RequestMapping("/bet")
    public String bet(@RequestParam("user") String user, @RequestParam("sn") String sn,
                      @RequestParam("amount") String amount, @RequestParam("betphase") String betphase,
                      @RequestParam("c") String c, @RequestParam("codeList") String codeList,
                      @RequestParam("formu") String formu, @RequestParam("boardType") String boardType,
                      @RequestParam("displaysn") String displaysn

    ) {

        try {
            String code[] = codeList.split(",");
            bi++;
            if (amount.equals("0") || (amount.equals("1") && boardType.equals("0"))) {
                for (String str : code) {
                    String overLog = betphase + "@" + sn + "@" + str + "@" + formu + "@" + displaysn;
                    saveOverLog(user, overLog, c);
                }
                String betlog = "第" + betphase + "期"  +
                        "計劃" +   displaysn 
                        + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
                saveLog(user + "bet", betlog);
                
//                String betlog = "第" + betphase + "期"  +
//                        "計劃" +   displaysn 
//                        + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
//                                + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
//                saveLog(user + "bet", betlog);
                return "";
            }

            if (boardType.equals("0")) {
              
                String r = h.getoddsInfo_boat();
                // 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数

                JsonParser pr = new JsonParser();
                JsonObject po = pr.parse(r).getAsJsonObject();
                JsonObject data = po.getAsJsonObject("data");
                Map<Integer, String> normal = new TreeMap<Integer, String>();
                Utils.producePl(normal, r); // 產生倍率 for single
                p_id = data.get("p_id").getAsString();

                // if (ret.indexOf(user) > -1) {

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

                String betRet = h.normalBet_boat(p_id, ossid, pl, i_index, m, "xyft5_d1_10");
                JsonParser parser = new JsonParser();
                JsonObject o = parser.parse(betRet).getAsJsonObject();
                String resCode = o.get("success").getAsString();

                if (resCode.equals("200")) {

                    for (String str : code) {
                        String overLog = betphase + "@" + sn + "@" + str + "@" + formu + "@" + displaysn;
                        saveOverLog(user, overLog, c);
                    }

                    String betlog = "第" + betphase + "期"  +
                            "計劃" +   displaysn 
                            + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
                    saveLog(user + "bet", betlog);

                }  

            } else if (boardType.equals("1")) { //華山
                JsonParser pr = new JsonParser();
                String r = MoutainHttpClient.httpPostBet(mountain_url[mountain_index % 4] + "/?m=bet",
                                                         mountain_token_sessid,
                                                         betphase,
                                                         amount,
                                                         sn,
                                                         code);
                JsonObject po = pr.parse(r).getAsJsonObject();
                String s = po.get("msg").getAsString();
                if (s.equals("投注成功")) {
                    for (String str : code) {
                        String overLog = betphase + "@" + sn + "@" + str + "@" + formu + "@" + displaysn;
                        saveOverLog(user, overLog, c);
                    }

                    String betlog = "第" + betphase + "期"  +
                            "計劃" +   displaysn 
                            + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
                    saveLog(user + "bet", betlog);
                } else {
                    String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(失敗)" + "(公式" + formu + ")";
                    // saveLog(user + "bet", betlog);
                    saveLog(user + "error", s.toString() + " bet error:" + betlog);

                    return mountaionRecoup(user, sn, amount, betphase, c, codeList, formu);
                }
            } else if (boardType.equals("2")) { //大立
                JsonParser pr = new JsonParser();
                JsonObject j = DaliHttpClient.getBetMD5_PL();
                String MD5 = j.get("MD5").getAsString();

                JsonArray a = j.getAsJsonArray("DATAODDS");

                String betString = "";//40001,9.909,1|40018,9.909,1|40036,9.909,1
                for (String str : code) {
                    int index = DaliHttpClient.getPlIndex(sn, str);
                    JsonObject bet = a.get(index).getAsJsonObject();
                    String pl = bet.get("OddsValue1").getAsString();
                    String betItemNo = bet.get("ItemNO").getAsString();
                    betString += betItemNo + "," + pl + "," + amount + "|";
                }
                betString = betString.substring(0, betString.length() - 1);

                String betid = DaliHttpClient.getBetID(betString);

                JsonObject result = DaliHttpClient.dali_bet(betid, MD5);

                if ((result.get("FaildReason").getAsString()).equals("0")) {
                    for (String str : code) {
                        String overLog = betphase + "@" + sn + "@" + str + "@" + formu + "@" + displaysn;
                        saveOverLog(user, overLog, c);
                    }

                    String betlog = "第" + betphase + "期"  +
                            "計劃" +   displaysn 
                            + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
                    saveLog(user + "bet", betlog);
                } else {
                    /*recoup++;
                    if (recoup == 3) {
                        recoup = 0;
                        return "error";
                    }*/
                    String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(失敗)" + "(公式" + formu + ")";
                    // saveLog(user + "bet", betlog);
                    saveLog(user + "error", result.toString() + " bet error:" + betlog);
                    return "error";
                }

            } else if (boardType.equals("3") || boardType.equals("4")) {
                String url = boardType.equals("3")?(leein_url[leein_index % 4]): (futsai_url[futsai_index % 4]) ;
                String cookie = boardType.equals("3")?(leein_php_cookid): (futsai_php_cookid) ;

                JsonObject pl = LeeinHttpClient.getPl();

                JsonArray a = new JsonArray();

                for (String str : code) {
                    String key = "B" + sn + "_" + str;
                    BigDecimal p = pl.get(key).getAsBigDecimal();
                    JsonObject d = new JsonObject();
                    d.addProperty("game", "B" + sn);
                    d.addProperty("contents", str);
                    d.addProperty("amount", amount);
                    d.addProperty("odds", p);
                    a.add(d);
                }

                JsonObject bet = new JsonObject();
                bet.addProperty("lottery", "XYFT");
                bet.addProperty("drawNumber", betphase);
                bet.add("bets", a);
                bet.addProperty("ignore", "false");

                String betS = bet.toString();

                JsonObject result = LeeinHttpClient.httpPostBet(betS);

                if (result.get("status").getAsString().equals("0")) {
                    for (String str : code) {
                        String overLog = betphase + "@" + sn + "@" + str + "@" + formu + "@" + displaysn;
                        saveOverLog(user, overLog, c);
                    }

                    String betlog = "第" + betphase + "期"  +
                            "計劃" +   displaysn 
                            + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
                    saveLog(user + "bet", betlog);

                }  


            } else if (boardType.equals("5")) {
                JsonObject r =  nd_h.getTodayWin(nd_h.getCookie());
                JsonObject betRate = r.get("betRate").getAsJsonObject();

//          

                JsonArray a = new JsonArray();

                for (String str : code) {
                    int computeInt = Integer.parseInt(sn)*10 +  Integer.parseInt(str) + 161;
                    String codeBetRate =  betRate.get(Integer.toString(computeInt)).getAsString();
                    
                    JsonObject d = new JsonObject();
                    d.addProperty("detailID", computeInt);
                    d.addProperty("betRate", codeBetRate);
                    d.addProperty("betMoney", amount);
                    a.add(d);
                }

              

                JsonObject result = NewDaliHttpClient.httpPostBet(nd_h.getCookie(), a);

                if (result.get("pass").getAsBoolean() == true) {
                    for (String str : code) {
                        String overLog = betphase + "@" + sn + "@" + str + "@" + formu + "@" + displaysn;
                        saveOverLog(user, overLog, c);
                    }

                    String betlog = "第" + betphase + "期"  +
                            "計劃" +   displaysn 
                            + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(成功)" + "(公式" + formu + ")";
                    saveLog(user + "bet", betlog);
                } else {
                     
                    String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關"
                                    + "投注點數(" + amount + ")" + "(失敗)" + "(公式" + formu + ")";
                    // saveLog(user + "bet", betlog);
                    saveLog(user + "error", result.toString() + " bet error:" + betlog);
                    return "error";
                }

            }

        } catch (Exception e) {
            saveLog(user + "error",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + e.getMessage() + " : bet 斷"
                                    + boardType);
            //h = httpClientCookie.getInstance(user, pwd);
            e.printStackTrace();
            return "error";
        } finally {

        }

        return "";
    }
 
 

   
    @RequestMapping("/betBS")
    public String betBS(@RequestParam("user") String user, @RequestParam("sn") String sn,
                        @RequestParam("amount") String amount, @RequestParam("betphase") String betphase,
                        @RequestParam("type") String type, @RequestParam("c") String c,
                        @RequestParam("codeList") String codeList) {
        // betStr 1OUo1
        // long unixTimestamp = Instant.now().getEpochSecond();
        // String timeStampe = Long.toString(unixTimestamp) + getRandom();
        // String rate = getBSRate(ltype);
        //
        // String url = "http://203.160.143.110/www_new/app/CA/CA_bet.php?";
        // String parameter = "smstime=" + timeStampe + "" + "&allms=1117" +
        // "&uid=" + convertUid(uid)
        // + "&langx=zh-cn&betStr=" + betStr + "," + ltype + ",," + rate + "," +
        // amount + ",1," + amount
        // + "" + "&gid=" + gid + "" + "&mid=" + mid +
        // "&gtype=CA&active=bet&usertype=a&ltype=" + ltype
        // + "&username=" + user + "" + "&timestamp=" + timeStampe + "";
        // int phase = 554432 + Integer.parseInt(gid);
        try {
            // url += URLEncoder.encode(prameter, "UTF-8");
            if (h == null) {
                h = httpClientCookie.getInstance(user, pwd);
            }
            // HttpGet httpget = new HttpGet(url + parameter);
            // //System.out.println(url + parameter);
            // //httpget.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");
            // // 建立HttpPost对象
            // HttpResponse response = new DefaultHttpClient().execute(httpget);
            // // 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数
            // if (response.getStatusLine().getStatusCode() == 200) {//
            // 如果状态码为200,就是正常返回
            // String ret = EntityUtils.toString(response.getEntity());
            bi++;
            // String sn = betStr.substring(0, 1);
            // if (ret.indexOf(user) > -1) {
            String code[] = codeList.split(",");
            for (String str : code) {
                String overLog = betphase + "@" + sn + "@" + str;
                saveOverLog(user, overLog, c);
            }

            String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "投注點數("
                            + amount + ")" + "(成功)";
            saveLog(user + "bet", betlog);

            int index = computeIndexForBs(sn, type);
            String id_pl = bs.get(index).toString(); // 15@1.963
            String ossid = id_pl.split("@")[0];
            String pl = id_pl.split("@")[1];
            String betRet = h.normalBet(p_id, ossid, pl, "0", amount, "pk10_lmp");

            // String overLog = betphase + "@" + sn + "@" + code ;
            // saveOverLog(user,overLog,c);
            // saveOverLog(document.getElementById("user").value,encodeURI(overLog),c);
            // Utils.WritePropertiesFile(user+"bet",
            // fillZero(Integer.toString(bi)), "第"+phase + "期，第" + sn + "名，號碼("
            // + code + ")，金額(" + amount + ") @" + ret);
            // } else {
            // saveLog(user + "ERROR", ret);
            // }

            // Utils.WritePropertiesFile(user+"bet",
            // fillZero(Integer.toString(bi)), "第"+phase + "期，" +
            // getBSNAME(betStr) + "名，，金額(" + amount + ") @" + ret);

            // return ret;
            // }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }

        return "";
    }

    @RequestMapping("/getHistory")
    public String getHistory() {

        try {

            JsonObject j = new JsonObject();

            FileInputStream fileIn = null;
            try {
                Properties configProperty = new OrderedProperties();
                String path = System.getProperty("user.dir");
                String hisFile = path + "/history.properties";
                File file = new File(hisFile);
                if (!file.exists())
                    file.createNewFile();
                fileIn = new FileInputStream(file);
                configProperty.load(new InputStreamReader(fileIn, "UTF-8"));

                // String logHtml="";
                StringBuilder logHtml = new StringBuilder();
                // for (Map.Entry<Object, Object> e : configProperty.entrySet())
                // {
                // String key = (String) e.getKey();
                // String value = (String) e.getValue();
                // System.out.println(value);
                // logHtml.insert(0, "<tr><td style=\"border: 1px solid
                // black\">"+value+"</td></tr>");
                // //logHtml+="<tr><td style=\"border: 1px solid
                // black\">"+value+"</td></tr>";
                // }
                for (Enumeration e = configProperty.propertyNames(); e.hasMoreElements();) {
                    String key = e.nextElement().toString();
                    String v = configProperty.getProperty(key);
                    String array[] = v.split(",");

                    String temp = "<tr ><td align=center style=\"border: 1px solid gray;border-collapse: collapse;padding-left: 0.1cm; padding-right: 0.1cm;\"> "
                                  + "<font size=\"5\">" + key + "</font></td>";
                    //temp += "<td class=\"nums\"  colspan=11 nowrap style=\"border: 1px solid gray;border-collapse: collapse;padding-top: 0.1cm; padding-bottom: 0.1cm;\">" ;
                    for (int i = 0; i < 10; i++) {
                        temp += "<td align=\"center\" style=\" weight:50px;height:50px;  bgcolor=white \">"
                                + "<img style=\"display:block; max-width:100%; max-height:100%;\" src=/auto/img/pk10/"
                                + Integer.parseInt(array[i]) + ".png></img></td>";
                        //                        if (Integer.parseInt(array[i]) == 1)
                        //                            temp += "<td align=\"center\" style=\" height:29px;font-size: 16px;font-weight:bold;border: 6px outset #c3c3c3; ;border-style:outset;background-color:#FFFF33\">"
                        //                            		+ "<img src=/auto/pk10/" +Integer.parseInt(array[i])+  ".png></img></td>";
                        //                        if (Integer.parseInt(array[i]) == 2)
                        //                            temp += "<td align=\"center\" style=\" height:29px;font-size: 16px;font-weight:bold;border: 6px outset #c3c3c3; ;border-style:outset;background-color:#0066FF\">"
                        //                            		+ "<font color=\"white\" style=\" text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;\">"
                        //
                        //                            		+ Integer.parseInt(array[i]) + "</font></td>";
                        //                        if (Integer.parseInt(array[i]) == 3)
                        //                            temp += "<td align=\"center\" style=\" height:29px;font-size: 16px;font-weight:bold;border: 6px outset #c3c3c3;;border-style:outset;background-color:#696969\">"
                        //                            		+ "<font color=\"white\" style=\" text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;\">"
                        //
                        //                            		+ Integer.parseInt(array[i]) + "</font></td>";
                        //                        if (Integer.parseInt(array[i]) == 4)
                        //                            temp += "<td align=\"center\" style=\" height:29px;font-size: 16px;font-weight:bold;border: 6px outset #c3c3c3;;border-style:outset;background-color:#FF5511\">"
                        //                            		+ "<font color=\"white\" style=\" text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;\">"
                        //
                        //                            		+ Integer.parseInt(array[i]) + "</font></td>";
                        //                        if (Integer.parseInt(array[i]) == 5)
                        //                            temp += "<td align=\"center\" style=\" height:29px;font-size: 16px;font-weight:bold;border: 6px outset #c3c3c3; ;border-style:outset;background-color:#00FFFF\">"
                        //                            		+ "<font color=\"white\" style=\" text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;\">"
                        //
                        //                            		+ Integer.parseInt(array[i]) + "</font></td>";
                        //                        if (Integer.parseInt(array[i]) == 6)
                        //                            temp += "<td align=\"center\" style=\" height:29px;font-size: 16px;font-weight:bold;border: 6px outset #c3c3c3; ;border-style:outset;background-color:#0000CC\">"
                        //                            		+ "<font color=\"white\" style=\" text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;\">"
                        //
                        //                            		+ Integer.parseInt(array[i]) + "</font></td>";
                        //                        if (Integer.parseInt(array[i]) == 7)
                        //                            temp += "<td align=\"center\" style=\" height:29px;font-size: 16px;font-weight:bold;border: 6px outset #c3c3c3; ;border-style:outset;background-color:#DCDCDC\">"
                        //                            		+ "<font color=\"white\" style=\" text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;\">"
                        //
                        //                            		+ Integer.parseInt(array[i]) + "</font></td>";
                        //                        if (Integer.parseInt(array[i]) == 8)
                        //                            temp += "<td align=\"center\" style=\" height:29px;font-size: 16px;font-weight:bold;border: 6px outset #c3c3c3; ;border-style:outset;background-color:#FF0000\">"
                        //                            		+ "<font color=\"white\" style=\" text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;\">"
                        //
                        //                            		+ Integer.parseInt(array[i]) + "</font></td>";
                        //                        if (Integer.parseInt(array[i]) == 9)
                        //                            temp += "<td align=\"center\" style=\" height:29px;font-size: 16px;font-weight:bold;border: 6px outset #c3c3c3; ;border-style:outset;background-color:#8B0000\">"
                        //                            		+ "<font color=\"white\" style=\" text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;\">"
                        //
                        //                            		+ Integer.parseInt(array[i]) + "</font></td>";
                        //                        if (Integer.parseInt(array[i]) == 10)
                        //                        	temp += "<td align=\"center\" style=\" height:29px;font-size: 16px;font-weight:bold;border: 6px outset #c3c3c3; ;border-style:outset;background-color:#32CD32\">"
                        //                            		+ "<font color=\"white\" style=\" text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;\">"
                        //
                        //                        			+ Integer.parseInt(array[i]) + "</font></td>";

                    }

                    // temp += "</td>" ;
                    temp += "</tr>";

                    logHtml.insert(0, temp);
                }
                String title = "<tr><td align=center nowrap style=\"border: 1px solid black\"><font size=\"3\">開獎期別</font></td>"
                               + "<td align=center nowrap style=\"border: 1px solid black\"><font size=\"3\">第一名</font></td>"
                               + "<td align=center nowrap style=\"border: 1px solid black\"><font size=\"3\">第二名</font></td>"
                               + "<td align=center  nowrap style=\"border: 1px solid black\"><font size=\"3\">第三名</font></td>"
                               + "<td align=center nowrap style=\"border: 1px solid black\"><font size=\"3\">第四名</font></td>"
                               + "<td align=center  nowrap style=\"border: 1px solid black\"><font size=\"3\">第五名</font></td>"
                               + "<td align=center nowrap style=\"border: 1px solid black\"><font size=\"3\">第六名</font></td>"
                               + "<td align=center nowrap style=\"border: 1px solid black\"><font size=\"3\">第七名</font></td>"
                               + "<td align=center  nowrap style=\"border: 1px solid black\"><font size=\"3\">第八名</font></td>"
                               + "<td align=center  nowrap style=\"border: 1px solid black\"><font size=\"3\">第九名</font></td>"
                               + "<td align=center  nowrap style=\"border: 1px solid black\"><font size=\"3\">第十名</font></td>"
                               + "</tr>";

                j.addProperty("logHtml",
                              "<table class=\"lot-table\" style=\"width:100%;border: 1px solid gray;border-collapse: collapse;\">"
                                         + title + logHtml + "</table>");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                try {
                    fileIn.close();
                } catch (Exception ex) {
                }
            }

            return j.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "null";
    }

    public String getRate(String latter) {
        if (latter.equals("A"))
            return "9.918";
        else if (latter.equals("B"))
            return "9.818";
        else if (latter.equals("C"))
            return "9.718";
        else
            return "9.618";
    }

    public String getBSRate(String latter) {
        if (latter.equals("A"))
            return "1.985";
        else if (latter.equals("B"))
            return "1.965";
        else if (latter.equals("C"))
            return "1.945";
        else
            return "1.925";
    }

    public String getBSNAME(String b) {

        String betstr = b.substring(1, b.length());
        if (betstr.equals("OUo1"))
            return "大";
        else if (betstr.equals("OUu1"))
            return "小";
        else if (betstr.equals("SCs1"))
            return "單號";
        else
            return "雙號";
    }

    public String getRandom() {

        int r = (int) (Math.random() * 998);
        return fillZero(Integer.toString(r));
    }

    public String convertUid(String uid) {
        String u1 = uid.substring(0, 16);
        String u2 = uid.substring(17, 33);

        String encode = u1 + URLEncoder.encode("|") + u2;
        return encode;
    }

    public String fillZero(String str) {
        if (str.length() == 1)
            return "0000" + str;
        else if (str.length() == 2)
            return "000" + str;
        else if (str.length() == 3)
            return "00" + str;
        else if (str.length() == 4)
            return "0" + str;
        else
            return str;
    }

    public String covertIntToLatter(int i) {
        if (i == 1)
            return "a";
        if (i == 2)
            return "b";
        if (i == 3)
            return "c";
        if (i == 4)
            return "d";
        if (i == 5)
            return "e";
        if (i == 6)
            return "f";
        if (i == 7)
            return "g";
        if (i == 8)
            return "h";
        if (i == 9)
            return "i";
        if (i == 10)
            return "j";
        if (i == 12)
            return "z";

        return "";
    }

    @RequestMapping("/getBETTIME")
    public String getBETTIME() {

        String url = "http://www.wydesy.net/pk10/getData";

        try {
            // url += URLEncoder.encode(prameter, "UTF-8");

            HttpGet httpget = new HttpGet(url);

            // 建立HttpPost对象
            HttpResponse response = new DefaultHttpClient().execute(httpget);
            // 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数
            if (response.getStatusLine().getStatusCode() == 200) {// 如果状态码为200,就是正常返回
                String ret = EntityUtils.toString(response.getEntity());
                JsonParser parser = new JsonParser();
                JsonObject o = parser.parse(ret).getAsJsonObject();
                JsonObject r = o.getAsJsonObject("next");

                String nextTime = r.get("awardTime").toString().substring(1,
                                                                          r.get("awardTime").toString().length() - 1);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 2017-10-22
                                                                                         // 23:27:00
                Date dateStr = formatter.parse(nextTime);

                Date n = new Date();
                long diff = dateStr.getTime() - n.getTime();// as given

                long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

                return Long.toString(seconds);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }

        return "";
    }

    @RequestMapping("/getMin")
    public int getMin() {
        return LocalDateTime.now().getMinute();
    }

    @RequestMapping("/saveLIMITDATE")
    public String saveLIMITDATE(@RequestParam("user") String user, @RequestParam("date") String date,
                                @RequestParam("pwd") String pwd, @RequestParam("pwd_in") String pwd_in,
                                @RequestParam("memo") String memo, @RequestParam("memo2") String memo2,
                                @RequestParam("memo3") String memo3, @RequestParam("key") String key,
                                @RequestParam("boss") String boss, @RequestParam("board") String board) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;

        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/limit.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));
            if (!key.equals("") && configProperty.containsKey(key))
                configProperty.remove(key);

            String v = configProperty.getProperty(user);
            String d = "";
            String sysDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
            if (v != null) {
                String[] a = v.split(",");
                if (a.length == 9) {
                    d = a[2];
                } else
                    d = sysDate;
            } else {
                d = sysDate;
            }

            configProperty.setProperty(user,
                                       date + "," + pwd + "," + d + "," + pwd_in + "," + memo + "," + memo2 + ","
                                             + memo3 + "," + boss + "," + board);

            fileOut = new FileOutputStream(file);
            configProperty.store(new OutputStreamWriter(fileOut, "UTF-8"), "sample properties");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }

        return "null";
    }

    @RequestMapping("/loadLimitDate")
    public String loadLimitDate(@RequestParam("boss") String boss) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;

        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/limit.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));
            StringBuilder html = new StringBuilder();

            Map<String, String> map = new HashMap<String, String>();

            for (Enumeration e = configProperty.propertyNames(); e.hasMoreElements();) {
                String key = e.nextElement().toString();
                String v = configProperty.getProperty(key);

                String array[] = v.split(",");
                System.out.println(v);
                if (array.length == 9) {
                    String b = array[7];
                    if (!b.equals(boss)) {
                        continue;
                    }

                    String limitDate = array[0].substring(0, 4) + "/" + array[0].substring(4, 6) + "/"
                                       + array[0].substring(6, 8);

                    String startDate = array[2].substring(0, 4) + "/" + array[2].substring(4, 6) + "/"
                                       + array[2].substring(6, 8);

                    String boardName = array[8].equals("0") ? "極速系統" :
                                       array[8].equals("1") ? "華山系統":
                                       array[8].equals("4") ? "福財神系統":
                                       array[8].equals("5") ? "新大立系統":
                                       array[8].equals("2") ? "大立系統": "利盈系統" ;
                    String temp = "<tr><td  align=\"center\"  style=\"font-size: 24px;font-weight:bold;border: 1px solid black;\"> "
                                  + boardName + "</td>"; //帳號

                    temp += "<td align=\"center\" class=\"context-menu-one\" style=\"font-size: 24px;font-weight:bold;border: 1px solid black;\">" //姓名
                            + key + "</td>";

                    temp += "<td align=\"center\" style=\"font-size: 24px;font-weight:bold;border: 1px solid black;\">" //姓名
                            + array[6] + "</td>";

                    temp += "<td align=\"center\" style=\"font-size: 24px;font-weight:bold;border: 1px solid black;\">" //使用期限
                            + limitDate + "</td>";
                    temp += "<td align=\"center\" style=\"font-size: 24px;font-weight:bold;border: 1px solid black;\">"
                            + array[1] + "</td>";
                    //temp += "<td align=\"center\" style=\"font-size: 24px;font-weight:bold;border: 1px solid black;\">" //初次使用時間
                    //		+ startDate + "</td>";
                    temp += "<td align=\"center\" style=\"font-size: 24px;font-weight:bold;border: 1px solid black;\">"
                            + array[3] + "</td>";
                    temp += "<td align=\"center\" style=\"font-size: 24px;font-weight:bold;border: 1px solid black;\">"
                            + array[4] + "</td>";
                    temp += "<td align=\"center\" style=\"font-size: 24px;font-weight:bold;border: 1px solid black;\">"
                            + array[5] + "</td>";
                    temp += "<td style=\"display:none;\">" + array[7] + "</td>";
                    temp += "</tr>";

                    map.put(key, temp);

                }

                //html.insert(0, temp);
            }

            Map<String, String> treeMap = new TreeMap<String, String>(map);

            int m_size = treeMap.size();
            for (String str : treeMap.keySet()) {
                int id = m_size;
                String v = treeMap.get(str).toString();

                String bgcolor = "CCEEFF";
                if (id % 2 == 1) {
                    bgcolor = "";

                }
                html.append("<tr  bgcolor=" + bgcolor + " >"
                            + treeMap.get(str).toString().substring(4, treeMap.get(str).toString().length() - 5)
                            + "<td style=\"padding-left:4px;padding-right:4px;font-size: 24px;font-weight:bold;border: 1px solid black;\" nowrap align=right>"
                            + id + "</td>" + "/<tr>");
                m_size--;
            }
            JsonObject j = new JsonObject();
            String returnhtml = "<tr>" + "<td width=\"200px\" align=center style=\"border: 1px solid black\">會員板</td>"
                                + "<td width=\"200px\" align=center style=\"border: 1px solid black\">帳號</td>"
                                + "<td width=\"200px\" align=center style=\"border: 1px solid black\">姓名</td>"
                                + "<td width=\"200px\" align=center style=\"border: 1px solid black\">使用期限</td>"
                                + "<td width=\"200px\"  align=center style=\"border: 1px solid black\">系統密碼</td>"
                                //+ "<td width=\"200px\"  align=center style=\"border: 1px solid black\">初次設定時間</td>"
                                + "<td width=\"200px\"  align=center style=\"border: 1px solid black\">極速密碼</td>"
                                + "<td width=\"200px\"  align=center style=\"border: 1px solid black\">遠端id</td>"
                                + "<td width=\"200px\"  align=center style=\"border: 1px solid black\">遠端密碼</td>"
                                + "<td width=\"20px\"  align=center style=\"border: 1px solid black\">ID</td>"
                                + html.toString();
            j.addProperty("returnhtml", returnhtml);
            j.addProperty("count", m_size);

            return j.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }

        return "null";
    }

    @RequestMapping("/deleteID")
    public String deleteID(@RequestParam("id") String id) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;

        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/limit.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));

            configProperty.remove(id);
            fileOut = new FileOutputStream(file);
            configProperty.store(new OutputStreamWriter(fileOut, "UTF-8"), "sample properties");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }

        return "null";
    }

    @RequestMapping("/loadID")
    public String loadID(@RequestParam("id") String id) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;

        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/limit.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));
            String v = configProperty.getProperty(id);
            return v;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }

        return "null";
    }

    @RequestMapping("/getAuthInformation")
    public String getAuthInformation(@RequestParam("user") String u, @RequestParam("pwd") String p) {
        String url = "http://www.sd8888.net:9999/checkLimitDate?user=" + u + "&pwd=" + p + "";
        String r = "";
        try {
            r = Utils.httpClientGet(url);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return r;

    }

    @RequestMapping("/checkLimitDate")
    public String checkLimitDate(@RequestParam("user") String u, @RequestParam("pwd") String p) {

        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;
        JsonObject j = new JsonObject();
        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/limit.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));
            if (configProperty.getProperty(u) == null) {
                j.addProperty("OK", "N");
                return j.toString();
            }
            String authString = configProperty.getProperty(u);
            String date = authString.split(",")[0];
            String sysPwd = authString.split(",")[1];
            String startDate = authString.split(",")[2];
            String pwd_in = authString.split(",")[3];

            String sysDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

            if (Integer.parseInt(date) >= Integer.parseInt(sysDate) && (p.equals(sysPwd) || p.equals(pwd_in))) {
                j.addProperty("OK", "Y");
                j.addProperty("pwd_in", pwd_in);
                j.addProperty("startDate", startDate);
                j.addProperty("end_date", date);

            } else {
                j.addProperty("OK", "N");
            }

            return j.toString();
        } catch (Exception e) {
            e.printStackTrace();
            j.addProperty("OK", "N");
            return j.toString();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }
    }

    @RequestMapping("/getLimitDate")
    public String getLimitDate(@RequestParam("user") String user) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;

        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/limit.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));
            return configProperty.getProperty(user).toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }

        return "loading...";
    }

    @RequestMapping("/deleteAll")
    public String deleteAll(@RequestParam("user") String u) {

        try {

            clearLog(user + "bet");
            clearLog(user + "overLOGDIS");
            clearLog(user + "_over");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "null";
    }

    @RequestMapping("/deleteHistory")
    public String deleteHistory(@RequestParam("user") String u) {

        try {

            String path = System.getProperty("user.dir");
            String hisFile = path + "/history.properties";
            File file = new File(hisFile);
            // System.out.println(hisFile);
            // System.out.println(file.exists());

            if (file.exists()) {
                file.delete();
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                                   + "deleteHistory delete suc");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return "null";
    }

    @RequestMapping("/setForce")
    public String setForce(@RequestParam("filename") String filename, @RequestParam("force") String force) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;

        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/" + filename + "force.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));
            configProperty.setProperty("force", force);

            fileOut = new FileOutputStream(file);
            configProperty.store(new OutputStreamWriter(fileOut, "UTF-8"), "sample properties");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }

        return "null";
    }

    @RequestMapping("/getForce")
    public String getForce(@RequestParam("filename") String filename) {
        FileInputStream fileIn = null;
        FileOutputStream fileOut = null;

        try {
            Properties configProperty = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            String path = System.getProperty("user.dir");
            String hisFile = path + "/" + filename + "force.properties";
            File file = new File(hisFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, "UTF-8"));
            String force = configProperty.getProperty("force");
            return force;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fileIn.close();
                fileOut.close();
            } catch (Exception ex) {
            }
        }

        return "1";
    }

    //String mountain_url = "http://w1.5a1234.com";
    String mountain_url[] = { "http://w1.5a1234.com", "http://w2.5a1234.com", "http://w3.5a1234.com",
                              "http://w4.5a1234.com" };
    int mountain_index = 0;

    String mountain_php_cookid = "";
    String mountain_token_sessid = "";
    String token = "";

    //leein
    String leein_php_cookid = "2a29530a2306=b00b0a238f1bb76547c75c442ce5bc273859ad7904b7bc3e;";
    String leein_url[] = { "https://0164955479-sy.cp168.ws", "https://1211067433-sy.cp168.ws",
                           "https://5461888297-sy.cp168.ws", "https://5184923658-sy.cp168.ws" };
    int leein_index = 0;
    
    //futsai
    String futsai_php_cookid = "2a29530a2306=b00b0a238f1bb76547c75c442ce5bc273859ad7904b7bc3e;";
    String futsai_url[] = { "https://1329036172-fcs.cp168.ws", "https://9402075398-fcs.cp168.ws",
                           "https://3390418965-fcs.cp168.ws", "https://0736268923-fcs.cp168.ws" };
    int futsai_index = 0;

    @RequestMapping("/imgcode")
    public @ResponseBody byte[] getImage(@RequestParam("_") String force) throws IOException {
        try {
            String im = "";
            CloseableHttpResponse httpresponse = null;

            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpGet HttpGet = new HttpGet(mountain_url[mountain_index % 4]);

            httpresponse = httpclient.execute(HttpGet);
            mountain_php_cookid = MoutainHttpClient.setCookie(httpresponse);
            System.out.println(mountain_php_cookid);

            HttpGet get2 = new HttpGet(mountain_url[mountain_index % 4] + "/imgcode.php");

            get2.setHeader("Cookie", mountain_php_cookid);

            httpresponse = httpclient.execute(get2);

            return IOUtils.toByteArray(httpresponse.getEntity().getContent());
        } catch (Exception e) {
            mountain_index++;
        }
        return null;
    }

    public String getPhpCookie() {
        try {

            CloseableHttpResponse httpresponse = null;

            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpGet HttpGet = new HttpGet(mountain_url[mountain_index % 4]);

            httpresponse = httpclient.execute(HttpGet);
            mountain_php_cookid = MoutainHttpClient.setCookie(httpresponse);
            return mountain_php_cookid;
        } catch (Exception e) {
            mountain_index++;
        }
        return null;
    }

    public String mountaionRecoup(@RequestParam("user") String user, @RequestParam("sn") String sn,
                                  @RequestParam("amount") String amount, @RequestParam("betphase") String betphase,
                                  @RequestParam("c") String c, @RequestParam("codeList") String codeList,
                                  @RequestParam("formu") String formu) {
        try {
            String code[] = codeList.split(",");
            JsonParser pr = new JsonParser();
            String r = MoutainHttpClient.httpPostBet(mountain_url[mountain_index % 4] + "/?m=bet",
                                                     mountain_token_sessid,
                                                     betphase,
                                                     amount,
                                                     sn,
                                                     code);
            JsonObject po = pr.parse(r).getAsJsonObject();
            String s = po.get("msg").getAsString();
            if (s.equals("投注成功")) {
                for (String str : code) {
                    String overLog = betphase + "@" + sn + "@" + str + "@" + formu;
                    saveOverLog(user, overLog, c);
                }

                String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "投注點數("
                                + amount + ")" + "(成功)" + "(公式" + formu + ")";
                saveLog(user + "bet", betlog);
            } else {
                String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "投注點數("
                                + amount + ")" + "(失敗)" + "(公式" + formu + ")";
                // saveLog(user + "bet", betlog);
                saveLog(user + "error", s.toString() + " recoup error:" + betlog);
                return mountaionRecoup2(user, sn, amount, betphase, c, codeList, formu);
            }
        } catch (Exception e) {
            saveLog(user + "error",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " : mountaionRecoup 斷");
            //h = httpClientCookie.getInstance(user, pwd);
            e.printStackTrace();
            return "error";
        }
        return "";
    }

    public String mountaionRecoup2(@RequestParam("user") String user, @RequestParam("sn") String sn,
                                   @RequestParam("amount") String amount, @RequestParam("betphase") String betphase,
                                   @RequestParam("c") String c, @RequestParam("codeList") String codeList,
                                   @RequestParam("formu") String formu) {
        try {
            String code[] = codeList.split(",");
            JsonParser pr = new JsonParser();
            String r = MoutainHttpClient.httpPostBet(mountain_url[mountain_index % 4] + "/?m=bet",
                                                     mountain_token_sessid,
                                                     betphase,
                                                     amount,
                                                     sn,
                                                     code);
            JsonObject po = pr.parse(r).getAsJsonObject();
            String s = po.get("msg").getAsString();
            if (s.equals("投注成功")) {
                for (String str : code) {
                    String overLog = betphase + "@" + sn + "@" + str + "@" + formu;
                    saveOverLog(user, overLog, c);
                }

                String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "投注點數("
                                + amount + ")" + "(成功)" + "(公式" + formu + ")";
                saveLog(user + "bet", betlog);
            } else {
                String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "投注點數("
                                + amount + ")" + "(失敗)" + "(公式" + formu + ")";
                // saveLog(user + "bet", betlog);
                saveLog(user + "error", s.toString() + " recoup2 error:" + betlog);
                return mountaionRecoup3(user, sn, amount, betphase, c, codeList, formu);
            }
        } catch (Exception e) {
            saveLog(user + "error",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " : mountaionRecoup2 斷");
            //h = httpClientCookie.getInstance(user, pwd);
            e.printStackTrace();
            return "error";
        }
        return "";
    }

    public String mountaionRecoup3(@RequestParam("user") String user, @RequestParam("sn") String sn,
                                   @RequestParam("amount") String amount, @RequestParam("betphase") String betphase,
                                   @RequestParam("c") String c, @RequestParam("codeList") String codeList,
                                   @RequestParam("formu") String formu) {
        try {
            String code[] = codeList.split(",");
            JsonParser pr = new JsonParser();
            String r = MoutainHttpClient.httpPostBet(mountain_url[mountain_index % 4] + "/?m=bet",
                                                     mountain_token_sessid,
                                                     betphase,
                                                     amount,
                                                     sn,
                                                     code);
            JsonObject po = pr.parse(r).getAsJsonObject();
            String s = po.get("msg").getAsString();
            if (s.equals("投注成功")) {
                for (String str : code) {
                    String overLog = betphase + "@" + sn + "@" + str + "@" + formu;
                    saveOverLog(user, overLog, c);
                }

                String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "投注點數("
                                + amount + ")" + "(成功)" + "(公式" + formu + ")";
                saveLog(user + "bet", betlog);
            } else {
                String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "投注點數("
                                + amount + ")" + "(失敗)" + "(公式" + formu + ")";
                // saveLog(user + "bet", betlog);
                saveLog(user + "error", s.toString() + " recoup3 error:" + betlog);
                return "error";
            }
        } catch (Exception e) {
            saveLog(user + "error",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " : mountaionRecoup3 斷");
            //h = httpClientCookie.getInstance(user, pwd);
            e.printStackTrace();
            return "error";
        }
        return "";
    }

}
