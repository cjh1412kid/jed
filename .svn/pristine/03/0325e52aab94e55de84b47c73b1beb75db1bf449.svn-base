package io.nuite.modules.order_platform_app.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import io.nuite.modules.order_platform_app.service.SaleShoesDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.nuite.common.utils.DateUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.service.SaleShoesAnalysisService;
import io.nuite.modules.order_platform_app.service.ShoesDataService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.service.BaseShopService;
import io.nuite.modules.sr_base.service.GoodsCategoryService;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;



/**
 * jrd销售分析接口
 * @author yy
 * @date 2018-11-29 18:26:05
 */
@RestController
@RequestMapping("/order/app/saleShoesAnalysis")
@Api(tags="jrd销售分析接口")
public class SaleShoesAnalysisController extends BaseController{
	private Logger logger = LoggerFactory.getLogger(getClass());
    
	@Autowired
	private SaleShoesAnalysisService saleShoesAnalysisService;
	
    @Autowired
    private GoodsShoesService goodsShoesService;
    
    @Autowired
    private ShoesDataService shoesDataService;
    
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    
    @Autowired
    private BaseShopService baseShopService;

    @Autowired
	private SaleShoesDetailService saleShoesDetailService;
	
	
    /**
     * 总部各个货品销量排行
     */
    @Login
    @GetMapping("hqShoesRankList")
    @ApiOperation("总部各个货品销量排行")
    public R hqShoesRankList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
    		@ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
    		@ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
    		@ApiParam("颜色") @RequestParam(value = "colorSeqList", required = false) List<Integer> colorSeqList,
    		@ApiParam(value="自定义属性", example="{SX1:'001',SX2:'002'}") @RequestParam(value = "sXMap", required = false, defaultValue = "{}") String sXMapStr,
    		
    		@ApiParam("排序字段(1:销量 2:库存 3:售罄率 4:平均售价)") @RequestParam("orderBy") Integer orderBy,
    		@ApiParam("排序方式(0:降序 1:升序)") @RequestParam("orderDir") Integer orderDir,
    		@ApiParam("起始条数") @RequestParam("start") Integer start,
    		@ApiParam("总条数") @RequestParam("num") Integer num,
    		
