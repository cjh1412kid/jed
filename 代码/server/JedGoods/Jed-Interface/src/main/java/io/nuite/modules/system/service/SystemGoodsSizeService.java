package io.nuite.modules.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;

import java.util.List;

public interface SystemGoodsSizeService extends IService<GoodsSizeEntity> {

    Page<GoodsSizeEntity> getGoodsSizePage(Integer companySeq, Integer pageNum, Integer pageSize);

    Integer addGoodsSize(GoodsSizeEntity goodsSizeEntity);

    void updateGoodsSize(GoodsSizeEntity goodsSizeEntity);

    Boolean hasShoesInSize(Integer seq);

    void deleteGoodsSize(Integer seq);

    GoodsSizeEntity edit(Integer seq);

    void addBatchGoodsSizes(List<GoodsSizeEntity> goodsSizeList);

    boolean sizeNameExisted(Integer sizeSeq, Integer companySeq, String sizeName);

    Integer getSizeSeqByCodeAndCompanySeq(Integer companySeq, String sizeCode);
}
