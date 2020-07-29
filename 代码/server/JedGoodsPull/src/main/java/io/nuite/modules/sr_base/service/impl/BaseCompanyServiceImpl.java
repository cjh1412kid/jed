package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.BaseCompanyDao;
import io.nuite.modules.sr_base.entity.BaseCompanyEntity;
import io.nuite.modules.sr_base.service.BaseCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BaseCompanyServiceImpl extends ServiceImpl<BaseCompanyDao, BaseCompanyEntity> implements BaseCompanyService {

    @Autowired
    private BaseCompanyDao baseCompanyDao;
    
	/**
	 * 根据seq获取公司信息指定字段
	 */
	@Override
	public Map<String, Object> getCompanyMapBySeq(Integer companySeq) {
		Wrapper<BaseCompanyEntity> wrapper = new EntityWrapper<BaseCompanyEntity>();
		wrapper.setSqlSelect("Name AS companyName, Address AS companyAddress, Remark AS companyRemark").where("Seq = {0} ", companySeq);
		return baseCompanyDao.selectMaps(wrapper).get(0);
	}
}
