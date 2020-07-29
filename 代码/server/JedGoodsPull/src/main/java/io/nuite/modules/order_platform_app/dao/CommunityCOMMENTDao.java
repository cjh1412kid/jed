package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.CommunityCOMMENTEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:57
 */
@Mapper
public interface CommunityCOMMENTDao extends BaseMapper<CommunityCOMMENTEntity> {

	List<CommunityCOMMENTEntity> getFirstCommentList(Integer contentSeq);

	List<CommunityCOMMENTEntity> getSecondCommentList(Integer commentSeq);
	
	int getContentRecordNum(Integer seq, Integer type);

}
