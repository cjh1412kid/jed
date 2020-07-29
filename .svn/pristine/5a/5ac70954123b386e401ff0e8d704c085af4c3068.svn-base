package io.nuite.modules.erp.service.impl;

import io.nuite.modules.erp.service.ErpShopService;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.erp.utils.DataHandleUtil;
import io.nuite.modules.erp.utils.ErpDataVo;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.service.BaseAreaService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description: 拉取ERP门店Service实现类
 * @author: jxj
 * @create: 2019-05-20 17:40
 */
@Service
public class ErpShopServiceImpl implements ErpShopService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BaseShopDao baseShopDao;

    @Autowired
    private BaseAreaService baseAreaService;

    @Value("${jrd.companySeq}")
    private Integer companySeq;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean getErpShop() throws Exception {
        logger.error("门店拉取开始");
        Long startTime = System.currentTimeMillis();
        JSONObject jsonObject = BoJunErpPortalUtil.querySql("C_STORE","CODE,NAME,C_STORETYPE_JZ_ID;NAME,C_CUSTOMERUP_ID;CODE,ISACTIVE",null,"ID ASC",0,999999999);
        ErpDataVo erpDataVo = JSONObject.toJavaObject(jsonObject,ErpDataVo.class);
        if(erpDataVo != null) {
            String[][] erpData = DataHandleUtil.removeBlank(erpDataVo.getRows());
            if(erpData != null && erpData.length > 0) {
                List<BaseShopEntity> baseShopEntities = new ArrayList<>(10);
                for(String[] row : erpData) {
                    //判断该条数据是否符合要求
                    if(row[2] != null && row[3] != null && "自营店".equals(row[2]) && "JEDWZ".equals(row[3])) {
                        BaseShopEntity baseShopEntity = new BaseShopEntity();
                        baseShopEntity.setName(row[1]);
                        baseShopEntity.setId(row[0]);
                        baseShopEntity.setAreaSeq(baseAreaService.getAreaSeqByCompanySeq(companySeq));
                        baseShopEntity.setCompanySeq(companySeq);
                        baseShopEntity.setInputTime(new Date());
                        baseShopEntity.setDel(0);
                        if(row[4] != null && "是".equals(row[4])) {
                            baseShopEntity.setIsActive(1);
                        }else {
                            baseShopEntity.setIsActive(0);
                        }
                        baseShopEntities.add(baseShopEntity);
                    }
                }
                //获取公司下全部门店
                Map<String,Object> map = new HashMap<>(10);
                map.put("companySeq",companySeq);
                List<BaseShopEntity> baseShopEntityList = baseShopDao.selectShop(map);
                List<BaseShopEntity> indexList = new ArrayList<>(10);
                //判断门店是否为空
                if(baseShopEntityList != null && baseShopEntityList.size() > 0) {
                    //循环判断门店是否存在,存在的更新,不存在的新增
                    for(int i = 0;i < baseShopEntities.size();i++) {
                        for(int j = 0;j < baseShopEntityList.size();j++) {
                            //已存在的加入更新列表
                            if(baseShopEntities.get(i) != null && (baseShopEntities.get(i).getName().equals(baseShopEntityList.get(j).getName()) || baseShopEntities.get(i).getId().equals(baseShopEntityList.get(j).getId()))) {
                                baseShopEntities.get(i).setSeq(baseShopEntityList.get(j).getSeq());
                                indexList.add(baseShopEntities.get(i));
                            }
                        }
                    }
                    //去除已存在的
                    baseShopEntities.removeAll(indexList);
                }
                //循环新增门店
                if(baseShopEntities != null && baseShopEntities.size() > 0) {
                    //sqlserver超过2100个参数就会报错
                    //这里进行100条一次批量插入
                    List<BaseShopEntity> baseShopList = new ArrayList<>(100);
                    for(int i = 0;i < baseShopEntities.size();i++) {
                        baseShopList.add(baseShopEntities.get(i));
                        if(baseShopList.size() == 100) {
                            baseShopDao.insertShop(baseShopList);
                            baseShopList.clear();
                        }else if(i == baseShopEntities.size() - 1) {
                            baseShopDao.insertShop(baseShopList);
                        }
                    }
                }
                //循环更新门店
                if(indexList.size() > 0) {
                    //sqlserver超过2100个参数就会报错
                    //这里进行100条一次批量更新
                    List<BaseShopEntity> baseShopList = new ArrayList<>(100);
                    for(int i = 0;i < indexList.size();i++) {
                        baseShopList.add(indexList.get(i));
                        if(baseShopList.size() == 100) {
                            baseShopDao.updateShop(baseShopList);
                            baseShopList.clear();
                        }else if(i == indexList.size() - 1) {
                            baseShopDao.updateShop(baseShopList);
                        }
                    }
                }
            }
        }
        logger.error("门店拉取结束,耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
        return true;
    }
}
