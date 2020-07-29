package io.nuite.modules.order_platform_app.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 用户管理接口
 * @author yy
 * @date 2018-04-25 13:47
 */
@RestController
@RequestMapping("/order/app/userJurisdiction")
@Api(tags = "订货平台 - 用户管理", description = "已创建的账号 + 创建用户 + 启用、停用账号")
public class UserJurisdictionController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());


	/**
     * 已添加的品牌方账号列表
     */
    @Login
    @GetMapping("createdUserList")
    @ApiOperation("已添加的品牌方账号列表")
    public R getCreatedUserList(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq) {
    	try {
//    		//查询已添加的品牌方账号列表
//    		List<Map<String,Object>> createdUserList = userJurisdictionService.getCreatedUserList(userSeq);
//    		for(Map<String,Object> map : createdUserList) {
//    			map.put("headImg", getBaseUserPictureUrl(map.get("userSeq").toString()) + map.get("headImg"));
//
//        		//添加分公司、代理商名称
//        		/*if((int)map.get("attachType") == Constant.UserAttachType.OFFICE.getValue()) {
//        			BaseAreaEntity baseAreaEntity = userJurisdictionService.getBaseAreaBySeq((int)(long)map.get("attachSeq"));
//        			map.put("attachName", baseAreaEntity.getName());
//        		} else if ((int)map.get("attachType") == Constant.UserAttachType.AGENT.getValue()) {
//        			BaseAgentEntity baseAgentEntity = userJurisdictionService.getBaseAgentBySeq((int)(long)map.get("attachSeq"));
//        			map.put("attachName", baseAgentEntity.getAgentName());
//        		}*/
//    		}

			return R.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }


	/**
     * 获取工厂所有代理
     */
    @Login
    @GetMapping("agentList")
    @ApiOperation("获取工厂所有代理")
    public R getAgentList(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
    	try {
    		//根据品牌编号查询区域列表
//    		List<Map<String,Object>> agentList = userJurisdictionService.getAgentListByBrandSeq(loginUser.getBrandSeq());
			return R.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }

    
    
	/**
     * 获取工厂所有分公司
     */
    @Login
    @GetMapping("shopList")
    @ApiOperation("获取工厂所有门店")
    public R getShopList(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
    	try {
    		/*//根据品牌编号查询区域列表
    		List<Map<String,Object>> areaList = userJurisdictionService.getAreaListByBrandSeq(loginUser.getBrandSeq());
    		List<Map<String,Object>> officeList;
    		List<Map<String,Object>> shopList;
    		for(Map<String,Object> areaMap : areaList) {
    			//根据区域编号查询办事处列表
    			officeList = userJurisdictionService.getOfficeListByAreaSeq((Integer)areaMap.get("seq"));
    			for(Map<String,Object> officeMap : officeList) {
        			//根据办事处编号查询门店列表
        			shopList = userJurisdictionService.getShopListByOfficeSeq((Integer)officeMap.get("seq"));
        			officeMap.put("shopList", shopList);
    			}
    			areaMap.put("officeList", officeList);
    		}*/
			return R.ok();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }
    
    
    
	/**
	 * 新增品牌方账号
	 */
	@Login
	@PostMapping("createUser")
	@ApiOperation("新增品牌方账号")
	public R createUser(@ApiIgnore @LoginUser BaseUserEntity loginUser,
			@ApiParam("账号所属类型(1.工厂，2.分公司，3.代理商)") @RequestParam("attachType") Integer attachType,
			@ApiParam("所属序号(所属类型为2,3时必传)") @RequestParam(value = "attachSeq", required = false) Integer attachSeq,
			@ApiParam("账号销售类型(2.贴牌商，3.批发商，4.直营店)") @RequestParam("saleType") Integer saleType,
			@ApiParam("门店序号(销售类型为4时必传)") @RequestParam(value = "shopSeq", required = false) Integer shopSeq,
			@ApiParam("账号") @RequestParam("accountName") String accountName,
			@ApiParam("密码") @RequestParam("password") String password,
			@ApiParam("姓名") @RequestParam("userName") String userName,
			@ApiParam("有效期(默认3年，可不传)") @RequestParam(value = "effectiveYears", required = false, defaultValue = "3") Integer effectiveYears,
			HttpServletRequest request) {
		try {
			
    		/*if(!isFactoryAdmin(loginUser)) {
    			return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
    		}
    		
			if(attachType != Constant.UserAttachType.FACTORY.getValue() && attachType != Constant.UserAttachType.OFFICE.getValue() && attachType != Constant.UserAttachType.AGENT.getValue()) {
				return R.error(HttpStatus.SC_FORBIDDEN, "参数错误");
			}
			
			if(attachType == Constant.UserAttachType.OFFICE.getValue() && attachSeq == null) {
				return R.error(HttpStatus.SC_FORBIDDEN, "请选择所属分公司");
			}
			
			if(attachType == Constant.UserAttachType.AGENT.getValue() && attachSeq == null) {
				return R.error(HttpStatus.SC_FORBIDDEN, "请选择所属代理商");
			}
			
			if(saleType != Constant.UserSaleType.OEM.getValue() && saleType != Constant.UserSaleType.WHOLESALER.getValue() && saleType != Constant.UserSaleType.STORE.getValue()) {
				return R.error(HttpStatus.SC_FORBIDDEN, "参数错误");
			}
			
			//选择代理商 + 直营店这种组合，就不用选门店了
			if(saleType == Constant.UserSaleType.STORE.getValue() && shopSeq == null && attachType != Constant.UserAttachType.AGENT.getValue() ) {
				return R.error(HttpStatus.SC_FORBIDDEN, "请选择门店");
			}*/
			//TODO 选择分公司 + 直营店这种组合，只能选该分公司下面的门店
			
			
			/*//判断账号是否已存在
			if(userJurisdictionService.accountNameIsExisted(accountName, null)) {
				return R.error(HttpStatus.SC_FORBIDDEN, "账号已存在，请重新输入");
			}
			
    		//查询创建人权限，看能否创建账号
    		UserJurisdictionEntity creatorUserJurisdiction = userJurisdictionService.getUserJurisdictionByUserSeq(loginUser.getSeq());
			if (creatorUserJurisdiction.getIntOfCreateUsers() - creatorUserJurisdiction.getAlreadyCreateUsers() <= 0) {
				return R.error(HttpStatus.SC_FORBIDDEN, "可创建账号数量达到上限，请购买名额");
			}
			
			try {
				userJurisdictionService.createUser(loginUser, attachType, attachSeq, saleType, shopSeq, accountName, password, userName, effectiveYears, creatorUserJurisdiction);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				return R.error("创建用户失败");
			}*/
			return R.ok("创建用户成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

	}
    
	
	
	/**
	 * 修改品牌方账号
	 */
	@Login
	@PostMapping("updateUser")
	@ApiOperation("修改品牌方账号")
	@Deprecated
	public R updateUser(@ApiIgnore @LoginUser BaseUserEntity loginUser,
			@ApiParam("要修改的用户的Seq") @RequestParam("userSeq") Integer userSeq,
			@ApiParam("公司类型") @RequestParam("companyType") Integer companyType,
			@ApiParam("公司名称") @RequestParam("companyName") String companyName,
			@ApiParam("门店序号(如果公司类型为4,需要门店序号)") @RequestParam(value = "shopSeq", required = false) Integer shopSeq,
			@ApiParam("账号") @RequestParam("accountName") String accountName,
			@ApiParam("密码") @RequestParam("password") String password,
			HttpServletRequest request) {
		try {
			if(companyType == 4 && shopSeq == null) {
				return R.error(HttpStatus.SC_FORBIDDEN, "请选择门店");
			}
			
			/*//判断账号是否已存在
			if(userJurisdictionService.accountNameIsExisted(accountName, userSeq)) {
				return R.error(HttpStatus.SC_FORBIDDEN, "账号已存在，请重新输入");
			}
			
			//判断公司名是否已被该厂家创建过
			//查询已添加的品牌方账号列表
			List<Map<String,Object>> createdUserList = userJurisdictionService.getCreatedUserList(loginUser.getSeq());
			if(companyType == 4) {
	    		for(Map<String,Object> map : createdUserList) {
	    			if(map.get("shopSeq") != null && ((Integer)map.get("shopSeq")).equals(shopSeq) && !((Integer)map.get("userSeq")).equals(userSeq)) {
						return R.error(HttpStatus.SC_FORBIDDEN, "您已创建过该门店的账号:" + map.get("accountName"));
	    			}
	    		}
			} else {
	    		for(Map<String,Object> map : createdUserList) {
					if((Integer)map.get("companyTypeSeq") != 4 && map.get("companyName").toString().equals(companyName) && !((Integer)map.get("userSeq")).equals(userSeq)) {
						return R.error(HttpStatus.SC_FORBIDDEN, "您已创建过该公司账号:" + map.get("accountName"));
					}
	    		}
			}
		
			try {
				userJurisdictionService.updateUser(loginUser, userSeq, companyType, companyName, shopSeq, accountName, password);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				return R.error("修改失败");
			}*/
			return R.ok("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

	}
	
	
	/**
	 * 启用、停用品牌方账号
	 */
	@Login
	@PostMapping("disableUser")
	@ApiOperation("启用、停用品牌方账号")
	public R disableUser(@ApiIgnore @LoginUser BaseUserEntity loginUser,
			@ApiParam("要禁用、启用的用户的Seq") @RequestParam("userSeq") Integer userSeq,
			@ApiParam("是否停用 0可用 1停用") @RequestParam("isDisable") Integer isDisable,
			HttpServletRequest request) {
		try {
			
    		/*if(!isFactoryAdmin(loginUser)) {
    			return R.error(HttpStatus.SC_FORBIDDEN, "对不起，您的权限不足");
    		}*/
    		
			/*try {
				userJurisdictionService.disableUser(userSeq, isDisable);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				return R.error("设置失败");
			}*/
			return R.ok("设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

	}
    
    

}
