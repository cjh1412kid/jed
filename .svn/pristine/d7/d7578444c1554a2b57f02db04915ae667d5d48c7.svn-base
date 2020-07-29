package io.nuite.modules.order_platform_app.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.order_platform_app.entity.ShoesValuateEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;

public interface ShoesValuateService {
	
	Integer getBrowseNum(Integer userSeq);
	
	Integer getCollectedNum(Integer userSeq);
	
	List<ShoesValuateEntity> getBrowseShoesValuateList(Integer userSeq, Integer start, Integer num);

	List<ShoesValuateEntity> getCollectedShoesValuateList(Integer userSeq, Integer start, Integer num);

	List<Map<String, Object>> getShoesBySeqList(List<Integer> shoesSeqList);

	/**
	 * 获取浏览记录
	 * @param page
	 * @param baseUserEntity
	 * @return
	 */
	List<Map<String, Object>> getBrowseShoesList(Page<ShoesValuateEntity> page,BaseUserEntity baseUserEntity);

	/**
	 * 获取收藏
	 * @param page
	 * @param baseUserEntity
	 * @return
	 */
	List<Map<String, Object>> getCollectedList(Page<ShoesValuateEntity> page,BaseUserEntity baseUserEntity);
	

	void deleteBrowseShoesValuate(List<Integer> seqList);

	void deleteAllBrowseShoesValuate(Integer userSeq);

	void deleteCollectedShoesValuate(List<Integer> seqList);

	void deleteAllCollectedShoesValuate(Integer userSeq);

	void deleteShoesValuateByShoesSeq(Integer shoesSeq);

}
