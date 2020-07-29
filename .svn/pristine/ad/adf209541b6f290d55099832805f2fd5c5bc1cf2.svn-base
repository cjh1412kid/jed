package io.nuite.modules.system.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;

import java.util.List;

public interface SystemGoodsCategoryService extends IService<GoodsCategoryEntity> {

	List<GoodsCategoryEntity> getAllGoodsCategoryByCompanySeq(Integer companySeq);

	Integer addGoodsCategory(GoodsCategoryEntity goodsCategoryEntity);

	void updateGoodsCategory(GoodsCategoryEntity goodsCategoryEntity);

	Boolean hasShoesInCategory(Integer seq);

	void deleteGoodsCategory(Integer seq);

	
    List<Integer> nOcategoryLsit(Integer companySeq);

    Boolean hasCategoryInCategory(Integer seq);

    void deleteGoodsCategoryAll(Integer seq);

}
