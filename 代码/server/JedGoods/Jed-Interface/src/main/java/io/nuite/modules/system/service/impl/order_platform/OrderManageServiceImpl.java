package io.nuite.modules.system.service.impl.order_platform;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.base.Joiner;

import io.nuite.common.exception.RRException;
import io.nuite.common.utils.*;
import io.nuite.modules.order_platform_app.dao.*;
import io.nuite.modules.order_platform_app.entity.*;
import io.nuite.modules.order_platform_app.service.BaseService;
import io.nuite.modules.order_platform_app.service.OrderService;
import io.nuite.modules.order_platform_app.service.PushRecordService;
import io.nuite.modules.order_platform_app.utils.JPushUtils;
import io.nuite.modules.order_platform_app.utils.OrderStatusEnum;
import io.nuite.modules.sr_base.dao.BaseBrandDao;
import io.nuite.modules.sr_base.dao.BaseCompanyDao;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.dao.GoodsBrandDao;
import io.nuite.modules.sr_base.dao.GoodsCategoryDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.BaseBrandEntity;
import io.nuite.modules.sr_base.entity.BaseCompanyEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsBrandEntity;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.dao.order_platform.OrderManageDao;
import io.nuite.modules.system.from.PartShippedForm;
import io.nuite.modules.system.from.ShippedProductForm;
import io.nuite.modules.system.service.order_platform.OrderManageService;
import io.nuite.modules.system.service.order_platform.SplitOrderModelService;
import io.nuite.modules.system.socketio.MessageEventHandler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderManageServiceImpl implements OrderManageService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderManageDao orderManageDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ExpressCompanyDao expressCompanyDao;

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private BaseUserDao baseUserDao;

    @Autowired
    private BaseBrandDao baseBrandDao;

    @Autowired
    private BaseCompanyDao baseCompanyDao;

    @Autowired
    private OrderExpressDao orderExpressDao;

    @Autowired
    private OrderProductsDao orderProductsDao;

    @Autowired
    private OrderExpressDetailsDao orderExpressDetailsDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MessageEventHandler messageEventHandler;

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private JPushUtils jPushUtils;

    @Autowired
    private PushRecordService pushRecordService;

    @Autowired
    protected BaseService baseService;

    @Autowired
    private ShoesDataDao shoesDataDao;
    
    @Autowired
    private GoodsShoesDao goodsShoesDao;
    
    @Autowired
    private GoodsCategoryDao goodsCategoryDao;
    
    @Autowired
    private GoodsBrandDao goodsBrandDao;
    
    @Autowired
    private SplitOrderModelService splitOrderModelService;
    

    /**
     * 订单列表
     */
    @Override
    public PageUtils orderlist(BaseUserEntity baseUserEntity, Map<String, Object> params) {
        List<OrderEntity> orderList;
        int totalCount = 0;
        Object attachTypeObj = params.get("attachType");
        Object attachSeqObj = params.get("attachSeq");
        Object userSeq = params.get("userSeq");
        List<Integer> userSeqList = new ArrayList<Integer>();
        if (userSeq != null && StringUtils.isNotBlank(userSeq.toString())) {
            userSeqList.add(Integer.parseInt(userSeq.toString()));
        } else if (attachSeqObj != null && attachTypeObj != null && StringUtils.isNotBlank(attachTypeObj.toString())
                && StringUtils.isNotBlank(attachSeqObj.toString())) {
            try {
                Integer attachType = Integer.parseInt(params.get("attachType").toString());
                Integer attachSeq = Integer.parseInt(params.get("attachSeq").toString());
                userSeqList = userListByKeywords(attachType, attachSeq);
            } catch (NumberFormatException numberException) {
                logger.debug("attachType || attachSeq", numberException);
            }
        }
        Object statusObj = params.get("orderStatus");
        Integer orderStatus = null;
        if (statusObj != null && !statusObj.toString().equals("")) {
            try {
                orderStatus = Integer.parseInt(statusObj.toString());
            } catch (NumberFormatException numberException) {
                logger.debug("orderStatus", numberException);
            }
        }
        // 在列表页中有列表显示的时候查询同等条件下没有分页的情况下数据量
        totalCount = orderManageDao.getTotalCount(userSeqList, orderStatus, params.get("keywords").toString(),
                params.get("startTime").toString(), params.get("endTime").toString(), baseUserEntity.getCompanySeq());
        // 根据公司的Seq查询所有的订单
        orderList = orderManageDao.orderList(userSeqList, orderStatus, params.get("keywords").toString(),
                params.get("startTime").toString(), params.get("endTime").toString(),
                baseUserEntity.getCompanySeq(), Integer.parseInt(params.get("page").toString()),
                Integer.parseInt(params.get("limit").toString()));
        
        // 获取状态名 和 鞋子总数量
        List<OrderProductsEntity> orderProductsList;
        int shoesTotalNum;
        for (OrderEntity orderEntity : orderList) {
        	//状态中文名
            orderEntity.setStatusName(OrderStatusEnum.get(orderEntity.getOrderStatus()).getFactoryName());

            orderProductsList = orderService.getOrderProductsListByOrderSeq(orderEntity.getSeq());
            shoesTotalNum = 0;
            for (OrderProductsEntity orderProductsEntity : orderProductsList) {
                shoesTotalNum += orderProductsEntity.getBuyCount();
            }
            //订单中鞋子总数
            orderEntity.setSpecies(shoesTotalNum);
        }

        return new PageUtils(orderList, totalCount, Integer.parseInt(params.get("limit").toString()),
                Integer.parseInt(params.get("page").toString()));
    }


    /**
     * 订单所有货号
     */
    @Override
    public List<Map<String, Object>> getOrderGoodIds(Integer orderSeq) {
        List<Map<String, Object>> list = orderManageDao.getOrderGoodIds(orderSeq);
        return list;
    }


    /**
     * 每个货号对应商品列表
     */
    @Override
    public List<Map<String, Object>> getGoodsIdProducts(Integer orderSeq, String goodId) {
        List<Map<String, Object>> list = orderManageDao.getOrderProductsList(orderSeq);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            if (!map.get("goodId").equals(goodId)) {
                list.remove(i);
                i--;
                continue;
            }
            map.put("img1", configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/"
                    + configUtils.getGoodsShoes() + "/" + map.get("goodId") + "/" + map.get("img1"));
        }
        return list;
    }


    /**
     * 订单商品列表
     */
    @Override
    public List<Map<String, Object>> getOrderProductsList(Integer orderSeq) {
        List<Map<String, Object>> list = orderManageDao.getOrderProductsList(orderSeq);
        for (Map<String, Object> map : list) {
            map.put("img1", configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/"
                    + configUtils.getGoodsShoes() + "/" + map.get("goodId") + "/" + map.get("img1"));
        }
        return list;
    }


    /**
     * 直接根据类型seq（公司/代理） 和 公司/代理的seq 返回对应其下的用户SeqList
     */
    private List<Integer> userListByKeywords(Integer attachType, Integer attachSeq) {
        List<Integer> list = new ArrayList<Integer>();
        if (attachType != null && attachSeq != null) {
            List<BaseUserEntity> userList = baseUserDao.selectList(new EntityWrapper<BaseUserEntity>().eq("Del", 0)
                    .eq("AttachType", attachType).eq("Attach_Seq", attachSeq));
            list = new ArrayList<>();
            for (BaseUserEntity baseUserEntity : userList) {
                list.add(baseUserEntity.getSeq());
            }
        }
        return list;
    }

    @Override
    public OrderEntity getOrderBySeq(Integer seq) {
        return orderDao.selectById(seq);
    }


    /**
     * 删除订单
     */
    @Override
    public void deleteOrder(Integer seq) {
        orderManageDao.deleteById(seq);
    }


    /**
     * 接单
     */
    @Override
    public void confirmOrder(Integer orderSeq, Date requireTime, BaseUserEntity loginUser) {
        OrderEntity newOrderEntity = new OrderEntity();
        newOrderEntity.setSeq(orderSeq);
        newOrderEntity.setOrderStatus(1);
        newOrderEntity.setConfirmTime(new Date());
        newOrderEntity.setRequireTime(requireTime);
        orderDao.updateById(newOrderEntity);

        OrderEntity orderEntity = getOrderBySeq(orderSeq);
        sendOrderConfirmPush(orderEntity, loginUser);
    }


    /**
     * 入库
     */
    @Override
    public R updateOrderStatus(Integer orderStatus, Integer seq, BaseUserEntity loginUser) {
        OrderEntity orderEntity = getOrderBySeq(seq);
        if (orderEntity == null) {
            return R.error("订单不存在！");
        }
        Integer currentStatus = orderEntity.getOrderStatus();

        OrderEntity newOrderEntity = new OrderEntity();
        newOrderEntity.setSeq(orderEntity.getSeq());
        // 入库
        if (currentStatus == 2 && orderStatus == 3) {
            newOrderEntity.setOrderStatus(orderStatus);
            newOrderEntity.setStoreTime(new Date());
            orderDao.updateById(newOrderEntity);
            sendOrderStorePush(orderEntity, loginUser);
        } else {
            return R.error("订单状态已变更，请刷新后再试");
        }
        return R.ok("操作成功");
    }


    /**
     * 修改订单总价格
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeOrderTotalPrice(Integer orderSeq, BigDecimal orderTotalPrice) {
        //原价格
        OrderEntity orderEntity = orderDao.selectById(orderSeq);
        BigDecimal orderPrice = orderEntity.getOrderPrice();

        //1.修改订单价格orderPrice
        orderEntity = new OrderEntity();
        orderEntity.setSeq(orderSeq);
        orderEntity.setOrderPrice(orderTotalPrice);
        orderDao.updateById(orderEntity);

        //2.按比例修改每个订单商品的价格
        //计算比例
        BigDecimal percent = orderTotalPrice.divide(orderPrice, 10, BigDecimal.ROUND_HALF_UP);

        Wrapper<OrderProductsEntity> wrapper = new EntityWrapper<OrderProductsEntity>();
        wrapper.where("Order_Seq = {0}", orderSeq);
        List<OrderProductsEntity> orderProductsList = orderProductsDao.selectList(wrapper);

        OrderProductsEntity orderProducts;
        for (OrderProductsEntity orderProductsEntity : orderProductsList) {
            orderProducts = new OrderProductsEntity();
            orderProducts.setSeq(orderProductsEntity.getSeq());
            orderProducts.setProductPrice(orderProductsEntity.getProductPrice().multiply(percent));
            orderProductsDao.updateById(orderProducts);
        }


    }


    /**
     * 修改某些商品单价
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeProductsPrice(Integer orderSeq, String seqPriceList) {
        JSONArray js = JSONArray.parseArray(seqPriceList);
        BigDecimal changeTotalPrice = BigDecimal.valueOf(0); //要修改的总价
        OrderProductsEntity orderProductsEntity;
        for (int i = 0; i < js.size(); i++) {
            JSONObject o = JSONObject.parseObject(js.getString(i));
            int productSeq = o.getIntValue("productSeq");
            BigDecimal changePrice = o.getBigDecimal("changePrice");

            if (changePrice != null) {
                //商品原价格
                orderProductsEntity = orderProductsDao.selectById(productSeq);
                BigDecimal productPrice = orderProductsEntity.getProductPrice();
                int buyCount = orderProductsEntity.getBuyCount();
                changeTotalPrice = changeTotalPrice.add(changePrice.subtract(productPrice).multiply(BigDecimal.valueOf(buyCount)));

                //1.修改商品单价
                orderProductsEntity = new OrderProductsEntity();
                orderProductsEntity.setSeq(productSeq);
                orderProductsEntity.setProductPrice(changePrice);
                orderProductsDao.updateById(orderProductsEntity);
            }
        }

        //2.修改订单总价
        //订单原价格
        OrderEntity orderEntity = orderDao.selectById(orderSeq);
        BigDecimal orderPrice = orderEntity.getOrderPrice();

        orderEntity = new OrderEntity();
        orderEntity.setSeq(orderSeq);
        orderEntity.setOrderPrice(orderPrice.add(changeTotalPrice));
        orderDao.updateById(orderEntity);

    }


    /**
     * 审核或修改已付金额
     */
    @Override
    public R payOrder(Integer seq, BigDecimal price, BaseUserEntity loginUser) {
        OrderEntity orderEntity = getOrderBySeq(seq);
        if (orderEntity == null) {
            return R.error("订单不存在！");
        }
        if (orderEntity.getOrderStatus() == 0 || orderEntity.getOrderStatus() == 7) {
            return R.error("当前状态不可以付款！");
        }

        OrderEntity newOrderEntity = new OrderEntity();
        newOrderEntity.setSeq(seq);
        // 如果订单状态为1，即为审核
        if (orderEntity.getOrderStatus() == 1) {
            newOrderEntity.setOrderStatus(2);
            newOrderEntity.setPaid(price);
            newOrderEntity.setPaymentTime(new Date());
            orderDao.updateById(newOrderEntity);
            sendOrderPayPush(orderEntity, price, loginUser);
            
        } else { // 其他订单状态为修改已付金额，与本次金额相加
            newOrderEntity.setPaid(orderEntity.getPaid().add(price));
            orderDao.updateById(newOrderEntity);
        }
        return R.ok("操作成功");
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

    @Override
    public List<BaseCompanyEntity> attachCompany(BaseUserEntity user) {
        return baseCompanyDao
                .selectList(new EntityWrapper<BaseCompanyEntity>().eq("Del", 0).eq("ParentSeq", user.getSeq()));
    }

    @Override
    public List<BaseBrandEntity> attachAgent(BaseUserEntity user) {
        return baseBrandDao
                .selectList(new EntityWrapper<BaseBrandEntity>().eq("Del", 0).eq("Brand_Seq", user.getBrandSeq()));
    }

    /**
     * 部分发货
     */
    @Override
    @Transactional
    public void partShipped(PartShippedForm partShippedForm, BaseUserEntity loginUser) throws IOException {
        String orderPicPath = FileUtils.getWebAppsPath() + configUtils.getPictureBaseUploadProject() + File.separator
                + configUtils.getOrderPlatformApp().getOrderPlatformDir() + File.separator
                + configUtils.getOrderPlatformApp().getOrderExpress() + File.separator + partShippedForm.getOrderSeq()
                + File.separator;

        Integer orderSeq = partShippedForm.getOrderSeq();

        OrderExpressEntity orderExpressEntity = new OrderExpressEntity();
        if (partShippedForm.getExpressFile() != null) {
            String fileName = FileUtils.upLoadFile(orderPicPath, null, partShippedForm.getExpressFile());
            orderExpressEntity.setExpressImg(fileName);
        }
        orderExpressEntity.setExpressCompanySeq(partShippedForm.getExpressCompanySeq());
        orderExpressEntity.setExpressNo(partShippedForm.getExpressNo());
        orderExpressEntity.setOrderSeq(orderSeq);
        orderExpressDao.insert(orderExpressEntity);

        Integer orderExpressSeq = orderExpressEntity.getSeq();

        int totalNum = 0; //发货总量
        for (ShippedProductForm shippedProductForm : partShippedForm.getList()) {
            Integer orderProductSeq = shippedProductForm.getSeq();
            OrderProductsEntity orderProductsEntity = orderProductsDao.selectById(orderProductSeq);
            if (orderProductsEntity == null)
                continue;
            Integer deliverNum = orderProductsEntity.getDeliverNum();
            Integer buyCount = orderProductsEntity.getBuyCount();
            Integer shoesDataSeq = orderProductsEntity.getShoesDataSeq();
            int deliverNumber = deliverNum != null ? deliverNum : 0;
            int buyNumber = buyCount != null ? buyCount : 0;

            int shipNum = shippedProductForm.getShipNum();
            totalNum = totalNum + shipNum;
            if (shipNum + deliverNumber > buyNumber) {
                throw new RRException("发货数量超过购买量");
            }
            OrderExpressDetailsEntity orderExpressDetailsEntity = new OrderExpressDetailsEntity();
            orderExpressDetailsEntity.setOrderExpressSeq(orderExpressSeq);
            orderExpressDetailsEntity.setOrderProductsSeq(orderProductSeq);
            orderExpressDetailsEntity.setShoesDataSeq(shoesDataSeq);
            orderExpressDetailsEntity.setNum(shipNum);
            orderExpressDetailsEntity.setOrderSeq(orderSeq);
            orderExpressDetailsDao.insert(orderExpressDetailsEntity);

            OrderProductsEntity deliverUpEntity = new OrderProductsEntity();
            deliverUpEntity.setSeq(orderProductSeq);
            deliverUpEntity.setDeliverNum(deliverNumber + shipNum);
            orderProductsDao.updateById(deliverUpEntity);
        }

        List<OrderProductsEntity> list = orderProductsDao
                .selectByMap(new MapUtils().put("Order_Seq", orderSeq).put("Del", 0));
        boolean allShipped = true;
        for (OrderProductsEntity orderProductsEntity : list) {
            Integer buyCount = orderProductsEntity.getBuyCount();
            Integer deliverNum = orderProductsEntity.getDeliverNum();
            int deliverNumber = deliverNum != null ? deliverNum : 0;
            int buyNumber = buyCount != null ? buyCount : 0;
            if (deliverNumber < buyNumber) {
                allShipped = false;
                break;
            }
        }

        // 时间处理
        Calendar calendar = Calendar.getInstance();
        Date nowDate = calendar.getTime();
        calendar.add(Calendar.DATE, 10);
        Date autoReceiveDate = calendar.getTime();

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setSeq(orderSeq);
        orderEntity.setDeliverTime(nowDate);
        if (allShipped) {
            orderEntity.setOrderStatus(OrderStatusEnum.ORDSTATUS_FIVE.getValue());
            orderEntity.setReceiveTime(autoReceiveDate);
        } else {
            orderEntity.setOrderStatus(OrderStatusEnum.ORDSTATUS_FOUR.getValue());
        }
        orderDao.updateById(orderEntity);
        sendOrderShippedPush(getOrderBySeq(orderSeq), totalNum, loginUser);
    }



    /**
     * 根据attachType，attachSeq获取所有用户
     */
    @Override
    public List<Map<String, Object>> getUserList(Integer companySeq, Integer brandSeq, Integer attachType, Integer attachSeq) {
        Wrapper<BaseUserEntity> wrapper = new EntityWrapper<BaseUserEntity>();
        wrapper.setSqlSelect("Seq AS seq, AccountName AS accountName");
        if (attachType != null && attachType == 1) {
            //工厂订货方账号
            wrapper.where("Company_Seq = {0} AND Brand_Seq = {1} AND AttachType = 1 AND SaleType IN (2,3,4)", companySeq, brandSeq);
        } else if (attachType != null && attachType == 2 && attachSeq != null) {
            //某一分公司订货方账号
            wrapper.where("Company_Seq = {0} AND Brand_Seq = {1} AND AttachType = 2 AND Attach_Seq = {2} AND SaleType IN (2,3,4)", companySeq, brandSeq, attachSeq);
        } else if (attachType != null && attachType == 3 && attachSeq != null) {
            //代理商订货方账号
            wrapper.where("Company_Seq = {0} AND Brand_Seq = {1} AND AttachType = 3 AND Attach_Seq = {2} AND SaleType IN (2,3)", companySeq, brandSeq, attachSeq);
        } else {
            //全部订货方账号
            wrapper.where("Company_Seq = {0} AND Brand_Seq = {1} AND AttachType IN (1,2,3) AND SaleType IN (2,3,4)", companySeq, brandSeq);
        }
        return baseUserDao.selectMaps(wrapper);
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
            orderService.changeShoesDataStock(map.get("shoesDataSeq"), map.get("changNum"));
        }

    }

    /**
     * 延长收货
     */
    @Override
    public void changeReceiveTime(Integer seq, Date receiveTime) {
        //修改订单收货时间
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setSeq(seq);
        orderEntity.setReceiveTime(receiveTime);
        orderDao.updateById(orderEntity);
    }


    //发送接单推送
    private void sendOrderConfirmPush(OrderEntity orderEntity, BaseUserEntity loginUser) {
        //发送手机推送消息
        BaseUserEntity submitOrderUser = null;  //提交订单人
        List<BaseUserEntity> factoryUserList = null;  //工厂所有财务
        try {
            //给用户推送
            submitOrderUser = baseUserService.getBaseUserBySeq(orderEntity.getUserSeq());
            List<String> aliasList = new ArrayList<String>();
            aliasList.add(submitOrderUser.getAccountName());
            jPushUtils.sendPush(aliasList, "您的订单：" + orderEntity.getOrderNum() + "已被接单，请及时与财务交洽付款！", "1");
            //给财务推送
            factoryUserList = baseService.getUserByPermission(loginUser, new String[]{PermissionKeys.ORDER_CHECK});
            aliasList = new ArrayList<String>();
            for (BaseUserEntity factoryUser : factoryUserList) {
                aliasList.add(factoryUser.getAccountName());
            }
            jPushUtils.sendPush(aliasList, "您有新订单款项需审核，请及时处理！订单号：" + orderEntity.getOrderNum(), "1");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("确认订单后，发送手机推送消息失败" + e.getMessage(), e);
        }

        //发送网页推送消息
        try {
            //给财务推送
            messageEventHandler.sendHandleOrderEvent(loginUser.getCompanySeq().toString(), PermissionKeys.ORDER_CHECK, "您有新订单款项需审核，请及时处理！订单号：" + orderEntity.getOrderNum(), orderEntity.getSeq(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("确认订单后，发送网页推送消息失败" + e.getMessage(), e);
        }

        //保存推送消息
        try {
            //保存用户推送
            pushRecordService.addPushRecord(loginUser.getSeq(), submitOrderUser.getSeq(), submitOrderUser.getAccountName(), 1, orderEntity.getSeq(), "您的订单：" + orderEntity.getOrderNum() + "已被接单，请及时与财务交洽付款！");
            //保存财务推送
            for (BaseUserEntity factoryUser : factoryUserList) {
                pushRecordService.addPushRecord(loginUser.getSeq(), factoryUser.getSeq(), factoryUser.getAccountName(), 1, orderEntity.getSeq(), "您有新订单款项需审核，请及时处理！订单号：" + orderEntity.getOrderNum());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("确认订单后，保存推送消息失败" + e.getMessage(), e);
        }
    }


    //发送入库推送消息
    private void sendOrderStorePush(OrderEntity orderEntity, BaseUserEntity loginUser) {
        //发送手机推送消息
        BaseUserEntity submitOrderUser = null;  //提交订单人
        List<BaseUserEntity> factoryUserList = null;  //工厂所有发货员
        try {
            //给用户推送
            submitOrderUser = baseUserService.getBaseUserBySeq(orderEntity.getUserSeq());
            List<String> aliasList = new ArrayList<String>();
            aliasList.add(submitOrderUser.getAccountName());
            jPushUtils.sendPush(aliasList, "您的订单：" + orderEntity.getOrderNum() + "已被商家入库，即将准备发货", "3");

            //给发货员推送
            factoryUserList = baseService.getUserByPermission(loginUser, new String[]{PermissionKeys.ORDER_DELIVER});
            aliasList = new ArrayList<String>();
            for (BaseUserEntity factoryUser : factoryUserList) {
                aliasList.add(factoryUser.getAccountName());
            }
            jPushUtils.sendPush(aliasList, "订单：" + orderEntity.getOrderNum() + "已入库，请及时发货", "3");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("订单入库后，发送手机推送消息失败" + e.getMessage(), e);
        }

        //发送网页推送消息
        try {
            //给发货员推送
            messageEventHandler.sendHandleOrderEvent(loginUser.getCompanySeq().toString(), PermissionKeys.ORDER_DELIVER, "订单：" + orderEntity.getOrderNum() + "已入库，请及时发货", orderEntity.getSeq(), 3);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("订单入库后，发送网页推送消息失败" + e.getMessage(), e);
        }

        //保存推送消息
        try {
            //保存用户推送
            pushRecordService.addPushRecord(loginUser.getSeq(), submitOrderUser.getSeq(), submitOrderUser.getAccountName(), 1, orderEntity.getSeq(), "您的订单：" + orderEntity.getOrderNum() + "已被商家入库，即将准备发货");
            //保存发货员推送
            for (BaseUserEntity factoryUser : factoryUserList) {
                pushRecordService.addPushRecord(loginUser.getSeq(), factoryUser.getSeq(), factoryUser.getAccountName(), 1, orderEntity.getSeq(), "订单：" + orderEntity.getOrderNum() + "已入库，请及时发货");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("订单入库后，保存推送消息失败" + e.getMessage(), e);
        }
    }

    //发送审核推送消息
    private void sendOrderPayPush(OrderEntity orderEntity, BigDecimal paid, BaseUserEntity loginUser) {
        //推送处理
        try {
            //给用户推送的内容
            String userMessage = "";
            String messageType = "";
            if (paid.compareTo(BigDecimal.valueOf(0)) == 0) { //付款为0，欠款
                userMessage = "您尚未支付款项，请与财务交洽好，以免影响出库发货！";
                messageType = "21";
            } else if (paid.compareTo(orderEntity.getOrderPrice()) < 0) { //部分款
                userMessage = "您已支付部分款项，请与财务交洽好，以免影响出库发货！";
                messageType = "22";
            } else if (paid.compareTo(orderEntity.getOrderPrice()) == 0) { //全款
                userMessage = "您已成功付款，请耐心等待出库发货哦！如有疑问请及时联系客服！";
                messageType = "23";
            }

            BaseUserEntity submitOrderUser = null;  //提交订单人
            List<BaseUserEntity> factoryUserList = null;  //工厂所有入库员
            //发送手机推送消息
            try {
                //给用户推送
                submitOrderUser = baseUserService.getBaseUserBySeq(orderEntity.getUserSeq());
                List<String> aliasList = new ArrayList<String>();
                aliasList.add(submitOrderUser.getAccountName());
                jPushUtils.sendPush(aliasList, "您的订单：" + orderEntity.getOrderNum() + "已被确认支付" + paid + "元。" + userMessage, messageType);

                //给入库员推送
                factoryUserList = baseService.getUserByPermission(loginUser, new String[]{PermissionKeys.ORDER_STORE});
                aliasList = new ArrayList<String>();
                for (BaseUserEntity factoryUser : factoryUserList) {
                    aliasList.add(factoryUser.getAccountName());
                }
                jPushUtils.sendPush(aliasList, "订单：" + orderEntity.getOrderNum() + "已通过财务审核，请及时出库发货！", "2");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("审核订单后，发送手机推送消息失败" + e.getMessage(), e);
            }

            //发送网页推送消息
            try {
                //给入库员推送
                messageEventHandler.sendHandleOrderEvent(loginUser.getCompanySeq().toString(), PermissionKeys.ORDER_STORE, "订单：" + orderEntity.getOrderNum() + "已通过财务审核，请及时出库发货！", orderEntity.getSeq(), 2);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("审核订单后，发送网页推送消息失败" + e.getMessage(), e);
            }

            //保存推送消息
            try {
                //保存用户推送
                pushRecordService.addPushRecord(loginUser.getSeq(), submitOrderUser.getSeq(), submitOrderUser.getAccountName(), 1, orderEntity.getSeq(), "您的订单：" + orderEntity.getOrderNum() + "已被确认支付" + paid + "元。" + userMessage);
                //保存入库员推送
                for (BaseUserEntity factoryUser : factoryUserList) {
                    pushRecordService.addPushRecord(loginUser.getSeq(), factoryUser.getSeq(), factoryUser.getAccountName(), 1, orderEntity.getSeq(), "订单：" + orderEntity.getOrderNum() + "已通过财务审核，请及时出库发货！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("审核订单后，保存推送消息失败" + e.getMessage(), e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("审核订单后，推送消息处理异常" + e.getMessage(), e);
        }
    }

    //发送发货推送消息
    private void sendOrderShippedPush(OrderEntity orderEntity, int totalNum, BaseUserEntity loginUser) {
        //推送消息处理
        try {
            //给用户推送的内容
            String userMessage = "";
            String messageType = "";
            if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_FOUR.getValue()) {
                userMessage = "您的货品已部分出库发货，剩余货品会及时发出，请及时关注物流信息";
                messageType = "4";
            } else if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_FIVE.getValue()) {
                userMessage = "您的货品已全部出库发货，请及时关注物流信息";
                messageType = "5";
            }
            //发送手机推送消息
            BaseUserEntity submitOrderUser = null;  //提交订单人
            try {
                //给用户发推送
                submitOrderUser = baseUserService.getBaseUserBySeq(orderEntity.getUserSeq());
                List<String> aliasList = new ArrayList<String>();
                aliasList.add(submitOrderUser.getAccountName());
                jPushUtils.sendPush(aliasList, "您的订单：" + orderEntity.getOrderNum() + "商家已发货，本次共发货" + totalNum + "件。" + userMessage, messageType);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("订单发货后，发送手机推送消息失败" + e.getMessage(), e);
            }

            //保存推送消息
            try {
                //保存用户推送
                pushRecordService.addPushRecord(loginUser.getSeq(), submitOrderUser.getSeq(), submitOrderUser.getAccountName(), 1, orderEntity.getSeq(), "您的订单：" + orderEntity.getOrderNum() + "商家已发货，本次共发货" + totalNum + "件。" + userMessage);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("订单发货后，保存推送消息失败" + e.getMessage(), e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("订单发货后，推送消息处理异常" + e.getMessage(), e);
        }
    }


    /**
     * 拆单
     */
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public void splitOrder(Integer[] orderSeqArr,  String splitSXId) {
		
		try {
			for (Integer orderSeq : orderSeqArr) {

				// 1.查询该订单所有商品，根据shoesDataSeq获取鞋子，并按SX归类
				List<OrderProductsEntity> orderProductslist = getOrderProductsListByOrderSeq(orderSeq);

				Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
				Method method = GoodsShoesEntity.class.getMethod("get" + splitSXId);

				for (OrderProductsEntity orderProducts : orderProductslist) {
					ShoesDataEntity shoesDataEntity = shoesDataDao.selectById(orderProducts.getShoesDataSeq());
					GoodsShoesEntity goodsShoesEntity = goodsShoesDao.selectById(shoesDataEntity.getShoesSeq());
					String goodsSXCode = method.invoke(goodsShoesEntity).toString();
					if (map.containsKey(goodsSXCode)) {
						List<Integer> list = map.get(goodsSXCode);
						list.add(orderProducts.getSeq());
					} else {
						List<Integer> list = new ArrayList<Integer>();
						list.add(orderProducts.getSeq());
						map.put(goodsSXCode, list);
					}
				}
				
				// 2.根据归类个数，拆分成对应个数的订单
	            OrderEntity orderEntity = orderDao.selectById(orderSeq);
				Integer parentOrderSeq = orderEntity.getSeq();
				String parentOrderNum = orderEntity.getOrderNum();
				
				int i = 0;
				for (List<Integer> list : map.values()) {
					i++;
					//新订单
					orderEntity.setSeq(null);
					orderEntity.setOrderNum(parentOrderNum + "_" + i);  //原订单号后加"_1 _2 _3"
					orderEntity.setIsSplit(0);
					orderEntity.setParentSeqs(parentOrderSeq.toString());
					orderDao.insert(orderEntity);
					
					//新订单的orderProducts
					BigDecimal totalPrice = BigDecimal.valueOf(0);
					for(Integer orderProductsSeq : list) {
						OrderProductsEntity orderProductsEntity = orderProductsDao.selectById(orderProductsSeq);
						orderProductsEntity.setSeq(null);
						orderProductsEntity.setOrderSeq(orderEntity.getSeq());
						orderProductsDao.insert(orderProductsEntity);
						
						totalPrice = totalPrice.add(orderProductsEntity.getProductPrice().multiply(BigDecimal.valueOf(orderProductsEntity.getBuyCount())));
					}
					
					//新订单价格
					orderEntity.setOrderPrice(totalPrice);
					orderDao.updateById(orderEntity);
				}
				
				//原订单标记为已拆单
				OrderEntity order = new OrderEntity();
				order.setSeq(parentOrderSeq);
				order.setIsSplit(1);
	            orderDao.updateById(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
            logger.error(e.getMessage(), e);
			throw new RuntimeException("拆单异常", e);
		}
		
	}


	/**
	 * 归类拆单合并成新订单，并往erp同步
	 * 把一个用户的全部订单拆单再合并的操作 
	 */
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public void classifySplitOrder(Integer companySeq, String startTime, String endTime) {
		
        Date nowDate = new Date();
        
		//查询大类为男的seq
    	GoodsCategoryEntity goodsCategoryEntity = new GoodsCategoryEntity();
    	goodsCategoryEntity.setCompanySeq(companySeq);
    	goodsCategoryEntity.setName("男");
    	goodsCategoryEntity = goodsCategoryDao.selectOne(goodsCategoryEntity);
		Integer categoryManSeq = goodsCategoryEntity.getSeq();
		//查询大类为女的seq
    	goodsCategoryEntity = new GoodsCategoryEntity();
    	goodsCategoryEntity.setCompanySeq(companySeq);
    	goodsCategoryEntity.setName("女");
    	goodsCategoryEntity = goodsCategoryDao.selectOne(goodsCategoryEntity);
		Integer categoryWomanSeq = goodsCategoryEntity.getSeq();
		
		//查询品牌为强人实体的code
		GoodsBrandEntity goodsBrandEntity = new GoodsBrandEntity();
		goodsBrandEntity.setCompanySeq(companySeq);
		goodsBrandEntity.setBrandName("强人实体");
		goodsBrandEntity = goodsBrandDao.selectOne(goodsBrandEntity);
		String brandQiangrenCode = goodsBrandEntity.getBrandCode();
		
		//查询品牌为童鞋的code
		goodsBrandEntity = new GoodsBrandEntity();
		goodsBrandEntity.setCompanySeq(companySeq);
		goodsBrandEntity.setBrandName("童鞋");
		goodsBrandEntity = goodsBrandDao.selectOne(goodsBrandEntity);
		String brandChildCode = goodsBrandEntity.getBrandCode();
		
		
		//查询时间段内的所有有订单的用户seq
        Wrapper<OrderEntity> wrapper = new EntityWrapper<OrderEntity>();
        wrapper.setSqlSelect("User_Seq").where("Company_Seq = {0} AND OrderStatus = 0 AND IsSplit = 0 AND ClassifySplitOrder = 0 AND InputTime >= {1} AND InputTime <= {2} ", companySeq, startTime, endTime).groupBy("User_Seq");
		List<Object> userSeqList = orderDao.selectObjs(wrapper);
		
		for(Object userSeq : userSeqList) {
			//查询该用户时间段内的所有待接单的订单seq
	        wrapper = new EntityWrapper<OrderEntity>();
	        wrapper.setSqlSelect("Seq").where("Company_Seq = {0} AND User_Seq = {1} AND OrderStatus = 0 AND IsSplit = 0 AND ClassifySplitOrder = 0 AND InputTime >= {2} AND InputTime <= {3} ", companySeq, userSeq, startTime, endTime);
			List<Object> orderSeqList = orderDao.selectObjs(wrapper);
			
    		//获取订单的所有商品列表
    		List<OrderProductsEntity> orderProductsEntityList = new ArrayList<OrderProductsEntity>();
    		if(orderSeqList.size() > 0) {
    			Wrapper<OrderProductsEntity> orderProductsWrapper = new EntityWrapper<OrderProductsEntity>();
    			orderProductsWrapper.in("Order_Seq", orderSeqList);
    			orderProductsEntityList = orderProductsDao.selectList(orderProductsWrapper);
    		}
			
    		/**需求原文：
    			1、订单里有 男女鞋  礼品  皮具     我们会先把男鞋（品牌为强人实体  大类为男）拆出去  生成一张订单  其他的还在原订单里
    			2、然后再把原订单里的品牌为强人实体  大类为女  拆出去   再生成一张订单
    			3、 然后把品牌为童鞋的 商品订单  从原订单里拆出去  生成一张童鞋订单
    			4、这样原订单里面就只剩下礼品和皮具
    		**/
			
    		//男鞋  （品牌为强人实体  大类为男）
			List<Integer> manProductSeqList = new ArrayList<Integer>();   //订单商品seq
			Set<Integer> manOrderSeqSet = new TreeSet<Integer>(); 	//订单seq
			//女鞋 （品牌为强人实体  大类为男）
			List<Integer> womanProductSeqList = new ArrayList<Integer>();   //订单商品seq
			Set<Integer> womanOrderSeqSet = new TreeSet<Integer>();   //订单seq   
			//童鞋 （品牌为童鞋）
			List<Integer> childProductSeqList = new ArrayList<Integer>();   //订单商品seq
			Set<Integer> childOrderSeqSet = new TreeSet<Integer>();   //订单seq   
			//其他
			List<Integer> otherProductSeqList = new ArrayList<Integer>();    //订单商品seq
			Set<Integer> otherOrderSeqSet = new TreeSet<Integer>();    //订单seq   
			for (OrderProductsEntity orderProducts : orderProductsEntityList) {
				ShoesDataEntity shoesDataEntity = shoesDataDao.selectById(orderProducts.getShoesDataSeq());
				GoodsShoesEntity goodsShoesEntity = goodsShoesDao.selectById(shoesDataEntity.getShoesSeq());
				if(goodsShoesEntity.getShoesBrand().equals(brandQiangrenCode) && goodsShoesEntity.getCategorySeq().equals(categoryManSeq)) {
					manProductSeqList.add(orderProducts.getSeq());
					manOrderSeqSet.add(orderProducts.getOrderSeq());
				} else if (goodsShoesEntity.getShoesBrand().equals(brandQiangrenCode) && goodsShoesEntity.getCategorySeq().equals(categoryWomanSeq)) {
					womanProductSeqList.add(orderProducts.getSeq());
					womanOrderSeqSet.add(orderProducts.getOrderSeq());
				} else if (goodsShoesEntity.getShoesBrand().equals(brandChildCode)) {
					childProductSeqList.add(orderProducts.getSeq());
					childOrderSeqSet.add(orderProducts.getOrderSeq());
				} else {
					otherProductSeqList.add(orderProducts.getSeq());
					otherOrderSeqSet.add(orderProducts.getOrderSeq());
				}
			}
    		
			//生成男鞋订单
			if(manProductSeqList.size() > 0) {
				createOrder(nowDate, companySeq, (Integer)userSeq, manProductSeqList, manOrderSeqSet, "M");
			}
			//生成女鞋订单
			if(womanProductSeqList.size() > 0) {
				createOrder(nowDate, companySeq, (Integer)userSeq, womanProductSeqList, womanOrderSeqSet, "W");
			}
			//生成童鞋订单
			if(childProductSeqList.size() > 0) {
				createOrder(nowDate, companySeq, (Integer)userSeq, childProductSeqList, childOrderSeqSet, "C");
			}
			//生成其他订单
			if(otherProductSeqList.size() > 0) {
				createOrder(nowDate, companySeq, (Integer)userSeq, otherProductSeqList, otherOrderSeqSet, "O");
			}
		}
	}
	
	
	

	private void createOrder(Date nowDate, Integer companySeq, Integer userSeq, List<Integer> productSeqList, Set<Integer> orderSeqSet, String suffix) {
		//生成一个新订单-男鞋订单
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setUserSeq(userSeq);
			//订单号
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
		String s = "0000" + userSeq;
	    String orderNum = df.format(nowDate) + s.substring(s.length() - 5) + suffix;
		orderEntity.setOrderNum(orderNum);
		orderEntity.setIsSplit(0);
		orderEntity.setParentSeqs(Joiner.on(",").join(orderSeqSet));  //将原来的订单的seq存在parentSeqs里
		orderEntity.setClassifySplitOrder(1);  //设置ClassifySplitOrder为1（不能再归类拆单同步）
		orderEntity.setImportErpState(0);  //如果是河南强人的订单，需要导入到ERP，设置erp同步状态0 （目前只有河南强人有拆单功能，所以直接设置没做判断）
		orderEntity.setPaid(BigDecimal.valueOf(0));
		orderEntity.setOrderStatus(0);
		orderEntity.setCompanySeq(companySeq);
			//查询第一个订单的地址
		OrderEntity firstOrder = orderDao.selectById(orderSeqSet.iterator().next());
		orderEntity.setReceiverName(firstOrder.getReceiverName());
		orderEntity.setTelephone(firstOrder.getTelephone());
		orderEntity.setFullAddress(firstOrder.getFullAddress());
		orderEntity.setInputTime(nowDate);
		orderEntity.setDel(0);
		orderDao.insert(orderEntity);
		
		
		//新订单的orderProducts
		BigDecimal totalPrice = BigDecimal.valueOf(0);
		for(Integer orderProductsSeq : productSeqList) {
			OrderProductsEntity orderProductsEntity = orderProductsDao.selectById(orderProductsSeq);
			orderProductsEntity.setSeq(null);
			orderProductsEntity.setOrderSeq(orderEntity.getSeq());
			orderProductsDao.insert(orderProductsEntity);
			
			totalPrice = totalPrice.add(orderProductsEntity.getProductPrice().multiply(BigDecimal.valueOf(orderProductsEntity.getBuyCount())));
		}
		
		//新订单价格
		orderEntity.setOrderPrice(totalPrice);
		orderDao.updateById(orderEntity);
	
		//标记原来的所有订单为3已归类拆单合并
		for(Integer orderSeq : orderSeqSet) {
			OrderEntity order = new OrderEntity();
			order.setSeq(orderSeq);
			order.setIsSplit(3);
	        orderDao.updateById(order);
		}
		
	}


	
	/**
	 * 按模板归类拆单合并成新订单，并往erp同步
	 * 把一个用户的全部订单拆单再合并的操作 
	 */
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public void splitOrderByModel(Integer companySeq, Integer modelSeq, String startTime, String endTime) {
		
        Date nowDate = new Date();
        
        //根据模板序号查询模板步骤详情
        List<SplitOrderModelDetailEntity> splitOrderModelDetailList = splitOrderModelService.getModelDetailByModelSeq(modelSeq);
        
		//查询时间段内的所有有订单的用户seq
        Wrapper<OrderEntity> wrapper = new EntityWrapper<OrderEntity>();
        wrapper.setSqlSelect("User_Seq").where("Company_Seq = {0} AND OrderStatus = 0 AND IsSplit = 0 AND ClassifySplitOrder = 0 AND InputTime >= {1} AND InputTime <= {2} ", companySeq, startTime, endTime).groupBy("User_Seq");
		List<Object> userSeqList = orderDao.selectObjs(wrapper);
		
		for(Object userSeq : userSeqList) {
			//查询该用户时间段内的所有待接单的订单seq
	        wrapper = new EntityWrapper<OrderEntity>();
	        wrapper.setSqlSelect("Seq").where("Company_Seq = {0} AND User_Seq = {1} AND OrderStatus = 0 AND IsSplit = 0 AND ClassifySplitOrder = 0 AND InputTime >= {2} AND InputTime <= {3} ", companySeq, userSeq, startTime, endTime);
			List<Object> orderSeqList = orderDao.selectObjs(wrapper);
			
    		//获取订单的所有商品列表
    		List<OrderProductsEntity> orderProductsEntityList = new ArrayList<OrderProductsEntity>();
    		if(orderSeqList.size() > 0) {
    			Wrapper<OrderProductsEntity> orderProductsWrapper = new EntityWrapper<OrderProductsEntity>();
    			orderProductsWrapper.in("Order_Seq", orderSeqList);
    			orderProductsEntityList = orderProductsDao.selectList(orderProductsWrapper);
    		}
			
    		//订单号后缀的随机数
    		int random = (int) (Math.random() * 900) + 100; //三位随机数
    		/**
    			1、先把商品各个属性满足步骤一的拆出去  生成一个订单，并从订单商品列表移除
				2、再把商品各个属性满足步骤二的拆出去  生成一个订单，并从订单商品列表移除
				······
    			n、最后，原订单里面剩下的生成一个订单
    		**/
    		for(int n = 1; n <= splitOrderModelDetailList.size(); n++) {
    			SplitOrderModelDetailEntity modelDetail = splitOrderModelDetailList.get(n-1);
    			
	    		//步骤n订单
				List<Integer> productSeqList = new ArrayList<Integer>();   //订单商品seq
				Set<Integer> orderSeqSet = new TreeSet<Integer>(); 	//订单seq
				for (int i = 0; i < orderProductsEntityList.size(); i++) {
					OrderProductsEntity orderProducts = orderProductsEntityList.get(i);
					ShoesDataEntity shoesDataEntity = shoesDataDao.selectById(orderProducts.getShoesDataSeq());
					GoodsShoesEntity goodsShoes = goodsShoesDao.selectById(shoesDataEntity.getShoesSeq());
					if((modelDetail.getShoesBrand() == null || ("," + modelDetail.getShoesBrand() + ",").contains("," + goodsShoes.getShoesBrand() + "," ))
						&& (modelDetail.getCategorySeq() == null || ("," + modelDetail.getCategorySeq() + ",").contains("," + goodsShoes.getCategorySeq() + "," ))
						&& (modelDetail.getPeriodSeq() == null || ("," + modelDetail.getPeriodSeq() + ",").contains("," + goodsShoes.getPeriodSeq() + "," ))
						&& (modelDetail.getSX1() == null || ("," + modelDetail.getSX1() + ",").contains("," + goodsShoes.getSX1() + "," ))
						&& (modelDetail.getSX2() == null || ("," + modelDetail.getSX2() + ",").contains("," + goodsShoes.getSX2() + "," ))
						&& (modelDetail.getSX3() == null || ("," + modelDetail.getSX3() + ",").contains("," + goodsShoes.getSX3() + "," ))
						&& (modelDetail.getSX4() == null || ("," + modelDetail.getSX4() + ",").contains("," + goodsShoes.getSX4() + "," ))
						&& (modelDetail.getSX5() == null || ("," + modelDetail.getSX5() + ",").contains("," + goodsShoes.getSX5() + "," ))
						&& (modelDetail.getSX6() == null || ("," + modelDetail.getSX6() + ",").contains("," + goodsShoes.getSX6() + "," ))
						&& (modelDetail.getSX7() == null || ("," + modelDetail.getSX7() + ",").contains("," + goodsShoes.getSX7() + "," ))
						&& (modelDetail.getSX8() == null || ("," + modelDetail.getSX8() + ",").contains("," + goodsShoes.getSX8() + "," ))
						&& (modelDetail.getSX9() == null || ("," + modelDetail.getSX9() + ",").contains("," + goodsShoes.getSX9() + "," ))
						&& (modelDetail.getSX10() == null || ("," + modelDetail.getSX10() + ",").contains("," + goodsShoes.getSX10() + "," ))
						&& (modelDetail.getSX11() == null || ("," + modelDetail.getSX11() + ",").contains("," + goodsShoes.getSX11() + "," ))
						&& (modelDetail.getSX12() == null || ("," + modelDetail.getSX12() + ",").contains("," + goodsShoes.getSX12() + "," ))
						&& (modelDetail.getSX13() == null || ("," + modelDetail.getSX13() + ",").contains("," + goodsShoes.getSX13() + "," ))
						&& (modelDetail.getSX14() == null || ("," + modelDetail.getSX14() + ",").contains("," + goodsShoes.getSX14() + "," ))
						&& (modelDetail.getSX15() == null || ("," + modelDetail.getSX15() + ",").contains("," + goodsShoes.getSX15() + "," ))
						&& (modelDetail.getSX16() == null || ("," + modelDetail.getSX16() + ",").contains("," + goodsShoes.getSX16() + "," ))
						&& (modelDetail.getSX17() == null || ("," + modelDetail.getSX17() + ",").contains("," + goodsShoes.getSX17() + "," ))
						&& (modelDetail.getSX18() == null || ("," + modelDetail.getSX18() + ",").contains("," + goodsShoes.getSX18() + "," ))
						&& (modelDetail.getSX19() == null || ("," + modelDetail.getSX19() + ",").contains("," + goodsShoes.getSX19() + "," ))
						&& (modelDetail.getSX20() == null || ("," + modelDetail.getSX20() + ",").contains("," + goodsShoes.getSX20() + "," ))) {
						productSeqList.add(orderProducts.getSeq());
						orderSeqSet.add(orderProducts.getOrderSeq());
						orderProductsEntityList.remove(i);
						i--;
					}
				}
				
				//生成步骤n订单
				if(productSeqList.size() > 0) {
					createOrder(nowDate, companySeq, (Integer)userSeq, productSeqList, orderSeqSet, random + "_" + n);
				}
				
    		}
    		
    		
			//其他
			List<Integer> otherProductSeqList = new ArrayList<Integer>();    //订单商品seq
			Set<Integer> otherOrderSeqSet = new TreeSet<Integer>();    //订单seq  
			for (OrderProductsEntity orderProducts : orderProductsEntityList) {
				otherProductSeqList.add(orderProducts.getSeq());
				otherOrderSeqSet.add(orderProducts.getOrderSeq());
			}
			//生成其他订单
			if(otherProductSeqList.size() > 0) {
				createOrder(nowDate, companySeq, (Integer)userSeq, otherProductSeqList, otherOrderSeqSet, random + "_0");
			}
			
		}
	}

}
