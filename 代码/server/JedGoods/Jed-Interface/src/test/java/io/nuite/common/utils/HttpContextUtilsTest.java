package io.nuite.common.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: yangchuang
 * @Date: 2018/8/10 8:40
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class HttpContextUtilsTest {

    @Test
    public void getHttpServletRequest() {
        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        String requestURI = httpServletRequest.getRequestURI();
        String servletPath = httpServletRequest.getServletPath();
        String contextPath = httpServletRequest.getContextPath();
        System.out.println("contextPath = " + contextPath);
        System.out.println("servletPath = " + servletPath);
        System.out.println("requestURI = " + requestURI);
        System.out.println("requestURL = " + requestURL);
        System.out.println("httpServletRequest = " + httpServletRequest);

    }

    @Test
    public void getDomain() {
        String domain = HttpContextUtils.getDomain();
        System.out.println("domain = " + domain);
    }

    @Test
    public void getOrigin() {

        String origin = HttpContextUtils.getOrigin();
        System.out.println("origin = " + origin);
    }
}