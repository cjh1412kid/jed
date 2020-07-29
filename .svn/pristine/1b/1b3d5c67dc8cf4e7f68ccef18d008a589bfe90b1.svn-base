package io.nuite.modules.order_platform_app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import io.nuite.modules.order_platform_app.dao.ShoesDataDailyDetailDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.order_platform_app.service.ShoesDataService;

@Service
public class ShoesDataServiceImpl extends ServiceImpl<ShoesDataDao, ShoesDataEntity> implements ShoesDataService {
	
    @Autowired
    private ShoesDataDao shoesDataDao;

    @Autowired
	private GoodsShoesDao goodsShoesDao;
    
	@Autowired
	private ShoesDataDailyDetailDao shoesDataDailyDetailDao;

    @Autowired
    private GoodsSizeDao goodsSizeDao;

    @Autowired
    private BaseShopDao baseShopDao;
	
    @Override
    public ShoesDataEntity selectShoesDataByColorAndSize(Integer colorSeq, Integer sizeSeq, Integer shoesSeq) {
        return this.selectOne(new EntityWrapper<ShoesDataEntity>().eq("Shoes_Seq", shoesSeq).eq("Color_Seq", colorSeq).eq("Size_Seq", sizeSeq));
    }

    
    
    /**
     * 获取总部+门店订购的所有鞋子
     */
	@Override
	public List<Object> getHqAndShopPickShoesSeqs(Integer companySeq) {
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
		wrapper.setSqlSelect("DISTINCT Shoes_Seq");
		wrapper.where("Company_Seq = {0}", companySeq);
		List<Object> list = shoesDataDao.selectObjs(wrapper);
		return list;
	}
	
	
    
    
    /**
     * 获取多个门店订购的所有鞋子(总仓时传-1和0，因此必须带上companySeq)
     */
	@Override
	public List<Object> getShopPickShoeSeqs(Integer companySeq, List<Integer> shopSeqList) {
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
		wrapper.setSqlSelect("DISTINCT Shoes_Seq").eq("Company_Seq", companySeq).in("Shop_Seq", shopSeqList);
		List<Object> list = shoesDataDao.selectObjs(wrapper);
		return list;
	}


	 /**
     * 获取所有门店订购的所有鞋子
     */
	@Override
	public List<Object> getAllShopPickShoeSeqs(Integer companySeq) {
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
		wrapper.setSqlSelect("DISTINCT Shoes_Seq").eq("Company_Seq", companySeq);
		List<Object> list = shoesDataDao.selectObjs(wrapper);
		return list;
	}


    /**
     * 获取全部范围（总部加门店） 出现断码的鞋子序号（在一定鞋子序号范围内）
     */
	@Override
	public List<Integer> getHqAndShopBreakSizeShoesSeqs(List<Integer> shoesSeqs,List<Integer> shopSeqList) {
		
		List<Integer> hqAndShopBreakSizeShoesSeq = new ArrayList<Integer>();
		//1.查询鞋子
		for(Object shoesSeq : shoesSeqs) {
			
			//统计该鞋子序号各个尺码的库存量（//TODO 不考虑订货时就断码订的情况）
			Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
			wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM(Stock) AS stock").where("Shoes_Seq = {0}", shoesSeq).groupBy("Size_Seq").orderBy("Size_Seq ASC");
			if(shopSeqList != null && shopSeqList.size() > 0) {
				wrapper.in("Shop_Seq",shopSeqList);
			}
			List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
			
			
			//判断是否断码
			for(int i = 0; i < list.size() - 2; i++) {
				Map<String, Object> map0 = list.get(i);
				Integer stock0 = (Integer) map0.get("stock");
				if(stock0 <= 0) {
					Map<String, Object> map1 = list.get(i+1);
					Integer stock1 = (Integer) map1.get("stock");
					if(stock1 <= 0) {
						Map<String, Object> map2 = list.get(i+2);
						Integer stock2 = (Integer) map2.get("stock");
						if(stock2 <= 0) {
							hqAndShopBreakSizeShoesSeq.add((Integer)shoesSeq);
							break;
						}
					}
				}
			}
			
		}
		return hqAndShopBreakSizeShoesSeq;
	}



