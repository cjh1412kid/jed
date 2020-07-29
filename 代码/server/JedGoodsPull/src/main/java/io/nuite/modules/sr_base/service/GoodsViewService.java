package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.GoodsViewEntity;

import java.util.Map;

public interface GoodsViewService extends IService<GoodsViewEntity> {
	
	GoodsViewEntity getGoodsViewBySeq(Integer shoesSeq);

	Map<String, Object> getGoodsViewMapBySeq(Integer shoesSeq);

	/**
	 * 根据货号查货品
	 * @param goodId
	 * @return
	 * @throws Exception
	 */
	GoodsViewEntity getGoodsViewByGoodId(String goodId) throws Exception;
}
