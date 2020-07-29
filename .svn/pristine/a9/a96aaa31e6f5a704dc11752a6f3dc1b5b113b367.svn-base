package io.nuite.modules.order_platform_app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.modules.order_platform_app.entity.ShoesInfoEntity;
import io.nuite.modules.order_platform_app.entity.ShoppingCartEntity;
import io.nuite.modules.order_platform_app.service.OrderService;
import io.nuite.modules.order_platform_app.service.ShoppingCartService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import io.nuite.common.utils.Constant;
import io.nuite.common.utils.DateUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 购物车接口
 * @author yy
 * @date 2018-04-16 13:47
 */
@RestController
@RequestMapping("/order/app/shoppingCart")
@Api(tags = "订货平台 - 购物车", description = "颜色尺码库存量 + 加入购物车 + 购物车列表 + 修改购物车")
public class ShoppingCartController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ShoppingCartService shoppingCartService;
    
	@Autowired
	private OrderService orderService;


    
	/**
     * 根据鞋子序号，获取每种颜色的尺码、库存
     */
    @Login
    @GetMapping("getShoesSizeStock")
    @ApiOperation("根据鞋子序号，获取每种颜色的尺码、库存")
    public R getShoesSizeStock(@ApiParam("鞋子Seq") @RequestParam("shoesSeq") Integer shoesSeq) {
    	try {
    		//1.查询鞋子有哪些颜色
    		List<Object> colorSeqList = shoppingCartService.getColorSeqListByShoesSeq(shoesSeq);
    		
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			if(colorSeqList != null && colorSeqList.size() > 0) {
				for(Object colorSeq : colorSeqList) {
					Map<String,Object> map = new HashMap<String,Object>();
					
					//2.根据颜色seq获取颜色信息
					GoodsColorEntity goodsColorEntity = shoppingCartService.getColorBySeq((Integer)colorSeq);
					map.put("colorSeq", colorSeq);
					map.put("colorCode", goodsColorEntity.getCode());
					map.put("colorName", goodsColorEntity.getName());
					
					//3.根据 鞋子Seq + 颜色seq 获取尺码、库存信息
					List<Map<String,Object>> sizeAndStockList = shoppingCartService.getSizeAndStockByShoesSeqAndColorSeq(shoesSeq, (Integer)colorSeq);
					for(Map<String,Object> sizeAndStockMap : sizeAndStockList) {
						//4.根据尺码seq获取尺码信息
						GoodsSizeEntity goodsSizeEntity = shoppingCartService.getSizeBySeq((Integer)sizeAndStockMap.get("sizeSeq"));
						sizeAndStockMap.put("sizeCode", goodsSizeEntity.getSizeCode());
						sizeAndStockMap.put("size", goodsSizeEntity.getSizeName());
					}
					map.put("sizeAndStock", sizeAndStockList);
					
					list.add(map);
				}
			}
			return R.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }
    
    
    
	/**
     * 加入购物车
     */
    @Login
    @GetMapping("addToShoppingCart")
    @ApiOperation("加入购物车")
    public R addToShoppingCart(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("鞋子数据Seq") @RequestParam("shoesDataSeq") Integer shoesDataSeq,
    		@ApiParam("数量") @RequestParam("buyCount") Integer buyCount,
    		@ApiParam("总价格") @RequestParam("totalPrice") BigDecimal totalPrice) {
    	try {
    		//数据校验：
				//获取鞋子信息实体，用于判断下架状态和价格
    		ShoesInfoEntity shoesInfoEntity = orderService.getShoesInfoByShoesDateSeq(shoesDataSeq);
				//判断鞋子是否已下架
			if(shoesInfoEntity.getOnSale() == 0 || (shoesInfoEntity.getOffSaleTime() != null && DateUtils.compareDay(new Date(), shoesInfoEntity.getOffSaleTime()) > 0) ) {
				return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您选择的商品已下架");
			}
				//鞋子价格
			BigDecimal price = BigDecimal.valueOf(0);
			BigDecimal shoesPrice;
			/*if(loginUser.getSaleType() == Constant.UserSaleType.OEM.getValue()) {
				shoesPrice = shoesInfoEntity.getOemPrice();
				price = shoesPrice.multiply(BigDecimal.valueOf(buyCount));
			} else if(loginUser.getSaleType() == Constant.UserSaleType.WHOLESALER.getValue()) {
				shoesPrice = shoesInfoEntity.getWholesalerPrice();
				price = shoesPrice.multiply(BigDecimal.valueOf(buyCount));
			} else if(loginUser.getSaleType() == Constant.UserSaleType.STORE.getValue()) {
				shoesPrice = shoesInfoEntity.getStorePrice();
				price = shoesPrice.multiply(BigDecimal.valueOf(buyCount));
			}*/
				//判断价格是否和前端传过来的一致
			if(price.compareTo(totalPrice) != 0) {
				return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您选择的商品价格已过期，请刷新后再试");
			}
			
			
    		//查询购物车列表，看是否已经存在此商品
    		ShoppingCartEntity shoppingCartEntity = shoppingCartService.getShoppingCartByUserSeqShoesDataSeq(loginUser.getSeq(), shoesDataSeq);
    		//购物车不存在，新增
    		if(shoppingCartEntity == null) {
				shoppingCartEntity = new ShoppingCartEntity();
				shoppingCartEntity.setUserSeq(loginUser.getSeq());
				shoppingCartEntity.setShoesDataSeq(shoesDataSeq);
				shoppingCartEntity.setBuyCount(buyCount);
				shoppingCartEntity.setTotalPrice(price);
				shoppingCartEntity.setIsChecked(1);
				shoppingCartEntity.setInputTime(new Date());
				shoppingCartEntity.setDel(0);
		        try {
					shoppingCartService.addToShoppingCart(shoppingCartEntity);
					return R.ok("加入成功");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
		    		return R.error(HttpStatus.SC_FORBIDDEN, "加入失败");
				}
    		} else { //已存在则修改
    			shoppingCartEntity.setBuyCount(shoppingCartEntity.getBuyCount() + buyCount);
    			price = shoppingCartEntity.getTotalPrice().add(price);
    			shoppingCartEntity.setTotalPrice(price);
		        try {
		        	shoppingCartService.updateShoppingCart(shoppingCartEntity);
					return R.ok("加入成功");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
		    		return R.error(HttpStatus.SC_FORBIDDEN, "加入失败");
				}
    		}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }
    
    
	/**
     * 购物车列表
     */
    @Login
    @GetMapping("shoppingCartList")
    @ApiOperation("购物车列表")
    public R shoppingCartList(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
    	try {
			List<Map<String,Object>> shoppingCartList = shoppingCartService.getShoppingCartListByUserSeq(loginUser.getSeq());
			for(Map<String, Object> map : shoppingCartList) {
				map.put("img", getGoodsShoesPictureUrl(map.get("goodId").toString()) + map.get("img"));
				
				//按下架时间判断鞋子是否已下架，修改onSale状态
				if(map.get("offSaleTime") != null && DateUtils.compareDay(new Date(), (Date)map.get("offSaleTime")) > 0) {
					map.put("onSale", 0);
				}
				
				//判断鞋子展示哪种价格
				/*if(loginUser.getSaleType() == Constant.UserSaleType.OEM.getValue()) {
					map.put("purchasePrice", map.get("oemPrice"));
				} else if(loginUser.getSaleType() == Constant.UserSaleType.WHOLESALER.getValue()) {
					map.put("purchasePrice", map.get("wholesalerPrice"));
				} else if(loginUser.getSaleType() == Constant.UserSaleType.STORE.getValue()) {
					map.put("purchasePrice", map.get("storePrice"));
				} else if(loginUser.getSaleType() == Constant.UserSaleType.FACTORY.getValue()){
					map.put("purchasePrice", map.get("salePrice"));
				}*/
				map.remove("oemPrice");
				map.remove("wholesalerPrice");
				map.remove("storePrice");
				map.remove("salePrice");
				
			}
			
			/** 20180910新需求，显示类似Excel形式的购物车列表  -- start --**/
			List<Map<String,Object>> excelShoppingCartList = new ArrayList<Map<String,Object>>();
			Set<Integer> shoesSeqSet = new TreeSet<Integer>();
			for(Map<String, Object> shoppingCart : shoppingCartList) {
				shoesSeqSet.add((Integer)shoppingCart.get("shoesSeq"));
			}
			
			//循环每个鞋子seq，获取该鞋子的所有颜色尺码库存数据（调用上面的接口）
			for(Integer shoesSeq : shoesSeqSet) {
				Map<String,Object> map = new HashMap<String,Object>();
				int buyCountTotal = 0;
				List<Integer> shoppingCartSeqList = new ArrayList<Integer>();
				
				//所有颜色尺码库存数据
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> colorSizeStockList = (List<Map<String, Object>>) getShoesSizeStock(shoesSeq).get("result");
				
				//双重循环每个颜色尺码库存，获取shoesDataSeq，并判断是否在购物车存在，存在则将购物车数据加入到尺码库存Map中
				for(Map<String,Object> colorSizeStock : colorSizeStockList) {
					
					//尺码库存List
					@SuppressWarnings("unchecked")
					List<Map<String,Object>> sizeStockList = (List<Map<String, Object>>) colorSizeStock.get("sizeAndStock");
					
					for(Map<String,Object> sizeStock : sizeStockList) {
						//初始化购买量、购物车seq等数据
						sizeStock.put("buyCount", "");
						sizeStock.put("shoppingCartSeq", "");
						
						int shoesDataSeq = (int) sizeStock.get("shoesDataSeq");
						
						//循环购物车，判断是否存在该shoesDataSeq
						for(Map<String, Object> shoppingCart : shoppingCartList) {
							//存在，在sizeStock中加入购物车数据，比计算购买量
							if((Integer)shoppingCart.get("shoesDataSeq") == shoesDataSeq) {
								
								//加入购买量、购物车seq等数据
								sizeStock.put("buyCount", shoppingCart.get("buyCount"));
								sizeStock.put("shoppingCartSeq", shoppingCart.get("seq"));
								
								map.put("goodId", shoppingCart.get("goodId"));
								map.put("goodName", shoppingCart.get("goodName"));
								map.put("img", shoppingCart.get("img"));
								map.put("onSale", shoppingCart.get("onSale"));
								map.put("purchasePrice", shoppingCart.get("purchasePrice"));
								buyCountTotal = buyCountTotal + (Integer)shoppingCart.get("buyCount");
								shoppingCartSeqList.add((Integer)shoppingCart.get("seq"));
								break;
							}
						}
					}
				}
				
				map.put("shoesSeq", shoesSeq);
				map.put("cartArr", colorSizeStockList);
				
				map.put("buyCountTotal", buyCountTotal);
				map.put("shoppingCartSeqArr", shoppingCartSeqList);
				excelShoppingCartList.add(map);
			}
			/** 20180910新需求，显示类似Excel形式的购物车列表  -- end --**/
			
			return R.ok(excelShoppingCartList);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }
    
    
    
    /**
     * 修改购物车
     */
    @Login
    @GetMapping("changeShoppingCart")
    @ApiOperation("修改购物车 （数量、是否选中）")
    public R changeShoppingCart(@ApiParam("购物车Seq") @RequestParam("shoppingCartSeq") Integer shoppingCartSeq,
    		@ApiParam("数量") @RequestParam(value = "buyCount", required = false) Integer buyCount,
    		@ApiParam("总价格") @RequestParam(value = "totalPrice", required = false) BigDecimal totalPrice,
    		@ApiParam("是否选中") @RequestParam("isChecked") Integer isChecked) {
    	try {
			ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
			shoppingCartEntity.setSeq(shoppingCartSeq);
			if(buyCount != null) {
				shoppingCartEntity.setBuyCount(buyCount);
			}
			if(totalPrice != null) {
				shoppingCartEntity.setTotalPrice(totalPrice);
			}
			shoppingCartEntity.setIsChecked(isChecked);
			shoppingCartService.updateShoppingCart(shoppingCartEntity);
    		
			return R.ok("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }

    
    /**
     * 购物车全选/全不选
     */
    @Login
    @GetMapping("checkAllShoppingCart")
    @ApiOperation("购物车全选/全不选")
    public R checkAllShoppingCart(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
    		@ApiParam("是否选中") @RequestParam("isChecked") Integer isChecked) {
    	try {
			ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
			shoppingCartEntity.setIsChecked(isChecked);
			shoppingCartService.checkAllShoppingCart(shoppingCartEntity, userSeq);
    		
			return R.ok("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }
    
    
    
    /**
     * 批量删除购物车
     */
    @Login
    @GetMapping("deleteShoppingCart")
    @ApiOperation("批量删除购物车")
    public R deleteShoppingCart(@ApiParam("购物车Seq数组(带[])") @RequestParam("shoppingCartSeqArr") String shoppingCartSeqArr) {
    	try {
    		@SuppressWarnings("unchecked")
    		List<Integer> seqList = JSONArray.toList(JSONArray.fromObject(shoppingCartSeqArr), Integer.class, new JsonConfig());
			shoppingCartService.deleteShoppingCart(seqList);
			return R.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }
    
    
    
}
