package io.nuite.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfiguration {
	

	@Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                //.sslSocketFactory(sslSocketFactory(), x509TrustManager())
                .retryOnConnectionFailure(true) //是否开启缓存
                .connectionPool(pool()) //连接池
                .connectTimeout(5 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(5 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(5 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
                .build();
    }

    

    @Bean
    public X509TrustManager x509TrustManager() {  //https协议通过X509TrustManager接口实现自己创建的证书
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            //信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create a new connection pool with tuning parameters appropriate for a single-user application.
     * The tuning parameters in this pool are subject to change in future OkHttp releases. Currently
     */
    @Bean
    public ConnectionPool pool() {
        /**
         * maxIdleConnections 连接池中整体的空闲连接的最大数量
         * keepAliveDuration 连接空闲时间最多为五分钟
         */
        return new ConnectionPool(200, 5, TimeUnit.MINUTES);
    }
}