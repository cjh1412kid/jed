package io.nuite.modules.order_platform_app.service;

import io.nuite.modules.order_platform_app.entity.*;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {

    Integer submitOrder(List<Map<String, Integer>> stockChangeList, List<Integer> shoppingCartSeqList, OrderEntity orderEntity,
                        List<OrderProductsEntity> orderProductsList);

    void changeShoesDataStock(Integer shoesDataSeq, Integer changNum);

    List<OrderProductsEntity> getOrderProductsListByOrderSeq(Integer orderSeq);

    void cancelOrder(Integer orderSeq, String remark, List<Map<String, Integer>> stockChangeList);

    void deleteOrder(Integer orderSeq);

    void updateOrderSuggestion(Integer orderSeq, Date requireTime, String suggestion);

    void orderConfirmReceived(Integer orderSeq);

    List<Object> getOrderSeqByCompanySeq(Integer companySeq);

    List<Map<String, Object>> getOrderSummary(List<Object> orderSeqlist, Integer start, Integer num);

    List<Map<String, Object>> getSummaryDetailList(List<Object> orderSeqlist, Integer shoesDataSeq);

    List<OrderEntity> getOrderListByCompanySeq(Integer companySeq, Integer orderStatus, Integer start, Integer num);

    List<OrderEntity> getOrderListByUserSeq(Integer userSeq, Integer orderStatus, Integer start, Integer num);

    GoodsShoesEntity getGoodsShoesByShoesDateSeq(Integer shoesDataSeq);

    OrderEntity getOrderBySeq(Integer orderSeq);

    void orderConfirmed(Integer orderSeq);

    void orderConfirmedPay(Integer orderSeq, BigDecimal paid, Integer companySeq);

    void orderStore(Integer orderSeq);

    List<Map<String, Object>> getExpressCompanyList();

    void updateOrderPaid(Integer orderSeq, BigDecimal paid);

    OrderProductsEntity getOrderProductsBySeq(Integer orderProductsSeq);

    Integer orderDeliverGoods(OrderEntity order, List<OrderProductsEntity> orderProductsList, OrderExpressEntity orderExpressEntity,
                              List<OrderExpressDetailsEntity> orderExpressDetailsList);

    Map<String, Object> getOrderMapBySeq(Integer orderSeq);

    Map<String, Object> getUserMapByUserSeq(Integer userSeq);

    List<Map<String, Object>> getOrderProductsList(Integer orderSeq);

    List<Map<String, Object>> getOrderExpressList(Integer orderSeq);

    ExpressCompanyEntity getExpressCompanyBySeq(Integer expressCompanySeq);

    List<Map<String, Object>> getOrderExpressDetailsList(Integer orderExpressSeq);

    ShoesInfoEntity getShoesInfoByShoesDateSeq(Integer shoesDataSeq);

    void changeOrder(OrderEntity orderChangeEntity, List<OrderProductsEntity> orderProductsChangeList,
                     List<Map<String, Integer>> stockChangeList);

}
