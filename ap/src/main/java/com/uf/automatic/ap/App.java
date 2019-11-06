package com.uf.automatic.ap;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uf.automatic.util.Utils;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ComponentScan({ "com.uf.automatic.controller", "com.uf.automatic.ap" })

public class App {
	public static void main(String[] args) {

		String path = System.getProperty("user.dir");
		String hisFile = path + "/history.properties";
		File file = new File(hisFile);
		if (file.exists()) {
			file.delete();
			System.out.println(file.exists());
		}

		//Utils.writeHistory();
		SpringApplication.run(App.class, args);
		
//		fullPhase();
//	    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//        Date date = new Date();
//        String first = dateFormat.format(date) + "001";
//        
		//run();
	}

	private static void run() {
		Timer timer = new Timer();
		NewTimerTask timerTask = new NewTimerTask();
		// 程序运行后立刻执行任务，每隔100ms执行一次
		timer.schedule(timerTask, 0, 30000);
	}
	
	
	public static void fullPhase() {
	    try {
            Date dNow = new Date();   //当前时间

            

            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(dNow);//把当前时间赋给日历
             
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
                //m.put(preDrawIssue, code);
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
            }
            
             
        }catch(Exception e) {
            e.printStackTrace();
        }
	}

}

class NewTimerTask extends TimerTask {

	@Override
	public void run() {
		Utils.writeHistory();
	}

}
