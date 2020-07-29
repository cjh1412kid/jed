package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.order_platform_app.entity.SievePlateDistributeEntity;
import io.nuite.modules.system.from.GoodsAllotForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-26 10:00:56
 */
@Mapper
public interface SievePlateDistributeDao extends BaseMapper<SievePlateDistributeEntity> {
    
    /**
     * 根据条件动态查询订单列表，分页
     *
     * @param page
     * @param companySeq
     * @param shopSeq
     * @param year
     * @param seasonSeq
     * @return
     */
    List<SievePlateDistributeEntity> listByCondition(Page page, @Param("companySeq") Integer companySeq,
                                                     @Param("shopSeq") Integer shopSeq,
                                                     @Param("year") Integer year,
                                                     @Param("seasonSeq") Integer seasonSeq);
    
    /**
     * 根据条件动态查询订单列表，分页，按货号汇总
     *
     * @param page
     * @param companySeq
     * @param year
     * @param seasonSeq
     * @return
     */
    List<GoodsAllotForm> listByGoodID(Page page,
                                      @Param("companySeq") Integer companySeq,
                                      @Param("year") Integer year,
                                      @Param("seasonSeq") Integer seasonSeq);
    
    /**
     * 查询订货会订单中存在的门店
     *
     * @param companySeq
     * @return
     */
    List<Map<String, Object>> selectShopsByCompanySeq(Integer companySeq);
    
    /**
     * 查询订货会订单中存在的年份
     *
     * @param companySeq
     * @return
     */
    List<Integer> selectYearsByCompanySeq(Integer companySeq);
    
    /**
     * 查询订货会订单中存在的季节
     *
     * @param companySeq
     * @return
     */
    List<Map<String, Object>> selectSeaSonsByCompanySeq(Integer companySeq);
    
}
