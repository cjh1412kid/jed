package io.nuite.modules.system.service.order_platform;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.entity.OrderEntity;
import io.nuite.modules.order_platform_app.entity.OrderProductsEntity;
import io.nuite.modules.sr_base.entity.BaseBrandEntity;
import io.nuite.modules.sr_base.entity.BaseCompanyEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.system.from.PartShippedForm;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderManageService {

    /**
     * 订单列表
     *
     * @return
     */
    PageUtils orderlist(BaseUserEntity baseUserEntity, Map<String, Object> params);

    List<Map<String, Object>> getOrderGoodIds(Integer orderSeq);

    List<Map<String, Object>> getGoodsIdProducts(Integer orderSeq, String goodId);

    List<Map<String, Object>> getOrderProductsList(Integer orderSeq);

    /**
     * 订单详情
     *
     * @return
     */
    OrderEntity getOrderBySeq(Integer seq);

    /**
     * 删除订单
     */
    void deleteOrder(Integer seq);

    void confirmOrder(Integer orderSeq, Date requireTime, BaseUserEntity loginUser);

    /**
     * 根据状态序号和订单序号对该订单状态进行跟进
     *
     * @param baseUserEntity
     */
    R updateOrderStatus(Integer orderStatus, Integer seq, BaseUserEntity loginUser);


    /**
     * 修改订单总价格
     *
     * @param orderSeq        订单序号
     * @param orderTotalPrice 修改金额
     */
    void changeOrderTotalPrice(Integer orderSeq, BigDecimal orderTotalPrice);

    /**
     * 修改某些商品单价
     *
     * @param orderSeq
     * @param seqPriceList
     */
    void changeProductsPrice(Integer orderSeq, String seqPriceList);

    /**
     * 付款
     *
     * @param seq        订单序号
     * @param price      付款金额
     * @param companySeq
     */
    R payOrder(Integer seq, BigDecimal price, BaseUserEntity loginUser);

    List<Map<String, Object>> getExpressCompanyList();

    List<BaseCompanyEntity> attachCompany(BaseUserEntity user);

    List<BaseBrandEntity> attachAgent(BaseUserEntity user);

    void partShipped(PartShippedForm partShippedForm, BaseUserEntity loginUser) throws IOException;

    List<Map<String, Object>> getUserList(Integer companySeq, Integer brandSeq, Integer attachType, Integer attachSeq);

    List<OrderProductsEntity> getOrderProductsListByOrderSeq(Integer orderSeq);

    void cancelOrder(Integer orderSeq, String remark, List<Map<String, Integer>> stockChangeList);

    void changeReceiveTime(Integer seq, Date receiveTime);

	void splitOrder(Integer[] orderSeqArr, String splitSX);

	void classifySplitOrder(Integer companySeq, String startTime, String endTime);

	void splitOrderByModel(Integer companySeq, Integer modelSeq, String startTime, String endTime);

}
