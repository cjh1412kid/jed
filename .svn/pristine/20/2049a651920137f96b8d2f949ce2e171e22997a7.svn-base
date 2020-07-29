package io.nuite.modules.system.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jcajce.provider.symmetric.AES.Wrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Joiner;

import io.nuite.common.exception.RRException;
import io.nuite.common.utils.DateUtils;
import io.nuite.common.utils.ImportMultipartExcelUtil;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.GoodsCategoryService;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.dao.ShoesArrivedDao;
import io.nuite.modules.system.dao.ShoesReplenishDao;
import io.nuite.modules.system.dao.ShoesReplenishDetailDao;
import io.nuite.modules.system.entity.ShoesArrivedEntity;
import io.nuite.modules.system.entity.ShoesReplenishDetailEntity;
import io.nuite.modules.system.entity.ShoesReplenishEntity;
import io.nuite.modules.system.service.ShoesReplenishService;

@Service
public class ShoesReplenishServiceImpl extends ServiceImpl<ShoesReplenishDao, ShoesReplenishEntity> implements ShoesReplenishService {
    
	@Autowired
    private ShoesReplenishDao shoesReplenishDao;
	
    @Autowired
    private GoodsShoesService goodsShoesService;
    
    @Autowired
    private ShoesReplenishDetailDao shoesReplenishDetailDao;
    
    @Autowired
    private ConfigUtils configUtils;
    
    @Autowired
    private ShoesArrivedDao shoesArrivedDao;
    
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void parseExcel(MultipartFile file, Integer companySeq) {
        List<List<Object>> rows = null;
        try {
            rows = ImportMultipartExcelUtil.importExcel(file);
        } catch (Exception e) {
            throw new RRException("文件解析异常");
        }
        
        //标题判断
        if (rows != null && rows.size() > 0) {
            List<Object> titleRow = rows.get(0);
            
            if (titleRow.get(0) == null || !"货号".equals(titleRow.get(0).toString().trim()) ||
                titleRow.get(1) == null || !"数量".equals(titleRow.get(1).toString().trim()) ||
                titleRow.get(2) == null || !"预计到货时间".equals(titleRow.get(2).toString().trim())
            ) {
                throw new RRException("标题不符合规范，正确标题：[货号,数量,预计到货时间]");
            }
        }
        
        List<ShoesReplenishEntity> saveDoList = new ArrayList<>();
        
        //循环行数据
        List<Object> row = null;
        for (int i = 1; i < rows.size(); i++) {
            row = rows.get(i);
            Object goodIDObj = row.get(0);
            Object countObj = row.get(1);
            Object timeObj = row.get(2);
            if (StringUtils.isBlank(goodIDObj.toString())) {
                throw new RRException("导入失败： 第" + (i + 1) + "行 货号为空");
            }
            if (StringUtils.isBlank(countObj.toString())) {
                throw new RRException("导入失败： 第" + (i + 1) + "行 数量为空");
            }
            if (StringUtils.isBlank(timeObj.toString())) {
                throw new RRException("导入失败： 第" + (i + 1) + "行 预计到货时间为空");
            }
            
            String goodID = goodIDObj.toString();
            int count = 0;
            try {
                count = Integer.parseInt(countObj.toString());
            } catch (NumberFormatException e) {
                throw new RRException("导入失败： 第" + (i + 1) + "行 数量值非数字");
            }
            
            Date date = DateUtils.parse(timeObj.toString(), DateUtils.DATE_TIME_PATTERN);
            
            if (date == null) {
                throw new RRException("导入失败： 第" + (i + 1) + "行 时间格式错误，正确格式：" + DateUtils.DATE_TIME_PATTERN);
            }
            
            //根据货号查询鞋子序号
            GoodsShoesEntity goodsShoesEntity = goodsShoesService.selectOne(new EntityWrapper<GoodsShoesEntity>().eq("GoodID", goodID).eq("Company_Seq", companySeq));
            if (goodsShoesEntity == null) {
                throw new RRException("导入失败： 第" + (i + 1) + "行  货号" + goodID + " 未查询到关联货品");
            }
            
            ShoesReplenishEntity shoesReplenishEntity = new ShoesReplenishEntity();
            shoesReplenishEntity.setShoesSeq(goodsShoesEntity.getSeq());
            shoesReplenishEntity.setReplenishNum(count);
            shoesReplenishEntity.setReplenishTime(date);
            
            saveDoList.add(shoesReplenishEntity);
        }
        
        for (ShoesReplenishEntity shoesReplenishEntity : saveDoList) {
            //判断是否已存在
            ShoesReplenishEntity replenishEntity = super.selectOne(new EntityWrapper<ShoesReplenishEntity>()
                .eq("Shoes_Seq", shoesReplenishEntity.getShoesSeq())
                .eq("ReplenishTime", shoesReplenishEntity.getReplenishTime())
                .eq("ReplenishNum", shoesReplenishEntity.getReplenishNum()));
            
            if (replenishEntity != null) {
                replenishEntity.setInputTime(new Date());
                super.updateById(replenishEntity);
            } else {
                shoesReplenishEntity.setDel(0);
                shoesReplenishEntity.setInputTime(new Date());
                super.insert(shoesReplenishEntity);
            }
        }
        
    }
    
    @Override
    public List<ShoesReplenishEntity> queryReplenishPlan(Integer shoeSeq) {
        List<ShoesReplenishEntity> shoesReplenishEntities = super.selectList(new EntityWrapper<ShoesReplenishEntity>().eq("Shoes_Seq", shoeSeq).orderBy("InputTime", false));
        return shoesReplenishEntities;
    }
    
