package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.MeetingOrderEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-27 14:26:30
 */
@Mapper
public interface MeetingOrderDao extends BaseMapper<MeetingOrderEntity> {

	List<BaseUserEntity> getHqUserList(@Param("companySeq") Integer companySeq);


	List<Map<String, Object>> getShopMeetingOrderList(@Param("companySeq") Integer companySeq,
                                                      @Param("shopSeq") Integer shopSeq,
                                                      @Param("year") Integer year,
                                                      @Param("seasonSeqList") List<Integer> seasonSeqList);
	
}
