package io.nuite.modules.system.dao.order_platform;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.system.entity.order_platform.ShopAllotOrderEntity;
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
 * @since 2019-04-03
 */

@Mapper
public interface ShopAllotOrderDao extends BaseMapper<ShopAllotOrderEntity> {
    
    /**
     * 获取指定货号尺码的已配码数量
     *
     * @param goodsAllotSeq 货品配码单号
     * @param size
     * @return
     */
    int getAllotCount(@Param("goodsAllotSeq") Integer goodsAllotSeq,
                      @Param("size") Integer size);
    
    /**
     * 根据公司seq，季节，年份 查询所有门店配码订单
     *
     * @param companySeq
     * @param year
     * @param seasonSeq
     * @return
     */
    List<Map<String, String>> downloadShopAllotExcel(@Param("companySeq") Integer companySeq,
                                                     @Param("shopSeq") Integer shopSeq,
                                                     @Param("year") Integer year,
                                                     @Param("seasonSeq") Integer seasonSeq);
    /**
     * 下载门店配码单-查询最大最小码
     *
     * @param companySeq
     * @param shopSeq
     * @param year
     * @param seasonSeq
     * @return
     */
    Map<String, Integer> selectMaxAndMinSize(@Param("companySeq") Integer companySeq,
                                             @Param("shopSeq") Integer shopSeq,
                                             @Param("year") Integer year,
                                             @Param("seasonSeq") Integer seasonSeq);
    
    /**
     * 获取门店配码订单
     *
     * @param shopAllotOrderSeq
     * @return
     */
    ShopAllotOrderEntity getShopAllot(@Param("seq") Integer shopAllotOrderSeq);
}
