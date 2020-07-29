package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.order_platform_app.dao.*;
import io.nuite.modules.order_platform_app.entity.*;
import io.nuite.modules.order_platform_app.service.MeetingPlanService;
import io.nuite.modules.sr_base.dao.GoodsPeriodDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingPlanServiceImpl implements MeetingPlanService {
    
    @Autowired
    private MeetingPlanDao meetingPlanDao;
    
    @Autowired
    private GoodsPeriodDao goodsPeriodDao;
    
    @Autowired
    private MeetingRemindDao meetingRemindDao;
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private OrderProductsDao orderProductsDao;
    
    @Autowired
    private ShoesDataDao shoesDataDao;
    
    @Autowired
    private GoodsShoesDao goodsShoesDao;
    
    @Autowired
    private ShoppingCartDao shoppingCartDao;



    /**
     * 查询用户所属工厂的所有波次
     */
	@Override
	public List<GoodsPeriodEntity> getAllPeriodList(Integer brandSeq) {
		Wrapper<GoodsPeriodEntity> wrapper = new EntityWrapper<GoodsPeriodEntity>();
		wrapper.where("Brand_Seq = {0}", brandSeq).orderBy("Seq DESC");
		return goodsPeriodDao.selectList(wrapper);
	}
	
	
	/**
	 * 设置提醒
	 */
	@Override
	public void updateMeetingRemind(Integer userSeq, Integer scanRemind, Integer orderRemind) {
		Wrapper<MeetingRemindEntity> wrapper = new EntityWrapper<MeetingRemindEntity>();
		wrapper.where("User_Seq = {0}", userSeq);
		List<MeetingRemindEntity> meetingRemindList = meetingRemindDao.selectList(wrapper);
		if(meetingRemindList.size() == 0) {
			MeetingRemindEntity meetingRemindEntity = new MeetingRemindEntity();
			meetingRemindEntity.setUserSeq(userSeq);
			meetingRemindEntity.setScanRemind(scanRemind);
			meetingRemindEntity.setOrderRemind(orderRemind);
			meetingRemindDao.insert(meetingRemindEntity);
		} else {
			MeetingRemindEntity meetingRemindEntity = meetingRemindList.get(0);
			meetingRemindEntity.setScanRemind(scanRemind);
			meetingRemindEntity.setOrderRemind(orderRemind);
			meetingRemindDao.updateById(meetingRemindEntity);
		}
	}
    
	
    /**
     * 查询用户某一波次的订货计划
     */
	@Override
	public List<MeetingPlanEntity> getUserMeetingPlanList(Integer userSeq, Integer periodSeq) {
		Wrapper<MeetingPlanEntity> wrapper = new EntityWrapper<MeetingPlanEntity>();
		wrapper.where("Period_Seq = {0} AND User_Seq = {1}", periodSeq, userSeq);
		return meetingPlanDao.selectList(wrapper);
	}


	/**
	 * 获取当前正在开订货会的波次
	 */
	@Override
	public GoodsPeriodEntity getMeetingPeriodNow(Integer brandSeq) {
		Wrapper<GoodsPeriodEntity> wrapper = new EntityWrapper<GoodsPeriodEntity>();
		wrapper.where("Brand_Seq = {0} AND MeetingStartTime <= GETDATE() AND MeetingEndTime >= GETDATE()", brandSeq);
		List<GoodsPeriodEntity> list = goodsPeriodDao.selectList(wrapper);
		if(list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}


	/**
	 * 获取当前购买的鞋子（大类、SX2、SX3都一致） 相对应的订货计划
	 */
/*	@Override
	public MeetingPlanEntity getShoesMeetingPlan(GoodsShoesEntity goodsShoesEntity, Integer periodSeq, Integer userSeq) {
		MeetingPlanEntity meetingPlanEntity = new MeetingPlanEntity();
		meetingPlanEntity.setPeriodSeq(periodSeq);
		meetingPlanEntity.setUserSeq(userSeq);
		meetingPlanEntity.setCategorySeq(goodsShoesEntity.getCategorySeq());
		if(goodsShoesEntity.getSX2() != null) {
			meetingPlanEntity.setSX2(goodsShoesEntity.getSX2());
		} else {
			return null;
		}
		if(goodsShoesEntity.getSX3() != null) {
			meetingPlanEntity.setSX3(goodsShoesEntity.getSX3());
		} else {
			return null;
		}
		return meetingPlanDao.selectOne(meetingPlanEntity);
	}*/


	/**
	 * 获取该用户 该波次订货会期间的所有订单
	 */
	@Override
	public List<OrderEntity> getAllOrderByUserSeq(Integer userSeq, GoodsPeriodEntity goodsPeriodEntity) {
		Wrapper<OrderEntity> wrapper = new EntityWrapper<OrderEntity>();
		wrapper.where("User_Seq = {0} AND IsSplit = 0 AND InputTime >= {1} AND InputTime <= {2}", userSeq, goodsPeriodEntity.getMeetingStartTime(), goodsPeriodEntity.getMeetingEndTime());
		return orderDao.selectList(wrapper);
	}


	/**
	 * 根据orderSeq集合获取所有OrderProducts
	 */
	@Override
	public List<OrderProductsEntity> getAllOrderProductByOrderSeqList(List<Integer> orderSeqList) {
		Wrapper<OrderProductsEntity> wrapper = new EntityWrapper<OrderProductsEntity>();
		wrapper.in("Order_Seq", orderSeqList);
		return orderProductsDao.selectList(wrapper);
	}


	/**
	 * 根据shoesDateSeq获取鞋子基本信息实体
	 */
	@Override
	public GoodsShoesEntity getGoodsShoesByShoesDateSeq(Integer shoesDataSeq) {
		ShoesDataEntity shoesDataEntity = shoesDataDao.selectById(shoesDataSeq);
		return goodsShoesDao.selectById(shoesDataEntity.getShoesSeq());
	}


	/**
	 * 根据用户Seq获取所有购物车列表
	 */
	@Override
	public List<ShoppingCartEntity> getShoppingCartListByUserSeq(Integer userSeq) {
		Wrapper<ShoppingCartEntity> wrapper = new EntityWrapper<ShoppingCartEntity>();
		wrapper.where("User_Seq = {0} ", userSeq);
		return shoppingCartDao.selectList(wrapper);
	}


	
	
	
	
	
	/**
	 * 获取所有门店的某年某季度某一品类的订货计划
	 */
	@Override
	public List<MeetingPlanEntity> getAllShopsMeetingPlan(Integer companySeq, Integer year, Integer seasonSeq, Integer categorySeq) {
		
		Wrapper<MeetingPlanEntity> wrapper = new EntityWrapper<MeetingPlanEntity>();
		wrapper.where("Company_Seq = {0} AND Year = {1} AND Season_Seq = {2} AND Category_Seq = {3}", companySeq, year, seasonSeq, categorySeq);
		return meetingPlanDao.selectList(wrapper);
		
	}
	
	
}
