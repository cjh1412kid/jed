package io.nuite.modules.erp;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class AAAA {

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
	
	public static void main(String[] args) {

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
		
		
		new AAAA().restTemplatePost(serverUrl, httpParams);

	}

	
	//测试restTemplate使用
    public String query() {
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(
                    "https://www.baidu.com" ,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);
        } catch (RestClientException e) {
            throw e;
        }

        System.out.println(response);
        return response.getBody();
    }
    
    
    
    /**
     * 使用restTemplate发送post请求
     * @param url
     * @param params
     */
    public void restTemplatePost(String url, HashMap<String, String> params){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        for(String key : params.keySet()) {
        	map.add(key, params.get(key));
        }
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        System.out.println(response);
        System.out.println(response.getBody());
    }
    
    
   
}
