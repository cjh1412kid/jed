package io.nuite.modules.order_platform_app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.common.utils.Constant;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.service.ShoesInfoService;
import io.nuite.modules.order_platform_app.service.ShopMainpushService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * jrd主推款设置接口
 * @author yy
 * @date 2019-03-14 13:47
 */
@RestController
@RequestMapping("/order/app/mainpush")
@Api(tags = "jrd主推款设置接口")
public class MainpushController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    
	@Autowired
	private ShoesInfoService shoesInfoService;
	
	
	@Autowired
	private ShopMainpushService shopMainpushService;
	
	
	
	/**
	 * 设置、取消鞋子主推
	 */
	@Login
	@PostMapping("setMainpush")
	@ApiOperation("设置、取消鞋子主推")
	public R setMainpush(@ApiIgnore @LoginUser BaseUserEntity loginUser,
			@ApiParam("鞋子seq") @RequestParam("shoesSeq") Integer shoesSeq,
			@ApiParam("是否主推(0:取消主推, 1:主推)") @RequestParam(value = "isMainpush", required = false) Integer isMainpush) {
		try {
			
			if(loginUser.getRoleCode().equals(Constant.Role.FACTORY_ADMIN.getCode()) 
					|| loginUser.getRoleCode().equals(Constant.Role.FACTORY_USER.getCode())) { // 公司主推
				shoesInfoService.setHqMainpush(shoesSeq, isMainpush);
			} else if(loginUser.getRoleCode().equals(Constant.Role.SHOP_ADMIN.getCode()) && loginUser.getShopSeq() != null) {  // 门店主推
				shopMainpushService.setShopMainpush(loginUser.getShopSeq(), shoesSeq, isMainpush);
			} else {
				return R.ok("参数有误");
			}
			return R.ok("设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

	}


    
    
    

}
