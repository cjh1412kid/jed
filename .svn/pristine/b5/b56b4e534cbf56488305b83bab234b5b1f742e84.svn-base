package io.nuite.modules.system.service.impl.order_platform;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.dao.AllocateTransferTransferInApplicationDao;
import io.nuite.modules.order_platform_app.dao.ExpressCompanyDao;
import io.nuite.modules.order_platform_app.dao.MessageDao;
import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationEntity;
import io.nuite.modules.order_platform_app.entity.ExpressCompanyEntity;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import io.nuite.modules.order_platform_app.utils.JPushUtils;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.system.dao.order_platform.AllocateTransferFactoryDao;
import io.nuite.modules.system.entity.order_platform.AllocateTransferFactoryEntity;
import io.nuite.modules.system.service.order_platform.AllocateTransferFactoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AllocateTransferFactoryServiceImpl  extends ServiceImpl<AllocateTransferFactoryDao, AllocateTransferFactoryEntity> implements AllocateTransferFactoryService {

	@Autowired
	private AllocateTransferFactoryDao allocateTransferFactoryDao;
	
	@Autowired
	private GoodsShoesDao goodsShoesDao;
	
	@Autowired
	private BaseShopDao baseShopDao;
	
	@Autowired
	private BaseUserDao baseUserDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private AllocateTransferTransferInApplicationDao allocateTransferTransferInApplicationDao;
	
    @Autowired
    private JPushUtils jPushUtils;
    
    @Autowired
    private ExpressCompanyDao expressCompanyDao;
    
	
	@Override
	public List<Map<String, Object>> getDate(String date) {
		List<Map<String, Object>> list=allocateTransferFactoryDao.getDate(date);
				for (Map<String, Object> map : list) {
					map.put("rank",1);
					map.put("name",map.get("allocateDate"));
				}
		return list;
	}

	@Override
	public List<Map<String, Object>> getShoes(String date) {
		List<Map<String, Object>> list=allocateTransferFactoryDao.getShoes(date);
		for (Map<String, Object> map : list) {
			Integer shoesSeq=(Integer) map.get("shoesSeq");
			GoodsShoesEntity goodsShoesEntity=goodsShoesDao.selectById(shoesSeq);
			map.put("shoeName", goodsShoesEntity.getGoodID());
			map.put("date",date);
			map.put("rank",2);
			map.put("parentName",date);
			map.put("name",goodsShoesEntity.getGoodID());
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getTime(String date, Integer shoesSeq) {
		GoodsShoesEntity goodsShoesEntity=goodsShoesDao.selectById(shoesSeq);
		List<Map<String, Object>> list=allocateTransferFactoryDao.getTime(date, shoesSeq);
		for (Map<String, Object> map : list) {
			map.put("date", date);
			map.put("shoesSeq", shoesSeq);
			map.put("rank", 3);
			map.put("parentName",goodsShoesEntity.getGoodID());
			map.put("name",map.get("allocateTime"));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getRecords(String date, Integer shoesSeq, String time,Page page) {
		//获取所有调入调出的序号
		
		
		List<Map<String, Object>> list=allocateTransferFactoryDao.getShops(date, shoesSeq, time,page);
		for (Map<String, Object> map : list) {
			Integer inShopSeq=(Integer) map.get("inShopSeq");
			Integer outShopSeq=(Integer) map.get("outShopSeq");
			String dateTime=date+" "+time.substring(0,5);
			AllocateTransferTransferInApplicationEntity allocateTransferTransferInApplication=allocateTransferTransferInApplicationDao.getallocateTransferTransferInApplication(inShopSeq,outShopSeq,shoesSeq,dateTime);
			if(allocateTransferTransferInApplication.getExpressCompanySeq()!=null) {
			ExpressCompanyEntity expressCompanyEntity=expressCompanyDao.selectById(allocateTransferTransferInApplication.getExpressCompanySeq());
			allocateTransferTransferInApplication.setExpressName(expressCompanyEntity.getName());
			}
			//根据调入和调出门店，日期，鞋子序号，时间 查询记录
			List<Map<String, Object>> records=allocateTransferFactoryDao.getRecords(date, shoesSeq, time, inShopSeq, outShopSeq);
			map.put("records", records);
			String state="待处理";
			if(allocateTransferTransferInApplication!=null) {
				if( allocateTransferTransferInApplication.getState()==1) {
					state="已同意";
				}else if( allocateTransferTransferInApplication.getState()==2){
					state="已拒绝";
				}
			}
			map.put("state",state);
			map.put("erpOrderNum",allocateTransferTransferInApplication.getErpOrderNum());
			map.put("expressName",allocateTransferTransferInApplication.getExpressName());
			map.put("expressNum",allocateTransferTransferInApplication.getExpressNum());
			String remark= allocateTransferTransferInApplication.getRemark();
			if(remark!=null) {
			String[] remarks=remark.split(",");
			if(remarks.length==1) {
				remark=remarks[0]+"双";
			}else if(remarks.length==2) {
				remark=remarks[0]+"双";
				if(StringUtils.equals("1", remarks[1])) {
					remark+=",状态：无库存";
				}else if(StringUtils.equals("2", remarks[1])) {
					remark+=",状态：已销售";
				}else if(StringUtils.equals("3", remarks[1])) {
					remark+=",状态：串码";
				}
			}
			}
			map.put("remark",remark);
			
		}
		page.setRecords(list);
		return page;
	}

	@Override
	public void deleteRecords(String date, Integer shoesSeq, String time) {
		allocateTransferFactoryDao.deleteRecords(date, shoesSeq, time);
	}

	@Override
	public AllocateTransferFactoryEntity getAllocateTransLast(Integer shoesSeq) {
		  Wrapper<AllocateTransferFactoryEntity> wrapper = new EntityWrapper<AllocateTransferFactoryEntity>();
		  wrapper.where("Shoes_Seq = {0}", shoesSeq);
		  wrapper.orderBy("InputTime", false);
		  List<AllocateTransferFactoryEntity> AllocateTransferFactorys= allocateTransferFactoryDao.selectList(wrapper);
		  if(AllocateTransferFactorys!=null&&AllocateTransferFactorys.size()>0) {
			  return AllocateTransferFactorys.get(0);
		  }else {
			  return null;  
		  }
	}

	@Override
	public void pushRecords(String date, Integer shoesSeq, String time,Integer inUserSeq,Page page,Integer companySeq) {
		//根据date,time,shoesSeq 查询调入调出记录
		String dateTime=date+" "+time.substring(0,5);
	List<AllocateTransferTransferInApplicationEntity> list=allocateTransferTransferInApplicationDao.getList(dateTime, shoesSeq);
		
	Date nowDate = new Date();
		for (AllocateTransferTransferInApplicationEntity allocateTransferTransferInApplicationEntity : list) {
			Integer inShopSeq=allocateTransferTransferInApplicationEntity.getInShopSeq();
			Integer outShopSeq=allocateTransferTransferInApplicationEntity.getOutShopSeq();
			BaseShopEntity outShopEntity = baseShopDao.selectById(outShopSeq);
			BaseShopEntity inShopEntity = baseShopDao.selectById(inShopSeq);
			   List<BaseUserEntity> inShopUserList = getShopAllUsers(inShopSeq,companySeq);
			
			   String content1="调入通知——";
				   if(outShopEntity==null) {
					   content1+="从总店调入货品";
				   }else {
					   content1+="从"+outShopEntity.getName()+"调入货品";
				   }
			   //2.1调入方门店所有账号添加一条消息
	        	for(BaseUserEntity inUser : inShopUserList) {
		        	MessageEntity messageEntity = new MessageEntity();
		        	if(inShopEntity==null) {
		        		messageEntity.setTitle( "总店调入货品");
		        	}else {
		        		messageEntity.setTitle(inShopEntity.getName() + "调入货品");
		        	}
		        	messageEntity.setContent(content1);
		        	messageEntity.setUserSeq(inUser.getSeq());
		        	messageEntity.setDel(0);
		        	messageEntity.setType(4);
		        	messageEntity.setApplicationSeq(allocateTransferTransferInApplicationEntity.getSeq());
		        	messageEntity.setApplicationType(1);
		        	MessageEntity messageEntity1 = messageDao.selectOne(messageEntity);
		        	if(messageEntity1==null) {
		        		messageEntity.setIsRead(0);
		        		messageEntity.setInputTime(nowDate);
		        		messageEntity.setCreator(inUserSeq);
		        		messageDao.insert(messageEntity);
		        		 List<String> aliasList = new ArrayList<String>();
		                    aliasList.add(inUser.getAccountName());
		                    jPushUtils.sendPush(aliasList, messageEntity.getContent(), "-2");
		        	}else {
		        		messageEntity1.setIsRead(0);
		        		messageDao.updateById(messageEntity1);
		        		 List<String> aliasList = new ArrayList<String>();
		                    aliasList.add(inUser.getAccountName());
		                    jPushUtils.sendPush(aliasList, messageEntity.getContent(), "-2");
		        	}
		        	
	        	}
	        	   String content="调出通知——";
				   if(inShopEntity==null) {
					   content+="货品调至总店";
				   }else {
					   content+="货品调至"+inShopEntity.getName(); 
				   }
	              //2.2调出方门店所有账号添加一条消息
	        	List<BaseUserEntity> outShopUserList = getShopAllUsers(outShopSeq,companySeq);
	        	for(BaseUserEntity outUser : outShopUserList) {
		        	MessageEntity messageEntity = new MessageEntity();
		        	if(outShopEntity==null) {
		        		messageEntity.setTitle("总店调出货品");
		        	}else {
		        		messageEntity.setTitle(outShopEntity.getName() + " 调出货品");
		        	}
		        	messageEntity.setContent(content);
		        	messageEntity.setUserSeq(outUser.getSeq());
		        	messageEntity.setDel(0);
		        	messageEntity.setType(4);
		        	messageEntity.setApplicationSeq(allocateTransferTransferInApplicationEntity.getSeq());
		        	messageEntity.setApplicationType(2);
		        	MessageEntity messageEntity1 = messageDao.selectOne(messageEntity);
		        	if(messageEntity1==null) {
		        		messageEntity.setIsRead(0);
		        		messageEntity.setInputTime(nowDate);
		        		messageEntity.setCreator(inUserSeq);
		        		messageDao.insert(messageEntity);
		        		 List<String> aliasList = new ArrayList<String>();
		                    aliasList.add(outUser.getAccountName());
		                    jPushUtils.sendPush(aliasList, messageEntity.getContent(), "-2");
		        	}else {
		        		messageEntity1.setIsRead(0);
		        		messageDao.updateById(messageEntity1);
		        		 List<String> aliasList = new ArrayList<String>();
		                    aliasList.add(outUser.getAccountName());
		                    jPushUtils.sendPush(aliasList, messageEntity.getContent(), "-2");
		        	}
	        	} 
		}
		
	}
	private List<BaseUserEntity> getShopAllUsers(Integer shopSeq,Integer companySeq) {
		Wrapper<BaseUserEntity> wrapper = new EntityWrapper<BaseUserEntity>();
		if(shopSeq==0) {
			wrapper.where("Shop_Seq is null AND Company_Seq = {1}", shopSeq,companySeq);
		}else {
			wrapper.where("Shop_Seq = {0} AND Company_Seq = {1}", shopSeq,companySeq);
		}
		List<BaseUserEntity> userList = baseUserDao.selectList(wrapper);
		return userList;
	}


	@Override
	public List<Map<String, Object>> getApplicationDetailMapList(Integer inShopSeq, Integer outShopSeq, Integer shoesSeq, Date inputTime) throws Exception {
        Wrapper<AllocateTransferFactoryEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("Shoes_Seq",shoesSeq).eq("InShop_Seq",inShopSeq).eq("OutShop_Seq",outShopSeq).eq("InputTime",inputTime);
        wrapper.setSqlSelect("Size_Seq AS sizeSeq,Num AS num");
	    return baseMapper.selectMaps(wrapper);
	}


	@Override
	public Page getAllShoes(String startDate, String endDate,String goodId, Page page,Integer companySeq) {
		List<Map<String, Object>> list=allocateTransferFactoryDao.getAllShoes(startDate, endDate, goodId,page,companySeq);
		for (Map<String, Object> map : list) {
			Integer shoesSeq=(Integer) map.get("shoesSeq");
			GoodsShoesEntity goodsShoesEntity=goodsShoesDao.selectById(shoesSeq);
			map.put("shoeName", goodsShoesEntity.getGoodID());
			//根据日期，货号，时间查询是否已推送
			String date=map.get("allocateDate").toString();
			String time=map.get("allocateTime").toString();
			String dateTime=date+" "+time.substring(0,5);
			List<AllocateTransferTransferInApplicationEntity> list2=allocateTransferTransferInApplicationDao.getList(dateTime, shoesSeq);
			Integer count=0;
			Integer stateCount=0;
			for (AllocateTransferTransferInApplicationEntity allocateTransferTransferInApplicationEntity : list2) {
				Integer seq=allocateTransferTransferInApplicationEntity.getSeq();
				//查询是否有消息推送
				MessageEntity messageEntity=new MessageEntity();
				messageEntity.setApplicationSeq(seq);
				messageEntity.setApplicationType(2);
				Wrapper<MessageEntity> wrapper=new EntityWrapper<MessageEntity>();
				wrapper.where("Application_Seq = {0} and  Application_Type =2", seq);
				List<MessageEntity> messageEntities=messageDao.selectList(wrapper);
				if(messageEntities.size()>0) {
					messageEntity=messageEntities.get(0);
				}else {
					messageEntity=null;
				}
				if(messageEntity!=null) {
					count++;
				}
				if(allocateTransferTransferInApplicationEntity.getState()==1||allocateTransferTransferInApplicationEntity.getState()==2) {
					stateCount++;
				}
			}
			if(count!=0) {
				map.put("message", "已推送");
			}else {
				map.put("message", "");
			}
			if(stateCount==0) {
				map.put("state", "0");//未处理
			}else if(stateCount!=list2.size()) {
				map.put("state", "1");//部分处理
			}else {
				map.put("state", "2");//全处理
			}
			
		}
		page.setRecords(list);
		return page;
	}

	@Override
	public AllocateTransferTransferInApplicationEntity getallocateTransferTransferInApplication(Integer inShopSeq,
			Integer outShopSeq, Integer shoesSeq, Date InputTime) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		return allocateTransferTransferInApplicationDao.getallocateTransferTransferInApplication(inShopSeq,outShopSeq,shoesSeq,sdf.format(InputTime));
	}

	@Override
	public void insertAllocateTransferTransferInApplicationEntity(
			AllocateTransferTransferInApplicationEntity allocateTransferTransferInApplicationEntity) {
		allocateTransferTransferInApplicationDao.insert(allocateTransferTransferInApplicationEntity);
	}

	@Override
	public AllocateTransferFactoryEntity getNearOne() {
		Wrapper<AllocateTransferFactoryEntity> wrapper=new EntityWrapper<AllocateTransferFactoryEntity>();
		wrapper.orderBy("InputTime", false);
		List<AllocateTransferFactoryEntity> allocateTransferFactoryEntities=allocateTransferFactoryDao.selectList(wrapper);
		if(allocateTransferFactoryEntities.size()>0) {
			return allocateTransferFactoryEntities.get(0);
		}
 		return null;
	}

}
