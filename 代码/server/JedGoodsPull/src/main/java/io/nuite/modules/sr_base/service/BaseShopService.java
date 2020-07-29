package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.BaseShopEntity;

import java.util.List;

public interface BaseShopService extends IService<BaseShopEntity> {

	List<Object> getShopSeqsByCompanySeq(Integer companySeq);

	List<BaseShopEntity> getShopsByCompanySeq(Integer companySeq);
	
	List<Integer> getAllShopSeqs(Integer brandSeq);
}
