package io.nuite.modules.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;

import java.util.List;

public interface SystemGoodsColorService extends IService<GoodsColorEntity> {

    Page<GoodsColorEntity> getGoodsColorPage(Integer companySeq, Integer pageNum, Integer pageSize);

    Integer addGoodsColor(GoodsColorEntity goodsColorEntity);

    void updateGoodsColor(GoodsColorEntity goodsColorEntity);

    Boolean hasShoesInColor(Integer seq);

    void deleteGoodsColor(Integer seq);

    GoodsColorEntity edit(Integer seq);

    void addBatchGoodsColors(List<GoodsColorEntity> goodsColorList);

    boolean colorNameExisted(Integer colorSeq, Integer companySeq, String name);

    Integer selectColorSeqByCode(Integer companySeq, String code);

}
