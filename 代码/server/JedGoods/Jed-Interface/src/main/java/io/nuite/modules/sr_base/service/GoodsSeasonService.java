package io.nuite.modules.sr_base.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import io.nuite.modules.sr_base.entity.GoodsSeasonEntity;


public interface GoodsSeasonService extends IService<GoodsSeasonEntity> {

    Page<GoodsSeasonEntity> selectSeasonPage(Integer companySeq, Integer pageNum, Integer pageSize);

    Boolean hasShoesInSeason(Integer seq);

    void deleteGoodsSeason(Integer seq);

    GoodsSeasonEntity edit(Integer seq);

    void addGoodsSeason(Integer integer, GoodsSeasonEntity goodsSeasonEntity);

    void updateGoodsSeason(GoodsSeasonEntity goodsSeasonEntity);

	List<GoodsSeasonEntity> getAllSeasonList(Integer brandSeq);

	GoodsSeasonEntity getSeasonByName(String name,Integer brandSeq);
}

