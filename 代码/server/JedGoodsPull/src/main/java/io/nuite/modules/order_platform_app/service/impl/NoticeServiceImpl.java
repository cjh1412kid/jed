package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.dao.NoticeDao;
import io.nuite.modules.order_platform_app.entity.NoticeEntity;
import io.nuite.modules.order_platform_app.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2019/3/14 14:07
 * @Version: 1.0
 */

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeDao, NoticeEntity> implements NoticeService {

    @Autowired
    NoticeDao noticeDao;

    @Override
    public Integer save(NoticeEntity noticeEntity) {
        return noticeDao.insert(noticeEntity);
    }

    @Override
    public void updateNameBySeq(String newName, Integer seq) {
        noticeDao.updateNameBySeq(newName, seq);
    }

    @Override
    public NoticeEntity getRecentOne(Integer companySeq) {

        return super.selectOne(new EntityWrapper<NoticeEntity>().
                eq("CompanySeq", companySeq)
                .orderBy("InputTime", false));
    }

    
    
    /**
     * 获取所有启用的公告
     */
	@Override
	public List<NoticeEntity> getEnableNoticeList(Integer companySeq) {
		Wrapper<NoticeEntity> wrapper = new EntityWrapper<NoticeEntity>();
		wrapper.where("CompanySeq = {0} AND isUsed = 1", companySeq).orderBy("InputTime DESC");
		return noticeDao.selectList(wrapper);
	}

	
	
	/**
	 * 设置是否展示
	 */
	@Override
	public void setIsUsed(Integer seq, Integer isUsed) {
		NoticeEntity noticeEntity = noticeDao.selectById(seq);
		if(noticeEntity != null) {
			if(isUsed == 0) {
				noticeEntity.setIsUsed(1);
			} else if(isUsed == 1) {
				noticeEntity.setIsUsed(0);
			}
		noticeDao.updateById(noticeEntity);
		}
	}

}
