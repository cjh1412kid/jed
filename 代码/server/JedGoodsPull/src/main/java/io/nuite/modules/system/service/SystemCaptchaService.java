package io.nuite.modules.system.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.system.entity.SystemCaptchaEntity;

public interface SystemCaptchaService extends IService<SystemCaptchaEntity>{


    /**
     * 获取图片验证码
     */
    //BufferedImage getCaptcha(String uuid);

    /**
     * 验证码效验
     * @param uuid  uuid
     * @param code  验证码
     * @return  true：成功  false：失败
     */
    //boolean validate(String uuid, String code);
}
