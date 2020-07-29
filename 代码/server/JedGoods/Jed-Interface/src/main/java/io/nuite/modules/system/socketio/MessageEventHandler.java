package io.nuite.modules.system.socketio;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

import io.nuite.common.utils.PermissionKeys;
import io.nuite.modules.system.service.ShiroService;

@Component
public class MessageEventHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public SocketIOServer socketIoServer;
	
    @Autowired
    private ShiroService shiroService;
    
	
	@OnConnect
	public void onConnect(SocketIOClient client) {
		String companySeq = client.getHandshakeData().getSingleUrlParam("companySeq");
		String userSeq = client.getHandshakeData().getSingleUrlParam("userSeq");
		
		logger.info("客户端:" + client.getSessionId() + "已连接, companySeq:" + companySeq + ", userSeq:" + userSeq);
		
		Set<String> permissions = shiroService.getUserPermissions(Long.parseLong(userSeq));
		if(permissions.contains(PermissionKeys.ORDER_RECEIVE)) {
			client.joinRoom(companySeq + "_" + PermissionKeys.ORDER_RECEIVE);
		}
		if(permissions.contains(PermissionKeys.ORDER_CHECK)) {
			client.joinRoom(companySeq + "_" + PermissionKeys.ORDER_CHECK);
		}
		if(permissions.contains(PermissionKeys.ORDER_STORE)) {
			client.joinRoom(companySeq + "_" + PermissionKeys.ORDER_STORE);
		}
		if(permissions.contains(PermissionKeys.ORDER_DELIVER)) {
			client.joinRoom(companySeq + "_" + PermissionKeys.ORDER_DELIVER);
		}
		if(permissions.contains(PermissionKeys.ORDER_CANCEL)) {
			client.joinRoom(companySeq + "_" + PermissionKeys.ORDER_CANCEL);
		}
		
		Set<String> rooms = client.getAllRooms();
		logger.info("客户端:" + client.getSessionId() + "成功加入房间rooms:" + rooms);
	}

	
	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		logger.info("客户端:" + client.getSessionId() + "断开连接");
	}


    @OnEvent(value = "message")
    public void onEvent(SocketIOClient client, AckRequest request, MessageInfo data) {
    	logger.info("客户端:" + client.getSessionId() + "发来消息：" + data.getMsgContent());
    }

    
    /**
     * 向客户端推送订单处理消息  （拥有指定权限的在线的工厂子账号）
     * @param companySeq  公司序号
     * @param permission  拥有的权限
     * @param message	消息内容
     * @param orderSeq	订单Seq
     * @param orderStatus	订单状态
     */
	public void sendHandleOrderEvent(String companySeq, String permission, String message, Integer orderSeq, Integer orderStatus) {
		String dateTime = new DateTime().toString("hh:mm:ss");
		Map<String, Object> extraMap = new HashMap<String, Object>();
		extraMap.put("orderSeq", orderSeq);
		extraMap.put("orderStatus", orderStatus);
		socketIoServer.getRoomOperations(companySeq + "_" + permission).sendEvent("message", dateTime, message, extraMap);
	}
	
}