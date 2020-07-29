package io.nuite.modules.sr_base.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

import io.nuite.modules.sr_base.entity.GoodsColorEntity;

public interface GoodsColorService extends IService<GoodsColorEntity> {
	/**
	 *
	 * @param companySeq
	 * @return
	 */
	List<GoodsColorEntity> getColorListByCompanySeq(Integer companySeq);
}