    @Override
	public List<Map<String, Object>> getShoesReplenishList(Integer shoesSeq) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=shoesReplenishDao.getReplenishList(shoesSeq);
		return list;
    }

	@Override
	public List<ShoesReplenishEntity> getSupplementList(Integer companySeq) {
		List<ShoesReplenishEntity> supplementList = shoesReplenishDao.getSupplementList(companySeq);
		return supplementList;
	}

	@Override
	public List<ShoesReplenishEntity> getHaveSupplementList(Integer companySeq) {
		// TODO Auto-generated method stub
		List<ShoesReplenishEntity> haveSupplementList = shoesReplenishDao.getHaveSupplementList(companySeq);
		return haveSupplementList;
	}
	
	@Override
	public List<ShoesReplenishEntity> getStoresSupplementList(Integer shopSeq) {
		List<ShoesReplenishEntity> storesSupplementList = shoesReplenishDao.getStoresSupplementList(shopSeq);
		return storesSupplementList;
	}
	
/*
 * 获取门店已经补单数据
 */
	@Override
	public List<ShoesReplenishEntity> getStoresHaveSupplementList(Integer shopSeq) {
		List<ShoesReplenishEntity> storesHaveSupplementList = shoesReplenishDao.getStoresHaveSupplementList(shopSeq);
		
		return storesHaveSupplementList;
	}

	@Override
	public List<String> getDaysList(Integer companySeq) {
		return shoesReplenishDao.getDaysList(companySeq);
	}

	@Override
	public List<Map<String, Object>> getReplenishList(Integer companySeq) {
		List<String> days=shoesReplenishDao.getDaysList(companySeq);
		List<Map<String, Object>> replenishList=new ArrayList<Map<String,Object>>();
		for (String replenishTime : days) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("replenishTime", replenishTime);
			List<Map<String, Object>> list=shoesReplenishDao.getReplenishListByTime(replenishTime,companySeq);
			for (Map<String, Object> map2 : list) {
				//根据ReplenishSeq查询详情
				Integer replenishSeq=(Integer) map2.get("seq");
				List<ShoesReplenishDetailEntity> shoesReplenishDetailEntities=shoesReplenishDetailDao.getDetailByReplenishSeq(replenishSeq);
				map2.put("shoesReplenishDetailEntities", shoesReplenishDetailEntities);
				Date inputDate=(Date) map2.get("inputTime");
				Date arrivedDate=(Date) map2.get("arrivedTime");
				Calendar calendar = Calendar.getInstance();
				if(inputDate!=null) {
				calendar.setTime(inputDate);
				String inputTime=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
				map2.put("inputTime", inputTime);
				}
				if(arrivedDate!=null) {
				calendar.setTime(arrivedDate);
				String arrivedTime=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
				map2.put("arrivedTime", arrivedTime);
				}
				String goodId=(String) map2.get("goodID");
				String img=(String) map2.get("img");
				 String imageUrl = configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/" + configUtils.getGoodsShoes()
	              + "/" + goodId + "/" + img;
				 map2.put("img", imageUrl);
			}
			map.put("replenishList", list);
			replenishList.add(map);
		}
		return replenishList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PageUtils getGoodsList(Page page, String replenishTime,Integer companySeq) {
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		if(StringUtils.isBlank(replenishTime)) {
		 list=shoesReplenishDao.getAllReplenishList(page,companySeq);
		}else {
			 list=shoesReplenishDao.getReplenishListByTime(page,replenishTime,companySeq);
		}
		for (Map<String, Object> map2 : list) {
			Date replenishDate=(Date) map2.get("replenishTime");
			Date inputDate=(Date) map2.get("inputTime");
			Date arrivedDate=(Date) map2.get("arrivedTime");
			Calendar calendar = Calendar.getInstance();
			if(replenishDate!=null) {
			calendar.setTime(replenishDate);
			String replenishtime=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);
			map2.put("replenishTime", replenishtime);
			}
			if(inputDate!=null) {
			calendar.setTime(inputDate);
			String inputTime=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
			map2.put("inputTime", inputTime);
			}
			if(arrivedDate!=null) {
			calendar.setTime(arrivedDate);
			String arrivedTime=calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
			map2.put("arrivedTime", arrivedTime);
			}
		}
		page.setRecords(list);
		return new PageUtils(page);
	}
						
	
	/**
	 * 查询总部已经补单的所有鞋子Seq的List
	 */
	@Override
	public List<Object> getSupplementShoesSeqList(Integer companySeq) {
		Wrapper<ShoesReplenishEntity> wrapper = new EntityWrapper<ShoesReplenishEntity>();
	    wrapper.setSqlSelect("DISTINCT Shoes_Seq").where("Company_Seq = {0}", companySeq);
		return shoesReplenishDao.selectObjs(wrapper);
	}

	@Override
	public List<Map<String, Object>> getGoodsSeqs(List<Integer> yearList, List<Integer> seasonSeqList,
			List<Integer> categorySeqList, Integer companySeq) {
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
		return shoesReplenishDao.getGoodsSeqs(years,seasonSeqs,categorySeqs,companySeq);
	}

	@Override
	public Map<String, Object> getTotalNum(Integer shoesSeq) {
		return shoesReplenishDao.getTotalNum(shoesSeq);
	}



}
