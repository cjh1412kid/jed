package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;

import java.util.List;

/**
 * @description: 尺码service类
 * @author: jxj
 * @create: 2019-04-29 09:11
 */

public interface GoodsSizeService extends IService<GoodsSizeEntity> {
    /**
     * 获取尺码列表
     * @param companySeq
     * @return
     * @throws Exception
     */
    List<GoodsSizeEntity> selectGoodsSize(Integer companySeq) throws Exception;
}
