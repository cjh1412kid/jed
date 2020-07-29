package io.nuite.modules.order_platform_app.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.ShopMainpushEntity;

import java.util.List;
import java.util.Map;

public interface ShopMainpushService extends IService<ShopMainpushEntity> {

	void setShopMainpush(Integer shopSeq, Integer shoesSeq, Integer isMainpush);

	Integer getIsShopMainpush(Integer shopSeq, Integer shoesSeq);

	/**
	 * 获取主推次数
	 * @param shoesSeq
	 * @return
	 */
	Map<String, Object> getShopMainPushTimes(Integer shoesSeq);

	/**
	 * 获取主推门店
	 * @param shoesSeq
	 * @return
	 * @throws Exception
	 */
	List<ShopMainpushEntity> selectMainPushShop(Integer shoesSeq) throws Exception;

}

