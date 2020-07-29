package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.common.exception.RRException;
import io.nuite.common.utils.ImportMultipartExcelUtil;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.order_platform_app.dao.SaleShoesDetailDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDailyDetailDao;
import io.nuite.modules.order_platform_app.dao.TargetSalesDao;
import io.nuite.modules.order_platform_app.dao.TargetShopDao;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.entity.TargetSalesEntity;
import io.nuite.modules.order_platform_app.entity.TargetShopEntity;
import io.nuite.modules.order_platform_app.service.TargetShopService;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.BaseUserRoleDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.BaseUserRoleEntity;
import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: 门店指标Service实现类
 * @author: jxj
 * @create: 2019-03-21 10:44
 */
@Service
public class TargetShopServiceImpl extends ServiceImpl<TargetShopDao,TargetShopEntity> implements TargetShopService {
	
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private SaleShoesDetailDao saleShoesDetailDao;
    @Autowired
    private TargetSalesDao targetSalesDao;
    @Autowired
    private BaseUserRoleDao baseUserRoleDao;
    
    @Autowired
    private BaseShopDao baseShopDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOrUpdateTarget(String list,BaseUserEntity baseUserEntity) throws Exception {
        boolean isSuccess = false;
        List<TargetShopEntity> targetShopEntities = new ArrayList<>(10);
        JSONArray jsonArray = JSONArray.fromObject(list);
        logger.info(jsonArray.toString());
        for(int i = 0;i < jsonArray.size();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            TargetShopEntity targetShopEntity = (TargetShopEntity)JSONObject.toBean(jsonObject,TargetShopEntity.class);
            targetShopEntity.setMiddleMoney(targetShopEntity.getMiddleMoney().setScale(2,BigDecimal.ROUND_HALF_UP));
            targetShopEntity.setStandardMoney(targetShopEntity.getStandardMoney().setScale(2,BigDecimal.ROUND_HALF_UP));
            targetShopEntity.setAdvanceMoney(targetShopEntity.getAdvanceMoney().setScale(2,BigDecimal.ROUND_HALF_UP));
            targetShopEntities.add(targetShopEntity);
        }
        List<TargetShopEntity> insertTargetShopEntities = new ArrayList<>(10);
        List<TargetShopEntity> updateTargetShopEntities = new ArrayList<>(10);
        for (int i = 0;i < targetShopEntities.size();i++) {
        	//标准指标
        	Integer standardSeq=targetShopEntities.get(i).getStandardSeq();
        	TargetShopEntity standardShop=new TargetShopEntity();
        	if(targetShopEntities.get(i).getStandardMoney()!=null&&targetShopEntities.get(i).getStandardMoney().compareTo(BigDecimal.ZERO)!=0) {
        	if(standardSeq==null) {
        		standardShop.setShopSeq(targetShopEntities.get(i).getShopSeq());
        		standardShop.setTargetYear(targetShopEntities.get(i).getTargetYear());
        		standardShop.setTargetMonth(targetShopEntities.get(i).getTargetMonth());
        		standardShop.setMoney(targetShopEntities.get(i).getStandardMoney());
        		standardShop.setCreator(baseUserEntity.getSeq());
        		standardShop.setDel(0);
        		standardShop.setTag(1);
        		standardShop.setInputTime(new Date());
        		 insertTargetShopEntities.add(standardShop);
        	}else {
        		standardShop.setSeq(targetShopEntities.get(i).getStandardSeq());
        		standardShop.setMoney(targetShopEntities.get(i).getStandardMoney());
        		updateTargetShopEntities.add(standardShop);
        	}
        	}
        	TargetShopEntity middleShop=new TargetShopEntity();
        	//中级指标
        	Integer middleSeq=targetShopEntities.get(i).getMiddleSeq();
        	if(targetShopEntities.get(i).getMiddleMoney()!=null&&targetShopEntities.get(i).getMiddleMoney().compareTo(BigDecimal.ZERO)!=0) {
        	if(middleSeq==null) {
        		middleShop.setShopSeq(targetShopEntities.get(i).getShopSeq());
        		middleShop.setTargetYear(targetShopEntities.get(i).getTargetYear());
        		middleShop.setTargetMonth(targetShopEntities.get(i).getTargetMonth());
        		middleShop.setMoney(targetShopEntities.get(i).getMiddleMoney());
        		middleShop.setCreator(baseUserEntity.getSeq());
        		middleShop.setDel(0);
        		middleShop.setTag(2);
        		middleShop.setInputTime(new Date());
        		 insertTargetShopEntities.add(middleShop);
        	}else {
        		middleShop.setSeq(targetShopEntities.get(i).getMiddleSeq());
        		middleShop.setMoney(targetShopEntities.get(i).getMiddleMoney());
        		updateTargetShopEntities.add(middleShop);
        	}
        	}
        	TargetShopEntity advanceShop=new TargetShopEntity();
        	//高级指标
        	Integer advanceSeq=targetShopEntities.get(i).getAdvanceSeq();
        	if(targetShopEntities.get(i).getAdvanceMoney()!=null&&targetShopEntities.get(i).getAdvanceMoney().compareTo(BigDecimal.ZERO)!=0) {
        	if(advanceSeq==null) {
        		advanceShop.setShopSeq(targetShopEntities.get(i).getShopSeq());
        		advanceShop.setTargetYear(targetShopEntities.get(i).getTargetYear());
        		advanceShop.setTargetMonth(targetShopEntities.get(i).getTargetMonth());
        		advanceShop.setMoney(targetShopEntities.get(i).getAdvanceMoney());
        		advanceShop.setCreator(baseUserEntity.getSeq());
        		advanceShop.setDel(0);
        		advanceShop.setTag(3);
        		advanceShop.setInputTime(new Date());
        		 insertTargetShopEntities.add(advanceShop);
        	}else {
        		advanceShop.setSeq(targetShopEntities.get(i).getAdvanceSeq());
        		advanceShop.setMoney(targetShopEntities.get(i).getAdvanceMoney());
        		updateTargetShopEntities.add(advanceShop);
        	}
        	}
        }

        if(insertTargetShopEntities.size() > 0) {
            isSuccess = retBool(baseMapper.insertTargetShop(insertTargetShopEntities));
        }
        if(updateTargetShopEntities.size() > 0) {
            isSuccess = retBool(baseMapper.updateTargetShop(updateTargetShopEntities));
        }
        return isSuccess;
    }

