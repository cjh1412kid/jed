package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.system.dao.SystemUserTokenDao;
import io.nuite.modules.system.entity.SystemUserTokenEntity;
import io.nuite.modules.system.oauth2.SystemTokenGenerator;
import io.nuite.modules.system.service.SystemUserTokenService;
import io.nuite.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class SystemUserTokenServiceImpl extends ServiceImpl<SystemUserTokenDao, SystemUserTokenEntity> implements SystemUserTokenService {
    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Autowired
    private SystemUserTokenDao systemUserTokenDao;

    @Override
    public R createToken(long userId) {
        //生成一个token
        String token = SystemTokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
        //判断是否生成过token
        SystemUserTokenEntity tokenEntity = systemUserTokenDao.selectBySeq((int) userId);
        if (tokenEntity == null) {
            tokenEntity = new SystemUserTokenEntity();
            tokenEntity.setUserSeq(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //保存token
            this.insert(tokenEntity);
        } else {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //更新token
            systemUserTokenDao.updateBySeq(tokenEntity);
        }
        R r = R.ok().put("token", token).put("expire", EXPIRE);

        return r;
    }

    @Override
    public void logout(long userId) {
        //生成一个token
        String token = SystemTokenGenerator.generateValue();

        //修改token
        SystemUserTokenEntity tokenEntity = new SystemUserTokenEntity();
        tokenEntity.setUserSeq(userId);
        tokenEntity.setToken(token);
        this.updateById(tokenEntity);
    }
}
