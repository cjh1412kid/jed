package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.BaseESmartDao;
import io.nuite.modules.sr_base.entity.BaseESmartEntity;
import io.nuite.modules.sr_base.service.BaseESmartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: yangchuang
 * @Date: 2018/8/10 15:20
 * @Version: 1.0
 * @Description:
 */

@Service
public class BaseESmartServiceImpl extends ServiceImpl<BaseESmartDao, BaseESmartEntity> implements BaseESmartService {

    @Autowired
    BaseESmartDao baseESmartDao;

    @Override
    public BaseESmartEntity getByUserSeq(Integer factoryUserSeq) {
        return baseESmartDao.selectByUserSeq(factoryUserSeq);
    }
}
