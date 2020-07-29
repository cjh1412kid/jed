package io.nuite.modules.system.service.order_platform;

import io.nuite.modules.system.entity.order_platform.ShopAllotOrderEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yangchuang
 * @since 2019-04-03
 */
public interface ShopAllotOrderService extends IService<ShopAllotOrderEntity> {
    
    /**
     * 保存门店配码订单
     *
     * @param shopAllotOrderEntity
     * @return
     */
    ShopAllotOrderEntity saveShopAllotOrder(ShopAllotOrderEntity shopAllotOrderEntity, Integer goodsAllotSeq);
    
    /**
     * 删除门店配码订单
     *
     * @param orderSeq 门店配码订单序号
     * @return
     */
    void deleteShopAllotOrder(Integer orderSeq, Integer goodAllotSeq);
    
    /**
     * 根据公司seq，季节，年份 查询所有门店配码订单
     *
     * @param companySeq
     * @param year
     * @param seasonSeq
     * @return
     */
    List<Map<String, String>> downloadShopAllotExcel(Integer companySeq, Integer shopSeq, Integer year, Integer seasonSeq);
    
    /**
     * 下载门店配码单-查询最大最小码
     *
     * @param companySeq
     * @param shopSeq
     * @param year
     * @param seasonSeq
     * @return
     */
    Map<String, Integer> selectMaxAndMinSize(Integer companySeq, Integer shopSeq, Integer year, Integer seasonSeq);
    
    /**
     * 获取门店配码订单
     *
     * @param shopAllotOrderSeq
     * @return
     */
    ShopAllotOrderEntity getShopAllot(Integer shopAllotOrderSeq);
    
    ShopAllotOrderEntity getShopAllotBySilevePlateDistributeSeq(Integer silevePlateDistributeSeq);
}
