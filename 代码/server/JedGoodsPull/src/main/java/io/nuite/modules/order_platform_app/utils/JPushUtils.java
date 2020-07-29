package io.nuite.modules.order_platform_app.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 极光推送工具类
 * API 文档: https://docs.jiguang.cn/jpush/server/push/rest_api_v3_push/
 * 源码: https://github.com/jpush/jpush-api-java-client
 * 例子源码: https://github.com/jpush/jpush-api-java-client/tree/master/example/main/java/cn/jpush/api/examples
 * @author yy
 * 
 */
@Component
public class JPushUtils {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigUtils configUtils;

	
	/**
	 * 使用极光推送发送推送消息
	 * @param aliasList 要推送给的注册id的别名（目前使用用户账号作别名）
	 * @param alert alert类型的notification通知，在手机通知栏显示
	 * @param msgContent 自定义消息message内容，用来标识是哪种类型的通知，目前0~7表示订单，-1表示直播，-2表示调拨
	 */
	public void sendPush(List<String> aliasList, String alert, String msgContent) {
		try {
			// 极光对接参数
			String appKey = configUtils.getOrderPlatformApp().getjPushAppKey();
			String masterSecret = configUtils.getOrderPlatformApp().getjPushMasterSecret();
			boolean apnsProduction = configUtils.getOrderPlatformApp().isjPushApnsProduction();
			
			ClientConfig clientConfig = ClientConfig.getInstance();
			final JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
			final PushPayload payload = buildPushObject_all_alias_alert(aliasList, alert, msgContent, apnsProduction);
			try {
			    PushResult result = jpushClient.sendPush(payload);
			    logger.info("极光推送成功: " + result);
			} catch (APIConnectionException e) {
				logger.error("极光推送，发送编号" + payload.getSendno() + "，连接失败，需要稍后重试", e);
				//极光推送时默认重试3次，如果都失败，用线程5分钟后重新发送一次
				retrySendPush(jpushClient, payload);
			} catch (APIRequestException e) {
				logger.error("极光推送，发送编号" + payload.getSendno() + "，消息有误，应当重新查看并修改: " + payload, e);
			}
		} catch (Exception e) {
			logger.error("极光推送发送推送消息异常:" + e.getMessage(), e);
		}
    }
	
	
    private PushPayload buildPushObject_all_alias_alert(List<String> aliasList, String alert, String msgContent, boolean apnsProduction) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(aliasList))
//                .setNotification(Notification.alert(alert))
                .setMessage(Message.newBuilder()
                        .setMsgContent(alert)
                        .setContentType(msgContent)
                        .addExtra("type", msgContent)
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(apnsProduction)
                        .build())
                .build();
    }
    
	private void retrySendPush(JPushClient jpushClient, PushPayload payload) {
		final ScheduledThreadPoolExecutor scheduledThreadPool = new ScheduledThreadPoolExecutor(1);
		scheduledThreadPool.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					PushResult result = jpushClient.sendPush(payload);
					logger.info("极光推送重试，推送成功: " + result);
				} catch (APIConnectionException e) {
					logger.error("极光推送重试，发送编号" + payload.getSendno() + "，连接失败", e);
				} catch (APIRequestException e) {
					logger.error("极光推送重试，发送编号" + payload.getSendno() + "，消息有误，应当重新查看并修改: " + payload, e);
				}
			}
		}, 5, TimeUnit.MINUTES);

		// 关闭线程池
		scheduledThreadPool.shutdown();
	}
	
}

