package io.nuite.modules.erp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.erp.service.ErpSizeGroupService;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.erp.utils.ErpDataVo;
import io.nuite.modules.sr_base.dao.GoodsSizeGroupDao;
import io.nuite.modules.sr_base.entity.GoodsSizeGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 拉取ERP尺寸组Service实现类
 * @author: jxj
 * @create: 2019-05-20 18:01
 */

public class ErpSizeGroupServiceImpl implements ErpSizeGroupService {
    @Autowired
    private GoodsSizeGroupDao goodsSizeGroupDao;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Override
    public boolean getErpSizeGroup() throws Exception {
        JSONObject jsonObject = BoJunErpPortalUtil.querySql("M_SIZEGROUP","NAME,DESCRIPTION",null,"ID ASC",0,999999999);
        ErpDataVo erpDataVo = JSONObject.toJavaObject(jsonObject,ErpDataVo.class);
        if(erpDataVo != null) {
            String[][] erpData = erpDataVo.getRows();
            List<GoodsSizeGroupEntity> goodsSizeGroupEntities = new ArrayList<>(10);
            for(String[] row : erpData) {
                GoodsSizeGroupEntity goodsSizeGroupEntity = new GoodsSizeGroupEntity();
                goodsSizeGroupEntity.setCompanySeq(companySeq);
                goodsSizeGroupEntity.setName(row[0]);
                goodsSizeGroupEntity.setDescription(row[1]);
                goodsSizeGroupEntity.setInputTime(new Date());
                goodsSizeGroupEntity.setDel(0);
                goodsSizeGroupEntities.add(goodsSizeGroupEntity);
            }
            Wrapper<GoodsSizeGroupEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("Company_Seq",companySeq);
            List<GoodsSizeGroupEntity> goodsSizeGroupEntityList = goodsSizeGroupDao.selectList(wrapper);
            List<GoodsSizeGroupEntity> indexList = new ArrayList<>(10);
            if(goodsSizeGroupEntityList.size() > 0) {
                for(int i = 0;i < goodsSizeGroupEntities.size();i++) {
                    for(int j = 0;j < goodsSizeGroupEntityList.size();j++) {
                        if(goodsSizeGroupEntities.get(i).getName().equals(goodsSizeGroupEntityList.get(j).getName())) {
                            indexList.add(goodsSizeGroupEntities.get(i));
                        }
                    }
                }
                for(int i = 0;i < indexList.size();i++) {
                    goodsSizeGroupEntities.remove(indexList.get(i));
                }
            }
            if(goodsSizeGroupEntities.size() > 0) {
                List<GoodsSizeGroupEntity> goodsSizeGroupList = new ArrayList<>(100);
                for(int i = 0;i < goodsSizeGroupEntities.size();i++) {
                    goodsSizeGroupList.add(goodsSizeGroupEntities.get(i));
                    if(goodsSizeGroupList.size() == 100) {
                        goodsSizeGroupDao.insertGoodsSizeGroup(goodsSizeGroupList);
                        goodsSizeGroupList.clear();
                    }else if(i == goodsSizeGroupEntities.size() - 1) {
                        goodsSizeGroupDao.insertGoodsSizeGroup(goodsSizeGroupList);
                    }
                }
            }
        }
        return true;
    }
}
