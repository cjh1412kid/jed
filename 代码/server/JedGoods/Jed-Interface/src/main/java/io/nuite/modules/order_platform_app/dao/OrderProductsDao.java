package io.nuite.modules.order_platform_app.dao;

import io.nuite.modules.order_platform_app.entity.OrderProductsEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:57
 */
@Mapper
public interface OrderProductsDao extends BaseMapper<OrderProductsEntity> {

    List<Integer> getShoeDataSeqsByOrderseqs(@Param("orderSeqs") List<Integer> orderSeqs);
}
