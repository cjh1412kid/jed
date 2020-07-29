package io.nuite.modules.order_platform_app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;

import io.nuite.modules.order_platform_app.dao.AllocateTransferTransferInApplicationDao;
import io.nuite.modules.order_platform_app.dao.AllocateTransferTransferInApplicationDetailDao;
import io.nuite.modules.order_platform_app.dao.ExpressCompanyDao;
import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationDetailEntity;
import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationEntity;
import io.nuite.modules.order_platform_app.service.AllocateTransferTransferInApplicationService;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.system.dao.order_platform.AllocateTransferFactoryDao;
import io.nuite.modules.system.entity.order_platform.AllocateTransferFactoryEntity;

/**
 * @description: 调入申请表ServiceImpl
 * @author: jxj
 * @create: 2019-12-03 14:26
 */
@Service
public class AllocateTransferTransferInApplicationServiceImpl extends ServiceImpl<AllocateTransferTransferInApplicationDao,AllocateTransferTransferInApplicationEntity> implements AllocateTransferTransferInApplicationService {

    @Autowired
    private AllocateTransferTransferInApplicationDao allocateTransferTransferInApplicationDao;

    @Autowired
    private AllocateTransferTransferInApplicationDetailDao allocateTransferTransferInApplicationDetailDao;

    @Autowired
    private BaseShopDao baseShopDao;
    
    @Autowired
    private GoodsSizeDao goodsSizeDao;
    
    @Autowired
    private ExpressCompanyDao expressCompanyDao;

	@Autowired
	private AllocateTransferFactoryDao allocateTransferFactoryDao;
    
    
    
    /**
     * 获取门店某一天的调入或调出列表
     */
	@Override
	public List<Map<String, Object>> getDayTransferInOutList(Integer userShopSeq, Integer type, Integer state, String dateStr) {
		
		//查询这一天所有的调入或调出鞋子Seq
		Wrapper<AllocateTransferTransferInApplicationEntity> wrapper = new EntityWrapper<AllocateTransferTransferInApplicationEntity>();
		wrapper.setSqlSelect("Shoes_Seq AS shoesSeq, MIN(State) AS state");
		if(type == 1) {  //调入
			wrapper.where("InShop_Seq = {0}", userShopSeq);
		} else if (type == 2) {  //调出
			wrapper.where("OutShop_Seq = {0}", userShopSeq);
		} else {
			return null;
		}
		wrapper.where("InputTime >= {0} And InputTime <= {1}", dateStr + " 00:00:00", dateStr + " 23:59:59");
		wrapper.groupBy("Shoes_Seq");
		if(state != -1) {
			wrapper.having("MIN(State) = {0}", state);
		}
		
		
		List<Map<String, Object>> shoesList = allocateTransferTransferInApplicationDao.selectMaps(wrapper);
		
		
		//循环添加每个尺码的总数量，合计总数量（顺便也添加详情页需要的申请列表List，不再提供专门的详情接口）
		for(Map<String, Object> map : shoesList) {
			Integer shoesSeq = (Integer) map.get("shoesSeq");
			
			//加上鞋子Seq再次查询申请List，其他条件不变
			Wrapper<AllocateTransferTransferInApplicationEntity> wrapper1 = new EntityWrapper<AllocateTransferTransferInApplicationEntity>();
			if(type == 1) {  //调入
				wrapper1.where("InShop_Seq = {0}", userShopSeq);
			} else if (type == 2) {  //调出
				wrapper1.where("OutShop_Seq = {0}", userShopSeq);
			} else {
				return null;
			}
			wrapper1.where("InputTime >= {0} And InputTime <= {1}", dateStr + " 00:00:00", dateStr + " 23:59:59");
			wrapper1.where("Shoes_Seq = {0}", shoesSeq);
			wrapper1.orderBy("InputTime DESC");
			
			//本货号当天所有的调入或调出申请
			List<AllocateTransferTransferInApplicationEntity> transferInApplicationList = allocateTransferTransferInApplicationDao.selectList(wrapper1);
			
			List<Map<String, Object>> transferInApplicationMapList = new ArrayList<Map<String, Object>>();
			Map<String, Integer> sizeTotalNumMap = new LinkedHashMap<String, Integer>();
			Integer allTotalNum = 0;
			//循环当天所有申请，查询申请详情，计算每个尺码总数量和总总数量
			for(AllocateTransferTransferInApplicationEntity applicationEntity : transferInApplicationList) {
				Map<String, Object> transferInApplicationMap = new HashMap<String, Object>();
				transferInApplicationMap.put("seq", applicationEntity.getSeq());
				transferInApplicationMap.put("inShopSeq", applicationEntity.getInShopSeq());
				String inShopName = "";
				if(applicationEntity.getInShopSeq() == 0) {
					inShopName = "总部";
				} else {
					inShopName = baseShopDao.selectById(applicationEntity.getInShopSeq()).getName();
				}
				transferInApplicationMap.put("inShopName", inShopName);
				
				transferInApplicationMap.put("outShopSeq", applicationEntity.getOutShopSeq());
				String outShopName = "";
				if(applicationEntity.getOutShopSeq() == 0) {
					outShopName = "总部";
				} else {
					outShopName = baseShopDao.selectById(applicationEntity.getOutShopSeq()).getName();
				}
				transferInApplicationMap.put("outShopName", outShopName);
				
				transferInApplicationMap.put("state", applicationEntity.getState());
				transferInApplicationMap.put("remark", applicationEntity.getRemark());
				transferInApplicationMap.put("inputTime", applicationEntity.getInputTime());
				transferInApplicationMap.put("handleTime", applicationEntity.getHandleTime());
				transferInApplicationMap.put("type", applicationEntity.getType());
				transferInApplicationMap.put("erpOrderNum", applicationEntity.getErpOrderNum());
				transferInApplicationMap.put("expressCompanySeq", applicationEntity.getExpressCompanySeq());
				String expressCompanyName = "";
				if(applicationEntity.getExpressCompanySeq() != null) {
					expressCompanyName = expressCompanyDao.selectById(applicationEntity.getExpressCompanySeq()).getName();
				}
				transferInApplicationMap.put("expressCompanyName", expressCompanyName);
				transferInApplicationMap.put("expressNum", applicationEntity.getExpressNum());
				
				//查询申请详情，获取每个尺码数量的Map
				Map<String, Integer> sizeNumMap = null;
				if(applicationEntity.getType() == 0) { //App调拨
					sizeNumMap = getApplicationDetailSizeNumMap(applicationEntity.getSeq());
				} else if(applicationEntity.getType() == 1) { //后台调拨
					sizeNumMap = getFactoryApplicationDetailSizeNumMap(applicationEntity.getSeq());
				}
				transferInApplicationMap.put("sizeNumMap", sizeNumMap);
				
				//汇总尺码总数
				sizeTotalNumMap = mergeToSizeTotalNumMap(sizeTotalNumMap, sizeNumMap);
				//汇总总总数
				allTotalNum = addToAllTotalNum(allTotalNum, sizeNumMap);
				
				transferInApplicationMapList.add(transferInApplicationMap);
			}
			
			map.put("sizeTotalNumMap", sizeTotalNumMap);
			map.put("allTotalNum", allTotalNum);
			
			map.put("transferInOutDetail", transferInApplicationMapList);
			
		}
		
		return shoesList;
	}




