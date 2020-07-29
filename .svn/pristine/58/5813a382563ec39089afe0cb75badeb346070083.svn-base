package io.nuite.modules.system.controller.order_platform;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.controller.ShoesInfoController;
import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationEntity;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.service.SaleShoesDetailService;
import io.nuite.modules.order_platform_app.service.ShoesDataDailyDetailService;
import io.nuite.modules.order_platform_app.service.ShoesDataService;
import io.nuite.modules.order_platform_app.service.ShoesInfoService;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import io.nuite.modules.sr_base.service.BaseShopService;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import io.nuite.modules.sr_base.service.GoodsSizeService;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.entity.order_platform.AllocateTransferFactoryEntity;
import io.nuite.modules.system.entity.order_platform.AllocateTransferPreviewEntity;
import io.nuite.modules.system.model.AllocateDataForm;
import io.nuite.modules.system.model.AllocateForm;
import io.nuite.modules.system.service.SysPeriodService;
import io.nuite.modules.system.service.order_platform.AllocateTransferFactoryService;
import io.nuite.modules.system.service.order_platform.AllocateTransferPreviewService;
import io.swagger.annotations.ApiParam;

/**
 * 后台 - 调拨管理
 * @author yy
 * @date 2019-09-04 16:47
 */
@RestController
@RequestMapping("system/allocateAndTransfer")
public class AllocateAndTransferController extends AbstractController {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ShoesInfoController shoesInfoController;
    
    @Autowired
    private SysPeriodService sysPeriodService;
    
    @Autowired
    private ShoesInfoService shoesInfoService;
    
    @Autowired
    private ShoesDataService shoesDataService;
    
    
    @Autowired
    private ShoesDataDailyDetailService shoesDataDailyDetailService;
    
    @Autowired
    private SaleShoesDetailService saleShoesDetailService;
    
    @Autowired
	private BaseShopService shopService;
   

    @Autowired
    protected ConfigUtils configUtils;
    
    @Autowired
    private GoodsSizeService goodsSizeService;
   
    @Autowired
    private AllocateTransferFactoryService allocateTransferFactoryService;
    
    @Autowired
    private AllocateTransferPreviewService allocateTransferPreviewService;
   
    @Autowired
    private GoodsShoesService goodsShoesService;
    
