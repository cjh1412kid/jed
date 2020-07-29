package io.nuite.modules.system.dao.order_platform;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import io.nuite.modules.system.entity.order_platform.AllocateTransferPreviewEntity;

@Mapper
public interface AllocateTransferPreviewDao extends BaseMapper<AllocateTransferPreviewEntity>{

//	List<Map<String, Object>> getDate(@Param("date")String date);
//	
//	List<Map<String, Object>> getShoes(@Param("date")String date);
//	
//	List<Map<String, Object>> getTime(@Param("date")String date,@Param("shoesSeq")Integer shoesSeq);
//	
//	List<Map<String, Object>> getShops(@Param("date")String date,@Param("shoesSeq")Integer shoesSeq,@Param("time")String time,Page page);
//	
//	List<Map<String, Object>> getAllRecords(@Param("date")String date,@Param("shoesSeq")Integer shoesSeq,@Param("time")String time,Page page);
//	
//	void deleteRecords(@Param("date")String date,@Param("shoesSeq")Integer shoesSeq,@Param("time")String time);
//
//	List<Map<String, Object>>  getAllShoes(@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("goodId")String goodId,Page page);
//
//	List<Map<String, Object>> getMsgByParams(@Param("date")String date,@Param("shoesSeq")Integer shoesSeq,@Param("time")String time);
//
//	List<Map<String, Object>> getRecords(@Param("date")String date,@Param("shoesSeq")Integer shoesSeq,@Param("time")String time,@Param("inShopSeq")Integer inShopSeq,@Param("outShopSeq")Integer outShopSeq);
}
