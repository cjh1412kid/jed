package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import io.nuite.common.utils.DateUtils;
import io.nuite.modules.order_platform_app.dao.*;
import io.nuite.modules.order_platform_app.entity.*;
import io.nuite.modules.order_platform_app.service.ShoesDataService;
import io.nuite.modules.order_platform_app.service.ShoesInfoService;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.GoodsCategoryDao;
import io.nuite.modules.sr_base.dao.GoodsSeasonDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.*;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.dao.ShoesReplenishDao;
import io.nuite.modules.system.dao.order_platform.AllocateTransferShopTempStockDao;
import io.nuite.modules.system.entity.order_platform.AllocateTransferShopTempStockEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ShoesInfoServiceImpl extends ServiceImpl<ShoesInfoDao, ShoesInfoEntity> implements ShoesInfoService {

    @Autowired
    private GoodsCategoryDao goodsCategoryDao;

    @Autowired
    private ShoesInfoDao shoesInfoDao;
    
    @Autowired
    private ShopMainpushDao shopMainpushDao;

	@Autowired
	private ConfigUtils configUtils;

	@Autowired
	private ShoesDataService shoesDataService;

	@Autowired
    private SaleShoesDetailDao saleShoesDetailDao;

	@Autowired
    private ShoesReplenishDao shoesReplenishDao;

	@Autowired
	private ShoesDataDao shoesDataDao;

	@Autowired
	private GoodsSeasonDao goodsSeasonDao;

	@Autowired
    private GoodsShoesDao goodsShoesDao;

	@Autowired
    private AllocateTransferFictitiousMallDao allocateTransferFictitiousMallDao;
	
	@Autowired
	private AllocateTransferShopTempStockDao allocateTransferShopTempStockDao;
	
	@Autowired
	private AllocateTransferTransferInApplicationDao allocateTransferTransferInApplicationDao;
	
	@Autowired
	private BaseShopDao baseShopDao;
	
	@Autowired
	private ShoesDataDailyDetailDao shoesDataDailyDetailDao;
	
    //@Autowired
    //private AllocateTransferFictitiousMallService allocateTransferFictitiousMallService;
    
    
	
    /**
     * 根据公司编号获取鞋子所有分类
     */
    @Override
    public List<Map<String, Object>> getShoesCategory(Integer companySeq, Integer parentSeq) {
		Wrapper<GoodsCategoryEntity> wrapper = new EntityWrapper<GoodsCategoryEntity>();
		wrapper.setSqlSelect("Seq AS seq, Name AS name").where("Company_Seq = {0} AND ParentSeq = {1} AND Visible = 0", companySeq, parentSeq);
        return goodsCategoryDao.selectMaps(wrapper);
    }


    
    /**
     * 根据shoesSeq获取ShoesInfo对象
     */
    @Override
    public ShoesInfoEntity getShoesInfoByShoesSeq(Integer shoesSeq) {
        ShoesInfoEntity shoesInfoEntity = new ShoesInfoEntity();
        shoesInfoEntity.setShoesSeq(shoesSeq);
        return shoesInfoDao.selectOne(shoesInfoEntity);
    }


	
	
    
    
    /**
     * 获取所有新品鞋子seq
     */
	@Override
	public List<Object> getHqNewestShoesSeqs(Integer companySeq) {
		
		//用户设置的新品
		Wrapper<ShoesInfoEntity> wrapper1 = new EntityWrapper<ShoesInfoEntity>();
		wrapper1.setSqlSelect("Shoes_Seq").where("IsNewest = 1");
		List<Object> list1 = shoesInfoDao.selectObjs(wrapper1);
		
		//近30天内上市的货品（第一次仓出店数据时间在30天内的货品）
		//获取当前日期30天前的日期
		String thirtyDaysBefore =  DateUtils.format(DateUtils.addDateDays(new Date(), -30));
		
//		SELECT 
//			Shoes_Seq
//		FROM 
//			[dbo].[YHSR_OP_ShoesData_Daily_Detail] 
//		WHERE 
//			Company_Seq = 1 AND type = 1
//		GROUP BY 
//			Shoes_Seq 	
//			HAVING MIN (UpdateTime) >= '2019-12-17 00:00:00'
		Wrapper<ShoesDataDailyDetailEntity> wrapper2 = new EntityWrapper<ShoesDataDailyDetailEntity>();
		wrapper2.setSqlSelect("Shoes_Seq").where("Company_Seq = {0} AND Type = 1", companySeq);
		wrapper2.groupBy("Shoes_Seq").having("MIN (UpdateTime) >= {0}", thirtyDaysBefore);
		List<Object> list2 = shoesDataDailyDetailDao.selectObjs(wrapper2);
		
		//合并两个List并去重
		List<Object> result = Stream.of(list1, list2).flatMap(Collection::stream).distinct().collect(Collectors.toList());
		
		return result;
	}
	
	
	/**
	 * 获取公司主推鞋子seq
	 */
	@Override
	public List<Object> getHqMainpushShoesSeqs(Integer companySeq) {
		Wrapper<ShoesInfoEntity> wrapper = new EntityWrapper<ShoesInfoEntity>();
		wrapper.setSqlSelect("Shoes_Seq").where("IsMainpush = 1");
		return shoesInfoDao.selectObjs(wrapper);
	}
	
	
	
	/**
	 * 获取某一门店主推鞋子seq
	 */
	@Override
	public List<Object> getShopMainpushShoesSeqs(Integer shopSeq) {
		Wrapper<ShopMainpushEntity> wrapper = new EntityWrapper<ShopMainpushEntity>();
		wrapper.setSqlSelect("Shoes_Seq").where("Shop_Seq = {0}", shopSeq);
		return shopMainpushDao.selectObjs(wrapper);
	}

	/**
	 * 获取所有门店主推鞋子seqs
	 */
	@Override
	public List<Object> getAllShopMainpushShoesSeqs(Integer companySeq) {
		Wrapper<BaseShopEntity> baseShopEntityWrapper = new EntityWrapper<BaseShopEntity>();
        baseShopEntityWrapper.eq("Company_Seq", companySeq).setSqlSelect("Seq");
        List<Object> shopSeqs = baseShopDao.selectObjs(baseShopEntityWrapper);
		
		Wrapper<ShopMainpushEntity> wrapper = new EntityWrapper<ShopMainpushEntity>();
		wrapper.setSqlSelect("Shoes_Seq").in("Shop_Seq", shopSeqs);
		return shopMainpushDao.selectObjs(wrapper);
	}
	
	
	
    /**
     * 全部鞋子列表
     */
	@Override
	public List<Map<String, Object>> getHqAndShopShoesList(List<Integer> satisfyShoesSeqs, Integer orderBy, Integer orderDir, Integer start,
			Integer num) {
		
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        
        String shoesSeqs = Joiner.on(",").join(satisfyShoesSeqs);
        
		List<Map<String, Object>> list = shoesInfoDao.getHqAndShopShoesList(page, shoesSeqs, orderBy, orderDir);
		return list;
	}

	
    /**
     * 全部鞋子列表(销售时间区间范围内)
     */
	@Override
	public List<Map<String, Object>> getHqAndShopShoesListOnSaleTime(List<Integer> satisfyShoesSeqs, Date saleTimeStart,
			Date saleTimeEnd, Integer stockMinNum, Integer stockMaxNum, Integer orderBy, Integer orderDir, Integer start, Integer num,List<Integer> shopSeqList) {
		
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        page.setSearchCount(false);
        
        String shoesSeqs = Joiner.on(",").join(satisfyShoesSeqs);
        
		List<Map<String, Object>> list = shoesInfoDao.getHqAndShopShoesListOnSaleTime(page, shoesSeqs, saleTimeStart, saleTimeEnd, stockMinNum, stockMaxNum, orderBy, orderDir,shopSeqList);
		return list;
	}
	
	
	
	
	/**
	 * 门店鞋子列表
	 */
	@Override
	public List<Map<String, Object>> getShopShoesList(List<Object> shopSeqList,Integer shopSeq, List<Integer> satisfyShoesSeqs, Integer orderBy, Integer orderDir,
			Integer start, Integer num) {
		
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        
        String shoesSeqs = Joiner.on(",").join(satisfyShoesSeqs);
        
		List<Map<String, Object>> list = shoesInfoDao.getShopShoesList(page,shopSeqList, shopSeq, shoesSeqs, orderBy, orderDir);
		return list;
		
	}


	/**
	 * 门店鞋子列表(销售时间区间范围内)
	 */
	@Override
	public List<Map<String, Object>> getShopShoesListOnSaleTime(List<Object> shopSeqList,Integer shopSeq, List<Integer> satisfyShoesSeqs, Date saleTimeStart,
			Date saleTimeEnd, Integer stockMinNum, Integer stockMaxNum, Integer orderBy, Integer orderDir, Integer start, Integer num) {
		
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        page.setSearchCount(false);
        
        String shoesSeqs = Joiner.on(",").join(satisfyShoesSeqs);
        
		List<Map<String, Object>> list = shoesInfoDao.getShopShoesListOnSaleTime(page,shopSeqList, shopSeq, shoesSeqs, saleTimeStart, saleTimeEnd, stockMinNum, stockMaxNum, orderBy, orderDir);
		return list;
	}
	
	
	
	

	/**
	 * 设置公司主推
	 */
	@Override
	public void setHqMainpush(Integer shoesSeq, Integer isMainpush) {
		ShoesInfoEntity shoesInfoEntity = new ShoesInfoEntity();
		shoesInfoEntity.setIsMainpush(isMainpush);
		Wrapper<ShoesInfoEntity> wrapper = new EntityWrapper<ShoesInfoEntity>();
		wrapper.where("Shoes_Seq = {0}", shoesSeq);
		shoesInfoDao.update(shoesInfoEntity, wrapper);
	}



	@Override
	public Page<Map<String, Object>> getPage(List<Integer> satisfyShoesSeqs, Integer orderBy, Integer orderDir,
			Integer start, Integer num) {

		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        
        String shoesSeqs = Joiner.on(",").join(satisfyShoesSeqs);
        
		List<Map<String, Object>> list = shoesInfoDao.getHqAndShopShoesList(page, shoesSeqs, orderBy, orderDir);
		return page;
	}



	@Override
	public Page<Map<String, Object>> getPage(List<Integer> satisfyShoesSeqs, Date saleTimeStart, Date saleTimeEnd,
			Integer stockMinNum, Integer stockMaxNum, Integer orderBy, Integer orderDir, Integer start, Integer num,
			List<Integer> shopSeqList) {
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        
        String shoesSeqs = Joiner.on(",").join(satisfyShoesSeqs);
        
		List<Map<String, Object>> list = shoesInfoDao.getHqAndShopShoesListOnSaleTime(page, shoesSeqs, saleTimeStart, saleTimeEnd, stockMinNum, stockMaxNum, orderBy, orderDir,shopSeqList);
		return page;
	}



	@Override
	public Page<Map<String, Object>> getPage(List<Object> shopSeqList, Integer shopSeq, List<Integer> satisfyShoesSeqs,
			Integer orderBy, Integer orderDir, Integer start, Integer num) {
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        
        String shoesSeqs = Joiner.on(",").join(satisfyShoesSeqs);
        
		List<Map<String, Object>> list = shoesInfoDao.getShopShoesList(page,shopSeqList, shopSeq, shoesSeqs, orderBy, orderDir);
		return page;
		
	}



	@Override
	public Page<Map<String, Object>> getPage(List<Object> shopSeqList, Integer shopSeq, List<Integer> satisfyShoesSeqs,
			Date saleTimeStart, Date saleTimeEnd, Integer stockMinNum, Integer stockMaxNum, Integer orderBy,
			Integer orderDir, Integer start, Integer num) {
		int pageSize = num;
		int pageNo = ((start - 1) / num) + 1;
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        
        String shoesSeqs = Joiner.on(",").join(satisfyShoesSeqs);
        
		List<Map<String, Object>> list = shoesInfoDao.getShopShoesListOnSaleTime(page,shopSeqList, shopSeq, shoesSeqs, saleTimeStart, saleTimeEnd, stockMinNum, stockMaxNum, orderBy, orderDir);
		return page;
	}



	@Override
	public Map<String, Object> getShopShoeOnSaleTime(Date saleTimeStart, Date saleTimeEnd, Integer shoesSeq) {
		Map<String, Object> map = shoesInfoDao.getShopShoesOnSaleTime( saleTimeStart, saleTimeEnd, shoesSeq);
		return map;
	}



	@Override
	public List<Map<String, Object>> getShoeOnSaleTimeOnShop(Date saleTimeStart, Date saleTimeEnd, Integer shoesSeq,Integer orderBy,Integer orderDir) {
		List<Map<String, Object>> list = shoesInfoDao.getShoeOnSaleTimeOnShop( saleTimeStart, saleTimeEnd, shoesSeq,orderBy,orderDir);
		return list;
	}



	@Override
	public List<Map<String, Object>> getAllSizeStock(Integer shoesSeq, Integer shopSeq) {
		return shoesInfoDao.getAllSizeStock(shoesSeq, shopSeq);
	}



	@Override
	public List<Map<String, Object>> getHeadOnSaleTime(Date saleTimeStart, Date saleTimeEnd, Integer shoesSeq) {
		List<Map<String, Object>> list = shoesInfoDao.getHeadOnSaleTime( saleTimeStart, saleTimeEnd, shoesSeq);
		return list;
	}



	@Override
	public List<Map<String, Object>> getSizeStock(Integer shoesSeq) {
		return shoesInfoDao.getSizeStock(shoesSeq);
	}

	@Override
	public List<Map<String, Object>> selectMainPushShoes(BaseUserEntity loginUser, List<Integer> shopSeqList, List<Integer> categorySeqList, Integer orderBy, Integer orderDir, Date saleTimeStart, Date saleTimeEnd, Page<Map<String, Object>> page) throws Exception {
		Map<String,Object> map = new HashMap<>(10);
		//如果门店序号中存在0,则需添加-1,0和-1都是总仓序号
		if(shopSeqList != null && shopSeqList.size() > 0) {
			for (Integer shopSeq : shopSeqList) {
				if(shopSeq == 0) {
					shopSeqList.add(-1);
					break;
				}
			}
		}
        //用于查询库存汇总
        List<Integer> shopSeqs = null;
        //总部账号
        if(loginUser.getShopSeq() == null) {
		    map.put("type",0);
		    map.put("shopSeq",0);
		    //门店序号列表不为空,循环添加到库存查询门店列表
            if(shopSeqList != null && shopSeqList.size() > 0) {
                shopSeqs = new ArrayList<>(10);
                for(Integer shopSeq : shopSeqList) {
                    shopSeqs.add(shopSeq);
                }
                //总仓库存添加总仓序号
                shopSeqs.add(-1);
                shopSeqs.add(0);
            }
        //门店账号
        }else {
            map.put("type",1);
			map.put("shopSeq",loginUser.getShopSeq());
			//门店序号列表不为空,循环添加到库存查询门店列表
            if(shopSeqList != null && shopSeqList.size() > 0) {
				shopSeqs = new ArrayList<>(10);
				for(Integer shopSeq : shopSeqList) {
					shopSeqs.add(shopSeq);
				}
                //总仓库存添加总仓序号
                shopSeqs.add(-1);
                shopSeqs.add(0);
            }
        }
        //TOP:1:50或者10,2:80或者20,3:100或者30
        map.put("ranking",1);
		//0是滞销,1是畅销
		map.put("orderType",0);
		map.put("companySeq",loginUser.getCompanySeq());
		map.put("shopSeqList",shopSeqList);
		map.put("categorySeqList",categorySeqList);
		map.put("orderBy",orderBy);
		map.put("orderDir",orderDir);
		map.put("seasonSeqList",selectLastSeason());
		map.put("saleTimeStart",saleTimeStart);
		map.put("saleTimeEnd",saleTimeEnd);
		//分页查询
		List<Map<String,Object>> shoesList = baseMapper.selectMainPushShoes(map,page);
		//不分页查询
		page = new Page<>(0,Integer.MAX_VALUE);
		List<Map<String,Object>> allShoesList = baseMapper.selectMainPushShoes(map,page);
		List<Integer> yearList = new ArrayList<>(10);
		yearList.add(2019);
		map.put("yearList",yearList);
		//滞销列表
		List<Map<String,Object>> unsalableShoesList = saleShoesDetailDao.selectUnsalableShoes(map);
		//畅销列表
        List<Map<String,Object>> salableShoesList = saleShoesDetailDao.selectSalableShoes(map);
        //补货列表
        List<Map<String,Object>> supplementShoesList = shoesReplenishDao.getReplenishSeqList(map);
		List<Integer> satisfyShoesSeqs = new ArrayList<>(10);
		for(Map<String,Object> shoes : allShoesList) {
			satisfyShoesSeqs.add(Integer.parseInt(shoes.get("seq").toString()));
		}
		Integer totalSaleNum = 0;
		for (Map<String,Object> shoes : allShoesList) {
			totalSaleNum += (Integer) shoes.get("salesNum");
		}
		Integer totalStockNum = shoesDataService.totalStockNum(satisfyShoesSeqs);
		Integer totalNum = shoesDataService.totalNum(satisfyShoesSeqs,shopSeqs);
		Integer totalShopNum = shoesDataService.totalShopNum(satisfyShoesSeqs,shopSeqs);
		for (Map<String,Object> shoes : shoesList) {
            shoes.put("isUnsalable",0);
            shoes.put("isSalable",0);
            shoes.put("isSupplement",0);
            //循环判断是否滞销
		    for(Map<String,Object> unsalableShoes : unsalableShoesList) {
                if(shoes.get("seq").equals(unsalableShoes.get("seq"))) {
                    shoes.put("isUnsalable",1);
                }
            }
            //循环判断是否畅销
            for(Map<String,Object> salableShoes : salableShoesList) {
                if(shoes.get("seq").equals(salableShoes.get("seq"))) {
                    shoes.put("isSalable",1);
                }
            }
            //循环判断是否补过货
            for(Map<String,Object> supplementShoes : supplementShoesList) {
                if(shoes.get("seq").equals(supplementShoes.get("seq"))) {
                    shoes.put("isSupplement",1);
                }
            }
            //图片路径
			shoes.put("img",getImgPath(shoes.get("goodId").toString()) + shoes.get("img"));
			//总SKU数
			shoes.put("totalSKUNum",page.getTotal());
			//总仓库存
			shoes.put("totalStockNum",totalStockNum);
			//总库存
            shoes.put("totalNum",totalNum);
            //总销量
            shoes.put("totalSaleNum",totalSaleNum);
            //门店总库存
            shoes.put("totalShopNum",totalShopNum);
            
            //如果是门店用户，展示当前货品是否已添加虚拟商城的标识
            map.put("isTransferOut", 0);
           /* if(loginUser.getShopSeq() != null) {
                if(allocateTransferFictitiousMallService.isTransferOutToFictitiousMall(loginUser.getShopSeq(), (Integer)shoes.get("seq"))) {
                	map.put("isTransferOut", 1);
                }
            }*/
		}
		return shoesList;
	}



	@Override
	public List<Map<String, Object>> selectSalableShoes(BaseUserEntity userEntity, List<Integer> shopSeqList, List<Integer> yearList,List<Integer> seasonSeqList, List<Integer> categorySeqList, Integer ranking, Integer orderBy, Integer orderDir, Date saleTimeStart, Date saleTimeEnd,Page<Map<String,Object>> page) throws Exception {
		Map<String,Object> map = new HashMap<>(10);
		//如果门店序号中存在0,则需添加-1,0和-1都是总仓序号
		if(shopSeqList != null && shopSeqList.size() > 0) {
			for (Integer shopSeq : shopSeqList) {
				if(shopSeq == 0) {
					shopSeqList.add(-1);
					break;
				}
			}
			map.put("shopSeq",shopSeqList.get(0));
		}else {
			map.put("shopSeq",0);
		}
		//总部账号
		if(userEntity.getShopSeq() == null) {
			map.put("type",0);
			//门店账号
		}else {
			map.put("type",1);
			if(shopSeqList == null || shopSeqList.size() == 0) {
                map.put("type",0);
			}
		}
		map.put("shopSeqList",shopSeqList);
		map.put("companySeq",userEntity.getCompanySeq());
		map.put("yearList",yearList);
		map.put("ranking",ranking);
		map.put("orderBy",orderBy);
		map.put("orderDir",orderDir);
		map.put("saleTimeStart",saleTimeStart);
		map.put("saleTimeEnd",saleTimeEnd);
        map.put("seasonSeqList",seasonSeqList);
        List<Map<String,Object>> allShoesList = saleShoesDetailDao.selectSalableShoes(map);
		//获取根据品类筛选的鞋子
        map.put("categorySeqList",categorySeqList);
        List<Map<String,Object>> categoryShoesList = saleShoesDetailDao.selectSalableShoes(map);
        List<Map<String,Object>> shoesList = new ArrayList<>(10);
        //如果品类不为空,比较品类筛选出的鞋子和所有鞋子(筛选出品类的鞋子要包含在所有鞋子中)
        if(categorySeqList != null && categorySeqList.size() > 0) {
            for(Map<String,Object> shoes : allShoesList) {
                for(Map<String,Object> categoryShoes : categoryShoesList) {
                    if(shoes.get("seq").equals(categoryShoes.get("seq"))) {
                        shoesList.add(shoes);
                    }
                }
            }
            //如果品类为空,返回所有鞋子
        }else {
            for(Map<String,Object> shoes : allShoesList) {
                shoesList.add(shoes);
            }
        }
        int day = (int) ((saleTimeEnd.getTime() - saleTimeStart.getTime()) / (24 * 60 * 60 * 1000)) + 1;
        Date newStartTime = getDateBefore(saleTimeStart,day);
        Date newEndTime = getDateBefore(saleTimeEnd,day);
        map.put("saleTimeStart",newStartTime);
        map.put("saleTimeEnd",newEndTime);
        map.put("orderType",1);
        List<Map<String,Object>> beforeShoesList = saleShoesDetailDao.selectBeforeSalableShoes(map);
        for (Map<String,Object> shoes : shoesList) {
			//图片路径
			shoes.put("img",getImgPath(shoes.get("goodId").toString()) + shoes.get("img"));
			Integer rank = Integer.parseInt(shoes.get("rank").toString());
			for(Map<String,Object> beforeShoes : beforeShoesList) {
                if(shoes.get("seq").equals(beforeShoes.get("seq"))) {
                	Integer beforeRank = Integer.parseInt(beforeShoes.get("rank").toString());
                	Integer circleCompare = beforeRank - rank;
                    shoes.put("circleCompare",circleCompare);
                    break;
                }
            }
		}
		return shoesList;
	}

	@Override
	public List<Map<String, Object>> selectUnsalableShoes(BaseUserEntity userEntity, List<Integer> shopSeqList, List<Integer> yearList,List<Integer> seasonSeqList, List<Integer> categorySeqList, Integer ranking, Integer orderBy, Integer orderDir, Date saleTimeStart, Date saleTimeEnd,Page<Map<String,Object>> page) throws Exception {
		Map<String,Object> map = new HashMap<>(10);
		//如果门店序号中存在0,则需添加-1,0和-1都是总仓序号
		if(shopSeqList != null && shopSeqList.size() > 0) {
			for (Integer shopSeq : shopSeqList) {
				if(shopSeq == 0) {
					shopSeqList.add(-1);
					break;
				}
			}
			map.put("shopSeq",shopSeqList.get(0));
		}else {
			map.put("shopSeq",0);
		}
		//总部账号
		if(userEntity.getShopSeq() == null) {
			map.put("type",0);
		//门店账号
		}else {
			map.put("type",1);
			if(shopSeqList == null || shopSeqList.size() == 0) {
				map.put("type",0);
			}
		}
		map.put("shopSeqList",shopSeqList);
		map.put("companySeq",userEntity.getCompanySeq());
		map.put("yearList",yearList);
		map.put("ranking",ranking);
		map.put("orderBy",orderBy);
		map.put("orderDir",orderDir);
		map.put("saleTimeStart",saleTimeStart);
		map.put("saleTimeEnd",saleTimeEnd);
		map.put("seasonSeqList",seasonSeqList);
		List<Map<String,Object>> allShoesList = saleShoesDetailDao.selectUnsalableShoes(map);
		//获取根据品类筛选的鞋子
		map.put("categorySeqList",categorySeqList);
		List<Map<String,Object>> categoryShoesList = saleShoesDetailDao.selectUnsalableShoes(map);
		List<Map<String,Object>> shoesList = new ArrayList<>(10);
		//如果品类不为空,比较品类筛选出的鞋子和所有鞋子(筛选出品类的鞋子要包含在所有鞋子中)
		if(categorySeqList != null && categorySeqList.size() > 0) {
			for(Map<String,Object> shoes : allShoesList) {
				for(Map<String,Object> categoryShoes : categoryShoesList) {
					if(shoes.get("seq").equals(categoryShoes.get("seq"))) {
						shoesList.add(shoes);
					}
				}
			}
			//如果品类为空,返回所有鞋子
		}else {
			for(Map<String,Object> shoes : allShoesList) {
				shoesList.add(shoes);
			}
		}
		int day = (int) ((saleTimeEnd.getTime() - saleTimeStart.getTime()) / (24 * 60 * 60 * 1000)) + 1;
		Date newStartTime = getDateBefore(saleTimeStart,day);
		Date newEndTime = getDateBefore(saleTimeEnd,day);
		map.put("saleTimeStart",newStartTime);
		map.put("saleTimeEnd",newEndTime);
		map.put("orderType",0);
		List<Map<String,Object>> beforeShoesList = saleShoesDetailDao.selectBeforeSalableShoes(map);
		for (Map<String,Object> shoes : shoesList) {
			//图片路径
			shoes.put("img",getImgPath(shoes.get("goodId").toString()) + shoes.get("img"));
			Integer rank = Integer.parseInt(shoes.get("rank").toString());
			for(Map<String,Object> beforeShoes : beforeShoesList) {
				if(shoes.get("seq").equals(beforeShoes.get("seq"))) {
					Integer beforeRank = Integer.parseInt(beforeShoes.get("rank").toString());
					Integer circleCompare = beforeRank - rank;
					shoes.put("circleCompare",circleCompare);
					break;
				}
			}
		}
		return shoesList;
	}

    /**
     * 计算日期
     * @param d
     * @param day
     * @return
     */
    private Date getDateBefore(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
        return now.getTime();
    }

    /**
     * 获取图片路径
     * @param folder
     * @return
     */
    private String getImgPath(String folder) {
		return configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/" + configUtils.getGoodsShoes() + "/" + folder + "/";
	}

	@Override
	public Map<String, Object> selectYearStockStructure(BaseUserEntity userEntity,Integer shopSeq,List<Integer> yearList) throws Exception {
        List<Integer> shopSeqList = new ArrayList<>(10);
		if(shopSeq != null) {
			shopSeqList.add(shopSeq);
			//如果门店序号中存在0,则需添加-1,0和-1都是总仓序号
			if(shopSeq == 0) {
				shopSeqList.add(-1);
			}
		}
        Map<String,Object> map = new HashMap<>(10);
    	map.put("shopSeqList",shopSeqList);
    	map.put("companySeq",userEntity.getCompanySeq());
    	map.put("yearList",yearList);
        Map<String,Object> result = new HashMap<>(10);
        //库存结构
        List<Map<String, Object>> stockStructure = shoesDataDao.selectYearStockStructure(map);
        //不分页查询鞋子列表
        List<Map<String, Object>> shoesStock = shoesDataDao.selectShoesStock(map,new Page<>(1,Integer.MAX_VALUE));
        Integer stock = 0;
        if(shoesStock != null) {
        	for (Map<String,Object> shoes : shoesStock) {
        		stock += Integer.parseInt(shoes.get("stock").toString());
			}
            result.put("skuNum",shoesStock.size());
        	result.put("stock",stock);
        }else {
            result.put("skuNum",0);
            result.put("stock",0);
        }
        for (Map<String,Object> stockDetail : stockStructure) {
            if(stock != 0) {
                BigDecimal percent = new BigDecimal(stockDetail.get("stock").toString()).divide(new BigDecimal(stock),2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                stockDetail.put("percent",percent + "%");
            }else {
                stockDetail.put("percent","0%");
            }
			List<Integer> years = new ArrayList<>(10);
            years.add((Integer) stockDetail.get("year"));
            map.put("yearList",years);
			List<Map<String,Object>> shoesList = shoesDataDao.selectShoesStock(map,new Page<>(0,Integer.MAX_VALUE));
			if(shoesList != null) {
				stockDetail.put("skuNum",shoesList.size());
			}else {
				stockDetail.put("skuNum",0);
			}
        }
        result.put("stockStructure",stockStructure);
        return result;
	}

	@Override
	public Map<String, Object> selectSeasonStockStructure(BaseUserEntity userEntity,Integer shopSeq,List<Integer> yearList,List<Integer> seasonSeqList) throws Exception {
        List<Integer> shopSeqList = new ArrayList<>(10);
		if(shopSeq != null) {
			shopSeqList.add(shopSeq);
			//如果门店序号中存在0,则需添加-1,0和-1都是总仓序号
			if(shopSeq == 0) {
				shopSeqList.add(-1);
			}
		}
        Map<String,Object> map = new HashMap<>(10);
        map.put("shopSeqList",shopSeqList);
        map.put("companySeq",userEntity.getCompanySeq());
		map.put("yearList",yearList);
		map.put("seasonSeqList",seasonSeqList);
        Map<String,Object> result = new HashMap<>(10);
        List<Map<String, Object>> stockStructure = shoesDataDao.selectSeasonStockStructure(map);
        result.put("stockStructure",stockStructure);
        List<Map<String, Object>> shoesStock = shoesDataDao.selectShoesStock(map,new Page<>(1,Integer.MAX_VALUE));
        Integer stock = 0;
		if(shoesStock != null) {
			for (Map<String,Object> shoes : shoesStock) {
				stock += Integer.parseInt(shoes.get("stock").toString());
			}
			result.put("skuNum",shoesStock.size());
			result.put("stock",stock);
		}else {
			result.put("skuNum",0);
			result.put("stock",0);
		}
        for (Map<String,Object> stockDetail : stockStructure) {
            if(stock != 0) {
                BigDecimal percent = new BigDecimal(stockDetail.get("stock").toString()).divide(new BigDecimal(stock),2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                stockDetail.put("percent",percent + "%");
            }else {
                stockDetail.put("percent","0%");
            }
			List<Integer> seasonSeqs = new ArrayList<>(10);
			seasonSeqs.add((Integer) stockDetail.get("seq"));
			map.put("seasonSeqList",seasonSeqs);
			List<Map<String,Object>> shoesList = shoesDataDao.selectShoesStock(map,new Page<>(0,Integer.MAX_VALUE));
			if(shoesList != null) {
				stockDetail.put("skuNum",shoesList.size());
			}else {
				stockDetail.put("skuNum",0);
			}
        }
        return result;
	}

	@Override
	public Map<String, Object> selectCategoryStockStructure(BaseUserEntity userEntity,Integer shopSeq,List<Integer> yearList,List<Integer> seasonSeqList,Integer type, List<Integer> categorySeqList) throws Exception {
        List<Integer> shopSeqList = new ArrayList<>(10);
		if(shopSeq != null) {
			shopSeqList.add(shopSeq);
			//如果门店序号中存在0,则需添加-1,0和-1都是总仓序号
			if(shopSeq == 0) {
				shopSeqList.add(-1);
			}
		}

        //类别序号列表为空时,查询第一大类
        if(type == 0) {
		    if(categorySeqList == null) {
                categorySeqList = new ArrayList<>(10);
            }
            if(categorySeqList.size() == 0) {
                categorySeqList.add(0);
            }
        }else {
		    if(categorySeqList == null) {
                categorySeqList = new ArrayList<>(10);
            }
            if(categorySeqList.size() == 0) {
                Wrapper<GoodsCategoryEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("ParentSeq",0);
                List<GoodsCategoryEntity> categoryList = goodsCategoryDao.selectList(wrapper);
                for(GoodsCategoryEntity goodsCategoryEntity : categoryList) {
                    categorySeqList.add(goodsCategoryEntity.getSeq());
                }
            }
        }
        Map<String,Object> result = new HashMap<>(10);
        //查询类别
        Wrapper<GoodsCategoryEntity> wrapper = new EntityWrapper<>();
		wrapper.in("ParentSeq",categorySeqList);
        List<GoodsCategoryEntity> categoryList = goodsCategoryDao.selectList(wrapper);
        Map<String,Object> map = new HashMap<>(10);
        map.put("shopSeqList",shopSeqList);
        map.put("companySeq",userEntity.getCompanySeq());
		map.put("yearList",yearList);
		map.put("seasonSeqList",seasonSeqList);
		List<Map<String,Object>> stockStructure = new ArrayList<>(10);
		for (GoodsCategoryEntity goodsCategoryEntity : categoryList) {
            map.put("categorySeq",goodsCategoryEntity.getSeq());
            Map<String, Object> stock = shoesDataDao.selectCategoryStockStructure(map);
            stock.put("categoryName",goodsCategoryEntity.getName());
            stock.put("categorySeq",goodsCategoryEntity.getSeq());
            stockStructure.add(stock);
        }
		result.put("stockStructure",stockStructure);
		map.put("categorySeqList",categorySeqList);
        List<Map<String, Object>> shoesStock = shoesDataDao.selectShoesStock(map,new Page<>(1,Integer.MAX_VALUE));
        Integer stock = 0;
		if(shoesStock != null) {
			for (Map<String,Object> shoes : shoesStock) {
				stock += Integer.parseInt(shoes.get("stock").toString());
			}
			result.put("skuNum",shoesStock.size());
			result.put("stock",stock);
		}else {
			result.put("skuNum",0);
			result.put("stock",0);
		}
        for (Map<String,Object> stockDetail : stockStructure) {
            if(stock != 0) {
                BigDecimal percent = new BigDecimal(stockDetail.get("stock").toString()).divide(new BigDecimal(stock),2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                stockDetail.put("percent",percent + "%");
            }else {
                stockDetail.put("percent","0%");
            }
			List<Integer> categorySeqs = new ArrayList<>(10);
			categorySeqs.add((Integer) stockDetail.get("categorySeq"));
			map.put("categorySeqList",categorySeqs);
			List<Map<String,Object>> shoesList = shoesDataDao.selectShoesStock(map,new Page<>(0,Integer.MAX_VALUE));
			if(shoesList != null) {
				stockDetail.put("skuNum",shoesList.size());
			}else {
				stockDetail.put("skuNum",0);
			}
        }
		return result;
	}

	@Override
	public List<Map<String, Object>> selectShoesStock(BaseUserEntity userEntity,Integer shopSeq,List<Integer> yearList,List<Integer> seasonSeqList, List<Integer> categorySeqList,Page<Map<String,Object>> page) throws Exception {
        List<Integer> shopSeqList = new ArrayList<>(10);
		if(shopSeq != null) {
			shopSeqList.add(shopSeq);
			//如果门店序号中存在0,则需添加-1,0和-1都是总仓序号
			if(shopSeq == 0) {
				shopSeqList.add(-1);
			}
		}
        Map<String,Object> map = new HashMap<>(10);
        map.put("shopSeqList",shopSeqList);
        map.put("companySeq",userEntity.getCompanySeq());
		map.put("yearList",yearList);
		map.put("seasonSeqList",seasonSeqList);
		map.put("categorySeqList",categorySeqList);
		List<Map<String, Object>> shoesStock = shoesDataDao.selectShoesStock(map,page);
		for (Map<String,Object> shoes : shoesStock) {
		    shoes.put("img",getImgPath(shoes.get("goodId").toString()) + shoes.get("img"));
        }
		return shoesStock;
	}

	@Override
	public List<Map<String, Object>> selectOldShoes(BaseUserEntity userEntity,List<Integer> shopSeqList, List<Integer> yearList, List<Integer> categorySeqList, List<Integer> seasonSeqList, Date saleTimeStart, Date saleTimeEnd, Page<Map<String, Object>> page) throws Exception {
		//如果门店序号中存在0,则需添加-1,0和-1都是总仓序号
		if(shopSeqList != null && shopSeqList.size() > 0) {
			for (Integer shopSeq : shopSeqList) {
				if(shopSeq == 0) {
					shopSeqList.add(-1);
					break;
				}
			}
		}
		//用于查询库存汇总
		List<Integer> shopSeqs = null;
		//总部账号
		if(userEntity.getShopSeq() == null) {
			//门店序号列表不为空,循环添加库存查询门店列表
			if(shopSeqList != null && shopSeqList.size() > 0) {
				shopSeqs = new ArrayList<>(10);
				for(Integer shopSeq : shopSeqList) {
					shopSeqs.add(shopSeq);
				}
				//总仓库存添加总仓序号
				shopSeqs.add(-1);
				shopSeqs.add(0);
			}
			//门店账号
		}else {
            //门店序号列表不为空,循环添加库存查询门店列表
            if(shopSeqList != null && shopSeqList.size() > 0) {
				shopSeqs = new ArrayList<>(10);
				for(Integer shopSeq : shopSeqList) {
					shopSeqs.add(shopSeq);
				}
				//总仓库存添加总仓序号
				shopSeqs.add(-1);
				shopSeqs.add(0);
			}
		}
    	Map<String,Object> map = new HashMap<>(10);
		map.put("shopSeqList",shopSeqList);
		map.put("companySeq",userEntity.getCompanySeq());
		map.put("yearList",yearList);
		map.put("categorySeqList",categorySeqList);
		map.put("seasonSeqList",seasonSeqList);
		map.put("saleTimeStart",saleTimeStart);
		map.put("saleTimeEnd",saleTimeEnd);
		//分页查询
		List<Map<String,Object>> shoesList = shoesInfoDao.selectOldShoes(map,page);
		//不分页查询
        page = new Page<>(0,Integer.MAX_VALUE);
        List<Map<String,Object>> allShoesList = shoesInfoDao.selectOldShoes(map,page);
		List<Integer> satisfyShoesSeqs = new ArrayList<>(10);
        Integer totalSaleNum = 0;
        for (Map<String,Object> shoes : allShoesList) {
            totalSaleNum += (Integer) shoes.get("salesNum");
        }
		for(Map<String,Object> shoes : allShoesList) {
			satisfyShoesSeqs.add(Integer.parseInt(shoes.get("seq").toString()));
		}
		//断码鞋子序号列表
		List<Integer> breakShoesSeqList = shoesDataService.getBreakShoesSeqList(userEntity.getCompanySeq(), satisfyShoesSeqs,shopSeqList);
		//缺码鞋子序号列表
		List<Integer> absenceShoesSeqList = shoesDataService.getAbsenceShoesSeqList(userEntity.getCompanySeq(), satisfyShoesSeqs,shopSeqList);
		Integer totalStockNum = shoesDataService.totalStockNum(satisfyShoesSeqs);
		Integer totalNum = shoesDataService.totalNum(satisfyShoesSeqs,shopSeqs);
		Integer totalShopNum = shoesDataService.totalShopNum(satisfyShoesSeqs,shopSeqs);
		for (Map<String,Object> shoes : shoesList) {
			//图片路径
			shoes.put("img",getImgPath(shoes.get("goodId").toString()) + shoes.get("img"));
			//总SKU数
			shoes.put("totalSKUNum",page.getTotal());
			//总仓库存
			shoes.put("totalStockNum",totalStockNum);
			//总库存
			shoes.put("totalNum",totalNum);
			//门店总库存
			shoes.put("totalShopNum",totalShopNum);
            //总销量
            shoes.put("totalSaleNum",totalSaleNum);
			//断码SKU数
			shoes.put("breakSKUNum",breakShoesSeqList.size());
			//缺码SKU数
			shoes.put("absenceSKUNum",absenceShoesSeqList.size());
		}
    	return shoesList;
	}

	@Override
	public List<Map<String, Object>> selectBreakOrAbsenceShoes(BaseUserEntity userEntity, List<Integer> shopSeqList, List<Integer> yearList, List<Integer> seasonSeqList,Integer type, Page<Map<String, Object>> page) throws Exception {
        //如果门店序号中存在0,则需添加-1,0和-1都是总仓序号
		if(shopSeqList != null && shopSeqList.size() > 0) {
			for (Integer shopSeq : shopSeqList) {
				if(shopSeq == 0) {
					shopSeqList.add(-1);
					break;
				}
			}
		}
        //用于查询库存汇总
        List<Integer> shopSeqs = null;
        //总部账号
        if(userEntity.getShopSeq() == null) {
            //门店序号列表不为空,循环添加库存查询门店列表
            if(shopSeqList != null && shopSeqList.size() > 0) {
                shopSeqs = new ArrayList<>(10);
                for(Integer shopSeq : shopSeqList) {
                    shopSeqs.add(shopSeq);
                }
                //总仓库存添加总仓序号
                shopSeqs.add(-1);
                shopSeqs.add(0);
            }
            //门店账号
        }else {
            //门店序号列表不为空,循环添加库存查询门店列表
            if(shopSeqList != null && shopSeqList.size() > 0) {
                shopSeqs = new ArrayList<>(10);
                for(Integer shopSeq : shopSeqList) {
                    shopSeqs.add(shopSeq);
                }
                //总仓库存添加总仓序号
                shopSeqs.add(-1);
                shopSeqs.add(0);
            }
        }
        Map<String,Object> map = new HashMap<>(10);
        map.put("shopSeqList",shopSeqList);
        Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("Company_Seq",userEntity.getCompanySeq());
        wrapper.in("Year",yearList);
        wrapper.in("Season_Seq",seasonSeqList);
        wrapper.setSqlSelect("Seq");
        List<Object> shoesSeqs = goodsShoesDao.selectObjs(wrapper);
        List<Integer> shoesSeqsInt = new ArrayList<>(10);
        for(Object shoesSeq : shoesSeqs) {
            shoesSeqsInt.add((Integer) shoesSeq);
        }
        List<Integer> shoesSeqList = new ArrayList<>(10);
        if(shoesSeqsInt.size() == 0) {
        	return new ArrayList<>();
		}
        //断码
        if(type == 0) {
            shoesSeqList = shoesDataService.getBreakShoesSeqList(userEntity.getCompanySeq(), shoesSeqsInt,shopSeqList);
        //缺码
        }else {
            shoesSeqList = shoesDataService.getAbsenceShoesSeqList(userEntity.getCompanySeq(), shoesSeqsInt,shopSeqList);
        }
        String shoesSeqString = null;
        if(shoesSeqList != null && shoesSeqList.size() > 0) {
            shoesSeqString = "(" + Joiner.on(",").join(shoesSeqList) + ")";
        }else {
        	return new ArrayList<>(10);
		}
        map.put("shoesSeqs",shoesSeqString);
        //分页查询
        List<Map<String,Object>> shoesList = shoesInfoDao.selectBreakOrAbsenceShoes(map,page);
        page = new Page<>(0,Integer.MAX_VALUE);
        //不分页查询
        List<Map<String,Object>> allShoesList = shoesInfoDao.selectBreakOrAbsenceShoes(map,page);
        List<Integer> satisfyShoesSeqs = new ArrayList<>(10);
        for(Map<String,Object> shoes : allShoesList) {
            satisfyShoesSeqs.add(Integer.parseInt(shoes.get("seq").toString()));
        }
        Integer totalStockNum = shoesDataService.totalStockNum(satisfyShoesSeqs);
        Integer totalShopNum = shoesDataService.totalShopNum(satisfyShoesSeqs,shopSeqs);
        for (Map<String,Object> shoes : shoesList) {
            //图片路径
            shoes.put("img",getImgPath(shoes.get("goodId").toString()) + shoes.get("img"));
            //总SKU数
            shoes.put("totalSKUNum",page.getTotal());
            //总仓库存
            shoes.put("totalStockNum",totalStockNum);
            //总库存
            //shoes.put("totalNum",shoesDataService.totalNum(satisfyShoesSeqs,shopSeqs));
            //门店总库存
            shoes.put("totalShopNum",totalShopNum);
        }
        return shoesList;
	}




	@Override
	public Integer getShopStock(Integer shopSeq) {
		return shoesInfoDao.getShopStock(shopSeq);
	}

	@Override
	public Integer getTempStock(Integer shopSeq) {
		AllocateTransferShopTempStockEntity allocateTransferShopTempStockEntity=new AllocateTransferShopTempStockEntity();
		allocateTransferShopTempStockEntity.setShopSeq(shopSeq);
		allocateTransferShopTempStockEntity=allocateTransferShopTempStockDao.selectOne(allocateTransferShopTempStockEntity);
		if(allocateTransferShopTempStockEntity==null) {
			allocateTransferShopTempStockEntity=new AllocateTransferShopTempStockEntity();
			allocateTransferShopTempStockEntity.setShopSeq(shopSeq);
			Integer tempStock=shoesInfoDao.getShopStock(shopSeq);
			allocateTransferShopTempStockEntity.setTempStock(tempStock);
			allocateTransferShopTempStockEntity.setDel(0);
			allocateTransferShopTempStockDao.insert(allocateTransferShopTempStockEntity);
		}
		return allocateTransferShopTempStockEntity.getTempStock();
	}
	
	@Override
	public void updateTempStock(Integer shopSeq, Integer tempStock) {
		AllocateTransferShopTempStockEntity allocateTransferShopTempStockEntity=new AllocateTransferShopTempStockEntity();
		allocateTransferShopTempStockEntity.setShopSeq(shopSeq);
		allocateTransferShopTempStockEntity=allocateTransferShopTempStockDao.selectOne(allocateTransferShopTempStockEntity);
		if(allocateTransferShopTempStockEntity!=null) {
			allocateTransferShopTempStockEntity.setTempStock(tempStock);
			allocateTransferShopTempStockDao.updateById(allocateTransferShopTempStockEntity);
		}
	}
	
	@Override
	public void updateChangeTempStock(Integer shopSeq, Integer changeTempStock) {
		AllocateTransferShopTempStockEntity allocateTransferShopTempStockEntity=new AllocateTransferShopTempStockEntity();
		allocateTransferShopTempStockEntity.setShopSeq(shopSeq);
		allocateTransferShopTempStockEntity=allocateTransferShopTempStockDao.selectOne(allocateTransferShopTempStockEntity);
		if(allocateTransferShopTempStockEntity!=null) {
			allocateTransferShopTempStockEntity.setTempStock(allocateTransferShopTempStockEntity.getTempStock()+changeTempStock);
			allocateTransferShopTempStockDao.updateById(allocateTransferShopTempStockEntity);
		}
	}
	
	@Override
	public void restoreTempStock() {
		Wrapper<AllocateTransferShopTempStockEntity> wrapper=new EntityWrapper<AllocateTransferShopTempStockEntity>();
		List<AllocateTransferShopTempStockEntity> AllocateTransferShopTempStocks=allocateTransferShopTempStockDao.selectList(wrapper);
		for (AllocateTransferShopTempStockEntity allocateTransferShopTempStockEntity : AllocateTransferShopTempStocks) {
			Integer tempStock=shoesInfoDao.getShopStock(allocateTransferShopTempStockEntity.getShopSeq());
			allocateTransferShopTempStockEntity.setTempStock(tempStock);
			allocateTransferShopTempStockDao.updateById(allocateTransferShopTempStockEntity);
		}
	}
	

    @Override
    public List<Map<String, Object>> selectExportOutShoes(BaseUserEntity userEntity, List<Integer> yearList, List<Integer> categorySeqList, List<Integer> seasonSeqList, Integer orderBy, Integer orderDir, Date saleTimeStart, Date saleTimeEnd,Integer isTransferOut, Page<Map<String,Object>> page) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("companySeq",userEntity.getCompanySeq());
        map.put("yearList",yearList);
        map.put("categorySeqList",categorySeqList);
        map.put("seasonSeqList",seasonSeqList);
        map.put("orderBy",orderBy);
        map.put("orderDir",orderDir);
        map.put("saleTimeStart",saleTimeStart);
        map.put("saleTimeEnd",saleTimeEnd);
        map.put("isTransferOut", isTransferOut);
        map.put("shopSeq", userEntity.getShopSeq());
        //分页查询
        List<Map<String,Object>> shoesList = allocateTransferFictitiousMallDao.selectExportOutShoes(map,page);
        page = new Page<>(0,Integer.MAX_VALUE);
        //不分页查询
        List<Map<String,Object>> allShoesList = allocateTransferFictitiousMallDao.selectExportOutShoes(map,page);
        List<Integer> satisfyShoesSeqs = new ArrayList<>(10);
        for(Map<String,Object> shoes : allShoesList) {
            satisfyShoesSeqs.add(Integer.parseInt(shoes.get("seq").toString()));
        }
        Wrapper<AllocateTransferFictitiousMallEntity> wrapper = new EntityWrapper<>();
        List<AllocateTransferFictitiousMallEntity> allocateTransferFictitiousMallEntities = allocateTransferFictitiousMallDao.selectList(wrapper);
        Integer totalStockNum = shoesDataService.totalStockNum(satisfyShoesSeqs);
        Integer totalNum = shoesDataService.totalNum(satisfyShoesSeqs,null);
        Integer totalShopNum = shoesDataService.totalShopNum(satisfyShoesSeqs,null);
        for (Map<String,Object> shoes : shoesList) {
            shoes.put("shopGood",0);
            for(AllocateTransferFictitiousMallEntity allocateTransferFictitiousMallEntity : allocateTransferFictitiousMallEntities) {
                if(allocateTransferFictitiousMallEntity.getShopSeq().equals(userEntity.getShopSeq()) && allocateTransferFictitiousMallEntity.getShoesSeq().equals((Integer) shoes.get("seq"))) {
                    shoes.put("shopGood",1);
                    break;
                }
            }
            //图片路径
            shoes.put("img",getImgPath(shoes.get("goodId").toString()) + shoes.get("img"));
            //总SKU数
            shoes.put("totalSKUNum",page.getTotal());
            //总仓库存
            shoes.put("totalStockNum",totalStockNum);
            //总库存
            shoes.put("totalNum",totalNum);
            //门店总库存
            shoes.put("totalShopNum",totalShopNum);
            //总调配仓
            shoes.put("totalTransferOutNum",allocateTransferFictitiousMallDao.selectExportShoesNumByShopSeq(null, (int)shoes.get("seq")));
            //门店调配仓
            shoes.put("totalShopTransferOutNum",allocateTransferFictitiousMallDao.selectExportShoesNumByShopSeq(userEntity.getShopSeq(), (Integer)shoes.get("seq")));
            
            
            //门店是否有调出记录
            Wrapper<AllocateTransferTransferInApplicationEntity> wrapper1=new EntityWrapper<AllocateTransferTransferInApplicationEntity>();
            wrapper1.where("OutShop_Seq ={0} AND Shoes_Seq={1}", userEntity.getShopSeq(), (Integer)shoes.get("seq"));
            List<AllocateTransferTransferInApplicationEntity> allocateTransferTransferInApplicationEntities=allocateTransferTransferInApplicationDao.selectList(wrapper1);
            if(allocateTransferTransferInApplicationEntities.size()>0) {
            	shoes.put("isAllocateTransfer", 1);
            } else {
            	shoes.put("isAllocateTransfer", 0);
            }
            
            //如果是门店用户，展示当前货品是否已添加虚拟商城的标识
            map.put("isTransferOut", 0);
            if(userEntity.getShopSeq() != null) {
               /* if(allocateTransferFictitiousMallService.isTransferOutToFictitiousMall(userEntity.getShopSeq(), (Integer)shoes.get("seq"))) {
                	map.put("isTransferOut", 1);
                }*/
            }
            
            
        }
        return shoesList;
    }

	@Override
	public List<Integer> selectLastSeason() throws Exception {
    	String seasonName = "冬";
    	List<Object> seasonSeqList = getSeasonSeq(seasonName);
    	List<Integer> seasonSeqs = new ArrayList<>(10);
        if(checkSale(seasonSeqList)) {
            addSeasonSeq(seasonSeqList,seasonSeqs);
            seasonName = "秋";
            seasonSeqList = getSeasonSeq(seasonName);
            addSeasonSeq(seasonSeqList,seasonSeqs);
        }else {
            seasonName = "秋";
            seasonSeqList = getSeasonSeq(seasonName);
            if(checkSale(seasonSeqList)) {
                addSeasonSeq(seasonSeqList,seasonSeqs);
                seasonName = "夏";
                seasonSeqList = getSeasonSeq(seasonName);
                addSeasonSeq(seasonSeqList,seasonSeqs);
            }else {
                seasonName = "夏";
                seasonSeqList = getSeasonSeq(seasonName);
                if(checkSale(seasonSeqList)) {
                    addSeasonSeq(seasonSeqList,seasonSeqs);
                    seasonName = "春";
                    seasonSeqList = getSeasonSeq(seasonName);
                    addSeasonSeq(seasonSeqList,seasonSeqs);
                }else {
                    seasonName = "春";
                    seasonSeqList = getSeasonSeq(seasonName);
                    if(checkSale(seasonSeqList)) {
                        addSeasonSeq(seasonSeqList,seasonSeqs);
                        seasonName = "冬";
                        seasonSeqList = getSeasonSeq(seasonName);
                        addSeasonSeq(seasonSeqList,seasonSeqs);
                    }
                }
            }
        }
		return seasonSeqs;
	}

	/**
	 * 确认是否有过销售
	 * @param seasonSeqList
	 * @return
	 * @throws Exception
	 */
	private boolean checkSale(List<Object> seasonSeqList) throws Exception {
        Calendar date = Calendar.getInstance();
        Integer year = date.get(Calendar.YEAR);
        Wrapper<GoodsShoesEntity> goodsShoesEntityWrapper = new EntityWrapper<>();
        goodsShoesEntityWrapper.in("Season_Seq",seasonSeqList).eq("Year",year).setSqlSelect("Seq");
        List<Object> shoesSeqList = goodsShoesDao.selectObjs(goodsShoesEntityWrapper);
        if(shoesSeqList == null || shoesSeqList.size() == 0) {
			return false;
		}
        Wrapper<SaleShoesDetailEntity> saleShoesDetailEntityWrapper = new EntityWrapper<>();
        saleShoesDetailEntityWrapper.in("ShoeSeq",shoesSeqList);
        List<SaleShoesDetailEntity> saleShoesDetailEntities = saleShoesDetailDao.selectList(saleShoesDetailEntityWrapper);
        if(saleShoesDetailEntities.size() > 0) {
            return true;
        }else {
            return false;
        }
    }

    private List<Object> getSeasonSeq(String seasonName) throws Exception {
        Wrapper<GoodsSeasonEntity> goodsSeasonEntityWrapper = new EntityWrapper<>();
        goodsSeasonEntityWrapper.like("SeasonName",seasonName).setSqlSelect("Seq");
        List<Object> seasonSeqList = goodsSeasonDao.selectObjs(goodsSeasonEntityWrapper);
        return seasonSeqList;
    }

    private void addSeasonSeq(List<Object> seasonSeqList,List<Integer> seasonSeqs) throws Exception {
        for (Object seq : seasonSeqList) {
            if(seasonSeqs.contains((Integer)seq)) {
                continue;
            }
            seasonSeqs.add((Integer) seq);
        }
    }


}