    @Autowired
    private BaseShopService baseShopService;
    
 
    
    
    /**
     * 货品列表
     */
    @GetMapping("goodsList")
    public R goodsList(
    		@ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
    		@ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
    		@ApiParam("分类") @RequestParam(value = "categorySeq", required = false) Integer categorySeq,

    		@ApiParam("尺码类型： 0:断码 1:缺码 2:自定义 3:齐码") @RequestParam(value = "sizeType", required = false) Integer sizeType,
    		@ApiParam("尺码：起始") @RequestParam(value = "sizeSeqStart", required = false) Integer sizeSeqStart,
    		@ApiParam("尺码：结束") @RequestParam(value = "sizeSeqEnd", required = false) Integer sizeSeqEnd,
    		
    		@ApiParam("库存最小数量") @RequestParam(value = "stockMinNum", required = false) Integer stockMinNum,
    		@ApiParam("库存最大数量") @RequestParam(value = "stockMaxNum", required = false) Integer stockMaxNum,
    		
    		@ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart", required = false) String saleTimeStart,
    		@ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd", required = false) String saleTimeEnd,
    		
		  	@ApiParam("门店序号")@RequestParam(value = "shopSeqList", required = false)List<Integer> shopSeqList,
    		
    		@ApiParam("模糊查询条件") @RequestParam(value = "keywords", required = false) String fuzzySearchParam,
    		
    		@ApiParam("补单") @RequestParam(value = "isReplenish", required = false) Integer supplement,
    		
    		@ApiParam("排序字段(1:销量 2:库存 3:售罄率 4:日平均销量 5:总库存 6:总仓库存 7:门店总库存)") @RequestParam("orderBy") Integer orderBy,
    		@ApiParam("排序方式(0:降序 1:升序)") @RequestParam("orderDir") Integer orderDir,
    		@RequestParam("page") Integer pageNum,
    		@RequestParam("limit") Integer pageSize,
    		@RequestParam("sidx") String sidx,
    		@RequestParam("sord") String sord,
    		@ApiParam("入仓时间") @RequestParam(value = "depositDate", required = false) String depositDate
    		) {
        try {
        	if(sidx!=null&&!"".equals(sidx)) {
        		
        		if("salesNum".equals(sidx)) {
        			orderBy = 1;
        		} else if("stock".equals(sidx)) {
        			orderBy = 2;
        		} else if("selloutRate".equals(sidx)) {
        			orderBy = 3;
        		} else if("salesDayAvg".equals(sidx)) {
        			orderBy = 4;
        		} else if("webtotalNum".equals(sidx)) {
         			orderBy = 5;
         		} else if("webtotalStockNum".equals(sidx)) {
         			orderBy = 6;
         		} else if("webtotalShopNum".equals(sidx)) {
         			orderBy = 7;
         		}
        		
        		
        		if("asc".equals(sord)) {
        			orderDir=1;
    			}else {
    				orderDir=0;
    			}
        	}
        	
        	if(sizeType!=null&&sizeType==4) {
        		sizeType=null;
        	}
        	Date saleDateStart=new Date();
        	Date saleDateEnd=new Date();
        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        	if(saleTimeStart == null||("").equals(saleTimeStart)) {
        		Calendar cal=Calendar.getInstance();
        		cal.set(2017, 1, 2);
        		saleDateStart =cal.getTime();
        	}else {
        		saleDateStart=sdf.parse(saleTimeStart);
        	}
        	if(saleTimeEnd == null||("").equals(saleTimeEnd)) {
        		saleDateEnd = new Date();
        	}else {
        		saleDateEnd=sdf.parse(saleTimeEnd);
        	}
        	
        	Date depositTime=null;
        	if(depositDate!=null&&!("").equals(depositDate)) {
        		depositTime=sdf.parse(depositDate);
        	}
        	
        	BaseUserEntity loginUser = getUser();
        	// 范围（0:全部 1:本店）
        	Integer rangeType = 0;
        	// 类型（0:全部货品 1:新品推荐 2:主推款）
        	Integer dataType = 0;

        	// 分类
        	List<Integer> categorySeqList = new ArrayList<Integer>();
        	if(categorySeq!=null) {
        		categorySeqList.add(categorySeq);	
        	}
        	// 颜色
        	List<Integer> colorSeqList = null;
    		// 自定义属性
    		String sXMapStr = null;
    
    		Integer num=pageSize;
    		Integer start=(pageNum-1)*pageSize+1;
    		
        	
        	R resultFromApp = shoesInfoController.getShoesList(loginUser, rangeType, dataType, supplement, 
        					yearList, seasonSeqList, categorySeqList, colorSeqList, sXMapStr, 
        					fuzzySearchParam, shopSeqList, sizeType, sizeSeqStart, sizeSeqEnd, 
        					orderBy, orderDir, start, num, saleDateStart, saleDateEnd, stockMinNum, stockMaxNum,depositTime,1);
        	
        	List<Map<String,Object>> shoesList = (List<Map<String, Object>>) resultFromApp.get("result");
        	
        	if(shoesList != null) {
	        	for (Map<String, Object> map : shoesList) {
					Integer shoesSeq=(Integer) map.get("seq");
					AllocateTransferFactoryEntity allocateTransferFactoryEntity=allocateTransferFactoryService.getAllocateTransLast(shoesSeq);
					ShoesDataDailyDetailEntity shoesDataDailyDetailEntity=shoesDataDailyDetailService.getOldOne(shoesSeq);
					if(shoesDataDailyDetailEntity!=null) {
						map.put("inTime", shoesDataDailyDetailEntity.getInputERPTime());
					}else {
						map.put("inTime", "");
					}
					if(allocateTransferFactoryEntity!=null) {
						map.put("time",sdf.format(allocateTransferFactoryEntity.getInputTime()));
					}else {
						map.put("time", "");
					}
					
				}
	        	
	        	//总记录数
	        	int totalCount = resultFromApp.get("totalCount") != null ? (Integer) resultFromApp.get("totalCount") : 0;
	            PageUtils pageUtils = new PageUtils(shoesList, totalCount, pageSize, pageNum);
	            
	            return R.ok().put("page", pageUtils);
	            
        	} else {
        		
        		PageUtils pageUtils = new PageUtils(null, 0, pageSize, pageNum);
        		return R.ok().put("page", pageUtils);
        	}
        	
        	
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }
    
    
    
    
    
    
    
    
    @PostMapping("add")
    public R add(@RequestBody GoodsPeriodEntity goodsPeriodEntity) {
        try {
            if (goodsPeriodEntity == null) {
                return R.error("数据格式不正确");
            }
            if(goodsPeriodEntity.getMeetingEndTime() != null) {
            	Calendar cal = Calendar.getInstance();
            	cal.setTime(goodsPeriodEntity.getMeetingEndTime());
            	cal.set(Calendar.HOUR_OF_DAY, 23);
            	cal.set(Calendar.MINUTE, 59);
            	cal.set(Calendar.SECOND, 59);
            	goodsPeriodEntity.setMeetingEndTime(cal.getTime());
            }
            sysPeriodService.addGoodsPeriod(getUser().getBrandSeq(), goodsPeriodEntity);
            return R.ok("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @PostMapping("update")
    public R update(@RequestBody GoodsPeriodEntity goodsPeriodEntity) {
        try {
            if (goodsPeriodEntity == null || goodsPeriodEntity.getSeq() == null) {
                return R.error("数据格式不正确");
            }
            if(goodsPeriodEntity.getMeetingEndTime() != null) {
            	Calendar cal = Calendar.getInstance();
            	cal.setTime(goodsPeriodEntity.getMeetingEndTime());
            	cal.set(Calendar.HOUR_OF_DAY, 23);
            	cal.set(Calendar.MINUTE, 59);
            	cal.set(Calendar.SECOND, 59);
            	goodsPeriodEntity.setMeetingEndTime(cal.getTime());
            }
            sysPeriodService.updateGoodsPeriod(goodsPeriodEntity);
            return R.ok("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    /**
     * 删除
     */
    @GetMapping("delete")
    public R delete(@RequestParam("seq") Integer seq) {
        try {
            // 判断该波次下是否有鞋子
            Boolean flag = sysPeriodService.hasShoesInPeriod(seq);
            if (flag) {
                return R.error("该波次已被使用，不可删除");
            }
            sysPeriodService.deleteGoodsPeriod(seq);
            return R.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @GetMapping("edit")
    public R edit(@RequestParam("seq") Integer seq) {
        try {
            GoodsPeriodEntity goodsPeriodEntity = sysPeriodService.edit(seq);
            return R.ok().put("goodsPeriod", goodsPeriodEntity);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }
    
    
    /**
     * 货品门店调拨列表
     */
    @GetMapping("getGoodDetail")
    public R getGoodDetail(
    		@ApiParam("销售时间区间：起始时间") @RequestParam(value = "saleTimeStart", required = false) String saleTimeStart,
    		@ApiParam("销售时间区间：结束时间") @RequestParam(value = "saleTimeEnd", required = false) String saleTimeEnd,
    		@ApiParam("货品序号") @RequestParam("shoesSeq") Integer shoesSeq,
    		@ApiParam("排序字段(1:销量 2:库存 3:售罄率 4:日平均销量)") @RequestParam(value ="orderBy", required = false) Integer orderBy,
    		@ApiParam("排序方式(0:降序 1:升序)") @RequestParam(value ="orderDir", required = false) Integer orderDir
    		) {
    	  try {
    		  if(orderBy==null) {
    			  orderBy=1;
    		  }
    		  if(orderDir==null) {
    			  orderDir=0;
    		  }
    		 //查询当前货号详情
    		  Date saleDateStart=new Date();
          	Date saleDateEnd=new Date();
          	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    		  BaseUserEntity loginUser = getUser();
    		  if(saleTimeStart == null||("").equals(saleTimeStart)) {
          		Calendar cal=Calendar.getInstance();
          		cal.set(2017, 1, 2);
          		saleDateStart =cal.getTime();
          	}else {
          		saleDateStart=sdf.parse(saleTimeStart);
          	}
          	if(saleTimeEnd == null||("").equals(saleTimeEnd)) {
          		saleDateEnd = new Date();
          	}else {
          		saleDateEnd=sdf.parse(saleTimeEnd);
          	}
          	 Map<String, Object> map=shoesInfoService.getShopShoeOnSaleTime(saleDateStart, saleDateEnd, shoesSeq);
				//图片
				map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
				//售罄率
				//如果售罄率存在
				if(map.get("stock")==null) {
					map.put("stock",0);
				}
				
				if(map.get("selloutRate") != null) {
					map.put("selloutRate", map.get("selloutRate") + "%");
					//如果有销量
				}else if(map.get("saleCount") != null) {
					BigDecimal saleCount = new BigDecimal(map.get("saleCount").toString());
					//查询进货记录
					Wrapper<ShoesDataDailyDetailEntity> inWrapper = new EntityWrapper<>();
					inWrapper.where("Shoes_Seq = {0} AND (Type = 0 OR Type = 1)",map.get("seq")).setSqlSelect("Shoes_Seq, SUM (Count) AS num").groupBy("Shoes_Seq");
					Integer inNum = 0;
					if(shoesDataDailyDetailService.selectList(inWrapper).size() > 0) {
						inNum = shoesDataDailyDetailService.selectList(inWrapper).get(0).getNum();
					}
					//查询退货记录
					Wrapper<ShoesDataDailyDetailEntity> outWrapper = new EntityWrapper<>();
					outWrapper.where("Shoes_Seq = {0} AND (Type = 2 OR Type = 4)",map.get("seq")).setSqlSelect("Shoes_Seq, SUM (Count) AS num").groupBy("Shoes_Seq");
					Integer outNum = 0;
					if(shoesDataDailyDetailService.selectList(outWrapper).size() > 0) {
						outNum = shoesDataDailyDetailService.selectList(outWrapper).get(0).getNum();
					}
					//计算总进货量
					BigDecimal totalInNum = new BigDecimal(inNum).subtract(new BigDecimal(outNum));
					if(totalInNum.compareTo(BigDecimal.ZERO) == 0) {
						map.put("selloutRate","0%");
					}else {
						BigDecimal selloutRate = saleCount.divide(totalInNum,2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
						map.put("selloutRate",selloutRate.toString() + "%");
					}
				}else {
					map.put("selloutRate","0%");
				}

				BigDecimal salesNum = new BigDecimal(0);
				if(map.get("salesNum") != null) {
					salesNum = new BigDecimal(map.get("salesNum").toString());
				}else {
					map.put("salesNum", 0);
				}
				//查询销售时间
				Wrapper<SaleShoesDetailEntity> saleShoesDetailEntityWrapper = new EntityWrapper<>();
				saleShoesDetailEntityWrapper.eq("ShoeSeq",map.get("seq")).orderBy("SaleDate",true);
				List<SaleShoesDetailEntity> saleShoesDetailEntities = saleShoesDetailService.selectList(saleShoesDetailEntityWrapper);
				//如果有销售记录
				if(saleShoesDetailEntities.size() > 0) {
					Date saleDate = saleShoesDetailEntities.get(0).getSaleDate();
					Integer days = 0;
					//如果销售时间比开始时间早
					if(saleDateStart.getTime() - saleDate.getTime() > 0L) {
						days = (int)((saleDateEnd.getTime() - saleDateStart.getTime()) / (1000*60*60*24)) + 1;
					}else {
						days = (int)((saleDateEnd.getTime() - saleDate.getTime()) / (1000*60*60*24)) + 1;
					}
					if(salesNum.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal salesDayAvg = salesNum.divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP);
						map.put("salesDayAvg",salesDayAvg);
					}else {
						map.put("salesDayAvg",0);
					}
				//如果没有销售记录
				}else {
					map.put("salesDayAvg",0);
				}

				//实际成交价
				List<Object> shopSeqs = new ArrayList<>(10);
				//获取门店序号
				
					Wrapper<BaseShopEntity> baseShopEntityWrapper = new EntityWrapper<>();
					baseShopEntityWrapper.where("Is_Active = 1");
					baseShopEntityWrapper.eq("Company_Seq",loginUser.getCompanySeq()).setSqlSelect("Seq");
					shopSeqs = shopService.selectObjs(baseShopEntityWrapper);
				
				map.put("dealPrice",saleShoesDetailService.getShopsAvgSalePriceOneShoesSeq(shopSeqs, shoesSeq, saleDateStart, saleDateEnd).setScale(2,BigDecimal.ROUND_HALF_UP));
    		 //查询门店尺码库存、总销量、日均销量、平均售价、总库存
				
				
    		  //根据货号查询尺码
				List<Object> sizeSeqs=shoesDataService.getAllSize(shoesSeq);
				List<Map<String, Object>> sizes=new ArrayList<Map<String, Object>>();
				for (Object sizeSeq : sizeSeqs) {
					GoodsSizeEntity goodsSizeEntity=goodsSizeService.selectById((int)sizeSeq);
					Map<String, Object> map1=new HashMap<String, Object>();
					map1.put("sizeName", goodsSizeEntity.getSizeName());
					map1.put("sizeSeq", goodsSizeEntity.getSeq());
					//查看当前尺码的库存总量
					List<Object> numCounts=shoesDataService.getNumCount(shoesSeq, goodsSizeEntity.getSeq());
					if(numCounts!=null&&numCounts.size()>0) {
						if(numCounts.get(0)==null||"".equals(numCounts.get(0))) {
							map1.put("numCount",0);
						}else {
							map1.put("numCount", numCounts.get(0));
						}
					}else {
						map1.put("numCount",0);
					}
					sizes.add(map1);
				}
				//查询门店信息
				
				
				
				List<Map<String, Object>> list2=shoesInfoService.getShoeOnSaleTimeOnShop(saleDateStart, saleDateEnd, shoesSeq,orderBy,orderDir);
				//添加总店数据
				
			 List<Map<String, Object>> list=shoesInfoService.getHeadOnSaleTime(saleDateStart, saleDateEnd, shoesSeq);
			 list.addAll(list2);
			 List< Map<String, Object>> list3=new ArrayList<Map<String,Object>>();
				for (Map<String, Object> map2 : list) {
					
					
					//根据鞋子序号和门店序号查询第一次进货时间
					ShoesDataDailyDetailEntity shoesDataDailyDetailEntity=shoesDataDailyDetailService.getNearOne(shoesSeq, (int)map2.get("seq"));
					if(shoesDataDailyDetailEntity==null) {
						  map2.put("date","无");
					}else {
						Date startDate=shoesDataDailyDetailEntity.getInputERPTime();
						Date endDate=new Date();
						   int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000*3600*24)) + 1;
						   map2.put("date",days);
					
					}
					
					if(map2.get("stock")==null) {
						map2.put("stock",0);
					}
					map2.put("stockTemp", map2.get("stock"));
					map2.put("oldStockTemp", map2.get("stock"));
					
					if(map2.get("selloutRate") != null) {
						map2.put("selloutRate", map2.get("selloutRate") + "%");
						//如果有销量
					}else if(map2.get("saleCount") != null) {
						BigDecimal saleCount = new BigDecimal(map2.get("saleCount").toString());
						//查询进货记录
						Wrapper<ShoesDataDailyDetailEntity> inWrapper = new EntityWrapper<>();
						inWrapper.where("Shoes_Seq = {0} AND (Type = 0 OR Type = 1) AND Shop_Seq = {1}",shoesSeq,map2.get("seq")).setSqlSelect("Shoes_Seq, SUM (Count) AS num").groupBy("Shoes_Seq");
						Integer inNum = 0;
						if(shoesDataDailyDetailService.selectList(inWrapper).size() > 0) {
							inNum = shoesDataDailyDetailService.selectList(inWrapper).get(0).getNum();
						}
						//查询退货记录
						Wrapper<ShoesDataDailyDetailEntity> outWrapper = new EntityWrapper<>();
						outWrapper.where("Shoes_Seq = {0} AND (Type = 2 OR Type = 4) AND Shop_Seq = {1}",shoesSeq,map2.get("seq")).setSqlSelect("Shoes_Seq, SUM (Count) AS num").groupBy("Shoes_Seq");
						Integer outNum = 0;
						if(shoesDataDailyDetailService.selectList(outWrapper).size() > 0) {
							outNum = shoesDataDailyDetailService.selectList(outWrapper).get(0).getNum();
						}
						//计算总进货量
						BigDecimal totalInNum = new BigDecimal(inNum).subtract(new BigDecimal(outNum));
						if(totalInNum.compareTo(BigDecimal.ZERO) == 0) {
							map2.put("selloutRate","0%");
						}else {
							BigDecimal selloutRate = saleCount.divide(totalInNum,2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
							map2.put("selloutRate",selloutRate.toString() + "%");
						}
					}else {
						map2.put("selloutRate","0%");
					}

					BigDecimal salesNum2 = new BigDecimal(0);
					if(map2.get("salesNum") != null) {
						salesNum2 = new BigDecimal(map2.get("salesNum").toString());
					}else {
						map2.put("salesNum", 0);
					}
					//查询销售时间
					Wrapper<SaleShoesDetailEntity> saleShoesDetailEntityWrapper2 = new EntityWrapper<>();
					saleShoesDetailEntityWrapper2.eq("ShoeSeq",shoesSeq).eq("ShopSeq",map2.get("seq")).orderBy("SaleDate",true);
					List<SaleShoesDetailEntity> saleShoesDetailEntities2= saleShoesDetailService.selectList(saleShoesDetailEntityWrapper2);
					//如果有销售记录
					if(saleShoesDetailEntities2.size() > 0) {
						Date saleDate = saleShoesDetailEntities2.get(0).getSaleDate();
						Integer days = 0;
						//如果销售时间比开始时间早
						if(saleDateStart.getTime() - saleDate.getTime() > 0L) {
							days = (int)((saleDateEnd.getTime() - saleDateStart.getTime()) / (1000*60*60*24)) + 1;
						}else {
							days = (int)((saleDateEnd.getTime() - saleDate.getTime()) / (1000*60*60*24)) + 1;
						}
						if(salesNum2.compareTo(BigDecimal.ZERO) > 0) {
							BigDecimal salesDayAvg = salesNum2.divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP);
							map2.put("salesDayAvg",salesDayAvg);
						}else {
							map2.put("salesDayAvg",0);
						}
					//如果没有销售记录
					}else {
						map2.put("salesDayAvg",0);
					}

					//实际成交价
					List<Object> shopSeqs2 = new ArrayList<>(10);
					shopSeqs2.add(map2.get("seq"));
					map2.put("dealPrice",saleShoesDetailService.getShopsAvgSalePriceOneShoesSeq(shopSeqs2, shoesSeq, saleDateStart, saleDateEnd).setScale(2,BigDecimal.ROUND_HALF_UP));
				
					//根据门店序号查询所有的总库存
						Integer allSum=shoesInfoService.getShopStock((int)map2.get("seq"));
						map2.put("allSum", allSum);
					//获取临时库存
						Integer tempStock=shoesInfoService.getTempStock((int)map2.get("seq"));
						map2.put("tempStock", tempStock);
						map2.put("oldtempStock", tempStock);
						map2.put("addOperate", false);
					//根据门店和货号查询库存
					List<Map<String, Object>> sizeList=shoesInfoService.getAllSizeStock(shoesSeq, (int)map2.get("seq"));
					for (Object sizeSeq : sizeSeqs) {
						GoodsSizeEntity goodsSizeEntity=goodsSizeService.selectById((int)sizeSeq);
							Boolean flag=false;
							for (Map<String, Object> map3 : sizeList) {
								Integer SizeSeq=(Integer) map3.get("sizeSeq");
								if(SizeSeq==(int)sizeSeq) {
									flag=true;
									map3.put("size", goodsSizeEntity.getSizeName());
									break;
								}
							}
							if(!flag) {
								Map<String, Object> sizeMap=new HashMap<String, Object>();
								sizeMap.put("sizeSeq",sizeSeq );
								sizeMap.put("stock", 0);
								sizeMap.put("total", 0);
								sizeMap.put("returnNum", 0);
								sizeMap.put("returnTotal", 0);
								sizeMap.put("size",goodsSizeEntity.getSizeName());
								sizeList.add(sizeMap);
							}
							
					}
					  Collections.sort(sizeList, new Comparator<Map<String, Object>>() {
				            @Override
				            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				                return (Integer) o1.get("sizeSeq") - (Integer) o2.get("sizeSeq");
				            }
				        });
					
					
					
					map2.put("sizeList", sizeList);
					list3.add(map2);
				}
				
				System.out.println(list3);
				
				//总部Map去除
				Map<String, Object> hdMap = list3.get(0);
				
				list3.remove(0);
				
				System.out.println(list3);
				
				//把在仓天数为无的排到最后
				Collections.sort(list3, new Comparator<Map<String, Object>>() {
			            @Override
			            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
			            	if(o1.get("date").toString().equals("无") && !o2.get("date").toString().equals("无")) {
			            		return 1;
			            	} else if(!o1.get("date").toString().equals("无") && o2.get("date").toString().equals("无")) {
			            		return -1;
			            	} else {
				                return 0;
			            	}
			            }
			     });
				 
				 //加入总部
				 list3.add(0, hdMap);
				  
				
				//查询同一尺码的库存总量
				List<Map<String, Object>> sizeList=shoesInfoService.getSizeStock(shoesSeq);
    		  return R.ok().put("map", map).put("list", list3).put("stockList", list3).put("sizes", sizes).put("sizeList", sizeList);
    	  } catch (Exception e) {
              e.printStackTrace();
              logger.error(e.getMessage(), e);
              return R.error("服务器异常");
          }	
    }
    
    @PostMapping("restoreTempStock")
    public R restoreTempStock() {
    	shoesInfoService.restoreTempStock();
    	return R.ok();
    }
    
    /**
     * 调拨预览接口
     * @param emptys[{detail:detail,shoesSeq:375},{detail:detail,shoesSeq:375}]
     * @param allocateList[{shopSeq:1,change:1,sizeSeq:37,shoesSeq:375},{shopSeq:1,change:-1,sizeSeq:37,shoesSeq:376}]
     * @return
     */
    @SuppressWarnings("static-access")
	@PostMapping("allocateTransferPreview")
    public R allocateTransferPreview( @RequestParam("allocateList")String allocateList, @RequestParam(value = "emptys",required = false)String emptys,@RequestParam(value = "tempStockList",required = false)String tempStockList) {
    	//清仓门店
    	  List<AllocateTransferPreviewEntity> list=new ArrayList<AllocateTransferPreviewEntity>();
    	  Date date=new Date();
//    	  if(tempStockList!=null) {
//    		  JSONArray tempStockArray=JSONArray.parseArray(tempStockList);
//    		  for (Object tempStock : tempStockArray) {
//    			  JSONObject tempStockJson=JSONObject.parseObject(tempStock.toString());
//    			  Integer shopSeq=tempStockJson.getInteger("shopSeq");
//    			  Integer tempStockNum=tempStockJson.getInteger("tempStock");
//    			  shoesInfoService.updateTempStock(shopSeq, tempStockNum);
//			}
//    	  }
    	  
    	  if(emptys!=null) {
    	JSONArray emptyArray=JSONArray.parseArray(emptys);
    	for (Object empty : emptyArray) {
			JSONObject emptyJson=JSONObject.parseObject(empty.toString());
			Integer shoesSeq=emptyJson.getInteger("shoesSeq");
			JSONObject detailJson=emptyJson.getJSONObject("detail");
    		Integer shopSeq=detailJson.getInteger("seq");
			JSONArray sizeList=detailJson.getJSONArray("sizeList");
    		for (Object size : sizeList) {
				JSONObject sizeJson=JSONObject.parseObject(size.toString());
    			Integer sizeSeq=sizeJson.getInteger("sizeSeq");
    			Integer stock=sizeJson.getInteger("returnNum");
    			if(stock!=0) {
    				AllocateTransferPreviewEntity allocateTransferPreviewEntity=new AllocateTransferPreviewEntity();
    				allocateTransferPreviewEntity.setInShopSeq(0);
    				allocateTransferPreviewEntity.setNum(stock);
    				allocateTransferPreviewEntity.setOutShopSeq(shopSeq);
    				allocateTransferPreviewEntity.setShoesSeq(shoesSeq);
    				allocateTransferPreviewEntity.setSizeSeq(sizeSeq);
    				allocateTransferPreviewEntity.setInputTime(date);
    				allocateTransferPreviewService.insert(allocateTransferPreviewEntity);
    				list.add(allocateTransferPreviewEntity);
    				
//    				 shoesInfoService.updateChangeTempStock(shopSeq, 0-stock);
//    				
//    				 shoesInfoService.updateChangeTempStock(0, stock);
    			}
    			
			}
		}
    	  }
    	 List<AllocateForm> fromDataBos = JSONArray.parseArray(allocateList, AllocateForm.class);
    	 for (AllocateForm allocateForm : fromDataBos) {
			Integer shopSeq=allocateForm.getShopSeq();
			BaseShopEntity baseShopEntity =shopService.selectById(shopSeq);
			if(baseShopEntity!=null&&baseShopEntity.getRegionSeq()!=null) {
				allocateForm.setRegionSeq(baseShopEntity.getRegionSeq());
			}else {
				allocateForm.setRegionSeq(0);
			}
		}
    	  Map<Integer, List<AllocateForm>> collect = fromDataBos.stream().collect(Collectors.groupingBy(AllocateForm::getRegionSeq,Collectors.toList()));
    	
    	  List<AllocateDataForm> toDataBos = AllocateDataForm.createByMap(collect);
    	 
    	
    	  
    	  
    	  List<AllocateForm> fromDatas=new ArrayList<AllocateForm>();
    	  for (AllocateDataForm allocateDataForm : toDataBos) {
    		  List<AllocateForm> allocates=allocateDataForm.getAllocateList();
    		  for (AllocateForm allocateForm : allocates) {
    			  if(allocateForm.getChange()>0) {
    				  numChangePreview(allocateForm, allocates, list,date);
//    				  for (AllocateForm allocateForm2 : allocates) {
//    					  if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
//    						  AllocateTransferFactoryEntity allocateTransferFactoryEntity=new AllocateTransferFactoryEntity();
//    						  if(allocateForm2.getChange()+allocateForm.getChange()<=0) {
//    							  allocateTransferFactoryEntity.setShoesSeq(allocateForm.getShoesSeq());
//  								allocateTransferFactoryEntity.setSizeSeq(allocateForm.getSizeSeq());
//  								allocateTransferFactoryEntity.setInShopSeq(allocateForm.getShopSeq());
//  								allocateTransferFactoryEntity.setNum(Math.abs(allocateForm.getChange()));
//  								allocateTransferFactoryEntity.setOutShopSeq(allocateForm2.getShopSeq());
//  								allocateForm2.setChange(allocateForm2.getChange()+allocateForm.getChange());
//  								allocateForm.setChange(0);
//  								if(allocateTransferFactoryEntity.getNum()!=0) {
//  	  								allocateTransferFactoryService.insert(allocateTransferFactoryEntity);
//  	      							list.add(allocateTransferFactoryEntity);
//  	  							}
//    						  }
//    						  
//    					  }
//    				  }
    				  
    			  }
    		  }
    		  for (AllocateForm allocateForm : allocates) {
    			  if(allocateForm.getChange()>0) {
    				Integer shopSeq=allocateForm.getShopSeq();
    				if(shopSeq!=0) {
    					BaseShopEntity baseShopEntity=baseShopService.selectById(shopSeq);
        				if(baseShopEntity.getLat()!=null&&baseShopEntity.getLng()!=null) {
        					distanceChangePreview(allocateForm, allocates,baseShopEntity.getLat(),baseShopEntity.getLng(),list,date);
        				}
    				}
    			  }
    		  }
    		  
    		  
    		  
    		  
    		  for (AllocateForm allocateForm : allocates) {
    			  if(allocateForm.getChange()>0) {
    				  for (AllocateForm allocateForm2 : allocates) {
    					if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
    						AllocateTransferPreviewEntity allocateTransferPreviewEntity=new AllocateTransferPreviewEntity();
    						if(allocateForm2.getChange()+allocateForm.getChange()>=0) {
    							allocateTransferPreviewEntity.setShoesSeq(allocateForm.getShoesSeq());
    							allocateTransferPreviewEntity.setSizeSeq(allocateForm.getSizeSeq());
    							allocateTransferPreviewEntity.setInShopSeq(allocateForm.getShopSeq());
    							allocateTransferPreviewEntity.setNum(Math.abs(allocateForm2.getChange()));
    							allocateTransferPreviewEntity.setOutShopSeq(allocateForm2.getShopSeq());
    							allocateTransferPreviewEntity.setInputTime(date);
    								allocateForm.setChange(allocateForm2.getChange()+allocateForm.getChange());
    								allocateForm2.setChange(0);
    							}else {
    								allocateTransferPreviewEntity.setShoesSeq(allocateForm.getShoesSeq());
    								allocateTransferPreviewEntity.setSizeSeq(allocateForm.getSizeSeq());
    								allocateTransferPreviewEntity.setInShopSeq(allocateForm.getShopSeq());
    								allocateTransferPreviewEntity.setNum(Math.abs(allocateForm.getChange()));
    								allocateTransferPreviewEntity.setOutShopSeq(allocateForm2.getShopSeq());
    								allocateTransferPreviewEntity.setInputTime(date);
    								allocateForm2.setChange(allocateForm2.getChange()+allocateForm.getChange());
    								allocateForm.setChange(0);
    							}
    							if(allocateTransferPreviewEntity.getNum()!=0) {
    								allocateTransferPreviewService.insert(allocateTransferPreviewEntity);
        							list.add(allocateTransferPreviewEntity);
    							}
    						
    					}  
    				  }
    			  }
    		  }
    		  fromDatas.addAll(allocates);
    		  
    	  }
    	  for (AllocateForm allocateForm : fromDatas) {
			  if(allocateForm.getChange()>0) {
				  for (AllocateForm allocateForm2 : fromDatas) {
					if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
						AllocateTransferPreviewEntity allocateTransferPreviewEntity=new AllocateTransferPreviewEntity();
							if(allocateForm2.getChange()+allocateForm.getChange()>=0) {
								allocateTransferPreviewEntity.setShoesSeq(allocateForm.getShoesSeq());
								allocateTransferPreviewEntity.setSizeSeq(allocateForm.getSizeSeq());
								allocateTransferPreviewEntity.setInShopSeq(allocateForm.getShopSeq());
								allocateTransferPreviewEntity.setNum(Math.abs(allocateForm2.getChange()));
								allocateTransferPreviewEntity.setOutShopSeq(allocateForm2.getShopSeq());
								allocateTransferPreviewEntity.setInputTime(date);
								allocateForm.setChange(allocateForm2.getChange()+allocateForm.getChange());
								allocateForm2.setChange(0);
							}else {
								allocateTransferPreviewEntity.setShoesSeq(allocateForm.getShoesSeq());
								allocateTransferPreviewEntity.setSizeSeq(allocateForm.getSizeSeq());
								allocateTransferPreviewEntity.setInShopSeq(allocateForm.getShopSeq());
								allocateTransferPreviewEntity.setNum(Math.abs(allocateForm.getChange()));
								allocateTransferPreviewEntity.setOutShopSeq(allocateForm2.getShopSeq());
								allocateTransferPreviewEntity.setInputTime(date);
								allocateForm2.setChange(allocateForm2.getChange()+allocateForm.getChange());
								allocateForm.setChange(0);
							}
							if(allocateTransferPreviewEntity.getNum()!=0) {
								allocateTransferPreviewService.insert(allocateTransferPreviewEntity);
								list.add(allocateTransferPreviewEntity);
							}
							
					}  
				  }
			  }
		  }
    	  //查询添加的调拨数据
    	  for (AllocateTransferPreviewEntity allocateTransferPreviewEntity : list) {
			Integer inShopSeq=allocateTransferPreviewEntity.getInShopSeq();
			Integer outShopSeq=allocateTransferPreviewEntity.getOutShopSeq();
			Date inputDate=allocateTransferPreviewEntity.getInputTime();
			Integer shoesSeq=allocateTransferPreviewEntity.getShoesSeq();
//			//根据条件查询
//			AllocateTransferTransferInApplicationEntity allocateTransferTransferInApplicationEntity=allocateTransferFactoryService.getallocateTransferTransferInApplication(inShopSeq, outShopSeq, shoesSeq, inputDate);
//			if(allocateTransferTransferInApplicationEntity==null) {
//				allocateTransferTransferInApplicationEntity=new AllocateTransferTransferInApplicationEntity();
//				allocateTransferTransferInApplicationEntity.setInShopSeq(inShopSeq);
//				allocateTransferTransferInApplicationEntity.setOutShopSeq(outShopSeq);
//				allocateTransferTransferInApplicationEntity.setShoesSeq(shoesSeq);
//				allocateTransferTransferInApplicationEntity.setInputTime(inputDate);
//				allocateTransferTransferInApplicationEntity.setType(1);
//				allocateTransferTransferInApplicationEntity.setState(0);
//				if(outShopSeq==0) {
//					allocateTransferTransferInApplicationEntity.setState(1);
//				}
//				allocateTransferFactoryService.insertAllocateTransferTransferInApplicationEntity(allocateTransferTransferInApplicationEntity);
//			}
			
			BaseShopEntity baseShopEntity=shopService.selectById(inShopSeq);
			BaseShopEntity baseShopEntity2=shopService.selectById(outShopSeq);
			if(baseShopEntity==null) {
				allocateTransferPreviewEntity.setInShopName("总店");
			}else {
				allocateTransferPreviewEntity.setInShopName(baseShopEntity.getName());	
			}
			if(baseShopEntity2==null) {
				allocateTransferPreviewEntity.setOutShopName("总店");
			}else {
				allocateTransferPreviewEntity.setOutShopName(baseShopEntity2.getName());
			}
			
    	  }
    	  
		return R.ok(list);
    }
    
    public void numChangePreview(AllocateForm allocateForm,List<AllocateForm> allocates,List<AllocateTransferPreviewEntity> list,Date date) {
    	Integer index=null;
    	Double  distance=0d;
    	Integer shopSeq=allocateForm.getShopSeq();
    	String lat=null;
    	String lng=null;
    	if(shopSeq!=0) {
    		BaseShopEntity baseShopEntity=baseShopService.selectById(shopSeq);
   		 	lat=baseShopEntity.getLat();
   		 	lng=baseShopEntity.getLng();
    	}
		if(lat!=null&&lng!=null) {
			AllocateTransferPreviewEntity allocateTransferPreviewEntity=new AllocateTransferPreviewEntity();
			  for (int i = 0; i < allocates.size(); i++) {
		    		AllocateForm allocateForm2=allocates.get(i);
				  if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
					  if(allocateForm2.getChange()+allocateForm.getChange()<=0) {
						  Integer shopSeq2=allocateForm2.getShopSeq();
							BaseShopEntity baseShopEntity2=baseShopService.selectById(shopSeq2);
			    			String lat2=baseShopEntity2.getLat();
			    			String lng2=baseShopEntity2.getLng();
			    			if(lat2!=null&&lng2!=null) {
			    				Double distance2=getDistance(Double.parseDouble(lat), Double.parseDouble(lng),Double.parseDouble(lat2),Double.parseDouble(lng2));
			    				if(distance2<distance||distance==0d) {
			    					distance=distance2;
			    					index=i;
			    				}
			    			}
					  }
				  }
			  }
			  if(index!=null) {
				  AllocateForm allocateForm2=allocates.get(index);
					if(allocateForm2.getChange()+allocateForm.getChange()>=0) {
						allocateTransferPreviewEntity.setShoesSeq(allocateForm.getShoesSeq());
						allocateTransferPreviewEntity.setSizeSeq(allocateForm.getSizeSeq());
						allocateTransferPreviewEntity.setInShopSeq(allocateForm.getShopSeq());
						allocateTransferPreviewEntity.setNum(Math.abs(allocateForm2.getChange()));
						allocateTransferPreviewEntity.setOutShopSeq(allocateForm2.getShopSeq());
						allocateTransferPreviewEntity.setInputTime(date);
						allocateForm.setChange(allocateForm2.getChange()+allocateForm.getChange());
						allocateForm2.setChange(0);
						if(allocateTransferPreviewEntity.getNum()!=0) {
							allocateTransferPreviewService.insert(allocateTransferPreviewEntity);
							list.add(allocateTransferPreviewEntity);
						}
					} 
			  }
		}else {
			  for (AllocateForm allocateForm2 : allocates) {
				  if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
					  AllocateTransferPreviewEntity allocateTransferPreviewEntity=new AllocateTransferPreviewEntity();
					  if(allocateForm2.getChange()+allocateForm.getChange()<=0) {
						  allocateTransferPreviewEntity.setShoesSeq(allocateForm.getShoesSeq());
						  allocateTransferPreviewEntity.setSizeSeq(allocateForm.getSizeSeq());
						  allocateTransferPreviewEntity.setInShopSeq(allocateForm.getShopSeq());
						  allocateTransferPreviewEntity.setNum(Math.abs(allocateForm.getChange()));
						  allocateTransferPreviewEntity.setOutShopSeq(allocateForm2.getShopSeq());
						  allocateTransferPreviewEntity.setInputTime(date);
						allocateForm2.setChange(allocateForm2.getChange()+allocateForm.getChange());
						allocateForm.setChange(0);
						if(allocateTransferPreviewEntity.getNum()!=0) {
							allocateTransferPreviewService.insert(allocateTransferPreviewEntity);
								list.add(allocateTransferPreviewEntity);
							}
					  }
					  
				  }
			  }
		}
		
    }
    
    
    
    /**
     * 根据距离判断调拨
     * @param allocateForm
     * @param allocates
     * @param lat
     * @param lng
     * @param list
     */
    public void distanceChangePreview(AllocateForm allocateForm,List<AllocateForm> allocates,String lat,String lng, List<AllocateTransferPreviewEntity> list,Date date) {
    	Integer index=null;
    	Double  distance=0d;
    	for (int i = 0; i < allocates.size(); i++) {
    		AllocateForm allocateForm2=allocates.get(i);
    		if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
    			Integer shopSeq=allocateForm2.getShopSeq();
				BaseShopEntity baseShopEntity=baseShopService.selectById(shopSeq);
    			String lat2=baseShopEntity.getLat();
    			String lng2=baseShopEntity.getLng();
    			if(lat2!=null&&lng2!=null) {
    				Double distance2=getDistance(Double.parseDouble(lat), Double.parseDouble(lng),Double.parseDouble(lat2),Double.parseDouble(lng2));
    				if(distance2<distance||distance==0d) {
    					distance=distance2;
    					index=i;
    				}
    			}
    		}
		}
    	if(index!=null) {
    		AllocateForm allocateForm2=allocates.get(index);
    		AllocateTransferPreviewEntity allocateTransferPreviewEntity=new AllocateTransferPreviewEntity();
    		if(allocateForm2.getChange()+allocateForm.getChange()>=0) {
    			allocateTransferPreviewEntity.setShoesSeq(allocateForm.getShoesSeq());
    			allocateTransferPreviewEntity.setSizeSeq(allocateForm.getSizeSeq());
    			allocateTransferPreviewEntity.setInShopSeq(allocateForm.getShopSeq());
    			allocateTransferPreviewEntity.setNum(Math.abs(allocateForm2.getChange()));
    			allocateTransferPreviewEntity.setOutShopSeq(allocateForm2.getShopSeq());
    			allocateTransferPreviewEntity.setInputTime(date);
    			allocateForm.setChange(allocateForm2.getChange()+allocateForm.getChange());
    			
    			allocateForm2.setChange(0);
    		}else {
    			allocateTransferPreviewEntity.setShoesSeq(allocateForm.getShoesSeq());
    			allocateTransferPreviewEntity.setSizeSeq(allocateForm.getSizeSeq());
    			allocateTransferPreviewEntity.setInShopSeq(allocateForm.getShopSeq());
    			allocateTransferPreviewEntity.setNum(Math.abs(allocateForm.getChange()));
    			allocateTransferPreviewEntity.setOutShopSeq(allocateForm2.getShopSeq());
    			allocateTransferPreviewEntity.setInputTime(date);
    			allocateForm2.setChange(allocateForm2.getChange()+allocateForm.getChange());
    			allocateForm.setChange(0);
    		}
    		if(allocateTransferPreviewEntity.getNum()!=0) {
    			allocateTransferPreviewService.insert(allocateTransferPreviewEntity);
    			list.add(allocateTransferPreviewEntity);
    		}
    		if(allocateForm.getChange()!=0) {
    			distanceChangePreview(allocateForm, allocates, lat, lng, list,date);
    		}
    	}
    }
    
    
    
    
    /**
     * 调拨接口
     * @param emptys[{detail:detail,shoesSeq:375},{detail:detail,shoesSeq:375}]
     * @param allocateList[{shopSeq:1,change:1,sizeSeq:37,shoesSeq:375},{shopSeq:1,change:-1,sizeSeq:37,shoesSeq:376}]
     * @return
     */
    @SuppressWarnings("static-access")
	@PostMapping("setAllocateTransfer")
    public R setAllocateTransfer( @RequestParam("allocateList")String allocateList, @RequestParam(value = "emptys",required = false)String emptys,@RequestParam(value = "tempStockList",required = false)String tempStockList) {
    	//清仓门店
    	  List<AllocateTransferFactoryEntity> list=new ArrayList<AllocateTransferFactoryEntity>();
    	  Date date=new Date();
    	  if(tempStockList!=null) {
    		  JSONArray tempStockArray=JSONArray.parseArray(tempStockList);
    		  for (Object tempStock : tempStockArray) {
    			  JSONObject tempStockJson=JSONObject.parseObject(tempStock.toString());
    			  Integer shopSeq=tempStockJson.getInteger("shopSeq");
    			  Integer tempStockNum=tempStockJson.getInteger("tempStock");
    			  shoesInfoService.updateTempStock(shopSeq, tempStockNum);
			}
    	  }
    	  
    	  if(emptys!=null) {
    	JSONArray emptyArray=JSONArray.parseArray(emptys);
    	for (Object empty : emptyArray) {
			JSONObject emptyJson=JSONObject.parseObject(empty.toString());
			Integer shoesSeq=emptyJson.getInteger("shoesSeq");
			JSONObject detailJson=emptyJson.getJSONObject("detail");
    		Integer shopSeq=detailJson.getInteger("seq");
			JSONArray sizeList=detailJson.getJSONArray("sizeList");
    		for (Object size : sizeList) {
				JSONObject sizeJson=JSONObject.parseObject(size.toString());
    			Integer sizeSeq=sizeJson.getInteger("sizeSeq");
    			Integer stock=sizeJson.getInteger("stock");
    			if(stock!=0) {
    				AllocateTransferFactoryEntity allocateTransferFactoryEntity=new AllocateTransferFactoryEntity();
    				allocateTransferFactoryEntity.setInShopSeq(0);
    				allocateTransferFactoryEntity.setNum(stock);
    				allocateTransferFactoryEntity.setOutShopSeq(shopSeq);
    				allocateTransferFactoryEntity.setShoesSeq(shoesSeq);
    				allocateTransferFactoryEntity.setSizeSeq(sizeSeq);
    				allocateTransferFactoryEntity.setInputTime(date);
    				allocateTransferFactoryEntity.setCompanySeq(getUser().getCompanySeq());
    				allocateTransferFactoryService.insert(allocateTransferFactoryEntity);
    				list.add(allocateTransferFactoryEntity);
    				
    				 shoesInfoService.updateChangeTempStock(shopSeq, 0-stock);
    				
    				 shoesInfoService.updateChangeTempStock(0, stock);
    			}
    			
			}
		}
    	  }
    	 List<AllocateForm> fromDataBos = JSONArray.parseArray(allocateList, AllocateForm.class);
    	 for (AllocateForm allocateForm : fromDataBos) {
			Integer shopSeq=allocateForm.getShopSeq();
			BaseShopEntity baseShopEntity =shopService.selectById(shopSeq);
			if(baseShopEntity!=null&&baseShopEntity.getRegionSeq()!=null) {
				allocateForm.setRegionSeq(baseShopEntity.getRegionSeq());
			}else {
				allocateForm.setRegionSeq(0);
			}
		}
    	  Map<Integer, List<AllocateForm>> collect = fromDataBos.stream().collect(Collectors.groupingBy(AllocateForm::getRegionSeq,Collectors.toList()));
    	
    	  List<AllocateDataForm> toDataBos = AllocateDataForm.createByMap(collect);
    	 
    	
    	  
    	  
    	  List<AllocateForm> fromDatas=new ArrayList<AllocateForm>();
    	  for (AllocateDataForm allocateDataForm : toDataBos) {
    		  List<AllocateForm> allocates=allocateDataForm.getAllocateList();
    		  for (AllocateForm allocateForm : allocates) {
    			  if(allocateForm.getChange()>0) {
    				  numChange(allocateForm, allocates, list,date);
//    				  for (AllocateForm allocateForm2 : allocates) {
//    					  if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
//    						  AllocateTransferFactoryEntity allocateTransferFactoryEntity=new AllocateTransferFactoryEntity();
//    						  if(allocateForm2.getChange()+allocateForm.getChange()<=0) {
//    							  allocateTransferFactoryEntity.setShoesSeq(allocateForm.getShoesSeq());
//  								allocateTransferFactoryEntity.setSizeSeq(allocateForm.getSizeSeq());
//  								allocateTransferFactoryEntity.setInShopSeq(allocateForm.getShopSeq());
//  								allocateTransferFactoryEntity.setNum(Math.abs(allocateForm.getChange()));
//  								allocateTransferFactoryEntity.setOutShopSeq(allocateForm2.getShopSeq());
//  								allocateForm2.setChange(allocateForm2.getChange()+allocateForm.getChange());
//  								allocateForm.setChange(0);
//  								if(allocateTransferFactoryEntity.getNum()!=0) {
//  	  								allocateTransferFactoryService.insert(allocateTransferFactoryEntity);
//  	      							list.add(allocateTransferFactoryEntity);
//  	  							}
//    						  }
//    						  
//    					  }
//    				  }
    				  
    			  }
    		  }
    		  for (AllocateForm allocateForm : allocates) {
    			  if(allocateForm.getChange()>0) {
    				Integer shopSeq=allocateForm.getShopSeq();
    				if(shopSeq!=0) {
    					BaseShopEntity baseShopEntity=baseShopService.selectById(shopSeq);
        				if(baseShopEntity.getLat()!=null&&baseShopEntity.getLng()!=null) {
        					distanceChange(allocateForm, allocates,baseShopEntity.getLat(),baseShopEntity.getLng(),list,date);
        				}
    				}
    			  }
    		  }
    		  
    		  
    		  
    		  
    		  for (AllocateForm allocateForm : allocates) {
    			  if(allocateForm.getChange()>0) {
    				  for (AllocateForm allocateForm2 : allocates) {
    					if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
    						AllocateTransferFactoryEntity allocateTransferFactoryEntity=new AllocateTransferFactoryEntity();
    						if(allocateForm2.getChange()+allocateForm.getChange()>=0) {
    								allocateTransferFactoryEntity.setShoesSeq(allocateForm.getShoesSeq());
    								allocateTransferFactoryEntity.setSizeSeq(allocateForm.getSizeSeq());
    								allocateTransferFactoryEntity.setInShopSeq(allocateForm.getShopSeq());
    								allocateTransferFactoryEntity.setNum(Math.abs(allocateForm2.getChange()));
    								allocateTransferFactoryEntity.setOutShopSeq(allocateForm2.getShopSeq());
    								allocateTransferFactoryEntity.setInputTime(date);
    								allocateTransferFactoryEntity.setCompanySeq(getUser().getCompanySeq());
    								allocateForm.setChange(allocateForm2.getChange()+allocateForm.getChange());
    								allocateForm2.setChange(0);
    							}else {
    								allocateTransferFactoryEntity.setShoesSeq(allocateForm.getShoesSeq());
    								allocateTransferFactoryEntity.setSizeSeq(allocateForm.getSizeSeq());
    								allocateTransferFactoryEntity.setInShopSeq(allocateForm.getShopSeq());
    								allocateTransferFactoryEntity.setNum(Math.abs(allocateForm.getChange()));
    								allocateTransferFactoryEntity.setOutShopSeq(allocateForm2.getShopSeq());
    								allocateTransferFactoryEntity.setInputTime(date);
    								allocateTransferFactoryEntity.setCompanySeq(getUser().getCompanySeq());
    								allocateForm2.setChange(allocateForm2.getChange()+allocateForm.getChange());
    								allocateForm.setChange(0);
    							}
    							if(allocateTransferFactoryEntity.getNum()!=0) {
    								allocateTransferFactoryService.insert(allocateTransferFactoryEntity);
        							list.add(allocateTransferFactoryEntity);
    							}
    						
    					}  
    				  }
    			  }
    		  }
    		  fromDatas.addAll(allocates);
    		  
    	  }
    	  for (AllocateForm allocateForm : fromDatas) {
			  if(allocateForm.getChange()>0) {
				  for (AllocateForm allocateForm2 : fromDatas) {
					if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
						AllocateTransferFactoryEntity allocateTransferFactoryEntity=new AllocateTransferFactoryEntity();
							if(allocateForm2.getChange()+allocateForm.getChange()>=0) {
								allocateTransferFactoryEntity.setShoesSeq(allocateForm.getShoesSeq());
								allocateTransferFactoryEntity.setSizeSeq(allocateForm.getSizeSeq());
								allocateTransferFactoryEntity.setInShopSeq(allocateForm.getShopSeq());
								allocateTransferFactoryEntity.setNum(Math.abs(allocateForm2.getChange()));
								allocateTransferFactoryEntity.setOutShopSeq(allocateForm2.getShopSeq());
								allocateTransferFactoryEntity.setInputTime(date);
								allocateTransferFactoryEntity.setCompanySeq(getUser().getCompanySeq());
								allocateForm.setChange(allocateForm2.getChange()+allocateForm.getChange());
								allocateForm2.setChange(0);
							}else {
								allocateTransferFactoryEntity.setShoesSeq(allocateForm.getShoesSeq());
								allocateTransferFactoryEntity.setSizeSeq(allocateForm.getSizeSeq());
								allocateTransferFactoryEntity.setInShopSeq(allocateForm.getShopSeq());
								allocateTransferFactoryEntity.setNum(Math.abs(allocateForm.getChange()));
								allocateTransferFactoryEntity.setOutShopSeq(allocateForm2.getShopSeq());
								allocateTransferFactoryEntity.setInputTime(date);
								allocateTransferFactoryEntity.setCompanySeq(getUser().getCompanySeq());
								allocateForm2.setChange(allocateForm2.getChange()+allocateForm.getChange());
								allocateForm.setChange(0);
							}
							if(allocateTransferFactoryEntity.getNum()!=0) {
								allocateTransferFactoryService.insert(allocateTransferFactoryEntity);
								list.add(allocateTransferFactoryEntity);
							}
							
					}  
				  }
			  }
		  }
    	  //查询添加的调拨数据
    	  for (AllocateTransferFactoryEntity allocateTransferFactoryEntity : list) {
			Integer inShopSeq=allocateTransferFactoryEntity.getInShopSeq();
			Integer outShopSeq=allocateTransferFactoryEntity.getOutShopSeq();
			Date inputDate=allocateTransferFactoryEntity.getInputTime();
			Integer shoesSeq=allocateTransferFactoryEntity.getShoesSeq();
			//根据条件查询
			AllocateTransferTransferInApplicationEntity allocateTransferTransferInApplicationEntity=allocateTransferFactoryService.getallocateTransferTransferInApplication(inShopSeq, outShopSeq, shoesSeq, inputDate);
			if(allocateTransferTransferInApplicationEntity==null) {
				allocateTransferTransferInApplicationEntity=new AllocateTransferTransferInApplicationEntity();
				allocateTransferTransferInApplicationEntity.setInShopSeq(inShopSeq);
				allocateTransferTransferInApplicationEntity.setOutShopSeq(outShopSeq);
				allocateTransferTransferInApplicationEntity.setShoesSeq(shoesSeq);
				allocateTransferTransferInApplicationEntity.setInputTime(inputDate);
				allocateTransferTransferInApplicationEntity.setType(1);
				allocateTransferTransferInApplicationEntity.setInUserSeq(getUser().getSeq());
				allocateTransferTransferInApplicationEntity.setState(0);
				if(outShopSeq==0) {
					allocateTransferTransferInApplicationEntity.setState(1);
				}
				allocateTransferFactoryService.insertAllocateTransferTransferInApplicationEntity(allocateTransferTransferInApplicationEntity);
			}
			
			//将调拨申请序号存入工厂调拨表 (20200609 yy 新增申请Seq字段，关联申请表，以解决App段查看调拨无法查看详情的问题)
			AllocateTransferFactoryEntity entity = new AllocateTransferFactoryEntity();
			entity.setSeq(allocateTransferFactoryEntity.getSeq());
			entity.setApplicationSeq(allocateTransferTransferInApplicationEntity.getSeq());
			allocateTransferFactoryService.updateById(entity);
			
			
			BaseShopEntity baseShopEntity=shopService.selectById(inShopSeq);
			BaseShopEntity baseShopEntity2=shopService.selectById(outShopSeq);
			if(baseShopEntity==null) {
				allocateTransferFactoryEntity.setInShopName("总店");
			}else {
				allocateTransferFactoryEntity.setInShopName(baseShopEntity.getName());	
			}
			if(baseShopEntity2==null) {
				allocateTransferFactoryEntity.setOutShopName("总店");
			}else {
				allocateTransferFactoryEntity.setOutShopName(baseShopEntity2.getName());
			}
			
    	  }
    	  
