package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.NoticeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: yangchuang
 * @Date: 2019/3/14 14:06
 * @Version: 1.0
 */
@Mapper
public interface NoticeDao extends BaseMapper<NoticeEntity> {

    /**
     * 根据seq更新标题
     *
     * @param newName
     * @param seq
     */
    void updateNameBySeq(@Param("newName") String newName, @Param("seq") Integer seq);

    /**
     * 插入一条新纪录，返回seq
     *
     * @param noticeEntity
     * @return
     */
    Integer save(NoticeEntity noticeEntity);
}
