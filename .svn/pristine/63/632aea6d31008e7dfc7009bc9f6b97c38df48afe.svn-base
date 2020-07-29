package io.nuite.modules.order_platform_app.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import io.nuite.modules.order_platform_app.dao.ShoesDataDao;
import io.nuite.modules.order_platform_app.dao.ShoppingCartDao;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import io.nuite.modules.order_platform_app.entity.ShoppingCartEntity;
import io.nuite.modules.order_platform_app.service.ShoppingCartService;
import io.nuite.modules.sr_base.dao.GoodsColorDao;
import io.nuite.modules.sr_base.dao.GoodsSizeDao;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    
    @Autowired
    private ShoppingCartDao shoppingCartDao;
    
    @Autowired
    private ShoesDataDao shoesDataDao;
    
    @Autowired
    private GoodsColorDao goodsColorDao;

    @Autowired
    private GoodsSizeDao goodsSizeDao;


    /**
     * 根据鞋子序号获取鞋子颜色seq列表
     */
	@Override
	public List<Object> getColorSeqListByShoesSeq(Integer shoesSeq) {
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
		wrapper.setSqlSelect("Color_Seq").where("Shoes_Seq = {0} ", shoesSeq).groupBy("Color_Seq").orderBy("Color_Seq ASC");
		return shoesDataDao.selectObjs(wrapper);
	}
	
	
	/**
	 * 根据seq获取颜色
	 */
	@Override
	public GoodsColorEntity getColorBySeq(Integer colorSeq) {
		return goodsColorDao.selectById(colorSeq);
	}
	
	
	/**
	 * 根据seq获取尺码
	 */
	@Override
	public GoodsSizeEntity getSizeBySeq(Integer sizeSeq) {
		return goodsSizeDao.selectById(sizeSeq);
	}
	
	
	/**
	 * 根据 鞋子Seq + 颜色seq 获取尺码、库存信息
	 */
	@Override
	public List<Map<String, Object>> getSizeAndStockByShoesSeqAndColorSeq(Integer shoesSeq, Integer colorSeq) {
		Wrapper<ShoesDataEntity> wrapper = new EntityWrapper<ShoesDataEntity>();
		wrapper.setSqlSelect("Seq AS shoesDataSeq, Size_Seq AS sizeSeq, Stock AS stock, Remark AS remark")
		.where("Shoes_Seq = {0} AND Color_Seq = {1}", shoesSeq, colorSeq).orderBy("Size_Seq ASC");
		return shoesDataDao.selectMaps(wrapper);
	}
	
	
	
	/**
	 * 根据用户编号，鞋子数据编号，查询购物车实体
	 */
	@Override
	public ShoppingCartEntity getShoppingCartByUserSeqShoesDataSeq(Integer userSeq, Integer shoesDataSeq) {
		ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
		shoppingCartEntity.setUserSeq(userSeq);
		shoppingCartEntity.setShoesDataSeq(shoesDataSeq);
		shoppingCartEntity.setDel(0);
		return shoppingCartDao.selectOne(shoppingCartEntity);
	}
	
	
	/**
	 * 加入购物车
	 */
	@Override
	public void addToShoppingCart(ShoppingCartEntity shoppingCartEntity) {
		shoppingCartDao.insert(shoppingCartEntity);
	}


	/**
	 * 购物车列表
	 */
	@Override
	public List<Map<String, Object>> getShoppingCartListByUserSeq(Integer userSeq) {
		return shoppingCartDao.getShoppingCartListByUserSeq(userSeq);
	}


	/**
	 * 修改购物车
	 */
	@Override
	public void updateShoppingCart(ShoppingCartEntity shoppingCartEntity) {
		shoppingCartDao.updateById(shoppingCartEntity);
	}


	/**
	 * 购物车全选/全不选
	 */
	@Override
	public void checkAllShoppingCart(ShoppingCartEntity shoppingCartEntity, Integer userSeq) {
		Wrapper<ShoppingCartEntity> wrapper = new EntityWrapper<ShoppingCartEntity>();
		wrapper.where("User_Seq = {0} ", userSeq);
		shoppingCartDao.update(shoppingCartEntity, wrapper);
	}
	
	
	/**
	 * 删除购物车
	 */
	@Override
	public void deleteShoppingCart(List<Integer> seqList) {
		shoppingCartDao.deleteBatchIds(seqList);
	}



    
}
