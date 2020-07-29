package io.nuite.modules.order_platform_app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.base.Joiner;

import io.nuite.modules.order_platform_app.dao.ShoesValuateDao;
import io.nuite.modules.order_platform_app.entity.ShoesValuateEntity;
import io.nuite.modules.order_platform_app.service.ShoesValuateService;

@Service
public class ShoesValuateServiceImpl implements ShoesValuateService {
    
    @Autowired
    private ShoesValuateDao shoesValuateDao;

    
    /**
     * 浏览记录总数
     */
	@Override
	public Integer getBrowseNum(Integer userSeq) {
		Wrapper<ShoesValuateEntity> wrapper = new EntityWrapper<ShoesValuateEntity>();
		wrapper.where("User_Seq = {0} AND IsBrowse = 0", userSeq);
		return shoesValuateDao.selectCount(wrapper);
	}
	
	
	/**
	 * 收藏记录总数
	 */
	@Override
	public Integer getCollectedNum(Integer userSeq) {
		Wrapper<ShoesValuateEntity> wrapper = new EntityWrapper<ShoesValuateEntity>();
		wrapper.where("User_Seq = {0} AND IsCollected = 1", userSeq);
		return shoesValuateDao.selectCount(wrapper);
	}
	
	
    
    /**
     * 根据用户序号，获取浏览信息列表
     */
	@Override
	public List<ShoesValuateEntity> getBrowseShoesValuateList(Integer userSeq, Integer start, Integer num) {
		Wrapper<ShoesValuateEntity> wrapper = new EntityWrapper<ShoesValuateEntity>();
		wrapper.where("User_Seq = {0} AND IsBrowse = 0", userSeq).orderBy("BrowseTime DESC");
		return shoesValuateDao.selectPage(new RowBounds(start - 1, num), wrapper);
	}
	
	
	
    /**
     * 根据用户序号，获取收藏信息列表
     */
	@Override
	public List<ShoesValuateEntity> getCollectedShoesValuateList(Integer userSeq, Integer start, Integer num) {
		Wrapper<ShoesValuateEntity> wrapper = new EntityWrapper<ShoesValuateEntity>();
		wrapper.where("User_Seq = {0} AND IsCollected = 1", userSeq).orderBy("CollectedTime DESC");
		return shoesValuateDao.selectPage(new RowBounds(start - 1, num), wrapper);
	}


	/**
	 * 根据鞋子序号列表获取鞋子信息
	 */
	@Override
	public List<Map<String, Object>> getShoesBySeqList(List<Integer> shoesSeqList) {
		String shoesSeqs = Joiner.on(",").join(shoesSeqList);
		return shoesValuateDao.getShoesBySeqs(shoesSeqs);
	}


	/**
	 * 删除浏览历史记录
	 */
	@Override
	public void deleteBrowseShoesValuate(List<Integer> seqList) {
		ShoesValuateEntity shoesValuateEntity = new ShoesValuateEntity();
		shoesValuateEntity.setIsBrowse(1);
		Wrapper<ShoesValuateEntity> wrapper = new EntityWrapper<ShoesValuateEntity>();
		wrapper.in("Seq", seqList);
		shoesValuateDao.update(shoesValuateEntity, wrapper);
	}


	/**
	 * 全部删除浏览历史记录
	 */
	@Override
	public void deleteAllBrowseShoesValuate(Integer userSeq) {
		ShoesValuateEntity shoesValuateEntity = new ShoesValuateEntity();
		shoesValuateEntity.setIsBrowse(1);
		Wrapper<ShoesValuateEntity> wrapper = new EntityWrapper<ShoesValuateEntity>();
		wrapper.where("User_Seq = {0}", userSeq);
		shoesValuateDao.update(shoesValuateEntity, wrapper);
		
	}


	/**
	 * 删除收藏
	 */
	@Override
	public void deleteCollectedShoesValuate(List<Integer> seqList) {
		ShoesValuateEntity shoesValuateEntity = new ShoesValuateEntity();
		shoesValuateEntity.setIsCollected(0);
		Wrapper<ShoesValuateEntity> wrapper = new EntityWrapper<ShoesValuateEntity>();
		wrapper.in("Seq", seqList);
		shoesValuateDao.update(shoesValuateEntity, wrapper);
	}


	/**
	 * 全部删除收藏
	 */
	@Override
	public void deleteAllCollectedShoesValuate(Integer userSeq) {
		ShoesValuateEntity shoesValuateEntity = new ShoesValuateEntity();
		shoesValuateEntity.setIsCollected(0);
		Wrapper<ShoesValuateEntity> wrapper = new EntityWrapper<ShoesValuateEntity>();
		wrapper.where("User_Seq = {0}", userSeq);
		shoesValuateDao.update(shoesValuateEntity, wrapper);
	}


	/**
	 * 根据鞋子序号删除所有 评价、收藏、浏览历史 记录
	 */
	@Override
	public void deleteShoesValuateByShoesSeq(Integer shoesSeq) {
		Wrapper<ShoesValuateEntity> wrapper = new EntityWrapper<ShoesValuateEntity>();
		wrapper.where("Shoes_Seq = {0}", shoesSeq);
		shoesValuateDao.delete(wrapper);
	}

    /**
     * 浏览记录
     */
	@Override
	public List<Map<String, Object>> getBrowseShoesList(Page<ShoesValuateEntity> page,BaseUserEntity baseUserEntity) {
		Map<String,Object> map = new HashMap<>(10);
		map.put("userSeq",baseUserEntity.getSeq());
		map.put("shopSeq",baseUserEntity.getShopSeq());
		List<Map<String, Object>> list=shoesValuateDao.getBrowseList(page,map);
		return list;
	}


	@Override
	public List<Map<String, Object>> getCollectedList(Page<ShoesValuateEntity> page,BaseUserEntity baseUserEntity) {
		Map<String,Object> map = new HashMap<>(10);
		map.put("userSeq",baseUserEntity.getSeq());
		map.put("shopSeq",baseUserEntity.getShopSeq());
		List<Map<String, Object>> list=shoesValuateDao.getCollectedList(page,map);
		return list;
	}



	

   

    
}
