package io.nuite.modules.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.system.entity.order_platform.SystemLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/29 14:26
 * @Version: 1.0
 * @Description:
 */

@Mapper
public interface SystemLogDao extends BaseMapper<SystemLogEntity>{

    List<SystemLogEntity> getLogListByStartAndNum(@Param("start") Integer start, @Param("num") Integer num);
}
