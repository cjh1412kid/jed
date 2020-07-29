package io.nuite.modules.system.controller.order_platform;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.entity.SievePlateDistributeEntity;
import io.nuite.modules.order_platform_app.service.SievePlateDistributeService;
import io.nuite.modules.system.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: yangchuang
 * @Date: 2019/3/27 13:57
 * @Version: 1.0
 */

@RestController
@Api(tags = "后台 - 订货会初步订单")
@RequestMapping("/system/sievePlateDistribute")
public class SievePlateDistributeController extends AbstractController {
    
    @Autowired
    SievePlateDistributeService sievePlateDistributeService;
    
    @ApiOperation("按门店汇总")
    @GetMapping("list")
    public R list(
        @ApiParam("当前页") @RequestParam Integer page,
        @ApiParam("每页数量") @RequestParam Integer limit,
        @ApiParam("年份") @RequestParam(required = false) Integer year,
        @ApiParam("季节") @RequestParam(required = false) Integer seasonSeq,
        @ApiParam("门店") @RequestParam Integer shopSeq
    ) {
    
        Page<Object> pageCondition = new Page<>(page, limit);
        Page pageRes = sievePlateDistributeService.listPageByCondition(pageCondition, getUser().getCompanySeq(), shopSeq, year, seasonSeq);
        return R.ok().put("page", new PageUtils(pageRes));
    }
    
    @ApiOperation("按货号汇总")
    @GetMapping("list2")
    public R listByGoodID(
        @ApiParam("当前页") @RequestParam Integer page,
        @ApiParam("每页数量") @RequestParam Integer limit,
        @ApiParam("年份") @RequestParam(required = false) Integer year,
        @ApiParam("季节") @RequestParam(required = false) Integer seasonSeq
    ) {
        Page<Object> pageCondition = new Page<>(page, limit);
        
        Page pageRes = sievePlateDistributeService.listPageByGoodID(pageCondition, getUser().getCompanySeq(), year, seasonSeq);
        return R.ok().put("page", new PageUtils(pageRes));
    }
    
    @ApiOperation("获取当前用户公司订单中存在的门店")
    @GetMapping("shops")
    public R queryShops() {
        List<Map<String, Object>> list = sievePlateDistributeService.queryShopsByCompanySeq(getUser().getCompanySeq());
        return R.ok().result(list);
    }
    
    @ApiOperation("获取当前用户公司订单中存在的年份")
    @GetMapping("years")
    public R queryYears() {
        List<Integer> list = sievePlateDistributeService.queryYearsByCompanySeq(getUser().getCompanySeq());
        return R.ok().result(list);
    }
    
    @ApiOperation("获取当前用户公司订单中存在的季节")
    @GetMapping("seasons")
    public R querySeasons() {
        List<Map<String, Object>> list = sievePlateDistributeService.querySeaSonsByCompanySeq(getUser().getCompanySeq());
        return R.ok().result(list);
    }
    
    @ApiOperation(value = "获取当前用户公司订单中最近的一条订单记录", notes = "用于获取最近的年份和季节初始化页面")
    @GetMapping("recent/one")
    public R queryRecentOne() {
        Wrapper wrapper = new EntityWrapper<SievePlateDistributeEntity>();
        wrapper.setSqlSelect("Season_Seq,Year");
        wrapper.eq("Company_Seq", getUser().getCompanySeq());
        wrapper.orderBy("InputTime", false);
        
        SievePlateDistributeEntity one = sievePlateDistributeService.selectOne(wrapper);
        
        return R.ok().result(one);
    }
    
}
