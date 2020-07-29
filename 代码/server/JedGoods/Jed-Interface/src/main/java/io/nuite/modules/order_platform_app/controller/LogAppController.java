package io.nuite.modules.order_platform_app.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.system.entity.order_platform.SystemLogEntity;
import io.nuite.modules.system.service.SystemLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/29 18:47
 * @Version: 1.0
 * @Description:
 */

@RestController
@RequestMapping("/order/app/log")
@Api(tags = "app-系统升级日志", description = "")
public class LogAppController {

    @Autowired
    SystemLogService logService;

    @Login
    @RequestMapping("list")
    @ApiOperation("系统升级日志")
    public R getLogList(@ApiIgnore @LoginUser BaseUserEntity loginUser, @ApiParam("起始条数") @RequestParam("start") Integer start,
                        @ApiParam("总条数") @RequestParam("num") Integer num) {

        List<SystemLogEntity> logList = logService.getLogList(start, num);

        return R.ok(logList);
    }
}