    @Override
    public Map<String, Object> selectTotalTarget(Integer targetYear,BaseUserEntity baseUserEntity) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        Map<String,Object> result = new HashMap<>(10);
        Wrapper<BaseUserRoleEntity> baseUserRoleEntityWrapper = new EntityWrapper<>();
        baseUserRoleEntityWrapper.eq("User_Seq",baseUserEntity.getSeq());
        List<BaseUserRoleEntity> baseUserRoleEntities = baseUserRoleDao.selectList(baseUserRoleEntityWrapper);
        if(baseUserRoleEntities.get(0).getRoleSeq().equals(new Integer(3))) {
            map.put("targetYear",targetYear);
            map.put("shopSeq",baseUserEntity.getShopSeq());
            //标准指标
            map.put("tag",1);
            TargetSalesEntity  StandardTargetSalesEntity = targetSalesDao.selectTotalTarget(map);
          
            //中级指标
            map.put("tag",2);
            TargetSalesEntity  MiddleTargetSalesEntity = targetSalesDao.selectTotalTarget(map);
           
            //高级指标
            map.put("tag",3);
            TargetSalesEntity  AdvancedTargetSalesEntity = targetSalesDao.selectTotalTarget(map);
            
            TargetSalesEntity targetSalesEntity=new TargetSalesEntity();
            targetSalesEntity.setMonthMoneyList(targetSalesDao.selectTotalMonthTarget(map));
            result.put("type", 1);
            result.put("targetSalesEntity", targetSalesEntity);
            result.put("StandardTargetSalesEntity",StandardTargetSalesEntity);
            result.put("MiddletargetSalesEntity",MiddleTargetSalesEntity);
            result.put("AdvancedTargetSalesEntity", AdvancedTargetSalesEntity);
        }else {
            map.put("targetYear",targetYear);
            map.put("brandSeq",baseUserEntity.getBrandSeq());
            //标准指标
            map.put("tag",1);
            TargetShopEntity StandardTargetShopEntity = baseMapper.selectTotalTarget(map);
          
            //标准指标
            map.put("tag",2);
            TargetShopEntity MiddleTargetShopEntity = baseMapper.selectTotalTarget(map);
            //高级指标
            map.put("tag",3);
            TargetShopEntity AdvancedTargetShopEntity = baseMapper.selectTotalTarget(map);
            
            TargetShopEntity targetShopEntity=new TargetShopEntity();
            targetShopEntity.setMonthMoneyList(baseMapper.selectTotalMonthTarget(map));
            
            result.put("type", 2);
            result.put("targetShopEntity", targetShopEntity);
            result.put("StandardTargetShopEntity",StandardTargetShopEntity);
            result.put("MiddleTargetShopEntity",MiddleTargetShopEntity);
            result.put("AdvancedTargetShopEntity",AdvancedTargetShopEntity);
        }
        return result;
    }

    @Override
    public Map<String, Object> selectMonthTarget(Integer targetYear,Integer targetMonth,BaseUserEntity baseUserEntity) throws Exception {
        Map<String,Object> map = new HashMap(10);
        Map<String,Object> result = new HashMap<>(10);
        Wrapper<BaseUserRoleEntity> baseUserRoleEntityWrapper = new EntityWrapper<>();
        baseUserRoleEntityWrapper.eq("User_Seq",baseUserEntity.getSeq());
        List<BaseUserRoleEntity> baseUserRoleEntities = baseUserRoleDao.selectList(baseUserRoleEntityWrapper);
        if(baseUserRoleEntities.get(0).getRoleSeq().equals(new Integer(3))) {
            map.put("targetYear",targetYear);
            map.put("targetMonth",targetMonth);
            map.put("shopSeq",baseUserEntity.getShopSeq());
            //标准指标
            map.put("tag",1);
            TargetSalesEntity StandardTargetSalesEntity = targetSalesDao.selectMonthTarget(map);
            //中级指标
            map.put("tag",2);
            TargetSalesEntity MiddleTargetSalesEntity = targetSalesDao.selectMonthTarget(map);
            //高级指标
            map.put("tag",3);
            TargetSalesEntity AdvancedTargetSalesEntity = targetSalesDao.selectMonthTarget(map);
            
            List<TargetSalesEntity> monthSalesEntities = targetSalesDao.selectMonthSalesTarget(map);
            TargetSalesEntity targetSalesEntity=new TargetSalesEntity();
            targetSalesEntity.setMonthShopMoneyList(monthSalesEntities);
            
            result.put("type", 1);
            result.put("targetSalesEntity", targetSalesEntity);
            result.put("StandardTargetSalesEntity",StandardTargetSalesEntity);
            result.put("MiddleTargetSalesEntity",MiddleTargetSalesEntity);
            result.put("AdvancedTargetSalesEntity",AdvancedTargetSalesEntity);
        }else {
            map.put("targetYear",targetYear);
            map.put("targetMonth",targetMonth);
            map.put("brandSeq",baseUserEntity.getBrandSeq());
            //标准指标
            map.put("tag",1);
            TargetShopEntity StandardTargetShopEntity = baseMapper.selectMonthTarget(map);
            //中级指标
            map.put("tag",2);
            TargetShopEntity MiddleTargetShopEntity = baseMapper.selectMonthTarget(map);
            //高级指标
            map.put("tag",3);
            TargetShopEntity AdvancedTargetShopEntity = baseMapper.selectMonthTarget(map);
            
            List<TargetShopEntity> monthShopEntities = baseMapper.selectMonthShopTarget(map);
            TargetShopEntity targetShopEntity=new TargetShopEntity();
            targetShopEntity.setMonthShopMoneyList(monthShopEntities);
            result.put("type", 2);
            result.put("targetShopEntity", targetShopEntity);
            result.put("StandardTargetShopEntity",StandardTargetShopEntity);
            result.put("MiddleTargetShopEntity",MiddleTargetShopEntity);
            result.put("AdvancedTargetShopEntity",AdvancedTargetShopEntity);
        }
        return result;
    }

    @Override
    public Map<String, Object> selectTotalTargetAnalyze(Integer targetYear,Integer targetMonth,BaseUserEntity baseUserEntity) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("targetYear",targetYear);
        map.put("targetMonth",targetMonth);
        map.put("brandSeq",baseUserEntity.getBrandSeq());
        map.put("tag", 1);
        TargetShopEntity   StandardTargetShopEntity= getTargetShopEntity(map);
        map.put("tag", 2);
        TargetShopEntity   MiddleTargetShopEntity= getTargetShopEntity(map);
        map.put("tag", 3);
        TargetShopEntity   AdvanceTargetShopEntity= getTargetShopEntity(map);
        
        Map<String,Object> result = new HashMap<>(10);
        result.put("StandardTargetShopEntity",StandardTargetShopEntity);
        result.put("MiddleTargetShopEntity",MiddleTargetShopEntity);
        result.put("AdvanceTargetShopEntity",AdvanceTargetShopEntity);
        BigDecimal completeMoney= targetSalesDao.getSaleSumByMonth(map);
        result.put("completeMoney",completeMoney);
        //同期完成额度
        map.put("targetYear",targetYear-1);
        //获取当前时间
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
    	calendar.setTime(date);
    	Integer month=calendar.get(Calendar.MONTH)+1;
        Integer day=calendar.get(Calendar.DATE);
        BigDecimal salesCount = new BigDecimal(0);
        if(targetMonth==null) {
        	//全年
        	//起始日期
        	map.put("beginTime", targetYear-1+"-"+1+"-"+1);
        	//终止日期
        	map.put("endTime", targetYear-1+"-"+month+"-"+day);
        	salesCount=targetSalesDao.getSaleSumByDate(map);
        }else {
        	//非全年
        	if(month.equals(targetMonth)) {
        		//当前月
        		//起始日期
        		map.put("beginTime", targetYear-1+"-"+month+"-"+1);
            	//终止日期
        		map.put("startTime", targetYear-1+"-"+month+"-"+day);
        		salesCount=targetSalesDao.getSaleSumByDate(map);
        	}else {
        		//非当前月
        		salesCount=targetSalesDao.getSaleSumByMonth(map);
        	}
        }
        
        
        result.put("salesCount",salesCount);
        if(completeMoney == null || salesCount == null || salesCount.compareTo(BigDecimal.ZERO) == 0) {
            result.put("compareLastYear",null);
        }else {
            result.put("compareLastYear",completeMoney.subtract(salesCount).divide(salesCount,4,BigDecimal.ROUND_HALF_UP));
        }
        return result;
    }

	@Override
	public Map<String, Object> selectSalesTargetAnalyze(Integer targetYear, Integer targetMonth, Integer shopSeq,
			BaseUserEntity baseUserEntity) throws Exception {
		Map<String,Object> map = new HashMap<>(10);
		Map<String,Object> result = new HashMap<>(10);
		Wrapper<BaseUserRoleEntity> baseUserRoleEntityWrapper = new EntityWrapper<>();
	    baseUserRoleEntityWrapper.eq("User_Seq",baseUserEntity.getSeq());
	    List<BaseUserRoleEntity> baseUserRoleEntities = baseUserRoleDao.selectList(baseUserRoleEntityWrapper);
	    map.put("targetYear",targetYear);
        map.put("targetMonth",targetMonth);
	    if(baseUserRoleEntities.get(0).getRoleSeq().equals(new Integer(3))) {
	        map.put("shopSeq",baseUserEntity.getShopSeq());
	    }else {
	    	map.put("shopSeq",shopSeq);
	    }
	    map.put("tag",1);
	    
	    TargetSalesEntity StandardTargetSaleEntity=getTargetSalesEntity(map);
	    
	    map.put("tag",2);
	    TargetSalesEntity MiddleTargetSaleEntity=getTargetSalesEntity(map);
	    map.put("tag",3);
	    TargetSalesEntity AdvanceTargetSaleEntity=getTargetSalesEntity(map);
	    
        result.put("StandardTargetSaleEntity", StandardTargetSaleEntity);
        result.put("MiddleTargetSaleEntity", MiddleTargetSaleEntity);
        result.put("AdvanceTargetSaleEntity", AdvanceTargetSaleEntity);
        BigDecimal completeMoney= targetSalesDao.getShopSaleSumByMonth(map);
        result.put("completeMoney", completeMoney);
        //同期完成额度
        map.put("targetYear",targetYear-1);
        //获取当前时间
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
    	calendar.setTime(date);
    	Integer month=calendar.get(Calendar.MONTH)+1;
        Integer day=calendar.get(Calendar.DATE);
        BigDecimal salesCount = new BigDecimal(0);
        if(targetMonth==null) {
        	//全年
        	//起始日期
        	map.put("beginTime", targetYear-1+"-"+1+"-"+1);
        	//终止日期
        	map.put("endTime", targetYear-1+"-"+month+"-"+day);
        	salesCount=targetSalesDao.querySaleSumByDate(map);
        }else {
        	//非全年
        	if(month.equals(targetMonth)) {
        		//当前月
        		//起始日期
        		map.put("beginTime", targetYear-1+"-"+month+"-"+1);
            	//终止日期
        		map.put("startTime", targetYear-1+"-"+month+"-"+day);
        		salesCount=targetSalesDao.querySaleSumByDate(map);
        	}else {
        		//非当前月
        		salesCount=targetSalesDao.getShopSaleSumByMonth(map);
        	}
        }
        result.put("salesCount",salesCount);
        if(completeMoney == null || salesCount == null || salesCount.compareTo(BigDecimal.ZERO) == 0) {
            result.put("compareShopLastYear",null);
        }else {
            result.put("compareShopLastYear",completeMoney.subtract(salesCount).divide(salesCount,4,BigDecimal.ROUND_HALF_UP));
        }

		return result;
	}
	
	public TargetSalesEntity getTargetSalesEntity(Map<String, Object> map) throws Exception {
		  TargetSalesEntity targetSalesEntity = targetSalesDao.selectSalesTargetAnalyze(map);
		    if(targetSalesEntity != null) {
	            if(targetSalesEntity.getCompletePercent() != null) {
	                targetSalesEntity.setCompletePercent(targetSalesEntity.getCompletePercent());
	                if(targetSalesEntity.getCompleteMoney() != null) {
	                    targetSalesEntity.setCompleteMoney(targetSalesEntity.getCompleteMoney());
	                }
	            }
	        }
		    return targetSalesEntity;
	}
	
	public  TargetShopEntity getTargetShopEntity(Map<String, Object> map) throws Exception {
		TargetShopEntity StandardTargetShopEntity = baseMapper.selectTotalTargetAnalyze(map);
        if(StandardTargetShopEntity != null) {
            if(StandardTargetShopEntity.getCompletePercent() != null) {
            	StandardTargetShopEntity.setCompletePercent(StandardTargetShopEntity.getCompletePercent());
            }
            if(StandardTargetShopEntity.getCompleteMoney() != null) {
            	StandardTargetShopEntity.setCompleteMoney(StandardTargetShopEntity.getCompleteMoney());
            }
            List<TargetShopEntity> list = baseMapper.selectTotalShopTargetAnalyze(map);
            List<SaleShoesDetailEntity> saleList = saleShoesDetailDao.selectShopCompleteMoney(map);
            for(int i = 0;i < list.size();i++) {
                for(int j = 0;j < saleList.size();j++) {
                    if(saleList.get(j).getShopSeq().equals(list.get(i).getShopSeq())) {
                        if(saleList.get(j).getCompleteMoney() != null) {
                            list.get(i).setCompleteMoney(saleList.get(j).getCompleteMoney());
                            if(list.get(i).getTotalMoney() != null && list.get(i).getTotalMoney().compareTo(BigDecimal.ZERO) > 0) {
                                list.get(i).setCompletePercent(list.get(i).getCompleteMoney().divide(list.get(i).getTotalMoney(),2,BigDecimal.ROUND_HALF_UP));
                            }
                        }
                    }
                }
            }
            StandardTargetShopEntity.setTotalShopCompleteList(list);
        }
        return StandardTargetShopEntity;
	}

	@Override
	public Map<String, Object> selectTargetList(Integer targetYear, Integer targetMonth, BaseUserEntity baseUserEntity,
			Integer type) throws Exception {
		 Map<String,Object> map = new HashMap<>(10);
	     Map<String,Object> result = new HashMap<>(10);
	     
	     Wrapper<BaseUserRoleEntity> baseUserRoleEntityWrapper = new EntityWrapper<>();
	     baseUserRoleEntityWrapper.eq("User_Seq",baseUserEntity.getSeq());
	     List<BaseUserRoleEntity> baseUserRoleEntities = baseUserRoleDao.selectList(baseUserRoleEntityWrapper);
	     if(baseUserRoleEntities.get(0).getRoleSeq().equals(new Integer(3))) {
	    	 map.put("targetYear",targetYear);
	         map.put("shopSeq",baseUserEntity.getShopSeq());
	         map.put("tag",type);
	    	 if(targetMonth==null) {
	    		 List<TargetSalesEntity> monthSalesEntities= targetSalesDao.selectTotalMonthTarget(map);
	    		 result.put("monthSalesEntities", monthSalesEntities);
	    	 }else {
	             map.put("targetMonth",targetMonth);
	    		 List<TargetSalesEntity> monthSalesEntities = targetSalesDao.selectMonthSalesTarget(map);
	    		 result.put("monthSalesEntities", monthSalesEntities);
	    	 }
	     }else {
	    	 map.put("targetYear",targetYear);
	         map.put("brandSeq",baseUserEntity.getBrandSeq());
	         map.put("tag",type);
	    	 if(targetMonth==null) {
	    		 List<TargetShopEntity> monthSalesEntities= baseMapper.selectTotalMonthTarget(map);
	    		 result.put("monthSalesEntities", monthSalesEntities);
	    	 }else {
	    		 map.put("targetMonth",targetMonth);
	    		 List<TargetSalesEntity> monthSalesEntities = targetSalesDao.selectMonthSalesTarget(map);
	    		 result.put("monthSalesEntities", monthSalesEntities);
	    	 } 
	     }
		return result;
	}

	@Override
	public PageUtils getTargetList(Page page, Integer seq, Map<String, Object> params) throws Exception {
		Map<String,Object> map = new HashMap<>(10);
        map.put("targetYear",params.get("year"));
        map.put("targetMonth",params.get("month"));
        map.put("brandSeq",seq);
        List<TargetShopEntity> list = baseMapper.selectTotalShopTargetAnalyzeByYear(page,map);
        List<SaleShoesDetailEntity> saleList = saleShoesDetailDao.selectShopCompleteMoneyYear(map);
        for(int i = 0;i < list.size();i++) {
            for(int j = 0;j < saleList.size();j++) {
            	Integer month=list.get(i).getTargetMonth();
            	Integer completeMonth=saleList.get(j).getCompleteMonth();
                if(saleList.get(j).getShopSeq().equals(list.get(i).getShopSeq())&&month==completeMonth) {
                    if(saleList.get(j).getCompleteMoney() != null&&saleList.get(j).getCompleteMoney().compareTo(BigDecimal.ZERO) > 0) {
                        list.get(i).setCompleteMoney(saleList.get(j).getCompleteMoney());
                        if(list.get(i).getStandardMoney() != null && list.get(i).getStandardMoney().compareTo(BigDecimal.ZERO) > 0) {
                            list.get(i).setCompletePercent(list.get(i).getCompleteMoney().divide(list.get(i).getStandardMoney(),2,BigDecimal.ROUND_HALF_UP));
                        }else {
                        	 list.get(i).setCompletePercent(BigDecimal.ZERO);
                        }
                        if(list.get(i).getMiddleMoney() != null && list.get(i).getMiddleMoney().compareTo(BigDecimal.ZERO) > 0) {
                        	 list.get(i).setMiddlePercent(list.get(i).getCompleteMoney().divide(list.get(i).getMiddleMoney(),2,BigDecimal.ROUND_HALF_UP));
                        }else {
                        	 list.get(i).setMiddlePercent(BigDecimal.ZERO);
                        }
                        if(list.get(i).getAdvanceMoney() != null && list.get(i).getAdvanceMoney().compareTo(BigDecimal.ZERO) > 0) {
                       	 list.get(i).setAdvancePercent(list.get(i).getCompleteMoney().divide(list.get(i).getAdvanceMoney(),2,BigDecimal.ROUND_HALF_UP));
                       }else {
                       	 list.get(i).setAdvancePercent(BigDecimal.ZERO);
                       }
                        
                    }else {
                    	 list.get(i).setCompleteMoney(BigDecimal.ZERO);
                    	 list.get(i).setCompletePercent(BigDecimal.ZERO);
                    	 list.get(i).setMiddlePercent(BigDecimal.ZERO);
                    	 list.get(i).setAdvancePercent(BigDecimal.ZERO);
                    }
                }
                if(list.get(i).getCompleteMoney()==null) {
                	list.get(i).setCompleteMoney(BigDecimal.ZERO);
                }
                if( list.get(i).getStandardMoney()==null) {
                	 list.get(i).setStandardMoney(BigDecimal.ZERO);
                }
                if(list.get(i).getCompletePercent()==null) {
                	list.get(i).setCompletePercent(BigDecimal.ZERO);
                }
                if( list.get(i).getMiddleMoney()==null) {
               	 list.get(i).setMiddleMoney(BigDecimal.ZERO);
               }
               if(list.get(i).getMiddlePercent()==null) {
               	list.get(i).setMiddlePercent(BigDecimal.ZERO);
               }
               if( list.get(i).getAdvanceMoney()==null) {
              	 list.get(i).setAdvanceMoney(BigDecimal.ZERO);
              }
              if(list.get(i).getAdvancePercent()==null) {
              	list.get(i).setAdvancePercent(BigDecimal.ZERO);
              }
            }
        }
        page.setRecords(list);
        return new PageUtils(page);
	}

	@Override
	public void parseJRDExcelToSave(MultipartFile file, BaseUserEntity userEntity,Integer year) throws Exception {
		  List<List<Object>> rows = ImportMultipartExcelUtil.importExcel(file);
	        
	        if (rows.isEmpty()) {
	            throw new RRException("Excel内容为空");
	        }
	        Integer userSeq=userEntity.getSeq();
	        for (int i = 2; i < rows.size(); i++) {
	        	List row = rows.get(i);
	        	String shopName = row.get(0).toString();
	        	  if(StringUtils.isNotBlank(shopName)) {
	        		  //根据shopName查询shopSeq
	        		  BaseShopEntity baseShopEntity=new BaseShopEntity();
	        		  baseShopEntity.setName(shopName);
	        		  baseShopEntity=baseShopDao.selectOne(baseShopEntity);
	        		  Integer shopSeq=baseShopEntity.getSeq();
	        		  for(int j = 1; j < row.size(); j++) {
		        		  Integer month=((j-1)/3);
		        		  Integer tag=j%3;
		        		  if(tag==0) {
		        			  tag=3; 
		        		  }
		        		  if(tag>0) {
		        			  month= month+1;
		        		  }
		        		  String money= row.get(j).toString();
		        		  if(StringUtils.isNotBlank(money)) {
		        			  TargetShopEntity targetShopEntity=new TargetShopEntity();
		        			  targetShopEntity.setShopSeq(shopSeq);
		        			  targetShopEntity.setTargetYear(year);
		        			  targetShopEntity.setTargetMonth(month);
		        			  targetShopEntity.setTag(tag);  
		        			  targetShopEntity=baseMapper.selectOne(targetShopEntity);
		        			  if(targetShopEntity!=null) {
		        				  targetShopEntity.setMoney(new BigDecimal(money));
		        				  targetShopEntity.setInputTime(new Date());
		        				baseMapper.updateById(targetShopEntity);
		        			  }else {
		        				  targetShopEntity=new TargetShopEntity();
		        				  targetShopEntity.setShopSeq(shopSeq);
			        			  targetShopEntity.setTargetYear(year);
			        			  targetShopEntity.setTargetMonth(month);
		        				  targetShopEntity.setMoney(new BigDecimal(money));
			        			  targetShopEntity.setCreator(userSeq);
			        			  targetShopEntity.setDel(0);
			        			  targetShopEntity.setTag(tag);  
			        			  targetShopEntity.setInputTime(new Date());
			        			  baseMapper.insert(targetShopEntity);
		        			  }
		        		  }
		        	  } 
	        	  }
	        	 
	        }
	}


}
