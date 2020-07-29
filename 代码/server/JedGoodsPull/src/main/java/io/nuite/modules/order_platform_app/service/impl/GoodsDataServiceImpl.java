package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.order_platform_app.dao.GoodsDataDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.dao.ShoesValuateDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.order_platform_app.entity.ShoesValuateEntity;
import io.nuite.modules.order_platform_app.service.GoodsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GoodsDataServiceImpl implements GoodsDataService {

	@Autowired
	private GoodsDataDao goodsDataDao;
	
    @Autowired
    private ShoesValuateDao shoesValuateDao;
    
    @Autowired
    private ShoesDataDao shoesDataDao;

	
	/**
	 * 获取商品评价列表
	 */
	@Override
	public List<Map<String, Object>> getAllEvaluate(Integer seq, Integer start, Integer num) {
		return goodsDataDao.getAllEvaluate(seq, start, num);
	}

	
	/**
	 * 获取该用户对该商品评分、收藏、建议
	 */
	@Override
	public ShoesValuateEntity getUserShoesValuate(Integer userSeq, Integer shoesSeq) {
		ShoesValuateEntity shoesValuateEntity = new ShoesValuateEntity();
		shoesValuateEntity.setUserSeq(userSeq);
		shoesValuateEntity.setShoesSeq(shoesSeq);
		return shoesValuateDao.selectOne(shoesValuateEntity);
	}

	
	/**
	 * 修改鞋子评分、收藏、建议
	 */
	@Override
	public void updateShoesValuate(Integer userSeq, Integer shoesSeq, ShoesValuateEntity shoesValuateEntity) {
		Wrapper<ShoesValuateEntity> wrapper = new EntityWrapper<ShoesValuateEntity>();
		wrapper.where("User_Seq = {0} AND Shoes_Seq = {1}", userSeq, shoesSeq);
		shoesValuateDao.update(shoesValuateEntity, wrapper);
	}
	

	/**
	 * 添加鞋子搜索次数
	 * @param seq
	 */
	@Override
	public void addShoesSearchTimes(Integer seq) {
		goodsDataDao.addShoesSearchTimes(seq);
	}

	
	/**
	 * 添加或更新鞋子浏览记录
	 * @param userSeq
	 * @param seq
	 */
	@Override
	public void addOrUpdateShoesBrowseRecord(Integer userSeq, Integer shoesSeq) {
		//查询是否已存在记录
		ShoesValuateEntity shoesValuateEntity = new ShoesValuateEntity();
		shoesValuateEntity.setUserSeq(userSeq);
		shoesValuateEntity.setShoesSeq(shoesSeq);
		shoesValuateEntity.setDel(0);
		ShoesValuateEntity selectedEntity = shoesValuateDao.selectOne(shoesValuateEntity);
		
		//根据查询记录更新或修改
		if(selectedEntity == null) {
			shoesValuateEntity.setIsBrowse(0);
			shoesValuateEntity.setBrowseTime(new Date());
			shoesValuateDao.insert(shoesValuateEntity);
		} else {
			shoesValuateEntity.setSeq(selectedEntity.getSeq());
			shoesValuateEntity.setIsBrowse(0);
			shoesValuateEntity.setBrowseTime(new Date());
			shoesValuateDao.updateById(shoesValuateEntity);
		}

	}


	/**
	 * 获取总库存
	 */
	@Override
	public Integer getStockQuantity(Integer shoesSeq) {
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
		wrapper.setSqlSelect("SUM(Stock) AS Stock").where("Shoes_Seq = {0}", shoesSeq);
		Integer stock = (Integer) shoesDataDao.selectObjs(wrapper).get(0);
		if(stock == null) {
			stock = 0;
		}
		return stock;
	}


}
