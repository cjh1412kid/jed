package io.nuite.modules.system.service.home_page_management;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.common.utils.PageInfo;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.system.entity.home_page_management.SystemSowingMapEntity;

import java.io.IOException;
import java.util.Map;

public interface SystemSowingMapService extends IService<SystemSowingMapEntity> {

    PageUtils sowingMapList(Integer brandSeq);

    /**
     * 获取该用户所有商品的列表
     */
    PageUtils goodList(Integer brandSeq, PageInfo pageInfo);

    SystemSowingMapEntity edit(Integer seq);

    /**
     * 返回改用户创建的数量
     */
    Integer getCreatedNumber(Integer brandSeq);

    void save(Integer brandSeq, SystemSowingMapEntity systemSowingMapEntity) throws IOException;

    void update(Integer brandSeq, SystemSowingMapEntity systemSowingMapEntity) throws IOException ;

	PageUtils periodList(Integer brandSeq, Map<String, Object> params);

}
