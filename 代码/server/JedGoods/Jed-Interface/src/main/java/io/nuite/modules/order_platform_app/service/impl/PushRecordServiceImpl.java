package io.nuite.modules.order_platform_app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import io.nuite.modules.order_platform_app.dao.PushRecordDao;
import io.nuite.modules.order_platform_app.entity.PushRecordEntity;
import io.nuite.modules.order_platform_app.service.PushRecordService;

@Service
public class PushRecordServiceImpl implements PushRecordService {
    
    @Autowired
    private PushRecordDao pushRecordDao;
    
    
    
    /**
     * 未读消息数量
     */
	@Override
	public List<Map<String,Object>> getUnreadRecordNum(Integer userSeq) {
		Wrapper<PushRecordEntity> wrapper = new EntityWrapper<PushRecordEntity>();
		wrapper.setSqlSelect("Type AS type, COUNT(Seq) AS num").where("ReceiveUserSeq = {0} AND IsRead = 0", userSeq).groupBy("Type");
		return pushRecordDao.selectMaps(wrapper);
	}



	/**
	 * 所有消息列表
	 */
	@Override
	public List<PushRecordEntity> getPushRecordList(Integer userSeq, Integer start, Integer num) {
		Wrapper<PushRecordEntity> wrapper = new EntityWrapper<PushRecordEntity>();
		wrapper.where("ReceiveUserSeq = {0}", userSeq).orderBy("InputTime DESC");
		return pushRecordDao.selectPage(new RowBounds(start - 1, num), wrapper);
	}



	/**
	 * 标记推送消息为已读
	 */
	@Override
	public void readPushRecord(Integer seq) {
		PushRecordEntity pushRecordEntity = new PushRecordEntity();
		pushRecordEntity.setSeq(seq);
		pushRecordEntity.setIsRead(1);
		pushRecordDao.updateById(pushRecordEntity);
	}



	/**
	 * 新增推送消息
	 */
	@Override
	public void addPushRecord(Integer pushUserSeq, Integer receiveUserSeq, String accountName, Integer type, Integer orderSeq, String content) {
		PushRecordEntity pushRecordEntity = new PushRecordEntity();
		pushRecordEntity.setPushUserSeq(pushUserSeq);
		pushRecordEntity.setReceiveUserSeq(receiveUserSeq);
		pushRecordEntity.setAccountName(accountName);
		pushRecordEntity.setType(type);
		pushRecordEntity.setOrderSeq(orderSeq);
		pushRecordEntity.setContent(content);
		pushRecordEntity.setIsRead(0);
		pushRecordEntity.setInputTime(new Date());
		pushRecordEntity.setDel(0);
		pushRecordDao.insert(pushRecordEntity);
		
	}
    

    
}
