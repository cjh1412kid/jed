package io.nuite.modules.sr_base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.erp.utils.ErpDataVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.service.BaseShopService;

@Service
public class BaseShopServiceImpl extends ServiceImpl<BaseShopDao, BaseShopEntity> implements BaseShopService {

	
    @Autowired
    private BaseShopDao baseShopDao;
	
	/**
	 * 根据公司序号获取所有门店序号
	 */
	@Override
	public List<Object> getShopSeqsByCompanySeq(Integer companySeq) {
        Wrapper<BaseShopEntity> wrapper = new EntityWrapper<BaseShopEntity>();
        wrapper.setSqlSelect("Seq").where("Company_Seq = {0}", companySeq);
		return baseShopDao.selectObjs(wrapper);
	}
	
	
	/**
	 * 根据公司序号获取所有门店
	 */
	@Override
	public List<BaseShopEntity> getShopsByCompanySeq(Integer companySeq) {
        Wrapper<BaseShopEntity> wrapper = new EntityWrapper<BaseShopEntity>();
        wrapper.where("Company_Seq = {0}", companySeq);
		BaseShopEntity baseShopEntity = new BaseShopEntity();
		baseShopEntity.setSeq(0);
		baseShopEntity.setName("总仓");
		List<BaseShopEntity> baseShopEntities = baseShopDao.selectList(wrapper);
		baseShopEntities.add(baseShopEntity);
		return baseShopEntities;
	}



	@Override
	public List<Integer> getAllShopSeqs(Integer brandSeq) {
		return baseShopDao.selectShopSeqs(brandSeq);
	}
	

}
