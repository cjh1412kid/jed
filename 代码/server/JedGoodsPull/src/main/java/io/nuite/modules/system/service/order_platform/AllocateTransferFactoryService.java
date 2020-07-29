package io.nuite.modules.system.service.order_platform;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationEntity;
import io.nuite.modules.system.entity.order_platform.AllocateTransferFactoryEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AllocateTransferFactoryService extends IService<AllocateTransferFactoryEntity> {

	List<Map<String, Object>> getDate(String date);
	
	List<Map<String, Object>> getShoes(String date);
	
	List<Map<String, Object>> getTime(String date, Integer shoesSeq);
	
	Page  getRecords(String date, Integer shoesSeq, String time, Page page);
	
	void deleteRecords(String date, Integer shoesSeq, String time);
	
	AllocateTransferFactoryEntity getAllocateTransLast(Integer shoesSeq);
	
	void pushRecords(String date, Integer shoesSeq, String time, Integer userSeq, Page page, Integer companySeq);

	/**
	 * 调拨详情
	 * @param inShopSeq
	 * @param outShopSeq
	 * @param shoesSeq
	 * @param inputTime
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getApplicationDetailMapList(Integer inShopSeq, Integer outShopSeq, Integer shoesSeq, Date inputTime) throws Exception;

	
	Page  getAllShoes(String startDate, String endDate, String goodId, Page page, Integer companySeq);
	
	AllocateTransferTransferInApplicationEntity getallocateTransferTransferInApplication(Integer inShopSeq, Integer outShopSeq, Integer shoesSeq, Date InputTime);

	void insertAllocateTransferTransferInApplicationEntity(AllocateTransferTransferInApplicationEntity allocateTransferTransferInApplicationEntity);

	AllocateTransferFactoryEntity getNearOne();
}
