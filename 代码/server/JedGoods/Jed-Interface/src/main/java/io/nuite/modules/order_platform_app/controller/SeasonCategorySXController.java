package io.nuite.modules.order_platform_app.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.service.ShoesInfoService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;
import io.nuite.modules.sr_base.entity.GoodsSXEntity;
import io.nuite.modules.sr_base.entity.GoodsSXOptionEntity;
import io.nuite.modules.sr_base.entity.GoodsSeasonEntity;
import io.nuite.modules.sr_base.service.GoodsColorService;
import io.nuite.modules.sr_base.service.GoodsSXOptionService;
import io.nuite.modules.sr_base.service.GoodsSXService;
import io.nuite.modules.sr_base.service.GoodsSeasonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * jrd季节、分类、属性获取接口（鞋子筛选条件）
 * @author yy
 * @date 2018-08-16 13:47
 */
@RestController
@RequestMapping("/order/app/seasonCategorySx")
@Api(tags = " jrd季节、分类、属性获取接口（鞋子筛选条件）")
public class SeasonCategorySXController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private GoodsSeasonService goodsSeasonService;
    
    @Autowired
    private ShoesInfoService shoesInfoService;
    
    @Autowired
    private GoodsSXService goodsSXService;
    
    @Autowired
    private GoodsSXOptionService goodsSXOptionService;
    
    @Autowired
    private GoodsColorService goodsColorService;
    
    
    
	/**
     * 获取所有季节
     */
    @Login
    @GetMapping("seasonList")
    @ApiOperation("获取所有季节")
    public R seasonList(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
    	try {
    		List<GoodsSeasonEntity> goodsSeasonList = goodsSeasonService.getAllSeasonList(loginUser.getBrandSeq());
    		return R.ok(goodsSeasonList);
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }

    
    /**
     * 鞋子所有分类
     */
    @Login
    @GetMapping("shoesCategory")
    @ApiOperation("鞋子所有分类")
    public R getShoesCategory(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
    	try {
    		//根据 工厂方的公司编号 查询 鞋子分类列表
    		List<Map<String, Object>> list0 = shoesInfoService.getShoesCategory(loginUser.getCompanySeq(), 0);
    		for(Map<String, Object> map0 : list0) {
    			List<Map<String, Object>> list1 = shoesInfoService.getShoesCategory(loginUser.getCompanySeq(), (int)map0.get("seq"));
    			for(Map<String, Object> map1 : list1) {
    				List<Map<String, Object>> list2 = shoesInfoService.getShoesCategory(loginUser.getCompanySeq(), (int)map1.get("seq"));
        			for(Map<String, Object> map2 : list2) {
        				List<Map<String, Object>> list3 = shoesInfoService.getShoesCategory(loginUser.getCompanySeq(), (int)map2.get("seq"));
            			for(Map<String, Object> map3 : list3) {
            				List<Map<String, Object>> list4 = shoesInfoService.getShoesCategory(loginUser.getCompanySeq(), (int)map3.get("seq"));
            				map3.put("child", list4);
            			}
        				map2.put("child", list3);
        			}
    				map1.put("child", list2);
    			}
    			map0.put("child", list1);
    		}
			return R.ok(list0);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    
    
    /**
     * 鞋子所有属性及可选值
     */
    @Login
    @GetMapping("shoesSXAndOptions")
    @ApiOperation("鞋子所有属性")
    public R shoesSXAndOptions(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
    	try {
    		
    		List<GoodsSXEntity> goodsSXList = goodsSXService.getGoodsSXListByCompanySeq(loginUser.getCompanySeq());
    		
    		for(GoodsSXEntity goodsSXEntity : goodsSXList) {
    			
    			List<GoodsSXOptionEntity>  goodsSXOptionList = goodsSXOptionService.getGoodsSXOptionListBySXSeq(goodsSXEntity.getSeq());
    			goodsSXEntity.setOptionsList(goodsSXOptionList);
    			
    		}
    		
    		return R.ok(goodsSXList);
    		
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
    
    
	/**
     * 获取所有颜色
     */
    @Login
    @GetMapping("colorList")
    @ApiOperation("获取所有颜色")
    public R colorList(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
    	try {
    		List<GoodsColorEntity> goodsColorList = goodsColorService.getColorListByCompanySeq(loginUser.getCompanySeq());
    		return R.ok(goodsColorList);
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }
    
    
    
}
