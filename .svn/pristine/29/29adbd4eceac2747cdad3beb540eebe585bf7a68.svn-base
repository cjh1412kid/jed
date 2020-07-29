package io.nuite.modules.order_platform_app.interceptor;

import io.jsonwebtoken.Claims;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.JwtUtils;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.system.service.ShiroService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 权限(Token)验证
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:38
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ShiroService shiroService;

    public static final String USER_KEY = "userSeq";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Login annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        } else {
            return true;
        }

        if (annotation == null) {
            return true;
        }

        //从header中读取token
        String token = request.getHeader(jwtUtils.getHeader());
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(jwtUtils.getHeader());
        }

        //判断是否为空
        if (StringUtils.isBlank(token)) {
            throw new RRException(jwtUtils.getHeader() + "不能为空", HttpStatus.UNAUTHORIZED.value());
        }
        //判断是否失效
        Claims claims = jwtUtils.getClaimByToken(token);
        if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
            throw new RRException(jwtUtils.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }

        //权限判断
        if (annotation.value().length > 0) {
            //该用户的所有权限
            Set<String> permissions = shiroService.getUserPermissions(Long.parseLong(claims.getSubject()));
            for (String s : annotation.value()) {
                if (!permissions.contains(s)) {
                    throw new RRException("对不起，您的权限不足", HttpStatus.FORBIDDEN.value());
                }
            }
        }

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, Long.parseLong(claims.getSubject()));

        return true;
    }
}
