package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.ShopMainpushEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-13 17:43:43
 */
@Mapper
public interface ShopMainpushDao extends BaseMapper<ShopMainpushEntity> {
	/**
	 * 主推款的门店数
	 * @param shoesSeq
	 * @return
	 */
	Map<String, Object> getShopMainPushList(@Param("shoesSeq") Integer shoesSeq);

	/**
	 * 获取主推门店
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<ShopMainpushEntity> selectMainPushShop(Map<String, Object> map) throws Exception;
}
