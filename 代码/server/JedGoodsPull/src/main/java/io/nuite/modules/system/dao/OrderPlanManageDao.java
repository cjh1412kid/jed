package io.nuite.modules.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.system.entity.OrderManageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-22
 */

@Mapper
public interface OrderPlanManageDao extends BaseMapper<OrderManageEntity> {
    
    List<OrderManageEntity> queryPage(Page<OrderManageEntity> page, @Param("params") Map<String, Object> params, @Param("companySeq") Integer companySeq);
    
    List<Map<String,Object>> selectAllExistSeasons(Integer companySeq);
    
    List<Integer> selectAllExistYears(Integer companySeq);
}
