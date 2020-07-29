package io.nuite.modules.erp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.erp.domain.ProductDo;
import io.nuite.modules.erp.service.ErpGoodsSyncService;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ErpGoodsSyncServiceImpl implements ErpGoodsSyncService {
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
    private GoodsShoesDao goodsShoesDao;
	
    
    @Autowired
    private ErpGoodsSyncServiceTransactionAddition erpGoodsSyncServiceTransactionAddition;
    
    
    
    
    /**
     * 同步Erp货品数据
     */
	@Override
	public boolean goodsSync(Integer goodsType) {
		Date startTaskTime = new Date();
		logger.error("###伯俊ERP同步货品方法ErpGoodsSyncServiceImpl goodsSync方法，开始执行: " + startTaskTime);
        try {
        	//根据货品类别，将的Del置为0
			goodsShoesDao.updateGoodDel(companySeq,goodsType);
            //根据货品类别查询我们的货品表，最大的InputTime，做为上一次同步时间
            Date goodsShoesMaxInputTime = getGoodsShoesMaxInputTime(goodsType);
            if (goodsShoesMaxInputTime == null){
            	goodsShoesMaxInputTime = defaultStartDate;
            }
            String maxInputTime = dateFormat.format(goodsShoesMaxInputTime);
            
        	String columns = "NAME,COLORSALIAS,M_DIM2_ID;ATTRIBNAME,M_DIM3_ID;ATTRIBNAME,M_DIM8_ID;ATTRIBNAME,M_DIM4_ID;ATTRIBNAME,"
        			+ "M_DIM6_ID;ATTRIBNAME,M_DIM5_ID;ATTRIBNAME,PRECOST,RETPRECOST,PACKCHARGE,SALEPRICE,PERCOST,PRICELIST,DLPRICE,PRICELIST2,"
        			+"CREATIONDATE,MODIFIEDDATE";
        	
        	//查询条件：类是鞋子、时间2017年之后、更新时间大于我们库里的最大时间
//			combine:"and",
//			expr1:{
//				combine:"and",
//				expr1:{
//		        	column:"M_DIM8_ID",
//		        	condition:" = 8"
//				 },
//				 expr2:{
//			        column:"M_DIM2_ID",
//			        condition:" >= 16"
//				}
//			 },
//			 expr2:{
//		        column:"MODIFIEDDATE",
//		        condition:"> 2019/05/20 11:11:11"
//			}
			Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
			sqlWhereMap.put("combine", "and");
			
			Map<String, Object> expr1 = new HashMap<String, Object>();
			expr1.put("combine", "and");
			//鞋子
			Map<String, Object> expr1_1 = new HashMap<String, Object>();
			expr1_1.put("column", "M_DIM8_ID");
			
			
			
			//鞋子对用的类在博钧为8，皮具为40;
			//expr1_1.put("condition", " in (8,40) ");
			if (goodsType==1){
				expr1_1.put("condition", " in (40) ");
			}else {
				expr1_1.put("condition", " in (8) ");
			}
			expr1.put("expr1", expr1_1);
			
			
			
			
			//2017年及以后
			Map<String, Object> expr1_2 = new HashMap<String, Object>();
			expr1_2.put("column", "M_DIM2_ID");
			expr1_2.put("condition", ">=16");
			expr1.put("expr2", expr1_2);
			
			sqlWhereMap.put("expr1", expr1);
			//修改时间
			Map<String, Object> expr2 = new HashMap<String, Object>();
			expr2.put("combine", "and");

			Map<String, Object> expr2_1 = new HashMap<String, Object>();
			expr2_1.put("column", "MODIFIEDDATE");
			expr2_1.put("condition", ">" + maxInputTime);
			expr2.put("expr1",expr2_1);

			Map<String, Object> expr2_2 = new HashMap<String, Object>();
			expr2_2.put("column", "ISACTIVE");
			expr2_2.put("condition", " IN ('Y')");
			expr2.put("expr2",expr2_2);


			sqlWhereMap.put("expr2", expr2);

			JSONObject jsonObject = BoJunErpPortalUtil.querySql("M_PRODUCT", columns, sqlWhereMap, "CREATIONDATE ASC", 0, 99999999);
			if(jsonObject == null) {
				logger.error("BojunErp同步货品，接口请求失败");
				return false;
			}
//			{
//				"message": "完成:0.0050 seconds",
//				"id": "112",
//				"count": 6540,
//				"code": 0,
//				"rows": [
//			["135GD-911802T黑","黑","2019年","春季","鞋子","女鞋","女单",null,27,27,0,32,27,138,0,null,"2019-04-20 10:37:57.0","2019-05-11 13:30:01.0"]
//			["135GD-911802T桔","桔","2019年","春季","鞋子","女鞋","女单",null,27,27,0,32,27,138,0,null,"2019-04-20 10:37:57.0","2019-05-11 13:30:01.0"]
//			["135GD-911802T米白","米白","2019年","春季","鞋子","女鞋","女单",null,27,27,0,32,27,138,0,null,"2019-04-20 10:37:57.0","2019-05-11 13:30:01.0"]
//			["135GD-911808T白","白","2019年","春季","鞋子","女鞋","女单",null,25,25,0,30,25,128,0,null,"2019-04-20 10:37:57.0","2019-05-11 13:31:16.0"]
//			["135GD-911808T黑","黑","2019年","春季","鞋子","女鞋","女单",null,25,25,0,30,25,128,0,null,"2019-04-20 10:37:57.0","2019-05-11 13:30:02.0"]
//			}
			Integer count = jsonObject.getInteger("count");
			if(count == 0) {
				return true;
			}
			
			JSONArray rows = jsonObject.getJSONArray("rows");
			List<ProductDo> productDoList = new ArrayList<ProductDo>();
			for(int i = 0; i < rows.size(); i++) {
				JSONArray goodsArr = rows.getJSONArray(i);
				
				ProductDo productDo = arrayToProductDo(goodsArr);
				
				productDoList.add(productDo);
			}
			
			//新增或修改鞋子
			erpGoodsSyncServiceTransactionAddition.insertOrUpdateGoods(productDoList);

			Date endTaskTime = new Date();
			logger.error("###伯俊ERP同步货品方法ErpGoodsSyncServiceImpl goodsSync方法，执行完毕: " + endTaskTime + "本次总计耗时:" + (endTaskTime.getTime() - startTaskTime.getTime()));

			return true;
		} catch (Exception e) {
			e.printStackTrace();
            logger.error("###伯俊ERP同步货品方法ErpGoodsSyncServiceImpl goodsSync方法，出错：" + e.getMessage(), e);
    		return false;
		}
	}
	
	
	//获取鞋子表中最大的Inputtime  （用于查询erp需要本次同步的数据，这里前提是同步后需要改goodsshoes表的inputtime，即使是修改的shoesInfo表）
	private Date getGoodsShoesMaxInputTime(Integer goodsType) throws ParseException {
		Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<GoodsShoesEntity>();
		wrapper.setSqlSelect("MAX (InputTime)").eq("Company_Seq",companySeq).eq("goods_type",goodsType);
		return (Date) goodsShoesDao.selectObjs(wrapper).get(0);
	
	}
	
	
	
	
	
	//将数组转换为ProductDo对象
	private ProductDo arrayToProductDo(JSONArray arr) {
		ProductDo productDo = new ProductDo();
		productDo.setName(arr.getString(0));
	    productDo.setColorsAlias(arr.getString(1));
	    productDo.setDim2Attribname(arr.getString(2));
	    productDo.setDim3Attribname(arr.getString(3));
	    productDo.setDim8Attribname(arr.getString(4));
	    productDo.setDim4Attribname(arr.getString(5));
	    productDo.setDim6Attribname(arr.getString(6));
	    productDo.setDim5Attribname(arr.getString(7));
	    productDo.setPreCost(arr.getBigDecimal(8));
	    productDo.setRetprecost(arr.getBigDecimal(9));
	    productDo.setPackcharge(arr.getBigDecimal(10));
	    productDo.setSaleprice(arr.getBigDecimal(11));
	    productDo.setPerCost(arr.getBigDecimal(12));
	    productDo.setPriceList(arr.getBigDecimal(13));
	    productDo.setDlPrice(arr.getBigDecimal(14));
	    productDo.setPriceList2(arr.getBigDecimal(15));
	    productDo.setCreationDate(arr.getDate(16));
	    productDo.setModifiedDate(arr.getDate(17));
		return productDo;
	}
	

}