	/**
	 * 获取全部范围（总部加门店） 出现缺码的鞋子序号（在一定鞋子序号范围内）
	 */
	@Override
	public List<Integer> getHqAndShopMissSizeShoesSeqs(List<Integer> shoesSeqs,List<Integer> shopSeqList) {
		
		List<Integer> hqAndShopMissSizeShoesSeq = new ArrayList<Integer>();
		//1.查询鞋子
		for(Object shoesSeq : shoesSeqs) {
			
			//统计该鞋子序号各个尺码的库存量
			Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
			wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM(Stock) AS stock").where("Shoes_Seq = {0}", shoesSeq).groupBy("Size_Seq").orderBy("Size_Seq ASC");
			if(shopSeqList != null && shopSeqList.size() > 0) {
				wrapper.in("Shop_Seq",shopSeqList);
			}
			List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
			
			
			//判断是否缺码
			for(int i = 0; i < list.size(); i++) {
				if(i != 0 && i != list.size() - 1) {
					Map<String, Object> map0 = list.get(i);
					Integer stock0 = (Integer) map0.get("stock");
					Map<String, Object> map1 = list.get(i-1);
					Integer stock1 = (Integer) map1.get("stock");
					Map<String, Object> map2 = list.get(i+1);
					Integer stock2 = (Integer) map2.get("stock");
					if(stock0 <= 0 && stock1 > 0 && stock2 > 0) {
						hqAndShopMissSizeShoesSeq.add((Integer)shoesSeq);
						break;
					}
				}

			}
			
		}
		return hqAndShopMissSizeShoesSeq;
	}



	/**
	 * 获取全部范围（总部加门店） 出现在自定义尺码范围内全码的鞋子序号（在一定鞋子序号范围内）
	 */
	@Override
	public List<Integer> getHqAndShopFullSizeInSomeRangeShoesSeqs(List<Integer> shoesSeqs, Integer sizeStart, Integer sizeEnd,List<Integer> shopSeqList) {
		
		List<Integer> hqAndShopFullSizeInSomeRangeShoesSeq = new ArrayList<Integer>();
		//1.查询鞋子
		for(Object shoesSeq : shoesSeqs) {
			hqAndShopFullSizeInSomeRangeShoesSeq.add((Integer)shoesSeq);
			
			//统计该鞋子序号各个尺码的库存量（//TODO 订货时没有订的码不计为缺货）
			Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
			wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM(Stock) AS stock").where("Shoes_Seq = {0}", shoesSeq).groupBy("Size_Seq").orderBy("Size_Seq ASC");
			if(shopSeqList != null && shopSeqList.size() > 0) {
				wrapper.in("Shop_Seq",shopSeqList);
			}
			List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
			
			flag: for(int i = sizeStart; i <= sizeEnd; i++ ) {
				//判断该尺码是否有货
				for(int j = 0; j < list.size(); j++) {
					Map<String, Object> map = list.get(j);
					Integer sizeSeq = (Integer) map.get("sizeSeq");
					if(sizeSeq == i) {
						Integer stock = (Integer) map.get("stock");
						if(stock <= 0) {
							hqAndShopFullSizeInSomeRangeShoesSeq.remove((Integer)shoesSeq);
							break flag;
						} else {
							break;
						}
					}

				}
			}
		}
		return hqAndShopFullSizeInSomeRangeShoesSeq;
	}


