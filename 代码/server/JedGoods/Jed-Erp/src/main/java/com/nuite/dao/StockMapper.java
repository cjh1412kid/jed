package com.nuite.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.nuite.entity.Stock;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {
    List<Map<String, Object>> queryChangeDetail(Map<String,Object> map);

    List<Map<String, Object>> queryStock(Map<String,Object> map);
}
