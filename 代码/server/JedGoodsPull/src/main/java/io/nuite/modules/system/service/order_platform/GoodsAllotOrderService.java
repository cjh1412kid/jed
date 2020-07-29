package io.nuite.modules.system.service.order_platform;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.system.entity.order_platform.GoodsAllotOrderEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yangchuang
 * @since 2019-04-01
 */
public interface GoodsAllotOrderService extends IService<GoodsAllotOrderEntity> {
    
   /* *//**
     * 保存货号配码订单
     *
     * @param goodsAllotOrderEntity
     * @return
     *//*
    GoodsAllotOrderEntity saveGoodsAllotOrder(GoodsAllotOrderEntity goodsAllotOrderEntity);
    
    *//**
     * 更新货号配码订单
     *
     * @param goodsAllotOrderEntity
     * @return
     *//*
    GoodsAllotOrderEntity updateGoodsAllotOrder(GoodsAllotOrderEntity goodsAllotOrderEntity);
    
    *//**
     * 删除配货单
     *
     * @param allotOrderSeq
     *//*
    void deleteGoodsAllotOrder(Integer allotOrderSeq);
    
    *//**
     * 根据货号、年份、季节查询货号配码订单
     *
     * @param companySeq
     * @param goodID
     * @param year
     * @param seasonSeq
     * @return
     *//*
    GoodsAllotOrderEntity queryGoodsAllotOrder(Integer companySeq, String goodID, Integer year, Integer seasonSeq);
    
    *//**
     * 根据单号获取货品配码单，门店配码使用
     *
     * @param goodsAllotSeq
     * @return
     *//*
    GoodsAllotOrderEntity queryGoodsAllotOrder(Integer goodsAllotSeq);
    
    *//**
     * 根据公司seq，季节，年份 查询所有货品配码订单
     *
     * @param companySeq
     * @param year
     * @param seasonSeq
     * @return
     *//*
    List<Map<String, String>> downloadGoodsAllotExcel(Integer companySeq, Integer year, Integer seasonSeq);
    
    *//**
     * 下载货号配码单-查询最大最小码
     *
     * @param companySeq
     * @param year
     * @param seasonSeq
     * @return
     *//*
    Map<String, Integer> selectMaxAndMinSize(Integer companySeq, Integer year, Integer seasonSeq);
    
    
    *//**
     * 货号重新配码操作： 查询该货号已配码门店列表
     *
     * @param companySeq
     * @param goodID
     * @param year
     * @param seasonSeq
     * @return
     *//*
    List<String> queryAllotShopName(Integer companySeq, String goodID, Integer year, Integer seasonSeq);
    
    *//**
     * 货号重新配码操作：删除存在的门店配码
     *
     * @param companySeq
     * @param goodID
     * @param year
     * @param seasonSeq
     *//*
    void deleteShopAllot(Integer companySeq, String goodID, Integer year, Integer seasonSeq);*/
}
