package io.nuite.modules.sr_base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.sr_base.entity.BaseESmartEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: yangchuang
 * @Date: 2018/8/10 15:15
 * @Version: 1.0
 * @Description:
 */
@Mapper
public interface BaseESmartDao extends BaseMapper<BaseESmartEntity> {

    BaseESmartEntity selectByUserSeq(Integer factoryUserSeq);
}
