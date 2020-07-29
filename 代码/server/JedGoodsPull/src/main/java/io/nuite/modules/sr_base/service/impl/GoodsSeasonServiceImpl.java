package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.GoodsSeasonDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.GoodsSeasonEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.GoodsSeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsSeasonServiceImpl extends ServiceImpl<GoodsSeasonDao, GoodsSeasonEntity> implements GoodsSeasonService {

    @Autowired
    private GoodsSeasonDao goodsSeasonDao;

    @Autowired
    private GoodsShoesDao goodsShoesDao;

    /**
     * 后台查询季节grid页面
     */
    @Override
    public Page<GoodsSeasonEntity> selectSeasonPage(Integer brandSeq, Integer pageNum, Integer pageSize) {
        Wrapper<GoodsSeasonEntity> wrapper = new EntityWrapper<GoodsSeasonEntity>();
        wrapper.eq("Brand_Seq", brandSeq);
        Page<GoodsSeasonEntity> page = new Page<GoodsSeasonEntity>(pageNum, pageSize);
        return this.selectPage(page, wrapper);
    }

    /**
     * 判断该波次是否有鞋子在使用
     */
    @Override
    public Boolean hasShoesInSeason(Integer seq) {
        Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<GoodsShoesEntity>();
        wrapper.eq("Season_Seq", seq);
        List<GoodsShoesEntity> list = goodsShoesDao.selectList(wrapper);
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteGoodsSeason(Integer seq) {
        goodsSeasonDao.deleteById(seq);
    }

    @Override
    public GoodsSeasonEntity edit(Integer seq) {
        GoodsSeasonEntity goodsSeasonEntity = new GoodsSeasonEntity();
        goodsSeasonEntity.setSeq(seq);
        return goodsSeasonDao.selectById(goodsSeasonEntity);
    }

    @Override
    public void addGoodsSeason(Integer brandSeq, GoodsSeasonEntity goodsSeasonEntity) {
        goodsSeasonEntity.setBrandSeq(brandSeq);
        goodsSeasonDao.insert(goodsSeasonEntity);
    }

    @Override
    public void updateGoodsSeason(GoodsSeasonEntity goodsSeasonEntity) {
        goodsSeasonDao.updateById(goodsSeasonEntity);
    }

    
    /**
     * app接口查询所有季节List
     */
	@Override
	public List<GoodsSeasonEntity> getAllSeasonList(Integer brandSeq) {
        Wrapper<GoodsSeasonEntity> wrapper = new EntityWrapper<GoodsSeasonEntity>();
        wrapper.eq("Brand_Seq", brandSeq);
        return this.selectList(wrapper);
	}

	@Override
	public GoodsSeasonEntity getSeasonByName(String name,Integer brandSeq) {
		 Wrapper<GoodsSeasonEntity> wrapper = new EntityWrapper<GoodsSeasonEntity>();
		  wrapper.eq("Brand_Seq", brandSeq);
		  wrapper.eq("SeasonName", name);
		return this.selectOne(wrapper);
	}

}
