package io.nuite.modules.sr_base.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.GoodsSXOptionEntity;

public interface GoodsSXOptionService extends IService<GoodsSXOptionEntity> {

	List<GoodsSXOptionEntity> getGoodsSXOptionListBySXSeq(Integer seq);

}
