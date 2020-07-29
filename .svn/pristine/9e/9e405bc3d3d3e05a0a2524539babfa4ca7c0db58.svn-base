package io.nuite.modules.order_platform_app.service;

import java.util.List;
import java.util.Map;

import io.nuite.modules.order_platform_app.entity.ShoppingCartEntity;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;

public interface ShoppingCartService {

	List<Object> getColorSeqListByShoesSeq(Integer shoesSeq);
	
	GoodsColorEntity getColorBySeq(Integer colorSeq);
	
	GoodsSizeEntity getSizeBySeq(Integer sizeSeq);
	
	List<Map<String, Object>> getSizeAndStockByShoesSeqAndColorSeq(Integer shoesSeq, Integer colorSeq);
	
	ShoppingCartEntity getShoppingCartByUserSeqShoesDataSeq(Integer userSeq, Integer shoesDataSeq);

	void addToShoppingCart(ShoppingCartEntity shoppingCartEntity);

	List<Map<String, Object>> getShoppingCartListByUserSeq(Integer userSeq);

	void updateShoppingCart(ShoppingCartEntity shoppingCartEntity);

	void checkAllShoppingCart(ShoppingCartEntity shoppingCartEntity, Integer userSeq);
	
	void deleteShoppingCart(List<Integer> seqList);


}
