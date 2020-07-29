package io.nuite.modules.erp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.erp.domain.Storage1Do;
import io.nuite.modules.erp.service.ErpShoesDataSyncService;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.order_platform_app.dao.ShoesDataCopyDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataCopyEntity;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ErpShoesDataSyncServiceImpl implements ErpShoesDataSyncService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Value("${jrd.companySeq}")
    private Integer companySeq;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    
    private static Date defaultStartDate;
    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        defaultStartDate = calendar.getTime();
    }
    
    @Autowired
    private ShoesDataDao shoesDataDao;

	@Autowired
	private ShoesDataCopyDao shoesDataCopyDao;
	
    
    @Autowired
    private ErpShoesDataServiceTransactionAddition erpShoesDataServiceTransactionAddition;

	@Autowired
	private GoodsShoesDao goodsShoesDao;
    
    
    
    /**
     * 同步Erp库存数据
     */
	@Override
	public boolean shoesDataSync(Integer goodsType) {
		Date startTaskTime = new Date();
		logger.error("###伯俊ERP同步库存方法ErpShoesDataSyncServiceImpl shoesDataSync方法，开始执行: " + startTaskTime);
        try {
			//删除copy表中数据
        	shoesDataCopyDao.deleteShoesData(companySeq,goodsType);
            //					店仓名称，         店仓类别ID，				款号，       		    库存数量，			颜色，							尺码， 						创建时间		 修改时间   店仓编码
        	String columns = "C_STORE_ID;NAME,C_STORE_ID;C_STORETYPE_JZ_ID,M_PRODUCT_ID;NAME,QTY,M_ATTRIBUTESETINSTANCE_ID;VALUE1,M_ATTRIBUTESETINSTANCE_ID;VALUE2_CODE,CREATIONDATE,MODIFIEDDATE,C_STORE_ID;CODE";
        	
        	//查询条件：店仓性质-鞋子，店仓类别-分公司+自营店，货品的类是鞋子，货品年份>2017年， 库存数据更新时间大于我们库里的最大时间
//			combine:"and",
//			expr1:{
//				combine:"and",
//				expr1:{
//		        	column:"C_STORE_ID;C_STOREKIND_ID",
//		        	condition:" = 4"
//				 },
//				 expr2:{
//			        column:"C_STORE_ID;C_STORETYPE_JZ_ID",
//			        condition:" IN (1,4)"
//				}
//			 },
//			 expr2:{
//				combine:"and",
//				expr1:{
//					combine:"and",
//					expr1:{
//			        	column:"M_PRODUCT_ID;M_DIM8_ID",
//			        	condition:" = 8"
//					 },
//					 expr2:{
//				        column:"M_PRODUCT_ID;M_DIM2_ID",
//				        condition:" >= 16"
//					}
//				 },
//				 expr2:{
//				    column:"MODIFIEDDATE",
//				    condition:"> 2019/05/20 11:11:11"
//				}
//			}
			Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
			sqlWhereMap.put("combine", "and");

			Map<String, Object> expr1 = new HashMap<String, Object>();
			//更新时间（按照库里最新时间进行拉取）
            expr1.put("column", "MODIFIEDDATE");
			Date updateDate = getShoesDataMaxInputTime(goodsType);
			if(updateDate == null) {
				updateDate = defaultStartDate;
			}
            expr1.put("condition", " > " + dateFormat.format(updateDate));
            sqlWhereMap.put("expr1", expr1);

			Map<String, Object> expr2 = new HashMap<String, Object>();
            expr2.put("combine", "and");

            Map<String, Object> expr2_1 = new HashMap<String, Object>();
            expr2_1.put("combine","or");

            Map<String, Object> expr2_1_1 = new HashMap<String, Object>();
            //总仓（温州一仓，温州调货仓）
            expr2_1_1.put("column", "C_STORE_ID");
            expr2_1_1.put("condition", " IN (2019,2020)");
            expr2_1.put("expr1", expr2_1_1);

            Map<String, Object> expr2_1_2 = new HashMap<String, Object>();
            expr2_1_2.put("combine","and");

            //自营店（上级经销商是吉尔达温州分公司并且门店类别是自营店的）
            Map<String, Object> expr2_1_2_1 = new HashMap<String, Object>();
            expr2_1_2_1.put("column", "C_STORE_ID;C_CUSTOMERUP_ID");
            expr2_1_2_1.put("condition", " = 2852");
            expr2_1_2.put("expr1",expr2_1_2_1);

            Map<String, Object> expr2_1_2_2 = new HashMap<String, Object>();
            expr2_1_2_2.put("column", "C_STORE_ID;C_STORETYPE_JZ_ID");
            expr2_1_2_2.put("condition", " = 1");
            expr2_1_2.put("expr2",expr2_1_2_2);

            expr2_1.put("expr2", expr2_1_2);
            expr2.put("expr1", expr2_1);

            Map<String, Object> expr2_2 = new HashMap<String, Object>();
            expr2_2.put("combine","and");

            Map<String, Object> expr2_2_1 = new HashMap<String, Object>();
            //2017年以后的货品
            expr2_2_1.put("column", "M_PRODUCT_ID;M_DIM2_ID;ATTRIBNAME");
            expr2_2_1.put("condition", " in ('2017年','2018年','2019年','2020年','2021年','2022年','2023年','2024年','2025年','2026年','2027年','2028年','2029年','2030年','2031年','2032年','2033年','2034年','2035年')");
            expr2_2.put("expr1", expr2_2_1);

            Map<String, Object> expr2_2_2 = new HashMap<String, Object>();
            //货品类型8为鞋子,40为皮具
            expr2_2_2.put("column", "M_PRODUCT_ID;M_DIM8_ID");
            
            
            if (goodsType==1){
				expr2_2_2.put("condition", " in (40)");
			}else {
				expr2_2_2.put("condition", " in (8)");
			}
            expr2_2.put("expr2", expr2_2_2);
            expr2.put("expr2",expr2_2);

            
            
			sqlWhereMap.put("expr2", expr2);

			JSONObject jsonObject = BoJunErpPortalUtil.querySql("V_FA_V_STORAGE1", columns, sqlWhereMap, "CREATIONDATE ASC", 0, 99999999);
			if(jsonObject == null) {
				logger.error("BojunErp同步库存，接口请求失败");
				return false;
			}
//			{
//				"message": "完成:0.755 seconds",
//				"id": "112",
//				"count": 32924,
//				"code": 0,
//				"rows": [
//					["娄桥店", 1, "369BU-828506黑", 2, "黑", "38", "2019-05-15 11:20:46.0", "2019-05-15 11:20:46.0"],
//					["娄桥店", 1, "5042GD-832007黑", 4, "黑", "36", "2019-05-15 11:20:41.0", "2019-05-15 11:20:41.0"],
//					["娄桥店", 1, "5042GD-832007黑", -3, "黑", "38", "2019-05-18 13:11:52.0", "2019-05-18 13:11:53.0"],
//					["一仓", 4, "8290GL-92619黑", 15, "黑", "39", "2019-05-16 15:52:01.0", "2019-05-18 13:11:53.0"],
//					["一仓", 4, "8290GL-92619米", 12, "米", "35", "2019-05-16 15:52:01.0", "2019-05-18 13:11:53.0"],
//					["一仓", 4, "8290GL-92619米", 21, "米", "36", "2019-05-16 15:52:00.0", "2019-05-18 13:11:53.0"],
//				]
//			}
			Integer count = jsonObject.getInteger("count");
			if(count == 0) {
				return true;
			}
			
			JSONArray rows = jsonObject.getJSONArray("rows");
			List<Storage1Do> storage1DoList = new ArrayList<Storage1Do>();
			for(int i = 0; i < rows.size(); i++) {
				JSONArray shoesDataArr = rows.getJSONArray(i);
				
				Storage1Do storage1Do = arrayToStorage1Do(shoesDataArr);

				storage1DoList.add(storage1Do);
			}
			
			//新增
			erpShoesDataServiceTransactionAddition.insertOrUpdateShoesData(storage1DoList,goodsType);
			
			Date endTaskTime = new Date();
			logger.error("###伯俊ERP同步库存方法ErpShoesDataSyncServiceImpl shoesDataSync方法，执行完毕: " + endTaskTime + "本次总计耗时:" + (endTaskTime.getTime() - startTaskTime.getTime()));
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
            logger.error("###伯俊ERP同步库存方法ErpShoesDataSyncServiceImpl shoesDataSync方法，出错：" + e.getMessage(), e);
    		return false;
		}
	}



	

	//获取库存表中最大的Inputtime  （用于查询erp需要本次同步的数据，注意每次同步后需修改shoesData表的inputtime）
	private Date getShoesDataMaxInputTime(Integer goodsType) {
		Wrapper<ShoesDataCopyEntity> wrapper = new EntityWrapper<ShoesDataCopyEntity>();
		wrapper.setSqlSelect("stockDate").eq("Company_Seq",companySeq).eq("goods_type",goodsType).orderBy("stockDate", false);
		List<Object> shoesDataEntityList =  shoesDataCopyDao.selectObjs(wrapper);
		if(shoesDataEntityList != null && shoesDataEntityList.size() > 0) {
			return (Date) shoesDataEntityList.get(0);
		}else {
			return null;
		}
	}
    
    
	
	//将数组转换为ProductDo对象
	private Storage1Do arrayToStorage1Do(JSONArray arr) {
		Storage1Do storage1Do = new Storage1Do();
	    storage1Do.setStoreName(arr.getString(0));
	    storage1Do.setStoreTypeId(arr.getInteger(1));
	    storage1Do.setProductName(arr.getString(2));
	    storage1Do.setqTY(arr.getInteger(3));
	    storage1Do.setColorName(arr.getString(4));
	    storage1Do.setSizeName(arr.getString(5));
	    storage1Do.setCreationDate(arr.getDate(6));
	    storage1Do.setModifiedDate(arr.getDate(7));
		storage1Do.setStoreId(arr.getString(8));
		return storage1Do;
	}
	
	
}
