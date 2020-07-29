package io.nuite.modules.sr_base.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;

public interface GoodsCategoryService extends IService<GoodsCategoryEntity> {

	GoodsCategoryEntity getGoodsCategoryBySeq(Integer categorySeq);

	List<Integer> getFinalCategorySeqList(Integer categorySeq);

	List<GoodsCategoryEntity> getCategoryByFuzzyName(Integer companySeq, String param);
	
	List<Integer> getCategorySeqList(Integer categorySeq);

	List<GoodsCategoryEntity> getSecondCategoryList(Integer companySeq);
}
