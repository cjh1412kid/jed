package io.nuite.modules.order_platform_app.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.order_platform_app.dao.SievePlateDao;
import io.nuite.modules.order_platform_app.entity.SievePlateEntity;
import io.nuite.modules.order_platform_app.service.SievePlateService;

@Service
public class SievePlateServiceImpl extends ServiceImpl<SievePlateDao, SievePlateEntity> implements SievePlateService {

    @Autowired
    private SievePlateDao sievePlateDao;
	
    
	/**
	 * 查询筛板列表
	 */
	@Override
	public List<Map<String, Object>> getSievePlateList(Integer companySeq, Integer year, Integer seasonSeq) {
		Wrapper<SievePlateEntity> wrapper = new EntityWrapper<SievePlateEntity>();
		wrapper.setSqlSelect("Category_Seq AS categorySeq, COUNT (DISTINCT OrderShoes_Seq) AS num")
		.where("Company_Seq = {0} AND Year = {1} AND Season_Seq = {2}", companySeq, year, seasonSeq)
		.groupBy("Category_Seq")
		.orderBy("num DESC");
		List<Map<String, Object>>  list = sievePlateDao.selectMaps(wrapper);
		return list;
	}


	
	/**
	 * 查询筛板详情
	 */
	@Override
	public List<Map<String, Object>> getSievePlateDetail(Integer companySeq, Integer year, Integer seasonSeq, Integer categorySeq) {
		Wrapper<SievePlateEntity> wrapper = new EntityWrapper<SievePlateEntity>();
		wrapper.setSqlSelect("GoodID AS goodId, COUNT (1) AS num")
		.where("Company_Seq = {0} AND Year = {1} AND Season_Seq = {2} AND Category_Seq = {3}", companySeq, year, seasonSeq, categorySeq)
		.groupBy("GoodID")
		.orderBy("num DESC");
		List<Map<String, Object>>  list = sievePlateDao.selectMaps(wrapper);
		return list;
	}

}
