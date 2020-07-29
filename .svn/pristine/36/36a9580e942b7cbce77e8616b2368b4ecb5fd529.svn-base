package io.nuite.modules.erp.controller;


import com.alibaba.fastjson.JSONObject;
import io.nuite.common.utils.R;
import io.nuite.modules.erp.service.*;
import io.nuite.modules.erp.service.impl.ErpGoodsSyncServiceTransactionAddition;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 伯俊ERP对接测试
 */
@RestController
@RequestMapping("/order/app/erp/bojun")
public class BoJunErpSyncController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ErpSeasonSyncService erpSeasonSyncService;
    
    @Autowired
    private ErpGoodsSyncService erpGoodsSyncService;
	
    @Autowired
    private ErpColorService erpColorService;
    
    @Autowired
    private ErpShoesDataSyncService erpShoesDataSyncService;
    
    @Autowired
    private ErpShopService erpShopService;

    @Autowired
	private ErpGoodsSyncServiceTransactionAddition erpGoodsSyncServiceTransactionAddition;
    
    
	/**
	 * testHttp
	 */
    @Login
	@PostMapping("testHttp")
	public R testHttp() {
		
//		 combine:"and",
//		 expr1:{
//        column:"QTYCAN",
//        condition:" > 10"
//		 },
//		 expr2:{
//	        column:"QTYCAN",
//	        condition:" < 20"
//		  }
		Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
		sqlWhereMap.put("combine", "and");
		Map<String, Object> expr1 = new HashMap<String, Object>();
		expr1.put("column", "QTYCAN");
		expr1.put("condition", "> 10");
		sqlWhereMap.put("expr1", expr1);
		Map<String, Object> expr2 = new HashMap<String, Object>();
		expr2.put("column", "QTYCAN");
		expr2.put("condition", "< 20");
		sqlWhereMap.put("expr2", expr2);
		JSONObject aa = BoJunErpPortalUtil.querySql("V_FA_V_STORAGE1", "M_PRODUCT_ID;NAME,M_PRODUCTALIAS_ID;NO,QTY,PRICELIST", sqlWhereMap, "QTY DESC", 0, 10);
		return R.ok(aa);
	}
	
	
	
	
	/**
	 * 货品同步
	 */
    @Login
	@PostMapping("goodsSync")
	public R goodsSync(Integer goodsType) {
		try {
			
			//步骤1：同步季节
			boolean seasonFlag = erpSeasonSyncService.seasonSync();
			if(!seasonFlag) {
				return R.error("失败，同步季节失败");
			}
			
			//步骤2：同步颜色
			boolean colorFlag = erpColorService.getErpColor();
            if(!colorFlag) {
            	return R.error("失败，同步颜色失败");
            }
			
			//步骤3：同步货品，同时同步出层级结构的分类  + 鞋子价格信息
			boolean goodsFlag = erpGoodsSyncService.goodsSync(goodsType);
            if(!goodsFlag) {
            	return R.error("失败，同步货品失败");
            }
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("失败");
		}
		return R.ok("成功");
	}
	
	
    
    
	/**
	 * 库存同步
	 */
    @Login
	@PostMapping("shoesDataSync")
	public R shoesDataSync(Integer goodsType) {
		try {
			
			//步骤1：同步门店
			boolean shopFlag = erpShopService.getErpShop();
			if(!shopFlag) {
				return R.error("失败，同步门店失败");
			}
			
			//步骤2：同步颜色
			boolean colorFlag = erpColorService.getErpColor();
            if(!colorFlag) {
            	return R.error("失败，同步颜色失败");
            }
			
			//步骤3：同步货品
			boolean goodsFlag = erpGoodsSyncService.goodsSync(goodsType);
            if(!goodsFlag) {
            	return R.error("失败，同步货品失败");
            }
            
			//步骤4：同步库存
			boolean shoesDataFlag = erpShoesDataSyncService.shoesDataSync(goodsType);
            if(!shoesDataFlag) {
            	return R.error("失败，同步库存失败");
            }
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("失败");
		}
		return R.ok("成功");
	}

	@GetMapping("/uploadImg")
	public R uploadImg(HttpServletRequest request) {
    	try {
    		String dirPath = getGoodsUploadUrl(request,"inputhere");
			erpGoodsSyncServiceTransactionAddition.upLoadImg("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559557455887&di=ebe313e50191c6dc34109eb6c655cbce&imgtype=0&src=http%3A%2F%2Fs13.sinaimg.cn%2Forignal%2F4618a628gb42d45594f8c",dirPath);
    		return R.ok();
		}catch (Exception e) {
    		e.printStackTrace();
		}
		return R.error();
	}

	
	
}
