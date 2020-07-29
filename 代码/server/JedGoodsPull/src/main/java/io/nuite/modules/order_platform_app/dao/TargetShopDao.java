package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.order_platform_app.entity.TargetShopEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description: 门店指标Dao层
 * @author: jxj
 * @create: 2019-03-21 10:30
 */
@Mapper
public interface TargetShopDao extends BaseMapper<TargetShopEntity> {
    /**
     * 新增门店指标
     * @param targetShopEntities
     * @return
     * @throws Exception
     */
    Integer insertTargetShop(List<TargetShopEntity> targetShopEntities) throws Exception;

    /**
     * 修改门店指标
     * @param targetShopEntities
     * @return
     * @throws Exception
     */
    Integer updateTargetShop(List<TargetShopEntity> targetShopEntities) throws Exception;

    /**
     * 获取总指标
     * @param map
     * @return
     * @throws Exception
     */
    TargetShopEntity selectTotalTarget(Map<String, Object> map) throws Exception;

    /**
     * 获取总月指标
     * @param map
     * @return
     * @throws Exception
     */
    List<TargetShopEntity> selectTotalMonthTarget(Map<String, Object> map) throws Exception;

    /**
     * 获取单个月指标
     * @param map
     * @return
     * @throws Exception
     */
    TargetShopEntity selectMonthTarget(Map<String, Object> map) throws Exception;

    /**
     * 获取单个月门店指标列表
     * @param map
     * @return
     * @throws Exception
     */
    List<TargetShopEntity> selectMonthShopTarget(Map<String, Object> map) throws Exception;

    /**
     * 获取总销售指标分析
     * @param map
     * @return
     * @throws Exception
     */
    TargetShopEntity selectTotalTargetAnalyze(Map<String, Object> map) throws Exception;

    /**
     * 获取门店总指标分析列表
     * @param map
     * @return
     * @throws Exception
     */
    List<TargetShopEntity> selectTotalShopTargetAnalyze(Map<String, Object> map) throws Exception;

    /**
     * 获取门店每个月的指标分析
     * @param map
     * @return
     * @throws Exception
     */

    List<TargetShopEntity> selectTotalShopTargetAnalyzeByYear(Page<TargetShopEntity> page, Map<String, Object> map) throws Exception;
}
