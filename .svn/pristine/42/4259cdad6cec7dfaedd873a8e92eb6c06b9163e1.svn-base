package io.nuite.modules.sr_base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.sr_base.dao.GoodsSXOptionDao;
import io.nuite.modules.sr_base.entity.GoodsSXOptionEntity;
import io.nuite.modules.sr_base.service.GoodsSXOptionService;

/**
 * @Author: yangchuang
 * @Date: 2018/8/6 15:43
 * @Version: 1.0
 * @Description:
 */

@Service
public class GoodsSXOptionServiceImpl extends ServiceImpl<GoodsSXOptionDao, GoodsSXOptionEntity> implements GoodsSXOptionService {

	@Autowired
	private GoodsSXOptionDao goodsSXOptionDao;
	
	
	@Override
	public List<GoodsSXOptionEntity> getGoodsSXOptionListBySXSeq(Integer seq) {
		Wrapper<GoodsSXOptionEntity> wrapper = new EntityWrapper<GoodsSXOptionEntity>();
		wrapper.where("SX_Seq = {0} ", seq);
		return goodsSXOptionDao.selectList(wrapper);
	}
}
