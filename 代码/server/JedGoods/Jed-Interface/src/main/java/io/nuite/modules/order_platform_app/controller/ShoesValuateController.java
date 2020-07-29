package io.nuite.modules.order_platform_app.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.order_platform_app.service.SaleShoesDetailService;
import io.nuite.modules.sr_base.service.BaseShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.common.utils.Constant;
import io.nuite.common.utils.DateUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.entity.ShoesValuateEntity;
import io.nuite.modules.order_platform_app.service.ShoesValuateService;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 鞋子评价、收藏、浏览历史接口
 * @author yy
 * @date 2018-04-09 13:47
 */
@RestController
@RequestMapping("/order/app/shoesValuate")
@Api(tags = "jed鞋子评价、收藏、浏览历史", description = "我的收藏 + 历史记录")
public class ShoesValuateController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ShoesValuateService shoesValuateService;

    @Autowired
	private BaseShopService shopService;

    @Autowired
	private SaleShoesDetailService saleShoesDetailService;
    
    /**
     * 我的浏览记录、收藏记录总数
     */
    /*@Login
    @GetMapping("browseCollectedNum")
    @ApiOperation("我的浏览记录、收藏记录总数")
    public R browseCollectedNum(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq) {
    	try {
    		//浏览记录、收藏记录总数
    		Integer browseNum = shoesValuateService.getBrowseNum(userSeq);
    		Integer collectedNum = shoesValuateService.getCollectedNum(userSeq);
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("browseNum", browseNum);
    		map.put("collectedNum", collectedNum);
			return R.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }*/

	@Login
	@GetMapping("/getBrowseCollectedNum")
	@ApiOperation("我的浏览记录、收藏记录总数")
	public R getBrowseCollectedNum(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
		try {
			Page<ShoesValuateEntity> page = new Page<>(0,10);
			List<Map<String, Object>> browseList = shoesValuateService.getBrowseShoesList(page,loginUser);
			Integer browseNum = page.getTotal();
			List<Map<String, Object>> collectedList = shoesValuateService.getCollectedList(page,loginUser);
			//浏览记录、收藏记录总数
			Integer collectedNum = page.getTotal();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("browseNum", browseNum);
			map.put("collectedNum", collectedNum);
			return R.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
	}
    
    /**
     * 浏览记录
     */
    @Login
    @GetMapping("/getBrowse")
    @ApiOperation("浏览记录")
    public R getBrowse(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("起始条数") @RequestParam("start") Integer start,
    		@ApiParam("总条数") @RequestParam("num") Integer num) {
    	try {
			Page<ShoesValuateEntity> page = new Page<>(start,num);
    		List<Map<String, Object>> shoesValuateList = shoesValuateService.getBrowseShoesList(page,loginUser);

    		/**平均价**/
			for(Map<String, Object> map : shoesValuateList) {
				//图片
				map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
				//售罄率
				if(map.get("selloutRate") != null) {
					map.put("selloutRate", map.get("selloutRate") + "%");
				}
				/*BigDecimal dealPrice1 = map.get("dealPrice1") == null?null : new BigDecimal(map.get("dealPrice1").toString());
				BigDecimal dealPrice2 = map.get("dealPrice2") == null?null : new BigDecimal(map.get("dealPrice2").toString());
				BigDecimal dealPrice3 = map.get("dealPrice3") == null?null : new BigDecimal(map.get("dealPrice3").toString());
				BigDecimal dealPrice = dealPrice1;
				if(dealPrice2 != null) {
					if(dealPrice3 != null) {
						dealPrice = dealPrice1.add(dealPrice2).add(dealPrice3).divide(new BigDecimal(3),2, RoundingMode.HALF_UP);
					}else {
						dealPrice = dealPrice1.add(dealPrice2).divide(new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP);
					}
				}
				//实际成交价
				map.put("dealPrice",dealPrice);*/
				List<Object> shopSeqs = new ArrayList<>(10);
				Integer shoesSeq = (Integer)map.get("shoesSeq");
				Wrapper<BaseShopEntity> baseShopEntityWrapper = new EntityWrapper<>();
				baseShopEntityWrapper.eq("Company_Seq",loginUser.getCompanySeq()).setSqlSelect("Seq");
				shopSeqs = shopService.selectObjs(baseShopEntityWrapper);
				map.put("dealPrice",saleShoesDetailService.getShopsAvgSalePriceOneShoesSeq(shopSeqs, shoesSeq,new SimpleDateFormat("yyyy-MM-dd").parse("2017-1-2"), new Date()));


			}
    		Map<String,Object> result = new HashMap<>(10);
			result.put("result",shoesValuateList);
			result.put("total",page.getTotal());
            return R.ok().result(result);
	       } catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    @Login
    @GetMapping("/getCollected")
    @ApiOperation("收藏记录")
    public R getCollected(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("起始条数") @RequestParam("start") Integer start,
    		@ApiParam("总条数") @RequestParam("num") Integer num) {
    	try {
			Page<ShoesValuateEntity> page = new Page<>(start - 1,num);
    		List<Map<String, Object>> shoesValuateList = shoesValuateService.getCollectedList(page,loginUser);
    		/**平均价**/
			for(Map<String, Object> map : shoesValuateList) {
				//图片
				map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
				//售罄率
				if(map.get("selloutRate") != null) {
					map.put("selloutRate", map.get("selloutRate") + "%");
				}
				/*BigDecimal dealPrice1 = map.get("dealPrice1") == null?null : new BigDecimal(map.get("dealPrice1").toString());
				BigDecimal dealPrice2 = map.get("dealPrice2") == null?null : new BigDecimal(map.get("dealPrice2").toString());
				BigDecimal dealPrice3 = map.get("dealPrice3") == null?null : new BigDecimal(map.get("dealPrice3").toString());
				BigDecimal dealPrice = dealPrice1;
				if(dealPrice2 != null) {
					if(dealPrice3 != null) {
						dealPrice = dealPrice1.add(dealPrice2).add(dealPrice3).divide(new BigDecimal(3),2, RoundingMode.HALF_UP);
					}else {
						dealPrice = dealPrice1.add(dealPrice2).divide(new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP);
					}
				}
				//实际成交价
				map.put("dealPrice",dealPrice);*/
				List<Object> shopSeqs = new ArrayList<>(10);
				Integer shoesSeq = (Integer)map.get("shoesSeq");
				Wrapper<BaseShopEntity> baseShopEntityWrapper = new EntityWrapper<>();
				baseShopEntityWrapper.eq("Company_Seq",loginUser.getCompanySeq()).setSqlSelect("Seq");
				shopSeqs = shopService.selectObjs(baseShopEntityWrapper);
				map.put("dealPrice",saleShoesDetailService.getShopsAvgSalePriceOneShoesSeq(shopSeqs, shoesSeq,new SimpleDateFormat("yyyy-MM-dd").parse("2017-1-2"), new Date()));

			}
			Map<String,Object> result = new HashMap<>(10);
			result.put("result",shoesValuateList);
			result.put("total",page.getTotal());
            return R.ok().result(result);
	       } catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    /**
     * 我的浏览记录列表
     *//*
    @Login
    @GetMapping("getMyBrowse")
    @ApiOperation("我的浏览记录列表")
    public R getMyBrowse(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("起始条数") @RequestParam("start") Integer start,
    		@ApiParam("总条数") @RequestParam("num") Integer num) {
    	try {
    		//1.用户浏览信息列表
    		List<ShoesValuateEntity> shoesValuateList = shoesValuateService.getBrowseShoesValuateList(loginUser.getSeq(), start, num);
    		
			// 2.根据鞋子序号获取鞋子详细信息
			if (shoesValuateList != null && shoesValuateList.size() > 0) {
				List<Integer> shoesSeqList = new ArrayList<Integer>();
				for (ShoesValuateEntity shoesValuate : shoesValuateList) {
					shoesSeqList.add(shoesValuate.getShoesSeq());
				}
				List<Map<String, Object>> shoesList = shoesValuateService.getShoesBySeqList(shoesSeqList);
				
				for (Map<String, Object> map : shoesList) {
					map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
					
					//按下架时间判断鞋子是否已下架，修改onSale状态
					if(map.get("offSaleTime") != null && DateUtils.compareDay(new Date(), (Date)map.get("offSaleTime")) > 0) {
						map.put("onSale", 0);
					}
					
					//判断鞋子展示哪种价格
					*//*if(loginUser.getSaleType() == Constant.UserSaleType.OEM.getValue()) {
						map.put("purchasePrice", map.get("oemPrice"));
					} else if(loginUser.getSaleType() == Constant.UserSaleType.WHOLESALER.getValue()) {
						map.put("purchasePrice", map.get("wholesalerPrice"));
					} else if(loginUser.getSaleType() == Constant.UserSaleType.STORE.getValue()) {
						map.put("purchasePrice", map.get("storePrice"));
					} else if(loginUser.getSaleType() == Constant.UserSaleType.FACTORY.getValue()){
						map.put("purchasePrice", map.get("salePrice"));
					}*//*
					map.remove("oemPrice");
					map.remove("wholesalerPrice");
					map.remove("storePrice");
					map.remove("salePrice");
					
					//这里做了循环匹配填入seq和browseTime，实际上两个list顺序应当是一致的（除非有鞋子被删除）
					for (ShoesValuateEntity shoesValuate : shoesValuateList) {
						if (shoesValuate.getShoesSeq().equals((Integer) map.get("shoesSeq"))) {
							map.put("seq", shoesValuate.getSeq());
							map.put("browseTime", shoesValuate.getBrowseTime());
							break;
						}
					}
				}
				
				return R.ok(shoesList);
			} else {
				return R.ok(new ArrayList<Object>());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }*/
    
    
    
    /**
     * 我的收藏
     *//*
    @Login
    @GetMapping("getMyCollected")
    @ApiOperation("我的收藏")
    public R getMyCollected(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("起始条数") @RequestParam("start") Integer start,
    		@ApiParam("总条数") @RequestParam("num") Integer num) {
    	try {
    		//1.用户收藏信息列表
    		List<ShoesValuateEntity> shoesValuateList = shoesValuateService.getCollectedShoesValuateList(loginUser.getSeq(), start, num);
    		
    		//2.根据鞋子序号获取鞋子详细信息
    		if (shoesValuateList != null && shoesValuateList.size() > 0) {
	    		List<Integer> shoesSeqList = new ArrayList<Integer>();
	    		for(ShoesValuateEntity shoesValuate : shoesValuateList) {
	    			shoesSeqList.add(shoesValuate.getShoesSeq());
	    		}
				List<Map<String,Object>> shoesList = shoesValuateService.getShoesBySeqList(shoesSeqList);
				
				for(Map<String, Object> map : shoesList) {
					map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
					
					//按下架时间判断鞋子是否已下架，修改onSale状态
					if(map.get("offSaleTime") != null && DateUtils.compareDay(new Date(), (Date)map.get("offSaleTime")) > 0) {
						map.put("onSale", 0);
					}
					
					//判断鞋子展示哪种价格
					*//*if(loginUser.getSaleType() == Constant.UserSaleType.OEM.getValue()) {
						map.put("purchasePrice", map.get("oemPrice"));
					} else if(loginUser.getSaleType() == Constant.UserSaleType.WHOLESALER.getValue()) {
						map.put("purchasePrice", map.get("wholesalerPrice"));
					} else if(loginUser.getSaleType() == Constant.UserSaleType.STORE.getValue()) {
						map.put("purchasePrice", map.get("storePrice"));
					} else if(loginUser.getSaleType() == Constant.UserSaleType.FACTORY.getValue()){
						map.put("purchasePrice", map.get("salePrice"));
					}*//*
					map.remove("oemPrice");
					map.remove("wholesalerPrice");
					map.remove("storePrice");
					map.remove("salePrice");
					
					//这里做了循环匹配填入seq和browseTime，实际上两个list顺序应当是一致的（除非有鞋子被删除）
					for(ShoesValuateEntity shoesValuate : shoesValuateList) {
						if(shoesValuate.getShoesSeq().equals((Integer)map.get("shoesSeq")) ) {
							map.put("seq", shoesValuate.getSeq());
							map.put("collectedTime", shoesValuate.getCollectedTime());
							break;
						}
					}
				}
				
				return R.ok(shoesList);
			} else {
				return R.ok(new ArrayList<Object>());
			}
    		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    */

    
    /**
     * 批量删除浏览历史记录
     */
    @Login
    @PostMapping("deleteBrowse")
    @ApiOperation("批量删除浏览历史记录")
    public R deleteBrowse(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
    		@ApiParam("浏览记录seq") @RequestParam("seqArr") String seqArr) {
    	try {
    		//删除浏览历史记录
    		@SuppressWarnings("unchecked")
    		List<Integer> seqList = JSONArray.toList(JSONArray.fromObject(seqArr), Integer.class, new JsonConfig());
    		shoesValuateService.deleteBrowseShoesValuate(seqList);
			return R.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
 
    
    
    /**
     * 全部删除浏览历史记录
     */
    @Login
    @PostMapping("deleteAllBrowse")
    @ApiOperation("全部删除浏览历史记录")
    public R deleteAllBrowse(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq) {
    	try {
    		//全部删除浏览历史记录
    		shoesValuateService.deleteAllBrowseShoesValuate(userSeq);
			return R.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    /**
     * 批量删除收藏
     */
    @Login
    @PostMapping("deleteCollected")
    @ApiOperation("批量删除收藏")
    public R deleteCollected(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
    		@ApiParam("收藏记录seq") @RequestParam("seqArr") String seqArr) {
    	try {
    		//删除收藏
    		@SuppressWarnings("unchecked")
    		List<Integer> seqList = JSONArray.toList(JSONArray.fromObject(seqArr), Integer.class, new JsonConfig());
    		shoesValuateService.deleteCollectedShoesValuate(seqList);
			return R.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    /**
     * 全部删除收藏
     */
    @Login
    @PostMapping("deleteAllCollected")
    @ApiOperation("全部删除收藏")
    public R deleteAllCollected(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq) {
    	try {
    		//全部删除浏览历史记录
    		shoesValuateService.deleteAllCollectedShoesValuate(userSeq);
			return R.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    

}
