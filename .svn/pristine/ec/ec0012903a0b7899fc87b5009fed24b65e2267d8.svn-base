package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.OnlineGroupMemberEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:57
 */
@Mapper
public interface OnlineGroupMemberDao extends BaseMapper<OnlineGroupMemberEntity> {

	//根据群组seq获取群成员列表
	List<Map<String, Object>> getMembersByGroupSeq(Integer groupSeq);
	
}
