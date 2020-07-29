package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.sr_base.dao.GoodsColorDao;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;
import io.nuite.modules.system.service.SystemGoodsColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemGoodsColorServiceImpl extends ServiceImpl<GoodsColorDao, GoodsColorEntity>
    implements SystemGoodsColorService {
    
    @Autowired
    private GoodsColorDao goodsColorDao;
    
    @Autowired
    private ShoesDataDao shoesDataDao;
    
    /**
     * 分页查询查询鞋子颜色
     */
    @Override
    public Page<GoodsColorEntity> getGoodsColorPage(Integer companySeq, Integer pageNum, Integer pageSize) {
        Wrapper<GoodsColorEntity> wrapper = new EntityWrapper<GoodsColorEntity>();
        wrapper.where("Company_Seq = {0}", companySeq).orderBy("InputTime", false);
        Page<GoodsColorEntity> page = new Page<GoodsColorEntity>(pageNum, pageSize);
        return this.selectPage(page, wrapper);
    }
    
    /**
     * 新增颜色
     */
    @Override
    public Integer addGoodsColor(GoodsColorEntity goodsColorEntity) {
        goodsColorDao.insert(goodsColorEntity);
        return goodsColorEntity.getSeq();
    }
    
    /**
     * 修改颜色
     */
    @Override
    public void updateGoodsColor(GoodsColorEntity goodsColorEntity) {
        goodsColorDao.updateById(goodsColorEntity);
    }
    
    /**
     * 判断颜色是否有鞋子在使用
     */
    @Override
    public Boolean hasShoesInColor(Integer seq) {
        Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
        wrapper.setSqlSelect("Top 1 Seq").where("Color_Seq = {0}", seq);
        List<ShoesDataEntity> list = shoesDataDao.selectList(wrapper);
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return false;
        }
        
    }
    
    /**
     * 删除颜色
     */
    @Override
    public void deleteGoodsColor(Integer seq) {
        goodsColorDao.deleteById(seq);
    }
    
    /**
     * 编辑
     */
    @Override
    public GoodsColorEntity edit(Integer seq) {
        GoodsColorEntity goodsColorEntity = new GoodsColorEntity();
        goodsColorEntity.setSeq(seq);
        return goodsColorDao.selectById(goodsColorEntity);
    }
    
    /**
     * 批量新增颜色
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBatchGoodsColors(List<GoodsColorEntity> goodsColorList) {
        //使用批处理会报错，先一条一条插入
        for (GoodsColorEntity goodsColorEntity : goodsColorList) {
            goodsColorDao.insert(goodsColorEntity);
        }
    }
    
    /**
     * 判断颜色是否已存在
     */
    @Override
    public boolean colorNameExisted(Integer colorSeq, Integer companySeq, String name) {
        Wrapper<GoodsColorEntity> wrapper = new EntityWrapper<GoodsColorEntity>();
        wrapper.where("Company_Seq = {0} AND Name = {1}", companySeq, name);
        List<GoodsColorEntity> list = goodsColorDao.selectList(wrapper);
        if (list.size() == 0) {
            return false;
        } else if (list.size() == 1) {
            if (colorSeq != null) { //修改，判断seq是否相同
                if (list.get(0).getSeq().equals(colorSeq)) {
                    return false;  //相同，返回false
                } else {
                    return true;
                }
            } else { //新增，返回true
                return true;
            }
        } else {
            return true;
        }
    }
    
    @Override
    public Integer selectColorSeqByCode(Integer companySeq, String code) {
        try {
            Integer colorSeq = goodsColorDao.selectColorSeqByCode(companySeq, code);
            return colorSeq;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
