package io.nuite.modules.order_platform_app.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.order_platform_app.dao.MeetingOrderDetailDao;
import io.nuite.modules.order_platform_app.entity.MeetingOrderDetailEntity;
import io.nuite.modules.order_platform_app.service.MeetingOrderDetailService;

@Service
public class MeetingOrderDetailServiceImpl extends ServiceImpl<MeetingOrderDetailDao, MeetingOrderDetailEntity> implements MeetingOrderDetailService {

	
    @Autowired
    private MeetingOrderDetailDao meetingOrderDetailDao;
	



	/**
	 * 获取订单中每个货号的订货量、发货量、尺码范围
	 */
	@Override
	public List<Map<String, Object>> getOrderGoodIdNum(Integer meetingOrderSeq) {
		Wrapper<MeetingOrderDetailEntity> wrapper = new EntityWrapper<MeetingOrderDetailEntity>();
		wrapper.setSqlSelect("OrderShoes_Seq AS orderShoesSeq, GoodID AS goodId, MIN (Size_Seq) AS minSizeSeq, MAX (Size_Seq) AS maxSizeSeq, SUM (BuyCount) AS buyCount, SUM (DeliverNum) AS deliverNum")
		.where("MeetingOrder_Seq = {0}", meetingOrderSeq).groupBy("OrderShoes_Seq, GoodID");
		return meetingOrderDetailDao.selectMaps(wrapper);
	}




	/**
	 * 获取订单中某一货号，各个尺码的订货量、发货量
	 */
	@Override
	public List<Map<String, Object>> getGoodIdSizeNum(Integer meetingOrderSeq, Integer orderShoesSeq) {
		Wrapper<MeetingOrderDetailEntity> wrapper = new EntityWrapper<MeetingOrderDetailEntity>();
		wrapper.setSqlSelect("Size_Seq AS sizeSeq, BuyCount AS buyCount, DeliverNum AS deliverNum")
		.where("MeetingOrder_Seq = {0} AND OrderShoes_Seq = {1}", meetingOrderSeq, orderShoesSeq);
		return meetingOrderDetailDao.selectMaps(wrapper);
	}

}