	//查询申请详情表，组装每个尺码的数量的Map
	private Map<String, Integer> getApplicationDetailSizeNumMap(Integer applicationSeq) {
		Wrapper<AllocateTransferTransferInApplicationDetailEntity> wrapper = new EntityWrapper<AllocateTransferTransferInApplicationDetailEntity>();
		wrapper.where("Application_Seq = {0}", applicationSeq);
		wrapper.orderBy("Size_Seq ASC");
		List<AllocateTransferTransferInApplicationDetailEntity> list = allocateTransferTransferInApplicationDetailDao.selectList(wrapper);
		
		Map<String, Integer> sizeNumMap = new LinkedHashMap<String, Integer>();
		for(AllocateTransferTransferInApplicationDetailEntity detailEntity : list) {
			String size = goodsSizeDao.selectById(detailEntity.getSizeSeq()).getSizeName();
			
			sizeNumMap.put(size, detailEntity.getNum());
		}
		
		return sortByKey(sizeNumMap, false);
	}
    
	//查询工厂（总部）调拨表，组装每个尺码的数量的Map
	private Map<String, Integer> getFactoryApplicationDetailSizeNumMap(Integer applicationSeq) {
		Wrapper<AllocateTransferFactoryEntity> wrapper = new EntityWrapper<AllocateTransferFactoryEntity>();
		wrapper.where("Application_Seq = {0}", applicationSeq);
		wrapper.orderBy("Size_Seq ASC");
		List<AllocateTransferFactoryEntity> list = allocateTransferFactoryDao.selectList(wrapper);
		
		Map<String, Integer> sizeNumMap = new LinkedHashMap<String, Integer>();
		for(AllocateTransferFactoryEntity detailEntity : list) {
			String size = goodsSizeDao.selectById(detailEntity.getSizeSeq()).getSizeName();
			
			sizeNumMap.put(size, detailEntity.getNum());
		}
		
		return sortByKey(sizeNumMap, false);
	}
	
	
	//将每个尺码数量的Map汇总到总的尺码数量Map中
	private Map<String, Integer> mergeToSizeTotalNumMap(Map<String, Integer> sizeTotalNumMap, Map<String, Integer> sizeNumMap) {
		for(String key : sizeNumMap.keySet()) {
			if(sizeTotalNumMap.containsKey(key)) {
				sizeTotalNumMap.put(key, sizeTotalNumMap.get(key) + sizeNumMap.get(key));
			} else {
				sizeTotalNumMap.put(key, sizeNumMap.get(key));
			}
		}
		return sortByKey(sizeTotalNumMap, false);
	}
	
	
	
	//将每个尺码数量的Map中数量取出汇总
	private Integer addToAllTotalNum(Integer allTotalNum, Map<String, Integer> sizeNumMap) {
		for (String key : sizeNumMap.keySet()) {
			allTotalNum += sizeNumMap.get(key);
		}
		return allTotalNum;
	}
	
	
	
	 /**
     * 根据map的key排序
     * 
     * @param map 待排序的map
     * @param isDesc 是否降序，true：降序，false：升序
     * @return 排序好的map
     * @author zero 2019/04/08
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }
    
    
}