    		@ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart", required = false) Date saleTimeStart,
    		@ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd", required = false) Date saleTimeEnd) {
    	try {
    		saleTimeEnd = DateUtils.addDateDays(saleTimeEnd, 1);
    		
    		@SuppressWarnings("unchecked")
			Map<String, String> sXMap = JSONObject.parseObject(sXMapStr, Map.class);
    		
    		/**1.根据属性条件，查询满足属性条件的所有鞋子序号**/
			List<Integer> shoesSeqList = goodsShoesService.getShoesSeqListOnSXParam(loginUser.getCompanySeq(), yearList, seasonSeqList, categorySeqList, colorSeqList, sXMap, null,saleTimeStart,saleTimeEnd,null,null);
    		if(shoesSeqList == null || shoesSeqList.size() == 0) {
    			return R.ok("没有满足条件的商品");
    		}

    		/** 2.根据筛出来的鞋子序号和排序、分页条件，查询鞋子列表  **/
    		List<Map<String,Object>> shoesList = saleShoesAnalysisService.getHqShoesRankList(shoesSeqList, saleTimeStart, saleTimeEnd, orderBy, orderDir, start, num);
    		/**处理图片**/
			for(Map<String, Object> map : shoesList) {
				//图片
				map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
				//售罄率
				if(map.get("selloutRate") != null) {
					map.put("selloutRate", map.get("selloutRate") + "%");
				}
			}
			
			
			
			/**  添加汇总数据：货号、时间范围内的总销量、总库存、总进货量、总售罄率、总平均售价  **/
			Map<String, Object> resultMap = new HashMap<String, Object>();
    		//销量
    		Integer allShopsSaleNum = saleShoesAnalysisService.getHqTotalSaleNum(shoesSeqList, saleTimeStart, saleTimeEnd);
    		resultMap.put("total_saleCount", allShopsSaleNum);
    		
    		//平均售价
    		BigDecimal avgSalePrice = saleShoesAnalysisService.getHqTotalAvgSalePrice(shoesSeqList, saleTimeStart, saleTimeEnd);
    		resultMap.put("total_avgSalePrice", avgSalePrice);
    		
    		//进货量、售罄率
			//总部加门店的进货总量
			Integer hqTotalInNum = saleShoesAnalysisService.getHqTotalInNum(shoesSeqList, saleTimeStart, saleTimeEnd);
			resultMap.put("total_inNum", hqTotalInNum);
			//计算售罄率
			BigDecimal sellOutRate = BigDecimal.ZERO;
			if(allShopsSaleNum != null && hqTotalInNum != null && hqTotalInNum != 0) {
				sellOutRate = BigDecimal.valueOf(allShopsSaleNum).divide(BigDecimal.valueOf(hqTotalInNum), 2, RoundingMode.HALF_UP);
			}
			resultMap.put("total_sellOutRate", sellOutRate);
			
    		//库存
			Integer hqAndShopStockNum = saleShoesAnalysisService.getHqTotalStockNum(shoesSeqList);
			resultMap.put("total_stock", hqAndShopStockNum);
			
			
			
			resultMap.put("shoesList", shoesList);
			
    		return R.ok(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    /**
     * 销售品类分析
     */
    @Login
    @GetMapping("hqShoesCategoryList")
    @ApiOperation("销售品类分析")
    public R hqShoesCategoryList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
    		@ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
    		@ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
    		@ApiParam("新老款")@RequestParam(value = "oldOrNew",required = false)Integer oldOrNew,
    		@ApiParam("起始价格")@RequestParam(value = "startPrice",required = false)BigDecimal startPrice,
    		@ApiParam("起始价格")@RequestParam(value = "endPrice",required = false)BigDecimal endPrice,
    		@ApiParam("店铺筛选")@RequestParam(value = "shopSeqList",required = false)List<Integer> shopSeqList,
    		@ApiParam("排序字段(1:销量 2:库存 3:售罄率 4:平均售价)") @RequestParam("orderBy") Integer orderBy,
    		@ApiParam("排序方式(0:降序 1:升序)") @RequestParam("orderDir") Integer orderDir,
    		@ApiParam("起始条数") @RequestParam("start") Integer start,
    		@ApiParam("总条数") @RequestParam("num") Integer num,
    		@ApiParam("颜色") @RequestParam(value = "colorSeqList", required = false) List<Integer> colorSeqList,
    		@ApiParam(value="自定义属性", example="{SX1:'001',SX2:'002'}") @RequestParam(value = "sXMap", required = false, defaultValue = "{}") String sXMapStr,
    		@ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart", required = false) Date saleTimeStart,
    		@ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd", required = false) Date saleTimeEnd,@ApiParam("传入的类型")@RequestParam(value = "type") Integer type) {
    	try {
    		saleTimeEnd = DateUtils.addDateDays(saleTimeEnd, 1);
    		@SuppressWarnings("unchecked")
			Map<String, String> sXMap = JSONObject.parseObject(sXMapStr, Map.class);
    		/**1.根据属性条件，查询满足属性条件的所有鞋子序号**/
			List<Integer> shoesSeqList = goodsShoesService.getShoesSeqListOnSXParam(loginUser.getCompanySeq(), yearList, seasonSeqList, categorySeqList, colorSeqList, sXMap, null,saleTimeStart,saleTimeEnd,null,null);
    		if(shoesSeqList == null || shoesSeqList.size() == 0) {
    			return R.ok("没有满足条件的商品");
    		}
    		if(categorySeqList == null || categorySeqList.size() == 0) {
    			categorySeqList=goodsCategoryService.getCategorySeqList(0);
    		}
    
			/**  添加汇总数据：货号、时间范围内的总销量、总库存、总进货量、总售罄率、总平均售价  **/
			Map<String, Object> resultMap = new HashMap<String, Object>();
    		//销量总量
    		Integer allShopsSaleNum = saleShoesAnalysisService.getTotalSaleNum(shopSeqList, shoesSeqList, saleTimeStart, saleTimeEnd, startPrice, endPrice);
    		//销售总金额
    		BigDecimal allShopSalePrice=saleShoesAnalysisService.getTotalSalePrice(shopSeqList, shoesSeqList, saleTimeStart, saleTimeEnd, startPrice, endPrice);
    		//销售总sku
    		Integer allShopsSaleGoods=saleShoesAnalysisService.getTotalSaleGood(shopSeqList, shoesSeqList, saleTimeStart, saleTimeEnd, startPrice, endPrice);
    		
    		resultMap.put("total_saleCount", allShopsSaleNum);
    		
    		resultMap.put("total_salePrice", allShopSalePrice);
    		
    		resultMap.put("total_saleGood", allShopsSaleGoods);

    		Calendar calendar=Calendar.getInstance();
    		//环比日期
    		calendar.setTime(saleTimeStart);
    		calendar.add(Calendar.MONTH, -1);
    		Date startSaleTime=calendar.getTime();
    		calendar.setTime(saleTimeEnd);
    		calendar.add(Calendar.MONTH, -1);
    		Date endSaleTime=calendar.getTime();
    		//同比日期
    		calendar.setTime(saleTimeStart);
    		calendar.add(Calendar.YEAR, -1);
    		Date startTime=calendar.getTime();
    		calendar.setTime(saleTimeEnd);
    		calendar.add(Calendar.YEAR, -1);
    		Date endTime=calendar.getTime();
    		//同比销售总量
    		Integer shopsSaleNumMonth= saleShoesAnalysisService.getTotalSaleNum(shopSeqList, shoesSeqList, startTime, endTime, startPrice, endPrice);
    		if(shopsSaleNumMonth!=0) {
				BigDecimal ShopsSaleNumMonthPercent=new BigDecimal((double)(allShopsSaleNum-shopsSaleNumMonth)/shopsSaleNumMonth).setScale(4, BigDecimal.ROUND_HALF_UP);
				resultMap.put("ShopsSaleNumMonthPercent", ShopsSaleNumMonthPercent);
				resultMap.put("ShopsSaleNumMonth",allShopsSaleNum-shopsSaleNumMonth);
    		}else {
				resultMap.put("ShopsSaleNumMonthPercent", "");
				resultMap.put("ShopsSaleNumMonth",0);
    		}
    		 //同比总金额
    		BigDecimal ShopSalePriceMonth=saleShoesAnalysisService.getTotalSalePrice(shopSeqList, shoesSeqList, startTime, endTime, startPrice, endPrice);
    		if(ShopSalePriceMonth.compareTo(BigDecimal.ZERO)!=0) {
    			BigDecimal ShopSalePriceMonthPercent=(allShopSalePrice.subtract(ShopSalePriceMonth)).divide(ShopSalePriceMonth,4, BigDecimal.ROUND_HALF_UP);
				resultMap.put("ShopSalePriceMonthPercent", ShopSalePriceMonthPercent);
				resultMap.put("ShopSalePriceMonth",allShopSalePrice.subtract(ShopSalePriceMonth));
    		}else {
				resultMap.put("ShopSalePriceMonthPercent", "");
				resultMap.put("ShopSalePriceMonth",0);
    		}
    		//同比总sku
    		Integer ShopsSaleGoodsMonth=saleShoesAnalysisService.getTotalSaleGood(shopSeqList, shoesSeqList, startTime, endTime, startPrice, endPrice);
    		if(ShopsSaleGoodsMonth!=0) {
				BigDecimal ShopsSaleGoodsMonthPercent=new BigDecimal((double)(allShopsSaleGoods-ShopsSaleGoodsMonth)/ShopsSaleGoodsMonth).setScale(4, BigDecimal.ROUND_HALF_UP);
				resultMap.put("ShopsSaleGoodsMonthPercent", ShopsSaleGoodsMonthPercent);
				resultMap.put("ShopsSaleGoodsMonth",allShopsSaleGoods-ShopsSaleGoodsMonth);
    		}else {
				resultMap.put("ShopsSaleGoodsMonthPercent", "");
				resultMap.put("ShopsSaleGoodsMonth",0);
    		}
    		 //环比销售总量
    		Integer ShopsSaleNumYear = saleShoesAnalysisService.getTotalSaleNum(shopSeqList, shoesSeqList, startSaleTime, endSaleTime, startPrice, endPrice);
    		if(ShopsSaleNumYear!=0) {
       		 	BigDecimal ShopsSaleNumYearPercent=new BigDecimal((double)(allShopsSaleNum-ShopsSaleNumYear)/ShopsSaleNumYear).setScale(4, BigDecimal.ROUND_HALF_UP);
				resultMap.put("ShopsSaleNumYearPercent", ShopsSaleNumYearPercent);
				resultMap.put("ShopsSaleNumYear",allShopsSaleNum-ShopsSaleNumYear);
    		}else {
				resultMap.put("ShopsSaleNumYearPercent", "");
				resultMap.put("ShopsSaleNumYear",0);
    		}
    		//环比总金额
    		BigDecimal ShopSalePriceYear=saleShoesAnalysisService.getTotalSalePrice(shopSeqList, shoesSeqList, startSaleTime, endSaleTime, startPrice, endPrice);
    		if(ShopSalePriceYear.compareTo(BigDecimal.ZERO)!=0) {
    			BigDecimal ShopSalePriceYearPercent=(allShopSalePrice.subtract(ShopSalePriceYear)).divide(ShopSalePriceYear,4, BigDecimal.ROUND_HALF_UP);
				resultMap.put("ShopSalePriceYearPercent", ShopSalePriceYearPercent);
				resultMap.put("ShopSalePriceYear",allShopSalePrice.subtract(ShopSalePriceYear));
    		}else {
    			resultMap.put("ShopSalePriceYearPercent", "");
				resultMap.put("ShopSalePriceYear",0);
    		}
    		//环比总sku
    		Integer ShopsSaleGoodsYear=saleShoesAnalysisService.getTotalSaleGood(shopSeqList, shoesSeqList, startTime, endTime, startPrice, endPrice);
    		if(ShopsSaleGoodsYear!=0) {
				BigDecimal ShopsSaleGoodsYearPercent=new BigDecimal((double)(allShopsSaleGoods-ShopsSaleGoodsYear)/ShopsSaleGoodsYear).setScale(4, BigDecimal.ROUND_HALF_UP);
				resultMap.put("ShopsSaleGoodsYearPercent", ShopsSaleGoodsYearPercent);
				resultMap.put("ShopsSaleGoodsYear",allShopsSaleGoods-ShopsSaleGoodsYear);
    		}else {
       		 	resultMap.put("ShopsSaleGoodsYearPercent", "");
				resultMap.put("ShopsSaleGoodsYear",0);
    		}
    		if(type==1) {
    			/** 2.根据筛出来的鞋子序号和排序、分页条件，查询鞋子销量列表  **/
    			List<Map<String,Object>> shoesList = saleShoesAnalysisService.getHqShoesRankListByParam(shoesSeqList, shopSeqList, saleTimeStart, saleTimeEnd, orderBy, orderDir, start, num, startPrice, endPrice,loginUser);
        		/**处理图片**/
    			for(Map<String, Object> map : shoesList) {
    				//图片
    				map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
    				//售罄率
    				if(map.get("selloutRate") != null) {
    					map.put("selloutRate", map.get("selloutRate") + "%");
    				}
					BigDecimal salesNum = new BigDecimal(0);
					if(map.get("saleCount") != null) {
						salesNum = new BigDecimal(map.get("saleCount").toString());
					}
					Wrapper<SaleShoesDetailEntity> saleShoesDetailEntityWrapper = new EntityWrapper<>();
					saleShoesDetailEntityWrapper.eq("ShoeSeq",map.get("seq")).orderBy("SaleDate",true);
					List<SaleShoesDetailEntity> saleShoesDetailEntities = saleShoesDetailService.selectList(saleShoesDetailEntityWrapper);
					if(saleShoesDetailEntities.size() > 0) {
						Date saleDate = saleShoesDetailEntities.get(0).getSaleDate();
						Integer days = 0;
						if(saleTimeStart.getTime() - saleDate.getTime() > 0L) {
							days = (int)((saleTimeEnd.getTime() - saleTimeStart.getTime()) / (1000*60*60*24));
						}else {
							days = (int)((saleTimeEnd.getTime() - saleDate.getTime()) / (1000*60*60*24));
						}
						if(salesNum.compareTo(BigDecimal.ZERO) > 0) {
							BigDecimal salesDayAvg = salesNum.divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP);
							map.put("salesDayAvg",salesDayAvg);
						}else {
							map.put("salesDayAvg",0);
						}
					}else {
						map.put("salesDayAvg",0);
					}
    			}
    			resultMap.put("shoesListBySale", shoesList);
    		}
		
			if(type==2) {
				/**3.根据筛出来的鞋子序号和排序、分页条件，查询鞋子销售金额列表 **/
	    		List<Map<String,Object>> shoesListByMoney = saleShoesAnalysisService.getHqShoesRankListByMoney(shoesSeqList, shopSeqList, saleTimeStart, saleTimeEnd, orderBy, orderDir, start, num, startPrice, endPrice,loginUser);
	    		/**处理图片**/
				for(Map<String, Object> map : shoesListByMoney) {
					//图片
					map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
				}
				resultMap.put("shoesListByMoney", shoesListByMoney);
			}
			if(type==3) {
				/**4.根据筛出来的鞋子序号和排序、分页条件，查询鞋子sku列表 **/
				List<Map<String,Object>> shoesList = saleShoesAnalysisService.getHqShoesRankListByParam(shoesSeqList, shopSeqList, saleTimeStart, saleTimeEnd, orderBy, orderDir, start, num, startPrice, endPrice,loginUser);
        		/**处理图片**/
    			for(Map<String, Object> map : shoesList) {
    				//图片
    				map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
    				//售罄率
    				if(map.get("selloutRate") != null) {
    					map.put("selloutRate", map.get("selloutRate") + "%");
    				}
					BigDecimal salesNum = new BigDecimal(0);
					if(map.get("saleCount") != null) {
						salesNum = new BigDecimal(map.get("saleCount").toString());
					}
					Wrapper<SaleShoesDetailEntity> saleShoesDetailEntityWrapper = new EntityWrapper<>();
					saleShoesDetailEntityWrapper.eq("ShoeSeq",map.get("seq")).orderBy("SaleDate",true);
					List<SaleShoesDetailEntity> saleShoesDetailEntities = saleShoesDetailService.selectList(saleShoesDetailEntityWrapper);
					if(saleShoesDetailEntities.size() > 0) {
						Date saleDate = saleShoesDetailEntities.get(0).getSaleDate();
						Integer days = 0;
						if(saleTimeStart.getTime() - saleDate.getTime() > 0L) {
							days = (int)((saleTimeEnd.getTime() - saleTimeStart.getTime()) / (1000*60*60*24));
						}else {
							days = (int)((saleTimeEnd.getTime() - saleDate.getTime()) / (1000*60*60*24));
						}
						if(salesNum.compareTo(BigDecimal.ZERO) > 0) {
							BigDecimal salesDayAvg = salesNum.divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP);
							map.put("salesDayAvg",salesDayAvg);
						}else {
							map.put("salesDayAvg",0);
						}
					}else {
						map.put("salesDayAvg",0);
					}
    			}
				resultMap.put("shoesListBySKU", shoesList);
			}
	
			
    		List<Map<String,Object>> saleNumPercents=new ArrayList<Map<String,Object>>();
    		List<Map<String,Object>> salePricePercents=new ArrayList<Map<String,Object>>();
    		List<Map<String, Object>> saleGoodsPercents=new ArrayList<Map<String,Object>>();
    		
			 for (Integer categorySeq : categorySeqList) {
				 
				 
				 
				Map<String, Object> saleNumPercentMap=new HashMap<String, Object>();
				Map<String, Object> salePricePercentMap=new HashMap<String, Object>();
				Map<String, Object> saleGoodsPercentMap=new HashMap<String, Object>();
				 
				 //查询分类名称
				 GoodsCategoryEntity goodsCategoryEntity=goodsCategoryService.selectById(categorySeq);
				 List<Integer> cateList=new ArrayList<Integer>();
				 cateList.clear();
				 cateList.add(categorySeq);
				 List<Integer> shoesSeqListByCategory = goodsShoesService.getShoesSeqListByParam(loginUser.getCompanySeq(), yearList, seasonSeqList, cateList, colorSeqList, sXMap, oldOrNew);
				 if(shoesSeqListByCategory==null||shoesSeqListByCategory.size()==0) {
					 continue;
				 }
				 
				 
				 //销量
				
				 Integer shopsSaleNum = saleShoesAnalysisService.getTotalSaleNum(shopSeqList, shoesSeqListByCategory, saleTimeStart, saleTimeEnd, startPrice, endPrice);
			
				 //销售金额
				 BigDecimal shopSalePrice=saleShoesAnalysisService.getTotalSalePrice(shopSeqList, shoesSeqListByCategory, saleTimeStart, saleTimeEnd, startPrice, endPrice);
				 //sku
				 Integer shopsSaleGoods=saleShoesAnalysisService.getTotalSaleGood(shopSeqList, shoesSeqListByCategory, saleTimeStart, saleTimeEnd, startPrice, endPrice);
				 BigDecimal saleNumPercent=null;
				 if(allShopsSaleNum!=0) {
						  saleNumPercent=new BigDecimal((double)shopsSaleNum/allShopsSaleNum).setScale(4, BigDecimal.ROUND_HALF_UP);
				}
				 BigDecimal salePricePercent=null;
				if(allShopSalePrice.compareTo(BigDecimal.ZERO)!=0) {
					 salePricePercent=shopSalePrice.divide(allShopSalePrice,4, BigDecimal.ROUND_HALF_UP);
				}
				 BigDecimal saleGoodsPercent=null;
				 if(allShopsSaleGoods!=0) {
					  saleGoodsPercent=new BigDecimal((double)shopsSaleGoods/allShopsSaleGoods).setScale(4, BigDecimal.ROUND_HALF_UP); 
				 }
				 saleNumPercentMap.put("categoryName", goodsCategoryEntity.getName());
				 saleNumPercentMap.put("CategorySeq", goodsCategoryEntity.getSeq());
				 saleNumPercentMap.put("saleNumPercent", saleNumPercent);
				 saleNumPercentMap.put("shopsSaleNum", shopsSaleNum);
				 
				 salePricePercentMap.put("categoryName", goodsCategoryEntity.getName());
				 salePricePercentMap.put("CategorySeq", goodsCategoryEntity.getSeq());
				 salePricePercentMap.put("salePricePercent", salePricePercent);
				 salePricePercentMap.put("shopSalePrice", shopSalePrice);
				 
				 saleGoodsPercentMap.put("categoryName", goodsCategoryEntity.getName());
				 saleGoodsPercentMap.put("CategorySeq", goodsCategoryEntity.getSeq());
				 saleGoodsPercentMap.put("saleGoodsPercent", saleGoodsPercent);
				 saleGoodsPercentMap.put("shopsSaleGoods", shopsSaleGoods);
				 
				 saleNumPercents.add(saleNumPercentMap);
				 
				 salePricePercents.add(salePricePercentMap);
				 
				 saleGoodsPercents.add(saleGoodsPercentMap);
			 
			 }
		
			resultMap.put("saleNumPercents", saleNumPercents);
			resultMap.put("salePricePercents", salePricePercents);
			resultMap.put("saleGoodsPercents", saleGoodsPercents);
			
    		return R.ok(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    /**
     * 销售品类分析
     */
    @Login
    @GetMapping("ShoesRankList")
    @ApiOperation("销售品类分析列表")
    public R ShoesRankList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
    		@ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
    		@ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
    		@ApiParam("新老款")@RequestParam(value = "oldOrNew",required = false)Integer oldOrNew,
    		@ApiParam("起始价格")@RequestParam(value = "startPrice",required = false)BigDecimal startPrice,
    		@ApiParam("起始价格")@RequestParam(value = "endPrice",required = false)BigDecimal endPrice,
    		@ApiParam("店铺筛选")@RequestParam(value = "shopSeqList",required = false)List<Integer> shopSeqList,
    		@ApiParam("排序字段(1:销量 2:库存 3:售罄率 4:平均售价)") @RequestParam("orderBy") Integer orderBy,
    		@ApiParam("排序方式(0:降序 1:升序)") @RequestParam("orderDir") Integer orderDir,
    		@ApiParam("起始条数") @RequestParam("start") Integer start,
    		@ApiParam("总条数") @RequestParam("num") Integer num,
    		@ApiParam("颜色") @RequestParam(value = "colorSeqList", required = false) List<Integer> colorSeqList,
    		@ApiParam(value="自定义属性", example="{SX1:'001',SX2:'002'}") @RequestParam(value = "sXMap", required = false, defaultValue = "{}") String sXMapStr,
    		@ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart", required = false) Date saleTimeStart,
    		@ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd", required = false) Date saleTimeEnd,@ApiParam("传入的类型")@RequestParam(value = "type") Integer type) {
    	try {
    		saleTimeEnd = DateUtils.addDateDays(saleTimeEnd, 1);
    		@SuppressWarnings("unchecked")
			Map<String, String> sXMap = JSONObject.parseObject(sXMapStr, Map.class);
    		/**1.根据属性条件，查询满足属性条件的所有鞋子序号**/
			List<Integer> shoesSeqList = goodsShoesService.getShoesSeqListOnSXParam(loginUser.getCompanySeq(), yearList, seasonSeqList, categorySeqList, colorSeqList, sXMap, null,saleTimeStart,saleTimeEnd,null,null);
    		if(shoesSeqList == null || shoesSeqList.size() == 0) {
    			return R.ok("没有满足条件的商品");
    		}
    		
    		
    		if(categorySeqList == null || categorySeqList.size() == 0) {
    			categorySeqList=goodsCategoryService.getCategorySeqList(0);
    		}
    
			/**  添加汇总数据：货号、时间范围内的总销量、总库存、总进货量、总售罄率、总平均售价  **/
			Map<String, Object> resultMap = new HashMap<String, Object>();
    	
    		
    		if(type==1) {
    			/** 2.根据筛出来的鞋子序号和排序、分页条件，查询鞋子销量列表  **/
    			List<Map<String,Object>> shoesList = saleShoesAnalysisService.getHqShoesRankListByParam(shoesSeqList, shopSeqList, saleTimeStart, saleTimeEnd, orderBy, orderDir, start, num, startPrice, endPrice,loginUser);
        		/**处理图片**/
    			for(Map<String, Object> map : shoesList) {
    				//图片
    				map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
    				//售罄率
    				if(map.get("selloutRate") != null) {
    					map.put("selloutRate", map.get("selloutRate") + "%");
    				}
    			}
    			resultMap.put("shoesListBySale", shoesList);
    		}
		
			if(type==2) {
				/**3.根据筛出来的鞋子序号和排序、分页条件，查询鞋子销售金额列表 **/
	    		List<Map<String,Object>> shoesListByMoney = saleShoesAnalysisService.getHqShoesRankListByMoney(shoesSeqList, shopSeqList, saleTimeStart, saleTimeEnd, orderBy, orderDir, start, num, startPrice, endPrice,loginUser);
	    		/**处理图片**/
				for(Map<String, Object> map : shoesListByMoney) {
					//图片
					map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
				}
				resultMap.put("shoesListByMoney", shoesListByMoney);
			}
			if(type==3) {
				/**4.根据筛出来的鞋子序号和排序、分页条件，查询鞋子sku列表 **/
				List<Map<String,Object>> shoesList = saleShoesAnalysisService.getHqShoesRankListByParam(shoesSeqList, shopSeqList, saleTimeStart, saleTimeEnd, orderBy, orderDir, start, num, startPrice, endPrice,loginUser);
        		/**处理图片**/
    			for(Map<String, Object> map : shoesList) {
    				//图片
    				map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
    				//售罄率
    				if(map.get("selloutRate") != null) {
    					map.put("selloutRate", map.get("selloutRate") + "%");
    				}
    			}
				resultMap.put("shoesListBySKU", shoesList);
			}
    		return R.ok(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
     
    
    
    
    
    
    
    
    
    /**
     * 门店各个货品销量排行
     */
    @Login
    @GetMapping("shopShoesRankList")
    @ApiOperation("门店各个货品销量排行")
    public R shopShoesRankList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("门店序号") @RequestParam("shopSeq") Integer shopSeq,
    		@ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
    		@ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
    		@ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList,
    		@ApiParam("颜色") @RequestParam(value = "colorSeqList", required = false) List<Integer> colorSeqList,
    		@ApiParam(value="自定义属性", example="{SX1:'001',SX2:'002'}") @RequestParam(value = "sXMap", required = false, defaultValue = "{}") String sXMapStr,
    		
    		@ApiParam("排序字段(1:销量 2:库存 3:售罄率 4:平均售价)") @RequestParam("orderBy") Integer orderBy,
    		@ApiParam("排序方式(0:降序 1:升序)") @RequestParam("orderDir") Integer orderDir,
    		@ApiParam("起始条数") @RequestParam("start") Integer start,
    		@ApiParam("总条数") @RequestParam("num") Integer num,
    		
    		@ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart", required = false) Date saleTimeStart,
    		@ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd", required = false) Date saleTimeEnd) {
    	try {
    		saleTimeEnd = DateUtils.addDateDays(saleTimeEnd, 1);
    		
    		@SuppressWarnings("unchecked")
			Map<String, String> sXMap = JSONObject.parseObject(sXMapStr, Map.class);
    		
    		/**1.根据属性条件，查询满足属性条件的所有鞋子序号**/
			List<Integer> shoesSeqList = goodsShoesService.getShoesSeqListOnSXParam(loginUser.getCompanySeq(), yearList, seasonSeqList, categorySeqList, colorSeqList, sXMap, null,saleTimeStart,saleTimeEnd,null,null);
    		
    		
    		/** 2.根据范围、类型、尺码条件，筛选出满足条件的所有鞋子序号  **/
    		// 门店订过的（有过库存的）鞋子序号
    		List<Object> shopOrderedShoeSeqs = shoesDataService.getShopPickShoeSeqs(loginUser.getCompanySeq(), Arrays.asList(shopSeq));
    		shoesSeqList.retainAll(shopOrderedShoeSeqs); //求交集
    		if(shoesSeqList == null || shoesSeqList.size() == 0) {
    			return R.ok("没有满足条件的商品");
    		}
    		
    		/** 3.根据筛出来的鞋子序号和排序、分页条件，查询鞋子列表  **/
    		List<Map<String,Object>> shoesList = saleShoesAnalysisService.getShopShoesRankList(shopSeq, shoesSeqList, saleTimeStart, saleTimeEnd, orderBy, orderDir, start, num);
    		
    		/**处理图片**/
			for(Map<String, Object> map : shoesList) {
				//图片
				map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
				//售罄率
				if(map.get("selloutRate") != null) {
					map.put("selloutRate", map.get("selloutRate") + "%");
				}
			}
    		
			
			
			/**  添加汇总数据：货号、时间范围内的总销量、总库存、总进货量、总售罄率、总平均售价  **/
			Map<String, Object> resultMap = new HashMap<String, Object>();
    		//销量
    		Integer allShopsSaleNum = saleShoesAnalysisService.getShopTotalSaleNum(shopSeq, shoesSeqList, saleTimeStart, saleTimeEnd);
    		resultMap.put("total_saleCount", allShopsSaleNum);
    		
    		//平均售价
    		BigDecimal avgSalePrice = saleShoesAnalysisService.getShopTotalAvgSalePrice(shopSeq, shoesSeqList, saleTimeStart, saleTimeEnd);
    		resultMap.put("total_avgSalePrice", avgSalePrice);
    		
    		//进货量、售罄率
			//总部加门店的进货总量
			Integer hqTotalInNum = saleShoesAnalysisService.getShopTotalInNum(shopSeq, shoesSeqList, saleTimeStart, saleTimeEnd);
			resultMap.put("total_inNum", hqTotalInNum);
			//计算售罄率
			BigDecimal sellOutRate = BigDecimal.ZERO;
			if(allShopsSaleNum != null && hqTotalInNum != null && hqTotalInNum != 0) {
				sellOutRate = BigDecimal.valueOf(allShopsSaleNum).divide(BigDecimal.valueOf(hqTotalInNum), 2, RoundingMode.HALF_UP);
			}
			resultMap.put("total_sellOutRate", sellOutRate);
			
    		//库存
			Integer hqAndShopStockNum = saleShoesAnalysisService.getShopTotalStockNum(shopSeq, shoesSeqList);
			resultMap.put("total_stock", hqAndShopStockNum);
			
			
			resultMap.put("shoesList", shoesList);
			
    		return R.ok(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
	
    
    
    
    /**
     * 销量门店排行
     */
    @Login
    @GetMapping("shopRankList")
    @ApiOperation("销量门店排行")
    public R shopRankList(@ApiIgnore @LoginUser BaseUserEntity loginUser,   		
    		@ApiParam("排序字段(1:销量 2:库存 3:售罄率 4:平均售价)") @RequestParam("orderBy") Integer orderBy,
    		@ApiParam("排序方式(0:降序 1:升序)") @RequestParam("orderDir") Integer orderDir,
    		@ApiParam("起始条数") @RequestParam("start") Integer start,
    		@ApiParam("总条数") @RequestParam("num") Integer num,
    		
    		@ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart", required = false) Date saleTimeStart,
    		@ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd", required = false) Date saleTimeEnd) {
    	try {
   		saleTimeEnd = DateUtils.addDateDays(saleTimeEnd, 1);
    		
    		/**查询门店列表  **/
    		List<Map<String,Object>> shopList = saleShoesAnalysisService.getShopRankList(loginUser.getCompanySeq(), saleTimeStart, saleTimeEnd, orderBy, orderDir, start, num);
    		
    		/**处理图片**/
			for(Map<String, Object> map : shopList) {
				//图片
				map.put("img", getBaseShopPictureUrl(map.get("shopSeq").toString()) + map.get("shopImage"));
				//售罄率
				if(map.get("selloutRate") != null) {
					map.put("selloutRate", map.get("selloutRate") + "%");
				}
			}
    		
    		return R.ok(shopList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    
    
	
}
