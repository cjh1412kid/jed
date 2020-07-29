package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.OnlineMessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 0
 *
 * @author admin
 * @email
 * @date 2018-04-11 11:29:57
 */
@Mapper
public interface OnlineMessageDao extends BaseMapper<OnlineMessageEntity> {

    //web端查询当前聊天记录
    List<OnlineMessageEntity> selectTop21List(@Param("users") Integer[] seqArray, @Param("time") Date time);
}
