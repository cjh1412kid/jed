package io.nuite.modules.order_platform_app.service.impl;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.order_platform_app.dao.ShopMainpushDao;
import io.nuite.modules.order_platform_app.entity.ShopMainpushEntity;
import io.nuite.modules.order_platform_app.service.ShopMainpushService;


@Service
public class ShopMainpushServiceImpl extends ServiceImpl<ShopMainpushDao, ShopMainpushEntity> implements ShopMainpushService {

	@Autowired
	private ShopMainpushDao shopMainpushDao;
	
	
	/**
	 * 设置门店主推
	 */
	@Override
	public void setShopMainpush(Integer shopSeq, Integer shoesSeq, Integer isMainpush) {
		if(isMainpush == 0) {  //取消主推
			Wrapper<ShopMainpushEntity> wrapper = new EntityWrapper<ShopMainpushEntity>();
			wrapper.where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq);
			shopMainpushDao.delete(wrapper);
		} else if(isMainpush == 1) {  //设置主推
			/* 原来有的全部删除，防止接口重复调用时出现重复数据 */
			Wrapper<ShopMainpushEntity> wrapper = new EntityWrapper<ShopMainpushEntity>();
			wrapper.where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq);
			shopMainpushDao.delete(wrapper);
			
			ShopMainpushEntity shopMainpushEntity = new ShopMainpushEntity();
			shopMainpushEntity.setShopSeq(shopSeq);
			shopMainpushEntity.setShoesSeq(shoesSeq);
			shopMainpushDao.insert(shopMainpushEntity);
		}
		
	}


	
	/**
	 * 获取门店是否主推(返回0:否, 1:是)
	 */
	@Override
	public Integer getIsShopMainpush(Integer shopSeq, Integer shoesSeq) {
		Wrapper<ShopMainpushEntity> wrapper = new EntityWrapper<ShopMainpushEntity>();
		wrapper.where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq);
		List<ShopMainpushEntity>  list = shopMainpushDao.selectList(wrapper);
		if(list != null && list.size() > 0) {
			return 1;
		} else {
			return 0;
		}
	}


    /**
     * 获取主推门店的数量
     */
	@Override
	public Map<String, Object> getShopMainPushTimes(Integer shoesSeq) {
		return shopMainpushDao.getShopMainPushList(shoesSeq);
	}

	@Override
	public List<ShopMainpushEntity> selectMainPushShop(Integer shoesSeq) throws Exception {
		Map<String,Object> map = new HashMap<>(10);
		map.put("shoesSeq",shoesSeq);
		return baseMapper.selectMainPushShop(map);
	}


}
