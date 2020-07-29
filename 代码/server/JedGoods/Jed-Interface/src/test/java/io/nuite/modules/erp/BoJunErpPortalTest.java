package io.nuite.modules.erp;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.nuite.common.utils.OkHttpUtil;





/**
 * 伯俊Erp Portal接口测试类
 * @author yy
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BoJunErpPortalTest {

	//MD5加密方法
	public static String MD5(String s){
		String r="";
		try{
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(s.getBytes());
			byte b[]=md.digest();	
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) { 
				i = b[offset]; 
				if(i<0) i+= 256; 
				if(i<16) 
				buf.append("0"); 
				buf.append(Integer.toHexString(i)); 
			}
			r=buf.toString();
		}catch(Exception e){	
		}
		return r;
	}
	
	
	//@Test
	public void test() {

		/**必选参数**/
		// sip_appkey - 应用程序的编号
		String serverUrl = "http://47.104.230.122:90/servlets/binserv/Rest";
		String sip_appkey = "sjcx@jed.com";
		String appSecret = "123";
		// sip_timestamp - 服务请求时间戳(yyyy-mm-dd hh:mm:ss.xxx)，支持毫秒，若系统不能产生毫秒，必须补足内容，如使用.000。
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		sdf.setLenient(false);
		String sip_timestamp = sdf.format(new Date());
		
		// sip_sign - 签名，使用sip_appkey + sip_timestamp + appSecret进行MD5哈希运算，结果为32位长字符串，全部小写，服务器需要校验此值。
		String sip_sign = MD5(sip_appkey + sip_timestamp + MD5(appSecret));
		
		
		
		/** 业务参数 **/
//		业务参数将封装为JSONArray格式的文本
//		transactions -[transaction,...] //多个Transaction, 一个transaction里的多个操作将全部成功，或全部失败，每个Transaction对象的定义见下
		
//		transaction:{
//			id: <transaction-id> // 通过ID使得客户端能获取transaction的执行情况
//			command:"ObjectCreate"|"ObjectModify"|"ObjectDelete"|"ObjectSubmit"|"ExecuteWebAction"|"ProcessOrder"|"Query"|"Import",//Transaction的操作命令，详见命令章节
//			params:{ //操作命令的参数
//				<command-param>:<command-value>,
//				...
//			}
//		}	
		JSONArray transactionsArr = new JSONArray();



		JSONObject transactionJson = new JSONObject();
		transactionJson.put("id", "2ed23ed2wd2qed2");
		transactionJson.put("command", "Query");
		
		JSONObject commandParams = new JSONObject();  //操作命令的参数
		
//		table:"V_FA_V_STORAGE1",
//		columns:["M_PRODUCT_ID;NAME","M_PRODUCTALIAS_ID;NO","QTY","PRICELIST"],
//		params:{
//		 combine:"and",
//		 expr1:{
//						combine:"and",
//						expr1:{
//										column:"C_STORE_ID;C_CUSTOMER_ID;NAME",
//		         				condition:" IN ('BURGEON','四级经销仓')"
//		        },
//						expr2:{
//										column:"QTY",
//										condition:" > 0"
//						} 
//		 },
//		 expr2:{
//	        column:"QTYCAN",
//	        condition:" > 10"
//		  }
//		}
		
		commandParams.put("table", "V_FA_V_STORAGE1");
		String[] columns = {"M_PRODUCT_ID;NAME","M_PRODUCTALIAS_ID;NO","QTY","PRICELIST"};
		commandParams.put("columns", columns);
		JSONObject params = new JSONObject();
//		params.put("combine", "and");
		JSONObject expr1 = new JSONObject();
		params.put("column", "QTYCAN");
		params.put("condition", "> 10");
