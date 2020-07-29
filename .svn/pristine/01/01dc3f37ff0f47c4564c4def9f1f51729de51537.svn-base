package io.nuite.modules.system.service.order_platform;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.OrderStatisticsModelEntity;

import java.util.List;

public interface OrderStatisticsModelService extends IService<OrderStatisticsModelEntity> {

	List<OrderStatisticsModelEntity> getOrderStatisticsModel(Integer companySeq);

    Page<OrderStatisticsModelEntity> getOrderStatisticsModelPage(Integer companySeq, Integer pageNum, Integer pageSize);

	boolean modelNameExisted(Integer seq, Integer companySeq, String modelName);

	Integer addOrderStatisticsModel(OrderStatisticsModelEntity orderStatisticsModelEntity);

	void updateOrderStatisticsModel(OrderStatisticsModelEntity orderStatisticsModelEntity);

	void deleteOrderStatisticsModel(Integer seq);

}
