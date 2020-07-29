package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.order_platform_app.dao.ReceiverInfoDao;
import io.nuite.modules.order_platform_app.entity.ReceiverInfoEntity;
import io.nuite.modules.order_platform_app.service.ReceiverInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class  ReceiverInfoServiceImpl implements ReceiverInfoService {
    
    @Autowired
    private ReceiverInfoDao receiverInfoDao;

    
    /**
     * 根据userSeq获取收货人列表
     */
	@Override
	public List<ReceiverInfoEntity> getReceiverInfoByUserSeq(Integer userSeq) {
		Wrapper<ReceiverInfoEntity> wrapper = new EntityWrapper<ReceiverInfoEntity>();
		wrapper.where("User_Seq = {0}", userSeq).orderBy("IsDefault DESC, InputTime DESC");
		return receiverInfoDao.selectList(wrapper);
	}
	
	
    /**
     * 新增收货人
     */
	@Override
	public void addReceiverInfo(ReceiverInfoEntity receiverInfoEntity) {
		receiverInfoDao.insert(receiverInfoEntity);
	}

	/**
	 * 修改收货人
	 */
	@Override
	public void updateReceiverInfo(ReceiverInfoEntity receiverInfoEntity) {
		receiverInfoDao.updateById(receiverInfoEntity);
	}

	/**
	 * 设置为默认收货人
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setReceiverDefault(Integer userSeq, Integer receiverInfoSeq) {
		//设置用户其他所有地址为非默认
		ReceiverInfoEntity receiverInfoEntity = new ReceiverInfoEntity();
		receiverInfoEntity.setIsDefault(0);
		Wrapper<ReceiverInfoEntity> wrapper = new EntityWrapper<ReceiverInfoEntity>();
		wrapper.where("User_Seq = {0}", userSeq);
		receiverInfoDao.update(receiverInfoEntity, wrapper);
		
		//设置为默认
		receiverInfoEntity = new ReceiverInfoEntity();
		receiverInfoEntity.setSeq(receiverInfoSeq);
		receiverInfoEntity.setIsDefault(1);
		receiverInfoDao.updateById(receiverInfoEntity);
	}

	/**
	 * 删除收货人
	 */
	@Override
	public void deleteReceiverInfo(Integer seq) {
		receiverInfoDao.deleteById(seq);
	}


	/**
	 * 根据seq获取收货人信息
	 */
	@Override
	public ReceiverInfoEntity getReceiverInfoBySeq(Integer receiverInfoSeq) {
		return receiverInfoDao.selectById(receiverInfoSeq);
	}

    
}
