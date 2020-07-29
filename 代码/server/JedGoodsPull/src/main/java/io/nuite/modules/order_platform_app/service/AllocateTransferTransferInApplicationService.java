package io.nuite.modules.order_platform_app.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationEntity;

import java.util.List;
import java.util.Map;

/**
 * @description: 调入申请表Service
 * @author: jxj
 * @create: 2019-12-03 14:25
 */
public interface AllocateTransferTransferInApplicationService extends IService<AllocateTransferTransferInApplicationEntity> {

	List<Map<String, Object>> getDayTransferInOutList(Integer userShopSeq, Integer type, Integer state, String dateStr);

}
