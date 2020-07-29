package io.nuite.modules.order_platform_app.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.SievePlateDistributeEntity;
import io.nuite.modules.system.entity.OrderManageEntity;

import java.util.List;
import java.util.Map;

public interface SievePlateDistributeService extends IService<SievePlateDistributeEntity> {
    
    Integer getIsGoodIdbeDistributed(Integer companySeq, String goodId);
    
    void distributeSievePlate(OrderManageEntity orderManageEntity, String shopDistributeNum);
    
    List<Map<String, Object>> getSievePlateDistributedDetail(Integer companySeq, String goodId);
    
    Map<String, Object> getShopDistributeNumOneCategory(Integer companySeq, Integer year, Integer seasonSeq, Integer shopSeq, Integer categorySeq);
    
    void resetDistributedSievePlate(List<Integer> sievePlateDistributeSeqList);
    
    List<SievePlateDistributeEntity> getResetDistributedSievePlateList(Integer companySeq, Integer year, Integer seasonSeq,
                                                                       Integer categorySeq, List<Integer> shopSeqs);
    
    /**
     * 根据条件动态查询订单列表，分页，按门店汇总
     *
     * @param page
     * @param companySeq
     * @param shopSeq
     * @param year
     * @param seasonSeq
     * @return
     */
    Page listPageByCondition(Page page, Integer companySeq, Integer shopSeq, Integer year, Integer seasonSeq);
    
    /**
     * 根据条件动态查询订单列表，分页，按货号汇总
     *
     * @param page
     * @param companySeq
     * @param year
     * @param seasonSeq
     * @return
     */
    Page listPageByGoodID(Page page, Integer companySeq, Integer year, Integer seasonSeq);
    
    /**
     * 查询当前用户公司的订单相关联的所有门店
     *
     * @param companySeq
     * @return
     */
    List<Map<String, Object>> queryShopsByCompanySeq(Integer companySeq);
    
    /**
     * 查询订货会订单中存在的年份
     *
     * @param companySeq
     * @return
     */
    List<Integer> queryYearsByCompanySeq(Integer companySeq);
    
    /**
     * 查询订货会订单中存在的季节
     *
     * @param companySeq
     * @return
     */
    List<Map<String, Object>> querySeaSonsByCompanySeq(Integer companySeq);
}

