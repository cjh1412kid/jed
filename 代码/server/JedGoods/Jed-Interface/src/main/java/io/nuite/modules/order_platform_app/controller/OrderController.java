package io.nuite.modules.order_platform_app.controller;

import io.nuite.common.utils.Constant;
import io.nuite.common.utils.DateUtils;
import io.nuite.common.utils.PermissionKeys;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.dao.OrderDao;
import io.nuite.modules.order_platform_app.entity.*;
import io.nuite.modules.order_platform_app.service.OrderService;
import io.nuite.modules.order_platform_app.service.PushRecordService;
import io.nuite.modules.order_platform_app.service.ReceiverInfoService;
import io.nuite.modules.order_platform_app.utils.JPushUtils;
import io.nuite.modules.order_platform_app.utils.OrderStatusEnum;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import io.nuite.modules.system.service.ShiroService;
import io.nuite.modules.system.socketio.MessageEventHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单接口
 *
 * @author yy
 * @date 2018-04-19 13:47
 */
@RestController
@RequestMapping("/order/app/order")
@Api(tags = "订货平台 - 订单", description = "提交订单+取消订单+删除订单+修改订单+订单流程处理")
public class OrderController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private JPushUtils jPushUtils;

    @Autowired
    private PushRecordService pushRecordService;

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private ReceiverInfoService receiverInfoService;

    @Autowired
    private MessageEventHandler messageEventHandler;


    /**
     * 订单列表
     */
    @Login
    @GetMapping("orderList")
    @ApiOperation("订单列表")
    public R orderList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                       @ApiParam("订单状态") @RequestParam("status") Integer orderStatus,
                       @ApiParam("起始条数") @RequestParam("start") Integer start,
                       @ApiParam("总条数") @RequestParam("num") Integer num) {
        try {
            List<OrderEntity> orderEntityList;
            //如果是工厂用户，按照公司编号companySeq查询订单列表
//            if (isFactoryUser(loginUser)) {
                orderEntityList = orderService.getOrderListByCompanySeq(loginUser.getCompanySeq(), orderStatus, start, num);
//            } else { //如果是订货方，按照用户编号userSeq查询订单列表
//                orderEntityList = orderService.getOrderListByUserSeq(loginUser.getSeq(), orderStatus, start, num);
//            }

            List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
            Map<String, Object> orderMap;
            List<OrderProductsEntity> orderProductsList;
            int shoesTotalNum;
            List<String> shoesImgList;
            GoodsShoesEntity goodsShoesEntity;
            for (OrderEntity orderEntity : orderEntityList) {
                orderMap = new HashMap<String, Object>();

                orderMap.put("Seq", orderEntity.getSeq());
                orderMap.put("orderNum", orderEntity.getOrderNum());
                orderMap.put("orderPrice", orderEntity.getOrderPrice());
                orderMap.put("paid", orderEntity.getPaid());
                orderMap.put("orderStatus", orderEntity.getOrderStatus());
                orderMap.put("inputTime", orderEntity.getInputTime());
//                if (isFactoryUser(loginUser)) {
                    orderMap.put("statusName", OrderStatusEnum.get(orderEntity.getOrderStatus()).getFactoryName());
//                } else {
//                    orderMap.put("statusName", OrderStatusEnum.get(orderEntity.getOrderStatus()).getBrandName());
//                }
                //根据订单序号Seq，查询订单关联的商品，获取商品图片
                orderProductsList = orderService.getOrderProductsListByOrderSeq(orderEntity.getSeq());

                shoesTotalNum = 0;
                shoesImgList = new ArrayList<String>();
                //获取orderProducts中shoesDateSeq获取每个鞋子的图片，可能重复
                for (OrderProductsEntity orderProductsEntity : orderProductsList) {
                    shoesTotalNum += orderProductsEntity.getBuyCount();
                    goodsShoesEntity = orderService.getGoodsShoesByShoesDateSeq(orderProductsEntity.getShoesDataSeq());
                    if (goodsShoesEntity != null) {
                        shoesImgList.add(getGoodsShoesPictureUrl(goodsShoesEntity.getGoodID()) + goodsShoesEntity.getImg1());
                    }
                }
                orderMap.put("species", shoesTotalNum); //订单中鞋子总数
                orderMap.put("photo", shoesImgList);

                orderList.add(orderMap);
            }
            return R.ok(orderList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 订单详情
     */
    @Login
    @GetMapping("orderDetail")
    @ApiOperation("订单详情")
    public R orderDetail(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                         @ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq) {
        try {
            //查询订单详情
            Map<String, Object> orderMap = orderService.getOrderMapBySeq(orderSeq);

            //订单用户信息
            Map<String, Object> userInfo = orderService.getUserMapByUserSeq((Integer) orderMap.get("userSeq"));
            orderMap.put("userInfo", userInfo);

            //订单状态名称
            Integer orderStatus = (Integer) orderMap.get("orderStatus");
//            if (isFactoryUser(loginUser)) {
                orderMap.put("statusName", OrderStatusEnum.get(orderStatus).getFactoryName());
//            } else {
//                orderMap.put("statusName", OrderStatusEnum.get(orderStatus).getBrandName());
//            }

            //订单关联商品列表
            List<Map<String, Object>> orderProductsList = orderService.getOrderProductsList(orderSeq);
            for (Map<String, Object> orderProductsMap : orderProductsList) {
                orderProductsMap.put("img", getGoodsShoesPictureUrl(orderProductsMap.get("goodId").toString()) + orderProductsMap.get("img"));
            }
            orderMap.put("orderProductsList", orderProductsList);

            //订单快递信息列表
            if (orderStatus == OrderStatusEnum.ORDSTATUS_FOUR.getValue() || orderStatus == OrderStatusEnum.ORDSTATUS_FIVE.getValue() || orderStatus == OrderStatusEnum.ORDSTATUS_SIX.getValue()) {
                List<Map<String, Object>> orderExpressList = orderService.getOrderExpressList(orderSeq);
                for (Map<String, Object> orderExpressMap : orderExpressList) {

                    //快递公司编码
                    if (orderExpressMap.get("expressCompanySeq") != null && !orderExpressMap.get("expressCompanySeq").toString().equals("")) {
                        orderExpressMap.put("expressCompanyCode", orderService.getExpressCompanyBySeq(((Long) orderExpressMap.get("expressCompanySeq")).intValue()).getCode());
                    }
                    //物流图片路径
                    if (orderExpressMap.get("expressImg") != null && !orderExpressMap.get("expressImg").toString().equals("")) {
                        orderExpressMap.put("expressImg", getOrderExpressPictureUrl(orderSeq.toString()) + orderExpressMap.get("expressImg"));
                    }

                    //快递发货详细信息列表
                    List<Map<String, Object>> orderExpressDetailsList = orderService.getOrderExpressDetailsList(((Long) orderExpressMap.get("orderExpressSeq")).intValue());
                    int totalNum = 0; //发货总量
                    for (Map<String, Object> orderExpressDetailsMap : orderExpressDetailsList) {
                        totalNum = totalNum + (int) orderExpressDetailsMap.get("num");
                        for (Map<String, Object> orderProductsMap : orderProductsList) {
                            if ((int) orderProductsMap.get("orderProductsSeq") == ((Long) orderExpressDetailsMap.get("orderProductsSeq")).intValue()) {
                                orderExpressDetailsMap.put("goodId", orderProductsMap.get("goodId"));
                                orderExpressDetailsMap.put("colorName", orderProductsMap.get("colorName"));
                                orderExpressDetailsMap.put("size", orderProductsMap.get("size"));
                                break;
                            }
                        }
                    }
                    orderExpressMap.put("totalNum", totalNum);
                    orderExpressMap.put("orderExpressDetailsList", orderExpressDetailsList);
                }

                orderMap.put("orderExpressList", orderExpressList);
            }

            return R.ok(orderMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 订单汇总
     */
    @Login
    @GetMapping("orderSummary")
    @ApiOperation("订单汇总功能")
    public R orderSummary(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                          @ApiParam("起始条数") @RequestParam("start") Integer start,
                          @ApiParam("总条数") @RequestParam("num") Integer num,
                          HttpServletRequest request) {
        try {
            //1.查询登录用户所属公司类型，判断是否属于工厂方，不是不能调用此接口
//            if (!isFactoryUser(loginUser)) {
//                return R.error(HttpStatus.SC_METHOD_NOT_ALLOWED, "非厂家不支持此功能");
//            }

            //2.根据厂家公司编号，获取所有该厂家的待入库订单序号
            List<Object> orderSeqlist = orderService.getOrderSeqByCompanySeq(loginUser.getCompanySeq());

            if (orderSeqlist != null && orderSeqlist.size() > 0) {
                //3.根据订单编号，查询汇总信息
                List<Map<String, Object>> orderSummaryList = orderService.getOrderSummary(orderSeqlist, start, num);

                //4.新增：给汇总信息添加在每个订单中的数量
                List<Map<String, Object>> summaryDetailList;
                for (Map<String, Object> map : orderSummaryList) {
                    summaryDetailList = orderService.getSummaryDetailList(orderSeqlist, (Integer) map.get("shoesDataSeq"));
                    map.put("summaryDetail", summaryDetailList);
                }
                return R.ok(orderSummaryList);
            } else {
                //没有待入库的订单，返回空[]
                return R.ok(orderSeqlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("订单汇总失败");
        }
    }


    /**
     * 提交订单
     */
    @Login
    @PostMapping("submitOrder")
    @ApiOperation(value = "提交订单", notes = "包括从购物车提交和直接购买，从购物车提交时，需要在shoesDataBuyCountList参数中加入购物车序号seq")
    public R submitOrder(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                         @ApiParam("收货人seq") @RequestParam("receiverInfoSeq") Integer receiverInfoSeq,
                         @ApiParam("购买商品列表") @RequestParam(value = "shoesDataBuyCountList") String shoesDataBuyCountList, //[{seq:3,shoesDataSeq:5,buyCount:2},{seq:4,shoesDataSeq:10,buyCount:1}]
                         @ApiParam("提交类型(1:购物车结算 2：直接购买)") @RequestParam("submitType") Integer submitType,
                         @ApiParam("订单价格") @RequestParam("orderPrice") BigDecimal orderPrice,
                         @ApiParam("要求到货时间") @RequestParam(value = "requireTime", required = false) Date requireTime,
                         @ApiParam("留言") @RequestParam(value = "suggestion", required = false) String suggestion,
                         HttpServletRequest request) {
        try {

            //订单号
            int random = (int) (Math.random() * 900) + 100; //三位随机数
            Date nowDate = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String orderNum = df.format(nowDate) + random;


            /**涉及到需要修改的表：
             YHSR_OP_ShoesData(订货平台鞋子数据表) 	修改库存
             YHSR_OP_ShoppingCart（购物车表） 		删除被提交订单的购物车
             YHSR_OP_Order (订单信息表)				新增一条订单信息
             YHSR_OP_OrderProducts（订单关联产品表） 将购买的商品列表分多条存储到这里
             **/
            //1.库存变动List
            List<Map<String, Integer>> stockChangeList = new ArrayList<Map<String, Integer>>();
            Map<String, Integer> stockChangeMap;

            //2.购物车序号List
            List<Integer> shoppingCartSeqList = new ArrayList<Integer>();

            //4.订单关联产品表对象List
            List<OrderProductsEntity> orderProductsList = new ArrayList<OrderProductsEntity>();
            OrderProductsEntity orderProductsEntity;

            JSONArray jsonArray = JSONArray.fromObject(shoesDataBuyCountList);
            JSONObject jsonObject;
            ShoesInfoEntity shoesInfoEntity;
            BigDecimal price = BigDecimal.valueOf(0);
            BigDecimal shoesPrice = null;
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                int shoesDataSeq = jsonObject.getInt("shoesDataSeq");
                int buyCount = jsonObject.getInt("buyCount");

                //数据校验：
                	//获取鞋子信息实体，用于判断下架状态和价格
                shoesInfoEntity = orderService.getShoesInfoByShoesDateSeq(shoesDataSeq);
                	//判断鞋子是否已下架
                if (shoesInfoEntity.getOnSale() == 0 || (shoesInfoEntity.getOffSaleTime() != null && DateUtils.compareDay(new Date(), shoesInfoEntity.getOffSaleTime()) > 0)) {
                    return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您选择的商品已下架");
                }
                	//鞋子价格累计
//                if (loginUser.getSaleType() == Constant.UserSaleType.OEM.getValue()) {
//                    shoesPrice = shoesInfoEntity.getOemPrice();
//                    price = price.add(shoesPrice.multiply(BigDecimal.valueOf(buyCount)));
//                } else if (loginUser.getSaleType() == Constant.UserSaleType.WHOLESALER.getValue()) {
//                    shoesPrice = shoesInfoEntity.getWholesalerPrice();
//                    price = price.add(shoesPrice.multiply(BigDecimal.valueOf(buyCount)));
//                } else if (loginUser.getSaleType() == Constant.UserSaleType.STORE.getValue()) {
//                    shoesPrice = shoesInfoEntity.getStorePrice();
//                    price = price.add(shoesPrice.multiply(BigDecimal.valueOf(buyCount)));
//                }


                //1.库存变动
                stockChangeMap = new HashMap<String, Integer>();
                stockChangeMap.put("shoesDataSeq", shoesDataSeq);
                stockChangeMap.put("changNum", -buyCount);
                stockChangeList.add(stockChangeMap);

                //2.购物车序号
                if (submitType == 1) {
                    shoppingCartSeqList.add(jsonObject.getInt("seq"));
                }

                //4.订单关联产品表对象
                orderProductsEntity = new OrderProductsEntity();
                orderProductsEntity.setShoesDataSeq(shoesDataSeq);
                orderProductsEntity.setProductPrice(shoesPrice);
                orderProductsEntity.setBuyCount(buyCount);
                orderProductsEntity.setDeliverNum(0);
                orderProductsEntity.setInputTime(nowDate);
                orderProductsEntity.setDel(0);
                orderProductsList.add(orderProductsEntity);
            }

            //判断价格是否和前端传过来的一致
            if (price.compareTo(orderPrice) != 0) {
                return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您选择的商品价格已过期，请刷新后再试");
            }
            //3.订单对象
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setUserSeq(loginUser.getSeq());
            orderEntity.setOrderNum(orderNum);
            orderEntity.setIsSplit(0);
            orderEntity.setClassifySplitOrder(0);
            orderEntity.setOrderPrice(price);
            orderEntity.setPaid(BigDecimal.valueOf(0));
            orderEntity.setOrderStatus(OrderStatusEnum.ORDSTATUS_ZERO.getValue());
            orderEntity.setCompanySeq(loginUser.getCompanySeq());

            //查询、拼接收货人信息
            ReceiverInfoEntity receiverInfoEntity = receiverInfoService.getReceiverInfoBySeq(receiverInfoSeq);
            orderEntity.setReceiverName(receiverInfoEntity.getReceiverName());
            orderEntity.setTelephone(receiverInfoEntity.getTelephone());
            orderEntity.setFullAddress(receiverInfoEntity.getProvince() + receiverInfoEntity.getCity() + receiverInfoEntity.getDistrict() + receiverInfoEntity.getDetailAddress());

            orderEntity.setInputTime(nowDate);
            orderEntity.setRequireTime(requireTime);
            orderEntity.setSuggestion(suggestion);
            orderEntity.setDel(0);

            //提交订单
            Integer orderSeq;
            try {
                orderSeq = orderService.submitOrder(stockChangeList, shoppingCartSeqList, orderEntity, orderProductsList);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
                if (e.getMessage().equals("库存不足")) {
                    return R.error(HttpStatus.SC_FORBIDDEN, "提交订单失败，库存不足");
                } else {
                    return R.error(HttpStatus.SC_FORBIDDEN, "提交订单失败");
                }
            }

            //发送手机推送消息
            List<BaseUserEntity> userList = null;     //工厂所有接单员
            try {
                //给接单员推送
                userList = baseService.getUserByPermission(loginUser, new String[]{PermissionKeys.ORDER_RECEIVE});
                List<String> aliasList = new ArrayList<String>();
                for (BaseUserEntity user : userList) {
                    aliasList.add(user.getAccountName());
                }
                jPushUtils.sendPush(aliasList, "您有新订单，请及时接单处理！订单号：" + orderNum, "0");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("提交订单后，发送手机推送消息失败" + e.getMessage(), e);
            }

            //发送网页推送消息
            try {
                //给接单员推送
                messageEventHandler.sendHandleOrderEvent(loginUser.getCompanySeq().toString(), PermissionKeys.ORDER_RECEIVE, "您有新订单，请及时接单处理！订单号：" + orderNum, orderSeq, 0);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("提交订单后，发送网页推送消息失败" + e.getMessage(), e);
            }

            //保存推送消息
            try {
                for (BaseUserEntity user : userList) {
                    pushRecordService.addPushRecord(loginUser.getSeq(), user.getSeq(), user.getAccountName(), 1, orderSeq, "您有新订单，请及时接单处理！订单号：" + orderNum);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("提交订单后，保存推送消息失败" + e.getMessage(), e);
            }

            //如果是依婷公主的订单，需要导入到ERP，设置erp同步状态0
            try {
            	if(configUtils.getYtgzCompanyseq().equals(loginUser.getCompanySeq())) {
            		OrderEntity ytgzOrder = new OrderEntity();
            		ytgzOrder.setSeq(orderSeq);
            		ytgzOrder.setImportErpState(0);
            		orderDao.updateById(ytgzOrder);
            	}
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("提交订单后，依婷公主的订单设置同步erp状态失败" + e.getMessage(), e);
            }
            
            List<Integer> list = new ArrayList<Integer>();
            list.add(orderSeq);
            return R.ok(list).put("msg", "提交订单成功");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }


    /**
     * 取消订单
     */
    @Login
    @PostMapping("cancelOrder")
    @ApiOperation("取消订单")
    public R cancelOrder(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                         @ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
                         @ApiParam("取消备注") @RequestParam("remark") String remark,
                         HttpServletRequest request) {
        try {
            /*取消条件判断*/
            OrderEntity orderEntity = orderService.getOrderBySeq(orderSeq);

            //订货方
//            if (!isFactoryUser(loginUser)) {
//                if (orderEntity.getOrderStatus() != OrderStatusEnum.ORDSTATUS_ZERO.getValue()) {
//                    return R.error(HttpStatus.SC_FORBIDDEN, "订单已确认，不可取消");
//                }
//            } else { //工厂
                //该工厂用户的所有权限
                Set<String> permissions = shiroService.getUserPermissions(loginUser.getSeq());

                if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_ZERO.getValue()) { //待确认
                    if (!(permissions.contains(PermissionKeys.ORDER_RECEIVE) && permissions.contains(PermissionKeys.ORDER_CANCEL))) {
                        return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
                    }
                } else if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_ONE.getValue()) { //待审核
                    if (!(permissions.contains(PermissionKeys.ORDER_CHECK) && permissions.contains(PermissionKeys.ORDER_CANCEL))) {
                        return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
                    }
                } else if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_TWO.getValue()) { //待入库
                    if (!(permissions.contains(PermissionKeys.ORDER_STORE) && permissions.contains(PermissionKeys.ORDER_CANCEL))) {
                        return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
                    }
                } else if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_THREE.getValue()) { //未发货
                    if (!(permissions.contains(PermissionKeys.ORDER_DELIVER) && permissions.contains(PermissionKeys.ORDER_CANCEL))) {
                        return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
                    }
                } else {
                    return R.error(HttpStatus.SC_FORBIDDEN, "当前订单状态不可取消");
                }
//            }


            /**涉及到需要修改的表：
             YHSR_OP_ShoesData(订货平台鞋子数据表) 	修改库存
             YHSR_OP_Order (订单信息表)				修改订单状态
             **/
            //根据订单编号，查询订单的购买的商品列表
            List<OrderProductsEntity> orderProductsList = orderService.getOrderProductsListByOrderSeq(orderSeq);
            List<Map<String, Integer>> stockChangeList = new ArrayList<Map<String, Integer>>();
            Map<String, Integer> stockChangeMap;
            for (OrderProductsEntity orderProducts : orderProductsList) {
                //库存变动
                stockChangeMap = new HashMap<String, Integer>();
                stockChangeMap.put("shoesDataSeq", orderProducts.getShoesDataSeq());
                stockChangeMap.put("changNum", orderProducts.getBuyCount());
                stockChangeList.add(stockChangeMap);
            }

            //取消订单
            try {
                orderService.cancelOrder(orderSeq, remark, stockChangeList);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
                return R.error(HttpStatus.SC_FORBIDDEN, "取消订单失败");
            }


            //发送手机推送消息
            BaseUserEntity submitUser = null;  //提交订单人
            List<BaseUserEntity> factoryUserList = null;  //工厂所有接单员
            try {
                //工厂取消订单
//                if (isFactoryUser(loginUser)) {
                    //给用户推送
                    submitUser = baseUserService.getBaseUserBySeq(orderEntity.getUserSeq());
                    List<String> aliasList = new ArrayList<String>();
                    aliasList.add(submitUser.getAccountName());
                    jPushUtils.sendPush(aliasList, "您的订单：" + orderEntity.getOrderNum() + "已被商家取消，备注：" + remark + "。 如有疑问请及时联系客服！", "7");

//                } else {  //用户取消订单
//                    //给接单员推送
//                    factoryUserList = baseService.getUserByPermission(loginUser, new String[]{PermissionKeys.ORDER_RECEIVE});
//                    List<String> aliasList = new ArrayList<String>();
//                    for (BaseUserEntity factoryUser : factoryUserList) {
//                        aliasList.add(factoryUser.getAccountName());
//                    }
//                    jPushUtils.sendPush(aliasList, "订单：" + orderEntity.getOrderNum() + "订货方已经取消，备注：" + remark + "。 请及时查看处理！", "7");
//                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("取消订单后，发送手机推送消息失败" + e.getMessage(), e);
            }

            //发送网页推送消息
            try {
                //用户取消订单
//                if (!isFactoryUser(loginUser)) {
                    //给接单员推送
                    messageEventHandler.sendHandleOrderEvent(loginUser.getCompanySeq().toString(), PermissionKeys.ORDER_RECEIVE, "订单：" + orderEntity.getOrderNum() + "订货方已经取消，备注：" + remark + "。 请及时查看处理！", orderSeq, 7);
//                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("取消订单后，发送网页推送消息失败" + e.getMessage(), e);
            }

            //保存推送消息
            try {
                //工厂取消订单
//                if (isFactoryUser(loginUser)) {
                    //保存用户推送
                    pushRecordService.addPushRecord(loginUser.getSeq(), submitUser.getSeq(), submitUser.getAccountName(), 1, orderSeq, "您的订单：" + orderEntity.getOrderNum() + "已被商家取消，备注：" + remark + "。 如有疑问请及时联系客服！");
//                } else {  //用户取消订单
//                    //保存接单员推送
//                    for (BaseUserEntity factoryUser : factoryUserList) {
//                        pushRecordService.addPushRecord(loginUser.getSeq(), factoryUser.getSeq(), factoryUser.getAccountName(), 1, orderSeq, "订单：" + orderEntity.getOrderNum() + "订货方已经取消，备注：" + remark + "。 请及时查看处理！");
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("取消订单后，保存推送消息失败" + e.getMessage(), e);
            }

            return R.ok("取消订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }


    /**
     * 修改订单留言、要求到货日期
     */
    @Login
    @GetMapping("updateOrderSuggestion")
    @ApiOperation(value = "修改订单留言、要求到货日期", notes = "只有未接单的订单可以修改留言和要求到货日期")
    public R updateOrderSuggestion(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
                                   @ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
                                   @ApiParam("要求到货时间") @RequestParam(value = "requireTime", required = false) Date requireTime,
                                   @ApiParam("留言") @RequestParam(value = "suggestion", required = false) String suggestion,
                                   HttpServletRequest request) {
        try {
            if (requireTime != null || suggestion != null) {
                orderService.updateOrderSuggestion(orderSeq, requireTime, suggestion);
            }
            return R.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error(HttpStatus.SC_FORBIDDEN, "修改订单失败");
        }
    }


    /**
     * 接单
     */
    @Login({PermissionKeys.ORDER_RECEIVE})
    @PostMapping("orderConfirmed")
    @ApiOperation("工厂接单员接单（确认订单 0 -> 1）")
    public R orderConfirmed(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                            @ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
                            HttpServletRequest request) {
        try {
            /**涉及到需要修改的表：
             YHSR_OP_Order (订单信息表)				修改订单状态
             **/
            try {
                //确认订单
                OrderEntity orderEntity = orderService.getOrderBySeq(orderSeq);
                if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_ZERO.getValue()) {
                    orderService.orderConfirmed(orderSeq);
                } else {
                    return R.error(HttpStatus.SC_FORBIDDEN, "只有待确认的订单可以执行此操作");
                }

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
                    messageEventHandler.sendHandleOrderEvent(loginUser.getCompanySeq().toString(), PermissionKeys.ORDER_CHECK, "您有新订单款项需审核，请及时处理！订单号：" + orderEntity.getOrderNum(), orderSeq, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("确认订单后，发送网页推送消息失败" + e.getMessage(), e);
                }

                //保存推送消息
                try {
                    //保存用户推送
                    pushRecordService.addPushRecord(loginUser.getSeq(), submitOrderUser.getSeq(), submitOrderUser.getAccountName(), 1, orderSeq, "您的订单：" + orderEntity.getOrderNum() + "已被接单，请及时与财务交洽付款！");
                    //保存财务推送
                    for (BaseUserEntity factoryUser : factoryUserList) {
                        pushRecordService.addPushRecord(loginUser.getSeq(), factoryUser.getSeq(), factoryUser.getAccountName(), 1, orderSeq, "您有新订单款项需审核，请及时处理！订单号：" + orderEntity.getOrderNum());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("确认订单后，保存推送消息失败" + e.getMessage(), e);
                }

                return R.ok("确认订单成功");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
                return R.error(HttpStatus.SC_FORBIDDEN, "确认订单失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }


    /**
     * 审核
     */
    @Login({PermissionKeys.ORDER_CHECK})
    @PostMapping("orderConfirmedPay")
    @ApiOperation("财务审核订单（确认支付 1 -> 2）")
    public R orderConfirmedPay(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                               @ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
                               @ApiParam("已付金额") @RequestParam("paid") BigDecimal paid) {
        try {
            /**涉及到需要修改的表：
             YHSR_OP_Order (订单信息表)				修改订单状态
             **/
            try {
                if (paid != null && paid.compareTo(BigDecimal.valueOf(0)) >= 0) {
                    orderService.updateOrderPaid(orderSeq, paid);
                } else {
                    return R.error("已付金额有误，审核失败");
                }

                OrderEntity orderEntity = orderService.getOrderBySeq(orderSeq);
                if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_ONE.getValue()) {
                    orderService.orderConfirmedPay(orderSeq, paid, loginUser.getCompanySeq());
                } else {
                    return R.error(HttpStatus.SC_FORBIDDEN, "只有待审核的订单可以执行此操作");
                }

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
                        messageEventHandler.sendHandleOrderEvent(loginUser.getCompanySeq().toString(), PermissionKeys.ORDER_STORE, "订单：" + orderEntity.getOrderNum() + "已通过财务审核，请及时出库发货！", orderSeq, 2);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("审核订单后，发送网页推送消息失败" + e.getMessage(), e);
                    }

                    //保存推送消息
                    try {
                        //保存用户推送
                        pushRecordService.addPushRecord(loginUser.getSeq(), submitOrderUser.getSeq(), submitOrderUser.getAccountName(), 1, orderSeq, "您的订单：" + orderEntity.getOrderNum() + "已被确认支付" + paid + "元。" + userMessage);
                        //保存入库员推送
                        for (BaseUserEntity factoryUser : factoryUserList) {
                            pushRecordService.addPushRecord(loginUser.getSeq(), factoryUser.getSeq(), factoryUser.getAccountName(), 1, orderSeq, "订单：" + orderEntity.getOrderNum() + "已通过财务审核，请及时出库发货！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("审核订单后，保存推送消息失败" + e.getMessage(), e);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("审核订单后，推送消息处理异常" + e.getMessage(), e);
                }

                return R.ok("审核成功");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
                return R.error(HttpStatus.SC_FORBIDDEN, "审核失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }


    /**
     * 修改订单已付金额
     */
    @Login({PermissionKeys.ORDER_CHECK})
    @PostMapping("updateOrderPaid")
    @ApiOperation("财务修改订单已付金额")
    public R updateOrderPaid(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                             @ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
                             @ApiParam("本次付款金额") @RequestParam("paid") BigDecimal paid) {
        try {

            OrderEntity orderEntity = orderService.getOrderBySeq(orderSeq);
            if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_ZERO.getValue() ||
                    orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_ONE.getValue() ||
                    orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_SEVEN.getValue()) {
                return R.error(HttpStatus.SC_FORBIDDEN, "当前订单状态不可以修改已付金额");
            }

            if (paid != null && paid.compareTo(BigDecimal.valueOf(0)) > 0) {
                orderService.updateOrderPaid(orderSeq, paid);
            } else {
                return R.error("金额有误，修改失败");
            }
            return R.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("修改失败");
        }

    }


    /**
     * 入库
     */
    @Login({PermissionKeys.ORDER_STORE})
    @PostMapping("orderStore")
    @ApiOperation("仓库管理员入库（订单商品入库 2 -> 3）")
    public R orderStore(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                        @ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
                        HttpServletRequest request) {
        try {
            /**涉及到需要修改的表：
             YHSR_OP_Order (订单信息表)				修改订单状态
             **/
            try {
                OrderEntity orderEntity = orderService.getOrderBySeq(orderSeq);
                if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_TWO.getValue()) {
                    orderService.orderStore(orderSeq);
                } else {
                    return R.error(HttpStatus.SC_FORBIDDEN, "只有待入库的订单可以执行此操作");
                }

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
                    messageEventHandler.sendHandleOrderEvent(loginUser.getCompanySeq().toString(), PermissionKeys.ORDER_DELIVER, "订单：" + orderEntity.getOrderNum() + "已入库，请及时发货", orderSeq, 3);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("订单入库后，发送网页推送消息失败" + e.getMessage(), e);
                }

                //保存推送消息
                try {
                    //保存用户推送
                    pushRecordService.addPushRecord(loginUser.getSeq(), submitOrderUser.getSeq(), submitOrderUser.getAccountName(), 1, orderSeq, "您的订单：" + orderEntity.getOrderNum() + "已被商家入库，即将准备发货");
                    //保存发货员推送
                    for (BaseUserEntity factoryUser : factoryUserList) {
                        pushRecordService.addPushRecord(loginUser.getSeq(), factoryUser.getSeq(), factoryUser.getAccountName(), 1, orderSeq, "订单：" + orderEntity.getOrderNum() + "已入库，请及时发货");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("订单入库后，保存推送消息失败" + e.getMessage(), e);
                }

                return R.ok("入库成功");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
                return R.error(HttpStatus.SC_FORBIDDEN, "入库失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }


    /**
     * 获取所有快递公司信息
     */
    @Login
    @GetMapping("expressCompanyList")
    @ApiOperation("快递公司信息列表")
    public R expressCompanyList(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
        try {
            return R.ok(orderService.getExpressCompanyList());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 部分发货
     */
    @Login({PermissionKeys.ORDER_DELIVER})
    @PostMapping("orderDeliver")
    @ApiOperation("订单(部分)发货（3 -> 4 / 3 -> 5 / 4 -> 5）")
    public R orderDeliverGoods(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                               @ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
                               @ApiParam("快递公司序号") @RequestParam(value = "expressCompanySeq", required = false) Integer expressCompanySeq,
                               @ApiParam("快递单号") @RequestParam(value = "expressNo", required = false) String expressNo,
                               @ApiParam("快递单图片") @RequestParam(value = "expressImg", required = false) MultipartFile expressImg,
                               @ApiParam("订单商品发货量") @RequestParam("productsNumJsonArr") String productsNumJsonArr,
                               HttpServletRequest request) {
        try {
            /**涉及到需要修改的表：
             YHSR_OP_Order (订单信息表)					判断并修改订单状态
             YHSR_OP_OrderProducts(订单产品表)  			修改多条商品发货量
             YHSR_OP_OrderExpress(订单快递表)			新增一条快递记录
             YHSR_OP_OrderExpressDetails(订单快递详情表) 新增多条商品发货数量记录
             **/


            try {
                //判断订单状态是否正确
                OrderEntity orderEntity = orderService.getOrderBySeq(orderSeq);
                if (orderEntity.getOrderStatus() != OrderStatusEnum.ORDSTATUS_THREE.getValue() && orderEntity.getOrderStatus() != OrderStatusEnum.ORDSTATUS_FOUR.getValue()) {
                    return R.error(HttpStatus.SC_FORBIDDEN, "只有未发货的订单可以执行此操作");
                }

                // 时间处理
                Calendar calendar = Calendar.getInstance();
                Date nowDate = calendar.getTime();
                calendar.add(Calendar.DATE, 10);
                Date autoReceiveDate = calendar.getTime();

                JSONArray jsonArray = JSONArray.fromObject(productsNumJsonArr);//[{orderProductsSeq:5,num:3},{orderProductsSeq:10,num:4}]
                // 1. 修改订单状态
                OrderEntity order = new OrderEntity();
                order.setSeq(orderSeq);
                //在service中，修改好orderproducts之后再查询判断是否是完全发货
                order.setDeliverTime(nowDate);
                order.setReceiveTime(autoReceiveDate);

                // 2. 修改多条订单商品已发货量
                List<OrderProductsEntity> orderProductsList = new ArrayList<OrderProductsEntity>();
                // 4. 新增多条快递商品发货记录
                List<OrderExpressDetailsEntity> orderExpressDetailsList = new ArrayList<OrderExpressDetailsEntity>();

                int totalNum = 0; //发货总量
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int orderProductsSeq = jsonObject.getInt("orderProductsSeq");
                    int num = jsonObject.getInt("num");
                    totalNum = totalNum + num;
                    OrderProductsEntity orderProductsEntity = orderService.getOrderProductsBySeq(orderProductsSeq);

                    //判断发货量是否正确
                    if (orderProductsEntity.getBuyCount() - orderProductsEntity.getDeliverNum() - num < 0) {
                        return R.error(HttpStatus.SC_FORBIDDEN, "发货量不能超过购买量");
                    }

                    // 2.商品已发货量 + num
                    OrderProductsEntity orderProducts = new OrderProductsEntity();
                    orderProducts.setSeq(orderProductsSeq);
                    orderProducts.setDeliverNum(orderProductsEntity.getDeliverNum() + num);
                    orderProductsList.add(orderProducts);
                    // 4. 快递商品发货记录
                    OrderExpressDetailsEntity orderExpressDetails = new OrderExpressDetailsEntity();
                    orderExpressDetails.setOrderProductsSeq(orderProductsSeq);
                    orderExpressDetails.setOrderSeq(orderProductsEntity.getOrderSeq());
                    orderExpressDetails.setShoesDataSeq(orderProductsEntity.getShoesDataSeq());
                    orderExpressDetails.setNum(num);
                    orderExpressDetails.setInputTime(nowDate);
                    orderExpressDetails.setDel(0);
                    orderExpressDetailsList.add(orderExpressDetails);

                }

                //3.新增一条快递记录
                OrderExpressEntity orderExpressEntity = new OrderExpressEntity();
                orderExpressEntity.setOrderSeq(orderSeq);
                if (expressCompanySeq != null && expressNo != null && !expressNo.equals("")) {
                    orderExpressEntity.setExpressCompanySeq(expressCompanySeq);
                    orderExpressEntity.setExpressNo(expressNo);
                } else if (expressImg != null) {
                    String expressImgName;
                    try {
                        String uploadUrl = getOrderExpressUploadUrl(request, orderSeq.toString());
                        expressImgName = upLoadFile(loginUser.getSeq(), uploadUrl, expressImg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("订单发货，图片上传失败" + e.getMessage(), e);
                        return R.error("图片上传失败，请检查图片格式");
                    }
                    orderExpressEntity.setExpressImg(expressImgName);
                } else {
                    return R.error(HttpStatus.SC_FORBIDDEN, "请填写快递信息或上传物流图片");
                }
                orderExpressEntity.setInputTime(nowDate);
                orderExpressEntity.setDel(0);

                Integer orderStatus = orderService.orderDeliverGoods(order, orderProductsList, orderExpressEntity, orderExpressDetailsList);


                //推送消息处理
                try {
                    //给用户推送的内容
                    String userMessage = "";
                    String messageType = "";
                    if (orderStatus == OrderStatusEnum.ORDSTATUS_FOUR.getValue()) {
                        userMessage = "您的货品已部分出库发货，剩余货品会及时发出，请及时关注物流信息";
                        messageType = "4";
                    } else if (orderStatus == OrderStatusEnum.ORDSTATUS_FIVE.getValue()) {
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
                        pushRecordService.addPushRecord(loginUser.getSeq(), submitOrderUser.getSeq(), submitOrderUser.getAccountName(), 1, orderSeq, "您的订单：" + orderEntity.getOrderNum() + "商家已发货，本次共发货" + totalNum + "件。" + userMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("订单发货后，保存推送消息失败" + e.getMessage(), e);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("订单发货后，推送消息处理异常" + e.getMessage(), e);
                }

                return R.ok("发货成功");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
                return R.error(HttpStatus.SC_FORBIDDEN, "发货失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }


    /**
     * 确认收货
     */
    @Login
    @PostMapping("orderConfirmReceived")
    @ApiOperation("订单确认收货")
    public R orderConfirmReceived(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                                  @ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
                                  HttpServletRequest request) {
        try {
            /**涉及到需要修改的表：
             YHSR_OP_Order (订单信息表)				修改订单状态
             **/
            //确认收货
            OrderEntity orderEntity = orderService.getOrderBySeq(orderSeq);

            if (!orderEntity.getUserSeq().equals(loginUser.getSeq())) {
                return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
            }

            if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_FIVE.getValue()) {
                orderService.orderConfirmReceived(orderSeq);
            } else {
                return R.error(HttpStatus.SC_FORBIDDEN, "只有已发货的订单可以执行此操作");
            }


            //发送手机推送消息
            List<BaseUserEntity> factoryUserList = null;  //工厂所有发货人
            try {
                //给发货员推送
                factoryUserList = baseService.getUserByPermission(loginUser, new String[]{PermissionKeys.ORDER_DELIVER});
                List<String> aliasList = new ArrayList<String>();
                for (BaseUserEntity factoryUser : factoryUserList) {
                    aliasList.add(factoryUser.getAccountName());
                }
                jPushUtils.sendPush(aliasList, "订单：" + orderEntity.getOrderNum() + "已被用户确认收货", "6");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("确认收货后，发送手机推送消息失败" + e.getMessage(), e);
            }

            //发送网页推送消息
            try {
                //给发货员推送
                messageEventHandler.sendHandleOrderEvent(loginUser.getCompanySeq().toString(), PermissionKeys.ORDER_DELIVER, "订单：" + orderEntity.getOrderNum() + "已被用户确认收货", orderSeq, 6);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("确认收货后，发送网页推送消息失败" + e.getMessage(), e);
            }

            //保存推送消息
            try {
                //保存发货员推送
                for (BaseUserEntity factoryUser : factoryUserList) {
                    pushRecordService.addPushRecord(loginUser.getSeq(), factoryUser.getSeq(), factoryUser.getAccountName(), 1, orderSeq, "订单：" + orderEntity.getOrderNum() + "已被用户确认收货");
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("确认收货后，保存推送消息失败" + e.getMessage(), e);
            }

            return R.ok("确认收货成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }


    /**
     * 删除订单
     */
    @Login
    @GetMapping("deleteOrder")
    @ApiOperation("删除订单")
    public R deleteOrder(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
                         @ApiParam("订单编号") @RequestParam("orderSeq") Integer orderSeq,
                         HttpServletRequest request) {
        try {

            /**涉及到需要修改的表：
             YHSR_OP_Order (订单信息表)				删除订单
             YHSR_OP_OrderProducts（订单关联产品表）  删除购买的商品列表
             **/
            OrderEntity orderEntity = orderService.getOrderBySeq(orderSeq);

            if (!orderEntity.getUserSeq().equals(userSeq)) {
                return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
            }

            if (orderEntity.getOrderStatus() == OrderStatusEnum.ORDSTATUS_SEVEN.getValue()) {
                orderService.deleteOrder(orderSeq);
            } else {
                return R.error(HttpStatus.SC_FORBIDDEN, "只有已取消的订单可以删除");
            }
            return R.ok("删除订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error(HttpStatus.SC_FORBIDDEN, "删除订单失败");
        }
    }


    /**
     * 修改订单
     */
    @Login
    @PostMapping("changeOrder")
    @ApiOperation(value = "修改订单", notes = "提供客户修改未被厂家接单的订单功能（增加、减少购买数量；减少购买货号）")
    public R changeOrder(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
                         @ApiParam("订单seq") @RequestParam("orderSeq") Integer orderSeq,
                         @ApiParam("订单商品列表（修改后的）") @RequestParam(value = "orderProductBuyCountList") String orderProductBuyCountList, //[{orderProductsSeq:5,buyCount:2},{orderProductsSeq:10,buyCount:1}]
                         @ApiParam("订单价格") @RequestParam("orderPrice") BigDecimal orderPrice,
                         HttpServletRequest request) {
        try {
            /**涉及到需要修改的表
             YHSR_OP_Order (订单信息表)				修改订单价格
             YHSR_OP_OrderProducts（订单关联产品表）  修改订单关联产品（只支持修改购买数量，修改后没有的算删除）
             YHSR_OP_ShoesData(订货平台鞋子数据表) 	修改库存
             **/
            OrderEntity oldOrderEntity = orderService.getOrderBySeq(orderSeq);

            if (!oldOrderEntity.getUserSeq().equals(userSeq)) {
                return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
            }
            if (oldOrderEntity.getOrderStatus() != OrderStatusEnum.ORDSTATUS_ZERO.getValue()) {
                return R.error(HttpStatus.SC_FORBIDDEN, "订单已确认，不可修改");
            }

            List<OrderProductsEntity> oldOrderProductsList = orderService.getOrderProductsListByOrderSeq(orderSeq);
            JSONArray jsonArray = JSONArray.fromObject(orderProductBuyCountList);
            JSONObject jsonObject;

            //1.订单价格
            BigDecimal price = BigDecimal.valueOf(0);
            //2.订单关联产品对象List
            List<OrderProductsEntity> orderProductsChangeList = new ArrayList<OrderProductsEntity>();
            OrderProductsEntity orderProductsEntity;
            //3.库存变动List
            List<Map<String, Integer>> stockChangeList = new ArrayList<Map<String, Integer>>();
            Map<String, Integer> stockChangeMap;
            boolean noneProductFlag = true; //修改后是否存在商品
            for (OrderProductsEntity oldOrderProducts : oldOrderProductsList) {
                int orderProductsSeq = oldOrderProducts.getSeq();
                int shoesDataSeq = oldOrderProducts.getShoesDataSeq();
                int oldBuyCount = oldOrderProducts.getBuyCount();
                BigDecimal productPrice = oldOrderProducts.getProductPrice();

                //判断本次该订单商品有没有传过来，有就修改，并修改库存，没有就删除
                boolean existFlag = false; //修改后订单商品是否存在
                for (int i = 0; i < jsonArray.size(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getInt("orderProductsSeq") == orderProductsSeq) {
                        if (jsonObject.getInt("buyCount") > 0) {
                            noneProductFlag = false;
                        }
                        //1.鞋子价格累计
                        price = price.add(productPrice.multiply(BigDecimal.valueOf(jsonObject.getInt("buyCount"))));

                        //2.修改订单关联的产品
                        orderProductsEntity = new OrderProductsEntity();
                        orderProductsEntity.setSeq(orderProductsSeq);
                        orderProductsEntity.setBuyCount(jsonObject.getInt("buyCount"));
                        orderProductsChangeList.add(orderProductsEntity);

                        //3.库存变动
                        stockChangeMap = new HashMap<String, Integer>();
                        stockChangeMap.put("shoesDataSeq", shoesDataSeq);
                        stockChangeMap.put("changNum", oldBuyCount - jsonObject.getInt("buyCount"));
                        stockChangeList.add(stockChangeMap);

                        //标记为存在
                        existFlag = true;
                        break;
                    }
                }

                //修改后订单商品不存在，删除
                if (!existFlag) {
                    //2.删除订单关联的产品
                    orderProductsEntity = new OrderProductsEntity();
                    orderProductsEntity.setSeq(orderProductsSeq);
                    orderProductsEntity.setBuyCount(0);
                    orderProductsEntity.setDel(1);
                    orderProductsChangeList.add(orderProductsEntity);

                    //3.库存变动
                    stockChangeMap = new HashMap<String, Integer>();
                    stockChangeMap.put("shoesDataSeq", shoesDataSeq);
                    stockChangeMap.put("changNum", oldBuyCount);
                    stockChangeList.add(stockChangeMap);
                }


            }

            //一件商品都不存在
            if (noneProductFlag) {
                return R.error(HttpStatus.SC_FORBIDDEN, "请至少选择一件商品！");
            }
            //判断价格是否和前端传过来的一致
            if (price.compareTo(orderPrice) != 0) {
                return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您选择的商品价格已过期，请刷新后再试");
            }

            //1.修改订单价格
            OrderEntity orderChangeEntity = new OrderEntity();
            orderChangeEntity.setSeq(orderSeq);
            orderChangeEntity.setOrderPrice(price);

            //修改订单
            try {
                orderService.changeOrder(orderChangeEntity, orderProductsChangeList, stockChangeList);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
                if (e.getMessage().equals("库存不足")) {
                    return R.error(HttpStatus.SC_FORBIDDEN, "修改订单失败，库存不足");
                } else {
                    return R.error(HttpStatus.SC_FORBIDDEN, "修改订单失败");
                }
            }

            return R.ok().put("msg", "修改订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }


}
