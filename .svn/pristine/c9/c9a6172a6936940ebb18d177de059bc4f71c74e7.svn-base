package io.nuite.modules.order_platform_app.service;

import io.nuite.modules.order_platform_app.entity.MeetingPlanEntity;
import io.nuite.modules.order_platform_app.entity.OrderEntity;
import io.nuite.modules.order_platform_app.entity.OrderProductsEntity;
import io.nuite.modules.order_platform_app.entity.ShoppingCartEntity;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;

import java.util.List;

public interface MeetingPlanService {
	
	List<GoodsPeriodEntity> getAllPeriodList(Integer brandSeq);

	List<MeetingPlanEntity> getUserMeetingPlanList(Integer userSeq, Integer periodSeq);

	void updateMeetingRemind(Integer userSeq, Integer scanRemind, Integer orderRemind);

	GoodsPeriodEntity getMeetingPeriodNow(Integer brandSeq);

	//MeetingPlanEntity getShoesMeetingPlan(GoodsShoesEntity goodsShoesEntity, Integer periodSeq, Integer userSeq);

	List<OrderEntity> getAllOrderByUserSeq(Integer userSeq, GoodsPeriodEntity goodsPeriodEntity);

	List<OrderProductsEntity> getAllOrderProductByOrderSeqList(List<Integer> orderSeqList);

	GoodsShoesEntity getGoodsShoesByShoesDateSeq(Integer shoesSeq);

	List<ShoppingCartEntity> getShoppingCartListByUserSeq(Integer userSeq);

	
	
	
	
	
	List<MeetingPlanEntity> getAllShopsMeetingPlan(Integer companySeq, Integer year, Integer seasonSeq, Integer categorySeq);

}
