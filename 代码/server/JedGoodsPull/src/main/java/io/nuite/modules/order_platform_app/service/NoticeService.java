package io.nuite.modules.order_platform_app.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.NoticeEntity;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2019/3/14 14:07
 * @Version: 1.0
 */

public interface NoticeService extends IService<NoticeEntity> {

    /**
     * 插入一条记录，且返回seq
     *
     * @param noticeEntity
     * @return
     */
    Integer save(NoticeEntity noticeEntity);

    /**
     * 根据seq更新标题
     *
     * @param newName
     * @param seq
     */
    void updateNameBySeq(String newName, Integer seq);

    /**
     * 根据公司seq获取最近的一个
     *
     * @param companySeq
     * @return
     */
    NoticeEntity getRecentOne(Integer companySeq);

	List<NoticeEntity> getEnableNoticeList(Integer companySeq);

	void setIsUsed(Integer seq, Integer isUsed);
}
