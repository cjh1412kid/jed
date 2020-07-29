package io.nuite.modules.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.sr_base.entity.BaseAreaEntity;
import io.nuite.modules.sr_base.entity.BaseRegionEntity;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;

import java.util.List;

public interface SystemShopMmanageService {

    /**
     * 根据用户获取门店列表
     *
     * @param user
     * @param page
     */
    PageUtils queryShopByUser(BaseUserEntity user,  Page page);

    /**
     * 删除门店
     */
    void delete(Integer seq);

    /**
     * 根据该门店的Seq返回该门店的信息
     */
    BaseShopEntity getShopBySeq(Integer seq);

    /**
     * 根据用父节点查询子节点中所有的大区
     *
     * @param long1
     */
    List<BaseAreaEntity> areaList(Long long1);

    /**
     * 修改门店
     */
    void updateShop(BaseShopEntity baseShopEntity);

    /**
     * 添加门店
     */
    void saveShop(BaseShopEntity baseShopEntity);

    List<BaseShopEntity> selectAllList(Integer brandSeq);

    List<BaseRegionEntity> regionList(Long long1);
}
