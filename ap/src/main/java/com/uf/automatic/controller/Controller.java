package com.uf.automatic.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uf.automatic.ap.OrderedProperties;
import com.uf.automatic.util.Utils;
import com.uf.automatic.util.httpClientCookie;

@RestController
public class Controller {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	int i = 0;
	int bi = 0;
	int over_i = 0;
	httpClientCookie h = null;
	String user = "";
	String pwd = "";
	String p_id = "";
	Map<Integer, String> normal = new TreeMap<Integer, String>();
	Map<Integer, String> bs = new TreeMap<Integer, String>();

	@RequestMapping("/getUid")
	public String getUid(@RequestParam("user") String u, @RequestParam("pwd") String p) {
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
           
            
            String IFOK =  o.get("OK").getAsString();
            if(IFOK.equals("Y")) {
                h = httpClientCookie.getInstance(user, pwd);
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
	public String getTodayWin(@RequestParam("user") String user) {

		try {
			if (h == null) {
				h = httpClientCookie.getInstance(user, pwd);
			}
			String ret = h.getoddsInfo();
			// 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数

			DecimalFormat df = new DecimalFormat("##.00");

			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(ret).getAsJsonObject();
			JsonObject data = o.getAsJsonObject("data");
			String todayWin = data.get("profit").getAsString();
			String usable_credit = data.get("usable_credit").getAsString();
			// String ltype = MemAry.get("ltype").toString();
			// String cash = MemAry.get("cash").toString();
			// String maxcredit =
			// MemAry.get("maxcredit").toString().substring(1,
			// MemAry.get("maxcredit").toString().length()-1);
			// String useBet = MemAry.get("useBet").toString() ;

			JsonObject j = new JsonObject();
			j.addProperty("todayWin", Double.parseDouble(df.format(Double.valueOf(todayWin))));
			j.addProperty("usable_credit", Double.parseDouble(df.format(Double.valueOf(usable_credit))));

			// j.addProperty("cash",
			// Double.parseDouble(df.format(Double.valueOf(cash))));
			// j.addProperty("maxcredit",
			// Double.parseDouble(df.format(Double.valueOf(maxcredit))));
			// j.addProperty("useBet",
			// Double.parseDouble(df.format(Double.valueOf(useBet))));

			// j.addProperty("ltype", ltype.substring(1, 2));

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

				j.addProperty("stoplose", stoplose);
				j.addProperty("stopwin", stopwin);
				j.addProperty("startstatus", startstatus);

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

				int i = 0;

				Map m = new HashMap();
				for (Enumeration e = configProperty.propertyNames(); e.hasMoreElements();) {

					String v = configProperty.getProperty(e.nextElement().toString());

					String formuStr = v.substring(v.length() - 5, v.length()); // (公式1)
					
					if(v.indexOf("第0關")>-1){  //下注0的不用顯示在log
						continue;
					}
					
					if (formuStr.equals("(公式1)")) {
						logHtml.insert(0,
								"<tr><td bgcolor=\"FFFF77\"  style=\"border: 1px solid black\">" + v + "</td></tr>");
					}
					if (formuStr.equals("(公式2)")) {
						logHtml.insert(0,
								"<tr><td bgcolor=\"66FF66\"  style=\"border: 1px solid black\">" + v + "</td></tr>");
					}
					if (formuStr.equals("(公式3)")) {
						logHtml.insert(0,
								"<tr><td bgcolor=\"FF8888\"  style=\"border: 1px solid black\">" + v + "</td></tr>");
					}
					if (formuStr.equals("(公式4)")) {
						logHtml.insert(0,
								"<tr><td bgcolor=\"5599FF\"  style=\"border: 1px solid black\">" + v + "</td></tr>");
					}
					if (formuStr.equals("(公式5)")) {
						logHtml.insert(0,
								"<tr><td bgcolor=\"DDDDDD\"  style=\"border: 1px solid black\">" + v + "</td></tr>");
					}
					
					if (formuStr.equals("(公式6)")) {
						logHtml.insert(0,
								"<tr><td bgcolor=\"FFB3FF\"  style=\"border: 1px solid black\">" + v + "</td></tr>");
					}

				}

				j.addProperty("logHtml", "<table style=\"border-collapse: collapse;\">" + logHtml + "</table>");

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
	public String getPhase() {
		try {

			String ret = h.getoddsInfo();
			// 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数

			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(ret).getAsJsonObject();
			JsonObject data = o.getAsJsonObject("data");
			Utils.producePl(normal, ret); // 產生倍率 for single
			Utils.producePl(bs, h.getoddsInfoForDouble()); // 產生倍率 for 大小單雙

			String drawIssue = data.get("nn").getAsString();
			p_id = data.get("p_id").getAsString();
			if (drawIssue != null && !drawIssue.equals("")) {
				return drawIssue;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

		}
		return "null";

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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "null";
	}

	@RequestMapping("/saveParam")
	public String saveParam(@RequestParam("user") String user, @RequestParam("type") String type,
			@RequestParam("betlist") String betlist, @RequestParam("betlist2") String betlist2,
			@RequestParam("betlist3") String betlist3, @RequestParam("betlist4") String betlist4,
			@RequestParam("betlist5") String betlist5,@RequestParam("betlist6") String betlist6, @RequestParam("stoplose") String stoplose,
			@RequestParam("stopwin") String stopwin, @RequestParam("startstatus") String startstatus) {
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
			configProperty.setProperty("stoplose", stoplose);
			configProperty.setProperty("stopwin", stopwin);
			configProperty.setProperty("startstatus", startstatus);
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
			@RequestParam("code") String code) {
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

			for (int x = 1; x < 7; x++) { // x → 公式幾
				for (int i = 0; i < 10; i++) {
					int sn = i + 1;

					String key = phase + "@" + sn + "@" + c[i] + "@" + x;
					if (configProperty.getProperty(key) != null) {
						if (overmp.get(user + key) == null) {
							overmp.put(user + key, "put");
							over_i++;
							// Utils.WritePropertiesFile(user+"overLOGDIS_log",
							// fillZero(Integer.toString(over_i)), "第"+phase +
							// "期，第" + sn + "名，號碼(" + code + ") 已過關!(第"+c+"關)");
							String t = new SimpleDateFormat("HH:mm:ss").format(new Date());
							Utils.WritePropertiesFile(user + "overLOGDIS_log", fillZero(Integer.toString(over_i)),
									"第" + phase + "期，第" + sn + "名，已過關!(第" + configProperty.getProperty(key) + "關)"
											+ "(公式" + x + ")");

							j.addProperty(covertIntToLatter(sn) + x, "Y");
						}

					}

				}

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
		}

		return "null";
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
				System.out.println("delete suc");
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

	// sn : 1~ 0 , code : 01~10
	@RequestMapping("/bet")
	public String bet(@RequestParam("user") String user, @RequestParam("sn") String sn,
			@RequestParam("amount") String amount, @RequestParam("betphase") String betphase,
			@RequestParam("c") String c, @RequestParam("codeList") String codeList,
			@RequestParam("formu") String formu) {

		try {
			if (h == null) {
				h = httpClientCookie.getInstance(user, pwd);
			}

			String r = h.getoddsInfo();
			// 发送GET,并返回一个HttpResponse对象，相对于POST，省去了添加NameValuePair数组作参数

			JsonParser pr = new JsonParser();
			JsonObject po = pr.parse(r).getAsJsonObject();
			JsonObject data = po.getAsJsonObject("data");
			Utils.producePl(normal, r); // 產生倍率 for single
			// //url += URLEncoder.encode(prameter, "UTF-8");
			//
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

			// if (ret.indexOf(user) > -1) {
			String code[] = codeList.split(",");
			String ossid = "";
			String pl = "";
			String i_index = "";
			String m = "";
			int i = 0;
			for (String str : code) {
			    String overLog = betphase + "@" + sn + "@" + str + "@" + formu;
				saveOverLog(user, overLog, c);
				//
				int index = computeIndex(sn, str);
				String id_pl = normal.get(index).toString(); // 15@1.963
				ossid += id_pl.split("@")[0] + ",";
				pl += id_pl.split("@")[1] + ",";
				i_index += i + ",";
				m += amount + ",";
				i++;
			}
			String betRet = h.normalBet(p_id, ossid, pl, i_index, m, "pk10_d1_10");

			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(betRet).getAsJsonObject();
			String resCode = o.get("success").getAsString();
			if (resCode.equals("200")) {

//				for (String str : code) {
//					String overLog = betphase + "@" + sn + "@" + str + "@" + formu;
//					saveOverLog(user, overLog, c);
//
//				}

				String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "下注金額("
						+ amount + ")" + "(成功)" + "(公式" + formu + ")";
				saveLog(user + "bet", betlog);

			} else {
				System.out.println(o.toString());
				String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "下注金額("
						+ amount + ")" + "(失敗)" + "(公式" + formu + ")";
				saveLog(user + "bet", betlog);
				//recoup(user, sn, amount, betphase, c, codeList, formu);
			}

			// String overLog = betphase + "@" + sn + "@" + code ;
			// saveOverLog(user,overLog,c);
			// saveOverLog(document.getElementById("user").value,encodeURI(overLog),c);
			// Utils.WritePropertiesFile(user+"bet",
			// fillZero(Integer.toString(bi)), "第"+phase + "期，第" + sn + "名，號碼("
			// + code + ")，金額(" + amount + ") @" + ret);
			// } else {
			// saveLog(user + "ERROR", ret);
			// }
			//
			// return ret;
			// }

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

		}

		return "";
	}

	public String recoup(@RequestParam("user") String user, @RequestParam("sn") String sn,
			@RequestParam("amount") String amount, @RequestParam("betphase") String betphase,
			@RequestParam("c") String c, @RequestParam("codeList") String codeList,
			@RequestParam("formu") String formu) {

		try {
			if (h == null) {
				h = httpClientCookie.getInstance(user, pwd);
			}

			// //url += URLEncoder.encode(prameter, "UTF-8");
			//
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

			// if (ret.indexOf(user) > -1) {
			String code[] = codeList.split(",");
			String ossid = "";
			String pl = "";
			String i_index = "";
			String m = "";
			int i = 0;
			for (String str : code) {
				// String overLog = betphase + "@" + sn + "@" + str;
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
			String betRet = h.normalBet(p_id, ossid, pl, i_index, m, "pk10_d1_10");

			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(betRet).getAsJsonObject();
			String resCode = o.get("success").getAsString();
			if (resCode.equals("200")) {

				for (String str : code) {
					String overLog = betphase + "@" + sn + "@" + str + "@" + formu;
					saveOverLog(user, overLog, c);

				}

				String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "下注金額("
						+ amount + ")" + "(補單成功)" + "(公式" + formu + ")";
				saveLog(user + "bet", betlog);

			} else {
				String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "下注金額("
						+ amount + ")" + "(補單失敗)" + "(公式" + formu + ")";
				saveLog(user + "bet", betlog);
				 
			}

			// String overLog = betphase + "@" + sn + "@" + code ;
			// saveOverLog(user,overLog,c);
			// saveOverLog(document.getElementById("user").value,encodeURI(overLog),c);
			// Utils.WritePropertiesFile(user+"bet",
			// fillZero(Integer.toString(bi)), "第"+phase + "期，第" + sn + "名，號碼("
			// + code + ")，金額(" + amount + ") @" + ret);
			// } else {
			// saveLog(user + "ERROR", ret);
			// }
			//
			// return ret;
			// }

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

		}

		return "";
	}

	@RequestMapping("/betBS")
	public String betBS(@RequestParam("user") String user, @RequestParam("sn") String sn,
			@RequestParam("amount") String amount, @RequestParam("betphase") String betphase,
			@RequestParam("type") String type, @RequestParam("c") String c, @RequestParam("codeList") String codeList) {
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

			String betlog = "第" + betphase + "期" + "，第" + sn + "名，號碼(" + codeList + ")" + "，第" + c + "關" + "下注金額("
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

					String temp = "<tr><td style=\\\"border: 1px solid black\\\"> " + key + "</td>";
					for (int i = 0; i < 10; i++) {
						if (Integer.parseInt(array[i]) == 1)
							temp += "<td align=\"center\" style=\"font-size: 16px;font-weight:bold;border: 1px solid black;background-color:#FFFF00\">"
									+ Integer.parseInt(array[i]) + "</td>";
						if (Integer.parseInt(array[i]) == 2)
							temp += "<td align=\"center\" style=\"font-size: 16px;font-weight:bold;border: 1px solid black;background-color:#ADD8E6\">"
									+ Integer.parseInt(array[i]) + "</td>";
						if (Integer.parseInt(array[i]) == 3)
							temp += "<td align=\"center\" style=\"font-size: 16px;font-weight:bold;border: 1px solid black;background-color:#F0FFFF\">"
									+ Integer.parseInt(array[i]) + "</td>";
						if (Integer.parseInt(array[i]) == 4)
							temp += "<td align=\"center\" style=\"font-size: 16px;font-weight:bold;border: 1px solid black;background-color:#D2691E\">"
									+ Integer.parseInt(array[i]) + "</td>";
						if (Integer.parseInt(array[i]) == 5)
							temp += "<td align=\"center\" style=\"font-size: 16px;font-weight:bold;border: 1px solid black;background-color:#00FFFF\">"
									+ Integer.parseInt(array[i]) + "</td>";
						if (Integer.parseInt(array[i]) == 6)
							temp += "<td align=\"center\" style=\"font-size: 16px;font-weight:bold;border: 1px solid black;background-color:#8A2BE2\">"
									+ Integer.parseInt(array[i]) + "</td>";
						if (Integer.parseInt(array[i]) == 7)
							temp += "<td align=\"center\" style=\"font-size: 16px;font-weight:bold;border: 1px solid black;background-color:#FFF8DC\">"
									+ Integer.parseInt(array[i]) + "</td>";
						if (Integer.parseInt(array[i]) == 8)
							temp += "<td align=\"center\" style=\"font-size: 16px;font-weight:bold;border: 1px solid black;background-color:#DC143C\">"
									+ Integer.parseInt(array[i]) + "</td>";
						if (Integer.parseInt(array[i]) == 9)
							temp += "<td align=\"center\" style=\"font-size: 16px;font-weight:bold;border: 1px solid black;background-color:#A52A2A\">"
									+ Integer.parseInt(array[i]) + "</td>";
						if (Integer.parseInt(array[i]) == 10)
							temp += "<td align=\"center\" style=\"font-size: 16px;font-weight:bold;border: 1px solid black;background-color:#7FFF00\">"
									+ Integer.parseInt(array[i]) + "</td>";

					}
					temp += "</tr>";

					logHtml.insert(0, temp);
				}

				String title = "<tr><td nowrap style=\"border: 1px solid black\">開獎期別</td><td nowrap style=\"border: 1px solid black\">第一名</td><td nowrap style=\"border: 1px solid black\">第二名</td><td  nowrap style=\"border: 1px solid black\">第三名</td>"
						+ "<td nowrap style=\"border: 1px solid black\">第四名</td><td  nowrap style=\"border: 1px solid black\">第五名</td><td  nowrap style=\"border: 1px solid black\">第六名</td>"
						+ "<td  nowrap style=\"border: 1px solid black\">第七名</td><td  nowrap style=\"border: 1px solid black\">第八名</td><td  nowrap style=\"border: 1px solid black\">第九名</td>"
						+ "<td  nowrap style=\"border: 1px solid black\">第十名</td>" + "</tr>";

				j.addProperty("logHtml", "<table style=\"border-collapse: collapse;\">" + title + logHtml + "</table>");

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
			@RequestParam("pwd") String pwd, @RequestParam("pwd_in") String pwd_in, @RequestParam("memo") String memo) {
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
			String v = configProperty.getProperty(user);
			String d = "";
			String sysDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
			if (v != null) {
				String[] a = v.split(",");
				if (a.length == 4) {
					d = a[2];
				} else
					d = sysDate;
			} else {
				d = sysDate;
			}

			configProperty.setProperty(user, date + "," + pwd + "," + d + "," + pwd_in+ "," + memo);

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
	public String loadLimitDate() {
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

				String limitDate = array[0].substring(0, 4) + "/" +
								   array[0].substring(4, 6)
								   + "/" +  array[0].substring(6, 8) ;
				
				String startDate = array[2].substring(0, 4) + "/" +
						   array[2].substring(4, 6)
						   + "/" +  array[2].substring(6, 8) ;
				
				String temp = "<tr  ><td  align=\"center\" class=\"context-menu-one\" style=\\\"border: 1px solid black\\\"> " + key + "</td>";
				temp += "<td align=\"center\" style=\"font-size: 20px;font-weight:bold;border: 1px solid black;\">"
						+ limitDate + "</td>";
				temp += "<td align=\"center\" style=\"font-size: 20px;font-weight:bold;border: 1px solid black;\">"
						+ array[1] + "</td>";
				temp += "<td align=\"center\" style=\"font-size: 20px;font-weight:bold;border: 1px solid black;\">"
						+ startDate + "</td>";
				temp += "<td align=\"center\" style=\"font-size: 20px;font-weight:bold;border: 1px solid black;\">"
						+ array[3] + "</td>";
				temp += "<td align=\"center\" style=\"font-size: 20px;font-weight:bold;border: 1px solid black;\">"
						+ array[4] + "</td>";
				temp += "</tr>";
				
				map.put(array[0] + key, temp);

				//html.insert(0, temp);
			}
			
			
			Map<String, String> treeMap = new TreeMap<String, String>(map);
			for (String str : treeMap.keySet()) {
			    
			    html.insert(0, treeMap.get(str).toString());
			}
			

			return "<tr><td width=\"200px\" align=center style=\"border: 1px solid black\">帳號</td><td width=\"200px\" align=center style=\"border: 1px solid black\">使用期限</td>"
					+ "<td width=\"200px\"  align=center style=\"border: 1px solid black\">系統密碼</td>"
					+ "<td width=\"200px\"  align=center style=\"border: 1px solid black\">初次設定時間</td>"
					+ "<td width=\"200px\"  align=center style=\"border: 1px solid black\">極速密碼</td>" 
					+ "<td width=\"200px\"  align=center style=\"border: 1px solid black\">備註</td>" + html.toString();
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
		String url = "http://220.132.126.216:9999/checkLimitDate?user=" + u + "&pwd=" + p + "";
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

}
