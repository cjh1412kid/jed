package io.nuite.modules.system.controller.order_platform;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.BaseAgentEntity;
import io.nuite.modules.sr_base.entity.BaseAreaEntity;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.service.order_platform.OrderPartyManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订货方管理
 *
 * @author Fl
 */
@RestController
@RequestMapping("/order/orderParty")
@Api(tags = "后台  - 订货方管理", description = "订货方管理")
public class OrderPartyManagementController extends AbstractController {

    @Autowired
    private OrderPartyManagementService OrderPartyManagementService;

    /**
     * 根据当前用户的Seq和订货方类型 返回 订货方的列表
     */
    @PostMapping("/list")
    @ApiOperation("订货方的列表")
    public R orderPartyList(@ApiParam("公司类别序号") @RequestParam("companyType") Integer saleType,
                            @ApiParam("当前页码") @RequestParam("page") Integer page,
                            @ApiParam("每页记录数") @RequestParam("limit") Integer limit) {
        try {
            PageUtils companyBrandPage = OrderPartyManagementService.orderPartyList(getUser(), saleType, page,
                    limit);
            return R.ok().put("page", companyBrandPage);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("获取列表失败");
        }
    }

    /**
     * 根据用户序号删除用户
     */
    @GetMapping("/deleteOrderParty")
    @RequiresPermissions("orderParty:delete")
    public R deleteOrderParty(@ApiParam("用户序号") @RequestParam("seq") Integer seq) {
        try {
            Integer i = OrderPartyManagementService.deleteOrderParty(seq);
            return R.ok(i);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("删除失败");
        }
    }

    /**
     * 根据用户序号获得用户的信息(用于修改)
     */
    @GetMapping("/edit")
    @ApiOperation("根据用户序号获得用户的信息 ")
    public R edit(@RequestParam("seq") Integer seq) {
        try {
            BaseUserEntity baseUserEntity = OrderPartyManagementService.edit(seq);
            return R.ok().put("user", baseUserEntity);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("获得用户的信息失败");
        }
    }

    /**
     * 修改账户
     */
    @PostMapping("/updateOrderParty")
    @RequiresPermissions("orderParty:update")
    public R updateOrderParty(BaseUserEntity baseUserEntity) {
        try {
            if (OrderPartyManagementService.judgeUserOrCompanyExistence(baseUserEntity)) {
                return R.error("该账号已存在");
            }
            if (OrderPartyManagementService.updateOrderParty(baseUserEntity, getAdminUser()) == 1) {
                return R.ok();
            }
            return R.error("修改信息有重复修改失败");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("修改失败");
        }
    }

    /**
     * 添加账户
     */
    @PostMapping("/addOrderParty")
    @RequiresPermissions("orderParty:add")
    public R addOrderParty(BaseUserEntity baseUserEntity) {
        try {
            if (OrderPartyManagementService.judgeUserOrCompanyExistence(baseUserEntity)) {
                return R.error("该账号已存在");
            }
            OrderPartyManagementService.addOrderParty(getAdminUser(), baseUserEntity);
            return R.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
    }

    /**
     * 返回工厂或某一分公司的门店列表
     */
    @GetMapping("/shopList")
    public R shopList(@RequestParam("attachSeq") Integer attachSeq) {
        try {
            List<BaseShopEntity> list = OrderPartyManagementService.shopList(getAdminUser(), attachSeq);
            return R.ok().put("list", list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("获得用户的信息失败");
        }
    }

    /**
     * 返回代理商列表
     */
    @GetMapping("/agentList")
    public R agentList() {
        try {
            List<BaseAgentEntity> agentList = OrderPartyManagementService.agentList(getAdminUser().getBrandSeq());
            return R.ok().put("agentList", agentList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("获取信息失败");
        }
    }

    /**
     * 返回分公司列表
     */
    @GetMapping("/branchOfficeList")
    public R branchOfficeList() {
        try {
            List<BaseAreaEntity> branchOfficeList = OrderPartyManagementService.branchOfficeList(getAdminUser().getBrandSeq());
            return R.ok().put("branchOfficeList", branchOfficeList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("获取信息失败");
        }
    }
}
