package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.BaseBrandEntity;

import java.util.Map;

public interface BaseBrandService extends IService<BaseBrandEntity> {

	Map<String, Object> getBrandMapBySeq(Integer brandSeq);

	/**
	 * 根据公司序号获取品牌序号
	 * @param companySeq
	 * @return
	 */
	Integer getBrandSeqByCompanySeq(Integer companySeq);
}
