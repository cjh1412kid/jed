package io.nuite.modules.order_platform_app.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.nuite.modules.order_platform_app.entity.SievePlateEntity;


public interface SievePlateService extends IService<SievePlateEntity> {

	List<Map<String, Object>> getSievePlateList(Integer companySeq, Integer year, Integer seasonSeq);

	List<Map<String, Object>> getSievePlateDetail(Integer companySeq, Integer year, Integer seasonSeq, Integer categorySeq);

}

