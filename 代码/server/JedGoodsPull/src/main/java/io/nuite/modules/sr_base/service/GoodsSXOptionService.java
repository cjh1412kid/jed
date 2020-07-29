package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.GoodsSXOptionEntity;

import java.util.List;

public interface GoodsSXOptionService extends IService<GoodsSXOptionEntity> {

	List<GoodsSXOptionEntity> getGoodsSXOptionListBySXSeq(Integer seq);

}
