package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import io.nuite.modules.system.service.SystemGoodsSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemGoodsSizeServiceImpl extends ServiceImpl<GoodsSizeDao, GoodsSizeEntity> implements SystemGoodsSizeService {

    @Autowired
    private GoodsSizeDao goodsSizeDao;

    @Autowired
    private ShoesDataDao shoesDataDao;

    /**
     * 分页查询查询鞋子尺码
     */
    @Override
    public Page<GoodsSizeEntity> getGoodsSizePage(Integer companySeq, Integer pageNum, Integer pageSize) {
        Wrapper<GoodsSizeEntity> wrapper = new EntityWrapper<GoodsSizeEntity>();
        wrapper.where("Company_Seq = {0}", companySeq);
        Page<GoodsSizeEntity> page = new Page<GoodsSizeEntity>(pageNum, pageSize);
        return this.selectPage(page, wrapper);
    }

    /**
     * 新增尺码
     */
    @Override
    public Integer addGoodsSize(GoodsSizeEntity goodsSizeEntity) {
        goodsSizeDao.insert(goodsSizeEntity);
        return goodsSizeEntity.getSeq();
    }

    /**
     * 修改尺码
     */
    @Override
    public void updateGoodsSize(GoodsSizeEntity goodsSizeEntity) {
        goodsSizeDao.updateById(goodsSizeEntity);
    }

    /**
     * 判断尺码是否有鞋子在使用
     */
    @Override
    public Boolean hasShoesInSize(Integer seq) {
        Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
        wrapper.setSqlSelect("Top 1 Seq").where("Size_Seq = {0}", seq);
        List<ShoesDataEntity> list = shoesDataDao.selectList(wrapper);
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 删除尺码
     */
    @Override
    public void deleteGoodsSize(Integer seq) {
        goodsSizeDao.deleteById(seq);
    }

    /**
     * 编辑
     */
    @Override
    public GoodsSizeEntity edit(Integer seq) {
        GoodsSizeEntity goodsSizeEntity = new GoodsSizeEntity();
        goodsSizeEntity.setSeq(seq);
        return goodsSizeDao.selectById(goodsSizeEntity);
    }

    /**
     * 批量新增尺码
     */
    @Override
	@Transactional(propagation = Propagation.REQUIRED)
    public void addBatchGoodsSizes(List<GoodsSizeEntity> goodsSizeList) {
        //使用批处理会报错，先一条一条插入
        for (GoodsSizeEntity goodsSizeEntity : goodsSizeList) {
            goodsSizeDao.insert(goodsSizeEntity);
        }
    }

    /**
     * 判断尺码是否已存在
     */
    @Override
    public boolean sizeNameExisted(Integer sizeSeq, Integer companySeq, String sizeName) {
        Wrapper<GoodsSizeEntity> wrapper = new EntityWrapper<GoodsSizeEntity>();
        wrapper.where("Company_Seq = {0} AND SizeName = {1}", companySeq, sizeName);
        List<GoodsSizeEntity> list = goodsSizeDao.selectList(wrapper);
        if (list.size() == 0) {
            return false;
        } else if (list.size() == 1){
            if (sizeSeq != null) { //修改，判断seq是否相同
                if (list.get(0).getSeq().equals(sizeSeq)) {
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
    public Integer getSizeSeqByCodeAndCompanySeq(Integer companySeq, String sizeCode) {

        try {
            Integer seq = goodsSizeDao.selectSizeSeqByCodeAndCompanySeq(companySeq, sizeCode);
            return seq;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
