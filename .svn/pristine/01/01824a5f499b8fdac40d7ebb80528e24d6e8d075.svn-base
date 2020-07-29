package io.nuite.modules.order_platform_app.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.entity.TargetShopEntity;
import io.nuite.modules.order_platform_app.service.TargetSalesService;
import io.nuite.modules.order_platform_app.service.TargetShopService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @description: 门店指标Controller类
 * @author: jxj
 * @create: 2019-03-21 10:52
 */
@RestController
@RequestMapping("/order/app/targetShop")
@Api(tags = "jed门店指标")
public class TargetShopController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TargetShopService targetShopService;
    @Autowired
    private TargetSalesService targetSalesService;

    @Login
    @ApiOperation("新增/修改门店指标")
    @PostMapping("/insertOrUpdateTarget")
    public R insertTarget(@ApiIgnore @LoginUser BaseUserEntity baseUserEntity,
                          @ApiParam(name = "list",value = "门店指标列表",required = true) @RequestParam String list) {
        try {
            if(baseUserEntity.getShopSeq() == null) {
                if(targetShopService.insertOrUpdateTarget(list,baseUserEntity)) {
                    return R.ok("操作成功");
                }
            }else {
                if(targetSalesService.insertOrUpdateTarget(list,baseUserEntity)) {
                    return R.ok("操作成功");
                }
            }
        }catch (Exception e) {
            if("导购标准总指标大于门店标准指标".equals(e.getMessage())) {
            	return   R.error("导购标准总指标大于门店标准指标");
            }
            if("导购中级总指标大于门店中级指标".equals(e.getMessage())) {
            	return  R.error("导购中级总指标大于门店中级指标");
            }
            if("导购高级总指标大于门店高级指标".equals(e.getMessage())) {
            	return  R.error("导购高级总指标大于门店高级指标");
            }
            if("未添加任何相关指标".equals(e.getMessage())) {
            	return  R.error("未添加任何相关指标");
            }
            logger.error(e.getMessage(),e);
        }
        return R.error("操作失败");
    }

    @Login
    @ApiOperation("获取总指标")
    @GetMapping("/selectTotalTarget")
    @ApiImplicitParams({@ApiImplicitParam(name = "targetYear",value = "指标年份",required = true,paramType = "query")})
    public R selectTotalTarget(@ApiIgnore @LoginUser BaseUserEntity baseUserEntity,
                               @RequestParam("targetYear") Integer targetYear) {
        try {
            return R.ok().result(targetShopService.selectTotalTarget(targetYear,baseUserEntity));
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取单个月指标")
    @GetMapping("/selectMonthTarget")
    @ApiImplicitParams({@ApiImplicitParam(name = "targetYear",value = "指标年份",required = true,paramType = "query"),
                        @ApiImplicitParam(name = "targetMonth",value = "指标月份",required = true,paramType = "query")})
    public R selectMonthTarget(@ApiIgnore @LoginUser BaseUserEntity baseUserEntity,
                               @RequestParam("targetYear") Integer targetYear,
                               @RequestParam("targetMonth") Integer targetMonth) {
        try {
            return R.ok().result(targetShopService.selectMonthTarget(targetYear,targetMonth,baseUserEntity));
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error();
    }
    
    
    @Login
    @ApiOperation("根据指标类型获取指标列表")
    @GetMapping("/selectTargetList")
    @ApiImplicitParams({@ApiImplicitParam(name = "targetYear",value = "指标年份",required = true,paramType = "query"),
                        @ApiImplicitParam(name = "targetMonth",value = "指标月份",required = false,paramType = "query"),
    	@ApiImplicitParam(name="type",value = "指标类型(1.基准指标、2.中级指标、3.高级指标)",required =true,paramType = "query")})
    public R selectTargetList(@ApiIgnore @LoginUser BaseUserEntity baseUserEntity,
                               @RequestParam("targetYear") Integer targetYear,
                               @RequestParam(value = "targetMonth",required = false) Integer targetMonth,
                               @RequestParam("type")Integer type) {
        try {
            return R.ok().result(targetShopService.selectTargetList(targetYear,targetMonth,baseUserEntity, type));
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取总销售指标分析")
    @GetMapping("/selectTotalTargetAnalyze")
    @ApiImplicitParams({@ApiImplicitParam(name = "targetYear",value = "指标年份",required = true,paramType = "query"),
                        @ApiImplicitParam(name = "targetMonth",value = "指标月份",paramType = "query")})
    public R selectTotalTargetAnalyze(@ApiIgnore @LoginUser BaseUserEntity baseUserEntity,
                                      @RequestParam("targetYear") Integer targetYear,
                                      Integer targetMonth) {
        try {
            return R.ok().result(targetShopService.selectTotalTargetAnalyze(targetYear,targetMonth,baseUserEntity));
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error();
    }
    
    @Login
    @ApiOperation("获取门店销售指标")
    @GetMapping("/selectSalesTargetAnalyze")
    @ApiImplicitParams({@ApiImplicitParam(name = "targetYear",value = "指标年份",required = true,paramType = "query"),
        				@ApiImplicitParam(name = "targetMonth",value = "指标月份",paramType = "query"),
       					@ApiImplicitParam(name ="shopSeq",value = "门店序号",paramType = "query")})
    public R selectSalesTargetAnalyze(@ApiIgnore @LoginUser BaseUserEntity baseUserEntity,
                                      @RequestParam("targetYear") Integer targetYear,
                                      Integer targetMonth,
                                      Integer shopSeq) {
    	
		try {
			return R.ok().result(targetShopService.selectSalesTargetAnalyze(targetYear, targetMonth, shopSeq, baseUserEntity));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return R.error();
    	
    }
}
