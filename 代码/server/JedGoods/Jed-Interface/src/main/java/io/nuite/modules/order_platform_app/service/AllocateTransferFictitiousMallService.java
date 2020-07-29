package io.nuite.modules.order_platform_app.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import io.nuite.modules.order_platform_app.entity.AllocateTransferFictitiousMallEntity;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;


public interface AllocateTransferFictitiousMallService extends IService<AllocateTransferFictitiousMallEntity> {

	List<Map<String, Object>> getShoesStockAndTransferOutNum(Integer shopSeq, Integer shoesSeq) throws Exception;

	void confirmTransferOut(Integer shopSeq, Integer shoesSeq, String sizeTransferOutNum);

	List<Map<String, Object>> getAllShopsTransferOutNumAndStock(Integer shoesSeq, Integer shopRegionSeq,Integer shopSeq);

	void confirmTransferIn(Integer inShopSeq, Integer inUserSeq, Integer shoesSeq, String sizesShopsTransferInNum);

	List<MessageEntity> getMyTransferMessage(Integer userSeq, Integer type, Page<Map<String,Object>> page);

	List<Map<String, Object>> getApplicationDetailMapList(Integer applicationSeq);

	void handleTransferApplication(Integer userSeq, Integer shopSeq, Integer applicationSeq, Integer state,String erpOrderNum,Integer expressCompanySeq, String expressNum, String remark);

	/**
	 * 获取获取调出货品的门店及尺码数量
	 * @param shoesSeq
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectExportShop(Integer shoesSeq) throws Exception;

	/**
	 * 取消调出
	 * @param baseUserEntity
	 * @param messageSeq
	 * @throws Exception
	 */
	void cancelExport(BaseUserEntity baseUserEntity,Integer messageSeq) throws Exception;

	/**
	 * 门店是否已将鞋子放入虚拟商城
	 * @param shopSeq
	 * @param shoesSeq
	 * @return
	 */
	boolean isTransferOutToFictitiousMall(Integer shopSeq, Integer shoesSeq);

	boolean isAutoTransferOut(Integer shopSeq, Integer shoesSeq);
	
	void cancelTransferOut(Integer shopSeq, Integer shoesSeq);
	
}

