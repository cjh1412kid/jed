package io.nuite.modules.system.socketio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;

@Component
@Order(value = 1)
public class MyCommandLineRunner implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SocketIOServer server;

	@Override
	public void run(String... args) throws Exception {
		server.start();
		logger.info("socket.io启动成功！");
	}
}