package io.nuite.modules.order_platform_app.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.nuite.modules.order_platform_app.entity.MeetingOrderEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.system.entity.order_platform.ShopAllotOrderDetailEntity;


public interface MeetingOrderService extends IService<MeetingOrderEntity> {

	List<Map<String, Object>> getHqMeetingOrder(Integer companySeq, Integer year, Integer seasonSeq, Integer orderStatus);

	List<Map<String, Object>> getShopMeetingOrder(Integer companySeq, Integer shopSeq, Integer year, Integer seasonSeq,
			Integer orderStatus);

	List<BaseUserEntity> getHqUserList(Integer companySeq);

	List<Map<String, Object>> getShopMeetingOrderList(Integer companySeq, Integer shopSeq, Integer year, List<Integer> seasonSeqList);

	List<ShopAllotOrderDetailEntity> getShopMeetingOrderDetail(Integer shopAllotOrderSeq);

	Integer getSizeSeqBySize(Integer companySeq, Integer size);

	Integer getShoesSeqByGoodId(Integer companySeq, String goodId);

	Integer getOrderShoesShopInNum(Integer shopSeq, Integer shoesSeq, Integer sizeSeq);

}

