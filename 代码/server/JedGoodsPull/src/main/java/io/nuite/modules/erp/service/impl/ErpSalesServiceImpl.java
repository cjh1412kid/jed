package io.nuite.modules.erp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.erp.service.ErpSalesService;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.erp.utils.DataHandleUtil;
import io.nuite.modules.erp.utils.ErpDataVo;
import io.nuite.modules.order_platform_app.dao.SalesDao;
import io.nuite.modules.order_platform_app.entity.SalesEntity;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 拉取ERP店员Service实现类
 * @author: jxj
 * @create: 2019-06-18 08:50
 */
@Service
public class ErpSalesServiceImpl implements ErpSalesService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BaseShopDao baseShopDao;

    @Autowired
    private SalesDao salesDao;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Override
    public boolean getErpSales() throws Exception {
        logger.error("导购员拉取开始");
        Long startTime = System.currentTimeMillis();
        //0 姓名
        String columns = "NAME," +
                //1 上级经销商
                "C_CUSTOMERUP_ID;CODE," +
                //2 所属店仓
                "C_STORE_ID;NAME," +
                //3 性别
                "SEX," +
                //4 是否营业员
                "ISSALER," +
                //5 店仓类别（自营店）
                "C_STORE_ID;C_STORETYPE_JZ_ID;NAME," +
                //6 是否可用
                "ISACTIVE," +
                //7 手机
                "HANDSET," +
                //8 入职时间
                "BEGIN_DATE," +
                //9 身份证
                "ID_CARD," +
                //10 家庭地址
                "ADDRESS," +
                //11 所属店仓编码
                "C_STORE_ID;CODE," +
                //12 在职状况
                "INCUMBENCY_STS," +
                //13 编号
                 "NO";
        JSONObject jsonObject = BoJunErpPortalUtil.querySql("C_V_EMPLOYEE",columns,null,"ID ASC",0,999999999);
        ErpDataVo erpDataVo = JSONObject.toJavaObject(jsonObject,ErpDataVo.class);
        if(erpDataVo != null) {
            String[][] erpData = DataHandleUtil.removeBlank(erpDataVo.getRows());
            if(erpData != null && erpData.length > 0) {
                //获取公司下所有门店dao
                Wrapper<BaseShopEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("Company_Seq",companySeq);
                List<BaseShopEntity> baseShopEntities = baseShopDao.selectList(wrapper);
                List<SalesEntity> salesEntities = new ArrayList<>(10);
                for(String[] row : erpData) {
                    if(row[6] != null && row[5] != null && row[1] != null && "是".equals(row[6]) && "自营店".equals(row[5]) && "JEDWZ".equals(row[1])) {
                        if(baseShopEntities != null && baseShopEntities.size() > 0) {
                            //循环门店
                            for(BaseShopEntity baseShopEntity : baseShopEntities) {
                                //获取当前门店下该导购员
                                Wrapper<SalesEntity> salesEntityWrapper = new EntityWrapper<>();
                                if(row[9] == null) {
                                    salesEntityWrapper.isNull("ID_Card_No");
                                }else {
                                    salesEntityWrapper.eq("ID_Card_No", row[9]);
                                }
                                salesEntityWrapper.eq("Name",row[0]);
                                salesEntityWrapper.eq("ShopSeq",baseShopEntity.getSeq());
                                List<SalesEntity> salesList = salesDao.selectList(salesEntityWrapper);
                                //判断导购员是否存在
                                if(salesList == null || salesList.size() == 0) {
                                    //判断该导购员所属的门店
                                    if(baseShopEntity != null && baseShopEntity.getId() != null && baseShopEntity.getId().equals(row[11])) {
                                        SalesEntity salesEntity = new SalesEntity();
                                        salesEntity.setName(row[0]);
                                        salesEntity.setSex(row[3]);
                                        salesEntity.setDel(0);
                                        salesEntity.setTel(row[7]);
                                        salesEntity.setHireDate(row[8]);
                                        salesEntity.setIdCardNo(row[9]);
                                        salesEntity.setShopSeq(baseShopEntity.getSeq());
                                        salesEntity.setInputTime(new Date());
                                        salesEntity.setNativePlace(row[10]);
                                        salesEntity.setNumber(row[13]);
                                        if("在职".equals(row[12])) {
                                            salesEntity.setIsDimission(0);
                                        }else {
                                            salesEntity.setIsDimission(1);
                                        }
                                        salesEntities.add(salesEntity);
                                        break;
                                    }
                                }else {
                                    SalesEntity salesEntity = salesList.get(0);
                                    salesEntity.setNumber(row[13]);
                                    salesEntity.setSex(row[3]);
                                    salesEntity.setTel(row[7]);
                                    salesEntity.setNativePlace(row[10]);
                                    if("在职".equals(row[12])) {
                                        salesEntity.setIsDimission(0);
                                    }else {
                                        salesEntity.setIsDimission(1);
                                    }
                                    salesDao.updateById(salesEntity);
                                }
                            }
                        }
                    }
                }
                if(salesEntities.size() > 0) {
                    salesDao.insertSales(salesEntities);
                }
            }
        }
        logger.error("导购员拉取结束,耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
        return true;
    }
}
