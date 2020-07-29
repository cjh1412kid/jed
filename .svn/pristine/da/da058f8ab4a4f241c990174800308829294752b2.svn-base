package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.base.Joiner;
import io.nuite.modules.order_platform_app.dao.*;
import io.nuite.modules.order_platform_app.entity.*;
import io.nuite.modules.order_platform_app.service.OrderService;
import io.nuite.modules.order_platform_app.utils.OrderStatusEnum;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductsDao orderProductsDao;

    @Autowired
    private ShoesDataDao shoesDataDao;

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private GoodsShoesDao goodsShoesDao;

    @Autowired
    private ExpressCompanyDao expressCompanyDao;

    @Autowired
    private OrderExpressDao orderExpressDao;

    @Autowired
    private OrderExpressDetailsDao orderExpressDetailsDao;

    @Autowired
    private BaseUserDao baseUserDao;

    @Autowired
    private ShoesInfoDao shoesInfoDao;


    /**
     * 提交订单
     *
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer submitOrder(List<Map<String, Integer>> stockChangeList, List<Integer> shoppingCartSeqList, OrderEntity orderEntity,
                               List<OrderProductsEntity> orderProductsList) {

        //1.修改库存
        for (Map<String, Integer> map : stockChangeList) {
            changeShoesDataStock(map.get("shoesDataSeq"), map.get("changNum"));
        }

        //2.删除购物车
        if (shoppingCartSeqList.size() > 0) {
            shoppingCartDao.deleteBatchIds(shoppingCartSeqList);
        }

        //3.新增订单
        orderDao.insert(orderEntity);

        //4.新增订单关联产品
        for (OrderProductsEntity orderProductsEntity : orderProductsList) {
            orderProductsEntity.setOrderSeq(orderEntity.getSeq());
            orderProductsDao.insert(orderProductsEntity);
        }

        return orderEntity.getSeq();
    }


    /**
     * 修改库存（注意：本类内部其他方法调用此方法时，必须添加事物，否则导致本方法的事物失效）
     */
    //TODO 后台管理端修改库存时，没有同步，可能会出问题
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized void changeShoesDataStock(Integer shoesDataSeq, Integer changNum) {
        /** 2018.08.06需求，库存小于0仍然可以提交订单，所以去掉了库存判断 **/
//			ShoesDataEntity shoesDataEntity = shoesDataDao.selectById(shoesDataSeq);
//			//判断库存是否足够
//			if(shoesDataEntity.getStock() + changNum >= 0) {
        shoesDataDao.changeShoesDataStock(shoesDataSeq, changNum);
//				shoesDataEntity = shoesDataDao.selectById(shoesDataSeq);
//				if(shoesDataEntity.getStock() < 0) { //防止同步修改将库存改成负数的情况
//					throw new RuntimeException("库存不足");
//				}
//			} else {
//				throw new RuntimeException("库存不足");
//			}
    }


    /**
     * 根据订单编号，查询订单的购买的商品列表
     */
    @Override
    public List<OrderProductsEntity> getOrderProductsListByOrderSeq(Integer orderSeq) {
        Map<String, Object> columnMap = new HashMap<String, Object>();
        columnMap.put("Order_Seq", orderSeq);
        return orderProductsDao.selectByMap(columnMap);

    }


    /**
     * 取消订单
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void cancelOrder(Integer orderSeq, String remark, List<Map<String, Integer>> stockChangeList) {

        //修改订单状态
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setSeq(orderSeq);
        orderEntity.setOrderStatus(OrderStatusEnum.ORDSTATUS_SEVEN.getValue());
        if (orderDao.selectOne(orderEntity) == null) {
            orderEntity.setRemark(remark);
            orderDao.updateById(orderEntity);
        } else {
            throw new RuntimeException();
        }

        //修改库存
        for (Map<String, Integer> map : stockChangeList) {
            changeShoesDataStock(map.get("shoesDataSeq"), map.get("changNum"));
        }

    }


    /**
     * 删除订单
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteOrder(Integer orderSeq) {
        //删除订单
        orderDao.deleteById(orderSeq);

        //删除订单关联产品
        Map<String, Object> columnMap = new HashMap<String, Object>();
        columnMap.put("Order_Seq", orderSeq);
        columnMap.put("Del", 0);
        orderProductsDao.deleteByMap(columnMap);

    }


    /**
     * 修改订单留言、要求到货日期
     */
    @Override
    public void updateOrderSuggestion(Integer orderSeq, Date requireTime, String suggestion) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setSeq(orderSeq);
        if (requireTime != null) {
            orderEntity.setRequireTime(requireTime);
        }
        if (suggestion != null) {
            orderEntity.setSuggestion(suggestion);
        }
        orderDao.updateById(orderEntity);
    }


    /**
     * 订单确认收货
     */
    @Override
    public void orderConfirmReceived(Integer orderSeq) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setSeq(orderSeq);
        orderEntity.setOrderStatus(OrderStatusEnum.ORDSTATUS_SIX.getValue());
        orderEntity.setReceiveTime(new Date());
        orderDao.updateById(orderEntity);
    }


    /**
     * 根据厂家编号，获取所有该厂家的待发货订单序号
     */
    @Override
    public List<Object> getOrderSeqByCompanySeq(Integer companySeq) {
        Wrapper<OrderEntity> wrapper = new EntityWrapper<OrderEntity>();
        wrapper.setSqlSelect("Seq").where("Company_Seq = {0} AND OrderStatus = 2 AND IsSplit = 0", companySeq);
        return orderDao.selectObjs(wrapper);
    }


    /**
     * 获取订单汇总数据
     */
    @Override
    public List<Map<String, Object>> getOrderSummary(List<Object> orderSeqlist, Integer start, Integer num) {
        String orderSeq = Joiner.on(",").join(orderSeqlist);
        return orderDao.getOrderSummary(orderSeq, start - 1, num);
    }


    /**
     * 获取订单汇总数据中每个订单的购买量
     */
    @Override
    public List<Map<String, Object>> getSummaryDetailList(List<Object> orderSeqlist, Integer shoesDataSeq) {
        String orderSeq = Joiner.on(",").join(orderSeqlist);
        return orderDao.getSummaryDetailList(orderSeq, shoesDataSeq);
    }


    /**
     * 根据公司编号获取订单列表（厂家）
     */
    @Override
    public List<OrderEntity> getOrderListByCompanySeq(Integer companySeq, Integer orderStatus, Integer start, Integer num) {
        return orderDao.getOrderListByCompanySeq(companySeq, orderStatus, start - 1, num);
    }


    /**
     * 根据用户编号获取订单列表（品牌方）
     */
    @Override
    public List<OrderEntity> getOrderListByUserSeq(Integer userSeq, Integer orderStatus, Integer start, Integer num) {
        return orderDao.getOrderListByUserSeq(userSeq, orderStatus, start - 1, num);
    }


    /**
     * 根据shoesDateSeq获取鞋子基本信息实体
     */
    @Override
    public GoodsShoesEntity getGoodsShoesByShoesDateSeq(Integer shoesDataSeq) {
        ShoesDataEntity shoesDataEntity = shoesDataDao.selectById(shoesDataSeq);
        return goodsShoesDao.selectById(shoesDataEntity.getShoesSeq());
    }


    /**
     * 根据seq获取订单
     */
    @Override
    public OrderEntity getOrderBySeq(Integer orderSeq) {
        return orderDao.selectById(orderSeq);
    }


    /**
     * 确认订单
     */
    @Override
    public void orderConfirmed(Integer orderSeq) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setSeq(orderSeq);
        orderEntity.setOrderStatus(OrderStatusEnum.ORDSTATUS_ONE.getValue());
        orderEntity.setConfirmTime(new Date());
        orderDao.updateById(orderEntity);
    }


    /**
     * 订单确认支付
     */
    @Override
    public void orderConfirmedPay(Integer orderSeq, BigDecimal paid, Integer companySeq) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setSeq(orderSeq);
        orderEntity.setOrderStatus(OrderStatusEnum.ORDSTATUS_TWO.getValue());
        orderEntity.setPaid(paid);
        orderEntity.setPaymentTime(new Date());
        orderDao.updateById(orderEntity);
    }


    /**
     * 订单入库
     */
    @Override
    public void orderStore(Integer orderSeq) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setSeq(orderSeq);
        orderEntity.setOrderStatus(OrderStatusEnum.ORDSTATUS_THREE.getValue());
        orderEntity.setStoreTime(new Date());
        orderDao.updateById(orderEntity);
    }


    /**
     * 获取所有快递公司信息
     */
    @Override
    public List<Map<String, Object>> getExpressCompanyList() {
        Wrapper<ExpressCompanyEntity> wrapper = new EntityWrapper<ExpressCompanyEntity>();
        wrapper.setSqlSelect("Seq AS seq, Code AS code, Name AS name");
        return expressCompanyDao.selectMaps(wrapper);
    }


    /**
     * 修改订单已付金额
     */
    @Override
    public void updateOrderPaid(Integer orderSeq, BigDecimal paid) {
        orderDao.updateOrderPaid(orderSeq, paid);
    }


    /**
     * 根据seq获取订单商品对象
     */
    @Override
    public OrderProductsEntity getOrderProductsBySeq(Integer orderProductsSeq) {
        return orderProductsDao.selectById(orderProductsSeq);
    }


    /**
     * 部分发货
     *
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer orderDeliverGoods(OrderEntity order, List<OrderProductsEntity> orderProductsList,
                                     OrderExpressEntity orderExpressEntity, List<OrderExpressDetailsEntity> orderExpressDetailsList) {


        //2.修改多条订单商品已发货量
        for (OrderProductsEntity orderProducts : orderProductsList) {
            orderProductsDao.updateById(orderProducts);
        }

        //1.判断并修改订单状态
        order.setOrderStatus(OrderStatusEnum.ORDSTATUS_FIVE.getValue());
        Wrapper<OrderProductsEntity> wrapper = new EntityWrapper<OrderProductsEntity>();
        wrapper.where("Order_Seq = {0}", order.getSeq());
        List<OrderProductsEntity> orderProductsEntityList = orderProductsDao.selectList(wrapper);
        for (OrderProductsEntity orderProductsEntity : orderProductsEntityList) {
            if (orderProductsEntity.getBuyCount() - orderProductsEntity.getDeliverNum() > 0) {
                //部分发货，订单状态改为4，自动收货时间置空
                order.setOrderStatus(OrderStatusEnum.ORDSTATUS_FOUR.getValue());
                order.setReceiveTime(null);
                break;
            }
        }
        orderDao.updateById(order);

        //3.新增一条快递记录
        orderExpressDao.insert(orderExpressEntity);

        //4.新增多条快递商品发货记录
        for (OrderExpressDetailsEntity orderExpressDetailsEntity : orderExpressDetailsList) {
            orderExpressDetailsEntity.setOrderExpressSeq(orderExpressEntity.getSeq());
            orderExpressDetailsDao.insert(orderExpressDetailsEntity);
        }

        return order.getOrderStatus();

    }


    /**
     * 根据seq获取订单指定字段map对象
     */
    @Override
    public Map<String, Object> getOrderMapBySeq(Integer orderSeq) {
        Wrapper<OrderEntity> wrapper = new EntityWrapper<OrderEntity>();
        wrapper.setSqlSelect("Seq AS orderSeq, User_Seq AS userSeq, OrderNum AS orderNum, OrderPrice AS orderPrice, Paid AS paid, OrderStatus AS orderStatus, "
                + "Company_Seq AS companySeq, ReceiverName AS receiverName, Telephone AS telephone, FullAddress AS fullAddress, "
                + "InputTime AS inputTime, RequireTime AS requireTime, ConfirmTime AS confirmTime, PaymentTime AS paymentTime, "
                + "StoreTime AS storeTime, DeliverTime AS deliverTime, ReceiveTime AS receiveTime, Remark AS remark, Suggestion AS suggestion");
        wrapper.where("Seq = {0}", orderSeq);
        return orderDao.selectMaps(wrapper).get(0);
    }


    /**
     * 根据userSeq查询用户指定字段map对象
     */
    @Override
    public Map<String, Object> getUserMapByUserSeq(Integer userSeq) {
        Wrapper<BaseUserEntity> wrapper = new EntityWrapper<BaseUserEntity>();
        wrapper.setSqlSelect("Seq AS userSeq, UserName AS userName, Telephone AS telephone");
        wrapper.where("Seq = {0}", userSeq);
        return baseUserDao.selectMaps(wrapper).get(0);
    }


    /**
     * 根据seq获取订单购买商品列表
     */
    @Override
    public List<Map<String, Object>> getOrderProductsList(Integer orderSeq) {
        return orderDao.getOrderProductsList(orderSeq);
    }


    /**
     * 根据seq获取订单快递列表
     */
    @Override
    public List<Map<String, Object>> getOrderExpressList(Integer orderSeq) {
        Wrapper<OrderExpressEntity> wrapper = new EntityWrapper<OrderExpressEntity>();
        wrapper.setSqlSelect("Seq AS orderExpressSeq, ExpressCompany_Seq AS expressCompanySeq, ExpressNo AS expressNo, ExpressImg AS expressImg, InputTime AS inputTime");
        wrapper.where("Order_Seq = {0}", orderSeq).orderBy("InputTime DESC, Seq DESC");
        return orderExpressDao.selectMaps(wrapper);
    }


    /**
     * 根据seq获取快递公司实体
     */
    @Override
    public ExpressCompanyEntity getExpressCompanyBySeq(Integer expressCompanySeq) {
        return expressCompanyDao.selectById(expressCompanySeq);
    }


    /**
     * 根据orderExpressSeq获取快递发货详情列表
     */
    @Override
    public List<Map<String, Object>> getOrderExpressDetailsList(Integer orderExpressSeq) {
        Wrapper<OrderExpressDetailsEntity> wrapper = new EntityWrapper<OrderExpressDetailsEntity>();
        wrapper.setSqlSelect("Seq AS orderExpressDetailsSeq, OrderProducts_Seq AS orderProductsSeq, Num AS num");
        wrapper.where("OrderExpress_Seq = {0}", orderExpressSeq);
        return orderExpressDetailsDao.selectMaps(wrapper);
    }


    /**
     * 根据shoesDateSeq获取shoesInfo实体
     */
    @Override
    public ShoesInfoEntity getShoesInfoByShoesDateSeq(Integer shoesDataSeq) {
        ShoesDataEntity shoesDataEntity = shoesDataDao.selectById(shoesDataSeq);
        ShoesInfoEntity shoesInfoEntity = new ShoesInfoEntity();
        shoesInfoEntity.setShoesSeq(shoesDataEntity.getShoesSeq());
        return shoesInfoDao.selectOne(shoesInfoEntity);
    }


    /**
     * 修改订单
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeOrder(OrderEntity orderChangeEntity, List<OrderProductsEntity> orderProductsChangeList,
                            List<Map<String, Integer>> stockChangeList) {

        //1.修改订单价格
        orderDao.updateById(orderChangeEntity);

        //2.修改订单关联产品（或删除）
        for (OrderProductsEntity orderProductsEntity : orderProductsChangeList) {
            orderProductsDao.updateById(orderProductsEntity);
        }

        //3.修改库存
        for (Map<String, Integer> map : stockChangeList) {
            changeShoesDataStock(map.get("shoesDataSeq"), map.get("changNum"));
        }
    }

}
