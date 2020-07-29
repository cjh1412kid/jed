package io.nuite.modules.order_platform_app.controller;

import io.nuite.common.utils.Constant;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.entity.SalesEntity;
import io.nuite.modules.order_platform_app.service.SalesService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * App门店导购接口
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-19
 */
@RestController
@RequestMapping("/order/app/sales")
@Api(tags = "jrd - 导购管理接口", description = "导购的增删改查")
public class SalesController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    SalesService salesService;
    
    @Login
    @PostMapping("add")
    @ApiOperation("新建导购")
    public R add(@ApiIgnore @LoginUser BaseUserEntity loginUser, SalesEntity salesEntity) {
        try {
            salesEntity.setCreator(loginUser.getSeq());
            salesEntity.setShopSeq(loginUser.getShopSeq());
            if(salesService.insertSales(salesEntity)) {
                return R.ok("新增成功");
            }else {
                return R.error("新增失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            if("导购员已存在".equals(e.getMessage())) {
                return R.error(e.getMessage());
            }
        }
        return R.error("新增失败");
    }
    
    @Login
    @GetMapping("del/{seq}")
    @ApiOperation("删除导购")
    public R delete(@ApiParam("导购员seq") @PathVariable("seq") Integer salesSeq) {
        try {
            salesService.deleteById(salesSeq);
            return R.ok("删除成功");
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error("删除失败");
    }
    
    @Login
    @PostMapping("update")
    @ApiOperation("更新导购")
    public R update(@ApiIgnore @LoginUser BaseUserEntity loginUser, SalesEntity salesEntity) {
        try {
            salesEntity.setInputTime(new Date());
            salesService.updateById(salesEntity);
            return R.ok("更新成功");
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error("更新失败");
    }
    
    @Login
    @GetMapping("list")
    @ApiOperation("查询导购")
    @ApiImplicitParams(@ApiImplicitParam(name = "shopSeq",value = "门店序号",paramType = "query"))
    public R list(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                  Integer shopSeq) {
        
        List list = null;
        
        if (Constant.Role.FACTORY_USER.getCode().equals(loginUser.getRoleCode()) || Constant.Role.FACTORY_ADMIN.getCode().equals(loginUser.getRoleCode())) {
            list = salesService.querySalesByCompanySeq(loginUser.getCompanySeq(),shopSeq);
        } else if (Constant.Role.SHOP_ADMIN.getCode().equals(loginUser.getRoleCode())) {
            list = salesService.querySalesByShopSeq(loginUser.getShopSeq());
        }
        
        return R.ok().result(list);
    }
}

