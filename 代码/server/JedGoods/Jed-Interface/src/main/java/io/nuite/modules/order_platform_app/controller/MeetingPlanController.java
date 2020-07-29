package io.nuite.modules.order_platform_app.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.dao.MeetingPlanDao;
import io.nuite.modules.order_platform_app.service.MeetingPlanService;
import io.nuite.modules.sr_base.dao.*;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 订货计划接口
 * @author yy
 * @date 2018-08-16 13:47
 */
@RestController
@RequestMapping("/order/app/meetingPlan")
@Api(tags = "订货平台 - 订货计划接口", description = "波次列表 + 设置提醒 + 订货计划柱状图")
public class MeetingPlanController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private MeetingPlanService meetingPlanService;
    
    @Autowired
    private MeetingPlanDao meetingPlanDao;
    
    @Autowired
    private GoodsCategoryDao goodsCategoryDao;
    
    @Autowired
    private GoodsSXDao goodsSXDao;
    
    @Autowired
    private GoodsSXOptionDao goodsSXOptionDao;
    
    @Autowired
    private GoodsShoesDao goodsShoesDao;
    
    @Autowired
    private GoodsPeriodDao goodsPeriodDao;
    
    
    
	/**
     * 波次列表
     */
    @Login
    @GetMapping("periodList")
    @ApiOperation("波次列表")
    public R periodList(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
    	try {
    		List<GoodsPeriodEntity> goodsPeriodList = meetingPlanService.getAllPeriodList(loginUser.getBrandSeq());
    		return R.ok(goodsPeriodList);
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }
    
    
    
	/**
     * 设置提醒
     */
    @Login
    @PostMapping("setRemind")
    @ApiOperation("设置提醒")
    public R setRemind(@ApiIgnore @RequestAttribute("userSeq") Integer userSeq,
    					@ApiParam("扫码提醒（0:不提醒,1:提醒）") @RequestParam("scanRemind") Integer scanRemind,
    					@ApiParam("下单提醒（0:不提醒,1:提醒）") @RequestParam("orderRemind") Integer orderRemind) {
    	try {
    		meetingPlanService.updateMeetingRemind(userSeq, scanRemind, orderRemind);
    		return R.ok();
    	} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}

    }
    
    

    
    
    
}