//		params.put("expr1", expr1);
		commandParams.put("params", params);
		
		
		transactionJson.put("params", commandParams);

		transactionsArr.add(transactionJson);
		
		
		// POST请求
		HashMap<String, String> httpParams = new HashMap<String, String>();
		httpParams.put("sip_appkey", sip_appkey);
		httpParams.put("sip_timestamp", sip_timestamp);
		httpParams.put("sip_sign", sip_sign);
		httpParams.put("transactions", transactionsArr.toString());
		System.out.println(httpParams.toString());
		
		String response = OkHttpUtil.post(serverUrl, httpParams);
		System.out.println(response);
	}
    
   
	
	
	@Test
	public void test2() {

		/**必选参数**/
		// sip_appkey - 应用程序的编号
		String serverUrl = "http://47.104.230.122:90/servlets/binserv/Rest";
		String sip_appkey = "sjcx@jed.com";
		String appSecret = "123";
		// sip_timestamp - 服务请求时间戳(yyyy-mm-dd hh:mm:ss.xxx)，支持毫秒，若系统不能产生毫秒，必须补足内容，如使用.000。
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		sdf.setLenient(false);
		String sip_timestamp = sdf.format(new Date());
		
		// sip_sign - 签名，使用sip_appkey + sip_timestamp + appSecret进行MD5哈希运算，结果为32位长字符串，全部小写，服务器需要校验此值。
		String sip_sign = MD5(sip_appkey + sip_timestamp + MD5(appSecret));
		
		
		
		/** 业务参数 **/
//		业务参数将封装为JSONArray格式的文本
//		transactions -[transaction,...] //多个Transaction, 一个transaction里的多个操作将全部成功，或全部失败，每个Transaction对象的定义见下
		
//		transaction:{
//			id: <transaction-id> // 通过ID使得客户端能获取transaction的执行情况
//			command:"ObjectCreate"|"ObjectModify"|"ObjectDelete"|"ObjectSubmit"|"ExecuteWebAction"|"ProcessOrder"|"Query"|"Import",//Transaction的操作命令，详见命令章节
//			params:{ //操作命令的参数
//				<command-param>:<command-value>,
//				...
//			}
//			orderby:{}
//		}	
		List<Map<String, Object>> transactionList = new ArrayList<Map<String, Object>>();


		Map<String, Object> transactionMap = new HashMap<String, Object>();
		transactionMap.put("id", "2ed23ed2wd2qed2");
		transactionMap.put("command", "Query");
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
//		table:"V_FA_V_STORAGE1",
//		columns:["M_PRODUCT_ID;NAME","M_PRODUCTALIAS_ID;NO","QTY","PRICELIST"],
//		where(params):{
//	        column:"QTYCAN",
//	        condition:" > 10"
//		}
//		orderby:{}
//		start :10
//		range :10
//		count :true
		
		paramsMap.put("table", "V_FA_V_STORAGE1");
		
		List<String> columns = new ArrayList<String>();
		columns.add("M_PRODUCT_ID;NAME");
		columns.add("M_PRODUCTALIAS_ID;NO");
		columns.add("QTY");
		columns.add("PRICELIST");
		paramsMap.put("columns", columns);
		
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("column", "QTYCAN");
		sqlWhereMap.put("condition", "> 10");
		paramsMap.put("params", sqlWhereMap);
		
		List<Map<String, Object>> orderbyList = new ArrayList<Map<String, Object>>();
		Map<String, Object> orderbyMap = new HashMap<String, Object>();
		orderbyMap.put("column", "QTY");
		orderbyMap.put("asc", false);
		orderbyList.add(orderbyMap);
		paramsMap.put("orderby", orderbyList);
		
		paramsMap.put("start", 0);
		paramsMap.put("range", 30);
		paramsMap.put("count", true);
		
		
		transactionMap.put("params", paramsMap);

		transactionList.add(transactionMap);
		
		
		// POST请求
		HashMap<String, String> httpParams = new HashMap<String, String>();
		httpParams.put("sip_appkey", sip_appkey);
		httpParams.put("sip_timestamp", sip_timestamp);
		httpParams.put("sip_sign", sip_sign);
		JSONArray array= JSONArray.parseArray(JSON.toJSONString(transactionList));
		
		httpParams.put("transactions", array.toJSONString());
		System.out.println(httpParams.toString());
		
		String response = OkHttpUtil.post(serverUrl, httpParams);
		System.out.println(response);
	}
	
}
