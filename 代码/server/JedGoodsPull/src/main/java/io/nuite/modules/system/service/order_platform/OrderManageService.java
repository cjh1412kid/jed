package io.nuite.modules.system.service.order_platform;

public interface OrderManageService {

   /* *//**
     * 订单列表
     *
     * @return
     *//*
    PageUtils orderlist(BaseUserEntity baseUserEntity, Map<String, Object> params);

    List<Map<String, Object>> getOrderGoodIds(Integer orderSeq);

    List<Map<String, Object>> getGoodsIdProducts(Integer orderSeq, String goodId);

    List<Map<String, Object>> getOrderProductsList(Integer orderSeq);

    *//**
     * 订单详情
     *
     * @return
     *//*
    OrderEntity getOrderBySeq(Integer seq);

    *//**
     * 删除订单
     *//*
    void deleteOrder(Integer seq);

    void confirmOrder(Integer orderSeq, Date requireTime, BaseUserEntity loginUser);

    *//**
     * 根据状态序号和订单序号对该订单状态进行跟进
     *
     * @param
     *//*
    R updateOrderStatus(Integer orderStatus, Integer seq, BaseUserEntity loginUser);


    *//**
     * 修改订单总价格
     *
     * @param
     * @param
     *//*
    void changeOrderTotalPrice(Integer orderSeq, BigDecimal orderTotalPrice);

    *//**
     * 修改某些商品单价
     *
     * @param
     * @param
     *//*
    void changeProductsPrice(Integer orderSeq, String seqPriceList);

    *//**
     * 付款
     *
     * @param
     * @param
     * @param
     *//*
    R payOrder(Integer seq, BigDecimal price, BaseUserEntity loginUser);

    List<Map<String, Object>> getExpressCompanyList();

    List<BaseCompanyEntity> attachCompany(BaseUserEntity user);

    List<BaseBrandEntity> attachAgent(BaseUserEntity user);

    //void partShipped(PartShippedForm partShippedForm, BaseUserEntity loginUser) throws IOException;

    List<Map<String, Object>> getUserList(Integer companySeq, Integer brandSeq, Integer attachType, Integer attachSeq);

    List<OrderProductsEntity> getOrderProductsListByOrderSeq(Integer orderSeq);

    void cancelOrder(Integer orderSeq, String remark, List<Map<String, Integer>> stockChangeList);

    void changeReceiveTime(Integer seq, Date receiveTime);

	void splitOrder(Integer[] orderSeqArr, String splitSX);

	void classifySplitOrder(Integer companySeq, String startTime, String endTime);

	void splitOrderByModel(Integer companySeq, Integer modelSeq, String startTime, String endTime);*/

}
