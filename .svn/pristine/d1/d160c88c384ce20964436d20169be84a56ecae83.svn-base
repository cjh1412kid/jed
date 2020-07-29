package io.nuite.modules.system.dao.order_platform;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderManageDao extends BaseMapper<OrderEntity> {
    /**
     * 订单总条数
     */
    int getTotalCount(@Param("userSeqList") List<Integer> userSeqList, @Param("orderStatus") Integer orderStatus,
                      @Param("keywords") String keywords, @Param("startTime") String startTime, @Param("endTime") String endTime,
                      @Param("companySeq") Integer companySeq);

    /**
     * 订单列表
     */
    List<OrderEntity> orderList(@Param("userSeqList") List<Integer> userSeqList,
                                @Param("orderStatus") Integer orderStatus, @Param("keywords") String keywords,
                                @Param("startTime") String startTime, @Param("endTime") String endTime,
                                @Param("companySeq") Integer companySeq, @Param("page") Integer page, @Param("limit") Integer limit);

    /**
     * 订单所有货号
     */
    List<Map<String, Object>> getOrderGoodIds(@Param("orderSeq") Integer orderSeq);


    /**
     * 订单商品列表
     */
    List<Map<String, Object>> getOrderProductsList(@Param("orderSeq") Integer orderSeq);


    Map<String, Object> getErpOrderProduct(@Param("orderSeq") Integer orderSeq);

    /**
     * 订单统计分析列表
     */
	List<Map<String, Object>> orderStatisticsList(@Param("companySeq")Integer companySeq, 
												@Param("userSeqs")String userSeqs, 
												@Param("startTime")Date startTime, 
												@Param("endTime")Date endTime,
												@Param("orderStatus")Integer orderStatus, 
												@Param("keywords")String keywords);
}
