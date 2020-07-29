package io.nuite.modules.system.service.order_platform;

import io.nuite.common.utils.PageUtils;
import io.nuite.modules.sr_base.entity.BaseAgentEntity;
import io.nuite.modules.sr_base.entity.BaseAreaEntity;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;

import java.util.List;

public interface OrderPartyManagementService {

    /**
     * 根据用户seq删除订货方
     */
    Integer deleteOrderParty(Integer seq);

    /**
     * 根据当前工厂的用户seq和订货方类型返回订货方列表
     */
    PageUtils orderPartyList(BaseUserEntity loginUser, Integer companyType, Integer page, Integer limit);

    /**
     * 根据用户seq获得订货方的具体信息(修改信息)
     */
    BaseUserEntity edit(Integer seq);

    /**
     * 修改账户信息
     */
    int updateOrderParty(BaseUserEntity baseUserEntity, BaseUserEntity userEntity);

    /**
     * 添加账户
     * 
     * @param i
     */
    void addOrderParty(BaseUserEntity baseUser, BaseUserEntity baseUserEntity)  throws Exception;


    /**
     * 判断该账号是否存在
     * 
     * @param userSeq
     */
    boolean judgeUserOrCompanyExistence(BaseUserEntity baseUserEntity);

    /**
     * 返回工厂或某一分公司的门店列表
     */
    List<BaseShopEntity> shopList(BaseUserEntity baseUserEntity, Integer attachSeq);

    /**
     * 返回代理商列表
     */
    List<BaseAgentEntity> agentList(Integer brandSeq);

    /**
     * 返回分公司列表
     */
    List<BaseAreaEntity> branchOfficeList(Integer brandSeq);

}
