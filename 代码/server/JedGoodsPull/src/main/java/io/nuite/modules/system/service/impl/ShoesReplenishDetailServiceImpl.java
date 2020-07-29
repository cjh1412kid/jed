package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.dao.ShoesReplenishDetailDao;
import io.nuite.modules.system.entity.ShoesReplenishDetailEntity;
import io.nuite.modules.system.service.ShoesReplenishDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoesReplenishDetailServiceImpl extends ServiceImpl<ShoesReplenishDetailDao, ShoesReplenishDetailEntity> implements ShoesReplenishDetailService {
    
	@Autowired
    private ShoesReplenishDetailDao shoesReplenishDetailDao;
    @Autowired
    private GoodsShoesService goodsShoesService;
    
    @Autowired
    private ConfigUtils configUtils;
    
    @Autowired
    private GoodsSizeDao goodsSizeDao;

	@Override
	public List<ShoesReplenishDetailEntity> getListByReplenishSeq(Integer replenishSeq) {
		Wrapper<ShoesReplenishDetailEntity> wrapper=new EntityWrapper<ShoesReplenishDetailEntity>();
		wrapper.where("ReplenishSeq={0}", replenishSeq);
		wrapper.orderBy("Size_Seq");
		 List<ShoesReplenishDetailEntity> list=shoesReplenishDetailDao.selectList(wrapper);
		 for (ShoesReplenishDetailEntity shoesReplenishDetailEntity : list) {
			Integer sizeSeq=shoesReplenishDetailEntity.getSizeSeq();
			GoodsSizeEntity goodsSizeEntity=goodsSizeDao.selectById(sizeSeq);
			shoesReplenishDetailEntity.setSizeName(goodsSizeEntity.getSizeName());
		}
		return list;
	}
    
   


}