	/**
	 * 获取全部范围（总部加门店） 出现齐码的鞋子序号（在一定鞋子序号范围内）
	 */
	@Override
	public List<Integer> getHqAndShopFullSizeShoesSeqs(List<Integer> shoesSeqs,List<Integer> shopSeqList) {
		List<Integer> hqAndShopFullSizeShoesSeq = new ArrayList<Integer>();
		//1.查询鞋子
		flag : for(Object shoesSeq : shoesSeqs) {
			
			//统计该鞋子序号各个尺码的库存量
			Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
			wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM(Stock) AS stock").where("Shoes_Seq = {0}", shoesSeq).groupBy("Size_Seq").orderBy("Size_Seq ASC");
			if(shopSeqList != null && shopSeqList.size() > 0) {
				wrapper.in("Shop_Seq",shopSeqList);
			}
			List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
			
			
			//判断是否齐码
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> map0 = list.get(i);
				Integer stock0 = (Integer) map0.get("stock");
				if(stock0 <= 0) {
					continue flag;
				}
			}
			
			hqAndShopFullSizeShoesSeq.add((Integer)shoesSeq);
			
		}
		return hqAndShopFullSizeShoesSeq;
	}
	
	
	
	
	
	

	/**
	 * 获取门店出现断码的鞋子序号（在一定鞋子序号范围内）
	 */
	@Override
	public List<Integer> getShopBreakSizeShoesSeqs(Integer shopSeq, List<Integer> shoesSeqs) {
		
		List<Integer> shopBreakSizeShoesSeq = new ArrayList<Integer>();
		//1.查询鞋子
		for(Object shoesSeq : shoesSeqs) {
			
			//统计该鞋子序号各个尺码的库存量（//TODO 不考虑订货时就断码订的情况）
			Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
			wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM(Stock) AS stock").where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq).groupBy("Size_Seq").orderBy("Size_Seq ASC");
			List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
			
			
			//判断是否断码
			for(int i = 0; i < list.size() - 2; i++) {
				Map<String, Object> map0 = list.get(i);
				Integer stock0 = (Integer) map0.get("stock");
				if(stock0 <= 0) {
					Map<String, Object> map1 = list.get(i+1);
					Integer stock1 = (Integer) map1.get("stock");
					if(stock1 <= 0) {
						Map<String, Object> map2 = list.get(i+2);
						Integer stock2 = (Integer) map2.get("stock");
						if(stock2 <= 0) {
							shopBreakSizeShoesSeq.add((Integer)shoesSeq);
							break;
						}
					}
				}
			}
			
		}
		return shopBreakSizeShoesSeq;
	}



	/**
	 * 获取门店 出现缺码的鞋子序号（在一定鞋子序号范围内）
	 */
	@Override
	public List<Integer> getShopMissSizeShoesSeqs(Integer shopSeq, List<Integer> shoesSeqs) {
		
		List<Integer> shopMissSizeShoesSeq = new ArrayList<Integer>();
		//1.查询鞋子
		for(Object shoesSeq : shoesSeqs) {
			
			//统计该鞋子序号各个尺码的库存量
			Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
			wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM(Stock) AS stock").where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq).groupBy("Size_Seq").orderBy("Size_Seq ASC");
			List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
			
			
			//判断是否缺码
			for(int i = 1; i < list.size() - 1; i++) {
				Map<String, Object> map0 = list.get(i);
				Integer stock0 = (Integer) map0.get("stock");
				if(stock0 <= 0) {
					shopMissSizeShoesSeq.add((Integer)shoesSeq);
					break;
				}
			}
			
		}
		return shopMissSizeShoesSeq;
	}



	/**
	 * 获取门店出现在自定义尺码范围内全码的鞋子序号（在一定鞋子序号范围内）
	 */
	@Override
	public List<Integer> getShopFullSizeInSomeRangeShoesSeqs(Integer shopSeq, List<Integer> shoesSeqs, Integer sizeStart, Integer sizeEnd) {
		
		List<Integer> shopFullSizeInSomeRangeShoesSeq = new ArrayList<Integer>();
		//1.查询鞋子
		for(Object shoesSeq : shoesSeqs) {
			shopFullSizeInSomeRangeShoesSeq.add((Integer)shoesSeq);
			
			//统计该鞋子序号各个尺码的库存量（//TODO 订货时没有订的码不计为缺货）
			Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
			wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM(Stock) AS stock").where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq).groupBy("Size_Seq").orderBy("Size_Seq ASC");
			List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
			
			flag: for(int i = sizeStart; i <= sizeEnd; i++ ) {
				//判断该尺码是否有货
				for(int j = 0; j < list.size(); j++) {
					Map<String, Object> map = list.get(j);
					Integer sizeSeq = (Integer) map.get("sizeSeq");
					if(sizeSeq == i) {
						Integer stock = (Integer) map.get("stock");
						if(stock <= 0) {
							shopFullSizeInSomeRangeShoesSeq.remove((Integer)shoesSeq);
							break flag;
						} else {
							break;
						}
					}

				}
			}
		}
		return shopFullSizeInSomeRangeShoesSeq;
	}


	
	
	/**
	 * 获取门店出现齐码的鞋子序号（在一定鞋子序号范围内）
	 */
	@Override
	public List<Integer> getShopFullSizeShoesSeqs(Integer shopSeq, List<Integer> shoesSeqs) {
		List<Integer> shopMissSizeShoesSeq = new ArrayList<Integer>();
		//1.查询鞋子
		flag : for(Object shoesSeq : shoesSeqs) {
			
			//统计该鞋子序号各个尺码的库存量
			Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
			wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM(Stock) AS stock").where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq).groupBy("Size_Seq").orderBy("Size_Seq ASC");
			List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
			
			
			//判断是否缺码
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> map0 = list.get(i);
				Integer stock0 = (Integer) map0.get("stock");
				if(stock0 <= 0) {
					continue flag;
				}
			}
			
			shopMissSizeShoesSeq.add((Integer)shoesSeq);
			
		}
		return shopMissSizeShoesSeq;
	}
	
	
	
	
	
	/**
	 * 根据门店序号和鞋子序号查询ShoesDate列表（按尺码从小到大排序）
	 */
	@Override
	public List<ShoesDataEntity> getShoesDateListByShoesSeq(Integer shopSeq, Integer shoesSeq) {
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
		wrapper.where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq).orderBy("Size_Seq ASC");
		return shoesDataDao.selectList(wrapper);
	}

	@Override
	public Map<String, Object> selectShopStockByShoesSeq(String goodId,Integer sizeSeq) throws Exception {
		Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("GoodID",goodId);
		Map<String,Object> map = new HashMap<>(10);
		map.put("shoesSeq",goodsShoesDao.selectList(wrapper).get(0).getSeq());
		map.put("sizeSeq",sizeSeq);
		Map<String,Object> result = new HashMap<>(10);
		result.put("result",baseMapper.selectShopStockByShoesSeq(map));
		return result;
	}

	@Override
	public Integer totalNum(List<Integer> satisfyShoesSeqs,List<Integer> shopSeqList) throws Exception {
		if(satisfyShoesSeqs == null || satisfyShoesSeqs.size() == 0) {
			return 0;
		}
		String shoesSeqs = "(" + Joiner.on(",").join(satisfyShoesSeqs) + ")";
		Wrapper<ShoesDataEntity> totalNumWrapper = new EntityWrapper<>();
		totalNumWrapper.setSqlSelect("SUM(Stock) as stock").where("Shoes_Seq in " + shoesSeqs);
		if(shopSeqList != null) {
			totalNumWrapper.in("Shop_Seq",shopSeqList);
		}
		List<ShoesDataEntity> shoesDataEntityList = shoesDataDao.selectList(totalNumWrapper);
		Integer totalNum = 0;
		if(shoesDataEntityList != null && shoesDataEntityList.size() > 0 && shoesDataEntityList.get(0) != null) {
			totalNum = shoesDataEntityList.get(0).getStock();
		}
		return totalNum;
	}

	@Override
	public Integer totalStockNum(List<Integer> satisfyShoesSeqs) throws Exception {
		if(satisfyShoesSeqs == null || satisfyShoesSeqs.size() == 0) {
			return 0;
		}
		String shoesSeqs = "(" + Joiner.on(",").join(satisfyShoesSeqs) + ")";
		Wrapper<ShoesDataEntity> totalStockNumWrapper = new EntityWrapper<>();
		totalStockNumWrapper.setSqlSelect("SUM(Stock) as stock").where("(Shop_Seq = 0 or Shop_Seq = -1) and Shoes_Seq in " + shoesSeqs);
		List<ShoesDataEntity> shoesDataEntityList = shoesDataDao.selectList(totalStockNumWrapper);
		Integer totalStockNum = 0;
		if(shoesDataEntityList != null && shoesDataEntityList.size() > 0 && shoesDataEntityList.get(0) != null) {
			totalStockNum = shoesDataEntityList.get(0).getStock();
		}
		return totalStockNum;
	}

	@Override
	public Integer totalShopNum(List<Integer> satisfyShoesSeqs,List<Integer> shopSeqs) throws Exception {
		if(satisfyShoesSeqs == null || satisfyShoesSeqs.size() == 0) {
			return 0;
		}
		String shoesSeqs = "(" + Joiner.on(",").join(satisfyShoesSeqs) + ")";
		Wrapper<ShoesDataEntity> totalShopNumWrapper = new EntityWrapper<>();
		totalShopNumWrapper.setSqlSelect("SUM(Stock) as stock").where("(Shop_Seq != 0 and Shop_Seq != -1) and Shoes_Seq in " + shoesSeqs);
		if(shopSeqs != null) {
			totalShopNumWrapper.in("Shop_Seq",shopSeqs);
		}
		List<ShoesDataEntity> shoesDataEntityList = shoesDataDao.selectList(totalShopNumWrapper);
		Integer totalShopNum = 0;
		if(shoesDataEntityList != null && shoesDataEntityList.size() > 0 && shoesDataEntityList.get(0) != null) {
			totalShopNum = shoesDataEntityList.get(0).getStock();
		}
		return totalShopNum;
	}



	@Override
	public List<Object> getAllSize(Integer shoesSeq) {
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<>();
		wrapper.setSqlSelect("distinct Size_Seq").where("Shoes_Seq = {0}", shoesSeq).orderBy("Size_Seq");
		List<Object> sizeSeqs=shoesDataDao.selectObjs(wrapper);
		return sizeSeqs;
	}



	@Override
	public List<Object> getNumCount(Integer shoesSeq, Integer sizeSeq) {
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<>();
		wrapper.setSqlSelect("sum(Stock)").where("Shoes_Seq = {0} AND Size_Seq={1}", shoesSeq,sizeSeq);
		List<Object> numCounts=shoesDataDao.selectObjs(wrapper);
		return numCounts;
	}

	@Override
	public List<Integer> getBreakShoesSeqList(Integer companySeq, List<Integer> shoesSeqList, List<Integer> shopSeqList) {
		List<Integer> hqAndShopBreakSizeShoesSeq = new ArrayList<Integer>();
		//如果门店序号列表为空,查询出当前公司下所有的门店
		if (shopSeqList == null) {
			shopSeqList = new ArrayList<>(10);
			shopSeqList.add(0);
			shopSeqList.add(-1);
			Wrapper<BaseShopEntity> baseShopEntityWrapper = new EntityWrapper<>();
			baseShopEntityWrapper.eq("Company_Seq", companySeq);
			List<BaseShopEntity> baseShopEntities = baseShopDao.selectList(baseShopEntityWrapper);
			for (BaseShopEntity baseShopEntity : baseShopEntities) {
				shopSeqList.add(baseShopEntity.getSeq());
			}
		}
		//获取shoesSeqList中所有货品在shopSeqList中所有门店的进货尺码
        Wrapper<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntityWrapper = new EntityWrapper<>();
		String shoesSeqStr = "(" + Joiner.on(",").join(shoesSeqList) + ")";
        shoesDataDailyDetailEntityWrapper.where("Shoes_Seq in " + shoesSeqStr).in("Shop_Seq", shopSeqList)
                .setSqlSelect("Size,Shoes_Seq").groupBy("Size,Shoes_Seq").orderBy("Shoes_Seq",true).orderBy("Size", true).eq("Company_Seq",companySeq);
        List<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntities = shoesDataDailyDetailDao.selectList(shoesDataDailyDetailEntityWrapper);
        //获取shoesSeqList中所有货品在shopSeqList中所有门店的库存尺码
        Wrapper<ShoesDataEntity> shoesDataEntityWrapper = new EntityWrapper<>();
        shoesDataEntityWrapper.in("Shop_Seq", shopSeqList).where("Shoes_Seq in " + shoesSeqStr).setSqlSelect("Size_Seq,Shoes_Seq")
                .groupBy("Size_Seq,Shoes_Seq").having("SUM(Stock) > 0").orderBy("Shoes_Seq",true).orderBy("Size_Seq",true).eq("Company_Seq",companySeq);
        List<ShoesDataEntity> shoesDataEntityList = shoesDataDao.selectList(shoesDataEntityWrapper);
        //获取公司下所有尺码
        Wrapper<GoodsSizeEntity> goodsSizeEntityWrapper = new EntityWrapper<>();
        goodsSizeEntityWrapper.eq("Company_Seq",companySeq);
        List<GoodsSizeEntity> goodsSizeEntities = goodsSizeDao.selectList(goodsSizeEntityWrapper);
        //循环查询每双鞋子在每个门店的尺码数据
		for (Integer shoesSeq : shoesSeqList) {
			//查询门店该鞋子进过货的最大尺码和最小尺码
			Integer minSize = 0;
			Integer maxSize = 0;
			List<Integer> shoesSizeList = new ArrayList<>(10);
            List<Integer> sizeList = new ArrayList<>(10);
            //取出当前货品所有的进货尺码
            for(ShoesDataDailyDetailEntity shoesDataDailyDetailEntity : shoesDataDailyDetailEntities) {
                if(shoesDataDailyDetailEntity.getShoesSeq().equals(shoesSeq)) {
                    for(GoodsSizeEntity goodsSizeEntity : goodsSizeEntities) {
                        if(shoesDataDailyDetailEntity.getSize().equals(goodsSizeEntity.getSeq())) {
                            shoesSizeList.add(Integer.parseInt(goodsSizeEntity.getSizeName()));
                        }
                    }

                }
            }
            //取出当前货品所有的库存尺码
            for(ShoesDataEntity shoesDataEntity : shoesDataEntityList) {
                if(shoesDataEntity.getShoesSeq().equals(shoesSeq)) {
                    for(GoodsSizeEntity goodsSizeEntity : goodsSizeEntities) {
                        if(shoesDataEntity.getSizeSeq().equals(goodsSizeEntity.getSeq())) {
                            sizeList.add(Integer.parseInt(goodsSizeEntity.getSizeName()));
                        }
                    }
                }
            }
			//如果进出库记录不为空,获取最大尺码和最小尺码
			if (shoesSizeList.size() > 0) {
				minSize = shoesSizeList.get(0);
				maxSize = shoesSizeList.get(shoesSizeList.size() - 1);
			//如果进出库记录为空,则不做是否断码计算
			} else {
				continue;
			}
			if (sizeList.size() > 0) {
				for (int i = 0;i < sizeList.size() - 2;i++) {
				    //如果当前尺码加上2和2个之后的尺码一致,说明存在三个连续的尺码,即不是断码
					if((Integer)sizeList.get(i) + 2 == (Integer) sizeList.get(i + 2)) {
						break;
					}
					//如果循环到最后都不存在连续的三个尺码,即为断码
					if(i == sizeList.size() - 3) {
						hqAndShopBreakSizeShoesSeq.add(shoesSeq);
					}
				}
			}
		}
		return hqAndShopBreakSizeShoesSeq;
	}

	@Override
	public List<Integer> getAbsenceShoesSeqList(Integer companySeq, List<Integer> shoesSeqList, List<Integer> shopSeqList) {
		List<Integer> hqAndShopMissSizeShoesSeq = new ArrayList<Integer>();
        //如果门店序号列表为空,查询出当前公司下所有的门店
        if (shopSeqList == null) {
            shopSeqList = new ArrayList<>(10);
			shopSeqList.add(0);
			shopSeqList.add(-1);
            Wrapper<BaseShopEntity> baseShopEntityWrapper = new EntityWrapper<>();
            baseShopEntityWrapper.eq("Company_Seq", companySeq);
            List<BaseShopEntity> baseShopEntities = baseShopDao.selectList(baseShopEntityWrapper);
            for (BaseShopEntity baseShopEntity : baseShopEntities) {
                shopSeqList.add(baseShopEntity.getSeq());
            }
        }
        //获取shoesSeqList中所有货品在shopSeqList中所有门店的进货尺码
        Wrapper<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntityWrapper = new EntityWrapper<>();
        String shoesSeqStr = "(" + Joiner.on(",").join(shoesSeqList) + ")";
        shoesDataDailyDetailEntityWrapper.where("Shoes_Seq in " + shoesSeqStr).in("Shop_Seq", shopSeqList)
                .setSqlSelect("Size,Shoes_Seq").groupBy("Size,Shoes_Seq").orderBy("Shoes_Seq",true).orderBy("Size", true).eq("Company_Seq",companySeq);
        List<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntities = shoesDataDailyDetailDao.selectList(shoesDataDailyDetailEntityWrapper);
        //获取shoesSeqList中所有货品在shopSeqList中所有门店的库存尺码
        Wrapper<ShoesDataEntity> shoesDataEntityWrapper = new EntityWrapper<>();
        shoesDataEntityWrapper.in("Shop_Seq", shopSeqList).where("Shoes_Seq in " + shoesSeqStr).setSqlSelect("Size_Seq,Shoes_Seq")
                .groupBy("Size_Seq,Shoes_Seq").having("SUM(Stock) > 0").orderBy("Shoes_Seq",true).orderBy("Size_Seq",true).eq("Company_Seq",companySeq);
        List<ShoesDataEntity> shoesDataEntityList = shoesDataDao.selectList(shoesDataEntityWrapper);
        //获取公司下所有尺码
        Wrapper<GoodsSizeEntity> goodsSizeEntityWrapper = new EntityWrapper<>();
        goodsSizeEntityWrapper.eq("Company_Seq",companySeq);
        List<GoodsSizeEntity> goodsSizeEntities = goodsSizeDao.selectList(goodsSizeEntityWrapper);
        //循环查询每双鞋子在每个门店的尺码数据
        for (Integer shoesSeq : shoesSeqList) {
            //查询门店该鞋子进过货的最大尺码和最小尺码
            Integer minSize = 0;
            Integer maxSize = 0;
            List<Integer> shoesSizeList = new ArrayList<>(10);
            List<Object> sizeList = new ArrayList<>(10);
            //取出当前货品所有的进货尺码
            for(ShoesDataDailyDetailEntity shoesDataDailyDetailEntity : shoesDataDailyDetailEntities) {
                if(shoesDataDailyDetailEntity.getShoesSeq().equals(shoesSeq)) {
                    for(GoodsSizeEntity goodsSizeEntity : goodsSizeEntities) {
                        if(shoesDataDailyDetailEntity.getSize().equals(goodsSizeEntity.getSeq())) {
                            shoesSizeList.add(Integer.parseInt(goodsSizeEntity.getSizeName()));
                        }
                    }
                }
            }
            //取出当前货品所有的库存尺码
            for(ShoesDataEntity shoesDataEntity : shoesDataEntityList) {
                if(shoesDataEntity.getShoesSeq().equals(shoesSeq)) {
                    for(GoodsSizeEntity goodsSizeEntity : goodsSizeEntities) {
                        if(shoesDataEntity.getSizeSeq().equals(goodsSizeEntity.getSeq())) {
                            sizeList.add(Integer.parseInt(goodsSizeEntity.getSizeName()));
                        }
                    }
                }
            }
            //如果进出库记录不为空,获取最大尺码和最小尺码
            if (shoesSizeList.size() > 0) {
                minSize = shoesSizeList.get(0);
                maxSize = shoesSizeList.get(shoesSizeList.size() - 1);
                //如果进出库记录为空,则不做是否断码计算
            } else {
                continue;
            }
            if (sizeList.size() > 0) {
                for (int i = 0;i < sizeList.size() - 2;i++) {
                    //如果当前尺码加上2和2个之后的尺码一致,说明存在三个连续的尺码,即不是断码
                    if((Integer)sizeList.get(i) + 2 == (Integer) sizeList.get(i + 2)) {
                        //第一个尺码在
                        if (sizeList.get(0).equals(minSize)) {
                        	//最后一个尺码在
                        	if(sizeList.get(sizeList.size() - 1).equals(maxSize)) {
								//缺一个码子算缺码
								if(sizeList.size() < maxSize - minSize + 1) {
									hqAndShopMissSizeShoesSeq.add(shoesSeq);
								}
							//最后一个尺码不在
							}else {
								//缺一个码子算缺码
								if(sizeList.size() < maxSize - minSize) {
									hqAndShopMissSizeShoesSeq.add(shoesSeq);
								}
							}
                        //第一个尺码不在
                        }else {
                        	//最后一个尺码在
							if(sizeList.get(sizeList.size() - 1).equals(maxSize)) {
								//缺一个码子算缺码
								if(sizeList.size() < maxSize - minSize) {
									hqAndShopMissSizeShoesSeq.add(shoesSeq);
								}
							//最后一尺码不在
							}else {
								//缺一个码子算缺码
								if(sizeList.size() < maxSize - minSize - 1) {
									hqAndShopMissSizeShoesSeq.add(shoesSeq);
								}
							}
                        }
                        break;
                    }
                }
            }
        }
		return hqAndShopMissSizeShoesSeq;
	}
}
