package io.nuite.modules.erp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.erp.service.ErpSizeService;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.erp.utils.ErpDataVo;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 拉取ERP尺寸Service实现类
 * @author: jxj
 * @create: 2019-05-20 17:55
 */
@Service
public class ErpSizeServiceImpl implements ErpSizeService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GoodsSizeDao goodsSizeDao;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Override
    public boolean getErpSize() throws Exception {
        logger.error("尺码拉取开始");
        Long startTime = System.currentTimeMillis();
        JSONObject jsonObject = BoJunErpPortalUtil.querySql("M_SIZE","VALUE,NAME,M_ATTRIBUTE_ID;NAME",null,"ID ASC",0,999999999);
        ErpDataVo erpDataVo = JSONObject.toJavaObject(jsonObject,ErpDataVo.class);
        if(erpDataVo != null) {
            String[][] erpData = erpDataVo.getRows();
            List<GoodsSizeEntity> goodsSizeExEntities = new ArrayList<>(10);
            for(String[] row : erpData) {
                GoodsSizeEntity goodsSizeExEntity = new GoodsSizeEntity();
                goodsSizeExEntity.setCompanySeq(companySeq);
                goodsSizeExEntity.setSizeCode(row[1]);
                goodsSizeExEntity.setSizeName(row[0]);
                goodsSizeExEntity.setInputTime(new Date());
                goodsSizeExEntity.setDel(0);
                goodsSizeExEntities.add(goodsSizeExEntity);
            }
            //获取当前公司下所有尺码
            Wrapper<GoodsSizeEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("Company_Seq",companySeq);
            List<GoodsSizeEntity> goodsSizeExEntityList = goodsSizeDao.selectList(wrapper);
            //已存在尺码列表
            List<GoodsSizeEntity> indexList = new ArrayList<>(10);
            if(goodsSizeExEntityList.size() > 0) {
                //循环判断尺码是否存在
                for(int i = 0;i < goodsSizeExEntities.size();i++) {
                    for(int j = 0;j < goodsSizeExEntityList.size();j++) {
                        //加入已存在尺码
                        if(goodsSizeExEntities.get(i).getSizeName().equals(goodsSizeExEntityList.get(j).getSizeName())){
                            indexList.add(goodsSizeExEntities.get(i));
                        }
                    }
                }
                //去除已存在尺码
                goodsSizeExEntities.removeAll(indexList);
            }
            //循环新增尺码
            if(goodsSizeExEntities.size() > 0) {
                //sqlserver超过2100个参数就会报错
                //这里进行100条一次批量插入
                List<GoodsSizeEntity> goodsSizeEx = new ArrayList<>(100);
                for(int i = 0;i < goodsSizeExEntities.size();i++) {
                    goodsSizeEx.add(goodsSizeExEntities.get(i));
                    if(goodsSizeEx.size() == 100) {
                        goodsSizeDao.insertGoodsSize(goodsSizeEx);
                        goodsSizeEx.clear();
                    }else if(i == goodsSizeExEntities.size() - 1) {
                        goodsSizeDao.insertGoodsSize(goodsSizeEx);
                    }
                }
            }
        }
        logger.error("尺码拉取结束,耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
        return true;
    }
}
