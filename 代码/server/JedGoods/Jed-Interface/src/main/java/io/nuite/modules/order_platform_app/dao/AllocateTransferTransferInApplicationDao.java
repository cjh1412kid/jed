package io.nuite.modules.order_platform_app.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationEntity;

/**
 * 调入申请表
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-09-12 08:52:00
 */
@Mapper
public interface AllocateTransferTransferInApplicationDao extends BaseMapper<AllocateTransferTransferInApplicationEntity> {
	AllocateTransferTransferInApplicationEntity getallocateTransferTransferInApplication(@Param("inShopSeq")Integer inShopSeq,@Param("outShopSeq")Integer outShopSeq,@Param("shoesSeq")Integer shoesSeq,@Param("inputTime")String inputTime);

	List<AllocateTransferTransferInApplicationEntity> getList(@Param("inputTime")String inputTime,@Param("shoesSeq")Integer shoesSeq);
}
