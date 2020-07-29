package io.nuite.modules.order_platform_app.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.order_platform_app.service.ShoesDataDailyDetailService;
import io.nuite.modules.order_platform_app.service.ShoesDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import io.nuite.common.utils.Constant;
import io.nuite.common.utils.DateUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.service.SaleShoesDetailService;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.service.BaseShopService;
import io.nuite.modules.system.service.ShoesReplenishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;



/**
 * jrd单个鞋子销售分析接口
 * @author yy
 * @date 2018-11-29 18:26:05
 */
@RestController
@RequestMapping("/order/app/goodIdAnalysis")
@Api(tags="jrd单个鞋子销售分析接口")
public class SaleShoesDetailController {
	private Logger logger = LoggerFactory.getLogger(getClass());
    
	@Autowired
	private SaleShoesDetailService saleShoesDetailService;
	
	@Autowired
	private BaseShopService shopService;
	
	@Autowired
	private ShoesDataService shoesDataService;

	@Autowired
	private ShoesReplenishService shoesReplenishService ;

	@Autowired
	private BaseShopService baseShopService;

	@Autowired
	private ShoesDataDailyDetailService shoesDataDailyDetailService;
	
	/**
     * 鞋子详情页销量与库存
     */
    @Login
    @GetMapping("saleNumAndStock")
    @ApiOperation(value = "鞋子详情页销量与库存")
    public R saleNumAndStock(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("鞋子序号") @RequestParam("shoesSeq") Integer shoesSeq,
    		@ApiParam("起始日期") @RequestParam("startDate") Date startDate,
    		@ApiParam("结束日期") @RequestParam("endDate") Date endDate
    		) {
    	try {
    		
    		Map<String, Object> result = new HashMap<String, Object>();
			Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<>();
			wrapper.eq("ShoeSeq",shoesSeq).orderBy("SaleDate",true);
			List<SaleShoesDetailEntity> saleShoesDetailEntities = saleShoesDetailService.selectList(wrapper);
			Date firstDate = new SimpleDateFormat("yyyy/MM/dd").parse("2017/1/2");
			if(saleShoesDetailEntities.size() > 0) {
				firstDate = saleShoesDetailEntities.get(0).getSaleDate();
			}
    		/**查询总公司数据**/
    		//获取所有门店序号
    		List<Object> shopSeqs = shopService.getShopSeqsByCompanySeq(loginUser.getCompanySeq());
    		Wrapper<ShoesDataDailyDetailEntity> shoesInWrapper = new EntityWrapper<>();
			shoesInWrapper.eq("Shoes_Seq",shoesSeq).eq("Type",0).setSqlSelect("Shoes_Seq,SUM(Count) as num").groupBy("Shoes_Seq");
			List<ShoesDataDailyDetailEntity> shoesInEntities = shoesDataDailyDetailService.selectList(shoesInWrapper);
			Wrapper<ShoesDataDailyDetailEntity> shoesOutWrapper = new EntityWrapper<>();
			shoesOutWrapper.eq("Shoes_Seq",shoesSeq).eq("Type",4).setSqlSelect("Shoes_Seq,SUM(Count) as num").groupBy("Shoes_Seq");
			List<ShoesDataDailyDetailEntity> shoesOutEntities = shoesDataDailyDetailService.selectList(shoesOutWrapper);
			//累计进货量
			Integer totalStock = 0;
			if(shoesInEntities.size() > 0) {
				if(shoesOutEntities.size() > 0) {
					totalStock = shoesInEntities.get(0).getNum() - shoesOutEntities.get(0).getNum();
					result.put("totalStock",totalStock);
				}else {
					totalStock = shoesInEntities.get(0).getNum();
					result.put("totalStock",totalStock);
				}
			}else {
				if(shoesOutEntities.size() > 0) {
					totalStock = 0 - shoesOutEntities.get(0).getNum();
					result.put("totalStock",totalStock);
				}else {
					result.put("totalStock",0);
				}

			}
    		//所有门店该货号时间段内的1.销量
    		Integer allShopsSaleNum = saleShoesDetailService.getShopsSaleNumOneShoesSeq(shopSeqs, shoesSeq, startDate, endDate);
    		result.put("saleNum", allShopsSaleNum);
			//所有门店该货号的累计销量
			Integer totalSaleNum = saleShoesDetailService.getShopsTotalSaleNumOneShoesSeq(shopSeqs, shoesSeq, new SimpleDateFormat("yyyy-MM-dd").parse("2017-1-2"),new Date());
			result.put("totalSaleNum", totalSaleNum);

			//日平均销量
			if(saleShoesDetailEntities.size() > 0) {
				Date saleDate = saleShoesDetailEntities.get(0).getSaleDate();
				Integer days = 0;
				if(startDate.getTime() - saleDate.getTime() > 0L) {
					days = (int)((endDate.getTime() - startDate.getTime()) / (1000*60*60*24)) + 1;
				}else {
					days = (int)((endDate.getTime() - saleDate.getTime()) / (1000*60*60*24)) + 1;
				}
				if(new BigDecimal(allShopsSaleNum).compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal salesDayAvg = new BigDecimal(allShopsSaleNum).divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP);
					result.put("dayAvgSaleNum",salesDayAvg);
				}else {
					result.put("dayAvgSaleNum",0);
				}
			}else {
				result.put("dayAvgSaleNum",0);
			}
			/*if(startDate == null) {
				days = (int)((System.currentTimeMillis() - firstDate.getTime()) / (1000*3600*24));
				result.put("dayAvgSaleNum",new BigDecimal(totalSaleNum).divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP));
			}else {
				days = (int)((endDate.getTime() - startDate.getTime()) / (1000*3600*24));
				result.put("dayAvgSaleNum",new BigDecimal(allShopsSaleNum).divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP));
			}*/
    		
    		//2.本货号在所有货号中的销量排名
    		Integer saleNumRank = saleShoesDetailService.getGoodsIdSaleNumRank(shopSeqs, shoesSeq, startDate, endDate);
    		result.put("saleNumRank", saleNumRank);
    		//品类排名
			Integer saleCategoryNumRank = saleShoesDetailService.getGoodsIdSaleNumRankByCategory(shopSeqs, shoesSeq, startDate, endDate);
			result.put("saleCategoryNumRank", saleCategoryNumRank);
    		
    		//3.所有门店该货号时间段内平均售价
    		BigDecimal avgSalePrice = saleShoesDetailService.getShopsAvgSalePriceOneShoesSeq(shopSeqs, shoesSeq, startDate, endDate);
    		result.put("avgSalePrice", avgSalePrice);
    		
    		//进货量、4.售罄率
			//总部加门店的进货总量
			Integer hqAndShopInNum = saleShoesDetailService.getHqAndShopInNumOneShoesSeq(shoesSeq, startDate, endDate);
			//计算售罄率
			BigDecimal sellOutRate = BigDecimal.ZERO;
			if(allShopsSaleNum != null && totalStock != null && totalStock != 0) {
				sellOutRate = BigDecimal.valueOf(allShopsSaleNum).divide(BigDecimal.valueOf(totalStock), 2, RoundingMode.HALF_UP);
			}
			result.put("sellOutRate", sellOutRate);
			
    		//5.库存
			Integer hqAndShopStockNum = saleShoesDetailService.getHqAndShopStockNumOneShoesSeq(shoesSeq);
			result.put("stock", hqAndShopStockNum);
			
			//6.本货号在所有货号中的库存排名
			Integer stockRank = saleShoesDetailService.getGoodsIdStockRank(shopSeqs, shoesSeq);
    		result.put("stockRank", stockRank);
			
			//7.尺码库存列表
			List<Map<String, Object>> hqAndShopStockDetail = saleShoesDetailService.getHqAndShopShoesStockDetail(shoesSeq);
			result.put("stockDetail", hqAndShopStockDetail);
			//获取补单列表
			List<Map<String, Object>> replenishList = shoesReplenishService.getShoesReplenishList(shoesSeq);
    		result.put("replenishList", replenishList);
			// 门店账号再添加该门店的数据
			if(loginUser.getRoleCode().equals(Constant.Role.SHOP_ADMIN.getCode()) && loginUser.getShopSeq() != null) {  
				
	    		/**查询门店数据**/
				//本门店序号
				Integer shopSeq = loginUser.getShopSeq();
				List<Object> thisShopSeqList = new ArrayList<Object>();
				thisShopSeqList.add(shopSeq);
				Wrapper<ShoesDataDailyDetailEntity> shoesShopInWrapper = new EntityWrapper<>();
				shoesShopInWrapper.eq("Shoes_Seq",shoesSeq).eq("Type",1).eq("Shop_Seq",loginUser.getShopSeq()).setSqlSelect("Shoes_Seq,SUM(Count) as num").groupBy("Shoes_Seq");
				List<ShoesDataDailyDetailEntity> shoesShopInEntities = shoesDataDailyDetailService.selectList(shoesShopInWrapper);
				Wrapper<ShoesDataDailyDetailEntity> shoesShopOutWrapper = new EntityWrapper<>();
				shoesShopOutWrapper.eq("Shoes_Seq",shoesSeq).eq("Type",2).eq("Shop_Seq",loginUser.getShopSeq()).setSqlSelect("Shoes_Seq,SUM(Count) as num").groupBy("Shoes_Seq");
				List<ShoesDataDailyDetailEntity> shoesShopOutEntities = shoesDataDailyDetailService.selectList(shoesShopOutWrapper);
				//门店累计进货量
				if(shoesShopInEntities.size() > 0) {
					if(shoesShopOutEntities.size() > 0) {
						result.put("totalShopStock", shoesShopInEntities.get(0).getNum() - shoesShopOutEntities.get(0).getNum());
					}else {
						result.put("totalShopStock", shoesShopInEntities.get(0).getNum());
					}
				}else {
					if(shoesShopOutEntities.size() > 0) {
						result.put("totalShopStock", 0 - shoesShopOutEntities.get(0).getNum());
					}else {
						result.put("totalShopStock", 0);
					}
				}
	    		//本门店该货号时间段内的1.销量
	    		Integer shopSaleNum = saleShoesDetailService.getShopsSaleNumOneShoesSeq(thisShopSeqList, shoesSeq, startDate, endDate);
	    		result.put("shopSaleNum", shopSaleNum);
	    		//本门店该货号的累计销量
				Integer shopTotalSaleNum = saleShoesDetailService.getShopsTotalSaleNumOneShoesSeq(thisShopSeqList, shoesSeq, new SimpleDateFormat("yyyy-MM-dd").parse("2017-1-2"),new Date());
				result.put("shopTotalSaleNum", shopTotalSaleNum);
				//门店日平均销量
				if(saleShoesDetailEntities.size() > 0) {
					Date saleDate = saleShoesDetailEntities.get(0).getSaleDate();
					Integer days = 0;
					if(startDate.getTime() - saleDate.getTime() > 0L) {
						days = (int)((endDate.getTime() - startDate.getTime()) / (1000*60*60*24)) + 1;
					}else {
						days = (int)((endDate.getTime() - saleDate.getTime()) / (1000*60*60*24)) + 1;
					}
					if(new BigDecimal(shopTotalSaleNum).compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal salesDayAvg = new BigDecimal(shopTotalSaleNum).divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP);
						result.put("dayAvgShopSaleNum",salesDayAvg);
					}else {
						result.put("dayAvgShopSaleNum",0);
					}
				}else {
					result.put("dayAvgShopSaleNum",0);
				}
				/*if(startDate == null) {
					days = (int)((System.currentTimeMillis() - firstDate.getTime()) / (1000*3600*24));
					result.put("dayAvgShopSaleNum",new BigDecimal(shopSaleNum).divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP));
				}else {
					days = (int)((endDate.getTime() - startDate.getTime()) / (1000*3600*24));
					result.put("dayAvgShopSaleNum",new BigDecimal(shopTotalSaleNum).divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP));
				}*/
	    		//2.本门店本货号在所有货号中的销量排名
	    		Integer shopSaleNumRank = saleShoesDetailService.getGoodsIdSaleNumRank(thisShopSeqList, shoesSeq, startDate, endDate);
	    		result.put("shopSaleNumRank", shopSaleNumRank);
				//门店品类排名
				Integer shopSaleCategoryNumRank = saleShoesDetailService.getGoodsIdSaleNumRankByCategory(thisShopSeqList, shoesSeq, startDate, endDate);
				result.put("shopSaleCategoryNumRank", shopSaleCategoryNumRank);
	    		
	    		//3.本门店该货号时间段内平均售价
	    		BigDecimal shopAvgSalePrice = saleShoesDetailService.getShopsAvgSalePriceOneShoesSeq(thisShopSeqList, shoesSeq, startDate, endDate);
	    		result.put("shopAvgSalePrice", shopAvgSalePrice);
	    		
	    		//进货量、4.售罄率
				//门店的进货总量
				Integer shopInNum = saleShoesDetailService.getShopInNumOneShoesSeq(shopSeq, shoesSeq, startDate, endDate);
				//计算售罄率
				BigDecimal shopSellOutRate = BigDecimal.ZERO;
				if(shopSaleNum != null && shopInNum != null && shopInNum != 0) {
					shopSellOutRate = BigDecimal.valueOf(shopSaleNum).divide(BigDecimal.valueOf(shopInNum), 2, RoundingMode.HALF_UP);
				}
				result.put("shopSellOutRate", shopSellOutRate);
				
	    		//5.库存
				Integer shopStockNum = saleShoesDetailService.getShopStockNumOneShoesSeq(shopSeq, shoesSeq);
				result.put("shopStock", shopStockNum);
				
				//6.本店本货号在所有货号中的库存排名
				Integer shopStockRank = saleShoesDetailService.getGoodsIdStockRank(thisShopSeqList, shoesSeq);
	    		result.put("shopStockRank", shopStockRank);
	    		
				//7.尺码库存列表
				List<Map<String, Object>> shopStockDetail = saleShoesDetailService.getShopShoesStockDetail(shopSeq, shoesSeq);
				result.put("shopStockDetail", shopStockDetail);
				
			}

