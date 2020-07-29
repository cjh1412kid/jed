package io.nuite.modules.system.service.order_platform;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.SplitOrderModelDetailEntity;
import io.nuite.modules.order_platform_app.entity.SplitOrderModelEntity;

import java.util.List;
import java.util.Map;

public interface SplitOrderModelService extends IService<SplitOrderModelEntity> {

	List<SplitOrderModelEntity> getSplitOrderModel(Integer companySeq);
	
	SplitOrderModelEntity getDefaultSplitOrderModel(Integer companySeq);

    Page<SplitOrderModelEntity> getSplitOrderModelPage(Integer companySeq, Integer pageNum, Integer pageSize);

	boolean modelNameExisted(Integer seq, Integer companySeq, String modelName);

	Integer addSplitOrderModel(SplitOrderModelEntity splitOrderModelEntity);

	void setIsNotDefaultExceptSeq(Integer companySeq, Integer seq);

	void updateSplitOrderModel(SplitOrderModelEntity splitOrderModelEntity);

	void deleteSplitOrderModel(Integer seq);

	List<SplitOrderModelDetailEntity> getModelDetailByModelSeq(Integer modelSeq);

	List<Map<String, Object>> getModelDetailMap(Integer companySeq, Integer modelSeq) throws Exception;

	Integer addSplitOrderModelDetail(SplitOrderModelDetailEntity splitOrderModelDetailEntity);

	Integer updateSplitOrderModelDetail(SplitOrderModelDetailEntity splitOrderModelDetailEntity);

	void deleteSplitOrderModelDetail(Integer seq);
}
