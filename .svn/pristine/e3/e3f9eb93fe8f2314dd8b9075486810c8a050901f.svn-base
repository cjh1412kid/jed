package io.nuite.modules.order_platform_app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.dao.TargetSalesDao;
import io.nuite.modules.order_platform_app.dao.TargetShopDao;
import io.nuite.modules.order_platform_app.entity.TargetSalesEntity;
import io.nuite.modules.order_platform_app.entity.TargetShopEntity;
import io.nuite.modules.order_platform_app.service.TargetSalesService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 导购员指标Service实现类
 * @author: jxj
 * @create: 2019-05-15 15:11
 */
@Service
public class TargetSalesServiceImpl extends ServiceImpl<TargetSalesDao,TargetSalesEntity> implements TargetSalesService {
    @Autowired
    private TargetShopDao targetShopDao;

    @Override
    public boolean insertOrUpdateTarget(String list, BaseUserEntity baseUserEntity) throws Exception {
        List<TargetSalesEntity> standardtargetSalesEntities = new ArrayList<>(10);
        List<TargetSalesEntity> middletargetSalesEntities = new ArrayList<>(10);
        List<TargetSalesEntity> advancetargetSalesEntities = new ArrayList<>(10);
        JSONArray jsonArray = JSONArray.fromObject(list);
        for(int i = 0;i < jsonArray.size();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Map<String,Object> map = (Map<String, Object>) JSONObject.toBean(jsonObject,Map.class);
            if(StringUtils.isNotBlank((map.get("standardMoney").toString()))&&!"0".equals(map.get("standardMoney").toString())) {
            TargetSalesEntity standardtargetSalesEntity = new TargetSalesEntity();
            standardtargetSalesEntity.setSalesSeq(Integer.parseInt(map.get("salesSeq").toString()));
            standardtargetSalesEntity.setMoney(new BigDecimal(map.get("standardMoney").toString()).setScale(2,BigDecimal.ROUND_HALF_UP));
            standardtargetSalesEntity.setTargetYear(Integer.parseInt(map.get("targetYear").toString()));
            standardtargetSalesEntity.setTargetMonth(Integer.parseInt(map.get("targetMonth").toString()));
            standardtargetSalesEntity.setTargetShopSeq(Integer.parseInt(map.get("standardTargetShopSeq").toString()));
            standardtargetSalesEntity.setTag(1);
            standardtargetSalesEntity.setInputTime(new Date());
            standardtargetSalesEntities.add(standardtargetSalesEntity);
            }
            if(StringUtils.isNotBlank((map.get("middleMoney").toString()))&&!"0".equals(map.get("middleMoney").toString())) {
            TargetSalesEntity middletargetSalesEntity = new TargetSalesEntity();
            middletargetSalesEntity.setSalesSeq(Integer.parseInt(map.get("salesSeq").toString()));
            middletargetSalesEntity.setMoney(new BigDecimal(map.get("middleMoney").toString()).setScale(2,BigDecimal.ROUND_HALF_UP));
            middletargetSalesEntity.setTargetYear(Integer.parseInt(map.get("targetYear").toString()));
            middletargetSalesEntity.setTargetMonth(Integer.parseInt(map.get("targetMonth").toString()));
            middletargetSalesEntity.setTargetShopSeq(Integer.parseInt(map.get("middleTargetShopSeq").toString()));
            middletargetSalesEntity.setTag(2);
            middletargetSalesEntity.setInputTime(new Date());
            middletargetSalesEntities.add(middletargetSalesEntity);
            }
            if(StringUtils.isNotBlank((map.get("advanceMoney").toString()))&&!"0".equals(map.get("advanceMoney").toString())) {
            TargetSalesEntity advancetargetSalesEntity = new TargetSalesEntity();
            advancetargetSalesEntity.setSalesSeq(Integer.parseInt(map.get("salesSeq").toString()));
            advancetargetSalesEntity.setMoney(new BigDecimal(map.get("advanceMoney").toString()).setScale(2,BigDecimal.ROUND_HALF_UP));
            advancetargetSalesEntity.setTargetYear(Integer.parseInt(map.get("targetYear").toString()));
            advancetargetSalesEntity.setTargetMonth(Integer.parseInt(map.get("targetMonth").toString()));
            advancetargetSalesEntity.setTargetShopSeq(Integer.parseInt(map.get("advanceTargetShopSeq").toString()));
            advancetargetSalesEntity.setTag(3);
            advancetargetSalesEntity.setInputTime(new Date());
            advancetargetSalesEntities.add(advancetargetSalesEntity);
            }
        }
        Map<String,Object> map = new HashMap<>(10);
     
        Boolean isSuccess=false;
        if(standardtargetSalesEntities.size()>0) {
        map.put("targetYear",standardtargetSalesEntities.get(0).getTargetYear());
        map.put("targetMonth",standardtargetSalesEntities.get(0).getTargetMonth());
        map.put("shopSeq",baseUserEntity.getShopSeq());
        map.put("tag", 1);
        TargetSalesEntity targetSalesEntity = baseMapper.selectMonthTarget(map);
        BigDecimal total = new BigDecimal(0);
        for (int i = 0;i < standardtargetSalesEntities.size();i++) {
            total = total.add(standardtargetSalesEntities.get(i).getMoney());
            if(total.compareTo(targetSalesEntity.getMonthMoney()) > 0) {
                throw new RuntimeException("导购标准总指标大于门店标准指标");
            }
            Wrapper<TargetShopEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("Target_Year",standardtargetSalesEntities.get(i).getTargetYear());
            wrapper.eq("Target_Month",standardtargetSalesEntities.get(i).getTargetMonth());
            wrapper.eq("Shop_Seq",baseUserEntity.getShopSeq());
            wrapper.eq("tag",1);
            List<TargetShopEntity> targetShopEntities = targetShopDao.selectList(wrapper);
            standardtargetSalesEntities.get(i).setTargetShopSeq(targetShopEntities.get(0).getSeq());
            standardtargetSalesEntities.get(i).setCreator(baseUserEntity.getSeq());
        }
        Wrapper<TargetSalesEntity> targetSalesEntityWrapper = new EntityWrapper<>();
        System.out.println(standardtargetSalesEntities.get(0).getTargetShopSeq());
        targetSalesEntityWrapper.eq("Target_Shop_Seq",standardtargetSalesEntities.get(0).getTargetShopSeq());
        baseMapper.delete(targetSalesEntityWrapper);
        isSuccess=retBool(baseMapper.insertTargetSales(standardtargetSalesEntities));
        }
        if(middletargetSalesEntities.size()>0) {
        map.put("targetYear",middletargetSalesEntities.get(0).getTargetYear());
        map.put("targetMonth",middletargetSalesEntities.get(0).getTargetMonth());
        map.put("shopSeq",baseUserEntity.getShopSeq());
        map.put("tag", 2);
        TargetSalesEntity targetSalesEntity2 = baseMapper.selectMonthTarget(map);
        BigDecimal total2 = new BigDecimal(0);
        for (int i = 0;i < middletargetSalesEntities.size();i++) {
        	total2 = total2.add(middletargetSalesEntities.get(i).getMoney());
            if(total2.compareTo(targetSalesEntity2.getMonthMoney()) > 0) {
                throw new RuntimeException("导购中级总指标大于门店中级指标");
            }
            Wrapper<TargetShopEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("Target_Year",middletargetSalesEntities.get(i).getTargetYear());
            wrapper.eq("Target_Month",middletargetSalesEntities.get(i).getTargetMonth());
            wrapper.eq("Shop_Seq",baseUserEntity.getShopSeq());
            wrapper.eq("tag",2);
            List<TargetShopEntity> targetShopEntities = targetShopDao.selectList(wrapper);
            middletargetSalesEntities.get(i).setTargetShopSeq(targetShopEntities.get(0).getSeq());
            middletargetSalesEntities.get(i).setCreator(baseUserEntity.getSeq());
        }
        Wrapper<TargetSalesEntity> targetSalesEntityWrapper = new EntityWrapper<>();
        System.out.println(middletargetSalesEntities.get(0).getTargetShopSeq());
        targetSalesEntityWrapper.eq("Target_Shop_Seq",middletargetSalesEntities.get(0).getTargetShopSeq());
        baseMapper.delete(targetSalesEntityWrapper);
        isSuccess=retBool(baseMapper.insertTargetSales(middletargetSalesEntities));
        }
        if(advancetargetSalesEntities.size()>0) {
        map.put("targetYear",advancetargetSalesEntities.get(0).getTargetYear());
        map.put("targetMonth",advancetargetSalesEntities.get(0).getTargetMonth());
        map.put("shopSeq",baseUserEntity.getShopSeq());
        map.put("tag", 3);
        TargetSalesEntity targetSalesEntity3 = baseMapper.selectMonthTarget(map);
        BigDecimal total3 = new BigDecimal(0);
        for (int i = 0;i < advancetargetSalesEntities.size();i++) {
        	total3 = total3.add(advancetargetSalesEntities.get(i).getMoney());
            if(total3.compareTo(targetSalesEntity3.getMonthMoney()) > 0) {
                throw new RuntimeException("导购高级总指标大于门店高级指标");
            }
            Wrapper<TargetShopEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("Target_Year",advancetargetSalesEntities.get(i).getTargetYear());
            wrapper.eq("Target_Month",advancetargetSalesEntities.get(i).getTargetMonth());
            wrapper.eq("Shop_Seq",baseUserEntity.getShopSeq());
            wrapper.eq("tag",3);
            List<TargetShopEntity> targetShopEntities = targetShopDao.selectList(wrapper);
            advancetargetSalesEntities.get(i).setTargetShopSeq(targetShopEntities.get(0).getSeq());
            advancetargetSalesEntities.get(i).setCreator(baseUserEntity.getSeq());
        }
        Wrapper<TargetSalesEntity> targetSalesEntityWrapper = new EntityWrapper<>();
        targetSalesEntityWrapper.eq("Target_Shop_Seq",advancetargetSalesEntities.get(0).getTargetShopSeq());
        baseMapper.delete(targetSalesEntityWrapper);
        isSuccess=retBool(baseMapper.insertTargetSales(advancetargetSalesEntities));
        }
        if(standardtargetSalesEntities.size()==0&&middletargetSalesEntities.size()==0&& advancetargetSalesEntities.size()==0) {
        	  throw new RuntimeException("未添加任何相关指标");
        }
        return isSuccess;
        
    }
}
