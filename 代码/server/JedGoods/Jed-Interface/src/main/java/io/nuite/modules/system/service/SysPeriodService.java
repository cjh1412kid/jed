package io.nuite.modules.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;

public interface SysPeriodService {

    Page<GoodsPeriodEntity> getPeriodPage(Integer companySeq, Integer pageNum, Integer pageSize);

    Boolean hasShoesInPeriod(Integer seq);

    void deleteGoodsPeriod(Integer seq);

    GoodsPeriodEntity edit(Integer seq);

    void addGoodsPeriod(Integer integer, GoodsPeriodEntity goodsPeriodEntity);

    void updateGoodsPeriod(GoodsPeriodEntity goodsPeriodEntity);

    Integer selectSeqByName(Integer brandSeq, String year, String name);
}
