package io.nuite.modules.erp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.base.Joiner;
import io.nuite.modules.erp.service.ErpSaleService;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.erp.utils.ErpDataVo;
import io.nuite.modules.order_platform_app.dao.SaleShoesDetailCopyDao;
import io.nuite.modules.order_platform_app.dao.SaleShoesDetailDao;
import io.nuite.modules.order_platform_app.dao.SalesDao;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailCopyEntity;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import io.nuite.modules.order_platform_app.entity.SalesEntity;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.BaseAreaService;
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
 * @description: 拉取ERP零售（存入销售表YHSR_OP_SaleShoesDetail，先放入Copy结尾的临时表再一次性insert into到正式表（完成后删除Copy表）防止App端卡顿）Service实现类
 * @author: jxj
 * @create: 2019-05-22 16:00
 */
@Service
public class ErpSaleServiceImpl implements ErpSaleService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SaleShoesDetailDao saleShoesDetailDao;
    @Autowired
    private SaleShoesDetailCopyDao saleShoesDetailCopyDao;
    @Autowired
    private GoodsShoesDao goodsShoesDao;
    @Autowired
    private BaseShopDao baseShopDao;
    @Autowired
    private BaseAreaService baseAreaService;
    @Autowired
    private SalesDao salesDao;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean getErpSale(Integer goodsType) throws Exception {
        logger.error("零售拉取开始");
        Long startTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Wrapper<SaleShoesDetailEntity> saleShoesDetailEntityWrapper = new EntityWrapper<>();
        saleShoesDetailEntityWrapper.eq("Company_Seq",companySeq).eq("AreaSeq",baseAreaService.getAreaSeqByCompanySeq(companySeq)).eq("goods_type",goodsType).orderBy("InputTime",false);
        List<SaleShoesDetailEntity> saleShoesDetailList = saleShoesDetailDao.selectList(saleShoesDetailEntityWrapper);
    
        Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
        Map<String, Object> expr1;
        Map<String, Object> expr2 = new HashMap<String, Object>();
        sqlWhereMap.put("combine", "and");
        if (saleShoesDetailList.size() > 0) {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition", " > " + simpleDateFormat.format(saleShoesDetailList.get(0).getInputTime()));
            //如果为空拉取所有数据
        } else {
            expr1= new HashMap<String, Object>();
            expr1.put("column", "CREATIONDATE");
            expr1.put("condition", ">"+simpleDateFormat.format(defaultStartDate));
        }
        expr2.put("column", "M_PRODUCT_ID;M_DIM8_ID");
        if (goodsType==1){
            expr2.put("condition", " in (40) ");
        }else {
            expr2.put("condition", " in (8) ");
        }
        sqlWhereMap.put("expr1", expr1);
        sqlWhereMap.put("expr2",expr2);
        
        //0 单据编号
        String columns = "M_RETAIL_ID;DOCNO," +
                //1 零售店仓
                "M_RETAIL_ID;C_STORE_ID;NAME," +
                //2 货号
                "M_PRODUCT_ID;NAME," +
                //3 成交数量
                "QTY," +
                //4 交易日期
                "M_RETAIL_ID;BILLDATE," +
                //5 成交价
                "PRICEACTUAL," +
                //6 发货仓类别(自营店)
                "M_RETAIL_ID;C_STORE_ID;C_STORETYPE_JZ_ID;NAME," +
                //7 发货仓性质(鞋子)
                "M_RETAIL_ID;C_STORE_ID;C_STOREKIND_ID;NAME," +
                //8 创建时间
                "CREATIONDATE," +
                //9 标准价
                "PRICELIST," +
                //10 是否可用
                "M_RETAIL_ID;ISACTIVE," +
                //11 是否可用
                "ISACTIVE," +
                //12 上级经销商
                "M_RETAIL_ID;C_STORE_ID;C_CUSTOMERUP_ID;CODE," +
                //13 零售店仓
                "M_RETAIL_ID;C_STORE_ID;CODE," +
                //14 店员身份证号
                "SALESREP_ID;ID_CARD," +
                //15 店员身份证号
                "SALESREP_ID;NAME,"+
                //16 店员编号
                "SALESREP_ID;NO";
            
        JSONObject jsonObject = BoJunErpPortalUtil.querySql("M_RETAILITEM",columns,sqlWhereMap,"ID ASC",0,999999999);
        ErpDataVo erpDataVo = JSONObject.toJavaObject(jsonObject,ErpDataVo.class);
        if(erpDataVo != null) {
            String[][] erpData = erpDataVo.getRows();
            //获取公司下所有货品
            Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("Company_Seq",companySeq).eq("goods_type",goodsType);
            List<GoodsShoesEntity> goodsShoesEntities = goodsShoesDao.selectList(wrapper);
            //零售列表
            List<SaleShoesDetailCopyEntity> saleShoesDetailEntities = new ArrayList<>(10);
            //获取公司下所有门店
            Wrapper<BaseShopEntity> shopEntityWrapper = new EntityWrapper<>();
            shopEntityWrapper.eq("Company_Seq",companySeq);
            List<BaseShopEntity> baseShopEntities = baseShopDao.selectList(shopEntityWrapper);
            if(erpData != null && erpData.length > 0) {
                for(String[] row : erpData) {
                    //循环货品列表判断货品名称
                    for(int i = 0;i < goodsShoesEntities.size();i++) {
                        //判断当前数据的货品名称是否存在
                        if(row[2].equals(goodsShoesEntities.get(i).getGoodID())) {
                            //判断数据是否符合要求
                            if(row[6] != null && row[12] != null && row[10] != null && row[11] != null && "自营店".equals(row[6]) && "JEDWZ".equals(row[12]) && "是".equals(row[10]) && "是".equals(row[11])) {
                                SaleShoesDetailCopyEntity saleShoesDetailCopyEntity = new SaleShoesDetailCopyEntity();
                                saleShoesDetailCopyEntity.setCompanySeq(companySeq);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
                                if(row[3] != null) {
                                    saleShoesDetailCopyEntity.setSaleCount(Integer.parseInt(row[3]));
                                }else {
                                    saleShoesDetailCopyEntity.setSaleCount(0);
                                }
                                if(row[8] != null) {
                                    saleShoesDetailCopyEntity.setInputTime(sdf.parse(row[8]));
                                }else {
                                    continue;
                                }
                                if(row[5] != null) {
                                    saleShoesDetailCopyEntity.setRealPrice(new BigDecimal(row[5]));
                                }else {
                                    saleShoesDetailCopyEntity.setRealPrice(new BigDecimal(0));
                                }
                                saleShoesDetailCopyEntity.setSaleType(3);
                                if(row[4] != null && !"null".equals(row[4])) {
                                    saleShoesDetailCopyEntity.setSaleDate(sd.parse(row[4]));
                                }else {
                                    continue;
                                }
                                saleShoesDetailCopyEntity.setShoeID(row[2]);
                                saleShoesDetailCopyEntity.setDel(0);
                                saleShoesDetailCopyEntity.setErpOrderNum(row[0]);
                                saleShoesDetailCopyEntity.setAreaSeq(baseAreaService.getAreaSeqByCompanySeq(companySeq));
                                saleShoesDetailCopyEntity.setShoeSeq(goodsShoesEntities.get(i).getSeq());
                                if(row[9] != null) {
                                    saleShoesDetailCopyEntity.setTagPrice(new BigDecimal(row[9]));
                                }else {
                                    saleShoesDetailCopyEntity.setTagPrice(new BigDecimal(0));
                                }
                                //循环门店判断门店名称
                                for(int j = 0;j < baseShopEntities.size();j++) {
                                    //判断门店是否存在
                                    if(row[13].equals(baseShopEntities.get(j).getId())) {
                                        saleShoesDetailCopyEntity.setShopSeq(baseShopEntities.get(j).getSeq());
                                        //有早前的数据,导购员不存在,如果导购员存在,则插入导购员序号
                                        if(row[15] != null) {
                                            //根据门店和导购员身份证号查询导购
                                            Wrapper<SalesEntity> salesEntityWrapper = new EntityWrapper<>();
                                            salesEntityWrapper.eq("ShopSeq",baseShopEntities.get(j).getSeq());
                                            //logger.info(baseShopEntities.get(j).getSeq().toString()+"=============导购店============");
                                            if(row[14] == null) {
                                                salesEntityWrapper.isNull("ID_Card_No");
                                            }else {
                                                salesEntityWrapper.eq("ID_Card_No",row[14]);
                                            }
                                            if(row[14] == null) {
                                                salesEntityWrapper.isNull("Number");
                                            }else {
                                                salesEntityWrapper.eq("Number",row[16]);
                                            }
                                            List<SalesEntity> salesEntities = salesDao.selectList(salesEntityWrapper);
                                            //如果导购员存在
                                            if(salesEntities != null) {
                                                //导购员只有一个,则插入导购员序号
                                                if(salesEntities.size() == 1) {
                                                    saleShoesDetailCopyEntity.setSaleSeq(salesEntities.get(0).getSeq());
                                                //如果导购员不存在,可能存在导购员人员调动,当前销售所在门店和现在门店不一致
                                                }else if(salesEntities.size() == 0) {
                                                    //根据身份证号查询,对方ERP数据里有一个人没有身份证号,所以用姓名辅助查询
                                                    salesEntityWrapper = new EntityWrapper<>();
                                                    salesEntityWrapper.eq("Name",row[15]);
                                                    if(row[14] == null) {
                                                        salesEntityWrapper.isNull("ID_Card_No");
                                                    }else {
                                                        salesEntityWrapper.eq("ID_Card_No",row[14]);
                                                    }
                                                    salesEntities = salesDao.selectList(salesEntityWrapper);
                                                    //对方ERP系统中,存在同一个人员同时属于两个门店,所以可能会查询出多条,如果存在多条只选其中一条插入导购员序号
                                                    if(salesEntities != null && salesEntities.size() > 0) {
                                                        saleShoesDetailCopyEntity.setSaleSeq(salesEntities.get(0).getSeq());
                                                    }
                                                //如果多于一条数据,则抛出导购员查询异常停止任务
                                                }else {
                                                    throw new RuntimeException("导购员数据查询有误");
                                                }
                                            }
                                        }
                                    }
                                }
                                saleShoesDetailEntities.add(saleShoesDetailCopyEntity);
                            }
                        }
                    }
                }
            }
            //循环新增零售数据
            if(saleShoesDetailEntities.size() > 0) {
                //sqlserver超过2100个参数就会报错
                //这里进行100条一次批量插入 优化方案用自带的insertBatch代替
                List<SaleShoesDetailCopyEntity> saleShoesDetailCopyEntityList = new ArrayList<>(100);
                for(int i = 0;i < saleShoesDetailEntities.size();i++) {
                    saleShoesDetailEntities.get(i).setGoodsType(goodsType);
                    saleShoesDetailCopyEntityList.add(saleShoesDetailEntities.get(i));
                    if(saleShoesDetailCopyEntityList.size() == 100) {
                        saleShoesDetailCopyDao.insertSaleShoesDetailCopy(saleShoesDetailCopyEntityList);
                        saleShoesDetailCopyEntityList.clear();
                    }else if(i == saleShoesDetailEntities.size() - 1) {
                        saleShoesDetailCopyDao.insertSaleShoesDetailCopy(saleShoesDetailCopyEntityList);
                    }
                }
            }
            //从副表同步到主表
            saleShoesDetailDao.selectSaleShoesDetailCopyIntoSaleShoesDetail();
            //清空副表
            saleShoesDetailCopyDao.deleteSaleShoesDetailCopy();
        }
        //获取有效鞋子序号
        Map<String,Object> map = new HashMap<>(10);
        map.put("companySeq",companySeq);
        List<Integer> seqList = goodsShoesDao.getAllShoesSeq(map);
        //删除无效鞋子
        if(seqList != null & seqList.size() > 0) {
            String seqs = Joiner.on(",").join(seqList);
            seqs = "(" + seqs + ")";
            map.put("seqs",seqs);
            //可以用批量删除 goodsShoesDao.deleteBatchIds();代替in
            goodsShoesDao.deleteInvalidShoes(map);
        }
        logger.error("零售拉取结束,耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
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
