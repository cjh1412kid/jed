package io.nuite.modules.system.dao.order_platform;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderPartyManagementDao {

    /**
     * 根据用户seq删除订货方
     */
    Integer deleteOrderParty(@Param("seq") Integer seq);

}
