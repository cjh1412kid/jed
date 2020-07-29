package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.code.kaptcha.Producer;
import io.nuite.modules.system.dao.SystemCaptchaDao;
import io.nuite.modules.system.entity.SystemCaptchaEntity;
import io.nuite.modules.system.service.SystemCaptchaService;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;

@Service
public class SystemCaptchaServiceImpl extends ServiceImpl<SystemCaptchaDao, SystemCaptchaEntity> implements SystemCaptchaService {
    @Autowired
    private Producer producer;

    @Override
    public BufferedImage getCaptcha(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            throw new RRException("uuid不能为空");
        }
        //生成文字验证码
        String code = producer.createText();

        SystemCaptchaEntity systemCaptchaEntity = new SystemCaptchaEntity();
        systemCaptchaEntity.setUuid(uuid);
        systemCaptchaEntity.setCode(code);
        //5分钟后过期
        systemCaptchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
        this.insertOrUpdate(systemCaptchaEntity);
        //返回处理后的验证码
        return producer.createImage(code);
    }

    @Override
    public boolean validate(String uuid, String code) {
        SystemCaptchaEntity systemCaptchaEntity = this.selectOne(new EntityWrapper<SystemCaptchaEntity>().eq("uuid", uuid));
        if (systemCaptchaEntity == null) {
            return false;
        }

        //删除验证码
        this.deleteById(uuid);

        if (systemCaptchaEntity.getCode().equalsIgnoreCase(code) && systemCaptchaEntity.getExpireTime().getTime() >= System.currentTimeMillis()) {
            return true;
        }

        return false;
    }

}
