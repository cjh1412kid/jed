package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.BaseESmartEntity;

/**
 * @Author: yangchuang
 * @Date: 2018/8/10 15:19
 * @Version: 1.0
 * @Description:
 */

public interface BaseESmartService extends IService<BaseESmartEntity> {

    BaseESmartEntity getByUserSeq(Integer factoryUserSeq);
}
