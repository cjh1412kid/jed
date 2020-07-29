package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2018-11-29 18:26:05
 */
@Mapper
public interface SaleShoesDetailDao extends BaseMapper<SaleShoesDetailEntity> {
    /**
     * 新增销售
     * @param saleShoesDetailEntities
     * @return
     * @throws Exception
     */
    Integer insertSaleShoesDetail(List<SaleShoesDetailEntity> saleShoesDetailEntities) throws Exception;

	/**
     * 获取门店总完成金额列表
     * @param map
     * @return
     * @throws Exception
     */
    List<SaleShoesDetailEntity> selectShopCompleteMoney(Map<String, Object> map) throws Exception;

    /**
     * 获取门店每月完成金额列表
     * @param map
     * @return
     * @throws Exception
     */
    List<SaleShoesDetailEntity> selectShopCompleteMoneyYear(Map<String, Object> map) throws Exception;

    /**
     * 获取货品销量排行
     * @param map
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> selectSaleNumRank(Map<String, Object> map) throws Exception;

    /**
     * 获取昨天销售前三货品信息
     * @param map
     * @return
     * @throws Exception
     */
    List<SaleShoesDetailEntity> selectSaleGoodsTopThree(Map<String, Object> map) throws Exception;

    /**
     * 获取昨天销售前三分类信息
     * @param map
     * @return
     * @throws Exception
     */
    List<SaleShoesDetailEntity> selectSaleCategoryTopThree(Map<String, Object> map) throws Exception;

    /**
     * 根据销售时间获取鞋子序号
     * @param map
     * @return
     * @throws Exception
     */
    List<Integer> getShoesSeq(Map<String, Object> map) throws Exception;

    /**
     * 畅销鞋子列表
     * @param map
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> selectSalableShoes(Map<String, Object> map) throws Exception;

    /**
     * 滞销鞋子列表
     * @param map
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> selectUnsalableShoes(Map<String, Object> map) throws Exception;

    /**
     * 畅滞销鞋子环比数据
     * @param map
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> selectBeforeSalableShoes(Map<String, Object> map) throws Exception;

    /**
     * 把中间库的数据插入主库
     * @return
     * @throws Exception
     */
    Integer selectSaleShoesDetailCopyIntoSaleShoesDetail() throws Exception;
}
