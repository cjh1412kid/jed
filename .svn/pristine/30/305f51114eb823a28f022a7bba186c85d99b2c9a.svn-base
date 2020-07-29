package io.nuite.modules.system.controller;

import io.nuite.common.utils.Constant;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import io.nuite.modules.system.from.SystemLoginForm;
import io.nuite.modules.system.service.SystemCaptchaService;
import io.nuite.modules.system.service.SystemUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * 登录相关
 */
@RestController
@RequestMapping("system")
@Api(tags = "后台 - 登陆相关接口", description = "登陆")
public class SystemLoginController extends AbstractController {
    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private SystemCaptchaService systemCaptchaService;
    @Autowired
    private SystemUserTokenService systemUserTokenService;

    /**
     * 验证码
     */
    @GetMapping("getcaptcha.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        // 获取图片验证码
        BufferedImage image = systemCaptchaService.getCaptcha(uuid);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 登录
     */
    @PostMapping("login")
    @ApiOperation("登录")
    public Map<String, Object> login(@RequestBody SystemLoginForm form) throws IOException {
        boolean captcha = systemCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            return R.error("验证码不正确");
        }

        // 用户信息
        BaseUserEntity user = baseUserService.queryByUserName(form.getUsername());

        // 账号不存在、密码错误
        if (user == null || !user.getPassword().equals(DigestUtils.sha256Hex(form.getPassword()))) {
            return R.error("账号或密码不正确");
        }
        // 账号锁定
        if (user.getDel() == 1) {
            return R.error("账号不存在,请联系平台");
        }

        if (user.getSeq() != Constant.SUPER_ADMIN && (user.getUserRole() == null || Constant.Role.SHOP_ADMIN.getCode().equals(user.getUserRole().getCode()))) {
            return R.error("非工厂不可登录管理端！");
        }

        // 生成token，并保存到数据库
        R r = systemUserTokenService.createToken(user.getSeq());
        return r;
    }

    /**
     * 退出
     */
    @PostMapping("logout")
    public R logout() {
        systemUserTokenService.logout(getUserSeq());
        return R.ok();
    }

}
