package io.nuite.modules.order_platform_app.service;

import java.util.Date;

import com.baomidou.mybatisplus.service.IService;

import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;


public interface ShoesDataDailyDetailService extends IService<ShoesDataDailyDetailEntity> {
	
	ShoesDataDailyDetailEntity getNearOne(Integer shoesSeq,Integer shopSeq);
	
	
	ShoesDataDailyDetailEntity getOldOne(Integer shoesSeq);
}

