package io.nuite.modules.order_platform_app.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.baomidou.mybatisplus.plugins.Page;

import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.order_platform_app.dao.AllocateTransferFictitiousMallDao;
import io.nuite.modules.order_platform_app.dao.AllocateTransferTransferInApplicationDao;
import io.nuite.modules.order_platform_app.dao.AllocateTransferTransferInApplicationDetailDao;
import io.nuite.modules.order_platform_app.dao.MessageDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.entity.AllocateTransferFictitiousMallEntity;
import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationDetailEntity;
import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationEntity;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.order_platform_app.service.AllocateTransferFictitiousMallService;
import io.nuite.modules.order_platform_app.service.PushRecordService;
import io.nuite.modules.order_platform_app.utils.JPushUtils;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Service
public class AllocateTransferFictitiousMallServiceImpl extends ServiceImpl<AllocateTransferFictitiousMallDao, AllocateTransferFictitiousMallEntity> implements AllocateTransferFictitiousMallService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ShoesDataDao shoesDataDao;
    
    @Autowired
    private BaseShopDao baseShopDao;
    
    @Autowired
    private AllocateTransferFictitiousMallDao allocateTransferFictitiousMallDao;
	
    @Autowired
    private AllocateTransferTransferInApplicationDao allocateTransferTransferInApplicationDao;
    
    @Autowired
    private AllocateTransferTransferInApplicationDetailDao allocateTransferTransferInApplicationDetailDao;
    
    @Autowired
    private BaseUserDao baseUserDao;
    
    @Autowired
    private MessageDao messageDao;
    
    @Autowired
    private JPushUtils jPushUtils;
    
    @Autowired
    private PushRecordService pushRecordService;

	@Autowired
	private GoodsSizeDao goodsSizeDao;
    
    
    
    
    /**
     * 获取门店某一鞋子各个尺码的 当前库存 + 调出数量（已加入虚拟商城数量）
     */
	@Override
	public List<Map<String, Object>> getShoesStockAndTransferOutNum(Integer shopSeq, Integer shoesSeq) throws Exception {
		List<Map<String, Object>> shoesStockAndTransferOutNumList = new ArrayList<Map<String, Object>>();
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
		wrapper.where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq).orderBy("Size_Seq ASC");
		List<ShoesDataEntity> shoesDataList = shoesDataDao.selectList(wrapper);
		List<Integer> sizeSeqList = allocateTransferFictitiousMallDao.selectShopAndExportSize(shopSeq,shoesSeq);
		for(Integer sizeSeq : sizeSeqList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("size", sizeSeq); //size和sizeseq相等
            map.put("stock", 0);
            for(ShoesDataEntity shoesDataEntity : shoesDataList) {
                if(sizeSeq.equals(shoesDataEntity.getSizeSeq())) {
                    map.put("stock", shoesDataEntity.getStock());
                }
            }
            //查询调出数量
            Integer transferOutNum = 0;
            AllocateTransferFictitiousMallEntity allocateTransferFictitiousMallEntity = new AllocateTransferFictitiousMallEntity();
            allocateTransferFictitiousMallEntity.setShopSeq(shopSeq);
            allocateTransferFictitiousMallEntity.setShoesSeq(shoesSeq);
            allocateTransferFictitiousMallEntity.setSizeSeq(sizeSeq);
            allocateTransferFictitiousMallEntity = allocateTransferFictitiousMallDao.selectOne(allocateTransferFictitiousMallEntity);
            if(allocateTransferFictitiousMallEntity != null) {
                transferOutNum = allocateTransferFictitiousMallEntity.getTransferOutNum();
            }
            map.put("transferOutNum", transferOutNum);
            shoesStockAndTransferOutNumList.add(map);
        }
		return shoesStockAndTransferOutNumList;
	}




	
	/**
	 * 确认调出（加入或修改虚拟商城）
	 */
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public void confirmTransferOut(Integer shopSeq, Integer shoesSeq, String sizeTransferOutNum) {
    	Date nowDate = new Date();
    	
		//1.删除可能已存在的虚拟商城数据
		Wrapper<AllocateTransferFictitiousMallEntity> wrapper = new EntityWrapper<AllocateTransferFictitiousMallEntity>();
		wrapper.where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq);
		allocateTransferFictitiousMallDao.delete(wrapper);
		
		//2.解析尺码导出数量Array，插入虚拟商城数据
		//[{size:35,num:2},{size:36,num:3}...]
        JSONArray sizeAndNumArray = JSONArray.fromObject(sizeTransferOutNum);
        for(int i = 0; i < sizeAndNumArray.size(); i++) {
            JSONObject sizeAndNum = sizeAndNumArray.getJSONObject(i);
            Integer size = sizeAndNum.getInt("size");
            Integer num = sizeAndNum.getInt("num");
            AllocateTransferFictitiousMallEntity allocateTransferFictitiousMallEntity = new AllocateTransferFictitiousMallEntity();
            allocateTransferFictitiousMallEntity.setShopSeq(shopSeq);
            allocateTransferFictitiousMallEntity.setShoesSeq(shoesSeq);
            allocateTransferFictitiousMallEntity.setColorSeq(null);
            allocateTransferFictitiousMallEntity.setSizeSeq(size); //sizeSeq与size相等
            allocateTransferFictitiousMallEntity.setTransferOutNum(num);
            allocateTransferFictitiousMallEntity.setInputTime(nowDate);
            allocateTransferFictitiousMallEntity.setDel(0);
            allocateTransferFictitiousMallEntity.setTransferOutType(1); //手动调出
            allocateTransferFictitiousMallDao.insert(allocateTransferFictitiousMallEntity);
        }
	}


	

	/**
	 * 获取鞋子某一尺码的各个门店的  调出数量（已加入虚拟商城数量）+ 当前库存
	 */
	@Override
	public List<Map<String, Object>> getAllShopsTransferOutNumAndStock(Integer shoesSeq, Integer shopRegionSeq,Integer shopSeq) {
		List<Map<String, Object>> transferOutNumAndStockList = new ArrayList<Map<String, Object>>();
		//获取虚拟商城调出当前货品的所有门店(除登陆人所在门店之外)
		Wrapper<AllocateTransferFictitiousMallEntity> wrapper = new EntityWrapper<AllocateTransferFictitiousMallEntity>();
		wrapper.where("Shoes_Seq = {0}", shoesSeq).setSqlSelect("Shop_Seq").groupBy("Shop_Seq").ne("Shop_Seq",shopSeq);
		List<Object> shopSeqList = allocateTransferFictitiousMallDao.selectObjs(wrapper);
		int i = 0;
        BaseShopEntity baseShop = baseShopDao.selectById(shopSeq);
        //当前门店经纬度
        BigDecimal lat = new BigDecimal(baseShop.getLat());
        BigDecimal lng = new BigDecimal(baseShop.getLng());
        List<Map<String, Object>> withRegional = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> withoutRegional = new ArrayList<Map<String, Object>>();
		//循环除当前门店之外的所有门店
        for (Object otherShopSeq : shopSeqList) {
        	//门店总调出数量
        	Integer shopTotalNum = 0;
        	Map<String,Object> shopMap = new HashMap<>(10);
        	//获取除当前门店之外门店的所有调出数据
			wrapper = new EntityWrapper<>();
			wrapper.eq("Shop_Seq",otherShopSeq).eq("Shoes_Seq",shoesSeq).orderBy("Size_Seq");
			List<AllocateTransferFictitiousMallEntity> fictitiousMallEntities = allocateTransferFictitiousMallDao.selectList(wrapper);
			//获取门店信息
			BaseShopEntity baseShopEntity = baseShopDao.selectById((Integer)otherShopSeq);
			Integer regionSeq = baseShopEntity.getRegionSeq();
			shopMap.put("shopName",baseShopEntity.getName());
			shopMap.put("shopSeq",baseShopEntity.getSeq());
			//通过经纬度计算距离
			BigDecimal length = lat.subtract(new BigDecimal(baseShopEntity.getLat()));
			BigDecimal width = lng.subtract(new BigDecimal(baseShopEntity.getLng()));
			BigDecimal distance = length.pow(2).add(width.pow(2));
			shopMap.put("distance",distance);
			List<Map<String,Object>> sizeAndNum = new ArrayList<>(10);
			//循环调出数据
			for(AllocateTransferFictitiousMallEntity fictitiousMallEntity : fictitiousMallEntities) {
				//计算门店总调出量
				shopTotalNum += fictitiousMallEntity.getTransferOutNum();
				Map<String,Object> sizeAndNumMap = new HashMap<>(10);
				sizeAndNumMap.put("size",goodsSizeDao.selectById(fictitiousMallEntity.getSizeSeq()).getSizeName());
				sizeAndNumMap.put("num",fictitiousMallEntity.getTransferOutNum());
				sizeAndNum.add(sizeAndNumMap);
			}
			shopMap.put("sizeAndNum",sizeAndNum);
			shopMap.put("shopTotalNum",shopTotalNum);
			//如果区域相同放到集合的前面
			if(regionSeq != null && regionSeq.equals(shopRegionSeq)) {
				transferOutNumAndStockList.add(i++, shopMap);
				withRegional.add(shopMap);
			//如果区域不同放到集合的后面
			} else {
				transferOutNumAndStockList.add(shopMap);
				withoutRegional.add(shopMap);
			}
		}

		//根据距离再次排序列表
        List<Map<String, Object>> transferOutNumAndStockListOrderByDistance = new ArrayList<Map<String, Object>>();
        Collections.sort(withRegional, new Comparator<Map<String,Object>>() {
            @Override
            public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {
                return new BigDecimal(arg0.get("distance").toString()).compareTo(new BigDecimal(arg1.get("distance").toString()));
            }
        });
        Collections.sort(withoutRegional, new Comparator<Map<String,Object>>() {
            @Override
            public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {
                return new BigDecimal(arg0.get("distance").toString()).compareTo(new BigDecimal(arg1.get("distance").toString()));
            }
        });
        transferOutNumAndStockListOrderByDistance.addAll(withRegional);
        transferOutNumAndStockListOrderByDistance.addAll(withoutRegional);
		return transferOutNumAndStockListOrderByDistance;
	}





	/**
	 * 确认调入（新增调货申请记录、详情， 新增调拨类型消息Message（申请方和调出方门店各一条（门店账号每人一条）））
	 */
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public void confirmTransferIn(Integer inShopSeq, Integer inUserSeq, Integer shoesSeq, String sizesShopsTransferInNum) {
		
    	Date nowDate = new Date();
		/* 1.新增调货申请记录、申请记录详情
			解析调入各个门店所有尺码对应的数量Array：1).取出不同的门店seq的Set，插入调入申请表；2).取出所有尺码数量，根据对应门店取出申请表seq，插入申请详情表
		*/
    	Set<Integer> outShopSeqSet = new TreeSet<Integer>();
        List<AllocateTransferTransferInApplicationDetailEntity> detailList = new ArrayList<AllocateTransferTransferInApplicationDetailEntity>();
        
    	//解析调入各个门店所有尺码对应的数量Array
		//[{shopSeq:1, sizeAndNum:[{size:35,num:10},{size:36,num:20}...]}, {}]
        JSONArray sizesShopsTransferInNumArray = JSONArray.fromObject(sizesShopsTransferInNum);
        for (int i = 0; i < sizesShopsTransferInNumArray.size(); i++) {
            //{shopSeq:1, sizeAndNum:[{size:35,num:10},{size:36,num:20}...]}
        	JSONObject sizesTransferInObject = sizesShopsTransferInNumArray.getJSONObject(i);
            int outShopSeq = sizesTransferInObject.getInt("shopSeq");

			outShopSeqSet.add(outShopSeq);
            
            //[{size:35,num:10},{size:36,num:20}...]
            JSONArray shopAndNumArray = sizesTransferInObject.getJSONArray("sizeAndNum");
            for(int j = 0; j < shopAndNumArray.size(); j++) {
            	//{size:35,num:10}
                JSONObject shopAndNumObject = shopAndNumArray.getJSONObject(j);
                Integer size = shopAndNumObject.getInt("size");
                Integer num = shopAndNumObject.getInt("num");
                
                AllocateTransferTransferInApplicationDetailEntity detailEntity = new AllocateTransferTransferInApplicationDetailEntity();
                detailEntity.setApplicationSeq(null);
                detailEntity.setShoesSeq(shoesSeq);
                detailEntity.setColorSeq(null);
                detailEntity.setSizeSeq(size);
                detailEntity.setNum(num);
                detailEntity.setOutShopSeq(outShopSeq);
                detailList.add(detailEntity);
            }
        }
		
		
        //1.1 插入调入申请表
          //调入方门店所有账号
        List<BaseUserEntity> inShopUserList = getShopAllUsers(inShopSeq);
        BaseShopEntity inShopEntity = baseShopDao.selectById(inShopSeq);
        
        Map<Integer, Integer> outShopSeqApplicationSeqMap = new HashMap<Integer, Integer>(); //调出方门店seq对应申请表seq的Map
        for(Integer outShopSeq : outShopSeqSet) {
        	AllocateTransferTransferInApplicationEntity applicationEntity = new AllocateTransferTransferInApplicationEntity();
        	applicationEntity.setInShopSeq(inShopSeq);
        	applicationEntity.setInUserSeq(inUserSeq);
        	applicationEntity.setShoesSeq(shoesSeq);
        	applicationEntity.setOutShopSeq(outShopSeq);
        	applicationEntity.setState(0);
        	applicationEntity.setRemark(null);
        	applicationEntity.setOutUserSeq(null);
        	applicationEntity.setInputTime(nowDate);
        	applicationEntity.setDel(0);
        	applicationEntity.setType(0);
        	allocateTransferTransferInApplicationDao.insert(applicationEntity);
        	outShopSeqApplicationSeqMap.put(outShopSeq, applicationEntity.getSeq());
        	
        	
            //2.新增调拨类型消息Message（申请方和调出方门店各一条（门店账号每人一条））
       	 		//给调出方 （所有门店账号）  -   新增调拨类型消息Message、发送极光推送、保存极光推送。
       	 		//给调入方 （所有门店账号）  -   新增调拨类型消息Message、不发送极光推送。）
        	BaseShopEntity outShopEntity = baseShopDao.selectById(outShopSeq);
              //2.1调入方门店所有账号添加一条消息
        	for(BaseUserEntity inUser : inShopUserList) {
	        	MessageEntity messageEntity = new MessageEntity();
	        	messageEntity.setTitle("我的申请");
	        	messageEntity.setContent("向" + outShopEntity.getName() + "提出的调货请求");
	        	messageEntity.setUserSeq(inUser.getSeq());
	        	messageEntity.setInputTime(nowDate);
	        	messageEntity.setDel(0);
	        	messageEntity.setType(4);
	        	messageEntity.setCreator(inUserSeq);
	        	messageEntity.setIsRead(0);
	        	messageEntity.setApplicationSeq(applicationEntity.getSeq());
	        	messageEntity.setApplicationType(1);
	        	messageDao.insert(messageEntity);
        	}
              //2.2调出方门店所有账号添加一条调拨类型消息Message、发送极光推送、保存极光推送。
        	List<BaseUserEntity> outShopUserList = getShopAllUsers(outShopSeq);
        			//新增Message
        	for(BaseUserEntity outUser : outShopUserList) {
	        	MessageEntity messageEntity = new MessageEntity();
	        	messageEntity.setTitle(inShopEntity.getName() + " 请求调货");
	        	messageEntity.setContent("");
	        	messageEntity.setUserSeq(outUser.getSeq());
	        	messageEntity.setInputTime(nowDate);
	        	messageEntity.setDel(0);
	        	messageEntity.setType(4);
	        	messageEntity.setCreator(inUserSeq);
	        	messageEntity.setIsRead(0);
	        	messageEntity.setApplicationSeq(applicationEntity.getSeq());
	        	messageEntity.setApplicationType(2);
	        	messageDao.insert(messageEntity);
        	}
        			//发送极光推送、保存极光推送
        	sendJpush(outShopUserList, inShopEntity, inUserSeq);
        }
        
        
        //1.2 根据对应门店取出申请表seq，插入申请详情表
        for(AllocateTransferTransferInApplicationDetailEntity detailEntity : detailList) {
        	Integer outShopSeq = detailEntity.getOutShopSeq();
        	Integer applicationSeq = outShopSeqApplicationSeqMap.get(outShopSeq);
        	
        	detailEntity.setApplicationSeq(applicationSeq);
        	allocateTransferTransferInApplicationDetailDao.insert(detailEntity);
        }
        
        
        
	}


	
	//获取门店所有用户
	private List<BaseUserEntity> getShopAllUsers(Integer shopSeq) {
		Wrapper<BaseUserEntity> wrapper = new EntityWrapper<BaseUserEntity>();
		wrapper.where("Shop_Seq = {0}", shopSeq);
		List<BaseUserEntity> userList = baseUserDao.selectList(wrapper);
		return userList;
	}

	//给调出方门店的所有用户发送推送消息
	private void sendJpush(List<BaseUserEntity> outShopUserList, BaseShopEntity inShopEntity, Integer inUserSeq) {
        //发送手机推送消息
        try {
            List<String> aliasList = new ArrayList<String>();
            for (BaseUserEntity user : outShopUserList) {
                aliasList.add(user.getAccountName());
            }
            jPushUtils.sendPush(aliasList, inShopEntity.getName() + " 请求调货", "-2");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("确认调入后，给调出方门店的所有用户发送推送消息失败" + e.getMessage(), e);
        }
        
        //保存推送消息
        try {
            for (BaseUserEntity user : outShopUserList) {
                pushRecordService.addPushRecord(inUserSeq, user.getSeq(), user.getAccountName(), 1, null, inShopEntity.getName() + " 请求调货");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("确认调入后，给调出方门店的所有用户保存推送消息失败" + e.getMessage(), e);
        }
        
	}


	/**
	 * 获取调货消息
	 */
	@Override
	public List<MessageEntity> getMyTransferMessage(Integer userSeq, Integer type, Page<Map<String,Object>> page) {
		Wrapper<MessageEntity> wrapper = new EntityWrapper<MessageEntity>();
		wrapper.where("(Type = 4 OR Type =5) ANd User_Seq = {0}", userSeq);
		if(type != null && type != 0) {
			wrapper.where("Application_Type = {0}", type);
		}
		List<MessageEntity> messageList = messageDao.selectPage(page,wrapper);
		return messageList;
	}
	
	
	
	
	
	/**
	 * 获取调货消息每个尺码的数量详情
	 */
	@Override
	public List<Map<String, Object>> getApplicationDetailMapList(Integer applicationSeq) {
		Wrapper<AllocateTransferTransferInApplicationDetailEntity> wrapper = new EntityWrapper<AllocateTransferTransferInApplicationDetailEntity>();
		wrapper.setSqlSelect("Size_Seq AS sizeSeq, Num AS num").where("Application_Seq = {0}", applicationSeq);
		List<Map<String, Object>> everySizeNumList = allocateTransferTransferInApplicationDetailDao.selectMaps(wrapper);
		return everySizeNumList;
	}
	
	
	
	

	/**
	 * 处理调货申请（同意、拒绝）
	 */
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public void handleTransferApplication(Integer userSeq, Integer shopSeq, Integer applicationSeq, Integer state,String erpOrderNum,Integer expressCompanySeq, String expressNum, String remark) {
		AllocateTransferTransferInApplicationEntity applicationEntity = new AllocateTransferTransferInApplicationEntity();
		applicationEntity.setSeq(applicationSeq);
		applicationEntity.setState(state);
		applicationEntity.setErpOrderNum(erpOrderNum);
		applicationEntity.setExpressCompanySeq(expressCompanySeq);
		applicationEntity.setExpressNum(expressNum);
		applicationEntity.setRemark(remark);
		applicationEntity.setOutUserSeq(userSeq);
		applicationEntity.setHandleTime(new Date());
		allocateTransferTransferInApplicationDao.updateById(applicationEntity);
		
		
		//同意后，修改之前调出方的虚拟商城调出数量
		Wrapper<AllocateTransferTransferInApplicationDetailEntity> wrapper = new EntityWrapper<AllocateTransferTransferInApplicationDetailEntity>();
		wrapper.where("Application_Seq = {0}", applicationSeq);
		List<AllocateTransferTransferInApplicationDetailEntity> applicationDetailList = allocateTransferTransferInApplicationDetailDao.selectList(wrapper);
		for(AllocateTransferTransferInApplicationDetailEntity applicationDetailEntity : applicationDetailList) {
			AllocateTransferFictitiousMallEntity allocateTransferFictitiousMallEntity = new AllocateTransferFictitiousMallEntity();
			allocateTransferFictitiousMallEntity.setShopSeq(shopSeq);
			allocateTransferFictitiousMallEntity.setShoesSeq(applicationDetailEntity.getShoesSeq());
			allocateTransferFictitiousMallEntity.setSizeSeq(applicationDetailEntity.getSizeSeq());
			allocateTransferFictitiousMallEntity = allocateTransferFictitiousMallDao.selectOne(allocateTransferFictitiousMallEntity);
			if(allocateTransferFictitiousMallEntity != null) {
				allocateTransferFictitiousMallEntity.setTransferOutNum(allocateTransferFictitiousMallEntity.getTransferOutNum() - applicationDetailEntity.getNum());
				allocateTransferFictitiousMallDao.updateById(allocateTransferFictitiousMallEntity);
			}
		}
	}

	@Override
	public Map<String, Object> selectExportShop(Integer shoesSeq) throws Exception {
		Map<String,Object> map = new HashMap<>(10);
		map.put("shoesSeq",shoesSeq);
		Map<String,Object> result = new HashMap<>(10);
		List<Map<String,Object>> shopDetailList = baseMapper.selectExportShop(map);
		Set<String> shopNameList = new LinkedHashSet<>(10);
		for(Map<String,Object> shopDetail : shopDetailList) {
			shopNameList.add(shopDetail.get("shopName").toString());
		}
		List<Map<String,Object>> shopList = new ArrayList<>();
		for(String shopName : shopNameList) {
			Map<String,Object> shop = new HashMap<>();
            List<Map<String,Object>> sizeNum = new ArrayList<>();
			for(Map<String,Object> shopDetail : shopDetailList) {
				if(shopDetail.get("shopName").toString().equals(shopName)) {
					Map<String,Object> size = new HashMap<>();
					size.put("transferOutNum",shopDetail.get("transferOutNum"));
					size.put("size",shopDetail.get("size"));
					sizeNum.add(size);
				}
			}
            shop.put("shopName",shopName);
			shop.put("sizeNum",sizeNum);
			shopList.add(shop);
		}
		Integer shopNum = allocateTransferFictitiousMallDao.selectExportShopNum(map);
		Integer shoesNum = allocateTransferFictitiousMallDao.selectExportShoesNum(map);
		result.put("shopList",shopList);
		result.put("shopNum",shopNum);
		result.put("shoesNum",shoesNum);
		return result;
	}

	@Override
	public void cancelExport(BaseUserEntity baseUserEntity, Integer messageSeq) throws Exception {

	}



	/**
	 * 门店是否已将鞋子放入虚拟商城
	 */
	@Override
	public boolean isTransferOutToFictitiousMall(Integer shopSeq, Integer shoesSeq) {
		Wrapper<AllocateTransferFictitiousMallEntity> wrapper = new EntityWrapper<AllocateTransferFictitiousMallEntity>();
		wrapper.setSqlSelect("TOP 1 * ").where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq);
		List<AllocateTransferFictitiousMallEntity> list = allocateTransferFictitiousMallDao.selectList(wrapper);
		if(list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}


	

	/**
	 * 判断货品是否是滞销自动调出的
	 */
	@Override
	public boolean isAutoTransferOut(Integer shopSeq, Integer shoesSeq) {
		Wrapper<AllocateTransferFictitiousMallEntity> wrapper = new EntityWrapper<AllocateTransferFictitiousMallEntity>();
		wrapper.setSqlSelect("Top 1 * ").where("Shop_Seq = {0} AND Shoes_Seq = {1} AND TransferOutType = 0", shopSeq, shoesSeq);
		List<AllocateTransferFictitiousMallEntity> list = allocateTransferFictitiousMallDao.selectList(wrapper);
		if(list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * 取消调出
	 */
	@Override
	public void cancelTransferOut(Integer shopSeq, Integer shoesSeq) {
		Wrapper<AllocateTransferFictitiousMallEntity> wrapper = new EntityWrapper<AllocateTransferFictitiousMallEntity>();
		wrapper.where("Shop_Seq = {0} AND Shoes_Seq = {1}", shopSeq, shoesSeq);
		allocateTransferFictitiousMallDao.delete(wrapper);
	}
	
	
}
