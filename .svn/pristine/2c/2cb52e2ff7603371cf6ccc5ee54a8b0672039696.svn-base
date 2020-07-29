package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description: 消息Dao类
 * @author: jxj
 * @create: 2019-03-30 13:09
 */
@Mapper
public interface MessageDao extends BaseMapper<MessageEntity> {
    /**
     * 获取消息
     * @param map
     * @param page
     * @return
     * @throws Exception
     */
    List<MessageEntity> selectMessage(Map<String,Object> map, Page<MessageEntity> page) throws Exception;

    /**
     * 获取单条消息
     * @param map
     * @return
     * @throws Exception
     */
    MessageEntity selectMessageBySeq(Map<String,Object> map) throws Exception;

    /**
     * 批量新增消息
     * @param messageEntities
     * @return
     * @throws Exception
     */
    Integer insertMessage(List<MessageEntity> messageEntities) throws Exception;
}
