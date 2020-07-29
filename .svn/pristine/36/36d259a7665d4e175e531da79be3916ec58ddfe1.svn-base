package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.dao.ShoesDataDailyDetailDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.service.ShoesDataDailyDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoesDataDailyDetailServiceImpl extends ServiceImpl<ShoesDataDailyDetailDao, ShoesDataDailyDetailEntity> implements ShoesDataDailyDetailService {

	@Autowired
	private ShoesDataDailyDetailDao shoesDataDailyDetailDao;
	
	@Override
	public ShoesDataDailyDetailEntity getNearOne(Integer shoesSeq, Integer shopSeq) {
		
		return shoesDataDailyDetailDao.getNearOne(shoesSeq, shopSeq);
	}

	@Override
	public ShoesDataDailyDetailEntity getOldOne(Integer shoesSeq) {
		return shoesDataDailyDetailDao.getOldOne(shoesSeq);
	}

	
}
