package io.nuite.modules.erp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.erp.service.ErpTargetService;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.erp.utils.DataHandleUtil;
import io.nuite.modules.erp.utils.ErpDataVo;
import io.nuite.modules.order_platform_app.dao.SalesDao;
import io.nuite.modules.order_platform_app.dao.TargetSalesDao;
import io.nuite.modules.order_platform_app.dao.TargetShopDao;
import io.nuite.modules.order_platform_app.entity.SalesEntity;
import io.nuite.modules.order_platform_app.entity.TargetSalesEntity;
import io.nuite.modules.order_platform_app.entity.TargetShopEntity;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: ERP拉取指标ServiceImpl
 * @author: jxj
 * @create: 2019-11-18 14:19
 */
@Service
public class ErpTargetServiceImpl implements ErpTargetService {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private BaseShopDao baseShopDao;

    @Autowired
    private TargetShopDao targetShopDao;

    @Autowired
    private TargetSalesDao targetSalesDao;

    @Autowired
    private SalesDao salesDao;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Value("${jrd.userSeq}")
    private Integer userSeq;

    @Override
    public boolean getErpShopTarget() throws Exception {
        logger.error("门店指标拉取开始");
        Long startTime = System.currentTimeMillis();
        //0 店铺编号
        String columns = "C_STORE_ID;CODE," +
                //1 年月
                "YEARMONTH," +
                //2 月营业指标
                "TOT_AMT_MARK," +
                //3 月销量指标
                "TOT_MONSALE_TARGET," +
                //4 是否可以
                "ISACTIVE";
        JSONObject jsonObject = BoJunErpPortalUtil.querySql("C_STOREMARK", columns, null, "ID ASC", 0, 999999999);
        ErpDataVo erpDataVo = JSONObject.toJavaObject(jsonObject, ErpDataVo.class);
        if (erpDataVo != null) {
            String[][] erpData = DataHandleUtil.removeBlank(erpDataVo.getRows());
            if (erpData != null && erpData.length > 0) {
                //获取当前公司下所有门店
                Wrapper<BaseShopEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("Company_Seq", companySeq);
                List<BaseShopEntity> baseShopEntities = baseShopDao.selectList(wrapper);
                List<TargetShopEntity> targetShopEntities = new ArrayList<>(10);
                for (String[] row : erpData) {
                    //判断该条数据是否符合要求
                    if (row[4] != null && row[4].equals("是")) {
                        if (baseShopEntities != null && baseShopEntities.size() > 0) {
                            //循环所有门店
                            for (BaseShopEntity baseShopEntity : baseShopEntities) {
                                //获取当前门店与数据对应时间和年份的指标
                                Wrapper<TargetShopEntity> targetShopEntityWrapper = new EntityWrapper<>();
                                Integer year = Integer.parseInt(row[1].substring(0, 4));
                                Integer month = Integer.parseInt(row[1].substring(4, row[1].length()));
                                targetShopEntityWrapper.eq("Shop_Seq", baseShopEntity.getSeq());
                                targetShopEntityWrapper.eq("Target_Year", year);
                                targetShopEntityWrapper.eq("Target_Month", month);
                                List<TargetShopEntity> targetShopEntityList = targetShopDao.selectList(targetShopEntityWrapper);
                                //如果数据不存在
                                if (targetShopEntityList == null || targetShopEntityList.size() == 0) {
                                    //配对当前门店的数据,插入数据库
                                    if (baseShopEntity != null && baseShopEntity.getId() != null && baseShopEntity.getId().equals(row[0])) {
                                        TargetShopEntity targetShopEntity = new TargetShopEntity();
                                        if (row[2] != null && !row[2].equals("")) {
                                            targetShopEntity.setMoney(new BigDecimal(row[2]).divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP));
                                        } else if (row[3] != null && !row[3].equals("")) {
                                            targetShopEntity.setMoney(new BigDecimal(row[3]).divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP));
                                        } else {
                                            targetShopEntity.setMoney(BigDecimal.ZERO);
                                        }
                                        targetShopEntity.setDel(0);
                                        targetShopEntity.setTargetYear(year);
                                        targetShopEntity.setTargetMonth(month);
                                        targetShopEntity.setTag(1);
                                        targetShopEntity.setCreator(userSeq);
                                        targetShopEntity.setShopSeq(baseShopEntity.getSeq());
                                        targetShopEntity.setInputTime(new Date());
                                        targetShopEntities.add(targetShopEntity);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (targetShopEntities.size() > 0) {
                    targetShopDao.insertTargetShop(targetShopEntities);
                }
            }
        }
        logger.error("门店指标拉取结束,耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
        return true;
    }

    @Override
    public boolean getErpSalesTarget() throws Exception {
        logger.error("店员指标拉取开始");
        Long startTime = System.currentTimeMillis();
        //0 店铺编号
        String columns = "C_STORE_ID;CODE," +
                //1 员工身份证号
                "SALESREP_ID;ID_CARD," +
                //2 年月
                "YEARMONTH," +
                //3 月营业指标
                "TOT_AMT_MARK," +
                //4 是否可以
                "ISACTIVE";
        JSONObject jsonObject = BoJunErpPortalUtil.querySql("C_SALERMARK", columns, null, "ID ASC", 0, 999999999);
        ErpDataVo erpDataVo = JSONObject.toJavaObject(jsonObject, ErpDataVo.class);
        if (erpDataVo != null) {
            String[][] erpData = DataHandleUtil.removeBlank(erpDataVo.getRows());
            if (erpData != null && erpData.length > 0) {
                //获取公司下所有门店
                Wrapper<BaseShopEntity> baseShopEntityWrapper = new EntityWrapper<>();
                baseShopEntityWrapper.eq("Company_Seq", companySeq);
                List<BaseShopEntity> baseShopEntities = baseShopDao.selectList(baseShopEntityWrapper);
                for (String[] row : erpData) {
                    if (row[4] != null && "是".equals(row[4])) {
                        //循环所有的门店判断
                        for (BaseShopEntity baseShopEntity : baseShopEntities) {
                            //当前导购员所在的门店
                            if(baseShopEntity.getId().equals(row[0])) {
                                //获取当前门店当前导购员
                                SalesEntity salesEntity = new SalesEntity();
                                salesEntity.setIdCardNo(row[1]);
                                salesEntity.setShopSeq(baseShopEntity.getSeq());
                                salesEntity = salesDao.selectOne(salesEntity);
                                //判断是否存在该导购员
                                if(salesEntity != null) {
                                    //获取该门店在对应年月的指标
                                    TargetShopEntity targetShopEntity = new TargetShopEntity();
                                    Integer year = Integer.parseInt(row[2].substring(0, 4));
                                    Integer month = Integer.parseInt(row[2].substring(4, row[2].length()));
                                    targetShopEntity.setTargetYear(year);
                                    targetShopEntity.setTargetMonth(month);
                                    targetShopEntity.setShopSeq(baseShopEntity.getSeq());
                                    targetShopEntity = targetShopDao.selectOne(targetShopEntity);
                                    //获取该导购员所对应的门店指标下的指标
                                    if(targetShopEntity == null) {
                                        continue;
                                    }
                                    TargetSalesEntity targetSalesEntity = new TargetSalesEntity();
                                    targetSalesEntity.setTargetShopSeq(targetShopEntity.getSeq());
                                    targetSalesEntity.setSalesSeq(salesEntity.getSeq());
                                    targetSalesEntity = targetSalesDao.selectOne(targetSalesEntity);
                                    //导购员指标不存在则新增
                                    if(targetSalesEntity == null) {
                                        targetSalesEntity = new TargetSalesEntity();
                                        targetSalesEntity.setTargetShopSeq(targetShopEntity.getSeq());
                                        targetSalesEntity.setSalesSeq(salesEntity.getSeq());
                                        targetSalesEntity.setMoney(new BigDecimal(row[3]));
                                        targetSalesDao.insert(targetSalesEntity);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        logger.error("店员指标拉取结束,耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
        return true;
    }
}
