package io.nuite.modules.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

import io.nuite.modules.system.entity.ShoesReplenishDetailEntity;

public interface ShoesReplenishDetailService extends IService<ShoesReplenishDetailEntity> {
    
  List<ShoesReplenishDetailEntity> getListByReplenishSeq(Integer replenishSeq);

}

