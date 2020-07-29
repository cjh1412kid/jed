package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 每日库存根据详情计算库存总情况
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-20 16:23:59
 */
@Mapper
public interface ShoesDataDailyDao extends BaseMapper<ShoesDataDailyEntity> {
	
}
