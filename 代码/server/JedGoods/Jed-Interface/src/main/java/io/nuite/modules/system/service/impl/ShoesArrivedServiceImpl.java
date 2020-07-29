package io.nuite.modules.system.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.Query;
import io.nuite.modules.system.dao.ShoesArrivedDao;
import io.nuite.modules.system.entity.ShoesArrivedEntity;
import io.nuite.modules.system.service.ShoesArrivedService;


@Service
public class ShoesArrivedServiceImpl extends ServiceImpl<ShoesArrivedDao, ShoesArrivedEntity> implements ShoesArrivedService {

	@Autowired
	private ShoesArrivedDao shoesArrivedDao;
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ShoesArrivedEntity> page = this.selectPage(
                new Query<ShoesArrivedEntity>(params).getPage(),
                new EntityWrapper<ShoesArrivedEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public List<ShoesArrivedEntity> getListByReplenishSeq(Integer replenishSeq) {
		  Wrapper<ShoesArrivedEntity> wrapper = new EntityWrapper<ShoesArrivedEntity>(); 
		  wrapper.where("Replenish_Seq = {0}", replenishSeq).orderBy("InputTime", false);
		List<ShoesArrivedEntity> arriveds=shoesArrivedDao.selectList(wrapper);
		return arriveds;
	}

}