    		return R.ok(result);
    		
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器开小差了");
		}

    }
    
    
    
    
	/**
     * 鞋子详情页所有门店销售情况排行
     */
    @Login
    @GetMapping("allShopsSaleDetail")
    @ApiOperation(value = "鞋子详情页所有门店销售情况排行")
    public R allShopsSaleDetail(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("鞋子序号") @RequestParam("shoesSeq") Integer shoesSeq,
    		@ApiParam("起始日期") @RequestParam("startDate") Date startDate,
    		@ApiParam("结束日期") @RequestParam("endDate") Date endDate
    		) {
    	try {
    		
    		//获取所有门店
    		List<BaseShopEntity> shopList = shopService.getShopsByCompanySeq(loginUser.getCompanySeq());
    		List<Map<String, Object>> allShopSaleDetailList = new ArrayList<Map<String, Object>>();
    		for(BaseShopEntity shopEntity : shopList) {
    			
    			Map<String, Object> shopSaleDetailMap = new HashMap<String, Object>();
    			
	    		/**查询门店数据**/
    			//1.店铺名称
    			String shopName = shopEntity.getName();
    			shopSaleDetailMap.put("shopSeq", shopEntity.getSeq());
    			shopSaleDetailMap.put("shopName", shopName);
    			
    			
				List<Object> thisShopSeqList = new ArrayList<Object>();
				thisShopSeqList.add(shopEntity.getSeq());
				//根据鞋子序号和门店序号查询第一次进货时间
				ShoesDataDailyDetailEntity shoesDataDailyDetailEntity=shoesDataDailyDetailService.getNearOne(shoesSeq, shopEntity.getSeq());
				if(shoesDataDailyDetailEntity!=null) {
					Date startTime=shoesDataDailyDetailEntity.getInputERPTime();
					Date endTime=new Date();
					int days = (int) ((endTime.getTime() - startTime.getTime()) / (1000*3600*24)) + 1;
					shopSaleDetailMap.put("stockDays",days);
				}else {
					shopSaleDetailMap.put("stockDays", 0);
				}
				
	    		//门店该货号时间段内的2.销量
	    		Integer shopSaleNum = saleShoesDetailService.getShopsSaleNumOneShoesSeq(thisShopSeqList, shoesSeq, startDate, endDate);
	    		shopSaleDetailMap.put("shopSaleNum", shopSaleNum);
	    		
	    		//5.门店该货号时间段内平均售价
	    		BigDecimal shopAvgSalePrice = saleShoesDetailService.getShopsAvgSalePriceOneShoesSeq(thisShopSeqList, shoesSeq, startDate, endDate);
	    		shopSaleDetailMap.put("shopAvgSalePrice", shopAvgSalePrice);
	    		
	    		//进货量、4.售罄率
				//门店的进货总量
				Integer shopInNum = saleShoesDetailService.getShopInNumOneShoesSeq(shopEntity.getSeq(), shoesSeq, startDate, endDate);
				//计算售罄率
				BigDecimal shopSellOutRate = BigDecimal.ZERO;
				if(shopSaleNum != null && shopInNum != null && shopInNum != 0) {
					shopSellOutRate = BigDecimal.valueOf(shopSaleNum).divide(BigDecimal.valueOf(shopInNum), 2, RoundingMode.HALF_UP);
				}
				shopSaleDetailMap.put("shopSellOutRate", shopSellOutRate);
				
	    		//3.库存
				Integer shopStockNum = saleShoesDetailService.getShopStockNumOneShoesSeq(shopEntity.getSeq(), shoesSeq);
				shopSaleDetailMap.put("shopStock", shopStockNum);

				BigDecimal salesNum = new BigDecimal(0);
				if(shopSaleNum != null) {
					salesNum = new BigDecimal(shopSaleNum);
				}
				Wrapper<SaleShoesDetailEntity> saleShoesDetailEntityWrapper = new EntityWrapper<>();
				saleShoesDetailEntityWrapper.eq("ShoeSeq",shoesSeq).eq("ShopSeq",shopEntity.getSeq()).orderBy("SaleDate",true);
				List<SaleShoesDetailEntity> saleShoesDetailEntities = saleShoesDetailService.selectList(saleShoesDetailEntityWrapper);
				if(saleShoesDetailEntities.size() > 0) {
					Date saleDate = saleShoesDetailEntities.get(0).getSaleDate();
					Integer days = 0;
					if(startDate.getTime() - saleDate.getTime() > 0L) {
						days = (int)((endDate.getTime() - startDate.getTime()) / (1000*60*60*24)) + 1;
					}else {
						days = (int)((endDate.getTime() - saleDate.getTime()) / (1000*60*60*24)) + 1;
					}
					if(salesNum.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal salesDayAvg = salesNum.divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP);
						shopSaleDetailMap.put("salesDayAvg",salesDayAvg);
					}else {
						shopSaleDetailMap.put("salesDayAvg",0);
					}
				}else {
					shopSaleDetailMap.put("salesDayAvg",0);
				}
				allShopSaleDetailList.add(shopSaleDetailMap);
    		}
			
    		
    		
			// 门店账号本门店排名
			if(loginUser.getRoleCode().equals(Constant.Role.SHOP_ADMIN.getCode()) && loginUser.getShopSeq() != null) {  
			}
			
    		//按照要求对List中数据排序
    		Collections.sort(allShopSaleDetailList, new CompareStock(false));
    		
    		
    		/**汇总数据: 查询总公司数据**/
			Map<String, Object> resultMap = new HashMap<String, Object>();
			//计算总仓货品在仓天数
			//一仓
			ShoesDataDailyDetailEntity firstStock = shoesDataDailyDetailService.getNearOne(shoesSeq,0);
			//调配仓
			ShoesDataDailyDetailEntity secondStock = shoesDataDailyDetailService.getNearOne(shoesSeq,-1);
			if(firstStock == null) {
				if(secondStock == null) {
					resultMap.put("stockTotalDays",0);
				}else {
					Date stockStartTime = secondStock.getInputERPTime();
					resultMap.put("stockTotalDays",(int)((System.currentTimeMillis() - stockStartTime.getTime()) / (1000*60*60*24)) + 1);
				}
			}else {
				if(secondStock == null) {
					Date stockStartTime = firstStock.getInputERPTime();
					resultMap.put("stockTotalDays",(int)((System.currentTimeMillis() - stockStartTime.getTime()) / (1000*60*60*24)) + 1);
				}else {
					Date firstStockStartTime = firstStock.getInputERPTime();
					Date secondStockStartTime = secondStock.getInputERPTime();
					if(firstStockStartTime.getTime() > secondStockStartTime.getTime()) {
						resultMap.put("stockTotalDays",(int)((System.currentTimeMillis() - secondStockStartTime.getTime()) / (1000*60*60*24)) + 1);
					}else {
						resultMap.put("stockTotalDays",(int)((System.currentTimeMillis() - firstStockStartTime.getTime()) / (1000*60*60*24)) + 1);
					}
				}
			}
    		//获取所有门店序号
    		List<Object> shopSeqs = shopService.getShopSeqsByCompanySeq(loginUser.getCompanySeq());
    		
    		//所有门店该货号时间段内的1.销量
    		Integer allShopsSaleNum = saleShoesDetailService.getShopsSaleNumOneShoesSeq(shopSeqs, shoesSeq, startDate, endDate);
    		resultMap.put("saleNum", allShopsSaleNum);
    		
    		//3.所有门店该货号时间段内平均售价
    		BigDecimal avgSalePrice = saleShoesDetailService.getShopsAvgSalePriceOneShoesSeq(shopSeqs, shoesSeq, startDate, endDate);
    		resultMap.put("avgSalePrice", avgSalePrice);
    		
    		//进货量、4.售罄率
			//总部加门店的进货总量
			Integer hqAndShopInNum = saleShoesDetailService.getHqAndShopInNumOneShoesSeq(shoesSeq, startDate, endDate);
			//计算售罄率
			BigDecimal sellOutRate = BigDecimal.ZERO;
			if(allShopsSaleNum != null && hqAndShopInNum != null && hqAndShopInNum != 0) {
				sellOutRate = BigDecimal.valueOf(allShopsSaleNum).divide(BigDecimal.valueOf(hqAndShopInNum), 2, RoundingMode.HALF_UP);
			}
			resultMap.put("sellOutRate", sellOutRate);
			
    		//5.库存
			Integer hqAndShopStockNum = saleShoesDetailService.getHqAndShopStockNumOneShoesSeq(shoesSeq);
			resultMap.put("stock", hqAndShopStockNum);

			BigDecimal salesNum = new BigDecimal(0);
			if(allShopsSaleNum != null) {
				salesNum = new BigDecimal(allShopsSaleNum);
			}
			Wrapper<SaleShoesDetailEntity> saleShoesDetailEntityWrapper = new EntityWrapper<>();
			saleShoesDetailEntityWrapper.eq("ShoeSeq",shoesSeq).orderBy("SaleDate",true);
			List<SaleShoesDetailEntity> saleShoesDetailEntities = saleShoesDetailService.selectList(saleShoesDetailEntityWrapper);
			if(saleShoesDetailEntities.size() > 0) {
				Date saleDate = saleShoesDetailEntities.get(0).getSaleDate();
				Integer days = 0;
				if(startDate.getTime() - saleDate.getTime() > 0L) {
					days = (int)((endDate.getTime() - startDate.getTime()) / (1000*60*60*24)) + 1;
				}else {
					days = (int)((endDate.getTime() - saleDate.getTime()) / (1000*60*60*24)) + 1;
				}
				if(salesNum.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal salesDayAvg = salesNum.divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP);
					resultMap.put("salesDayAvg",salesDayAvg);
				}else {
					resultMap.put("salesDayAvg",0);
				}
			}else {
				resultMap.put("salesDayAvg",0);
			}
			resultMap.put("allShopSaleDetailList", allShopSaleDetailList);
    		
    		return R.ok(resultMap);
    		
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器开小差了");
		}

    }
    
    
    
    
    //比较销量
	private static final class CompareSales implements Comparator<Map<String, Object>> {
	     boolean isAsc;

	     public CompareSales(boolean isAsc) {
	    	 this.isAsc = isAsc;
	     }

	     /*
	      * int compare(Person p1, Person p2) 返回一个基本类型的整型，
	      * 返回负数表示：p1 小于p2，
	      * 返回0 表示：p1和p2相等，
	      * 返回正数表示：p1大于p2
	      */
	     @Override
	     public int compare(Map<String, Object> map1, Map<String, Object> map2) {
	         //按照Sales进行排列
	         if((Integer)map1.get("shopSaleNum") > (Integer)map2.get("shopSaleNum")){
	        	 if(isAsc) {
		             return 1;
	        	 } else {
	        		 return -1;  //Comparator默认按照升序排列，通过返回相反的值，实现降序
	        	 }
	         } else if(((Integer)map1.get("shopSaleNum")).intValue() == (Integer)map2.get("shopSaleNum")){
	             return 0;
	         } else {
	        	 if(isAsc) {
		             return -1;
	        	 } else {
	        		 return 1;
	        	 }
	         }
	     }
	}
	//比较销量
	private static final class CompareStock implements Comparator<Map<String, Object>> {
		boolean isAsc;

		public CompareStock(boolean isAsc) {
			this.isAsc = isAsc;
		}

		/*
         * int compare(Person p1, Person p2) 返回一个基本类型的整型，
         * 返回负数表示：p1 小于p2，
         * 返回0 表示：p1和p2相等，
         * 返回正数表示：p1大于p2
         */
		@Override
		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
			//按照Stock进行排列
			if((Integer)map1.get("shopStock") > (Integer)map2.get("shopStock")){
				if(isAsc) {
					return 1;
				} else {
					return -1;  //Comparator默认按照升序排列，通过返回相反的值，实现降序
				}
			} else if(((Integer)map1.get("shopStock")).intValue() == (Integer)map2.get("shopStock")){
				return 0;
			} else {
				if(isAsc) {
					return -1;
				} else {
					return 1;
				}
			}
		}
	}
	
	
	
	/**
     * 库存详情和平均售价销量走势图（所有门店销售情况排行列表点击"查看更多"）
     */
    @Login
    @GetMapping("shopStockAndTrendChart")
    @ApiOperation(value = "库存详情和平均售价销量走势图")
    public R shopStockAndTrendChart(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("鞋子序号") @RequestParam("shoesSeq") Integer shoesSeq,
    		@ApiParam("门店序号（不传则为所有门店汇总）") @RequestParam(value = "shopSeq", required = false) Integer shopSeq,
    		@ApiParam("起始日期") @RequestParam("startDate") Date startDate,
    		@ApiParam("结束日期") @RequestParam("endDate") Date endDate
    		) {
    	try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			
			
			
			//门店序号不为空,某一门店的库存和售价销量图
			if(shopSeq != null) {
				//1.尺码库存列表
				List<Map<String, Object>> shopStockDetail = saleShoesDetailService.getShopShoesStockDetail(shopSeq, shoesSeq);
				resultMap.put("shopStockDetail", shopStockDetail);
	    		
				
				//2.每日的平均售价、销量
				List<Map<String, Object>> saleNumAvgPriceList = new ArrayList<Map<String, Object>>();
	    		Calendar calendar = Calendar.getInstance();
	    		Date iteratorDate = startDate;
	    		while (!iteratorDate.after(endDate)) {
	    			Date start = iteratorDate;
	    			Date end = DateUtils.addDateDays(iteratorDate, 1);
	    			
	    			Map<String, Object> map = new HashMap<String, Object>();
	    			map.put("date", DateUtils.format2(start));
	    			//查询平均售价、销量
		    		//本门店该货号时间段内的销量
		    		Integer shopSaleNum = saleShoesDetailService.getShopsSaleNumOneShoesSeq(Arrays.asList(shopSeq), shoesSeq, start, end);
		    		map.put("saleNum", shopSaleNum);
		    		
		    		//本门店该货号时间段内平均售价
		    		BigDecimal shopAvgSalePrice = saleShoesDetailService.getShopsAvgSalePriceOneShoesSeq(Arrays.asList(shopSeq), shoesSeq, start, end);
		    		map.put("avgSalePrice", shopAvgSalePrice);
		    		saleNumAvgPriceList.add(map);
		    		
	    			calendar.setTime(iteratorDate);
	    			calendar.add(Calendar.DAY_OF_MONTH, 1); //+1天
	    			iteratorDate = calendar.getTime();
	    		}
	    		resultMap.put("saleNumAvgPriceList", saleNumAvgPriceList);
			} else {
				//门店序号为空， 所有门店汇总
	    		//获取所有门店序号
	    		List<Object> shopSeqs = shopService.getShopSeqsByCompanySeq(loginUser.getCompanySeq());
	    		
				//1.尺码库存列表
				List<Map<String, Object>> shopStockDetail = saleShoesDetailService.getAllShopsShoesStockDetail(shopSeqs, shoesSeq);
				resultMap.put("shopStockDetail", shopStockDetail);
	    		
				
				//2.每日的平均售价、销量
				List<Map<String, Object>> saleNumAvgPriceList = new ArrayList<Map<String, Object>>();
	    		Calendar calendar = Calendar.getInstance();
	    		Date iteratorDate = startDate;
	    		while (!iteratorDate.after(endDate)) {
	    			Date start = iteratorDate;
	    			Date end = DateUtils.addDateDays(iteratorDate, 1);
	    			
	    			Map<String, Object> map = new HashMap<String, Object>();
	    			map.put("date", DateUtils.format2(start));
	    			//查询平均售价、销量
		    		//本门店该货号时间段内的销量
		    		Integer shopSaleNum = saleShoesDetailService.getShopsSaleNumOneShoesSeq(shopSeqs, shoesSeq, start, end);
		    		map.put("saleNum", shopSaleNum);
		    		
		    		//本门店该货号时间段内平均售价
		    		BigDecimal shopAvgSalePrice = saleShoesDetailService.getShopsAvgSalePriceOneShoesSeq(shopSeqs, shoesSeq, start, end);
		    		map.put("avgSalePrice", shopAvgSalePrice);
		    		saleNumAvgPriceList.add(map);
		    		
	    			calendar.setTime(iteratorDate);
	    			calendar.add(Calendar.DAY_OF_MONTH, 1); //+1天
	    			iteratorDate = calendar.getTime();
	    		}
	    		resultMap.put("saleNumAvgPriceList", saleNumAvgPriceList);
				
			}
			
    		return R.ok(resultMap);
    		
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器开小差了");
		}

    }
    
    
    
    
	/**
     * 销售数量曲线图
     */
    @Login
    @GetMapping("saleNumDiagram")
    @ApiOperation(value = "销售数量曲线图")
    public R saleNumDiagram(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("鞋子序号") @RequestParam("shoesSeq") Integer shoesSeq,
    		/*@ApiParam("门店序号List（逗号隔开）") @RequestParam("shopSeqs") List<Integer> shopSeqs,*/
    		@ApiParam("起始日期") @RequestParam("startDate") Date startDate,
    		@ApiParam("结束日期") @RequestParam("endDate") Date endDate
    		) {
    	try {
    		
    		/*
    		 * [
    		 * ['日期', '2019-03-10','2019-03-11','2019-03-12','2019-03-13' ]
    		 * ['门店001', 100,200,300,400,500 ]
    		 * ['门店002', 100,200,300,400,500 ]
    		 * ['门店003', 100,200,300,400,500 ]
    		 * ]
    		 */
			List<List<Object>> resultList = new ArrayList<List<Object>>();
			
			List<Object> dateList = new ArrayList<Object>();
			dateList.add("日期");
    		Calendar calendar = Calendar.getInstance();
    		Date iteratorDate = startDate;
    		while (!iteratorDate.after(endDate)) {
    			
    			dateList.add(DateUtils.format2(iteratorDate));
    			
    			calendar.setTime(iteratorDate);
    			calendar.add(Calendar.DAY_OF_MONTH, 1); //+1天
    			iteratorDate = calendar.getTime();
    		}
    		resultList.add(dateList);
    		
    		Wrapper<BaseShopEntity> wrapper = new EntityWrapper<>();
    		wrapper.eq("Company_Seq",loginUser.getCompanySeq());
    		List<BaseShopEntity> list = baseShopService.selectList(wrapper);
    		List<Object> shopSeqs = new ArrayList<>(30);
    		for(int i = 0;i < list.size();i++) {
    			shopSeqs.add(list.get(i).getSeq());
			}
			List<Object> shopList = new ArrayList<Object>();
			shopList.add("总销售");
			calendar = Calendar.getInstance();
			iteratorDate = startDate;
			while (!iteratorDate.after(endDate)) {
				Date start = iteratorDate;
				Date end = DateUtils.addDateDays(iteratorDate, 1);

				Integer shopSaleNum = saleShoesDetailService.getShopsSaleNumOneShoesSeq(shopSeqs, shoesSeq, start, end);
				shopList.add(shopSaleNum);

				calendar.setTime(iteratorDate);
				calendar.add(Calendar.DAY_OF_MONTH, 1); //+1天
				iteratorDate = calendar.getTime();
			}
			resultList.add(shopList);
    		return R.ok(resultList);
    		
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器开小差了");
		}

    }
    
    
    
	/**
     * 获取所有门店
     */
    @Login
    @GetMapping("getAllShops")
    @ApiOperation(value = "获取所有门店")
    public R getAllShops(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
    	try {
    		//获取所有门店
    		List<BaseShopEntity> shopList = shopService.getShopsByCompanySeq(loginUser.getCompanySeq());
			
    		return R.ok(shopList);
    		
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器开小差了");
		}

    }

}
