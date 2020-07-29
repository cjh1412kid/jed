package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.GoodsCategoryDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.system.service.SystemGoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemGoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryDao,GoodsCategoryEntity> implements SystemGoodsCategoryService {

    @Autowired
    private GoodsCategoryDao goodsCategoryDao;

    @Autowired
    private GoodsShoesDao goodsShoesDao;

    /**
     * 全部分类
     */
    @Override
    public List<GoodsCategoryEntity> getAllGoodsCategoryByCompanySeq(Integer companySeq) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Company_Seq", companySeq);
        return goodsCategoryDao.selectByMap(map);
    }

    /**
     * 新增分类
     */
    @Override
    public Integer addGoodsCategory(GoodsCategoryEntity goodsCategoryEntity) {
        goodsCategoryDao.insert(goodsCategoryEntity);
        return goodsCategoryEntity.getSeq();
    }

    /**
     * 修改分类
     */
    @Override
    public void updateGoodsCategory(GoodsCategoryEntity goodsCategoryEntity) {
        // 首先判断是否有子分类
        Wrapper<GoodsCategoryEntity> wrapper = new EntityWrapper<GoodsCategoryEntity>();
        wrapper.eq("parentSeq", goodsCategoryEntity.getSeq()).eq("Del", 0);
        List<GoodsCategoryEntity> list = goodsCategoryDao.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            goodsCategoryDao.updateById(goodsCategoryEntity);
        }
        List<GoodsCategoryEntity> lastList = new ArrayList<GoodsCategoryEntity>();
        if (list != null && !list.isEmpty()) {
            for (GoodsCategoryEntity categoryEntity : list) {
                lastList.add(categoryEntity);
                List<GoodsCategoryEntity> nextlist = goodsCategoryDao
                        .selectList(new EntityWrapper<GoodsCategoryEntity>().eq("parentSeq", categoryEntity.getSeq()));
                if (nextlist != null && !nextlist.isEmpty()) {
                    for (GoodsCategoryEntity nextgoodsCategoryEntity : nextlist) {
                        lastList.add(nextgoodsCategoryEntity);
                    }
                }
            }
            lastList.add(goodsCategoryEntity);
            for (GoodsCategoryEntity categoryEntitNext : lastList) {
                categoryEntitNext.setVisible(goodsCategoryEntity.getVisible());
                goodsCategoryDao.updateById(categoryEntitNext);
            }
        }

    }

    /**
     * 判断分类下是否有鞋子
     */
    @Override
    public Boolean hasShoesInCategory(Integer seq) {
        Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<GoodsShoesEntity>();
        wrapper.eq("Category_Seq", seq);
        List<GoodsShoesEntity> list = goodsShoesDao.selectList(wrapper);
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean hasCategoryInCategory(Integer seq) {
        // 首先判断是否有子分类
        Wrapper<GoodsCategoryEntity> wrapper = new EntityWrapper<GoodsCategoryEntity>();
        wrapper.eq("parentSeq", seq);
        List<GoodsCategoryEntity> list = goodsCategoryDao.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            return this.hasShoesInCategory(seq);
        }
        // 如果有子分类查询所有子分类
        List<GoodsCategoryEntity> lastList = new ArrayList<GoodsCategoryEntity>();
        if (list != null && !list.isEmpty()) {
            for (GoodsCategoryEntity goodsCategoryEntity : list) {
                lastList.add(goodsCategoryEntity);
                List<GoodsCategoryEntity> nextlist = goodsCategoryDao.selectList(
                        new EntityWrapper<GoodsCategoryEntity>().eq("parentSeq", goodsCategoryEntity.getSeq()));
                if (nextlist != null && !nextlist.isEmpty()) {
                    for (GoodsCategoryEntity nextgoodsCategoryEntity : nextlist) {
                        lastList.add(nextgoodsCategoryEntity);
                    }
                }
            }
            for (GoodsCategoryEntity goodsCategoryEntity : lastList) {
                if (this.hasShoesInCategory(goodsCategoryEntity.getSeq())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 删除分类
     */
    @Override
    public void deleteGoodsCategory(Integer seq) {
        goodsCategoryDao.deleteById(seq);
    }

    /**
     * 删除分类
     */
    @Override
    public void deleteGoodsCategoryAll(Integer seq) {
        // 首先判断是否有子分类
        Wrapper<GoodsCategoryEntity> wrapper = new EntityWrapper<GoodsCategoryEntity>();
        wrapper.eq("parentSeq", seq).eq("Del", 0);
        List<GoodsCategoryEntity> list = goodsCategoryDao.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            this.deleteGoodsCategory(seq);
        }
        // 如果有子分类查询所有子分类
        List<GoodsCategoryEntity> lastList = new ArrayList<GoodsCategoryEntity>();
        if (list != null && !list.isEmpty()) {
            for (GoodsCategoryEntity goodsCategoryEntity : list) {
                lastList.add(goodsCategoryEntity);
                List<GoodsCategoryEntity> nextlist = goodsCategoryDao.selectList(
                        new EntityWrapper<GoodsCategoryEntity>().eq("parentSeq", goodsCategoryEntity.getSeq()));
                if (nextlist != null && !nextlist.isEmpty()) {
                    for (GoodsCategoryEntity nextgoodsCategoryEntity : nextlist) {
                        lastList.add(nextgoodsCategoryEntity);
                    }
                }
            }
            for (GoodsCategoryEntity goodsCategoryEntity : lastList) {
                this.deleteGoodsCategory(goodsCategoryEntity.getSeq());
            }
            this.deleteGoodsCategory(seq);
        }
    }

    @Override
    public List<Integer> nOcategoryLsit(Integer companySeq) {
        List<GoodsCategoryEntity> list = goodsCategoryDao.selectList(
                new EntityWrapper<GoodsCategoryEntity>().eq("Company_Seq", companySeq).eq("ParentSeq", 0).eq("Del", 0));
        List<Integer> nOcategoryLsit = new ArrayList<Integer>();
        List<GoodsCategoryEntity> nextCategoryList = new ArrayList<GoodsCategoryEntity>();
        if (list != null && !list.isEmpty()) {
            for (GoodsCategoryEntity goodsCategoryEntity : list) {
                List<GoodsCategoryEntity> nextlist = goodsCategoryDao
                        .selectList(new EntityWrapper<GoodsCategoryEntity>()
                                .eq("ParentSeq", goodsCategoryEntity.getSeq()).eq("Del", 0));
                if (nextlist != null && !nextlist.isEmpty()) {
                    for (GoodsCategoryEntity nextCategoryEntity : nextlist) {
                        if (nextlist != null && !nextlist.isEmpty()) {
                            List<GoodsCategoryEntity> lastlist = goodsCategoryDao
                                    .selectList(new EntityWrapper<GoodsCategoryEntity>()
                                            .eq("ParentSeq", nextCategoryEntity.getSeq()).eq("Del", 0));
                            if (lastlist != null && !lastlist.isEmpty()) {
                                for (GoodsCategoryEntity lastgoodsCategoryEntity : lastlist) {
                                    nextCategoryList.add(lastgoodsCategoryEntity);
                                }
                            }
                        }
                    }
                }
            }
            for (GoodsCategoryEntity goodsCategoryEntity : nextCategoryList) {
                nOcategoryLsit.add(goodsCategoryEntity.getSeq());
            }
        }
        return nOcategoryLsit;
    }

}
