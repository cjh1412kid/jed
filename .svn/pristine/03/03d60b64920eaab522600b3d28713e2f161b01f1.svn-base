package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.GoodsSXEntity;

import java.util.List;
import java.util.Map;

public interface GoodsSXService extends IService<GoodsSXEntity> {

    List<GoodsSXEntity> getGoodsSXListByCompanySeq(Integer companySeq);

    /**
     * 根据公司seq查询所有可见的属性
     * @param companySeq
     * @return
     */
    List<GoodsSXEntity> getGoodsSXListByCompanySeqAndVisibled(Integer companySeq);

    List<GoodsSXEntity> getGoodsSXContainOptionByCompanySeq(Integer companySeq);

	List<Map<String, Object>> selectSXIdNameByCompanySeq(Integer companySeq);

}
