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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.dao.SievePlateDao;
import io.nuite.modules.order_platform_app.entity.MeetingPlanEntity;
import io.nuite.modules.order_platform_app.entity.SievePlateDistributeEntity;
import io.nuite.modules.order_platform_app.entity.SievePlateEntity;
import io.nuite.modules.order_platform_app.service.MeetingPlanService;
import io.nuite.modules.order_platform_app.service.SievePlateDistributeService;
import io.nuite.modules.order_platform_app.service.SievePlateService;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.service.BaseShopService;
import io.nuite.modules.sr_base.service.GoodsCategoryService;
import io.nuite.modules.system.entity.OrderManageEntity;
import io.nuite.modules.system.entity.order_platform.ShopAllotOrderEntity;
import io.nuite.modules.system.service.OrderPlanManageService;
import io.nuite.modules.system.service.order_platform.ShopAllotOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;


/**
 * jrd订货会接口: 筛板、审核、生成订货会订单
 * @author yy
 */
@RestController
@RequestMapping("/order/app/sievePlate")
@Api(tags="jrd订货会接口: 筛板、审核、生成订货会订单")
public class SievePlateController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SievePlateDao sievePlateDao;
	
    @Autowired
    private SievePlateService sievePlateService;
    
    @Autowired
    private OrderPlanManageService orderPlanManageService;
    
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    
    @Autowired
    private SievePlateDistributeService sievePlateDistributeService;
    
    @Autowired
    private BaseShopService baseShopService;
    
    @Autowired
    private MeetingPlanService meetingPlanService;
    
    @Autowired
    private ShopAllotOrderService shopAllotOrderService;
    
    
    
	/**
	 * 加入筛板
	 */
	@Login
	@PostMapping("addToSievePlate")
	@ApiOperation("加入筛板")
	public R addToSievePlate(@ApiIgnore @LoginUser BaseUserEntity loginUser,
				@ApiParam("货号") @RequestParam("goodId") String goodId) {
		try {
			//查询用户是否已经加入过筛版
			SievePlateEntity sievePlateEntity = new SievePlateEntity();
			sievePlateEntity.setUserSeq(loginUser.getSeq());
			sievePlateEntity.setGoodID(goodId);
			sievePlateEntity = sievePlateDao.selectOne(sievePlateEntity);
			if(sievePlateEntity != null) {
				return R.ok("此货号已经加入过筛板");
			}
			
			
			//查询订货会鞋子信息
			OrderManageEntity orderManageEntity = orderPlanManageService.getOrderManageEntityByGoodId(goodId);
			if(orderManageEntity == null) {
				return R.error(403, "此货号不是订货会商品，无法加入筛板");
			}
			
			
			//加入筛板表
			sievePlateEntity = new SievePlateEntity();
			
//			Seq				int	0	0	0	-1	0	0		0	0	0	0	序号(主键)		-1			
//			Company_Seq		int	0	0	0	0	0	0		0	0	0	0	公司序号		0			
//			Year			int	0	0	0	0	0	0		0	0	0	0	年份		0			
//			Season_Seq		int	0	0	0	0	0	0		0	0	0	0	季节序号		0			
//			OrderShoes_Seq	int	0	0	0	0	0	0		0	0	0	0	订货会鞋子序号		0			
//			GoodID			varchar	50	0	-1	0	0	0		0	0	0	0	货号	Chinese_PRC_CI_AS	0			
//			Category_Seq	int	0	0	-1	0	0	0		0	0	0	0	分类序号		0			
//			User_Seq		int	0	0	0	0	0	0		0	0	0	0	用户序号		0			
//			InputTime		datetime	0	0	-1	0	0	0	(getdate())	0	0	0	0	入库时间		0			
//			Del				int	0	0	0	0	0	0	((0))	0	0	0	0	删除标识(0:未删除,1:已删除)		0			
			sievePlateEntity.setCompanySeq(loginUser.getCompanySeq());
			sievePlateEntity.setYear(orderManageEntity.getYear());
			sievePlateEntity.setSeasonSeq(orderManageEntity.getSeasonSeq());
			sievePlateEntity.setOrderShoesSeq(orderManageEntity.getSeq());
			sievePlateEntity.setGoodID(goodId);
			sievePlateEntity.setCategorySeq(orderManageEntity.getCategorySeq());
			sievePlateEntity.setUserSeq(loginUser.getSeq());
			sievePlateEntity.setInputTime(new Date());
			sievePlateEntity.setDel(0);
			sievePlateService.insert(sievePlateEntity);
			
			return R.ok("加入成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

	}
    
    
    
    /**
     * 	筛板数据统计排行（每个品类的货号种数）
     */
    @Login
    @GetMapping("sievePlateList")
    @ApiOperation("筛板数据统计排行")
    public R sievePlateList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("年份") @RequestParam("year") Integer year,
    		@ApiParam("季节序号") @RequestParam("seasonSeq") Integer seasonSeq) {
    	try {
    		List<Map<String, Object>> list = sievePlateService.getSievePlateList(loginUser.getCompanySeq(), year, seasonSeq);
    		
    		for(Map<String, Object> map: list) {
    			
    			//获取品类名称（categorySeq对应的分类名称）
    			Integer categorySeq = (Integer) map.get("categorySeq");
    			GoodsCategoryEntity goodsCategoryEntity = goodsCategoryService.selectById(categorySeq);
    			map.put("categoryName", goodsCategoryEntity.getName());
    			
    		}
    		
			return R.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    
    /**
     * 	品类详情（某品类每个货号的筛选次数）
     */
    @Login
    @GetMapping("sievePlateDetail")
    @ApiOperation("品类详情（某品类每个货号的筛选次数）")
    public R sievePlateDetail(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("年份") @RequestParam("year") Integer year,
    		@ApiParam("季节序号") @RequestParam("seasonSeq") Integer seasonSeq,
    		@ApiParam("品类序号") @RequestParam("categorySeq") Integer categorySeq) {
    	try {
    		List<Map<String, Object>> list = sievePlateService.getSievePlateDetail(loginUser.getCompanySeq(), year, seasonSeq, categorySeq);
    		
    		for(Map<String, Object> map: list) {
    			
    			//判断是否分配 0:未分配 1:已分配
    			String goodId = (String)map.get("goodId");
    			Integer flag = sievePlateDistributeService.getIsGoodIdbeDistributed(loginUser.getCompanySeq(), goodId);
    			map.put("distributed", flag);
    			
    		}
    		
			return R.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    
    
	/**
	 * 订货分配（某一货号，各个门店分配一定数量）
	 */
	@Login
	@PostMapping("distributeSievePlate")
	@ApiOperation("订货分配")
	public R distributeSievePlate(@ApiIgnore @LoginUser BaseUserEntity loginUser,
				@ApiParam("货号") @RequestParam("goodId") String goodId,
				@ApiParam(value="门店分配数量", example = "[{shopSeq:3,num:100},{shopSeq:4,num:200}]") @RequestParam("shopDistributeNum") String shopDistributeNum) {
		try {
			//查询订货会鞋子信息
			OrderManageEntity orderManageEntity = orderPlanManageService.getOrderManageEntityByGoodId(goodId);
			if (orderManageEntity==null ) {
				return R.error("订货会鞋子不存在");
			}
			sievePlateDistributeService.distributeSievePlate(orderManageEntity, shopDistributeNum);
			 
			return R.ok("分配成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

	}
	
	
	
	
    /**
     * 查询分配详情（某货号各个门店已分配的情况）
     */
    @Login
    @GetMapping("sievePlateDistributedDetail")
    @ApiOperation("查询分配详情")
    public R sievePlateDistributedDetail(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("货号") @RequestParam("goodId") String goodId) {
    	try {
			List<Map<String, Object>> list = sievePlateDistributeService.getSievePlateDistributedDetail(loginUser.getCompanySeq(), goodId);

			for(Map<String, Object> map: list) {
				//添加门店名称
				BaseShopEntity baseShopEntity = baseShopService.selectById((Integer)map.get("shopSeq"));
				map.put("shopName", baseShopEntity.getName());
			}

			return R.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    
    /**
     * 审核（审核某一品类，每家门店的分配数量是否与订货计划数量一致，找出不一致的）
     */
    @Login
    @GetMapping("reviewSievePlateDistribution")
    @ApiOperation("审核")
    public R reviewSievePlateDistribution(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("年份") @RequestParam("year") Integer year,
    		@ApiParam("季节序号") @RequestParam("seasonSeq") Integer seasonSeq,
    		@ApiParam("品类序号") @RequestParam("categorySeq") Integer categorySeq) {
    	try {
    		List<Map<String, Object>> shopList = new ArrayList<Map<String, Object>>();
    		
    		//查询此品类每个门店的订货计划
    		List<MeetingPlanEntity> meetingPlanEntityList = meetingPlanService.getAllShopsMeetingPlan(loginUser.getCompanySeq(), year, seasonSeq, categorySeq);
    		
    		
    		for(MeetingPlanEntity meetingPlanEntity : meetingPlanEntityList) {
    			
    			//该门店的计划数量、款数
    			Integer shopSeq = meetingPlanEntity.getShopSeq();
    			Integer planNum = meetingPlanEntity.getPlanNum();
    			Integer planGoodsIDs = meetingPlanEntity.getPlanGoodsIDs();
    			
        		//查询该门店的实际分配数量、款数
    			Map<String, Object> map = sievePlateDistributeService.getShopDistributeNumOneCategory(loginUser.getCompanySeq(), year, seasonSeq, shopSeq, categorySeq);
    			if(map!=null) {
    				Integer distributeNum = (Integer)map.get("distributeNum");
        			Integer distributeGoodIds = (Integer)map.get("distributeGoodIds");
        			Integer compareNum = distributeNum - planNum;
        			Integer compareGoodIds = distributeGoodIds - planGoodsIDs;
        			if(compareNum != 0 || compareGoodIds != 0) {   //不满足订货计划
        				
        				Map<String, Object> shopMap = new HashMap<String, Object>();
        				
        				shopMap.put("shopSeq", shopSeq);
            			BaseShopEntity baseShopEntity = baseShopService.selectById(shopSeq);
            			shopMap.put("shopName", baseShopEntity.getName());
        				shopMap.put("planNum", planNum);
        				shopMap.put("planGoodsIDs", planGoodsIDs);
        				shopMap.put("distributeNum", distributeNum);
        				shopMap.put("distributeGoodIds", distributeGoodIds);
        				shopMap.put("compareNum", compareNum);
        				shopMap.put("compareGoodIds", compareGoodIds);
        				
        				shopList.add(shopMap);
        			}
    			}else {
    				Integer distributeNum = 0;
        			Integer distributeGoodIds = 0;
        			
        			//对比
        			Integer compareNum = distributeNum - planNum;
        			Integer compareGoodIds = distributeGoodIds - planGoodsIDs;
        			if(compareNum != 0 || compareGoodIds != 0) {   //不满足订货计划
        				
        				Map<String, Object> shopMap = new HashMap<String, Object>();
        				
        				shopMap.put("shopSeq", shopSeq);
            			BaseShopEntity baseShopEntity = baseShopService.selectById(shopSeq);
            			shopMap.put("shopName", baseShopEntity.getName());
        				shopMap.put("planNum", planNum);
        				shopMap.put("planGoodsIDs", planGoodsIDs);
        				shopMap.put("distributeNum", distributeNum);
        				shopMap.put("distributeGoodIds", distributeGoodIds);
        				shopMap.put("compareNum", compareNum);
        				shopMap.put("compareGoodIds", compareGoodIds);
        				
        				shopList.add(shopMap);
        			}
				}
    			
    			
    			
    			
    		}
    		
    		
			return R.ok(shopList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    
	/**
	 * 重订（将门店该品类所有货号已分配的数量清空）
	 */
	@Login
	@PostMapping("resetDistributedSievePlate")
	@ApiOperation("重订（将门店该品类所有货号已分配的数量清空）")
	public R resetDistributedSievePlate(@ApiIgnore @LoginUser BaseUserEntity loginUser,
				@ApiParam("年份") @RequestParam("year") Integer year,
	    		@ApiParam("季节序号") @RequestParam("seasonSeq") Integer seasonSeq,
	    		@ApiParam("品类序号") @RequestParam("categorySeq") Integer categorySeq,
				@ApiParam("门店序号（逗号隔开）") @RequestParam("shopSeqs") List<Integer> shopSeqs) {
		try {

			//获取要重订的筛板分配实体
			List<SievePlateDistributeEntity> list = sievePlateDistributeService.getResetDistributedSievePlateList(loginUser.getCompanySeq(), year, seasonSeq, categorySeq, shopSeqs);
			List<Integer> sievePlateDistributeSeqList = new ArrayList<Integer>();
			for(SievePlateDistributeEntity sievePlateDistributeEntity : list) {
				//判断是否已经配码，已经配码的无法删除
				ShopAllotOrderEntity shopAllotOrderEntity = shopAllotOrderService.getShopAllotBySilevePlateDistributeSeq(sievePlateDistributeEntity.getSeq());
				if(shopAllotOrderEntity == null) {
					sievePlateDistributeSeqList.add(sievePlateDistributeEntity.getSeq());
				}
				
			}
			
			//重订
			if(sievePlateDistributeSeqList.size() > 0) {
				sievePlateDistributeService.resetDistributedSievePlate(sievePlateDistributeSeqList);
			}
			return R.ok("重订成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

	}
	
	
	
    
}
