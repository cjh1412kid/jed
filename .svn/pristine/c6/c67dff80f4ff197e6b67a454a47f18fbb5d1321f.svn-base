package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import io.nuite.modules.order_platform_app.dao.SaleShoesDetailDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDailyDetailDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.order_platform_app.service.SaleShoesDetailService;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleShoesDetailServiceImpl extends ServiceImpl<SaleShoesDetailDao, SaleShoesDetailEntity> implements SaleShoesDetailService {

    @Autowired
    private SaleShoesDetailDao saleShoesDetailDao;

    @Autowired
    private ShoesDataDailyDetailDao shoesDataDailyDetailDao;

    @Autowired
    private ShoesDataDao shoesDataDao;

    @Autowired
    private GoodsSizeDao goodsSizeDao;

    @Autowired
    private GoodsShoesDao goodsShoesDao;

    /**
     * jrd 获取多个门店某一货号时间段内的销售量（双数）
     */
    @Override
    public Integer getShopsSaleNumOneShoesSeq(List<Object> shopSeqs, Integer shoesSeq, Date startDate, Date endDate) {
        Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
        wrapper.setSqlSelect("SUM (SaleCount)").in("ShopSeq", shopSeqs)
                .where("ShoeSeq = {0} AND SaleDate >= {1} AND SaleDate <= {2} ", shoesSeq, startDate, endDate);

        List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            return (Integer) list.get(0);
        } else {
            return 0;
        }
    }

    /**
     * jrd 获取多个门店某一货号时间段内的销售量（双数）
     */
    @Override
    public Integer getShopsTotalSaleNumOneShoesSeq(List<Object> shopSeqs, Integer shoesSeq, Date startDate, Date endDate) {
        Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
        wrapper.setSqlSelect("SUM (SaleCount)").in("ShopSeq", shopSeqs).where("ShoeSeq = {0} AND SaleDate >= {1} AND SaleDate <= {2} ", shoesSeq, startDate, endDate);
        ;
        List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            return (Integer) list.get(0);
        } else {
            return 0;
        }
    }


    /**
     * jrd 获取多个门店时间段内某一货号的平均售价
     */
    @Override
    public BigDecimal getShopsAvgSalePriceOneShoesSeq(List<Object> shopSeqs, Integer shoesSeq, Date startDate, Date endDate) {
        Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
        wrapper.setSqlSelect("SUM (SaleCount * RealPrice) / NULLIF(SUM (SaleCount),0) AS avgPrice").in("ShopSeq", shopSeqs)
                .where("ShoeSeq = {0} AND SaleDate >= {1} AND SaleDate <= {2} ", shoesSeq, startDate, endDate);

        List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            return (BigDecimal) list.get(0);
        } else {
            return BigDecimal.ZERO;
        }
    }


    /**
     * jrd获取总部和门店时间段内的进货量
     */
    @Override
    public Integer getHqAndShopInNumOneShoesSeq(Integer shoesSeq, Date startDate, Date endDate) {
        Wrapper<ShoesDataDailyDetailEntity> inWrapper = new EntityWrapper<ShoesDataDailyDetailEntity>();
        inWrapper.setSqlSelect("SUM (Count)")
                .where("Shoes_Seq = {0} AND Type = 1", shoesSeq, startDate, endDate);
        List<Object> inList = shoesDataDailyDetailDao.selectObjs(inWrapper);

        Wrapper<ShoesDataDailyDetailEntity> outWrapper = new EntityWrapper<ShoesDataDailyDetailEntity>();
        outWrapper.setSqlSelect("SUM (Count)")
                .where("Shoes_Seq = {0} AND Type = 2", shoesSeq, startDate, endDate);
        List<Object> outList = shoesDataDailyDetailDao.selectObjs(outWrapper);
        Integer inNum = 0;
        Integer outNum = 0;
        if (inList != null && inList.size() > 0 && inList.get(0) != null) {
            inNum = (Integer) inList.get(0);
        }
        if (outList != null && outList.size() > 0 && outList.get(0) != null) {
            outNum = (Integer) outList.get(0);
        }
        return inNum - outNum;
    }


    /**
     * jrd获取某一门店时间段内的进货量
     */
    @Override
    public Integer getShopInNumOneShoesSeq(Integer shopSeq, Integer shoesSeq, Date startDate, Date endDate) {
        Wrapper<ShoesDataDailyDetailEntity> inWrapper = new EntityWrapper<ShoesDataDailyDetailEntity>();
        inWrapper.setSqlSelect("SUM (Count)")
                .where("Shop_Seq = {0} AND Shoes_Seq = {1} AND Type = 1", shopSeq, shoesSeq, startDate, endDate);
        List<Object> inList = shoesDataDailyDetailDao.selectObjs(inWrapper);

        Wrapper<ShoesDataDailyDetailEntity> outWrapper = new EntityWrapper<ShoesDataDailyDetailEntity>();
        outWrapper.setSqlSelect("SUM (Count)")
                .where("Shop_Seq = {0} AND Shoes_Seq = {1} AND Type = 2", shopSeq, shoesSeq, startDate, endDate);
        List<Object> outList = shoesDataDailyDetailDao.selectObjs(outWrapper);

        Wrapper<ShoesDataDailyDetailEntity> turnWrapper = new EntityWrapper<ShoesDataDailyDetailEntity>();
        turnWrapper.setSqlSelect("SUM (Count)")
                .where("Shop_Seq = {0} AND Shoes_Seq = {1} AND Type = 5", shopSeq, shoesSeq, startDate, endDate);
        List<Object> turnList = shoesDataDailyDetailDao.selectObjs(turnWrapper);
        Integer inNum = 0;
        Integer outNum = 0;
        Integer turnNum = 0;
        if (inList != null && inList.size() > 0 && inList.get(0) != null) {
            inNum = (Integer) inList.get(0);
        }
        if (outList != null && outList.size() > 0 && outList.get(0) != null) {
            outNum = (Integer) outList.get(0);
        }
        if (turnList != null && turnList.size() > 0 && turnList.get(0) != null) {
            turnNum = (Integer) turnList.get(0);
        }
        return inNum - outNum + turnNum;
    }


    /**
     * jrd获取总部和门店某货号的库存量
     */
    @Override
    public Integer getHqAndShopStockNumOneShoesSeq(Integer shoesSeq) {
        Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
        wrapper.setSqlSelect("SUM (Stock)").where("Shoes_Seq = {0}", shoesSeq);
        List<Object> list = shoesDataDao.selectObjs(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            return (Integer) list.get(0);
        } else {
            return 0;
        }
    }

    /**
     * jrd获取总部和门店某货号的库存量
     */
    @Override
    public Integer getShopStockNumOneShoesSeq(Integer shopSeq, Integer shoesSeq) {
        Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
        wrapper.setSqlSelect("SUM (Stock)").where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq);
        List<Object> list = shoesDataDao.selectObjs(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            return (Integer) list.get(0);
        } else {
            return 0;
        }
    }


    /**
     * 某一门店在多个门店序号中时间段内鞋子的销量排名
     */
    @Deprecated
    @Override
    public Integer getShopSaleNumRankOneShoesSeq(List<Object> shopSeqs, Integer shopSeq, Integer shoesSeq, Date startDate,
                                                 Date endDate) {

        Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
        wrapper.setSqlSelect("ShopSeq AS shopSeq, RANK() OVER (ORDER BY SUM (SaleCount) DESC) AS No").in("ShopSeq", shopSeqs)
                .where("ShoeSeq = {0} AND SaleDate >= {1} AND SaleDate <= {2} ", shoesSeq, startDate, endDate)
                .groupBy("ShopSeq");

        List<Map<String, Object>> list = saleShoesDetailDao.selectMaps(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            for (Map<String, Object> map : list) {
                if (((Integer) map.get("shopSeq")).equals(shopSeq)) {
                    return ((Long) map.get("No")).intValue();
                }
            }
        }

        return null;

    }


    /**
     * 某一门店在多个门店序号中时间段内鞋子的库存排名
     */
    @Deprecated
    @Override
    public Integer getShopStockRankOneShoesSeq(List<Object> shopSeqs, Integer shopSeq, Integer shoesSeq) {

        Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
        wrapper.setSqlSelect("Shop_Seq AS shopSeq, RANK() OVER (ORDER BY SUM (Stock) DESC) AS No").in("Shop_Seq", shopSeqs)
                .where("Shoes_Seq = {0}", shoesSeq)
                .groupBy("Shop_Seq");

        List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            for (Map<String, Object> map : list) {
                if (((Integer) map.get("shopSeq")).equals(shopSeq)) {
                    return ((Long) map.get("No")).intValue();
                }
            }
        }

        return null;

    }


    /**
     * 获取总部和门店某货号鞋子的详细的库存列表
     */
    @Override
    public List<Map<String, Object>> getHqAndShopShoesStockDetail(Integer shoesSeq) {
        Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
        wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM (Stock) AS stock").where("Shoes_Seq = {0}", shoesSeq).groupBy("Size_Seq");
        List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            for (Map<String, Object> map : list) {
                //获取尺码值
                GoodsSizeEntity goodsSizeEntity = goodsSizeDao.selectById((Integer) map.get("sizeSeq"));
                map.put("size", goodsSizeEntity.getSizeName());
            }
            return list;
        } else {
            return null;
        }

    }


    /**
     * 获取门店某货号鞋子的详细的库存列表
     */
    @Override
    public List<Map<String, Object>> getShopShoesStockDetail(Integer shopSeq, Integer shoesSeq) {
        Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
        if (shopSeq > 0) {
            wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM (Stock) AS stock").where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq).groupBy("Size_Seq");
        } else {
            wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM (Stock) AS stock").where("Shop_Seq <= 0 AND Shoes_Seq = {1}", shopSeq, shoesSeq).groupBy("Size_Seq");
        }
        List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            for (Map<String, Object> map : list) {
                //获取尺码值
                GoodsSizeEntity goodsSizeEntity = goodsSizeDao.selectById((Integer) map.get("sizeSeq"));
                map.put("size", goodsSizeEntity.getSizeName());
            }
            return list;
        } else {
            return null;
        }

    }


    /**
     * 获取多个门店某货号鞋子的详细的库存列表
     */
    @Override
    public List<Map<String, Object>> getAllShopsShoesStockDetail(List<Object> shopSeqs, Integer shoesSeq) {
        Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
        wrapper.setSqlSelect("Size_Seq AS sizeSeq, SUM (Stock) AS stock").in("Shop_Seq", shopSeqs)
                .where("Shoes_Seq = {0}", shoesSeq).groupBy("Size_Seq");
        List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            for (Map<String, Object> map : list) {
                //获取尺码值
                GoodsSizeEntity goodsSizeEntity = goodsSizeDao.selectById((Integer) map.get("sizeSeq"));
                map.put("size", goodsSizeEntity.getSizeName());
            }
            return list;
        } else {
            return null;
        }

    }


    /**
     * （多个门店序号范围内）本货号在所有货号中的销量排名
     */
    @Override
    public Integer getGoodsIdSaleNumRank(List<Object> shopSeqs, Integer shoesSeq, Date startDate, Date endDate) throws Exception {
        Map<String, Object> params = new HashMap<>(10);
        GoodsShoesEntity goodsShoesEntity = goodsShoesDao.selectById(shoesSeq);
        params.put("list", shopSeqs);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("year", goodsShoesEntity.getYear());
        params.put("seasonSeq", goodsShoesEntity.getSeasonSeq());
        List<Map<String, Object>> list = saleShoesDetailDao.selectSaleNumRank(params);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            for (Map<String, Object> map : list) {
                if (((Integer) map.get("shoeSeq")).equals(shoesSeq)) {
                    return ((Long) map.get("No")).intValue();
                }
            }
        }

        return null;

    }

    /**
     * （多个门店序号范围内）本货号在所有货号中的销量排名
     */
    @Override
    public Integer getGoodsIdSaleNumRankByCategory(List<Object> shopSeqs, Integer shoesSeq, Date startDate, Date endDate) throws Exception {
        Map<String, Object> params = new HashMap<>(10);
        GoodsShoesEntity goodsShoesEntity = goodsShoesDao.selectById(shoesSeq);
        params.put("list", shopSeqs);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("year", goodsShoesEntity.getYear());
        params.put("seasonSeq", goodsShoesEntity.getSeasonSeq());
        params.put("categorySeq", goodsShoesEntity.getCategorySeq());
        List<Map<String, Object>> list = saleShoesDetailDao.selectSaleNumRank(params);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            for (Map<String, Object> map : list) {
                if (((Integer) map.get("shoeSeq")).equals(shoesSeq)) {
                    return ((Long) map.get("No")).intValue();
                }
            }
        }

        return null;

    }


    /**
     * （多个门店序号范围内）本货号在所有货号中的库存排名
     */
    @Override
    public Integer getGoodsIdStockRank(List<Object> shopSeqs, Integer shoesSeq) {

        Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
        wrapper.setSqlSelect("Shoes_Seq AS shoesSeq, RANK() OVER (ORDER BY SUM (Stock) DESC) AS No").in("Shop_Seq", shopSeqs)
                .groupBy("Shoes_Seq");

        List<Map<String, Object>> list = shoesDataDao.selectMaps(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            for (Map<String, Object> map : list) {
                if (((Integer) map.get("shoesSeq")).equals(shoesSeq)) {
                    return ((Long) map.get("No")).intValue();
                }
            }
        }

        return null;

    }


    /**
     * 查询近7天总销量
     */
    @Override
    public Integer getAllShopsSaleNum(Date sevenDayBefore, Integer shoesSeq) {
        Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
        wrapper.setSqlSelect("SUM (SaleCount) AS saleNum")
                .where("SaleDate >= {0} AND ShoeSeq = {1}", sevenDayBefore, shoesSeq);

        List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            return (Integer) list.get(0);
        } else {
            return null;
        }
    }


    /**
     * 查询近7天门店销量
     */
    @Override
    public Integer getShopSaleNum(Integer shopSeq, Date sevenDayBefore, Integer shoesSeq) {
        Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
        wrapper.setSqlSelect("SUM (SaleCount) AS saleNum")
                .where("ShopSeq = {0} AND SaleDate >= {1} AND ShoeSeq = {2}", shopSeq, sevenDayBefore, shoesSeq);

        List<Object> list = saleShoesDetailDao.selectObjs(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            return (Integer) list.get(0);
        } else {
            return null;
        }
    }


    /**
     * 查询近7天销量最好的店铺序号
     */
    @Override
    public Map<String, Object> getMostSaleShopSeq(Date sevenDayBefore, Integer shoesSeq) {

        Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
        wrapper.setSqlSelect("Top 1 ShopSeq AS shopSeq, SUM (SaleCount) AS saleNum")
                .where("SaleDate >= {0} AND ShoeSeq = {1}", sevenDayBefore, shoesSeq)
                .groupBy("ShopSeq").orderBy("saleNum DESC");

        List<Map<String, Object>> list = saleShoesDetailDao.selectMaps(wrapper);
        if (list != null && list.size() > 0 && list.get(0) != null) {
            return list.get(0);
        } else {
            return null;
        }

    }


    
    /**
     * 门店 起止时间内 指定货品  总销售数量+SKU数
     */
	@Override
	public Map<String, Object> getShopSaleNumAndSKUsMap(Integer companySeq, List<Integer> shopSeqList, Date saleTimeStart, Date saleTimeEnd, List<Integer> shoesSeqList) {
		Wrapper<SaleShoesDetailEntity> wrapper = new EntityWrapper<SaleShoesDetailEntity>();
        wrapper.setSqlSelect("ISNULL(SUM(SaleCount),0) AS saleNum")
                .where("Company_Seq = {0} AND SaleDate >= {1} AND SaleDate <= {2}", companySeq, saleTimeStart, saleTimeEnd);
        
        //添加货品条件（货品为空查所有货品）
	    if(shoesSeqList != null && shoesSeqList.size() > 0) {
			String shoesSeqs = Joiner.on(",").join(shoesSeqList);  //使用wrapper.in方式时当shoesSeq超过2100条时报错，改用字符串拼接方式
			wrapper.where("ShoeSeq IN (" + shoesSeqs + ")");
	    }
	    
	   //添加门店条件（门店为空查所有门店）
       if(shopSeqList != null && shopSeqList.size() > 0) {
    	   wrapper.in("ShopSeq", shopSeqList);
       }

        List<Map<String, Object>> list = saleShoesDetailDao.selectMaps(wrapper);

        Wrapper<SaleShoesDetailEntity> wrapper1 = new EntityWrapper<SaleShoesDetailEntity>();
        wrapper1.setSqlSelect("COUNT(DISTINCT (ShoeSeq)) AS SKUs")
                .where("Company_Seq = {0}", companySeq);

        //添加货品条件（货品为空查所有货品）
        if(shoesSeqList != null && shoesSeqList.size() > 0) {
            String shoesSeqs = Joiner.on(",").join(shoesSeqList);  //使用wrapper.in方式时当shoesSeq超过2100条时报错，改用字符串拼接方式
            wrapper1.where("ShoeSeq IN (" + shoesSeqs + ")");
        }

        //添加门店条件（门店为空查所有门店）
        if(shopSeqList != null && shopSeqList.size() > 0) {
            wrapper1.in("ShopSeq", shopSeqList);
        }

        List<Map<String, Object>> list1 = saleShoesDetailDao.selectMaps(wrapper1);

        if (list != null && list.size() > 0 && list.get(0) != null) {
            if(list != null && list.size() > 0 && list.get(0) != null) {
                list.get(0).put("SKUs",list1.get(0).get("SKUs"));
            }else {
                list.get(0).put("SKUs",0);
            }
            return list.get(0);
        } else {
            return null;
        }
        
	}
    
    
}
