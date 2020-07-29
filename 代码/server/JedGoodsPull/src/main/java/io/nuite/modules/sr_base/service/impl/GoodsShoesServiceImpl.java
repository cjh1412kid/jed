package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.GoodsCategoryService;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsShoesServiceImpl extends ServiceImpl<GoodsShoesDao, GoodsShoesEntity> implements GoodsShoesService {
    
    @Autowired
    private GoodsShoesDao goodsShoesDao;
    
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    
    /**
     * 根据公司seq、货号获取商品
     */
    @Override
    public GoodsShoesEntity getGoodsByGoodId(Integer companySeq, String goodId) {
        GoodsShoesEntity goodsEntity = new GoodsShoesEntity();
        goodsEntity.setCompanySeq(companySeq);
        goodsEntity.setGoodID(goodId);
        return goodsShoesDao.selectOne(goodsEntity);
    }
    
    @Override
    public int getCountBySXAndOption(Integer companySeq, String sxid, String optCode) {
        return goodsShoesDao.getCountBySXAndOption(companySeq, sxid, optCode);
    }
    
    /**
     * 根据属性条件，查询满足的所有鞋子货号
     */
    @Override
    public List<Integer> getShoesSeqListOnSXParam(Integer companySeq, List<Integer> yearList, List<Integer> seasonSeqList, List<Integer> categorySeqList,
                                                  List<Integer> colorSeqList, Map<String, String> sXMap, String fuzzySearchParam, Date saleTimeStart,Date saleTimeEnd,List<Integer> shopSeqList,Date depositTime) {

		//多个年份
		String years = null;
		
		if (yearList != null && yearList.size() > 0) {
		    years = Joiner.on(",").join(yearList);
		}
		
		//多个季节
		String seasonSeqs = null;
		if (seasonSeqList != null && seasonSeqList.size() > 0) {
		    seasonSeqs = Joiner.on(",").join(seasonSeqList);
		}
		
		//多个分类
		String categorySeqs = null;
		if (categorySeqList != null && categorySeqList.size() > 0) {
		    List<Integer> seqList = new ArrayList<Integer>();
		    for (Integer categorySeq : categorySeqList) {
		        //查询该分类的所有终级（目前是第五级）分类序号
		        List<Integer> finalCategorySeqList = goodsCategoryService.getFinalCategorySeqList(categorySeq);
		        seqList.addAll(finalCategorySeqList);
		    }
		    categorySeqs = Joiner.on(",").join(seqList);
		}
		
		//多个颜色
		String colorSeqs = null;
		if (colorSeqList != null && colorSeqList.size() > 0) {
		    colorSeqs = Joiner.on(",").join(colorSeqList);
		}
		
		//自定义属性
		String sXsql = "";
		if(sXMap!=null) {
			for (String s : sXMap.keySet()) {
		        sXsql = sXsql + " AND " + s + " = '" + sXMap.get(s) + "'";
		    }
		}
		List<Integer> list = goodsShoesDao.getShoesSeqListOnSXParam(companySeq, years, seasonSeqs, categorySeqs, colorSeqs, sXsql, null, saleTimeStart, saleTimeEnd, shopSeqList, depositTime);
		
		
		
        // 当模糊字段不为空时，根据模糊字段查询满足条件的鞋子货号，然后取交集
        if (StringUtils.isNotBlank(fuzzySearchParam)) {
        	
            String[] paramArr = fuzzySearchParam.split("\\s+");
            List<String> paramList = new ArrayList<String>(Arrays.asList(paramArr));
            
            Set<Integer> categorySeqSet = new TreeSet<Integer>();
            for (int i = 0; i < paramList.size(); i++) {
                String param = paramList.get(i);
                
                //判断此参数是否为某一分类，如果是，查询出该所有分类，并查询终极分类序号List，并从参数数组中删除
                List<GoodsCategoryEntity> goodsCategoryList = goodsCategoryService.getCategoryByFuzzyName(companySeq, param);
                if (goodsCategoryList != null && goodsCategoryList.size() > 0) {
                    paramList.remove(i);
                    i--;
                    for (GoodsCategoryEntity goodsCategoryEntity : goodsCategoryList) {
                        //查询该分类的所有终级（目前是第五级）分类序号
                        List<Integer> finalCategorySeqList = goodsCategoryService.getFinalCategorySeqList(goodsCategoryEntity.getSeq());
                        categorySeqSet.addAll(finalCategorySeqList);
                    }
                }
            }
            
            String paramCategorySeqs = null;
            if (categorySeqSet != null && categorySeqSet.size() > 0) {
            	paramCategorySeqs = Joiner.on(",").join(categorySeqSet);
            }
            
            List<Integer> listOnFuzzySearchParam = goodsShoesDao.getShoesSeqListOnFuzzySearchParam(companySeq, paramList, paramCategorySeqs);
            
            //求交集
            list.retainAll(listOnFuzzySearchParam);
        }
        
        return list;
    }
    
    @Override
    public List<Integer> queryExistYearList(Integer companySeq) {
        return goodsShoesDao.selectExistYearList(companySeq);
    }
    
    @Override
    public List<Map<Integer, String>> queryExistSeasonList(Integer companySeq) {
        return goodsShoesDao.selectExistSeasonList(companySeq);
    }

    
    
    /**
     * 带新老款筛选的查询满足条件的鞋子Seq（销售品类分析处用）
     */
	@Override
	public List<Integer> getShoesSeqListByParam(Integer companySeq, List<Integer> yearList, List<Integer> seasonSeqList,
			List<Integer> categorySeqList, List<Integer> colorSeqList, Map<String, String> sXMap, Integer oldOrNew) {

	            //多个年份
	            String years = null;
	            if (yearList != null && yearList.size() > 0) {
	                years = Joiner.on(",").join(yearList);
	            }
	            
	            //多个季节
	            String seasonSeqs = null;
	            if (seasonSeqList != null && seasonSeqList.size() > 0) {
	                seasonSeqs = Joiner.on(",").join(seasonSeqList);
	            }
	            
	            //多个分类
	            String categorySeqs = null;
	            if (categorySeqList != null && categorySeqList.size() > 0) {
	                List<Integer> seqList = new ArrayList<Integer>();
	                for (Integer categorySeq : categorySeqList) {
	                    //查询该分类的所有终级（目前是第五级）分类序号
	                    List<Integer> finalCategorySeqList = goodsCategoryService.getFinalCategorySeqList(categorySeq);
	                    seqList.addAll(finalCategorySeqList);
	                }
	                categorySeqs = Joiner.on(",").join(seqList);
	            }
	            
	            //多个颜色
	            String colorSeqs = null;
	            if (colorSeqList != null && colorSeqList.size() > 0) {
	                colorSeqs = Joiner.on(",").join(colorSeqList);
	            }
	            
	            //自定义属性
	            String sXsql = "";
	            for (String s : sXMap.keySet()) {
	                sXsql = sXsql + " AND " + s + " = '" + sXMap.get(s) + "'";
	            }
	            
	            List<Integer> list = goodsShoesDao.getShoesSeqListOnSXParam(companySeq, years, seasonSeqs, categorySeqs, colorSeqs, sXsql, oldOrNew, null, null, null, null);
	            
	            return list;
	            
	}

	@Override
	public GoodsShoesEntity goodsList(String goodId) {
		Wrapper<GoodsShoesEntity> wrapper=new EntityWrapper<GoodsShoesEntity>();
		wrapper.where("GoodID = {0}", goodId);
		List<GoodsShoesEntity> goodsShoesEntities=goodsShoesDao.selectList(wrapper);
		if(goodsShoesEntities!=null&&goodsShoesEntities.size()>0) {
			return goodsShoesEntities.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getSizeMap(Integer shoesSeq) {
		return goodsShoesDao.getSizeMap(shoesSeq);
	}
}
