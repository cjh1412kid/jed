package io.nuite.modules.system.controller;

import io.nuite.common.exception.RRException;
import io.nuite.common.utils.R;
import io.nuite.modules.system.entity.echart.EchartDataVo;
import io.nuite.modules.system.entity.echart.OrderDataVo;
import io.nuite.modules.system.service.ChartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/16 18:40
 * @Version: 1.0
 * @Description:
 */

@RestController
@RequestMapping("/chart")
public class ChartController extends AbstractController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ChartService chartService;

    @RequestMapping("getOrderData")
    public R getOrderDataByDate(Integer dateType, @RequestParam(required = false, defaultValue = "0") Integer changeVal, @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {

//        log.info("post参数： {} {},{},{}", dateType, changeVal, startTime, endTime);
        EchartDataVo echartDataVo = null;
        if (dateType == 0) {
            echartDataVo = chartService.getOrdersCountByDateAndCompanySeq(getUser().getCompanySeq(), startTime, endTime);
        } else {
            echartDataVo = chartService.getOrdersCountByDateAndCompanySeq(dateType, changeVal, getUser().getCompanySeq());
        }

        if (echartDataVo == null) {
            throw new RRException("服务器异常");
        }
        return R.ok(echartDataVo);
    }

    @RequestMapping("getSalesDataWithWeek")
    public R getSalesDataWithWeek(Integer shoeSeq) {

        List<OrderDataVo> salesWithWeek = chartService.getSalesWithWeek(shoeSeq, getUser().getCompanySeq());
        if (salesWithWeek == null) {
            return R.error();
        }
        return R.ok(salesWithWeek);
    }

}
