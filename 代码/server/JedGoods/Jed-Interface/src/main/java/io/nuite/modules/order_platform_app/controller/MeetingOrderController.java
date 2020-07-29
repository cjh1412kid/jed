package io.nuite.modules.order_platform_app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.common.utils.Constant;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.dao.MessageDao;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import io.nuite.modules.order_platform_app.service.MeetingOrderDetailService;
import io.nuite.modules.order_platform_app.service.MeetingOrderService;
import io.nuite.modules.order_platform_app.utils.JPushUtils;
import io.nuite.modules.order_platform_app.utils.MeetingOrderStatusEnum;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import io.nuite.modules.sr_base.service.BaseShopService;
import io.nuite.modules.system.entity.order_platform.ShopAllotOrderDetailEntity;
import io.nuite.modules.system.service.SystemGoodsSizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;


/**
 * jrd订货会订单
 * @author yy
 */
@RestController
@RequestMapping("/order/app/meetingOrder")
@Api(tags="jrd订货会订单接口")
public class MeetingOrderController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
    @Autowired
    private MeetingOrderService meetingOrderService;
    
    @Autowired
    private MeetingOrderDetailService meetingOrderDetailService;
    
    @Autowired
    private SystemGoodsSizeService goodsSizeService;
    
    @Autowired
    private BaseShopService baseShopService;
    
    @Autowired
    private JPushUtils jPushUtils;
    
	@Autowired
	private BaseShopService shopService;
	
	@Autowired
	private MessageDao messageDao;

	
	
    
    
    /**
     * 	订单列表(弃用)
     */
    @Deprecated
    @Login
    @GetMapping("orderList")
    public R orderList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("年份") @RequestParam("year") Integer year,
    		@ApiParam("季节序号") @RequestParam("seasonSeq") Integer seasonSeq,
    		@ApiParam("订单状态(0：未发货、1：部分发货、 2：已发货、3：已取消)") @RequestParam("orderStatus") Integer orderStatus) {
    	try {
    		
    		List<Map<String, Object>> meetingOrderList = null;
    		
    		
			if(loginUser.getRoleCode().equals(Constant.Role.FACTORY_ADMIN.getCode()) 
					|| loginUser.getRoleCode().equals(Constant.Role.FACTORY_USER.getCode())) { // 公司账号
				
				//查询公司全部订单
				meetingOrderList = meetingOrderService.getHqMeetingOrder(loginUser.getCompanySeq(), year, seasonSeq, orderStatus);
				
			} else if(loginUser.getRoleCode().equals(Constant.Role.SHOP_ADMIN.getCode()) && loginUser.getShopSeq() != null) {  // 门店账号
				
				
				//查询本门店订单
				meetingOrderList = meetingOrderService.getShopMeetingOrder(loginUser.getCompanySeq(), loginUser.getShopSeq(), year, seasonSeq, orderStatus);

			} else {
				return R.error();
			}
    		
			
			
			for(Map<String, Object> meetingOrderMap : meetingOrderList) { //添加订单详情
				
				Integer meetingOrderSeq = (Integer)meetingOrderMap.get("seq");
				
				//门店名称、收货地址
				Integer shopSeq = (Integer)meetingOrderMap.get("shopSeq");
				BaseShopEntity baseShopEntity = baseShopService.selectById(shopSeq);
				meetingOrderMap.put("shopName", baseShopEntity.getName());
				meetingOrderMap.put("shopAddress", baseShopEntity.getAddress());
				
				//订单状态中文
				Integer status = (Integer)meetingOrderMap.get("orderStatus");
				meetingOrderMap.put("statusName", MeetingOrderStatusEnum.get(status).getFactoryName());
				
				
				//订单中每个货号的订货量、发货量、尺码范围
				List<Map<String, Object>> goodIdNumList = meetingOrderDetailService.getOrderGoodIdNum(meetingOrderSeq);

				for(Map<String, Object> goodIdNumMap : goodIdNumList) {
					//尺码范围
					Integer minSizeSeq = (Integer)goodIdNumMap.get("minSizeSeq");
					GoodsSizeEntity minGoodsSizeEntity = goodsSizeService.selectById(minSizeSeq);
					Integer maxSizeSeq = (Integer)goodIdNumMap.get("maxSizeSeq");
					GoodsSizeEntity maxGoodsSizeEntity = goodsSizeService.selectById(maxSizeSeq);
					goodIdNumMap.put("sizeRange", minGoodsSizeEntity.getSizeName() + " ~ " + maxGoodsSizeEntity.getSizeName());
					
					//某一货号中各个尺码的订货量、发货量
					Integer orderShoesSeq = (Integer)goodIdNumMap.get("orderShoesSeq");
					List<Map<String, Object>> sizeNumList = meetingOrderDetailService.getGoodIdSizeNum(meetingOrderSeq, orderShoesSeq);
					for(Map<String, Object> sizeNumMap : sizeNumList) {
						//查询尺码
						Integer sizeSeq = (Integer)sizeNumMap.get("sizeSeq");
						GoodsSizeEntity goodsSizeEntity = goodsSizeService.selectById(sizeSeq);
						sizeNumMap.put("sizeName", goodsSizeEntity.getSizeName());
					}
					
					goodIdNumMap.put("sizeNumList", sizeNumList);
					
				}
				
				meetingOrderMap.put("goodIdNumlist", goodIdNumList);
				
			}
			
			return R.ok(meetingOrderList);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    
    
    
    
    /**
     * 	订单列表
     */
    @Login
    @GetMapping("meetingOrderList")
    @ApiOperation("订单列表")
    public R meetingOrderList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("年份") @RequestParam("year") Integer year,
    		@ApiParam("季节序号") @RequestParam("seasonSeqList") List<Integer> seasonSeqList,
    		@ApiParam("订单状态(0：未完成、1：已完成)") @RequestParam("orderState") Integer orderState) {
    	try {
    		
			if(loginUser.getRoleCode().equals(Constant.Role.FACTORY_ADMIN.getCode()) 
					|| loginUser.getRoleCode().equals(Constant.Role.FACTORY_USER.getCode())) { // 公司账号
				
				//查询公司全部订单
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				
	    		//获取所有门店
	    		List<BaseShopEntity> shopList = shopService.getShopsByCompanySeq(loginUser.getCompanySeq());
				for(BaseShopEntity shopEntity : shopList) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("shopSeq", shopEntity.getSeq());
					resultMap.put("shopName", shopEntity.getName());
					
					
					//查询本门店本次订货会的订单：货号-分配数量（即筛板分配表中已完成配码的数据）
					List<Map<String, Object>> orderList = meetingOrderService.getShopMeetingOrderList(loginUser.getCompanySeq(), shopEntity.getSeq(), year, seasonSeqList);
					
					for(int i = 0; i < orderList.size(); i++) {
						Map<String, Object> orderMap = orderList.get(i);
						
						//查询订单详情，并判断订单状态（已完成、未完成），根据状态筛选条件去除不满足的订单
						Integer shopAllotOrderSeq = (Integer) orderMap.get("shopAllotOrderSeq");
						
						List<ShopAllotOrderDetailEntity> list = meetingOrderService.getShopMeetingOrderDetail(shopAllotOrderSeq);
						
						Integer orderStatus = 1;
						Integer totalAllotCount = 0;  //总配码量
						Integer totalInNum = 0;	 //总进货量
						for(ShopAllotOrderDetailEntity shopAllotOrderDetailEntity : list) {
							//判断本尺码的鞋子是否全部发货
							
							//此尺码的配码的数量
							Integer allotCount = shopAllotOrderDetailEntity.getCount();
							totalAllotCount += allotCount;
							//查询门店此鞋子此尺码的进货量
								//1.根据尺码的值，查询尺码序号
							Integer sizeSeq = meetingOrderService.getSizeSeqBySize(loginUser.getCompanySeq(), shopAllotOrderDetailEntity.getSize());
								//2.根据货号，获取鞋子序号
							Integer shoesSeq = meetingOrderService.getShoesSeqByGoodId(loginUser.getCompanySeq(), (String)orderMap.get("goodId"));
							
							Integer inNum = 0;
							if(sizeSeq != null && shoesSeq != null) {
								inNum = meetingOrderService.getOrderShoesShopInNum(shopEntity.getSeq(), shoesSeq, sizeSeq);
							}
							shopAllotOrderDetailEntity.setInNum(inNum);
							totalInNum += inNum;
							
							if(allotCount > inNum) {
								orderStatus = 0;
							}
						}
						
						orderMap.put("orderStatus", orderStatus);
						orderMap.put("totalAllotCount", totalAllotCount);
						orderMap.put("totalInNum", totalInNum);
						orderMap.put("orderDetail", list);
						
						
						//如果订单状态不符合筛选条件，去除
						if(!orderState.equals(orderStatus)) {
							orderList.remove(i);
							i--;
						}
						
					}
					
					resultMap.put("orderList", orderList);

					//没有满足条件订单的门店不返回（本身没有配码后订单或者不满足是否完成条件）
					if(orderList.size() > 0) {
						resultList.add(resultMap);
					}
				}
				
				return R.ok(resultList);
				
				
			} else if(loginUser.getRoleCode().equals(Constant.Role.SHOP_ADMIN.getCode()) && loginUser.getShopSeq() != null) {  // 门店账号
				
				//查询本门店订单
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				
	    		//获取门店
	    		BaseShopEntity shopEntity = shopService.selectById(loginUser.getShopSeq());
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("shopSeq", shopEntity.getSeq());
				resultMap.put("shopName", shopEntity.getName());
					
					
				//查询本门店本次订货会的订单：货号-分配数量（即筛板分配表中已完成配码的数据）
				List<Map<String, Object>> orderList = meetingOrderService.getShopMeetingOrderList(loginUser.getCompanySeq(), shopEntity.getSeq(), year, seasonSeqList);
					
				for(int i = 0; i < orderList.size(); i++) {
					Map<String, Object> orderMap = orderList.get(i);
						
					//查询订单详情，并判断订单状态（已完成、未完成），根据状态筛选条件去除不满足的订单
					Integer shopAllotOrderSeq = (Integer) orderMap.get("shopAllotOrderSeq");
						
					List<ShopAllotOrderDetailEntity> list = meetingOrderService.getShopMeetingOrderDetail(shopAllotOrderSeq);
						
					Integer orderStatus = 1;
					Integer totalAllotCount = 0;  //总配码量
					Integer totalInNum = 0;	 //总进货量
					for(ShopAllotOrderDetailEntity shopAllotOrderDetailEntity : list) {
						//判断本尺码的鞋子是否全部发货
							
						//此尺码的配码的数量
						Integer allotCount = shopAllotOrderDetailEntity.getCount();
						totalAllotCount += allotCount;
						//查询门店此鞋子此尺码的进货量
								//1.根据尺码的值，查询尺码序号
						Integer sizeSeq = meetingOrderService.getSizeSeqBySize(loginUser.getCompanySeq(), shopAllotOrderDetailEntity.getSize());
								//2.根据货号，获取鞋子序号
						Integer shoesSeq = meetingOrderService.getShoesSeqByGoodId(loginUser.getCompanySeq(), (String)orderMap.get("goodId"));
							
						Integer inNum = 0;
						if(sizeSeq != null && shoesSeq != null) {
							inNum = meetingOrderService.getOrderShoesShopInNum(shopEntity.getSeq(), shoesSeq, sizeSeq);
						}
						shopAllotOrderDetailEntity.setInNum(inNum);
						totalInNum += inNum;
						
						if(allotCount > inNum) {
							orderStatus = 0;
						}
					}
						
					orderMap.put("orderStatus", orderStatus);
					orderMap.put("totalAllotCount", totalAllotCount);
					orderMap.put("totalInNum", totalInNum);
					orderMap.put("orderDetail", list);
						
						
					//如果订单状态不符合筛选条件，去除
					if(!orderState.equals(orderStatus)) {
						orderList.remove(i);
						i--;
					}
					
				}
					
				resultMap.put("orderList", orderList);
				resultList.add(resultMap);
				return R.ok(resultList);
				
			} else {
				return R.ok();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    
    
    
    
    
    
    /**
     * 	催单接口
     */
    @Login
    @GetMapping("reminderOrder")
    @ApiOperation("催单接口")
    public R reminderOrder(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("门店名称") @RequestParam("shopName") String shopName,
    		@ApiParam("货号") @RequestParam(value="goodId", required = false) String goodId) {
    	try {
			if(loginUser.getRoleCode().equals(Constant.Role.FACTORY_ADMIN.getCode()) 
					|| loginUser.getRoleCode().equals(Constant.Role.FACTORY_USER.getCode())) { // 公司账号
				
				return R.error("该功能仅限门店账号使用");
				
			} else if(loginUser.getRoleCode().equals(Constant.Role.SHOP_ADMIN.getCode()) && loginUser.getShopSeq() != null) {  // 门店账号
			    
				String message = shopName + " 催单了！请尽快发货";
				
				//所有总部用户
			    List<BaseUserEntity> userList = meetingOrderService.getHqUserList(loginUser.getCompanySeq());
			    
			    List<String> aliasList = new ArrayList<String>();
			    for (BaseUserEntity user : userList) {
			    	
					//存入消息表
					MessageEntity messageEntity = new MessageEntity();
					messageEntity.setTitle("催单消息");
					messageEntity.setContent(message);
					messageEntity.setUserSeq(user.getSeq());
					messageEntity.setInputTime(new Date());
					messageEntity.setDel(0);
					messageEntity.setType(3);
					messageEntity.setCreator(loginUser.getSeq());
					messageEntity.setIsRead(0);
					messageDao.insert(messageEntity);
			    	
					//给总部用户推送
			    	aliasList.add(user.getAccountName());
			    }
			    jPushUtils.sendPush(aliasList, message, "0");
			    
			}
			
			return R.ok("已通知管理员");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    

}
