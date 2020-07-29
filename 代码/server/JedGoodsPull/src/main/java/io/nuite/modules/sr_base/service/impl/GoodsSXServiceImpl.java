package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.GoodsSXDao;
import io.nuite.modules.sr_base.entity.GoodsSXEntity;
import io.nuite.modules.sr_base.service.GoodsSXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: yangchuang
 * @Date: 2018/8/6 15:46
 * @Version: 1.0
 * @Description:
 */

@Service
public class GoodsSXServiceImpl extends ServiceImpl<GoodsSXDao, GoodsSXEntity> implements GoodsSXService {

    @Autowired
    private GoodsSXDao goodsSXDao;

    @Override
    public List<GoodsSXEntity> getGoodsSXListByCompanySeq(Integer companySeq) {
        Wrapper<GoodsSXEntity> wrapper = new EntityWrapper<GoodsSXEntity>();
        wrapper.where("Company_Seq = {0}", companySeq);
        return goodsSXDao.selectList(wrapper);
    }

    @Override
    public List<GoodsSXEntity> getGoodsSXListByCompanySeqAndVisibled(Integer companySeq) {
        return goodsSXDao.selectList(new EntityWrapper<GoodsSXEntity>()
                .eq("Company_Seq",companySeq)
                .eq("Visible",0));
    }

    @Override
    public List<GoodsSXEntity> getGoodsSXContainOptionByCompanySeq(Integer companySeq) {
        return goodsSXDao.selectListContainOption(companySeq);
    }

	@Override
	public List<Map<String, Object>> selectSXIdNameByCompanySeq(Integer companySeq) {
        Wrapper<GoodsSXEntity> wrapper = new EntityWrapper<GoodsSXEntity>();
        wrapper.setSqlSelect("SXID AS id, '属性:' + SXName AS name").where("Company_Seq = {0}", companySeq);
		return goodsSXDao.selectMaps(wrapper);
	}
}
