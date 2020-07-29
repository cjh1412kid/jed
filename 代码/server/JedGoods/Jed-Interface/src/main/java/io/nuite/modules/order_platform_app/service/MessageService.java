package io.nuite.modules.order_platform_app.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;

import java.util.List;
import java.util.Map;

/**
 * @description: 消息Service类
 * @author: jxj
 * @create: 2019-03-30 13:15
 */

public interface MessageService extends IService<MessageEntity> {

    /**
     * 获取消息
     * @param baseUserEntity
     * @param type
     * @param page
     * @return
     * @throws Exception
     */
    Map<String,Object> selectMessage(BaseUserEntity baseUserEntity, Integer type, Page<MessageEntity> page) throws Exception;

    /**
     * 获取单条消息
     * @param seq
     * @return
     * @throws Exception
     */
    Map<String,Object> selectMessageBySeq(Integer seq) throws Exception;
}
