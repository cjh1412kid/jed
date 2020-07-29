package io.nuite.modules.system.service.impl.order_platform;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.order_platform_app.dao.MeetingPlanDao;
import io.nuite.modules.order_platform_app.dao.MeetingRemindDao;
import io.nuite.modules.order_platform_app.entity.MeetingPlanEntity;
import io.nuite.modules.sr_base.dao.*;
import io.nuite.modules.sr_base.entity.*;
import io.nuite.modules.system.service.SystemGoodsCategoryService;
import io.nuite.modules.system.service.order_platform.SysMeetingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SysMeetingPlanServiceImpl implements SysMeetingPlanService {
    @Autowired
    private MeetingPlanDao meetingPlanDao;
    
    @Autowired
    private BaseAreaDao baseAreaDao;
    
    @Autowired
    private BaseAgentDao baseAgentDao;
    
    @Autowired
    private GoodsPeriodDao goodsPeriodDao;
    
    @Autowired
    private GoodsCategoryDao goodsCategoryDao;
    
    @Autowired
    private GoodsSXDao goodsSXDao;
    
    @Autowired
    private GoodsSXOptionDao goodsSXOptionDao;
    
    @Autowired
    private MeetingRemindDao meetingRemindDao;
    
    @Autowired
    SystemGoodsCategoryService categoryService;
    
    @Override
    public Page<Map<String, Object>> getShopPlanList(Page page, Integer companySeq, Integer seasonSeq, Integer year, Integer uploadState) {
        List<Map<String, Object>> list = meetingPlanDao.getShopPlanList(page, companySeq, seasonSeq, year, uploadState);
        page.setRecords(list);
        return page;
    }
    
    /**
     * 所有波次列表
     */
    @Override
    public List<GoodsPeriodEntity> getPeriodListByBrandSeq(Integer brandSeq) {
        Wrapper<GoodsPeriodEntity> wrapper = new EntityWrapper<GoodsPeriodEntity>();
        wrapper.where("Brand_Seq = {0}", brandSeq).orderBy("Seq DESC");
        return goodsPeriodDao.selectList(wrapper);
    }
    
    /**
     * 订货计划详细列表
     */
    @Override
    public PageUtils getUserPlanDetailsList(Integer companySeq, Integer userSeq, Integer periodSeq, Integer pageNo, Integer pageSize) {
        
        //查询用户某一波次的全部订货计划列表
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        Wrapper<MeetingPlanEntity> meetingPlanWrapper = new EntityWrapper<MeetingPlanEntity>();
        meetingPlanWrapper.where("User_Seq = {0} AND Period_Seq = {1}", userSeq, periodSeq);
        List<Map<String, Object>> meetingPlanList = meetingPlanDao.selectMapsPage(page, meetingPlanWrapper);
        
        GoodsSXEntity goodsSX2Entity = new GoodsSXEntity();
        goodsSX2Entity.setCompanySeq(companySeq);
        goodsSX2Entity.setSXId("SX2");
        goodsSX2Entity = goodsSXDao.selectOne(goodsSX2Entity);
        
        GoodsSXEntity goodsSX3Entity = new GoodsSXEntity();
        goodsSX3Entity.setCompanySeq(companySeq);
        goodsSX3Entity.setSXId("SX3");
        goodsSX3Entity = goodsSXDao.selectOne(goodsSX3Entity);
        
        /** 订货会期间实际购买的订单所有商品 **/
//		//获取该用户 该波次订货会期间的所有订单
//   		GoodsPeriodEntity goodsPeriodEntity = goodsPeriodDao.selectById(periodSeq);
//		List<OrderEntity> orderEntityList = meetingPlanService.getAllOrderByUserSeq(userSeq, goodsPeriodEntity);
//		List<Integer> orderSeqList = new ArrayList<Integer>();
//		for(OrderEntity orderEntity : orderEntityList) {
//			orderSeqList.add(orderEntity.getSeq());
//		}
//
//		//获取订单的所有商品列表
//		List<OrderProductsEntity> orderProductsEntityList = new ArrayList<OrderProductsEntity>();
//		if(orderSeqList.size() > 0) {
//			orderProductsEntityList = meetingPlanService.getAllOrderProductByOrderSeqList(orderSeqList);
//		}
        /** 订货会期间实际购买的订单所有商品 **/
        
        for (Map<String, Object> map : meetingPlanList) {
            //分类名称
            GoodsCategoryEntity goodsCategoryEntity = goodsCategoryDao.selectById((int) map.get("categorySeq"));
            map.put("categoryName", goodsCategoryEntity.getName());
            
            //SX2Value名称
            GoodsSXOptionEntity goodsSX2OptionEntity = new GoodsSXOptionEntity();
            goodsSX2OptionEntity.setSXSeq(goodsSX2Entity.getSeq());
            goodsSX2OptionEntity.setCode((String) map.get("SX2"));
            goodsSX2OptionEntity = goodsSXOptionDao.selectOne(goodsSX2OptionEntity);
            map.put("SX2Value", goodsSX2OptionEntity.getValue());
            
            //SX3Value名称
            GoodsSXOptionEntity goodsSX3OptionEntity = new GoodsSXOptionEntity();
            goodsSX3OptionEntity.setSXSeq(goodsSX3Entity.getSeq());
            goodsSX3OptionEntity.setCode((String) map.get("SX3"));
            goodsSX3OptionEntity = goodsSXOptionDao.selectOne(goodsSX3OptionEntity);
            map.put("SX3Value", goodsSX3OptionEntity.getValue());
            
            /** 计算实际购买量  **/
//    		Integer num = 0;
//    		BigDecimal money = BigDecimal.valueOf(0);
//    		Set<String> goodsIDsSet = new TreeSet<String>();
//    		Integer goodsIDs = 0;
//    		//查询每个shoesDatesSeq对应的鞋子是否和订货计划中的Category_Seq, SX2, SX3一致
//    		for(int i = 0; i < orderProductsEntityList.size(); i++) {
//    			OrderProductsEntity orderProductsEntity = orderProductsEntityList.get(i);
//    			GoodsShoesEntity goodsShoes = meetingPlanService.getGoodsShoesByShoesDateSeq(orderProductsEntity.getShoesDataSeq());
//    			
//				//移除掉波次不一致的订单商品，下次meetingPlan循环不用查询，提高效率
//    			if(!goodsShoes.getPeriodSeq().equals(periodSeq)) {
//    				orderProductsEntityList.remove(i);
//    				i--;
//    				continue;
//    			}
//    			
//    			if(goodsShoes.getPeriodSeq().equals(periodSeq) 
//    					&& goodsShoes.getCategorySeq().equals((int)map.get("categorySeq"))
//    					&& goodsShoes.getSX2().equals((String)map.get("SX2"))
//    					&& goodsShoes.getSX3().equals((String)map.get("SX3"))) {
//    				num = num + orderProductsEntity.getBuyCount();
//    				money = money.add(orderProductsEntity.getProductPrice().multiply(BigDecimal.valueOf(orderProductsEntity.getBuyCount())));
//    				goodsIDsSet.add(goodsShoes.getGoodID());
//    				
//    				//如果商品3个类型与本条订货任务一致，计算好数量后，直接移除掉，下次meetingPlan循环不用查询（后面的meetingPlan一定不会一致），提高效率
//    				orderProductsEntityList.remove(i);
//    				i--;
//    			}
//    		}
//    		goodsIDs = goodsIDsSet.size();
//            
//    		map.put("actualNum", num);
//    		map.put("actualMoney", money);
//    		map.put("actualGoodsIDs", goodsIDs);
            /** 计算实际购买量  **/
            
        }
        return new PageUtils(meetingPlanList, page.getTotal(), pageSize, pageNo);
    }
    
    @Override
    public Page getShopPlanDetailsList(Page page, Integer companySeq, Integer shopSeq, Integer year, Integer seasonSeq) {
        
        List<MeetingPlanEntity> meetingPlanList = meetingPlanDao.selectByPageAndCondition(page, shopSeq, year, seasonSeq);
        page.setRecords(meetingPlanList);
        
        return page;
    }
    
    /**
     * 删除用户的订货计划
     */
    @Override
    public void deleteMeetingPlan(Integer shopSeq, Integer year, Integer seasonSeq) {
        Wrapper<MeetingPlanEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("Shop_Seq", shopSeq);
        wrapper.eq("Year", year);
        wrapper.eq("Season_Seq", seasonSeq);
        meetingPlanDao.delete(wrapper);
    }
    
    /**
     * 批量插入订货计划
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBatchMeetingPlan(Integer[] shopSeqArr, BaseUserEntity userEntity, List<List<Object>> rows, Integer year, Integer seasonSeq) {
        
        //获取每一行数据，创建一个计划实体
        List<MeetingPlanEntity> meetingPlanList = new ArrayList<>();
        MeetingPlanEntity meetingPlanEntity;
        Integer companySeq = userEntity.getCompanySeq();
        int userSeq = userEntity.getSeq().intValue();
        //分类map
        HashMap<String, List<GoodsCategoryEntity>> categoryMap = new HashMap<>();
        
        //验证重复Map <'大类+中类+风格',行数索引>
        Map<String, Integer> duplicateMap = new HashMap<String, Integer>();
        
        //记录大类中类的序号
        Map<String, List<GoodsCategoryEntity>> daLeiAndZhLeiMap = new HashMap<>();
        
        for (int i = 1; i < rows.size(); i++) {
            List<Object> row = rows.get(i);
            if (row.size() != 5 || row.get(0) == null || StringUtils.isBlank(row.get(0).toString())
                || row.get(1) == null || StringUtils.isBlank(row.get(1).toString())
                || row.get(2) == null || StringUtils.isBlank(row.get(2).toString())
                || row.get(3) == null || StringUtils.isBlank(row.get(3).toString())
                || row.get(4) == null || StringUtils.isBlank(row.get(4).toString())) {
                throw new RRException("excel表内容不符合要求，第" + (i + 1) + "行数据不能为空，请删除空行");
            }
            
            //创建实体
            meetingPlanEntity = new MeetingPlanEntity();
            
            //大类
            String daLei = row.get(0).toString().trim();
            //中类
            String zhLei = row.get(1).toString().trim();
            //风格
            String style = row.get(2).toString().trim();
            //订货款数
            Integer goodIDNum = Integer.valueOf(row.get(3).toString());
            //订货数量
            Integer planNum = Integer.valueOf(row.get(4).toString());
            
            //大类、中类判断
            GoodsCategoryEntity daLeiEntity = null;
            GoodsCategoryEntity ZhLeiEntity = null;
            
            if (!categoryMap.containsKey(daLei + "+" + zhLei)) {
                daLeiEntity = categoryService.selectOne(new EntityWrapper<GoodsCategoryEntity>()
                    .eq("Company_Seq", userEntity.getCompanySeq())
                    .eq("Category_Code", daLei));
                
                if (daLeiEntity == null) {
                    throw new RRException("第" + (i + 1) + "行数据：大类编号" + daLei + "不存在，请在分类管理里添加");
                }
                
                ZhLeiEntity = categoryService.selectOne(new EntityWrapper<GoodsCategoryEntity>()
                    .eq("ParentSeq", daLeiEntity.getSeq()).eq("Category_Code", zhLei));
                
                if (ZhLeiEntity == null) {
                    throw new RRException("第" + (i + 1) + "行数据：中类编号" + zhLei + "不存在，请在分类管理里添加");
                } else {
                    List<GoodsCategoryEntity> list = new ArrayList();
                    getChildCategoryList(list, Collections.singletonList(ZhLeiEntity));
                    categoryMap.put(daLei + "+" + zhLei, list);
                    
                    List<GoodsCategoryEntity> daleiAndZhLei = new ArrayList<>();
                    daleiAndZhLei.add(daLeiEntity);
                    daleiAndZhLei.add(ZhLeiEntity);
                    
                    daLeiAndZhLeiMap.put(daLei + "+" + zhLei, daleiAndZhLei);
                    
                    meetingPlanEntity.setDaLeiSeq(daLeiEntity.getSeq());
                    meetingPlanEntity.setZhLeiSeq(ZhLeiEntity.getSeq());
                }
                
            } else {
                List<GoodsCategoryEntity> daleiAndZhLei2 = daLeiAndZhLeiMap.get(daLei + "+" + zhLei);
                daLeiEntity = daleiAndZhLei2.get(0);
                ZhLeiEntity = daleiAndZhLei2.get(1);
                
                meetingPlanEntity.setDaLeiSeq(daLeiEntity.getSeq());
                meetingPlanEntity.setZhLeiSeq(ZhLeiEntity.getSeq());
            }
            
            //判断风格是否存在
            List<GoodsCategoryEntity> goodsCategoryEntities = categoryMap.get(daLei + "+" + zhLei);
            boolean fenggeExist = false;
            for (GoodsCategoryEntity goodsCategoryEntity : goodsCategoryEntities) {
                if (goodsCategoryEntity.getCatetoryCode() != null && goodsCategoryEntity.getCatetoryCode().equals(style)) {
                    fenggeExist = true;
                    meetingPlanEntity.setCategorySeq(goodsCategoryEntity.getSeq());
                    break;
                }
            }
            if (!fenggeExist) {
                throw new RRException("第" + (i + 1) + "行数据：风格编号" + style
                    + "不存在，请在分类管理里添加(" + daLeiEntity.getName() + "-" + ZhLeiEntity.getName() + "-  )");
            }
            
            //验证重复  大类+中类+风格
            String key = daLei + zhLei + style;
            if (duplicateMap.containsKey(key)) {
                int i1 = duplicateMap.get(key);
                throw new RRException("excel表内容不符合要求，第" + (i + 1) + "行数据和第" + (i1 + 1) + "行数据重复");
            } else {
                duplicateMap.put(key, i);
            }
            
            meetingPlanEntity.setPlanGoodsIDs(goodIDNum);
            meetingPlanEntity.setPlanNum(planNum);
            meetingPlanEntity.setInputTime(new Date());
            meetingPlanEntity.setSeasonSeq(seasonSeq);
            meetingPlanEntity.setYear(year);
            meetingPlanEntity.setCompanySeq(companySeq);
            meetingPlanEntity.setUserSeq(userSeq);
            meetingPlanList.add(meetingPlanEntity);
        }
        
        for (Integer shopSeq : shopSeqArr) {
            //删除原来的计划
            deleteMeetingPlan(shopSeq, year, seasonSeq);
            
            //新增新的计划
            for (MeetingPlanEntity meetingPlan : meetingPlanList) {
                meetingPlan.setShopSeq(shopSeq);
                meetingPlanDao.insert(meetingPlan);
            }
        }
        
    }
    
    /**
     * 获取父级分类下的所有子分类
     *
     * @param categoryList
     * @param parentCategorys
     */
    private void getChildCategoryList(List<GoodsCategoryEntity> categoryList, List<GoodsCategoryEntity> parentCategorys) {
        for (GoodsCategoryEntity categoryEntity : parentCategorys) {
            
            List<GoodsCategoryEntity> categoryEntities = categoryService.selectList(new EntityWrapper<GoodsCategoryEntity>()
                .eq("ParentSeq", categoryEntity.getSeq()));
            if (categoryEntities != null && !categoryEntities.isEmpty()) {
                categoryList.addAll(categoryEntities);
                getChildCategoryList(categoryList, categoryEntities);
            }
            
        }
    }
    
}
