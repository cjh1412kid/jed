package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.OrderEntity;
import io.nuite.modules.system.entity.echart.OrderDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 0
 *
 * @author admin
 * @email
 * @date 2018-04-11 11:29:57
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    /**
     * 订单汇总
     *
     * @param num
     * @param
     */
    List<Map<String, Object>> getOrderSummary(@Param("orderSeq") String orderSeq,
                                              @Param("start") Integer start,
                                              @Param("num") Integer num);

    /**
     * 汇总中一个鞋子数据序号，每个订单的购买量
     */
    List<Map<String, Object>> getSummaryDetailList(@Param("orderSeq") String orderSeq, @Param("shoesDataSeq") Integer shoesDataSeq);

    /**
     * 根据公司编号获取订单列表（厂家）
     */
    List<OrderEntity> getOrderListByCompanySeq(@Param("companySeq") Integer companySeq,
                                               @Param("orderStatus") Integer orderStatus,
                                               @Param("start") Integer start,
                                               @Param("num") Integer num);

    /**
     * 根据用户编号获取订单列表（品牌方）
     */
    List<OrderEntity> getOrderListByUserSeq(@Param("userSeq") Integer userSeq,
                                            @Param("orderStatus") Integer orderStatus,
                                            @Param("start") Integer start,
                                            @Param("num") Integer num);

    /**
     * 根据seq获取订单购买商品列表
     */
    List<Map<String, Object>> getOrderProductsList(Integer orderSeq);

    /**
     * 修改订单已付金额
     *
     * @param orderSeq
     * @param paid
     */
    void updateOrderPaid(@Param("orderSeq") Integer orderSeq, @Param("paid") BigDecimal paid);

    List<OrderDataVo> getOrderDataByDate();

    List<OrderEntity> getOrdersByDateAndCompanySeq(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("companySeq") int companySeq);

    /**
     * 获取指定时间区间的日订单数对象集合 统计用
     *
     * @param startTime
     * @param endTime
     * @param companySeq
     * @return
     */
    List<OrderDataVo> getOrdersCountByDay(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("companySeq") int companySeq);

    /**
     * 按年获取订单，按月分组
     * @param startTime
     * @param companySeq
     * @return
     */
    List<OrderDataVo> getOrdersCountByMonthOfYear(@Param("pramDate") Date startTime, @Param("companySeq") int companySeq);

    /**
     * 获取当天的所有订单号集合
     * @param companySeq
     * @return
     */
    List<Integer> getOrderSeqsByCompanySeqAndToday(@Param("companySeq") int companySeq);
}
