package io.nuite.modules.order_platform_app.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.order_platform_app.dao.MeetingOrderDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDailyDetailDao;
import io.nuite.modules.order_platform_app.entity.MeetingOrderEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.service.MeetingOrderService;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import io.nuite.modules.system.dao.order_platform.ShopAllotOrderDetailDao;
import io.nuite.modules.system.entity.order_platform.ShopAllotOrderDetailEntity;


@Service
public class MeetingOrderServiceImpl extends ServiceImpl<MeetingOrderDao, MeetingOrderEntity> implements MeetingOrderService {

	
    @Autowired
    private MeetingOrderDao meetingOrderDao;
    
    @Autowired
    private ShopAllotOrderDetailDao shopAllotOrderDetailDao;
    
    @Autowired
    private GoodsSizeDao goodsSizeDao;
    
    @Autowired
    private GoodsShoesDao goodsShoesDao;
	
    @Autowired
    private ShoesDataDailyDetailDao shoesDataDailyDetailDao;
    
    
	
	
	/**
	 * 查询总部订单
	 */
    @Deprecated
	@Override
	public List<Map<String, Object>> getHqMeetingOrder(Integer companySeq, Integer year, Integer seasonSeq, Integer orderStatus) {
		Wrapper<MeetingOrderEntity> wrapper = new EntityWrapper<MeetingOrderEntity>();
		wrapper.setSqlSelect("Seq AS seq, Shop_Seq AS shopSeq, OrderNum AS orderNum, OrderStatus AS orderStatus")
		.where("Company_Seq = {0} AND Year = {1} AND Season_Seq = {2} AND OrderStatus = {3}", companySeq, year, seasonSeq, orderStatus);
		return meetingOrderDao.selectMaps(wrapper);
		
	}



	/**
	 * 查询门店订单
	 */
	@Override
	public List<Map<String, Object>> getShopMeetingOrder(Integer companySeq, Integer shopSeq, Integer year, Integer seasonSeq,
			Integer orderStatus) {
		Wrapper<MeetingOrderEntity> wrapper = new EntityWrapper<MeetingOrderEntity>();
		wrapper.setSqlSelect("Seq AS seq, Shop_Seq AS shopSeq, OrderNum AS orderNum, OrderStatus AS orderStatus")
		.where("Company_Seq = {0} AND Shop_Seq = {1} AND Year = {2} AND Season_Seq = {3} AND OrderStatus = {4}", companySeq, shopSeq, year, seasonSeq, orderStatus);
		return meetingOrderDao.selectMaps(wrapper);
		
	}



	/**
	 * 获取总部所有账号
	 */
	@Override
	public List<BaseUserEntity> getHqUserList(Integer companySeq) {
		return meetingOrderDao.getHqUserList(companySeq);
	}
	
	
	
	/**
	 * 查询门店本次订货会订单（即筛板分配表中已完成配码的数据）
	 */
	@Override
	public List<Map<String, Object>> getShopMeetingOrderList(Integer companySeq, Integer shopSeq, Integer year, List<Integer> seasonSeqList) {
		List<Map<String, Object>> list = meetingOrderDao.getShopMeetingOrderList(companySeq, shopSeq, year, seasonSeqList);
		return list;
	}



	/**
	 * 查询订货会订单详情
	 */
	@Override
	public List<ShopAllotOrderDetailEntity> getShopMeetingOrderDetail(Integer shopAllotOrderSeq) {
		Wrapper<ShopAllotOrderDetailEntity> wrapper = new EntityWrapper<ShopAllotOrderDetailEntity>();
		wrapper.where("OrderSeq = {0}", shopAllotOrderSeq);
		return shopAllotOrderDetailDao.selectList(wrapper);
	}



	/**
	 * 根据尺码值获取尺码序号
	 */
	@Override
	public Integer getSizeSeqBySize(Integer companySeq, Integer size) {
		Wrapper<GoodsSizeEntity> wrapper = new EntityWrapper<GoodsSizeEntity>();
		wrapper.where("Company_Seq = {0} AND SizeName = {1}", companySeq, size);
		List<GoodsSizeEntity> list = goodsSizeDao.selectList(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return list.get(0).getSeq();
		} else {
			return null;
		}
	}



	/**
	 * 根据货号获取鞋子序号
	 */
	@Override
	public Integer getShoesSeqByGoodId(Integer companySeq, String goodId) {
		Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<GoodsShoesEntity>();
		wrapper.where("Company_Seq = {0} AND GoodID = {1}", companySeq, goodId);
		List<GoodsShoesEntity> list = goodsShoesDao.selectList(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return list.get(0).getSeq();
		} else {
			return null;
		}
	}



	/**
	 * 获取门店某一货号某一尺码的鞋子的进货量
	 */
	@Override
	public Integer getOrderShoesShopInNum(Integer shopSeq, Integer shoesSeq, Integer sizeSeq) {
		Wrapper<ShoesDataDailyDetailEntity> wrapper = new EntityWrapper<ShoesDataDailyDetailEntity>();
		wrapper.setSqlSelect("SUM (Count)")
		.where("Shop_Seq = {0} AND Shoes_Seq = {1} AND Type = 1 AND Size = {2}", shopSeq, shoesSeq, sizeSeq);
		
		List<Object> list = shoesDataDailyDetailDao.selectObjs(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}

}
