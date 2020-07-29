package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.ShoppingCartEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:57
 */
@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCartEntity> {

	List<Map<String, Object>> getShoppingCartListByUserSeq(Integer userSeq);

    List<ShoppingCartEntity> getShoppingCartListByDateAndShoesDataSeq(@Param("startTime") Date startTime, @Param("endTime")Date endTime, @Param("shoesDataSeqs")List<Integer> shoesDataSeqs);
	
}
