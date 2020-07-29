package io.nuite.modules.system.service.order_platform;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;

import java.util.List;
import java.util.Map;

public interface SysMeetingPlanService {
    
    /**
     * 获取门店的订货计划
     * 分页查询和条件查询
     *
     * @param page
     * @param companySeq
     * @param seasonSeq
     * @param year
     * @param uploadState
     * @return
     */
    Page<Map<String, Object>> getShopPlanList(Page page, Integer companySeq, Integer seasonSeq, Integer year, Integer uploadState);
    
    List<GoodsPeriodEntity> getPeriodListByBrandSeq(Integer brandSeq);
    
    void deleteMeetingPlan(Integer shopSeq, Integer year, Integer seasonSeq);
    
    PageUtils getUserPlanDetailsList(Integer companySeq, Integer userSeq, Integer periodSeq, Integer pageNo, Integer pageSize);
    
    Page getShopPlanDetailsList(Page page, Integer companySeq, Integer shopSeq, Integer year, Integer seasonSeq);
    
    void addBatchMeetingPlan(Integer[] shopSeqArr, BaseUserEntity baseUserEntity, List<List<Object>> rows, Integer year, Integer seasonSeq);
}
