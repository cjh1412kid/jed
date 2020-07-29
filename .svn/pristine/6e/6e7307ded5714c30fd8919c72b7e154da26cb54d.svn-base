package io.nuite.modules.erp.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.nuite.common.utils.OkHttpUtil;
import net.sf.json.JsonConfig;



/**
 * 伯俊Erp Portal接口封装工具类
 * @author yy
 */
public class BoJunErpPortalUtil {
    private static Logger logger = LoggerFactory.getLogger(BoJunErpPortalUtil.class);
    
	//伯俊ERP Portal Rest接口地址、账号、密码
	private static final String URL = "http://47.104.230.122:90/servlets/binserv/Rest";
	private static final String APPKEY = "sjcx@jed.com";
	private static final String APPSECRET = "123";
	
	
	
	
	/**
	 * 查询接口
	 * @param table  表名
	 * @param columns   字段名，字段名后加";"的，表示查询外键所在表的对应字段
	 * @param whereParamMap   where后面的条件，需要自己拼接（比较复杂）
	 * @param orderBy   排序部分的sql，如"id DESC,time ASC"
	 * @param start    起始条数（从0开始）
	 * @param num	数量
	 * @return
	 */
	public static JSONObject querySql(String table, String columns, Map<String, Object> whereParamMap, String orderBy, Integer start, Integer num) {

		try {
			/**一、必选参数**/
			//1.sip_appkey: 应用程序的编号
			String serverUrl = URL;
			String sip_appkey = APPKEY;
			String appSecret = APPSECRET;
			//2.sip_timestamp: 服务请求时间戳(yyyy-mm-dd hh:mm:ss.xxx)，支持毫秒，若系统不能产生毫秒，必须补足内容，如使用.000。
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			sdf.setLenient(false);
			String sip_timestamp = sdf.format(new Date());
			
			//3.sip_sign: 签名，使用sip_appkey + sip_timestamp + appSecret进行MD5哈希运算，结果为32位长字符串，全部小写，服务器需要校验此值。
			String sip_sign = MD5(sip_appkey + sip_timestamp + MD5(appSecret));
			
			
			/** 二、业务参数 **/
//			业务参数将封装为JSONArray格式的文本
//			transactions -[transaction,...] //多个Transaction, 一个transaction里的多个操作将全部成功，或全部失败，每个Transaction对象的定义见下
			
//			transaction:{
//				id: <transaction-id> // 通过ID使得客户端能获取transaction的执行情况
//				command:"ObjectCreate"|"ObjectModify"|"ObjectDelete"|"ObjectSubmit"|"ExecuteWebAction"|"ProcessOrder"|"Query"|"Import",//Transaction的操作命令，详见命令章节
//				params:{ //操作命令的参数
//					<command-param>:<command-value>,
//					...
//				}
//				orderby:{}
//			}	
			List<Map<String, Object>> transactionList = new ArrayList<Map<String, Object>>();

			Map<String, Object> transactionMap = new HashMap<String, Object>();
			transactionMap.put("id", "1");
			transactionMap.put("command", "Query");
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
//			table:"V_FA_V_STORAGE1",
//			columns:["M_PRODUCT_ID;NAME","M_PRODUCTALIAS_ID;NO","QTY","PRICELIST"],
//			where(params):{
//		        column:"QTYCAN",
//		        condition:" > 10"
//			}
//			orderby:{}
//			start :10
//			range :10
//			count :true
			
			paramsMap.put("table", table);
			
			if(columns != null && !columns.equals("")) {
				paramsMap.put("columns", columns.split(","));
			}
			
//			Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
//			sqlWhereMap.put("column", "QTYCAN");
//			sqlWhereMap.put("condition", "> 10");
			if(whereParamMap != null) {
				paramsMap.put("params", whereParamMap);
			}
			
			if(orderBy != null && !orderBy.equals("")) {
				List<Map<String, Object>> orderbyList = new ArrayList<Map<String, Object>>();
				for(String orderbyStr : orderBy.split(",")) {
					Map<String, Object> orderbyMap = new HashMap<String, Object>();
					int index = orderbyStr.indexOf(" ");
					orderbyMap.put("column", orderbyStr.substring(0, index));
					if(orderbyStr.substring(index + 1, orderbyStr.length()).equals("DESC")) {
						orderbyMap.put("asc", false);
					} else {
						orderbyMap.put("asc", true);
					}
					orderbyList.add(orderbyMap);
				}
				paramsMap.put("orderby", orderbyList);
			}
			
			if(start != null) {
				paramsMap.put("start", start);
			}
			if(num != null) {
				paramsMap.put("range", num);
			}
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
			//logger.info("请求伯俊ErpRestPortal接口，参数：" + httpParams.toString());
			
			String response = OkHttpUtil.post(serverUrl, httpParams);
			//logger.info("请求伯俊ErpRestPortal接口，返回：" + response);
			
			JSONArray arr = JSONArray.parseArray(response);
			JSONObject json = arr.getJSONObject(0);
			if(json.getInteger("code") == 0) {
				return json;
			} else {
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return null;
		}
	}
    
	
	
	//MD5加密方法
	private static String MD5(String s){
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
	
	
	
}
