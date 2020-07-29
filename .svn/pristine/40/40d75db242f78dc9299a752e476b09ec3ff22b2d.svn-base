package io.nuite.modules.erp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.erp.service.ErpColorService;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.erp.utils.DataHandleUtil;
import io.nuite.modules.erp.utils.ErpDataVo;
import io.nuite.modules.sr_base.dao.GoodsColorDao;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 拉取ERP颜色Service实现类
 * @author: jxj
 * @create: 2019-05-20 17:32
 */
@Service
public class ErpColorServiceImpl implements ErpColorService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GoodsColorDao goodsColorDao;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean getErpColor() throws Exception {
        logger.error("颜色拉取开始");
        Long startTime = System.currentTimeMillis();
        JSONObject jsonObject = BoJunErpPortalUtil.querySql("M_COLOR","VALUE,NAME,ISACTIVE",null,"ID ASC",0,999999999);
        ErpDataVo erpDataVo = JSONObject.toJavaObject(jsonObject,ErpDataVo.class);
        if(erpDataVo != null) {
            String[][] erpData = DataHandleUtil.removeBlank(erpDataVo.getRows());
            if(erpData != null && erpData.length > 0) {
                List<GoodsColorEntity> goodsColorEntities = new ArrayList<>(10);
                for (String[] row : erpData) {
                    //判断ERP里数据是否可用
                    if(row[2] != null && "是".equals(row[2])) {
                        GoodsColorEntity goodsColorExEntity = new GoodsColorEntity();
                        goodsColorExEntity.setCompanySeq(companySeq);
                        goodsColorExEntity.setCode(row[0]);
                        goodsColorExEntity.setName(row[1]);
                        goodsColorExEntity.setInputTime(new Date());
                        goodsColorExEntity.setDel(0);
                        goodsColorEntities.add(goodsColorExEntity);
                    }
                }
                //获取公司下所有颜色
                Wrapper<GoodsColorEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("Company_Seq",companySeq);
                List<GoodsColorEntity> goodsColorExEntityList = goodsColorDao.selectList(wrapper);
                //已存在颜色列表
                List<GoodsColorEntity> indexList = new ArrayList<>(10);
                //循环判断颜色是否存在
                if(goodsColorExEntityList != null && goodsColorExEntityList.size() > 0) {
                    for(int i = 0;i < goodsColorEntities.size();i++) {
                        for(int j = 0;j < goodsColorExEntityList.size();j++) {
                            //如果已存在加入已存在列表
                            if(goodsColorEntities.get(i) != null && goodsColorEntities.get(i).getName().equals(goodsColorExEntityList.get(j).getName())) {
                                indexList.add(goodsColorEntities.get(i));
                            }
                        }
                    }
                    //删除已存在的
                    goodsColorEntities.removeAll(indexList);
                }
                //循环新增颜色
                if(goodsColorEntities.size() > 0) {
                    //sqlserver超过2100个参数就会报错
                    //这里进行100条一次批量插入
                    List<GoodsColorEntity> goodsColorExList = new ArrayList<>(100);
                    for(int i = 0;i < goodsColorEntities.size();i++) {
                        goodsColorExList.add(goodsColorEntities.get(i));
                        if(goodsColorExList.size() == 100) {
                            goodsColorDao.insertGoodsColor(goodsColorExList);
                            goodsColorExList.clear();
                        }else if(i == goodsColorEntities.size() - 1) {
                            goodsColorDao.insertGoodsColor(goodsColorExList);
                        }
                    }
                }
            }
        }
        logger.error("颜色拉取结束,耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
        return true;
    }
}
