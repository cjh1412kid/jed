package io.nuite.modules.order_platform_app.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.MeetingOrderDetailEntity;

import java.util.List;
import java.util.Map;

public interface MeetingOrderDetailService extends IService<MeetingOrderDetailEntity> {

	List<Map<String, Object>> getOrderGoodIdNum(Integer meetingOrderSeq);

	List<Map<String, Object>> getGoodIdSizeNum(Integer meetingOrderSeq, Integer orderShoesSeq);

}

