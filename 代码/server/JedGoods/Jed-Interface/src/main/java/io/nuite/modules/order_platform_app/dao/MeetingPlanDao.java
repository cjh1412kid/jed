package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.order_platform_app.entity.MeetingPlanEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 0
 *
 * @author admin
 * @email
 * @date 2018-04-11 11:29:58
 */
@Mapper
public interface MeetingPlanDao extends BaseMapper<MeetingPlanEntity> {
    
    List<Map<String, Object>> getShopPlanList(Page<Map<String, Object>> page,
                                              @Param("companySeq") Integer companySeq,
                                              @Param("seasonSeq") Integer seasonSeq,
                                              @Param("year") Integer year,
                                              @Param("uploadState") Integer uploadState);
    
    List<MeetingPlanEntity> selectByPageAndCondition(Page page,
                                                     @Param("shopSeq") Integer shopSeq,
                                                     @Param("year") Integer year,
                                                     @Param("seasonSeq") Integer seasonSeq);
}
