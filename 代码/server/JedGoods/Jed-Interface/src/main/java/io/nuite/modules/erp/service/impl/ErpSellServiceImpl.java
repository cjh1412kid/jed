package io.nuite.modules.erp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.erp.service.ErpSellService;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.erp.utils.ErpDataVo;
import io.nuite.modules.order_platform_app.dao.ShoesDataDailyDetailCopyDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDailyDetailDao;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailCopyEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.GoodsColorDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description: 拉取ERP销售（存入进出库记录表YHSR_OP_ShoesData_Daily_Detail，先放入Copy结尾的临时表再一次性insert into到正式表（完成后删除Copy表）正式表防止App端卡顿）Service实现类
 * @author: jxj
 * @create: 2019-05-21 14:08
 */
@Service
public class ErpSellServiceImpl implements ErpSellService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShoesDataDailyDetailDao shoesDataDailyDetailDao;
    @Autowired
    private ShoesDataDailyDetailCopyDao shoesDataDailyDetailCopyDao;
    @Autowired
    private GoodsShoesDao goodsShoesDao;
    @Autowired
    private BaseShopDao baseShopDao;
    @Autowired
    private GoodsColorDao goodsColorDao;
    @Autowired
    private GoodsSizeDao goodsSizeDao;
    @Autowired
    private ShoesDataDao shoesDataDao;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean getErpSell(Integer goodsType) throws Exception {
        logger.error("销售拉取开始");
        Long startTime = System.currentTimeMillis();
        //获取公司下所有货品
        
        Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("Company_Seq", companySeq).eq("goods_type",goodsType);
        List<GoodsShoesEntity> goodsShoesEntities = goodsShoesDao.selectList(wrapper);
        List<ShoesDataDailyDetailCopyEntity> shoesDataDailyDetailCopyEntities = new ArrayList<>(10);
        
        //获取公司下所有门店
        Wrapper<BaseShopEntity> shopEntityWrapper = new EntityWrapper<>();
        shopEntityWrapper.eq("Company_Seq", companySeq);
        List<BaseShopEntity> baseShopEntities = baseShopDao.selectList(shopEntityWrapper);
        
        //获取公司下所有颜色
        Wrapper<GoodsColorEntity> colorEntityWrapper = new EntityWrapper<>();
        colorEntityWrapper.eq("Company_Seq", companySeq);
        List<GoodsColorEntity> goodsColorEntities = goodsColorDao.selectList(colorEntityWrapper);
        //获取公司下所有尺码
        Wrapper<GoodsSizeEntity> sizeEntityWrapper = new EntityWrapper<>();
        sizeEntityWrapper.eq("Company_Seq", companySeq);
        List<GoodsSizeEntity> goodsSizeEntities = goodsSizeDao.selectList(sizeEntityWrapper);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        
        //获取公司下所有库存
        Wrapper<ShoesDataEntity> shoesDataEntityWrapper = new EntityWrapper<>();
        shoesDataEntityWrapper.eq("Company_Seq",companySeq).eq("goods_type",goodsType);
        List<ShoesDataEntity> shoesDataEntities = shoesDataDao.selectList(shoesDataEntityWrapper);
        
        //总仓发门店
        //获取最新一条总仓发门店数据的ERP时间
        Wrapper<ShoesDataDailyDetailEntity> shopInWrapper = new EntityWrapper<>();
        shopInWrapper.setSqlSelect("Input_ERP_Time").eq("Type", 1).eq("Company_Seq",companySeq).eq("goods_type",goodsType).orderBy("Input_ERP_Time", false);
        List<ShoesDataDailyDetailEntity> shopInList = shoesDataDailyDetailDao.selectList(shopInWrapper);
        //如果数据不为空,拉取ERP数据的时间大于该时间
        Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
        Map<String, Object> expr1;
        Map<String, Object> expr2 = new HashMap<String, Object>();
        
        
        sqlWhereMap.put("combine", "and");
       
        if (shopInList.size() > 0) {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition", " > " + simpleDateFormat.format(shopInList.get(0).getInputERPTime()));
        //如果为空拉取所有数据
        } else {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition", " > " + simpleDateFormat.format(defaultStartDate));
        }
        expr2.put("column", "M_PRODUCT_ID;M_DIM8_ID");
        if (goodsType==1){
            expr2.put("condition", " in (40)");
        }else {
            expr2.put("condition", " in (8)");
        }
        sqlWhereMap.put("expr1", expr1);
        sqlWhereMap.put("expr2",expr2);
        //0 单据编号
        String columns = "M_SALE_ID;DOCNO," +
                //1 收货店仓
                "M_SALE_ID;C_DEST_ID;NAME," +
                //2 发货店仓
                "M_SALE_ID;C_STORE_ID;NAME," +
                //3 货号
                "M_PRODUCT_ID;NAME," +
                //4 颜色
                "M_PRODUCT_ID;M_COLOR_ID;NAME," +
                //5 尺码
                "M_ATTRIBUTESETINSTANCE_ID;VALUE2_ID;VALUE," +
                //6 入库数量
                "QTY," +
                //7 实际到货日期
                "M_SALE_ID;BILLDATE," +
                //8 销售价
                "PRICEACTUAL," +
                //9 创建时间
                "CREATIONDATE," +
                //10 发货仓类别(分公司)
                "M_SALE_ID;C_STORE_ID;C_STORETYPE_JZ_ID;NAME," +
                //11 发货仓性质(鞋子)
                "M_SALE_ID;C_STORE_ID;C_STOREKIND_ID;NAME," +
                //12 收货仓类别(自营店)
                "M_SALE_ID;C_DEST_ID;C_STORETYPE_JZ_ID;NAME," +
                //13 收货仓性质(鞋子)
                "M_SALE_ID;C_DEST_ID;C_STOREKIND_ID;NAME," +
                //14 标准价
                "PRICELIST," +
                //15 是否可用
                "M_SALE_ID;ISACTIVE," +
                //16 是否可用
                "ISACTIVE," +
                //17 上级经销商
                "M_SALE_ID;C_DEST_ID;C_CUSTOMERUP_ID;CODE," +
                //18 所属经销商
                "M_SALE_ID;C_STORE_ID;C_CUSTOMER_ID;CODE," +
                //19 收货店仓编号
                "M_SALE_ID;C_DEST_ID;CODE";
        JSONObject jsonObject = BoJunErpPortalUtil.querySql("M_SALEITEM", columns, sqlWhereMap, "ID ASC", 0, 999999999);
        ErpDataVo erpDataVo = JSONObject.toJavaObject(jsonObject, ErpDataVo.class);
        String[][] erpData = {};
        if (erpDataVo != null) {
            erpData = erpDataVo.getRows();
            if (erpData != null && erpData.length > 0) {
                for (String[] row : erpData) {
                    for (int i = 0; i < goodsShoesEntities.size(); i++) {
                        //判断该条数据货号是否存在
                        if (goodsShoesEntities.get(i).getGoodID().equals(row[3])) {
                            //判断该条数据是否符合要求
                            if (row[10] != null && row[18] != null && row[12] != null && row[17] != null && row[15] != null && row[16] != null && "分公司".equals(row[10]) && "JEDWZ".equals(row[18]) && "自营店".equals(row[12]) && "JEDWZ".equals(row[17]) && "是".equals(row[15]) && "是".equals(row[16])) {
                                ShoesDataDailyDetailCopyEntity shoesDataDailyDetailCopyEntity = new ShoesDataDailyDetailCopyEntity();
                                shoesDataDailyDetailCopyEntity.setCompanySeq(companySeq);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
                                if (row[9] != null) {
                                    shoesDataDailyDetailCopyEntity.setInputERPTime(sdf.parse(row[9]));
                                } else {
                                    continue;
                                }
                                shoesDataDailyDetailCopyEntity.setType(1);
                                shoesDataDailyDetailCopyEntity.setShopName(row[1]);
                                shoesDataDailyDetailCopyEntity.setShoesId(row[3]);
                                if (row[6] != null) {
                                    shoesDataDailyDetailCopyEntity.setCount(Integer.parseInt(row[6]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setCount(0);
                                }
                                if (row[8] != null) {
                                    shoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(row[8]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(0));
                                }
                                if (row[7] != null && !"null".equals(row[7])) {
                                    shoesDataDailyDetailCopyEntity.setUpdateTime(sd.parse(row[7]));
                                } else {
                                    continue;
                                }
                                shoesDataDailyDetailCopyEntity.setErpOrderNum(row[0]);
                                if (row[14] != null) {
                                    shoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(row[14]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(0));
                                }
                                if (row[3] != null) {
                                    for (int j = 0; j < goodsShoesEntities.size(); j++) {
                                        if (goodsShoesEntities.get(j).getGoodID().equals(row[3])) {
                                            shoesDataDailyDetailCopyEntity.setShoesSeq(goodsShoesEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[19] != null) {
                                    for (int j = 0; j < baseShopEntities.size(); j++) {
                                        if (baseShopEntities.get(j).getId().equals(row[19])) {
                                            shoesDataDailyDetailCopyEntity.setShopSeq(baseShopEntities.get(j).getSeq());
                                        }else if ("T84".equals(row[19])) {
                                            shoesDataDailyDetailCopyEntity.setShopSeq(94);
                                        }
                                    }
                                }
                                if (row[5] != null) {
                                    for (int j = 0; j < goodsSizeEntities.size(); j++) {
                                        if (goodsSizeEntities.get(j).getSizeName().equals(row[5])) {
                                            shoesDataDailyDetailCopyEntity.setSize(goodsSizeEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[4] != null) {
                                    for (int j = 0; j < goodsColorEntities.size(); j++) {
                                        if (goodsColorEntities.get(j).getName().equals(row[4])) {
                                            shoesDataDailyDetailCopyEntity.setColor(goodsColorEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                shoesDataDailyDetailCopyEntities.add(shoesDataDailyDetailCopyEntity);
                            }
                        }
                    }
                }
            }
        }

        //门店退总仓
        //获取最新一条门店退总仓数据的ERP时间
        Wrapper<ShoesDataDailyDetailEntity> shopOutWrapper = new EntityWrapper<>();
        shopOutWrapper.setSqlSelect("Input_ERP_Time").eq("Type", 2).eq("Company_Seq",companySeq).eq("goods_type",goodsType).orderBy("Input_ERP_Time", false);
        List<ShoesDataDailyDetailEntity> shopOutList = shoesDataDailyDetailDao.selectList(shopOutWrapper);
        //如果数据不为空,拉取ERP数据的时间大于该时间
        //sqlWhereMap.remove("expr1");
        if (shopOutList.size() > 0) {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition", " > " + simpleDateFormat.format(shopOutList.get(0).getInputERPTime()));
            //如果为空拉取所有数据
        } else {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition", " > "+simpleDateFormat.format(defaultStartDate));
        }
        sqlWhereMap.put("expr1",expr1);
        
        //0 单据编号
        columns = "M_RET_SALE_ID;DOCNO," +
                //1 收货店仓
                "M_RET_SALE_ID;C_STORE_ID;NAME," +
                //2 发货店仓
                "M_RET_SALE_ID;C_ORIG_ID;NAME," +
                //3 货号
                "M_PRODUCT_ID;NAME," +
                //4 颜色
                "M_PRODUCT_ID;M_COLOR_ID;NAME," +
                //5 尺码
                "M_ATTRIBUTESETINSTANCE_ID;VALUE2_ID;VALUE," +
                //6 入库数量
                "QTY," +
                //7 出库日期
                "M_RET_SALE_ID;BILLDATE," +
                //8 退货价
                "PRICEACTUAL," +
                //9 创建时间
                "CREATIONDATE," +
                //10 发货仓类别(自营店)
                "M_RET_SALE_ID;C_ORIG_ID;C_STORETYPE_JZ_ID;NAME," +
                //11 发货仓性质(鞋子)
                "M_RET_SALE_ID;C_ORIG_ID;C_STOREKIND_ID;NAME," +
                //12 收货仓类别(分公司)
                "M_RET_SALE_ID;C_STORE_ID;C_STORETYPE_JZ_ID;NAME," +
                //13 收货仓性质(鞋子)
                "M_RET_SALE_ID;C_STORE_ID;C_STOREKIND_ID;NAME," +
                //14 标准价
                "PRICELIST," +
                //15 是否可用
                "M_RET_SALE_ID;ISACTIVE," +
                //16 是否可用
                "ISACTIVE," +
                //17 上级经销商
                "M_RET_SALE_ID;C_ORIG_ID;C_CUSTOMERUP_ID;CODE," +
                //18 所属经销商
                "M_RET_SALE_ID;C_STORE_ID;C_CUSTOMER_ID;CODE," +
                //19 发货店仓编号
                "M_RET_SALE_ID;C_ORIG_ID;CODE";
        jsonObject = BoJunErpPortalUtil.querySql("M_RET_SALEITEM", columns, sqlWhereMap, "ID ASC", 0, 999999999);
        erpDataVo = JSONObject.toJavaObject(jsonObject, ErpDataVo.class);
        if (erpDataVo != null) {
            erpData = erpDataVo.getRows();
            if (erpData != null && erpData.length > 0) {
                for (String[] row : erpData) {
                    for (int i = 0; i < goodsShoesEntities.size(); i++) {
                        //判断该条数据货号是否存在
                        if (goodsShoesEntities.get(i).getGoodID().equals(row[3])) {
                            //判断数据是否符合要求
                            if (row[10] != null && row[17] != null && row[12] != null && row[18] != null && row[15] != null && row[16] != null && "自营店".equals(row[10]) && "JEDWZ".equals(row[17]) && "分公司".equals(row[12]) && "JEDWZ".equals(row[18]) && "是".equals(row[15]) && "是".equals(row[16])) {
                                ShoesDataDailyDetailCopyEntity shoesDataDailyDetailCopyEntity = new ShoesDataDailyDetailCopyEntity();
                                shoesDataDailyDetailCopyEntity.setCompanySeq(companySeq);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
                                if (row[9] != null) {
                                    shoesDataDailyDetailCopyEntity.setInputERPTime(sdf.parse(row[9]));
                                } else {
                                    continue;
                                }
                                shoesDataDailyDetailCopyEntity.setType(2);
                                shoesDataDailyDetailCopyEntity.setShopName(row[2]);
                                shoesDataDailyDetailCopyEntity.setShoesId(row[3]);
                                if (row[6] != null) {
                                    shoesDataDailyDetailCopyEntity.setCount(Integer.parseInt(row[6]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setCount(0);
                                }
                                if (row[8] != null && !"null".equals(row[8])) {
                                    shoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(row[8]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(0));
                                }
                                if (row[7] != null && !"null".equals(row[7])) {
                                    shoesDataDailyDetailCopyEntity.setUpdateTime(sd.parse(row[7]));
                                } else {
                                    continue;
                                }
                                shoesDataDailyDetailCopyEntity.setErpOrderNum(row[0]);
                                if (row[14] != null) {
                                    shoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(row[14]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(0));
                                }
                                if (row[3] != null) {
                                    for (int j = 0; j < goodsShoesEntities.size(); j++) {
                                        if (goodsShoesEntities.get(j).getGoodID().equals(row[3])) {
                                            shoesDataDailyDetailCopyEntity.setShoesSeq(goodsShoesEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[19] != null) {
                                    for (int j = 0; j < baseShopEntities.size(); j++) {
                                        if (baseShopEntities.get(j).getId().equals(row[19])) {
                                            shoesDataDailyDetailCopyEntity.setShopSeq(baseShopEntities.get(j).getSeq());
                                        }else if ("T84".equals(row[19])) {
                                            shoesDataDailyDetailCopyEntity.setShopSeq(94);
                                        }
                                    
                                    }
                                }
                                if (row[5] != null) {
                                    for (int j = 0; j < goodsSizeEntities.size(); j++) {
                                        if (goodsSizeEntities.get(j).getSizeName().equals(row[5])) {
                                            shoesDataDailyDetailCopyEntity.setSize(goodsSizeEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[4] != null) {
                                    for (int j = 0; j < goodsColorEntities.size(); j++) {
                                        if (goodsColorEntities.get(j).getName().equals(row[4])) {
                                            shoesDataDailyDetailCopyEntity.setColor(goodsColorEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                shoesDataDailyDetailCopyEntities.add(shoesDataDailyDetailCopyEntity);
                            }
                        }
                    }
                }
            }
        }

        //总仓进货
        //获取最新一条总仓进货数据的ERP数据
        Wrapper<ShoesDataDailyDetailEntity> stockInWrapper = new EntityWrapper<>();
        stockInWrapper.setSqlSelect("Input_ERP_Time").eq("Type", 0).eq("Company_Seq",companySeq).eq("goods_type",goodsType).orderBy("Input_ERP_Time", false);
        List<ShoesDataDailyDetailEntity> stockInList = shoesDataDailyDetailDao.selectList(stockInWrapper);
        //如果数据不为空,拉取ERP数据的时间大于该时间
        if (stockInList.size() > 0) {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition", " > " + simpleDateFormat.format(stockInList.get(0).getInputERPTime()));
            //如果为空拉取所有数据
        } else {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition"," > " +simpleDateFormat.format(defaultStartDate));
        }
        sqlWhereMap.put("expr1",expr1);
        
        //0 单据编号
        columns = "M_PURCHASE_ID;DOCNO," +
                //1 采购店仓
                "M_PURCHASE_ID;C_STORE_ID;NAME," +
                //2 供应商
                "M_PURCHASE_ID;C_SUPPLIER_ID;NAME," +
                //3 货号
                "M_PRODUCT_ID;NAME," +
                //4 颜色
                "M_PRODUCT_ID;M_COLOR_ID;NAME," +
                //5 尺码
                "M_ATTRIBUTESETINSTANCE_ID;VALUE2_ID;VALUE," +
                //6 入库数量
                "QTY," +
                //7 实际到货日期
                "M_PURCHASE_ID;BILLDATE," +
                //8 采购价
                "PRICEACTUAL," +
                //9 创建时间
                "CREATIONDATE," +
                //10 收货仓类别(营销中心)
                "M_PURCHASE_ID;C_STORE_ID;C_STORETYPE_JZ_ID;NAME," +
                //11 收货仓性质(鞋子)
                "M_PURCHASE_ID;C_STORE_ID;C_STOREKIND_ID;NAME," +
                //12 标准价
                "PRICELIST," +
                //13 是否可用
                "M_PURCHASE_ID;ISACTIVE," +
                //14 是否可用
                "ISACTIVE," +
                //15 所属经销商
                "M_PURCHASE_ID;C_STORE_ID;C_CUSTOMER_ID;CODE";
        jsonObject = BoJunErpPortalUtil.querySql("M_PURCHASEITEM", columns, sqlWhereMap, "ID ASC", 0, 999999999);
        erpDataVo = JSONObject.toJavaObject(jsonObject, ErpDataVo.class);
        if (erpDataVo != null) {
            erpData = erpDataVo.getRows();
            if (erpData != null && erpData.length > 0) {
                for (String[] row : erpData) {
                    for (int i = 0; i < goodsShoesEntities.size(); i++) {
                        //判断该条数据货号是否存在
                        if (goodsShoesEntities.get(i).getGoodID().equals(row[3])) {
                            //判断该条数据是否符合要求
                            if (row[10] != null && row[15] != null && row[13] != null && row[14] != null && "营销中心".equals(row[10]) && "99999".equals(row[15]) && "是".equals(row[13]) && "是".equals(row[14])) {
                                ShoesDataDailyDetailCopyEntity shoesDataDailyDetailCopyEntity = new ShoesDataDailyDetailCopyEntity();
                                shoesDataDailyDetailCopyEntity.setCompanySeq(companySeq);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
                                if (row[9] != null) {
                                    shoesDataDailyDetailCopyEntity.setInputERPTime(sdf.parse(row[9]));
                                } else {
                                    continue;
                                }
                                shoesDataDailyDetailCopyEntity.setType(0);
                                shoesDataDailyDetailCopyEntity.setShopName(row[1]);
                                shoesDataDailyDetailCopyEntity.setShoesId(row[3]);
                                if (row[6] != null) {
                                    shoesDataDailyDetailCopyEntity.setCount(Integer.parseInt(row[6]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setCount(0);
                                }
                                if (row[8] != null) {
                                    shoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(row[8]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(0));
                                }
                                if (row[7] != null && !"null".equals(row[7])) {
                                    shoesDataDailyDetailCopyEntity.setUpdateTime(sd.parse(row[7]));
                                } else {
                                    continue;
                                }
                                shoesDataDailyDetailCopyEntity.setErpOrderNum(row[0]);
                                if (row[12] != null) {
                                    shoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(row[12]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(0));
                                }
                                if (row[3] != null) {
                                    for (int j = 0; j < goodsShoesEntities.size(); j++) {
                                        if (goodsShoesEntities.get(j).getGoodID().equals(row[3])) {
                                            shoesDataDailyDetailCopyEntity.setShoesSeq(goodsShoesEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                shoesDataDailyDetailCopyEntity.setShopSeq(0);
                                if (row[5] != null) {
                                    for (int j = 0; j < goodsSizeEntities.size(); j++) {
                                        if (goodsSizeEntities.get(j).getSizeName().equals(row[5])) {
                                            shoesDataDailyDetailCopyEntity.setSize(goodsSizeEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[4] != null) {
                                    for (int j = 0; j < goodsColorEntities.size(); j++) {
                                        if (goodsColorEntities.get(j).getName().equals(row[4])) {
                                            shoesDataDailyDetailCopyEntity.setColor(goodsColorEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                shoesDataDailyDetailCopyEntities.add(shoesDataDailyDetailCopyEntity);
                            }
                        }
                    }
                }
            }
        }

        //总仓退货
        //获取最新一条总仓退货数据的ERP时间
        Wrapper<ShoesDataDailyDetailEntity> stockOutWrapper = new EntityWrapper<>();
        stockOutWrapper.setSqlSelect("Input_ERP_Time").eq("Type", 4).eq("Company_Seq",companySeq).eq("goods_type",goodsType).orderBy("Input_ERP_Time", false);
        List<ShoesDataDailyDetailEntity> stockOutList = shoesDataDailyDetailDao.selectList(stockOutWrapper);
        //如果数据不为空,拉取ERP数据的时间大于该时间
        if (stockOutList.size() > 0) {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition", " > " + simpleDateFormat.format(stockOutList.get(0).getInputERPTime()));
            //如果为空拉取所有数据
        } else {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition"," > "+ simpleDateFormat.format(defaultStartDate));
        }
        sqlWhereMap.put("expr1",expr1);
        
        //0 单据编号
        columns = "M_RET_PUR_ID;DOCNO," +
                //1 退货店仓
                "M_RET_PUR_ID;C_STORE_ID;NAME," +
                //2 供应商
                "M_RET_PUR_ID;C_SUPPLIER_ID;NAME," +
                //3 货号
                "M_PRODUCT_ID;NAME," +
                //4 颜色
                "M_PRODUCT_ID;M_COLOR_ID;NAME," +
                //5 尺码
                "M_ATTRIBUTESETINSTANCE_ID;VALUE2_ID;VALUE," +
                //6 出库数量
                "QTY," +
                //7 出库日期
                "M_RET_PUR_ID;BILLDATE," +
                //8 退货价
                "PRICEACTUAL," +
                //9 创建时间
                "CREATIONDATE," +
                //10 发货仓类别(营销中心)
                "M_RET_PUR_ID;C_STORE_ID;C_STORETYPE_JZ_ID;NAME," +
                //11 发货仓性质(鞋子)
                "M_RET_PUR_ID;C_STORE_ID;C_STOREKIND_ID;NAME," +
                //12 标准价
                "PRICELIST," +
                //13 是否可用
                "M_RET_PUR_ID;ISACTIVE," +
                //14 是否可用
                "ISACTIVE," +
                //15 所属经销商
                "M_RET_PUR_ID;C_STORE_ID;C_CUSTOMER_ID;CODE";
        jsonObject = BoJunErpPortalUtil.querySql("M_RET_PURITEM", columns, sqlWhereMap, "ID ASC", 0, 999999999);
        erpDataVo = JSONObject.toJavaObject(jsonObject, ErpDataVo.class);
        if (erpDataVo != null) {
            erpData = erpDataVo.getRows();
            if (erpData != null && erpData.length > 0) {
                for (String[] row : erpData) {
                    for (int i = 0; i < goodsShoesEntities.size(); i++) {
                        if (goodsShoesEntities.get(i).getGoodID().equals(row[3])) {
                            if (row[10] != null && row[15] != null && row[13] != null && row[14] != null && "营销中心".equals(row[10]) && "99999".equals(row[15]) && "是".equals(row[13]) && "是".equals(row[14])) {
                                ShoesDataDailyDetailCopyEntity shoesDataDailyDetailCopyEntity = new ShoesDataDailyDetailCopyEntity();
                                shoesDataDailyDetailCopyEntity.setCompanySeq(companySeq);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
                                if (row[9] != null) {
                                    shoesDataDailyDetailCopyEntity.setInputERPTime(sdf.parse(row[9]));
                                } else {
                                    continue;
                                }
                                shoesDataDailyDetailCopyEntity.setType(4);
                                shoesDataDailyDetailCopyEntity.setShopName(row[1]);
                                shoesDataDailyDetailCopyEntity.setShoesId(row[3]);
                                if (row[6] != null) {
                                    shoesDataDailyDetailCopyEntity.setCount(Integer.parseInt(row[6]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setCount(0);
                                }
                                if (row[8] != null) {
                                    shoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(row[8]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(0));
                                }
                                if (row[7] != null && !"null".equals(row[7])) {
                                    shoesDataDailyDetailCopyEntity.setUpdateTime(sd.parse(row[7]));
                                } else {
                                    continue;
                                }
                                shoesDataDailyDetailCopyEntity.setErpOrderNum(row[0]);
                                if (row[12] != null) {
                                    shoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(row[12]));
                                } else {
                                    shoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(0));
                                }
                                if (row[3] != null) {
                                    for (int j = 0; j < goodsShoesEntities.size(); j++) {
                                        if (goodsShoesEntities.get(j).getGoodID().equals(row[3])) {
                                            shoesDataDailyDetailCopyEntity.setShoesSeq(goodsShoesEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                shoesDataDailyDetailCopyEntity.setShopSeq(0);
                                if (row[5] != null) {
                                    for (int j = 0; j < goodsSizeEntities.size(); j++) {
                                        if (goodsSizeEntities.get(j).getSizeName().equals(row[5])) {
                                            shoesDataDailyDetailCopyEntity.setSize(goodsSizeEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[4] != null) {
                                    for (int j = 0; j < goodsColorEntities.size(); j++) {
                                        if (goodsColorEntities.get(j).getName().equals(row[4])) {
                                            shoesDataDailyDetailCopyEntity.setColor(goodsColorEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                shoesDataDailyDetailCopyEntities.add(shoesDataDailyDetailCopyEntity);
                            }
                        }
                    }
                }
            }
        }

        //门店调拨
        //获取最新一条门店调拨数据的ERP时间
        Wrapper<ShoesDataDailyDetailEntity> shopToShopWrapper = new EntityWrapper<>();
        shopToShopWrapper.setSqlSelect("Input_ERP_Time").eq("Type", 5).eq("Company_Seq",companySeq).eq("goods_type",goodsType).orderBy("Input_ERP_Time", false);
        List<ShoesDataDailyDetailEntity> shopToShopList = shoesDataDailyDetailDao.selectList(shopToShopWrapper);
        //如果数据不为空,拉取ERP数据的时间大于该时间
        if (shopToShopList.size() > 0) {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition", " > " + simpleDateFormat.format(shopToShopList.get(0).getInputERPTime()));
            //如果为空拉取所有数据
        } else {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition"," > "+ simpleDateFormat.format(defaultStartDate));
        }
        sqlWhereMap.put("expr1",expr1);
        
        //0 单据编号
        columns = "M_TRANSFER_ID;DOCNO," +
                //1 发货店仓
                "M_TRANSFER_ID;C_ORIG_ID;NAME," +
                //2 收货店仓
                "M_TRANSFER_ID;C_DEST_ID;NAME," +
                //3 货号
                "M_PRODUCT_ID;NAME," +
                //4 颜色
                "M_PRODUCT_ID;M_COLOR_ID;NAME," +
                //5 尺码
                "M_ATTRIBUTESETINSTANCE_ID;VALUE2_ID;VALUE," +
                //6 入库数量
                "QTY," +
                //7 出库日期
                "M_TRANSFER_ID;BILLDATE," +
                //8 零售价
                "PRICELIST," +
                //9 创建时间
                "CREATIONDATE," +
                //10 发货仓类别(自营店)
                "M_TRANSFER_ID;C_ORIG_ID;C_STORETYPE_JZ_ID;NAME," +
                //11 发货仓性质(鞋子)
                "M_TRANSFER_ID;C_ORIG_ID;C_STOREKIND_ID;NAME," +
                //12 收货仓类别(自营店)
                "M_TRANSFER_ID;C_DEST_ID;C_STORETYPE_JZ_ID;NAME," +
                //13 收货仓性质(鞋子)
                "M_TRANSFER_ID;C_DEST_ID;C_STOREKIND_ID;NAME," +
                //14 标准价
                "PRICELIST," +
                //15 实际到货日期
                "M_TRANSFER_ID;REALDATE," +
                //16 是否可用
                "M_TRANSFER_ID;ISACTIVE," +
                //17 是否可用
                "ISACTIVE," +
                //18 上级经销商
                "M_TRANSFER_ID;C_ORIG_ID;C_CUSTOMERUP_ID;CODE," +
                //19 上级经销商
                "M_TRANSFER_ID;C_DEST_ID;C_CUSTOMERUP_ID;CODE," +
                //20 发货店仓编码
                "M_TRANSFER_ID;C_ORIG_ID;CODE," +
                //21 收货店仓编码
                "M_TRANSFER_ID;C_DEST_ID;CODE";
        jsonObject = BoJunErpPortalUtil.querySql("M_TRANSFERITEM", columns, sqlWhereMap, "ID ASC", 0, 999999999);
        erpDataVo = JSONObject.toJavaObject(jsonObject, ErpDataVo.class);
        if (erpDataVo != null) {
            erpData = erpDataVo.getRows();
            if (erpData != null && erpData.length > 0) {
                for (String[] row : erpData) {
                    for (int i = 0; i < goodsShoesEntities.size(); i++) {
                        //判断该条数据货号是否存在
                        if (goodsShoesEntities.get(i).getGoodID().equals(row[3])) {
                            //判断该条数据是否符合要求
                            if (row[10] != null && row[18] != null && row[12] != null && row[19] != null && row[16] != null && row[17] != null && "自营店".equals(row[10]) && "JEDWZ".equals(row[18]) && "自营店".equals(row[12]) && "JEDWZ".equals(row[19]) && "是".equals(row[16]) && "是".equals(row[17])) {
                                ShoesDataDailyDetailCopyEntity inShoesDataDailyDetailCopyEntity = new ShoesDataDailyDetailCopyEntity();
                                inShoesDataDailyDetailCopyEntity.setCompanySeq(companySeq);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
                                if (row[9] != null) {
                                    inShoesDataDailyDetailCopyEntity.setInputERPTime(sdf.parse(row[9]));
                                } else {
                                    continue;
                                }
                                inShoesDataDailyDetailCopyEntity.setType(5);
                                inShoesDataDailyDetailCopyEntity.setShopName(row[2]);
                                inShoesDataDailyDetailCopyEntity.setShoesId(row[3]);
                                if (row[6] != null) {
                                    inShoesDataDailyDetailCopyEntity.setCount(Integer.parseInt(row[6]));
                                } else {
                                    inShoesDataDailyDetailCopyEntity.setCount(0);
                                }
                                if (row[8] != null) {
                                    inShoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(row[8]));
                                } else {
                                    inShoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(0));
                                }
                                if (row[7] != null && !"null".equals(row[7])) {
                                    inShoesDataDailyDetailCopyEntity.setUpdateTime(sd.parse(row[7]));
                                } else {
                                    continue;
                                }
                                inShoesDataDailyDetailCopyEntity.setErpOrderNum(row[0]);
                                if (row[14] != null) {
                                    inShoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(row[14]));
                                } else {
                                    inShoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(0));
                                }
                                if (row[3] != null) {
                                    for (int j = 0; j < goodsShoesEntities.size(); j++) {
                                        if (goodsShoesEntities.get(j).getGoodID().equals(row[3])) {
                                            inShoesDataDailyDetailCopyEntity.setShoesSeq(goodsShoesEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[21] != null) {
                                    for (int j = 0; j < baseShopEntities.size(); j++) {
                                        if (baseShopEntities.get(j).getId().equals(row[21])) {
                                            inShoesDataDailyDetailCopyEntity.setShopSeq(baseShopEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[5] != null) {
                                    for (int j = 0; j < goodsSizeEntities.size(); j++) {
                                        if (goodsSizeEntities.get(j).getSizeName().equals(row[5])) {
                                            inShoesDataDailyDetailCopyEntity.setSize(goodsSizeEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[4] != null) {
                                    for (int j = 0; j < goodsColorEntities.size(); j++) {
                                        if (goodsColorEntities.get(j).getName().equals(row[4])) {
                                            inShoesDataDailyDetailCopyEntity.setColor(goodsColorEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                shoesDataDailyDetailCopyEntities.add(inShoesDataDailyDetailCopyEntity);
                                ShoesDataDailyDetailCopyEntity outShoesDataDailyDetailCopyEntity = new ShoesDataDailyDetailCopyEntity();
                                outShoesDataDailyDetailCopyEntity.setCompanySeq(companySeq);
                                if (row[9] != null) {
                                    outShoesDataDailyDetailCopyEntity.setInputERPTime(sdf.parse(row[9]));
                                } else {
                                    continue;
                                }
                                outShoesDataDailyDetailCopyEntity.setType(5);
                                outShoesDataDailyDetailCopyEntity.setShopName(row[1]);
                                outShoesDataDailyDetailCopyEntity.setShoesId(row[3]);
                                if (row[6] != null) {
                                    outShoesDataDailyDetailCopyEntity.setCount(0 - Integer.parseInt(row[6]));
                                } else {
                                    outShoesDataDailyDetailCopyEntity.setCount(0);
                                }
                                if (row[8] != null) {
                                    outShoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(row[8]));
                                } else {
                                    outShoesDataDailyDetailCopyEntity.setPrice(new BigDecimal(0));
                                }
                                if (row[7] != null && !"null".equals(row[7])) {
                                    outShoesDataDailyDetailCopyEntity.setUpdateTime(sd.parse(row[7]));
                                } else {
                                    continue;
                                }
                                outShoesDataDailyDetailCopyEntity.setErpOrderNum(row[0]);
                                if (row[14] != null) {
                                    outShoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(row[14]));
                                } else {
                                    outShoesDataDailyDetailCopyEntity.setTagPrice(new BigDecimal(0));
                                }
                                if (row[3] != null) {
                                    for (int j = 0; j < goodsShoesEntities.size(); j++) {
                                        if (goodsShoesEntities.get(j).getGoodID().equals(row[3])) {
                                            outShoesDataDailyDetailCopyEntity.setShoesSeq(goodsShoesEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[20] != null) {
                                    for (int j = 0; j < baseShopEntities.size(); j++) {
                                        if (baseShopEntities.get(j).getId().equals(row[20])) {
                                            outShoesDataDailyDetailCopyEntity.setShopSeq(baseShopEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[5] != null) {
                                    for (int j = 0; j < goodsSizeEntities.size(); j++) {
                                        if (goodsSizeEntities.get(j).getSizeName().equals(row[5])) {
                                            outShoesDataDailyDetailCopyEntity.setSize(goodsSizeEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                if (row[4] != null) {
                                    for (int j = 0; j < goodsColorEntities.size(); j++) {
                                        if (goodsColorEntities.get(j).getName().equals(row[4])) {
                                            outShoesDataDailyDetailCopyEntity.setColor(goodsColorEntities.get(j).getSeq());
                                        }
                                    }
                                }
                                shoesDataDailyDetailCopyEntities.add(outShoesDataDailyDetailCopyEntity);
                            }
                        }
                    }
                }
            }
        }
        //循环新增销售数据
        if (shoesDataDailyDetailCopyEntities.size() > 0) {
            
            //sqlserver超过2100个参数就会报错
            //这里进行100条一次批量插入
            List<ShoesDataDailyDetailCopyEntity> shoesDataDailyDetailCopyEntityList = new ArrayList<>(100);
            for (int i = 0; i < shoesDataDailyDetailCopyEntities.size(); i++) {
                if (null==shoesDataDailyDetailCopyEntities.get(i).getShopSeq()){
                    shoesDataDailyDetailCopyEntities.remove((shoesDataDailyDetailCopyEntities.get(i)));
                    continue;
                }
                //goodsType 0：鞋子，1皮具
                 shoesDataDailyDetailCopyEntities.get(i).setGoodsType(goodsType);
                shoesDataDailyDetailCopyEntityList.add(shoesDataDailyDetailCopyEntities.get(i));
                if (shoesDataDailyDetailCopyEntityList.size() == 100) {
                    shoesDataDailyDetailCopyDao.insertShoesDataDailyDetailCopy(shoesDataDailyDetailCopyEntityList);
                    shoesDataDailyDetailCopyEntityList.clear();
                } else if (i == shoesDataDailyDetailCopyEntities.size() - 1) {
                    shoesDataDailyDetailCopyDao.insertShoesDataDailyDetailCopy(shoesDataDailyDetailCopyEntityList);
                }
            }
        }
        //从副表同步到主表
        shoesDataDailyDetailDao.selectSaleShoesDetailCopyIntoSaleShoesDetail();
        //删除副表数据
        shoesDataDailyDetailCopyDao.deleteShoesDataDailyDetailCopy();
        //获取所有的进出库记录,循环计算货品的总进货量
        Wrapper<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntityWrapper = new EntityWrapper<>();
        shoesDataDailyDetailEntityWrapper.setSqlSelect("Shop_Seq AS shopSeq,Shoes_Seq AS shoesSeq,Type AS type,Color AS color,Size AS size,Count AS count").eq("Company_Seq",companySeq).eq("goods_type",goodsType);
        List<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntityList = shoesDataDailyDetailDao.selectList(shoesDataDailyDetailEntityWrapper);
        for (ShoesDataDailyDetailEntity shoesDataDailyDetailEntity : shoesDataDailyDetailEntityList) {
            for (ShoesDataEntity shoesDataEntity : shoesDataEntities) {
                if (shoesDataDailyDetailEntity.getShoesSeq().equals(shoesDataEntity.getShoesSeq()) &&
                        shoesDataDailyDetailEntity.getShopSeq().equals(shoesDataEntity.getShopSeq()) &&
                        shoesDataDailyDetailEntity.getSize().equals(shoesDataEntity.getSizeSeq()) &&
                        shoesDataDailyDetailEntity.getColor().equals(shoesDataEntity.getColorSeq())) {
                    if(shoesDataDailyDetailEntity.getType() == 0 || shoesDataDailyDetailEntity.getType() == 1 ||shoesDataDailyDetailEntity.getType() == 5) {
                        shoesDataEntity.setNum(shoesDataEntity.getNum() + shoesDataDailyDetailEntity.getCount());
                    }else if(shoesDataDailyDetailEntity.getType() == 2 || shoesDataDailyDetailEntity.getType() == 4) {
                        shoesDataEntity.setNum(shoesDataEntity.getNum() - shoesDataDailyDetailEntity.getCount());
                    }

                }
            }
        }
        logger.info("========总库存计算完毕=========");
        //循环更新库存中的总进货量
        if (shoesDataEntities.size() > 0) {
            //sqlserver超过2100个参数就会报错
            //这里进行100条一次批量更新
            List<ShoesDataEntity> shoesDataList = new ArrayList<>(100);
            for (int i = 0; i < shoesDataEntities.size(); i++) {
                shoesDataList.add(shoesDataEntities.get(i));
                if (shoesDataList.size() == 100) {
                    shoesDataDao.updateShoesDataNum(shoesDataList);
                    shoesDataList.clear();
                } else if (i == shoesDataEntities.size() - 1) {
                    shoesDataDao.updateShoesDataNum(shoesDataList);
                }
            }
        }
        logger.error("销售拉取结束,耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
        return true;
    }
    
    private static Date defaultStartDate;
    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        defaultStartDate = calendar.getTime();
    }
    
    
    
    
    
    
    
    
    
}
