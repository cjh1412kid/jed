package io.nuite.modules.system.service;

import io.nuite.modules.system.entity.echart.EchartDataVo;
import io.nuite.modules.system.entity.echart.OrderDataVo;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/16 18:45
 * @Version: 1.0
 * @Description:
 */
public interface ChartService {

    /**
     * 获取订单最近7天的每日数量
     *
     * @return
     */
    List<OrderDataVo> getOrderDataByDate();

    /**
     * 查询当前一周的某款鞋子的订单数据
     *
     * @param shoeSeq
     * @return
     */
    List<OrderDataVo> getSalesWithWeek(Integer shoeSeq, Integer companySeq);

    /**
     * @param dateType  日期类型  1 年，2 月，3 周，0 自定义
     * @param changeVal 上下移动（周、月、年）
     * @return
     */
    EchartDataVo getOrdersCountByDateAndCompanySeq(Integer dateType, Integer changeVal, Integer companySeq);

    /**
     * 自定义查询时间
     * @param companySeq
     * @param startTime
     * @param endTime
     * @return
     */
    EchartDataVo getOrdersCountByDateAndCompanySeq(Integer companySeq, String startTime, String endTime);

}
