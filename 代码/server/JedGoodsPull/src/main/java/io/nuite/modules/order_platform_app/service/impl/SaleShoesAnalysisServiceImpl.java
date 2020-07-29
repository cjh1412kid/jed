package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.base.Joiner;
import io.nuite.modules.order_platform_app.dao.SaleShoesAnalysisDao;
import io.nuite.modules.order_platform_app.dao.SaleShoesDetailDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDailyDetailDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.order_platform_app.service.SaleShoesAnalysisService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SaleShoesAnalysisServiceImpl implements SaleShoesAnalysisService {

    @Autowired
    private SaleShoesAnalysisDao saleShoesAnalysisDao;
    
    @Autowired
    private SaleShoesDetailDao saleShoesDetailDao;
    
    @Autowired
    private ShoesDataDailyDetailDao shoesDataDailyDetailDao;
    
    @Autowired
    private ShoesDataDao shoesDataDao;
    
    
    
    
	
    /**
     * 总部各个货品销量排行
     */
	@Override
	public List<Map<String, Object>> getHqShoesRankList(List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd, Integer orderBy, Integer orderDir, Integer start,
			Integer num) {
		
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        
        String shoesSeqs = Joiner.on(",").join(shoesSeqList);
        
		List<Map<String, Object>> list = saleShoesAnalysisDao.getHqShoesRankList(page, shoesSeqs, saleTimeStart, saleTimeEnd, orderBy, orderDir);
		return list;
	}

	
	
	
	/**
	 * 门店各个货品销量排行
	 */
	@Override
	public List<Map<String, Object>> getShopShoesRankList(Integer shopSeq, List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd, Integer orderBy, Integer orderDir,
			Integer start, Integer num) {
		
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        
        String shoesSeqs = Joiner.on(",").join(shoesSeqList);
        
		List<Map<String, Object>> list = saleShoesAnalysisDao.getShopShoesRankList(page, shopSeq, shoesSeqs, saleTimeStart, saleTimeEnd, orderBy, orderDir);
		return list;
		
	}




	/**
	 *  门店销量排行
	 */
	@Override
	public List<Map<String, Object>> getShopRankList(Integer companySeq, Date saleTimeStart, Date saleTimeEnd, Integer orderBy,
			Integer orderDir, Integer start, Integer num) {
		
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        
		List<Map<String, Object>> list = saleShoesAnalysisDao.getShopRankList(page, companySeq, saleTimeStart, saleTimeEnd, orderBy, orderDir);
		return list;
		
	}




	/**
	 * 总部时间段内某些货号的总销量
	 */
	@Override
	public Integer getHqTotalSaleNum(List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd) {
		Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
		wrapper.setSqlSelect("SUM (SaleCount)").in("ShoeSeq", shoesSeqList)
		.where("SaleDate >= {0} AND SaleDate <= {1} ", saleTimeStart, saleTimeEnd);
		
		List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}




	/**
	 * 总部时间段内某些货号的总平均售价
	 */
	@Override
	public BigDecimal getHqTotalAvgSalePrice(List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd) {
		Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
		wrapper.setSqlSelect("SUM (SaleCount * RealPrice) / NULLIF(SUM (SaleCount),0) AS avgPrice").in("ShoeSeq", shoesSeqList)
		.where("SaleDate >= {0} AND SaleDate <= {1} ", saleTimeStart, saleTimeEnd);
		
		List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return (BigDecimal) list.get(0);
		} else {
			return BigDecimal.ZERO;
		}
	}




	/**
	 * 总部时间段内某些货号的总进货量
	 */
	@Override
	public Integer getHqTotalInNum(List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd) {
		Wrapper<ShoesDataDailyDetailEntity> wrapper = new EntityWrapper<ShoesDataDailyDetailEntity>();
		wrapper.setSqlSelect("SUM (Count)").in("Shoes_Seq", shoesSeqList)
		.where("(Type = 0 OR Type = 1)");
		
		List<Object> list = shoesDataDailyDetailDao.selectObjs(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}




	/**
	 * 总部某些货号的总库存
	 */
	@Override
	public Integer getHqTotalStockNum(List<Integer> shoesSeqList) {
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
		wrapper.setSqlSelect("SUM (Stock)").in("Shoes_Seq", shoesSeqList);
		List<Object> list = shoesDataDao.selectObjs(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}




	/**
	 * 门店时间段内某些货号的总销量
	 */
	@Override
	public Integer getShopTotalSaleNum(Integer shopSeq, List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd) {
		Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
		wrapper.setSqlSelect("SUM (SaleCount)").in("ShoeSeq", shoesSeqList)
		.where("ShopSeq = {0} AND SaleDate >= {1} AND SaleDate <= {2} ", shopSeq, saleTimeStart, saleTimeEnd);
		
		List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}




	/**
	 * 门店时间段内某些货号的总平均售价
	 */
	@Override
	public BigDecimal getShopTotalAvgSalePrice(Integer shopSeq, List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd) {
		Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
		wrapper.setSqlSelect("SUM (SaleCount * RealPrice) / NULLIF(SUM (SaleCount),0) AS avgPrice").in("ShoeSeq", shoesSeqList)
		.where("ShopSeq = {0} AND SaleDate >= {1} AND SaleDate <= {2} ", shopSeq, saleTimeStart, saleTimeEnd);
		
		List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return (BigDecimal) list.get(0);
		} else {
			return BigDecimal.ZERO;
		}
		
	}




	/**
	 * 门店时间段内某些货号的总进货量
	 */
	@Override
	public Integer getShopTotalInNum(Integer shopSeq, List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd) {
		Wrapper<ShoesDataDailyDetailEntity> wrapper = new EntityWrapper<ShoesDataDailyDetailEntity>();
		wrapper.setSqlSelect("SUM (Count)").in("Shoes_Seq", shoesSeqList)
		.where("Shop_Seq = {0} AND Type = 1", shopSeq);
		
		List<Object> list = shoesDataDailyDetailDao.selectObjs(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}




	/**
	 * 门店某些货号的总库存
	 */
	@Override
	public Integer getShopTotalStockNum(Integer shopSeq, List<Integer> shoesSeqList) {
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
		wrapper.setSqlSelect("SUM (Stock)").in("Shoes_Seq", shoesSeqList)
		.where("Shop_Seq = {0}", shopSeq);
		List<Object> list = shoesDataDao.selectObjs(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}



	@Override
	public List<Map<String, Object>> getHqShoesRankListByParam(List<Integer> shoesSeqList, List<Integer> shopSeqList,
															   Date saleTimeStart, Date saleTimeEnd, Integer orderBy, Integer orderDir, Integer start, Integer num,
															   BigDecimal startPrice, BigDecimal endPrice, BaseUserEntity loginUser) {
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
     
        String shoesSeqs = Joiner.on(",").join(shoesSeqList);
        String shopSeqs="";
        if(shopSeqList!=null) {
        	   shopSeqs = Joiner.on(",").join(shopSeqList);
        }
     
		List<Map<String, Object>> list = saleShoesAnalysisDao.getHqShoesAnalysisList(page, shopSeqs, shoesSeqs, startPrice, endPrice, saleTimeStart, saleTimeEnd, orderBy, orderDir,loginUser.getShopSeq());
		return list;
	}


	@Override
	public Integer getTotalSaleNum(List<Integer> shopSeqList, List<Integer> shoesSeqList, Date saleTimeStart,
			Date saleTimeEnd,BigDecimal startPrice,BigDecimal endPrice) {
		Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
		if(shoesSeqList == null || shoesSeqList.size() == 0) {
			return 0;
		}
		String shoesSeqs = "(" + Joiner.on(",").join(shoesSeqList) + ")";
		wrapper.setSqlSelect("SUM (SaleCount)").where("ShoeSeq IN " + shoesSeqs);
		if(shopSeqList!=null) {
			wrapper.in("ShopSeq", shopSeqList);
		}
		wrapper.where("SaleDate >= {0} AND SaleDate <= {1} ", saleTimeStart, saleTimeEnd);
		if(startPrice!=null) {
			wrapper.where("RealPrice >= {0}  ", startPrice);
		}
		if(endPrice!=null) {
			wrapper.where("RealPrice < {0} ", endPrice);
		}
	
		
		List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return (Integer) list.get(0);
		} else {
			return 0;
		}
	}




	@Override
	public BigDecimal getTotalSalePrice(List<Integer> shopSeqList, List<Integer> shoesSeqList, Date saleTimeStart,
			Date saleTimeEnd,BigDecimal startPrice,BigDecimal endPrice) {
		Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
		if(shoesSeqList == null || shoesSeqList.size() == 0) {
			return BigDecimal.ZERO;
		}
		String shoesSeqs = "(" + Joiner.on(",").join(shoesSeqList) + ")";
		wrapper.setSqlSelect("SUM( RealPrice)").where("ShoeSeq IN " + shoesSeqs);
		if(shopSeqList!=null) {
			wrapper.in("ShopSeq", shopSeqList);
		}
		wrapper.where("SaleDate >= {0} AND SaleDate <= {1} ", saleTimeStart, saleTimeEnd);
		if(startPrice!=null) {
			wrapper.where("RealPrice >= {0}  ", startPrice);
		}
		if(endPrice!=null) {
			wrapper.where("RealPrice < {0} ", endPrice);
		}
		List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
		if(list != null && list.size() > 0 && list.get(0) != null) {
			return (BigDecimal) list.get(0);
		} else {
			return BigDecimal.ZERO;
		}
	}




	@Override
	public Integer getTotalSaleGood(List<Integer> shopSeqList, List<Integer> shoesSeqList, Date saleTimeStart,
			Date saleTimeEnd, BigDecimal startPrice, BigDecimal endPrice) {
		Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
		if(shoesSeqList == null || shoesSeqList.size() == 0) {
			return 0;
		}
		String shoesSeqs = "(" + Joiner.on(",").join(shoesSeqList) + ")";
		wrapper.setSqlSelect("count(ShoeSeq)").where("ShoeSeq IN " + shoesSeqs);
		if(shopSeqList!=null) {
			wrapper.in("ShopSeq", shopSeqList);
		}
		wrapper.where("SaleDate >= {0} AND SaleDate <= {1} ", saleTimeStart, saleTimeEnd);
		if(startPrice!=null) {
			wrapper.where("RealPrice >= {0}  ", startPrice);
		}
		if(endPrice!=null) {
			wrapper.where("RealPrice < {0} ", endPrice);
		}
		wrapper.groupBy("ShoeSeq");
		List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
		if(list != null ) {
			return  list.size();
		} else {
			return 0;
		}
	}




	@Override
	public List<Map<String, Object>> getHqShoesRankListByMoney(List<Integer> shoesSeqList, List<Integer> shopSeqList,
			Date saleTimeStart, Date saleTimeEnd, Integer orderBy, Integer orderDir, Integer start, Integer num,
			BigDecimal startPrice, BigDecimal endPrice, BaseUserEntity loginUser) {
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
     
        String shoesSeqs = Joiner.on(",").join(shoesSeqList);
        String shopSeqs="";
        if(shopSeqList!=null) {
        	   shopSeqs = Joiner.on(",").join(shopSeqList);
        }
     
		List<Map<String, Object>> list = saleShoesAnalysisDao.getHqShoesAnalysisListByMoney(page, shopSeqs, shoesSeqs, startPrice, endPrice, saleTimeStart, saleTimeEnd, orderBy, orderDir,loginUser.getShopSeq());
		return list;
	}







	
}
