package io.nuite.modules.sr_base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.erp.utils.ErpDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.nuite.modules.sr_base.dao.GoodsColorDao;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;
import io.nuite.modules.sr_base.service.GoodsColorService;

@Service
public class GoodsColorServiceImpl extends ServiceImpl<GoodsColorDao, GoodsColorEntity> implements GoodsColorService {


    @Autowired
    private GoodsColorDao goodsColorDao;


    /**
     * 获取公司所有颜色
     */
	@Override
	public List<GoodsColorEntity> getColorListByCompanySeq(Integer companySeq) {
        Wrapper<GoodsColorEntity> wrapper = new EntityWrapper<GoodsColorEntity>();
        wrapper.where("Company_Seq = {0}", companySeq);
        return goodsColorDao.selectList(wrapper);
	}
}