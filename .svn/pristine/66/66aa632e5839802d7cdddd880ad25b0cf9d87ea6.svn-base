package io.nuite.modules.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import io.nuite.common.utils.PageUtils;
import io.nuite.modules.system.entity.ShoesReplenishEntity;

public interface ShoesReplenishService extends IService<ShoesReplenishEntity> {
    
    /**
     * 解析补货计划excel
     *
     * @param file
     * @param companySeq
     */
    void parseExcel(MultipartFile file, Integer companySeq) throws Exception;
    
    /**
     * 查询补货计划
     *
     * @param shoeSeq
     * @return
     */
    List<ShoesReplenishEntity> queryReplenishPlan(Integer shoeSeq);
    
    /**
	 * 获取补单到货时间
	 * @param page
	 * @param baseUserEntity
	 * @return
	 */
	List<Map<String, Object>> getShoesReplenishList(Integer shoesSeq);
	
	List<Map<String, Object>> getReplenishList(Integer companySeq);
    
	/**
	 * 获取补单列表
	 * @return
	 */
	List<ShoesReplenishEntity> getSupplementList(Integer companySeq);
	
	List<ShoesReplenishEntity> getHaveSupplementList(Integer companySeq);
	
	List<ShoesReplenishEntity> getStoresSupplementList(Integer shopSeq);
	
	List<ShoesReplenishEntity> getStoresHaveSupplementList(Integer shopSeq);
	
	List<String> getDaysList(Integer companySeq);
	
	PageUtils getGoodsList(Page page,String replenishTime,Integer companySeq);

	List<Map<String, Object>> getGoodsSeqs(List<Integer> yearList,List<Integer> seasonSeqList,List<Integer> categorySeqList,Integer companySeq);
	
	List<Object> getSupplementShoesSeqList(Integer companySeq);

	Map<String, Object> getTotalNum(Integer shoesSeq);
}

