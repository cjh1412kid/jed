package io.nuite.modules.order_platform_app.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import io.nuite.modules.system.entity.ShoesReplenishEntity;
import io.nuite.modules.system.service.ShoesReplenishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("/order/app/shoesreplenish")
@Api(tags = "jed货品管理-导入补货计划")
public class ShoesReplenishsController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ShoesReplenishService shoesReplenishService;
    
    @Autowired
    private GoodsShoesService goodsShoesService;
    
    @Login
    @GetMapping("/replenishList")
    @ApiOperation("补单动态功能")
    public R list(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
    	try {
  
    		List<Map<String, Object>> replenishList=shoesReplenishService.getReplenishList(loginUser.getCompanySeq());
    		return R.ok(replenishList);
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    @Login
    @GetMapping("/newReplenishList")
    @ApiOperation("新补单动态功能")
    public R replenishList(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		 @ApiParam("年份") @RequestParam(value = "yearList", required = false) List<Integer> yearList,
    		 @ApiParam("季节") @RequestParam(value = "seasonSeqList", required = false) List<Integer> seasonSeqList,
    		 @ApiParam("分类") @RequestParam(value = "categorySeqList", required = false) List<Integer> categorySeqList) {
    	try {
    		//根据年份季节品类查询满足条件的goodsSeq
    		List<Map<String, Object>> goodsSeqs=shoesReplenishService.getGoodsSeqs(yearList, seasonSeqList, categorySeqList, loginUser.getCompanySeq());
    		for (Map<String, Object> map : goodsSeqs) {
				Integer shoesSeq=(Integer)map.get("shoesSeq");
				GoodsShoesEntity goodsShoesEntity=goodsShoesService.selectById(shoesSeq);
				//根据shoesSeq 查询补货总量和到货总量
				Map<String, Object> totalNum=shoesReplenishService.getTotalNum(shoesSeq);
				map.put("goodId", goodsShoesEntity.getGoodID());
				map.putAll(totalNum);
			}
    		return R.ok(goodsSeqs);
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
    }
    
    
}
