package io.nuite.modules.system.dao.order_platform;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.system.entity.order_platform.GoodsAllotOrderEntity;
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
 * @since 2019-04-01
 */

@Mapper
public interface GoodsAllotOrderDao extends BaseMapper<GoodsAllotOrderEntity> {
    
    /**
     * 根据公司seq，季节，年份 查询所有货品配码订单
     *
     * @param companySeq
     * @param year
     * @param seasonSeq
     * @return
     */
    List<GoodsAllotOrderEntity> downloadGoodsAllotExcel(@Param("companySeq") Integer companySeq,
                                                        @Param("year") Integer year,
                                                        @Param("seasonSeq") Integer seasonSeq);
    
    /**
     * 根据货号、年份、季节查询货号配码订单
     *
     * @param companySeq
     * @param goodID
     * @param year
     * @param seasonSeq
     * @return
     */
    GoodsAllotOrderEntity selectGoodsAllotOrder(@Param("companySeq") Integer companySeq,
                                                @Param("goodID") String goodID,
                                                @Param("year") Integer year,
                                                @Param("seasonSeq") Integer seasonSeq);
    
    /**
     * 根据单号获取货品配码单
     *
     * @param goodsAllotSeq
     * @return
     */
    GoodsAllotOrderEntity selectGoodsAllotOrder2(@Param("seq") Integer goodsAllotSeq);
    
    /**
     * 下载货号配码单-查询最大最小码
     *
     * @param companySeq
     * @param year
     * @param seasonSeq
     * @return
     */
    Map<String, Integer> selectMaxAndMinSize(@Param("companySeq") Integer companySeq,
                                             @Param("year") Integer year,
                                             @Param("seasonSeq") Integer seasonSeq);
    
    /**
     * 货号重新配码操作： 查询该货号已配码门店列表
     *
     * @param companySeq
     * @param goodID
     * @param year
     * @param seasonSeq
     * @return
     */
    List<String> selectAllotShopName(@Param("companySeq") Integer companySeq,
                                     @Param("goodID") String goodID,
                                     @Param("year") Integer year,
                                     @Param("seasonSeq") Integer seasonSeq);
    
    /**
     * 货号重新配码操作：删除存在的门店配码
     *
     * @param companySeq
     * @param goodID
     * @param year
     * @param seasonSeq
     */
    void deleteShopAllot(@Param("companySeq") Integer companySeq,
                         @Param("goodID") String goodID,
                         @Param("year") Integer year,
                         @Param("seasonSeq") Integer seasonSeq);
    
    /**
     * 货号重新配码操作：删除存在的门店配码
     *
     * @param companySeq
     * @param goodID
     * @param year
     * @param seasonSeq
     */
    List<Integer> selectShopAllotSeq(@Param("companySeq") Integer companySeq,
                         @Param("goodID") String goodID,
                         @Param("year") Integer year,
                         @Param("seasonSeq") Integer seasonSeq);
}
