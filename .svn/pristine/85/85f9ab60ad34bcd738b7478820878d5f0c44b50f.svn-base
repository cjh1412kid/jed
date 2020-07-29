package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.TargetSalesEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @description: 导购员指标Dao层
 * @author: jxj
 * @create: 2019-03-21 10:30
 */
@Mapper
public interface TargetSalesDao extends BaseMapper<TargetSalesEntity> {
    /**
     * 新增导购员指标
     * @param targetSalesEntities
     * @return
     * @throws Exception
     */
    Integer insertTargetSales(List<TargetSalesEntity> targetSalesEntities) throws Exception;

    /**
     * 修改导购员指标
     * @param targetSalesEntities
     * @return
     * @throws Exception
     */
    Integer updateTargetSales(List<TargetSalesEntity> targetSalesEntities) throws Exception;

    /**
     * 获取总指标
     * @param map
     * @return
     * @throws Exception
     */
    TargetSalesEntity selectTotalTarget(Map<String, Object> map) throws Exception;

    /**
     * 获取总月指标
     * @param map
     * @return
     * @throws Exception
     */
    List<TargetSalesEntity> selectTotalMonthTarget(Map<String, Object> map) throws Exception;

    /**
     * 获取单个月指标
     * @param map
     * @return
     * @throws Exception
     */
    TargetSalesEntity selectMonthTarget(Map<String, Object> map) throws Exception;

    /**
     * 获取单个月门店指标列表
     * @param map
     * @return
     * @throws Exception
     */
    List<TargetSalesEntity> selectMonthSalesTarget(Map<String, Object> map) throws Exception;

    /**
     * 获取单个月导购员周指标列表
     * @param map
     * @return
     * @throws Exception
     */
    List<TargetSalesEntity> selectWeekSalesTarget(Map<String, Object> map) throws Exception;

    /**
     * 获取每周导购员日指标列表
     * @param map
     * @return
     * @throws Exception
     */
    List<TargetSalesEntity> selectDaySalesTarget(Map<String, Object> map) throws Exception;

    /**
     * 获取门店当前年度指标和当前月度指标
     * @param map
     * @return
     * @throws Exception
     */
    TargetSalesEntity selectSalesTotalTarget(Map<String, Object> map) throws Exception;

    /**
     * 获取门店销售指标分析
     * @param map
     * @return
     * @throws Exception
     */
    TargetSalesEntity selectSalesTargetAnalyze(Map<String, Object> map) throws Exception;

    /**
     * 获取门店销售周指标分析
     * @param map
     * @return
     * @throws Exception
     */
    List<TargetSalesEntity> selectSalesWeekTargetAnalyze(Map<String, Object> map) throws Exception;

    /**
     * 获取门店每周的日销售指标分析
     * @param map
     * @return
     * @throws Exception
     */
    List<TargetSalesEntity> selectSalesDayTargetAnalyze(Map<String, Object> map) throws Exception;
    
    /**
     * 获取同期销售额
     * @param map
     * @return
     * @throws Exception
     */
    BigDecimal getSaleSumByMonth(Map<String, Object> map) throws Exception;

    BigDecimal getSaleSumByDate(Map<String, Object> map) throws Exception;

    BigDecimal getShopSaleSumByMonth(Map<String, Object> map) throws Exception;

    BigDecimal querySaleSumByDate(Map<String, Object> map) throws Exception;
}
