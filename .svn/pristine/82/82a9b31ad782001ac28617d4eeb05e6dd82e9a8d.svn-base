package io.nuite.modules.order_platform_app.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.nuite.modules.sr_base.utils.ConfigUtils;
import net.sf.json.JSONObject;


/**
 * 亲加直播工具类
 * API 文档: http://www.gotye.com.cn/docs/live/restapi.html
 * 源码: 没有提供SDK，编写http请求调用
 * 例子源码: http://7xrwo2.dl1.z0.glb.clouddn.com/ApiCall.java
 * @author yy
 */
/*@Component
public class GotyeUtils {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigUtils configUtils;
	
	
	
	*//**
	 * 使用后台管理账号授权
	 * @return
	 *//*
	private String appAccessToken() {
		try {
			// 亲加对接参数
			String username = configUtils.getOrderPlatformApp().getGotyeUsername();
			String password = configUtils.getOrderPlatformApp().getGotyePassword();
			String apiUrl = configUtils.getOrderPlatformApp().getGotyeApiUrl();
			
			JSONObject param = new JSONObject();
			param.put("scope", "app");
			param.put("username", username);
			param.put("password", password);
			String result = GotyeHttpUtils.post(apiUrl + "/AccessToken", param.toString(), null);
			if(result != null) {
				JSONObject json = JSONObject.fromObject(result);
				if(json.getInt("status") == 200) {
					return json.getString("accessToken");
				} else {
					logger.error("亲加授权，授权失败:" + json);
					return null;
				}
			} else {
				logger.error("亲加授权，http请求失败");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("亲加授权，异常:" + e.getMessage(), e);
			return null;
		}
	}
	
	
	*//**
	 * 查询主播室列表
	 * @param creator 创建者，即工厂用户的seq
	 * @return
	 *//*
	public JSONObject getRooms(String creator) {
		try {
			// 亲加对接参数
			String apiUrl = configUtils.getOrderPlatformApp().getGotyeApiUrl();
			
			//获取授权
			String token = appAccessToken();
			if(token == null) {
				logger.error("亲加查询主播室列表，获取授权失败");
				return null;
			}
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", token);
			
			//根据创建人获取主播室信息
			JSONObject param = new JSONObject();
			param.put("creator", creator);
			param.put("type", 0);
			String result = GotyeHttpUtils.post(apiUrl + "/GetRooms", param.toString(), headers);
			
			if(result != null) {
				JSONObject json = JSONObject.fromObject(result);
				if(json.getInt("status") == 200 && json.get("entities") != null) {
					return json.getJSONArray("entities").getJSONObject(0);
				} else {
					logger.error("亲加查询主播室列表，失败，参数：" + param.toString() + "，返回：" + json);
					return null;
				}
			} else {
				logger.error("亲加查询主播室列表，http请求失败！");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("亲加查询主播室列表，异常:" + e.getMessage(), e);
			return null;
		}
	}
	
	
	
	*//**
	 * 新建主播室
	 * @param roomName 主播室名称
	 * @param creator 创建者，即工厂用户的seq
	 * @return
	 *//*
	public JSONObject createRoom(String roomName, String creator) {
		try {
			// 亲加对接参数
			String apiUrl = configUtils.getOrderPlatformApp().getGotyeApiUrl();
			
			//获取授权
			String token = appAccessToken();
			if(token == null) {
				logger.error("亲加新建主播室，获取授权失败");
				return null;
			}
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", token);
			
			//根据创建人获取主播室信息
			JSONObject param = new JSONObject();
			param.put("roomName", roomName);
			param.put("anchorPwd", new Random().nextInt(900000) + 100000 + "");
			param.put("assistPwd", new Random().nextInt(900000) + 100000 + "");
			param.put("userPwd", new Random().nextInt(900000) + 100000 + "");
			param.put("creator", creator);
			String result = GotyeHttpUtils.post(apiUrl + "/CreateRoom", param.toString(), headers);
			
			if(result != null) {
				JSONObject json = JSONObject.fromObject(result);
				if(json.getInt("status") == 200 && json.get("entity") != null) {
					return json.getJSONObject("entities");
				} else {
					logger.error("亲加新建主播室，失败，参数：" + param.toString() + "，返回：" + json);
					return null;
				}
			} else {
				logger.error("亲加新建主播室，http请求失败！");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("亲加新建主播室，异常:" + e.getMessage(), e);
			return null;
		}
	}
	

}*/
