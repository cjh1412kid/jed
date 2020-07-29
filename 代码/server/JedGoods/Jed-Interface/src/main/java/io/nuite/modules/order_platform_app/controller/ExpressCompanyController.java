package io.nuite.modules.order_platform_app.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.entity.ExpressCompanyEntity;
import io.nuite.modules.order_platform_app.service.ExpressCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description: 快递公司Controller
 * @author: jxj
 * @create: 2019-11-22 14:41
 */
@RestController
@RequestMapping("/order/app/expressCompanyController")
@Api(tags = "jed - 快递公司")
public class ExpressCompanyController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ExpressCompanyService expressCompanyService;

    @Login
    @GetMapping("/selectExpressCompanyList")
    @ApiOperation("快递公司列表(不分页)")
    public R selectExpressCompanyList() {
        try {
            Wrapper<ExpressCompanyEntity> wrapper = new EntityWrapper<>();
            wrapper.setSqlSelect("Seq,Name");
            return R.ok(expressCompanyService.selectList(wrapper));
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error();
    }
}