		return R.ok(list);
    }
    
    /**
     * 根据数量满足，距离最近调拨
     * @param allocateForm
     * @param allocates
     * @param list
     */
    public void numChange(AllocateForm allocateForm,List<AllocateForm> allocates,List<AllocateTransferFactoryEntity> list,Date date) {
    	Integer index=null;
    	Double  distance=0d;
    	Integer shopSeq=allocateForm.getShopSeq();
    	String lat=null;
    	String lng=null;
    	if(shopSeq!=0) {
    		BaseShopEntity baseShopEntity=baseShopService.selectById(shopSeq);
   		 	lat=baseShopEntity.getLat();
   		 	lng=baseShopEntity.getLng();
    	}
		if(lat!=null&&lng!=null) {
			  AllocateTransferFactoryEntity allocateTransferFactoryEntity=new AllocateTransferFactoryEntity();
			  for (int i = 0; i < allocates.size(); i++) {
		    		AllocateForm allocateForm2=allocates.get(i);
				  if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
					  if(allocateForm2.getChange()+allocateForm.getChange()<=0) {
						  Integer shopSeq2=allocateForm2.getShopSeq();
							BaseShopEntity baseShopEntity2=baseShopService.selectById(shopSeq2);
			    			String lat2=baseShopEntity2.getLat();
			    			String lng2=baseShopEntity2.getLng();
			    			if(lat2!=null&&lng2!=null) {
			    				Double distance2=getDistance(Double.parseDouble(lat), Double.parseDouble(lng),Double.parseDouble(lat2),Double.parseDouble(lng2));
			    				if(distance2<distance||distance==0d) {
			    					distance=distance2;
			    					index=i;
			    				}
			    			}
					  }
				  }
			  }
			  if(index!=null) {
				  AllocateForm allocateForm2=allocates.get(index);
					if(allocateForm2.getChange()+allocateForm.getChange()>=0) {
						allocateTransferFactoryEntity.setShoesSeq(allocateForm.getShoesSeq());
						allocateTransferFactoryEntity.setSizeSeq(allocateForm.getSizeSeq());
						allocateTransferFactoryEntity.setInShopSeq(allocateForm.getShopSeq());
						allocateTransferFactoryEntity.setNum(Math.abs(allocateForm2.getChange()));
						allocateTransferFactoryEntity.setOutShopSeq(allocateForm2.getShopSeq());
						allocateTransferFactoryEntity.setInputTime(date);
						allocateTransferFactoryEntity.setCompanySeq(getUser().getCompanySeq());
						allocateForm.setChange(allocateForm2.getChange()+allocateForm.getChange());
						allocateForm2.setChange(0);
						if(allocateTransferFactoryEntity.getNum()!=0) {
							allocateTransferFactoryService.insert(allocateTransferFactoryEntity);
							list.add(allocateTransferFactoryEntity);
						}
					} 
			  }
		}else {
			  for (AllocateForm allocateForm2 : allocates) {
				  if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
					  AllocateTransferFactoryEntity allocateTransferFactoryEntity=new AllocateTransferFactoryEntity();
					  if(allocateForm2.getChange()+allocateForm.getChange()<=0) {
						  allocateTransferFactoryEntity.setShoesSeq(allocateForm.getShoesSeq());
						allocateTransferFactoryEntity.setSizeSeq(allocateForm.getSizeSeq());
						allocateTransferFactoryEntity.setInShopSeq(allocateForm.getShopSeq());
						allocateTransferFactoryEntity.setNum(Math.abs(allocateForm.getChange()));
						allocateTransferFactoryEntity.setOutShopSeq(allocateForm2.getShopSeq());
						allocateTransferFactoryEntity.setInputTime(date);
						allocateTransferFactoryEntity.setCompanySeq(getUser().getCompanySeq());
						allocateForm2.setChange(allocateForm2.getChange()+allocateForm.getChange());
						allocateForm.setChange(0);
						if(allocateTransferFactoryEntity.getNum()!=0) {
								allocateTransferFactoryService.insert(allocateTransferFactoryEntity);
								list.add(allocateTransferFactoryEntity);
							}
					  }
					  
				  }
			  }
		}
		
    }
    
    
    
    /**
     * 根据距离判断调拨
     * @param allocateForm
     * @param allocates
     * @param lat
     * @param lng
     * @param list
     */
    public void distanceChange(AllocateForm allocateForm,List<AllocateForm> allocates,String lat,String lng, List<AllocateTransferFactoryEntity> list,Date date) {
    	Integer index=null;
    	Double  distance=0d;
    	for (int i = 0; i < allocates.size(); i++) {
    		AllocateForm allocateForm2=allocates.get(i);
    		if(allocateForm2.getChange()<0&&allocateForm2.getSizeSeq()==allocateForm.getSizeSeq()) {
    			Integer shopSeq=allocateForm2.getShopSeq();
				BaseShopEntity baseShopEntity=baseShopService.selectById(shopSeq);
    			String lat2=baseShopEntity.getLat();
    			String lng2=baseShopEntity.getLng();
    			if(lat2!=null&&lng2!=null) {
    				Double distance2=getDistance(Double.parseDouble(lat), Double.parseDouble(lng),Double.parseDouble(lat2),Double.parseDouble(lng2));
    				if(distance2<distance||distance==0d) {
    					distance=distance2;
    					index=i;
    				}
    			}
    		}
		}
    	if(index!=null) {
    		AllocateForm allocateForm2=allocates.get(index);
        	AllocateTransferFactoryEntity allocateTransferFactoryEntity=new AllocateTransferFactoryEntity();
    		if(allocateForm2.getChange()+allocateForm.getChange()>=0) {
    			allocateTransferFactoryEntity.setShoesSeq(allocateForm.getShoesSeq());
    			allocateTransferFactoryEntity.setSizeSeq(allocateForm.getSizeSeq());
    			allocateTransferFactoryEntity.setInShopSeq(allocateForm.getShopSeq());
    			allocateTransferFactoryEntity.setNum(Math.abs(allocateForm2.getChange()));
    			allocateTransferFactoryEntity.setOutShopSeq(allocateForm2.getShopSeq());
    			allocateTransferFactoryEntity.setInputTime(date);
    			allocateTransferFactoryEntity.setCompanySeq(getUser().getCompanySeq());
    			allocateForm.setChange(allocateForm2.getChange()+allocateForm.getChange());
    			
    			allocateForm2.setChange(0);
    		}else {
    			allocateTransferFactoryEntity.setShoesSeq(allocateForm.getShoesSeq());
    			allocateTransferFactoryEntity.setSizeSeq(allocateForm.getSizeSeq());
    			allocateTransferFactoryEntity.setInShopSeq(allocateForm.getShopSeq());
    			allocateTransferFactoryEntity.setNum(Math.abs(allocateForm.getChange()));
    			allocateTransferFactoryEntity.setOutShopSeq(allocateForm2.getShopSeq());
    			allocateTransferFactoryEntity.setInputTime(date);
    			allocateTransferFactoryEntity.setCompanySeq(getUser().getCompanySeq());
    			allocateForm2.setChange(allocateForm2.getChange()+allocateForm.getChange());
    			allocateForm.setChange(0);
    		}
    		if(allocateTransferFactoryEntity.getNum()!=0) {
    			allocateTransferFactoryService.insert(allocateTransferFactoryEntity);
    			list.add(allocateTransferFactoryEntity);
    		}
    		if(allocateForm.getChange()!=0) {
    			distanceChange(allocateForm, allocates, lat, lng, list,date);
    		}
    	}
    }
    
    private static final double EARTH_RADIUS = 6371393; // 平均半径,单位：m；不是赤道半径。赤道为6378左右
    
    /**
     * @描述 反余弦进行计算
     * @参数  [lat1, lng1, lat2, lng2]
     * @返回值  double
     * @创建人  Young
     * @创建时间  2019/3/13 20:31
     **/
    public static double getDistance(Double lat1,Double lng1,Double lat2,Double lng2) {
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        double radiansAX = Math.toRadians(lng1); // A经弧度
        double radiansAY = Math.toRadians(lat1); // A纬弧度
        double radiansBX = Math.toRadians(lng2); // B经弧度
        double radiansBY = Math.toRadians(lat2); // B纬弧度
 
        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
            + Math.sin(radiansAY) * Math.sin(radiansBY);
//        System.out.println("cos = " + cos); // 值域[-1,1]
        double acos = Math.acos(cos); // 反余弦值
//        System.out.println("acos = " + acos); // 值域[0,π]
//        System.out.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]
        return EARTH_RADIUS * acos; // 最终结果
    }

    
    
    
    @GetMapping("yearList")
    public R yearList() {
    	return R.ok();
    }
    
    
    /**
     * 查询树状结构分类
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam(value = "date",required = false)String dateTime) {
    	List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
    	//查询所有日期
    	
    	List<Map<String, Object>> dates=allocateTransferFactoryService.getDate(dateTime);
    	
    	result.addAll(dates);
    		for (Map<String, Object> map : dates) {
    			//根据日期查询货品
    			String date=(String) map.get("allocateDate");
    			List<Map<String, Object>> shoesList=allocateTransferFactoryService.getShoes(date);
    			result.addAll(shoesList);
    			Integer length=0;
    			for (Map<String, Object> map2 : shoesList) {
					Integer shoesSeq=(Integer) map2.get("shoesSeq");
    				//根据货品序号和日期查询时间
					List<Map<String, Object>> timeList=allocateTransferFactoryService.getTime(date, shoesSeq);
	    			result.addAll(timeList);
	    			map2.put("length", timeList.size());
	    			map2.put("timeList",timeList);
	    			length+= timeList.size();
				}
    			map.put("length",length);
    			map.put("shoesList", shoesList);
    		}
    	return R.ok().put("list", result).put("dates", dates);
    }
    
    @GetMapping("/allList")
    public R allList(@RequestParam(value = "startDate",required = false)String startDate,
    		@RequestParam(value = "endDate",required = false)String endDate,
    		@RequestParam(value = "goodId",required = false)String goodId,
    		@RequestParam("page") Integer pageNum,
    		@RequestParam("limit") Integer pageSize) {
    	 Page<Object> page = new Page<>(pageNum, pageSize);
    	 Page recordList=allocateTransferFactoryService.getAllShoes(startDate, endDate,goodId, page,getUser().getCompanySeq());
    	return R.ok().put("page", new PageUtils(recordList));
    } 
    
    
    
    /**
     * 查询调拨记录
     * @param date
     * @param shoesSeq
     * @param time
     * @return
     */
    @GetMapping("/recordList")
    public R recordList( @RequestParam(value = "date")String date,@RequestParam(value = "shoesSeq",required = false)Integer shoesSeq,@RequestParam(value = "allocateTime",required = false)String allocateTime,
    		@RequestParam("page") Integer pageNum,
    		@RequestParam("limit") Integer pageSize) {
    	//根据date,shoeSeq,time查询记录
    	 Page<Object> page1 = new Page<>(pageNum, pageSize);
    	 Page recordList=allocateTransferFactoryService.getRecords(date, shoesSeq, allocateTime, page1);
    	
    	return R.ok().put("page", new PageUtils(recordList));
    }
    
    @GetMapping("getNearOne")
    public R getNearOne() {
    	AllocateTransferFactoryEntity allocateTransferFactoryEntity=allocateTransferFactoryService.getNearOne();
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	String date="";
    	if(allocateTransferFactoryEntity!=null) {
    		date=sdf.format(allocateTransferFactoryEntity.getInputTime());
    	}
		return R.ok().put("date", date);
    }
    
    
    @PostMapping("/deleteRecord")
    public R delete(  @RequestParam(value = "allChoose")String allChoose
    		) {
    	JSONArray chooseArray=JSONArray.parseArray(allChoose);
 	   for (int j = 0; j < chooseArray.size(); j++) {
        	Object	choose=chooseArray.get(j);
        	JSONObject chooseJson=JSONObject.parseObject(choose.toString());
        	String date=chooseJson.getString("date");
        	Integer shoesSeq=chooseJson.getInteger("shoesSeq");
        	String allocateTime=chooseJson.getString("time");
    	//根据date,shoeSeq,time查询记录
    	allocateTransferFactoryService.deleteRecords(date, shoesSeq, allocateTime);
 	   }
    	return R.ok();
    }
    
    @PostMapping("/pushRecord")
    public R pushRecord( @RequestParam(value = "allChoose")String allChoose
    		) {
    	JSONArray chooseArray=JSONArray.parseArray(allChoose);
    	   for (int j = 0; j < chooseArray.size(); j++) {
           	Object	choose=chooseArray.get(j);
           	JSONObject chooseJson=JSONObject.parseObject(choose.toString());
           	String date=chooseJson.getString("date");
           	Integer shoesSeq=chooseJson.getInteger("shoesSeq");
           	String allocateTime=chooseJson.getString("time");
           	Page<Object> page1 = new Page<>(1, 1000);
        	
          //根据date,shoeSeq,time查询记录
        	allocateTransferFactoryService.pushRecords(date, shoesSeq, allocateTime,getUser().getSeq(),page1,getUser().getCompanySeq());
    	   }
    	 
    
    	
    	return R.ok();
    }
    
    
    
    
    
    /**
     * 调拨记录批量导出（尺码横向）
     *
     * @param response
     */
    @GetMapping("exportRecordHorizontal")
    public void exportRecordHorizontal(HttpServletResponse response,@RequestParam(value = "allChoose")String allChoose) {
    	JSONArray chooseArray=JSONArray.parseArray(allChoose);
        ServletOutputStream out = null;
        HSSFWorkbook wb = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("调拨记录—横向.xls", "UTF-8"));
            // 创建excel
            wb = new HSSFWorkbook();

            //列标题单元格样式
            HSSFCellStyle headCellStyle = wb.createCellStyle();
            //设置居中:
            headCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            headCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            //设置字体:
            HSSFFont font = wb.createFont();
            font.setFontName("仿宋_GB2312");
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            font.setFontHeightInPoints((short) 12);
            headCellStyle.setFont(font);//选择需要用到的字体格式

            //内容单元格样式
            HSSFCellStyle contentCellStyle = wb.createCellStyle();
            //设置居中:
            contentCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            contentCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

            
            
            
            
            /*导出鞋子到第一个sheet */
            // 创建sheet页
            HSSFSheet sheet = wb.createSheet("鞋子");
            //默认宽度和高度
            sheet.setDefaultColumnWidth(15);
            sheet.setDefaultRowHeightInPoints(15);
            //获取每条数据并获取最大和最小尺码
            Integer minSize=null;
            Integer maxSize=null;
            for (int j = 0; j < chooseArray.size(); j++) {
            	Object	choose=chooseArray.get(j);
            	JSONObject chooseJson=JSONObject.parseObject(choose.toString());
            	String date=chooseJson.getString("date");
            	Integer shoesSeq=chooseJson.getInteger("shoesSeq");
            	if(goodsShoesService.selectById(shoesSeq).getGoodsType() == 1) {  //跳过皮具
            		continue;
            	}
            	String allocateTime=chooseJson.getString("time");
            	  Page<Object> page1 = new Page<>(1, 5000);
            	   // 订货平台全部已上架货品列表
                  Page recordPage = allocateTransferFactoryService.getRecords(date, shoesSeq, allocateTime, page1);
                  List<Map<String, Object>> recordList = (List<Map<String, Object>>) recordPage.getRecords();
                  List<Map<String, Object>> allList=new ArrayList<Map<String,Object>>();
                  for (Map<String, Object> map : recordList) {
                	  List<Map<String, Object>> records=(List<Map<String, Object>>) map.get("records");
                	  allList.addAll(records);
                  }
                  for (int i = 0; i < allList.size(); i++) {
                	   Map<String, Object> recordEntity = allList.get(i);// 获得行对象
                	  Integer size=Integer.parseInt(recordEntity.get("size").toString());
                	  if(minSize==null||minSize>size) {
                		  minSize=size;
                	  }
                	  if(maxSize==null||maxSize<size) {
                		  maxSize=size;
                	  }
                  }
            }
            
            
            if(minSize != null && maxSize != null) {
	            List<String> title =new ArrayList<String>();
	            title.add("货品名称");
	            for (Integer i = minSize; i <= maxSize; i++) {
	            	title.add(i.toString());
				}
	            title.add("调出方");
	            title.add("调入方");
	            title.add("调拨时间");
	            HSSFRow row = sheet.createRow(0);
	            row.setHeightInPoints(20);
	            for (int i = 0; i < title.size(); i++) {
	                HSSFCell cell = row.createCell(i);
	                cell.setCellStyle(headCellStyle);
	                cell.setCellValue(title.get(i));
	            }
	            int h = 1;
	            for (int j = 0; j < chooseArray.size(); j++) {
	            	Object	choose=chooseArray.get(j);
	            	JSONObject chooseJson=JSONObject.parseObject(choose.toString());
	            	String date=chooseJson.getString("date");
	            	Integer shoesSeq=chooseJson.getInteger("shoesSeq");
	            	if(goodsShoesService.selectById(shoesSeq).getGoodsType() == 1) {  //跳过皮具
	            		continue;
	            	}
	            	String allocateTime=chooseJson.getString("time");
	            	  Page<Object> page1 = new Page<>(1, 5000);
	            	   // 订货平台全部已上架货品列表
	                  Page recordPage = allocateTransferFactoryService.getRecords(date, shoesSeq, allocateTime, page1);
	                  List<Map<String, Object>> recordList = (List<Map<String, Object>>) recordPage.getRecords();
	                  List<Map<String, Object>> allList=new ArrayList<Map<String,Object>>();
	                  HSSFCell cell;
	                  for (Map<String, Object> map : recordList) {
	                	  List<Map<String, Object>> records=(List<Map<String, Object>>) map.get("records");
	                	  Map<String, Object> goodsMap=new HashMap<String, Object>();
	                	  goodsMap.put("goodID", records.get(0).get("goodID"));
	                	  goodsMap.put("outShopName", records.get(0).get("outShopName"));
	                	  goodsMap.put("inShopName", records.get(0).get("inShopName"));
	                	  goodsMap.put("inputTime", records.get(0).get("inputTime"));
	                	  for (Map<String, Object> map2 : records) {
							String size=map2.get("size").toString();
							String num=map2.get("num").toString();
							goodsMap.put(size, num);
	                	  }
	                	  allList.add(goodsMap);
	                  }
	                  // 循环多少次
	                
	                  for (int i = 0; i < allList.size(); i++) {
	                      row = sheet.createRow(h++);// 创建行
	                      row.setHeightInPoints(20);//行高度
	                      Map<String, Object> recordEntity = allList.get(i);// 获得行对象
	
	                      cell = row.createCell(0);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("goodID").toString());
	                      for (Integer k = minSize; k <= maxSize; k++) {
	                          cell = row.createCell(k-minSize+1);
	                          cell.setCellStyle(contentCellStyle);
	                          if(recordEntity.get(k.toString())!=null) {
	                        	  cell.setCellValue(recordEntity.get(k.toString()).toString()); 
	                          }
	                      }
	                      cell = row.createCell(maxSize-minSize+2);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("outShopName").toString());
	                      
	                      cell = row.createCell(maxSize-minSize+3);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("inShopName").toString());
	                      
	                      cell = row.createCell(maxSize-minSize+4);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("inputTime").toString());
	                  }
				}
            }
            
            
            
            /* 导出皮具到第二个sheet */
            // 创建sheet页
            HSSFSheet sheet2 = wb.createSheet("皮具");
            //默认宽度和高度
            sheet2.setDefaultColumnWidth(15);
            sheet2.setDefaultRowHeightInPoints(15);

            
            //获取每条数据并获取最大和最小尺码
            boolean hasLeather =false;
            for (int j = 0; j < chooseArray.size(); j++) {
            	Object	choose=chooseArray.get(j);
            	JSONObject chooseJson=JSONObject.parseObject(choose.toString());
            	Integer shoesSeq=chooseJson.getInteger("shoesSeq");
            	if(goodsShoesService.selectById(shoesSeq).getGoodsType() == 1) {  //判断是否有皮具
            		hasLeather = true;
            		break;
            	}
            }
            
            
            if(hasLeather) {
	            List<String> title2 =new ArrayList<String>();
	            title2.add("货品名称");
	            title2.add("00");
	            title2.add("调出方");
	            title2.add("调入方");
	            title2.add("调拨时间");
	            HSSFRow row2 = sheet2.createRow(0);
	            row2.setHeightInPoints(20);
	            for (int i = 0; i < title2.size(); i++) {
	                HSSFCell cell = row2.createCell(i);
	                cell.setCellStyle(headCellStyle);
	                cell.setCellValue(title2.get(i));
	            }
	            int h2 = 1;
	            for (int j = 0; j < chooseArray.size(); j++) {
	            	Object	choose=chooseArray.get(j);
	            	JSONObject chooseJson=JSONObject.parseObject(choose.toString());
	            	String date=chooseJson.getString("date");
	            	Integer shoesSeq=chooseJson.getInteger("shoesSeq");
	            	if(goodsShoesService.selectById(shoesSeq).getGoodsType() == 0) {  //跳过鞋子
	            		continue;
	            	}
	            	String allocateTime=chooseJson.getString("time");
	            	  Page<Object> page1 = new Page<>(1, 5000);
	            	   // 订货平台全部已上架货品列表
	                  Page recordPage = allocateTransferFactoryService.getRecords(date, shoesSeq, allocateTime, page1);
	                  List<Map<String, Object>> recordList = (List<Map<String, Object>>) recordPage.getRecords();
	                  List<Map<String, Object>> allList=new ArrayList<Map<String,Object>>();
	                  HSSFCell cell;
	                  for (Map<String, Object> map : recordList) {
	                	  List<Map<String, Object>> records=(List<Map<String, Object>>) map.get("records");
	                	  Map<String, Object> goodsMap=new HashMap<String, Object>();
	                	  goodsMap.put("goodID", records.get(0).get("goodID"));
	                	  goodsMap.put("outShopName", records.get(0).get("outShopName"));
	                	  goodsMap.put("inShopName", records.get(0).get("inShopName"));
	                	  goodsMap.put("inputTime", records.get(0).get("inputTime"));
	                	  for (Map<String, Object> map2 : records) {
							String size=map2.get("size").toString();
							String num=map2.get("num").toString();
							goodsMap.put(size, num);
	                	  }
	                	  allList.add(goodsMap);
	                  }
	                  // 循环多少次
	                
	                  for (int i = 0; i < allList.size(); i++) {
	                      row2 = sheet2.createRow(h2++);// 创建行
	                      row2.setHeightInPoints(20);//行高度
	                      Map<String, Object> recordEntity = allList.get(i);// 获得行对象
	
	                      cell = row2.createCell(0);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("goodID").toString());
	
	                      cell = row2.createCell(1);
	                      cell.setCellStyle(contentCellStyle);
	                      if(recordEntity.get("00")!=null) {
	                    	  cell.setCellValue(recordEntity.get("00").toString()); 
	                      }
	                      
	                      cell = row2.createCell(2);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("outShopName").toString());
	                      
	                      cell = row2.createCell(3);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("inShopName").toString());
	                      
	                      cell = row2.createCell(4);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("inputTime").toString());
	                  }
				}
            }
            
            
            
            
            // 获取输出流，写入excel并关闭
            out = response.getOutputStream();
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出失败: " + e.getMessage(), e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (wb != null) {
                    wb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    
    
    
    /**
     * 调拨记录批量导出（尺码纵向）
     *
     * @param response
     */
    @GetMapping("exportRecordVertical")
    public void exportRecordVertical(HttpServletResponse response,@RequestParam(value = "allChoose")String allChoose) {
    	JSONArray chooseArray=JSONArray.parseArray(allChoose);
        ServletOutputStream out = null;
        HSSFWorkbook wb = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("调拨记录-纵向.xls", "UTF-8"));
            // 创建excel
            wb = new HSSFWorkbook();

            //列标题单元格样式
            HSSFCellStyle headCellStyle = wb.createCellStyle();
            //设置居中:
            headCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            headCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            //设置字体:
            HSSFFont font = wb.createFont();
            font.setFontName("仿宋_GB2312");
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            font.setFontHeightInPoints((short) 12);
            headCellStyle.setFont(font);//选择需要用到的字体格式

            //内容单元格样式
            HSSFCellStyle contentCellStyle = wb.createCellStyle();
            //设置居中:
            contentCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            contentCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

            
            
            /*导出鞋子到第一个sheet */
            // 创建sheet页
            HSSFSheet sheet = wb.createSheet("鞋子");
            //默认宽度和高度
            sheet.setDefaultColumnWidth(15);
            sheet.setDefaultRowHeightInPoints(15);
            //不同列的宽度
            sheet.setColumnWidth(0, 18 * 256);
            sheet.setColumnWidth(1, 18 * 256);
            sheet.setColumnWidth(2, 40 * 256);


            String[] title = {"货品名称", "尺码", "数量","调出方","调入方","调拨时间"};
            HSSFRow row = sheet.createRow(0);
            row.setHeightInPoints(20);
            for (int i = 0; i < title.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(headCellStyle);
                cell.setCellValue(title[i]);
            }
            int h = 1;
            for (int j = 0; j < chooseArray.size(); j++) {
            	Object	choose=chooseArray.get(j);
            	JSONObject chooseJson=JSONObject.parseObject(choose.toString());
            	String date=chooseJson.getString("date");
            	Integer shoesSeq=chooseJson.getInteger("shoesSeq");
            	if(goodsShoesService.selectById(shoesSeq).getGoodsType() == 1) {  //跳过皮具
            		continue;
            	}
            	String allocateTime=chooseJson.getString("time");
            	  Page<Object> page1 = new Page<>(1, 5000);
            	   // 订货平台全部已上架货品列表
                  Page recordPage = allocateTransferFactoryService.getRecords(date, shoesSeq, allocateTime, page1);
                  List<Map<String, Object>> recordList = (List<Map<String, Object>>) recordPage.getRecords();
                  List<Map<String, Object>> allList=new ArrayList<Map<String,Object>>();
                  for (Map<String, Object> map : recordList) {
                	  List<Map<String, Object>> records=(List<Map<String, Object>>) map.get("records");
                	  allList.addAll(records);
                  }
                  // 循环多少次
                  HSSFCell cell;
                  for (int i = 0; i < allList.size(); i++) {
                      row = sheet.createRow(h++);// 创建行
                      row.setHeightInPoints(20);//行高度
                      Map<String, Object> recordEntity = allList.get(i);// 获得行对象

                      cell = row.createCell(0);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("goodID").toString());

                      cell = row.createCell(1);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("size").toString());
                      
                      cell = row.createCell(2);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("num").toString());
                      
                      cell = row.createCell(3);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("outShopName").toString());
                      
                      cell = row.createCell(4);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("inShopName").toString());
                      
                      cell = row.createCell(5);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("inputTime").toString());
                  }
			}
         
          
            /* 导出皮具到第二个sheet */
            // 创建sheet页
            HSSFSheet sheet2 = wb.createSheet("皮具");
            //默认宽度和高度
            sheet2.setDefaultColumnWidth(15);
            sheet2.setDefaultRowHeightInPoints(15);
            //不同列的宽度
            sheet2.setColumnWidth(0, 18 * 256);
            sheet2.setColumnWidth(1, 18 * 256);
            sheet2.setColumnWidth(2, 40 * 256);


            String[] title2 = {"货品名称", "尺码", "数量","调出方","调入方","调拨时间"};
            HSSFRow row2 = sheet2.createRow(0);
            row2.setHeightInPoints(20);
            for (int i = 0; i < title2.length; i++) {
                HSSFCell cell = row2.createCell(i);
                cell.setCellStyle(headCellStyle);
                cell.setCellValue(title2[i]);
            }
            int h2 = 1;
            for (int j = 0; j < chooseArray.size(); j++) {
            	Object	choose=chooseArray.get(j);
            	JSONObject chooseJson=JSONObject.parseObject(choose.toString());
            	String date=chooseJson.getString("date");
            	Integer shoesSeq=chooseJson.getInteger("shoesSeq");
            	if(goodsShoesService.selectById(shoesSeq).getGoodsType() == 0) {  //跳过鞋子
            		continue;
            	}
            	String allocateTime=chooseJson.getString("time");
            	  Page<Object> page1 = new Page<>(1, 5000);
            	   // 订货平台全部已上架货品列表
                  Page recordPage = allocateTransferFactoryService.getRecords(date, shoesSeq, allocateTime, page1);
                  List<Map<String, Object>> recordList = (List<Map<String, Object>>) recordPage.getRecords();
                  List<Map<String, Object>> allList=new ArrayList<Map<String,Object>>();
                  for (Map<String, Object> map : recordList) {
                	  List<Map<String, Object>> records=(List<Map<String, Object>>) map.get("records");
                	  allList.addAll(records);
                  }
                  // 循环多少次
                  HSSFCell cell;
                  for (int i = 0; i < allList.size(); i++) {
                      row2 = sheet2.createRow(h2++);// 创建行
                      row2.setHeightInPoints(20);//行高度
                      Map<String, Object> recordEntity = allList.get(i);// 获得行对象

                      cell = row2.createCell(0);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("goodID").toString());

                      cell = row2.createCell(1);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("size").toString());
                      
                      cell = row2.createCell(2);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("num").toString());
                      
                      cell = row2.createCell(3);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("outShopName").toString());
                      
                      cell = row2.createCell(4);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("inShopName").toString());
                      
                      cell = row2.createCell(5);
                      cell.setCellStyle(contentCellStyle);
                      cell.setCellValue(recordEntity.get("inputTime").toString());
                  }
			}
          
           
            // 获取输出流，写入excel并关闭
            out = response.getOutputStream();
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出失败: " + e.getMessage(), e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (wb != null) {
                    wb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    
    
    
    
    protected String getGoodsShoesPictureUrl(String folder) {
        return baseDir() + configUtils.getGoodsShoes() + "/" + folder + "/";
    }
    private String baseDir() {
        return configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/";
    }
}
