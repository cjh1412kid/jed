package io;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import io.nuite.common.utils.SpringContextUtils;
import io.nuite.datasources.DynamicDataSourceConfig;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextClosedEvent;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({DynamicDataSourceConfig.class})
public class GoodsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.listeners((ApplicationListener<?>) applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                SocketIOServer socketIOServer = SpringContextUtils.getBean("socketIOServer", SocketIOServer.class);
                try {
                    socketIOServer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return application.sources(GoodsApplication.class);
    }


    @Autowired
    protected ConfigUtils configUtils;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(configUtils.getWebsocketHost());
        config.setPort(configUtils.getWebsocketPort());
        SocketIOServer server = new SocketIOServer(config);
        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

}
