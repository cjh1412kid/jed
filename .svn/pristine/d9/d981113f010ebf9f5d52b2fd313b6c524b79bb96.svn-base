package io.nuite.modules.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.nuite.modules.system.entity.ShoesReplenishDetailEntity;

/**
 * ${comments}
 * 
 * @author yangchuang
 * @email ychuang92@163.com
 * @date 2019-04-29 10:02:01
 */
@Mapper
public interface ShoesReplenishDetailDao extends BaseMapper<ShoesReplenishDetailEntity> {
	List<ShoesReplenishDetailEntity> getDetailByReplenishSeq(@Param("replenishSeq")Integer replenishSeq);
}
