package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.GoodsViewDao;
import io.nuite.modules.sr_base.entity.GoodsViewEntity;
import io.nuite.modules.sr_base.service.GoodsViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GoodsViewServiceImpl extends ServiceImpl<GoodsViewDao, GoodsViewEntity> implements GoodsViewService {
	
    @Autowired
    private GoodsViewDao goodsViewDao;
    
    
	/**
	 * 根据seq获取GoodsView对象
	 */
	@Override
	public GoodsViewEntity getGoodsViewBySeq(Integer shoesSeq) {
		return goodsViewDao.selectById(shoesSeq);
	}
	
	
	/**
	 * 根据seq获取GoodsView的Map对象
	 */
	@Override
	public Map<String, Object> getGoodsViewMapBySeq(Integer shoesSeq) {
		Wrapper<GoodsViewEntity> wrapper = new EntityWrapper<GoodsViewEntity>();
		wrapper.where("Seq = {0} ", shoesSeq);
		return goodsViewDao.selectMaps(wrapper).get(0);
	}

	@Override
	public GoodsViewEntity getGoodsViewByGoodId(String goodId) throws Exception {
		Wrapper<GoodsViewEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("GoodID",goodId);
		GoodsViewEntity goodsViewEntity = new GoodsViewEntity();
		List<GoodsViewEntity> goodsViewEntities = goodsViewDao.selectList(wrapper);
		if(goodsViewEntities.size() > 0) {
			goodsViewEntity = goodsViewEntities.get(0);
		}
		return goodsViewEntity;
	}
}
