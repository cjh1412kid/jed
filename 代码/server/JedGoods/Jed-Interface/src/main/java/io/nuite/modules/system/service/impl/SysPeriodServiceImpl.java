package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.GoodsPeriodDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.system.service.SysPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysPeriodServiceImpl extends ServiceImpl<GoodsPeriodDao, GoodsPeriodEntity> implements SysPeriodService {

    @Autowired
    private GoodsPeriodDao goodsPeriodDao;

    @Autowired
    private GoodsShoesDao goodsShoesDao;

    @Override
    public Page<GoodsPeriodEntity> getPeriodPage(Integer BrandSeq, Integer pageNum, Integer pageSize) {
        Wrapper<GoodsPeriodEntity> wrapper = new EntityWrapper<GoodsPeriodEntity>();
        wrapper.eq("Brand_Seq", BrandSeq).eq("Del", "0");
        Page<GoodsPeriodEntity> page = new Page<GoodsPeriodEntity>(pageNum, pageSize,"InputTime",false);
        return this.selectPage(page, wrapper);
    }

    /**
     * 判断该波次是否有鞋子在使用
     */
    @Override
    public Boolean hasShoesInPeriod(Integer seq) {
        Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<GoodsShoesEntity>();
        wrapper.eq("Period_Seq", seq).eq("Del", 0);
        List<GoodsShoesEntity> list = goodsShoesDao.selectList(wrapper);
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteGoodsPeriod(Integer seq) {
        goodsPeriodDao.deleteById(seq);
    }

    @Override
    public GoodsPeriodEntity edit(Integer seq) {
        GoodsPeriodEntity goodsPeriodEntity = new GoodsPeriodEntity();
        goodsPeriodEntity.setSeq(seq);
        return goodsPeriodDao.selectById(goodsPeriodEntity);
    }

    @Override
    public void addGoodsPeriod(Integer brandSeq, GoodsPeriodEntity goodsPeriodEntity) {
        goodsPeriodEntity.setBrandSeq(brandSeq);
        goodsPeriodDao.insert(goodsPeriodEntity);
    }

    @Override
    public void updateGoodsPeriod(GoodsPeriodEntity goodsPeriodEntity) {
        goodsPeriodDao.updateById(goodsPeriodEntity);
    }

    @Override
    public Integer selectSeqByName(Integer brandSeq, String year, String name) {
        List<GoodsPeriodEntity> periodList = goodsPeriodDao.selectList(new EntityWrapper<GoodsPeriodEntity>().eq("Brand_Seq", brandSeq)
                .eq("Year", year)
                .like("Name", year + name));

        if (periodList.isEmpty()) {
            GoodsPeriodEntity goodsPeriodEntity = new GoodsPeriodEntity();
            goodsPeriodEntity.setBrandSeq(brandSeq);
            goodsPeriodEntity.setName(year + name);
            goodsPeriodEntity.setYear(Integer.valueOf(year));
            goodsPeriodEntity.setSaleDate(new Date());
            goodsPeriodDao.insert(goodsPeriodEntity);
            return goodsPeriodEntity.getSeq();
        } else {
            return periodList.get(0).getSeq();
        }

    }
}
