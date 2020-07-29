package io.nuite.modules.order_platform_app.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.order_platform_app.dao.ShoesDataDailyDetailDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.service.ShoesDataDailyDetailService;


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
