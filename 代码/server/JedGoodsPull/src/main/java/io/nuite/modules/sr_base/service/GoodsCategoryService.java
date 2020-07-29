package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;

import java.util.List;

public interface GoodsCategoryService extends IService<GoodsCategoryEntity> {

	GoodsCategoryEntity getGoodsCategoryBySeq(Integer categorySeq);

	List<Integer> getFinalCategorySeqList(Integer categorySeq);

	List<GoodsCategoryEntity> getCategoryByFuzzyName(Integer companySeq, String param);
	
	List<Integer> getCategorySeqList(Integer categorySeq);

	List<GoodsCategoryEntity> getSecondCategoryList(Integer companySeq);
}
